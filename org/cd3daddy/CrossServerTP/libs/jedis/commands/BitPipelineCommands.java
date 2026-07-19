package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.BitCountOption;
import org.cd3daddy.CrossServerTP.libs.jedis.args.BitOP;
import org.cd3daddy.CrossServerTP.libs.jedis.params.BitPosParams;

public interface BitPipelineCommands {
  Response<Boolean> setbit(String paramString, long paramLong, boolean paramBoolean);
  
  Response<Boolean> getbit(String paramString, long paramLong);
  
  Response<Long> bitcount(String paramString);
  
  Response<Long> bitcount(String paramString, long paramLong1, long paramLong2);
  
  Response<Long> bitcount(String paramString, long paramLong1, long paramLong2, BitCountOption paramBitCountOption);
  
  Response<Long> bitpos(String paramString, boolean paramBoolean);
  
  Response<Long> bitpos(String paramString, boolean paramBoolean, BitPosParams paramBitPosParams);
  
  Response<List<Long>> bitfield(String paramString, String... paramVarArgs);
  
  Response<List<Long>> bitfieldReadonly(String paramString, String... paramVarArgs);
  
  Response<Long> bitop(BitOP paramBitOP, String paramString, String... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\BitPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */