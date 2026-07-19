package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface SampleKeyedCommands {
  long waitReplicas(String paramString, int paramInt, long paramLong);
  
  KeyValue<Long, Long> waitAOF(String paramString, long paramLong1, long paramLong2, long paramLong3);
  
  Object eval(String paramString1, String paramString2);
  
  Object evalsha(String paramString1, String paramString2);
  
  Boolean scriptExists(String paramString1, String paramString2);
  
  List<Boolean> scriptExists(String paramString, String... paramVarArgs);
  
  String scriptLoad(String paramString1, String paramString2);
  
  String scriptFlush(String paramString);
  
  String scriptFlush(String paramString, FlushMode paramFlushMode);
  
  String scriptKill(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SampleKeyedCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */