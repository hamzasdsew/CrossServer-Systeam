package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;

public interface TopKFilterPipelineCommands {
  Response<String> topkReserve(String paramString, long paramLong);
  
  Response<String> topkReserve(String paramString, long paramLong1, long paramLong2, long paramLong3, double paramDouble);
  
  Response<List<String>> topkAdd(String paramString, String... paramVarArgs);
  
  Response<List<String>> topkIncrBy(String paramString, Map<String, Long> paramMap);
  
  Response<List<Boolean>> topkQuery(String paramString, String... paramVarArgs);
  
  Response<List<String>> topkList(String paramString);
  
  Response<Map<String, Long>> topkListWithCount(String paramString);
  
  Response<Map<String, Object>> topkInfo(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\TopKFilterPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */