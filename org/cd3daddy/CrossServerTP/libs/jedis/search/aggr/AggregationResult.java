/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AggregationResult
/*     */ {
/*     */   private final long totalResults;
/*     */   private final List<Map<String, Object>> results;
/*  22 */   private Long cursorId = Long.valueOf(-1L);
/*     */   
/*     */   private AggregationResult(Object resp, long cursorId) {
/*  25 */     this(resp);
/*  26 */     this.cursorId = Long.valueOf(cursorId);
/*     */   }
/*     */   
/*     */   private AggregationResult(Object resp) {
/*  30 */     List<Object> list = (List<Object>)SafeEncoder.encodeObject(resp);
/*     */ 
/*     */     
/*  33 */     this.totalResults = ((Long)list.get(0)).longValue();
/*  34 */     this.results = new ArrayList<>(list.size() - 1);
/*     */     
/*  36 */     for (int i = 1; i < list.size(); i++) {
/*  37 */       List<Object> mapList = (List<Object>)list.get(i);
/*  38 */       Map<String, Object> map = new HashMap<>(mapList.size() / 2, 1.0F);
/*  39 */       for (int j = 0; j < mapList.size(); j += 2) {
/*  40 */         Object r = mapList.get(j);
/*  41 */         if (r instanceof JedisDataException) {
/*  42 */           throw (JedisDataException)r;
/*     */         }
/*  44 */         map.put((String)r, mapList.get(j + 1));
/*     */       } 
/*  46 */       this.results.add(map);
/*     */     } 
/*     */   }
/*     */   
/*     */   private AggregationResult(long totalResults, List<Map<String, Object>> results) {
/*  51 */     this.totalResults = totalResults;
/*  52 */     this.results = results;
/*     */   }
/*     */   
/*     */   private void setCursorId(Long cursorId) {
/*  56 */     this.cursorId = cursorId;
/*     */   }
/*     */   
/*     */   public Long getCursorId() {
/*  60 */     return this.cursorId;
/*     */   }
/*     */   
/*     */   public long getTotalResults() {
/*  64 */     return this.totalResults;
/*     */   }
/*     */   
/*     */   public List<Map<String, Object>> getResults() {
/*  68 */     return Collections.unmodifiableList(this.results);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Row> getRows() {
/*  76 */     return (List<Row>)this.results.stream().map(Row::new).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public Row getRow(int index) {
/*  80 */     return new Row(this.results.get(index));
/*     */   }
/*     */   
/*  83 */   public static final Builder<AggregationResult> SEARCH_AGGREGATION_RESULT = new Builder<AggregationResult>()
/*     */     {
/*     */       private static final String TOTAL_RESULTS_STR = "total_results";
/*     */       
/*     */       private static final String RESULTS_STR = "results";
/*     */       
/*     */       private static final String FIELDS_STR = "extra_attributes";
/*     */ 
/*     */       
/*     */       public AggregationResult build(Object data) {
/*  93 */         List<Long> list = (List)data;
/*     */         
/*  95 */         if (list.get(0) instanceof KeyValue) {
/*  96 */           List<KeyValue> kvList = (List<KeyValue>)data;
/*  97 */           long l = -1L;
/*  98 */           List<Map<String, Object>> list1 = null;
/*  99 */           for (KeyValue kv : kvList) {
/* 100 */             List<List<KeyValue>> resList; String key = (String)BuilderFactory.STRING.build(kv.getKey());
/* 101 */             switch (key) {
/*     */               case "total_results":
/* 103 */                 l = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */               
/*     */               case "results":
/* 106 */                 resList = (List<List<KeyValue>>)kv.getValue();
/* 107 */                 list1 = new ArrayList<>(resList.size());
/* 108 */                 for (List<KeyValue> rikv : resList) {
/* 109 */                   for (KeyValue ikv : rikv) {
/* 110 */                     if ("extra_attributes".equals(BuilderFactory.STRING.build(ikv.getKey()))) {
/* 111 */                       list1.add(BuilderFactory.ENCODED_OBJECT_MAP.build(ikv.getValue()));
/*     */                     }
/*     */                   } 
/*     */                 } 
/*     */             } 
/*     */ 
/*     */           
/*     */           } 
/* 119 */           return new AggregationResult(l, list1);
/*     */         } 
/*     */         
/* 122 */         list = (List)SafeEncoder.encodeObject(data);
/*     */ 
/*     */         
/* 125 */         long totalResults = ((Long)list.get(0)).longValue();
/* 126 */         List<Map<String, Object>> results = new ArrayList<>(list.size() - 1);
/*     */         
/* 128 */         for (int i = 1; i < list.size(); i++) {
/* 129 */           List<Object> mapList = (List<Object>)list.get(i);
/* 130 */           Map<String, Object> map = new HashMap<>(mapList.size() / 2, 1.0F);
/* 131 */           for (int j = 0; j < mapList.size(); j += 2) {
/* 132 */             Object r = mapList.get(j);
/* 133 */             if (r instanceof JedisDataException) {
/* 134 */               throw (JedisDataException)r;
/*     */             }
/* 136 */             map.put((String)r, mapList.get(j + 1));
/*     */           } 
/* 138 */           results.add(map);
/*     */         } 
/* 140 */         return new AggregationResult(totalResults, results);
/*     */       }
/*     */     };
/*     */   
/* 144 */   public static final Builder<AggregationResult> SEARCH_AGGREGATION_RESULT_WITH_CURSOR = new Builder<AggregationResult>()
/*     */     {
/*     */       public AggregationResult build(Object data) {
/* 147 */         List<Object> list = (List<Object>)data;
/*     */         
/* 149 */         AggregationResult r = (AggregationResult)AggregationResult.SEARCH_AGGREGATION_RESULT.build(list.get(0));
/* 150 */         r.setCursorId((Long)list.get(1));
/* 151 */         return r;
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\AggregationResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */