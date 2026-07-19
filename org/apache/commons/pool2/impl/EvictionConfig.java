/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.time.Duration;
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
/*     */ public class EvictionConfig
/*     */ {
/*  33 */   private static final Duration MAX_DURATION = Duration.ofMillis(Long.MAX_VALUE);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Duration idleEvictDuration;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Duration idleSoftEvictDuration;
/*     */ 
/*     */ 
/*     */   
/*     */   private final int minIdle;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EvictionConfig(Duration idleEvictDuration, Duration idleSoftEvictDuration, int minIdle) {
/*  52 */     this.idleEvictDuration = PoolImplUtils.isPositive(idleEvictDuration) ? idleEvictDuration : MAX_DURATION;
/*  53 */     this.idleSoftEvictDuration = PoolImplUtils.isPositive(idleSoftEvictDuration) ? idleSoftEvictDuration : MAX_DURATION;
/*  54 */     this.minIdle = minIdle;
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
/*     */   @Deprecated
/*     */   public EvictionConfig(long poolIdleEvictMillis, long poolIdleSoftEvictMillis, int minIdle) {
/*  72 */     this(Duration.ofMillis(poolIdleEvictMillis), Duration.ofMillis(poolIdleSoftEvictMillis), minIdle);
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
/*     */   public Duration getIdleEvictDuration() {
/*  87 */     return this.idleEvictDuration;
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
/*     */   @Deprecated
/*     */   public long getIdleEvictTime() {
/* 103 */     return this.idleEvictDuration.toMillis();
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
/*     */   @Deprecated
/*     */   public Duration getIdleEvictTimeDuration() {
/* 120 */     return this.idleEvictDuration;
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
/*     */   public Duration getIdleSoftEvictDuration() {
/* 135 */     return this.idleSoftEvictDuration;
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
/*     */   @Deprecated
/*     */   public long getIdleSoftEvictTime() {
/* 151 */     return this.idleSoftEvictDuration.toMillis();
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
/*     */   @Deprecated
/*     */   public Duration getIdleSoftEvictTimeDuration() {
/* 167 */     return this.idleSoftEvictDuration;
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
/*     */   public int getMinIdle() {
/* 180 */     return this.minIdle;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 188 */     StringBuilder builder = new StringBuilder();
/* 189 */     builder.append("EvictionConfig [idleEvictDuration=");
/* 190 */     builder.append(this.idleEvictDuration);
/* 191 */     builder.append(", idleSoftEvictDuration=");
/* 192 */     builder.append(this.idleSoftEvictDuration);
/* 193 */     builder.append(", minIdle=");
/* 194 */     builder.append(this.minIdle);
/* 195 */     builder.append("]");
/* 196 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\EvictionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */