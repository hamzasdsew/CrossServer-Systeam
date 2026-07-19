/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SetCommands
/*     */ {
/*     */   long sadd(String paramString, String... paramVarArgs);
/*     */   
/*     */   Set<String> smembers(String paramString);
/*     */   
/*     */   long srem(String paramString, String... paramVarArgs);
/*     */   
/*     */   String spop(String paramString);
/*     */   
/*     */   Set<String> spop(String paramString, long paramLong);
/*     */   
/*     */   long scard(String paramString);
/*     */   
/*     */   boolean sismember(String paramString1, String paramString2);
/*     */   
/*     */   List<Boolean> smismember(String paramString, String... paramVarArgs);
/*     */   
/*     */   String srandmember(String paramString);
/*     */   
/*     */   List<String> srandmember(String paramString, int paramInt);
/*     */   
/*     */   default ScanResult<String> sscan(String key, String cursor) {
/* 130 */     return sscan(key, cursor, new ScanParams());
/*     */   }
/*     */   
/*     */   ScanResult<String> sscan(String paramString1, String paramString2, ScanParams paramScanParams);
/*     */   
/*     */   Set<String> sdiff(String... paramVarArgs);
/*     */   
/*     */   long sdiffstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   Set<String> sinter(String... paramVarArgs);
/*     */   
/*     */   long sinterstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   long sintercard(String... paramVarArgs);
/*     */   
/*     */   long sintercard(int paramInt, String... paramVarArgs);
/*     */   
/*     */   Set<String> sunion(String... paramVarArgs);
/*     */   
/*     */   long sunionstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   long smove(String paramString1, String paramString2, String paramString3);
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SetCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */