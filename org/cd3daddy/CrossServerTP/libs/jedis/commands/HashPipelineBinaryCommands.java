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
/*    */ public interface HashPipelineBinaryCommands
/*    */ {
/*    */   Response<Long> hset(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   Response<Long> hset(byte[] paramArrayOfbyte, Map<byte[], byte[]> paramMap);
/*    */   
/*    */   Response<byte[]> hget(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   Response<Long> hsetnx(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   Response<String> hmset(byte[] paramArrayOfbyte, Map<byte[], byte[]> paramMap);
/*    */   
/*    */   Response<List<byte[]>> hmget(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> hincrBy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, long paramLong);
/*    */   
/*    */   Response<Double> hincrByFloat(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble);
/*    */   
/*    */   Response<Boolean> hexists(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   Response<Long> hdel(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> hlen(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<Set<byte[]>> hkeys(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<List<byte[]>> hvals(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<Map<byte[], byte[]>> hgetAll(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<byte[]> hrandfield(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<List<byte[]>> hrandfield(byte[] paramArrayOfbyte, long paramLong);
/*    */   
/*    */   Response<List<Map.Entry<byte[], byte[]>>> hrandfieldWithValues(byte[] paramArrayOfbyte, long paramLong);
/*    */   
/*    */   default Response<ScanResult<Map.Entry<byte[], byte[]>>> hscan(byte[] key, byte[] cursor) {
/* 48 */     return hscan(key, cursor, new ScanParams());
/*    */   }
/*    */   
/*    */   Response<ScanResult<Map.Entry<byte[], byte[]>>> hscan(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ScanParams paramScanParams);
/*    */   
/*    */   Response<Long> hstrlen(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HashPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */