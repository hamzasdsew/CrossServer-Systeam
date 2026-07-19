package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;

public interface ControlBinaryCommands extends AccessControlLogBinaryCommands, ClientBinaryCommands {
  List<Object> roleBinary();
  
  Long objectRefcount(byte[] paramArrayOfbyte);
  
  byte[] objectEncoding(byte[] paramArrayOfbyte);
  
  Long objectIdletime(byte[] paramArrayOfbyte);
  
  List<byte[]> objectHelpBinary();
  
  Long objectFreq(byte[] paramArrayOfbyte);
  
  byte[] memoryDoctorBinary();
  
  Long memoryUsage(byte[] paramArrayOfbyte);
  
  Long memoryUsage(byte[] paramArrayOfbyte, int paramInt);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ControlBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */