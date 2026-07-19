/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*    */ 
/*    */ 
/*    */ public interface HashCommands
/*    */ {
/*    */   long hset(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   long hset(String paramString, Map<String, String> paramMap);
/*    */   
/*    */   String hget(String paramString1, String paramString2);
/*    */   
/*    */   long hsetnx(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   String hmset(String paramString, Map<String, String> paramMap);
/*    */   
/*    */   List<String> hmget(String paramString, String... paramVarArgs);
/*    */   
/*    */   long hincrBy(String paramString1, String paramString2, long paramLong);
/*    */   
/*    */   double hincrByFloat(String paramString1, String paramString2, double paramDouble);
/*    */   
/*    */   boolean hexists(String paramString1, String paramString2);
/*    */   
/*    */   long hdel(String paramString, String... paramVarArgs);
/*    */   
/*    */   long hlen(String paramString);
/*    */   
/*    */   Set<String> hkeys(String paramString);
/*    */   
/*    */   List<String> hvals(String paramString);
/*    */   
/*    */   Map<String, String> hgetAll(String paramString);
/*    */   
/*    */   String hrandfield(String paramString);
/*    */   
/*    */   List<String> hrandfield(String paramString, long paramLong);
/*    */   
/*    */   List<Map.Entry<String, String>> hrandfieldWithValues(String paramString, long paramLong);
/*    */   
/*    */   default ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
/* 47 */     return hscan(key, cursor, new ScanParams());
/*    */   }
/*    */   
/*    */   ScanResult<Map.Entry<String, String>> hscan(String paramString1, String paramString2, ScanParams paramScanParams);
/*    */   
/*    */   long hstrlen(String paramString1, String paramString2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HashCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */