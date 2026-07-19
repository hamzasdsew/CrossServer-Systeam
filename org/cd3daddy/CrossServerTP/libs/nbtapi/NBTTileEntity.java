/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.block.BlockState;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.CheckUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NBTTileEntity
/*     */   extends NBTCompound
/*     */ {
/*     */   private final BlockState tile;
/*     */   private final boolean readonly;
/*     */   private final Object compound;
/*     */   private boolean closed = false;
/*     */   
/*     */   protected NBTTileEntity(BlockState tile, boolean readonly) {
/*  30 */     super(null, null);
/*  31 */     if (tile == null || (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_8_R3) && !tile.isPlaced())) {
/*  32 */       throw new NullPointerException("Tile can't be null/not placed!");
/*     */     }
/*  34 */     this.tile = tile;
/*  35 */     this.readonly = readonly;
/*  36 */     if (readonly) {
/*  37 */       this.compound = getCompound();
/*     */     } else {
/*  39 */       this.compound = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NBTTileEntity(BlockState tile) {
/*  50 */     super(null, null);
/*  51 */     if (tile == null || (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_8_R3) && !tile.isPlaced())) {
/*  52 */       throw new NullPointerException("Tile can't be null/not placed!");
/*     */     }
/*  54 */     this.readonly = false;
/*  55 */     this.compound = null;
/*  56 */     this.tile = tile;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setClosed() {
/*  61 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isClosed() {
/*  66 */     return this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isReadOnly() {
/*  71 */     return this.readonly;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getCompound() {
/*  77 */     if (this.readonly && this.compound != null) {
/*  78 */       return this.compound;
/*     */     }
/*  80 */     if (!Bukkit.isPrimaryThread())
/*  81 */       throw new NbtApiException("BlockEntity NBT needs to be accessed sync!"); 
/*  82 */     return NBTReflectionUtil.getTileEntityNBTTagCompound(this.tile);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setCompound(Object compound) {
/*  87 */     if (this.readonly) {
/*  88 */       throw new NbtApiException("Tried setting data in read only mode!");
/*     */     }
/*  90 */     if (!Bukkit.isPrimaryThread())
/*  91 */       throw new NbtApiException("BlockEntity NBT needs to be accessed sync!"); 
/*  92 */     NBTReflectionUtil.setTileEntityNBTTagCompound(this.tile, compound);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTCompound getPersistentDataContainer() {
/* 102 */     CheckUtil.assertAvailable(MinecraftVersion.MC1_14_R1);
/* 103 */     if (hasTag("PublicBukkitValues")) {
/* 104 */       return getCompound("PublicBukkitValues");
/*     */     }
/* 106 */     NBTContainer container = new NBTContainer();
/* 107 */     container.addCompound("PublicBukkitValues").setString("__nbtapi", "Marker to make the PersistentDataContainer have content");
/*     */     
/* 109 */     mergeCompound(container);
/* 110 */     return getCompound("PublicBukkitValues");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTTileEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */