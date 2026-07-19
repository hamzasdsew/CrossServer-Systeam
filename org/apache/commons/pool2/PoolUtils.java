/*      */ package org.apache.commons.pool2;
/*      */ 
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Timer;
/*      */ import java.util.TimerTask;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class PoolUtils
/*      */ {
/*      */   private static final String MSG_FACTOR_NEGATIVE = "factor must be positive.";
/*      */   private static final String MSG_MIN_IDLE = "minIdle must be non-negative.";
/*      */   static final String MSG_NULL_KEY = "key must not be null.";
/*      */   private static final String MSG_NULL_KEYED_POOL = "keyedPool must not be null.";
/*      */   static final String MSG_NULL_KEYS = "keys must not be null.";
/*      */   private static final String MSG_NULL_POOL = "pool must not be null.";
/*      */   
/*      */   private static final class ErodingFactor
/*      */   {
/*      */     private final float factor;
/*      */     private volatile transient long nextShrinkMillis;
/*      */     private volatile transient int idleHighWaterMark;
/*      */     
/*      */     public ErodingFactor(float factor) {
/*   66 */       this.factor = factor;
/*   67 */       this.nextShrinkMillis = System.currentTimeMillis() + (long)(900000.0F * factor);
/*   68 */       this.idleHighWaterMark = 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public long getNextShrink() {
/*   77 */       return this.nextShrinkMillis;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*   85 */       return "ErodingFactor{factor=" + this.factor + ", idleHighWaterMark=" + this.idleHighWaterMark + '}';
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void update(long nowMillis, int numIdle) {
/*   98 */       int idle = Math.max(0, numIdle);
/*   99 */       this.idleHighWaterMark = Math.max(idle, this.idleHighWaterMark);
/*  100 */       float maxInterval = 15.0F;
/*  101 */       float minutes = 15.0F + -14.0F / this.idleHighWaterMark * idle;
/*      */       
/*  103 */       this.nextShrinkMillis = nowMillis + (long)(minutes * 60000.0F * this.factor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ErodingKeyedObjectPool<K, V>
/*      */     implements KeyedObjectPool<K, V>
/*      */   {
/*      */     private final KeyedObjectPool<K, V> keyedPool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final PoolUtils.ErodingFactor erodingFactor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected ErodingKeyedObjectPool(KeyedObjectPool<K, V> keyedPool, PoolUtils.ErodingFactor erodingFactor) {
/*  135 */       if (keyedPool == null) {
/*  136 */         throw new IllegalArgumentException("keyedPool must not be null.");
/*      */       }
/*      */       
/*  139 */       this.keyedPool = keyedPool;
/*  140 */       this.erodingFactor = erodingFactor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ErodingKeyedObjectPool(KeyedObjectPool<K, V> keyedPool, float factor) {
/*  156 */       this(keyedPool, new PoolUtils.ErodingFactor(factor));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addObject(K key) throws Exception {
/*  164 */       this.keyedPool.addObject(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V borrowObject(K key) throws Exception {
/*  172 */       return this.keyedPool.borrowObject(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() throws Exception {
/*  180 */       this.keyedPool.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear(K key) throws Exception {
/*  188 */       this.keyedPool.clear(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() {
/*      */       try {
/*  197 */         this.keyedPool.close();
/*  198 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected PoolUtils.ErodingFactor getErodingFactor(K key) {
/*  211 */       return this.erodingFactor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected KeyedObjectPool<K, V> getKeyedPool() {
/*  220 */       return this.keyedPool;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public List<K> getKeys() {
/*  228 */       return this.keyedPool.getKeys();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumActive() {
/*  236 */       return this.keyedPool.getNumActive();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumActive(K key) {
/*  244 */       return this.keyedPool.getNumActive(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumIdle() {
/*  252 */       return this.keyedPool.getNumIdle();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumIdle(K key) {
/*  260 */       return this.keyedPool.getNumIdle(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void invalidateObject(K key, V obj) {
/*      */       try {
/*  269 */         this.keyedPool.invalidateObject(key, obj);
/*  270 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void returnObject(K key, V obj) throws Exception {
/*  290 */       boolean discard = false;
/*  291 */       long nowMillis = System.currentTimeMillis();
/*  292 */       PoolUtils.ErodingFactor factor = getErodingFactor(key);
/*  293 */       synchronized (this.keyedPool) {
/*  294 */         if (factor.getNextShrink() < nowMillis) {
/*  295 */           int numIdle = getNumIdle(key);
/*  296 */           if (numIdle > 0) {
/*  297 */             discard = true;
/*      */           }
/*      */           
/*  300 */           factor.update(nowMillis, numIdle);
/*      */         } 
/*      */       } 
/*      */       try {
/*  304 */         if (discard) {
/*  305 */           this.keyedPool.invalidateObject(key, obj);
/*      */         } else {
/*  307 */           this.keyedPool.returnObject(key, obj);
/*      */         } 
/*  309 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  319 */       return "ErodingKeyedObjectPool{factor=" + this.erodingFactor + ", keyedPool=" + this.keyedPool + '}';
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class ErodingObjectPool<T>
/*      */     implements ObjectPool<T>
/*      */   {
/*      */     private final ObjectPool<T> pool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final PoolUtils.ErodingFactor factor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ErodingObjectPool(ObjectPool<T> pool, float factor) {
/*  352 */       this.pool = pool;
/*  353 */       this.factor = new PoolUtils.ErodingFactor(factor);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addObject() throws Exception {
/*  361 */       this.pool.addObject();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public T borrowObject() throws Exception {
/*  369 */       return this.pool.borrowObject();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() throws Exception {
/*  377 */       this.pool.clear();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() {
/*      */       try {
/*  386 */         this.pool.close();
/*  387 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumActive() {
/*  397 */       return this.pool.getNumActive();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumIdle() {
/*  405 */       return this.pool.getNumIdle();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void invalidateObject(T obj) {
/*      */       try {
/*  414 */         this.pool.invalidateObject(obj);
/*  415 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void returnObject(T obj) {
/*  433 */       boolean discard = false;
/*  434 */       long nowMillis = System.currentTimeMillis();
/*  435 */       synchronized (this.pool) {
/*  436 */         if (this.factor.getNextShrink() < nowMillis) {
/*      */           
/*  438 */           int numIdle = this.pool.getNumIdle();
/*  439 */           if (numIdle > 0) {
/*  440 */             discard = true;
/*      */           }
/*      */           
/*  443 */           this.factor.update(nowMillis, numIdle);
/*      */         } 
/*      */       } 
/*      */       try {
/*  447 */         if (discard) {
/*  448 */           this.pool.invalidateObject(obj);
/*      */         } else {
/*  450 */           this.pool.returnObject(obj);
/*      */         } 
/*  452 */       } catch (Exception exception) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  462 */       return "ErodingObjectPool{factor=" + this.factor + ", pool=" + this.pool + '}';
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ErodingPerKeyKeyedObjectPool<K, V>
/*      */     extends ErodingKeyedObjectPool<K, V>
/*      */   {
/*      */     private final float factor;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  480 */     private final Map<K, PoolUtils.ErodingFactor> factors = Collections.synchronizedMap(new HashMap<>());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ErodingPerKeyKeyedObjectPool(KeyedObjectPool<K, V> keyedPool, float factor) {
/*  492 */       super(keyedPool, (PoolUtils.ErodingFactor)null);
/*  493 */       this.factor = factor;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected PoolUtils.ErodingFactor getErodingFactor(K key) {
/*  503 */       return this.factors.computeIfAbsent(key, k -> new PoolUtils.ErodingFactor(this.factor));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  512 */       return "ErodingPerKeyKeyedObjectPool{factor=" + this.factor + ", keyedPool=" + 
/*  513 */         getKeyedPool() + '}';
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class KeyedObjectPoolMinIdleTimerTask<K, V>
/*      */     extends TimerTask
/*      */   {
/*      */     private final int minIdle;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final K key;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final KeyedObjectPool<K, V> keyedPool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     KeyedObjectPoolMinIdleTimerTask(KeyedObjectPool<K, V> keyedPool, K key, int minIdle) throws IllegalArgumentException {
/*  549 */       if (keyedPool == null) {
/*  550 */         throw new IllegalArgumentException("keyedPool must not be null.");
/*      */       }
/*      */       
/*  553 */       this.keyedPool = keyedPool;
/*  554 */       this.key = key;
/*  555 */       this.minIdle = minIdle;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/*  563 */       boolean success = false;
/*      */       try {
/*  565 */         if (this.keyedPool.getNumIdle(this.key) < this.minIdle) {
/*  566 */           this.keyedPool.addObject(this.key);
/*      */         }
/*  568 */         success = true;
/*      */       }
/*  570 */       catch (Exception e) {
/*  571 */         cancel();
/*      */       }
/*      */       finally {
/*      */         
/*  575 */         if (!success) {
/*  576 */           cancel();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  586 */       StringBuilder sb = new StringBuilder();
/*  587 */       sb.append("KeyedObjectPoolMinIdleTimerTask");
/*  588 */       sb.append("{minIdle=").append(this.minIdle);
/*  589 */       sb.append(", key=").append(this.key);
/*  590 */       sb.append(", keyedPool=").append(this.keyedPool);
/*  591 */       sb.append('}');
/*  592 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class ObjectPoolMinIdleTimerTask<T>
/*      */     extends TimerTask
/*      */   {
/*      */     private final int minIdle;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ObjectPool<T> pool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     ObjectPoolMinIdleTimerTask(ObjectPool<T> pool, int minIdle) throws IllegalArgumentException {
/*  624 */       if (pool == null) {
/*  625 */         throw new IllegalArgumentException("pool must not be null.");
/*      */       }
/*  627 */       this.pool = pool;
/*  628 */       this.minIdle = minIdle;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void run() {
/*  636 */       boolean success = false;
/*      */       try {
/*  638 */         if (this.pool.getNumIdle() < this.minIdle) {
/*  639 */           this.pool.addObject();
/*      */         }
/*  641 */         success = true;
/*      */       }
/*  643 */       catch (Exception e) {
/*  644 */         cancel();
/*      */       } finally {
/*      */         
/*  647 */         if (!success) {
/*  648 */           cancel();
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  658 */       StringBuilder sb = new StringBuilder();
/*  659 */       sb.append("ObjectPoolMinIdleTimerTask");
/*  660 */       sb.append("{minIdle=").append(this.minIdle);
/*  661 */       sb.append(", pool=").append(this.pool);
/*  662 */       sb.append('}');
/*  663 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final class SynchronizedKeyedObjectPool<K, V>
/*      */     implements KeyedObjectPool<K, V>
/*      */   {
/*  688 */     private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final KeyedObjectPool<K, V> keyedPool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SynchronizedKeyedObjectPool(KeyedObjectPool<K, V> keyedPool) throws IllegalArgumentException {
/*  703 */       if (keyedPool == null) {
/*  704 */         throw new IllegalArgumentException("keyedPool must not be null.");
/*      */       }
/*      */       
/*  707 */       this.keyedPool = keyedPool;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addObject(K key) throws Exception {
/*  715 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/*  716 */       writeLock.lock();
/*      */       try {
/*  718 */         this.keyedPool.addObject(key);
/*      */       } finally {
/*  720 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V borrowObject(K key) throws Exception {
/*  729 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/*  730 */       writeLock.lock();
/*      */       try {
/*  732 */         return this.keyedPool.borrowObject(key);
/*      */       } finally {
/*  734 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() throws Exception {
/*  743 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/*  744 */       writeLock.lock();
/*      */       try {
/*  746 */         this.keyedPool.clear();
/*      */       } finally {
/*  748 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear(K key) throws Exception {
/*  757 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/*  758 */       writeLock.lock();
/*      */       try {
/*  760 */         this.keyedPool.clear(key);
/*      */       } finally {
/*  762 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() {
/*  771 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/*  772 */       writeLock.lock();
/*      */       try {
/*  774 */         this.keyedPool.close();
/*  775 */       } catch (Exception exception) {
/*      */       
/*      */       } finally {
/*  778 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public List<K> getKeys() {
/*  787 */       ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
/*  788 */       readLock.lock();
/*      */       try {
/*  790 */         return this.keyedPool.getKeys();
/*      */       } finally {
/*  792 */         readLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumActive() {
/*  801 */       ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
/*  802 */       readLock.lock();
/*      */       try {
/*  804 */         return this.keyedPool.getNumActive();
/*      */       } finally {
/*  806 */         readLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumActive(K key) {
/*  815 */       ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
/*  816 */       readLock.lock();
/*      */       try {
/*  818 */         return this.keyedPool.getNumActive(key);
/*      */       } finally {
/*  820 */         readLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumIdle() {
/*  829 */       ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
/*  830 */       readLock.lock();
/*      */       try {
/*  832 */         return this.keyedPool.getNumIdle();
/*      */       } finally {
/*  834 */         readLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumIdle(K key) {
/*  843 */       ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
/*  844 */       readLock.lock();
/*      */       try {
/*  846 */         return this.keyedPool.getNumIdle(key);
/*      */       } finally {
/*  848 */         readLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void invalidateObject(K key, V obj) {
/*  857 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/*  858 */       writeLock.lock();
/*      */       try {
/*  860 */         this.keyedPool.invalidateObject(key, obj);
/*  861 */       } catch (Exception exception) {
/*      */       
/*      */       } finally {
/*  864 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void returnObject(K key, V obj) {
/*  873 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/*  874 */       writeLock.lock();
/*      */       try {
/*  876 */         this.keyedPool.returnObject(key, obj);
/*  877 */       } catch (Exception exception) {
/*      */       
/*      */       } finally {
/*  880 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  889 */       StringBuilder sb = new StringBuilder();
/*  890 */       sb.append("SynchronizedKeyedObjectPool");
/*  891 */       sb.append("{keyedPool=").append(this.keyedPool);
/*  892 */       sb.append('}');
/*  893 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SynchronizedKeyedPooledObjectFactory<K, V>
/*      */     implements KeyedPooledObjectFactory<K, V>
/*      */   {
/*  913 */     private final ReentrantReadWriteLock.WriteLock writeLock = (new ReentrantReadWriteLock()).writeLock();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final KeyedPooledObjectFactory<K, V> keyedFactory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SynchronizedKeyedPooledObjectFactory(KeyedPooledObjectFactory<K, V> keyedFactory) throws IllegalArgumentException {
/*  928 */       if (keyedFactory == null) {
/*  929 */         throw new IllegalArgumentException("keyedFactory must not be null.");
/*      */       }
/*      */       
/*  932 */       this.keyedFactory = keyedFactory;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void activateObject(K key, PooledObject<V> p) throws Exception {
/*  940 */       this.writeLock.lock();
/*      */       try {
/*  942 */         this.keyedFactory.activateObject(key, p);
/*      */       } finally {
/*  944 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void destroyObject(K key, PooledObject<V> p) throws Exception {
/*  953 */       this.writeLock.lock();
/*      */       try {
/*  955 */         this.keyedFactory.destroyObject(key, p);
/*      */       } finally {
/*  957 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PooledObject<V> makeObject(K key) throws Exception {
/*  966 */       this.writeLock.lock();
/*      */       try {
/*  968 */         return this.keyedFactory.makeObject(key);
/*      */       } finally {
/*  970 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void passivateObject(K key, PooledObject<V> p) throws Exception {
/*  979 */       this.writeLock.lock();
/*      */       try {
/*  981 */         this.keyedFactory.passivateObject(key, p);
/*      */       } finally {
/*  983 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/*  992 */       StringBuilder sb = new StringBuilder();
/*  993 */       sb.append("SynchronizedKeyedPooledObjectFactory");
/*  994 */       sb.append("{keyedFactory=").append(this.keyedFactory);
/*  995 */       sb.append('}');
/*  996 */       return sb.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean validateObject(K key, PooledObject<V> p) {
/* 1004 */       this.writeLock.lock();
/*      */       try {
/* 1006 */         return this.keyedFactory.validateObject(key, p);
/*      */       } finally {
/* 1008 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SynchronizedObjectPool<T>
/*      */     implements ObjectPool<T>
/*      */   {
/* 1034 */     private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final ObjectPool<T> pool;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SynchronizedObjectPool(ObjectPool<T> pool) throws IllegalArgumentException {
/* 1050 */       if (pool == null) {
/* 1051 */         throw new IllegalArgumentException("pool must not be null.");
/*      */       }
/* 1053 */       this.pool = pool;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void addObject() throws Exception {
/* 1061 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/* 1062 */       writeLock.lock();
/*      */       try {
/* 1064 */         this.pool.addObject();
/*      */       } finally {
/* 1066 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public T borrowObject() throws Exception {
/* 1075 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/* 1076 */       writeLock.lock();
/*      */       try {
/* 1078 */         return this.pool.borrowObject();
/*      */       } finally {
/* 1080 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void clear() throws Exception {
/* 1089 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/* 1090 */       writeLock.lock();
/*      */       try {
/* 1092 */         this.pool.clear();
/*      */       } finally {
/* 1094 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() {
/* 1103 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/* 1104 */       writeLock.lock();
/*      */       try {
/* 1106 */         this.pool.close();
/* 1107 */       } catch (Exception exception) {
/*      */       
/*      */       } finally {
/* 1110 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumActive() {
/* 1119 */       ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
/* 1120 */       readLock.lock();
/*      */       try {
/* 1122 */         return this.pool.getNumActive();
/*      */       } finally {
/* 1124 */         readLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getNumIdle() {
/* 1133 */       ReentrantReadWriteLock.ReadLock readLock = this.readWriteLock.readLock();
/* 1134 */       readLock.lock();
/*      */       try {
/* 1136 */         return this.pool.getNumIdle();
/*      */       } finally {
/* 1138 */         readLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void invalidateObject(T obj) {
/* 1147 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/* 1148 */       writeLock.lock();
/*      */       try {
/* 1150 */         this.pool.invalidateObject(obj);
/* 1151 */       } catch (Exception exception) {
/*      */       
/*      */       } finally {
/* 1154 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void returnObject(T obj) {
/* 1163 */       ReentrantReadWriteLock.WriteLock writeLock = this.readWriteLock.writeLock();
/* 1164 */       writeLock.lock();
/*      */       try {
/* 1166 */         this.pool.returnObject(obj);
/* 1167 */       } catch (Exception exception) {
/*      */       
/*      */       } finally {
/* 1170 */         writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1179 */       StringBuilder sb = new StringBuilder();
/* 1180 */       sb.append("SynchronizedObjectPool");
/* 1181 */       sb.append("{pool=").append(this.pool);
/* 1182 */       sb.append('}');
/* 1183 */       return sb.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class SynchronizedPooledObjectFactory<T>
/*      */     implements PooledObjectFactory<T>
/*      */   {
/* 1203 */     private final ReentrantReadWriteLock.WriteLock writeLock = (new ReentrantReadWriteLock()).writeLock();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final PooledObjectFactory<T> factory;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     SynchronizedPooledObjectFactory(PooledObjectFactory<T> factory) throws IllegalArgumentException {
/* 1219 */       if (factory == null) {
/* 1220 */         throw new IllegalArgumentException("factory must not be null.");
/*      */       }
/* 1222 */       this.factory = factory;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void activateObject(PooledObject<T> p) throws Exception {
/* 1230 */       this.writeLock.lock();
/*      */       try {
/* 1232 */         this.factory.activateObject(p);
/*      */       } finally {
/* 1234 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void destroyObject(PooledObject<T> p) throws Exception {
/* 1243 */       this.writeLock.lock();
/*      */       try {
/* 1245 */         this.factory.destroyObject(p);
/*      */       } finally {
/* 1247 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PooledObject<T> makeObject() throws Exception {
/* 1256 */       this.writeLock.lock();
/*      */       try {
/* 1258 */         return this.factory.makeObject();
/*      */       } finally {
/* 1260 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void passivateObject(PooledObject<T> p) throws Exception {
/* 1269 */       this.writeLock.lock();
/*      */       try {
/* 1271 */         this.factory.passivateObject(p);
/*      */       } finally {
/* 1273 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1282 */       StringBuilder sb = new StringBuilder();
/* 1283 */       sb.append("SynchronizedPoolableObjectFactory");
/* 1284 */       sb.append("{factory=").append(this.factory);
/* 1285 */       sb.append('}');
/* 1286 */       return sb.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean validateObject(PooledObject<T> p) {
/* 1294 */       this.writeLock.lock();
/*      */       try {
/* 1296 */         return this.factory.validateObject(p);
/*      */       } finally {
/* 1298 */         this.writeLock.unlock();
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TimerHolder
/*      */   {
/* 1308 */     static final Timer MIN_IDLE_TIMER = new Timer(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> Map<K, TimerTask> checkMinIdle(KeyedObjectPool<K, V> keyedPool, Collection<K> keys, int minIdle, long periodMillis) throws IllegalArgumentException {
/* 1354 */     if (keys == null) {
/* 1355 */       throw new IllegalArgumentException("keys must not be null.");
/*      */     }
/* 1357 */     Map<K, TimerTask> tasks = new HashMap<>(keys.size());
/* 1358 */     Iterator<K> iter = keys.iterator();
/* 1359 */     while (iter.hasNext()) {
/* 1360 */       K key = iter.next();
/* 1361 */       TimerTask task = checkMinIdle(keyedPool, key, minIdle, periodMillis);
/* 1362 */       tasks.put(key, task);
/*      */     } 
/* 1364 */     return tasks;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> TimerTask checkMinIdle(KeyedObjectPool<K, V> keyedPool, K key, int minIdle, long periodMillis) throws IllegalArgumentException {
/* 1397 */     if (keyedPool == null) {
/* 1398 */       throw new IllegalArgumentException("keyedPool must not be null.");
/*      */     }
/* 1400 */     if (key == null) {
/* 1401 */       throw new IllegalArgumentException("key must not be null.");
/*      */     }
/* 1403 */     if (minIdle < 0) {
/* 1404 */       throw new IllegalArgumentException("minIdle must be non-negative.");
/*      */     }
/* 1406 */     TimerTask<K, V> task = new KeyedObjectPoolMinIdleTimerTask<>(keyedPool, key, minIdle);
/*      */     
/* 1408 */     getMinIdleTimer().schedule(task, 0L, periodMillis);
/* 1409 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> TimerTask checkMinIdle(ObjectPool<T> pool, int minIdle, long periodMillis) throws IllegalArgumentException {
/* 1437 */     if (pool == null) {
/* 1438 */       throw new IllegalArgumentException("keyedPool must not be null.");
/*      */     }
/* 1440 */     if (minIdle < 0) {
/* 1441 */       throw new IllegalArgumentException("minIdle must be non-negative.");
/*      */     }
/* 1443 */     TimerTask<T> task = new ObjectPoolMinIdleTimerTask<>(pool, minIdle);
/* 1444 */     getMinIdleTimer().schedule(task, 0L, periodMillis);
/* 1445 */     return task;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void checkRethrow(Throwable t) {
/* 1462 */     if (t instanceof ThreadDeath) {
/* 1463 */       throw (ThreadDeath)t;
/*      */     }
/* 1465 */     if (t instanceof VirtualMachineError) {
/* 1466 */       throw (VirtualMachineError)t;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> KeyedObjectPool<K, V> erodingPool(KeyedObjectPool<K, V> keyedPool) {
/* 1492 */     return erodingPool(keyedPool, 1.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> KeyedObjectPool<K, V> erodingPool(KeyedObjectPool<K, V> keyedPool, float factor) {
/* 1527 */     return erodingPool(keyedPool, factor, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> KeyedObjectPool<K, V> erodingPool(KeyedObjectPool<K, V> keyedPool, float factor, boolean perKey) {
/* 1573 */     if (keyedPool == null) {
/* 1574 */       throw new IllegalArgumentException("keyedPool must not be null.");
/*      */     }
/* 1576 */     if (factor <= 0.0F) {
/* 1577 */       throw new IllegalArgumentException("factor must be positive.");
/*      */     }
/* 1579 */     if (perKey) {
/* 1580 */       return new ErodingPerKeyKeyedObjectPool<>(keyedPool, factor);
/*      */     }
/* 1582 */     return new ErodingKeyedObjectPool<>(keyedPool, factor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ObjectPool<T> erodingPool(ObjectPool<T> pool) {
/* 1604 */     return erodingPool(pool, 1.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ObjectPool<T> erodingPool(ObjectPool<T> pool, float factor) {
/* 1638 */     if (pool == null) {
/* 1639 */       throw new IllegalArgumentException("pool must not be null.");
/*      */     }
/* 1641 */     if (factor <= 0.0F) {
/* 1642 */       throw new IllegalArgumentException("factor must be positive.");
/*      */     }
/* 1644 */     return new ErodingObjectPool<>(pool, factor);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Timer getMinIdleTimer() {
/* 1653 */     return TimerHolder.MIN_IDLE_TIMER;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> void prefill(KeyedObjectPool<K, V> keyedPool, Collection<K> keys, int count) throws Exception, IllegalArgumentException {
/* 1683 */     if (keys == null) {
/* 1684 */       throw new IllegalArgumentException("keys must not be null.");
/*      */     }
/* 1686 */     keyedPool.addObjects(keys, count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <K, V> void prefill(KeyedObjectPool<K, V> keyedPool, K key, int count) throws Exception, IllegalArgumentException {
/* 1712 */     if (keyedPool == null) {
/* 1713 */       throw new IllegalArgumentException("keyedPool must not be null.");
/*      */     }
/* 1715 */     keyedPool.addObjects(key, count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> void prefill(ObjectPool<T> pool, int count) throws Exception {
/* 1737 */     if (pool == null) {
/* 1738 */       throw new IllegalArgumentException("pool must not be null.");
/*      */     }
/* 1740 */     pool.addObjects(count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> KeyedPooledObjectFactory<K, V> synchronizedKeyedPooledFactory(KeyedPooledObjectFactory<K, V> keyedFactory) {
/* 1756 */     return new SynchronizedKeyedPooledObjectFactory<>(keyedFactory);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <K, V> KeyedObjectPool<K, V> synchronizedPool(KeyedObjectPool<K, V> keyedPool) {
/* 1789 */     return new SynchronizedKeyedObjectPool<>(keyedPool);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> ObjectPool<T> synchronizedPool(ObjectPool<T> pool) {
/* 1812 */     if (pool == null) {
/* 1813 */       throw new IllegalArgumentException("pool must not be null.");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1827 */     return new SynchronizedObjectPool<>(pool);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> PooledObjectFactory<T> synchronizedPooledFactory(PooledObjectFactory<T> factory) {
/* 1841 */     return new SynchronizedPooledObjectFactory<>(factory);
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\PoolUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */