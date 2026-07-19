package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListPosition;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LPosParams;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface ListCommands {
  long rpush(String paramString, String... paramVarArgs);
  
  long lpush(String paramString, String... paramVarArgs);
  
  long llen(String paramString);
  
  List<String> lrange(String paramString, long paramLong1, long paramLong2);
  
  String ltrim(String paramString, long paramLong1, long paramLong2);
  
  String lindex(String paramString, long paramLong);
  
  String lset(String paramString1, long paramLong, String paramString2);
  
  long lrem(String paramString1, long paramLong, String paramString2);
  
  String lpop(String paramString);
  
  List<String> lpop(String paramString, int paramInt);
  
  Long lpos(String paramString1, String paramString2);
  
  Long lpos(String paramString1, String paramString2, LPosParams paramLPosParams);
  
  List<Long> lpos(String paramString1, String paramString2, LPosParams paramLPosParams, long paramLong);
  
  String rpop(String paramString);
  
  List<String> rpop(String paramString, int paramInt);
  
  long linsert(String paramString1, ListPosition paramListPosition, String paramString2, String paramString3);
  
  long lpushx(String paramString, String... paramVarArgs);
  
  long rpushx(String paramString, String... paramVarArgs);
  
  List<String> blpop(int paramInt, String... paramVarArgs);
  
  List<String> blpop(int paramInt, String paramString);
  
  KeyValue<String, String> blpop(double paramDouble, String... paramVarArgs);
  
  KeyValue<String, String> blpop(double paramDouble, String paramString);
  
  List<String> brpop(int paramInt, String... paramVarArgs);
  
  List<String> brpop(int paramInt, String paramString);
  
  KeyValue<String, String> brpop(double paramDouble, String... paramVarArgs);
  
  KeyValue<String, String> brpop(double paramDouble, String paramString);
  
  String rpoplpush(String paramString1, String paramString2);
  
  String brpoplpush(String paramString1, String paramString2, int paramInt);
  
  String lmove(String paramString1, String paramString2, ListDirection paramListDirection1, ListDirection paramListDirection2);
  
  String blmove(String paramString1, String paramString2, ListDirection paramListDirection1, ListDirection paramListDirection2, double paramDouble);
  
  KeyValue<String, List<String>> lmpop(ListDirection paramListDirection, String... paramVarArgs);
  
  KeyValue<String, List<String>> lmpop(ListDirection paramListDirection, int paramInt, String... paramVarArgs);
  
  KeyValue<String, List<String>> blmpop(double paramDouble, ListDirection paramListDirection, String... paramVarArgs);
  
  KeyValue<String, List<String>> blmpop(double paramDouble, ListDirection paramListDirection, int paramInt, String... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ListCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */