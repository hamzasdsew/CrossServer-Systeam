/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IndexOptions
/*     */   implements IParams
/*     */ {
/*     */   public static final int USE_TERM_OFFSETS = 1;
/*     */   public static final int KEEP_FIELD_FLAGS = 2;
/*     */   public static final int KEEP_TERM_FREQUENCIES = 8;
/*     */   public static final int DEFAULT_FLAGS = 11;
/*     */   private final int flags;
/*     */   private List<String> stopwords;
/*  40 */   private long expire = 0L;
/*     */ 
/*     */ 
/*     */   
/*     */   private IndexDefinition definition;
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexOptions(int flags) {
/*  49 */     this.flags = flags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IndexOptions defaultOptions() {
/*  56 */     return new IndexOptions(11);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexOptions setStopwords(String... stopwords) {
/*  66 */     this.stopwords = Arrays.asList(stopwords);
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexOptions setNoStopwords() {
/*  76 */     this.stopwords = new ArrayList<>(0);
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IndexOptions setTemporary(long expire) {
/*  87 */     this.expire = expire;
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public IndexDefinition getDefinition() {
/*  92 */     return this.definition;
/*     */   }
/*     */   
/*     */   public IndexOptions setDefinition(IndexDefinition definition) {
/*  96 */     this.definition = definition;
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 103 */     if (this.definition != null) {
/* 104 */       this.definition.addParams(args);
/*     */     }
/*     */     
/* 107 */     if ((this.flags & 0x1) == 0) {
/* 108 */       args.add(SearchProtocol.SearchKeyword.NOOFFSETS.name());
/*     */     }
/* 110 */     if ((this.flags & 0x2) == 0) {
/* 111 */       args.add(SearchProtocol.SearchKeyword.NOFIELDS.name());
/*     */     }
/* 113 */     if ((this.flags & 0x8) == 0) {
/* 114 */       args.add(SearchProtocol.SearchKeyword.NOFREQS.name());
/*     */     }
/* 116 */     if (this.expire > 0L) {
/* 117 */       args.add(SearchProtocol.SearchKeyword.TEMPORARY.name());
/* 118 */       args.add(Long.toString(this.expire));
/*     */     } 
/*     */     
/* 121 */     if (this.stopwords != null) {
/* 122 */       args.add(SearchProtocol.SearchKeyword.STOPWORDS.name());
/* 123 */       args.add(Integer.toString(this.stopwords.size()));
/* 124 */       if (!this.stopwords.isEmpty())
/* 125 */         args.addObjects(this.stopwords); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\IndexOptions.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */