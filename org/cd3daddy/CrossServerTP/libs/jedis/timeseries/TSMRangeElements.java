/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ public class TSMRangeElements
/*    */   extends KeyValue<String, List<TSElement>> {
/*    */   private final Map<String, String> labels;
/*    */   private final List<AggregationType> aggregators;
/*    */   private final List<String> reducers;
/*    */   private final List<String> sources;
/*    */   
/*    */   public TSMRangeElements(String key, Map<String, String> labels, List<TSElement> value) {
/* 15 */     super(key, value);
/* 16 */     this.labels = labels;
/* 17 */     this.aggregators = null;
/* 18 */     this.reducers = null;
/* 19 */     this.sources = null;
/*    */   }
/*    */   
/*    */   public TSMRangeElements(String key, Map<String, String> labels, List<AggregationType> aggregators, List<TSElement> value) {
/* 23 */     super(key, value);
/* 24 */     this.labels = labels;
/* 25 */     this.aggregators = aggregators;
/* 26 */     this.reducers = null;
/* 27 */     this.sources = null;
/*    */   }
/*    */   
/*    */   public TSMRangeElements(String key, Map<String, String> labels, List<String> reducers, List<String> sources, List<TSElement> value) {
/* 31 */     super(key, value);
/* 32 */     this.labels = labels;
/* 33 */     this.aggregators = null;
/* 34 */     this.reducers = reducers;
/* 35 */     this.sources = sources;
/*    */   }
/*    */   
/*    */   public Map<String, String> getLabels() {
/* 39 */     return this.labels;
/*    */   }
/*    */   
/*    */   public List<AggregationType> getAggregators() {
/* 43 */     return this.aggregators;
/*    */   }
/*    */   
/*    */   public List<String> getReducers() {
/* 47 */     return this.reducers;
/*    */   }
/*    */   
/*    */   public List<String> getSources() {
/* 51 */     return this.sources;
/*    */   }
/*    */   
/*    */   public List<TSElement> getElements() {
/* 55 */     return (List<TSElement>)getValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     StringBuilder sb = (new StringBuilder()).append(getClass().getSimpleName()).append("{key=").append((String)getKey()).append(", labels=").append(this.labels);
/* 62 */     if (this.aggregators != null) {
/* 63 */       sb.append(", aggregators=").append(this.aggregators);
/*    */     }
/* 65 */     if (this.reducers != null && this.sources != null) {
/* 66 */       sb.append(", reducers").append(this.reducers).append(", sources").append(this.sources);
/*    */     }
/* 68 */     return sb.append(", elements=").append(getElements()).append('}').toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSMRangeElements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */