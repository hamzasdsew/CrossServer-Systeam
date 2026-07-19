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

public interface KeyPipelineCommands {
  Response<Boolean> exists(String paramString);
  
  Response<Long> exists(String... paramVarArgs);
  
  Response<Long> persist(String paramString);
  
  Response<String> type(String paramString);
  
  Response<byte[]> dump(String paramString);
  
  Response<String> restore(String paramString, long paramLong, byte[] paramArrayOfbyte);
  
  Response<String> restore(String paramString, long paramLong, byte[] paramArrayOfbyte, RestoreParams paramRestoreParams);
  
  Response<Long> expire(String paramString, long paramLong);
  
  Response<Long> expire(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> pexpire(String paramString, long paramLong);
  
  Response<Long> pexpire(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> expireTime(String paramString);
  
  Response<Long> pexpireTime(String paramString);
  
  Response<Long> expireAt(String paramString, long paramLong);
  
  Response<Long> expireAt(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> pexpireAt(String paramString, long paramLong);
  
  Response<Long> pexpireAt(String paramString, long paramLong, ExpiryOption paramExpiryOption);
  
  Response<Long> ttl(String paramString);
  
  Response<Long> pttl(String paramString);
  
  Response<Long> touch(String paramString);
  
  Response<Long> touch(String... paramVarArgs);
  
  Response<List<String>> sort(String paramString);
  
  Response<Long> sort(String paramString1, String paramString2);
  
  Response<List<String>> sort(String paramString, SortingParams paramSortingParams);
  
  Response<Long> sort(String paramString1, SortingParams paramSortingParams, String paramString2);
  
  Response<List<String>> sortReadonly(String paramString, SortingParams paramSortingParams);
  
  Response<Long> del(String paramString);
  
  Response<Long> del(String... paramVarArgs);
  
  Response<Long> unlink(String paramString);
  
  Response<Long> unlink(String... paramVarArgs);
  
  Response<Boolean> copy(String paramString1, String paramString2, boolean paramBoolean);
  
  Response<String> rename(String paramString1, String paramString2);
  
  Response<Long> renamenx(String paramString1, String paramString2);
  
  Response<Long> memoryUsage(String paramString);
  
  Response<Long> memoryUsage(String paramString, int paramInt);
  
  Response<Long> objectRefcount(String paramString);
  
  Response<String> objectEncoding(String paramString);
  
  Response<Long> objectIdletime(String paramString);
  
  Response<Long> objectFreq(String paramString);
  
  Response<String> migrate(String paramString1, int paramInt1, String paramString2, int paramInt2);
  
  Response<String> migrate(String paramString, int paramInt1, int paramInt2, MigrateParams paramMigrateParams, String... paramVarArgs);
  
  Response<Set<String>> keys(String paramString);
  
  Response<ScanResult<String>> scan(String paramString);
  
  Response<ScanResult<String>> scan(String paramString, ScanParams paramScanParams);
  
  Response<ScanResult<String>> scan(String paramString1, ScanParams paramScanParams, String paramString2);
  
  Response<String> randomKey();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\KeyPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */