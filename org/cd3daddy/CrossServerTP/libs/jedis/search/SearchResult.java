/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SearchResult
/*     */ {
/*     */   private final long totalResults;
/*     */   private final List<Document> documents;
/*     */   
/*     */   private SearchResult(long totalResults, List<Document> documents) {
/*  22 */     this.totalResults = totalResults;
/*  23 */     this.documents = documents;
/*     */   }
/*     */   
/*     */   public long getTotalResults() {
/*  27 */     return this.totalResults;
/*     */   }
/*     */   
/*     */   public List<Document> getDocuments() {
/*  31 */     return Collections.unmodifiableList(this.documents);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  36 */     return getClass().getSimpleName() + "{Total results:" + this.totalResults + ", Documents:" + this.documents + "}";
/*     */   }
/*     */   
/*     */   public static class SearchResultBuilder
/*     */     extends Builder<SearchResult>
/*     */   {
/*     */     private final boolean hasContent;
/*     */     private final boolean hasScores;
/*     */     private final boolean decode;
/*     */     
/*     */     public SearchResultBuilder(boolean hasContent, boolean hasScores, boolean decode) {
/*  47 */       this.hasContent = hasContent;
/*  48 */       this.hasScores = hasScores;
/*  49 */       this.decode = decode;
/*     */     }
/*     */ 
/*     */     
/*     */     public SearchResult build(Object data) {
/*  54 */       List<Object> resp = (List<Object>)data;
/*     */       
/*  56 */       int step = 1;
/*  57 */       int scoreOffset = 0;
/*  58 */       int contentOffset = 1;
/*  59 */       if (this.hasScores) {
/*  60 */         step++;
/*  61 */         scoreOffset = 1;
/*  62 */         contentOffset++;
/*     */       } 
/*  64 */       if (this.hasContent) {
/*  65 */         step++;
/*     */       }
/*     */ 
/*     */       
/*  69 */       long totalResults = ((Long)resp.get(0)).longValue();
/*  70 */       List<Document> documents = new ArrayList<>(resp.size() - 1);
/*     */       int i;
/*  72 */       for (i = 1; i < resp.size(); i += step) {
/*     */         
/*  74 */         String id = (String)BuilderFactory.STRING.build(resp.get(i));
/*  75 */         double score = this.hasScores ? ((Double)BuilderFactory.DOUBLE.build(resp.get(i + scoreOffset))).doubleValue() : 1.0D;
/*  76 */         List<byte[]> fields = this.hasContent ? (List<byte[]>)resp.get(i + contentOffset) : null;
/*     */         
/*  78 */         documents.add(Document.load(id, score, fields, this.decode));
/*     */       } 
/*     */       
/*  81 */       return new SearchResult(totalResults, documents);
/*     */     }
/*     */   }
/*     */   
/*  85 */   public static Builder<SearchResult> SEARCH_RESULT_BUILDER = new Builder<SearchResult>()
/*     */     {
/*     */       private static final String TOTAL_RESULTS_STR = "total_results";
/*     */       
/*     */       private static final String RESULTS_STR = "results";
/*     */       
/*     */       public SearchResult build(Object data) {
/*  92 */         List<KeyValue> list = (List<KeyValue>)data;
/*  93 */         long totalResults = -1L;
/*  94 */         List<Document> results = null;
/*  95 */         for (KeyValue kv : list) {
/*  96 */           String key = (String)BuilderFactory.STRING.build(kv.getKey());
/*  97 */           switch (key) {
/*     */             case "total_results":
/*  99 */               totalResults = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */ 
/*     */ 
/*     */             
/*     */             case "results":
/* 104 */               results = (List<Document>)((List)kv.getValue()).stream().map(Document.SEARCH_DOCUMENT::build).collect(Collectors.toList());
/*     */           } 
/*     */         
/*     */         } 
/* 108 */         return new SearchResult(totalResults, results);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\SearchResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */