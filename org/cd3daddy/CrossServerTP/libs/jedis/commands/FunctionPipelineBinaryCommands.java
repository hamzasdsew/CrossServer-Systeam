package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FunctionRestorePolicy;

public interface FunctionPipelineBinaryCommands {
  Response<Object> fcall(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Response<Object> fcallReadonly(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Response<String> functionDelete(byte[] paramArrayOfbyte);
  
  Response<byte[]> functionDump();
  
  Response<String> functionFlush();
  
  Response<String> functionFlush(FlushMode paramFlushMode);
  
  Response<String> functionKill();
  
  Response<List<Object>> functionListBinary();
  
  Response<List<Object>> functionList(byte[] paramArrayOfbyte);
  
  Response<List<Object>> functionListWithCodeBinary();
  
  Response<List<Object>> functionListWithCode(byte[] paramArrayOfbyte);
  
  Response<String> functionLoad(byte[] paramArrayOfbyte);
  
  Response<String> functionLoadReplace(byte[] paramArrayOfbyte);
  
  Response<String> functionRestore(byte[] paramArrayOfbyte);
  
  Response<String> functionRestore(byte[] paramArrayOfbyte, FunctionRestorePolicy paramFunctionRestorePolicy);
  
  Response<Object> functionStatsBinary();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\FunctionPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */