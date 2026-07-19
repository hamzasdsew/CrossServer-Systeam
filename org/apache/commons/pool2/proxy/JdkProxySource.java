/*    */ package org.apache.commons.pool2.proxy;
/*    */ 
/*    */ import java.lang.reflect.Proxy;
/*    */ import java.util.Arrays;
/*    */ import org.apache.commons.pool2.UsageTracking;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JdkProxySource<T>
/*    */   implements ProxySource<T>
/*    */ {
/*    */   private final ClassLoader classLoader;
/*    */   private final Class<?>[] interfaces;
/*    */   
/*    */   public JdkProxySource(ClassLoader classLoader, Class<?>[] interfaces) {
/* 43 */     this.classLoader = classLoader;
/*    */     
/* 45 */     this.interfaces = (Class[])Arrays.<Class<?>[]>copyOf((Class<?>[][])interfaces, interfaces.length);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T createProxy(T pooledObject, UsageTracking<T> usageTracking) {
/* 51 */     return (T)Proxy.newProxyInstance(this.classLoader, this.interfaces, new JdkProxyHandler<>(pooledObject, usageTracking));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T resolveProxy(T proxy) {
/* 58 */     return ((JdkProxyHandler<T>)Proxy.getInvocationHandler(proxy)).disableProxy();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 66 */     StringBuilder builder = new StringBuilder();
/* 67 */     builder.append("JdkProxySource [classLoader=");
/* 68 */     builder.append(this.classLoader);
/* 69 */     builder.append(", interfaces=");
/* 70 */     builder.append(Arrays.toString((Object[])this.interfaces));
/* 71 */     builder.append("]");
/* 72 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\JdkProxySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */