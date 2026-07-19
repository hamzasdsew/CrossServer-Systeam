package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.Slowlog;

public interface SlowlogCommands {
  String slowlogReset();
  
  long slowlogLen();
  
  List<Slowlog> slowlogGet();
  
  List<Object> slowlogGetBinary();
  
  List<Slowlog> slowlogGet(long paramLong);
  
  List<Object> slowlogGetBinary(long paramLong);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\SlowlogCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */