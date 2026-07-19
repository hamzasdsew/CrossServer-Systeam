package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FunctionRestorePolicy;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.FunctionStats;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.LibraryInfo;

public interface FunctionPipelineCommands {
  Response<Object> fcall(String paramString, List<String> paramList1, List<String> paramList2);
  
  Response<Object> fcallReadonly(String paramString, List<String> paramList1, List<String> paramList2);
  
  Response<String> functionDelete(String paramString);
  
  Response<byte[]> functionDump();
  
  Response<String> functionFlush();
  
  Response<String> functionFlush(FlushMode paramFlushMode);
  
  Response<String> functionKill();
  
  Response<List<LibraryInfo>> functionList();
  
  Response<List<LibraryInfo>> functionList(String paramString);
  
  Response<List<LibraryInfo>> functionListWithCode();
  
  Response<List<LibraryInfo>> functionListWithCode(String paramString);
  
  Response<String> functionLoad(String paramString);
  
  Response<String> functionLoadReplace(String paramString);
  
  Response<String> functionRestore(byte[] paramArrayOfbyte);
  
  Response<String> functionRestore(byte[] paramArrayOfbyte, FunctionRestorePolicy paramFunctionRestorePolicy);
  
  Response<FunctionStats> functionStats();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\FunctionPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */