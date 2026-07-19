/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Document
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 4884173545291367373L;
/*     */   private final String id;
/*     */   private Double score;
/*     */   private final Map<String, Object> fields;
/*     */   
/*     */   public Document(String id) {
/*  26 */     this(id, 1.0D);
/*     */   }
/*     */   
/*     */   public Document(String id, double score) {
/*  30 */     this(id, new HashMap<>(), score);
/*     */   }
/*     */   
/*     */   public Document(String id, Map<String, Object> fields) {
/*  34 */     this(id, fields, 1.0D);
/*     */   }
/*     */   
/*     */   public Document(String id, Map<String, Object> fields, double score) {
/*  38 */     this.id = id;
/*  39 */     this.fields = fields;
/*  40 */     this.score = Double.valueOf(score);
/*     */   }
/*     */   
/*     */   private Document(String id, Double score, Map<String, Object> fields) {
/*  44 */     this.id = id;
/*  45 */     this.score = score;
/*  46 */     this.fields = fields;
/*     */   }
/*     */   
/*     */   public Iterable<Map.Entry<String, Object>> getProperties() {
/*  50 */     return this.fields.entrySet();
/*     */   }
/*     */   
/*     */   public static Document load(String id, double score, byte[] payload, List<byte[]> fields) {
/*  54 */     return load(id, score, fields, true);
/*     */   }
/*     */   
/*     */   public static Document load(String id, double score, List<byte[]> fields, boolean decode) {
/*  58 */     Document ret = new Document(id, score);
/*  59 */     if (fields != null) {
/*  60 */       for (int i = 0; i < fields.size(); i += 2) {
/*  61 */         byte[] rawKey = fields.get(i);
/*  62 */         byte[] rawValue = fields.get(i + 1);
/*  63 */         String key = SafeEncoder.encode(rawKey);
/*  64 */         Object value = (rawValue == null) ? null : (decode ? SafeEncoder.encode(rawValue) : rawValue);
/*  65 */         ret.set(key, value);
/*     */       } 
/*     */     }
/*  68 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  75 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Double getScore() {
/*  82 */     return this.score;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(String key) {
/*  93 */     return this.fields.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getString(String key) {
/* 104 */     Object value = this.fields.get(key);
/* 105 */     if (value instanceof String) {
/* 106 */       return (String)value;
/*     */     }
/* 108 */     return (value instanceof byte[]) ? SafeEncoder.encode((byte[])value) : value.toString();
/*     */   }
/*     */   
/*     */   public boolean hasProperty(String key) {
/* 112 */     return this.fields.containsKey(key);
/*     */   }
/*     */   
/*     */   public Document set(String key, Object value) {
/* 116 */     this.fields.put(key, value);
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document setScore(float score) {
/* 127 */     this.score = Double.valueOf(score);
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 133 */     return "id:" + getId() + ", score: " + getScore() + ", properties:" + 
/* 134 */       getProperties();
/*     */   }
/*     */   
/* 137 */   static Builder<Document> SEARCH_DOCUMENT = new Builder<Document>()
/*     */     {
/*     */       private static final String ID_STR = "id";
/*     */       
/*     */       private static final String SCORE_STR = "score";
/*     */       
/*     */       private static final String FIELDS_STR = "extra_attributes";
/*     */       
/*     */       public Document build(Object data) {
/* 146 */         List<KeyValue> list = (List<KeyValue>)data;
/* 147 */         String id = null;
/* 148 */         Double score = null;
/* 149 */         Map<String, Object> fields = null;
/* 150 */         for (KeyValue kv : list) {
/* 151 */           String key = (String)BuilderFactory.STRING.build(kv.getKey());
/* 152 */           switch (key) {
/*     */             case "id":
/* 154 */               id = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */             
/*     */             case "score":
/* 157 */               score = (Double)BuilderFactory.DOUBLE.build(kv.getValue());
/*     */             
/*     */             case "extra_attributes":
/* 160 */               fields = (Map<String, Object>)BuilderFactory.ENCODED_OBJECT_MAP.build(kv.getValue());
/*     */           } 
/*     */ 
/*     */ 
/*     */         
/*     */         } 
/* 166 */         return new Document(id, score, fields);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\Document.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */