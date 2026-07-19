/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ public class TagField
/*     */   extends SchemaField {
/*     */   private boolean sortable;
/*     */   private boolean sortableUNF;
/*     */   private boolean noIndex;
/*     */   private byte[] separator;
/*     */   private boolean caseSensitive;
/*     */   private boolean withSuffixTrie;
/*     */   
/*     */   public TagField(String fieldName) {
/*  19 */     super(fieldName);
/*     */   }
/*     */   
/*     */   public TagField(FieldName fieldName) {
/*  23 */     super(fieldName);
/*     */   }
/*     */   
/*     */   public static TagField of(String fieldName) {
/*  27 */     return new TagField(fieldName);
/*     */   }
/*     */   
/*     */   public static TagField of(FieldName fieldName) {
/*  31 */     return new TagField(fieldName);
/*     */   }
/*     */ 
/*     */   
/*     */   public TagField as(String attribute) {
/*  36 */     super.as(attribute);
/*  37 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagField sortable() {
/*  44 */     this.sortable = true;
/*  45 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagField sortableUNF() {
/*  52 */     this.sortableUNF = true;
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagField sortableUnNormalizedForm() {
/*  60 */     return sortableUNF();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagField noIndex() {
/*  67 */     this.noIndex = true;
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagField separator(char separator) {
/*  75 */     if (separator < '') {
/*  76 */       this.separator = new byte[] { (byte)separator };
/*     */     } else {
/*  78 */       this.separator = SafeEncoder.encode(String.valueOf(separator));
/*     */     } 
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagField caseSensitive() {
/*  87 */     this.caseSensitive = true;
/*  88 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TagField withSuffixTrie() {
/*  96 */     this.withSuffixTrie = true;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 102 */     args.addParams((IParams)this.fieldName);
/* 103 */     args.add(SearchProtocol.SearchKeyword.TAG);
/*     */     
/* 105 */     if (this.separator != null) {
/* 106 */       args.add(SearchProtocol.SearchKeyword.SEPARATOR).add(this.separator);
/*     */     }
/*     */     
/* 109 */     if (this.caseSensitive) {
/* 110 */       args.add(SearchProtocol.SearchKeyword.CASESENSITIVE);
/*     */     }
/*     */     
/* 113 */     if (this.withSuffixTrie) {
/* 114 */       args.add(SearchProtocol.SearchKeyword.WITHSUFFIXTRIE);
/*     */     }
/*     */     
/* 117 */     if (this.sortableUNF) {
/* 118 */       args.add(SearchProtocol.SearchKeyword.SORTABLE).add(SearchProtocol.SearchKeyword.UNF);
/* 119 */     } else if (this.sortable) {
/* 120 */       args.add(SearchProtocol.SearchKeyword.SORTABLE);
/*     */     } 
/*     */     
/* 123 */     if (this.noIndex)
/* 124 */       args.add(SearchProtocol.SearchKeyword.NOINDEX); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\schemafields\TagField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */