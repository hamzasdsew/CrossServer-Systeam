package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GetExParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LCSParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.SetParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.LCSMatchResult;

public interface StringPipelineBinaryCommands extends BitPipelineBinaryCommands {
  Response<String> set(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<String> set(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, SetParams paramSetParams);
  
  Response<byte[]> get(byte[] paramArrayOfbyte);
  
  Response<byte[]> setGet(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, SetParams paramSetParams);
  
  Response<byte[]> getDel(byte[] paramArrayOfbyte);
  
  Response<byte[]> getEx(byte[] paramArrayOfbyte, GetExParams paramGetExParams);
  
  Response<Long> setrange(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  Response<byte[]> getrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  Response<byte[]> getSet(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<Long> setnx(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<String> setex(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  Response<String> psetex(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  Response<List<byte[]>> mget(byte[]... paramVarArgs);
  
  Response<String> mset(byte[]... paramVarArgs);
  
  Response<Long> msetnx(byte[]... paramVarArgs);
  
  Response<Long> incr(byte[] paramArrayOfbyte);
  
  Response<Long> incrBy(byte[] paramArrayOfbyte, long paramLong);
  
  Response<Double> incrByFloat(byte[] paramArrayOfbyte, double paramDouble);
  
  Response<Long> decr(byte[] paramArrayOfbyte);
  
  Response<Long> decrBy(byte[] paramArrayOfbyte, long paramLong);
  
  Response<Long> append(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<byte[]> substr(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  Response<Long> strlen(byte[] paramArrayOfbyte);
  
  Response<LCSMatchResult> lcs(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, LCSParams paramLCSParams);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StringPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */