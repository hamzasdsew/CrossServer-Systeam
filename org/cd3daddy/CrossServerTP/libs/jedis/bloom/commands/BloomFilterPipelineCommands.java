package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.BFInsertParams;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.BFReserveParams;

public interface BloomFilterPipelineCommands {
  Response<String> bfReserve(String paramString, double paramDouble, long paramLong);
  
  Response<String> bfReserve(String paramString, double paramDouble, long paramLong, BFReserveParams paramBFReserveParams);
  
  Response<Boolean> bfAdd(String paramString1, String paramString2);
  
  Response<List<Boolean>> bfMAdd(String paramString, String... paramVarArgs);
  
  Response<List<Boolean>> bfInsert(String paramString, String... paramVarArgs);
  
  Response<List<Boolean>> bfInsert(String paramString, BFInsertParams paramBFInsertParams, String... paramVarArgs);
  
  Response<Boolean> bfExists(String paramString1, String paramString2);
  
  Response<List<Boolean>> bfMExists(String paramString, String... paramVarArgs);
  
  Response<Map.Entry<Long, byte[]>> bfScanDump(String paramString, long paramLong);
  
  Response<String> bfLoadChunk(String paramString, long paramLong, byte[] paramArrayOfbyte);
  
  Response<Long> bfCard(String paramString);
  
  Response<Map<String, Object>> bfInfo(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\BloomFilterPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */