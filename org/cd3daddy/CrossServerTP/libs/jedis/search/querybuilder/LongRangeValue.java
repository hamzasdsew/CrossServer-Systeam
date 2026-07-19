/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ public class LongRangeValue
/*    */   extends RangeValue
/*    */ {
/*    */   private final long from;
/*    */   private final long to;
/*    */   
/*    */   public boolean isCombinable() {
/* 10 */     return false;
/*    */   }
/*    */   
/*    */   private static void appendNum(StringBuilder sb, long n, boolean inclusive) {
/* 14 */     if (!inclusive) {
/* 15 */       sb.append("(");
/*    */     }
/* 17 */     if (n == Long.MIN_VALUE) {
/* 18 */       sb.append("-inf");
/* 19 */     } else if (n == Long.MAX_VALUE) {
/* 20 */       sb.append("inf");
/*    */     } else {
/* 22 */       sb.append(Long.toString(n));
/*    */     } 
/*    */   }
/*    */   
/*    */   public LongRangeValue(long from, long to) {
/* 27 */     this.from = from;
/* 28 */     this.to = to;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void appendFrom(StringBuilder sb, boolean inclusive) {
/* 33 */     appendNum(sb, this.from, inclusive);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void appendTo(StringBuilder sb, boolean inclusive) {
/* 38 */     appendNum(sb, this.to, inclusive);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\LongRangeValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */