package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;

import java.util.List;
import java.util.Map;

public interface RedisTimeSeriesCommands {
  String tsCreate(String paramString);
  
  String tsCreate(String paramString, TSCreateParams paramTSCreateParams);
  
  long tsDel(String paramString, long paramLong1, long paramLong2);
  
  String tsAlter(String paramString, TSAlterParams paramTSAlterParams);
  
  long tsAdd(String paramString, double paramDouble);
  
  long tsAdd(String paramString, long paramLong, double paramDouble);
  
  long tsAdd(String paramString, long paramLong, double paramDouble, TSCreateParams paramTSCreateParams);
  
  List<Long> tsMAdd(Map.Entry<String, TSElement>... paramVarArgs);
  
  long tsIncrBy(String paramString, double paramDouble);
  
  long tsIncrBy(String paramString, double paramDouble, long paramLong);
  
  long tsDecrBy(String paramString, double paramDouble);
  
  long tsDecrBy(String paramString, double paramDouble, long paramLong);
  
  List<TSElement> tsRange(String paramString, long paramLong1, long paramLong2);
  
  List<TSElement> tsRange(String paramString, TSRangeParams paramTSRangeParams);
  
  List<TSElement> tsRevRange(String paramString, long paramLong1, long paramLong2);
  
  List<TSElement> tsRevRange(String paramString, TSRangeParams paramTSRangeParams);
  
  Map<String, TSMRangeElements> tsMRange(long paramLong1, long paramLong2, String... paramVarArgs);
  
  Map<String, TSMRangeElements> tsMRange(TSMRangeParams paramTSMRangeParams);
  
  Map<String, TSMRangeElements> tsMRevRange(long paramLong1, long paramLong2, String... paramVarArgs);
  
  Map<String, TSMRangeElements> tsMRevRange(TSMRangeParams paramTSMRangeParams);
  
  TSElement tsGet(String paramString);
  
  TSElement tsGet(String paramString, TSGetParams paramTSGetParams);
  
  Map<String, TSMGetElement> tsMGet(TSMGetParams paramTSMGetParams, String... paramVarArgs);
  
  String tsCreateRule(String paramString1, String paramString2, AggregationType paramAggregationType, long paramLong);
  
  String tsCreateRule(String paramString1, String paramString2, AggregationType paramAggregationType, long paramLong1, long paramLong2);
  
  String tsDeleteRule(String paramString1, String paramString2);
  
  List<String> tsQueryIndex(String... paramVarArgs);
  
  TSInfo tsInfo(String paramString);
  
  TSInfo tsInfoDebug(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\RedisTimeSeriesCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */