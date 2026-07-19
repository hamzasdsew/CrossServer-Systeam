/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public enum ListPosition
/*    */   implements Rawable {
/*  7 */   BEFORE, AFTER;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   ListPosition() {
/* 12 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 17 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\ListPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */