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
/*     */ public interface SortedSetPipelineBinaryCommands
/*     */ {
/*     */   Response<Long> zadd(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Response<Long> zadd(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2, ZAddParams paramZAddParams);
/*     */   
/*     */   Response<Long> zadd(byte[] paramArrayOfbyte, Map<byte[], Double> paramMap);
/*     */   
/*     */   Response<Long> zadd(byte[] paramArrayOfbyte, Map<byte[], Double> paramMap, ZAddParams paramZAddParams);
/*     */   
/*     */   Response<Double> zaddIncr(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2, ZAddParams paramZAddParams);
/*     */   
/*     */   Response<Long> zrem(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Double> zincrby(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Response<Double> zincrby(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2, ZIncrByParams paramZIncrByParams);
/*     */   
/*     */   Response<Long> zrank(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Response<Long> zrevrank(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Response<KeyValue<Long, Double>> zrankWithScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Response<KeyValue<Long, Double>> zrevrankWithScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Response<List<byte[]>> zrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<List<byte[]>> zrevrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<List<Tuple>> zrangeWithScores(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeWithScores(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<byte[]> zrandmember(byte[] paramArrayOfbyte);
/*     */   
/*     */   Response<List<byte[]>> zrandmember(byte[] paramArrayOfbyte, long paramLong);
/*     */   
/*     */   Response<List<Tuple>> zrandmemberWithScores(byte[] paramArrayOfbyte, long paramLong);
/*     */   
/*     */   Response<Long> zcard(byte[] paramArrayOfbyte);
/*     */   
/*     */   Response<Double> zscore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Response<List<Double>> zmscore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Tuple> zpopmax(byte[] paramArrayOfbyte);
/*     */   
/*     */   Response<List<Tuple>> zpopmax(byte[] paramArrayOfbyte, int paramInt);
/*     */   
/*     */   Response<Tuple> zpopmin(byte[] paramArrayOfbyte);
/*     */   
/*     */   Response<List<Tuple>> zpopmin(byte[] paramArrayOfbyte, int paramInt);
/*     */   
/*     */   Response<Long> zcount(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<Long> zcount(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   default Response<ScanResult<Tuple>> zscan(byte[] key, byte[] cursor) {
/* 129 */     return zscan(key, cursor, new ScanParams());
/*     */   }
/*     */   
/*     */   Response<List<byte[]>> zrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<byte[]>> zrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<List<byte[]>> zrevrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<byte[]>> zrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<byte[]>> zrevrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<List<byte[]>> zrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<byte[]>> zrevrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<byte[]>> zrevrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<List<Tuple>> zrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<Long> zremrangeByRank(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   Response<Long> zremrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   Response<Long> zremrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<Long> zlexcount(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<List<byte[]>> zrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<List<byte[]>> zrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<byte[]>> zrevrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<List<byte[]>> zrevrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   Response<List<byte[]>> zrange(byte[] paramArrayOfbyte, ZRangeParams paramZRangeParams);
/*     */   
/*     */   Response<List<Tuple>> zrangeWithScores(byte[] paramArrayOfbyte, ZRangeParams paramZRangeParams);
/*     */   
/*     */   Response<Long> zrangestore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ZRangeParams paramZRangeParams);
/*     */   
/*     */   Response<Long> zremrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   Response<ScanResult<Tuple>> zscan(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ScanParams paramScanParams);
/*     */   
/*     */   Response<KeyValue<byte[], Tuple>> bzpopmax(double paramDouble, byte[]... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<byte[], Tuple>> bzpopmin(double paramDouble, byte[]... paramVarArgs);
/*     */   
/*     */   Response<List<byte[]>> zdiff(byte[]... paramVarArgs);
/*     */   
/*     */   Response<List<Tuple>> zdiffWithScores(byte[]... paramVarArgs);
/*     */   
/*     */   @Deprecated
/*     */   Response<Long> zdiffStore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Long> zdiffstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   Response<List<byte[]>> zinter(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   Response<List<Tuple>> zinterWithScores(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Long> zinterstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Long> zinterstore(byte[] paramArrayOfbyte, ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Long> zintercard(byte[]... paramVarArgs);
/*     */   
/*     */   Response<Long> zintercard(long paramLong, byte[]... paramVarArgs);
/*     */   
/*     */   Response<List<byte[]>> zunion(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   Response<List<Tuple>> zunionWithScores(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Long> zunionstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   Response<Long> zunionstore(byte[] paramArrayOfbyte, ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<byte[], List<Tuple>>> zmpop(SortedSetOption paramSortedSetOption, byte[]... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<byte[], List<Tuple>>> zmpop(SortedSetOption paramSortedSetOption, int paramInt, byte[]... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<byte[], List<Tuple>>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, byte[]... paramVarArgs);
/*     */   
/*     */   Response<KeyValue<byte[], List<Tuple>>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, int paramInt, byte[]... paramVarArgs);
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SortedSetPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */