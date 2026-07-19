/*     */ package com.alphastudio.CrossBorderCore.redis;
/*     */ 
/*     */ import java.util.logging.Logger;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import redis.clients.jedis.Jedis;
/*     */ import redis.clients.jedis.JedisPool;
/*     */ import redis.clients.jedis.JedisPoolConfig;
/*     */ import redis.clients.jedis.exceptions.JedisException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RedisManager
/*     */ {
/*     */   private final JedisPool jedisPool;
/*     */   private final Logger logger;
/*     */   private final int maxRetries;
/*     */   private final int initialRetryDelayMs;
/*     */   private volatile boolean isShuttingDown = false;
/*     */   
/*     */   public RedisManager(String host, int port, String password, int database, int maxConnections, int minConnections, int timeout, Logger logger) {
/*  23 */     this(host, port, password, database, maxConnections, Math.max(1, maxConnections / 2), minConnections, timeout, 2, 250, logger);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public RedisManager(String host, int port, String password, int database, int maxConnections, int minConnections, int timeout, int maxRetries, int initialRetryDelayMs, Logger logger) {
/*     */     this(host, port, password, database, maxConnections, Math.max(1, maxConnections / 2), minConnections, timeout, maxRetries, initialRetryDelayMs, logger);
/*     */   }
/*     */
/*     */   public RedisManager(String host, int port, String password, int database, int maxConnections, int maxIdleConnections, int minConnections, int timeout, int maxRetries, int initialRetryDelayMs, Logger logger) {
/*  29 */     this.logger = logger;
/*  30 */     this.maxRetries = Math.max(0, maxRetries);
/*  31 */     this.initialRetryDelayMs = Math.max(1, initialRetryDelayMs);
/*     */     
/*     */     int safeMaxConnections = Math.max(1, maxConnections);
/*     */     int safeMaxIdleConnections = Math.max(0, Math.min(maxIdleConnections, safeMaxConnections));
/*     */     int safeMinConnections = Math.max(0, Math.min(minConnections, safeMaxIdleConnections));
/*  33 */     JedisPoolConfig poolConfig = new JedisPoolConfig();
/*  34 */     poolConfig.setMaxTotal(safeMaxConnections);
/*  35 */     poolConfig.setMaxIdle(safeMaxIdleConnections);
/*  36 */     poolConfig.setMinIdle(safeMinConnections);
/*  37 */     poolConfig.setTestOnBorrow(true);
/*  38 */     poolConfig.setTestOnReturn(true);
/*  39 */     poolConfig.setTestWhileIdle(true);
/*     */     
/*  41 */     if (password != null && !password.isEmpty()) {
/*  42 */       this.jedisPool = new JedisPool((GenericObjectPoolConfig)poolConfig, host, port, timeout, password, database);
/*     */     } else {
/*  44 */       this.jedisPool = new JedisPool((GenericObjectPoolConfig)poolConfig, host, port, timeout, null, database);
/*     */     } 
/*     */     
/*  47 */     logger.info("Redis connection established to " + host + ":" + port + " (DB: " + database + ")");
/*  48 */     logger.info("Redis reconnection settings: maxRetries=" + maxRetries + ", initialRetryDelay=" + initialRetryDelayMs + "ms");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int calculateBackoffDelay(int attemptNumber) {
/*  57 */     long delay = (long)this.initialRetryDelayMs * (1L << Math.min(attemptNumber, 16));
/*     */     return (int)Math.min(delay, 30000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T executeWithRetry(RedisOperation<T> operation) {
/*  98 */     return executeWithRetry(operation, 0);
/*     */   }
/*     */   
/*     */   private <T> T executeWithRetry(RedisOperation<T> operation, int attemptNumber) {
/* 102 */     if (this.isShuttingDown) {
/* 103 */       return null;
/*     */     }
/*     */     
/* 106 */     try { Jedis jedis = this.jedisPool.getResource(); 
/* 107 */       try { T t = operation.execute(jedis);
/* 108 */         if (jedis != null) jedis.close();  return t; } catch (Throwable throwable) { if (jedis != null) try { jedis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (JedisException e)
/* 109 */     { if (isNonRetriable(e)) {
/* 110 */         this.logger.severe("Redis operation failed with a non-retriable error: " + e.getMessage());
/* 111 */         return null;
/*     */       } 
/* 113 */       if (attemptNumber < this.maxRetries) {
/* 114 */         int delayMs = calculateBackoffDelay(attemptNumber);
/* 115 */         this.logger.warning("Redis operation failed (attempt " + (attemptNumber + 1) + "/" + (this.maxRetries + 1) + "): " + e.getMessage() + ". Retrying in " + delayMs + "ms...");
/*     */         
/*     */         try {
/* 118 */           Thread.sleep(delayMs);
/* 119 */         } catch (InterruptedException ie) {
/* 120 */           Thread.currentThread().interrupt();
/* 121 */           this.logger.warning("Retry interrupted");
/* 122 */           return null;
/*     */         } 
/*     */         
/* 125 */         return executeWithRetry(operation, attemptNumber + 1);
/*     */       } 
/* 127 */       this.logger.severe("Redis operation failed after " + (this.maxRetries + 1) + " attempts: " + e.getMessage());
/* 128 */       return null; }
/*     */   
/*     */   }
/*     */ 
/*     */   private boolean isNonRetriable(JedisException exception) {
/* 132 */     String message = exception.getMessage();
/* 133 */     if (message == null) {
/* 134 */       return false;
/*     */     }
/* 136 */     String normalized = message.toUpperCase();
/* 137 */     return (normalized.contains("READONLY") || normalized.contains("NOAUTH") || normalized.contains("WRONGPASS") || normalized.contains("NOPERM"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool getJedisPool() {
/* 145 */     return this.jedisPool;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 140 */     this.isShuttingDown = true;
/* 141 */     this.jedisPool.close();
/* 142 */     this.logger.info("Redis connection closed");
/*     */   }
/*     */   
/*     */   public boolean isShuttingDown() {
/* 146 */     return this.isShuttingDown;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface RedisOperation<T> {
/*     */     T execute(Jedis param1Jedis);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\redis\RedisManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
