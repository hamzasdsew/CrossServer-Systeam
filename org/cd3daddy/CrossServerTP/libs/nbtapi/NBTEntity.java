/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.entity.Entity;
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
/*     */ public class NBTEntity
/*     */   extends NBTCompound
/*     */ {
/*     */   private final Entity ent;
/*     */   private final boolean readonly;
/*     */   private final Object compound;
/*     */   private boolean closed = false;
/*     */   
/*     */   protected NBTEntity(Entity entity, boolean readonly) {
/*  29 */     super(null, null);
/*  30 */     if (entity == null) {
/*  31 */       throw new NullPointerException("Entity can't be null!");
/*     */     }
/*  33 */     this.readonly = readonly;
/*  34 */     this.ent = entity;
/*  35 */     if (readonly) {
/*  36 */       this.compound = getCompound();
/*     */     } else {
/*  38 */       this.compound = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NBTEntity(Entity entity) {
/*  49 */     super(null, null);
/*  50 */     if (entity == null) {
/*  51 */       throw new NullPointerException("Entity can't be null!");
/*     */     }
/*  53 */     this.readonly = false;
/*  54 */     this.compound = null;
/*  55 */     this.ent = entity;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setClosed() {
/*  60 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isClosed() {
/*  65 */     return this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isReadOnly() {
/*  70 */     return this.readonly;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getCompound() {
/*  76 */     if (this.readonly && this.compound != null) {
/*  77 */       return this.compound;
/*     */     }
/*  79 */     if (!Bukkit.isPrimaryThread())
/*  80 */       throw new NbtApiException("Entity NBT needs to be accessed sync!"); 
/*  81 */     return NBTReflectionUtil.getEntityNBTTagCompound(NBTReflectionUtil.getNMSEntity(this.ent));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setCompound(Object compound) {
/*  86 */     if (this.readonly) {
/*  87 */       throw new NbtApiException("Tried setting data in read only mode!");
/*     */     }
/*  89 */     if (!Bukkit.isPrimaryThread())
/*  90 */       throw new NbtApiException("Entity NBT needs to be accessed sync!"); 
/*  91 */     NBTReflectionUtil.setEntityNBTTag(compound, NBTReflectionUtil.getNMSEntity(this.ent));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTCompound getPersistentDataContainer() {
/* 101 */     CheckUtil.assertAvailable(MinecraftVersion.MC1_14_R1);
/* 102 */     return new NBTPersistentDataContainer(this.ent.getPersistentDataContainer());
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */