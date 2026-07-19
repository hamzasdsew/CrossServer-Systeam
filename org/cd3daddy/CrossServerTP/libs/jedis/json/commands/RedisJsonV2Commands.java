/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonSetParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path2;
/*    */ import org.json.JSONArray;
/*    */ 
/*    */ public interface RedisJsonV2Commands
/*    */ {
/*    */   default String jsonSet(String key, Object object) {
/* 11 */     return jsonSet(key, Path2.ROOT_PATH, object);
/*    */   }
/*    */   
/*    */   default String jsonSetWithEscape(String key, Object object) {
/* 15 */     return jsonSetWithEscape(key, Path2.ROOT_PATH, object);
/*    */   }
/*    */   
/*    */   default String jsonSet(String key, Object object, JsonSetParams params) {
/* 19 */     return jsonSet(key, Path2.ROOT_PATH, object, params);
/*    */   }
/*    */   
/*    */   default String jsonSetWithEscape(String key, Object object, JsonSetParams params) {
/* 23 */     return jsonSetWithEscape(key, Path2.ROOT_PATH, object, params);
/*    */   }
/*    */   
/*    */   String jsonSet(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   String jsonSetWithEscape(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   String jsonSet(String paramString, Path2 paramPath2, Object paramObject, JsonSetParams paramJsonSetParams);
/*    */   
/*    */   String jsonSetWithEscape(String paramString, Path2 paramPath2, Object paramObject, JsonSetParams paramJsonSetParams);
/*    */   
/*    */   String jsonMerge(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   Object jsonGet(String paramString);
/*    */   
/*    */   Object jsonGet(String paramString, Path2... paramVarArgs);
/*    */   
/*    */   List<JSONArray> jsonMGet(String... keys) {
/* 41 */     return jsonMGet(Path2.ROOT_PATH, keys);
/*    */   }
/*    */   
/*    */   List<JSONArray> jsonMGet(Path2 paramPath2, String... paramVarArgs);
/*    */   
/*    */   long jsonDel(String paramString);
/*    */   
/*    */   long jsonDel(String paramString, Path2 paramPath2);
/*    */   
/*    */   long jsonClear(String paramString);
/*    */   
/*    */   long jsonClear(String paramString, Path2 paramPath2);
/*    */   
/*    */   List<Boolean> jsonToggle(String paramString, Path2 paramPath2);
/*    */   
/*    */   List<Class<?>> jsonType(String paramString, Path2 paramPath2);
/*    */   
/*    */   List<Long> jsonStrAppend(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   List<Long> jsonStrLen(String paramString, Path2 paramPath2);
/*    */   
/*    */   Object jsonNumIncrBy(String paramString, Path2 paramPath2, double paramDouble);
/*    */   
/*    */   List<Long> jsonArrAppend(String paramString, Path2 paramPath2, Object... paramVarArgs);
/*    */   
/*    */   List<Long> jsonArrAppendWithEscape(String paramString, Path2 paramPath2, Object... paramVarArgs);
/*    */   
/*    */   List<Long> jsonArrIndex(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   List<Long> jsonArrIndexWithEscape(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   List<Long> jsonArrInsert(String paramString, Path2 paramPath2, int paramInt, Object... paramVarArgs);
/*    */   
/*    */   List<Long> jsonArrInsertWithEscape(String paramString, Path2 paramPath2, int paramInt, Object... paramVarArgs);
/*    */   
/*    */   List<Object> jsonArrPop(String paramString, Path2 paramPath2);
/*    */   
/*    */   List<Object> jsonArrPop(String paramString, Path2 paramPath2, int paramInt);
/*    */   
/*    */   List<Long> jsonArrLen(String paramString, Path2 paramPath2);
/*    */   
/*    */   List<Long> jsonArrTrim(String paramString, Path2 paramPath2, int paramInt1, int paramInt2);
/*    */   
/*    */   List<Long> jsonObjLen(String paramString, Path2 paramPath2);
/*    */   
/*    */   List<List<String>> jsonObjKeys(String paramString, Path2 paramPath2);
/*    */   
/*    */   List<Long> jsonDebugMemory(String paramString, Path2 paramPath2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\commands\RedisJsonV2Commands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */