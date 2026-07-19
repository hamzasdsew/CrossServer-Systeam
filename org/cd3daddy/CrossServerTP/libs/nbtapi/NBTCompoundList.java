/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBTCompoundList;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NBTCompoundList
/*     */   extends NBTList<ReadWriteNBT>
/*     */   implements ReadWriteNBTCompoundList
/*     */ {
/*     */   protected NBTCompoundList(NBTCompound owner, String name, NBTType type, Object list) {
/*  19 */     super(owner, name, type, list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTListCompound addCompound() {
/*  28 */     return (NBTListCompound)addCompound((NBTCompound)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTCompound addCompound(NBTCompound comp) {
/*  39 */     if (getParent().isReadOnly()) {
/*  40 */       throw new NbtApiException("Tried setting data in read only mode!");
/*     */     }
/*     */     try {
/*  43 */       Object compound = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
/*  44 */       if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
/*  45 */         ReflectionMethod.LIST_ADD.run(this.listObject, new Object[] { Integer.valueOf(size()), compound });
/*     */       } else {
/*  47 */         ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, new Object[] { compound });
/*     */       } 
/*  49 */       getParent().saveCompound();
/*  50 */       NBTListCompound listcomp = new NBTListCompound(this, compound);
/*  51 */       if (comp != null) {
/*  52 */         listcomp.mergeCompound(comp);
/*     */       }
/*  54 */       return listcomp;
/*  55 */     } catch (Exception ex) {
/*  56 */       throw new NbtApiException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ReadWriteNBT addCompound(ReadableNBT comp) {
/*  62 */     if (comp instanceof NBTCompound) {
/*  63 */       return addCompound((NBTCompound)comp);
/*     */     }
/*  65 */     return null;
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
/*     */   public boolean add(ReadWriteNBT empty) {
/*  79 */     return (addCompound((ReadableNBT)empty) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, ReadWriteNBT element) {
/*  84 */     if (element != null) {
/*  85 */       throw new NbtApiException("You need to pass null! ListCompounds from other lists won't work.");
/*     */     }
/*  87 */     if (getParent().isReadOnly()) {
/*  88 */       throw new NbtApiException("Tried setting data in read only mode!");
/*     */     }
/*     */     try {
/*  91 */       Object compound = ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().newInstance();
/*  92 */       if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
/*  93 */         ReflectionMethod.LIST_ADD.run(this.listObject, new Object[] { Integer.valueOf(index), compound });
/*     */       } else {
/*  95 */         ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, new Object[] { compound });
/*     */       } 
/*  97 */       getParent().saveCompound();
/*  98 */     } catch (Exception ex) {
/*  99 */       throw new NbtApiException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTListCompound get(int index) {
/*     */     try {
/* 106 */       Object compound = ReflectionMethod.LIST_GET_COMPOUND.run(this.listObject, new Object[] { Integer.valueOf(index) });
/* 107 */       return new NBTListCompound(this, compound);
/* 108 */     } catch (Exception ex) {
/* 109 */       throw new NbtApiException(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTListCompound set(int index, ReadWriteNBT element) {
/* 115 */     throw new NbtApiException("This method doesn't work in the ListCompound context.");
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object asTag(ReadWriteNBT object) {
/* 120 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTCompoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */