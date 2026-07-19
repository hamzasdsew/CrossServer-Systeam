/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*     */ 
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TSCreateParams
/*     */   implements IParams
/*     */ {
/*     */   private Long retentionPeriod;
/*     */   private boolean uncompressed;
/*     */   private boolean compressed;
/*     */   private Long chunkSize;
/*     */   private DuplicatePolicy duplicatePolicy;
/*     */   private Map<String, String> labels;
/*     */   
/*     */   public static TSCreateParams createParams() {
/*  27 */     return new TSCreateParams();
/*     */   }
/*     */   
/*     */   public TSCreateParams retention(long retentionPeriod) {
/*  31 */     this.retentionPeriod = Long.valueOf(retentionPeriod);
/*  32 */     return this;
/*     */   }
/*     */   
/*     */   public TSCreateParams uncompressed() {
/*  36 */     this.uncompressed = true;
/*  37 */     return this;
/*     */   }
/*     */   
/*     */   public TSCreateParams compressed() {
/*  41 */     this.compressed = true;
/*  42 */     return this;
/*     */   }
/*     */   
/*     */   public TSCreateParams chunkSize(long chunkSize) {
/*  46 */     this.chunkSize = Long.valueOf(chunkSize);
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   public TSCreateParams duplicatePolicy(DuplicatePolicy duplicatePolicy) {
/*  51 */     this.duplicatePolicy = duplicatePolicy;
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSCreateParams labels(Map<String, String> labels) {
/*  62 */     this.labels = labels;
/*  63 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSCreateParams label(String label, String value) {
/*  70 */     if (this.labels == null) {
/*  71 */       this.labels = new LinkedHashMap<>();
/*     */     }
/*  73 */     this.labels.put(label, value);
/*  74 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/*  80 */     if (this.retentionPeriod != null) {
/*  81 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.RETENTION).add(Protocol.toByteArray(this.retentionPeriod.longValue()));
/*     */     }
/*     */     
/*  84 */     if (this.uncompressed) {
/*  85 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.ENCODING).add(TimeSeriesProtocol.TimeSeriesKeyword.UNCOMPRESSED);
/*  86 */     } else if (this.compressed) {
/*  87 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.ENCODING).add(TimeSeriesProtocol.TimeSeriesKeyword.COMPRESSED);
/*     */     } 
/*     */     
/*  90 */     if (this.chunkSize != null) {
/*  91 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.CHUNK_SIZE).add(Protocol.toByteArray(this.chunkSize.longValue()));
/*     */     }
/*     */     
/*  94 */     if (this.duplicatePolicy != null) {
/*  95 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.DUPLICATE_POLICY).add(this.duplicatePolicy);
/*     */     }
/*     */     
/*  98 */     if (this.labels != null) {
/*  99 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.LABELS);
/* 100 */       this.labels.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSCreateParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */