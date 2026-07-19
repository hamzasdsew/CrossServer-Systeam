/*    */ package org.apache.commons.pool2.proxy;
/*    */ 
/*    */ import java.lang.reflect.InvocationHandler;
/*    */ import java.lang.reflect.Method;
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
/*    */ 
/*    */ 
/*    */ class JdkProxyHandler<T>
/*    */   extends BaseProxyHandler<T>
/*    */   implements InvocationHandler
/*    */ {
/*    */   JdkProxyHandler(T pooledObject, UsageTracking<T> usageTracking) {
/* 43 */     super(pooledObject, usageTracking);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 50 */     return doInvoke(method, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\JdkProxyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */