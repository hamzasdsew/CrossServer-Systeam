package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;

public interface ScriptingKeyPipelineBinaryCommands {
  Response<Object> eval(byte[] paramArrayOfbyte);
  
  Response<Object> eval(byte[] paramArrayOfbyte, int paramInt, byte[]... paramVarArgs);
  
  Response<Object> eval(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Response<Object> evalReadonly(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Response<Object> evalsha(byte[] paramArrayOfbyte);
  
  Response<Object> evalsha(byte[] paramArrayOfbyte, int paramInt, byte[]... paramVarArgs);
  
  Response<Object> evalsha(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Response<Object> evalshaReadonly(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ScriptingKeyPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */