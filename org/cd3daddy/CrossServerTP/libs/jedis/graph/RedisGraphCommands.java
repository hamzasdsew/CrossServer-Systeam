package org.cd3daddy.CrossServerTP.libs.jedis.graph;

import java.util.List;
import java.util.Map;

@Deprecated
public interface RedisGraphCommands {
  @Deprecated
  ResultSet graphQuery(String paramString1, String paramString2);
  
  @Deprecated
  ResultSet graphReadonlyQuery(String paramString1, String paramString2);
  
  @Deprecated
  ResultSet graphQuery(String paramString1, String paramString2, long paramLong);
  
  @Deprecated
  ResultSet graphReadonlyQuery(String paramString1, String paramString2, long paramLong);
  
  @Deprecated
  ResultSet graphQuery(String paramString1, String paramString2, Map<String, Object> paramMap);
  
  @Deprecated
  ResultSet graphReadonlyQuery(String paramString1, String paramString2, Map<String, Object> paramMap);
  
  @Deprecated
  ResultSet graphQuery(String paramString1, String paramString2, Map<String, Object> paramMap, long paramLong);
  
  @Deprecated
  ResultSet graphReadonlyQuery(String paramString1, String paramString2, Map<String, Object> paramMap, long paramLong);
  
  @Deprecated
  String graphDelete(String paramString);
  
  @Deprecated
  List<String> graphList();
  
  @Deprecated
  List<String> graphProfile(String paramString1, String paramString2);
  
  @Deprecated
  List<String> graphExplain(String paramString1, String paramString2);
  
  @Deprecated
  List<List<Object>> graphSlowlog(String paramString);
  
  @Deprecated
  String graphConfigSet(String paramString, Object paramObject);
  
  @Deprecated
  Map<String, Object> graphConfigGet(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\RedisGraphCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */