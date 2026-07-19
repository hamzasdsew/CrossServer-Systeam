/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RangeValue
/*    */   extends Value
/*    */ {
/*    */   private boolean inclusiveMin = true;
/*    */   private boolean inclusiveMax = true;
/*    */   
/*    */   public boolean isCombinable() {
/* 13 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void appendFrom(StringBuilder paramStringBuilder, boolean paramBoolean);
/*    */   
/*    */   protected abstract void appendTo(StringBuilder paramStringBuilder, boolean paramBoolean);
/*    */   
/*    */   public String toString() {
/* 22 */     StringBuilder sb = new StringBuilder();
/* 23 */     sb.append('[');
/* 24 */     appendFrom(sb, this.inclusiveMin);
/* 25 */     sb.append(' ');
/* 26 */     appendTo(sb, this.inclusiveMax);
/* 27 */     sb.append(']');
/* 28 */     return sb.toString();
/*    */   }
/*    */   
/*    */   public RangeValue inclusiveMin(boolean val) {
/* 32 */     this.inclusiveMin = val;
/* 33 */     return this;
/*    */   }
/*    */   
/*    */   public RangeValue inclusiveMax(boolean val) {
/* 37 */     this.inclusiveMax = val;
/* 38 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\RangeValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */