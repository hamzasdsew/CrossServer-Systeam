package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.params.MigrateParams;

public interface DatabasePipelineCommands {
  Response<String> select(int paramInt);
  
  Response<Long> dbSize();
  
  Response<String> swapDB(int paramInt1, int paramInt2);
  
  Response<Long> move(String paramString, int paramInt);
  
  Response<Long> move(byte[] paramArrayOfbyte, int paramInt);
  
  Response<Boolean> copy(String paramString1, String paramString2, int paramInt, boolean paramBoolean);
  
  Response<Boolean> copy(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt, boolean paramBoolean);
  
  Response<String> migrate(String paramString, int paramInt1, byte[] paramArrayOfbyte, int paramInt2, int paramInt3);
  
  Response<String> migrate(String paramString, int paramInt1, int paramInt2, int paramInt3, MigrateParams paramMigrateParams, byte[]... paramVarArgs);
  
  Response<String> migrate(String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3);
  
  Response<String> migrate(String paramString, int paramInt1, int paramInt2, int paramInt3, MigrateParams paramMigrateParams, String... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\DatabasePipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */