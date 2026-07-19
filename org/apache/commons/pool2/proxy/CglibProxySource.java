/*    */ package org.apache.commons.pool2.proxy;
/*    */ 
/*    */ import net.sf.cglib.proxy.Callback;
/*    */ import net.sf.cglib.proxy.Enhancer;
/*    */ import net.sf.cglib.proxy.Factory;
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
/*    */ public class CglibProxySource<T>
/*    */   implements ProxySource<T>
/*    */ {
/*    */   private final Class<? extends T> superclass;
/*    */   
/*    */   public CglibProxySource(Class<? extends T> superclass) {
/* 41 */     this.superclass = superclass;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T createProxy(T pooledObject, UsageTracking<T> usageTracking) {
/* 47 */     Enhancer enhancer = new Enhancer();
/* 48 */     enhancer.setSuperclass(this.superclass);
/*    */     
/* 50 */     CglibProxyHandler<T> proxyInterceptor = new CglibProxyHandler<>(pooledObject, usageTracking);
/*    */     
/* 52 */     enhancer.setCallback((Callback)proxyInterceptor);
/*    */     
/* 54 */     return (T)enhancer.create();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T resolveProxy(T proxy) {
/* 63 */     CglibProxyHandler<T> cglibProxyHandler = (CglibProxyHandler<T>)((Factory)proxy).getCallback(0);
/* 64 */     return cglibProxyHandler.disableProxy();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     StringBuilder builder = new StringBuilder();
/* 73 */     builder.append("CglibProxySource [superclass=");
/* 74 */     builder.append(this.superclass);
/* 75 */     builder.append("]");
/* 76 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\CglibProxySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */