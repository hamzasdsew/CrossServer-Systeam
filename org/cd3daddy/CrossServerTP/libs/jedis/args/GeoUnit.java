/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public enum GeoUnit
/*    */   implements Rawable {
/*  8 */   M, KM, MI, FT;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   GeoUnit() {
/* 13 */     this.raw = SafeEncoder.encode(name().toLowerCase(Locale.ENGLISH));
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 18 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\GeoUnit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */