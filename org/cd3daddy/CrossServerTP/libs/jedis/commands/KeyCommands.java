package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Set;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ExpiryOption;
import org.cd3daddy.CrossServerTP.libs.jedis.params.MigrateParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.RestoreParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.SortingParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;

public interface KeyCommands {
  boolean exists(String paramString);
  
  long exists(String... paramVarArgs);
  
  long persist(String paramString);
  
  String type(String paramString);
  
  byte[] dump(String paramString);
  
  String restore(String paramString, long paramLong, byte[] paramArrayOfbyte);
  
  String restore(String paramString, long paramLong, byte[] paramArrayOfbyte, RestoreParams paramRestoreParams);
  
  long expire(String paramString, long paramLong);
  
  long expire(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  long pexpire(String paramString, long paramLong);
  
  long pexpire(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  long expireTime(String paramString);
  
  long pexpireTime(String paramString);
  
  long expireAt(String paramString, long paramLong);
  
  long expireAt(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  long pexpireAt(String paramString, long paramLong);
  
  long pexpireAt(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  long ttl(String paramString);
  
  long pttl(String paramString);
  
  long touch(String paramString);
  
  long touch(String... paramVarArgs);
  
  List<String> sort(String paramString);
  
  long sort(String paramString1, String paramString2);
  
  List<String> sort(String paramString, SortingParams paramSortingParams);
  
  long sort(String paramString1, SortingParams paramSortingParams, String paramString2);
  
  List<String> sortReadonly(String paramString, SortingParams paramSortingParams);
  
  long del(String paramString);
  
  long del(String... paramVarArgs);
  
  long unlink(String paramString);
  
  long unlink(String... paramVarArgs);
  
  boolean copy(String paramString1, String paramString2, boolean paramBoolean);
  
  String rename(String paramString1, String paramString2);
  
  long renamenx(String paramString1, String paramString2);
  
  Long memoryUsage(String paramString);
  
  Long memoryUsage(String paramString, int paramInt);
  
  Long objectRefcount(String paramString);
  
  String objectEncoding(String paramString);
  
  Long objectIdletime(String paramString);
  
  Long objectFreq(String paramString);
  
  String migrate(String paramString1, int paramInt1, String paramString2, int paramInt2);
  
  String migrate(String paramString, int paramInt1, int paramInt2, MigrateParams paramMigrateParams, String... paramVarArgs);
  
  Set<String> keys(String paramString);
  
  ScanResult<String> scan(String paramString);
  
  ScanResult<String> scan(String paramString, ScanParams paramScanParams);
  
  ScanResult<String> scan(String paramString1, ScanParams paramScanParams, String paramString2);
  
  String randomKey();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\KeyCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */