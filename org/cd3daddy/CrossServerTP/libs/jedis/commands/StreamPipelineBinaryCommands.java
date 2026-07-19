/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAddParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAutoClaimParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XClaimParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XTrimParams;
/*    */ 
/*    */ public interface StreamPipelineBinaryCommands {
/*    */   default Response<byte[]> xadd(byte[] key, Map<byte[], byte[]> hash, XAddParams params) {
/* 12 */     return xadd(key, params, hash);
/*    */   }
/*    */   
/*    */   Response<byte[]> xadd(byte[] paramArrayOfbyte, XAddParams paramXAddParams, Map<byte[], byte[]> paramMap);
/*    */   
/*    */   Response<Long> xlen(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<List<Object>> xrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   Response<List<Object>> xrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt);
/*    */   
/*    */   Response<List<Object>> xrevrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   Response<List<Object>> xrevrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt);
/*    */   
/*    */   Response<Long> xack(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[]... paramVarArgs);
/*    */   
/*    */   Response<String> xgroupCreate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, boolean paramBoolean);
/*    */   
/*    */   Response<String> xgroupSetID(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   Response<Long> xgroupDestroy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   Response<Boolean> xgroupCreateConsumer(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   Response<Long> xgroupDelConsumer(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   Response<Long> xdel(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> xtrim(byte[] paramArrayOfbyte, long paramLong, boolean paramBoolean);
/*    */   
/*    */   Response<Long> xtrim(byte[] paramArrayOfbyte, XTrimParams paramXTrimParams);
/*    */   
/*    */   Response<Object> xpending(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   Response<List<Object>> xpending(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, XPendingParams paramXPendingParams);
/*    */   
/*    */   Response<List<byte[]>> xclaim(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, XClaimParams paramXClaimParams, byte[]... paramVarArgs);
/*    */   
/*    */   Response<List<byte[]>> xclaimJustId(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, XClaimParams paramXClaimParams, byte[]... paramVarArgs);
/*    */   
/*    */   Response<List<Object>> xautoclaim(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, byte[] paramArrayOfbyte4, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   Response<List<Object>> xautoclaimJustId(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, byte[] paramArrayOfbyte4, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   Response<Object> xinfoStream(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<Object> xinfoStreamFull(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<Object> xinfoStreamFull(byte[] paramArrayOfbyte, int paramInt);
/*    */   
/*    */   Response<List<Object>> xinfoGroups(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<List<Object>> xinfoConsumers(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   Response<List<Object>> xread(XReadParams paramXReadParams, Map.Entry<byte[], byte[]>... paramVarArgs);
/*    */   
/*    */   Response<List<Object>> xreadGroup(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, XReadGroupParams paramXReadGroupParams, Map.Entry<byte[], byte[]>... paramVarArgs);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StreamPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */