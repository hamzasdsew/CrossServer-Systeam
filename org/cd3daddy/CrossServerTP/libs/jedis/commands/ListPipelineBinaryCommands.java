package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import org.cd3daddy.CrossServerTP.libs.jedis.Response;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ListPosition;
import org.cd3daddy.CrossServerTP.libs.jedis.params.LPosParams;
import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;

public interface ListPipelineBinaryCommands {
  Response<Long> rpush(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<Long> lpush(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<Long> llen(byte[] paramArrayOfbyte);
  
  Response<List<byte[]>> lrange(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  Response<String> ltrim(byte[] paramArrayOfbyte, long paramLong1, long paramLong2);
  
  Response<byte[]> lindex(byte[] paramArrayOfbyte, long paramLong);
  
  Response<String> lset(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  Response<Long> lrem(byte[] paramArrayOfbyte1, long paramLong, byte[] paramArrayOfbyte2);
  
  Response<byte[]> lpop(byte[] paramArrayOfbyte);
  
  Response<List<byte[]>> lpop(byte[] paramArrayOfbyte, int paramInt);
  
  Response<Long> lpos(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<Long> lpos(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, LPosParams paramLPosParams);
  
  Response<List<Long>> lpos(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, LPosParams paramLPosParams, long paramLong);
  
  Response<byte[]> rpop(byte[] paramArrayOfbyte);
  
  Response<List<byte[]>> rpop(byte[] paramArrayOfbyte, int paramInt);
  
  Response<Long> linsert(byte[] paramArrayOfbyte1, ListPosition paramListPosition, byte[] paramArrayOfbyte2, byte[] paramArrayOfbyte3);
  
  Response<Long> lpushx(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<Long> rpushx(byte[] paramArrayOfbyte, byte[]... paramVarArgs);
  
  Response<List<byte[]>> blpop(int paramInt, byte[]... paramVarArgs);
  
  Response<KeyValue<byte[], byte[]>> blpop(double paramDouble, byte[]... paramVarArgs);
  
  Response<List<byte[]>> brpop(int paramInt, byte[]... paramVarArgs);
  
  Response<KeyValue<byte[], byte[]>> brpop(double paramDouble, byte[]... paramVarArgs);
  
  Response<byte[]> rpoplpush(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2);
  
  Response<byte[]> brpoplpush(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, int paramInt);
  
  Response<byte[]> lmove(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ListDirection paramListDirection1, ListDirection paramListDirection2);
  
  Response<byte[]> blmove(byte[] paramArrayOfbyte1, byte[] paramArrayOfbyte2, ListDirection paramListDirection1, ListDirection paramListDirection2, double paramDouble);
  
  Response<KeyValue<byte[], List<byte[]>>> lmpop(ListDirection paramListDirection, byte[]... paramVarArgs);
  
  Response<KeyValue<byte[], List<byte[]>>> lmpop(ListDirection paramListDirection, int paramInt, byte[]... paramVarArgs);
  
  Response<KeyValue<byte[], List<byte[]>>> blmpop(double paramDouble, ListDirection paramListDirection, byte[]... paramVarArgs);
  
  Response<KeyValue<byte[], List<byte[]>>> blmpop(double paramDouble, ListDirection paramListDirection, int paramInt, byte[]... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ListPipelineBinaryCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */