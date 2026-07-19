package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.CFInsertParams;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.CFReserveParams;

public interface CuckooFilterCommands {
  String cfReserve(String paramString, long paramLong);
  
  String cfReserve(String paramString, long paramLong, CFReserveParams paramCFReserveParams);
  
  boolean cfAdd(String paramString1, String paramString2);
  
  boolean cfAddNx(String paramString1, String paramString2);
  
  List<Boolean> cfInsert(String paramString, String... paramVarArgs);
  
  List<Boolean> cfInsert(String paramString, CFInsertParams paramCFInsertParams, String... paramVarArgs);
  
  List<Boolean> cfInsertNx(String paramString, String... paramVarArgs);
  
  List<Boolean> cfInsertNx(String paramString, CFInsertParams paramCFInsertParams, String... paramVarArgs);
  
  boolean cfExists(String paramString1, String paramString2);
  
  List<Boolean> cfMExists(String paramString, String... paramVarArgs);
  
  boolean cfDel(String paramString1, String paramString2);
  
  long cfCount(String paramString1, String paramString2);
  
  Map.Entry<Long, byte[]> cfScanDump(String paramString, long paramLong);
  
  String cfLoadChunk(String paramString, long paramLong, byte[] paramArrayOfbyte);
  
  Map<String, Object> cfInfo(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\CuckooFilterCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */