/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ 
/*    */ public abstract class AbstractPipeline
/*    */   extends PipeliningBase implements Closeable {
/*    */   protected AbstractPipeline(CommandObjects commandObjects) {
/*  8 */     super(commandObjects);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public abstract void close();
/*    */ 
/*    */   
/*    */   public abstract void sync();
/*    */ 
/*    */   
/*    */   public Response<Long> publish(String channel, String message) {
/* 20 */     return appendCommand(this.commandObjects.publish(channel, message));
/*    */   }
/*    */   
/*    */   public Response<Long> publish(byte[] channel, byte[] message) {
/* 24 */     return appendCommand(this.commandObjects.publish(channel, message));
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\AbstractPipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */