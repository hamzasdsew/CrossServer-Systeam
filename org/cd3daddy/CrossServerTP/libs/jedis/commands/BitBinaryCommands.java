package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.BitCountOption;
import org.cd3daddy.CrossServerTP.libs.jedis.args.BitOP;
import org.cd3daddy.CrossServerTP.libs.jedis.params.BitPosParams;

public interface BitBinaryCommands {
  boolean setbit(byte[] paramArrayOfbyte, long paramLong, boolean paramBoolean);
  
  boolean getbit(byte[] paramArrayOfbyte, long paramLong);
  
  long bitcount(byte[] paramArrayOfbyte);
  
  long bitcount(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  long bitcount(byte[] paramArrayOfbyte, long paramLong1, long paramLong2, BitCountOption paramBitCountOption);
  
  long bitpos(byte[] paramArrayOfbyte, boolean paramBoolean);
  
  long bitpos(byte[] paramArrayOfbyte, boolean paramBoolean, BitPosParams paramBitPosParams);
  
  List<Long> bitfield(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  List<Long> bitfieldReadonly(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  long bitop(BitOP paramBitOP, byte[] paramArrayOfbyte, byte[]... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\BitBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */