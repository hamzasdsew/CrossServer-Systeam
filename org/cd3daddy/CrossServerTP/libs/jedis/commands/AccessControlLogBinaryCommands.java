package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.AccessControlUser;

public interface AccessControlLogBinaryCommands {
  byte[] aclWhoAmIBinary();
  
  byte[] aclGenPassBinary();
  
  byte[] aclGenPassBinary(int paramInt);
  
  List<byte[]> aclListBinary();
  
  List<byte[]> aclUsersBinary();
  
  AccessControlUser aclGetUser(byte[] paramArrayOfbyte);
  
  String aclSetUser(byte[] paramArrayOfbyte);
  
  String aclSetUser(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  long aclDelUser(byte[]... paramVarArgs);
  
  List<byte[]> aclCatBinary();
  
  List<byte[]> aclCat(byte[] paramArrayOfbyte);
  
  List<byte[]> aclLogBinary();
  
  List<byte[]> aclLogBinary(int paramInt);
  
  String aclLogReset();
  
  String aclLoad();
  
  String aclSave();
  
  byte[] aclDryRunBinary(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, byte[]... paramVarArgs);
  
  byte[] aclDryRunBinary(byte[] paramArrayOfbyte, CommandArguments paramCommandArguments);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\AccessControlLogBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */