package org.cd3daddy.CrossServerTP.libs.jedis.commands;

public interface HyperLogLogCommands {
  long pfadd(String paramString, String... paramVarArgs);
  
  String pfmerge(String paramString, String... paramVarArgs);
  
  long pfcount(String paramString);
  
  long pfcount(String... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\HyperLogLogCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */