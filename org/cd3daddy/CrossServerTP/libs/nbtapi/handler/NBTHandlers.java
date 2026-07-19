/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.handler;
/*    */ 
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBT;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.NBTHandler;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*    */ 
/*    */ 
/*    */ public class NBTHandlers
/*    */ {
/* 12 */   public static final NBTHandler<ItemStack> ITEM_STACK = new NBTHandler<ItemStack>()
/*    */     {
/*    */       public boolean fuzzyMatch(Object obj)
/*    */       {
/* 16 */         return obj instanceof ItemStack;
/*    */       }
/*    */ 
/*    */       
/*    */       public void set(ReadWriteNBT nbt, String key, ItemStack value) {
/* 21 */         nbt.removeKey(key);
/* 22 */         ReadWriteNBT tag = nbt.getOrCreateCompound(key);
/* 23 */         tag.mergeCompound((ReadableNBT)NBT.itemStackToNBT(value));
/*    */       }
/*    */ 
/*    */       
/*    */       public ItemStack get(ReadableNBT nbt, String key) {
/* 28 */         ReadableNBT tag = nbt.getCompound(key);
/* 29 */         if (tag != null) {
/* 30 */           return NBT.itemStackFromNBT(tag);
/*    */         }
/* 32 */         return null;
/*    */       }
/*    */     };
/*    */ 
/*    */   
/* 37 */   public static final NBTHandler<ReadableNBT> STORE_READABLE_TAG = new NBTHandler<ReadableNBT>()
/*    */     {
/*    */       public boolean fuzzyMatch(Object obj)
/*    */       {
/* 41 */         return obj instanceof ReadableNBT;
/*    */       }
/*    */ 
/*    */       
/*    */       public void set(ReadWriteNBT nbt, String key, ReadableNBT value) {
/* 46 */         nbt.removeKey(key);
/* 47 */         nbt.getOrCreateCompound(key).mergeCompound(value);
/*    */       }
/*    */ 
/*    */       
/*    */       public ReadableNBT get(ReadableNBT nbt, String key) {
/* 52 */         ReadableNBT tag = nbt.getCompound(key);
/* 53 */         if (tag != null) {
/* 54 */           ReadWriteNBT value = NBT.createNBTObject();
/* 55 */           value.mergeCompound(tag);
/* 56 */           return (ReadableNBT)value;
/*    */         } 
/* 58 */         return null;
/*    */       }
/*    */     };
/*    */ 
/*    */   
/* 63 */   public static final NBTHandler<ReadWriteNBT> STORE_READWRITE_TAG = new NBTHandler<ReadWriteNBT>()
/*    */     {
/*    */       public boolean fuzzyMatch(Object obj)
/*    */       {
/* 67 */         return obj instanceof ReadWriteNBT;
/*    */       }
/*    */ 
/*    */       
/*    */       public void set(ReadWriteNBT nbt, String key, ReadWriteNBT value) {
/* 72 */         nbt.removeKey(key);
/* 73 */         nbt.getOrCreateCompound(key).mergeCompound((ReadableNBT)value);
/*    */       }
/*    */ 
/*    */       
/*    */       public ReadWriteNBT get(ReadableNBT nbt, String key) {
/* 78 */         ReadableNBT tag = nbt.getCompound(key);
/* 79 */         if (tag != null) {
/* 80 */           ReadWriteNBT value = NBT.createNBTObject();
/* 81 */           value.mergeCompound(tag);
/* 82 */           return value;
/*    */         } 
/* 84 */         return null;
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\handler\NBTHandlers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */