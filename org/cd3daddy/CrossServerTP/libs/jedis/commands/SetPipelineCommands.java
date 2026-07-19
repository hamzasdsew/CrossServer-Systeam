/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*    */ 
/*    */ 
/*    */ public interface SetPipelineCommands
/*    */ {
/*    */   Response<Long> sadd(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Set<String>> smembers(String paramString);
/*    */   
/*    */   Response<Long> srem(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<String> spop(String paramString);
/*    */   
/*    */   Response<Set<String>> spop(String paramString, long paramLong);
/*    */   
/*    */   Response<Long> scard(String paramString);
/*    */   
/*    */   Response<Boolean> sismember(String paramString1, String paramString2);
/*    */   
/*    */   Response<List<Boolean>> smismember(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<String> srandmember(String paramString);
/*    */   
/*    */   Response<List<String>> srandmember(String paramString, int paramInt);
/*    */   
/*    */   default Response<ScanResult<String>> sscan(String key, String cursor) {
/* 33 */     return sscan(key, cursor, new ScanParams());
/*    */   }
/*    */   
/*    */   Response<ScanResult<String>> sscan(String paramString1, String paramString2, ScanParams paramScanParams);
/*    */   
/*    */   Response<Set<String>> sdiff(String... paramVarArgs);
/*    */   
/*    */   Response<Long> sdiffStore(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Set<String>> sinter(String... paramVarArgs);
/*    */   
/*    */   Response<Long> sinterstore(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Long> sintercard(String... paramVarArgs);
/*    */   
/*    */   Response<Long> sintercard(int paramInt, String... paramVarArgs);
/*    */   
/*    */   Response<Set<String>> sunion(String... paramVarArgs);
/*    */   
/*    */   Response<Long> sunionstore(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Long> smove(String paramString1, String paramString2, String paramString3);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SetPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */