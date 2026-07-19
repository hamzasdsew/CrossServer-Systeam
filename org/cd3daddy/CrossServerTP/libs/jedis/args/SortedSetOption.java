/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public enum SortedSetOption
/*    */   implements Rawable {
/*  7 */   MIN, MAX;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   SortedSetOption() {
/* 12 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 17 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\SortedSetOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */