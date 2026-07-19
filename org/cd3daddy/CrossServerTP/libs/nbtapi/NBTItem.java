/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.function.BiConsumer;
/*     */ import javax.annotation.Nullable;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteItemNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
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
/*     */ public class NBTItem
/*     */   extends NBTCompound
/*     */   implements ReadWriteItemNBT
/*     */ {
/*     */   private ItemStack bukkitItem;
/*     */   private final boolean directApply;
/*     */   private final boolean finalizer;
/*  31 */   private ItemStack originalSrcStack = null;
/*  32 */   private Object cachedCompound = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean closed = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NBTItem(ItemStack item) {
/*  44 */     this(item, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NBTItem(ItemStack item, boolean directApply, boolean readOnly, boolean finalizer) {
/*  55 */     super(null, null, readOnly);
/*  56 */     if (item == null || item.getType() == Material.AIR || item.getAmount() <= 0) {
/*  57 */       throw new NullPointerException("ItemStack can't be null/air/amount of 0! This is not a NBTAPI bug!");
/*     */     }
/*  59 */     this.finalizer = finalizer;
/*  60 */     if (finalizer) {
/*  61 */       this.bukkitItem = item;
/*  62 */       this.originalSrcStack = item;
/*  63 */       this.directApply = false;
/*  64 */     } else if (readOnly) {
/*  65 */       this.bukkitItem = item;
/*  66 */       this.directApply = false;
/*     */     } else {
/*  68 */       this.directApply = directApply;
/*  69 */       this.bukkitItem = item.clone();
/*  70 */       if (directApply) {
/*  71 */         this.originalSrcStack = item;
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
/*     */   @Deprecated
/*     */   public NBTItem(ItemStack item, boolean directApply) {
/*  86 */     super(null, null);
/*  87 */     if (item == null || item.getType() == Material.AIR || item.getAmount() <= 0) {
/*  88 */       throw new NullPointerException("ItemStack can't be null/air/amount of 0! This is not a NBTAPI bug!");
/*     */     }
/*  90 */     this.finalizer = false;
/*  91 */     this.directApply = directApply;
/*  92 */     this.bukkitItem = item.clone();
/*  93 */     if (directApply) {
/*  94 */       this.originalSrcStack = item;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getCompound() {
/* 100 */     if (this.closed) {
/* 101 */       throw new NbtApiException("Tried using closed NBT data!");
/*     */     }
/* 103 */     if (isReadOnly() && (this.cachedCompound != null || ClassWrapper.CRAFT_ITEMSTACK
/* 104 */       .getClazz().isAssignableFrom(this.bukkitItem.getClass()))) {
/* 105 */       if (this.cachedCompound == null) {
/* 106 */         this
/* 107 */           .cachedCompound = NBTReflectionUtil.getItemRootNBTTagCompound(NBTReflectionUtil.getCraftItemHandle(this.bukkitItem));
/*     */       }
/* 109 */       return this.cachedCompound;
/*     */     } 
/* 111 */     if (this.finalizer) {
/* 112 */       if (this.cachedCompound == null) {
/* 113 */         updateCachedCompound();
/*     */       }
/* 115 */       return this.cachedCompound;
/*     */     } 
/* 117 */     return NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { this.bukkitItem }));
/*     */   }
/*     */   
/*     */   private void updateCachedCompound() {
/* 121 */     if (this.finalizer) {
/* 122 */       this
/* 123 */         .cachedCompound = NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { this.bukkitItem }));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void finalizeChanges() {
/* 128 */     if (!this.finalizer || this.cachedCompound == null) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     if (NBTReflectionUtil.getKeys(this).isEmpty()) {
/* 133 */       this.cachedCompound = null;
/*     */     }
/* 135 */     if (ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.originalSrcStack.getClass())) {
/* 136 */       Object nmsStack = NBTReflectionUtil.getCraftItemHandle(this.originalSrcStack);
/* 137 */       NBTReflectionUtil.setItemStackCompound(nmsStack, this.cachedCompound);
/* 138 */       this.bukkitItem = this.originalSrcStack;
/*     */     } else {
/* 140 */       Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { this.bukkitItem });
/* 141 */       NBTReflectionUtil.setItemStackCompound(stack, this.cachedCompound);
/* 142 */       this.bukkitItem = (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, new Object[] { stack });
/* 143 */       this.originalSrcStack.setItemMeta(this.bukkitItem.getItemMeta());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setClosed() {
/* 149 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isClosed() {
/* 154 */     return this.closed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setCompound(Object compound) {
/* 160 */     if (isReadOnly()) {
/* 161 */       throw new NbtApiException("Tried setting data in read only mode!");
/*     */     }
/* 163 */     if (this.closed) {
/* 164 */       throw new NbtApiException("Tried using closed NBT data!");
/*     */     }
/* 166 */     if (this.finalizer) {
/* 167 */       this.cachedCompound = compound;
/*     */       return;
/*     */     } 
/* 170 */     if (compound != null && ((Set)ReflectionMethod.COMPOUND_GET_KEYS.run(compound, new Object[0])).isEmpty()) {
/* 171 */       compound = null;
/*     */     }
/* 173 */     if (ClassWrapper.CRAFT_ITEMSTACK.getClazz().isAssignableFrom(this.bukkitItem.getClass())) {
/* 174 */       Object nmsStack = NBTReflectionUtil.getCraftItemHandle(this.bukkitItem);
/* 175 */       NBTReflectionUtil.setItemStackCompound(nmsStack, compound);
/*     */     } else {
/* 177 */       Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { this.bukkitItem });
/* 178 */       NBTReflectionUtil.setItemStackCompound(stack, compound);
/* 179 */       this.bukkitItem = (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, new Object[] { stack });
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
/*     */   @Deprecated
/*     */   public void applyNBT(ItemStack item) {
/* 194 */     if (item == null || item.getType() == Material.AIR) {
/* 195 */       throw new NullPointerException("ItemStack can't be null/Air! This is not a NBTAPI bug!");
/*     */     }
/* 197 */     NBTItem nbti = new NBTItem(new ItemStack(item.getType()));
/* 198 */     nbti.mergeCompound(this);
/* 199 */     item.setItemMeta(nbti.getItem().getItemMeta());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void mergeNBT(ItemStack item) {
/* 209 */     NBTItem nbti = new NBTItem(item);
/* 210 */     nbti.mergeCompound(this);
/* 211 */     item.setItemMeta(nbti.getItem().getItemMeta());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void mergeCustomNBT(ItemStack item) {
/* 221 */     if (item == null || item.getType() == Material.AIR) {
/* 222 */       throw new NullPointerException("ItemStack can't be null/Air!");
/*     */     }
/* 224 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/*     */       
/* 226 */       NBT.modify(item, nbt -> nbt.mergeCompound((ReadableNBT)this));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 231 */     ItemMeta meta = item.getItemMeta();
/* 232 */     NBTReflectionUtil.getUnhandledNBTTags(meta)
/* 233 */       .putAll(NBTReflectionUtil.getUnhandledNBTTags(this.bukkitItem.getItemMeta()));
/* 234 */     item.setItemMeta(meta);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public boolean hasCustomNbtData() {
/* 244 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4))
/*     */     {
/* 246 */       return hasNBTData();
/*     */     }
/* 248 */     finalizeChanges();
/* 249 */     ItemMeta meta = this.bukkitItem.getItemMeta();
/* 250 */     return !NBTReflectionUtil.getUnhandledNBTTags(meta).isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void clearCustomNBT() {
/* 258 */     finalizeChanges();
/* 259 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/*     */       
/* 261 */       setCompound((Object)null);
/*     */       return;
/*     */     } 
/* 264 */     ItemMeta meta = this.bukkitItem.getItemMeta();
/* 265 */     NBTReflectionUtil.getUnhandledNBTTags(meta).clear();
/* 266 */     this.bukkitItem.setItemMeta(meta);
/* 267 */     updateCachedCompound();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getItem() {
/* 274 */     return this.bukkitItem;
/*     */   }
/*     */   
/*     */   protected void setItem(ItemStack item) {
/* 278 */     this.bukkitItem = item;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNBTData() {
/* 288 */     return (getCompound() != null);
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
/*     */   public void modifyMeta(BiConsumer<ReadableNBT, ItemMeta> handler) {
/* 302 */     finalizeChanges();
/* 303 */     ItemMeta meta = this.bukkitItem.getItemMeta();
/* 304 */     handler.accept((new NBTContainer(getResolvedObject())).setReadOnly(true), meta);
/* 305 */     this.bukkitItem.setItemMeta(meta);
/* 306 */     updateCachedCompound();
/* 307 */     if (this.directApply) {
/* 308 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 309 */         throw new NbtApiException("Direct apply mode meta changes don't work anymore in 1.20.5+. Please switch to the modern NBT.modify sytnax!");
/*     */       }
/*     */       
/* 312 */       applyNBT(this.originalSrcStack);
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
/*     */   public <T extends ItemMeta> void modifyMeta(Class<T> type, BiConsumer<ReadableNBT, T> handler) {
/* 327 */     finalizeChanges();
/*     */     
/* 329 */     ItemMeta itemMeta = this.bukkitItem.getItemMeta();
/* 330 */     handler.accept((new NBTContainer(getResolvedObject())).setReadOnly(true), (T)itemMeta);
/* 331 */     this.bukkitItem.setItemMeta(itemMeta);
/* 332 */     updateCachedCompound();
/* 333 */     if (this.directApply) {
/* 334 */       applyNBT(this.originalSrcStack);
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
/*     */   public static NBTContainer convertItemtoNBT(ItemStack item) {
/* 347 */     return NBTReflectionUtil.convertNMSItemtoNBTCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { item }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @Deprecated
/*     */   public static ItemStack convertNBTtoItem(NBTCompound comp) {
/* 360 */     return (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, new Object[] {
/* 361 */           NBTReflectionUtil.convertNBTCompoundtoNMSItem(comp)
/*     */         });
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
/*     */   public static NBTContainer convertItemArraytoNBT(ItemStack[] items) {
/* 374 */     NBTContainer container = new NBTContainer();
/* 375 */     container.setInteger("size", Integer.valueOf(items.length));
/* 376 */     NBTCompoundList list = container.getCompoundList("items");
/* 377 */     for (int i = 0; i < items.length; i++) {
/* 378 */       ItemStack item = items[i];
/* 379 */       if (item != null && item.getType() != Material.AIR) {
/*     */ 
/*     */         
/* 382 */         NBTListCompound entry = list.addCompound();
/* 383 */         entry.setInteger("Slot", Integer.valueOf(i));
/* 384 */         entry.mergeCompound(convertItemtoNBT(item));
/*     */       } 
/* 386 */     }  return container;
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
/*     */   @Nullable
/*     */   @Deprecated
/*     */   public static ItemStack[] convertNBTtoItemArray(NBTCompound comp) {
/* 403 */     if (!comp.hasTag("size")) {
/* 404 */       return null;
/*     */     }
/* 406 */     ItemStack[] rebuild = new ItemStack[comp.getInteger("size").intValue()];
/* 407 */     for (int i = 0; i < rebuild.length; i++) {
/* 408 */       rebuild[i] = new ItemStack(Material.AIR);
/*     */     }
/* 410 */     if (!comp.hasTag("items")) {
/* 411 */       return rebuild;
/*     */     }
/* 413 */     NBTCompoundList list = comp.getCompoundList("items");
/* 414 */     for (ReadWriteNBT lcomp : list) {
/* 415 */       if (lcomp instanceof NBTCompound) {
/* 416 */         int slot = lcomp.getInteger("Slot").intValue();
/* 417 */         rebuild[slot] = convertNBTtoItem((NBTCompound)lcomp);
/*     */       } 
/*     */     } 
/* 420 */     return rebuild;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void saveCompound() {
/* 425 */     if (this.directApply)
/* 426 */       applyNBT(this.originalSrcStack); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */