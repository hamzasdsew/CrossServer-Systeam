/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.Collections;
/*     */ import java.util.Deque;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.logging.Level;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.DataFixerUtil;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.GsonWrapper;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.ReflectionUtil;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.CodecHelper;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ObjectCreator;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
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
/*     */ public class NBTReflectionUtil
/*     */ {
/*  45 */   private static Field field_unhandledTags = null;
/*  46 */   private static Field field_handle = null;
/*  47 */   private static Object type_custom_data = null;
/*  48 */   private static Object registry_access = null;
/*  49 */   public static Codec<Object> itemstack_codec = null;
/*  50 */   public static DynamicOps<Object> nbtOps = null;
/*  51 */   public static DynamicOps<Object> nbtRegistryOps = null;
/*  52 */   public static Object problemReporter = null;
/*     */   
/*     */   static {
/*     */     try {
/*  56 */       field_unhandledTags = ClassWrapper.CRAFT_METAITEM.getClazz().getDeclaredField("unhandledTags");
/*  57 */       field_unhandledTags.setAccessible(true);
/*  58 */     } catch (NoSuchFieldException noSuchFieldException) {}
/*     */ 
/*     */     
/*     */     try {
/*  62 */       field_handle = ClassWrapper.CRAFT_ITEMSTACK.getClazz().getDeclaredField("handle");
/*  63 */       field_handle.setAccessible(true);
/*  64 */     } catch (NoSuchFieldException noSuchFieldException) {}
/*     */ 
/*     */     
/*  67 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/*     */       try {
/*  69 */         Field typeField = ReflectionUtil.getMappedField(ClassWrapper.NMS_DATACOMPONENTS.getClazz(), "net.minecraft.core.component.DataComponents#CUSTOM_DATA");
/*  70 */         type_custom_data = typeField.get(null);
/*  71 */       } catch (Exception e) {
/*  72 */         MinecraftVersion.getLogger().log(Level.WARNING, "Unable to find DataComponents#CUSTOM_DATA, NBTApi will not be able to read/write custom data on 1.20+", e);
/*     */       } 
/*     */ 
/*     */       
/*     */       try {
/*  77 */         Object nmsServer = ReflectionMethod.NMSSERVER_GETSERVER.run(Bukkit.getServer(), new Object[0]);
/*  78 */         registry_access = ReflectionMethod.NMSSERVER_GETREGISTRYACCESS.run(nmsServer, new Object[0]);
/*  79 */         itemstack_codec = (Codec<Object>)ReflectionUtil.getMappedField(ClassWrapper.NMS_ITEMSTACK.getClazz(), "net.minecraft.world.item.ItemStack#CODEC").get(null);
/*  80 */         nbtOps = (DynamicOps<Object>)ReflectionUtil.getMappedField(ClassWrapper.NMS_NBTOPS.getClazz(), "net.minecraft.nbt.NbtOps#INSTANCE").get(null);
/*  81 */         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
/*  82 */           nbtRegistryOps = (DynamicOps<Object>)ReflectionMethod.GET_SERIALIZATION_CONTEXT.run(registry_access, new Object[] { nbtOps });
/*  83 */           problemReporter = ReflectionUtil.getMappedField(ClassWrapper.NMS_PROBLEM_REPORTER.getClazz(), "net.minecraft.util.ProblemReporter#DISCARDING").get(null);
/*     */         } 
/*  85 */       } catch (Exception e) {
/*  86 */         e.printStackTrace();
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getNMSEntity(Entity entity) {
/*     */     try {
/* 106 */       return ReflectionMethod.CRAFT_ENTITY_GET_HANDLE.run(ClassWrapper.CRAFT_ENTITY.getClazz().cast(entity), new Object[0]);
/* 107 */     } catch (Exception e) {
/* 108 */       throw new NbtApiException("Exception while getting the NMS Entity from a Bukkit Entity!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object readNBT(InputStream stream) {
/*     */     try {
/* 120 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R3)) {
/* 121 */         return ReflectionMethod.NBTFILE_READV2.run(null, new Object[] { stream, ReflectionMethod.NBTACCOUNTER_CREATE_UNLIMITED
/* 122 */               .run(null, new Object[0]) });
/*     */       }
/* 124 */       return ReflectionMethod.NBTFILE_READ.run(null, new Object[] { stream });
/*     */     }
/* 126 */     catch (Exception e) {
/*     */       try {
/* 128 */         stream.close();
/* 129 */       } catch (IOException iOException) {}
/*     */       
/* 131 */       throw new NbtApiException("Exception while reading a NBT File!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object writeNBT(Object nbt, OutputStream stream) {
/*     */     try {
/* 144 */       return ReflectionMethod.NBTFILE_WRITE.run(null, new Object[] { nbt, stream });
/* 145 */     } catch (Exception e) {
/* 146 */       throw new NbtApiException("Exception while writing NBT!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getCraftItemHandle(ItemStack item) {
/*     */     try {
/* 159 */       return field_handle.get(item);
/* 160 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 161 */       throw new NbtApiException("Error getting handle from " + item.getClass(), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void writeApiNBT(NBTCompound comp, OutputStream stream) {
/*     */     try {
/* 173 */       Object workingtag = comp.getResolvedObject();
/* 174 */       if (workingtag == null) {
/* 175 */         workingtag = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
/*     */       }
/* 177 */       ReflectionMethod.NBTFILE_WRITE.run(null, new Object[] { workingtag, stream });
/* 178 */     } catch (Exception e) {
/* 179 */       throw new NbtApiException("Exception while writing NBT!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getItemRootNBTTagCompound(Object nmsitem) {
/*     */     try {
/* 192 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 193 */         Object customData = ReflectionMethod.NMSDATACOMPONENTHOLDER_GET.run(nmsitem, new Object[] { type_custom_data });
/* 194 */         if (customData == null) {
/* 195 */           return null;
/*     */         }
/* 197 */         return ReflectionMethod.NMSCUSTOMDATA_GETCOPY.run(customData, new Object[0]);
/*     */       } 
/* 199 */       Object answer = ReflectionMethod.NMSITEM_GETTAG.run(nmsitem, new Object[0]);
/* 200 */       return answer;
/*     */     }
/* 202 */     catch (Exception e) {
/* 203 */       throw new NbtApiException("Exception while getting an Itemstack's NBTCompound!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setItemStackCompound(Object nmsItem, Object compound) {
/* 214 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 215 */       if (compound == null) {
/* 216 */         ReflectionMethod.NMSITEM_SET.run(nmsItem, new Object[] { type_custom_data, null });
/*     */       } else {
/* 218 */         ReflectionMethod.NMSITEM_SET.run(nmsItem, new Object[] { type_custom_data, ObjectCreator.NMS_CUSTOMDATA
/* 219 */               .getInstance(new Object[] { compound }) });
/*     */       } 
/*     */     } else {
/* 222 */       ReflectionMethod.ITEMSTACK_SET_TAG.run(nmsItem, new Object[] { compound });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object convertNBTCompoundtoNMSItem(NBTCompound nbtcompound) {
/* 233 */     Object nmsComp = null;
/*     */     try {
/* 235 */       nmsComp = getToCompount(nbtcompound.getCompound(), nbtcompound);
/* 236 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 237 */         if (nbtcompound.hasTag("DataVersion", NBTType.NBTTagInt)) {
/* 238 */           int dataVersion = nbtcompound.getInteger("DataVersion").intValue();
/* 239 */           int currentVersion = DataFixerUtil.getCurrentVersion();
/* 240 */           if (dataVersion < currentVersion) {
/* 241 */             nmsComp = DataFixerUtil.fixUpRawItemData(nmsComp, dataVersion, currentVersion);
/*     */           }
/* 243 */         } else if (nbtcompound.hasTag("tag") || nbtcompound.hasTag("Count")) {
/* 244 */           nmsComp = DataFixerUtil.fixUpRawItemData(nmsComp, 3700, DataFixerUtil.getCurrentVersion());
/*     */         } 
/* 246 */         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5))
/* 247 */           return CodecHelper.convertNbtToItemStack(nmsComp); 
/* 248 */         if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
/* 249 */           Optional<Object> opt = (Optional<Object>)ReflectionMethod.NMSITEM_LOAD_MODERN.run(null, new Object[] { registry_access, nmsComp });
/*     */           
/* 251 */           return opt.orElse(null);
/*     */         } 
/* 253 */         return ReflectionMethod.NMSITEM_LOAD.run(null, new Object[] { registry_access, nmsComp });
/*     */       } 
/* 255 */       if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_11_R1.getVersionId()) {
/* 256 */         return ObjectCreator.NMS_COMPOUNDFROMITEM.getInstance(new Object[] { nmsComp });
/*     */       }
/* 258 */       return ReflectionMethod.NMSITEM_CREATESTACK.run(null, new Object[] { nmsComp });
/*     */     }
/* 260 */     catch (Exception e) {
/* 261 */       throw new NbtApiException("Exception while converting NBTCompound to NMS ItemStack! " + nmsComp, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NBTContainer convertNMSItemtoNBTCompound(Object nmsitem) {
/*     */     try {
/*     */       NBTContainer container;
/* 274 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
/* 275 */         container = new NBTContainer(CodecHelper.convertItemStackToNbt(nmsitem));
/* 276 */       } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 277 */         container = new NBTContainer(ReflectionMethod.NMSITEM_SAVE_MODERN.run(nmsitem, new Object[] { registry_access }));
/*     */       } else {
/* 279 */         Object answer = ReflectionMethod.NMSITEM_SAVE.run(nmsitem, new Object[] { ObjectCreator.NMS_NBTTAGCOMPOUND
/* 280 */               .getInstance(new Object[0]) });
/* 281 */         container = new NBTContainer(answer);
/*     */       } 
/* 283 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_12_R1)) {
/* 284 */         container.setInteger("DataVersion", Integer.valueOf(DataFixerUtil.getCurrentVersion()));
/*     */       }
/* 286 */       return container;
/* 287 */     } catch (Exception e) {
/* 288 */       throw new NbtApiException("Exception while converting NMS ItemStack to NBTCompound!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static Map<String, Object> getUnhandledNBTTags(ItemMeta meta) {
/*     */     try {
/* 302 */       return (Map<String, Object>)field_unhandledTags.get(meta);
/* 303 */     } catch (Exception e) {
/* 304 */       throw new NbtApiException("Exception while getting unhandled tags from ItemMeta!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getEntityNBTTagCompound(Object nmsEntity) {
/*     */     try {
/* 316 */       Object answer, nbt = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
/*     */       
/* 318 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
/* 319 */         Object output = ReflectionMethod.NMS_GET_TAG_VALUE_OUTPUT.run(null, new Object[] { problemReporter, registry_access });
/*     */         
/* 321 */         ReflectionMethod.NMS_ENTITY_GET_NBT_1216.run(nmsEntity, new Object[] { output });
/*     */         
/* 323 */         answer = ReflectionMethod.NMS_TAG_VALUE_OUTPUT_TO_TAG_COMPOUND.run(output, new Object[0]);
/*     */       } else {
/* 325 */         answer = ReflectionMethod.NMS_ENTITY_GET_NBT.run(nmsEntity, new Object[] { nbt });
/*     */       } 
/* 327 */       if (answer == null)
/* 328 */         answer = nbt; 
/* 329 */       return answer;
/* 330 */     } catch (Exception e) {
/* 331 */       throw new NbtApiException("Exception while getting NBTCompound from NMS Entity!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object setEntityNBTTag(Object nbtTag, Object nmsEntity) {
/*     */     try {
/* 344 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
/* 345 */         Object valueInputTag = ReflectionMethod.NMS_GET_TAG_VALUE_INPUT.run(null, new Object[] { problemReporter, registry_access, nbtTag });
/*     */         
/* 347 */         ReflectionMethod.NMS_ENTITY_SET_NBT_1216.run(nmsEntity, new Object[] { valueInputTag });
/*     */       } else {
/* 349 */         ReflectionMethod.NMS_ENTITY_SET_NBT.run(nmsEntity, new Object[] { nbtTag });
/*     */       } 
/*     */       
/* 352 */       return nmsEntity;
/* 353 */     } catch (Exception ex) {
/* 354 */       throw new NbtApiException("Exception while setting the NBTCompound of an Entity", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getTileEntityNBTTagCompound(BlockState tile) {
/*     */     try {
/* 366 */       Object cworld = ClassWrapper.CRAFT_WORLD.getClazz().cast(tile.getWorld());
/* 367 */       Object nmsworld = ReflectionMethod.CRAFT_WORLD_GET_HANDLE.run(cworld, new Object[0]);
/* 368 */       Object o = null;
/* 369 */       if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
/* 370 */         o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY_1_7_10.run(nmsworld, new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), 
/* 371 */               Integer.valueOf(tile.getZ()) });
/*     */       } else {
/* 373 */         Object pos = ObjectCreator.NMS_BLOCKPOSITION.getInstance(new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), Integer.valueOf(tile.getZ()) });
/* 374 */         o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY.run(nmsworld, new Object[] { pos });
/*     */       } 
/*     */       
/* 377 */       if (o == null) {
/* 378 */         throw new NbtApiException("The passed BlockState(" + tile.getType() + ") doesn't point to a BlockEntity. Only BlockEntities like Chest/Signs/Furnance/etc have NBT.");
/*     */       }
/*     */ 
/*     */       
/* 382 */       Object answer = null;
/* 383 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
/* 384 */         Object output = ReflectionMethod.NMS_GET_TAG_VALUE_OUTPUT.run(null, new Object[] { problemReporter, registry_access });
/*     */         
/* 386 */         ReflectionMethod.TILEENTITY_GET_NBT_1216.run(o, new Object[] { output });
/*     */         
/* 388 */         answer = ReflectionMethod.NMS_TAG_VALUE_OUTPUT_TO_TAG_COMPOUND.run(output, new Object[0]);
/* 389 */       } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 390 */         answer = ReflectionMethod.TILEENTITY_GET_NBT_1205.run(o, new Object[] { registry_access });
/* 391 */       } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_18_R1)) {
/* 392 */         answer = ReflectionMethod.TILEENTITY_GET_NBT_1181.run(o, new Object[0]);
/*     */       } else {
/* 394 */         answer = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
/* 395 */         ReflectionMethod.TILEENTITY_GET_NBT.run(o, new Object[] { answer });
/*     */       } 
/* 397 */       if (answer == null) {
/* 398 */         throw new NbtApiException("Unable to get NBTCompound from TileEntity! " + tile + " " + o);
/*     */       }
/* 400 */       return answer;
/* 401 */     } catch (Exception e) {
/* 402 */       throw new NbtApiException("Exception while getting NBTCompound from TileEntity!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setTileEntityNBTTagCompound(BlockState tile, Object comp) {
/*     */     try {
/* 414 */       Object cworld = ClassWrapper.CRAFT_WORLD.getClazz().cast(tile.getWorld());
/* 415 */       Object nmsworld = ReflectionMethod.CRAFT_WORLD_GET_HANDLE.run(cworld, new Object[0]);
/* 416 */       Object o = null;
/* 417 */       if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
/* 418 */         o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY_1_7_10.run(nmsworld, new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), 
/* 419 */               Integer.valueOf(tile.getZ()) });
/*     */       } else {
/* 421 */         Object pos = ObjectCreator.NMS_BLOCKPOSITION.getInstance(new Object[] { Integer.valueOf(tile.getX()), Integer.valueOf(tile.getY()), Integer.valueOf(tile.getZ()) });
/* 422 */         o = ReflectionMethod.NMS_WORLD_GET_TILEENTITY.run(nmsworld, new Object[] { pos });
/*     */       } 
/* 424 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R5)) {
/* 425 */         Object valueInput = ReflectionMethod.NMS_GET_TAG_VALUE_INPUT.run(null, new Object[] { problemReporter, registry_access, comp });
/*     */         
/* 427 */         ReflectionMethod.TILEENTITY_SET_NBT_1216.run(o, new Object[] { valueInput });
/* 428 */       } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 429 */         ReflectionMethod.TILEENTITY_SET_NBT_1205.run(o, new Object[] { comp, registry_access });
/* 430 */       } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1)) {
/* 431 */         ReflectionMethod.TILEENTITY_SET_NBT.run(o, new Object[] { comp });
/* 432 */       } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1)) {
/* 433 */         Object blockData = ReflectionMethod.TILEENTITY_GET_BLOCKDATA.run(o, new Object[0]);
/* 434 */         ReflectionMethod.TILEENTITY_SET_NBT_LEGACY1161.run(o, new Object[] { blockData, comp });
/*     */       } else {
/* 436 */         ReflectionMethod.TILEENTITY_SET_NBT_LEGACY1151.run(o, new Object[] { comp });
/*     */       } 
/* 438 */     } catch (Exception e) {
/* 439 */       throw new NbtApiException("Exception while setting NBTData for a TileEntity!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getSubNBTTagCompound(Object compound, String name) {
/*     */     try {
/* 452 */       if (((Boolean)ReflectionMethod.COMPOUND_HAS_KEY.run(compound, new Object[] { name })).booleanValue()) {
/* 453 */         Object comp = ReflectionMethod.COMPOUND_GET_COMPOUND.run(compound, new Object[] { name });
/* 454 */         if (comp instanceof Optional) {
/* 455 */           return ((Optional)comp).orElse(null);
/*     */         }
/* 457 */         return comp;
/*     */       } 
/* 459 */       throw new NbtApiException("Tried getting invalid compound '" + name + "' from '" + compound + "'!");
/*     */     }
/* 461 */     catch (Exception e) {
/* 462 */       throw new NbtApiException("Exception while getting NBT subcompounds!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void addNBTTagCompound(NBTCompound comp, String name) {
/* 473 */     if (name == null) {
/* 474 */       remove(comp, name);
/*     */       return;
/*     */     } 
/* 477 */     Object nbttag = comp.getCompound();
/* 478 */     if (nbttag == null) {
/* 479 */       nbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*     */     }
/* 481 */     if (!validCompound(comp)) {
/*     */       return;
/*     */     }
/* 484 */     Object workingtag = getToCompount(nbttag, comp);
/*     */     try {
/* 486 */       ReflectionMethod.COMPOUND_SET.run(workingtag, new Object[] { name, ClassWrapper.NMS_NBTTAGCOMPOUND
/* 487 */             .getClazz().newInstance() });
/* 488 */       comp.setCompound(nbttag);
/* 489 */     } catch (Exception e) {
/* 490 */       throw new NbtApiException("Exception while adding a Compound!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean validCompound(NBTCompound comp) {
/* 501 */     Object root = comp.getCompound();
/* 502 */     if (root instanceof Optional) {
/* 503 */       root = ((Optional)root).orElse(null);
/*     */     }
/* 505 */     if (root == null) {
/* 506 */       root = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*     */     }
/* 508 */     Object tmp = getToCompount(root, comp);
/* 509 */     comp.setResolvedObject(tmp);
/* 510 */     return (tmp != null);
/*     */   }
/*     */   
/*     */   public static Object getToCompount(Object nbttag, NBTCompound comp) {
/* 514 */     Deque<String> structure = new ArrayDeque<>();
/* 515 */     while (comp.getParent() != null) {
/* 516 */       structure.add(comp.getName());
/* 517 */       comp = comp.getParent();
/*     */     } 
/* 519 */     if (nbttag instanceof Optional) {
/* 520 */       nbttag = ((Optional)nbttag).orElse(null);
/*     */     }
/* 522 */     while (!structure.isEmpty()) {
/* 523 */       String target = structure.pollLast();
/* 524 */       nbttag = getSubNBTTagCompound(nbttag, target);
/* 525 */       if (nbttag instanceof Optional) {
/* 526 */         nbttag = ((Optional)nbttag).orElse(null);
/*     */       }
/* 528 */       if (nbttag == null) {
/* 529 */         throw new NbtApiException("Unable to find tag '" + target + "' in " + nbttag);
/*     */       }
/*     */     } 
/* 532 */     return nbttag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void mergeOtherNBTCompound(NBTCompound comp, NBTCompound nbtcompoundSrc) {
/* 542 */     Object workingtagSrc = nbtcompoundSrc.getResolvedObject();
/* 543 */     if (workingtagSrc == null) {
/*     */       return;
/*     */     }
/* 546 */     Object rootnbttag = comp.getCompound();
/* 547 */     if (rootnbttag == null) {
/* 548 */       rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*     */     }
/* 550 */     if (!validCompound(comp))
/* 551 */       throw new NbtApiException("The Compound wasn't able to be linked back to the root!"); 
/* 552 */     Object workingtag = getToCompount(rootnbttag, comp);
/*     */     try {
/* 554 */       ReflectionMethod.COMPOUND_MERGE.run(workingtag, new Object[] { workingtagSrc });
/* 555 */       comp.setCompound(rootnbttag);
/* 556 */     } catch (Exception e) {
/* 557 */       throw new NbtApiException("Exception while merging two NBTCompounds!", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void set(NBTCompound comp, String key, Object val) {
/* 569 */     if (val == null) {
/* 570 */       remove(comp, key);
/*     */       return;
/*     */     } 
/* 573 */     Object rootnbttag = comp.getCompound();
/* 574 */     if (rootnbttag == null) {
/* 575 */       rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*     */     }
/* 577 */     if (!validCompound(comp)) {
/* 578 */       throw new NbtApiException("The Compound wasn't able to be linked back to the root!");
/*     */     }
/* 580 */     Object workingtag = getToCompount(rootnbttag, comp);
/*     */     try {
/* 582 */       ReflectionMethod.COMPOUND_SET.run(workingtag, new Object[] { key, val });
/* 583 */       comp.setCompound(rootnbttag);
/* 584 */     } catch (Exception e) {
/* 585 */       throw new NbtApiException("Exception while setting key '" + key + "' to '" + val + "'!", e);
/*     */     } 
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
/*     */   public static <T> NBTList<T> getList(NBTCompound comp, String key, NBTType type, Class<T> clazz) {
/* 600 */     Object workingtag = comp.getResolvedObject();
/* 601 */     if (workingtag == null) {
/* 602 */       workingtag = dummyNBT.getCompound();
/*     */     }
/*     */     try {
/* 605 */       Object nbt = null;
/* 606 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
/* 607 */         nbt = ReflectionMethod.COMPOUND_GET_LIST.run(workingtag, new Object[] { key });
/* 608 */         if (nbt instanceof Optional) {
/* 609 */           nbt = ((Optional)nbt).orElse(null);
/*     */         }
/* 611 */         if (nbt == null) {
/* 612 */           nbt = ClassWrapper.NMS_NBTTAGLIST.getClazz().newInstance();
/*     */         }
/*     */       } else {
/* 615 */         nbt = ReflectionMethod.COMPOUND_GET_LIST_LEGACY.run(workingtag, new Object[] { key, Integer.valueOf(type.getId()) });
/*     */       } 
/* 617 */       if (clazz == String.class)
/* 618 */         return new NBTStringList(comp, key, type, nbt); 
/* 619 */       if (clazz == NBTListCompound.class)
/* 620 */         return new NBTCompoundList(comp, key, type, nbt); 
/* 621 */       if (clazz == Integer.class)
/* 622 */         return new NBTIntegerList(comp, key, type, nbt); 
/* 623 */       if (clazz == Float.class)
/* 624 */         return new NBTFloatList(comp, key, type, nbt); 
/* 625 */       if (clazz == Double.class)
/* 626 */         return new NBTDoubleList(comp, key, type, nbt); 
/* 627 */       if (clazz == Long.class)
/* 628 */         return new NBTLongList(comp, key, type, nbt); 
/* 629 */       if (clazz == int[].class)
/* 630 */         return new NBTIntArrayList(comp, key, type, nbt); 
/* 631 */       if (clazz == UUID.class) {
/* 632 */         return new NBTUUIDList(comp, key, type, nbt);
/*     */       }
/* 634 */       return null;
/*     */     }
/* 636 */     catch (Exception ex) {
/* 637 */       throw new NbtApiException("Exception while getting a list with the type '" + type + "'!", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static NBTType getListType(NBTCompound comp, String key) {
/* 642 */     Object workingtag = comp.getResolvedObject();
/* 643 */     if (workingtag == null)
/* 644 */       workingtag = dummyNBT.getCompound(); 
/*     */     try {
/*     */       Field f;
/* 647 */       Object nbt = ReflectionMethod.COMPOUND_GET.run(workingtag, new Object[] { key });
/* 648 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
/* 649 */         if (nbt instanceof Optional) {
/* 650 */           nbt = ((Optional)nbt).orElse(null);
/*     */         }
/* 652 */         if (nbt == null) {
/* 653 */           return NBTType.NBTTagEnd;
/*     */         }
/* 655 */         if ((new NBTStringList(comp, key, NBTType.NBTTagString, nbt)).isEmpty()) {
/* 656 */           return NBTType.NBTTagEnd;
/*     */         }
/* 658 */         Object compound = ReflectionMethod.LIST_GET.run(nbt, new Object[] { Integer.valueOf(0) });
/* 659 */         if (compound instanceof Optional) {
/* 660 */           compound = ((Optional)compound).orElse(null);
/*     */         }
/* 662 */         if (compound == null) {
/* 663 */           return NBTType.NBTTagEnd;
/*     */         }
/* 665 */         return NBTType.fromName((String)ReflectionMethod.TAGTYPE_GET_NAME
/* 666 */             .run(ReflectionMethod.TAGTYPE_OWN_TYPE.run(compound, new Object[0]), new Object[0]));
/*     */       } 
/* 668 */       String fieldname = "type";
/* 669 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_17_R1)) {
/* 670 */         fieldname = "w";
/*     */       }
/*     */       
/*     */       try {
/* 674 */         f = nbt.getClass().getDeclaredField(fieldname);
/* 675 */       } catch (NoSuchFieldException ignore) {
/*     */         
/* 677 */         f = nbt.getClass().getDeclaredField("type");
/*     */       } 
/* 679 */       f.setAccessible(true);
/* 680 */       return NBTType.valueOf(f.getByte(nbt));
/* 681 */     } catch (Exception ex) {
/* 682 */       throw new NbtApiException("Exception while getting the list type!", ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Object getEntry(NBTCompound comp, String key) {
/* 687 */     Object workingtag = comp.getResolvedObject();
/*     */     try {
/* 689 */       return ReflectionMethod.COMPOUND_GET.run(workingtag, new Object[] { key });
/* 690 */     } catch (Exception ex) {
/* 691 */       throw new NbtApiException("Exception while getting an Entry!", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setObject(NBTCompound comp, String key, Object value) {
/* 703 */     if (!MinecraftVersion.hasGsonSupport())
/*     */       return; 
/*     */     try {
/* 706 */       String json = GsonWrapper.getString(value);
/* 707 */       setData(comp, ReflectionMethod.COMPOUND_SET_STRING, key, json);
/* 708 */     } catch (Exception e) {
/* 709 */       throw new NbtApiException("Exception while setting the Object '" + value + "'!", e);
/*     */     } 
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
/*     */   public static <T> T getObject(NBTCompound comp, String key, Class<T> type) {
/* 722 */     if (!MinecraftVersion.hasGsonSupport())
/* 723 */       return null; 
/* 724 */     String json = (String)getData(comp, ReflectionMethod.COMPOUND_GET_STRING, key);
/* 725 */     if (json == null) {
/* 726 */       return null;
/*     */     }
/* 728 */     return (T)GsonWrapper.deserializeJson(json, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void remove(NBTCompound comp, String key) {
/* 738 */     Object rootnbttag = comp.getCompound();
/* 739 */     if (rootnbttag == null) {
/*     */       return;
/*     */     }
/* 742 */     if (!validCompound(comp))
/*     */       return; 
/* 744 */     Object workingtag = getToCompount(rootnbttag, comp);
/* 745 */     ReflectionMethod.COMPOUND_REMOVE_KEY.run(workingtag, new Object[] { key });
/* 746 */     comp.setCompound(rootnbttag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getKeys(NBTCompound comp) {
/* 757 */     Object workingtag = comp.getResolvedObject();
/* 758 */     if (workingtag == null) {
/* 759 */       return Collections.emptySet();
/*     */     }
/* 761 */     return (Set<String>)ReflectionMethod.COMPOUND_GET_KEYS.run(workingtag, new Object[0]);
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
/*     */   public static void setData(NBTCompound comp, ReflectionMethod type, String key, Object data) {
/* 773 */     if (data == null) {
/* 774 */       remove(comp, key);
/*     */       return;
/*     */     } 
/* 777 */     Object rootnbttag = comp.getCompound();
/* 778 */     if (rootnbttag == null) {
/* 779 */       rootnbttag = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*     */     }
/* 781 */     if (!validCompound(comp))
/* 782 */       throw new NbtApiException("The Compound wasn't able to be linked back to the root!"); 
/* 783 */     Object workingtag = getToCompount(rootnbttag, comp);
/* 784 */     type.run(workingtag, new Object[] { key, data });
/* 785 */     comp.setCompound(rootnbttag);
/*     */   }
/*     */   
/* 788 */   private static final NBTContainer dummyNBT = new NBTContainer();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getData(NBTCompound comp, ReflectionMethod type, String key) {
/* 799 */     Object workingtag = comp.getResolvedObject();
/*     */     
/* 801 */     if (workingtag == null) {
/* 802 */       workingtag = dummyNBT.getCompound();
/*     */     }
/* 804 */     if (workingtag instanceof Optional) {
/* 805 */       workingtag = ((Optional)workingtag).orElseGet(() -> dummyNBT.getCompound());
/*     */     }
/* 807 */     Object obj = type.run(workingtag, new Object[] { key });
/* 808 */     if (obj instanceof Optional) {
/* 809 */       return ((Optional)obj).orElseGet(() -> getDefaultValue(type));
/*     */     }
/* 811 */     return obj;
/*     */   }
/*     */   
/*     */   private static Object getDefaultValue(ReflectionMethod type) {
/* 815 */     if (type == ReflectionMethod.COMPOUND_GET_STRING)
/* 816 */       return ""; 
/* 817 */     if (type == ReflectionMethod.COMPOUND_GET_BYTE)
/* 818 */       return Byte.valueOf((byte)0); 
/* 819 */     if (type == ReflectionMethod.COMPOUND_GET_SHORT)
/* 820 */       return Short.valueOf((short)0); 
/* 821 */     if (type == ReflectionMethod.COMPOUND_GET_BOOLEAN)
/* 822 */       return Boolean.valueOf(false); 
/* 823 */     if (type == ReflectionMethod.COMPOUND_GET_INT)
/* 824 */       return Integer.valueOf(0); 
/* 825 */     if (type == ReflectionMethod.COMPOUND_GET_LONG)
/* 826 */       return Long.valueOf(0L); 
/* 827 */     if (type == ReflectionMethod.COMPOUND_GET_FLOAT)
/* 828 */       return Float.valueOf(0.0F); 
/* 829 */     if (type == ReflectionMethod.COMPOUND_GET_DOUBLE)
/* 830 */       return Double.valueOf(0.0D); 
/* 831 */     if (type == ReflectionMethod.COMPOUND_GET_BYTEARRAY)
/* 832 */       return new byte[0]; 
/* 833 */     if (type == ReflectionMethod.COMPOUND_GET_INTARRAY)
/* 834 */       return new int[0]; 
/* 835 */     if (type == ReflectionMethod.COMPOUND_GET_LONGARRAY) {
/* 836 */       return new long[0];
/*     */     }
/* 838 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTReflectionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */