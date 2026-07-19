/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings;
/*     */ 
/*     */ import java.util.logging.Level;
/*     */ import org.bukkit.Bukkit;
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
/*     */ public enum ClassWrapper
/*     */ {
/*  19 */   CRAFT_ITEMSTACK(PackageWrapper.CRAFTBUKKIT, "inventory.CraftItemStack", null, null),
/*  20 */   CRAFT_METAITEM(PackageWrapper.CRAFTBUKKIT, "inventory.CraftMetaItem", null, null),
/*  21 */   CRAFT_ENTITY(PackageWrapper.CRAFTBUKKIT, "entity.CraftEntity", null, null),
/*  22 */   CRAFT_WORLD(PackageWrapper.CRAFTBUKKIT, "CraftWorld", null, null),
/*  23 */   CRAFT_SERVER(PackageWrapper.CRAFTBUKKIT, "CraftServer", null, null),
/*  24 */   CRAFT_PERSISTENTDATACONTAINER(PackageWrapper.CRAFTBUKKIT, "persistence.CraftPersistentDataContainer", MinecraftVersion.MC1_14_R1, null),
/*     */   
/*  26 */   NMS_NBTBASE(PackageWrapper.NMS, "NBTBase", null, null, "net.minecraft.nbt", "net.minecraft.nbt.Tag"),
/*  27 */   NMS_TAGTYPE(PackageWrapper.NMS, "NBTTagType", MinecraftVersion.MC1_21_R4, null, "net.minecraft.nbt", "net.minecraft.nbt.TagType"),
/*  28 */   NMS_NBTTAGSTRING(PackageWrapper.NMS, "NBTTagString", null, null, "net.minecraft.nbt", "net.minecraft.nbt.StringTag"),
/*     */   
/*  30 */   NMS_NBTTAGINT(PackageWrapper.NMS, "NBTTagInt", null, null, "net.minecraft.nbt", "net.minecraft.nbt.IntTag"),
/*  31 */   NMS_NBTTAGINTARRAY(PackageWrapper.NMS, "NBTTagIntArray", null, null, "net.minecraft.nbt", "net.minecraft.nbt.IntArrayTag"),
/*     */   
/*  33 */   NMS_NBTTAGFLOAT(PackageWrapper.NMS, "NBTTagFloat", null, null, "net.minecraft.nbt", "net.minecraft.nbt.FloatTag"),
/*  34 */   NMS_NBTTAGDOUBLE(PackageWrapper.NMS, "NBTTagDouble", null, null, "net.minecraft.nbt", "net.minecraft.nbt.DoubleTag"),
/*     */   
/*  36 */   NMS_NBTTAGLONG(PackageWrapper.NMS, "NBTTagLong", null, null, "net.minecraft.nbt", "net.minecraft.nbt.LongTag"),
/*  37 */   NMS_ITEMSTACK(PackageWrapper.NMS, "ItemStack", null, null, "net.minecraft.world.item", "net.minecraft.world.item.ItemStack"),
/*     */   
/*  39 */   NMS_NBTTAGCOMPOUND(PackageWrapper.NMS, "NBTTagCompound", null, null, "net.minecraft.nbt", "net.minecraft.nbt.CompoundTag"),
/*     */   
/*  41 */   NMS_NBTTAGLIST(PackageWrapper.NMS, "NBTTagList", null, null, "net.minecraft.nbt", "net.minecraft.nbt.ListTag"),
/*  42 */   NMS_NBTCOMPRESSEDSTREAMTOOLS(PackageWrapper.NMS, "NBTCompressedStreamTools", null, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtIo"),
/*     */   
/*  44 */   NMS_MOJANGSONPARSER(PackageWrapper.NMS, "MojangsonParser", null, null, "net.minecraft.nbt", "net.minecraft.nbt.TagParser"),
/*     */   
/*  46 */   NMS_TILEENTITY(PackageWrapper.NMS, "TileEntity", null, null, "net.minecraft.world.level.block.entity", "net.minecraft.world.level.block.entity.BlockEntity"),
/*     */   
/*  48 */   NMS_BLOCKPOSITION(PackageWrapper.NMS, "BlockPosition", MinecraftVersion.MC1_8_R3, null, "net.minecraft.core", "net.minecraft.core.BlockPos"),
/*     */   
/*  50 */   NMS_WORLDSERVER(PackageWrapper.NMS, "WorldServer", null, null, "net.minecraft.server.level", "net.minecraft.server.level.ServerLevel"),
/*     */   
/*  52 */   NMS_MINECRAFTSERVER(PackageWrapper.NMS, "MinecraftServer", null, null, "net.minecraft.server", "net.minecraft.server.MinecraftServer"),
/*     */   
/*  54 */   NMS_WORLD(PackageWrapper.NMS, "World", null, null, "net.minecraft.world.level", "net.minecraft.world.level.Level"),
/*  55 */   NMS_ENTITY(PackageWrapper.NMS, "Entity", null, null, "net.minecraft.world.entity", "net.minecraft.world.entity.Entity"),
/*     */   
/*  57 */   NMS_ENTITYTYPES(PackageWrapper.NMS, "EntityTypes", null, null, "net.minecraft.world.entity", "net.minecraft.world.entity.EntityType"),
/*     */   
/*  59 */   NMS_REGISTRYSIMPLE(PackageWrapper.NMS, "RegistrySimple", MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_12_R1),
/*  60 */   NMS_REGISTRYMATERIALS(PackageWrapper.NMS, "RegistryMaterials", null, null, "net.minecraft.core", "net.minecraft.core.MappedRegistry"),
/*     */   
/*  62 */   NMS_IREGISTRY(PackageWrapper.NMS, "IRegistry", null, null, "net.minecraft.core", "net.minecraft.core.Registry"),
/*  63 */   NMS_MINECRAFTKEY(PackageWrapper.NMS, "MinecraftKey", MinecraftVersion.MC1_8_R3, null, "net.minecraft.resources", "net.minecraft.resources.ResourceKey"),
/*     */   
/*  65 */   NMS_GAMEPROFILESERIALIZER(PackageWrapper.NMS, "GameProfileSerializer", null, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtUtils"),
/*     */   
/*  67 */   NMS_IBLOCKDATA(PackageWrapper.NMS, "IBlockData", MinecraftVersion.MC1_8_R3, null, "net.minecraft.world.level.block.state", "net.minecraft.world.level.block.state.BlockState"),
/*     */   
/*  69 */   NMS_NBTACCOUNTER(PackageWrapper.NMS, "NBTReadLimiter", MinecraftVersion.MC1_20_R3, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtAccounter"),
/*     */   
/*  71 */   NMS_CUSTOMDATA(PackageWrapper.NMS, "CustomData", MinecraftVersion.MC1_20_R4, null, "net.minecraft.world.item.component", "net.minecraft.world.item.component.CustomData"),
/*     */   
/*  73 */   NMS_DATACOMPONENTTYPE(PackageWrapper.NMS, "DataComponentType", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponentType"),
/*     */   
/*  75 */   NMS_DATACOMPONENTS(PackageWrapper.NMS, "DataComponents", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponents"),
/*     */   
/*  77 */   NMS_DATACOMPONENTHOLDER(PackageWrapper.NMS, "DataComponentHolder", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core.component", "net.minecraft.core.component.DataComponentHolder"),
/*     */   
/*  79 */   NMS_PROVIDER(PackageWrapper.NMS, "HolderLookup$a", MinecraftVersion.MC1_20_R4, null, "net.minecraft.core", "net.minecraft.core.HolderLookup$Provider"),
/*     */   
/*  81 */   NMS_SERVER(PackageWrapper.NMS, "MinecraftServer", MinecraftVersion.MC1_20_R4, null, "net.minecraft.server", "net.minecraft.server.MinecraftServer"),
/*     */   
/*  83 */   NMS_DATAFIXERS(PackageWrapper.NMS, "DataConverterRegistry", MinecraftVersion.MC1_20_R4, null, "net.minecraft.util.datafix", "net.minecraft.util.datafix.DataFixers"),
/*     */   
/*  85 */   NMS_REFERENCES(PackageWrapper.NMS, "DataConverterTypes", MinecraftVersion.MC1_20_R4, null, "net.minecraft.util.datafix.fixes", "net.minecraft.util.datafix.fixes.References"),
/*     */   
/*  87 */   NMS_NBTOPS(PackageWrapper.NMS, "DynamicOpsNBT", MinecraftVersion.MC1_20_R4, null, "net.minecraft.nbt", "net.minecraft.nbt.NbtOps"),
/*     */   
/*  89 */   NMS_PROBLEM_REPORTER(PackageWrapper.NMS, "ProblemReporter", MinecraftVersion.MC1_21_R5, null, "net.minecraft.util", "net.minecraft.util.ProblemReporter"),
/*     */   
/*  91 */   NMS_TAG_VALUE_INPUT(PackageWrapper.NMS, "TagValueInput", MinecraftVersion.MC1_21_R5, null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.TagValueInput"),
/*     */   
/*  93 */   NMS_VALUE_INPUT(PackageWrapper.NMS, "ValueInput", MinecraftVersion.MC1_21_R5, null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.ValueInput"),
/*     */   
/*  95 */   NMS_TAG_VALUE_OUTPUT(PackageWrapper.NMS, "TagValueOutput", MinecraftVersion.MC1_21_R5, null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.TagValueOutput"),
/*     */   
/*  97 */   NMS_VALUE_OUTPUT(PackageWrapper.NMS, "ValueOutput", MinecraftVersion.MC1_21_R5, null, "net.minecraft.world.level.storage", "net.minecraft.world.level.storage.ValueOutput"),
/*     */   
/*  99 */   NMS_DYNAMICOPS(PackageWrapper.NONE, "DynamicOps", MinecraftVersion.MC1_21_R5, null, "com.mojang.serialization", "com.mojang.serialization.DynamicOps"),
/*     */   
/* 101 */   GAMEPROFILE(PackageWrapper.NONE, "com.mojang.authlib.GameProfile", MinecraftVersion.MC1_8_R3, null);
/*     */ 
/*     */   
/*     */   private boolean enabled = false;
/*     */ 
/*     */   
/*     */   private Class<?> clazz;
/*     */   
/*     */   private final String mojangName;
/*     */ 
/*     */   
/*     */   ClassWrapper(PackageWrapper packageId, String clazzName, MinecraftVersion from, MinecraftVersion to, String mojangMap, String mojangName) {
/* 113 */     this.mojangName = mojangName;
/* 114 */     if (from != null && MinecraftVersion.getVersion().getVersionId() < from.getVersionId()) {
/*     */       return;
/*     */     }
/* 117 */     if (to != null && MinecraftVersion.getVersion().getVersionId() > to.getVersionId()) {
/*     */       return;
/*     */     }
/* 120 */     this.enabled = true;
/*     */     try {
/* 122 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1) && mojangName != null) {
/*     */         
/*     */         try {
/* 125 */           this.clazz = Class.forName(mojangName);
/*     */           return;
/* 127 */         } catch (ClassNotFoundException classNotFoundException) {}
/*     */       }
/*     */ 
/*     */       
/* 131 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1) && mojangMap != null) {
/* 132 */         this.clazz = Class.forName(mojangMap + "." + clazzName);
/* 133 */       } else if (packageId == PackageWrapper.NONE) {
/* 134 */         this.clazz = Class.forName(clazzName);
/* 135 */       } else if (MinecraftVersion.isForgePresent() && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4 && 
/* 136 */         Forge1710Mappings.getClassMappings().get(name()) != null) {
/* 137 */         this.clazz = Class.forName(clazzName = Forge1710Mappings.getClassMappings().get(name()));
/* 138 */       } else if (packageId == PackageWrapper.CRAFTBUKKIT) {
/*     */         
/* 140 */         this.clazz = Class.forName(Bukkit.getServer().getClass().getPackage().getName() + "." + clazzName);
/*     */       } else {
/*     */         
/* 143 */         String version = MinecraftVersion.getVersion().getPackageName();
/* 144 */         this.clazz = Class.forName(packageId.getUri() + "." + version + "." + clazzName);
/*     */       } 
/* 146 */     } catch (Throwable ex) {
/* 147 */       MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error while trying to resolve the class '" + clazzName + "'!", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<?> getClazz() {
/* 155 */     return this.clazz;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/* 162 */     return this.enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMojangName() {
/* 169 */     return this.mojangName;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\nmsmappings\ClassWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */