/*      */ package org.apache.commons.pool2.impl;
/*      */ 
/*      */ import java.time.Duration;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collectors;
/*      */ import org.apache.commons.pool2.DestroyMode;
/*      */ import org.apache.commons.pool2.ObjectPool;
/*      */ import org.apache.commons.pool2.PoolUtils;
/*      */ import org.apache.commons.pool2.PooledObject;
/*      */ import org.apache.commons.pool2.PooledObjectFactory;
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
/*      */ public class GenericObjectPool<T>
/*      */   extends BaseGenericObjectPool<T>
/*      */   implements ObjectPool<T>, GenericObjectPoolMXBean, UsageTracking<T>
/*      */ {
/*      */   private static final String ONAME_BASE = "org.apache.commons.pool2:type=GenericObjectPool,name=";
/*      */   private volatile String factoryType;
/*      */   
/*      */   private static void wait(Object obj, Duration duration) throws InterruptedException {
/*   90 */     obj.wait(duration.toMillis(), duration.getNano() % 1000000);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*   95 */   private volatile int maxIdle = 8;
/*      */   
/*   97 */   private volatile int minIdle = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final PooledObjectFactory<T> factory;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  108 */   private final ConcurrentHashMap<BaseGenericObjectPool.IdentityWrapper<T>, PooledObject<T>> allObjects = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  117 */   private final AtomicLong createCount = new AtomicLong();
/*      */   
/*      */   private long makeObjectCount;
/*      */   
/*  121 */   private final Object makeObjectCountLock = new Object();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final LinkedBlockingDeque<PooledObject<T>> idleObjects;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public GenericObjectPool(PooledObjectFactory<T> factory) {
/*  133 */     this(factory, new GenericObjectPoolConfig<>());
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
/*      */   public GenericObjectPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig<T> config) {
/*  150 */     super(config, "org.apache.commons.pool2:type=GenericObjectPool,name=", config.getJmxNamePrefix());
/*      */     
/*  152 */     if (factory == null) {
/*  153 */       jmxUnregister();
/*  154 */       throw new IllegalArgumentException("Factory may not be null");
/*      */     } 
/*  156 */     this.factory = factory;
/*      */     
/*  158 */     this.idleObjects = new LinkedBlockingDeque<>(config.getFairness());
/*      */     
/*  160 */     setConfig(config);
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
/*      */   public GenericObjectPool(PooledObjectFactory<T> factory, GenericObjectPoolConfig<T> config, AbandonedConfig abandonedConfig) {
/*  178 */     this(factory, config);
/*  179 */     setAbandonedConfig(abandonedConfig);
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
/*      */   private void addIdleObject(PooledObject<T> p) throws Exception {
/*  192 */     if (!PooledObject.isNull(p)) {
/*  193 */       this.factory.passivateObject(p);
/*  194 */       if (getLifo()) {
/*  195 */         this.idleObjects.addFirst(p);
/*      */       } else {
/*  197 */         this.idleObjects.addLast(p);
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
/*      */   public void addObject() throws Exception {
/*  218 */     assertOpen();
/*  219 */     if (this.factory == null) {
/*  220 */       throw new IllegalStateException("Cannot add objects without a factory.");
/*      */     }
/*  222 */     addIdleObject(create());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public T borrowObject() throws Exception {
/*  233 */     return borrowObject(getMaxWaitDuration());
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
/*      */   public T borrowObject(Duration borrowMaxWaitDuration) throws Exception {
/*  285 */     assertOpen();
/*      */     
/*  287 */     AbandonedConfig ac = this.abandonedConfig;
/*  288 */     if (ac != null && ac.getRemoveAbandonedOnBorrow() && getNumIdle() < 2 && 
/*  289 */       getNumActive() > getMaxTotal() - 3) {
/*  290 */       removeAbandoned(ac);
/*      */     }
/*      */     
/*  293 */     PooledObject<T> p = null;
/*      */ 
/*      */ 
/*      */     
/*  297 */     boolean blockWhenExhausted = getBlockWhenExhausted();
/*      */ 
/*      */     
/*  300 */     Instant waitTime = Instant.now();
/*      */     
/*  302 */     while (p == null) {
/*  303 */       boolean create = false;
/*  304 */       p = this.idleObjects.pollFirst();
/*  305 */       if (p == null) {
/*  306 */         p = create();
/*  307 */         if (!PooledObject.isNull(p)) {
/*  308 */           create = true;
/*      */         }
/*      */       } 
/*  311 */       if (blockWhenExhausted) {
/*  312 */         if (PooledObject.isNull(p)) {
/*  313 */           p = borrowMaxWaitDuration.isNegative() ? this.idleObjects.takeFirst() : this.idleObjects.pollFirst(borrowMaxWaitDuration);
/*      */         }
/*  315 */         if (PooledObject.isNull(p)) {
/*  316 */           throw new NoSuchElementException(appendStats("Timeout waiting for idle object, borrowMaxWaitDuration=" + borrowMaxWaitDuration));
/*      */         }
/*      */       }
/*  319 */       else if (PooledObject.isNull(p)) {
/*  320 */         throw new NoSuchElementException(appendStats("Pool exhausted"));
/*      */       } 
/*  322 */       if (!p.allocate()) {
/*  323 */         p = null;
/*      */       }
/*      */       
/*  326 */       if (!PooledObject.isNull(p)) {
/*      */         try {
/*  328 */           this.factory.activateObject(p);
/*  329 */         } catch (Exception e) {
/*      */           try {
/*  331 */             destroy(p, DestroyMode.NORMAL);
/*  332 */           } catch (Exception exception) {}
/*      */ 
/*      */           
/*  335 */           p = null;
/*  336 */           if (create) {
/*      */             
/*  338 */             NoSuchElementException nsee = new NoSuchElementException(appendStats("Unable to activate object"));
/*  339 */             nsee.initCause(e);
/*  340 */             throw nsee;
/*      */           } 
/*      */         } 
/*  343 */         if (!PooledObject.isNull(p) && getTestOnBorrow()) {
/*  344 */           boolean validate = false;
/*  345 */           Throwable validationThrowable = null;
/*      */           try {
/*  347 */             validate = this.factory.validateObject(p);
/*  348 */           } catch (Throwable t) {
/*  349 */             PoolUtils.checkRethrow(t);
/*  350 */             validationThrowable = t;
/*      */           } 
/*  352 */           if (!validate) {
/*      */             try {
/*  354 */               destroy(p, DestroyMode.NORMAL);
/*  355 */               this.destroyedByBorrowValidationCount.incrementAndGet();
/*  356 */             } catch (Exception exception) {}
/*      */ 
/*      */             
/*  359 */             p = null;
/*  360 */             if (create) {
/*      */               
/*  362 */               NoSuchElementException nsee = new NoSuchElementException(appendStats("Unable to validate object"));
/*  363 */               nsee.initCause(validationThrowable);
/*  364 */               throw nsee;
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  371 */     updateStatsBorrow(p, Duration.between(waitTime, Instant.now()));
/*      */     
/*  373 */     return (T)p.getObject();
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
/*      */   public T borrowObject(long borrowMaxWaitMillis) throws Exception {
/*  427 */     return borrowObject(Duration.ofMillis(borrowMaxWaitMillis));
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
/*  450 */     PooledObject<T> p = this.idleObjects.poll();
/*      */     
/*  452 */     while (p != null) {
/*      */       try {
/*  454 */         destroy(p, DestroyMode.NORMAL);
/*  455 */       } catch (Exception e) {
/*  456 */         swallowException(e);
/*      */       } 
/*  458 */       p = this.idleObjects.poll();
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
/*      */   public void close() {
/*  473 */     if (isClosed()) {
/*      */       return;
/*      */     }
/*      */     
/*  477 */     synchronized (this.closeLock) {
/*  478 */       if (isClosed()) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  484 */       stopEvictor();
/*      */       
/*  486 */       this.closed = true;
/*      */       
/*  488 */       clear();
/*      */       
/*  490 */       jmxUnregister();
/*      */ 
/*      */       
/*  493 */       this.idleObjects.interuptTakeWaiters();
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
/*      */   private PooledObject<T> create() throws Exception {
/*      */     PooledObject<T> p;
/*  511 */     int localMaxTotal = getMaxTotal();
/*      */     
/*  513 */     if (localMaxTotal < 0) {
/*  514 */       localMaxTotal = Integer.MAX_VALUE;
/*      */     }
/*      */     
/*  517 */     Instant localStartInstant = Instant.now();
/*  518 */     Duration maxWaitDurationRaw = getMaxWaitDuration();
/*  519 */     Duration localMaxWaitDuration = maxWaitDurationRaw.isNegative() ? Duration.ZERO : maxWaitDurationRaw;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  526 */     Boolean create = null;
/*  527 */     while (create == null) {
/*  528 */       synchronized (this.makeObjectCountLock) {
/*  529 */         long newCreateCount = this.createCount.incrementAndGet();
/*  530 */         if (newCreateCount > localMaxTotal) {
/*      */ 
/*      */           
/*  533 */           this.createCount.decrementAndGet();
/*  534 */           if (this.makeObjectCount == 0L) {
/*      */ 
/*      */ 
/*      */             
/*  538 */             create = Boolean.FALSE;
/*      */           
/*      */           }
/*      */           else {
/*      */ 
/*      */             
/*  544 */             wait(this.makeObjectCountLock, localMaxWaitDuration);
/*      */           } 
/*      */         } else {
/*      */           
/*  548 */           this.makeObjectCount++;
/*  549 */           create = Boolean.TRUE;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  554 */       if (create == null && localMaxWaitDuration.compareTo(Duration.ZERO) > 0 && 
/*  555 */         Duration.between(localStartInstant, Instant.now()).compareTo(localMaxWaitDuration) >= 0) {
/*  556 */         create = Boolean.FALSE;
/*      */       }
/*      */     } 
/*      */     
/*  560 */     if (!create.booleanValue()) {
/*  561 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     try {
/*  566 */       p = this.factory.makeObject();
/*  567 */       if (PooledObject.isNull(p)) {
/*  568 */         this.createCount.decrementAndGet();
/*  569 */         throw new NullPointerException(String.format("%s.makeObject() = null", new Object[] { this.factory.getClass().getSimpleName() }));
/*      */       } 
/*  571 */       if (getTestOnCreate() && !this.factory.validateObject(p)) {
/*  572 */         this.createCount.decrementAndGet();
/*  573 */         return null;
/*      */       } 
/*  575 */     } catch (Throwable e) {
/*  576 */       this.createCount.decrementAndGet();
/*  577 */       throw e;
/*      */     } finally {
/*  579 */       synchronized (this.makeObjectCountLock) {
/*  580 */         this.makeObjectCount--;
/*  581 */         this.makeObjectCountLock.notifyAll();
/*      */       } 
/*      */     } 
/*      */     
/*  585 */     AbandonedConfig ac = this.abandonedConfig;
/*  586 */     if (ac != null && ac.getLogAbandoned()) {
/*  587 */       p.setLogAbandoned(true);
/*  588 */       p.setRequireFullStackTrace(ac.getRequireFullStackTrace());
/*      */     } 
/*      */     
/*  591 */     this.createdCount.incrementAndGet();
/*  592 */     this.allObjects.put(new BaseGenericObjectPool.IdentityWrapper<>((T)p.getObject()), p);
/*  593 */     return p;
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
/*      */   private void destroy(PooledObject<T> toDestroy, DestroyMode destroyMode) throws Exception {
/*  606 */     toDestroy.invalidate();
/*  607 */     this.idleObjects.remove(toDestroy);
/*  608 */     this.allObjects.remove(new BaseGenericObjectPool.IdentityWrapper(toDestroy.getObject()));
/*      */     try {
/*  610 */       this.factory.destroyObject(toDestroy, destroyMode);
/*      */     } finally {
/*  612 */       this.destroyedCount.incrementAndGet();
/*  613 */       this.createCount.decrementAndGet();
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
/*      */   private void ensureIdle(int idleCount, boolean always) throws Exception {
/*  635 */     if (idleCount < 1 || isClosed() || (!always && !this.idleObjects.hasTakeWaiters())) {
/*      */       return;
/*      */     }
/*      */     
/*  639 */     while (this.idleObjects.size() < idleCount) {
/*  640 */       PooledObject<T> p = create();
/*  641 */       if (PooledObject.isNull(p)) {
/*      */         break;
/*      */       }
/*      */ 
/*      */       
/*  646 */       if (getLifo()) {
/*  647 */         this.idleObjects.addFirst(p); continue;
/*      */       } 
/*  649 */       this.idleObjects.addLast(p);
/*      */     } 
/*      */     
/*  652 */     if (isClosed())
/*      */     {
/*      */ 
/*      */       
/*  656 */       clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   void ensureMinIdle() throws Exception {
/*  662 */     ensureIdle(getMinIdle(), true);
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
/*      */   public void evict() throws Exception {
/*  674 */     assertOpen();
/*      */     
/*  676 */     if (!this.idleObjects.isEmpty()) {
/*      */       
/*  678 */       PooledObject<T> underTest = null;
/*  679 */       EvictionPolicy<T> evictionPolicy = getEvictionPolicy();
/*      */       
/*  681 */       synchronized (this.evictionLock) {
/*      */ 
/*      */ 
/*      */         
/*  685 */         EvictionConfig evictionConfig = new EvictionConfig(getMinEvictableIdleDuration(), getSoftMinEvictableIdleDuration(), getMinIdle());
/*      */         
/*  687 */         boolean testWhileIdle = getTestWhileIdle();
/*      */         
/*  689 */         for (int i = 0, m = getNumTests(); i < m; i++) {
/*  690 */           if (this.evictionIterator == null || !this.evictionIterator.hasNext()) {
/*  691 */             this.evictionIterator = new BaseGenericObjectPool.EvictionIterator(this, this.idleObjects);
/*      */           }
/*  693 */           if (!this.evictionIterator.hasNext()) {
/*      */             return;
/*      */           }
/*      */ 
/*      */           
/*      */           try {
/*  699 */             underTest = this.evictionIterator.next();
/*  700 */           } catch (NoSuchElementException nsee) {
/*      */ 
/*      */             
/*  703 */             i--;
/*  704 */             this.evictionIterator = null;
/*      */           } 
/*      */ 
/*      */           
/*  708 */           if (!underTest.startEvictionTest()) {
/*      */ 
/*      */             
/*  711 */             i--;
/*      */           } else {
/*      */             boolean evict;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             try {
/*  720 */               evict = evictionPolicy.evict(evictionConfig, underTest, this.idleObjects
/*  721 */                   .size());
/*  722 */             } catch (Throwable t) {
/*      */ 
/*      */               
/*  725 */               PoolUtils.checkRethrow(t);
/*  726 */               swallowException(new Exception(t));
/*      */               
/*  728 */               evict = false;
/*      */             } 
/*      */             
/*  731 */             if (evict) {
/*  732 */               destroy(underTest, DestroyMode.NORMAL);
/*  733 */               this.destroyedByEvictorCount.incrementAndGet();
/*      */             } else {
/*  735 */               if (testWhileIdle) {
/*  736 */                 boolean active = false;
/*      */                 try {
/*  738 */                   this.factory.activateObject(underTest);
/*  739 */                   active = true;
/*  740 */                 } catch (Exception e) {
/*  741 */                   destroy(underTest, DestroyMode.NORMAL);
/*  742 */                   this.destroyedByEvictorCount.incrementAndGet();
/*      */                 } 
/*  744 */                 if (active) {
/*  745 */                   boolean validate = false;
/*  746 */                   Throwable validationThrowable = null;
/*      */                   try {
/*  748 */                     validate = this.factory.validateObject(underTest);
/*  749 */                   } catch (Throwable t) {
/*  750 */                     PoolUtils.checkRethrow(t);
/*  751 */                     validationThrowable = t;
/*      */                   } 
/*  753 */                   if (!validate) {
/*  754 */                     destroy(underTest, DestroyMode.NORMAL);
/*  755 */                     this.destroyedByEvictorCount.incrementAndGet();
/*  756 */                     if (validationThrowable != null) {
/*  757 */                       if (validationThrowable instanceof RuntimeException) {
/*  758 */                         throw (RuntimeException)validationThrowable;
/*      */                       }
/*  760 */                       throw (Error)validationThrowable;
/*      */                     } 
/*      */                   } else {
/*      */                     try {
/*  764 */                       this.factory.passivateObject(underTest);
/*  765 */                     } catch (Exception e) {
/*  766 */                       destroy(underTest, DestroyMode.NORMAL);
/*  767 */                       this.destroyedByEvictorCount.incrementAndGet();
/*      */                     } 
/*      */                   } 
/*      */                 } 
/*      */               } 
/*  772 */               underTest.endEvictionTest(this.idleObjects);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  779 */     AbandonedConfig ac = this.abandonedConfig;
/*  780 */     if (ac != null && ac.getRemoveAbandonedOnMaintenance()) {
/*  781 */       removeAbandoned(ac);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PooledObjectFactory<T> getFactory() {
/*  792 */     return this.factory;
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
/*      */   public String getFactoryType() {
/*  804 */     if (this.factoryType == null) {
/*  805 */       StringBuilder result = new StringBuilder();
/*  806 */       result.append(this.factory.getClass().getName());
/*  807 */       result.append('<');
/*      */       
/*  809 */       Class<?> pooledObjectType = PoolImplUtils.getFactoryType((Class)this.factory.getClass());
/*  810 */       result.append(pooledObjectType.getName());
/*  811 */       result.append('>');
/*  812 */       this.factoryType = result.toString();
/*      */     } 
/*  814 */     return this.factoryType;
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
/*      */   public int getMaxIdle() {
/*  833 */     return this.maxIdle;
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
/*      */   public int getMinIdle() {
/*  855 */     int maxIdleSave = getMaxIdle();
/*  856 */     return Math.min(this.minIdle, maxIdleSave);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumActive() {
/*  861 */     return this.allObjects.size() - this.idleObjects.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getNumIdle() {
/*  866 */     return this.idleObjects.size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int getNumTests() {
/*  876 */     int numTestsPerEvictionRun = getNumTestsPerEvictionRun();
/*  877 */     if (numTestsPerEvictionRun >= 0) {
/*  878 */       return Math.min(numTestsPerEvictionRun, this.idleObjects.size());
/*      */     }
/*  880 */     return (int)Math.ceil(this.idleObjects.size() / 
/*  881 */         Math.abs(numTestsPerEvictionRun));
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
/*  894 */     if (getBlockWhenExhausted()) {
/*  895 */       return this.idleObjects.getTakeQueueLength();
/*      */     }
/*  897 */     return 0;
/*      */   }
/*      */   
/*      */   PooledObject<T> getPooledObject(T obj) {
/*  901 */     return this.allObjects.get(new BaseGenericObjectPool.IdentityWrapper<>(obj));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   String getStatsString() {
/*  907 */     return super.getStatsString() + 
/*  908 */       String.format(", createdCount=%,d, makeObjectCount=%,d, maxIdle=%,d, minIdle=%,d", new Object[] {
/*  909 */           Long.valueOf(this.createdCount.get()), Long.valueOf(this.makeObjectCount), Integer.valueOf(this.maxIdle), Integer.valueOf(this.minIdle)
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
/*      */   public void invalidateObject(T obj) throws Exception {
/*  924 */     invalidateObject(obj, DestroyMode.NORMAL);
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
/*      */   public void invalidateObject(T obj, DestroyMode destroyMode) throws Exception {
/*  940 */     PooledObject<T> p = getPooledObject(obj);
/*  941 */     if (p == null) {
/*  942 */       if (isAbandonedConfig()) {
/*      */         return;
/*      */       }
/*  945 */       throw new IllegalStateException("Invalidated object not currently part of this pool");
/*      */     } 
/*  947 */     synchronized (p) {
/*  948 */       if (p.getState() != PooledObjectState.INVALID) {
/*  949 */         destroy(p, destroyMode);
/*      */       }
/*      */     } 
/*  952 */     ensureIdle(1, false);
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
/*      */   public Set<DefaultPooledObjectInfo> listAllObjects() {
/*  969 */     return (Set<DefaultPooledObjectInfo>)this.allObjects.values().stream().map(DefaultPooledObjectInfo::new).collect(Collectors.toSet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void preparePool() throws Exception {
/*  979 */     if (getMinIdle() < 1) {
/*      */       return;
/*      */     }
/*  982 */     ensureMinIdle();
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
/*      */   private void removeAbandoned(AbandonedConfig abandonedConfig) {
/*  994 */     ArrayList<PooledObject<T>> remove = createRemoveList(abandonedConfig, this.allObjects);
/*      */     
/*  996 */     remove.forEach(pooledObject -> {
/*      */           if (abandonedConfig.getLogAbandoned()) {
/*      */             pooledObject.printStackTrace(abandonedConfig.getLogWriter());
/*      */           }
/*      */           try {
/*      */             invalidateObject((T)pooledObject.getObject(), DestroyMode.ABANDONED);
/* 1002 */           } catch (Exception e) {
/*      */             swallowException(e);
/*      */           } 
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
/*      */   public void returnObject(T obj) {
/* 1027 */     PooledObject<T> p = getPooledObject(obj);
/*      */     
/* 1029 */     if (p == null) {
/* 1030 */       if (!isAbandonedConfig()) {
/* 1031 */         throw new IllegalStateException("Returned object not currently part of this pool");
/*      */       }
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/* 1037 */     markReturningState(p);
/*      */     
/* 1039 */     Duration activeTime = p.getActiveDuration();
/*      */     
/* 1041 */     if (getTestOnReturn() && !this.factory.validateObject(p)) {
/*      */       try {
/* 1043 */         destroy(p, DestroyMode.NORMAL);
/* 1044 */       } catch (Exception e) {
/* 1045 */         swallowException(e);
/*      */       } 
/*      */       try {
/* 1048 */         ensureIdle(1, false);
/* 1049 */       } catch (Exception e) {
/* 1050 */         swallowException(e);
/*      */       } 
/* 1052 */       updateStatsReturn(activeTime);
/*      */       
/*      */       return;
/*      */     } 
/*      */     try {
/* 1057 */       this.factory.passivateObject(p);
/* 1058 */     } catch (Exception e1) {
/* 1059 */       swallowException(e1);
/*      */       try {
/* 1061 */         destroy(p, DestroyMode.NORMAL);
/* 1062 */       } catch (Exception e) {
/* 1063 */         swallowException(e);
/*      */       } 
/*      */       try {
/* 1066 */         ensureIdle(1, false);
/* 1067 */       } catch (Exception e) {
/* 1068 */         swallowException(e);
/*      */       } 
/* 1070 */       updateStatsReturn(activeTime);
/*      */       
/*      */       return;
/*      */     } 
/* 1074 */     if (!p.deallocate()) {
/* 1075 */       throw new IllegalStateException("Object has already been returned to this pool or is invalid");
/*      */     }
/*      */ 
/*      */     
/* 1079 */     int maxIdleSave = getMaxIdle();
/* 1080 */     if (isClosed() || (maxIdleSave > -1 && maxIdleSave <= this.idleObjects.size())) {
/*      */       try {
/* 1082 */         destroy(p, DestroyMode.NORMAL);
/* 1083 */       } catch (Exception e) {
/* 1084 */         swallowException(e);
/*      */       } 
/*      */       try {
/* 1087 */         ensureIdle(1, false);
/* 1088 */       } catch (Exception e) {
/* 1089 */         swallowException(e);
/*      */       } 
/*      */     } else {
/* 1092 */       if (getLifo()) {
/* 1093 */         this.idleObjects.addFirst(p);
/*      */       } else {
/* 1095 */         this.idleObjects.addLast(p);
/*      */       } 
/* 1097 */       if (isClosed())
/*      */       {
/*      */ 
/*      */         
/* 1101 */         clear();
/*      */       }
/*      */     } 
/* 1104 */     updateStatsReturn(activeTime);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConfig(GenericObjectPoolConfig<T> conf) {
/* 1115 */     setConfig(conf);
/* 1116 */     setMaxIdle(conf.getMaxIdle());
/* 1117 */     setMinIdle(conf.getMinIdle());
/* 1118 */     setMaxTotal(conf.getMaxTotal());
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
/*      */   public void setMaxIdle(int maxIdle) {
/* 1138 */     this.maxIdle = maxIdle;
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
/*      */   public void setMinIdle(int minIdle) {
/* 1160 */     this.minIdle = minIdle;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void toStringAppendFields(StringBuilder builder) {
/* 1165 */     super.toStringAppendFields(builder);
/* 1166 */     builder.append(", factoryType=");
/* 1167 */     builder.append(this.factoryType);
/* 1168 */     builder.append(", maxIdle=");
/* 1169 */     builder.append(this.maxIdle);
/* 1170 */     builder.append(", minIdle=");
/* 1171 */     builder.append(this.minIdle);
/* 1172 */     builder.append(", factory=");
/* 1173 */     builder.append(this.factory);
/* 1174 */     builder.append(", allObjects=");
/* 1175 */     builder.append(this.allObjects);
/* 1176 */     builder.append(", createCount=");
/* 1177 */     builder.append(this.createCount);
/* 1178 */     builder.append(", idleObjects=");
/* 1179 */     builder.append(this.idleObjects);
/* 1180 */     builder.append(", abandonedConfig=");
/* 1181 */     builder.append(this.abandonedConfig);
/*      */   }
/*      */ 
/*      */   
/*      */   public void use(T pooledObject) {
/* 1186 */     AbandonedConfig abandonedCfg = this.abandonedConfig;
/* 1187 */     if (abandonedCfg != null && abandonedCfg.getUseUsageTracking()) {
/* 1188 */       PooledObject<T> po = getPooledObject(pooledObject);
/* 1189 */       if (po != null)
/* 1190 */         po.use(); 
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\GenericObjectPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */