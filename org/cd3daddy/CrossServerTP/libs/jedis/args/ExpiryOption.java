/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ExpiryOption
/*    */   implements Rawable
/*    */ {
/* 13 */   NX,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   XX,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   GT,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 28 */   LT;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   ExpiryOption() {
/* 33 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 38 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\ExpiryOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */