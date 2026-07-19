/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.logging.Level;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.NBTFileHandle;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteItemNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableItemNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.ProxyBuilder;
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
/*     */ public class NBT
/*     */ {
/*     */   public static boolean preloadApi() {
/*     */     try {
/*  56 */       if (MinecraftVersion.getVersion() == MinecraftVersion.UNKNOWN) {
/*  57 */         NbtApiException.confirmedBroken = Boolean.valueOf(true);
/*  58 */         return false;
/*     */       } 
/*  60 */       for (ClassWrapper c : ClassWrapper.values()) {
/*  61 */         if (c.isEnabled() && c.getClazz() == null) {
/*  62 */           NbtApiException.confirmedBroken = Boolean.valueOf(true);
/*  63 */           return false;
/*     */         } 
/*     */       } 
/*  66 */       for (ReflectionMethod method : ReflectionMethod.values()) {
/*  67 */         if (method.isCompatible() && !method.isLoaded()) {
/*  68 */           NbtApiException.confirmedBroken = Boolean.valueOf(true);
/*  69 */           return false;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  74 */       return true;
/*  75 */     } catch (Exception ex) {
/*  76 */       NbtApiException.confirmedBroken = Boolean.valueOf(true);
/*  77 */       MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error during the selfcheck!", ex);
/*  78 */       return false;
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
/*     */   public static ReadableNBT readNbt(ItemStack item) {
/*  91 */     return (ReadableNBT)new NBTItem(item.clone(), false, true, false);
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
/*     */   public static <T> T get(ItemStack item, Function<ReadableItemNBT, T> getter) {
/* 105 */     NBTItem nbt = new NBTItem(item, false, true, false);
/* 106 */     T ret = getter.apply(nbt);
/* 107 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 108 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 110 */     nbt.setClosed();
/* 111 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void get(ItemStack item, Consumer<ReadableItemNBT> getter) {
/* 121 */     NBTItem nbt = new NBTItem(item, false, true, false);
/* 122 */     getter.accept(nbt);
/* 123 */     nbt.setClosed();
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
/*     */   public static <T> T get(Entity entity, Function<ReadableNBT, T> getter) {
/* 135 */     NBTEntity nbt = new NBTEntity(entity, true);
/* 136 */     T ret = getter.apply(nbt);
/* 137 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 138 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 140 */     nbt.setClosed();
/* 141 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void get(Entity entity, Consumer<ReadableNBT> getter) {
/* 151 */     NBTEntity nbt = new NBTEntity(entity, true);
/* 152 */     getter.accept(nbt);
/* 153 */     nbt.setClosed();
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
/*     */   public static <T> T get(BlockState blockState, Function<ReadableNBT, T> getter) {
/* 167 */     NBTTileEntity nbt = new NBTTileEntity(blockState, true);
/* 168 */     T ret = getter.apply(nbt);
/* 169 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 170 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 172 */     nbt.setClosed();
/* 173 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void get(BlockState blockState, Consumer<ReadableNBT> getter) {
/* 183 */     NBTTileEntity nbt = new NBTTileEntity(blockState, true);
/* 184 */     getter.accept(nbt);
/* 185 */     nbt.setClosed();
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
/*     */   public static <T> T getPersistentData(Entity entity, Function<ReadableNBT, T> getter) {
/* 199 */     T ret = getter.apply((new NBTEntity(entity)).getPersistentDataContainer());
/* 200 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 201 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 203 */     return ret;
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
/*     */   public static <T> T getPersistentData(BlockState blockState, Function<ReadableNBT, T> getter) {
/* 217 */     T ret = getter.apply((new NBTTileEntity(blockState)).getPersistentDataContainer());
/* 218 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 219 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 221 */     return ret;
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
/*     */   public static <T> T modify(ItemStack item, Function<ReadWriteItemNBT, T> function) {
/* 233 */     NBTItem nbti = new NBTItem(item, false, false, true);
/* 234 */     T val = function.apply(nbti);
/* 235 */     nbti.finalizeChanges();
/* 236 */     if (val instanceof ReadableNBT || val instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 237 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 239 */     nbti.setClosed();
/* 240 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void modify(ItemStack item, Consumer<ReadWriteItemNBT> consumer) {
/* 251 */     NBTItem nbti = new NBTItem(item, false, false, true);
/* 252 */     consumer.accept(nbti);
/* 253 */     nbti.finalizeChanges();
/* 254 */     nbti.setClosed();
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
/*     */   public static <T> T modify(Entity entity, Function<ReadWriteNBT, T> function) {
/* 266 */     NBTEntity nbtEnt = new NBTEntity(entity);
/* 267 */     NBTContainer cont = new NBTContainer(nbtEnt.getCompound());
/* 268 */     T ret = function.apply(cont);
/* 269 */     nbtEnt.setCompound(cont.getCompound());
/* 270 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 271 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 273 */     nbtEnt.setClosed();
/* 274 */     return ret;
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
/*     */   public static void modifyComponents(ItemStack item, Consumer<ReadWriteNBT> consumer) {
/* 286 */     if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 287 */       throw new NbtApiException("This method only works for 1.20.5+!");
/*     */     }
/* 289 */     ReadWriteNBT nbti = itemStackToNBT(item);
/* 290 */     consumer.accept(nbti.getOrCreateCompound("components"));
/* 291 */     ItemStack tmp = itemStackFromNBT((ReadableNBT)nbti);
/* 292 */     item.setItemMeta(tmp.getItemMeta());
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
/*     */   public static <T> T modifyComponents(ItemStack item, Function<ReadWriteNBT, T> function) {
/* 305 */     if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 306 */       throw new NbtApiException("This method only works for 1.20.5+!");
/*     */     }
/* 308 */     ReadWriteNBT nbti = itemStackToNBT(item);
/* 309 */     T ret = function.apply(nbti.getOrCreateCompound("components"));
/* 310 */     ItemStack tmp = itemStackFromNBT((ReadableNBT)nbti);
/* 311 */     item.setItemMeta(tmp.getItemMeta());
/* 312 */     return ret;
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
/*     */   public static void getComponents(ItemStack item, Consumer<ReadableNBT> consumer) {
/* 324 */     if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 325 */       throw new NbtApiException("This method only works for 1.20.5+!");
/*     */     }
/* 327 */     ReadWriteNBT nbti = itemStackToNBT(item);
/* 328 */     consumer.accept(nbti.getOrCreateCompound("components"));
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
/*     */   public static <T> T getComponents(ItemStack item, Function<ReadableNBT, T> function) {
/* 341 */     if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 342 */       throw new NbtApiException("This method only works for 1.20.5+!");
/*     */     }
/* 344 */     ReadWriteNBT nbti = itemStackToNBT(item);
/* 345 */     return function.apply(nbti.getOrCreateCompound("components"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void modify(Entity entity, Consumer<ReadWriteNBT> consumer) {
/* 356 */     NBTEntity nbtEnt = new NBTEntity(entity);
/* 357 */     NBTContainer cont = new NBTContainer(nbtEnt.getCompound());
/* 358 */     consumer.accept(cont);
/* 359 */     nbtEnt.setCompound(cont.getCompound());
/* 360 */     nbtEnt.setClosed();
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
/*     */   public static <T> T modifyPersistentData(Entity entity, Function<ReadWriteNBT, T> function) {
/* 373 */     T ret = function.apply((new NBTEntity(entity)).getPersistentDataContainer());
/* 374 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 375 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 377 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void modifyPersistentData(Entity entity, Consumer<ReadWriteNBT> consumer) {
/* 388 */     consumer.accept((new NBTEntity(entity)).getPersistentDataContainer());
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
/*     */   public static <T> T modify(BlockState blockState, Function<ReadWriteNBT, T> function) {
/* 400 */     NBTTileEntity blockEnt = new NBTTileEntity(blockState);
/* 401 */     NBTContainer cont = new NBTContainer(blockEnt.getCompound());
/* 402 */     T ret = function.apply(cont);
/* 403 */     blockEnt.setCompound(cont.getCompound());
/* 404 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 405 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 407 */     blockEnt.setClosed();
/* 408 */     return ret;
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
/*     */   public static void modify(BlockState blockState, Consumer<ReadWriteNBT> consumer) {
/* 421 */     NBTTileEntity blockEnt = new NBTTileEntity(blockState);
/* 422 */     NBTContainer cont = new NBTContainer(blockEnt.getCompound());
/* 423 */     consumer.accept(cont);
/* 424 */     blockEnt.setCompound(cont.getCompound());
/* 425 */     blockEnt.setClosed();
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
/*     */   public static <T> T modifyPersistentData(BlockState blockState, Function<ReadWriteNBT, T> function) {
/* 438 */     T ret = function.apply((new NBTTileEntity(blockState)).getPersistentDataContainer());
/* 439 */     if (ret instanceof ReadableNBT || ret instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 440 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 442 */     return ret;
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
/*     */   public static void modifyPersistentData(BlockState blockState, Consumer<ReadWriteNBT> consumer) {
/* 454 */     consumer.accept((new NBTTileEntity(blockState)).getPersistentDataContainer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT gameProfileToNBT(GameProfile profile) {
/* 464 */     return NBTGameProfile.toNBT(profile);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static GameProfile gameProfileFromNBT(ReadableNBT compound) {
/* 474 */     return NBTGameProfile.fromNBT((NBTCompound)compound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT itemStackToNBT(ItemStack itemStack) {
/* 484 */     return NBTItem.convertItemtoNBT(itemStack);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static ItemStack itemStackFromNBT(ReadableNBT compound) {
/* 495 */     return NBTItem.convertNBTtoItem((NBTCompound)compound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT itemStackArrayToNBT(ItemStack[] itemStacks) {
/* 505 */     return NBTItem.convertItemArraytoNBT(itemStacks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static ItemStack[] itemStackArrayFromNBT(ReadableNBT compound) {
/* 516 */     return NBTItem.convertNBTtoItemArray((NBTCompound)compound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT createNBTObject() {
/* 525 */     return new NBTContainer();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT parseNBT(String nbtString) {
/* 535 */     return new NBTContainer(nbtString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT readNBT(InputStream stream) {
/* 545 */     return new NBTContainer(stream);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ReadWriteNBT wrapNMSTag(Object nmsNbtTag) {
/* 556 */     return new NBTContainer(nmsNbtTag);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NBTFileHandle getFileHandle(File file) throws IOException {
/* 567 */     return new NBTFile(file);
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
/*     */   public static ReadWriteNBT readFile(File file) throws IOException {
/* 580 */     return NBTFile.readFrom(file);
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
/*     */   public static void writeFile(File file, ReadWriteNBT nbt) throws IOException {
/* 593 */     NBTFile.saveTo(file, (NBTCompound)nbt);
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
/*     */   public static <T extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> T readNbt(ItemStack item, Class<T> wrapper) {
/* 605 */     return (T)(new ProxyBuilder(new NBTItem(item, false, true, false), wrapper)).readOnly().build();
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
/*     */   public static <T extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> T readNbt(Entity entity, Class<T> wrapper) {
/* 617 */     return (T)(new ProxyBuilder(new NBTEntity(entity, true), wrapper)).readOnly().build();
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
/*     */   public static <T extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> T readNbt(BlockState blockState, Class<T> wrapper) {
/* 629 */     return (T)(new ProxyBuilder(new NBTTileEntity(blockState, true), wrapper)).readOnly().build();
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
/*     */   public static <T, X extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> T modify(ItemStack item, Class<X> wrapper, Function<X, T> function) {
/* 642 */     NBTItem nbti = new NBTItem(item, false, false, true);
/* 643 */     T val = function.apply((X)(new ProxyBuilder(nbti, wrapper)).build());
/* 644 */     nbti.finalizeChanges();
/* 645 */     if (val instanceof ReadableNBT || val instanceof org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList) {
/* 646 */       throw new NbtApiException("Tried returning part of the NBT to outside of the NBT scope!");
/*     */     }
/* 648 */     nbti.setClosed();
/* 649 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <X extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> void modify(ItemStack item, Class<X> wrapper, Consumer<X> consumer) {
/* 660 */     NBTItem nbti = new NBTItem(item, false, false, true);
/* 661 */     consumer.accept((X)(new ProxyBuilder(nbti, wrapper)).build());
/* 662 */     nbti.finalizeChanges();
/* 663 */     nbti.setClosed();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <X extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> void modify(Entity entity, Class<X> wrapper, Consumer<X> consumer) {
/* 674 */     NBTEntity nbtEnt = new NBTEntity(entity);
/* 675 */     NBTContainer cont = new NBTContainer(nbtEnt.getCompound());
/* 676 */     consumer.accept((X)(new ProxyBuilder(cont, wrapper)).build());
/* 677 */     nbtEnt.setCompound(cont.getCompound());
/* 678 */     cont.setClosed();
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
/*     */   public static <T, X extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> T modify(Entity entity, Class<X> wrapper, Function<X, T> function) {
/* 690 */     NBTEntity nbtEnt = new NBTEntity(entity);
/* 691 */     NBTContainer cont = new NBTContainer(nbtEnt.getCompound());
/* 692 */     T val = function.apply((X)(new ProxyBuilder(cont, wrapper)).build());
/* 693 */     nbtEnt.setCompound(cont.getCompound());
/* 694 */     cont.setClosed();
/* 695 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <X extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> void modify(BlockState blockState, Class<X> wrapper, Consumer<X> consumer) {
/* 706 */     NBTTileEntity blockEnt = new NBTTileEntity(blockState);
/* 707 */     NBTContainer cont = new NBTContainer(blockEnt.getCompound());
/* 708 */     consumer.accept((X)(new ProxyBuilder(cont, wrapper)).build());
/* 709 */     blockEnt.setCompound(cont);
/* 710 */     cont.setClosed();
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
/*     */   public static <T, X extends org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper.NBTProxy> T modify(BlockState blockState, Class<X> wrapper, Function<X, T> function) {
/* 722 */     NBTTileEntity blockEnt = new NBTTileEntity(blockState);
/* 723 */     NBTContainer cont = new NBTContainer(blockEnt.getCompound());
/* 724 */     T val = function.apply((X)(new ProxyBuilder(cont, wrapper)).build());
/* 725 */     blockEnt.setCompound(cont);
/* 726 */     cont.setClosed();
/* 727 */     return val;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */