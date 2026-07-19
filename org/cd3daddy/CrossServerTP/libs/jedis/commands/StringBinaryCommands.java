package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GetExParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LCSParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.SetParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.LCSMatchResult;

public interface StringBinaryCommands extends BitBinaryCommands {
  String set(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  String set(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, SetParams paramSetParams);
  
  byte[] get(byte[] paramArrayOfbyte);
  
  byte[] setGet(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  byte[] setGet(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, SetParams paramSetParams);
  
  byte[] getDel(byte[] paramArrayOfbyte);
  
  byte[] getEx(byte[] paramArrayOfbyte, GetExParams paramGetExParams);
  
  long setrange(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  byte[] getrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  byte[] getSet(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  long setnx(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  String setex(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  String psetex(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  List<byte[]> mget(byte[]... paramVarArgs);
  
  String mset(byte[]... paramVarArgs);
  
  long msetnx(byte[]... paramVarArgs);
  
  long incr(byte[] paramArrayOfbyte);
  
  long incrBy(byte[] paramArrayOfbyte, long paramLong);
  
  double incrByFloat(byte[] paramArrayOfbyte, double paramDouble);
  
  long decr(byte[] paramArrayOfbyte);
  
  long decrBy(byte[] paramArrayOfbyte, long paramLong);
  
  long append(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  byte[] substr(byte[] paramArrayOfbyte, int paramInt1, int paramInt2);
  
  long strlen(byte[] paramArrayOfbyte);
  
  LCSMatchResult lcs(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, LCSParams paramLCSParams);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StringBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */