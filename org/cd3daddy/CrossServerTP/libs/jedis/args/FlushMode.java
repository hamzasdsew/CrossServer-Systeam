/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum FlushMode
/*    */   implements Rawable
/*    */ {
/* 13 */   SYNC,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   ASYNC;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   FlushMode() {
/* 23 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 28 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\FlushMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */