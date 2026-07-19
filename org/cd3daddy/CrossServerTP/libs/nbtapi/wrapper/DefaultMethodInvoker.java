/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper;
/*    */ 
/*    */ import java.lang.invoke.MethodHandles;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.Arrays;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*    */ 
/*    */ 
/*    */ 
/*    */ class DefaultMethodInvoker
/*    */ {
/*    */   private static Method invokeDefaultMethod;
/*    */   
/*    */   static {
/*    */     try {
/* 18 */       invokeDefaultMethod = InvocationHandler.class.getDeclaredMethod("invokeDefault", new Class[] { Object.class, Method.class, Object[].class });
/*    */       
/* 20 */       invokeDefaultMethod.setAccessible(true);
/* 21 */     } catch (NoSuchMethodException|SecurityException noSuchMethodException) {}
/*    */   }
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
/*    */   
/*    */   public static Object invokeDefault(Class<?> srcInt, Object target, Method method, Object[] args) {
/* 35 */     if (invokeDefaultMethod != null) {
/*    */       try {
/* 37 */         return invokeDefaultMethod.invoke(null, new Object[] { target, method, args });
/* 38 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 39 */         throw new NbtApiException("Error while trying to invoke a default method for Java 9+. " + target + " " + method + " " + 
/* 40 */             Arrays.toString(args), e);
/*    */       } 
/*    */     }
/*    */     try {
/* 44 */       Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(new Class[] { Class.class });
/* 45 */       constructor.setAccessible(true);
/* 46 */       return ((MethodHandles.Lookup)constructor.newInstance(new Object[] { srcInt })).in(srcInt).unreflectSpecial(method, srcInt).bindTo(target)
/* 47 */         .invokeWithArguments(args);
/* 48 */     } catch (Throwable e) {
/* 49 */       throw new NbtApiException("Error while trying to invoke a default method for Java 8. " + target + " " + method + " " + 
/* 50 */           Arrays.toString(args), e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\wrapper\DefaultMethodInvoker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */