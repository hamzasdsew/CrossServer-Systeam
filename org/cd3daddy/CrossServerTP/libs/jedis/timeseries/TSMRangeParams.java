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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TSMRangeParams
/*     */   implements IParams
/*     */ {
/*     */   private Long fromTimestamp;
/*     */   private Long toTimestamp;
/*     */   private boolean latest;
/*     */   private long[] filterByTimestamps;
/*     */   private double[] filterByValues;
/*     */   private boolean withLabels;
/*     */   private String[] selectedLabels;
/*     */   private Integer count;
/*     */   private byte[] align;
/*     */   private AggregationType aggregationType;
/*     */   private long bucketDuration;
/*     */   private byte[] bucketTimestamp;
/*     */   private boolean empty;
/*     */   private String[] filters;
/*     */   private String groupByLabel;
/*     */   private String groupByReduce;
/*     */   
/*     */   public TSMRangeParams(long fromTimestamp, long toTimestamp) {
/*  45 */     this.fromTimestamp = Long.valueOf(fromTimestamp);
/*  46 */     this.toTimestamp = Long.valueOf(toTimestamp);
/*     */   }
/*     */   
/*     */   public static TSMRangeParams multiRangeParams(long fromTimestamp, long toTimestamp) {
/*  50 */     return new TSMRangeParams(fromTimestamp, toTimestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   public TSMRangeParams() {}
/*     */   
/*     */   public static TSMRangeParams multiRangeParams() {
/*  57 */     return new TSMRangeParams();
/*     */   }
/*     */   
/*     */   public TSMRangeParams fromTimestamp(long fromTimestamp) {
/*  61 */     this.fromTimestamp = Long.valueOf(fromTimestamp);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams toTimestamp(long toTimestamp) {
/*  66 */     this.toTimestamp = Long.valueOf(toTimestamp);
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams latest() {
/*  71 */     this.latest = true;
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams filterByTS(long... timestamps) {
/*  76 */     this.filterByTimestamps = timestamps;
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams filterByValues(double min, double max) {
/*  81 */     this.filterByValues = new double[] { min, max };
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams withLabels(boolean withLabels) {
/*  86 */     this.withLabels = withLabels;
/*  87 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams withLabels() {
/*  91 */     return withLabels(true);
/*     */   }
/*     */   
/*     */   public TSMRangeParams selectedLabels(String... labels) {
/*  95 */     this.selectedLabels = labels;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams count(int count) {
/* 100 */     this.count = Integer.valueOf(count);
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   private TSMRangeParams align(byte[] raw) {
/* 105 */     this.align = raw;
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams align(long timestamp) {
/* 113 */     return align(Protocol.toByteArray(timestamp));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams alignStart() {
/* 120 */     return align(TimeSeriesProtocol.MINUS);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams alignEnd() {
/* 127 */     return align(TimeSeriesProtocol.PLUS);
/*     */   }
/*     */   
/*     */   public TSMRangeParams aggregation(AggregationType aggregationType, long bucketDuration) {
/* 131 */     this.aggregationType = aggregationType;
/* 132 */     this.bucketDuration = bucketDuration;
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams bucketTimestamp(String bucketTimestamp) {
/* 140 */     this.bucketTimestamp = SafeEncoder.encode(bucketTimestamp);
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams bucketTimestampLow() {
/* 148 */     this.bucketTimestamp = TimeSeriesProtocol.MINUS;
/* 149 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams bucketTimestampHigh() {
/* 156 */     this.bucketTimestamp = TimeSeriesProtocol.PLUS;
/* 157 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams bucketTimestampMid() {
/* 164 */     this.bucketTimestamp = Protocol.BYTES_TILDE;
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TSMRangeParams empty() {
/* 172 */     this.empty = true;
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams filter(String... filters) {
/* 177 */     this.filters = filters;
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public TSMRangeParams groupBy(String label, String reduce) {
/* 182 */     this.groupByLabel = label;
/* 183 */     this.groupByReduce = reduce;
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 190 */     if (this.filters == null) {
/* 191 */       throw new IllegalArgumentException("FILTER arguments must be set.");
/*     */     }
/*     */     
/* 194 */     if (this.fromTimestamp == null) {
/* 195 */       args.add(TimeSeriesProtocol.MINUS);
/*     */     } else {
/* 197 */       args.add(Protocol.toByteArray(this.fromTimestamp.longValue()));
/*     */     } 
/*     */     
/* 200 */     if (this.toTimestamp == null) {
/* 201 */       args.add(TimeSeriesProtocol.PLUS);
/*     */     } else {
/* 203 */       args.add(Protocol.toByteArray(this.toTimestamp.longValue()));
/*     */     } 
/*     */     
/* 206 */     if (this.latest) {
/* 207 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.LATEST);
/*     */     }
/*     */     
/* 210 */     if (this.filterByTimestamps != null) {
/* 211 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER_BY_TS);
/* 212 */       for (long ts : this.filterByTimestamps) {
/* 213 */         args.add(Protocol.toByteArray(ts));
/*     */       }
/*     */     } 
/*     */     
/* 217 */     if (this.filterByValues != null) {
/* 218 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER_BY_VALUE);
/* 219 */       for (double value : this.filterByValues) {
/* 220 */         args.add(Protocol.toByteArray(value));
/*     */       }
/*     */     } 
/*     */     
/* 224 */     if (this.withLabels) {
/* 225 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.WITHLABELS);
/* 226 */     } else if (this.selectedLabels != null) {
/* 227 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.SELECTED_LABELS);
/* 228 */       for (String label : this.selectedLabels) {
/* 229 */         args.add(label);
/*     */       }
/*     */     } 
/*     */     
/* 233 */     if (this.count != null) {
/* 234 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.COUNT).add(Protocol.toByteArray(this.count.intValue()));
/*     */     }
/*     */     
/* 237 */     if (this.aggregationType != null) {
/*     */       
/* 239 */       if (this.align != null) {
/* 240 */         args.add(TimeSeriesProtocol.TimeSeriesKeyword.ALIGN).add(this.align);
/*     */       }
/*     */       
/* 243 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.AGGREGATION).add(this.aggregationType).add(Protocol.toByteArray(this.bucketDuration));
/*     */       
/* 245 */       if (this.bucketTimestamp != null) {
/* 246 */         args.add(TimeSeriesProtocol.TimeSeriesKeyword.BUCKETTIMESTAMP).add(this.bucketTimestamp);
/*     */       }
/*     */       
/* 249 */       if (this.empty) {
/* 250 */         args.add(TimeSeriesProtocol.TimeSeriesKeyword.EMPTY);
/*     */       }
/*     */     } 
/*     */     
/* 254 */     args.add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER);
/* 255 */     for (String filter : this.filters) {
/* 256 */       args.add(filter);
/*     */     }
/*     */     
/* 259 */     if (this.groupByLabel != null && this.groupByReduce != null)
/* 260 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.GROUPBY).add(this.groupByLabel).add(TimeSeriesProtocol.TimeSeriesKeyword.REDUCE).add(this.groupByReduce); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSMRangeParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */