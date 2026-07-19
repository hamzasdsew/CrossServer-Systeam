package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Set;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ExpiryOption;
import org.cd3daddy.CrossServerTP.libs.jedis.params.MigrateParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.RestoreParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.SortingParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;

public interface KeyBinaryCommands {
  boolean exists(byte[] paramArrayOfbyte);
  
  long exists(byte[]... paramVarArgs);
  
  long persist(byte[] paramArrayOfbyte);
  
  String type(byte[] paramArrayOfbyte);
  
  byte[] dump(byte[] paramArrayOfbyte);
  
  String restore(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  String restore(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2, RestoreParams paramRestoreParams);
  
  long expire(byte[] paramArrayOfbyte, long paramLong);
  
  long expire(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  long pexpire(byte[] paramArrayOfbyte, long paramLong);
  
  long pexpire(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  long expireTime(byte[] paramArrayOfbyte);
  
  long pexpireTime(byte[] paramArrayOfbyte);
  
  long expireAt(byte[] paramArrayOfbyte, long paramLong);
  
  long expireAt(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  long pexpireAt(byte[] paramArrayOfbyte, long paramLong);
  
  long pexpireAt(byte[] paramArrayOfbyte, long paramLong, ExpiryOption paramExpiryOption);
  
  long ttl(byte[] paramArrayOfbyte);
  
  long pttl(byte[] paramArrayOfbyte);
  
  long touch(byte[] paramArrayOfbyte);
  
  long touch(byte[]... paramVarArgs);
  
  List<byte[]> sort(byte[] paramArrayOfbyte);
  
  List<byte[]> sort(byte[] paramArrayOfbyte, SortingParams paramSortingParams);
  
  long del(byte[] paramArrayOfbyte);
  
  long del(byte[]... paramVarArgs);
  
  long unlink(byte[] paramArrayOfbyte);
  
  long unlink(byte[]... paramVarArgs);
  
  boolean copy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, boolean paramBoolean);
  
  String rename(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  long renamenx(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  long sort(byte[] paramArrayOfbyte1, SortingParams paramSortingParams, byte[] paramArrayOfbyte2);
  
  long sort(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  List<byte[]> sortReadonly(byte[] paramArrayOfbyte, SortingParams paramSortingParams);
  
  Long memoryUsage(byte[] paramArrayOfbyte);
  
  Long memoryUsage(byte[] paramArrayOfbyte, int paramInt);
  
  Long objectRefcount(byte[] paramArrayOfbyte);
  
  byte[] objectEncoding(byte[] paramArrayOfbyte);
  
  Long objectIdletime(byte[] paramArrayOfbyte);
  
  Long objectFreq(byte[] paramArrayOfbyte);
  
  String migrate(String paramString, int paramInt1, byte[] paramArrayOfbyte, int paramInt2);
  
  String migrate(String paramString, int paramInt1, int paramInt2, MigrateParams paramMigrateParams, byte[]... paramVarArgs);
  
  Set<byte[]> keys(byte[] paramArrayOfbyte);
  
  ScanResult<byte[]> scan(byte[] paramArrayOfbyte);
  
  ScanResult<byte[]> scan(byte[] paramArrayOfbyte, ScanParams paramScanParams);
  
  ScanResult<byte[]> scan(byte[] paramArrayOfbyte1, ScanParams paramScanParams, byte[] paramArrayOfbyte2);
  
  byte[] randomBinaryKey();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\KeyBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */