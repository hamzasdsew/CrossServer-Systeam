/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ import java.util.StringJoiner;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.GeoCoordinate;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Values
/*    */ {
/*    */   private Values() {
/* 13 */     throw new InstantiationError("Must not instantiate this class");
/*    */   }
/*    */   
/*    */   private static abstract class ScalableValue
/*    */     extends Value {
/*    */     public boolean isCombinable() {
/* 19 */       return true;
/*    */     }
/*    */     private ScalableValue() {} }
/*    */   
/*    */   public static Value value(final String s) {
/* 24 */     return new ScalableValue()
/*    */       {
/*    */         public String toString() {
/* 27 */           return s;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static GeoValue geo(GeoCoordinate coord, double radius, GeoUnit unit) {
/* 33 */     return new GeoValue(coord.getLongitude(), coord.getLatitude(), radius, unit);
/*    */   }
/*    */   
/*    */   public static RangeValue between(double from, double to) {
/* 37 */     return new DoubleRangeValue(from, to);
/*    */   }
/*    */   
/*    */   public static RangeValue between(int from, int to) {
/* 41 */     return new LongRangeValue(from, to);
/*    */   }
/*    */   
/*    */   public static RangeValue eq(double d) {
/* 45 */     return new DoubleRangeValue(d, d);
/*    */   }
/*    */   
/*    */   public static RangeValue eq(int i) {
/* 49 */     return new LongRangeValue(i, i);
/*    */   }
/*    */   
/*    */   public static RangeValue lt(double d) {
/* 53 */     return (new DoubleRangeValue(Double.NEGATIVE_INFINITY, d)).inclusiveMax(false);
/*    */   }
/*    */   
/*    */   public static RangeValue lt(int d) {
/* 57 */     return (new LongRangeValue(Long.MIN_VALUE, d)).inclusiveMax(false);
/*    */   }
/*    */   
/*    */   public static RangeValue gt(double d) {
/* 61 */     return (new DoubleRangeValue(d, Double.POSITIVE_INFINITY)).inclusiveMin(false);
/*    */   }
/*    */   
/*    */   public static RangeValue gt(int d) {
/* 65 */     return (new LongRangeValue(d, Long.MAX_VALUE)).inclusiveMin(false);
/*    */   }
/*    */   
/*    */   public static RangeValue le(double d) {
/* 69 */     return lt(d).inclusiveMax(true);
/*    */   }
/*    */   
/*    */   public static RangeValue le(int d) {
/* 73 */     return lt(d).inclusiveMax(true);
/*    */   }
/*    */   
/*    */   public static RangeValue ge(double d) {
/* 77 */     return gt(d).inclusiveMin(true);
/*    */   }
/*    */   
/*    */   public static RangeValue ge(int d) {
/* 81 */     return gt(d).inclusiveMin(true);
/*    */   }
/*    */   
/*    */   public static Value tags(String... tags) {
/* 85 */     if (tags.length == 0) {
/* 86 */       throw new IllegalArgumentException("Must have at least one tag");
/*    */     }
/* 88 */     final StringJoiner sj = new StringJoiner(" | ");
/* 89 */     for (String s : tags) {
/* 90 */       sj.add(s);
/*    */     }
/* 92 */     return new Value()
/*    */       {
/*    */         public String toString() {
/* 95 */           return "{" + sj.toString() + "}";
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\Values.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */