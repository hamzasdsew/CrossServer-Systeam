/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonSetParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path2;
/*    */ import org.json.JSONArray;
/*    */ 
/*    */ public interface RedisJsonV2PipelineCommands
/*    */ {
/*    */   default Response<String> jsonSet(String key, Object object) {
/* 12 */     return jsonSet(key, Path2.ROOT_PATH, object);
/*    */   }
/*    */   
/*    */   default Response<String> jsonSetWithEscape(String key, Object object) {
/* 16 */     return jsonSetWithEscape(key, Path2.ROOT_PATH, object);
/*    */   }
/*    */   
/*    */   default Response<String> jsonSet(String key, Object object, JsonSetParams params) {
/* 20 */     return jsonSet(key, Path2.ROOT_PATH, object, params);
/*    */   }
/*    */   
/*    */   default Response<String> jsonSetWithEscape(String key, Object object, JsonSetParams params) {
/* 24 */     return jsonSetWithEscape(key, Path2.ROOT_PATH, object, params);
/*    */   }
/*    */   
/*    */   Response<String> jsonSet(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   Response<String> jsonSetWithEscape(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   Response<String> jsonSet(String paramString, Path2 paramPath2, Object paramObject, JsonSetParams paramJsonSetParams);
/*    */   
/*    */   Response<String> jsonSetWithEscape(String paramString, Path2 paramPath2, Object paramObject, JsonSetParams paramJsonSetParams);
/*    */   
/*    */   Response<String> jsonMerge(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   Response<Object> jsonGet(String paramString);
/*    */   
/*    */   Response<Object> jsonGet(String paramString, Path2... paramVarArgs);
/*    */   
/*    */   Response<List<JSONArray>> jsonMGet(String... keys) {
/* 42 */     return jsonMGet(Path2.ROOT_PATH, keys);
/*    */   }
/*    */   
/*    */   Response<List<JSONArray>> jsonMGet(Path2 paramPath2, String... paramVarArgs);
/*    */   
/*    */   Response<Long> jsonDel(String paramString);
/*    */   
/*    */   Response<Long> jsonDel(String paramString, Path2 paramPath2);
/*    */   
/*    */   Response<Long> jsonClear(String paramString);
/*    */   
/*    */   Response<Long> jsonClear(String paramString, Path2 paramPath2);
/*    */   
/*    */   Response<List<Boolean>> jsonToggle(String paramString, Path2 paramPath2);
/*    */   
/*    */   Response<List<Class<?>>> jsonType(String paramString, Path2 paramPath2);
/*    */   
/*    */   Response<List<Long>> jsonStrAppend(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   Response<List<Long>> jsonStrLen(String paramString, Path2 paramPath2);
/*    */   
/*    */   Response<Object> jsonNumIncrBy(String paramString, Path2 paramPath2, double paramDouble);
/*    */   
/*    */   Response<List<Long>> jsonArrAppend(String paramString, Path2 paramPath2, Object... paramVarArgs);
/*    */   
/*    */   Response<List<Long>> jsonArrAppendWithEscape(String paramString, Path2 paramPath2, Object... paramVarArgs);
/*    */   
/*    */   Response<List<Long>> jsonArrIndex(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   Response<List<Long>> jsonArrIndexWithEscape(String paramString, Path2 paramPath2, Object paramObject);
/*    */   
/*    */   Response<List<Long>> jsonArrInsert(String paramString, Path2 paramPath2, int paramInt, Object... paramVarArgs);
/*    */   
/*    */   Response<List<Long>> jsonArrInsertWithEscape(String paramString, Path2 paramPath2, int paramInt, Object... paramVarArgs);
/*    */   
/*    */   Response<List<Object>> jsonArrPop(String paramString, Path2 paramPath2);
/*    */   
/*    */   Response<List<Object>> jsonArrPop(String paramString, Path2 paramPath2, int paramInt);
/*    */   
/*    */   Response<List<Long>> jsonArrLen(String paramString, Path2 paramPath2);
/*    */   
/*    */   Response<List<Long>> jsonArrTrim(String paramString, Path2 paramPath2, int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\commands\RedisJsonV2PipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */