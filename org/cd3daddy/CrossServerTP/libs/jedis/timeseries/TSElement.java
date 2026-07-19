/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ public class TSElement
/*    */ {
/*    */   private final long timestamp;
/*    */   private final double value;
/*    */   
/*    */   public TSElement(long timestamp, double value) {
/*  9 */     this.timestamp = timestamp;
/* 10 */     this.value = value;
/*    */   }
/*    */   
/*    */   public long getTimestamp() {
/* 14 */     return this.timestamp;
/*    */   }
/*    */   
/*    */   public double getValue() {
/* 18 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 23 */     return 31 * Long.hashCode(this.timestamp) + Long.hashCode(Double.doubleToLongBits(this.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 28 */     if (obj == null) return false; 
/* 29 */     if (obj == this) return true; 
/* 30 */     if (!(obj instanceof TSElement)) return false;
/*    */     
/* 32 */     TSElement other = (TSElement)obj;
/* 33 */     return (this.timestamp == other.timestamp && this.value == other.value);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 39 */     return "(" + this.timestamp + ":" + this.value + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */