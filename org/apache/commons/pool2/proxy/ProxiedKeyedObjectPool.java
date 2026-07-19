/*     */ package org.apache.commons.pool2.proxy;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.commons.pool2.KeyedObjectPool;
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
/*     */ 
/*     */ public class ProxiedKeyedObjectPool<K, V>
/*     */   implements KeyedObjectPool<K, V>
/*     */ {
/*     */   private final KeyedObjectPool<K, V> pool;
/*     */   private final ProxySource<V> proxySource;
/*     */   
/*     */   public ProxiedKeyedObjectPool(KeyedObjectPool<K, V> pool, ProxySource<V> proxySource) {
/*  49 */     this.pool = pool;
/*  50 */     this.proxySource = proxySource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addObject(K key) throws Exception {
/*  56 */     this.pool.addObject(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V borrowObject(K key) throws Exception {
/*  62 */     UsageTracking<V> usageTracking = null;
/*  63 */     if (this.pool instanceof UsageTracking) {
/*  64 */       usageTracking = (UsageTracking)this.pool;
/*     */     }
/*  66 */     return this.proxySource.createProxy((V)this.pool.borrowObject(key), usageTracking);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() throws Exception {
/*  71 */     this.pool.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear(K key) throws Exception {
/*  76 */     this.pool.clear(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  81 */     this.pool.close();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<K> getKeys() {
/*  92 */     return this.pool.getKeys();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumActive() {
/*  97 */     return this.pool.getNumActive();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumActive(K key) {
/* 102 */     return this.pool.getNumActive(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumIdle() {
/* 107 */     return this.pool.getNumIdle();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getNumIdle(K key) {
/* 112 */     return this.pool.getNumIdle(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public void invalidateObject(K key, V proxy) throws Exception {
/* 117 */     this.pool.invalidateObject(key, this.proxySource.resolveProxy(proxy));
/*     */   }
/*     */ 
/*     */   
/*     */   public void returnObject(K key, V proxy) throws Exception {
/* 122 */     this.pool.returnObject(key, this.proxySource.resolveProxy(proxy));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     StringBuilder builder = new StringBuilder();
/* 131 */     builder.append("ProxiedKeyedObjectPool [pool=");
/* 132 */     builder.append(this.pool);
/* 133 */     builder.append(", proxySource=");
/* 134 */     builder.append(this.proxySource);
/* 135 */     builder.append("]");
/* 136 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\ProxiedKeyedObjectPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */