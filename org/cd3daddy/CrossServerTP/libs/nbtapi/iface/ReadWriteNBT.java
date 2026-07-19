package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;

import java.util.UUID;
import javax.annotation.Nullable;
import org.bukkit.inventory.ItemStack;

public interface ReadWriteNBT extends ReadableNBT {
  void mergeCompound(ReadableNBT paramReadableNBT);
  
  void setString(String paramString1, String paramString2);
  
  void setInteger(String paramString, Integer paramInteger);
  
  void setDouble(String paramString, Double paramDouble);
  
  void setByte(String paramString, Byte paramByte);
  
  void setShort(String paramString, Short paramShort);
  
  void setLong(String paramString, Long paramLong);
  
  void setFloat(String paramString, Float paramFloat);
  
  void setByteArray(String paramString, byte[] paramArrayOfbyte);
  
  void setIntArray(String paramString, int[] paramArrayOfint);
  
  void setLongArray(String paramString, long[] paramArrayOflong);
  
  void setBoolean(String paramString, Boolean paramBoolean);
  
  void setItemStack(String paramString, ItemStack paramItemStack);
  
  void setItemStackArray(String paramString, ItemStack[] paramArrayOfItemStack);
  
  void setUUID(String paramString, UUID paramUUID);
  
  void removeKey(String paramString);
  
  ReadWriteNBT getOrCreateCompound(String paramString);
  
  @Nullable
  ReadWriteNBT getCompound(String paramString);
  
  ReadWriteNBT resolveOrCreateCompound(String paramString);
  
  <T> void set(String paramString, T paramT, NBTHandler<T> paramNBTHandler);
  
  <E extends Enum<?>> void setEnum(String paramString, E paramE);
  
  ReadWriteNBTList<String> getStringList(String paramString);
  
  ReadWriteNBTList<Integer> getIntegerList(String paramString);
  
  ReadWriteNBTList<int[]> getIntArrayList(String paramString);
  
  ReadWriteNBTList<UUID> getUUIDList(String paramString);
  
  ReadWriteNBTList<Float> getFloatList(String paramString);
  
  ReadWriteNBTList<Double> getDoubleList(String paramString);
  
  ReadWriteNBTList<Long> getLongList(String paramString);
  
  ReadWriteNBTCompoundList getCompoundList(String paramString);
  
  @Nullable
  ReadWriteNBT resolveCompound(String paramString);
  
  void clearNBT();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\ReadWriteNBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */