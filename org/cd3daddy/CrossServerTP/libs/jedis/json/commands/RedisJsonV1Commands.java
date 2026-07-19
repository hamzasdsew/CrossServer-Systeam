/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonSetParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public interface RedisJsonV1Commands
/*    */ {
/*    */   @Deprecated
/*    */   default String jsonSetLegacy(String key, Object pojo) {
/* 15 */     return jsonSet(key, Path.ROOT_PATH, pojo);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   default String jsonSetLegacy(String key, Object pojo, JsonSetParams params) {
/* 20 */     return jsonSet(key, Path.ROOT_PATH, pojo, params);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   String jsonSet(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   String jsonSetWithPlainString(String paramString1, Path paramPath, String paramString2);
/*    */   
/*    */   @Deprecated
/*    */   String jsonSet(String paramString, Path paramPath, Object paramObject, JsonSetParams paramJsonSetParams);
/*    */   
/*    */   @Deprecated
/*    */   String jsonMerge(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   Object jsonGet(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   <T> T jsonGet(String paramString, Class<T> paramClass);
/*    */   
/*    */   @Deprecated
/*    */   Object jsonGet(String paramString, Path... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   String jsonGetAsPlainString(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   <T> T jsonGet(String paramString, Class<T> paramClass, Path... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   <T> List<T> jsonMGet(Class<T> clazz, String... keys) {
/* 51 */     return jsonMGet(Path.ROOT_PATH, clazz, keys);
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   <T> List<T> jsonMGet(Path paramPath, Class<T> paramClass, String... paramVarArgs);
/*    */   
/*    */   long jsonDel(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   long jsonDel(String paramString, Path paramPath);
/*    */   
/*    */   long jsonClear(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   long jsonClear(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   String jsonToggle(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Class<?> jsonType(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Class<?> jsonType(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   long jsonStrAppend(String paramString, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   long jsonStrAppend(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonStrLen(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonStrLen(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   double jsonNumIncrBy(String paramString, Path paramPath, double paramDouble);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonArrAppend(String paramString, Path paramPath, Object... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   long jsonArrIndex(String paramString, Path paramPath, Object paramObject);
/*    */   
/*    */   @Deprecated
/*    */   long jsonArrInsert(String paramString, Path paramPath, int paramInt, Object... paramVarArgs);
/*    */   
/*    */   @Deprecated
/*    */   Object jsonArrPop(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   <T> T jsonArrPop(String paramString, Class<T> paramClass);
/*    */   
/*    */   @Deprecated
/*    */   Object jsonArrPop(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   <T> T jsonArrPop(String paramString, Class<T> paramClass, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Object jsonArrPop(String paramString, Path paramPath, int paramInt);
/*    */   
/*    */   @Deprecated
/*    */   <T> T jsonArrPop(String paramString, Class<T> paramClass, Path paramPath, int paramInt);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonArrLen(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonArrLen(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonArrTrim(String paramString, Path paramPath, int paramInt1, int paramInt2);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonObjLen(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Long jsonObjLen(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   List<String> jsonObjKeys(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   List<String> jsonObjKeys(String paramString, Path paramPath);
/*    */   
/*    */   @Deprecated
/*    */   long jsonDebugMemory(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   long jsonDebugMemory(String paramString, Path paramPath);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\commands\RedisJsonV1Commands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */