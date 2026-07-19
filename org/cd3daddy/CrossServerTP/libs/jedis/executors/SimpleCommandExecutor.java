/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.executors;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*    */ 
/*    */ public class SimpleCommandExecutor
/*    */   implements CommandExecutor {
/*    */   protected final Connection connection;
/*    */   
/*    */   public SimpleCommandExecutor(Connection connection) {
/* 12 */     this.connection = connection;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 17 */     IOUtils.closeQuietly((AutoCloseable)this.connection);
/*    */   }
/*    */ 
/*    */   
/*    */   public final <T> T executeCommand(CommandObject<T> commandObject) {
/* 22 */     return (T)this.connection.executeCommand(commandObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\executors\SimpleCommandExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */