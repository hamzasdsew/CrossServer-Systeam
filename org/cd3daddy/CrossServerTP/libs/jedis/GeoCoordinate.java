/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ public class GeoCoordinate
/*    */ {
/*    */   private double longitude;
/*    */   private double latitude;
/*    */   
/*    */   public GeoCoordinate(double longitude, double latitude) {
/*  9 */     this.longitude = longitude;
/* 10 */     this.latitude = latitude;
/*    */   }
/*    */   
/*    */   public double getLongitude() {
/* 14 */     return this.longitude;
/*    */   }
/*    */   
/*    */   public double getLatitude() {
/* 18 */     return this.latitude;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 23 */     if (o == null) return false; 
/* 24 */     if (o == this) return true; 
/* 25 */     if (!(o instanceof GeoCoordinate)) return false;
/*    */     
/* 27 */     GeoCoordinate that = (GeoCoordinate)o;
/*    */     
/* 29 */     if (Double.compare(that.longitude, this.longitude) != 0) return false; 
/* 30 */     return (Double.compare(that.latitude, this.latitude) == 0);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 38 */     long temp = Double.doubleToLongBits(this.longitude);
/* 39 */     int result = (int)(temp ^ temp >>> 32L);
/* 40 */     temp = Double.doubleToLongBits(this.latitude);
/* 41 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 42 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 47 */     return "(" + this.longitude + "," + this.latitude + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\GeoCoordinate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */