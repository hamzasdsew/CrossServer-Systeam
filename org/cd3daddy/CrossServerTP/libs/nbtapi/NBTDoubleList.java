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
/*    */ public class NBTDoubleList
/*    */   extends NBTList<Double>
/*    */ {
/*    */   protected NBTDoubleList(NBTCompound owner, String name, NBTType type, Object list) {
/* 18 */     super(owner, name, type, list);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object asTag(Double object) {
/*    */     try {
/* 24 */       Constructor<?> con = ClassWrapper.NMS_NBTTAGDOUBLE.getClazz().getDeclaredConstructor(new Class[] { double.class });
/* 25 */       con.setAccessible(true);
/* 26 */       return con.newInstance(new Object[] { object });
/* 27 */     } catch (InstantiationException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException e) {
/*    */       
/* 29 */       throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Double get(int index) {
/*    */     try {
/* 36 */       Object obj = ReflectionMethod.LIST_GET.run(this.listObject, new Object[] { Integer.valueOf(index) });
/* 37 */       return Double.valueOf(obj.toString());
/* 38 */     } catch (NumberFormatException nf) {
/* 39 */       return Double.valueOf(0.0D);
/* 40 */     } catch (Exception ex) {
/* 41 */       throw new NbtApiException(ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTDoubleList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */