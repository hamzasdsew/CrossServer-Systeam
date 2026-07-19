package org.cd3daddy.CrossServerTP.libs.jedis.graph;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;

@Deprecated
public interface RedisGraphPipelineCommands {
  @Deprecated
  Response<ResultSet> graphQuery(String paramString1, String paramString2);
  
  @Deprecated
  Response<ResultSet> graphReadonlyQuery(String paramString1, String paramString2);
  
  @Deprecated
  Response<ResultSet> graphQuery(String paramString1, String paramString2, long paramLong);
  
  @Deprecated
  Response<ResultSet> graphReadonlyQuery(String paramString1, String paramString2, long paramLong);
  
  @Deprecated
  Response<ResultSet> graphQuery(String paramString1, String paramString2, Map<String, Object> paramMap);
  
  @Deprecated
  Response<ResultSet> graphReadonlyQuery(String paramString1, String paramString2, Map<String, Object> paramMap);
  
  @Deprecated
  Response<ResultSet> graphQuery(String paramString1, String paramString2, Map<String, Object> paramMap, long paramLong);
  
  @Deprecated
  Response<ResultSet> graphReadonlyQuery(String paramString1, String paramString2, Map<String, Object> paramMap, long paramLong);
  
  @Deprecated
  Response<String> graphDelete(String paramString);
  
  @Deprecated
  Response<List<String>> graphProfile(String paramString1, String paramString2);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\RedisGraphPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */