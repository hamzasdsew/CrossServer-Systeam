/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.nio.charset.Charset;
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
/*     */ 
/*     */ public class AbandonedConfig
/*     */ {
/*  37 */   private static final Duration DEFAULT_REMOVE_ABANDONED_TIMEOUT_DURATION = Duration.ofMinutes(5L);
/*     */ 
/*     */   
/*     */   private boolean removeAbandonedOnBorrow;
/*     */ 
/*     */   
/*     */   private boolean removeAbandonedOnMaintenance;
/*     */ 
/*     */   
/*     */   public static AbandonedConfig copy(AbandonedConfig abandonedConfig) {
/*  47 */     return (abandonedConfig == null) ? null : new AbandonedConfig(abandonedConfig);
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
/*  64 */   private Duration removeAbandonedTimeoutDuration = DEFAULT_REMOVE_ABANDONED_TIMEOUT_DURATION;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean logAbandoned;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean requireFullStackTrace = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private PrintWriter logWriter = new PrintWriter(new OutputStreamWriter(System.out, Charset.defaultCharset()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useUsageTracking;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbandonedConfig() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private AbandonedConfig(AbandonedConfig abandonedConfig) {
/* 108 */     setLogAbandoned(abandonedConfig.getLogAbandoned());
/* 109 */     setLogWriter(abandonedConfig.getLogWriter());
/* 110 */     setRemoveAbandonedOnBorrow(abandonedConfig.getRemoveAbandonedOnBorrow());
/* 111 */     setRemoveAbandonedOnMaintenance(abandonedConfig.getRemoveAbandonedOnMaintenance());
/* 112 */     setRemoveAbandonedTimeout(abandonedConfig.getRemoveAbandonedTimeoutDuration());
/* 113 */     setUseUsageTracking(abandonedConfig.getUseUsageTracking());
/* 114 */     setRequireFullStackTrace(abandonedConfig.getRequireFullStackTrace());
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
/*     */   public boolean getLogAbandoned() {
/* 130 */     return this.logAbandoned;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() {
/* 141 */     return this.logWriter;
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
/*     */   public boolean getRemoveAbandonedOnBorrow() {
/* 157 */     return this.removeAbandonedOnBorrow;
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
/*     */   public boolean getRemoveAbandonedOnMaintenance() {
/* 176 */     return this.removeAbandonedOnMaintenance;
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
/*     */   public int getRemoveAbandonedTimeout() {
/* 193 */     return (int)this.removeAbandonedTimeoutDuration.getSeconds();
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
/*     */   public Duration getRemoveAbandonedTimeoutDuration() {
/* 209 */     return this.removeAbandonedTimeoutDuration;
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
/*     */   public boolean getRequireFullStackTrace() {
/* 224 */     return this.requireFullStackTrace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getUseUsageTracking() {
/* 235 */     return this.useUsageTracking;
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
/*     */   public void setLogAbandoned(boolean logAbandoned) {
/* 247 */     this.logAbandoned = logAbandoned;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter logWriter) {
/* 257 */     this.logWriter = logWriter;
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
/*     */   public void setRemoveAbandonedOnBorrow(boolean removeAbandonedOnBorrow) {
/* 269 */     this.removeAbandonedOnBorrow = removeAbandonedOnBorrow;
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
/*     */   public void setRemoveAbandonedOnMaintenance(boolean removeAbandonedOnMaintenance) {
/* 281 */     this.removeAbandonedOnMaintenance = removeAbandonedOnMaintenance;
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
/*     */   public void setRemoveAbandonedTimeout(Duration removeAbandonedTimeout) {
/* 298 */     this.removeAbandonedTimeoutDuration = PoolImplUtils.nonNull(removeAbandonedTimeout, DEFAULT_REMOVE_ABANDONED_TIMEOUT_DURATION);
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
/*     */   public void setRemoveAbandonedTimeout(int removeAbandonedTimeoutSeconds) {
/* 316 */     setRemoveAbandonedTimeout(Duration.ofSeconds(removeAbandonedTimeoutSeconds));
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
/*     */   public void setRequireFullStackTrace(boolean requireFullStackTrace) {
/* 329 */     this.requireFullStackTrace = requireFullStackTrace;
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
/*     */   public void setUseUsageTracking(boolean useUsageTracking) {
/* 343 */     this.useUsageTracking = useUsageTracking;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 351 */     StringBuilder builder = new StringBuilder();
/* 352 */     builder.append("AbandonedConfig [removeAbandonedOnBorrow=");
/* 353 */     builder.append(this.removeAbandonedOnBorrow);
/* 354 */     builder.append(", removeAbandonedOnMaintenance=");
/* 355 */     builder.append(this.removeAbandonedOnMaintenance);
/* 356 */     builder.append(", removeAbandonedTimeoutDuration=");
/* 357 */     builder.append(this.removeAbandonedTimeoutDuration);
/* 358 */     builder.append(", logAbandoned=");
/* 359 */     builder.append(this.logAbandoned);
/* 360 */     builder.append(", logWriter=");
/* 361 */     builder.append(this.logWriter);
/* 362 */     builder.append(", useUsageTracking=");
/* 363 */     builder.append(this.useUsageTracking);
/* 364 */     builder.append("]");
/* 365 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\AbandonedConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */