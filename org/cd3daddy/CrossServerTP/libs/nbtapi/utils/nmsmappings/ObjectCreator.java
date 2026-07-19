/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.logging.Level;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ObjectCreator
/*    */ {
/* 19 */   NMS_NBTTAGCOMPOUND(null, null, ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz(), new Class[0]),
/* 20 */   NMS_CUSTOMDATA(MinecraftVersion.MC1_20_R4, null, ClassWrapper.NMS_CUSTOMDATA.getClazz(), new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND.getClazz() }),
/* 21 */   NMS_BLOCKPOSITION(null, null, ClassWrapper.NMS_BLOCKPOSITION.getClazz(), new Class[] { int.class, int.class, int.class }),
/* 22 */   NMS_COMPOUNDFROMITEM(MinecraftVersion.MC1_11_R1, MinecraftVersion.MC1_20_R3, ClassWrapper.NMS_ITEMSTACK.getClazz(), new Class[] { ClassWrapper.NMS_NBTTAGCOMPOUND
/* 23 */       .getClazz() });
/*    */   
/*    */   private Constructor<?> construct;
/*    */   private Class<?> targetClass;
/*    */   
/*    */   ObjectCreator(MinecraftVersion from, MinecraftVersion to, Class<?> clazz, Class<?>... args) {
/* 29 */     if (clazz == null)
/*    */       return; 
/* 31 */     if (from != null && MinecraftVersion.getVersion().getVersionId() < from.getVersionId())
/*    */       return; 
/* 33 */     if (to != null && MinecraftVersion.getVersion().getVersionId() > to.getVersionId())
/*    */       return; 
/*    */     try {
/* 36 */       this.targetClass = clazz;
/* 37 */       this.construct = clazz.getDeclaredConstructor(args);
/* 38 */       this.construct.setAccessible(true);
/* 39 */     } catch (Exception ex) {
/* 40 */       MinecraftVersion.getLogger().log(Level.SEVERE, "Unable to find the constructor for the class '" + clazz.getName() + "'", ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getInstance(Object... args) {
/*    */     try {
/* 52 */       return this.construct.newInstance(args);
/* 53 */     } catch (Exception ex) {
/* 54 */       throw new NbtApiException("Exception while creating a new instance of '" + this.targetClass + "'", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\nmsmappings\ObjectCreator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */