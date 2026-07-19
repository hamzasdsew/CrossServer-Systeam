package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FunctionRestorePolicy;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.FunctionStats;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.LibraryInfo;

public interface FunctionCommands {
  Object fcall(String paramString, List<String> paramList1, List<String> paramList2);
  
  Object fcallReadonly(String paramString, List<String> paramList1, List<String> paramList2);
  
  String functionDelete(String paramString);
  
  byte[] functionDump();
  
  String functionFlush();
  
  String functionFlush(FlushMode paramFlushMode);
  
  String functionKill();
  
  List<LibraryInfo> functionList();
  
  List<LibraryInfo> functionList(String paramString);
  
  List<LibraryInfo> functionListWithCode();
  
  List<LibraryInfo> functionListWithCode(String paramString);
  
  String functionLoad(String paramString);
  
  String functionLoadReplace(String paramString);
  
  String functionRestore(byte[] paramArrayOfbyte);
  
  String functionRestore(byte[] paramArrayOfbyte, FunctionRestorePolicy paramFunctionRestorePolicy);
  
  FunctionStats functionStats();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\FunctionCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */