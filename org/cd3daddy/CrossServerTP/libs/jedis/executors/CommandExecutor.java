/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.executors;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*    */ 
/*    */ public interface CommandExecutor
/*    */   extends AutoCloseable {
/*    */   <T> T executeCommand(CommandObject<T> paramCommandObject);
/*    */   
/*    */   default <T> T broadcastCommand(CommandObject<T> commandObject) {
/* 10 */     return executeCommand(commandObject);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\executors\CommandExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */