/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ClientAttributeOption
/*    */   implements Rawable
/*    */ {
/* 10 */   LIB_NAME("LIB-NAME"),
/* 11 */   LIB_VER("LIB-VER");
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   ClientAttributeOption(String str) {
/* 16 */     this.raw = SafeEncoder.encode(str);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 21 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\ClientAttributeOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */