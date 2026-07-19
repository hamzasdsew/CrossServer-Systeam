package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListPosition;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LPosParams;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface ListPipelineCommands {
  Response<Long> rpush(String paramString, String... paramVarArgs);
  
  Response<Long> lpush(String paramString, String... paramVarArgs);
  
  Response<Long> llen(String paramString);
  
  Response<List<String>> lrange(String paramString, long paramLong1, long paramLong2);
  
  Response<String> ltrim(String paramString, long paramLong1, long paramLong2);
  
  Response<String> lindex(String paramString, long paramLong);
  
  Response<String> lset(String paramString1, long paramLong, String paramString2);
  
  Response<Long> lrem(String paramString1, long paramLong, String paramString2);
  
  Response<String> lpop(String paramString);
  
  Response<List<String>> lpop(String paramString, int paramInt);
  
  Response<Long> lpos(String paramString1, String paramString2);
  
  Response<Long> lpos(String paramString1, String paramString2, LPosParams paramLPosParams);
  
  Response<List<Long>> lpos(String paramString1, String paramString2, LPosParams paramLPosParams, long paramLong);
  
  Response<String> rpop(String paramString);
  
  Response<List<String>> rpop(String paramString, int paramInt);
  
  Response<Long> linsert(String paramString1, ListPosition paramListPosition, String paramString2, String paramString3);
  
  Response<Long> lpushx(String paramString, String... paramVarArgs);
  
  Response<Long> rpushx(String paramString, String... paramVarArgs);
  
  Response<List<String>> blpop(int paramInt, String paramString);
  
  Response<KeyValue<String, String>> blpop(double paramDouble, String paramString);
  
  Response<List<String>> brpop(int paramInt, String paramString);
  
  Response<KeyValue<String, String>> brpop(double paramDouble, String paramString);
  
  Response<List<String>> blpop(int paramInt, String... paramVarArgs);
  
  Response<KeyValue<String, String>> blpop(double paramDouble, String... paramVarArgs);
  
  Response<List<String>> brpop(int paramInt, String... paramVarArgs);
  
  Response<KeyValue<String, String>> brpop(double paramDouble, String... paramVarArgs);
  
  Response<String> rpoplpush(String paramString1, String paramString2);
  
  Response<String> brpoplpush(String paramString1, String paramString2, int paramInt);
  
  Response<String> lmove(String paramString1, String paramString2, ListDirection paramListDirection1, ListDirection paramListDirection2);
  
  Response<String> blmove(String paramString1, String paramString2, ListDirection paramListDirection1, ListDirection paramListDirection2, double paramDouble);
  
  Response<KeyValue<String, List<String>>> lmpop(ListDirection paramListDirection, String... paramVarArgs);
  
  Response<KeyValue<String, List<String>>> lmpop(ListDirection paramListDirection, int paramInt, String... paramVarArgs);
  
  Response<KeyValue<String, List<String>>> blmpop(double paramDouble, ListDirection paramListDirection, String... paramVarArgs);
  
  Response<KeyValue<String, List<String>>> blmpop(double paramDouble, ListDirection paramListDirection, int paramInt, String... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ListPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */