/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.io.Closeable;
/*    */ import java.util.List;
/*    */ 
/*    */ public abstract class AbstractTransaction
/*    */   extends PipeliningBase implements Closeable {
/*    */   protected AbstractTransaction() {
/*  9 */     super(new CommandObjects());
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract void multi();
/*    */ 
/*    */   
/*    */   public abstract String watch(String... paramVarArgs);
/*    */ 
/*    */   
/*    */   public abstract String watch(byte[]... paramVarArgs);
/*    */ 
/*    */   
/*    */   public abstract String unwatch();
/*    */ 
/*    */   
/*    */   public abstract void close();
/*    */ 
/*    */   
/*    */   public abstract List<Object> exec();
/*    */   
/*    */   public abstract String discard();
/*    */   
/*    */   public Response<Long> waitReplicas(int replicas, long timeout) {
/* 33 */     return appendCommand(this.commandObjects.waitReplicas(replicas, timeout));
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\AbstractTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */