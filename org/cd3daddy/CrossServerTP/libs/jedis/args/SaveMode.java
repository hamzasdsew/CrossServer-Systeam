/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SaveMode
/*    */   implements Rawable
/*    */ {
/* 10 */   NOSAVE,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   SAVE;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   SaveMode() {
/* 20 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 25 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\SaveMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */