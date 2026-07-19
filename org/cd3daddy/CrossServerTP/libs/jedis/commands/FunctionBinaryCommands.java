package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FunctionRestorePolicy;

public interface FunctionBinaryCommands {
  Object fcall(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Object fcallReadonly(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  String functionDelete(byte[] paramArrayOfbyte);
  
  byte[] functionDump();
  
  String functionFlush();
  
  String functionFlush(FlushMode paramFlushMode);
  
  String functionKill();
  
  List<Object> functionListBinary();
  
  List<Object> functionList(byte[] paramArrayOfbyte);
  
  List<Object> functionListWithCodeBinary();
  
  List<Object> functionListWithCode(byte[] paramArrayOfbyte);
  
  String functionLoad(byte[] paramArrayOfbyte);
  
  String functionLoadReplace(byte[] paramArrayOfbyte);
  
  String functionRestore(byte[] paramArrayOfbyte);
  
  String functionRestore(byte[] paramArrayOfbyte, FunctionRestorePolicy paramFunctionRestorePolicy);
  
  Object functionStatsBinary();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\FunctionBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */