/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.mcf;
/*    */ 
/*    */ import io.github.resilience4j.circuitbreaker.CircuitBreaker;
/*    */ import io.github.resilience4j.decorators.Decorators;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.MultiClusterPooledConnectionProvider;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CircuitBreakerFailoverConnectionProvider
/*    */   extends CircuitBreakerFailoverBase
/*    */ {
/*    */   public CircuitBreakerFailoverConnectionProvider(MultiClusterPooledConnectionProvider provider) {
/* 19 */     super(provider);
/*    */   }
/*    */   
/*    */   public Connection getConnection() {
/* 23 */     MultiClusterPooledConnectionProvider.Cluster cluster = this.provider.getCluster();
/*    */     
/* 25 */     Decorators.DecorateSupplier<Connection> supplier = Decorators.ofSupplier(() -> handleGetConnection(cluster));
/*    */     
/* 27 */     supplier.withRetry(cluster.getRetry());
/* 28 */     supplier.withCircuitBreaker(cluster.getCircuitBreaker());
/* 29 */     supplier.withFallback(defaultCircuitBreakerFallbackException, e -> handleClusterFailover(cluster.getCircuitBreaker()));
/*    */ 
/*    */     
/* 32 */     return supplier.decorate().get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Connection handleGetConnection(MultiClusterPooledConnectionProvider.Cluster cluster) {
/* 39 */     Connection connection = cluster.getConnection();
/* 40 */     connection.ping();
/* 41 */     return connection;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Connection handleClusterFailover(CircuitBreaker circuitBreaker) {
/* 49 */     clusterFailover(circuitBreaker);
/*    */ 
/*    */     
/* 52 */     return getConnection();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\mcf\CircuitBreakerFailoverConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */