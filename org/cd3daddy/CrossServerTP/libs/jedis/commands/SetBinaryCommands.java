/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*    */ 
/*    */ 
/*    */ public interface SetBinaryCommands
/*    */ {
/*    */   long sadd(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Set<byte[]> smembers(byte[] paramArrayOfbyte);
/*    */   
/*    */   long srem(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   byte[] spop(byte[] paramArrayOfbyte);
/*    */   
/*    */   Set<byte[]> spop(byte[] paramArrayOfbyte, long paramLong);
/*    */   
/*    */   long scard(byte[] paramArrayOfbyte);
/*    */   
/*    */   boolean sismember(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
/*    */   
/*    */   List<Boolean> smismember(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   byte[] srandmember(byte[] paramArrayOfbyte);
/*    */   
/*    */   List<byte[]> srandmember(byte[] paramArrayOfbyte, int paramInt);
/*    */   
/*    */   default ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
/* 32 */     return sscan(key, cursor, new ScanParams());
/*    */   }
/*    */   
/*    */   ScanResult<byte[]> sscan(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ScanParams paramScanParams);
/*    */   
/*    */   Set<byte[]> sdiff(byte[]... paramVarArgs);
/*    */   
/*    */   long sdiffstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   Set<byte[]> sinter(byte[]... paramVarArgs);
/*    */   
/*    */   long sinterstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   long sintercard(byte[]... paramVarArgs);
/*    */   
/*    */   long sintercard(int paramInt, byte[]... paramVarArgs);
/*    */   
/*    */   Set<byte[]> sunion(byte[]... paramVarArgs);
/*    */   
/*    */   long sunionstore(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
/*    */   
/*    */   long smove(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SetBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */