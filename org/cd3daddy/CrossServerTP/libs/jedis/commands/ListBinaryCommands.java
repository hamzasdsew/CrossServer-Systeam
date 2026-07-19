package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListPosition;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LPosParams;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface ListBinaryCommands {
  long rpush(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  long lpush(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  long llen(byte[] paramArrayOfbyte);
  
  List<byte[]> lrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  String ltrim(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  byte[] lindex(byte[] paramArrayOfbyte, long paramLong);
  
  String lset(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  long lrem(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  byte[] lpop(byte[] paramArrayOfbyte);
  
  List<byte[]> lpop(byte[] paramArrayOfbyte, int paramInt);
  
  Long lpos(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Long lpos(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, LPosParams paramLPosParams);
  
  List<Long> lpos(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, LPosParams paramLPosParams, long paramLong);
  
  byte[] rpop(byte[] paramArrayOfbyte);
  
  List<byte[]> rpop(byte[] paramArrayOfbyte, int paramInt);
  
  long linsert(byte[] paramArrayOfbyte1, ListPosition paramListPosition, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
  
  long lpushx(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  long rpushx(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  List<byte[]> blpop(int paramInt, byte[]... paramVarArgs);
  
  KeyValue<byte[], byte[]> blpop(double paramDouble, byte[]... paramVarArgs);
  
  List<byte[]> brpop(int paramInt, byte[]... paramVarArgs);
  
  KeyValue<byte[], byte[]> brpop(double paramDouble, byte[]... paramVarArgs);
  
  byte[] rpoplpush(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  byte[] brpoplpush(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt);
  
  byte[] lmove(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ListDirection paramListDirection1, ListDirection paramListDirection2);
  
  byte[] blmove(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ListDirection paramListDirection1, ListDirection paramListDirection2, double paramDouble);
  
  KeyValue<byte[], List<byte[]>> lmpop(ListDirection paramListDirection, byte[]... paramVarArgs);
  
  KeyValue<byte[], List<byte[]>> lmpop(ListDirection paramListDirection, int paramInt, byte[]... paramVarArgs);
  
  KeyValue<byte[], List<byte[]>> blmpop(double paramDouble, ListDirection paramListDirection, byte[]... paramVarArgs);
  
  KeyValue<byte[], List<byte[]>> blmpop(double paramDouble, ListDirection paramListDirection, int paramInt, byte[]... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ListBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */