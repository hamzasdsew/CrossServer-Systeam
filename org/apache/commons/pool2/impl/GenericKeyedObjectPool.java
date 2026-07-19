/*      */ package org.apache.commons.pool2.impl;
/*      */ 
/*      */ import java.time.Duration;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Deque;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Objects;
/*      */ import java.util.TreeMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReadWriteLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.commons.pool2.DestroyMode;
/*      */ import org.apache.commons.pool2.KeyedObjectPool;
/*      */ import org.apache.commons.pool2.KeyedPooledObjectFactory;
/*      */ import org.apache.commons.pool2.PoolUtils;
/*      */ import org.apache.commons.pool2.PooledObject;
/*      */ import org.apache.commons.pool2.PooledObjectState;
/*      */ import org.apache.commons.pool2.UsageTracking;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class GenericKeyedObjectPool<K, T>
/*      */   extends BaseGenericObjectPool<T>
/*      */   implements KeyedObjectPool<K, T>, GenericKeyedObjectPoolMXBean<K>, UsageTracking<T>
/*      */ {
/*      */   private static class ObjectDeque<S>
/*      */   {
/*      */     private final LinkedBlockingDeque<PooledObject<S>> idleObjects;
/*  108 */     private final AtomicInteger createCount = new AtomicInteger(0);
/*      */     
/*      */     private long makeObjectCount;
/*  111 */     private final Object makeObjectCountLock = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  117 */     private final Map<BaseGenericObjectPool.IdentityWrapper<S>, PooledObject<S>> allObjects = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  126 */     private final AtomicLong numInterested = new AtomicLong();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ObjectDeque(boolean fairness) {
/*  134 */       this.idleObjects = new LinkedBlockingDeque<>(fairness);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Map<BaseGenericObjectPool.IdentityWrapper<S>, PooledObject<S>> getAllObjects() {
/*  143 */       return this.allObjects;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public AtomicInteger getCreateCount() {
/*  153 */       return this.createCount;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public LinkedBlockingDeque<PooledObject<S>> getIdleObjects() {
/*  162 */       return this.idleObjects;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public AtomicLong getNumInterested() {
/*  171 */       return this.numInterested;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  176 */       StringBuilder builder = new StringBuilder();
/*  177 */       builder.append("ObjectDeque [idleObjects=");
/*  178 */       builder.append(this.idleObjects);
/*  179 */       builder.append(", createCount=");
/*  180 */       builder.append(this.createCount);
/*  181 */       builder.append(", allObjects=");
/*  182 */       builder.append(this.allObjects);
/*  183 */       builder.append(", numInterested=");
/*  184 */       builder.append(this.numInterested);
/*  185 */       builder.append("]");
/*  186 */       return builder.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  191 */   private static final Integer ZERO = Integer.valueOf(0);
/*      */ 
/*      */   
/*      */   private static final String ONAME_BASE = "org.apache.commons.pool2:type=GenericKeyedObjectPool,name=";
/*      */ 
/*      */   
/*  197 */   private volatile int maxIdlePerKey = 8;
/*      */ 
/*      */   
/*  200 */   private volatile int minIdlePerKey = 0;
/*      */ 
/*      */ 
/*      */   
/*  204 */   private volatile int maxTotalPerKey = 8;
/*      */ 
/*      */ 
/*      */   
/*      */   private final KeyedPooledObjectFactory<K, T> factory;
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean fairness;
/*      */ 
/*      */ 
/*      */   
/*  216 */   private final Map<K, ObjectDeque<T>> poolMap = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  225 */   private final ArrayList<K> poolKeyList = new ArrayList<>();
/*      */   
/*  227 */   private final ReadWriteLock keyLock = new ReentrantReadWriteLock(true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  235 */   private final AtomicInteger numTotal = new AtomicInteger(0);
/*      */ 
/*      */ 
/*      */   
/*      */   private Iterator<K> evictionKeyIterator;
/*      */ 
/*      */ 
/*      */   
/*      */   private K evictionKey;
/*      */ 
/*      */ 
/*      */   
/*      */   public GenericKeyedObjectPool(KeyedPooledObjectFactory<K, T> factory) {
/*  248 */     this(factory, new GenericKeyedObjectPoolConfig<>());
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
/*      */   public GenericKeyedObjectPool(KeyedPooledObjectFactory<K, T> factory, GenericKeyedObjectPoolConfig<T> config) {
/*  264 */     super(config, "org.apache.commons.pool2:type=GenericKeyedObjectPool,name=", config.getJmxNamePrefix());
/*      */     
/*  266 */     if (factory == null) {
/*  267 */       jmxUnregister();
/*  268 */       throw new IllegalArgumentException("Factory may not be null");
/*      */     } 
/*  270 */     this.factory = factory;
/*  271 */     this.fairness = config.getFairness();
/*      */     
/*  273 */     setConfig(config);
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
/*      */   public GenericKeyedObjectPool(KeyedPooledObjectFactory<K, T> factory, GenericKeyedObjectPoolConfig<T> config, AbandonedConfig abandonedConfig) {
/*  292 */     this(factory, config);
/*  293 */     setAbandonedConfig(abandonedConfig);
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
/*      */   private void addIdleObject(K key, PooledObject<T> p) throws Exception {
/*  306 */     if (!PooledObject.isNull(p)) {
/*  307 */       this.factory.passivateObject(key, p);
/*  308 */       LinkedBlockingDeque<PooledObject<T>> idleObjects = ((ObjectDeque<T>)this.poolMap.get(key)).getIdleObjects();
/*  309 */       if (getLifo()) {
/*  310 */         idleObjects.addFirst(p);
/*      */       } else {
/*  312 */         idleObjects.addLast(p);
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
/*      */ 
/*      */   
/*      */   public void addObject(K key) throws Exception {
/*  338 */     assertOpen();
/*  339 */     register(key);
/*      */     try {
/*  341 */       addIdleObject(key, create(key));
/*      */     } finally {
/*  343 */       deregister(key);
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
/*      */   public T borrowObject(K key) throws Exception {
/*  355 */     return borrowObject(key, getMaxWaitDuration().toMillis());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public T borrowObject(K key, long borrowMaxWaitMillis) throws Exception {
/*  421 */     assertOpen();
/*      */     
/*  423 */     AbandonedConfig ac = this.abandonedConfig;
/*  424 */     if (ac != null && ac.getRemoveAbandonedOnBorrow() && getNumIdle() < 2 && 
/*  425 */       getNumActive() > getMaxTotal() - 3) {
/*  426 */       removeAbandoned(ac);
/*      */     }
/*      */     
/*  429 */     PooledObject<T> p = null;
/*      */ 
/*      */ 
/*      */     
/*  433 */     boolean blockWhenExhausted = getBlockWhenExhausted();
/*      */ 
/*      */     
/*  436 */     Instant waitTime = Instant.now();
/*  437 */     ObjectDeque<T> objectDeque = register(key);
/*      */     
/*      */     try {
/*  440 */       while (p == null) {
/*  441 */         boolean create = false;
/*  442 */         p = objectDeque.getIdleObjects().pollFirst();
/*  443 */         if (p == null) {
/*  444 */           p = create(key);
/*  445 */           if (!PooledObject.isNull(p)) {
/*  446 */             create = true;
/*      */           }
/*      */         } 
/*  449 */         if (blockWhenExhausted) {
/*  450 */           if (PooledObject.isNull(p))
/*      */           {
/*  452 */             p = (borrowMaxWaitMillis < 0L) ? objectDeque.getIdleObjects().takeFirst() : objectDeque.getIdleObjects().pollFirst(borrowMaxWaitMillis, TimeUnit.MILLISECONDS);
/*      */           }
/*  454 */           if (PooledObject.isNull(p)) {
/*  455 */             throw new NoSuchElementException(appendStats("Timeout waiting for idle object, borrowMaxWaitMillis=" + borrowMaxWaitMillis));
/*      */           }
/*      */         }
/*  458 */         else if (PooledObject.isNull(p)) {
/*  459 */           throw new NoSuchElementException(appendStats("Pool exhausted"));
/*      */         } 
/*  461 */         if (!p.allocate()) {
/*  462 */           p = null;
/*      */         }
/*      */         
/*  465 */         if (!PooledObject.isNull(p)) {
/*      */           try {
/*  467 */             this.factory.activateObject(key, p);
/*  468 */           } catch (Exception e) {
/*      */             try {
/*  470 */               destroy(key, p, true, DestroyMode.NORMAL);
/*  471 */             } catch (Exception exception) {}
/*      */ 
/*      */             
/*  474 */             p = null;
/*  475 */             if (create) {
/*  476 */               NoSuchElementException nsee = new NoSuchElementException(appendStats("Unable to activate object"));
/*  477 */               nsee.initCause(e);
/*  478 */               throw nsee;
/*      */             } 
/*      */           } 
/*  481 */           if (!PooledObject.isNull(p) && getTestOnBorrow()) {
/*  482 */             boolean validate = false;
/*  483 */             Throwable validationThrowable = null;
/*      */             try {
/*  485 */               validate = this.factory.validateObject(key, p);
/*  486 */             } catch (Throwable t) {
/*  487 */               PoolUtils.checkRethrow(t);
/*  488 */               validationThrowable = t;
/*      */             } 
/*  490 */             if (!validate) {
/*      */               try {
/*  492 */                 destroy(key, p, true, DestroyMode.NORMAL);
/*  493 */                 this.destroyedByBorrowValidationCount.incrementAndGet();
/*  494 */               } catch (Exception exception) {}
/*      */ 
/*      */               
/*  497 */               p = null;
/*  498 */               if (create) {
/*      */                 
/*  500 */                 NoSuchElementException nsee = new NoSuchElementException(appendStats("Unable to validate object"));
/*  501 */                 nsee.initCause(validationThrowable);
/*  502 */                 throw nsee;
/*      */               } 
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  509 */       deregister(key);
/*      */     } 
/*      */     
/*  512 */     updateStatsBorrow(p, Duration.between(waitTime, Instant.now()));
/*      */     
/*  514 */     return (T)p.getObject();
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
/*      */   private int calculateDeficit(ObjectDeque<T> objectDeque) {
/*  528 */     if (objectDeque == null) {
/*  529 */       return getMinIdlePerKey();
/*      */     }
/*      */ 
/*      */     
/*  533 */     int maxTotal = getMaxTotal();
/*  534 */     int maxTotalPerKeySave = getMaxTotalPerKey();
/*      */ 
/*      */ 
/*      */     
/*  538 */     int objectDefecit = getMinIdlePerKey() - objectDeque.getIdleObjects().size();
/*  539 */     if (maxTotalPerKeySave > 0) {
/*  540 */       int growLimit = Math.max(0, maxTotalPerKeySave - objectDeque
/*  541 */           .getIdleObjects().size());
/*  542 */       objectDefecit = Math.min(objectDefecit, growLimit);
/*      */     } 
/*      */ 
/*      */     
/*  546 */     if (maxTotal > 0) {
/*  547 */       int growLimit = Math.max(0, maxTotal - getNumActive() - getNumIdle());
/*  548 */       objectDefecit = Math.min(objectDefecit, growLimit);
/*      */     } 
/*      */     
/*  551 */     return objectDefecit;
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
/*      */   public void clear() {
/*  574 */     this.poolMap.keySet().forEach(key -> clear((K)key, false));
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
/*      */   public void clear(K key) {
/*  592 */     clear(key, true);
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
/*      */   public void clear(K key, boolean reuseCapacity) {
/*  612 */     if (!this.poolMap.containsKey(key)) {
/*      */       return;
/*      */     }
/*  615 */     ObjectDeque<T> objectDeque = register(key);
/*  616 */     int freedCapacity = 0;
/*      */     try {
/*  618 */       LinkedBlockingDeque<PooledObject<T>> idleObjects = objectDeque.getIdleObjects();
/*  619 */       PooledObject<T> p = idleObjects.poll();
/*  620 */       while (p != null) {
/*      */         try {
/*  622 */           if (destroy(key, p, true, DestroyMode.NORMAL)) {
/*  623 */             freedCapacity++;
/*      */           }
/*  625 */         } catch (Exception e) {
/*  626 */           swallowException(e);
/*      */         } 
/*  628 */         p = idleObjects.poll();
/*      */       } 
/*      */     } finally {
/*  631 */       deregister(key);
/*      */     } 
/*  633 */     if (reuseCapacity) {
/*  634 */       reuseCapacity(freedCapacity);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearOldest() {
/*  645 */     TreeMap<PooledObject<T>, K> map = new TreeMap<>();
/*      */     
/*  647 */     this.poolMap.forEach((key, value) -> value.getIdleObjects().forEach(()));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  655 */     int itemsToRemove = (int)(map.size() * 0.15D) + 1;
/*  656 */     Iterator<Map.Entry<PooledObject<T>, K>> iter = map.entrySet().iterator();
/*      */     
/*  658 */     while (iter.hasNext() && itemsToRemove > 0) {
/*  659 */       Map.Entry<PooledObject<T>, K> entry = iter.next();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  664 */       K key = entry.getValue();
/*  665 */       PooledObject<T> p = entry.getKey();
/*      */       
/*  667 */       boolean destroyed = true;
/*      */       try {
/*  669 */         destroyed = destroy(key, p, false, DestroyMode.NORMAL);
/*  670 */       } catch (Exception e) {
/*  671 */         swallowException(e);
/*      */       } 
/*  673 */       if (destroyed) {
/*  674 */         itemsToRemove--;
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
/*      */   public void close() {
/*  692 */     if (isClosed()) {
/*      */       return;
/*      */     }
/*      */     
/*  696 */     synchronized (this.closeLock) {
/*  697 */       if (isClosed()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  703 */       stopEvictor();
/*      */       
/*  705 */       this.closed = true;
/*      */       
/*  707 */       clear();
/*      */       
/*  709 */       jmxUnregister();
/*      */ 
/*      */       
/*  712 */       this.poolMap.values().forEach(e -> e.getIdleObjects().interuptTakeWaiters());
/*      */ 
/*      */       
/*  715 */       clear();
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
/*      */   private PooledObject<T> create(K key) throws Exception {
/*  730 */     int maxTotalPerKeySave = getMaxTotalPerKey();
/*  731 */     if (maxTotalPerKeySave < 0) {
/*  732 */       maxTotalPerKeySave = Integer.MAX_VALUE;
/*      */     }
/*  734 */     int maxTotal = getMaxTotal();
/*      */     
/*  736 */     ObjectDeque<T> objectDeque = this.poolMap.get(key);
/*      */ 
/*      */     
/*  739 */     boolean loop = true;
/*      */     
/*  741 */     while (loop) {
/*  742 */       int newNumTotal = this.numTotal.incrementAndGet();
/*  743 */       if (maxTotal > -1 && newNumTotal > maxTotal) {
/*  744 */         this.numTotal.decrementAndGet();
/*  745 */         if (getNumIdle() == 0) {
/*  746 */           return null;
/*      */         }
/*  748 */         clearOldest(); continue;
/*      */       } 
/*  750 */       loop = false;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  759 */     Boolean create = null;
/*  760 */     while (create == null) {
/*  761 */       synchronized (objectDeque.makeObjectCountLock) {
/*  762 */         long newCreateCount = objectDeque.getCreateCount().incrementAndGet();
/*      */         
/*  764 */         if (newCreateCount > maxTotalPerKeySave) {
/*      */ 
/*      */           
/*  767 */           objectDeque.getCreateCount().decrementAndGet();
/*  768 */           if (objectDeque.makeObjectCount == 0L) {
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  773 */             create = Boolean.FALSE;
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/*  779 */             objectDeque.makeObjectCountLock.wait();
/*      */           } 
/*      */         } else {
/*      */           
/*  783 */           objectDeque.makeObjectCount++;
/*  784 */           create = Boolean.TRUE;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  789 */     if (!create.booleanValue()) {
/*  790 */       this.numTotal.decrementAndGet();
/*  791 */       return null;
/*      */     } 
/*      */     
/*  794 */     PooledObject<T> p = null;
/*      */     try {
/*  796 */       p = this.factory.makeObject(key);
/*  797 */       if (PooledObject.isNull(p)) {
/*  798 */         this.numTotal.decrementAndGet();
/*  799 */         objectDeque.getCreateCount().decrementAndGet();
/*  800 */         throw new NullPointerException(String.format("%s.makeObject() = null", new Object[] { this.factory.getClass().getSimpleName() }));
/*      */       } 
/*  802 */       if (getTestOnCreate() && !this.factory.validateObject(key, p)) {
/*  803 */         this.numTotal.decrementAndGet();
/*  804 */         objectDeque.getCreateCount().decrementAndGet();
/*  805 */         return null;
/*      */       } 
/*  807 */     } catch (Exception e) {
/*  808 */       this.numTotal.decrementAndGet();
/*  809 */       objectDeque.getCreateCount().decrementAndGet();
/*  810 */       throw e;
/*      */     } finally {
/*  812 */       synchronized (objectDeque.makeObjectCountLock) {
/*  813 */         objectDeque.makeObjectCount--;
/*  814 */         objectDeque.makeObjectCountLock.notifyAll();
/*      */       } 
/*      */     } 
/*      */     
/*  818 */     AbandonedConfig ac = this.abandonedConfig;
/*  819 */     if (ac != null && ac.getLogAbandoned()) {
/*  820 */       p.setLogAbandoned(true);
/*  821 */       p.setRequireFullStackTrace(ac.getRequireFullStackTrace());
/*      */     } 
/*      */     
/*  824 */     this.createdCount.incrementAndGet();
/*  825 */     objectDeque.getAllObjects().put(new BaseGenericObjectPool.IdentityWrapper(p.getObject()), p);
/*  826 */     return p;
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
/*      */   private void deregister(K k) {
/*  838 */     Lock lock = this.keyLock.readLock();
/*      */     try {
/*  840 */       lock.lock();
/*  841 */       ObjectDeque<T> objectDeque = this.poolMap.get(k);
/*  842 */       if (objectDeque == null) {
/*  843 */         throw new IllegalStateException("Attempt to de-register a key for a non-existent pool");
/*      */       }
/*  845 */       long numInterested = objectDeque.getNumInterested().decrementAndGet();
/*  846 */       if (numInterested < 0L) {
/*  847 */         throw new IllegalStateException("numInterested count for key " + k + " is less than zero");
/*      */       }
/*  849 */       if (numInterested == 0L && objectDeque.getCreateCount().get() == 0) {
/*      */ 
/*      */         
/*  852 */         lock.unlock();
/*  853 */         lock = this.keyLock.writeLock();
/*  854 */         lock.lock();
/*      */ 
/*      */         
/*  857 */         objectDeque = this.poolMap.get(k);
/*  858 */         if (null != objectDeque && objectDeque.getNumInterested().get() == 0L && objectDeque.getCreateCount().get() == 0) {
/*      */ 
/*      */ 
/*      */           
/*  862 */           this.poolMap.remove(k);
/*  863 */           this.poolKeyList.remove(k);
/*      */         } 
/*      */       } 
/*      */     } finally {
/*  867 */       lock.unlock();
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
/*      */   private boolean destroy(K key, PooledObject<T> toDestroy, boolean always, DestroyMode destroyMode) throws Exception {
/*  885 */     ObjectDeque<T> objectDeque = register(key);
/*      */     
/*      */     try {
/*      */       boolean isIdle;
/*  889 */       synchronized (toDestroy) {
/*      */         
/*  891 */         isIdle = toDestroy.getState().equals(PooledObjectState.IDLE);
/*      */ 
/*      */         
/*  894 */         if (isIdle || always) {
/*  895 */           isIdle = objectDeque.getIdleObjects().remove(toDestroy);
/*      */         }
/*      */       } 
/*  898 */       if (isIdle || always) {
/*  899 */         objectDeque.getAllObjects().remove(new BaseGenericObjectPool.IdentityWrapper(toDestroy.getObject()));
/*  900 */         toDestroy.invalidate();
/*      */         
/*      */         try {
/*  903 */           this.factory.destroyObject(key, toDestroy, destroyMode);
/*      */         } finally {
/*  905 */           objectDeque.getCreateCount().decrementAndGet();
/*  906 */           this.destroyedCount.incrementAndGet();
/*  907 */           this.numTotal.decrementAndGet();
/*      */         } 
/*  909 */         return true;
/*      */       } 
/*  911 */       return false;
/*      */     } finally {
/*  913 */       deregister(key);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void ensureMinIdle() throws Exception {
/*  920 */     int minIdlePerKeySave = getMinIdlePerKey();
/*  921 */     if (minIdlePerKeySave < 1) {
/*      */       return;
/*      */     }
/*      */     
/*  925 */     for (K k : this.poolMap.keySet()) {
/*  926 */       ensureMinIdle(k);
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
/*      */   private void ensureMinIdle(K key) throws Exception {
/*  948 */     ObjectDeque<T> objectDeque = this.poolMap.get(key);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  958 */     int deficit = calculateDeficit(objectDeque);
/*      */     
/*  960 */     for (int i = 0; i < deficit && calculateDeficit(objectDeque) > 0; i++) {
/*  961 */       addObject(key);
/*      */ 
/*      */ 
/*      */       
/*  965 */       if (objectDeque == null) {
/*  966 */         objectDeque = this.poolMap.get(key);
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
/*      */   public void evict() throws Exception {
/*  981 */     assertOpen();
/*      */     
/*  983 */     if (getNumIdle() > 0) {
/*      */       
/*  985 */       PooledObject<T> underTest = null;
/*  986 */       EvictionPolicy<T> evictionPolicy = getEvictionPolicy();
/*      */       
/*  988 */       synchronized (this.evictionLock) {
/*      */ 
/*      */ 
/*      */         
/*  992 */         EvictionConfig evictionConfig = new EvictionConfig(getMinEvictableIdleDuration(), getSoftMinEvictableIdleDuration(), getMinIdlePerKey());
/*      */         
/*  994 */         boolean testWhileIdle = getTestWhileIdle();
/*      */         
/*  996 */         for (int i = 0, m = getNumTests(); i < m; i++) {
/*  997 */           Deque<PooledObject<T>> idleObjects; if (this.evictionIterator == null || !this.evictionIterator.hasNext()) {
/*  998 */             if (this.evictionKeyIterator == null || 
/*  999 */               !this.evictionKeyIterator.hasNext()) {
/* 1000 */               List<K> keyCopy = new ArrayList<>();
/* 1001 */               Lock readLock = this.keyLock.readLock();
/* 1002 */               readLock.lock();
/*      */               try {
/* 1004 */                 keyCopy.addAll(this.poolKeyList);
/*      */               } finally {
/* 1006 */                 readLock.unlock();
/*      */               } 
/* 1008 */               this.evictionKeyIterator = keyCopy.iterator();
/*      */             } 
/* 1010 */             while (this.evictionKeyIterator.hasNext()) {
/* 1011 */               this.evictionKey = this.evictionKeyIterator.next();
/* 1012 */               ObjectDeque<T> objectDeque = this.poolMap.get(this.evictionKey);
/* 1013 */               if (objectDeque == null) {
/*      */                 continue;
/*      */               }
/*      */               
/* 1017 */               Deque<PooledObject<T>> deque = objectDeque.getIdleObjects();
/* 1018 */               this.evictionIterator = new BaseGenericObjectPool.EvictionIterator(this, deque);
/* 1019 */               if (this.evictionIterator.hasNext()) {
/*      */                 break;
/*      */               }
/* 1022 */               this.evictionIterator = null;
/*      */             } 
/*      */           } 
/* 1025 */           if (this.evictionIterator == null) {
/*      */             return;
/*      */           }
/*      */ 
/*      */           
/*      */           try {
/* 1031 */             underTest = this.evictionIterator.next();
/* 1032 */             idleObjects = this.evictionIterator.getIdleObjects();
/* 1033 */           } catch (NoSuchElementException nsee) {
/*      */ 
/*      */             
/* 1036 */             i--;
/* 1037 */             this.evictionIterator = null;
/*      */           } 
/*      */ 
/*      */           
/* 1041 */           if (!underTest.startEvictionTest()) {
/*      */ 
/*      */             
/* 1044 */             i--;
/*      */           } else {
/*      */             boolean evict;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/* 1053 */               evict = evictionPolicy.evict(evictionConfig, underTest, ((ObjectDeque)this.poolMap
/* 1054 */                   .get(this.evictionKey)).getIdleObjects().size());
/* 1055 */             } catch (Throwable t) {
/*      */ 
/*      */               
/* 1058 */               PoolUtils.checkRethrow(t);
/* 1059 */               swallowException(new Exception(t));
/*      */               
/* 1061 */               evict = false;
/*      */             } 
/*      */             
/* 1064 */             if (evict) {
/* 1065 */               destroy(this.evictionKey, underTest, true, DestroyMode.NORMAL);
/* 1066 */               this.destroyedByEvictorCount.incrementAndGet();
/*      */             } else {
/* 1068 */               if (testWhileIdle) {
/* 1069 */                 boolean active = false;
/*      */                 try {
/* 1071 */                   this.factory.activateObject(this.evictionKey, underTest);
/* 1072 */                   active = true;
/* 1073 */                 } catch (Exception e) {
/* 1074 */                   destroy(this.evictionKey, underTest, true, DestroyMode.NORMAL);
/* 1075 */                   this.destroyedByEvictorCount.incrementAndGet();
/*      */                 } 
/* 1077 */                 if (active) {
/* 1078 */                   boolean validate = false;
/* 1079 */                   Throwable validationThrowable = null;
/*      */                   try {
/* 1081 */                     validate = this.factory.validateObject(this.evictionKey, underTest);
/* 1082 */                   } catch (Throwable t) {
/* 1083 */                     PoolUtils.checkRethrow(t);
/* 1084 */                     validationThrowable = t;
/*      */                   } 
/* 1086 */                   if (!validate) {
/* 1087 */                     destroy(this.evictionKey, underTest, true, DestroyMode.NORMAL);
/* 1088 */                     this.destroyedByEvictorCount.incrementAndGet();
/* 1089 */                     if (validationThrowable != null) {
/* 1090 */                       if (validationThrowable instanceof RuntimeException) {
/* 1091 */                         throw (RuntimeException)validationThrowable;
/*      */                       }
/* 1093 */                       throw (Error)validationThrowable;
/*      */                     } 
/*      */                   } else {
/*      */                     try {
/* 1097 */                       this.factory.passivateObject(this.evictionKey, underTest);
/* 1098 */                     } catch (Exception e) {
/* 1099 */                       destroy(this.evictionKey, underTest, true, DestroyMode.NORMAL);
/* 1100 */                       this.destroyedByEvictorCount.incrementAndGet();
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/* 1105 */               underTest.endEvictionTest(idleObjects);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1112 */     AbandonedConfig ac = this.abandonedConfig;
/* 1113 */     if (ac != null && ac.getRemoveAbandonedOnMaintenance()) {
/* 1114 */       removeAbandoned(ac);
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
/*      */   public KeyedPooledObjectFactory<K, T> getFactory() {
/* 1126 */     return this.factory;
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
/*      */   public List<K> getKeys() {
/* 1138 */     return (List<K>)this.poolKeyList.clone();
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
/*      */   public int getMaxIdlePerKey() {
/* 1158 */     return this.maxIdlePerKey;
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
/*      */   public int getMaxTotalPerKey() {
/* 1172 */     return this.maxTotalPerKey;
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
/*      */   public int getMinIdlePerKey() {
/* 1194 */     int maxIdlePerKeySave = getMaxIdlePerKey();
/* 1195 */     return Math.min(this.minIdlePerKey, maxIdlePerKeySave);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumActive() {
/* 1200 */     return this.numTotal.get() - getNumIdle();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumActive(K key) {
/* 1205 */     ObjectDeque<T> objectDeque = this.poolMap.get(key);
/* 1206 */     if (objectDeque != null) {
/* 1207 */       return objectDeque.getAllObjects().size() - objectDeque
/* 1208 */         .getIdleObjects().size();
/*      */     }
/* 1210 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Integer> getNumActivePerKey() {
/* 1215 */     return (Map<String, Integer>)this.poolMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> Integer.valueOf(((ObjectDeque)e.getValue()).getAllObjects().size() - ((ObjectDeque)e.getValue()).getIdleObjects().size()), (t, u) -> u));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getNumIdle() {
/* 1223 */     return this.poolMap.values().stream().mapToInt(e -> e.getIdleObjects().size()).sum();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumIdle(K key) {
/* 1228 */     ObjectDeque<T> objectDeque = this.poolMap.get(key);
/* 1229 */     return (objectDeque != null) ? objectDeque.getIdleObjects().size() : 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getNumTests() {
/* 1239 */     int totalIdle = getNumIdle();
/* 1240 */     int numTests = getNumTestsPerEvictionRun();
/* 1241 */     if (numTests >= 0) {
/* 1242 */       return Math.min(numTests, totalIdle);
/*      */     }
/* 1244 */     return (int)Math.ceil(totalIdle / Math.abs(numTests));
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
/*      */   public int getNumWaiters() {
/* 1257 */     if (getBlockWhenExhausted())
/*      */     {
/* 1259 */       return this.poolMap.values().stream().mapToInt(e -> e.getIdleObjects().getTakeQueueLength()).sum();
/*      */     }
/* 1261 */     return 0;
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
/*      */   public Map<String, Integer> getNumWaitersByKey() {
/* 1274 */     Map<String, Integer> result = new HashMap<>();
/* 1275 */     this.poolMap.forEach((k, deque) -> result.put(k.toString(), getBlockWhenExhausted() ? Integer.valueOf(deque.getIdleObjects().getTakeQueueLength()) : ZERO));
/*      */ 
/*      */     
/* 1278 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   String getStatsString() {
/* 1284 */     return super.getStatsString() + 
/* 1285 */       String.format(", fairness=%s, maxIdlePerKey%,d, maxTotalPerKey=%,d, minIdlePerKey=%,d, numTotal=%,d", new Object[] {
/* 1286 */           Boolean.valueOf(this.fairness), Integer.valueOf(this.maxIdlePerKey), Integer.valueOf(this.maxTotalPerKey), Integer.valueOf(this.minIdlePerKey), Integer.valueOf(this.numTotal.get())
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean hasBorrowWaiters() {
/* 1297 */     return (getBlockWhenExhausted() && this.poolMap.values().stream().anyMatch(deque -> deque.getIdleObjects().hasTakeWaiters()));
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
/*      */   public void invalidateObject(K key, T obj) throws Exception {
/* 1317 */     invalidateObject(key, obj, DestroyMode.NORMAL);
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
/*      */   public void invalidateObject(K key, T obj, DestroyMode destroyMode) throws Exception {
/* 1339 */     ObjectDeque<T> objectDeque = this.poolMap.get(key);
/* 1340 */     PooledObject<T> p = (objectDeque != null) ? (PooledObject<T>)objectDeque.getAllObjects().get(new BaseGenericObjectPool.IdentityWrapper<>(obj)) : null;
/* 1341 */     if (p == null) {
/* 1342 */       throw new IllegalStateException(appendStats("Object not currently part of this pool"));
/*      */     }
/* 1344 */     synchronized (p) {
/* 1345 */       if (p.getState() != PooledObjectState.INVALID) {
/* 1346 */         destroy(key, p, true, destroyMode);
/* 1347 */         reuseCapacity();
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
/*      */   public Map<String, List<DefaultPooledObjectInfo>> listAllObjects() {
/* 1366 */     return (Map<String, List<DefaultPooledObjectInfo>>)this.poolMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toString(), e -> (List)((ObjectDeque)e.getValue()).getAllObjects().values().stream().map(DefaultPooledObjectInfo::new).collect(Collectors.toList())));
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
/*      */   public void preparePool(K key) throws Exception {
/* 1379 */     int minIdlePerKeySave = getMinIdlePerKey();
/* 1380 */     if (minIdlePerKeySave < 1) {
/*      */       return;
/*      */     }
/* 1383 */     ensureMinIdle(key);
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
/*      */   private ObjectDeque<T> register(K k) {
/* 1399 */     Lock lock = this.keyLock.readLock();
/* 1400 */     ObjectDeque<T> objectDeque = null;
/*      */     try {
/* 1402 */       lock.lock();
/* 1403 */       objectDeque = this.poolMap.get(k);
/* 1404 */       if (objectDeque == null) {
/*      */         
/* 1406 */         lock.unlock();
/* 1407 */         lock = this.keyLock.writeLock();
/* 1408 */         lock.lock();
/* 1409 */         AtomicBoolean allocated = new AtomicBoolean();
/* 1410 */         objectDeque = this.poolMap.computeIfAbsent(k, key -> {
/*      */               allocated.set(true);
/*      */               
/*      */               ObjectDeque<T> deque = new ObjectDeque<>(this.fairness);
/*      */               
/*      */               deque.getNumInterested().incrementAndGet();
/*      */               
/*      */               this.poolKeyList.add((K)k);
/*      */               return deque;
/*      */             });
/* 1420 */         if (!allocated.get()) {
/*      */ 
/*      */           
/* 1423 */           objectDeque = this.poolMap.get(k);
/* 1424 */           objectDeque.getNumInterested().incrementAndGet();
/*      */         } 
/*      */       } else {
/* 1427 */         objectDeque.getNumInterested().incrementAndGet();
/*      */       } 
/*      */     } finally {
/* 1430 */       lock.unlock();
/*      */     } 
/* 1432 */     return objectDeque;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeAbandoned(AbandonedConfig abandonedConfig) {
/* 1443 */     this.poolMap.forEach((key, value) -> {
/*      */           ArrayList<PooledObject<T>> remove = createRemoveList(abandonedConfig, value.getAllObjects());
/*      */           remove.forEach(());
/*      */         });
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
/*      */   public void returnObject(K key, T obj) {
/* 1488 */     ObjectDeque<T> objectDeque = this.poolMap.get(key);
/*      */     
/* 1490 */     if (objectDeque == null) {
/* 1491 */       throw new IllegalStateException("No keyed pool found under the given key.");
/*      */     }
/*      */     
/* 1494 */     PooledObject<T> p = (PooledObject<T>)objectDeque.getAllObjects().get(new BaseGenericObjectPool.IdentityWrapper<>(obj));
/*      */     
/* 1496 */     if (PooledObject.isNull(p)) {
/* 1497 */       throw new IllegalStateException("Returned object not currently part of this pool");
/*      */     }
/*      */     
/* 1500 */     markReturningState(p);
/*      */     
/* 1502 */     Duration activeTime = p.getActiveDuration();
/*      */     
/*      */     try {
/* 1505 */       if (getTestOnReturn() && !this.factory.validateObject(key, p)) {
/*      */         try {
/* 1507 */           destroy(key, p, true, DestroyMode.NORMAL);
/* 1508 */         } catch (Exception e) {
/* 1509 */           swallowException(e);
/*      */         } 
/* 1511 */         whenWaitersAddObject(key, objectDeque.idleObjects);
/*      */         
/*      */         return;
/*      */       } 
/*      */       try {
/* 1516 */         this.factory.passivateObject(key, p);
/* 1517 */       } catch (Exception e1) {
/* 1518 */         swallowException(e1);
/*      */         try {
/* 1520 */           destroy(key, p, true, DestroyMode.NORMAL);
/* 1521 */         } catch (Exception e) {
/* 1522 */           swallowException(e);
/*      */         } 
/* 1524 */         whenWaitersAddObject(key, objectDeque.idleObjects);
/*      */         
/*      */         return;
/*      */       } 
/* 1528 */       if (!p.deallocate()) {
/* 1529 */         throw new IllegalStateException("Object has already been returned to this pool");
/*      */       }
/*      */       
/* 1532 */       int maxIdle = getMaxIdlePerKey();
/* 1533 */       LinkedBlockingDeque<PooledObject<T>> idleObjects = objectDeque.getIdleObjects();
/*      */       
/* 1535 */       if (isClosed() || (maxIdle > -1 && maxIdle <= idleObjects.size())) {
/*      */         try {
/* 1537 */           destroy(key, p, true, DestroyMode.NORMAL);
/* 1538 */         } catch (Exception e) {
/* 1539 */           swallowException(e);
/*      */         } 
/*      */       } else {
/* 1542 */         if (getLifo()) {
/* 1543 */           idleObjects.addFirst(p);
/*      */         } else {
/* 1545 */           idleObjects.addLast(p);
/*      */         } 
/* 1547 */         if (isClosed())
/*      */         {
/*      */ 
/*      */           
/* 1551 */           clear(key);
/*      */         }
/*      */       } 
/*      */     } finally {
/* 1555 */       if (hasBorrowWaiters()) {
/* 1556 */         reuseCapacity();
/*      */       }
/* 1558 */       updateStatsReturn(activeTime);
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
/*      */   private void reuseCapacity() {
/* 1576 */     int maxTotalPerKeySave = getMaxTotalPerKey();
/* 1577 */     int maxQueueLength = 0;
/* 1578 */     LinkedBlockingDeque<PooledObject<T>> mostLoadedPool = null;
/* 1579 */     K mostLoadedKey = null;
/*      */ 
/*      */     
/* 1582 */     for (Map.Entry<K, ObjectDeque<T>> entry : this.poolMap.entrySet()) {
/* 1583 */       K k = entry.getKey();
/* 1584 */       LinkedBlockingDeque<PooledObject<T>> pool = ((ObjectDeque<T>)entry.getValue()).getIdleObjects();
/* 1585 */       int queueLength = pool.getTakeQueueLength();
/* 1586 */       if (getNumActive(k) < maxTotalPerKeySave && queueLength > maxQueueLength) {
/* 1587 */         maxQueueLength = queueLength;
/* 1588 */         mostLoadedPool = pool;
/* 1589 */         mostLoadedKey = k;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1594 */     if (mostLoadedPool != null) {
/* 1595 */       register(mostLoadedKey);
/*      */ 
/*      */       
/*      */       try {
/* 1599 */         addIdleObject(mostLoadedKey, create(mostLoadedKey));
/* 1600 */       } catch (Exception e) {
/* 1601 */         swallowException(e);
/*      */       } finally {
/* 1603 */         deregister(mostLoadedKey);
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
/*      */   private void reuseCapacity(int newCapacity) {
/* 1616 */     int bound = (newCapacity < 1) ? 1 : newCapacity;
/* 1617 */     for (int i = 0; i < bound; i++) {
/* 1618 */       reuseCapacity();
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
/*      */   public void setConfig(GenericKeyedObjectPoolConfig<T> conf) {
/* 1630 */     setConfig(conf);
/* 1631 */     setMaxIdlePerKey(conf.getMaxIdlePerKey());
/* 1632 */     setMaxTotalPerKey(conf.getMaxTotalPerKey());
/* 1633 */     setMaxTotal(conf.getMaxTotal());
/* 1634 */     setMinIdlePerKey(conf.getMinIdlePerKey());
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
/*      */   public void setMaxIdlePerKey(int maxIdlePerKey) {
/* 1654 */     this.maxIdlePerKey = maxIdlePerKey;
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
/*      */   public void setMaxTotalPerKey(int maxTotalPerKey) {
/* 1667 */     this.maxTotalPerKey = maxTotalPerKey;
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
/*      */   public void setMinIdlePerKey(int minIdlePerKey) {
/* 1690 */     this.minIdlePerKey = minIdlePerKey;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void toStringAppendFields(StringBuilder builder) {
/* 1695 */     super.toStringAppendFields(builder);
/* 1696 */     builder.append(", maxIdlePerKey=");
/* 1697 */     builder.append(this.maxIdlePerKey);
/* 1698 */     builder.append(", minIdlePerKey=");
/* 1699 */     builder.append(this.minIdlePerKey);
/* 1700 */     builder.append(", maxTotalPerKey=");
/* 1701 */     builder.append(this.maxTotalPerKey);
/* 1702 */     builder.append(", factory=");
/* 1703 */     builder.append(this.factory);
/* 1704 */     builder.append(", fairness=");
/* 1705 */     builder.append(this.fairness);
/* 1706 */     builder.append(", poolMap=");
/* 1707 */     builder.append(this.poolMap);
/* 1708 */     builder.append(", poolKeyList=");
/* 1709 */     builder.append(this.poolKeyList);
/* 1710 */     builder.append(", keyLock=");
/* 1711 */     builder.append(this.keyLock);
/* 1712 */     builder.append(", numTotal=");
/* 1713 */     builder.append(this.numTotal);
/* 1714 */     builder.append(", evictionKeyIterator=");
/* 1715 */     builder.append(this.evictionKeyIterator);
/* 1716 */     builder.append(", evictionKey=");
/* 1717 */     builder.append(this.evictionKey);
/* 1718 */     builder.append(", abandonedConfig=");
/* 1719 */     builder.append(this.abandonedConfig);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void use(T pooledObject) {
/* 1727 */     AbandonedConfig abandonedCfg = this.abandonedConfig;
/* 1728 */     if (abandonedCfg != null && abandonedCfg.getUseUsageTracking()) {
/* 1729 */       this.poolMap.values().stream()
/* 1730 */         .map(pool -> (PooledObject)pool.getAllObjects().get(new BaseGenericObjectPool.IdentityWrapper(pooledObject)))
/* 1731 */         .filter(Objects::nonNull)
/* 1732 */         .findFirst()
/* 1733 */         .ifPresent(PooledObject::use);
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
/*      */   private void whenWaitersAddObject(K key, LinkedBlockingDeque<PooledObject<T>> idleObjects) {
/* 1746 */     if (idleObjects.hasTakeWaiters())
/*      */       try {
/* 1748 */         addObject(key);
/* 1749 */       } catch (Exception e) {
/* 1750 */         swallowException(e);
/*      */       }  
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\GenericKeyedObjectPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */