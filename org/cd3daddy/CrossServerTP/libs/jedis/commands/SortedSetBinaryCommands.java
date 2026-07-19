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
/*     */ public interface SortedSetBinaryCommands
/*     */ {
/*     */   long zadd(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2);
/*     */   
/*     */   long zadd(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2, ZAddParams paramZAddParams);
/*     */   
/*     */   long zadd(byte[] paramArrayOfbyte, Map<byte[], Double> paramMap);
/*     */   
/*     */   long zadd(byte[] paramArrayOfbyte, Map<byte[], Double> paramMap, ZAddParams paramZAddParams);
/*     */   
/*     */   Double zaddIncr(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2, ZAddParams paramZAddParams);
/*     */   
/*     */   long zrem(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   double zincrby(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Double zincrby(byte[] paramArrayOfbyte1, double paramDouble, byte[] paramArrayOfbyte2, ZIncrByParams paramZIncrByParams);
/*     */   
/*     */   Long zrank(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   Long zrevrank(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   KeyValue<Long, Double> zrankWithScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   KeyValue<Long, Double> zrevrankWithScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   List<byte[]> zrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   List<byte[]> zrevrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   List<Tuple> zrangeWithScores(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   List<Tuple> zrevrangeWithScores(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   List<byte[]> zrange(byte[] paramArrayOfbyte, ZRangeParams paramZRangeParams);
/*     */   
/*     */   List<Tuple> zrangeWithScores(byte[] paramArrayOfbyte, ZRangeParams paramZRangeParams);
/*     */   
/*     */   long zrangestore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ZRangeParams paramZRangeParams);
/*     */   
/*     */   byte[] zrandmember(byte[] paramArrayOfbyte);
/*     */   
/*     */   List<byte[]> zrandmember(byte[] paramArrayOfbyte, long paramLong);
/*     */   
/*     */   List<Tuple> zrandmemberWithScores(byte[] paramArrayOfbyte, long paramLong);
/*     */   
/*     */   long zcard(byte[] paramArrayOfbyte);
/*     */   
/*     */   Double zscore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*     */   
/*     */   List<Double> zmscore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   Tuple zpopmax(byte[] paramArrayOfbyte);
/*     */   
/*     */   List<Tuple> zpopmax(byte[] paramArrayOfbyte, int paramInt);
/*     */   
/*     */   Tuple zpopmin(byte[] paramArrayOfbyte);
/*     */   
/*     */   default ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
/* 128 */     return zscan(key, cursor, new ScanParams());
/*     */   }
/*     */   
/*     */   List<Tuple> zpopmin(byte[] paramArrayOfbyte, int paramInt);
/*     */   
/*     */   long zcount(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   long zcount(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<byte[]> zrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<byte[]> zrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<byte[]> zrevrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<byte[]> zrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<byte[]> zrevrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<byte[]> zrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<byte[]> zrevrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<byte[]> zrevrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<Tuple> zrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2, int paramInt1, int paramInt2);
/*     */   
/*     */   List<Tuple> zrevrangeByScoreWithScores(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   long zremrangeByRank(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
/*     */   
/*     */   long zremrangeByScore(byte[] paramArrayOfbyte, double paramDouble1, double paramDouble2);
/*     */   
/*     */   long zremrangeByScore(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   long zlexcount(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<byte[]> zrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<byte[]> zrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   List<byte[]> zrevrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   List<byte[]> zrevrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt1, int paramInt2);
/*     */   
/*     */   long zremrangeByLex(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*     */   
/*     */   ScanResult<Tuple> zscan(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ScanParams paramScanParams);
/*     */   
/*     */   KeyValue<byte[], Tuple> bzpopmax(double paramDouble, byte[]... paramVarArgs);
/*     */   
/*     */   KeyValue<byte[], Tuple> bzpopmin(double paramDouble, byte[]... paramVarArgs);
/*     */   
/*     */   List<byte[]> zdiff(byte[]... paramVarArgs);
/*     */   
/*     */   List<Tuple> zdiffWithScores(byte[]... paramVarArgs);
/*     */   
/*     */   @Deprecated
/*     */   long zdiffStore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   long zdiffstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   List<byte[]> zinter(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   List<Tuple> zinterWithScores(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   long zinterstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   long zinterstore(byte[] paramArrayOfbyte, ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   long zintercard(byte[]... paramVarArgs);
/*     */   
/*     */   long zintercard(long paramLong, byte[]... paramVarArgs);
/*     */   
/*     */   List<byte[]> zunion(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   List<Tuple> zunionWithScores(ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   long zunionstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*     */   
/*     */   long zunionstore(byte[] paramArrayOfbyte, ZParams paramZParams, byte[]... paramVarArgs);
/*     */   
/*     */   KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption paramSortedSetOption, byte[]... paramVarArgs);
/*     */   
/*     */   KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption paramSortedSetOption, int paramInt, byte[]... paramVarArgs);
/*     */   
/*     */   KeyValue<byte[], List<Tuple>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, byte[]... paramVarArgs);
/*     */   
/*     */   KeyValue<byte[], List<Tuple>> bzmpop(double paramDouble, SortedSetOption paramSortedSetOption, int paramInt, byte[]... paramVarArgs);
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SortedSetBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */