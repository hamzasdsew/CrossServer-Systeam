/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum BitOP
/*    */   implements Rawable
/*    */ {
/* 10 */   AND, OR, XOR, NOT;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   BitOP() {
/* 15 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 20 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\BitOP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */