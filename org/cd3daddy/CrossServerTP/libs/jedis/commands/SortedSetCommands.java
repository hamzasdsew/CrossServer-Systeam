/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortedSetOption;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZAddParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZIncrByParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZRangeParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
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
/*     */ public interface SortedSetCommands
/*     */ {
/*     */   long zadd(String paramString1, double paramDouble, String paramString2);
/*     */   
/*     */   long zadd(String paramString1, double paramDouble, String paramString2, ZAddParams paramZAddParams);
/*     */   
/*     */   long zadd(String paramString, Map<String, Double> paramMap);
/*     */   
/*     */   long zadd(String paramString, Map<String, Double> paramMap, ZAddParams paramZAddParams);
/*     */   
/*     */   Double zaddIncr(String paramString1, double paramDouble, String paramString2, ZAddParams paramZAddParams);
/*     */   
/*     */   long zrem(String paramString, String... paramVarArgs);
/*     */   
/*     */   double zincrby(String paramString1, double paramDouble, String paramString2);
/*     */   
/*     */   Double zincrby(String paramString1, double paramDouble, String paramString2, ZIncrByParams paramZIncrByParams);
/*     */   
/*     */   Long zrank(String paramString1, String paramString2);
/*     */   
/*     */   Long zrevrank(String paramString1, String paramString2);
/*     */   
/*     */   KeyValue<Long, Double> zrankWithScore(String paramString1, String paramString2);
/*     */   
/*     */   KeyValue<Long, Double> zrevrankWithScore(String paramString1, String paramString2);
/*     */   
/*     */   List<String> zrange(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   List<String> zrevrange(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   List<Tuple> zrangeWithScores(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   List<Tuple> zrevrangeWithScores(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   List<String> zrange(String paramString, ZRangeParams paramZRangeParams);
/*     */   
/*     */   List<Tuple> zrangeWithScores(String paramString, ZRangeParams paramZRangeParams);
/*     */   
/*     */   long zrangestore(String paramString1, String paramString2, ZRangeParams paramZRangeParams);
/*     */   
/*     */   String zrandmember(String paramString);
/*     */   
/*     */   List<String> zrandmember(String paramString, long paramLong);
/*     */   
/*     */   List<Tuple> zrandmemberWithScores(String paramString, long paramLong);
/*     */   
/*     */   long zcard(String paramString);
/*     */   
/*     */   Double zscore(String paramString1, String paramString2);
/*     */   
/*     */   List<Double> zmscore(String paramString, String... paramVarArgs);
/*     */   
/*     */   Tuple zpopmax(String paramString);
/*     */   
/*     */   List<Tuple> zpopmax(String paramString, int paramInt);
/*     */   
/*     */   Tuple zpopmin(String paramString);
/*     */   
/*     */   List<Tuple> zpopmin(String paramString, int paramInt);
/*     */   
/*     */   long zcount(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   long zcount(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<String> zrangeByScore(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<String> zrangeByScore(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<String> zrevrangeByScore(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<String> zrangeByScore(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<String> zrevrangeByScore(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<String> zrangeByScore(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<String> zrevrangeByScore(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<String> zrevrangeByScore(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   long zremrangeByRank(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   long zremrangeByScore(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   long zremrangeByScore(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   long zlexcount(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<String> zrangeByLex(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<String> zrangeByLex(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<String> zrevrangeByLex(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   List<String> zrevrangeByLex(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   long zremrangeByLex(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   default ScanResult<Tuple> zscan(String key, String cursor) {
/* 659 */     return zscan(key, cursor, new ScanParams());
/*     */   }
/*     */   
/*     */   ScanResult<Tuple> zscan(String paramString1, String paramString2, ScanParams paramScanParams);
/*     */   
/*     */   KeyValue<String, Tuple> bzpopmax(double paramDouble, String... paramVarArgs);
/*     */   
/*     */   KeyValue<String, Tuple> bzpopmin(double paramDouble, String... paramVarArgs);
/*     */   
/*     */   List<String> zdiff(String... paramVarArgs);
/*     */   
/*     */   List<Tuple> zdiffWithScores(String... paramVarArgs);
/*     */   
/*     */   @Deprecated
/*     */   long zdiffStore(String paramString, String... paramVarArgs);
/*     */   
/*     */   long zdiffstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   List<String> zinter(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   List<Tuple> zinterWithScores(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   long zinterstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   long zinterstore(String paramString, ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   long zintercard(String... paramVarArgs);
/*     */   
/*     */   long zintercard(long paramLong, String... paramVarArgs);
/*     */   
/*     */   List<String> zunion(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   List<Tuple> zunionWithScores(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   long zunionstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   long zunionstore(String paramString, ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   KeyValue<String, List<Tuple>> zmpop(SortedSetOption paramSortedSetOption, String... paramVarArgs);
/*     */   
/*     */   KeyValue<String, List<Tuple>> zmpop(SortedSetOption paramSortedSetOption, int paramInt, String... paramVarArgs);
/*     */   
/*     */   KeyValue<String, List<Tuple>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, String... paramVarArgs);
/*     */   
/*     */   KeyValue<String, List<Tuple>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, int paramInt, String... paramVarArgs);
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SortedSetCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */