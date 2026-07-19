package org.cd3daddy.CrossServerTP.libs.jedis.commands;

public interface HyperLogLogBinaryCommands {
  long pfadd(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  String pfmerge(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  long pfcount(byte[] paramArrayOfbyte);
  
  long pfcount(byte[]... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HyperLogLogBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */