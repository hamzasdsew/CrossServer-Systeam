package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.Response;

public interface HyperLogLogPipelineCommands {
  Response<Long> pfadd(String paramString, String... paramVarArgs);
  
  Response<String> pfmerge(String paramString, String... paramVarArgs);
  
  Response<Long> pfcount(String paramString);
  
  Response<Long> pfcount(String... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HyperLogLogPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */