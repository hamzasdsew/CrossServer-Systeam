/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum UnblockType
/*    */   implements Rawable
/*    */ {
/* 10 */   TIMEOUT, ERROR;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   UnblockType() {
/* 15 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 20 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\UnblockType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */