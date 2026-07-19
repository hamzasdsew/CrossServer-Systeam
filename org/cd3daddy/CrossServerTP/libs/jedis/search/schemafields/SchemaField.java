/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*    */ 
/*    */ public abstract class SchemaField
/*    */   implements IParams {
/*    */   protected final FieldName fieldName;
/*    */   
/*    */   public SchemaField(String fieldName) {
/* 11 */     this.fieldName = new FieldName(fieldName);
/*    */   }
/*    */   
/*    */   public SchemaField(FieldName fieldName) {
/* 15 */     this.fieldName = fieldName;
/*    */   }
/*    */   
/*    */   public SchemaField as(String attribute) {
/* 19 */     this.fieldName.as(attribute);
/* 20 */     return this;
/*    */   }
/*    */   
/*    */   public final FieldName getFieldName() {
/* 24 */     return this.fieldName;
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 28 */     return this.fieldName.getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\schemafields\SchemaField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */