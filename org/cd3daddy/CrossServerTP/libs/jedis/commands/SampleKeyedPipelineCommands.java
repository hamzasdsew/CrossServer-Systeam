package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface SampleKeyedPipelineCommands {
  Response<Long> waitReplicas(String paramString, int paramInt, long paramLong);
  
  Response<KeyValue<Long, Long>> waitAOF(String paramString, long paramLong1, long paramLong2, long paramLong3);
  
  Response<Object> eval(String paramString1, String paramString2);
  
  Response<Object> evalsha(String paramString1, String paramString2);
  
  Response<List<Boolean>> scriptExists(String paramString, String... paramVarArgs);
  
  Response<String> scriptLoad(String paramString1, String paramString2);
  
  Response<String> scriptFlush(String paramString);
  
  Response<String> scriptFlush(String paramString, FlushMode paramFlushMode);
  
  Response<String> scriptKill(String paramString);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SampleKeyedPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */