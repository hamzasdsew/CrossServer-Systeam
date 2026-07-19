/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TSAlterParams
/*    */   implements IParams
/*    */ {
/*    */   private Long retentionPeriod;
/*    */   private Long chunkSize;
/*    */   private DuplicatePolicy duplicatePolicy;
/*    */   private Map<String, String> labels;
/*    */   
/*    */   public static TSAlterParams alterParams() {
/* 26 */     return new TSAlterParams();
/*    */   }
/*    */   
/*    */   public TSAlterParams retention(long retentionPeriod) {
/* 30 */     this.retentionPeriod = Long.valueOf(retentionPeriod);
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public TSAlterParams chunkSize(long chunkSize) {
/* 35 */     this.chunkSize = Long.valueOf(chunkSize);
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public TSAlterParams duplicatePolicy(DuplicatePolicy duplicatePolicy) {
/* 40 */     this.duplicatePolicy = duplicatePolicy;
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public TSAlterParams labels(Map<String, String> labels) {
/* 45 */     this.labels = labels;
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public TSAlterParams label(String label, String value) {
/* 50 */     if (this.labels == null) {
/* 51 */       this.labels = new LinkedHashMap<>();
/*    */     }
/* 53 */     this.labels.put(label, value);
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public TSAlterParams labelsReset() {
/* 58 */     return labels(Collections.emptyMap());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 64 */     if (this.retentionPeriod != null) {
/* 65 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.RETENTION).add(Protocol.toByteArray(this.retentionPeriod.longValue()));
/*    */     }
/*    */     
/* 68 */     if (this.chunkSize != null) {
/* 69 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.CHUNK_SIZE).add(Protocol.toByteArray(this.chunkSize.longValue()));
/*    */     }
/*    */     
/* 72 */     if (this.duplicatePolicy != null) {
/* 73 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.DUPLICATE_POLICY).add(this.duplicatePolicy);
/*    */     }
/*    */     
/* 76 */     if (this.labels != null) {
/* 77 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.LABELS);
/* 78 */       this.labels.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSAlterParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */