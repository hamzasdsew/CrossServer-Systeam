/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.DoublePrecision;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SearchBuilderFactory
/*     */ {
/*  21 */   public static final Builder<Map<String, Object>> SEARCH_PROFILE_PROFILE = new Builder<Map<String, Object>>()
/*     */     {
/*  23 */       private final String ITERATORS_PROFILE_STR = "Iterators profile";
/*  24 */       private final String CHILD_ITERATORS_STR = "Child iterators";
/*  25 */       private final String RESULT_PROCESSORS_PROFILE_STR = "Result processors profile";
/*     */ 
/*     */       
/*     */       public Map<String, Object> build(Object data) {
/*  29 */         List<Object> list = (List<Object>)SafeEncoder.encodeObject(data);
/*  30 */         Map<String, Object> profileMap = new HashMap<>(list.size(), 1.0F);
/*     */         
/*  32 */         for (Object listObject : list) {
/*  33 */           Object attributeValue; List<Object> attributeList = (List<Object>)listObject;
/*  34 */           String attributeName = (String)attributeList.get(0);
/*     */ 
/*     */           
/*  37 */           if (attributeList.size() == 2) {
/*     */             
/*  39 */             Object value = attributeList.get(1);
/*  40 */             if (attributeName.equals("Iterators profile")) {
/*  41 */               attributeValue = parseIterators(value);
/*  42 */             } else if (attributeName.endsWith(" time")) {
/*  43 */               attributeValue = DoublePrecision.parseEncodedFloatingPointNumber(value);
/*     */             } else {
/*  45 */               attributeValue = value;
/*     */             }
/*     */           
/*  48 */           } else if (attributeList.size() > 2) {
/*     */             
/*  50 */             if (attributeName.equals("Result processors profile")) {
/*  51 */               List<Map<String, Object>> resultProcessorsProfileList = new ArrayList<>(attributeList.size() - 1);
/*  52 */               for (int i = 1; i < attributeList.size(); i++) {
/*  53 */                 resultProcessorsProfileList.add(parseResultProcessors(attributeList.get(i)));
/*     */               }
/*  55 */               attributeValue = resultProcessorsProfileList;
/*     */             } else {
/*  57 */               attributeValue = attributeList.subList(1, attributeList.size());
/*     */             } 
/*     */           } else {
/*     */             
/*  61 */             attributeValue = null;
/*     */           } 
/*     */           
/*  64 */           profileMap.put(attributeName, attributeValue);
/*     */         } 
/*  66 */         return profileMap;
/*     */       }
/*     */       
/*     */       private Map<String, Object> parseResultProcessors(Object data) {
/*  70 */         List<Object> list = (List<Object>)data;
/*  71 */         Map<String, Object> map = new HashMap<>(list.size() / 2, 1.0F);
/*  72 */         for (int i = 0; i < list.size(); i += 2) {
/*  73 */           String key = (String)list.get(i);
/*  74 */           Object value = list.get(i + 1);
/*  75 */           if (key.equals("Time")) {
/*  76 */             value = DoublePrecision.parseEncodedFloatingPointNumber(value);
/*     */           }
/*  78 */           map.put(key, value);
/*     */         } 
/*  80 */         return map;
/*     */       }
/*     */       private Object parseIterators(Object data) {
/*     */         Map<String, Object> iteratorsProfile;
/*  84 */         if (!(data instanceof List)) return data; 
/*  85 */         List<String> iteratorsAttributeList = (List)data;
/*  86 */         int childIteratorsIndex = iteratorsAttributeList.indexOf("Child iterators");
/*     */         
/*  88 */         if (childIteratorsIndex < 0) childIteratorsIndex = iteratorsAttributeList.indexOf("Child iterator");
/*     */ 
/*     */         
/*  91 */         if (childIteratorsIndex < 0) {
/*  92 */           childIteratorsIndex = iteratorsAttributeList.size();
/*  93 */           iteratorsProfile = new HashMap<>(childIteratorsIndex / 2, 1.0F);
/*     */         } else {
/*  95 */           iteratorsProfile = new HashMap<>(1 + childIteratorsIndex / 2, 1.0F);
/*     */         } 
/*     */         
/*  98 */         for (int i = 0; i < childIteratorsIndex; i += 2) {
/*  99 */           String key = iteratorsAttributeList.get(i);
/* 100 */           Object value = iteratorsAttributeList.get(i + 1);
/* 101 */           if (key.equals("Time")) {
/* 102 */             value = DoublePrecision.parseEncodedFloatingPointNumber(value);
/*     */           }
/* 104 */           iteratorsProfile.put(key, value);
/*     */         } 
/*     */         
/* 107 */         if (childIteratorsIndex + 1 < iteratorsAttributeList.size()) {
/* 108 */           List<Object> childIteratorsList = new ArrayList(iteratorsAttributeList.size() - childIteratorsIndex - 1);
/* 109 */           for (int j = childIteratorsIndex + 1; j < iteratorsAttributeList.size(); j++) {
/* 110 */             childIteratorsList.add(parseIterators(iteratorsAttributeList.get(j)));
/*     */           }
/* 112 */           iteratorsProfile.put("Child iterators", childIteratorsList);
/*     */         } 
/* 114 */         return iteratorsProfile;
/*     */       }
/*     */     };
/*     */   
/* 118 */   public static final Builder<Map<String, List<String>>> SEARCH_SYNONYM_GROUPS = new Builder<Map<String, List<String>>>()
/*     */     {
/*     */       public Map<String, List<String>> build(Object data) {
/* 121 */         List list = (List)data;
/* 122 */         if (list.isEmpty()) return Collections.emptyMap();
/*     */         
/* 124 */         if (list.get(0) instanceof KeyValue) {
/* 125 */           return (Map<String, List<String>>)((List)data).stream().collect(Collectors.toMap(kv -> (String)BuilderFactory.STRING.build(kv.getKey()), kv -> (List)BuilderFactory.STRING_LIST.build(kv.getValue())));
/*     */         }
/*     */ 
/*     */         
/* 129 */         Map<String, List<String>> dump = new HashMap<>(list.size() / 2, 1.0F);
/* 130 */         for (int i = 0; i < list.size(); i += 2) {
/* 131 */           dump.put(BuilderFactory.STRING.build(list.get(i)), BuilderFactory.STRING_LIST.build(list.get(i + 1)));
/*     */         }
/* 133 */         return dump;
/*     */       }
/*     */     };
/*     */   
/* 137 */   public static final Builder<Map<String, Map<String, Double>>> SEARCH_SPELLCHECK_RESPONSE = new Builder<Map<String, Map<String, Double>>>()
/*     */     {
/*     */       private static final String TERM = "TERM";
/*     */       
/*     */       private static final String RESULTS = "results";
/*     */ 
/*     */       
/*     */       public Map<String, Map<String, Double>> build(Object data) {
/* 145 */         List<KeyValue> rawDataList = (List)data;
/* 146 */         if (rawDataList.isEmpty()) return Collections.emptyMap();
/*     */         
/* 148 */         if (rawDataList.get(0) instanceof KeyValue) {
/* 149 */           KeyValue rawData = rawDataList.get(0);
/* 150 */           String header = (String)BuilderFactory.STRING.build(rawData.getKey());
/* 151 */           if (!"results".equals(header)) {
/* 152 */             throw new IllegalStateException("Unrecognized header: " + header);
/*     */           }
/*     */           
/* 155 */           return (Map<String, Map<String, Double>>)((List)rawData.getValue()).stream().collect(Collectors.toMap(rawTerm -> (String)BuilderFactory.STRING.build(rawTerm.getKey()), rawTerm -> (Map)((List)rawTerm.getValue()).stream().collect(Collectors.toMap((), ())), (x, y) -> x, LinkedHashMap::new));
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 163 */         Map<String, Map<String, Double>> returnTerms = new LinkedHashMap<>(rawDataList.size());
/*     */         
/* 165 */         for (KeyValue rawData : rawDataList) {
/* 166 */           List<Object> rawElements = (List<Object>)rawData;
/*     */           
/* 168 */           String header = (String)BuilderFactory.STRING.build(rawElements.get(0));
/* 169 */           if (!"TERM".equals(header)) {
/* 170 */             throw new IllegalStateException("Unrecognized header: " + header);
/*     */           }
/* 172 */           String term = (String)BuilderFactory.STRING.build(rawElements.get(1));
/*     */           
/* 174 */           List<List<Object>> list = (List<List<Object>>)rawElements.get(2);
/* 175 */           Map<String, Double> entries = new LinkedHashMap<>(list.size());
/* 176 */           list.forEach(entry -> (Double)entries.put(BuilderFactory.STRING.build(entry.get(1)), BuilderFactory.DOUBLE.build(entry.get(0))));
/*     */           
/* 178 */           returnTerms.put(term, entries);
/*     */         } 
/* 180 */         return returnTerms;
/*     */       }
/*     */     };
/*     */   
/*     */   private SearchBuilderFactory() {
/* 185 */     throw new InstantiationError("Must not instantiate this class");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\SearchBuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */