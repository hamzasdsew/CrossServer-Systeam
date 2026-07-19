/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.providers;
/*     */ 
/*     */ import io.github.resilience4j.circuitbreaker.CircuitBreaker;
/*     */ import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
/*     */ import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
/*     */ import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnCallNotPermittedEvent;
/*     */ import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnErrorEvent;
/*     */ import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnFailureRateExceededEvent;
/*     */ import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnSlowCallRateExceededEvent;
/*     */ import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnStateTransitionEvent;
/*     */ import io.github.resilience4j.core.IntervalFunction;
/*     */ import io.github.resilience4j.retry.Retry;
/*     */ import io.github.resilience4j.retry.RetryConfig;
/*     */ import io.github.resilience4j.retry.RetryRegistry;
/*     */ import io.github.resilience4j.retry.event.RetryOnErrorEvent;
/*     */ import io.github.resilience4j.retry.event.RetryOnRetryEvent;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Consumer;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.ConnectionPool;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.MultiClusterClientConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisValidationException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Pool;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public class MultiClusterPooledConnectionProvider
/*     */   implements ConnectionProvider
/*     */ {
/*  36 */   private final Logger log = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private final Map<Integer, Cluster> multiClusterMap = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private volatile Integer activeMultiClusterIndex = Integer.valueOf(1);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile boolean lastClusterCircuitBreakerForcedOpen = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Consumer<String> clusterFailoverPostProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiClusterPooledConnectionProvider(MultiClusterClientConfig multiClusterClientConfig) {
/*  68 */     if (multiClusterClientConfig == null) {
/*  69 */       throw new JedisValidationException("MultiClusterClientConfig must not be NULL for MultiClusterPooledConnectionProvider");
/*     */     }
/*     */ 
/*     */     
/*  73 */     RetryConfig.Builder retryConfigBuilder = RetryConfig.custom();
/*  74 */     retryConfigBuilder.maxAttempts(multiClusterClientConfig.getRetryMaxAttempts());
/*  75 */     retryConfigBuilder.intervalFunction(IntervalFunction.ofExponentialBackoff(multiClusterClientConfig.getRetryWaitDuration(), multiClusterClientConfig
/*  76 */           .getRetryWaitDurationExponentialBackoffMultiplier()));
/*  77 */     retryConfigBuilder.failAfterMaxAttempts(false);
/*  78 */     retryConfigBuilder.retryExceptions((Class[])multiClusterClientConfig.getRetryIncludedExceptionList().stream().toArray(x$0 -> new Class[x$0]));
/*     */     
/*  80 */     List<Class<?>> retryIgnoreExceptionList = multiClusterClientConfig.getRetryIgnoreExceptionList();
/*  81 */     if (retryIgnoreExceptionList != null && !retryIgnoreExceptionList.isEmpty()) {
/*  82 */       retryConfigBuilder.ignoreExceptions((Class[])retryIgnoreExceptionList.stream().toArray(x$0 -> new Class[x$0]));
/*     */     }
/*  84 */     RetryConfig retryConfig = retryConfigBuilder.build();
/*     */ 
/*     */ 
/*     */     
/*  88 */     CircuitBreakerConfig.Builder circuitBreakerConfigBuilder = CircuitBreakerConfig.custom();
/*  89 */     circuitBreakerConfigBuilder.failureRateThreshold(multiClusterClientConfig.getCircuitBreakerFailureRateThreshold());
/*  90 */     circuitBreakerConfigBuilder.slowCallRateThreshold(multiClusterClientConfig.getCircuitBreakerSlowCallRateThreshold());
/*  91 */     circuitBreakerConfigBuilder.slowCallDurationThreshold(multiClusterClientConfig.getCircuitBreakerSlowCallDurationThreshold());
/*  92 */     circuitBreakerConfigBuilder.minimumNumberOfCalls(multiClusterClientConfig.getCircuitBreakerSlidingWindowMinCalls());
/*  93 */     circuitBreakerConfigBuilder.slidingWindowType(multiClusterClientConfig.getCircuitBreakerSlidingWindowType());
/*  94 */     circuitBreakerConfigBuilder.slidingWindowSize(multiClusterClientConfig.getCircuitBreakerSlidingWindowSize());
/*  95 */     circuitBreakerConfigBuilder.recordExceptions((Class[])multiClusterClientConfig.getCircuitBreakerIncludedExceptionList().stream().toArray(x$0 -> new Class[x$0]));
/*  96 */     circuitBreakerConfigBuilder.automaticTransitionFromOpenToHalfOpenEnabled(false);
/*     */     
/*  98 */     List<Class<?>> circuitBreakerIgnoreExceptionList = multiClusterClientConfig.getCircuitBreakerIgnoreExceptionList();
/*  99 */     if (circuitBreakerIgnoreExceptionList != null && !circuitBreakerIgnoreExceptionList.isEmpty()) {
/* 100 */       circuitBreakerConfigBuilder.ignoreExceptions((Class[])circuitBreakerIgnoreExceptionList.stream().toArray(x$0 -> new Class[x$0]));
/*     */     }
/* 102 */     CircuitBreakerConfig circuitBreakerConfig = circuitBreakerConfigBuilder.build();
/*     */ 
/*     */ 
/*     */     
/* 106 */     MultiClusterClientConfig.ClusterConfig[] clusterConfigs = multiClusterClientConfig.getClusterConfigs();
/* 107 */     for (MultiClusterClientConfig.ClusterConfig config : clusterConfigs) {
/*     */       
/* 109 */       String clusterId = "cluster:" + config.getPriority() + ":" + config.getHostAndPort();
/*     */       
/* 111 */       Retry retry = RetryRegistry.of(retryConfig).retry(clusterId);
/*     */       
/* 113 */       Retry.EventPublisher retryPublisher = retry.getEventPublisher();
/* 114 */       retryPublisher.onRetry(event -> this.log.warn(String.valueOf(event)));
/* 115 */       retryPublisher.onError(event -> this.log.error(String.valueOf(event)));
/*     */       
/* 117 */       CircuitBreaker circuitBreaker = CircuitBreakerRegistry.of(circuitBreakerConfig).circuitBreaker(clusterId);
/*     */       
/* 119 */       CircuitBreaker.EventPublisher circuitBreakerEventPublisher = circuitBreaker.getEventPublisher();
/* 120 */       circuitBreakerEventPublisher.onCallNotPermitted(event -> this.log.error(String.valueOf(event)));
/* 121 */       circuitBreakerEventPublisher.onError(event -> this.log.error(String.valueOf(event)));
/* 122 */       circuitBreakerEventPublisher.onFailureRateExceeded(event -> this.log.error(String.valueOf(event)));
/* 123 */       circuitBreakerEventPublisher.onSlowCallRateExceeded(event -> this.log.error(String.valueOf(event)));
/* 124 */       circuitBreakerEventPublisher.onStateTransition(event -> this.log.warn(String.valueOf(event)));
/*     */       
/* 126 */       this.multiClusterMap.put(Integer.valueOf(config.getPriority()), new Cluster(new ConnectionPool(config.getHostAndPort(), config
/* 127 */               .getJedisClientConfig()), retry, circuitBreaker));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int incrementActiveMultiClusterIndex() {
/* 145 */     synchronized (this.activeMultiClusterIndex) {
/*     */       
/* 147 */       String originalClusterName = getClusterCircuitBreaker().getName();
/*     */ 
/*     */       
/* 150 */       if (this.activeMultiClusterIndex.intValue() + 1 > this.multiClusterMap.size()) {
/*     */         
/* 152 */         this.lastClusterCircuitBreakerForcedOpen = true;
/*     */         
/* 154 */         throw new JedisConnectionException("Cluster/database endpoint could not failover since the MultiClusterClientConfig was not provided with an additional cluster/database endpoint according to its prioritized sequence. If applicable, consider failing back OR restarting with an available cluster/database endpoint");
/*     */       } 
/*     */ 
/*     */       
/* 158 */       Integer integer1 = this.activeMultiClusterIndex, integer2 = this.activeMultiClusterIndex = Integer.valueOf(this.activeMultiClusterIndex.intValue() + 1);
/*     */       
/* 160 */       CircuitBreaker circuitBreaker = getClusterCircuitBreaker();
/*     */ 
/*     */ 
/*     */       
/* 164 */       if (CircuitBreaker.State.FORCED_OPEN.equals(circuitBreaker.getState())) {
/* 165 */         incrementActiveMultiClusterIndex();
/*     */       } else {
/* 167 */         this.log.warn("Cluster/database endpoint successfully updated from '{}' to '{}'", originalClusterName, circuitBreaker.getName());
/*     */       } 
/*     */     } 
/* 170 */     return this.activeMultiClusterIndex.intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void validateTargetConnection(int multiClusterIndex) {
/* 182 */     CircuitBreaker circuitBreaker = getClusterCircuitBreaker(multiClusterIndex);
/*     */     
/* 184 */     CircuitBreaker.State originalState = circuitBreaker.getState();
/*     */ 
/*     */     
/*     */     try {
/* 188 */       circuitBreaker.transitionToClosedState();
/*     */       
/* 190 */       try (Connection targetConnection = getConnection(multiClusterIndex)) {
/* 191 */         targetConnection.ping();
/*     */       }
/*     */     
/* 194 */     } catch (Exception e) {
/*     */ 
/*     */       
/* 197 */       if (CircuitBreaker.State.FORCED_OPEN.equals(originalState)) {
/* 198 */         circuitBreaker.transitionToForcedOpenState();
/*     */       }
/* 200 */       throw new JedisValidationException(circuitBreaker.getName() + " failed to connect. Please check configuration and try again.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setActiveMultiClusterIndex(int multiClusterIndex) {
/* 216 */     synchronized (this.activeMultiClusterIndex) {
/*     */ 
/*     */       
/* 219 */       if (this.activeMultiClusterIndex.intValue() == multiClusterIndex && 
/* 220 */         !CircuitBreaker.State.FORCED_OPEN.equals(getClusterCircuitBreaker(multiClusterIndex).getState())) {
/*     */         return;
/*     */       }
/* 223 */       if (multiClusterIndex < 1 || multiClusterIndex > this.multiClusterMap.size()) {
/* 224 */         throw new JedisValidationException("MultiClusterIndex: " + multiClusterIndex + " is not within the configured range. Please choose an index between 1 and " + this.multiClusterMap
/* 225 */             .size());
/*     */       }
/* 227 */       validateTargetConnection(multiClusterIndex);
/*     */       
/* 229 */       String originalClusterName = getClusterCircuitBreaker().getName();
/*     */       
/* 231 */       if (this.activeMultiClusterIndex.intValue() == multiClusterIndex) {
/* 232 */         this.log.warn("Cluster/database endpoint '{}' successfully closed its circuit breaker", originalClusterName);
/*     */       } else {
/* 234 */         this.log.warn("Cluster/database endpoint successfully updated from '{}' to '{}'", originalClusterName, 
/* 235 */             getClusterCircuitBreaker(multiClusterIndex).getName());
/*     */       } 
/* 237 */       this.activeMultiClusterIndex = Integer.valueOf(multiClusterIndex);
/* 238 */       this.lastClusterCircuitBreakerForcedOpen = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 244 */     ((Cluster)this.multiClusterMap.get(this.activeMultiClusterIndex)).getConnectionPool().close();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/* 249 */     return ((Cluster)this.multiClusterMap.get(this.activeMultiClusterIndex)).getConnection();
/*     */   }
/*     */   
/*     */   public Connection getConnection(int multiClusterIndex) {
/* 253 */     return ((Cluster)this.multiClusterMap.get(Integer.valueOf(multiClusterIndex))).getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection(CommandArguments args) {
/* 258 */     return ((Cluster)this.multiClusterMap.get(this.activeMultiClusterIndex)).getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<?, Pool<Connection>> getConnectionMap() {
/* 263 */     ConnectionPool connectionPool = ((Cluster)this.multiClusterMap.get(this.activeMultiClusterIndex)).getConnectionPool();
/* 264 */     return (Map)Collections.singletonMap(connectionPool.getFactory(), connectionPool);
/*     */   }
/*     */   
/*     */   public Cluster getCluster() {
/* 268 */     return this.multiClusterMap.get(this.activeMultiClusterIndex);
/*     */   }
/*     */   
/*     */   public CircuitBreaker getClusterCircuitBreaker() {
/* 272 */     return ((Cluster)this.multiClusterMap.get(this.activeMultiClusterIndex)).getCircuitBreaker();
/*     */   }
/*     */   
/*     */   public CircuitBreaker getClusterCircuitBreaker(int multiClusterIndex) {
/* 276 */     return ((Cluster)this.multiClusterMap.get(Integer.valueOf(multiClusterIndex))).getCircuitBreaker();
/*     */   }
/*     */   
/*     */   public boolean isLastClusterCircuitBreakerForcedOpen() {
/* 280 */     return this.lastClusterCircuitBreakerForcedOpen;
/*     */   }
/*     */   
/*     */   public void runClusterFailoverPostProcessor(Integer multiClusterIndex) {
/* 284 */     if (this.clusterFailoverPostProcessor != null)
/* 285 */       this.clusterFailoverPostProcessor.accept(getClusterCircuitBreaker(multiClusterIndex.intValue()).getName()); 
/*     */   }
/*     */   
/*     */   public void setClusterFailoverPostProcessor(Consumer<String> clusterFailoverPostProcessor) {
/* 289 */     this.clusterFailoverPostProcessor = clusterFailoverPostProcessor;
/*     */   }
/*     */   
/*     */   public static class Cluster
/*     */   {
/*     */     private final ConnectionPool connectionPool;
/*     */     private final Retry retry;
/*     */     private final CircuitBreaker circuitBreaker;
/*     */     
/*     */     public Cluster(ConnectionPool connectionPool, Retry retry, CircuitBreaker circuitBreaker) {
/* 299 */       this.connectionPool = connectionPool;
/* 300 */       this.retry = retry;
/* 301 */       this.circuitBreaker = circuitBreaker;
/*     */     }
/*     */     
/*     */     public Connection getConnection() {
/* 305 */       return this.connectionPool.getResource();
/*     */     }
/*     */     
/*     */     public ConnectionPool getConnectionPool() {
/* 309 */       return this.connectionPool;
/*     */     }
/*     */     
/*     */     public Retry getRetry() {
/* 313 */       return this.retry;
/*     */     }
/*     */     
/*     */     public CircuitBreaker getCircuitBreaker() {
/* 317 */       return this.circuitBreaker;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\providers\MultiClusterPooledConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */