package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.BFInsertParams;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.BFReserveParams;

public interface BloomFilterCommands {
  String bfReserve(String paramString, double paramDouble, long paramLong);
  
  String bfReserve(String paramString, double paramDouble, long paramLong, BFReserveParams paramBFReserveParams);
  
  boolean bfAdd(String paramString1, String paramString2);
  
  List<Boolean> bfMAdd(String paramString, String... paramVarArgs);
  
  List<Boolean> bfInsert(String paramString, String... paramVarArgs);
  
  List<Boolean> bfInsert(String paramString, BFInsertParams paramBFInsertParams, String... paramVarArgs);
  
  boolean bfExists(String paramString1, String paramString2);
  
  List<Boolean> bfMExists(String paramString, String... paramVarArgs);
  
  Map.Entry<Long, byte[]> bfScanDump(String paramString, long paramLong);
  
  String bfLoadChunk(String paramString, long paramLong, byte[] paramArrayOfbyte);
  
  long bfCard(String paramString);
  
  Map<String, Object> bfInfo(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\BloomFilterCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */