package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Map;

public interface SentinelCommands {
  String sentinelMyId();
  
  List<Map<String, String>> sentinelMasters();
  
  Map<String, String> sentinelMaster(String paramString);
  
  List<Map<String, String>> sentinelSentinels(String paramString);
  
  List<String> sentinelGetMasterAddrByName(String paramString);
  
  Long sentinelReset(String paramString);
  
  @Deprecated
  List<Map<String, String>> sentinelSlaves(String paramString);
  
  List<Map<String, String>> sentinelReplicas(String paramString);
  
  String sentinelFailover(String paramString);
  
  String sentinelMonitor(String paramString1, String paramString2, int paramInt1, int paramInt2);
  
  String sentinelRemove(String paramString);
  
  String sentinelSet(String paramString, Map<String, String> paramMap);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SentinelCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */