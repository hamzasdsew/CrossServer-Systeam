/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DoubleRangeValue
/*    */   extends RangeValue
/*    */ {
/*    */   private final double from;
/*    */   private final double to;
/*    */   
/*    */   private static void appendNum(StringBuilder sb, double n, boolean inclusive) {
/* 12 */     if (!inclusive) {
/* 13 */       sb.append("(");
/*    */     }
/* 15 */     if (n == Double.NEGATIVE_INFINITY) {
/* 16 */       sb.append("-inf");
/* 17 */     } else if (n == Double.POSITIVE_INFINITY) {
/* 18 */       sb.append("inf");
/*    */     } else {
/* 20 */       sb.append(n);
/*    */     } 
/*    */   }
/*    */   
/*    */   public DoubleRangeValue(double from, double to) {
/* 25 */     this.from = from;
/* 26 */     this.to = to;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void appendFrom(StringBuilder sb, boolean inclusive) {
/* 31 */     appendNum(sb, this.from, inclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void appendTo(StringBuilder sb, boolean inclusive) {
/* 36 */     appendNum(sb, this.to, inclusive);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\DoubleRangeValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */