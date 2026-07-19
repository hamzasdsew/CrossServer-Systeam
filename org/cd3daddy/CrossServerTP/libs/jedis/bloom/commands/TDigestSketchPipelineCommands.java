package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.TDigestMergeParams;

public interface TDigestSketchPipelineCommands {
  Response<String> tdigestCreate(String paramString);
  
  Response<String> tdigestCreate(String paramString, int paramInt);
  
  Response<String> tdigestReset(String paramString);
  
  Response<String> tdigestMerge(String paramString, String... paramVarArgs);
  
  Response<String> tdigestMerge(TDigestMergeParams paramTDigestMergeParams, String paramString, String... paramVarArgs);
  
  Response<Map<String, Object>> tdigestInfo(String paramString);
  
  Response<String> tdigestAdd(String paramString, double... paramVarArgs);
  
  Response<List<Double>> tdigestCDF(String paramString, double... paramVarArgs);
  
  Response<List<Double>> tdigestQuantile(String paramString, double... paramVarArgs);
  
  Response<Double> tdigestMin(String paramString);
  
  Response<Double> tdigestMax(String paramString);
  
  Response<Double> tdigestTrimmedMean(String paramString, double paramDouble1, double paramDouble2);
  
  Response<List<Long>> tdigestRank(String paramString, double... paramVarArgs);
  
  Response<List<Long>> tdigestRevRank(String paramString, double... paramVarArgs);
  
  Response<List<Double>> tdigestByRank(String paramString, long... paramVarArgs);
  
  Response<List<Double>> tdigestByRevRank(String paramString, long... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\TDigestSketchPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */