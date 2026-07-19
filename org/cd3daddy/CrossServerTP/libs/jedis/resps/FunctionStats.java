/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ public class FunctionStats
/*    */ {
/*    */   private final Map<String, Object> runningScript;
/*    */   private final Map<String, Map<String, Object>> engines;
/*    */   
/*    */   public FunctionStats(Map<String, Object> script, Map<String, Map<String, Object>> engines) {
/* 16 */     this.runningScript = script;
/* 17 */     this.engines = engines;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getRunningScript() {
/* 21 */     return this.runningScript;
/*    */   }
/*    */   
/*    */   public Map<String, Map<String, Object>> getEngines() {
/* 25 */     return this.engines;
/*    */   }
/*    */   
/* 28 */   public static final Builder<FunctionStats> FUNCTION_STATS_BUILDER = new Builder<FunctionStats>()
/*    */     {
/*    */       public FunctionStats build(Object data)
/*    */       {
/* 32 */         if (data == null) return null; 
/* 33 */         List<List<Object>> list = (List)data;
/* 34 */         if (list.isEmpty()) return null;
/*    */         
/* 36 */         if (list.get(0) instanceof KeyValue) {
/*    */           
/* 38 */           Map<String, Object> map = null;
/* 39 */           Map<String, Map<String, Object>> map1 = null;
/*    */           
/* 41 */           for (KeyValue kv : list) {
/* 42 */             List<KeyValue> ilist; switch ((String)BuilderFactory.STRING.build(kv.getKey())) {
/*    */               case "running_script":
/* 44 */                 map = (Map<String, Object>)BuilderFactory.ENCODED_OBJECT_MAP.build(kv.getValue());
/*    */               
/*    */               case "engines":
/* 47 */                 ilist = (List<KeyValue>)kv.getValue();
/* 48 */                 map1 = new LinkedHashMap<>(ilist.size());
/* 49 */                 for (KeyValue ikv : kv.getValue()) {
/* 50 */                   map1.put(BuilderFactory.STRING.build(ikv.getKey()), BuilderFactory.ENCODED_OBJECT_MAP
/* 51 */                       .build(ikv.getValue()));
/*    */                 }
/*    */             } 
/*    */ 
/*    */           
/*    */           } 
/* 57 */           return new FunctionStats(map, map1);
/*    */         } 
/*    */ 
/*    */         
/* 61 */         Map<String, Object> runningScriptMap = (list.get(1) == null) ? null : (Map<String, Object>)BuilderFactory.ENCODED_OBJECT_MAP.build(list.get(1));
/*    */         
/* 63 */         List<Object> enginesList = list.get(3);
/*    */         
/* 65 */         Map<String, Map<String, Object>> enginesMap = new LinkedHashMap<>(enginesList.size() / 2);
/* 66 */         for (int i = 0; i < enginesList.size(); i += 2) {
/* 67 */           enginesMap.put(BuilderFactory.STRING.build(enginesList.get(i)), BuilderFactory.ENCODED_OBJECT_MAP
/* 68 */               .build(enginesList.get(i + 1)));
/*    */         }
/*    */         
/* 71 */         return new FunctionStats(runningScriptMap, enginesMap);
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\FunctionStats.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */