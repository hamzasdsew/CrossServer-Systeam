/*     */ package org.apache.commons.pool2.proxy;
/*     */ 
/*     */ import org.apache.commons.pool2.ObjectPool;
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
/*     */ public class ProxiedObjectPool<T>
/*     */   implements ObjectPool<T>
/*     */ {
/*     */   private final ObjectPool<T> pool;
/*     */   private final ProxySource<T> proxySource;
/*     */   
/*     */   public ProxiedObjectPool(ObjectPool<T> pool, ProxySource<T> proxySource) {
/*  45 */     this.pool = pool;
/*  46 */     this.proxySource = proxySource;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addObject() throws Exception {
/*  51 */     this.pool.addObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T borrowObject() throws Exception {
/*  58 */     UsageTracking<T> usageTracking = null;
/*  59 */     if (this.pool instanceof UsageTracking) {
/*  60 */       usageTracking = (UsageTracking<T>)this.pool;
/*     */     }
/*  62 */     return this.proxySource.createProxy((T)this.pool.borrowObject(), usageTracking);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() throws Exception {
/*  68 */     this.pool.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  74 */     this.pool.close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumActive() {
/*  80 */     return this.pool.getNumActive();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumIdle() {
/*  86 */     return this.pool.getNumIdle();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void invalidateObject(T proxy) throws Exception {
/*  92 */     this.pool.invalidateObject(this.proxySource.resolveProxy(proxy));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void returnObject(T proxy) throws Exception {
/*  98 */     this.pool.returnObject(this.proxySource.resolveProxy(proxy));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 106 */     StringBuilder builder = new StringBuilder();
/* 107 */     builder.append("ProxiedObjectPool [pool=");
/* 108 */     builder.append(this.pool);
/* 109 */     builder.append(", proxySource=");
/* 110 */     builder.append(this.proxySource);
/* 111 */     builder.append("]");
/* 112 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\ProxiedObjectPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */