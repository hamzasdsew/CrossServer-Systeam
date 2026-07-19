/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.mcf;
/*    */ 
/*    */ import io.github.resilience4j.circuitbreaker.CircuitBreaker;
/*    */ import io.github.resilience4j.decorators.Decorators;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.executors.CommandExecutor;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.MultiClusterPooledConnectionProvider;
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
/*    */ public class CircuitBreakerCommandExecutor
/*    */   extends CircuitBreakerFailoverBase
/*    */   implements CommandExecutor
/*    */ {
/*    */   public CircuitBreakerCommandExecutor(MultiClusterPooledConnectionProvider provider) {
/* 24 */     super(provider);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T> T executeCommand(CommandObject<T> commandObject) {
/* 29 */     MultiClusterPooledConnectionProvider.Cluster cluster = this.provider.getCluster();
/*    */     
/* 31 */     Decorators.DecorateSupplier<T> supplier = Decorators.ofSupplier(() -> handleExecuteCommand(commandObject, cluster));
/*    */     
/* 33 */     supplier.withRetry(cluster.getRetry());
/* 34 */     supplier.withCircuitBreaker(cluster.getCircuitBreaker());
/* 35 */     supplier.withFallback(defaultCircuitBreakerFallbackException, e -> handleClusterFailover(commandObject, cluster.getCircuitBreaker()));
/*    */ 
/*    */     
/* 38 */     return supplier.decorate().get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <T> T handleExecuteCommand(CommandObject<T> commandObject, MultiClusterPooledConnectionProvider.Cluster cluster) {
/* 45 */     try (Connection connection = cluster.getConnection()) {
/* 46 */       return (T)connection.executeCommand(commandObject);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private <T> T handleClusterFailover(CommandObject<T> commandObject, CircuitBreaker circuitBreaker) {
/* 55 */     clusterFailover(circuitBreaker);
/*    */ 
/*    */     
/* 58 */     return executeCommand(commandObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\mcf\CircuitBreakerCommandExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */