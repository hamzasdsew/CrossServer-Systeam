package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Set;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ExpiryOption;
import org.cd3daddy.CrossServerTP.libs.jedis.params.MigrateParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.RestoreParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.SortingParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;

public interface KeyPipelineBinaryCommands {
  Response<Boolean> exists(byte[] paramArrayOfbyte);
  
  Response<Long> exists(byte[]... paramVarArgs);
  
  Response<Long> persist(byte[] paramArrayOfbyte);
  
  Response<String> type(byte[] paramArrayOfbyte);
  
  Response<byte[]> dump(byte[] paramArrayOfbyte);
  
  Response<String> restore(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  Response<String> restore(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2, RestoreParams paramRestoreParams);
  
  Response<Long> expire(byte[] paramArrayOfbyte, long paramLong);
  
  Response<Long> expire(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> pexpire(byte[] paramArrayOfbyte, long paramLong);
  
  Response<Long> pexpire(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> expireTime(byte[] paramArrayOfbyte);
  
  Response<Long> pexpireTime(byte[] paramArrayOfbyte);
  
  Response<Long> expireAt(byte[] paramArrayOfbyte, long paramLong);
  
  Response<Long> expireAt(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> pexpireAt(byte[] paramArrayOfbyte, long paramLong);
  
  Response<Long> pexpireAt(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> ttl(byte[] paramArrayOfbyte);
  
  Response<Long> pttl(byte[] paramArrayOfbyte);
  
  Response<Long> touch(byte[] paramArrayOfbyte);
  
  Response<Long> touch(byte[]... paramVarArgs);
  
  Response<List<byte[]>> sort(byte[] paramArrayOfbyte);
  
  Response<List<byte[]>> sort(byte[] paramArrayOfbyte, SortingParams paramSortingParams);
  
  Response<List<byte[]>> sortReadonly(byte[] paramArrayOfbyte, SortingParams paramSortingParams);
  
  Response<Long> del(byte[] paramArrayOfbyte);
  
  Response<Long> del(byte[]... paramVarArgs);
  
  Response<Long> unlink(byte[] paramArrayOfbyte);
  
  Response<Long> unlink(byte[]... paramVarArgs);
  
  Response<Boolean> copy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, boolean paramBoolean);
  
  Response<String> rename(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<Long> renamenx(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<Long> sort(byte[] paramArrayOfbyte1, SortingParams paramSortingParams, byte[] paramArrayOfbyte2);
  
  Response<Long> sort(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<Long> memoryUsage(byte[] paramArrayOfbyte);
  
  Response<Long> memoryUsage(byte[] paramArrayOfbyte, int paramInt);
  
  Response<Long> objectRefcount(byte[] paramArrayOfbyte);
  
  Response<byte[]> objectEncoding(byte[] paramArrayOfbyte);
  
  Response<Long> objectIdletime(byte[] paramArrayOfbyte);
  
  Response<Long> objectFreq(byte[] paramArrayOfbyte);
  
  Response<String> migrate(String paramString, int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
  
  Response<String> migrate(String paramString, int paramInt1, int paramInt2, MigrateParams paramMigrateParams, byte[]... paramVarArgs);
  
  Response<Set<byte[]>> keys(byte[] paramArrayOfbyte);
  
  Response<ScanResult<byte[]>> scan(byte[] paramArrayOfbyte);
  
  Response<ScanResult<byte[]>> scan(byte[] paramArrayOfbyte, ScanParams paramScanParams);
  
  Response<ScanResult<byte[]>> scan(byte[] paramArrayOfbyte1, ScanParams paramScanParams, byte[] paramArrayOfbyte2);
  
  Response<byte[]> randomBinaryKey();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\KeyPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */