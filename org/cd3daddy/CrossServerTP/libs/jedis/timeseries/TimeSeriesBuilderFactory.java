/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*     */ 
/*     */ import java.util.AbstractMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ public final class TimeSeriesBuilderFactory {
/*  15 */   public static final Builder<TSElement> TIMESERIES_ELEMENT = new Builder<TSElement>()
/*     */     {
/*     */       public TSElement build(Object data) {
/*  18 */         List<Object> list = (List<Object>)data;
/*  19 */         if (list == null || list.isEmpty()) return null; 
/*  20 */         return new TSElement(((Long)BuilderFactory.LONG.build(list.get(0))).longValue(), ((Double)BuilderFactory.DOUBLE.build(list.get(1))).doubleValue());
/*     */       }
/*     */     };
/*     */   
/*  24 */   public static final Builder<List<TSElement>> TIMESERIES_ELEMENT_LIST = new Builder<List<TSElement>>()
/*     */     {
/*     */       public List<TSElement> build(Object data) {
/*  27 */         return (List<TSElement>)((List)data).stream().map(pairObject -> (List)pairObject)
/*  28 */           .map(pairList -> new TSElement(((Long)BuilderFactory.LONG.build(pairList.get(0))).longValue(), ((Double)BuilderFactory.DOUBLE.build(pairList.get(1))).doubleValue()))
/*     */           
/*  30 */           .collect(Collectors.toList());
/*     */       }
/*     */     };
/*     */   
/*  34 */   public static final Builder<Map<String, TSMRangeElements>> TIMESERIES_MRANGE_RESPONSE = new Builder<Map<String, TSMRangeElements>>()
/*     */     {
/*     */       public Map<String, TSMRangeElements> build(Object data)
/*     */       {
/*  38 */         return (Map<String, TSMRangeElements>)((List)data).stream().map(tsObject -> (List)tsObject)
/*  39 */           .map(tsList -> new TSMRangeElements((String)BuilderFactory.STRING.build(tsList.get(0)), (Map<String, String>)BuilderFactory.STRING_MAP_FROM_PAIRS.build(tsList.get(1)), (List<TSElement>)TimeSeriesBuilderFactory.TIMESERIES_ELEMENT_LIST.build(tsList.get(2))))
/*     */ 
/*     */           
/*  42 */           .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, Function.identity(), (x, y) -> x, LinkedHashMap::new));
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*  47 */   public static final Builder<Map<String, TSMRangeElements>> TIMESERIES_MRANGE_RESPONSE_RESP3 = new Builder<Map<String, TSMRangeElements>>()
/*     */     {
/*     */       public Map<String, TSMRangeElements> build(Object data)
/*     */       {
/*  51 */         List<KeyValue> dataList = (List<KeyValue>)data;
/*  52 */         Map<String, TSMRangeElements> map = new LinkedHashMap<>(dataList.size() / 2, 1.0F);
/*  53 */         for (KeyValue kv : dataList) {
/*  54 */           TSMRangeElements elements; List<Object> aggrMapObj; KeyValue aggKV; List<KeyValue> rdcMapObj, srcMapObj; String key = (String)BuilderFactory.STRING.build(kv.getKey());
/*  55 */           List<Object> valueList = (List<Object>)kv.getValue();
/*     */           
/*  57 */           switch (valueList.size()) {
/*     */             case 3:
/*  59 */               aggrMapObj = (List<Object>)valueList.get(1);
/*  60 */               aggKV = (KeyValue)aggrMapObj.get(0);
/*  61 */               assert "aggregators".equalsIgnoreCase((String)BuilderFactory.STRING.build(aggKV.getKey()));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  66 */               elements = new TSMRangeElements(key, (Map<String, String>)BuilderFactory.STRING_MAP.build(valueList.get(0)), (List<AggregationType>)((List)aggKV.getValue()).stream().map(BuilderFactory.STRING::build).map(AggregationType::safeValueOf).collect(Collectors.toList()), (List<TSElement>)TimeSeriesBuilderFactory.TIMESERIES_ELEMENT_LIST.build(valueList.get(2)));
/*     */               break;
/*     */             case 4:
/*  69 */               rdcMapObj = (List<KeyValue>)valueList.get(1);
/*  70 */               assert "reducers".equalsIgnoreCase((String)BuilderFactory.STRING.build(((KeyValue)rdcMapObj.get(0)).getKey()));
/*  71 */               srcMapObj = (List<KeyValue>)valueList.get(2);
/*  72 */               assert "sources".equalsIgnoreCase((String)BuilderFactory.STRING.build(((KeyValue)srcMapObj.get(0)).getKey()));
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  77 */               elements = new TSMRangeElements(key, (Map<String, String>)BuilderFactory.STRING_MAP.build(valueList.get(0)), (List<String>)BuilderFactory.STRING_LIST.build(((KeyValue)rdcMapObj.get(0)).getValue()), (List<String>)BuilderFactory.STRING_LIST.build(((KeyValue)srcMapObj.get(0)).getValue()), (List<TSElement>)TimeSeriesBuilderFactory.TIMESERIES_ELEMENT_LIST.build(valueList.get(3)));
/*     */               break;
/*     */             default:
/*  80 */               throw new IllegalStateException();
/*     */           } 
/*  82 */           map.put(key, elements);
/*     */         } 
/*  84 */         return map;
/*     */       }
/*     */     };
/*     */   
/*  88 */   public static final Builder<Map<String, TSMGetElement>> TIMESERIES_MGET_RESPONSE = new Builder<Map<String, TSMGetElement>>()
/*     */     {
/*     */       public Map<String, TSMGetElement> build(Object data)
/*     */       {
/*  92 */         return (Map<String, TSMGetElement>)((List)data).stream().map(tsObject -> (List)tsObject)
/*  93 */           .map(tsList -> new TSMGetElement((String)BuilderFactory.STRING.build(tsList.get(0)), (Map<String, String>)BuilderFactory.STRING_MAP_FROM_PAIRS.build(tsList.get(1)), (TSElement)TimeSeriesBuilderFactory.TIMESERIES_ELEMENT.build(tsList.get(2))))
/*     */ 
/*     */           
/*  96 */           .collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, Function.identity()));
/*     */       }
/*     */     };
/*     */   
/* 100 */   public static final Builder<Map<String, TSMGetElement>> TIMESERIES_MGET_RESPONSE_RESP3 = new Builder<Map<String, TSMGetElement>>()
/*     */     {
/*     */       public Map<String, TSMGetElement> build(Object data)
/*     */       {
/* 104 */         List<KeyValue> dataList = (List<KeyValue>)data;
/* 105 */         Map<String, TSMGetElement> map = new LinkedHashMap<>(dataList.size());
/* 106 */         for (KeyValue kv : dataList) {
/* 107 */           String key = (String)BuilderFactory.STRING.build(kv.getKey());
/* 108 */           List<Object> valueList = (List<Object>)kv.getValue();
/*     */ 
/*     */           
/* 111 */           TSMGetElement value = new TSMGetElement(key, (Map<String, String>)BuilderFactory.STRING_MAP.build(valueList.get(0)), (TSElement)TimeSeriesBuilderFactory.TIMESERIES_ELEMENT.build(valueList.get(1)));
/* 112 */           map.put(key, value);
/*     */         } 
/* 114 */         return map;
/*     */       }
/*     */     };
/*     */   
/*     */   private TimeSeriesBuilderFactory() {
/* 119 */     throw new InstantiationError("Must not instantiate this class");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TimeSeriesBuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */