package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;

public interface ScriptingKeyBinaryCommands {
  Object eval(byte[] paramArrayOfbyte);
  
  Object eval(byte[] paramArrayOfbyte, int paramInt, byte[]... paramVarArgs);
  
  Object eval(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Object evalReadonly(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Object evalsha(byte[] paramArrayOfbyte);
  
  Object evalsha(byte[] paramArrayOfbyte, int paramInt, byte[]... paramVarArgs);
  
  Object evalsha(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
  
  Object evalshaReadonly(byte[] paramArrayOfbyte, List<byte[]> paramList1, List<byte[]> paramList2);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ScriptingKeyBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */