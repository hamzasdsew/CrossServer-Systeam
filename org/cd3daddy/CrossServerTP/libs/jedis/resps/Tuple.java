/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Objects;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.ByteArrayComparator;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public class Tuple
/*    */   implements Comparable<Tuple> {
/*    */   private byte[] element;
/*    */   private Double score;
/*    */   
/*    */   public Tuple(String element, Double score) {
/* 14 */     this(SafeEncoder.encode(element), score);
/*    */   }
/*    */ 
/*    */   
/*    */   public Tuple(byte[] element, Double score) {
/* 19 */     this.element = element;
/* 20 */     this.score = score;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 25 */     int prime = 31;
/* 26 */     int result = 1;
/* 27 */     result = 31 * result;
/* 28 */     if (null != this.element) {
/* 29 */       for (byte b : this.element) {
/* 30 */         result = 31 * result + b;
/*    */       }
/*    */     }
/* 33 */     long temp = Double.doubleToLongBits(this.score.doubleValue());
/* 34 */     result = 31 * result + (int)(temp ^ temp >>> 32L);
/* 35 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 40 */     if (obj == null) return false; 
/* 41 */     if (obj == this) return true; 
/* 42 */     if (!(obj instanceof Tuple)) return false;
/*    */     
/* 44 */     Tuple other = (Tuple)obj;
/* 45 */     if (!Arrays.equals(this.element, other.element)) return false; 
/* 46 */     return Objects.equals(this.score, other.score);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Tuple other) {
/* 51 */     return compare(this, other);
/*    */   }
/*    */   
/*    */   public static int compare(Tuple t1, Tuple t2) {
/* 55 */     int compScore = Double.compare(t1.score.doubleValue(), t2.score.doubleValue());
/* 56 */     if (compScore != 0) return compScore;
/*    */     
/* 58 */     return ByteArrayComparator.compare(t1.element, t2.element);
/*    */   }
/*    */   
/*    */   public String getElement() {
/* 62 */     if (null != this.element) {
/* 63 */       return SafeEncoder.encode(this.element);
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getBinaryElement() {
/* 70 */     return this.element;
/*    */   }
/*    */   
/*    */   public double getScore() {
/* 74 */     return this.score.doubleValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 80 */     return '[' + SafeEncoder.encode(this.element) + ',' + this.score + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\Tuple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */