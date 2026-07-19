/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*    */ 
/*    */ public class VectorField extends SchemaField {
/*    */   private final VectorAlgorithm algorithm;
/*    */   private final Map<String, Object> attributes;
/*    */   
/*    */   public enum VectorAlgorithm {
/* 13 */     FLAT,
/* 14 */     HNSW;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public VectorField(String fieldName, VectorAlgorithm algorithm, Map<String, Object> attributes) {
/* 21 */     super(fieldName);
/* 22 */     this.algorithm = algorithm;
/* 23 */     this.attributes = attributes;
/*    */   }
/*    */   
/*    */   public VectorField(FieldName fieldName, VectorAlgorithm algorithm, Map<String, Object> attributes) {
/* 27 */     super(fieldName);
/* 28 */     this.algorithm = algorithm;
/* 29 */     this.attributes = attributes;
/*    */   }
/*    */ 
/*    */   
/*    */   public VectorField as(String attribute) {
/* 34 */     super.as(attribute);
/* 35 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 40 */     args.addParams((IParams)this.fieldName);
/* 41 */     args.add(SearchProtocol.SearchKeyword.VECTOR);
/*    */     
/* 43 */     args.add(this.algorithm);
/* 44 */     args.add(Integer.valueOf(this.attributes.size() * 2));
/* 45 */     this.attributes.forEach((name, value) -> args.add(name).add(value));
/*    */   }
/*    */   
/*    */   public static Builder builder() {
/* 49 */     return new Builder();
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Builder
/*    */   {
/*    */     private FieldName fieldName;
/*    */     private VectorField.VectorAlgorithm algorithm;
/*    */     private Map<String, Object> attributes;
/*    */     
/*    */     private Builder() {}
/*    */     
/*    */     public VectorField build() {
/* 62 */       if (this.fieldName == null || this.algorithm == null || this.attributes == null || this.attributes.isEmpty()) {
/* 63 */         throw new IllegalArgumentException("All required VectorField parameters are not set.");
/*    */       }
/* 65 */       return new VectorField(this.fieldName, this.algorithm, this.attributes);
/*    */     }
/*    */     
/*    */     public Builder fieldName(String fieldName) {
/* 69 */       this.fieldName = FieldName.of(fieldName);
/* 70 */       return this;
/*    */     }
/*    */     
/*    */     public Builder fieldName(FieldName fieldName) {
/* 74 */       this.fieldName = fieldName;
/* 75 */       return this;
/*    */     }
/*    */     
/*    */     public Builder as(String attribute) {
/* 79 */       this.fieldName.as(attribute);
/* 80 */       return this;
/*    */     }
/*    */     
/*    */     public Builder algorithm(VectorField.VectorAlgorithm algorithm) {
/* 84 */       this.algorithm = algorithm;
/* 85 */       return this;
/*    */     }
/*    */     
/*    */     public Builder attributes(Map<String, Object> attributes) {
/* 89 */       this.attributes = attributes;
/* 90 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addAttribute(String name, Object value) {
/* 94 */       if (this.attributes == null) {
/* 95 */         this.attributes = new LinkedHashMap<>();
/*    */       }
/* 97 */       this.attributes.put(name, value);
/* 98 */       return this;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\schemafields\VectorField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */