/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
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
/*     */ public class TSRangeParams
/*     */   implements IParams
/*     */ {
/*     */   private Long fromTimestamp;
/*     */   private Long toTimestamp;
/*     */   private boolean latest;
/*     */   private long[] filterByTimestamps;
/*     */   private double[] filterByValues;
/*     */   private Integer count;
/*     */   private byte[] align;
/*     */   private AggregationType aggregationType;
/*     */   private long bucketDuration;
/*     */   private byte[] bucketTimestamp;
/*     */   private boolean empty;
/*     */   
/*     */   public TSRangeParams(long fromTimestamp, long toTimestamp) {
/*  37 */     this.fromTimestamp = Long.valueOf(fromTimestamp);
/*  38 */     this.toTimestamp = Long.valueOf(toTimestamp);
/*     */   }
/*     */   
/*     */   public static TSRangeParams rangeParams(long fromTimestamp, long toTimestamp) {
/*  42 */     return new TSRangeParams(fromTimestamp, toTimestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   public TSRangeParams() {}
/*     */   
/*     */   public static TSRangeParams rangeParams() {
/*  49 */     return new TSRangeParams();
/*     */   }
/*     */   
/*     */   public TSRangeParams fromTimestamp(long fromTimestamp) {
/*  53 */     this.fromTimestamp = Long.valueOf(fromTimestamp);
/*  54 */     return this;
/*     */   }
/*     */   
/*     */   public TSRangeParams toTimestamp(long toTimestamp) {
/*  58 */     this.toTimestamp = Long.valueOf(toTimestamp);
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public TSRangeParams latest() {
/*  63 */     this.latest = true;
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public TSRangeParams filterByTS(long... timestamps) {
/*  68 */     this.filterByTimestamps = timestamps;
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public TSRangeParams filterByValues(double min, double max) {
/*  73 */     this.filterByValues = new double[] { min, max };
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public TSRangeParams count(int count) {
/*  78 */     this.count = Integer.valueOf(count);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   private TSRangeParams align(byte[] raw) {
/*  83 */     this.align = raw;
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams align(long timestamp) {
/*  91 */     return align(Protocol.toByteArray(timestamp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams alignStart() {
/*  98 */     return align(TimeSeriesProtocol.MINUS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams alignEnd() {
/* 105 */     return align(TimeSeriesProtocol.PLUS);
/*     */   }
/*     */   
/*     */   public TSRangeParams aggregation(AggregationType aggregationType, long bucketDuration) {
/* 109 */     this.aggregationType = aggregationType;
/* 110 */     this.bucketDuration = bucketDuration;
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams bucketTimestamp(String bucketTimestamp) {
/* 118 */     this.bucketTimestamp = SafeEncoder.encode(bucketTimestamp);
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams bucketTimestampLow() {
/* 126 */     this.bucketTimestamp = TimeSeriesProtocol.MINUS;
/* 127 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams bucketTimestampHigh() {
/* 134 */     this.bucketTimestamp = TimeSeriesProtocol.PLUS;
/* 135 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams bucketTimestampMid() {
/* 142 */     this.bucketTimestamp = Protocol.BYTES_TILDE;
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSRangeParams empty() {
/* 150 */     this.empty = true;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 157 */     if (this.fromTimestamp == null) {
/* 158 */       args.add(TimeSeriesProtocol.MINUS);
/*     */     } else {
/* 160 */       args.add(Protocol.toByteArray(this.fromTimestamp.longValue()));
/*     */     } 
/*     */     
/* 163 */     if (this.toTimestamp == null) {
/* 164 */       args.add(TimeSeriesProtocol.PLUS);
/*     */     } else {
/* 166 */       args.add(Protocol.toByteArray(this.toTimestamp.longValue()));
/*     */     } 
/*     */     
/* 169 */     if (this.latest) {
/* 170 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.LATEST);
/*     */     }
/*     */     
/* 173 */     if (this.filterByTimestamps != null) {
/* 174 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER_BY_TS);
/* 175 */       for (long ts : this.filterByTimestamps) {
/* 176 */         args.add(Protocol.toByteArray(ts));
/*     */       }
/*     */     } 
/*     */     
/* 180 */     if (this.filterByValues != null) {
/* 181 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER_BY_VALUE);
/* 182 */       for (double value : this.filterByValues) {
/* 183 */         args.add(Protocol.toByteArray(value));
/*     */       }
/*     */     } 
/*     */     
/* 187 */     if (this.count != null) {
/* 188 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.COUNT).add(Protocol.toByteArray(this.count.intValue()));
/*     */     }
/*     */     
/* 191 */     if (this.aggregationType != null) {
/*     */       
/* 193 */       if (this.align != null) {
/* 194 */         args.add(TimeSeriesProtocol.TimeSeriesKeyword.ALIGN).add(this.align);
/*     */       }
/*     */       
/* 197 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.AGGREGATION).add(this.aggregationType).add(Protocol.toByteArray(this.bucketDuration));
/*     */       
/* 199 */       if (this.bucketTimestamp != null) {
/* 200 */         args.add(TimeSeriesProtocol.TimeSeriesKeyword.BUCKETTIMESTAMP).add(this.bucketTimestamp);
/*     */       }
/*     */       
/* 203 */       if (this.empty)
/* 204 */         args.add(TimeSeriesProtocol.TimeSeriesKeyword.EMPTY); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSRangeParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */