/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*    */ 
/*    */ public class SortedField
/*    */ {
/*    */   private final String fieldName;
/*    */   private final SortOrder sortOrder;
/*    */   
/*    */   public enum SortOrder {
/*  9 */     ASC, DESC;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SortedField(String fieldName, SortOrder order) {
/* 16 */     this.fieldName = fieldName;
/* 17 */     this.sortOrder = order;
/*    */   }
/*    */   
/*    */   public final String getOrder() {
/* 21 */     return this.sortOrder.toString();
/*    */   }
/*    */   
/*    */   public final String getField() {
/* 25 */     return this.fieldName;
/*    */   }
/*    */   
/*    */   public static SortedField asc(String field) {
/* 29 */     return new SortedField(field, SortOrder.ASC);
/*    */   }
/*    */   
/*    */   public static SortedField desc(String field) {
/* 33 */     return new SortedField(field, SortOrder.DESC);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\SortedField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */