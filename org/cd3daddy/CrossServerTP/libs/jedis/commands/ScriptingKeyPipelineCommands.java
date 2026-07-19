package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;

public interface ScriptingKeyPipelineCommands {
  Response<Object> eval(String paramString);
  
  Response<Object> eval(String paramString, int paramInt, String... paramVarArgs);
  
  Response<Object> eval(String paramString, List<String> paramList1, List<String> paramList2);
  
  Response<Object> evalReadonly(String paramString, List<String> paramList1, List<String> paramList2);
  
  Response<Object> evalsha(String paramString);
  
  Response<Object> evalsha(String paramString, int paramInt, String... paramVarArgs);
  
  Response<Object> evalsha(String paramString, List<String> paramList1, List<String> paramList2);
  
  Response<Object> evalshaReadonly(String paramString, List<String> paramList1, List<String> paramList2);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ScriptingKeyPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */