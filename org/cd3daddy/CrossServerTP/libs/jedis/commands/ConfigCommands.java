package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.Map;

public interface ConfigCommands {
  Map<String, String> configGet(String paramString);
  
  Map<String, String> configGet(String... paramVarArgs);
  
  Map<byte[], byte[]> configGet(byte[] paramArrayOfbyte);
  
  Map<byte[], byte[]> configGet(byte[]... paramVarArgs);
  
  String configSet(String paramString1, String paramString2);
  
  String configSet(String... paramVarArgs);
  
  String configSet(Map<String, String> paramMap);
  
  String configSet(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  String configSet(byte[]... paramVarArgs);
  
  String configSetBinary(Map<byte[], byte[]> paramMap);
  
  String configResetStat();
  
  String configRewrite();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ConfigCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */