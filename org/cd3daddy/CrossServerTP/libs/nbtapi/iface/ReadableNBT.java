/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ReadableNBT
/*     */ {
/*     */   String getString(String paramString);
/*     */   
/*     */   Integer getInteger(String paramString);
/*     */   
/*     */   Double getDouble(String paramString);
/*     */   
/*     */   Byte getByte(String paramString);
/*     */   
/*     */   Short getShort(String paramString);
/*     */   
/*     */   Long getLong(String paramString);
/*     */   
/*     */   Float getFloat(String paramString);
/*     */   
/*     */   @Nullable
/*     */   byte[] getByteArray(String paramString);
/*     */   
/*     */   @Nullable
/*     */   int[] getIntArray(String paramString);
/*     */   
/*     */   @Nullable
/*     */   long[] getLongArray(String paramString);
/*     */   
/*     */   Boolean getBoolean(String paramString);
/*     */   
/*     */   @Nullable
/*     */   ItemStack getItemStack(String paramString);
/*     */   
/*     */   @Nullable
/*     */   ItemStack[] getItemStackArray(String paramString);
/*     */   
/*     */   @Nullable
/*     */   UUID getUUID(String paramString);
/*     */   
/*     */   boolean hasTag(String paramString);
/*     */   
/*     */   default boolean hasTag(String key, NBTType type) {
/* 169 */     return (hasTag(key) && getType(key) == type);
/*     */   }
/*     */   
/*     */   Set<String> getKeys();
/*     */   
/*     */   @Nullable
/*     */   ReadableNBT getCompound(String paramString);
/*     */   
/*     */   ReadableNBTList<String> getStringList(String paramString);
/*     */   
/*     */   ReadableNBTList<Integer> getIntegerList(String paramString);
/*     */   
/*     */   ReadableNBTList<int[]> getIntArrayList(String paramString);
/*     */   
/*     */   ReadableNBTList<UUID> getUUIDList(String paramString);
/*     */   
/*     */   ReadableNBTList<Float> getFloatList(String paramString);
/*     */   
/*     */   ReadableNBTList<Double> getDoubleList(String paramString);
/*     */   
/*     */   ReadableNBTList<Long> getLongList(String paramString);
/*     */   
/*     */   @Nullable
/*     */   NBTType getListType(String paramString);
/*     */   
/*     */   ReadableNBTList<ReadWriteNBT> getCompoundList(String paramString);
/*     */   
/*     */   <T> T getOrDefault(String paramString, T paramT);
/*     */   
/*     */   @Nullable
/*     */   <T> T getOrNull(String paramString, Class<?> paramClass);
/*     */   
/*     */   @Nullable
/*     */   <T> T resolveOrNull(String paramString, Class<?> paramClass);
/*     */   
/*     */   <T> T resolveOrDefault(String paramString, T paramT);
/*     */   
/*     */   @Nullable
/*     */   ReadableNBT resolveCompound(String paramString);
/*     */   
/*     */   <T> T get(String paramString, NBTHandler<T> paramNBTHandler);
/*     */   
/*     */   @Nullable
/*     */   <E extends Enum<E>> E getEnum(String paramString, Class<E> paramClass);
/*     */   
/*     */   NBTType getType(String paramString);
/*     */   
/*     */   void writeCompound(OutputStream paramOutputStream);
/*     */   
/*     */   ReadWriteNBT extractDifference(ReadableNBT paramReadableNBT);
/*     */   
/*     */   String toString();
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\ReadableNBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */