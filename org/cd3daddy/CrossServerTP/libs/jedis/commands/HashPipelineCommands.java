/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*    */ 
/*    */ 
/*    */ public interface HashPipelineCommands
/*    */ {
/*    */   Response<Long> hset(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   Response<Long> hset(String paramString, Map<String, String> paramMap);
/*    */   
/*    */   Response<String> hget(String paramString1, String paramString2);
/*    */   
/*    */   Response<Long> hsetnx(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   Response<String> hmset(String paramString, Map<String, String> paramMap);
/*    */   
/*    */   Response<List<String>> hmget(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Long> hincrBy(String paramString1, String paramString2, long paramLong);
/*    */   
/*    */   Response<Double> hincrByFloat(String paramString1, String paramString2, double paramDouble);
/*    */   
/*    */   Response<Boolean> hexists(String paramString1, String paramString2);
/*    */   
/*    */   Response<Long> hdel(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Long> hlen(String paramString);
/*    */   
/*    */   Response<Set<String>> hkeys(String paramString);
/*    */   
/*    */   Response<List<String>> hvals(String paramString);
/*    */   
/*    */   Response<Map<String, String>> hgetAll(String paramString);
/*    */   
/*    */   Response<String> hrandfield(String paramString);
/*    */   
/*    */   Response<List<String>> hrandfield(String paramString, long paramLong);
/*    */   
/*    */   Response<List<Map.Entry<String, String>>> hrandfieldWithValues(String paramString, long paramLong);
/*    */   
/*    */   default Response<ScanResult<Map.Entry<String, String>>> hscan(String key, String cursor) {
/* 48 */     return hscan(key, cursor, new ScanParams());
/*    */   }
/*    */   
/*    */   Response<ScanResult<Map.Entry<String, String>>> hscan(String paramString1, String paramString2, ScanParams paramScanParams);
/*    */   
/*    */   Response<Long> hstrlen(String paramString1, String paramString2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HashPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */