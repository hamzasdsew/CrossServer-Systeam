package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GetExParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LCSParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.SetParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.LCSMatchResult;

public interface StringPipelineCommands extends BitPipelineCommands {
  Response<String> set(String paramString1, String paramString2);
  
  Response<String> set(String paramString1, String paramString2, SetParams paramSetParams);
  
  Response<String> get(String paramString);
  
  Response<String> setGet(String paramString1, String paramString2, SetParams paramSetParams);
  
  Response<String> getDel(String paramString);
  
  Response<String> getEx(String paramString, GetExParams paramGetExParams);
  
  Response<Long> setrange(String paramString1, long paramLong, String paramString2);
  
  Response<String> getrange(String paramString, long paramLong1, long paramLong2);
  
  Response<String> getSet(String paramString1, String paramString2);
  
  Response<Long> setnx(String paramString1, String paramString2);
  
  Response<String> setex(String paramString1, long paramLong, String paramString2);
  
  Response<String> psetex(String paramString1, long paramLong, String paramString2);
  
  Response<List<String>> mget(String... paramVarArgs);
  
  Response<String> mset(String... paramVarArgs);
  
  Response<Long> msetnx(String... paramVarArgs);
  
  Response<Long> incr(String paramString);
  
  Response<Long> incrBy(String paramString, long paramLong);
  
  Response<Double> incrByFloat(String paramString, double paramDouble);
  
  Response<Long> decr(String paramString);
  
  Response<Long> decrBy(String paramString, long paramLong);
  
  Response<Long> append(String paramString1, String paramString2);
  
  Response<String> substr(String paramString, int paramInt1, int paramInt2);
  
  Response<Long> strlen(String paramString);
  
  Response<LCSMatchResult> lcs(String paramString1, String paramString2, LCSParams paramLCSParams);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StringPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */