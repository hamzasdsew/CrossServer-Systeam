package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.params.GetExParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LCSParams;
import org.cd3daddy.CrossServerTP.libs.jedis.params.SetParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.LCSMatchResult;

public interface StringCommands extends BitCommands {
  String set(String paramString1, String paramString2);
  
  String set(String paramString1, String paramString2, SetParams paramSetParams);
  
  String get(String paramString);
  
  String setGet(String paramString1, String paramString2);
  
  String setGet(String paramString1, String paramString2, SetParams paramSetParams);
  
  String getDel(String paramString);
  
  String getEx(String paramString, GetExParams paramGetExParams);
  
  long setrange(String paramString1, long paramLong, String paramString2);
  
  String getrange(String paramString, long paramLong1, long paramLong2);
  
  String getSet(String paramString1, String paramString2);
  
  long setnx(String paramString1, String paramString2);
  
  String setex(String paramString1, long paramLong, String paramString2);
  
  String psetex(String paramString1, long paramLong, String paramString2);
  
  List<String> mget(String... paramVarArgs);
  
  String mset(String... paramVarArgs);
  
  long msetnx(String... paramVarArgs);
  
  long incr(String paramString);
  
  long incrBy(String paramString, long paramLong);
  
  double incrByFloat(String paramString, double paramDouble);
  
  long decr(String paramString);
  
  long decrBy(String paramString, long paramLong);
  
  long append(String paramString1, String paramString2);
  
  String substr(String paramString, int paramInt1, int paramInt2);
  
  long strlen(String paramString);
  
  LCSMatchResult lcs(String paramString1, String paramString2, LCSParams paramLCSParams);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StringCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */