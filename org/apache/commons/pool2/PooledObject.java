/*     */ package org.apache.commons.pool2;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Deque;
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
/*     */ public interface PooledObject<T>
/*     */   extends Comparable<PooledObject<T>>
/*     */ {
/*     */   static boolean isNull(PooledObject<?> pooledObject) {
/*  44 */     return (pooledObject == null || pooledObject.getObject() == null);
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
/*     */ 
/*     */   
/*     */   default Duration getActiveDuration() {
/*  97 */     Instant lastReturnInstant = getLastReturnInstant();
/*  98 */     Instant lastBorrowInstant = getLastBorrowInstant();
/*     */     
/* 100 */     return lastReturnInstant.isAfter(lastBorrowInstant) ? 
/* 101 */       Duration.between(lastBorrowInstant, lastReturnInstant) : 
/* 102 */       Duration.between(lastBorrowInstant, Instant.now());
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
/*     */   @Deprecated
/*     */   default Duration getActiveTime() {
/* 116 */     return getActiveDuration();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default long getBorrowedCount() {
/* 137 */     return -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Instant getCreateInstant() {
/* 147 */     return Instant.ofEpochMilli(getCreateTime());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Duration getFullDuration() {
/* 167 */     return Duration.between(getCreateInstant(), Instant.now());
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
/*     */   default Duration getIdleDuration() {
/* 179 */     return Duration.ofMillis(getIdleTimeMillis());
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
/*     */   @Deprecated
/*     */   default Duration getIdleTime() {
/* 193 */     return Duration.ofMillis(getIdleTimeMillis());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Instant getLastBorrowInstant() {
/* 214 */     return Instant.ofEpochMilli(getLastBorrowTime());
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Instant getLastReturnInstant() {
/* 233 */     return Instant.ofEpochMilli(getLastReturnTime());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default Instant getLastUsedInstant() {
/* 254 */     return Instant.ofEpochMilli(getLastUsedTime());
/*     */   }
/*     */   
/*     */   default void setRequireFullStackTrace(boolean requireFullStackTrace) {}
/*     */   
/*     */   boolean allocate();
/*     */   
/*     */   int compareTo(PooledObject<T> paramPooledObject);
/*     */   
/*     */   boolean deallocate();
/*     */   
/*     */   boolean endEvictionTest(Deque<PooledObject<T>> paramDeque);
/*     */   
/*     */   boolean equals(Object paramObject);
/*     */   
/*     */   @Deprecated
/*     */   long getActiveTimeMillis();
/*     */   
/*     */   @Deprecated
/*     */   long getCreateTime();
/*     */   
/*     */   @Deprecated
/*     */   long getIdleTimeMillis();
/*     */   
/*     */   @Deprecated
/*     */   long getLastBorrowTime();
/*     */   
/*     */   @Deprecated
/*     */   long getLastReturnTime();
/*     */   
/*     */   @Deprecated
/*     */   long getLastUsedTime();
/*     */   
/*     */   T getObject();
/*     */   
/*     */   PooledObjectState getState();
/*     */   
/*     */   int hashCode();
/*     */   
/*     */   void invalidate();
/*     */   
/*     */   void markAbandoned();
/*     */   
/*     */   void markReturning();
/*     */   
/*     */   void printStackTrace(PrintWriter paramPrintWriter);
/*     */   
/*     */   void setLogAbandoned(boolean paramBoolean);
/*     */   
/*     */   boolean startEvictionTest();
/*     */   
/*     */   String toString();
/*     */   
/*     */   void use();
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\PooledObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */