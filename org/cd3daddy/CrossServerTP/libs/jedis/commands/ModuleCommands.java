package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Module;
import org.cd3daddy.CrossServerTP.libs.jedis.params.ModuleLoadExParams;

public interface ModuleCommands {
  String moduleLoad(String paramString);
  
  String moduleLoad(String paramString, String... paramVarArgs);
  
  String moduleLoadEx(String paramString, ModuleLoadExParams paramModuleLoadExParams);
  
  String moduleUnload(String paramString);
  
  List<Module> moduleList();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ModuleCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */