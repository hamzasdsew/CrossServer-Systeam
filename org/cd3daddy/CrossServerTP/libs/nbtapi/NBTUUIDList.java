/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.UUID;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.UUIDUtil;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTUUIDList
/*    */   extends NBTList<UUID>
/*    */ {
/*    */   private final NBTContainer tmpContainer;
/*    */   
/*    */   protected NBTUUIDList(NBTCompound owner, String name, NBTType type, Object list) {
/* 22 */     super(owner, name, type, list);
/* 23 */     this.tmpContainer = new NBTContainer();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object asTag(UUID object) {
/*    */     try {
/* 29 */       Constructor<?> con = ClassWrapper.NMS_NBTTAGINTARRAY.getClazz().getDeclaredConstructor(new Class[] { int[].class });
/* 30 */       con.setAccessible(true);
/* 31 */       return con.newInstance(new Object[] { UUIDUtil.uuidToIntArray(object) });
/* 32 */     } catch (InstantiationException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException e) {
/*    */       
/* 34 */       throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public UUID get(int index) {
/*    */     try {
/* 41 */       Object obj = ReflectionMethod.LIST_GET.run(this.listObject, new Object[] { Integer.valueOf(index) });
/* 42 */       ReflectionMethod.COMPOUND_SET.run(this.tmpContainer.getCompound(), new Object[] { "tmp", obj });
/* 43 */       int[] val = this.tmpContainer.getIntArray("tmp");
/* 44 */       this.tmpContainer.removeKey("tmp");
/* 45 */       return UUIDUtil.uuidFromIntArray(val);
/* 46 */     } catch (NumberFormatException nf) {
/* 47 */       return null;
/* 48 */     } catch (Exception ex) {
/* 49 */       throw new NbtApiException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTUUIDList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */