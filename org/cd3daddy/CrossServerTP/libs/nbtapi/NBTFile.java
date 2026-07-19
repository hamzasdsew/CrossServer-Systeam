/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.NBTFileHandle;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ObjectCreator;
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
/*     */ public class NBTFile
/*     */   extends NBTCompound
/*     */   implements NBTFileHandle
/*     */ {
/*     */   private final File file;
/*     */   private Object nbt;
/*     */   
/*     */   @Deprecated
/*     */   public NBTFile(File file) throws IOException {
/*  31 */     super(null, null);
/*  32 */     if (file == null) {
/*  33 */       throw new NullPointerException("File can't be null!");
/*     */     }
/*  35 */     this.file = file;
/*  36 */     if (file.exists()) {
/*  37 */       this.nbt = NBTReflectionUtil.readNBT(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0]));
/*     */     } else {
/*  39 */       this.nbt = ObjectCreator.NMS_NBTTAGCOMPOUND.getInstance(new Object[0]);
/*  40 */       save();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void save() throws IOException {
/*     */     try {
/*  52 */       getWriteLock().lock();
/*  53 */       saveTo(this.file, this);
/*     */     } finally {
/*  55 */       getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public File getFile() {
/*  64 */     return this.file;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getCompound() {
/*  69 */     return this.nbt;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setCompound(Object compound) {
/*  74 */     this.nbt = compound;
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
/*     */   @Deprecated
/*     */   public static NBTCompound readFrom(File file) throws IOException {
/*  89 */     if (!file.exists())
/*  90 */       return new NBTContainer(); 
/*  91 */     return new NBTContainer(NBTReflectionUtil.readNBT(Files.newInputStream(file.toPath(), new java.nio.file.OpenOption[0])));
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
/*     */   @Deprecated
/*     */   public static void saveTo(File file, NBTCompound nbt) throws IOException {
/* 106 */     if (!file.exists()) {
/* 107 */       file.getParentFile().mkdirs();
/* 108 */       if (!file.createNewFile())
/* 109 */         throw new IOException("Unable to create file at " + file.getAbsolutePath()); 
/*     */     } 
/* 111 */     nbt.writeCompound(Files.newOutputStream(file.toPath(), new java.nio.file.OpenOption[0]));
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */