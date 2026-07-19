/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.graph.entities;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public final class Point
/*    */ {
/*    */   private static final double EPSILON = 1.0E-5D;
/*    */   private final double latitude;
/*    */   private final double longitude;
/*    */   
/*    */   public Point(double latitude, double longitude) {
/* 23 */     this.latitude = latitude;
/* 24 */     this.longitude = longitude;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Point(List<Double> values) {
/* 31 */     if (values == null || values.size() != 2) {
/* 32 */       throw new IllegalArgumentException("Point requires two doubles.");
/*    */     }
/* 34 */     this.latitude = ((Double)values.get(0)).doubleValue();
/* 35 */     this.longitude = ((Double)values.get(1)).doubleValue();
/*    */   }
/*    */   
/*    */   public double getLatitude() {
/* 39 */     return this.latitude;
/*    */   }
/*    */   
/*    */   public double getLongitude() {
/* 43 */     return this.longitude;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object other) {
/* 48 */     if (this == other) return true; 
/* 49 */     if (!(other instanceof Point)) return false; 
/* 50 */     Point o = (Point)other;
/* 51 */     return (Math.abs(this.latitude - o.latitude) < 1.0E-5D && 
/* 52 */       Math.abs(this.longitude - o.longitude) < 1.0E-5D);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 57 */     return Objects.hash(new Object[] { Double.valueOf(this.latitude), Double.valueOf(this.longitude) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 62 */     return "Point{latitude=" + this.latitude + ", longitude=" + this.longitude + "}";
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\entities\Point.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */