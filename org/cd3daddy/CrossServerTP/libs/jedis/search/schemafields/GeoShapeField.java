/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*    */ 
/*    */ public class GeoShapeField
/*    */   extends SchemaField
/*    */ {
/*    */   private final CoordinateSystem system;
/*    */   
/*    */   public enum CoordinateSystem
/*    */   {
/* 15 */     FLAT,
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 20 */     SPHERICAL;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public GeoShapeField(String fieldName, CoordinateSystem system) {
/* 26 */     super(fieldName);
/* 27 */     this.system = system;
/*    */   }
/*    */   
/*    */   public GeoShapeField(FieldName fieldName, CoordinateSystem system) {
/* 31 */     super(fieldName);
/* 32 */     this.system = system;
/*    */   }
/*    */   
/*    */   public static GeoShapeField of(String fieldName, CoordinateSystem system) {
/* 36 */     return new GeoShapeField(fieldName, system);
/*    */   }
/*    */ 
/*    */   
/*    */   public GeoShapeField as(String attribute) {
/* 41 */     super.as(attribute);
/* 42 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 47 */     args.addParams((IParams)this.fieldName).add(SearchProtocol.SearchKeyword.GEOSHAPE).add(this.system);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\schemafields\GeoShapeField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */