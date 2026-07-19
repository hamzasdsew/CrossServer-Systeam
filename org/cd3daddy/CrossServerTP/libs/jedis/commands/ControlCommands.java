package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Map;

public interface ControlCommands extends AccessControlLogCommands, ClientCommands {
  List<Object> role();
  
  Long objectRefcount(String paramString);
  
  String objectEncoding(String paramString);
  
  Long objectIdletime(String paramString);
  
  List<String> objectHelp();
  
  Long objectFreq(String paramString);
  
  String memoryDoctor();
  
  Long memoryUsage(String paramString);
  
  Long memoryUsage(String paramString, int paramInt);
  
  String memoryPurge();
  
  Map<String, Object> memoryStats();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ControlCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */