/*      */ package org.apache.commons.pool2.impl;
/*      */ 
/*      */ import java.io.PrintWriter;
/*      */ import java.io.StringWriter;
/*      */ import java.io.Writer;
/*      */ import java.lang.management.ManagementFactory;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.time.Duration;
/*      */ import java.time.Instant;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Deque;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.ScheduledFuture;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import java.util.stream.Collectors;
/*      */ import javax.management.InstanceAlreadyExistsException;
/*      */ import javax.management.JMException;
/*      */ import javax.management.MBeanRegistrationException;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.ObjectName;
/*      */ import org.apache.commons.pool2.BaseObject;
/*      */ import org.apache.commons.pool2.PooledObject;
/*      */ import org.apache.commons.pool2.PooledObjectState;
/*      */ import org.apache.commons.pool2.SwallowedExceptionListener;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class BaseGenericObjectPool<T>
/*      */   extends BaseObject
/*      */   implements AutoCloseable
/*      */ {
/*      */   public static final int MEAN_TIMING_STATS_CACHE_SIZE = 100;
/*      */   
/*      */   class EvictionIterator
/*      */     implements Iterator<PooledObject<T>>
/*      */   {
/*      */     private final Deque<PooledObject<T>> idleObjects;
/*      */     private final Iterator<PooledObject<T>> idleObjectIterator;
/*      */     
/*      */     EvictionIterator(Deque<PooledObject<T>> idleObjects) {
/*   78 */       this.idleObjects = idleObjects;
/*      */       
/*   80 */       if (BaseGenericObjectPool.this.getLifo()) {
/*   81 */         this.idleObjectIterator = idleObjects.descendingIterator();
/*      */       } else {
/*   83 */         this.idleObjectIterator = idleObjects.iterator();
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Deque<PooledObject<T>> getIdleObjects() {
/*   92 */       return this.idleObjects;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasNext() {
/*   98 */       return this.idleObjectIterator.hasNext();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public PooledObject<T> next() {
/*  104 */       return this.idleObjectIterator.next();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public void remove() {
/*  110 */       this.idleObjectIterator.remove();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class Evictor
/*      */     implements Runnable
/*      */   {
/*      */     private ScheduledFuture<?> scheduledFuture;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void cancel() {
/*  128 */       this.scheduledFuture.cancel(false);
/*      */     }
/*      */     
/*      */     BaseGenericObjectPool<T> owner() {
/*  132 */       return BaseGenericObjectPool.this;
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
/*      */     public void run() {
/*  145 */       ClassLoader savedClassLoader = Thread.currentThread().getContextClassLoader();
/*      */       try {
/*  147 */         if (BaseGenericObjectPool.this.factoryClassLoader != null) {
/*      */           
/*  149 */           ClassLoader cl = BaseGenericObjectPool.this.factoryClassLoader.get();
/*  150 */           if (cl == null) {
/*      */ 
/*      */ 
/*      */             
/*  154 */             cancel();
/*      */             return;
/*      */           } 
/*  157 */           Thread.currentThread().setContextClassLoader(cl);
/*      */         } 
/*      */ 
/*      */         
/*      */         try {
/*  162 */           BaseGenericObjectPool.this.evict();
/*  163 */         } catch (Exception e) {
/*  164 */           BaseGenericObjectPool.this.swallowException(e);
/*  165 */         } catch (OutOfMemoryError oome) {
/*      */ 
/*      */           
/*  168 */           oome.printStackTrace(System.err);
/*      */         } 
/*      */         
/*      */         try {
/*  172 */           BaseGenericObjectPool.this.ensureMinIdle();
/*  173 */         } catch (Exception e) {
/*  174 */           BaseGenericObjectPool.this.swallowException(e);
/*      */         } 
/*      */       } finally {
/*      */         
/*  178 */         Thread.currentThread().setContextClassLoader(savedClassLoader);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
/*  188 */       this.scheduledFuture = scheduledFuture;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  193 */       return getClass().getName() + " [scheduledFuture=" + this.scheduledFuture + "]";
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
/*      */   static class IdentityWrapper<T>
/*      */   {
/*      */     private final T instance;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public IdentityWrapper(T instance) {
/*  217 */       this.instance = instance;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean equals(Object other) {
/*  223 */       return (other instanceof IdentityWrapper && ((IdentityWrapper)other).instance == this.instance);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public T getObject() {
/*  230 */       return this.instance;
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/*  235 */       return System.identityHashCode(this.instance);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  240 */       StringBuilder builder = new StringBuilder();
/*  241 */       builder.append("IdentityWrapper [instance=");
/*  242 */       builder.append(this.instance);
/*  243 */       builder.append("]");
/*  244 */       return builder.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class StatsStore
/*      */   {
/*      */     private static final int NONE = -1;
/*      */ 
/*      */     
/*      */     private final AtomicLong[] values;
/*      */ 
/*      */     
/*      */     private final int size;
/*      */ 
/*      */     
/*      */     private int index;
/*      */ 
/*      */     
/*      */     StatsStore(int size) {
/*  265 */       this.size = size;
/*  266 */       this.values = new AtomicLong[size];
/*  267 */       Arrays.setAll(this.values, i -> new AtomicLong(-1L));
/*      */     }
/*      */     
/*      */     void add(Duration value) {
/*  271 */       add(value.toMillis());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     synchronized void add(long value) {
/*  281 */       this.values[this.index].set(value);
/*  282 */       this.index++;
/*  283 */       if (this.index == this.size) {
/*  284 */         this.index = 0;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public long getMean() {
/*  294 */       double result = 0.0D;
/*  295 */       int counter = 0;
/*  296 */       for (int i = 0; i < this.size; i++) {
/*  297 */         long value = this.values[i].get();
/*  298 */         if (value != -1L) {
/*  299 */           counter++;
/*  300 */           result = result * (counter - 1) / counter + value / counter;
/*      */         } 
/*      */       } 
/*  303 */       return (long)result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Duration getMeanDuration() {
/*  312 */       return Duration.ofMillis(getMean());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     synchronized List<AtomicLong> getValues() {
/*  321 */       return (List<AtomicLong>)Arrays.<AtomicLong>stream(this.values, 0, this.index).collect(Collectors.toList());
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  326 */       StringBuilder builder = new StringBuilder();
/*  327 */       builder.append("StatsStore [");
/*      */       
/*  329 */       builder.append(getValues());
/*  330 */       builder.append("], size=");
/*  331 */       builder.append(this.size);
/*  332 */       builder.append(", index=");
/*  333 */       builder.append(this.index);
/*  334 */       builder.append("]");
/*  335 */       return builder.toString();
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
/*  346 */   private static final String EVICTION_POLICY_TYPE_NAME = EvictionPolicy.class.getName();
/*  347 */   private static final Duration DEFAULT_REMOVE_ABANDONED_TIMEOUT = Duration.ofSeconds(2147483647L);
/*      */   
/*  349 */   private volatile int maxTotal = -1;
/*      */   private volatile boolean blockWhenExhausted = true;
/*  351 */   private volatile Duration maxWaitDuration = BaseObjectPoolConfig.DEFAULT_MAX_WAIT;
/*      */   private volatile boolean lifo = true;
/*      */   private final boolean fairness;
/*      */   private volatile boolean testOnCreate = false;
/*      */   private volatile boolean testOnBorrow = false;
/*      */   private volatile boolean testOnReturn = false;
/*      */   private volatile boolean testWhileIdle = false;
/*  358 */   private volatile Duration durationBetweenEvictionRuns = BaseObjectPoolConfig.DEFAULT_DURATION_BETWEEN_EVICTION_RUNS;
/*  359 */   private volatile int numTestsPerEvictionRun = 3;
/*      */   
/*  361 */   private volatile Duration minEvictableIdleDuration = BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_DURATION;
/*  362 */   private volatile Duration softMinEvictableIdleDuration = BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION;
/*      */   private volatile EvictionPolicy<T> evictionPolicy;
/*  364 */   private volatile Duration evictorShutdownTimeoutDuration = BaseObjectPoolConfig.DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT;
/*      */   
/*  366 */   final Object closeLock = new Object();
/*      */   
/*      */   volatile boolean closed;
/*  369 */   final Object evictionLock = new Object();
/*      */ 
/*      */   
/*      */   private Evictor evictor;
/*      */ 
/*      */   
/*      */   EvictionIterator evictionIterator;
/*      */   
/*      */   private final WeakReference<ClassLoader> factoryClassLoader;
/*      */   
/*      */   private final ObjectName objectName;
/*      */   
/*      */   private final String creationStackTrace;
/*      */   
/*  383 */   private final AtomicLong borrowedCount = new AtomicLong();
/*  384 */   private final AtomicLong returnedCount = new AtomicLong();
/*  385 */   final AtomicLong createdCount = new AtomicLong();
/*  386 */   final AtomicLong destroyedCount = new AtomicLong();
/*  387 */   final AtomicLong destroyedByEvictorCount = new AtomicLong();
/*  388 */   final AtomicLong destroyedByBorrowValidationCount = new AtomicLong();
/*      */   
/*  390 */   private final StatsStore activeTimes = new StatsStore(100);
/*  391 */   private final StatsStore idleTimes = new StatsStore(100);
/*  392 */   private final StatsStore waitTimes = new StatsStore(100);
/*      */   
/*  394 */   private final AtomicReference<Duration> maxBorrowWaitDuration = new AtomicReference<>(Duration.ZERO);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile SwallowedExceptionListener swallowedExceptionListener;
/*      */ 
/*      */ 
/*      */   
/*      */   private volatile boolean messageStatistics;
/*      */ 
/*      */ 
/*      */   
/*      */   protected volatile AbandonedConfig abandonedConfig;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BaseGenericObjectPool(BaseObjectPoolConfig<T> config, String jmxNameBase, String jmxNamePrefix) {
/*  413 */     if (config.getJmxEnabled()) {
/*  414 */       this.objectName = jmxRegister(config, jmxNameBase, jmxNamePrefix);
/*      */     } else {
/*  416 */       this.objectName = null;
/*      */     } 
/*      */ 
/*      */     
/*  420 */     this.creationStackTrace = getStackTrace(new Exception());
/*      */ 
/*      */     
/*  423 */     ClassLoader cl = Thread.currentThread().getContextClassLoader();
/*  424 */     if (cl == null) {
/*  425 */       this.factoryClassLoader = null;
/*      */     } else {
/*  427 */       this.factoryClassLoader = new WeakReference<>(cl);
/*      */     } 
/*      */     
/*  430 */     this.fairness = config.getFairness();
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
/*      */   String appendStats(String string) {
/*  444 */     return this.messageStatistics ? (string + ", " + getStatsString()) : string;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void assertOpen() throws IllegalStateException {
/*  452 */     if (isClosed()) {
/*  453 */       throw new IllegalStateException("Pool not open");
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
/*      */   ArrayList<PooledObject<T>> createRemoveList(AbandonedConfig abandonedConfig, Map<IdentityWrapper<T>, PooledObject<T>> allObjects) {
/*  471 */     Instant timeout = Instant.now().minus(abandonedConfig.getRemoveAbandonedTimeoutDuration());
/*  472 */     ArrayList<PooledObject<T>> remove = new ArrayList<>();
/*  473 */     allObjects.values().forEach(pooledObject -> {
/*      */           synchronized (pooledObject) {
/*      */             if (pooledObject.getState() == PooledObjectState.ALLOCATED && pooledObject.getLastUsedInstant().compareTo(timeout) <= 0) {
/*      */               pooledObject.markAbandoned();
/*      */               
/*      */               remove.add(pooledObject);
/*      */             } 
/*      */           } 
/*      */         });
/*  482 */     return remove;
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
/*      */   public final boolean getBlockWhenExhausted() {
/*  515 */     return this.blockWhenExhausted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getBorrowedCount() {
/*  524 */     return this.borrowedCount.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getCreatedCount() {
/*  533 */     return this.createdCount.get();
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
/*      */   public final String getCreationStackTrace() {
/*  545 */     return this.creationStackTrace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getDestroyedByBorrowValidationCount() {
/*  555 */     return this.destroyedByBorrowValidationCount.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getDestroyedByEvictorCount() {
/*  564 */     return this.destroyedByEvictorCount.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getDestroyedCount() {
/*  573 */     return this.destroyedCount.get();
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
/*      */   public final Duration getDurationBetweenEvictionRuns() {
/*  587 */     return this.durationBetweenEvictionRuns;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EvictionPolicy<T> getEvictionPolicy() {
/*  598 */     return this.evictionPolicy;
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
/*      */   public final String getEvictionPolicyClassName() {
/*  610 */     return this.evictionPolicy.getClass().getName();
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
/*      */   @Deprecated
/*      */   public final Duration getEvictorShutdownTimeout() {
/*  625 */     return this.evictorShutdownTimeoutDuration;
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
/*      */   public final Duration getEvictorShutdownTimeoutDuration() {
/*  638 */     return this.evictorShutdownTimeoutDuration;
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
/*      */   @Deprecated
/*      */   public final long getEvictorShutdownTimeoutMillis() {
/*  652 */     return this.evictorShutdownTimeoutDuration.toMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean getFairness() {
/*  663 */     return this.fairness;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final ObjectName getJmxName() {
/*  673 */     return this.objectName;
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
/*      */   public final boolean getLifo() {
/*  689 */     return this.lifo;
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
/*      */   public boolean getLogAbandoned() {
/*  702 */     AbandonedConfig ac = this.abandonedConfig;
/*  703 */     return (ac != null && ac.getLogAbandoned());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Duration getMaxBorrowWaitDuration() {
/*  713 */     return this.maxBorrowWaitDuration.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final long getMaxBorrowWaitTimeMillis() {
/*  724 */     return ((Duration)this.maxBorrowWaitDuration.get()).toMillis();
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
/*      */   public final int getMaxTotal() {
/*  738 */     return this.maxTotal;
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
/*      */   public final Duration getMaxWaitDuration() {
/*  756 */     return this.maxWaitDuration;
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
/*      */   @Deprecated
/*      */   public final long getMaxWaitMillis() {
/*  775 */     return this.maxWaitDuration.toMillis();
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
/*      */   public final Duration getMeanActiveDuration() {
/*  787 */     return this.activeTimes.getMeanDuration();
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
/*      */   @Deprecated
/*      */   public final long getMeanActiveTimeMillis() {
/*  800 */     return this.activeTimes.getMean();
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
/*      */   public final Duration getMeanBorrowWaitDuration() {
/*  812 */     return this.waitTimes.getMeanDuration();
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
/*      */   @Deprecated
/*      */   public final long getMeanBorrowWaitTimeMillis() {
/*  825 */     return this.waitTimes.getMean();
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
/*      */   public final Duration getMeanIdleDuration() {
/*  837 */     return this.idleTimes.getMeanDuration();
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
/*      */   @Deprecated
/*      */   public final long getMeanIdleTimeMillis() {
/*  850 */     return this.idleTimes.getMean();
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
/*      */   public boolean getMessageStatistics() {
/*  864 */     return this.messageStatistics;
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
/*      */   public final Duration getMinEvictableIdleDuration() {
/*  881 */     return this.minEvictableIdleDuration;
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
/*      */   @Deprecated
/*      */   public final Duration getMinEvictableIdleTime() {
/*  900 */     return this.minEvictableIdleDuration;
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
/*      */   @Deprecated
/*      */   public final long getMinEvictableIdleTimeMillis() {
/*  918 */     return this.minEvictableIdleDuration.toMillis();
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
/*      */   public final int getNumTestsPerEvictionRun() {
/*  943 */     return this.numTestsPerEvictionRun;
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
/*      */   public boolean getRemoveAbandonedOnBorrow() {
/*  957 */     AbandonedConfig ac = this.abandonedConfig;
/*  958 */     return (ac != null && ac.getRemoveAbandonedOnBorrow());
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
/*      */   public boolean getRemoveAbandonedOnMaintenance() {
/*  971 */     AbandonedConfig ac = this.abandonedConfig;
/*  972 */     return (ac != null && ac.getRemoveAbandonedOnMaintenance());
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
/*      */   @Deprecated
/*      */   public int getRemoveAbandonedTimeout() {
/*  989 */     return (int)getRemoveAbandonedTimeoutDuration().getSeconds();
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
/*      */   public Duration getRemoveAbandonedTimeoutDuration() {
/* 1003 */     AbandonedConfig ac = this.abandonedConfig;
/* 1004 */     return (ac != null) ? ac.getRemoveAbandonedTimeoutDuration() : DEFAULT_REMOVE_ABANDONED_TIMEOUT;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long getReturnedCount() {
/* 1014 */     return this.returnedCount.get();
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
/*      */   public final Duration getSoftMinEvictableIdleDuration() {
/* 1034 */     return this.softMinEvictableIdleDuration;
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
/*      */   public final Duration getSoftMinEvictableIdleTime() {
/* 1056 */     return this.softMinEvictableIdleDuration;
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
/*      */   @Deprecated
/*      */   public final long getSoftMinEvictableIdleTimeMillis() {
/* 1077 */     return this.softMinEvictableIdleDuration.toMillis();
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
/*      */   private String getStackTrace(Exception e) {
/* 1089 */     Writer w = new StringWriter();
/* 1090 */     PrintWriter pw = new PrintWriter(w);
/* 1091 */     e.printStackTrace(pw);
/* 1092 */     return w.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   String getStatsString() {
/* 1102 */     return String.format("activeTimes=%s, blockWhenExhausted=%s, borrowedCount=%,d, closed=%s, createdCount=%,d, destroyedByBorrowValidationCount=%,d, destroyedByEvictorCount=%,d, evictorShutdownTimeoutDuration=%s, fairness=%s, idleTimes=%s, lifo=%s, maxBorrowWaitDuration=%s, maxTotal=%s, maxWaitDuration=%s, minEvictableIdleDuration=%s, numTestsPerEvictionRun=%s, returnedCount=%s, softMinEvictableIdleDuration=%s, testOnBorrow=%s, testOnCreate=%s, testOnReturn=%s, testWhileIdle=%s, durationBetweenEvictionRuns=%s, waitTimes=%s", new Object[] { this.activeTimes
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1108 */           .getValues(), Boolean.valueOf(this.blockWhenExhausted), Long.valueOf(this.borrowedCount.get()), Boolean.valueOf(this.closed), Long.valueOf(this.createdCount.get()), Long.valueOf(this.destroyedByBorrowValidationCount.get()), 
/* 1109 */           Long.valueOf(this.destroyedByEvictorCount.get()), this.evictorShutdownTimeoutDuration, Boolean.valueOf(this.fairness), this.idleTimes.getValues(), Boolean.valueOf(this.lifo), this.maxBorrowWaitDuration.get(), 
/* 1110 */           Integer.valueOf(this.maxTotal), this.maxWaitDuration, this.minEvictableIdleDuration, Integer.valueOf(this.numTestsPerEvictionRun), this.returnedCount, this.softMinEvictableIdleDuration, Boolean.valueOf(this.testOnBorrow), 
/* 1111 */           Boolean.valueOf(this.testOnCreate), Boolean.valueOf(this.testOnReturn), Boolean.valueOf(this.testWhileIdle), this.durationBetweenEvictionRuns, this.waitTimes.getValues() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final SwallowedExceptionListener getSwallowedExceptionListener() {
/* 1121 */     return this.swallowedExceptionListener;
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
/*      */   public final boolean getTestOnBorrow() {
/* 1138 */     return this.testOnBorrow;
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
/*      */   public final boolean getTestOnCreate() {
/* 1156 */     return this.testOnCreate;
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
/*      */   public final boolean getTestOnReturn() {
/* 1172 */     return this.testOnReturn;
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
/*      */   public final boolean getTestWhileIdle() {
/* 1189 */     return this.testWhileIdle;
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
/*      */   @Deprecated
/*      */   public final Duration getTimeBetweenEvictionRuns() {
/* 1205 */     return this.durationBetweenEvictionRuns;
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
/*      */   @Deprecated
/*      */   public final long getTimeBetweenEvictionRunsMillis() {
/* 1220 */     return this.durationBetweenEvictionRuns.toMillis();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAbandonedConfig() {
/* 1231 */     return (this.abandonedConfig != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final boolean isClosed() {
/* 1239 */     return this.closed;
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
/*      */   private ObjectName jmxRegister(BaseObjectPoolConfig<T> config, String jmxNameBase, String jmxNamePrefix) {
/* 1257 */     ObjectName newObjectName = null;
/* 1258 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 1259 */     int i = 1;
/* 1260 */     boolean registered = false;
/* 1261 */     String base = config.getJmxNameBase();
/* 1262 */     if (base == null) {
/* 1263 */       base = jmxNameBase;
/*      */     }
/* 1265 */     while (!registered) {
/*      */       try {
/*      */         ObjectName objName;
/*      */ 
/*      */         
/* 1270 */         if (i == 1) {
/* 1271 */           objName = new ObjectName(base + jmxNamePrefix);
/*      */         } else {
/* 1273 */           objName = new ObjectName(base + jmxNamePrefix + i);
/*      */         } 
/* 1275 */         if (!mbs.isRegistered(objName)) {
/* 1276 */           mbs.registerMBean(this, objName);
/* 1277 */           newObjectName = objName;
/* 1278 */           registered = true;
/*      */           continue;
/*      */         } 
/* 1281 */         i++;
/*      */       }
/* 1283 */       catch (MalformedObjectNameException e) {
/* 1284 */         if ("pool".equals(jmxNamePrefix) && jmxNameBase
/* 1285 */           .equals(base)) {
/*      */           
/* 1287 */           registered = true;
/*      */           continue;
/*      */         } 
/* 1290 */         jmxNamePrefix = "pool";
/*      */         
/* 1292 */         base = jmxNameBase;
/*      */       }
/* 1294 */       catch (InstanceAlreadyExistsException e) {
/*      */         
/* 1296 */         i++;
/* 1297 */       } catch (MBeanRegistrationException|javax.management.NotCompliantMBeanException e) {
/*      */         
/* 1299 */         registered = true;
/*      */       } 
/*      */     } 
/* 1302 */     return newObjectName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void jmxUnregister() {
/* 1309 */     if (this.objectName != null) {
/*      */       try {
/* 1311 */         ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.objectName);
/* 1312 */       } catch (MBeanRegistrationException|javax.management.InstanceNotFoundException e) {
/* 1313 */         swallowException(e);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void markReturningState(PooledObject<T> pooledObject) {
/* 1323 */     synchronized (pooledObject) {
/* 1324 */       if (pooledObject.getState() != PooledObjectState.ALLOCATED) {
/* 1325 */         throw new IllegalStateException("Object has already been returned to this pool or is invalid");
/*      */       }
/* 1327 */       pooledObject.markReturning();
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
/*      */   public void setAbandonedConfig(AbandonedConfig abandonedConfig) {
/* 1340 */     this.abandonedConfig = AbandonedConfig.copy(abandonedConfig);
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
/*      */   public final void setBlockWhenExhausted(boolean blockWhenExhausted) {
/* 1355 */     this.blockWhenExhausted = blockWhenExhausted;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setConfig(BaseObjectPoolConfig<T> config) {
/* 1364 */     setLifo(config.getLifo());
/* 1365 */     setMaxWait(config.getMaxWaitDuration());
/* 1366 */     setBlockWhenExhausted(config.getBlockWhenExhausted());
/* 1367 */     setTestOnCreate(config.getTestOnCreate());
/* 1368 */     setTestOnBorrow(config.getTestOnBorrow());
/* 1369 */     setTestOnReturn(config.getTestOnReturn());
/* 1370 */     setTestWhileIdle(config.getTestWhileIdle());
/* 1371 */     setNumTestsPerEvictionRun(config.getNumTestsPerEvictionRun());
/* 1372 */     setMinEvictableIdleDuration(config.getMinEvictableIdleDuration());
/* 1373 */     setDurationBetweenEvictionRuns(config.getDurationBetweenEvictionRuns());
/* 1374 */     setSoftMinEvictableIdleDuration(config.getSoftMinEvictableIdleDuration());
/* 1375 */     EvictionPolicy<T> policy = config.getEvictionPolicy();
/* 1376 */     if (policy == null) {
/*      */       
/* 1378 */       setEvictionPolicyClassName(config.getEvictionPolicyClassName());
/*      */     } else {
/*      */       
/* 1381 */       setEvictionPolicy(policy);
/*      */     } 
/* 1383 */     setEvictorShutdownTimeout(config.getEvictorShutdownTimeoutDuration());
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
/*      */   public final void setDurationBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
/* 1400 */     this.durationBetweenEvictionRuns = PoolImplUtils.nonNull(timeBetweenEvictionRuns, BaseObjectPoolConfig.DEFAULT_DURATION_BETWEEN_EVICTION_RUNS);
/* 1401 */     startEvictor(this.durationBetweenEvictionRuns);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setEvictionPolicy(EvictionPolicy<T> evictionPolicy) {
/* 1412 */     this.evictionPolicy = evictionPolicy;
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
/*      */   private void setEvictionPolicy(String className, ClassLoader classLoader) throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
/* 1438 */     Class<?> clazz = Class.forName(className, true, classLoader);
/* 1439 */     Object policy = clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 1440 */     this.evictionPolicy = (EvictionPolicy<T>)policy;
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
/*      */   public final void setEvictionPolicyClassName(String evictionPolicyClassName) {
/* 1456 */     setEvictionPolicyClassName(evictionPolicyClassName, Thread.currentThread().getContextClassLoader());
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
/*      */   public final void setEvictionPolicyClassName(String evictionPolicyClassName, ClassLoader classLoader) {
/* 1475 */     Class<?> epClass = EvictionPolicy.class;
/* 1476 */     ClassLoader epClassLoader = epClass.getClassLoader();
/*      */     try {
/*      */       try {
/* 1479 */         setEvictionPolicy(evictionPolicyClassName, classLoader);
/* 1480 */       } catch (ClassCastException|ClassNotFoundException e) {
/* 1481 */         setEvictionPolicy(evictionPolicyClassName, epClassLoader);
/*      */       } 
/* 1483 */     } catch (ClassCastException e) {
/* 1484 */       throw new IllegalArgumentException("Class " + evictionPolicyClassName + " from class loaders [" + classLoader + ", " + epClassLoader + "] do not implement " + EVICTION_POLICY_TYPE_NAME);
/*      */     }
/* 1486 */     catch (ClassNotFoundException|InstantiationException|IllegalAccessException|InvocationTargetException|NoSuchMethodException e) {
/*      */       
/* 1488 */       throw new IllegalArgumentException("Unable to create " + EVICTION_POLICY_TYPE_NAME + " instance of type " + evictionPolicyClassName, e);
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
/*      */   public final void setEvictorShutdownTimeout(Duration evictorShutdownTimeout) {
/* 1503 */     this.evictorShutdownTimeoutDuration = PoolImplUtils.nonNull(evictorShutdownTimeout, BaseObjectPoolConfig.DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT);
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
/*      */   @Deprecated
/*      */   public final void setEvictorShutdownTimeoutMillis(long evictorShutdownTimeoutMillis) {
/* 1516 */     setEvictorShutdownTimeout(Duration.ofMillis(evictorShutdownTimeoutMillis));
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
/*      */   public final void setLifo(boolean lifo) {
/* 1532 */     this.lifo = lifo;
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
/*      */   public final void setMaxTotal(int maxTotal) {
/* 1547 */     this.maxTotal = maxTotal;
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
/*      */   public final void setMaxWait(Duration maxWaitDuration) {
/* 1566 */     this.maxWaitDuration = PoolImplUtils.nonNull(maxWaitDuration, BaseObjectPoolConfig.DEFAULT_MAX_WAIT);
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
/*      */   @Deprecated
/*      */   public final void setMaxWaitMillis(long maxWaitMillis) {
/* 1586 */     setMaxWait(Duration.ofMillis(maxWaitMillis));
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
/*      */   public void setMessagesStatistics(boolean messagesDetails) {
/* 1600 */     this.messageStatistics = messagesDetails;
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
/*      */   @Deprecated
/*      */   public final void setMinEvictableIdle(Duration minEvictableIdleTime) {
/* 1620 */     this.minEvictableIdleDuration = PoolImplUtils.nonNull(minEvictableIdleTime, BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_DURATION);
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
/*      */   public final void setMinEvictableIdleDuration(Duration minEvictableIdleTime) {
/* 1638 */     this.minEvictableIdleDuration = PoolImplUtils.nonNull(minEvictableIdleTime, BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_DURATION);
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
/*      */   @Deprecated
/*      */   public final void setMinEvictableIdleTime(Duration minEvictableIdleTime) {
/* 1658 */     this.minEvictableIdleDuration = PoolImplUtils.nonNull(minEvictableIdleTime, BaseObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_DURATION);
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
/*      */   @Deprecated
/*      */   public final void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
/* 1677 */     setMinEvictableIdleTime(Duration.ofMillis(minEvictableIdleTimeMillis));
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
/*      */   public final void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
/* 1697 */     this.numTestsPerEvictionRun = numTestsPerEvictionRun;
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
/*      */   @Deprecated
/*      */   public final void setSoftMinEvictableIdle(Duration softMinEvictableIdleTime) {
/* 1721 */     this.softMinEvictableIdleDuration = PoolImplUtils.nonNull(softMinEvictableIdleTime, BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION);
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
/*      */   public final void setSoftMinEvictableIdleDuration(Duration softMinEvictableIdleTime) {
/* 1743 */     this.softMinEvictableIdleDuration = PoolImplUtils.nonNull(softMinEvictableIdleTime, BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION);
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
/*      */   @Deprecated
/*      */   public final void setSoftMinEvictableIdleTime(Duration softMinEvictableIdleTime) {
/* 1767 */     this.softMinEvictableIdleDuration = PoolImplUtils.nonNull(softMinEvictableIdleTime, BaseObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION);
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
/*      */   @Deprecated
/*      */   public final void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
/* 1790 */     setSoftMinEvictableIdleTime(Duration.ofMillis(softMinEvictableIdleTimeMillis));
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
/*      */   public final void setSwallowedExceptionListener(SwallowedExceptionListener swallowedExceptionListener) {
/* 1802 */     this.swallowedExceptionListener = swallowedExceptionListener;
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
/*      */   public final void setTestOnBorrow(boolean testOnBorrow) {
/* 1820 */     this.testOnBorrow = testOnBorrow;
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
/*      */   public final void setTestOnCreate(boolean testOnCreate) {
/* 1839 */     this.testOnCreate = testOnCreate;
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
/*      */   public final void setTestOnReturn(boolean testOnReturn) {
/* 1856 */     this.testOnReturn = testOnReturn;
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
/*      */   public final void setTestWhileIdle(boolean testWhileIdle) {
/* 1876 */     this.testWhileIdle = testWhileIdle;
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
/*      */   @Deprecated
/*      */   public final void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
/* 1895 */     this.durationBetweenEvictionRuns = PoolImplUtils.nonNull(timeBetweenEvictionRuns, BaseObjectPoolConfig.DEFAULT_DURATION_BETWEEN_EVICTION_RUNS);
/* 1896 */     startEvictor(this.durationBetweenEvictionRuns);
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
/*      */   @Deprecated
/*      */   public final void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
/* 1914 */     setTimeBetweenEvictionRuns(Duration.ofMillis(timeBetweenEvictionRunsMillis));
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
/*      */   final void startEvictor(Duration delay) {
/* 1928 */     synchronized (this.evictionLock) {
/* 1929 */       boolean isPositiverDelay = PoolImplUtils.isPositive(delay);
/* 1930 */       if (this.evictor == null) {
/* 1931 */         if (isPositiverDelay) {
/* 1932 */           this.evictor = new Evictor();
/* 1933 */           EvictionTimer.schedule(this.evictor, delay, delay);
/*      */         } 
/* 1935 */       } else if (isPositiverDelay) {
/* 1936 */         synchronized (EvictionTimer.class) {
/* 1937 */           EvictionTimer.cancel(this.evictor, this.evictorShutdownTimeoutDuration, true);
/* 1938 */           this.evictor = null;
/* 1939 */           this.evictionIterator = null;
/* 1940 */           this.evictor = new Evictor();
/* 1941 */           EvictionTimer.schedule(this.evictor, delay, delay);
/*      */         } 
/*      */       } else {
/* 1944 */         EvictionTimer.cancel(this.evictor, this.evictorShutdownTimeoutDuration, false);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void stopEvictor() {
/* 1953 */     startEvictor(Duration.ofMillis(-1L));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void swallowException(Exception swallowException) {
/* 1963 */     SwallowedExceptionListener listener = getSwallowedExceptionListener();
/*      */     
/* 1965 */     if (listener == null) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/* 1970 */       listener.onSwallowException(swallowException);
/* 1971 */     } catch (VirtualMachineError e) {
/* 1972 */       throw e;
/* 1973 */     } catch (Throwable throwable) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void toStringAppendFields(StringBuilder builder) {
/* 1980 */     builder.append("maxTotal=");
/* 1981 */     builder.append(this.maxTotal);
/* 1982 */     builder.append(", blockWhenExhausted=");
/* 1983 */     builder.append(this.blockWhenExhausted);
/* 1984 */     builder.append(", maxWaitDuration=");
/* 1985 */     builder.append(this.maxWaitDuration);
/* 1986 */     builder.append(", lifo=");
/* 1987 */     builder.append(this.lifo);
/* 1988 */     builder.append(", fairness=");
/* 1989 */     builder.append(this.fairness);
/* 1990 */     builder.append(", testOnCreate=");
/* 1991 */     builder.append(this.testOnCreate);
/* 1992 */     builder.append(", testOnBorrow=");
/* 1993 */     builder.append(this.testOnBorrow);
/* 1994 */     builder.append(", testOnReturn=");
/* 1995 */     builder.append(this.testOnReturn);
/* 1996 */     builder.append(", testWhileIdle=");
/* 1997 */     builder.append(this.testWhileIdle);
/* 1998 */     builder.append(", durationBetweenEvictionRuns=");
/* 1999 */     builder.append(this.durationBetweenEvictionRuns);
/* 2000 */     builder.append(", numTestsPerEvictionRun=");
/* 2001 */     builder.append(this.numTestsPerEvictionRun);
/* 2002 */     builder.append(", minEvictableIdleTimeDuration=");
/* 2003 */     builder.append(this.minEvictableIdleDuration);
/* 2004 */     builder.append(", softMinEvictableIdleTimeDuration=");
/* 2005 */     builder.append(this.softMinEvictableIdleDuration);
/* 2006 */     builder.append(", evictionPolicy=");
/* 2007 */     builder.append(this.evictionPolicy);
/* 2008 */     builder.append(", closeLock=");
/* 2009 */     builder.append(this.closeLock);
/* 2010 */     builder.append(", closed=");
/* 2011 */     builder.append(this.closed);
/* 2012 */     builder.append(", evictionLock=");
/* 2013 */     builder.append(this.evictionLock);
/* 2014 */     builder.append(", evictor=");
/* 2015 */     builder.append(this.evictor);
/* 2016 */     builder.append(", evictionIterator=");
/* 2017 */     builder.append(this.evictionIterator);
/* 2018 */     builder.append(", factoryClassLoader=");
/* 2019 */     builder.append(this.factoryClassLoader);
/* 2020 */     builder.append(", oname=");
/* 2021 */     builder.append(this.objectName);
/* 2022 */     builder.append(", creationStackTrace=");
/* 2023 */     builder.append(this.creationStackTrace);
/* 2024 */     builder.append(", borrowedCount=");
/* 2025 */     builder.append(this.borrowedCount);
/* 2026 */     builder.append(", returnedCount=");
/* 2027 */     builder.append(this.returnedCount);
/* 2028 */     builder.append(", createdCount=");
/* 2029 */     builder.append(this.createdCount);
/* 2030 */     builder.append(", destroyedCount=");
/* 2031 */     builder.append(this.destroyedCount);
/* 2032 */     builder.append(", destroyedByEvictorCount=");
/* 2033 */     builder.append(this.destroyedByEvictorCount);
/* 2034 */     builder.append(", destroyedByBorrowValidationCount=");
/* 2035 */     builder.append(this.destroyedByBorrowValidationCount);
/* 2036 */     builder.append(", activeTimes=");
/* 2037 */     builder.append(this.activeTimes);
/* 2038 */     builder.append(", idleTimes=");
/* 2039 */     builder.append(this.idleTimes);
/* 2040 */     builder.append(", waitTimes=");
/* 2041 */     builder.append(this.waitTimes);
/* 2042 */     builder.append(", maxBorrowWaitDuration=");
/* 2043 */     builder.append(this.maxBorrowWaitDuration);
/* 2044 */     builder.append(", swallowedExceptionListener=");
/* 2045 */     builder.append(this.swallowedExceptionListener);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void updateStatsBorrow(PooledObject<T> p, Duration waitDuration) {
/*      */     Duration currentMaxDuration;
/* 2055 */     this.borrowedCount.incrementAndGet();
/* 2056 */     this.idleTimes.add(p.getIdleDuration());
/* 2057 */     this.waitTimes.add(waitDuration);
/*      */ 
/*      */ 
/*      */     
/*      */     do {
/* 2062 */       currentMaxDuration = this.maxBorrowWaitDuration.get();
/* 2063 */       if (currentMaxDuration.compareTo(waitDuration) >= 0) {
/*      */         break;
/*      */       }
/* 2066 */     } while (!this.maxBorrowWaitDuration.compareAndSet(currentMaxDuration, waitDuration));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   final void updateStatsReturn(Duration activeTime) {
/* 2076 */     this.returnedCount.incrementAndGet();
/* 2077 */     this.activeTimes.add(activeTime);
/*      */   }
/*      */   
/*      */   public abstract void close();
/*      */   
/*      */   abstract void ensureMinIdle() throws Exception;
/*      */   
/*      */   public abstract void evict() throws Exception;
/*      */   
/*      */   public abstract int getNumIdle();
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\BaseGenericObjectPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */