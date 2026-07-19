/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.mcf;
/*    */ 
/*    */ import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
/*    */ import io.github.resilience4j.circuitbreaker.CircuitBreaker;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.MultiClusterPooledConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CircuitBreakerFailoverBase
/*    */   implements AutoCloseable
/*    */ {
/* 25 */   protected static final List<Class<? extends Throwable>> defaultCircuitBreakerFallbackException = Arrays.asList((Class<? extends Throwable>[])new Class[] { CallNotPermittedException.class });
/*    */   
/*    */   protected final MultiClusterPooledConnectionProvider provider;
/*    */   
/*    */   public CircuitBreakerFailoverBase(MultiClusterPooledConnectionProvider provider) {
/* 30 */     this.provider = provider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 35 */     IOUtils.closeQuietly((AutoCloseable)this.provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected synchronized void clusterFailover(CircuitBreaker circuitBreaker) {
/* 44 */     if (!CircuitBreaker.State.FORCED_OPEN.equals(circuitBreaker.getState())) {
/*    */ 
/*    */ 
/*    */       
/* 48 */       circuitBreaker.transitionToForcedOpenState();
/*    */ 
/*    */ 
/*    */       
/* 52 */       int activeMultiClusterIndex = this.provider.incrementActiveMultiClusterIndex();
/*    */ 
/*    */       
/* 55 */       this.provider.runClusterFailoverPostProcessor(Integer.valueOf(activeMultiClusterIndex));
/*    */ 
/*    */     
/*    */     }
/* 59 */     else if (this.provider.isLastClusterCircuitBreakerForcedOpen()) {
/* 60 */       throw new JedisConnectionException("Cluster/database endpoint could not failover since the MultiClusterClientConfig was not provided with an additional cluster/database endpoint according to its prioritized sequence. If applicable, consider failing back OR restarting with an available cluster/database endpoint");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\mcf\CircuitBreakerFailoverBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */