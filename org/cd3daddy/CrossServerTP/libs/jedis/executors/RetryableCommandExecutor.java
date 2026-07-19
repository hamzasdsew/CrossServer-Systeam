/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.executors;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class RetryableCommandExecutor
/*     */   implements CommandExecutor
/*     */ {
/*  18 */   private final Logger log = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   protected final ConnectionProvider provider;
/*     */   
/*     */   protected final int maxAttempts;
/*     */   protected final Duration maxTotalRetriesDuration;
/*     */   
/*     */   public RetryableCommandExecutor(ConnectionProvider provider, int maxAttempts, Duration maxTotalRetriesDuration) {
/*  26 */     this.provider = provider;
/*  27 */     this.maxAttempts = maxAttempts;
/*  28 */     this.maxTotalRetriesDuration = maxTotalRetriesDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  33 */     IOUtils.closeQuietly((AutoCloseable)this.provider);
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> T executeCommand(CommandObject<T> commandObject) {
/*     */     JedisConnectionException jedisConnectionException;
/*  39 */     Instant deadline = Instant.now().plus(this.maxTotalRetriesDuration);
/*     */     
/*  41 */     int consecutiveConnectionFailures = 0;
/*  42 */     JedisException lastException = null;
/*  43 */     for (int attemptsLeft = this.maxAttempts; attemptsLeft > 0; attemptsLeft--) {
/*  44 */       Connection connection = null;
/*     */       try {
/*  46 */         connection = this.provider.getConnection(commandObject.getArguments());
/*     */         
/*  48 */         return (T)execute(connection, (CommandObject)commandObject);
/*     */       }
/*  50 */       catch (JedisConnectionException jce) {
/*  51 */         jedisConnectionException = jce;
/*  52 */         consecutiveConnectionFailures++;
/*  53 */         this.log.debug("Failed connecting to Redis: {}", connection, jce);
/*     */         
/*  55 */         boolean reset = handleConnectionProblem(attemptsLeft - 1, consecutiveConnectionFailures, deadline);
/*  56 */         if (reset) {
/*  57 */           consecutiveConnectionFailures = 0;
/*     */         }
/*     */       } finally {
/*  60 */         if (connection != null) {
/*  61 */           connection.close();
/*     */         }
/*     */       } 
/*  64 */       if (Instant.now().isAfter(deadline)) {
/*  65 */         throw new JedisException("Retry deadline exceeded.");
/*     */       }
/*     */     } 
/*     */     
/*  69 */     JedisException maxAttemptsException = new JedisException("No more attempts left.");
/*  70 */     maxAttemptsException.addSuppressed((Throwable)jedisConnectionException);
/*  71 */     throw maxAttemptsException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> T execute(Connection connection, CommandObject<T> commandObject) {
/*  79 */     return (T)connection.executeCommand(commandObject);
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
/*     */   private boolean handleConnectionProblem(int attemptsLeft, int consecutiveConnectionFailures, Instant doneDeadline) {
/*  93 */     if (consecutiveConnectionFailures < 2) {
/*  94 */       return false;
/*     */     }
/*     */     
/*  97 */     sleep(getBackoffSleepMillis(attemptsLeft, doneDeadline));
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   private static long getBackoffSleepMillis(int attemptsLeft, Instant deadline) {
/* 102 */     if (attemptsLeft <= 0) {
/* 103 */       return 0L;
/*     */     }
/*     */     
/* 106 */     long millisLeft = Duration.between(Instant.now(), deadline).toMillis();
/* 107 */     if (millisLeft < 0L) {
/* 108 */       throw new JedisException("Retry deadline exceeded.");
/*     */     }
/*     */     
/* 111 */     return millisLeft / (attemptsLeft * (attemptsLeft + 1));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sleep(long sleepMillis) {
/*     */     try {
/* 120 */       TimeUnit.MILLISECONDS.sleep(sleepMillis);
/* 121 */     } catch (InterruptedException e) {
/* 122 */       throw new JedisException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\executors\RetryableCommandExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */