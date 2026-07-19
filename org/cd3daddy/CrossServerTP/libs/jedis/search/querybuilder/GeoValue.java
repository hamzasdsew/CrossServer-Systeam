/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GeoValue
/*    */   extends Value
/*    */ {
/*    */   private final GeoUnit unit;
/*    */   private final double lon;
/*    */   private final double lat;
/*    */   private final double radius;
/*    */   
/*    */   public GeoValue(double lon, double lat, double radius, GeoUnit unit) {
/* 17 */     this.lon = lon;
/* 18 */     this.lat = lat;
/* 19 */     this.radius = radius;
/* 20 */     this.unit = unit;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "[" + this.lon + " " + this.lat + " " + this.radius + " " + this.unit
/* 26 */       .name().toLowerCase(Locale.ENGLISH) + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isCombinable() {
/* 31 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\GeoValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */