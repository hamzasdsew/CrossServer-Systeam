package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface SampleBinaryKeyedPipelineCommands {
  Response<Long> waitReplicas(byte[] paramArrayOfbyte, int paramInt, long paramLong);
  
  Response<KeyValue<Long, Long>> waitAOF(byte[] paramArrayOfbyte, long paramLong1, long paramLong2, long paramLong3);
  
  Response<Object> eval(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<Object> evalsha(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<List<Boolean>> scriptExists(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<byte[]> scriptLoad(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<String> scriptFlush(byte[] paramArrayOfbyte);
  
  Response<String> scriptFlush(byte[] paramArrayOfbyte, FlushMode paramFlushMode);
  
  Response<String> scriptKill(byte[] paramArrayOfbyte);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SampleBinaryKeyedPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */