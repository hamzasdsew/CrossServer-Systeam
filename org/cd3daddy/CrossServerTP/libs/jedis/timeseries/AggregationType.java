/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public enum AggregationType
/*    */   implements Rawable {
/*  9 */   AVG, SUM, MIN, MAX,
/* 10 */   RANGE, COUNT, FIRST, LAST,
/* 11 */   STD_P("STD.P"), STD_S("STD.S"),
/* 12 */   VAR_P("VAR.P"), VAR_S("VAR.S"),
/* 13 */   TWA;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   AggregationType() {
/* 18 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */   
/*    */   AggregationType(String alt) {
/* 22 */     this.raw = SafeEncoder.encode(alt);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 27 */     return this.raw;
/*    */   }
/*    */   
/*    */   public static AggregationType safeValueOf(String str) {
/*    */     try {
/* 32 */       return valueOf(str.replace('.', '_').toUpperCase(Locale.ENGLISH));
/* 33 */     } catch (IllegalArgumentException iae) {
/* 34 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\AggregationType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */