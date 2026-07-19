/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*     */ 
/*     */ public class TextField
/*     */   extends SchemaField {
/*     */   private boolean sortable;
/*     */   private boolean sortableUNF;
/*     */   private boolean noStem;
/*     */   private boolean noIndex;
/*     */   private String phoneticMatcher;
/*     */   private Double weight;
/*     */   private boolean withSuffixTrie;
/*     */   
/*     */   public TextField(String fieldName) {
/*  19 */     super(fieldName);
/*     */   }
/*     */   
/*     */   public TextField(FieldName fieldName) {
/*  23 */     super(fieldName);
/*     */   }
/*     */   
/*     */   public static TextField of(String fieldName) {
/*  27 */     return new TextField(fieldName);
/*     */   }
/*     */   
/*     */   public static TextField of(FieldName fieldName) {
/*  31 */     return new TextField(fieldName);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextField as(String attribute) {
/*  36 */     super.as(attribute);
/*  37 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField sortable() {
/*  44 */     this.sortable = true;
/*  45 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField sortableUNF() {
/*  52 */     this.sortableUNF = true;
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField sortableUnNormalizedForm() {
/*  60 */     return sortableUNF();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField noStem() {
/*  67 */     this.noStem = true;
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField noIndex() {
/*  75 */     this.noIndex = true;
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField phonetic(String matcher) {
/*  83 */     this.phoneticMatcher = matcher;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField weight(double weight) {
/*  92 */     this.weight = Double.valueOf(weight);
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextField withSuffixTrie() {
/* 101 */     this.withSuffixTrie = true;
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 107 */     args.addParams((IParams)this.fieldName);
/* 108 */     args.add(SearchProtocol.SearchKeyword.TEXT);
/*     */     
/* 110 */     if (this.weight != null) {
/* 111 */       args.add(SearchProtocol.SearchKeyword.WEIGHT).add(this.weight);
/*     */     }
/*     */     
/* 114 */     if (this.noStem) {
/* 115 */       args.add(SearchProtocol.SearchKeyword.NOSTEM);
/*     */     }
/*     */     
/* 118 */     if (this.phoneticMatcher != null) {
/* 119 */       args.add(SearchProtocol.SearchKeyword.PHONETIC).add(this.phoneticMatcher);
/*     */     }
/*     */     
/* 122 */     if (this.withSuffixTrie) {
/* 123 */       args.add(SearchProtocol.SearchKeyword.WITHSUFFIXTRIE);
/*     */     }
/*     */     
/* 126 */     if (this.sortableUNF) {
/* 127 */       args.add(SearchProtocol.SearchKeyword.SORTABLE).add(SearchProtocol.SearchKeyword.UNF);
/* 128 */     } else if (this.sortable) {
/* 129 */       args.add(SearchProtocol.SearchKeyword.SORTABLE);
/*     */     } 
/*     */     
/* 132 */     if (this.noIndex)
/* 133 */       args.add(SearchProtocol.SearchKeyword.NOINDEX); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\schemafields\TextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */