package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface SampleBinaryKeyedCommands {
  long waitReplicas(byte[] paramArrayOfbyte, int paramInt, long paramLong);
  
  KeyValue<Long, Long> waitAOF(byte[] paramArrayOfbyte, long paramLong1, long paramLong2, long paramLong3);
  
  Object eval(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Object evalsha(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Boolean scriptExists(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  List<Boolean> scriptExists(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  byte[] scriptLoad(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  String scriptFlush(byte[] paramArrayOfbyte);
  
  String scriptFlush(byte[] paramArrayOfbyte, FlushMode paramFlushMode);
  
  String scriptKill(byte[] paramArrayOfbyte);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SampleBinaryKeyedCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */