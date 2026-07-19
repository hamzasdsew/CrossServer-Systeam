package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.AccessControlLogEntry;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.AccessControlUser;

public interface AccessControlLogCommands {
  String aclWhoAmI();
  
  String aclGenPass();
  
  String aclGenPass(int paramInt);
  
  List<String> aclList();
  
  List<String> aclUsers();
  
  AccessControlUser aclGetUser(String paramString);
  
  String aclSetUser(String paramString);
  
  String aclSetUser(String paramString, String... paramVarArgs);
  
  long aclDelUser(String... paramVarArgs);
  
  List<String> aclCat();
  
  List<String> aclCat(String paramString);
  
  List<AccessControlLogEntry> aclLog();
  
  List<AccessControlLogEntry> aclLog(int paramInt);
  
  String aclLogReset();
  
  String aclLoad();
  
  String aclSave();
  
  String aclDryRun(String paramString1, String paramString2, String... paramVarArgs);
  
  String aclDryRun(String paramString, CommandArguments paramCommandArguments);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\AccessControlLogCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */