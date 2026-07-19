/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.MojangToMapping;
/*    */ 
/*    */ 
/*    */ public final class ReflectionUtil
/*    */ {
/*    */   public static Field getMappedField(Class<?> clazz, String mapping) {
/* 11 */     String mojmapName = mapping.split("#")[1];
/*    */     try {
/* 13 */       return clazz.getField(mojmapName);
/* 14 */     } catch (NoSuchFieldException|SecurityException noSuchFieldException) {
/*    */ 
/*    */       
/*    */       try {
/* 18 */         return clazz.getDeclaredField((String)MojangToMapping.getMapping().get(mapping));
/* 19 */       } catch (Exception e) {
/* 20 */         throw new NbtApiException("Unable to find field " + mapping + " in class " + clazz.getName(), e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\ReflectionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */