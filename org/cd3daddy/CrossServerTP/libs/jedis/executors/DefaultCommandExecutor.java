/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.executors;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*    */ 
/*    */ public class DefaultCommandExecutor
/*    */   implements CommandExecutor {
/*    */   protected final ConnectionProvider provider;
/*    */   
/*    */   public DefaultCommandExecutor(ConnectionProvider provider) {
/* 13 */     this.provider = provider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 18 */     IOUtils.closeQuietly((AutoCloseable)this.provider);
/*    */   }
/*    */ 
/*    */   
/*    */   public final <T> T executeCommand(CommandObject<T> commandObject) {
/* 23 */     try (Connection connection = this.provider.getConnection(commandObject.getArguments())) {
/* 24 */       return (T)connection.executeCommand(commandObject);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\executors\DefaultCommandExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */