/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonSetParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public interface RedisJsonV1PipelineCommands
/*    */ {
/*    */   @Deprecated
/*    */   default Response<String> jsonSetLegacy(String key, Object pojo) {
/* 16 */     return jsonSet(key, Path.ROOT_PATH, pojo);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   default Response<String> jsonSetLegacy(String key, Object pojo, JsonSetParams params) {
/* 21 */     return jsonSet(key, Path.ROOT_PATH, pojo, params);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   Response<String> jsonSet(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   Response<String> jsonSet(String paramString, Path paramPath, Object paramObject, JsonSetParams paramJsonSetParams);
/*    */   
/*    */   @Deprecated
/*    */   Response<String> jsonMerge(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   Response<Object> jsonGet(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   <T> Response<T> jsonGet(String paramString, Class<T> paramClass);
/*    */   
/*    */   @Deprecated
/*    */   Response<Object> jsonGet(String paramString, Path... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   <T> Response<T> jsonGet(String paramString, Class<T> paramClass, Path... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   <T> Response<List<T>> jsonMGet(Class<T> clazz, String... keys) {
/* 46 */     return jsonMGet(Path.ROOT_PATH, clazz, keys);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   <T> Response<List<T>> jsonMGet(Path paramPath, Class<T> paramClass, String... paramVarArgs);
/*    */   
/*    */   Response<Long> jsonDel(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonDel(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonClear(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonClear(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Response<String> jsonToggle(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Response<Class<?>> jsonType(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Response<Class<?>> jsonType(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonStrAppend(String paramString, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonStrAppend(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonStrLen(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonStrLen(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Response<Double> jsonNumIncrBy(String paramString, Path paramPath, double paramDouble);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonArrAppend(String paramString, Path paramPath, Object... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonArrIndex(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonArrInsert(String paramString, Path paramPath, int paramInt, Object... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   Response<Object> jsonArrPop(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   <T> Response<T> jsonArrPop(String paramString, Class<T> paramClass);
/*    */   
/*    */   @Deprecated
/*    */   Response<Object> jsonArrPop(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   <T> Response<T> jsonArrPop(String paramString, Class<T> paramClass, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Response<Object> jsonArrPop(String paramString, Path paramPath, int paramInt);
/*    */   
/*    */   @Deprecated
/*    */   <T> Response<T> jsonArrPop(String paramString, Class<T> paramClass, Path paramPath, int paramInt);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonArrLen(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonArrLen(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Response<Long> jsonArrTrim(String paramString, Path paramPath, int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\commands\RedisJsonV1PipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */