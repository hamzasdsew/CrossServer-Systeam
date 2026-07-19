package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.params.CommandListFilterByParams;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.CommandDocument;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.CommandInfo;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface CommandCommands {
  long commandCount();
  
  Map<String, CommandDocument> commandDocs(String... paramVarArgs);
  
  List<String> commandGetKeys(String... paramVarArgs);
  
  List<KeyValue<String, List<String>>> commandGetKeysAndFlags(String... paramVarArgs);
  
  Map<String, CommandInfo> commandInfo(String... paramVarArgs);
  
  List<String> commandList();
  
  List<String> commandListFilterBy(CommandListFilterByParams paramCommandListFilterByParams);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\CommandCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */