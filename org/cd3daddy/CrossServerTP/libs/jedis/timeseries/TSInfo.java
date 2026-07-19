/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.DoublePrecision;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TSInfo
/*     */ {
/*     */   private static final String DUPLICATE_POLICY_PROPERTY = "duplicatePolicy";
/*     */   private static final String LABELS_PROPERTY = "labels";
/*     */   private static final String RULES_PROPERTY = "rules";
/*     */   private static final String CHUNKS_PROPERTY = "Chunks";
/*     */   private static final String CHUNKS_BYTES_PER_SAMPLE_PROPERTY = "bytesPerSample";
/*     */   private final Map<String, Object> properties;
/*     */   private final Map<String, String> labels;
/*     */   private final Map<String, Rule> rules;
/*     */   private final List<Map<String, Object>> chunks;
/*     */   
/*     */   private TSInfo(Map<String, Object> properties, Map<String, String> labels, Map<String, Rule> rules, List<Map<String, Object>> chunks) {
/*  29 */     this.properties = properties;
/*  30 */     this.labels = labels;
/*  31 */     this.rules = rules;
/*  32 */     this.chunks = chunks;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getProperties() {
/*  36 */     return this.properties;
/*     */   }
/*     */   
/*     */   public Object getProperty(String property) {
/*  40 */     return this.properties.get(property);
/*     */   }
/*     */   
/*     */   public Long getIntegerProperty(String property) {
/*  44 */     return (Long)this.properties.get(property);
/*     */   }
/*     */   
/*     */   public Map<String, String> getLabels() {
/*  48 */     return this.labels;
/*     */   }
/*     */   
/*     */   public String getLabel(String label) {
/*  52 */     return this.labels.get(label);
/*     */   }
/*     */   
/*     */   public Map<String, Rule> getRules() {
/*  56 */     return this.rules;
/*     */   }
/*     */   
/*     */   public Rule getRule(String rule) {
/*  60 */     return this.rules.get(rule);
/*     */   }
/*     */   
/*     */   public List<Map<String, Object>> getChunks() {
/*  64 */     return this.chunks;
/*     */   }
/*     */   
/*  67 */   public static Builder<TSInfo> TIMESERIES_INFO = new Builder<TSInfo>()
/*     */     {
/*     */       public TSInfo build(Object data) {
/*  70 */         List<Object> list = (List<Object>)data;
/*  71 */         Map<String, Object> properties = new HashMap<>();
/*  72 */         Map<String, String> labels = null;
/*  73 */         Map<String, TSInfo.Rule> rules = null;
/*  74 */         List<Map<String, Object>> chunks = null;
/*     */         
/*  76 */         for (int i = 0; i < list.size(); i += 2) {
/*  77 */           String prop = SafeEncoder.encode((byte[])list.get(i));
/*  78 */           Object object = list.get(i + 1);
/*  79 */           if (object instanceof List) {
/*  80 */             List<List<Object>> list2; List<Map<String, Object>> list1; List<Object> rulesDataList; List<List<Object>> rulesValueList; List<List<Object>> list3; List<Map<String, Object>> chunksValueList; switch (prop) {
/*     */               case "labels":
/*  82 */                 labels = (Map<String, String>)BuilderFactory.STRING_MAP_FROM_PAIRS.build(object);
/*  83 */                 object = labels;
/*     */                 break;
/*     */               case "rules":
/*  86 */                 rulesDataList = (List)object;
/*  87 */                 rulesValueList = new ArrayList<>(rulesDataList.size());
/*  88 */                 rules = new HashMap<>(rulesDataList.size());
/*  89 */                 for (Object ruleData : rulesDataList) {
/*  90 */                   List<Object> encodedRule = (List<Object>)SafeEncoder.encodeObject(ruleData);
/*  91 */                   rulesValueList.add(encodedRule);
/*  92 */                   rules.put((String)encodedRule.get(0), new TSInfo.Rule((String)encodedRule.get(0), ((Long)encodedRule.get(1)).longValue(), 
/*  93 */                         AggregationType.safeValueOf((String)encodedRule.get(2)), ((Long)encodedRule.get(3)).longValue()));
/*     */                 } 
/*  95 */                 list2 = rulesValueList;
/*     */                 break;
/*     */               case "Chunks":
/*  98 */                 list3 = list2;
/*  99 */                 chunksValueList = new ArrayList<>(list3.size());
/* 100 */                 chunks = new ArrayList<>(list3.size());
/* 101 */                 for (Object<Object> chunkData : list3) {
/* 102 */                   Map<String, Object> chunk = (Map<String, Object>)BuilderFactory.ENCODED_OBJECT_MAP.build(chunkData);
/* 103 */                   chunksValueList.add(new HashMap<>(chunk));
/* 104 */                   if (chunk.containsKey("bytesPerSample")) {
/* 105 */                     chunk.put("bytesPerSample", 
/* 106 */                         DoublePrecision.parseEncodedFloatingPointNumber(chunk.get("bytesPerSample")));
/*     */                   }
/* 108 */                   chunks.add(chunk);
/*     */                 } 
/* 110 */                 list1 = chunksValueList;
/*     */                 break;
/*     */               default:
/* 113 */                 object = SafeEncoder.encodeObject(list1);
/*     */                 break;
/*     */             } 
/* 116 */           } else if (object instanceof byte[]) {
/* 117 */             object = SafeEncoder.encode((byte[])object);
/* 118 */             if ("duplicatePolicy".equals(prop)) {
/*     */               try {
/* 120 */                 object = DuplicatePolicy.valueOf(((String)object).toUpperCase());
/* 121 */               } catch (Exception exception) {}
/*     */             }
/*     */           } 
/* 124 */           properties.put(prop, object);
/*     */         } 
/*     */         
/* 127 */         return new TSInfo(properties, labels, rules, chunks);
/*     */       }
/*     */     };
/*     */   
/* 131 */   public static Builder<TSInfo> TIMESERIES_INFO_RESP3 = new Builder<TSInfo>()
/*     */     {
/*     */       public TSInfo build(Object data) {
/* 134 */         List<KeyValue> list = (List<KeyValue>)data;
/* 135 */         Map<String, Object> properties = new HashMap<>();
/* 136 */         Map<String, String> labels = null;
/* 137 */         Map<String, TSInfo.Rule> rules = null;
/* 138 */         List<Map<String, Object>> chunks = null;
/*     */         
/* 140 */         for (KeyValue propertyValue : list) {
/* 141 */           String prop = (String)BuilderFactory.STRING.build(propertyValue.getKey());
/* 142 */           Object object = propertyValue.getValue();
/* 143 */           if (object instanceof List) {
/* 144 */             Map<String, List<Object>> map1; List<Map<String, Object>> list1; List<KeyValue> rulesDataList; Map<String, List<Object>> rulesValueMap; List<List<KeyValue>> chunksDataList; List<Map<String, Object>> chunksValueList; switch (prop) {
/*     */               case "labels":
/* 146 */                 labels = (Map<String, String>)BuilderFactory.STRING_MAP.build(object);
/* 147 */                 object = labels;
/*     */                 break;
/*     */               case "rules":
/* 150 */                 rulesDataList = (List)object;
/* 151 */                 rulesValueMap = new HashMap<>(rulesDataList.size(), 1.0F);
/* 152 */                 rules = new HashMap<>(rulesDataList.size());
/* 153 */                 for (KeyValue rkv : rulesDataList) {
/* 154 */                   String ruleName = (String)BuilderFactory.STRING.build(rkv.getKey());
/* 155 */                   List<Object> ruleValueList = (List<Object>)BuilderFactory.ENCODED_OBJECT_LIST.build(rkv.getValue());
/* 156 */                   rulesValueMap.put(ruleName, ruleValueList);
/* 157 */                   rules.put(ruleName, new TSInfo.Rule(ruleName, ruleValueList));
/*     */                 } 
/* 159 */                 map1 = rulesValueMap;
/*     */                 break;
/*     */               case "Chunks":
/* 162 */                 chunksDataList = (List)map1;
/* 163 */                 chunksValueList = new ArrayList<>(chunksDataList.size());
/* 164 */                 chunks = new ArrayList<>(chunksDataList.size());
/* 165 */                 for (List<KeyValue> chunkDataAsList : chunksDataList) {
/*     */                   
/* 167 */                   Map<String, Object> chunk = (Map<String, Object>)chunkDataAsList.stream().collect(Collectors.toMap(kv -> (String)BuilderFactory.STRING.build(kv.getKey()), kv -> BuilderFactory.ENCODED_OBJECT.build(kv.getValue())));
/*     */                   
/* 169 */                   chunksValueList.add(chunk);
/* 170 */                   chunks.add(chunk);
/*     */                 } 
/* 172 */                 list1 = chunksValueList;
/*     */                 break;
/*     */               default:
/* 175 */                 object = SafeEncoder.encodeObject(list1);
/*     */                 break;
/*     */             } 
/* 178 */           } else if (object instanceof byte[]) {
/* 179 */             object = BuilderFactory.STRING.build(object);
/* 180 */             if ("duplicatePolicy".equals(prop)) {
/*     */               try {
/* 182 */                 object = DuplicatePolicy.valueOf(((String)object).toUpperCase());
/* 183 */               } catch (Exception exception) {}
/*     */             }
/*     */           } 
/* 186 */           properties.put(prop, object);
/*     */         } 
/*     */         
/* 189 */         return new TSInfo(properties, labels, rules, chunks);
/*     */       }
/*     */     };
/*     */   
/*     */   public static class Rule
/*     */   {
/*     */     private final String compactionKey;
/*     */     private final long bucketDuration;
/*     */     private final AggregationType aggregator;
/*     */     private final long alignmentTimestamp;
/*     */     
/*     */     private Rule(String compaction, List<Object> encodedValues) {
/* 201 */       this(compaction, ((Long)encodedValues.get(0)).longValue(), 
/* 202 */           AggregationType.safeValueOf((String)encodedValues.get(1)), ((Long)encodedValues
/* 203 */           .get(2)).longValue());
/*     */     }
/*     */     
/*     */     private Rule(String compaction, long bucket, AggregationType aggregation, long alignment) {
/* 207 */       this.compactionKey = compaction;
/* 208 */       this.bucketDuration = bucket;
/* 209 */       this.aggregator = aggregation;
/* 210 */       this.alignmentTimestamp = alignment;
/*     */     }
/*     */     
/*     */     public String getCompactionKey() {
/* 214 */       return this.compactionKey;
/*     */     }
/*     */     
/*     */     public long getBucketDuration() {
/* 218 */       return this.bucketDuration;
/*     */     }
/*     */     
/*     */     public AggregationType getAggregator() {
/* 222 */       return this.aggregator;
/*     */     }
/*     */     
/*     */     public long getAlignmentTimestamp() {
/* 226 */       return this.alignmentTimestamp;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */