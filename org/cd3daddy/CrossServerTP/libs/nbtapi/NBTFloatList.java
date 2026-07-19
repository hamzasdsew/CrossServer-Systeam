/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
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
/*    */ public class NBTFloatList
/*    */   extends NBTList<Float>
/*    */ {
/*    */   protected NBTFloatList(NBTCompound owner, String name, NBTType type, Object list) {
/* 18 */     super(owner, name, type, list);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object asTag(Float object) {
/*    */     try {
/* 24 */       Constructor<?> con = ClassWrapper.NMS_NBTTAGFLOAT.getClazz().getDeclaredConstructor(new Class[] { float.class });
/* 25 */       con.setAccessible(true);
/* 26 */       return con.newInstance(new Object[] { object });
/* 27 */     } catch (InstantiationException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException e) {
/*    */       
/* 29 */       throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Float get(int index) {
/*    */     try {
/* 36 */       Object obj = ReflectionMethod.LIST_GET.run(this.listObject, new Object[] { Integer.valueOf(index) });
/* 37 */       return Float.valueOf(obj.toString());
/* 38 */     } catch (NumberFormatException nf) {
/* 39 */       return Float.valueOf(0.0F);
/* 40 */     } catch (Exception ex) {
/* 41 */       throw new NbtApiException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTFloatList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */