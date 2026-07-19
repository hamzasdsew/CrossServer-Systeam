/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.GeoCoordinate;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ public class GeoRadiusResponse
/*    */ {
/*    */   private byte[] member;
/*    */   private double distance;
/*    */   private GeoCoordinate coordinate;
/*    */   private long rawScore;
/*    */   
/*    */   public GeoRadiusResponse(byte[] member) {
/* 17 */     this.member = member;
/*    */   }
/*    */   
/*    */   public void setDistance(double distance) {
/* 21 */     this.distance = distance;
/*    */   }
/*    */   
/*    */   public void setCoordinate(GeoCoordinate coordinate) {
/* 25 */     this.coordinate = coordinate;
/*    */   }
/*    */   
/*    */   public void setRawScore(long rawScore) {
/* 29 */     this.rawScore = rawScore;
/*    */   }
/*    */   
/*    */   public byte[] getMember() {
/* 33 */     return this.member;
/*    */   }
/*    */   
/*    */   public String getMemberByString() {
/* 37 */     return SafeEncoder.encode(this.member);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double getDistance() {
/* 45 */     return this.distance;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GeoCoordinate getCoordinate() {
/* 52 */     return this.coordinate;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getRawScore() {
/* 61 */     return this.rawScore;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 66 */     if (obj == this) {
/* 67 */       return true;
/*    */     }
/*    */     
/* 70 */     if (!(obj instanceof GeoRadiusResponse)) {
/* 71 */       return false;
/*    */     }
/*    */     
/* 74 */     GeoRadiusResponse response = (GeoRadiusResponse)obj;
/* 75 */     return (Double.compare(this.distance, response.getDistance()) == 0 && this.rawScore == response
/* 76 */       .getRawScore() && this.coordinate.equals(response.coordinate) && 
/* 77 */       Arrays.equals(this.member, response.getMember()));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 82 */     int hash = 7;
/* 83 */     hash = 67 * hash + Arrays.hashCode(this.member);
/* 84 */     hash = 67 * hash + (int)(Double.doubleToLongBits(this.distance) ^ Double.doubleToLongBits(this.distance) >>> 32L);
/* 85 */     hash = 67 * hash + Objects.hashCode(this.coordinate);
/* 86 */     hash = 67 * hash + (int)(this.rawScore ^ this.rawScore >>> 32L);
/* 87 */     return hash;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\GeoRadiusResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */