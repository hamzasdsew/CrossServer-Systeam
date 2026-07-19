package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;

public interface ScriptingKeyCommands {
  Object eval(String paramString);
  
  Object eval(String paramString, int paramInt, String... paramVarArgs);
  
  Object eval(String paramString, List<String> paramList1, List<String> paramList2);
  
  Object evalReadonly(String paramString, List<String> paramList1, List<String> paramList2);
  
  Object evalsha(String paramString);
  
  Object evalsha(String paramString, int paramInt, String... paramVarArgs);
  
  Object evalsha(String paramString, List<String> paramList1, List<String> paramList2);
  
  Object evalshaReadonly(String paramString, List<String> paramList1, List<String> paramList2);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ScriptingKeyCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */