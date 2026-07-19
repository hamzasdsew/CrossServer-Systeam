/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.providers;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ 
/*    */ public class ManagedConnectionProvider
/*    */   implements ConnectionProvider {
/*    */   private Connection connection;
/*    */   
/*    */   public final void setConnection(Connection connection) {
/* 11 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void close() {}
/*    */ 
/*    */   
/*    */   public final Connection getConnection() {
/* 20 */     return this.connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public final Connection getConnection(CommandArguments args) {
/* 25 */     return this.connection;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\providers\ManagedConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */