/*     */ package org.apache.commons.pool2.proxy;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.apache.commons.pool2.UsageTracking;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class BaseProxyHandler<T>
/*     */ {
/*     */   private volatile T pooledObject;
/*     */   private final UsageTracking<T> usageTracking;
/*     */   
/*     */   BaseProxyHandler(T pooledObject, UsageTracking<T> usageTracking) {
/*  46 */     this.pooledObject = pooledObject;
/*  47 */     this.usageTracking = usageTracking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   T disableProxy() {
/*  59 */     T result = this.pooledObject;
/*  60 */     this.pooledObject = null;
/*  61 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object doInvoke(Method method, Object[] args) throws Throwable {
/*  74 */     validateProxiedObject();
/*  75 */     T object = getPooledObject();
/*  76 */     if (this.usageTracking != null) {
/*  77 */       this.usageTracking.use(object);
/*     */     }
/*  79 */     return method.invoke(object, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   T getPooledObject() {
/*  89 */     return this.pooledObject;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     StringBuilder builder = new StringBuilder();
/*  99 */     builder.append(getClass().getName());
/* 100 */     builder.append(" [pooledObject=");
/* 101 */     builder.append(this.pooledObject);
/* 102 */     builder.append(", usageTracking=");
/* 103 */     builder.append(this.usageTracking);
/* 104 */     builder.append("]");
/* 105 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validateProxiedObject() {
/* 116 */     if (this.pooledObject == null)
/* 117 */       throw new IllegalStateException("This object may no longer be used as it has been returned to the Object Pool."); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\BaseProxyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */