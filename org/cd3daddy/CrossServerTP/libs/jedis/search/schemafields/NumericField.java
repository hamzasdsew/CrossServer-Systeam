/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*    */ 
/*    */ 
/*    */ public class NumericField
/*    */   extends SchemaField
/*    */ {
/*    */   private boolean sortable;
/*    */   private boolean noIndex;
/*    */   
/*    */   public NumericField(String fieldName) {
/* 16 */     super(fieldName);
/*    */   }
/*    */   
/*    */   public NumericField(FieldName fieldName) {
/* 20 */     super(fieldName);
/*    */   }
/*    */   
/*    */   public static NumericField of(String fieldName) {
/* 24 */     return new NumericField(fieldName);
/*    */   }
/*    */   
/*    */   public static NumericField of(FieldName fieldName) {
/* 28 */     return new NumericField(fieldName);
/*    */   }
/*    */ 
/*    */   
/*    */   public NumericField as(String attribute) {
/* 33 */     super.as(attribute);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NumericField sortable() {
/* 41 */     this.sortable = true;
/* 42 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NumericField noIndex() {
/* 49 */     this.noIndex = true;
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 55 */     args.addParams((IParams)this.fieldName);
/* 56 */     args.add(SearchProtocol.SearchKeyword.NUMERIC);
/*    */     
/* 58 */     if (this.sortable) {
/* 59 */       args.add(SearchProtocol.SearchKeyword.SORTABLE);
/*    */     }
/*    */     
/* 62 */     if (this.noIndex)
/* 63 */       args.add(SearchProtocol.SearchKeyword.NOINDEX); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\schemafields\NumericField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */