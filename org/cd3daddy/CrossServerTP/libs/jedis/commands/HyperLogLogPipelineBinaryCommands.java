package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.Response;

public interface HyperLogLogPipelineBinaryCommands {
  Response<Long> pfadd(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<String> pfmerge(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<Long> pfcount(byte[] paramArrayOfbyte);
  
  Response<Long> pfcount(byte[]... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HyperLogLogPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */