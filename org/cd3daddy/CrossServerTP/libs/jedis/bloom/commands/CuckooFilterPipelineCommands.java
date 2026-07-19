package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.CFInsertParams;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.CFReserveParams;

public interface CuckooFilterPipelineCommands {
  Response<String> cfReserve(String paramString, long paramLong);
  
  Response<String> cfReserve(String paramString, long paramLong, CFReserveParams paramCFReserveParams);
  
  Response<Boolean> cfAdd(String paramString1, String paramString2);
  
  Response<Boolean> cfAddNx(String paramString1, String paramString2);
  
  Response<List<Boolean>> cfInsert(String paramString, String... paramVarArgs);
  
  Response<List<Boolean>> cfInsert(String paramString, CFInsertParams paramCFInsertParams, String... paramVarArgs);
  
  Response<List<Boolean>> cfInsertNx(String paramString, String... paramVarArgs);
  
  Response<List<Boolean>> cfInsertNx(String paramString, CFInsertParams paramCFInsertParams, String... paramVarArgs);
  
  Response<Boolean> cfExists(String paramString1, String paramString2);
  
  Response<Boolean> cfDel(String paramString1, String paramString2);
  
  Response<Long> cfCount(String paramString1, String paramString2);
  
  Response<Map.Entry<Long, byte[]>> cfScanDump(String paramString, long paramLong);
  
  Response<String> cfLoadChunk(String paramString, long paramLong, byte[] paramArrayOfbyte);
  
  Response<Map<String, Object>> cfInfo(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\CuckooFilterPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */