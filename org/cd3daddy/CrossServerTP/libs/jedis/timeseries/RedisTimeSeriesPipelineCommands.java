package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;

public interface RedisTimeSeriesPipelineCommands {
  Response<String> tsCreate(String paramString);
  
  Response<String> tsCreate(String paramString, TSCreateParams paramTSCreateParams);
  
  Response<Long> tsDel(String paramString, long paramLong1, long paramLong2);
  
  Response<String> tsAlter(String paramString, TSAlterParams paramTSAlterParams);
  
  Response<Long> tsAdd(String paramString, double paramDouble);
  
  Response<Long> tsAdd(String paramString, long paramLong, double paramDouble);
  
  Response<Long> tsAdd(String paramString, long paramLong, double paramDouble, TSCreateParams paramTSCreateParams);
  
  Response<List<Long>> tsMAdd(Map.Entry<String, TSElement>... paramVarArgs);
  
  Response<Long> tsIncrBy(String paramString, double paramDouble);
  
  Response<Long> tsIncrBy(String paramString, double paramDouble, long paramLong);
  
  Response<Long> tsDecrBy(String paramString, double paramDouble);
  
  Response<Long> tsDecrBy(String paramString, double paramDouble, long paramLong);
  
  Response<List<TSElement>> tsRange(String paramString, long paramLong1, long paramLong2);
  
  Response<List<TSElement>> tsRange(String paramString, TSRangeParams paramTSRangeParams);
  
  Response<List<TSElement>> tsRevRange(String paramString, long paramLong1, long paramLong2);
  
  Response<List<TSElement>> tsRevRange(String paramString, TSRangeParams paramTSRangeParams);
  
  Response<Map<String, TSMRangeElements>> tsMRange(long paramLong1, long paramLong2, String... paramVarArgs);
  
  Response<Map<String, TSMRangeElements>> tsMRange(TSMRangeParams paramTSMRangeParams);
  
  Response<Map<String, TSMRangeElements>> tsMRevRange(long paramLong1, long paramLong2, String... paramVarArgs);
  
  Response<Map<String, TSMRangeElements>> tsMRevRange(TSMRangeParams paramTSMRangeParams);
  
  Response<TSElement> tsGet(String paramString);
  
  Response<TSElement> tsGet(String paramString, TSGetParams paramTSGetParams);
  
  Response<Map<String, TSMGetElement>> tsMGet(TSMGetParams paramTSMGetParams, String... paramVarArgs);
  
  Response<String> tsCreateRule(String paramString1, String paramString2, AggregationType paramAggregationType, long paramLong);
  
  Response<String> tsCreateRule(String paramString1, String paramString2, AggregationType paramAggregationType, long paramLong1, long paramLong2);
  
  Response<String> tsDeleteRule(String paramString1, String paramString2);
  
  Response<List<String>> tsQueryIndex(String... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\RedisTimeSeriesPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */