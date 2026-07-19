package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.bloom.TDigestMergeParams;

public interface TDigestSketchCommands {
  String tdigestCreate(String paramString);
  
  String tdigestCreate(String paramString, int paramInt);
  
  String tdigestReset(String paramString);
  
  String tdigestMerge(String paramString, String... paramVarArgs);
  
  String tdigestMerge(TDigestMergeParams paramTDigestMergeParams, String paramString, String... paramVarArgs);
  
  Map<String, Object> tdigestInfo(String paramString);
  
  String tdigestAdd(String paramString, double... paramVarArgs);
  
  List<Double> tdigestCDF(String paramString, double... paramVarArgs);
  
  List<Double> tdigestQuantile(String paramString, double... paramVarArgs);
  
  double tdigestMin(String paramString);
  
  double tdigestMax(String paramString);
  
  double tdigestTrimmedMean(String paramString, double paramDouble1, double paramDouble2);
  
  List<Long> tdigestRank(String paramString, double... paramVarArgs);
  
  List<Long> tdigestRevRank(String paramString, double... paramVarArgs);
  
  List<Double> tdigestByRank(String paramString, long... paramVarArgs);
  
  List<Double> tdigestByRevRank(String paramString, long... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\TDigestSketchCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */