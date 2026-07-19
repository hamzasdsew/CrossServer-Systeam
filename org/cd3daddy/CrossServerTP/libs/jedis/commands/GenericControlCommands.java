package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Module;
import org.cd3daddy.CrossServerTP.libs.jedis.params.FailoverParams;

public interface GenericControlCommands extends ConfigCommands, ScriptingControlCommands, SlowlogCommands {
  String failover();
  
  String failover(FailoverParams paramFailoverParams);
  
  String failoverAbort();
  
  List<Module> moduleList();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\GenericControlCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */