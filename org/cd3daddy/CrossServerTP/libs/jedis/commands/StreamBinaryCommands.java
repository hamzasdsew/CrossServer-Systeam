/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAddParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAutoClaimParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XClaimParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XTrimParams;
/*    */ 
/*    */ public interface StreamBinaryCommands {
/*    */   default byte[] xadd(byte[] key, Map<byte[], byte[]> hash, XAddParams params) {
/* 11 */     return xadd(key, params, hash);
/*    */   }
/*    */   
/*    */   byte[] xadd(byte[] paramArrayOfbyte, XAddParams paramXAddParams, Map<byte[], byte[]> paramMap);
/*    */   
/*    */   long xlen(byte[] paramArrayOfbyte);
/*    */   
/*    */   List<Object> xrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   List<Object> xrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt);
/*    */   
/*    */   List<Object> xrevrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   List<Object> xrevrange(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, int paramInt);
/*    */   
/*    */   long xack(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[]... paramVarArgs);
/*    */   
/*    */   String xgroupCreate(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, boolean paramBoolean);
/*    */   
/*    */   String xgroupSetID(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   long xgroupDestroy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   boolean xgroupCreateConsumer(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   long xgroupDelConsumer(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */   
/*    */   long xdel(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   long xtrim(byte[] paramArrayOfbyte, long paramLong, boolean paramBoolean);
/*    */   
/*    */   long xtrim(byte[] paramArrayOfbyte, XTrimParams paramXTrimParams);
/*    */   
/*    */   Object xpending(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   List<Object> xpending(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, XPendingParams paramXPendingParams);
/*    */   
/*    */   List<byte[]> xclaim(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, XClaimParams paramXClaimParams, byte[]... paramVarArgs);
/*    */   
/*    */   List<byte[]> xclaimJustId(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, XClaimParams paramXClaimParams, byte[]... paramVarArgs);
/*    */   
/*    */   List<Object> xautoclaim(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, byte[] paramArrayOfbyte4, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   List<Object> xautoclaimJustId(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3, long paramLong, byte[] paramArrayOfbyte4, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   Object xinfoStream(byte[] paramArrayOfbyte);
/*    */   
/*    */   Object xinfoStreamFull(byte[] paramArrayOfbyte);
/*    */   
/*    */   Object xinfoStreamFull(byte[] paramArrayOfbyte, int paramInt);
/*    */   
/*    */   List<Object> xinfoGroups(byte[] paramArrayOfbyte);
/*    */   
/*    */   List<Object> xinfoConsumers(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   List<Object> xread(XReadParams paramXReadParams, Map.Entry<byte[], byte[]>... paramVarArgs);
/*    */   
/*    */   List<Object> xreadGroup(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, XReadGroupParams paramXReadGroupParams, Map.Entry<byte[], byte[]>... paramVarArgs);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StreamBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */