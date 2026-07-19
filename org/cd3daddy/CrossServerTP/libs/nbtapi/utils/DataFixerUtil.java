/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*     */ 
/*     */ import com.mojang.datafixers.DSL;
/*     */ import com.mojang.datafixers.DataFixer;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTCompound;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTReflectionUtil;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFixerUtil
/*     */ {
/*     */   public static final int VERSION1_12R1 = 1343;
/*     */   public static final int VERSION1_16R1 = 2586;
/*     */   public static final int VERSION1_17R1 = 2730;
/*     */   public static final int VERSION1_18R1 = 2975;
/*     */   public static final int VERSION1_19R1 = 3120;
/*     */   public static final int VERSION1_19R3 = 3337;
/*     */   public static final int VERSION1_20R1 = 3465;
/*     */   public static final int VERSION1_20R2 = 3578;
/*     */   public static final int VERSION1_20R3 = 3700;
/*     */   public static final int VERSION1_20R4 = 3837;
/*     */   public static final int VERSION1_21R1 = 3953;
/*     */   public static final int VERSION1_21R2 = 4080;
/*     */   public static final int VERSION1_21R3 = 4189;
/*     */   public static final int VERSION1_21R4 = 4323;
/*     */   public static final int VERSION1_21R5 = 4435;
/*     */   public static final int VERSION1_21R6 = 4554;
/*     */   @Deprecated
/*     */   public static final int VERSION1_12_2 = 1343;
/*     */   @Deprecated
/*     */   public static final int VERSION1_16_5 = 2586;
/*     */   @Deprecated
/*     */   public static final int VERSION1_17_1 = 2730;
/*     */   @Deprecated
/*     */   public static final int VERSION1_18_2 = 2975;
/*     */   @Deprecated
/*     */   public static final int VERSION1_19_2 = 3120;
/*     */   @Deprecated
/*     */   public static final int VERSION1_19_4 = 3337;
/*     */   @Deprecated
/*     */   public static final int VERSION1_20_1 = 3465;
/*     */   @Deprecated
/*     */   public static final int VERSION1_20_2 = 3578;
/*     */   @Deprecated
/*     */   public static final int VERSION1_20_4 = 3700;
/*     */   @Deprecated
/*     */   public static final int VERSION1_20_5 = 3837;
/*     */   @Deprecated
/*     */   public static final int VERSION1_21 = 3953;
/*     */   @Deprecated
/*     */   public static final int VERSION1_21_2 = 4080;
/*     */   @Deprecated
/*     */   public static final int VERSION1_21_3 = 4189;
/*     */   @Deprecated
/*     */   public static final int VERSION1_21_4 = 4323;
/*     */   @Deprecated
/*     */   public static final int VERSION1_21_5 = 4435;
/*     */   
/*     */   public static Object fixUpRawItemData(Object nbt, int fromVersion, int toVersion) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
/*  71 */     DataFixer dataFixer = (DataFixer)ReflectionMethod.GET_DATAFIXER.run(null, new Object[0]);
/*  72 */     DSL.TypeReference itemStackReference = (DSL.TypeReference)ReflectionUtil.getMappedField(ClassWrapper.NMS_REFERENCES.getClazz(), "net.minecraft.util.datafix.fixes.References#ITEM_STACK").get(null);
/*  73 */     DynamicOps<Object> nbtOps = (DynamicOps<Object>)ReflectionUtil.getMappedField(ClassWrapper.NMS_NBTOPS.getClazz(), "net.minecraft.nbt.NbtOps#INSTANCE").get(null);
/*  74 */     Dynamic<Object> fixed = dataFixer.update(itemStackReference, new Dynamic(nbtOps, nbt), fromVersion, toVersion);
/*     */     
/*  76 */     return fixed.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT fixUpItemData(ReadWriteNBT nbt, int fromVersion, int toVersion) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
/*  81 */     return NBT.wrapNMSTag(fixUpRawItemData(
/*  82 */           NBTReflectionUtil.getToCompount(((NBTCompound)nbt).getCompound(), (NBTCompound)nbt), fromVersion, toVersion));
/*     */   }
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
/*     */   public static int getCurrentVersion() {
/*  96 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6))
/*  97 */       return 4554; 
/*  98 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5))
/*  99 */       return 4435; 
/* 100 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4))
/* 101 */       return 4323; 
/* 102 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R3))
/* 103 */       return 4189; 
/* 104 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R2))
/* 105 */       return 4080; 
/* 106 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R1))
/* 107 */       return 3953; 
/* 108 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4))
/* 109 */       return 3837; 
/* 110 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R3))
/* 111 */       return 3700; 
/* 112 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R1))
/* 113 */       return 3465; 
/* 114 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_19_R3))
/* 115 */       return 3337; 
/* 116 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_19_R1))
/* 117 */       return 3120; 
/* 118 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1))
/* 119 */       return 2975; 
/* 120 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1))
/* 121 */       return 2730; 
/* 122 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1))
/* 123 */       return 2586; 
/* 124 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_12_R1)) {
/* 125 */       return 1343;
/*     */     }
/* 127 */     throw new NbtApiException("Trying to update data *to* a version before 1.12.2? Something is probably going wrong, contact the plugin author.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\DataFixerUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */