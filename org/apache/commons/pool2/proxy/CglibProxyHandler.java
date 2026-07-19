/*    */ package org.apache.commons.pool2.proxy;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import net.sf.cglib.proxy.MethodInterceptor;
/*    */ import net.sf.cglib.proxy.MethodProxy;
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
/*    */ 
/*    */ 
/*    */ class CglibProxyHandler<T>
/*    */   extends BaseProxyHandler<T>
/*    */   implements MethodInterceptor
/*    */ {
/*    */   CglibProxyHandler(T pooledObject, UsageTracking<T> usageTracking) {
/* 46 */     super(pooledObject, usageTracking);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object intercept(Object object, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
/* 52 */     return doInvoke(method, args);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\CglibProxyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */