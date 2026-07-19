package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientAttributeOption;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientPauseMode;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientType;
import org.cd3daddy.CrossServerTP.libs.jedis.args.UnblockType;
import org.cd3daddy.CrossServerTP.libs.jedis.params.ClientKillParams;

public interface ClientCommands {
  String clientKill(String paramString);
  
  String clientKill(String paramString, int paramInt);
  
  long clientKill(ClientKillParams paramClientKillParams);
  
  String clientGetname();
  
  String clientList();
  
  String clientList(ClientType paramClientType);
  
  String clientList(long... paramVarArgs);
  
  String clientInfo();
  
  String clientSetInfo(ClientAttributeOption paramClientAttributeOption, String paramString);
  
  String clientSetname(String paramString);
  
  long clientId();
  
  long clientUnblock(long paramLong);
  
  long clientUnblock(long paramLong, UnblockType paramUnblockType);
  
  String clientPause(long paramLong);
  
  String clientPause(long paramLong, ClientPauseMode paramClientPauseMode);
  
  String clientUnpause();
  
  String clientNoEvictOn();
  
  String clientNoEvictOff();
  
  String clientNoTouchOn();
  
  String clientNoTouchOff();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ClientCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */