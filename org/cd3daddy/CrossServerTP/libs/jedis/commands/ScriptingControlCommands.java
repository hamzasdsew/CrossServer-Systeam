package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;

public interface ScriptingControlCommands {
  Boolean scriptExists(String paramString);
  
  List<Boolean> scriptExists(String... paramVarArgs);
  
  Boolean scriptExists(byte[] paramArrayOfbyte);
  
  List<Boolean> scriptExists(byte[]... paramVarArgs);
  
  String scriptLoad(String paramString);
  
  byte[] scriptLoad(byte[] paramArrayOfbyte);
  
  String scriptFlush();
  
  String scriptFlush(FlushMode paramFlushMode);
  
  String scriptKill();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ScriptingControlCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */