/*      */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.stream.Collectors;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.GeoRadiusResponse;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.LCSMatchResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamEntry;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*      */ 
/*      */ public final class BuilderFactory {
/*   18 */   public static final Builder<Object> RAW_OBJECT = new Builder()
/*      */     {
/*      */       public Object build(Object data) {
/*   21 */         return data;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*   26 */         return "Object";
/*      */       }
/*      */     };
/*      */   
/*   30 */   public static final Builder<List<Object>> RAW_OBJECT_LIST = new Builder<List<Object>>()
/*      */     {
/*      */       public List<Object> build(Object data) {
/*   33 */         return (List<Object>)data;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*   38 */         return "List<Object>";
/*      */       }
/*      */     };
/*      */   
/*   42 */   public static final Builder<Object> ENCODED_OBJECT = new Builder()
/*      */     {
/*      */       public Object build(Object data) {
/*   45 */         return SafeEncoder.encodeObject(data);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*   50 */         return "Object";
/*      */       }
/*      */     };
/*      */   
/*   54 */   public static final Builder<List<Object>> ENCODED_OBJECT_LIST = new Builder<List<Object>>()
/*      */     {
/*      */       public List<Object> build(Object data) {
/*   57 */         return (List<Object>)SafeEncoder.encodeObject(data);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*   62 */         return "List<Object>";
/*      */       }
/*      */     };
/*      */   
/*   66 */   public static final Builder<Long> LONG = new Builder<Long>()
/*      */     {
/*      */       public Long build(Object data) {
/*   69 */         return (Long)data;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*   74 */         return "Long";
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*   79 */   public static final Builder<List<Long>> LONG_LIST = new Builder<List<Long>>()
/*      */     {
/*      */       public List<Long> build(Object data)
/*      */       {
/*   83 */         if (null == data) {
/*   84 */           return null;
/*      */         }
/*   86 */         return (List<Long>)data;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*   91 */         return "List<Long>";
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*   96 */   public static final Builder<Double> DOUBLE = new Builder<Double>()
/*      */     {
/*      */       public Double build(Object data) {
/*   99 */         if (data == null) return null; 
/*  100 */         if (data instanceof Double) return (Double)data; 
/*  101 */         return DoublePrecision.parseFloatingPointNumber(BuilderFactory.STRING.build(data));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  106 */         return "Double";
/*      */       }
/*      */     };
/*      */   
/*  110 */   public static final Builder<List<Double>> DOUBLE_LIST = new Builder<List<Double>>()
/*      */     {
/*      */       public List<Double> build(Object data)
/*      */       {
/*  114 */         if (null == data) return null; 
/*  115 */         return (List<Double>)((List)data).stream().map(BuilderFactory.DOUBLE::build).collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  120 */         return "List<Double>";
/*      */       }
/*      */     };
/*      */   
/*  124 */   public static final Builder<Boolean> BOOLEAN = new Builder<Boolean>()
/*      */     {
/*      */       public Boolean build(Object data) {
/*  127 */         if (data == null) return null; 
/*  128 */         if (data instanceof Boolean) return (Boolean)data; 
/*  129 */         return Boolean.valueOf((((Long)data).longValue() == 1L));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  134 */         return "Boolean";
/*      */       }
/*      */     };
/*      */   
/*  138 */   public static final Builder<List<Boolean>> BOOLEAN_LIST = new Builder<List<Boolean>>()
/*      */     {
/*      */       public List<Boolean> build(Object data)
/*      */       {
/*  142 */         if (null == data) return null; 
/*  143 */         return (List<Boolean>)((List)data).stream().map(BuilderFactory.BOOLEAN::build).collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  148 */         return "List<Boolean>";
/*      */       }
/*      */     };
/*      */   
/*  152 */   public static final Builder<List<Boolean>> BOOLEAN_WITH_ERROR_LIST = new Builder<List<Boolean>>()
/*      */     {
/*      */       public List<Boolean> build(Object data)
/*      */       {
/*  156 */         if (null == data) return null; 
/*  157 */         return (List<Boolean>)((List)data).stream()
/*      */           
/*  159 */           .map(val -> (val instanceof org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException) ? null : BuilderFactory.BOOLEAN.build(val))
/*  160 */           .collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  165 */         return "List<Boolean>";
/*      */       }
/*      */     };
/*      */   
/*  169 */   public static final Builder<byte[]> BINARY = new Builder<byte[]>()
/*      */     {
/*      */       public byte[] build(Object data) {
/*  172 */         return (byte[])data;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  177 */         return "byte[]";
/*      */       }
/*      */     };
/*      */   
/*  181 */   public static final Builder<List<byte[]>> BINARY_LIST = new Builder<List<byte[]>>()
/*      */     {
/*      */       public List<byte[]> build(Object data)
/*      */       {
/*  185 */         return (List<byte[]>)data;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  190 */         return "List<byte[]>";
/*      */       }
/*      */     };
/*      */   
/*  194 */   public static final Builder<Set<byte[]>> BINARY_SET = new Builder<Set<byte[]>>()
/*      */     {
/*      */       public Set<byte[]> build(Object data)
/*      */       {
/*  198 */         if (null == data) {
/*  199 */           return null;
/*      */         }
/*  201 */         List<byte[]> l = BuilderFactory.BINARY_LIST.build(data);
/*  202 */         return (Set)BuilderFactory.SetFromList.of((List)l);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  207 */         return "Set<byte[]>";
/*      */       }
/*      */     };
/*      */   
/*  211 */   public static final Builder<List<Map.Entry<byte[], byte[]>>> BINARY_PAIR_LIST = new Builder<List<Map.Entry<byte[], byte[]>>>()
/*      */     {
/*      */       
/*      */       public List<Map.Entry<byte[], byte[]>> build(Object data)
/*      */       {
/*  216 */         List<byte[]> flatHash = (List<byte[]>)data;
/*  217 */         List<Map.Entry<byte[], byte[]>> pairList = new ArrayList<>();
/*  218 */         Iterator<byte[]> iterator = (Iterator)flatHash.iterator();
/*  219 */         while (iterator.hasNext()) {
/*  220 */           pairList.add((Map.Entry)new AbstractMap.SimpleEntry<>(iterator.next(), iterator.next()));
/*      */         }
/*      */         
/*  223 */         return pairList;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  228 */         return "List<Map.Entry<byte[], byte[]>>";
/*      */       }
/*      */     };
/*      */   
/*  232 */   public static final Builder<List<Map.Entry<byte[], byte[]>>> BINARY_PAIR_LIST_FROM_PAIRS = new Builder<List<Map.Entry<byte[], byte[]>>>()
/*      */     {
/*      */       
/*      */       public List<Map.Entry<byte[], byte[]>> build(Object data)
/*      */       {
/*  237 */         List<Object> list = (List<Object>)data;
/*  238 */         List<Map.Entry<byte[], byte[]>> pairList = new ArrayList<>();
/*  239 */         for (Object object : list) {
/*  240 */           List<byte[]> flat = (List<byte[]>)object;
/*  241 */           pairList.add((Map.Entry)new AbstractMap.SimpleEntry<>(flat.get(0), flat.get(1)));
/*      */         } 
/*      */         
/*  244 */         return pairList;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  249 */         return "List<Map.Entry<byte[], byte[]>>";
/*      */       }
/*      */     };
/*      */   
/*  253 */   public static final Builder<String> STRING = new Builder<String>()
/*      */     {
/*      */       public String build(Object data) {
/*  256 */         return (data == null) ? null : SafeEncoder.encode((byte[])data);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  261 */         return "String";
/*      */       }
/*      */     };
/*      */   
/*  265 */   public static final Builder<List<String>> STRING_LIST = new Builder<List<String>>()
/*      */     {
/*      */       public List<String> build(Object data)
/*      */       {
/*  269 */         if (null == data) return null; 
/*  270 */         return (List<String>)((List)data).stream().map(BuilderFactory.STRING::build).collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  275 */         return "List<String>";
/*      */       }
/*      */     };
/*      */   
/*  279 */   public static final Builder<Set<String>> STRING_SET = new Builder<Set<String>>()
/*      */     {
/*      */       public Set<String> build(Object data)
/*      */       {
/*  283 */         if (null == data) return null; 
/*  284 */         return (Set<String>)((List)data).stream().map(BuilderFactory.STRING::build).collect(Collectors.toSet());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  289 */         return "Set<String>";
/*      */       }
/*      */     };
/*      */   
/*  293 */   public static final Builder<Map<byte[], byte[]>> BINARY_MAP = new Builder<Map<byte[], byte[]>>()
/*      */     {
/*      */       public Map<byte[], byte[]> build(Object data)
/*      */       {
/*  297 */         List<Object> list = (List<Object>)data;
/*  298 */         if (list.isEmpty()) return (Map)Collections.emptyMap();
/*      */         
/*  300 */         if (list.get(0) instanceof KeyValue) {
/*  301 */           JedisByteHashMap jedisByteHashMap1 = new JedisByteHashMap();
/*  302 */           Iterator<KeyValue> iterator1 = list.iterator();
/*  303 */           while (iterator1.hasNext()) {
/*  304 */             KeyValue kv = iterator1.next();
/*  305 */             jedisByteHashMap1.put(BuilderFactory.BINARY.build(kv.getKey()), BuilderFactory.BINARY.build(kv.getValue()));
/*      */           } 
/*  307 */           return (Map<byte[], byte[]>)jedisByteHashMap1;
/*      */         } 
/*  309 */         JedisByteHashMap jedisByteHashMap = new JedisByteHashMap();
/*  310 */         Iterator iterator = list.iterator();
/*  311 */         while (iterator.hasNext()) {
/*  312 */           jedisByteHashMap.put(BuilderFactory.BINARY.build(iterator.next()), BuilderFactory.BINARY.build(iterator.next()));
/*      */         }
/*  314 */         return (Map<byte[], byte[]>)jedisByteHashMap;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/*  320 */         return "Map<byte[], byte[]>";
/*      */       }
/*      */     };
/*      */   
/*  324 */   public static final Builder<Map<String, String>> STRING_MAP = new Builder<Map<String, String>>()
/*      */     {
/*      */       public Map<String, String> build(Object data)
/*      */       {
/*  328 */         List<Object> list = (List<Object>)data;
/*  329 */         if (list.isEmpty()) return Collections.emptyMap();
/*      */         
/*  331 */         if (list.get(0) instanceof KeyValue) {
/*  332 */           Map<String, String> map1 = new HashMap<>(list.size(), 1.0F);
/*  333 */           Iterator<KeyValue> iterator1 = list.iterator();
/*  334 */           while (iterator1.hasNext()) {
/*  335 */             KeyValue kv = iterator1.next();
/*  336 */             map1.put(BuilderFactory.STRING.build(kv.getKey()), BuilderFactory.STRING.build(kv.getValue()));
/*      */           } 
/*  338 */           return map1;
/*      */         } 
/*  340 */         Map<String, String> map = new HashMap<>(list.size() / 2, 1.0F);
/*  341 */         Iterator iterator = list.iterator();
/*  342 */         while (iterator.hasNext()) {
/*  343 */           map.put(BuilderFactory.STRING.build(iterator.next()), BuilderFactory.STRING.build(iterator.next()));
/*      */         }
/*  345 */         return map;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/*  351 */         return "Map<String, String>";
/*      */       }
/*      */     };
/*      */   
/*  355 */   public static final Builder<Map<String, Object>> ENCODED_OBJECT_MAP = new Builder<Map<String, Object>>()
/*      */     {
/*      */       public Map<String, Object> build(Object data) {
/*  358 */         if (data == null) return null; 
/*  359 */         List<Object> list = (List<Object>)data;
/*  360 */         if (list.isEmpty()) return Collections.emptyMap();
/*      */         
/*  362 */         if (list.get(0) instanceof KeyValue) {
/*  363 */           Map<String, Object> map1 = new HashMap<>(list.size(), 1.0F);
/*  364 */           Iterator<KeyValue> iterator1 = list.iterator();
/*  365 */           while (iterator1.hasNext()) {
/*  366 */             KeyValue kv = iterator1.next();
/*  367 */             map1.put(BuilderFactory.STRING.build(kv.getKey()), BuilderFactory.ENCODED_OBJECT.build(kv.getValue()));
/*      */           } 
/*  369 */           return map1;
/*      */         } 
/*  371 */         Map<String, Object> map = new HashMap<>(list.size() / 2, 1.0F);
/*  372 */         Iterator iterator = list.iterator();
/*  373 */         while (iterator.hasNext()) {
/*  374 */           map.put(BuilderFactory.STRING.build(iterator.next()), BuilderFactory.ENCODED_OBJECT.build(iterator.next()));
/*      */         }
/*  376 */         return map;
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*  381 */   public static final Builder<Object> AGGRESSIVE_ENCODED_OBJECT = new Builder()
/*      */     {
/*      */       public Object build(Object data) {
/*  384 */         if (data == null) return null;
/*      */         
/*  386 */         if (data instanceof List) {
/*  387 */           List list = (List)data;
/*  388 */           if (list.isEmpty()) return Collections.emptyMap();
/*      */           
/*  390 */           if (list.get(0) instanceof KeyValue) {
/*  391 */             return ((List)data).stream()
/*  392 */               .filter(kv -> (kv != null && kv.getKey() != null && kv.getValue() != null))
/*  393 */               .collect(Collectors.toMap(kv -> (String)BuilderFactory.STRING.build(kv.getKey()), kv -> build(kv.getValue())));
/*      */           }
/*      */           
/*  396 */           return list.stream().map(this::build).collect(Collectors.toList());
/*      */         } 
/*  398 */         if (data instanceof byte[]) {
/*  399 */           return BuilderFactory.STRING.build(data);
/*      */         }
/*  401 */         return data;
/*      */       }
/*      */     };
/*      */   
/*  405 */   public static final Builder<Map<String, Object>> AGGRESSIVE_ENCODED_OBJECT_MAP = new Builder<Map<String, Object>>()
/*      */     {
/*      */       public Map<String, Object> build(Object data) {
/*  408 */         return (Map<String, Object>)BuilderFactory.AGGRESSIVE_ENCODED_OBJECT.build(data);
/*      */       }
/*      */     };
/*      */   
/*  412 */   public static final Builder<List<Map.Entry<String, String>>> STRING_PAIR_LIST = new Builder<List<Map.Entry<String, String>>>()
/*      */     {
/*      */       
/*      */       public List<Map.Entry<String, String>> build(Object data)
/*      */       {
/*  417 */         List<byte[]> flatHash = (List<byte[]>)data;
/*  418 */         List<Map.Entry<String, String>> pairList = new ArrayList<>(flatHash.size() / 2);
/*  419 */         Iterator<byte[]> iterator = (Iterator)flatHash.iterator();
/*  420 */         while (iterator.hasNext()) {
/*  421 */           pairList.add(KeyValue.of(BuilderFactory.STRING.build(iterator.next()), BuilderFactory.STRING.build(iterator.next())));
/*      */         }
/*      */         
/*  424 */         return pairList;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  429 */         return "List<Map.Entry<String, String>>";
/*      */       }
/*      */     };
/*      */   
/*  433 */   public static final Builder<List<Map.Entry<String, String>>> STRING_PAIR_LIST_FROM_PAIRS = new Builder<List<Map.Entry<String, String>>>()
/*      */     {
/*      */       
/*      */       public List<Map.Entry<String, String>> build(Object data)
/*      */       {
/*  438 */         return (List<Map.Entry<String, String>>)((List)data).stream().map(o -> (List)o)
/*  439 */           .map(l -> KeyValue.of(BuilderFactory.STRING.build(l.get(0)), BuilderFactory.STRING.build(l.get(1))))
/*  440 */           .collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  445 */         return "List<Map.Entry<String, String>>";
/*      */       }
/*      */     };
/*      */   
/*  449 */   public static final Builder<Map<String, Long>> STRING_LONG_MAP = new Builder<Map<String, Long>>()
/*      */     {
/*      */       public Map<String, Long> build(Object data)
/*      */       {
/*  453 */         List<Object> list = (List<Object>)data;
/*  454 */         if (list.isEmpty()) return Collections.emptyMap();
/*      */         
/*  456 */         if (list.get(0) instanceof KeyValue) {
/*  457 */           Map<String, Long> map1 = new LinkedHashMap<>(list.size(), 1.0F);
/*  458 */           Iterator<KeyValue> iterator1 = list.iterator();
/*  459 */           while (iterator1.hasNext()) {
/*  460 */             KeyValue kv = iterator1.next();
/*  461 */             map1.put(BuilderFactory.STRING.build(kv.getKey()), BuilderFactory.LONG.build(kv.getValue()));
/*      */           } 
/*  463 */           return map1;
/*      */         } 
/*  465 */         Map<String, Long> map = new LinkedHashMap<>(list.size() / 2, 1.0F);
/*  466 */         Iterator iterator = list.iterator();
/*  467 */         while (iterator.hasNext()) {
/*  468 */           map.put(BuilderFactory.STRING.build(iterator.next()), BuilderFactory.LONG.build(iterator.next()));
/*      */         }
/*  470 */         return map;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/*  476 */         return "Map<String, Long>";
/*      */       }
/*      */     };
/*      */   
/*  480 */   public static final Builder<KeyValue<String, String>> KEYED_ELEMENT = new Builder<KeyValue<String, String>>()
/*      */     {
/*      */       public KeyValue<String, String> build(Object data)
/*      */       {
/*  484 */         if (data == null) return null; 
/*  485 */         List<Object> l = (List<Object>)data;
/*  486 */         return KeyValue.of(BuilderFactory.STRING.build(l.get(0)), BuilderFactory.STRING.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  491 */         return "KeyValue<String, String>";
/*      */       }
/*      */     };
/*      */   
/*  495 */   public static final Builder<KeyValue<byte[], byte[]>> BINARY_KEYED_ELEMENT = new Builder<KeyValue<byte[], byte[]>>()
/*      */     {
/*      */       public KeyValue<byte[], byte[]> build(Object data)
/*      */       {
/*  499 */         if (data == null) return null; 
/*  500 */         List<Object> l = (List<Object>)data;
/*  501 */         return KeyValue.of(BuilderFactory.BINARY.build(l.get(0)), BuilderFactory.BINARY.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  506 */         return "KeyValue<byte[], byte[]>";
/*      */       }
/*      */     };
/*      */   
/*  510 */   public static final Builder<KeyValue<Long, Double>> ZRANK_WITHSCORE_PAIR = new Builder<KeyValue<Long, Double>>()
/*      */     {
/*      */       public KeyValue<Long, Double> build(Object data) {
/*  513 */         if (data == null) {
/*  514 */           return null;
/*      */         }
/*  516 */         List<Object> l = (List<Object>)data;
/*  517 */         return new KeyValue(BuilderFactory.LONG.build(l.get(0)), BuilderFactory.DOUBLE.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  522 */         return "KeyValue<Long, Double>";
/*      */       }
/*      */     };
/*      */   
/*  526 */   public static final Builder<KeyValue<String, List<String>>> KEYED_STRING_LIST = new Builder<KeyValue<String, List<String>>>()
/*      */     {
/*      */       
/*      */       public KeyValue<String, List<String>> build(Object data)
/*      */       {
/*  531 */         if (data == null) return null; 
/*  532 */         List<byte[]> l = (List<byte[]>)data;
/*  533 */         return new KeyValue(BuilderFactory.STRING.build(l.get(0)), BuilderFactory.STRING_LIST.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  538 */         return "KeyValue<String, List<String>>";
/*      */       }
/*      */     };
/*      */   
/*  542 */   public static final Builder<KeyValue<Long, Long>> LONG_LONG_PAIR = new Builder<KeyValue<Long, Long>>()
/*      */     {
/*      */       public KeyValue<Long, Long> build(Object data)
/*      */       {
/*  546 */         if (data == null) return null; 
/*  547 */         List<Object> dataList = (List<Object>)data;
/*  548 */         return new KeyValue(BuilderFactory.LONG.build(dataList.get(0)), BuilderFactory.LONG.build(dataList.get(1)));
/*      */       }
/*      */     };
/*      */   
/*  552 */   public static final Builder<List<KeyValue<String, List<String>>>> KEYED_STRING_LIST_LIST = new Builder<List<KeyValue<String, List<String>>>>()
/*      */     {
/*      */       public List<KeyValue<String, List<String>>> build(Object data)
/*      */       {
/*  556 */         List<Object> list = (List<Object>)data;
/*  557 */         return (List<KeyValue<String, List<String>>>)list.stream().map(BuilderFactory.KEYED_STRING_LIST::build).collect(Collectors.toList());
/*      */       }
/*      */     };
/*      */   
/*  561 */   public static final Builder<KeyValue<byte[], List<byte[]>>> KEYED_BINARY_LIST = new Builder<KeyValue<byte[], List<byte[]>>>()
/*      */     {
/*      */       
/*      */       public KeyValue<byte[], List<byte[]>> build(Object data)
/*      */       {
/*  566 */         if (data == null) return null; 
/*  567 */         List<byte[]> l = (List<byte[]>)data;
/*  568 */         return new KeyValue(BuilderFactory.BINARY.build(l.get(0)), BuilderFactory.BINARY_LIST.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  573 */         return "KeyValue<byte[], List<byte[]>>";
/*      */       }
/*      */     };
/*      */   
/*  577 */   public static final Builder<Tuple> TUPLE = new Builder<Tuple>()
/*      */     {
/*      */       public Tuple build(Object data)
/*      */       {
/*  581 */         List<byte[]> l = (List<byte[]>)data;
/*  582 */         if (l.isEmpty()) {
/*  583 */           return null;
/*      */         }
/*  585 */         return new Tuple(l.get(0), BuilderFactory.DOUBLE.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  590 */         return "Tuple";
/*      */       }
/*      */     };
/*      */   
/*  594 */   public static final Builder<KeyValue<String, Tuple>> KEYED_TUPLE = new Builder<KeyValue<String, Tuple>>()
/*      */     {
/*      */       public KeyValue<String, Tuple> build(Object data)
/*      */       {
/*  598 */         List<Object> l = (List<Object>)data;
/*  599 */         if (l.isEmpty()) {
/*  600 */           return null;
/*      */         }
/*  602 */         return KeyValue.of(BuilderFactory.STRING.build(l.get(0)), new Tuple(BuilderFactory.BINARY.build(l.get(1)), BuilderFactory.DOUBLE.build(l.get(2))));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  607 */         return "KeyValue<String, Tuple>";
/*      */       }
/*      */     };
/*      */   
/*  611 */   public static final Builder<KeyValue<byte[], Tuple>> BINARY_KEYED_TUPLE = new Builder<KeyValue<byte[], Tuple>>()
/*      */     {
/*      */       public KeyValue<byte[], Tuple> build(Object data)
/*      */       {
/*  615 */         List<Object> l = (List<Object>)data;
/*  616 */         if (l.isEmpty()) {
/*  617 */           return null;
/*      */         }
/*  619 */         return KeyValue.of(BuilderFactory.BINARY.build(l.get(0)), new Tuple(BuilderFactory.BINARY.build(l.get(1)), BuilderFactory.DOUBLE.build(l.get(2))));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  624 */         return "KeyValue<byte[], Tuple>";
/*      */       }
/*      */     };
/*      */   
/*  628 */   public static final Builder<List<Tuple>> TUPLE_LIST = new Builder<List<Tuple>>()
/*      */     {
/*      */       public List<Tuple> build(Object data)
/*      */       {
/*  632 */         if (null == data) {
/*  633 */           return null;
/*      */         }
/*  635 */         List<byte[]> l = (List<byte[]>)data;
/*  636 */         List<Tuple> result = new ArrayList<>(l.size() / 2);
/*  637 */         Iterator<byte[]> iterator = (Iterator)l.iterator();
/*  638 */         while (iterator.hasNext()) {
/*  639 */           result.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
/*      */         }
/*  641 */         return result;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  646 */         return "List<Tuple>";
/*      */       }
/*      */     };
/*      */   
/*  650 */   public static final Builder<List<Tuple>> TUPLE_LIST_RESP3 = new Builder<List<Tuple>>()
/*      */     {
/*      */       public List<Tuple> build(Object data)
/*      */       {
/*  654 */         if (null == data) return null; 
/*  655 */         return (List<Tuple>)((List)data).stream().map(BuilderFactory.TUPLE::build).collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  660 */         return "List<Tuple>";
/*      */       }
/*      */     };
/*      */   
/*  664 */   public static final Builder<Set<Tuple>> TUPLE_ZSET = new Builder<Set<Tuple>>()
/*      */     {
/*      */       public Set<Tuple> build(Object data)
/*      */       {
/*  668 */         if (null == data) {
/*  669 */           return null;
/*      */         }
/*  671 */         List<byte[]> l = (List<byte[]>)data;
/*  672 */         Set<Tuple> result = new LinkedHashSet<>(l.size() / 2, 1.0F);
/*  673 */         Iterator<byte[]> iterator = (Iterator)l.iterator();
/*  674 */         while (iterator.hasNext()) {
/*  675 */           result.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
/*      */         }
/*  677 */         return result;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  682 */         return "ZSet<Tuple>";
/*      */       }
/*      */     };
/*      */   
/*  686 */   public static final Builder<Set<Tuple>> TUPLE_ZSET_RESP3 = new Builder<Set<Tuple>>()
/*      */     {
/*      */       public Set<Tuple> build(Object data)
/*      */       {
/*  690 */         if (null == data) return null; 
/*  691 */         return (Set<Tuple>)((List)data).stream().map(BuilderFactory.TUPLE::build).collect(Collectors.toCollection(LinkedHashSet::new));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  696 */         return "ZSet<Tuple>";
/*      */       }
/*      */     };
/*      */   
/*  700 */   private static final Builder<List<Tuple>> TUPLE_LIST_FROM_PAIRS = new Builder<List<Tuple>>()
/*      */     {
/*      */       public List<Tuple> build(Object data)
/*      */       {
/*  704 */         if (data == null) return null; 
/*  705 */         return (List<Tuple>)((List)data).stream().map(BuilderFactory.TUPLE::build).collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  710 */         return "List<Tuple>";
/*      */       }
/*      */     };
/*      */   
/*  714 */   public static final Builder<KeyValue<String, List<Tuple>>> KEYED_TUPLE_LIST = new Builder<KeyValue<String, List<Tuple>>>()
/*      */     {
/*      */       
/*      */       public KeyValue<String, List<Tuple>> build(Object data)
/*      */       {
/*  719 */         if (data == null) return null; 
/*  720 */         List<Object> l = (List<Object>)data;
/*  721 */         return new KeyValue(BuilderFactory.STRING.build(l.get(0)), BuilderFactory.TUPLE_LIST_FROM_PAIRS.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  726 */         return "KeyValue<String, List<Tuple>>";
/*      */       }
/*      */     };
/*      */   
/*  730 */   public static final Builder<KeyValue<byte[], List<Tuple>>> BINARY_KEYED_TUPLE_LIST = new Builder<KeyValue<byte[], List<Tuple>>>()
/*      */     {
/*      */       
/*      */       public KeyValue<byte[], List<Tuple>> build(Object data)
/*      */       {
/*  735 */         if (data == null) return null; 
/*  736 */         List<Object> l = (List<Object>)data;
/*  737 */         return new KeyValue(BuilderFactory.BINARY.build(l.get(0)), BuilderFactory.TUPLE_LIST_FROM_PAIRS.build(l.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  742 */         return "KeyValue<byte[], List<Tuple>>";
/*      */       }
/*      */     };
/*      */   
/*  746 */   public static final Builder<ScanResult<String>> SCAN_RESPONSE = new Builder<ScanResult<String>>()
/*      */     {
/*      */       public ScanResult<String> build(Object data) {
/*  749 */         List<Object> result = (List<Object>)data;
/*  750 */         String newcursor = new String((byte[])result.get(0));
/*  751 */         List<byte[]> rawResults = (List<byte[]>)result.get(1);
/*  752 */         List<String> results = new ArrayList<>(rawResults.size());
/*  753 */         for (byte[] bs : rawResults) {
/*  754 */           results.add(SafeEncoder.encode(bs));
/*      */         }
/*  756 */         return new ScanResult(newcursor, results);
/*      */       }
/*      */     };
/*      */   
/*  760 */   public static final Builder<ScanResult<Map.Entry<String, String>>> HSCAN_RESPONSE = new Builder<ScanResult<Map.Entry<String, String>>>()
/*      */     {
/*      */       public ScanResult<Map.Entry<String, String>> build(Object data)
/*      */       {
/*  764 */         List<Object> result = (List<Object>)data;
/*  765 */         String newcursor = new String((byte[])result.get(0));
/*  766 */         List<byte[]> rawResults = (List<byte[]>)result.get(1);
/*  767 */         List<Map.Entry<String, String>> results = new ArrayList<>(rawResults.size() / 2);
/*  768 */         Iterator<byte[]> iterator = (Iterator)rawResults.iterator();
/*  769 */         while (iterator.hasNext()) {
/*  770 */           results.add(new AbstractMap.SimpleEntry<>(SafeEncoder.encode(iterator.next()), 
/*  771 */                 SafeEncoder.encode(iterator.next())));
/*      */         }
/*  773 */         return new ScanResult(newcursor, results);
/*      */       }
/*      */     };
/*      */   
/*  777 */   public static final Builder<ScanResult<String>> SSCAN_RESPONSE = new Builder<ScanResult<String>>()
/*      */     {
/*      */       public ScanResult<String> build(Object data) {
/*  780 */         List<Object> result = (List<Object>)data;
/*  781 */         String newcursor = new String((byte[])result.get(0));
/*  782 */         List<byte[]> rawResults = (List<byte[]>)result.get(1);
/*  783 */         List<String> results = new ArrayList<>(rawResults.size());
/*  784 */         for (byte[] bs : rawResults) {
/*  785 */           results.add(SafeEncoder.encode(bs));
/*      */         }
/*  787 */         return new ScanResult(newcursor, results);
/*      */       }
/*      */     };
/*      */   
/*  791 */   public static final Builder<ScanResult<Tuple>> ZSCAN_RESPONSE = new Builder<ScanResult<Tuple>>()
/*      */     {
/*      */       public ScanResult<Tuple> build(Object data) {
/*  794 */         List<Object> result = (List<Object>)data;
/*  795 */         String newcursor = new String((byte[])result.get(0));
/*  796 */         List<byte[]> rawResults = (List<byte[]>)result.get(1);
/*  797 */         List<Tuple> results = new ArrayList<>(rawResults.size() / 2);
/*  798 */         Iterator<byte[]> iterator = (Iterator)rawResults.iterator();
/*  799 */         while (iterator.hasNext()) {
/*  800 */           results.add(new Tuple(iterator.next(), BuilderFactory.DOUBLE.build(iterator.next())));
/*      */         }
/*  802 */         return new ScanResult(newcursor, results);
/*      */       }
/*      */     };
/*      */   
/*  806 */   public static final Builder<ScanResult<byte[]>> SCAN_BINARY_RESPONSE = new Builder<ScanResult<byte[]>>()
/*      */     {
/*      */       public ScanResult<byte[]> build(Object data) {
/*  809 */         List<Object> result = (List<Object>)data;
/*  810 */         byte[] newcursor = (byte[])result.get(0);
/*  811 */         List<byte[]> rawResults = (List<byte[]>)result.get(1);
/*  812 */         return new ScanResult(newcursor, rawResults);
/*      */       }
/*      */     };
/*      */   
/*  816 */   public static final Builder<ScanResult<Map.Entry<byte[], byte[]>>> HSCAN_BINARY_RESPONSE = new Builder<ScanResult<Map.Entry<byte[], byte[]>>>()
/*      */     {
/*      */       public ScanResult<Map.Entry<byte[], byte[]>> build(Object data)
/*      */       {
/*  820 */         List<Object> result = (List<Object>)data;
/*  821 */         byte[] newcursor = (byte[])result.get(0);
/*  822 */         List<byte[]> rawResults = (List<byte[]>)result.get(1);
/*  823 */         List<Map.Entry<byte[], byte[]>> results = new ArrayList<>(rawResults.size() / 2);
/*  824 */         Iterator<byte[]> iterator = (Iterator)rawResults.iterator();
/*  825 */         while (iterator.hasNext()) {
/*  826 */           results.add((Map.Entry)new AbstractMap.SimpleEntry<>(iterator.next(), iterator.next()));
/*      */         }
/*  828 */         return new ScanResult(newcursor, results);
/*      */       }
/*      */     };
/*      */   
/*  832 */   public static final Builder<ScanResult<byte[]>> SSCAN_BINARY_RESPONSE = new Builder<ScanResult<byte[]>>()
/*      */     {
/*      */       public ScanResult<byte[]> build(Object data) {
/*  835 */         List<Object> result = (List<Object>)data;
/*  836 */         byte[] newcursor = (byte[])result.get(0);
/*  837 */         List<byte[]> rawResults = (List<byte[]>)result.get(1);
/*  838 */         return new ScanResult(newcursor, rawResults);
/*      */       }
/*      */     };
/*      */   
/*  842 */   public static final Builder<Map<String, Long>> PUBSUB_NUMSUB_MAP = new Builder<Map<String, Long>>()
/*      */     {
/*      */       public Map<String, Long> build(Object data)
/*      */       {
/*  846 */         List<Object> flatHash = (List<Object>)data;
/*  847 */         Map<String, Long> hash = new HashMap<>(flatHash.size() / 2, 1.0F);
/*  848 */         Iterator<Object> iterator = flatHash.iterator();
/*  849 */         while (iterator.hasNext()) {
/*  850 */           hash.put(SafeEncoder.encode((byte[])iterator.next()), (Long)iterator.next());
/*      */         }
/*  852 */         return hash;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  857 */         return "PUBSUB_NUMSUB_MAP<String, String>";
/*      */       }
/*      */     };
/*      */   
/*  861 */   public static final Builder<List<GeoCoordinate>> GEO_COORDINATE_LIST = new Builder<List<GeoCoordinate>>()
/*      */     {
/*      */       public List<GeoCoordinate> build(Object data) {
/*  864 */         if (null == data) {
/*  865 */           return null;
/*      */         }
/*  867 */         return interpretGeoposResult((List<Object>)data);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  872 */         return "List<GeoCoordinate>";
/*      */       }
/*      */       
/*      */       private List<GeoCoordinate> interpretGeoposResult(List<Object> responses) {
/*  876 */         List<GeoCoordinate> responseCoordinate = new ArrayList<>(responses.size());
/*  877 */         for (Object response : responses) {
/*  878 */           if (response == null) {
/*  879 */             responseCoordinate.add((GeoCoordinate)null); continue;
/*      */           } 
/*  881 */           List<Object> respList = (List<Object>)response;
/*      */           
/*  883 */           GeoCoordinate coord = new GeoCoordinate(((Double)BuilderFactory.DOUBLE.build(respList.get(0))).doubleValue(), ((Double)BuilderFactory.DOUBLE.build(respList.get(1))).doubleValue());
/*  884 */           responseCoordinate.add(coord);
/*      */         } 
/*      */         
/*  887 */         return responseCoordinate;
/*      */       }
/*      */     };
/*      */   
/*  891 */   public static final Builder<List<GeoRadiusResponse>> GEORADIUS_WITH_PARAMS_RESULT = new Builder<List<GeoRadiusResponse>>()
/*      */     {
/*      */       public List<GeoRadiusResponse> build(Object data) {
/*  894 */         if (data == null) {
/*  895 */           return null;
/*      */         }
/*      */         
/*  898 */         List<Object> objectList = (List<Object>)data;
/*      */         
/*  900 */         List<GeoRadiusResponse> responses = new ArrayList<>(objectList.size());
/*  901 */         if (objectList.isEmpty()) {
/*  902 */           return responses;
/*      */         }
/*      */         
/*  905 */         if (objectList.get(0) instanceof List) {
/*      */ 
/*      */           
/*  908 */           for (Object obj : objectList) {
/*  909 */             List<Object> informations = (List<Object>)obj;
/*      */             
/*  911 */             GeoRadiusResponse resp = new GeoRadiusResponse((byte[])informations.get(0));
/*      */             
/*  913 */             int size = informations.size();
/*  914 */             for (int idx = 1; idx < size; idx++) {
/*  915 */               Object info = informations.get(idx);
/*  916 */               if (info instanceof List) {
/*      */                 
/*  918 */                 List<Object> coord = (List<Object>)info;
/*      */                 
/*  920 */                 resp.setCoordinate(new GeoCoordinate(((Double)BuilderFactory.DOUBLE.build(coord.get(0))).doubleValue(), ((Double)BuilderFactory.DOUBLE
/*  921 */                       .build(coord.get(1))).doubleValue()));
/*  922 */               } else if (info instanceof Long) {
/*      */                 
/*  924 */                 resp.setRawScore(((Long)BuilderFactory.LONG.build(info)).longValue());
/*      */               } else {
/*      */                 
/*  927 */                 resp.setDistance(((Double)BuilderFactory.DOUBLE.build(info)).doubleValue());
/*      */               } 
/*      */             } 
/*      */             
/*  931 */             responses.add(resp);
/*      */           } 
/*      */         } else {
/*      */           
/*  935 */           for (Object obj : objectList) {
/*  936 */             responses.add(new GeoRadiusResponse((byte[])obj));
/*      */           }
/*      */         } 
/*      */         
/*  940 */         return responses;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/*  945 */         return "GeoRadiusWithParamsResult";
/*      */       }
/*      */     };
/*      */   
/*  949 */   public static final Builder<Map<String, CommandDocument>> COMMAND_DOCS_RESPONSE = new Builder<Map<String, CommandDocument>>()
/*      */     {
/*      */       public Map<String, CommandDocument> build(Object data) {
/*  952 */         if (data == null) return null; 
/*  953 */         List<Object> list = (List<Object>)data;
/*  954 */         if (list.isEmpty()) return Collections.emptyMap();
/*      */         
/*  956 */         if (list.get(0) instanceof KeyValue) {
/*  957 */           Map<String, CommandDocument> map1 = new HashMap<>(list.size(), 1.0F);
/*  958 */           Iterator<KeyValue> iterator1 = list.iterator();
/*  959 */           while (iterator1.hasNext()) {
/*  960 */             KeyValue kv = iterator1.next();
/*  961 */             map1.put(BuilderFactory.STRING.build(kv.getKey()), new CommandDocument(BuilderFactory.ENCODED_OBJECT_MAP.build(kv.getValue())));
/*      */           } 
/*  963 */           return map1;
/*      */         } 
/*  965 */         Map<String, CommandDocument> map = new HashMap<>(list.size() / 2, 1.0F);
/*  966 */         Iterator iterator = list.iterator();
/*  967 */         while (iterator.hasNext()) {
/*  968 */           map.put(BuilderFactory.STRING.build(iterator.next()), new CommandDocument(BuilderFactory.ENCODED_OBJECT_MAP.build(iterator.next())));
/*      */         }
/*  970 */         return map;
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*  975 */   public static final Builder<Map<String, CommandInfo>> COMMAND_INFO_RESPONSE = new Builder<Map<String, CommandInfo>>()
/*      */     {
/*      */       public Map<String, CommandInfo> build(Object data) {
/*  978 */         if (data == null) {
/*  979 */           return null;
/*      */         }
/*      */         
/*  982 */         List<Object> rawList = (List<Object>)data;
/*  983 */         Map<String, CommandInfo> map = new HashMap<>(rawList.size());
/*      */         
/*  985 */         for (Object rawCommandInfo : rawList) {
/*  986 */           if (rawCommandInfo == null) {
/*      */             continue;
/*      */           }
/*      */           
/*  990 */           List<Object> commandInfo = (List<Object>)rawCommandInfo;
/*  991 */           String name = BuilderFactory.STRING.build(commandInfo.get(0));
/*  992 */           CommandInfo info = CommandInfo.COMMAND_INFO_BUILDER.build(commandInfo);
/*  993 */           map.put(name, info);
/*      */         } 
/*      */         
/*  996 */         return map;
/*      */       }
/*      */     };
/*      */   
/* 1000 */   private static final Builder<List<List<Long>>> CLUSTER_SHARD_SLOTS_RANGES = new Builder<List<List<Long>>>()
/*      */     {
/*      */       public List<List<Long>> build(Object data)
/*      */       {
/* 1004 */         if (null == data) {
/* 1005 */           return null;
/*      */         }
/*      */         
/* 1008 */         List<Long> rawSlots = (List<Long>)data;
/* 1009 */         List<List<Long>> slotsRanges = new ArrayList<>();
/* 1010 */         for (int i = 0; i < rawSlots.size(); i += 2) {
/* 1011 */           slotsRanges.add(Arrays.asList(new Long[] { rawSlots.get(i), rawSlots.get(i + 1) }));
/*      */         } 
/* 1013 */         return slotsRanges;
/*      */       }
/*      */     };
/*      */   
/* 1017 */   private static final Builder<List<ClusterShardNodeInfo>> CLUSTER_SHARD_NODE_INFO_LIST = new Builder<List<ClusterShardNodeInfo>>()
/*      */     {
/*      */       
/* 1020 */       final Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1024 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1025 */         tempMappingFunctions.put("id", BuilderFactory.STRING);
/* 1026 */         tempMappingFunctions.put("endpoint", BuilderFactory.STRING);
/* 1027 */         tempMappingFunctions.put("ip", BuilderFactory.STRING);
/* 1028 */         tempMappingFunctions.put("hostname", BuilderFactory.STRING);
/* 1029 */         tempMappingFunctions.put("port", BuilderFactory.LONG);
/* 1030 */         tempMappingFunctions.put("tls-port", BuilderFactory.LONG);
/* 1031 */         tempMappingFunctions.put("role", BuilderFactory.STRING);
/* 1032 */         tempMappingFunctions.put("replication-offset", BuilderFactory.LONG);
/* 1033 */         tempMappingFunctions.put("health", BuilderFactory.STRING);
/*      */         
/* 1035 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public List<ClusterShardNodeInfo> build(Object data) {
/* 1041 */         if (null == data) {
/* 1042 */           return null;
/*      */         }
/*      */         
/* 1045 */         List<ClusterShardNodeInfo> response = new ArrayList<>();
/*      */         
/* 1047 */         List<Object> clusterShardNodeInfos = (List<Object>)data;
/* 1048 */         for (Object clusterShardNodeInfoObject : clusterShardNodeInfos) {
/* 1049 */           List<Object> clusterShardNodeInfo = (List<Object>)clusterShardNodeInfoObject;
/* 1050 */           Iterator<Object> iterator = clusterShardNodeInfo.iterator();
/* 1051 */           response.add(new ClusterShardNodeInfo(BuilderFactory.createMapFromDecodingFunctions(iterator, this.mappingFunctions)));
/*      */         } 
/*      */         
/* 1054 */         return response;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1059 */         return "List<ClusterShardNodeInfo>";
/*      */       }
/*      */     };
/*      */   
/* 1063 */   public static final Builder<List<ClusterShardInfo>> CLUSTER_SHARD_INFO_LIST = new Builder<List<ClusterShardInfo>>()
/*      */     {
/*      */       
/* 1066 */       final Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1070 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1071 */         tempMappingFunctions.put("slots", BuilderFactory.CLUSTER_SHARD_SLOTS_RANGES);
/* 1072 */         tempMappingFunctions.put("nodes", BuilderFactory.CLUSTER_SHARD_NODE_INFO_LIST);
/*      */         
/* 1074 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public List<ClusterShardInfo> build(Object data) {
/* 1080 */         if (null == data) {
/* 1081 */           return null;
/*      */         }
/*      */         
/* 1084 */         List<ClusterShardInfo> response = new ArrayList<>();
/*      */         
/* 1086 */         List<Object> clusterShardInfos = (List<Object>)data;
/* 1087 */         for (Object clusterShardInfoObject : clusterShardInfos) {
/* 1088 */           List<Object> clusterShardInfo = (List<Object>)clusterShardInfoObject;
/* 1089 */           Iterator<Object> iterator = clusterShardInfo.iterator();
/* 1090 */           response.add(new ClusterShardInfo(BuilderFactory.createMapFromDecodingFunctions(iterator, this.mappingFunctions)));
/*      */         } 
/*      */         
/* 1093 */         return response;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1098 */         return "List<ClusterShardInfo>";
/*      */       }
/*      */     };
/*      */   
/* 1102 */   public static final Builder<List<Module>> MODULE_LIST = new Builder<List<Module>>()
/*      */     {
/*      */       public List<Module> build(Object data) {
/* 1105 */         if (data == null) {
/* 1106 */           return null;
/*      */         }
/*      */         
/* 1109 */         List<List<Object>> objectList = (List<List<Object>>)data;
/*      */         
/* 1111 */         List<Module> responses = new ArrayList<>(objectList.size());
/* 1112 */         if (objectList.isEmpty()) {
/* 1113 */           return responses;
/*      */         }
/*      */         
/* 1116 */         for (List<Object> moduleResp : objectList) {
/* 1117 */           if (moduleResp.get(0) instanceof KeyValue) {
/* 1118 */             responses.add(new Module(BuilderFactory.STRING.build(((KeyValue)moduleResp.get(0)).getValue()), ((Long)BuilderFactory.LONG
/* 1119 */                   .build(((KeyValue)moduleResp.get(1)).getValue())).intValue()));
/*      */             
/*      */             continue;
/*      */           } 
/* 1123 */           Module m = new Module(SafeEncoder.encode((byte[])moduleResp.get(1)), ((Long)moduleResp.get(3)).intValue());
/* 1124 */           responses.add(m);
/*      */         } 
/*      */         
/* 1127 */         return responses;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1132 */         return "List<Module>";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1139 */   public static final Builder<AccessControlUser> ACCESS_CONTROL_USER = new Builder<AccessControlUser>()
/*      */     {
/*      */       public AccessControlUser build(Object data) {
/* 1142 */         Map<String, Object> map = BuilderFactory.ENCODED_OBJECT_MAP.build(data);
/* 1143 */         if (map == null) return null; 
/* 1144 */         return new AccessControlUser(map);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1149 */         return "AccessControlUser";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1156 */   public static final Builder<List<AccessControlLogEntry>> ACCESS_CONTROL_LOG_ENTRY_LIST = new Builder<List<AccessControlLogEntry>>()
/*      */     {
/*      */       
/* 1159 */       private final Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1163 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1164 */         tempMappingFunctions.put("count", BuilderFactory.LONG);
/* 1165 */         tempMappingFunctions.put("reason", BuilderFactory.STRING);
/* 1166 */         tempMappingFunctions.put("context", BuilderFactory.STRING);
/* 1167 */         tempMappingFunctions.put("object", BuilderFactory.STRING);
/* 1168 */         tempMappingFunctions.put("username", BuilderFactory.STRING);
/* 1169 */         tempMappingFunctions.put("age-seconds", BuilderFactory.DOUBLE);
/* 1170 */         tempMappingFunctions.put("client-info", BuilderFactory.STRING);
/* 1171 */         tempMappingFunctions.put("entry-id", BuilderFactory.LONG);
/* 1172 */         tempMappingFunctions.put("timestamp-created", BuilderFactory.LONG);
/* 1173 */         tempMappingFunctions.put("timestamp-last-updated", BuilderFactory.LONG);
/*      */         
/* 1175 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public List<AccessControlLogEntry> build(Object data) {
/* 1181 */         if (null == data) {
/* 1182 */           return null;
/*      */         }
/*      */         
/* 1185 */         List<AccessControlLogEntry> list = new ArrayList<>();
/* 1186 */         List<List<Object>> logEntries = (List<List<Object>>)data;
/* 1187 */         for (List<Object> logEntryData : logEntries) {
/* 1188 */           Iterator<Object> logEntryDataIterator = logEntryData.iterator();
/*      */           
/* 1190 */           AccessControlLogEntry accessControlLogEntry = new AccessControlLogEntry(BuilderFactory.createMapFromDecodingFunctions(logEntryDataIterator, this.mappingFunctions, BuilderFactory
/* 1191 */                 .BACKUP_BUILDERS_FOR_DECODING_FUNCTIONS));
/* 1192 */           list.add(accessControlLogEntry);
/*      */         } 
/* 1194 */         return list;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1199 */         return "List<AccessControlLogEntry>";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/* 1205 */   public static final Builder<StreamEntryID> STREAM_ENTRY_ID = new Builder<StreamEntryID>()
/*      */     {
/*      */       public StreamEntryID build(Object data) {
/* 1208 */         if (null == data) {
/* 1209 */           return null;
/*      */         }
/* 1211 */         String id = SafeEncoder.encode((byte[])data);
/* 1212 */         return new StreamEntryID(id);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1217 */         return "StreamEntryID";
/*      */       }
/*      */     };
/*      */   
/* 1221 */   public static final Builder<List<StreamEntryID>> STREAM_ENTRY_ID_LIST = new Builder<List<StreamEntryID>>()
/*      */     {
/*      */       public List<StreamEntryID> build(Object data)
/*      */       {
/* 1225 */         if (null == data) {
/* 1226 */           return null;
/*      */         }
/* 1228 */         List<Object> objectList = (List<Object>)data;
/* 1229 */         List<StreamEntryID> responses = new ArrayList<>(objectList.size());
/* 1230 */         if (!objectList.isEmpty()) {
/* 1231 */           for (Object object : objectList) {
/* 1232 */             responses.add(BuilderFactory.STREAM_ENTRY_ID.build(object));
/*      */           }
/*      */         }
/* 1235 */         return responses;
/*      */       }
/*      */     };
/*      */   
/* 1239 */   public static final Builder<StreamEntry> STREAM_ENTRY = new Builder<StreamEntry>()
/*      */     {
/*      */       public StreamEntry build(Object data)
/*      */       {
/* 1243 */         if (null == data) {
/* 1244 */           return null;
/*      */         }
/* 1246 */         List<Object> objectList = (List<Object>)data;
/*      */         
/* 1248 */         if (objectList.isEmpty()) {
/* 1249 */           return null;
/*      */         }
/*      */         
/* 1252 */         String entryIdString = SafeEncoder.encode((byte[])objectList.get(0));
/* 1253 */         StreamEntryID entryID = new StreamEntryID(entryIdString);
/* 1254 */         List<byte[]> hash = (List<byte[]>)objectList.get(1);
/*      */         
/* 1256 */         Iterator<byte[]> hashIterator = (Iterator)hash.iterator();
/* 1257 */         Map<String, String> map = new HashMap<>(hash.size() / 2, 1.0F);
/* 1258 */         while (hashIterator.hasNext()) {
/* 1259 */           map.put(SafeEncoder.encode(hashIterator.next()), SafeEncoder.encode(hashIterator.next()));
/*      */         }
/* 1261 */         return new StreamEntry(entryID, map);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1266 */         return "StreamEntry";
/*      */       }
/*      */     };
/*      */   
/* 1270 */   public static final Builder<List<StreamEntry>> STREAM_ENTRY_LIST = new Builder<List<StreamEntry>>()
/*      */     {
/*      */       public List<StreamEntry> build(Object data)
/*      */       {
/* 1274 */         if (null == data) {
/* 1275 */           return null;
/*      */         }
/* 1277 */         List<ArrayList<Object>> objectList = (List<ArrayList<Object>>)data;
/*      */         
/* 1279 */         List<StreamEntry> responses = new ArrayList<>(objectList.size() / 2);
/* 1280 */         if (objectList.isEmpty()) {
/* 1281 */           return responses;
/*      */         }
/*      */         
/* 1284 */         for (ArrayList<Object> res : objectList) {
/* 1285 */           if (res == null) {
/* 1286 */             responses.add((StreamEntry)null);
/*      */             continue;
/*      */           } 
/* 1289 */           String entryIdString = SafeEncoder.encode((byte[])res.get(0));
/* 1290 */           StreamEntryID entryID = new StreamEntryID(entryIdString);
/* 1291 */           List<byte[]> hash = (List<byte[]>)res.get(1);
/* 1292 */           if (hash == null) {
/* 1293 */             responses.add(new StreamEntry(entryID, null));
/*      */             
/*      */             continue;
/*      */           } 
/* 1297 */           Iterator<byte[]> hashIterator = (Iterator)hash.iterator();
/* 1298 */           Map<String, String> map = new HashMap<>(hash.size() / 2, 1.0F);
/* 1299 */           while (hashIterator.hasNext()) {
/* 1300 */             map.put(SafeEncoder.encode(hashIterator.next()), SafeEncoder.encode(hashIterator.next()));
/*      */           }
/* 1302 */           responses.add(new StreamEntry(entryID, map));
/*      */         } 
/*      */         
/* 1305 */         return responses;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1310 */         return "List<StreamEntry>";
/*      */       }
/*      */     };
/*      */   
/* 1314 */   public static final Builder<Map.Entry<StreamEntryID, List<StreamEntry>>> STREAM_AUTO_CLAIM_RESPONSE = new Builder<Map.Entry<StreamEntryID, List<StreamEntry>>>()
/*      */     {
/*      */       
/*      */       public Map.Entry<StreamEntryID, List<StreamEntry>> build(Object data)
/*      */       {
/* 1319 */         if (null == data) {
/* 1320 */           return null;
/*      */         }
/*      */         
/* 1323 */         List<Object> objectList = (List<Object>)data;
/* 1324 */         return new AbstractMap.SimpleEntry<>(BuilderFactory.STREAM_ENTRY_ID.build(objectList.get(0)), BuilderFactory.STREAM_ENTRY_LIST
/* 1325 */             .build(objectList.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1330 */         return "Map.Entry<StreamEntryID, List<StreamEntry>>";
/*      */       }
/*      */     };
/*      */   
/* 1334 */   public static final Builder<Map.Entry<StreamEntryID, List<StreamEntryID>>> STREAM_AUTO_CLAIM_JUSTID_RESPONSE = new Builder<Map.Entry<StreamEntryID, List<StreamEntryID>>>()
/*      */     {
/*      */       
/*      */       public Map.Entry<StreamEntryID, List<StreamEntryID>> build(Object data)
/*      */       {
/* 1339 */         if (null == data) {
/* 1340 */           return null;
/*      */         }
/*      */         
/* 1343 */         List<Object> objectList = (List<Object>)data;
/* 1344 */         return new AbstractMap.SimpleEntry<>(BuilderFactory.STREAM_ENTRY_ID.build(objectList.get(0)), BuilderFactory.STREAM_ENTRY_ID_LIST
/* 1345 */             .build(objectList.get(1)));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1350 */         return "Map.Entry<StreamEntryID, List<StreamEntryID>>";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1358 */   public static final Builder<Map.Entry<StreamEntryID, List<StreamEntryID>>> STREAM_AUTO_CLAIM_ID_RESPONSE = STREAM_AUTO_CLAIM_JUSTID_RESPONSE;
/*      */ 
/*      */   
/* 1361 */   public static final Builder<List<Map.Entry<String, List<StreamEntry>>>> STREAM_READ_RESPONSE = new Builder<List<Map.Entry<String, List<StreamEntry>>>>()
/*      */     {
/*      */       public List<Map.Entry<String, List<StreamEntry>>> build(Object data)
/*      */       {
/* 1365 */         if (data == null) return null; 
/* 1366 */         List list = (List)data;
/* 1367 */         if (list.isEmpty()) return Collections.emptyList();
/*      */         
/* 1369 */         if (list.get(0) instanceof KeyValue) {
/* 1370 */           return (List<Map.Entry<String, List<StreamEntry>>>)list.stream()
/* 1371 */             .map(kv -> new KeyValue(BuilderFactory.STRING.build(kv.getKey()), BuilderFactory.STREAM_ENTRY_LIST.build(kv.getValue())))
/*      */             
/* 1373 */             .collect(Collectors.toList());
/*      */         }
/* 1375 */         List<Map.Entry<String, List<StreamEntry>>> result = new ArrayList<>(list.size());
/* 1376 */         for (Object streamObj : list) {
/* 1377 */           List<Object> stream = (List<Object>)streamObj;
/* 1378 */           String streamKey = BuilderFactory.STRING.build(stream.get(0));
/* 1379 */           List<StreamEntry> streamEntries = BuilderFactory.STREAM_ENTRY_LIST.build(stream.get(1));
/* 1380 */           result.add(KeyValue.of(streamKey, streamEntries));
/*      */         } 
/* 1382 */         return result;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1388 */         return "List<Entry<String, List<StreamEntry>>>";
/*      */       }
/*      */     };
/*      */   
/* 1392 */   public static final Builder<List<StreamPendingEntry>> STREAM_PENDING_ENTRY_LIST = new Builder<List<StreamPendingEntry>>()
/*      */     {
/*      */       public List<StreamPendingEntry> build(Object data)
/*      */       {
/* 1396 */         if (null == data) {
/* 1397 */           return null;
/*      */         }
/*      */         
/* 1400 */         List<Object> streamsEntries = (List<Object>)data;
/* 1401 */         List<StreamPendingEntry> result = new ArrayList<>(streamsEntries.size());
/* 1402 */         for (Object streamObj : streamsEntries) {
/* 1403 */           List<Object> stream = (List<Object>)streamObj;
/* 1404 */           String id = SafeEncoder.encode((byte[])stream.get(0));
/* 1405 */           String consumerName = SafeEncoder.encode((byte[])stream.get(1));
/* 1406 */           long idleTime = ((Long)BuilderFactory.LONG.build(stream.get(2))).longValue();
/* 1407 */           long deliveredTimes = ((Long)BuilderFactory.LONG.build(stream.get(3))).longValue();
/* 1408 */           result.add(new StreamPendingEntry(new StreamEntryID(id), consumerName, idleTime, deliveredTimes));
/*      */         } 
/*      */         
/* 1411 */         return result;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1416 */         return "List<StreamPendingEntry>";
/*      */       }
/*      */     };
/*      */   
/* 1420 */   public static final Builder<StreamInfo> STREAM_INFO = new Builder<StreamInfo>()
/*      */     {
/* 1422 */       Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1426 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1427 */         tempMappingFunctions.put("last-generated-id", BuilderFactory.STREAM_ENTRY_ID);
/* 1428 */         tempMappingFunctions.put("first-entry", BuilderFactory.STREAM_ENTRY);
/* 1429 */         tempMappingFunctions.put("length", BuilderFactory.LONG);
/* 1430 */         tempMappingFunctions.put("radix-tree-keys", BuilderFactory.LONG);
/* 1431 */         tempMappingFunctions.put("radix-tree-nodes", BuilderFactory.LONG);
/* 1432 */         tempMappingFunctions.put("last-entry", BuilderFactory.STREAM_ENTRY);
/* 1433 */         tempMappingFunctions.put("groups", BuilderFactory.LONG);
/*      */         
/* 1435 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public StreamInfo build(Object data) {
/* 1441 */         if (null == data) {
/* 1442 */           return null;
/*      */         }
/*      */         
/* 1445 */         List<Object> streamsEntries = (List<Object>)data;
/* 1446 */         Iterator<Object> iterator = streamsEntries.iterator();
/*      */         
/* 1448 */         return new StreamInfo(BuilderFactory.createMapFromDecodingFunctions(iterator, this.mappingFunctions));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1453 */         return "StreamInfo";
/*      */       }
/*      */     };
/*      */   
/* 1457 */   public static final Builder<List<StreamGroupInfo>> STREAM_GROUP_INFO_LIST = new Builder<List<StreamGroupInfo>>()
/*      */     {
/* 1459 */       Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1463 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1464 */         tempMappingFunctions.put("name", BuilderFactory.STRING);
/* 1465 */         tempMappingFunctions.put("consumers", BuilderFactory.LONG);
/* 1466 */         tempMappingFunctions.put("pending", BuilderFactory.LONG);
/* 1467 */         tempMappingFunctions.put("last-delivered-id", BuilderFactory.STREAM_ENTRY_ID);
/*      */         
/* 1469 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public List<StreamGroupInfo> build(Object data) {
/* 1475 */         if (null == data) {
/* 1476 */           return null;
/*      */         }
/*      */         
/* 1479 */         List<StreamGroupInfo> list = new ArrayList<>();
/* 1480 */         List<Object> streamsEntries = (List<Object>)data;
/* 1481 */         Iterator<Object> groupsArray = streamsEntries.iterator();
/*      */         
/* 1483 */         while (groupsArray.hasNext()) {
/*      */           
/* 1485 */           List<Object> groupInfo = (List<Object>)groupsArray.next();
/*      */           
/* 1487 */           Iterator<Object> groupInfoIterator = groupInfo.iterator();
/*      */           
/* 1489 */           StreamGroupInfo streamGroupInfo = new StreamGroupInfo(BuilderFactory.createMapFromDecodingFunctions(groupInfoIterator, this.mappingFunctions));
/*      */           
/* 1491 */           list.add(streamGroupInfo);
/*      */         } 
/*      */         
/* 1494 */         return list;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1500 */         return "List<StreamGroupInfo>";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1508 */   public static final Builder<List<StreamConsumersInfo>> STREAM_CONSUMERS_INFO_LIST = new Builder<List<StreamConsumersInfo>>()
/*      */     {
/*      */       
/* 1511 */       Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1514 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1515 */         tempMappingFunctions.put("name", BuilderFactory.STRING);
/* 1516 */         tempMappingFunctions.put("idle", BuilderFactory.LONG);
/* 1517 */         tempMappingFunctions.put("pending", BuilderFactory.LONG);
/* 1518 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public List<StreamConsumersInfo> build(Object data) {
/* 1525 */         if (null == data) {
/* 1526 */           return null;
/*      */         }
/*      */         
/* 1529 */         List<StreamConsumersInfo> list = new ArrayList<>();
/* 1530 */         List<Object> streamsEntries = (List<Object>)data;
/* 1531 */         Iterator<Object> groupsArray = streamsEntries.iterator();
/*      */         
/* 1533 */         while (groupsArray.hasNext()) {
/*      */           
/* 1535 */           List<Object> groupInfo = (List<Object>)groupsArray.next();
/*      */           
/* 1537 */           Iterator<Object> consumerInfoIterator = groupInfo.iterator();
/*      */ 
/*      */           
/* 1540 */           StreamConsumersInfo streamGroupInfo = new StreamConsumersInfo(BuilderFactory.createMapFromDecodingFunctions(consumerInfoIterator, this.mappingFunctions));
/* 1541 */           list.add(streamGroupInfo);
/*      */         } 
/*      */         
/* 1544 */         return list;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1550 */         return "List<StreamConsumersInfo>";
/*      */       }
/*      */     };
/*      */   
/* 1554 */   public static final Builder<List<StreamConsumerInfo>> STREAM_CONSUMER_INFO_LIST = new Builder<List<StreamConsumerInfo>>()
/*      */     {
/*      */       
/* 1557 */       Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1560 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1561 */         tempMappingFunctions.put("name", BuilderFactory.STRING);
/* 1562 */         tempMappingFunctions.put("idle", BuilderFactory.LONG);
/* 1563 */         tempMappingFunctions.put("pending", BuilderFactory.LONG);
/* 1564 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public List<StreamConsumerInfo> build(Object data) {
/* 1571 */         if (null == data) {
/* 1572 */           return null;
/*      */         }
/*      */         
/* 1575 */         List<StreamConsumerInfo> list = new ArrayList<>();
/* 1576 */         List<Object> streamsEntries = (List<Object>)data;
/* 1577 */         Iterator<Object> groupsArray = streamsEntries.iterator();
/*      */         
/* 1579 */         while (groupsArray.hasNext()) {
/*      */           
/* 1581 */           List<Object> groupInfo = (List<Object>)groupsArray.next();
/*      */           
/* 1583 */           Iterator<Object> consumerInfoIterator = groupInfo.iterator();
/*      */ 
/*      */           
/* 1586 */           StreamConsumerInfo streamConsumerInfo = new StreamConsumerInfo(BuilderFactory.createMapFromDecodingFunctions(consumerInfoIterator, this.mappingFunctions));
/* 1587 */           list.add(streamConsumerInfo);
/*      */         } 
/*      */         
/* 1590 */         return list;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1595 */         return "List<StreamConsumerInfo>";
/*      */       }
/*      */     };
/*      */   
/* 1599 */   private static final Builder<List<StreamConsumerFullInfo>> STREAM_CONSUMER_FULL_INFO_LIST = new Builder<List<StreamConsumerFullInfo>>()
/*      */     {
/*      */       
/* 1602 */       final Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1606 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1607 */         tempMappingFunctions.put("name", BuilderFactory.STRING);
/* 1608 */         tempMappingFunctions.put("seen-time", BuilderFactory.LONG);
/* 1609 */         tempMappingFunctions.put("pel-count", BuilderFactory.LONG);
/* 1610 */         tempMappingFunctions.put("pending", BuilderFactory.ENCODED_OBJECT_LIST);
/*      */         
/* 1612 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public List<StreamConsumerFullInfo> build(Object data) {
/* 1618 */         if (null == data) {
/* 1619 */           return null;
/*      */         }
/*      */         
/* 1622 */         List<StreamConsumerFullInfo> list = new ArrayList<>();
/* 1623 */         List<Object> streamsEntries = (List<Object>)data;
/*      */         
/* 1625 */         for (Object streamsEntry : streamsEntries) {
/* 1626 */           List<Object> consumerInfoList = (List<Object>)streamsEntry;
/* 1627 */           Iterator<Object> consumerInfoIterator = consumerInfoList.iterator();
/*      */           
/* 1629 */           StreamConsumerFullInfo consumerInfo = new StreamConsumerFullInfo(BuilderFactory.createMapFromDecodingFunctions(consumerInfoIterator, this.mappingFunctions));
/* 1630 */           list.add(consumerInfo);
/*      */         } 
/* 1632 */         return list;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1637 */         return "List<StreamConsumerFullInfo>";
/*      */       }
/*      */     };
/*      */   
/* 1641 */   private static final Builder<List<StreamGroupFullInfo>> STREAM_GROUP_FULL_INFO_LIST = new Builder<List<StreamGroupFullInfo>>()
/*      */     {
/*      */       
/* 1644 */       final Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1648 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1649 */         tempMappingFunctions.put("name", BuilderFactory.STRING);
/* 1650 */         tempMappingFunctions.put("consumers", BuilderFactory.STREAM_CONSUMER_FULL_INFO_LIST);
/* 1651 */         tempMappingFunctions.put("pending", BuilderFactory.ENCODED_OBJECT_LIST);
/* 1652 */         tempMappingFunctions.put("last-delivered-id", BuilderFactory.STREAM_ENTRY_ID);
/* 1653 */         tempMappingFunctions.put("pel-count", BuilderFactory.LONG);
/*      */         
/* 1655 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public List<StreamGroupFullInfo> build(Object data) {
/* 1661 */         if (null == data) {
/* 1662 */           return null;
/*      */         }
/*      */         
/* 1665 */         List<StreamGroupFullInfo> list = new ArrayList<>();
/* 1666 */         List<Object> streamsEntries = (List<Object>)data;
/*      */         
/* 1668 */         for (Object streamsEntry : streamsEntries) {
/*      */           
/* 1670 */           List<Object> groupInfo = (List<Object>)streamsEntry;
/*      */           
/* 1672 */           Iterator<Object> groupInfoIterator = groupInfo.iterator();
/*      */ 
/*      */           
/* 1675 */           StreamGroupFullInfo groupFullInfo = new StreamGroupFullInfo(BuilderFactory.createMapFromDecodingFunctions(groupInfoIterator, this.mappingFunctions));
/* 1676 */           list.add(groupFullInfo);
/*      */         } 
/*      */         
/* 1679 */         return list;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1684 */         return "List<StreamGroupFullInfo>";
/*      */       }
/*      */     };
/*      */   
/* 1688 */   public static final Builder<StreamFullInfo> STREAM_FULL_INFO = new Builder<StreamFullInfo>()
/*      */     {
/* 1690 */       final Map<String, Builder> mappingFunctions = createDecoderMap();
/*      */ 
/*      */       
/*      */       private Map<String, Builder> createDecoderMap() {
/* 1694 */         Map<String, Builder> tempMappingFunctions = new HashMap<>();
/* 1695 */         tempMappingFunctions.put("last-generated-id", BuilderFactory.STREAM_ENTRY_ID);
/* 1696 */         tempMappingFunctions.put("length", BuilderFactory.LONG);
/* 1697 */         tempMappingFunctions.put("radix-tree-keys", BuilderFactory.LONG);
/* 1698 */         tempMappingFunctions.put("radix-tree-nodes", BuilderFactory.LONG);
/* 1699 */         tempMappingFunctions.put("groups", BuilderFactory.STREAM_GROUP_FULL_INFO_LIST);
/* 1700 */         tempMappingFunctions.put("entries", BuilderFactory.STREAM_ENTRY_LIST);
/*      */         
/* 1702 */         return tempMappingFunctions;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public StreamFullInfo build(Object data) {
/* 1708 */         if (null == data) {
/* 1709 */           return null;
/*      */         }
/*      */         
/* 1712 */         List<Object> streamsEntries = (List<Object>)data;
/* 1713 */         Iterator<Object> iterator = streamsEntries.iterator();
/*      */         
/* 1715 */         return new StreamFullInfo(BuilderFactory.createMapFromDecodingFunctions(iterator, this.mappingFunctions));
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1720 */         return "StreamFullInfo";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1728 */   public static final Builder<StreamFullInfo> STREAM_INFO_FULL = STREAM_FULL_INFO;
/*      */   
/* 1730 */   public static final Builder<StreamPendingSummary> STREAM_PENDING_SUMMARY = new Builder<StreamPendingSummary>()
/*      */     {
/*      */       public StreamPendingSummary build(Object data)
/*      */       {
/* 1734 */         if (null == data) {
/* 1735 */           return null;
/*      */         }
/*      */         
/* 1738 */         List<Object> objectList = (List<Object>)data;
/* 1739 */         long total = ((Long)BuilderFactory.LONG.build(objectList.get(0))).longValue();
/* 1740 */         String minId = SafeEncoder.encode((byte[])objectList.get(1));
/* 1741 */         String maxId = SafeEncoder.encode((byte[])objectList.get(2));
/* 1742 */         List<List<Object>> consumerObjList = (List<List<Object>>)objectList.get(3);
/* 1743 */         Map<String, Long> map = new HashMap<>(consumerObjList.size());
/* 1744 */         for (List<Object> consumerObj : consumerObjList) {
/* 1745 */           map.put(SafeEncoder.encode((byte[])consumerObj.get(0)), Long.valueOf(Long.parseLong(SafeEncoder.encode((byte[])consumerObj.get(1)))));
/*      */         }
/* 1747 */         return new StreamPendingSummary(total, new StreamEntryID(minId), new StreamEntryID(maxId), map);
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1752 */         return "StreamPendingSummary";
/*      */       }
/*      */     };
/*      */ 
/*      */   
/* 1757 */   private static final List<Builder> BACKUP_BUILDERS_FOR_DECODING_FUNCTIONS = Arrays.asList(new Builder[] { STRING, LONG, DOUBLE });
/*      */ 
/*      */   
/*      */   private static Map<String, Object> createMapFromDecodingFunctions(Iterator<Object> iterator, Map<String, Builder> mappingFunctions) {
/* 1761 */     return createMapFromDecodingFunctions(iterator, mappingFunctions, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, Object> createMapFromDecodingFunctions(Iterator<Object> iterator, Map<String, Builder> mappingFunctions, Collection<Builder> backupBuilders) {
/* 1767 */     if (!iterator.hasNext()) {
/* 1768 */       return Collections.emptyMap();
/*      */     }
/*      */     
/* 1771 */     Map<String, Object> resultMap = new HashMap<>();
/* 1772 */     while (iterator.hasNext()) {
/* 1773 */       String mapKey; Object rawValue, tempObject = iterator.next();
/*      */ 
/*      */ 
/*      */       
/* 1777 */       if (tempObject instanceof KeyValue) {
/* 1778 */         KeyValue kv = (KeyValue)tempObject;
/* 1779 */         mapKey = STRING.build(kv.getKey());
/* 1780 */         rawValue = kv.getValue();
/*      */       } else {
/* 1782 */         mapKey = STRING.build(tempObject);
/* 1783 */         rawValue = iterator.next();
/*      */       } 
/*      */       
/* 1786 */       if (mappingFunctions.containsKey(mapKey)) {
/* 1787 */         resultMap.put(mapKey, ((Builder)mappingFunctions.get(mapKey)).build(rawValue)); continue;
/*      */       } 
/* 1789 */       Collection<Builder> builders = (backupBuilders != null) ? backupBuilders : mappingFunctions.values();
/* 1790 */       for (Builder b : builders) {
/*      */         try {
/* 1792 */           resultMap.put(mapKey, b.build(rawValue));
/*      */           break;
/* 1794 */         } catch (ClassCastException classCastException) {}
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1800 */     return resultMap;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/* 1805 */   public static final Builder<LCSMatchResult> STR_ALGO_LCS_RESULT_BUILDER = new Builder<LCSMatchResult>()
/*      */     {
/*      */       public LCSMatchResult build(Object data) {
/* 1808 */         if (data == null) {
/* 1809 */           return null;
/*      */         }
/*      */         
/* 1812 */         if (data instanceof byte[])
/* 1813 */           return new LCSMatchResult(BuilderFactory.STRING.build(data)); 
/* 1814 */         if (data instanceof Long) {
/* 1815 */           return new LCSMatchResult(((Long)BuilderFactory.LONG.build(data)).longValue());
/*      */         }
/* 1817 */         long len = 0L;
/* 1818 */         List<LCSMatchResult.MatchedPosition> matchedPositions = new ArrayList<>();
/*      */         
/* 1820 */         List<Object> objectList = (List<Object>)data;
/* 1821 */         if (objectList.get(0) instanceof KeyValue) {
/* 1822 */           Iterator<KeyValue> iterator = objectList.iterator();
/* 1823 */           while (iterator.hasNext()) {
/* 1824 */             KeyValue kv = iterator.next();
/* 1825 */             if ("matches".equalsIgnoreCase(BuilderFactory.STRING.build(kv.getKey()))) {
/* 1826 */               addMatchedPosition(matchedPositions, kv.getValue()); continue;
/* 1827 */             }  if ("len".equalsIgnoreCase(BuilderFactory.STRING.build(kv.getKey()))) {
/* 1828 */               len = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*      */             }
/*      */           } 
/*      */         } else {
/* 1832 */           for (int i = 0; i < objectList.size(); i += 2) {
/* 1833 */             if ("matches".equalsIgnoreCase(BuilderFactory.STRING.build(objectList.get(i)))) {
/* 1834 */               addMatchedPosition(matchedPositions, objectList.get(i + 1));
/* 1835 */             } else if ("len".equalsIgnoreCase(BuilderFactory.STRING.build(objectList.get(i)))) {
/* 1836 */               len = ((Long)BuilderFactory.LONG.build(objectList.get(i + 1))).longValue();
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 1841 */         return new LCSMatchResult(matchedPositions, len);
/*      */       }
/*      */ 
/*      */       
/*      */       private void addMatchedPosition(List<LCSMatchResult.MatchedPosition> matchedPositions, Object o) {
/* 1846 */         List<Object> matches = (List<Object>)o;
/* 1847 */         for (Object obj : matches) {
/* 1848 */           if (obj instanceof List) {
/* 1849 */             List<Object> positions = (List<Object>)obj;
/*      */ 
/*      */             
/* 1852 */             LCSMatchResult.Position a = new LCSMatchResult.Position(((Long)BuilderFactory.LONG.build(((List)positions.get(0)).get(0))).longValue(), ((Long)BuilderFactory.LONG.build(((List)positions.get(0)).get(1))).longValue());
/*      */ 
/*      */ 
/*      */             
/* 1856 */             LCSMatchResult.Position b = new LCSMatchResult.Position(((Long)BuilderFactory.LONG.build(((List)positions.get(1)).get(0))).longValue(), ((Long)BuilderFactory.LONG.build(((List)positions.get(1)).get(1))).longValue());
/*      */             
/* 1858 */             long matchLen = 0L;
/* 1859 */             if (positions.size() >= 3) {
/* 1860 */               matchLen = ((Long)BuilderFactory.LONG.build(positions.get(2))).longValue();
/*      */             }
/* 1862 */             matchedPositions.add(new LCSMatchResult.MatchedPosition(a, b, matchLen));
/*      */           } 
/*      */         } 
/*      */       }
/*      */     };
/*      */   
/* 1868 */   public static final Builder<Map<String, String>> STRING_MAP_FROM_PAIRS = new Builder<Map<String, String>>()
/*      */     {
/*      */       public Map<String, String> build(Object data)
/*      */       {
/* 1872 */         List list = (List)data;
/* 1873 */         if (list.isEmpty()) return Collections.emptyMap();
/*      */         
/* 1875 */         if (list.get(0) instanceof KeyValue) {
/* 1876 */           return (Map<String, String>)list.stream()
/* 1877 */             .collect(Collectors.toMap(kv -> (String)BuilderFactory.STRING.build(kv.getKey()), kv -> (String)BuilderFactory.STRING.build(kv.getValue())));
/*      */         }
/*      */ 
/*      */         
/* 1881 */         Map<String, String> map = new HashMap<>(list.size());
/* 1882 */         for (Object object : list) {
/* 1883 */           if (object == null)
/* 1884 */             continue;  List<Object> flat = (List<Object>)object;
/* 1885 */           if (flat.isEmpty())
/* 1886 */             continue;  map.put(BuilderFactory.STRING.build(flat.get(0)), BuilderFactory.STRING.build(flat.get(1)));
/*      */         } 
/* 1888 */         return map;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1893 */         return "Map<String, String>";
/*      */       }
/*      */     };
/*      */   
/* 1897 */   public static final Builder<Map<String, Object>> ENCODED_OBJECT_MAP_FROM_PAIRS = new Builder<Map<String, Object>>()
/*      */     {
/*      */       public Map<String, Object> build(Object data)
/*      */       {
/* 1901 */         List list = (List)data;
/* 1902 */         if (list.isEmpty()) return Collections.emptyMap();
/*      */         
/* 1904 */         if (list.get(0) instanceof KeyValue) {
/* 1905 */           return (Map<String, Object>)list.stream()
/* 1906 */             .collect(Collectors.toMap(kv -> (String)BuilderFactory.STRING.build(kv.getKey()), kv -> BuilderFactory.ENCODED_OBJECT.build(kv.getValue())));
/*      */         }
/*      */ 
/*      */         
/* 1910 */         Map<String, Object> map = new HashMap<>(list.size());
/* 1911 */         for (Object object : list) {
/* 1912 */           if (object == null)
/* 1913 */             continue;  List<Object> flat = (List<Object>)object;
/* 1914 */           if (flat.isEmpty())
/* 1915 */             continue;  map.put(BuilderFactory.STRING.build(flat.get(0)), BuilderFactory.STRING.build(flat.get(1)));
/*      */         } 
/* 1917 */         return map;
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1922 */         return "Map<String, String>";
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/* 1930 */   public static final Builder<List<LibraryInfo>> LIBRARY_LIST = LibraryInfo.LIBRARY_INFO_LIST;
/*      */   
/* 1932 */   public static final Builder<List<List<String>>> STRING_LIST_LIST = new Builder<List<List<String>>>()
/*      */     {
/*      */       public List<List<String>> build(Object data)
/*      */       {
/* 1936 */         if (null == data) return null; 
/* 1937 */         return (List<List<String>>)((List)data).stream().map(BuilderFactory.STRING_LIST::build).collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1942 */         return "List<List<String>>";
/*      */       }
/*      */     };
/*      */   
/* 1946 */   public static final Builder<List<List<Object>>> ENCODED_OBJECT_LIST_LIST = new Builder<List<List<Object>>>()
/*      */     {
/*      */       public List<List<Object>> build(Object data)
/*      */       {
/* 1950 */         if (null == data) return null; 
/* 1951 */         return (List<List<Object>>)((List)data).stream().map(BuilderFactory.ENCODED_OBJECT_LIST::build).collect(Collectors.toList());
/*      */       }
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1956 */         return "List<List<Object>>";
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*      */   protected static class SetFromList<E>
/*      */     extends AbstractSet<E>
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -2850347066962734052L;
/*      */     
/*      */     private final List<E> list;
/*      */ 
/*      */     
/*      */     private SetFromList(List<E> list) {
/* 1971 */       this.list = list;
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1976 */       this.list.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1981 */       return this.list.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1986 */       return this.list.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1991 */       return this.list.contains(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 1996 */       return this.list.remove(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean add(E e) {
/* 2001 */       return (!contains(e) && this.list.add(e));
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<E> iterator() {
/* 2006 */       return this.list.iterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public Object[] toArray() {
/* 2011 */       return this.list.toArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public <T> T[] toArray(T[] a) {
/* 2016 */       return this.list.toArray(a);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2021 */       return this.list.toString();
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2026 */       return this.list.hashCode();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object o) {
/* 2031 */       if (o == null) return false; 
/* 2032 */       if (o == this) return true; 
/* 2033 */       if (!(o instanceof Set)) return false;
/*      */       
/* 2035 */       Collection<?> c = (Collection)o;
/* 2036 */       if (c.size() != size()) {
/* 2037 */         return false;
/*      */       }
/*      */       
/* 2040 */       return containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsAll(Collection<?> c) {
/* 2045 */       return this.list.containsAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean removeAll(Collection<?> c) {
/* 2050 */       return this.list.removeAll(c);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean retainAll(Collection<?> c) {
/* 2055 */       return this.list.retainAll(c);
/*      */     }
/*      */     
/*      */     protected static <E> SetFromList<E> of(List<E> list) {
/* 2059 */       if (list == null) {
/* 2060 */         return null;
/*      */       }
/* 2062 */       return new SetFromList<>(list);
/*      */     }
/*      */   }
/*      */   
/*      */   private BuilderFactory() {
/* 2067 */     throw new InstantiationError("Must not instantiate this class");
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\BuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */