/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FTCreateParams
/*     */   implements IParams
/*     */ {
/*     */   private IndexDataType dataType;
/*     */   private Collection<String> prefix;
/*     */   private String filter;
/*     */   private String language;
/*     */   private String languageField;
/*     */   private Double score;
/*     */   private String scoreField;
/*     */   private boolean maxTextFields;
/*     */   private boolean noOffsets;
/*     */   private Long temporary;
/*     */   private boolean noHL;
/*     */   private boolean noFields;
/*     */   private boolean noFreqs;
/*     */   private Collection<String> stopwords;
/*     */   private boolean skipInitialScan;
/*     */   
/*     */   public static FTCreateParams createParams() {
/*  35 */     return new FTCreateParams();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams on(IndexDataType dataType) {
/*  43 */     this.dataType = dataType;
/*  44 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams prefix(String... prefixes) {
/*  51 */     if (this.prefix == null) {
/*  52 */       this.prefix = new ArrayList<>(prefixes.length);
/*     */     }
/*  54 */     Arrays.<String>stream(prefixes).forEach(p -> this.prefix.add(p));
/*  55 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams addPrefix(String prefix) {
/*  64 */     if (this.prefix == null) {
/*  65 */       this.prefix = new ArrayList<>();
/*     */     }
/*  67 */     this.prefix.add(prefix);
/*  68 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams filter(String filter) {
/*  75 */     this.filter = filter;
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams language(String defaultLanguage) {
/*  83 */     this.language = defaultLanguage;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams languageField(String languageAttribute) {
/*  91 */     this.languageField = languageAttribute;
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams score(double defaultScore) {
/*  99 */     this.score = Double.valueOf(defaultScore);
/* 100 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams scoreField(String scoreField) {
/* 108 */     this.scoreField = scoreField;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams maxTextFields() {
/* 116 */     this.maxTextFields = true;
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams noOffsets() {
/* 125 */     this.noOffsets = true;
/* 126 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams temporary(long seconds) {
/* 133 */     this.temporary = Long.valueOf(seconds);
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams noHL() {
/* 141 */     this.noHL = true;
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams noHighlights() {
/* 149 */     return noHL();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams noFields() {
/* 157 */     this.noFields = true;
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams noFreqs() {
/* 166 */     this.noFreqs = true;
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams stopwords(String... stopwords) {
/* 174 */     this.stopwords = Arrays.asList(stopwords);
/* 175 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams noStopwords() {
/* 182 */     this.stopwords = Collections.emptyList();
/* 183 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTCreateParams skipInitialScan() {
/* 190 */     this.skipInitialScan = true;
/* 191 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 197 */     if (this.dataType != null) {
/* 198 */       args.add(SearchProtocol.SearchKeyword.ON).add(this.dataType);
/*     */     }
/*     */     
/* 201 */     if (this.prefix != null) {
/* 202 */       args.add(SearchProtocol.SearchKeyword.PREFIX).add(Integer.valueOf(this.prefix.size())).addObjects(this.prefix);
/*     */     }
/*     */     
/* 205 */     if (this.filter != null) {
/* 206 */       args.add(SearchProtocol.SearchKeyword.FILTER).add(this.filter);
/*     */     }
/*     */     
/* 209 */     if (this.language != null) {
/* 210 */       args.add(SearchProtocol.SearchKeyword.LANGUAGE).add(this.language);
/*     */     }
/* 212 */     if (this.languageField != null) {
/* 213 */       args.add(SearchProtocol.SearchKeyword.LANGUAGE_FIELD).add(this.languageField);
/*     */     }
/*     */     
/* 216 */     if (this.score != null) {
/* 217 */       args.add(SearchProtocol.SearchKeyword.SCORE).add(this.score);
/*     */     }
/* 219 */     if (this.scoreField != null) {
/* 220 */       args.add(SearchProtocol.SearchKeyword.SCORE_FIELD).add(this.scoreField);
/*     */     }
/*     */     
/* 223 */     if (this.maxTextFields) {
/* 224 */       args.add(SearchProtocol.SearchKeyword.MAXTEXTFIELDS);
/*     */     }
/*     */     
/* 227 */     if (this.noOffsets) {
/* 228 */       args.add(SearchProtocol.SearchKeyword.NOOFFSETS);
/*     */     }
/*     */     
/* 231 */     if (this.temporary != null) {
/* 232 */       args.add(SearchProtocol.SearchKeyword.TEMPORARY).add(this.temporary);
/*     */     }
/*     */     
/* 235 */     if (this.noHL) {
/* 236 */       args.add(SearchProtocol.SearchKeyword.NOHL);
/*     */     }
/*     */     
/* 239 */     if (this.noFields) {
/* 240 */       args.add(SearchProtocol.SearchKeyword.NOFIELDS);
/*     */     }
/*     */     
/* 243 */     if (this.noFreqs) {
/* 244 */       args.add(SearchProtocol.SearchKeyword.NOFREQS);
/*     */     }
/*     */     
/* 247 */     if (this.stopwords != null) {
/* 248 */       args.add(SearchProtocol.SearchKeyword.STOPWORDS).add(Integer.valueOf(this.stopwords.size()));
/* 249 */       this.stopwords.forEach(w -> args.add(w));
/*     */     } 
/*     */     
/* 252 */     if (this.skipInitialScan)
/* 253 */       args.add(SearchProtocol.SearchKeyword.SKIPINITIALSCAN); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\FTCreateParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */