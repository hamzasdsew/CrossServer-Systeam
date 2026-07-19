/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*    */ 
/*    */ 
/*    */ public interface HashBinaryCommands
/*    */ {
/*    */   long hset(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   long hset(byte[] paramArrayOfbyte, Map<byte[], byte[]> paramMap);
/*    */   
/*    */   byte[] hget(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   long hsetnx(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   String hmset(byte[] paramArrayOfbyte, Map<byte[], byte[]> paramMap);
/*    */   
/*    */   List<byte[]> hmget(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   long hincrBy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, long paramLong);
/*    */   
/*    */   double hincrByFloat(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, double paramDouble);
/*    */   
/*    */   boolean hexists(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   long hdel(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   long hlen(byte[] paramArrayOfbyte);
/*    */   
/*    */   Set<byte[]> hkeys(byte[] paramArrayOfbyte);
/*    */   
/*    */   List<byte[]> hvals(byte[] paramArrayOfbyte);
/*    */   
/*    */   Map<byte[], byte[]> hgetAll(byte[] paramArrayOfbyte);
/*    */   
/*    */   byte[] hrandfield(byte[] paramArrayOfbyte);
/*    */   
/*    */   List<byte[]> hrandfield(byte[] paramArrayOfbyte, long paramLong);
/*    */   
/*    */   List<Map.Entry<byte[], byte[]>> hrandfieldWithValues(byte[] paramArrayOfbyte, long paramLong);
/*    */   
/*    */   default ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
/* 47 */     return hscan(key, cursor, new ScanParams());
/*    */   }
/*    */   
/*    */   ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ScanParams paramScanParams);
/*    */   
/*    */   long hstrlen(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HashBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */