package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
import org.cd3daddy.CrossServerTP.libs.jedis.params.MigrateParams;

public interface DatabaseCommands {
  String select(int paramInt);
  
  long dbSize();
  
  String flushDB();
  
  String flushDB(FlushMode paramFlushMode);
  
  String swapDB(int paramInt1, int paramInt2);
  
  long move(String paramString, int paramInt);
  
  long move(byte[] paramArrayOfbyte, int paramInt);
  
  boolean copy(String paramString1, String paramString2, int paramInt, boolean paramBoolean);
  
  boolean copy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt, boolean paramBoolean);
  
  String migrate(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3);
  
  String migrate(String paramString, int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3);
  
  String migrate(String paramString, int paramInt1, int paramInt2, int paramInt3, MigrateParams paramMigrateParams, String... paramVarArgs);
  
  String migrate(String paramString, int paramInt1, int paramInt2, int paramInt3, MigrateParams paramMigrateParams, byte[]... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\DatabaseCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */