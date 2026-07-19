package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientAttributeOption;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientPauseMode;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientType;
import org.cd3daddy.CrossServerTP.libs.jedis.args.UnblockType;
import org.cd3daddy.CrossServerTP.libs.jedis.params.ClientKillParams;

public interface ClientBinaryCommands {
  String clientKill(byte[] paramArrayOfbyte);
  
  String clientKill(String paramString, int paramInt);
  
  long clientKill(ClientKillParams paramClientKillParams);
  
  byte[] clientGetnameBinary();
  
  byte[] clientListBinary();
  
  byte[] clientListBinary(ClientType paramClientType);
  
  byte[] clientListBinary(long... paramVarArgs);
  
  byte[] clientInfoBinary();
  
  String clientSetInfo(ClientAttributeOption paramClientAttributeOption, byte[] paramArrayOfbyte);
  
  String clientSetname(byte[] paramArrayOfbyte);
  
  long clientId();
  
  long clientUnblock(long paramLong);
  
  long clientUnblock(long paramLong, UnblockType paramUnblockType);
  
  String clientPause(long paramLong);
  
  String clientPause(long paramLong, ClientPauseMode paramClientPauseMode);
  
  String clientUnpause();
  
  String clientNoEvictOn();
  
  String clientNoEvictOff();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ClientBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */