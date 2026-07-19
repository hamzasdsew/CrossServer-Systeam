/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
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
/*     */ public class NBTContainer
/*     */   extends NBTCompound
/*     */ {
/*     */   private Object nbt;
/*     */   private boolean closed;
/*     */   private boolean readOnly;
/*     */   
/*     */   @Deprecated
/*     */   public NBTContainer() {
/*  29 */     super(null, null);
/*  30 */     this.nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NBTContainer(Object nbt) {
/*  41 */     super(null, null);
/*  42 */     if (nbt == null) {
/*  43 */       nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*     */     }
/*  45 */     if (!ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz().isAssignableFrom(nbt.getClass())) {
/*  46 */       throw new NbtApiException("The object '" + nbt.getClass() + "' is not a valid NBT-Object!");
/*     */     }
/*  48 */     this.nbt = nbt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public NBTContainer(InputStream inputsteam) {
/*  59 */     super(null, null);
/*  60 */     this.nbt = NBTReflectionUtil.readNBT(inputsteam);
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
/*     */   public NBTContainer(String nbtString) {
/*  72 */     super(null, null);
/*  73 */     if (nbtString == null) {
/*  74 */       throw new NullPointerException("The String can't be null!");
/*     */     }
/*     */     try {
/*  77 */       this.nbt = ReflectionMethod.PARSE_NBT.run(null, new Object[] { nbtString });
/*  78 */     } catch (Exception ex) {
/*  79 */       throw new NbtApiException("Unable to parse Malformed Json!", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getCompound() {
/*  85 */     return this.nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCompound(Object tag) {
/*  90 */     this.nbt = tag;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setClosed() {
/*  95 */     this.closed = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isClosed() {
/* 100 */     return this.closed;
/*     */   }
/*     */   
/*     */   protected boolean isReadOnly() {
/* 104 */     return this.readOnly;
/*     */   }
/*     */   
/*     */   protected NBTContainer setReadOnly(boolean readOnly) {
/* 108 */     this.readOnly = true;
/* 109 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */