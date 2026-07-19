/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import org.apache.commons.pool2.BaseObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseObjectPoolConfig<T>
/*     */   extends BaseObject
/*     */   implements Cloneable
/*     */ {
/*     */   public static final boolean DEFAULT_LIFO = true;
/*     */   public static final boolean DEFAULT_FAIRNESS = false;
/*     */   @Deprecated
/*     */   public static final long DEFAULT_MAX_WAIT_MILLIS = -1L;
/*  68 */   public static final Duration DEFAULT_MAX_WAIT = Duration.ofMillis(-1L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static final long DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS = 1800000L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   public static final Duration DEFAULT_MIN_EVICTABLE_IDLE_DURATION = Duration.ofMillis(1800000L);
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
/*  98 */   public static final Duration DEFAULT_MIN_EVICTABLE_IDLE_TIME = Duration.ofMillis(1800000L);
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
/*     */   public static final long DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 119 */   public static final Duration DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME = Duration.ofMillis(-1L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 128 */   public static final Duration DEFAULT_SOFT_MIN_EVICTABLE_IDLE_DURATION = Duration.ofMillis(-1L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static final long DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT_MILLIS = 10000L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 147 */   public static final Duration DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT = Duration.ofMillis(10000L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DEFAULT_NUM_TESTS_PER_EVICTION_RUN = 3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean DEFAULT_TEST_ON_CREATE = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean DEFAULT_TEST_ON_BORROW = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean DEFAULT_TEST_ON_RETURN = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean DEFAULT_TEST_WHILE_IDLE = false;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static final long DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS = -1L;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   public static final Duration DEFAULT_DURATION_BETWEEN_EVICTION_RUNS = Duration.ofMillis(-1L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/* 218 */   public static final Duration DEFAULT_TIME_BETWEEN_EVICTION_RUNS = Duration.ofMillis(-1L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean DEFAULT_BLOCK_WHEN_EXHAUSTED = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final boolean DEFAULT_JMX_ENABLE = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_JMX_NAME_PREFIX = "pool";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 248 */   public static final String DEFAULT_JMX_NAME_BASE = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   public static final String DEFAULT_EVICTION_POLICY_CLASS_NAME = DefaultEvictionPolicy.class.getName();
/*     */   
/*     */   private boolean lifo = true;
/*     */   
/*     */   private boolean fairness = false;
/*     */   
/* 262 */   private Duration maxWaitDuration = DEFAULT_MAX_WAIT;
/*     */   
/* 264 */   private Duration minEvictableIdleDuration = DEFAULT_MIN_EVICTABLE_IDLE_TIME;
/*     */   
/* 266 */   private Duration evictorShutdownTimeoutDuration = DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT;
/*     */   
/* 268 */   private Duration softMinEvictableIdleDuration = DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME;
/*     */   
/* 270 */   private int numTestsPerEvictionRun = 3;
/*     */   
/*     */   private EvictionPolicy<T> evictionPolicy;
/*     */   
/* 274 */   private String evictionPolicyClassName = DEFAULT_EVICTION_POLICY_CLASS_NAME;
/*     */   
/*     */   private boolean testOnCreate = false;
/*     */   
/*     */   private boolean testOnBorrow = false;
/*     */   
/*     */   private boolean testOnReturn = false;
/*     */   
/*     */   private boolean testWhileIdle = false;
/*     */   
/* 284 */   private Duration durationBetweenEvictionRuns = DEFAULT_DURATION_BETWEEN_EVICTION_RUNS;
/*     */ 
/*     */   
/*     */   private boolean blockWhenExhausted = true;
/*     */   
/*     */   private boolean jmxEnabled = true;
/*     */   
/* 291 */   private String jmxNamePrefix = "pool";
/*     */   
/* 293 */   private String jmxNameBase = DEFAULT_JMX_NAME_BASE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBlockWhenExhausted() {
/* 303 */     return this.blockWhenExhausted;
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
/*     */   public Duration getDurationBetweenEvictionRuns() {
/* 315 */     return this.durationBetweenEvictionRuns;
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
/*     */   public EvictionPolicy<T> getEvictionPolicy() {
/* 327 */     return this.evictionPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEvictionPolicyClassName() {
/* 338 */     return this.evictionPolicyClassName;
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
/*     */   public Duration getEvictorShutdownTimeout() {
/* 352 */     return this.evictorShutdownTimeoutDuration;
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
/*     */   public Duration getEvictorShutdownTimeoutDuration() {
/* 364 */     return this.evictorShutdownTimeoutDuration;
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
/*     */   @Deprecated
/*     */   public long getEvictorShutdownTimeoutMillis() {
/* 377 */     return this.evictorShutdownTimeoutDuration.toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getFairness() {
/* 388 */     return this.fairness;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getJmxEnabled() {
/* 397 */     return this.jmxEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJmxNameBase() {
/* 407 */     return this.jmxNameBase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getJmxNamePrefix() {
/* 416 */     return this.jmxNamePrefix;
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
/*     */   public boolean getLifo() {
/* 428 */     return this.lifo;
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
/*     */   public Duration getMaxWaitDuration() {
/* 440 */     return this.maxWaitDuration;
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
/*     */   @Deprecated
/*     */   public long getMaxWaitMillis() {
/* 453 */     return this.maxWaitDuration.toMillis();
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
/*     */   public Duration getMinEvictableIdleDuration() {
/* 465 */     return this.minEvictableIdleDuration;
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
/*     */   public Duration getMinEvictableIdleTime() {
/* 479 */     return this.minEvictableIdleDuration;
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
/*     */   @Deprecated
/*     */   public long getMinEvictableIdleTimeMillis() {
/* 492 */     return this.minEvictableIdleDuration.toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumTestsPerEvictionRun() {
/* 503 */     return this.numTestsPerEvictionRun;
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
/*     */   public Duration getSoftMinEvictableIdleDuration() {
/* 515 */     return this.softMinEvictableIdleDuration;
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
/*     */   public Duration getSoftMinEvictableIdleTime() {
/* 529 */     return this.softMinEvictableIdleDuration;
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
/*     */   @Deprecated
/*     */   public long getSoftMinEvictableIdleTimeMillis() {
/* 542 */     return this.softMinEvictableIdleDuration.toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTestOnBorrow() {
/* 553 */     return this.testOnBorrow;
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
/*     */   public boolean getTestOnCreate() {
/* 565 */     return this.testOnCreate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTestOnReturn() {
/* 576 */     return this.testOnReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getTestWhileIdle() {
/* 587 */     return this.testWhileIdle;
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
/*     */   public Duration getTimeBetweenEvictionRuns() {
/* 601 */     return this.durationBetweenEvictionRuns;
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
/*     */   @Deprecated
/*     */   public long getTimeBetweenEvictionRunsMillis() {
/* 614 */     return this.durationBetweenEvictionRuns.toMillis();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlockWhenExhausted(boolean blockWhenExhausted) {
/* 625 */     this.blockWhenExhausted = blockWhenExhausted;
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
/*     */   public void setEvictionPolicy(EvictionPolicy<T> evictionPolicy) {
/* 637 */     this.evictionPolicy = evictionPolicy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEvictionPolicyClassName(String evictionPolicyClassName) {
/* 648 */     this.evictionPolicyClassName = evictionPolicyClassName;
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
/*     */   public void setEvictorShutdownTimeout(Duration evictorShutdownTimeoutDuration) {
/* 660 */     this.evictorShutdownTimeoutDuration = PoolImplUtils.nonNull(evictorShutdownTimeoutDuration, DEFAULT_EVICTOR_SHUTDOWN_TIMEOUT);
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
/*     */   public void setEvictorShutdownTimeoutMillis(Duration evictorShutdownTimeout) {
/* 674 */     setEvictorShutdownTimeout(evictorShutdownTimeout);
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
/*     */   @Deprecated
/*     */   public void setEvictorShutdownTimeoutMillis(long evictorShutdownTimeoutMillis) {
/* 687 */     setEvictorShutdownTimeout(Duration.ofMillis(evictorShutdownTimeoutMillis));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFairness(boolean fairness) {
/* 698 */     this.fairness = fairness;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJmxEnabled(boolean jmxEnabled) {
/* 707 */     this.jmxEnabled = jmxEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJmxNameBase(String jmxNameBase) {
/* 717 */     this.jmxNameBase = jmxNameBase;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setJmxNamePrefix(String jmxNamePrefix) {
/* 726 */     this.jmxNamePrefix = jmxNamePrefix;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLifo(boolean lifo) {
/* 737 */     this.lifo = lifo;
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
/*     */   public void setMaxWait(Duration maxWaitDuration) {
/* 749 */     this.maxWaitDuration = PoolImplUtils.nonNull(maxWaitDuration, DEFAULT_MAX_WAIT);
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
/*     */   @Deprecated
/*     */   public void setMaxWaitMillis(long maxWaitMillis) {
/* 762 */     setMaxWait(Duration.ofMillis(maxWaitMillis));
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
/*     */   public void setMinEvictableIdleDuration(Duration minEvictableIdleTime) {
/* 774 */     this.minEvictableIdleDuration = PoolImplUtils.nonNull(minEvictableIdleTime, DEFAULT_MIN_EVICTABLE_IDLE_TIME);
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
/*     */   public void setMinEvictableIdleTime(Duration minEvictableIdleTime) {
/* 788 */     this.minEvictableIdleDuration = PoolImplUtils.nonNull(minEvictableIdleTime, DEFAULT_MIN_EVICTABLE_IDLE_TIME);
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
/*     */   @Deprecated
/*     */   public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
/* 801 */     this.minEvictableIdleDuration = Duration.ofMillis(minEvictableIdleTimeMillis);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
/* 812 */     this.numTestsPerEvictionRun = numTestsPerEvictionRun;
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
/*     */   public void setSoftMinEvictableIdleDuration(Duration softMinEvictableIdleTime) {
/* 824 */     this.softMinEvictableIdleDuration = PoolImplUtils.nonNull(softMinEvictableIdleTime, DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME);
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
/*     */   public void setSoftMinEvictableIdleTime(Duration softMinEvictableIdleTime) {
/* 838 */     this.softMinEvictableIdleDuration = PoolImplUtils.nonNull(softMinEvictableIdleTime, DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME);
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
/*     */   @Deprecated
/*     */   public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
/* 851 */     setSoftMinEvictableIdleTime(Duration.ofMillis(softMinEvictableIdleTimeMillis));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTestOnBorrow(boolean testOnBorrow) {
/* 862 */     this.testOnBorrow = testOnBorrow;
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
/*     */   public void setTestOnCreate(boolean testOnCreate) {
/* 874 */     this.testOnCreate = testOnCreate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTestOnReturn(boolean testOnReturn) {
/* 885 */     this.testOnReturn = testOnReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTestWhileIdle(boolean testWhileIdle) {
/* 896 */     this.testWhileIdle = testWhileIdle;
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
/*     */   public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
/* 908 */     this.durationBetweenEvictionRuns = PoolImplUtils.nonNull(timeBetweenEvictionRuns, DEFAULT_DURATION_BETWEEN_EVICTION_RUNS);
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
/*     */   @Deprecated
/*     */   public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
/* 921 */     setTimeBetweenEvictionRuns(Duration.ofMillis(timeBetweenEvictionRunsMillis));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void toStringAppendFields(StringBuilder builder) {
/* 926 */     builder.append("lifo=");
/* 927 */     builder.append(this.lifo);
/* 928 */     builder.append(", fairness=");
/* 929 */     builder.append(this.fairness);
/* 930 */     builder.append(", maxWaitDuration=");
/* 931 */     builder.append(this.maxWaitDuration);
/* 932 */     builder.append(", minEvictableIdleTime=");
/* 933 */     builder.append(this.minEvictableIdleDuration);
/* 934 */     builder.append(", softMinEvictableIdleTime=");
/* 935 */     builder.append(this.softMinEvictableIdleDuration);
/* 936 */     builder.append(", numTestsPerEvictionRun=");
/* 937 */     builder.append(this.numTestsPerEvictionRun);
/* 938 */     builder.append(", evictionPolicyClassName=");
/* 939 */     builder.append(this.evictionPolicyClassName);
/* 940 */     builder.append(", testOnCreate=");
/* 941 */     builder.append(this.testOnCreate);
/* 942 */     builder.append(", testOnBorrow=");
/* 943 */     builder.append(this.testOnBorrow);
/* 944 */     builder.append(", testOnReturn=");
/* 945 */     builder.append(this.testOnReturn);
/* 946 */     builder.append(", testWhileIdle=");
/* 947 */     builder.append(this.testWhileIdle);
/* 948 */     builder.append(", timeBetweenEvictionRuns=");
/* 949 */     builder.append(this.durationBetweenEvictionRuns);
/* 950 */     builder.append(", blockWhenExhausted=");
/* 951 */     builder.append(this.blockWhenExhausted);
/* 952 */     builder.append(", jmxEnabled=");
/* 953 */     builder.append(this.jmxEnabled);
/* 954 */     builder.append(", jmxNamePrefix=");
/* 955 */     builder.append(this.jmxNamePrefix);
/* 956 */     builder.append(", jmxNameBase=");
/* 957 */     builder.append(this.jmxNameBase);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\BaseObjectPoolConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */