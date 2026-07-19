/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.util.Optional;
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
/*    */ public class NBTStringList
/*    */   extends NBTList<String>
/*    */ {
/*    */   protected NBTStringList(NBTCompound owner, String name, NBTType type, Object list) {
/* 19 */     super(owner, name, type, list);
/*    */   }
/*    */ 
/*    */   
/*    */   public String get(int index) {
/*    */     try {
/* 25 */       Object ret = ReflectionMethod.LIST_GET_STRING.run(this.listObject, new Object[] { Integer.valueOf(index) });
/* 26 */       if (ret instanceof Optional) {
/* 27 */         return ((Optional<String>)ret).orElse("");
/*    */       }
/* 29 */       return (String)ret;
/* 30 */     } catch (Exception ex) {
/* 31 */       throw new NbtApiException(ex);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected Object asTag(String object) {
/*    */     try {
/* 38 */       Constructor<?> con = ClassWrapper.NMS_NBTTAGSTRING.getClazz().getDeclaredConstructor(new Class[] { String.class });
/* 39 */       con.setAccessible(true);
/* 40 */       return con.newInstance(new Object[] { object });
/* 41 */     } catch (InstantiationException|IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException|NoSuchMethodException|SecurityException e) {
/*    */       
/* 43 */       throw new NbtApiException("Error while wrapping the Object " + object + " to it's NMS object!", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTStringList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */