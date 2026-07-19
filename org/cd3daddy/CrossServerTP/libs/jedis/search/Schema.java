/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Schema
/*     */ {
/*     */   public enum FieldType
/*     */   {
/*  17 */     TAG,
/*  18 */     TEXT,
/*  19 */     GEO,
/*  20 */     NUMERIC,
/*  21 */     VECTOR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  28 */   public final List<Field> fields = new ArrayList<>();
/*     */ 
/*     */   
/*     */   public static Schema from(Field... fields) {
/*  32 */     Schema schema = new Schema();
/*  33 */     for (Field field : fields) {
/*  34 */       schema.addField(field);
/*     */     }
/*  36 */     return schema;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema addTextField(String name, double weight) {
/*  47 */     this.fields.add(new TextField(name, weight));
/*  48 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema addSortableTextField(String name, double weight) {
/*  59 */     this.fields.add(new TextField(name, weight, true));
/*  60 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema addGeoField(String name) {
/*  70 */     this.fields.add(new Field(name, FieldType.GEO, false));
/*  71 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema addNumericField(String name) {
/*  81 */     this.fields.add(new Field(name, FieldType.NUMERIC, false));
/*  82 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Schema addSortableNumericField(String name) {
/*  87 */     this.fields.add(new Field(name, FieldType.NUMERIC, true));
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addTagField(String name) {
/*  92 */     this.fields.add(new TagField(name));
/*  93 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addTagField(String name, String separator) {
/*  97 */     this.fields.add(new TagField(name, separator));
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addTagField(String name, boolean caseSensitive) {
/* 102 */     this.fields.add(new TagField(name, caseSensitive, false));
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addTagField(String name, String separator, boolean caseSensitive) {
/* 107 */     this.fields.add(new TagField(name, separator, caseSensitive, false));
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addSortableTagField(String name, String separator) {
/* 112 */     this.fields.add(new TagField(name, separator, true));
/* 113 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addSortableTagField(String name, boolean caseSensitive) {
/* 117 */     this.fields.add(new TagField(name, caseSensitive, true));
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addSortableTagField(String name, String separator, boolean caseSensitive) {
/* 122 */     this.fields.add(new TagField(name, separator, caseSensitive, true));
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addVectorField(String name, VectorField.VectorAlgo algorithm, Map<String, Object> attributes) {
/* 127 */     this.fields.add(new VectorField(name, algorithm, attributes));
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addFlatVectorField(String name, Map<String, Object> attributes) {
/* 132 */     this.fields.add(new VectorField(name, VectorField.VectorAlgo.FLAT, attributes));
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addHNSWVectorField(String name, Map<String, Object> attributes) {
/* 137 */     this.fields.add(new VectorField(name, VectorField.VectorAlgo.HNSW, attributes));
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public Schema addField(Field field) {
/* 142 */     this.fields.add(field);
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Schema as(String attribute) {
/* 152 */     ((Field)this.fields.get(this.fields.size() - 1)).as(attribute);
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 158 */     return "Schema{fields=" + this.fields + "}";
/*     */   }
/*     */   
/*     */   public static class Field
/*     */     implements IParams {
/*     */     protected final FieldName fieldName;
/*     */     protected final Schema.FieldType type;
/*     */     protected final boolean sortable;
/*     */     protected final boolean noIndex;
/*     */     
/*     */     public Field(String name, Schema.FieldType type) {
/* 169 */       this(name, type, false, false);
/*     */     }
/*     */     
/*     */     public Field(String name, Schema.FieldType type, boolean sortable) {
/* 173 */       this(name, type, sortable, false);
/*     */     }
/*     */     
/*     */     public Field(String name, Schema.FieldType type, boolean sortable, boolean noindex) {
/* 177 */       this(FieldName.of(name), type, sortable, noindex);
/*     */     }
/*     */     
/*     */     public Field(FieldName name, Schema.FieldType type) {
/* 181 */       this(name, type, false, false);
/*     */     }
/*     */     
/*     */     public Field(FieldName name, Schema.FieldType type, boolean sortable, boolean noIndex) {
/* 185 */       this.fieldName = name;
/* 186 */       this.type = type;
/* 187 */       this.sortable = sortable;
/* 188 */       this.noIndex = noIndex;
/*     */     }
/*     */     
/*     */     public void as(String attribute) {
/* 192 */       this.fieldName.as(attribute);
/*     */     }
/*     */ 
/*     */     
/*     */     public final void addParams(CommandArguments args) {
/* 197 */       this.fieldName.addParams(args);
/* 198 */       args.add(this.type.name());
/* 199 */       addTypeArgs(args);
/* 200 */       if (this.sortable) {
/* 201 */         args.add("SORTABLE");
/*     */       }
/* 203 */       if (this.noIndex) {
/* 204 */         args.add("NOINDEX");
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected void addTypeArgs(CommandArguments args) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 217 */       return "Field{name='" + this.fieldName + "', type=" + this.type + ", sortable=" + this.sortable + ", noindex=" + this.noIndex + "}";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class TextField
/*     */     extends Field
/*     */   {
/*     */     private final double weight;
/*     */     
/*     */     private final boolean nostem;
/*     */     private final String phonetic;
/*     */     
/*     */     public TextField(String name) {
/* 231 */       this(name, 1.0D);
/*     */     }
/*     */     
/*     */     public TextField(FieldName name) {
/* 235 */       this(name, 1.0D, false, false, false, (String)null);
/*     */     }
/*     */     
/*     */     public TextField(String name, double weight) {
/* 239 */       this(name, weight, false);
/*     */     }
/*     */     
/*     */     public TextField(String name, double weight, boolean sortable) {
/* 243 */       this(name, weight, sortable, false);
/*     */     }
/*     */     
/*     */     public TextField(String name, double weight, boolean sortable, boolean nostem) {
/* 247 */       this(name, weight, sortable, nostem, false);
/*     */     }
/*     */     
/*     */     public TextField(String name, double weight, boolean sortable, boolean nostem, boolean noindex) {
/* 251 */       this(name, weight, sortable, nostem, noindex, (String)null);
/*     */     }
/*     */     
/*     */     public TextField(String name, double weight, boolean sortable, boolean nostem, boolean noindex, String phonetic) {
/* 255 */       super(name, Schema.FieldType.TEXT, sortable, noindex);
/* 256 */       this.weight = weight;
/* 257 */       this.nostem = nostem;
/* 258 */       this.phonetic = phonetic;
/*     */     }
/*     */     
/*     */     public TextField(FieldName name, double weight, boolean sortable, boolean nostem, boolean noindex, String phonetic) {
/* 262 */       super(name, Schema.FieldType.TEXT, sortable, noindex);
/* 263 */       this.weight = weight;
/* 264 */       this.nostem = nostem;
/* 265 */       this.phonetic = phonetic;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addTypeArgs(CommandArguments args) {
/* 270 */       if (this.weight != 1.0D) {
/* 271 */         args.add("WEIGHT");
/* 272 */         args.add(Double.toString(this.weight));
/*     */       } 
/* 274 */       if (this.nostem) {
/* 275 */         args.add("NOSTEM");
/*     */       }
/* 277 */       if (this.phonetic != null) {
/* 278 */         args.add("PHONETIC");
/* 279 */         args.add(this.phonetic);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 285 */       return "TextField{name='" + this.fieldName + "', type=" + this.type + ", sortable=" + this.sortable + ", noindex=" + this.noIndex + ", weight=" + this.weight + ", nostem=" + this.nostem + ", phonetic='" + this.phonetic + "'}";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class TagField
/*     */     extends Field
/*     */   {
/*     */     private final String separator;
/*     */     
/*     */     public TagField(String name) {
/* 296 */       this(name, (String)null);
/*     */     }
/*     */     private final boolean caseSensitive;
/*     */     public TagField(String name, String separator) {
/* 300 */       this(name, separator, false);
/*     */     }
/*     */     
/*     */     public TagField(String name, boolean sortable) {
/* 304 */       this(name, (String)null, sortable);
/*     */     }
/*     */     
/*     */     public TagField(String name, String separator, boolean sortable) {
/* 308 */       this(name, separator, false, sortable);
/*     */     }
/*     */     
/*     */     public TagField(String name, boolean caseSensitive, boolean sortable) {
/* 312 */       this(name, (String)null, caseSensitive, sortable);
/*     */     }
/*     */     
/*     */     public TagField(String name, String separator, boolean caseSensitive, boolean sortable) {
/* 316 */       super(name, Schema.FieldType.TAG, sortable);
/* 317 */       this.separator = separator;
/* 318 */       this.caseSensitive = caseSensitive;
/*     */     }
/*     */     
/*     */     public TagField(FieldName name, String separator, boolean sortable) {
/* 322 */       this(name, separator, false, sortable);
/*     */     }
/*     */     
/*     */     public TagField(FieldName name, String separator, boolean caseSensitive, boolean sortable) {
/* 326 */       super(name, Schema.FieldType.TAG, sortable, false);
/* 327 */       this.separator = separator;
/* 328 */       this.caseSensitive = caseSensitive;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addTypeArgs(CommandArguments args) {
/* 333 */       if (this.separator != null) {
/* 334 */         args.add("SEPARATOR");
/* 335 */         args.add(this.separator);
/*     */       } 
/* 337 */       if (this.caseSensitive) {
/* 338 */         args.add("CASESENSITIVE");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 344 */       return "TagField{name='" + this.fieldName + "', type=" + this.type + ", sortable=" + this.sortable + ", noindex=" + this.noIndex + ", separator='" + this.separator + ", caseSensitive='" + this.caseSensitive + "'}";
/*     */     } }
/*     */   
/*     */   public static class VectorField extends Field {
/*     */     private final VectorAlgo algorithm;
/*     */     private final Map<String, Object> attributes;
/*     */     
/*     */     public enum VectorAlgo {
/* 352 */       FLAT,
/* 353 */       HNSW;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public VectorField(String name, VectorAlgo algorithm, Map<String, Object> attributes) {
/* 360 */       super(name, Schema.FieldType.VECTOR);
/* 361 */       this.algorithm = algorithm;
/* 362 */       this.attributes = attributes;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addTypeArgs(CommandArguments args) {
/* 367 */       args.add(this.algorithm);
/* 368 */       args.add(Integer.valueOf(this.attributes.size() * 2));
/* 369 */       for (Map.Entry<String, Object> entry : this.attributes.entrySet()) {
/* 370 */         args.add(entry.getKey());
/* 371 */         args.add(entry.getValue());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 377 */       return "VectorField{name='" + this.fieldName + "', type=" + this.type + ", algorithm=" + this.algorithm + ", attributes=" + this.attributes + "}";
/*     */     }
/*     */   }
/*     */   
/*     */   public enum VectorAlgo {
/*     */     FLAT, HNSW;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\Schema.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */