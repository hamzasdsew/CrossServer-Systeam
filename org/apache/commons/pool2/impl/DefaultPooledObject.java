/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.time.Clock;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Deque;
/*     */ import org.apache.commons.pool2.PooledObject;
/*     */ import org.apache.commons.pool2.PooledObjectState;
/*     */ import org.apache.commons.pool2.TrackedUse;
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
/*     */ public class DefaultPooledObject<T>
/*     */   implements PooledObject<T>
/*     */ {
/*     */   private final T object;
/*  43 */   private PooledObjectState state = PooledObjectState.IDLE;
/*  44 */   private final Clock systemClock = Clock.systemUTC();
/*  45 */   private final Instant createInstant = now();
/*     */   
/*  47 */   private volatile Instant lastBorrowInstant = this.createInstant;
/*  48 */   private volatile Instant lastUseInstant = this.createInstant;
/*  49 */   private volatile Instant lastReturnInstant = this.createInstant;
/*     */   private volatile boolean logAbandoned;
/*  51 */   private volatile CallStack borrowedBy = NoOpCallStack.INSTANCE;
/*  52 */   private volatile CallStack usedBy = NoOpCallStack.INSTANCE;
/*     */ 
/*     */ 
/*     */   
/*     */   private volatile long borrowedCount;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultPooledObject(T object) {
/*  62 */     this.object = object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean allocate() {
/*  72 */     if (this.state == PooledObjectState.IDLE) {
/*  73 */       this.state = PooledObjectState.ALLOCATED;
/*  74 */       this.lastBorrowInstant = now();
/*  75 */       this.lastUseInstant = this.lastBorrowInstant;
/*  76 */       this.borrowedCount++;
/*  77 */       if (this.logAbandoned) {
/*  78 */         this.borrowedBy.fillInStackTrace();
/*     */       }
/*  80 */       return true;
/*     */     } 
/*  82 */     if (this.state == PooledObjectState.EVICTION)
/*     */     {
/*  84 */       this.state = PooledObjectState.EVICTION_RETURN_TO_HEAD;
/*     */     }
/*     */ 
/*     */     
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(PooledObject<T> other) {
/*  93 */     int compareTo = getLastReturnInstant().compareTo(other.getLastReturnInstant());
/*  94 */     if (compareTo == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */       
/*  99 */       return System.identityHashCode(this) - System.identityHashCode(other);
/*     */     }
/* 101 */     return compareTo;
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
/*     */   public synchronized boolean deallocate() {
/* 114 */     if (this.state == PooledObjectState.ALLOCATED || this.state == PooledObjectState.RETURNING) {
/* 115 */       this.state = PooledObjectState.IDLE;
/* 116 */       this.lastReturnInstant = now();
/* 117 */       this.borrowedBy.clear();
/* 118 */       return true;
/*     */     } 
/*     */     
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean endEvictionTest(Deque<PooledObject<T>> idleQueue) {
/* 127 */     if (this.state == PooledObjectState.EVICTION) {
/* 128 */       this.state = PooledObjectState.IDLE;
/* 129 */       return true;
/*     */     } 
/* 131 */     if (this.state == PooledObjectState.EVICTION_RETURN_TO_HEAD) {
/* 132 */       this.state = PooledObjectState.IDLE;
/* 133 */       idleQueue.offerFirst(this);
/*     */     } 
/*     */     
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getActiveTimeMillis() {
/* 141 */     return getActiveDuration().toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getBorrowedCount() {
/* 151 */     return this.borrowedCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public Instant getCreateInstant() {
/* 156 */     return this.createInstant;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCreateTime() {
/* 161 */     return this.createInstant.toEpochMilli();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Duration getIdleDuration() {
/* 169 */     Duration elapsed = Duration.between(this.lastReturnInstant, now());
/* 170 */     return elapsed.isNegative() ? Duration.ZERO : elapsed;
/*     */   }
/*     */ 
/*     */   
/*     */   public Duration getIdleTime() {
/* 175 */     return getIdleDuration();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getIdleTimeMillis() {
/* 180 */     return getIdleDuration().toMillis();
/*     */   }
/*     */ 
/*     */   
/*     */   public Instant getLastBorrowInstant() {
/* 185 */     return this.lastBorrowInstant;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastBorrowTime() {
/* 190 */     return this.lastBorrowInstant.toEpochMilli();
/*     */   }
/*     */ 
/*     */   
/*     */   public Instant getLastReturnInstant() {
/* 195 */     return this.lastReturnInstant;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastReturnTime() {
/* 200 */     return this.lastReturnInstant.toEpochMilli();
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
/*     */   
/*     */   public Instant getLastUsedInstant() {
/* 214 */     if (this.object instanceof TrackedUse) {
/* 215 */       return PoolImplUtils.max(((TrackedUse)this.object).getLastUsedInstant(), this.lastUseInstant);
/*     */     }
/* 217 */     return this.lastUseInstant;
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
/*     */   
/*     */   public long getLastUsedTime() {
/* 231 */     return getLastUsedInstant().toEpochMilli();
/*     */   }
/*     */ 
/*     */   
/*     */   public T getObject() {
/* 236 */     return this.object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PooledObjectState getState() {
/* 245 */     return this.state;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void invalidate() {
/* 253 */     this.state = PooledObjectState.INVALID;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void markAbandoned() {
/* 261 */     this.state = PooledObjectState.ABANDONED;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void markReturning() {
/* 269 */     this.state = PooledObjectState.RETURNING;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Instant now() {
/* 278 */     return this.systemClock.instant();
/*     */   }
/*     */ 
/*     */   
/*     */   public void printStackTrace(PrintWriter writer) {
/* 283 */     boolean written = this.borrowedBy.printStackTrace(writer);
/* 284 */     written |= this.usedBy.printStackTrace(writer);
/* 285 */     if (written) {
/* 286 */       writer.flush();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLogAbandoned(boolean logAbandoned) {
/* 292 */     this.logAbandoned = logAbandoned;
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
/*     */ 
/*     */   
/*     */   public void setRequireFullStackTrace(boolean requireFullStackTrace) {
/* 307 */     this.borrowedBy = CallStackUtils.newCallStack("'Pooled object created' yyyy-MM-dd HH:mm:ss Z 'by the following code has not been returned to the pool:'", true, requireFullStackTrace);
/*     */ 
/*     */     
/* 310 */     this.usedBy = CallStackUtils.newCallStack("The last code to use this object was:", false, requireFullStackTrace);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean startEvictionTest() {
/* 316 */     if (this.state == PooledObjectState.IDLE) {
/* 317 */       this.state = PooledObjectState.EVICTION;
/* 318 */       return true;
/*     */     } 
/* 320 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 325 */     StringBuilder result = new StringBuilder();
/* 326 */     result.append("Object: ");
/* 327 */     result.append(this.object.toString());
/* 328 */     result.append(", State: ");
/* 329 */     synchronized (this) {
/* 330 */       result.append(this.state.toString());
/*     */     } 
/* 332 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void use() {
/* 338 */     this.lastUseInstant = now();
/* 339 */     this.usedBy.fillInStackTrace();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\DefaultPooledObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */