/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
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
/*     */ public enum ReflectionMethod
/*     */ {
/*  25 */   COMPOUND_SET_FLOAT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, float.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setFloat"), new Since(MinecraftVersion.MC1_18_R1, "putFloat(java.lang.String,float)")
/*     */     
/*     */     }),
/*  28 */   COMPOUND_SET_STRING(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setString"), new Since(MinecraftVersion.MC1_18_R1, "putString(java.lang.String,java.lang.String)")
/*     */     
/*     */     }),
/*  31 */   COMPOUND_SET_INT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, int.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setInt"), new Since(MinecraftVersion.MC1_18_R1, "putInt(java.lang.String,int)")
/*     */     
/*     */     }),
/*  34 */   COMPOUND_SET_BYTEARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, byte[].class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setByteArray"), new Since(MinecraftVersion.MC1_18_R1, "putByteArray(java.lang.String,byte[])")
/*     */     
/*     */     }),
/*  37 */   COMPOUND_SET_INTARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, int[].class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setIntArray"), new Since(MinecraftVersion.MC1_18_R1, "putIntArray(java.lang.String,int[])")
/*     */     
/*     */     }),
/*  40 */   COMPOUND_SET_LONGARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, long[].class }, MinecraftVersion.MC1_16_R1, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "a"), new Since(MinecraftVersion.MC1_18_R1, "putLongArray(java.lang.String,long[])")
/*     */     
/*     */     }),
/*  43 */   COMPOUND_SET_LONG(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, long.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setLong"), new Since(MinecraftVersion.MC1_18_R1, "putLong(java.lang.String,long)")
/*     */     
/*     */     }),
/*  46 */   COMPOUND_SET_SHORT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, short.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setShort"), new Since(MinecraftVersion.MC1_18_R1, "putShort(java.lang.String,short)")
/*     */     
/*     */     }),
/*  49 */   COMPOUND_SET_BYTE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, byte.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setByte"), new Since(MinecraftVersion.MC1_18_R1, "putByte(java.lang.String,byte)")
/*     */     
/*     */     }),
/*  52 */   COMPOUND_SET_DOUBLE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, double.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setDouble"), new Since(MinecraftVersion.MC1_18_R1, "putDouble(java.lang.String,double)")
/*     */     
/*     */     }),
/*  55 */   COMPOUND_SET_BOOLEAN(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, boolean.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setBoolean"), new Since(MinecraftVersion.MC1_18_R1, "putBoolean(java.lang.String,boolean)")
/*     */     
/*     */     }),
/*  58 */   COMPOUND_SET_UUID(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, UUID.class }, MinecraftVersion.MC1_16_R1, MinecraftVersion.MC1_21_R3, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "a"), new Since(MinecraftVersion.MC1_18_R1, "putUUID(java.lang.String,java.util.UUID)")
/*     */     
/*     */     }),
/*  61 */   COMPOUND_MERGE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_8_R3, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "a"), new Since(MinecraftVersion.MC1_18_R1, "merge(net.minecraft.nbt.CompoundTag)")
/*     */     
/*     */     }),
/*  64 */   COMPOUND_SET(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, ClassWrapper.NMS_NBTBASE.getClazz() }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "set"), new Since(MinecraftVersion.MC1_18_R1, "put(java.lang.String,net.minecraft.nbt.Tag)")
/*     */     
/*     */     }),
/*  67 */   COMPOUND_GET(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "get"), new Since(MinecraftVersion.MC1_18_R1, "get(java.lang.String)")
/*     */     
/*     */     }),
/*  70 */   COMPOUND_GET_LIST_LEGACY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class, int.class }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_21_R3, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getList"), new Since(MinecraftVersion.MC1_18_R1, "getList(java.lang.String,int)")
/*     */     
/*     */     }),
/*  73 */   COMPOUND_GET_LIST(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_21_R4, "getList(java.lang.String)")
/*     */     
/*     */     }),
/*  76 */   COMPOUND_OWN_TYPE_LEGACY(ClassWrapper.NMS_NBTBASE, new Class[0], MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getTypeId")
/*     */     }),
/*  78 */   TAGTYPE_OWN_TYPE(ClassWrapper.NMS_NBTBASE, new Class[0], MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_21_R4, "getType()")
/*     */     }),
/*  80 */   TAGTYPE_GET_NAME(ClassWrapper.NMS_TAGTYPE, new Class[0], MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_21_R4, "getName()")
/*     */     
/*     */     }),
/*  83 */   COMPOUND_GET_FLOAT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getFloat"), new Since(MinecraftVersion.MC1_18_R1, "getFloat(java.lang.String)")
/*     */     
/*     */     }),
/*  86 */   COMPOUND_GET_STRING(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getString"), new Since(MinecraftVersion.MC1_18_R1, "getString(java.lang.String)")
/*     */     
/*     */     }),
/*  89 */   COMPOUND_GET_INT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getInt"), new Since(MinecraftVersion.MC1_18_R1, "getInt(java.lang.String)")
/*     */     
/*     */     }),
/*  92 */   COMPOUND_GET_BYTEARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getByteArray"), new Since(MinecraftVersion.MC1_18_R1, "getByteArray(java.lang.String)")
/*     */     
/*     */     }),
/*  95 */   COMPOUND_GET_INTARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getIntArray"), new Since(MinecraftVersion.MC1_18_R1, "getIntArray(java.lang.String)")
/*     */     
/*     */     }),
/*  98 */   COMPOUND_GET_LONGARRAY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_16_R1, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "getLongArray"), new Since(MinecraftVersion.MC1_18_R1, "getLongArray(java.lang.String)")
/*     */     
/*     */     }),
/* 101 */   COMPOUND_GET_LONG(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getLong"), new Since(MinecraftVersion.MC1_18_R1, "getLong(java.lang.String)")
/*     */     
/*     */     }),
/* 104 */   COMPOUND_GET_SHORT(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getShort"), new Since(MinecraftVersion.MC1_18_R1, "getShort(java.lang.String)")
/*     */     
/*     */     }),
/* 107 */   COMPOUND_GET_BYTE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getByte"), new Since(MinecraftVersion.MC1_18_R1, "getByte(java.lang.String)")
/*     */     
/*     */     }),
/* 110 */   COMPOUND_GET_DOUBLE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getDouble"), new Since(MinecraftVersion.MC1_18_R1, "getDouble(java.lang.String)")
/*     */     
/*     */     }),
/* 113 */   COMPOUND_GET_BOOLEAN(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getBoolean"), new Since(MinecraftVersion.MC1_18_R1, "getBoolean(java.lang.String)")
/*     */     
/*     */     }),
/* 116 */   COMPOUND_GET_UUID(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_16_R1, MinecraftVersion.MC1_21_R3, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "a"), new Since(MinecraftVersion.MC1_18_R1, "getUUID(java.lang.String)")
/*     */     
/*     */     }),
/* 119 */   COMPOUND_GET_COMPOUND(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getCompound"), new Since(MinecraftVersion.MC1_18_R1, "getCompound(java.lang.String)")
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 124 */   NMSITEM_GETTAG(ClassWrapper.NMS_ITEMSTACK, new Class[0], MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getTag"), new Since(MinecraftVersion.MC1_18_R1, "getTag()")
/*     */     }),
/* 126 */   NMSITEM_SAVE(ClassWrapper.NMS_ITEMSTACK, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "save"), new Since(MinecraftVersion.MC1_18_R1, "save(net.minecraft.nbt.CompoundTag)")
/*     */     
/*     */     }),
/* 129 */   NMSITEM_CREATESTACK(ClassWrapper.NMS_ITEMSTACK, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_10_R1, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "createStack")
/*     */     
/*     */     }),
/* 132 */   COMPOUND_REMOVE_KEY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "remove"), new Since(MinecraftVersion.MC1_18_R1, "remove(java.lang.String)")
/*     */     
/*     */     }),
/* 135 */   COMPOUND_HAS_KEY(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "hasKey"), new Since(MinecraftVersion.MC1_18_R1, "contains(java.lang.String)")
/*     */     
/*     */     }),
/* 138 */   COMPOUND_GET_TYPE(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[] { String.class }, MinecraftVersion.MC1_8_R3, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "b"), new Since(MinecraftVersion.MC1_9_R1, "d"), new Since(MinecraftVersion.MC1_15_R1, "e"), new Since(MinecraftVersion.MC1_16_R1, "d"), new Since(MinecraftVersion.MC1_18_R1, "getTagType(java.lang.String)")
/*     */ 
/*     */     
/*     */     }),
/* 142 */   COMPOUND_GET_KEYS(ClassWrapper.NMS_NBTTAGCOMPOUND, new Class[0], MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "c"), new Since(MinecraftVersion.MC1_13_R1, "getKeys"), new Since(MinecraftVersion.MC1_18_R1, "getAllKeys()"), new Since(MinecraftVersion.MC1_21_R5, "keySet()")
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 149 */   LIST_REMOVE_KEY(ClassWrapper.NMS_NBTTAGLIST, new Class[] { int.class }, MinecraftVersion.MC1_8_R3, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "a"), new Since(MinecraftVersion.MC1_9_R1, "remove"), new Since(MinecraftVersion.MC1_18_R1, "remove(int)")
/*     */     
/*     */     }),
/* 152 */   LIST_SIZE(ClassWrapper.NMS_NBTTAGLIST, new Class[0], MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "size"), new Since(MinecraftVersion.MC1_18_R1, "size()")
/*     */     }),
/* 154 */   LIST_SET(ClassWrapper.NMS_NBTTAGLIST, new Class[] { int.class, ClassWrapper.NMS_NBTBASE.getClazz() }, MinecraftVersion.MC1_8_R3, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "a"), new Since(MinecraftVersion.MC1_13_R1, "set"), new Since(MinecraftVersion.MC1_18_R1, "setTag(int,net.minecraft.nbt.Tag)")
/*     */ 
/*     */     
/*     */     }),
/* 158 */   LEGACY_LIST_ADD(ClassWrapper.NMS_NBTTAGLIST, new Class[] { ClassWrapper.NMS_NBTBASE.getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_13_R2, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "add")
/*     */     }),
/* 160 */   LIST_ADD(ClassWrapper.NMS_NBTTAGLIST, new Class[] { int.class, ClassWrapper.NMS_NBTBASE.getClazz() }, MinecraftVersion.MC1_14_R1, new Since[] { new Since(MinecraftVersion.MC1_14_R1, "add"), new Since(MinecraftVersion.MC1_18_R1, "addTag(int,net.minecraft.nbt.Tag)")
/*     */     
/*     */     }),
/* 163 */   LIST_GET_STRING(ClassWrapper.NMS_NBTTAGLIST, new Class[] { int.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getString"), new Since(MinecraftVersion.MC1_18_R1, "getString(int)")
/*     */     }),
/* 165 */   LIST_GET_COMPOUND(ClassWrapper.NMS_NBTTAGLIST, new Class[] { int.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "get"), new Since(MinecraftVersion.MC1_18_R1, "getCompound(int)")
/*     */     }),
/* 167 */   LIST_GET(ClassWrapper.NMS_NBTTAGLIST, new Class[] { int.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "get"), new Since(MinecraftVersion.MC1_8_R3, "g"), new Since(MinecraftVersion.MC1_9_R1, "h"), new Since(MinecraftVersion.MC1_12_R1, "i"), new Since(MinecraftVersion.MC1_13_R1, "get"), new Since(MinecraftVersion.MC1_18_R1, "get(int)")
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 172 */   ITEMSTACK_SET_TAG(ClassWrapper.NMS_ITEMSTACK, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "setTag"), new Since(MinecraftVersion.MC1_18_R1, "setTag(net.minecraft.nbt.CompoundTag)")
/*     */     
/*     */     }),
/* 175 */   ITEMSTACK_NMSCOPY(ClassWrapper.CRAFT_ITEMSTACK, new Class[] { ItemStack.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "asNMSCopy")
/*     */     }),
/* 177 */   ITEMSTACK_BUKKITMIRROR(ClassWrapper.CRAFT_ITEMSTACK, new Class[] { ClassWrapper.NMS_ITEMSTACK.getClazz() }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "asCraftMirror")
/*     */     
/*     */     }),
/* 180 */   CRAFT_WORLD_GET_HANDLE(ClassWrapper.CRAFT_WORLD, new Class[0], MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getHandle")
/*     */     }),
/* 182 */   NMS_WORLD_GET_TILEENTITY(ClassWrapper.NMS_WORLDSERVER, new Class[] { ClassWrapper.NMS_BLOCKPOSITION.getClazz() }, MinecraftVersion.MC1_8_R3, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "getTileEntity"), new Since(MinecraftVersion.MC1_18_R1, "getBlockEntity(net.minecraft.core.BlockPos)")
/*     */     
/*     */     }),
/* 185 */   NMS_WORLD_REMOVE_TILEENTITY(ClassWrapper.NMS_WORLDSERVER, new Class[] { ClassWrapper.NMS_BLOCKPOSITION.getClazz() }, MinecraftVersion.MC1_8_R3, MinecraftVersion.MC1_17_R1, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "t"), new Since(MinecraftVersion.MC1_9_R1, "s"), new Since(MinecraftVersion.MC1_13_R1, "n"), new Since(MinecraftVersion.MC1_14_R1, "removeTileEntity")
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 190 */   NMS_WORLD_GET_TILEENTITY_1_7_10(ClassWrapper.NMS_WORLDSERVER, new Class[] { int.class, int.class, int.class }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getTileEntity")
/*     */     
/*     */     }),
/* 193 */   TILEENTITY_LOAD_LEGACY191(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_MINECRAFTSERVER
/* 194 */       .getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_9_R1, MinecraftVersion.MC1_9_R1, new Since[] { new Since(MinecraftVersion.MC1_9_R1, "a")
/*     */     }),
/* 196 */   TILEENTITY_LOAD_LEGACY183(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_8_R3, MinecraftVersion.MC1_9_R2, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "c"), new Since(MinecraftVersion.MC1_9_R1, "a"), new Since(MinecraftVersion.MC1_9_R2, "c")
/*     */     
/*     */     }),
/* 199 */   TILEENTITY_LOAD_LEGACY1121(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_WORLD
/* 200 */       .getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_10_R1, MinecraftVersion.MC1_12_R1, new Since[] { new Since(MinecraftVersion.MC1_10_R1, "a"), new Since(MinecraftVersion.MC1_12_R1, "create")
/*     */     
/*     */     }),
/* 203 */   TILEENTITY_LOAD_LEGACY1151(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_13_R1, MinecraftVersion.MC1_15_R1, new Since[] { new Since(MinecraftVersion.MC1_12_R1, "create")
/*     */     }),
/* 205 */   TILEENTITY_LOAD(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_IBLOCKDATA
/* 206 */       .getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_16_R1, MinecraftVersion.MC1_16_R3, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "create")
/*     */     
/*     */     }),
/* 209 */   TILEENTITY_GET_NBT(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_17_R1, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "b"), new Since(MinecraftVersion.MC1_9_R1, "save")
/*     */     
/*     */     }),
/* 212 */   TILEENTITY_GET_NBT_1181(ClassWrapper.NMS_TILEENTITY, new Class[0], MinecraftVersion.MC1_18_R1, MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_18_R1, "saveWithId()")
/*     */     }),
/* 214 */   TILEENTITY_SET_NBT_LEGACY1151(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND
/* 215 */       .getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_15_R1, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "a"), new Since(MinecraftVersion.MC1_12_R1, "load")
/*     */     
/*     */     }),
/* 218 */   TILEENTITY_SET_NBT_LEGACY1161(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_IBLOCKDATA
/* 219 */       .getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_16_R1, MinecraftVersion.MC1_16_R3, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "load")
/*     */     }),
/* 221 */   TILEENTITY_SET_NBT(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_17_R1, MinecraftVersion.MC1_20_R4, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "load"), new Since(MinecraftVersion.MC1_18_R1, "load(net.minecraft.nbt.CompoundTag)")
/*     */     
/*     */     }),
/* 224 */   TILEENTITY_GET_BLOCKDATA(ClassWrapper.NMS_TILEENTITY, new Class[0], MinecraftVersion.MC1_16_R1, new Since[] { new Since(MinecraftVersion.MC1_16_R1, "getBlock"), new Since(MinecraftVersion.MC1_18_R1, "getBlockState()")
/*     */ 
/*     */     
/*     */     }),
/* 228 */   CRAFT_ENTITY_GET_HANDLE(ClassWrapper.CRAFT_ENTITY, new Class[0], MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "getHandle")
/*     */     }),
/* 230 */   NMS_ENTITY_SET_NBT(ClassWrapper.NMS_ENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "f"), new Since(MinecraftVersion.MC1_16_R1, "load"), new Since(MinecraftVersion.MC1_18_R1, "load(net.minecraft.nbt.CompoundTag)")
/*     */ 
/*     */     
/*     */     }),
/* 234 */   NMS_ENTITY_GET_NBT(ClassWrapper.NMS_ENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "e"), new Since(MinecraftVersion.MC1_12_R1, "save"), new Since(MinecraftVersion.MC1_18_R1, "saveWithoutId(net.minecraft.nbt.CompoundTag)")
/*     */ 
/*     */     
/*     */     }),
/* 238 */   NMS_ENTITY_GETSAVEID(ClassWrapper.NMS_ENTITY, new Class[0], MinecraftVersion.MC1_14_R1, new Since[] { new Since(MinecraftVersion.MC1_14_R1, "getSaveID"), new Since(MinecraftVersion.MC1_18_R1, "getEncodeId()")
/*     */     
/*     */     }),
/* 241 */   NBTFILE_READ(ClassWrapper.NMS_NBTCOMPRESSEDSTREAMTOOLS, new Class[] { InputStream.class }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_20_R2, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "a"), new Since(MinecraftVersion.MC1_18_R1, "readCompressed(java.io.InputStream)")
/*     */ 
/*     */     
/*     */     }),
/* 245 */   NBTFILE_READV2(ClassWrapper.NMS_NBTCOMPRESSEDSTREAMTOOLS, new Class[] { InputStream.class, ClassWrapper.NMS_NBTACCOUNTER
/* 246 */       .getClazz() }, MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_20_R3, "readCompressed(java.io.InputStream,net.minecraft.nbt.NbtAccounter)")
/*     */ 
/*     */     
/*     */     }),
/* 250 */   NBTACCOUNTER_CREATE_UNLIMITED(ClassWrapper.NMS_NBTACCOUNTER, new Class[0], MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_20_R3, "unlimitedHeap()")
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 255 */   NBTFILE_WRITE(ClassWrapper.NMS_NBTCOMPRESSEDSTREAMTOOLS, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND
/* 256 */       .getClazz(), OutputStream.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "a"), new Since(MinecraftVersion.MC1_18_R1, "writeCompressed(net.minecraft.nbt.CompoundTag,java.io.OutputStream)")
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 261 */   PARSE_NBT(ClassWrapper.NMS_MOJANGSONPARSER, new Class[] { String.class }, MinecraftVersion.MC1_7_R4, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "parse"), new Since(MinecraftVersion.MC1_18_R1, "parseTag(java.lang.String)"), new Since(MinecraftVersion.MC1_21_R5, "parseCompoundFully(java.lang.String)")
/*     */     
/*     */     }),
/* 264 */   REGISTRY_KEYSET(ClassWrapper.NMS_REGISTRYSIMPLE, new Class[0], MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new Since[] { new Since(MinecraftVersion.MC1_11_R1, "keySet")
/*     */     }),
/* 266 */   REGISTRY_GET(ClassWrapper.NMS_REGISTRYSIMPLE, new Class[] { Object.class }, MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new Since[] { new Since(MinecraftVersion.MC1_11_R1, "get")
/*     */     }),
/* 268 */   REGISTRY_SET(ClassWrapper.NMS_REGISTRYSIMPLE, new Class[] { Object.class, Object.class }, MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new Since[] { new Since(MinecraftVersion.MC1_11_R1, "a")
/*     */     }),
/* 270 */   REGISTRY_GET_INVERSE(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[] { Object.class }, MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_13_R1, new Since[] { new Since(MinecraftVersion.MC1_11_R1, "b")
/*     */     }),
/* 272 */   REGISTRYMATERIALS_KEYSET(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[0], MinecraftVersion.MC1_13_R1, MinecraftVersion.MC1_17_R1, new Since[] { new Since(MinecraftVersion.MC1_13_R1, "keySet")
/*     */     }),
/* 274 */   REGISTRYMATERIALS_GET(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[] { ClassWrapper.NMS_MINECRAFTKEY.getClazz() }, MinecraftVersion.MC1_13_R1, MinecraftVersion.MC1_17_R1, new Since[] { new Since(MinecraftVersion.MC1_13_R1, "get")
/*     */     }),
/* 276 */   REGISTRYMATERIALS_GETKEY(ClassWrapper.NMS_REGISTRYMATERIALS, new Class[] { Object.class }, MinecraftVersion.MC1_13_R2, MinecraftVersion.MC1_17_R1, new Since[] { new Since(MinecraftVersion.MC1_13_R2, "getKey")
/*     */     
/*     */     }),
/* 279 */   GAMEPROFILE_DESERIALIZE(ClassWrapper.NMS_GAMEPROFILESERIALIZER, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND
/* 280 */       .getClazz() }, MinecraftVersion.MC1_7_R4, MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_7_R4, "deserialize"), new Since(MinecraftVersion.MC1_18_R1, "readGameProfile(net.minecraft.nbt.CompoundTag)")
/*     */     
/*     */     }),
/* 283 */   GAMEPROFILE_SERIALIZE(ClassWrapper.NMS_GAMEPROFILESERIALIZER, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND
/* 284 */       .getClazz(), ClassWrapper.GAMEPROFILE.getClazz() }, MinecraftVersion.MC1_8_R3, MinecraftVersion.MC1_20_R3, new Since[] { new Since(MinecraftVersion.MC1_8_R3, "serialize"), new Since(MinecraftVersion.MC1_18_R1, "writeGameProfile(net.minecraft.nbt.CompoundTag,com.mojang.authlib.GameProfile)")
/*     */ 
/*     */ 
/*     */     
/*     */     }),
/* 289 */   CRAFT_PERSISTENT_DATA_CONTAINER_TO_TAG(ClassWrapper.CRAFT_PERSISTENTDATACONTAINER, new Class[0], MinecraftVersion.MC1_14_R1, new Since[] { new Since(MinecraftVersion.MC1_14_R1, "toTagCompound")
/*     */     }),
/* 291 */   CRAFT_PERSISTENT_DATA_CONTAINER_GET_MAP(ClassWrapper.CRAFT_PERSISTENTDATACONTAINER, new Class[0], MinecraftVersion.MC1_14_R1, new Since[] { new Since(MinecraftVersion.MC1_14_R1, "getRaw")
/*     */     }),
/* 293 */   CRAFT_PERSISTENT_DATA_CONTAINER_PUT_ALL(ClassWrapper.CRAFT_PERSISTENTDATACONTAINER, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND
/* 294 */       .getClazz() }, MinecraftVersion.MC1_14_R1, new Since[] { new Since(MinecraftVersion.MC1_14_R1, "putAll")
/*     */     
/*     */     }),
/* 297 */   NMSDATACOMPONENTHOLDER_GET(ClassWrapper.NMS_DATACOMPONENTHOLDER, new Class[] { ClassWrapper.NMS_DATACOMPONENTTYPE.getClazz() }, MinecraftVersion.MC1_20_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "get(net.minecraft.core.component.DataComponentType)")
/*     */     }),
/* 299 */   NMSCUSTOMDATA_GETCOPY(ClassWrapper.NMS_CUSTOMDATA, new Class[0], MinecraftVersion.MC1_20_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "copyTag()")
/*     */     }),
/* 301 */   NMSITEM_SET(ClassWrapper.NMS_ITEMSTACK, new Class[] { ClassWrapper.NMS_DATACOMPONENTTYPE.getClazz(), Object.class }, MinecraftVersion.MC1_20_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "set(net.minecraft.core.component.DataComponentType,java.lang.Object)")
/*     */     }),
/* 303 */   NMSITEM_SAVE_MODERN(ClassWrapper.NMS_ITEMSTACK, new Class[] { ClassWrapper.NMS_PROVIDER.getClazz() }, MinecraftVersion.MC1_20_R4, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "save(net.minecraft.core.HolderLookup$Provider)")
/*     */     }),
/* 305 */   NMSITEM_LOAD(ClassWrapper.NMS_ITEMSTACK, new Class[] { ClassWrapper.NMS_PROVIDER.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_20_R4, MinecraftVersion.MC1_21_R3, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "parseOptional(net.minecraft.core.HolderLookup$Provider,net.minecraft.nbt.CompoundTag)")
/*     */     }),
/* 307 */   NMSITEM_LOAD_MODERN(ClassWrapper.NMS_ITEMSTACK, new Class[] { ClassWrapper.NMS_PROVIDER.getClazz(), ClassWrapper.NMS_NBTBASE.getClazz() }, MinecraftVersion.MC1_21_R4, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "parse(net.minecraft.core.HolderLookup$Provider,net.minecraft.nbt.Tag)")
/*     */     }),
/* 309 */   NMSSERVER_GETREGISTRYACCESS(ClassWrapper.NMS_SERVER, new Class[0], MinecraftVersion.MC1_20_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "registryAccess()")
/*     */     }),
/* 311 */   NMSSERVER_GETSERVER(ClassWrapper.CRAFT_SERVER, new Class[0], MinecraftVersion.MC1_20_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "getServer()")
/*     */     }),
/* 313 */   TILEENTITY_GET_NBT_1205(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_PROVIDER.getClazz() }, MinecraftVersion.MC1_20_R4, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "saveWithId(net.minecraft.core.HolderLookup$Provider)")
/*     */     }),
/* 315 */   TILEENTITY_SET_NBT_1205(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), ClassWrapper.NMS_PROVIDER.getClazz() }, MinecraftVersion.MC1_20_R4, MinecraftVersion.MC1_21_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "loadWithComponents(net.minecraft.nbt.CompoundTag,net.minecraft.core.HolderLookup$Provider)")
/*     */     }),
/* 317 */   GET_DATAFIXER(ClassWrapper.NMS_DATAFIXERS, new Class[0], MinecraftVersion.MC1_20_R4, new Since[] { new Since(MinecraftVersion.MC1_20_R4, "getDataFixer()")
/*     */     
/*     */     }),
/* 320 */   GET_SERIALIZATION_CONTEXT(ClassWrapper.NMS_PROVIDER, new Class[] { ClassWrapper.NMS_DYNAMICOPS.getClazz() }, MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "createSerializationContext(com.mojang.serialization.DynamicOps)")
/*     */     }),
/* 322 */   NMS_GET_TAG_VALUE_INPUT(ClassWrapper.NMS_TAG_VALUE_INPUT, new Class[] { ClassWrapper.NMS_PROBLEM_REPORTER.getClazz(), ClassWrapper.NMS_PROVIDER.getClazz(), ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }, MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "create(net.minecraft.util.ProblemReporter,net.minecraft.core.HolderLookup$Provider,net.minecraft.nbt.CompoundTag)")
/*     */     }),
/* 324 */   NMS_GET_TAG_VALUE_OUTPUT(ClassWrapper.NMS_TAG_VALUE_OUTPUT, new Class[] { ClassWrapper.NMS_PROBLEM_REPORTER.getClazz(), ClassWrapper.NMS_PROVIDER.getClazz() }, MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "createWithContext(net.minecraft.util.ProblemReporter,net.minecraft.core.HolderLookup$Provider)")
/*     */     }),
/* 326 */   NMS_TAG_VALUE_OUTPUT_TO_TAG_COMPOUND(ClassWrapper.NMS_TAG_VALUE_OUTPUT, new Class[0], MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "buildResult()")
/*     */     }),
/* 328 */   NMS_ENTITY_SET_NBT_1216(ClassWrapper.NMS_ENTITY, new Class[] { ClassWrapper.NMS_VALUE_INPUT.getClazz() }, MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "load(net.minecraft.world.level.storage.ValueInput)")
/*     */     
/*     */     }),
/* 331 */   NMS_ENTITY_GET_NBT_1216(ClassWrapper.NMS_ENTITY, new Class[] { ClassWrapper.NMS_VALUE_OUTPUT.getClazz() }, MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "saveWithoutId(net.minecraft.world.level.storage.ValueOutput)")
/*     */     
/*     */     }),
/* 334 */   TILEENTITY_GET_NBT_1216(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_VALUE_OUTPUT.getClazz() }, MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "saveWithId(net.minecraft.world.level.storage.ValueOutput)")
/*     */     }),
/* 336 */   TILEENTITY_SET_NBT_1216(ClassWrapper.NMS_TILEENTITY, new Class[] { ClassWrapper.NMS_VALUE_INPUT.getClazz() }, MinecraftVersion.MC1_21_R5, new Since[] { new Since(MinecraftVersion.MC1_21_R5, "loadWithComponents(net.minecraft.world.level.storage.ValueInput)") });
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean loaded = false;
/*     */ 
/*     */   
/*     */   private boolean compatible = false;
/*     */ 
/*     */   
/* 346 */   private String methodName = null;
/*     */   private MinecraftVersion removedAfter;
/*     */   private Since targetVersion;
/*     */   
/*     */   ReflectionMethod(ClassWrapper targetClass, Class<?>[] args, MinecraftVersion addedSince, MinecraftVersion removedAfter, Since... methodnames) {
/* 351 */     this.removedAfter = removedAfter;
/* 352 */     this.parentClassWrapper = targetClass;
/*     */ 
/*     */     
/* 355 */     boolean specialCase = (MinecraftVersion.isForgePresent() && name().equals("COMPOUND_MERGE") && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4);
/*     */     
/* 357 */     if (!specialCase && (!MinecraftVersion.isAtLeastVersion(addedSince) || (this.removedAfter != null && 
/* 358 */       MinecraftVersion.isNewerThan(removedAfter))))
/*     */       return; 
/* 360 */     this.compatible = true;
/* 361 */     MinecraftVersion server = MinecraftVersion.getVersion();
/* 362 */     Since target = methodnames[0];
/* 363 */     for (Since s : methodnames) {
/* 364 */       if (s.version.getVersionId() <= server.getVersionId() && target.version
/* 365 */         .getVersionId() < s.version.getVersionId())
/* 366 */         target = s; 
/*     */     } 
/* 368 */     this.targetVersion = target;
/* 369 */     String targetMethodName = this.targetVersion.name;
/*     */     try {
/* 371 */       if (MinecraftVersion.isForgePresent() && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
/* 372 */         targetMethodName = Forge1710Mappings.getMethodMapping().getOrDefault(name(), targetMethodName);
/* 373 */       } else if (this.targetVersion.version.isMojangMapping()) {
/*     */         
/*     */         try {
/* 376 */           String name = this.targetVersion.name.split("\\(")[0];
/* 377 */           this.method = targetClass.getClazz().getMethod(name, args);
/* 378 */           this.method.setAccessible(true);
/* 379 */           this.loaded = true;
/* 380 */           this.methodName = name;
/*     */           return;
/* 382 */         } catch (NoSuchMethodException noSuchMethodException) {
/*     */ 
/*     */           
/* 385 */           targetMethodName = MojangToMapping.getMapping().getOrDefault(targetClass
/* 386 */               .getMojangName() + "#" + this.targetVersion.name, "Unmapped" + this.targetVersion.name);
/*     */         } 
/* 388 */       }  this.method = targetClass.getClazz().getDeclaredMethod(targetMethodName, args);
/* 389 */       this.method.setAccessible(true);
/* 390 */       this.loaded = true;
/* 391 */       this.methodName = this.targetVersion.name;
/* 392 */     } catch (NullPointerException|NoSuchMethodException|SecurityException ex) {
/*     */       try {
/* 394 */         if (this.targetVersion.version.isMojangMapping())
/* 395 */           targetMethodName = MojangToMapping.getMapping().getOrDefault(targetClass
/* 396 */               .getMojangName() + "#" + this.targetVersion.name, "Unmapped" + this.targetVersion.name); 
/* 397 */         this.method = targetClass.getClazz().getMethod(targetMethodName, args);
/* 398 */         this.method.setAccessible(true);
/* 399 */         this.loaded = true;
/* 400 */         this.methodName = this.targetVersion.name;
/* 401 */       } catch (NullPointerException|NoSuchMethodException|SecurityException ex2) {
/* 402 */         MinecraftVersion.getLogger()
/* 403 */           .warning("[NBTAPI] Unable to find the method '" + targetMethodName + "' in '" + (
/* 404 */             (targetClass.getClazz() == null) ? targetClass.getMojangName() : 
/* 405 */             targetClass.getClazz().getSimpleName()) + "' Args: " + 
/* 406 */             Arrays.toString((Object[])args) + " Enum: " + this);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Method method;
/*     */ 
/*     */ 
/*     */   
/*     */   private ClassWrapper parentClassWrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object run(Object target, Object... args) {
/* 424 */     if (this.method == null)
/* 425 */       throw new NbtApiException("Method not loaded! '" + this + "'"); 
/*     */     try {
/* 427 */       return this.method.invoke(target, args);
/* 428 */     } catch (Exception ex) {
/* 429 */       throw new NbtApiException("Error while calling the method '" + this.methodName + "', loaded: " + this.loaded + ", Enum: " + this + ", Passed Class: " + (
/* 430 */           (target == null) ? "null" : target.getClass()) + " Args: " + ((args == null) ? "null" : Arrays.toString(args)) + " Classes: " + (
/* 431 */           (args == null) ? "null" : Arrays.asList(args).stream().map(a -> (a == null) ? "NULL" : a.getClass().getName()).collect(Collectors.toList())), ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodName() {
/* 439 */     return this.methodName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isLoaded() {
/* 446 */     return this.loaded;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCompatible() {
/* 453 */     return this.compatible;
/*     */   }
/*     */   
/*     */   public Since getSelectedVersionInfo() {
/* 457 */     return this.targetVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassWrapper getParentClassWrapper() {
/* 464 */     return this.parentClassWrapper;
/*     */   }
/*     */   
/*     */   public static class Since {
/*     */     public final MinecraftVersion version;
/*     */     public final String name;
/*     */     
/*     */     public Since(MinecraftVersion version, String name) {
/* 472 */       this.version = version;
/* 473 */       this.name = name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\nmsmappings\ReflectionMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */