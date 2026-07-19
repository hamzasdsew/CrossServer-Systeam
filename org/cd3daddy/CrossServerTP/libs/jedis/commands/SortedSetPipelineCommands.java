/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
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
/*     */ public interface SortedSetPipelineCommands
/*     */ {
/*     */   Response<Long> zadd(String paramString1, double paramDouble, String paramString2);
/*     */   
/*     */   Response<Long> zadd(String paramString1, double paramDouble, String paramString2, ZAddParams paramZAddParams);
/*     */   
/*     */   Response<Long> zadd(String paramString, Map<String, Double> paramMap);
/*     */   
/*     */   Response<Long> zadd(String paramString, Map<String, Double> paramMap, ZAddParams paramZAddParams);
/*     */   
/*     */   Response<Double> zaddIncr(String paramString1, double paramDouble, String paramString2, ZAddParams paramZAddParams);
/*     */   
/*     */   Response<Long> zrem(String paramString, String... paramVarArgs);
/*     */   
/*     */   Response<Double> zincrby(String paramString1, double paramDouble, String paramString2);
/*     */   
/*     */   Response<Double> zincrby(String paramString1, double paramDouble, String paramString2, ZIncrByParams paramZIncrByParams);
/*     */   
/*     */   Response<Long> zrank(String paramString1, String paramString2);
/*     */   
/*     */   Response<Long> zrevrank(String paramString1, String paramString2);
/*     */   
/*     */   Response<KeyValue<Long, Double>> zrankWithScore(String paramString1, String paramString2);
/*     */   
/*     */   Response<KeyValue<Long, Double>> zrevrankWithScore(String paramString1, String paramString2);
/*     */   
/*     */   Response<List<String>> zrange(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<List<String>> zrevrange(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<List<Tuple>> zrangeWithScores(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeWithScores(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<String> zrandmember(String paramString);
/*     */   
/*     */   Response<List<String>> zrandmember(String paramString, long paramLong);
/*     */   
/*     */   Response<List<Tuple>> zrandmemberWithScores(String paramString, long paramLong);
/*     */   
/*     */   Response<Long> zcard(String paramString);
/*     */   
/*     */   Response<Double> zscore(String paramString1, String paramString2);
/*     */   
/*     */   Response<List<Double>> zmscore(String paramString, String... paramVarArgs);
/*     */   
/*     */   Response<Tuple> zpopmax(String paramString);
/*     */   
/*     */   Response<List<Tuple>> zpopmax(String paramString, int paramInt);
/*     */   
/*     */   Response<Tuple> zpopmin(String paramString);
/*     */   
/*     */   Response<List<Tuple>> zpopmin(String paramString, int paramInt);
/*     */   
/*     */   Response<Long> zcount(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<Long> zcount(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   default Response<ScanResult<Tuple>> zscan(String key, String cursor) {
/* 129 */     return zscan(key, cursor, new ScanParams());
/*     */   }
/*     */   
/*     */   Response<List<String>> zrangeByScore(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<String>> zrangeByScore(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<List<String>> zrevrangeByScore(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<String>> zrangeByScore(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<String>> zrevrangeByScore(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<List<String>> zrangeByScore(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<String>> zrevrangeByScore(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<String>> zrevrangeByScore(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(String paramString, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<String>> zrange(String paramString, ZRangeParams paramZRangeParams);
/*     */   
/*     */   Response<List<Tuple>> zrangeWithScores(String paramString, ZRangeParams paramZRangeParams);
/*     */   
/*     */   Response<Long> zrangestore(String paramString1, String paramString2, ZRangeParams paramZRangeParams);
/*     */   
/*     */   Response<Long> zremrangeByRank(String paramString, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<Long> zremrangeByScore(String paramString, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<Long> zremrangeByScore(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<Long> zlexcount(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<List<String>> zrangeByLex(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<List<String>> zrangeByLex(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<String>> zrevrangeByLex(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<List<String>> zrevrangeByLex(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<Long> zremrangeByLex(String paramString1, String paramString2, String paramString3);
/*     */   
/*     */   Response<ScanResult<Tuple>> zscan(String paramString1, String paramString2, ScanParams paramScanParams);
/*     */   
/*     */   Response<KeyValue<String, Tuple>> bzpopmax(double paramDouble, String... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<String, Tuple>> bzpopmin(double paramDouble, String... paramVarArgs);
/*     */   
/*     */   Response<List<String>> zdiff(String... paramVarArgs);
/*     */   
/*     */   Response<List<Tuple>> zdiffWithScores(String... paramVarArgs);
/*     */   
/*     */   @Deprecated
/*     */   Response<Long> zdiffStore(String paramString, String... paramVarArgs);
/*     */   
/*     */   Response<Long> zdiffstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   Response<Long> zinterstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   Response<Long> zinterstore(String paramString, ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   Response<List<String>> zinter(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   Response<List<Tuple>> zinterWithScores(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   Response<Long> zintercard(String... paramVarArgs);
/*     */   
/*     */   Response<Long> zintercard(long paramLong, String... paramVarArgs);
/*     */   
/*     */   Response<List<String>> zunion(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   Response<List<Tuple>> zunionWithScores(ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   Response<Long> zunionstore(String paramString, String... paramVarArgs);
/*     */   
/*     */   Response<Long> zunionstore(String paramString, ZParams paramZParams, String... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<String, List<Tuple>>> zmpop(SortedSetOption paramSortedSetOption, String... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<String, List<Tuple>>> zmpop(SortedSetOption paramSortedSetOption, int paramInt, String... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<String, List<Tuple>>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, String... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<String, List<Tuple>>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, int paramInt, String... paramVarArgs);
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SortedSetPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */