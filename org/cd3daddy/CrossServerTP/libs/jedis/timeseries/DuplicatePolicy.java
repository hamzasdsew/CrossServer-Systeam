/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DuplicatePolicy
/*    */   implements Rawable
/*    */ {
/* 14 */   BLOCK,
/*    */ 
/*    */ 
/*    */   
/* 18 */   FIRST,
/*    */ 
/*    */ 
/*    */   
/* 22 */   LAST,
/*    */ 
/*    */ 
/*    */   
/* 26 */   MIN,
/*    */ 
/*    */ 
/*    */   
/* 30 */   MAX,
/*    */ 
/*    */ 
/*    */   
/* 34 */   SUM;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   DuplicatePolicy() {
/* 39 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 44 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\DuplicatePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */