package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;

public interface CountMinSketchPipelineCommands {
  Response<String> cmsInitByDim(String paramString, long paramLong1, long paramLong2);
  
  Response<String> cmsInitByProb(String paramString, double paramDouble1, double paramDouble2);
  
  Response<List<Long>> cmsIncrBy(String paramString, Map<String, Long> paramMap);
  
  Response<List<Long>> cmsQuery(String paramString, String... paramVarArgs);
  
  Response<String> cmsMerge(String paramString, String... paramVarArgs);
  
  Response<String> cmsMerge(String paramString, Map<String, Long> paramMap);
  
  Response<Map<String, Object>> cmsInfo(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\CountMinSketchPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */