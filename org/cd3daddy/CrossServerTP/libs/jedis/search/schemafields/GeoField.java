/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*    */ 
/*    */ public class GeoField
/*    */   extends SchemaField {
/*    */   public GeoField(String fieldName) {
/* 11 */     super(fieldName);
/*    */   }
/*    */   
/*    */   public GeoField(FieldName fieldName) {
/* 15 */     super(fieldName);
/*    */   }
/*    */   
/*    */   public static GeoField of(String fieldName) {
/* 19 */     return new GeoField(fieldName);
/*    */   }
/*    */   
/*    */   public static GeoField of(FieldName fieldName) {
/* 23 */     return new GeoField(fieldName);
/*    */   }
/*    */ 
/*    */   
/*    */   public GeoField as(String attribute) {
/* 28 */     super.as(attribute);
/* 29 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 34 */     args.addParams((IParams)this.fieldName);
/* 35 */     args.add(SearchProtocol.SearchKeyword.GEO);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\schemafields\GeoField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */