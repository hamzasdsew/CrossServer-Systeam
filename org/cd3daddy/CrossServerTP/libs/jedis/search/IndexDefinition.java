/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ 
/*     */ public class IndexDefinition implements IParams {
/*     */   private final Type type;
/*     */   private String[] prefixes;
/*     */   private String filter;
/*     */   private String languageField;
/*     */   private String language;
/*     */   private String scoreFiled;
/*     */   
/*     */   public enum Type {
/*  14 */     HASH,
/*  15 */     JSON;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  24 */   private double score = 1.0D;
/*     */   
/*     */   public IndexDefinition() {
/*  27 */     this(null);
/*     */   }
/*     */   
/*     */   public IndexDefinition(Type type) {
/*  31 */     this.type = type;
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  35 */     return this.type;
/*     */   }
/*     */   
/*     */   public String[] getPrefixes() {
/*  39 */     return this.prefixes;
/*     */   }
/*     */   
/*     */   public IndexDefinition setPrefixes(String... prefixes) {
/*  43 */     this.prefixes = prefixes;
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public String getFilter() {
/*  48 */     return this.filter;
/*     */   }
/*     */   
/*     */   public IndexDefinition setFilter(String filter) {
/*  52 */     this.filter = filter;
/*  53 */     return this;
/*     */   }
/*     */   
/*     */   public String getLanguageField() {
/*  57 */     return this.languageField;
/*     */   }
/*     */   
/*     */   public IndexDefinition setLanguageField(String languageField) {
/*  61 */     this.languageField = languageField;
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public String getLanguage() {
/*  66 */     return this.language;
/*     */   }
/*     */   
/*     */   public IndexDefinition setLanguage(String language) {
/*  70 */     this.language = language;
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public String getScoreFiled() {
/*  75 */     return this.scoreFiled;
/*     */   }
/*     */   
/*     */   public IndexDefinition setScoreFiled(String scoreFiled) {
/*  79 */     this.scoreFiled = scoreFiled;
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public double getScore() {
/*  84 */     return this.score;
/*     */   }
/*     */   
/*     */   public IndexDefinition setScore(double score) {
/*  88 */     this.score = score;
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/*  95 */     if (this.type != null) {
/*  96 */       args.add(SearchProtocol.SearchKeyword.ON.name());
/*  97 */       args.add(this.type.name());
/*     */     } 
/*     */     
/* 100 */     if (this.prefixes != null && this.prefixes.length > 0) {
/* 101 */       args.add(SearchProtocol.SearchKeyword.PREFIX.name());
/* 102 */       args.add(Integer.toString(this.prefixes.length));
/* 103 */       args.addObjects((Object[])this.prefixes);
/*     */     } 
/*     */     
/* 106 */     if (this.filter != null) {
/* 107 */       args.add(SearchProtocol.SearchKeyword.FILTER.name());
/* 108 */       args.add(this.filter);
/*     */     } 
/*     */     
/* 111 */     if (this.languageField != null) {
/* 112 */       args.add(SearchProtocol.SearchKeyword.LANGUAGE_FIELD.name());
/* 113 */       args.add(this.languageField);
/*     */     } 
/*     */     
/* 116 */     if (this.language != null) {
/* 117 */       args.add(SearchProtocol.SearchKeyword.LANGUAGE.name());
/* 118 */       args.add(this.language);
/*     */     } 
/*     */     
/* 121 */     if (this.scoreFiled != null) {
/* 122 */       args.add(SearchProtocol.SearchKeyword.SCORE_FIELD.name());
/* 123 */       args.add(this.scoreFiled);
/*     */     } 
/*     */     
/* 126 */     if (this.score != 1.0D) {
/* 127 */       args.add(SearchProtocol.SearchKeyword.SCORE.name());
/* 128 */       args.add(Double.toString(this.score));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\IndexDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */