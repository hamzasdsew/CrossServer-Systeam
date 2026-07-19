package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.BitCountOption;
import org.cd3daddy.CrossServerTP.libs.jedis.args.BitOP;
import org.cd3daddy.CrossServerTP.libs.jedis.params.BitPosParams;

public interface BitPipelineBinaryCommands {
  Response<Boolean> setbit(byte[] paramArrayOfbyte, long paramLong, boolean paramBoolean);
  
  Response<Boolean> getbit(byte[] paramArrayOfbyte, long paramLong);
  
  Response<Long> bitcount(byte[] paramArrayOfbyte);
  
  Response<Long> bitcount(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  Response<Long> bitcount(byte[] paramArrayOfbyte, long paramLong1, long paramLong2, BitCountOption paramBitCountOption);
  
  Response<Long> bitpos(byte[] paramArrayOfbyte, boolean paramBoolean);
  
  Response<Long> bitpos(byte[] paramArrayOfbyte, boolean paramBoolean, BitPosParams paramBitPosParams);
  
  Response<List<Long>> bitfield(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<List<Long>> bitfieldReadonly(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<Long> bitop(BitOP paramBitOP, byte[] paramArrayOfbyte, byte[]... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\BitPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */