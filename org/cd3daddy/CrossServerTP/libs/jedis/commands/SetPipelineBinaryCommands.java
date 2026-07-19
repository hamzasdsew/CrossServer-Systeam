/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*    */ 
/*    */ 
/*    */ public interface SetPipelineBinaryCommands
/*    */ {
/*    */   Response<Long> sadd(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Set<byte[]>> smembers(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<Long> srem(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<byte[]> spop(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<Set<byte[]>> spop(byte[] paramArrayOfbyte, long paramLong);
/*    */   
/*    */   Response<Long> scard(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<Boolean> sismember(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   Response<List<Boolean>> smismember(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<byte[]> srandmember(byte[] paramArrayOfbyte);
/*    */   
/*    */   Response<List<byte[]>> srandmember(byte[] paramArrayOfbyte, int paramInt);
/*    */   
/*    */   default Response<ScanResult<byte[]>> sscan(byte[] key, byte[] cursor) {
/* 33 */     return sscan(key, cursor, new ScanParams());
/*    */   }
/*    */   
/*    */   Response<ScanResult<byte[]>> sscan(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ScanParams paramScanParams);
/*    */   
/*    */   Response<Set<byte[]>> sdiff(byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> sdiffstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Set<byte[]>> sinter(byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> sinterstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> sintercard(byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> sintercard(int paramInt, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Set<byte[]>> sunion(byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> sunionstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Response<Long> smove(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SetPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */