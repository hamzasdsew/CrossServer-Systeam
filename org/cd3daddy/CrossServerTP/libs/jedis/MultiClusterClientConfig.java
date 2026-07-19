/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisValidationException;
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
/*     */ 
/*     */ 
/*     */ public final class MultiClusterClientConfig
/*     */ {
/*     */   private static final int RETRY_MAX_ATTEMPTS_DEFAULT = 3;
/*     */   private static final int RETRY_WAIT_DURATION_DEFAULT = 500;
/*     */   private static final int RETRY_WAIT_DURATION_EXPONENTIAL_BACKOFF_MULTIPLIER_DEFAULT = 2;
/*  30 */   private static final Class RETRY_INCLUDED_EXCEPTIONS_DEFAULT = JedisConnectionException.class;
/*     */   
/*     */   private static final float CIRCUIT_BREAKER_FAILURE_RATE_THRESHOLD_DEFAULT = 50.0F;
/*     */   private static final int CIRCUIT_BREAKER_SLIDING_WINDOW_MIN_CALLS_DEFAULT = 100;
/*  34 */   private static final CircuitBreakerConfig.SlidingWindowType CIRCUIT_BREAKER_SLIDING_WINDOW_TYPE_DEFAULT = CircuitBreakerConfig.SlidingWindowType.COUNT_BASED;
/*     */   private static final int CIRCUIT_BREAKER_SLIDING_WINDOW_SIZE_DEFAULT = 100;
/*     */   private static final int CIRCUIT_BREAKER_SLOW_CALL_DURATION_THRESHOLD_DEFAULT = 60000;
/*     */   private static final float CIRCUIT_BREAKER_SLOW_CALL_RATE_THRESHOLD_DEFAULT = 100.0F;
/*  38 */   private static final Class CIRCUIT_BREAKER_INCLUDED_EXCEPTIONS_DEFAULT = JedisConnectionException.class;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final ClusterConfig[] clusterConfigs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int retryMaxAttempts;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Duration retryWaitDuration;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int retryWaitDurationExponentialBackoffMultiplier;
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Class> retryIncludedExceptionList;
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Class> retryIgnoreExceptionList;
/*     */ 
/*     */ 
/*     */   
/*     */   private float circuitBreakerFailureRateThreshold;
/*     */ 
/*     */ 
/*     */   
/*     */   private int circuitBreakerSlidingWindowMinCalls;
/*     */ 
/*     */ 
/*     */   
/*     */   private CircuitBreakerConfig.SlidingWindowType circuitBreakerSlidingWindowType;
/*     */ 
/*     */ 
/*     */   
/*     */   private int circuitBreakerSlidingWindowSize;
/*     */ 
/*     */ 
/*     */   
/*     */   private Duration circuitBreakerSlowCallDurationThreshold;
/*     */ 
/*     */ 
/*     */   
/*     */   private float circuitBreakerSlowCallRateThreshold;
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Class> circuitBreakerIncludedExceptionList;
/*     */ 
/*     */ 
/*     */   
/*     */   private List<Class> circuitBreakerIgnoreExceptionList;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiClusterClientConfig(ClusterConfig[] clusterConfigs) {
/* 104 */     this.clusterConfigs = clusterConfigs;
/*     */   }
/*     */   
/*     */   public ClusterConfig[] getClusterConfigs() {
/* 108 */     return this.clusterConfigs;
/*     */   }
/*     */   
/*     */   public int getRetryMaxAttempts() {
/* 112 */     return this.retryMaxAttempts;
/*     */   }
/*     */   
/*     */   public Duration getRetryWaitDuration() {
/* 116 */     return this.retryWaitDuration;
/*     */   }
/*     */   
/*     */   public int getRetryWaitDurationExponentialBackoffMultiplier() {
/* 120 */     return this.retryWaitDurationExponentialBackoffMultiplier;
/*     */   }
/*     */   
/*     */   public float getCircuitBreakerFailureRateThreshold() {
/* 124 */     return this.circuitBreakerFailureRateThreshold;
/*     */   }
/*     */   
/*     */   public int getCircuitBreakerSlidingWindowMinCalls() {
/* 128 */     return this.circuitBreakerSlidingWindowMinCalls;
/*     */   }
/*     */   
/*     */   public int getCircuitBreakerSlidingWindowSize() {
/* 132 */     return this.circuitBreakerSlidingWindowSize;
/*     */   }
/*     */   
/*     */   public Duration getCircuitBreakerSlowCallDurationThreshold() {
/* 136 */     return this.circuitBreakerSlowCallDurationThreshold;
/*     */   }
/*     */   
/*     */   public float getCircuitBreakerSlowCallRateThreshold() {
/* 140 */     return this.circuitBreakerSlowCallRateThreshold;
/*     */   }
/*     */   
/*     */   public List<Class> getRetryIncludedExceptionList() {
/* 144 */     return this.retryIncludedExceptionList;
/*     */   }
/*     */   
/*     */   public List<Class> getRetryIgnoreExceptionList() {
/* 148 */     return this.retryIgnoreExceptionList;
/*     */   }
/*     */   
/*     */   public List<Class> getCircuitBreakerIncludedExceptionList() {
/* 152 */     return this.circuitBreakerIncludedExceptionList;
/*     */   }
/*     */   
/*     */   public List<Class> getCircuitBreakerIgnoreExceptionList() {
/* 156 */     return this.circuitBreakerIgnoreExceptionList;
/*     */   }
/*     */   
/*     */   public CircuitBreakerConfig.SlidingWindowType getCircuitBreakerSlidingWindowType() {
/* 160 */     return this.circuitBreakerSlidingWindowType;
/*     */   }
/*     */   
/*     */   public static class ClusterConfig
/*     */   {
/*     */     private int priority;
/*     */     private HostAndPort hostAndPort;
/*     */     private JedisClientConfig clientConfig;
/*     */     
/*     */     public ClusterConfig(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/* 170 */       this.hostAndPort = hostAndPort;
/* 171 */       this.clientConfig = clientConfig;
/*     */     }
/*     */     
/*     */     public int getPriority() {
/* 175 */       return this.priority;
/*     */     }
/*     */     
/*     */     private void setPriority(int priority) {
/* 179 */       this.priority = priority;
/*     */     }
/*     */     
/*     */     public HostAndPort getHostAndPort() {
/* 183 */       return this.hostAndPort;
/*     */     }
/*     */     
/*     */     public JedisClientConfig getJedisClientConfig() {
/* 187 */       return this.clientConfig;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private MultiClusterClientConfig.ClusterConfig[] clusterConfigs;
/* 195 */     private int retryMaxAttempts = 3;
/* 196 */     private int retryWaitDuration = 500;
/* 197 */     private int retryWaitDurationExponentialBackoffMultiplier = 2;
/*     */     
/*     */     private List<Class> retryIncludedExceptionList;
/*     */     private List<Class> retryIgnoreExceptionList;
/* 201 */     private float circuitBreakerFailureRateThreshold = 50.0F;
/* 202 */     private int circuitBreakerSlidingWindowMinCalls = 100;
/* 203 */     private CircuitBreakerConfig.SlidingWindowType circuitBreakerSlidingWindowType = MultiClusterClientConfig.CIRCUIT_BREAKER_SLIDING_WINDOW_TYPE_DEFAULT;
/* 204 */     private int circuitBreakerSlidingWindowSize = 100;
/* 205 */     private int circuitBreakerSlowCallDurationThreshold = 60000;
/* 206 */     private float circuitBreakerSlowCallRateThreshold = 100.0F;
/*     */     
/*     */     private List<Class> circuitBreakerIncludedExceptionList;
/*     */     private List<Class> circuitBreakerIgnoreExceptionList;
/*     */     private List<Class<? extends Throwable>> circuitBreakerFallbackExceptionList;
/*     */     
/*     */     public Builder(MultiClusterClientConfig.ClusterConfig[] clusterConfigs) {
/* 213 */       if (clusterConfigs == null || clusterConfigs.length < 1) {
/* 214 */         throw new JedisValidationException("ClusterClientConfigs are required for MultiClusterPooledConnectionProvider");
/*     */       }
/* 216 */       for (int i = 0; i < clusterConfigs.length; i++) {
/* 217 */         clusterConfigs[i].setPriority(i + 1);
/*     */       }
/* 219 */       this.clusterConfigs = clusterConfigs;
/*     */     }
/*     */     
/*     */     public Builder retryMaxAttempts(int retryMaxAttempts) {
/* 223 */       this.retryMaxAttempts = retryMaxAttempts;
/* 224 */       return this;
/*     */     }
/*     */     
/*     */     public Builder retryWaitDuration(int retryWaitDuration) {
/* 228 */       this.retryWaitDuration = retryWaitDuration;
/* 229 */       return this;
/*     */     }
/*     */     
/*     */     public Builder retryWaitDurationExponentialBackoffMultiplier(int retryWaitDurationExponentialBackoffMultiplier) {
/* 233 */       this.retryWaitDurationExponentialBackoffMultiplier = retryWaitDurationExponentialBackoffMultiplier;
/* 234 */       return this;
/*     */     }
/*     */     
/*     */     public Builder retryIncludedExceptionList(List<Class<?>> retryIncludedExceptionList) {
/* 238 */       this.retryIncludedExceptionList = retryIncludedExceptionList;
/* 239 */       return this;
/*     */     }
/*     */     
/*     */     public Builder retryIgnoreExceptionList(List<Class<?>> retryIgnoreExceptionList) {
/* 243 */       this.retryIgnoreExceptionList = retryIgnoreExceptionList;
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerFailureRateThreshold(float circuitBreakerFailureRateThreshold) {
/* 248 */       this.circuitBreakerFailureRateThreshold = circuitBreakerFailureRateThreshold;
/* 249 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerSlidingWindowMinCalls(int circuitBreakerSlidingWindowMinCalls) {
/* 253 */       this.circuitBreakerSlidingWindowMinCalls = circuitBreakerSlidingWindowMinCalls;
/* 254 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerSlidingWindowType(CircuitBreakerConfig.SlidingWindowType circuitBreakerSlidingWindowType) {
/* 258 */       this.circuitBreakerSlidingWindowType = circuitBreakerSlidingWindowType;
/* 259 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerSlidingWindowSize(int circuitBreakerSlidingWindowSize) {
/* 263 */       this.circuitBreakerSlidingWindowSize = circuitBreakerSlidingWindowSize;
/* 264 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerSlowCallDurationThreshold(int circuitBreakerSlowCallDurationThreshold) {
/* 268 */       this.circuitBreakerSlowCallDurationThreshold = circuitBreakerSlowCallDurationThreshold;
/* 269 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerSlowCallRateThreshold(float circuitBreakerSlowCallRateThreshold) {
/* 273 */       this.circuitBreakerSlowCallRateThreshold = circuitBreakerSlowCallRateThreshold;
/* 274 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerIncludedExceptionList(List<Class<?>> circuitBreakerIncludedExceptionList) {
/* 278 */       this.circuitBreakerIncludedExceptionList = circuitBreakerIncludedExceptionList;
/* 279 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerIgnoreExceptionList(List<Class<?>> circuitBreakerIgnoreExceptionList) {
/* 283 */       this.circuitBreakerIgnoreExceptionList = circuitBreakerIgnoreExceptionList;
/* 284 */       return this;
/*     */     }
/*     */     
/*     */     public Builder circuitBreakerFallbackExceptionList(List<Class<? extends Throwable>> circuitBreakerFallbackExceptionList) {
/* 288 */       this.circuitBreakerFallbackExceptionList = circuitBreakerFallbackExceptionList;
/* 289 */       return this;
/*     */     }
/*     */     
/*     */     public MultiClusterClientConfig build() {
/* 293 */       MultiClusterClientConfig config = new MultiClusterClientConfig(this.clusterConfigs);
/*     */       
/* 295 */       config.retryMaxAttempts = this.retryMaxAttempts;
/* 296 */       config.retryWaitDuration = Duration.ofMillis(this.retryWaitDuration);
/* 297 */       config.retryWaitDurationExponentialBackoffMultiplier = this.retryWaitDurationExponentialBackoffMultiplier;
/*     */       
/* 299 */       if (this.retryIncludedExceptionList != null && !this.retryIncludedExceptionList.isEmpty()) {
/* 300 */         config.retryIncludedExceptionList = this.retryIncludedExceptionList;
/*     */       } else {
/*     */         
/* 303 */         config.retryIncludedExceptionList = new ArrayList();
/* 304 */         config.retryIncludedExceptionList.add(MultiClusterClientConfig.RETRY_INCLUDED_EXCEPTIONS_DEFAULT);
/*     */       } 
/*     */       
/* 307 */       if (this.retryIgnoreExceptionList != null && !this.retryIgnoreExceptionList.isEmpty()) {
/* 308 */         config.retryIgnoreExceptionList = this.retryIgnoreExceptionList;
/*     */       }
/* 310 */       config.circuitBreakerFailureRateThreshold = this.circuitBreakerFailureRateThreshold;
/* 311 */       config.circuitBreakerSlidingWindowMinCalls = this.circuitBreakerSlidingWindowMinCalls;
/* 312 */       config.circuitBreakerSlidingWindowType = this.circuitBreakerSlidingWindowType;
/* 313 */       config.circuitBreakerSlidingWindowSize = this.circuitBreakerSlidingWindowSize;
/* 314 */       config.circuitBreakerSlowCallDurationThreshold = Duration.ofMillis(this.circuitBreakerSlowCallDurationThreshold);
/* 315 */       config.circuitBreakerSlowCallRateThreshold = this.circuitBreakerSlowCallRateThreshold;
/*     */       
/* 317 */       if (this.circuitBreakerIncludedExceptionList != null && !this.circuitBreakerIncludedExceptionList.isEmpty()) {
/* 318 */         config.circuitBreakerIncludedExceptionList = this.circuitBreakerIncludedExceptionList;
/*     */       } else {
/*     */         
/* 321 */         config.circuitBreakerIncludedExceptionList = new ArrayList();
/* 322 */         config.circuitBreakerIncludedExceptionList.add(MultiClusterClientConfig.CIRCUIT_BREAKER_INCLUDED_EXCEPTIONS_DEFAULT);
/*     */       } 
/*     */       
/* 325 */       if (this.circuitBreakerIgnoreExceptionList != null && !this.circuitBreakerIgnoreExceptionList.isEmpty()) {
/* 326 */         config.circuitBreakerIgnoreExceptionList = this.circuitBreakerIgnoreExceptionList;
/*     */       }
/* 328 */       return config;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\MultiClusterClientConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */