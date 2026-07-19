/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.executors;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ThreadLocalRandom;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.ConnectionPool;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisBroadcastException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisClusterOperationException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisRedirectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ClusterConnectionProvider;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class ClusterCommandExecutor implements CommandExecutor {
/*  22 */   private final Logger log = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   public final ClusterConnectionProvider provider;
/*     */   
/*     */   protected final int maxAttempts;
/*     */   protected final Duration maxTotalRetriesDuration;
/*     */   
/*     */   public ClusterCommandExecutor(ClusterConnectionProvider provider, int maxAttempts, Duration maxTotalRetriesDuration) {
/*  30 */     this.provider = provider;
/*  31 */     this.maxAttempts = maxAttempts;
/*  32 */     this.maxTotalRetriesDuration = maxTotalRetriesDuration;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  37 */     this.provider.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> T broadcastCommand(CommandObject<T> commandObject) {
/*  42 */     Map<String, ConnectionPool> connectionMap = this.provider.getConnectionMap();
/*     */     
/*  44 */     boolean isErrored = false;
/*  45 */     T reply = null;
/*  46 */     JedisBroadcastException bcastError = new JedisBroadcastException();
/*  47 */     for (Map.Entry<String, ConnectionPool> entry : connectionMap.entrySet()) {
/*  48 */       HostAndPort node = HostAndPort.from(entry.getKey());
/*  49 */       ConnectionPool pool = entry.getValue();
/*  50 */       try (Connection connection = pool.getResource()) {
/*  51 */         T aReply = execute(connection, commandObject);
/*  52 */         bcastError.addReply(node, aReply);
/*  53 */         if (!isErrored)
/*  54 */           if (reply == null) {
/*  55 */             reply = aReply;
/*  56 */           } else if (!reply.equals(aReply)) {
/*     */ 
/*     */             
/*  59 */             isErrored = true;
/*  60 */             reply = null;
/*     */           }  
/*  62 */       } catch (Exception anError) {
/*  63 */         bcastError.addReply(node, anError);
/*  64 */         isErrored = true;
/*     */       } 
/*     */     } 
/*  67 */     if (isErrored) {
/*  68 */       throw bcastError;
/*     */     }
/*  70 */     return reply;
/*     */   }
/*     */   
/*     */   public final <T> T executeCommand(CommandObject<T> commandObject) {
/*     */     JedisRedirectionException jedisRedirectionException1;
/*  75 */     Instant deadline = Instant.now().plus(this.maxTotalRetriesDuration);
/*     */     
/*  77 */     JedisRedirectionException redirect = null;
/*  78 */     int consecutiveConnectionFailures = 0;
/*  79 */     Exception lastException = null;
/*  80 */     for (int attemptsLeft = this.maxAttempts; attemptsLeft > 0; attemptsLeft--) {
/*  81 */       JedisConnectionException jedisConnectionException; Connection connection = null;
/*     */       try {
/*  83 */         if (redirect != null) {
/*  84 */           connection = this.provider.getConnection(redirect.getTargetNode());
/*  85 */           if (redirect instanceof org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisAskDataException)
/*     */           {
/*  87 */             connection.executeCommand((ProtocolCommand)Protocol.Command.ASKING);
/*     */           }
/*     */         } else {
/*  90 */           connection = this.provider.getConnection(commandObject.getArguments());
/*     */         } 
/*     */         
/*  93 */         return (T)execute(connection, (CommandObject)commandObject);
/*     */       }
/*  95 */       catch (JedisClusterOperationException jnrcne) {
/*  96 */         throw jnrcne;
/*  97 */       } catch (JedisConnectionException jce) {
/*  98 */         jedisConnectionException = jce;
/*  99 */         consecutiveConnectionFailures++;
/* 100 */         this.log.debug("Failed connecting to Redis: {}", connection, jce);
/*     */         
/* 102 */         boolean reset = handleConnectionProblem(attemptsLeft - 1, consecutiveConnectionFailures, deadline);
/* 103 */         if (reset) {
/* 104 */           consecutiveConnectionFailures = 0;
/* 105 */           redirect = null;
/*     */         } 
/* 107 */       } catch (JedisRedirectionException jre) {
/*     */         
/* 109 */         if (jedisConnectionException == null || jedisConnectionException instanceof JedisRedirectionException) {
/* 110 */           jedisRedirectionException1 = jre;
/*     */         }
/* 112 */         this.log.debug("Redirected by server to {}", jre.getTargetNode());
/* 113 */         consecutiveConnectionFailures = 0;
/* 114 */         redirect = jre;
/*     */         
/* 116 */         if (jre instanceof org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisMovedDataException)
/*     */         {
/* 118 */           this.provider.renewSlotCache(connection);
/*     */         }
/*     */       } finally {
/* 121 */         IOUtils.closeQuietly((AutoCloseable)connection);
/*     */       } 
/* 123 */       if (Instant.now().isAfter(deadline)) {
/* 124 */         throw new JedisClusterOperationException("Cluster retry deadline exceeded.");
/*     */       }
/*     */     } 
/*     */     
/* 128 */     JedisClusterOperationException maxAttemptsException = new JedisClusterOperationException("No more cluster attempts left.");
/*     */     
/* 130 */     maxAttemptsException.addSuppressed((Throwable)jedisRedirectionException1);
/* 131 */     throw maxAttemptsException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected <T> T execute(Connection connection, CommandObject<T> commandObject) {
/* 139 */     return (T)connection.executeCommand(commandObject);
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
/*     */   private boolean handleConnectionProblem(int attemptsLeft, int consecutiveConnectionFailures, Instant doneDeadline) {
/* 152 */     if (this.maxAttempts < 3) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 158 */       if (attemptsLeft == 0) {
/* 159 */         this.provider.renewSlotCache();
/* 160 */         return true;
/*     */       } 
/* 162 */       return false;
/*     */     } 
/*     */     
/* 165 */     if (consecutiveConnectionFailures < 2) {
/* 166 */       return false;
/*     */     }
/*     */     
/* 169 */     sleep(getBackoffSleepMillis(attemptsLeft, doneDeadline));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 174 */     this.provider.renewSlotCache();
/* 175 */     return true;
/*     */   }
/*     */   
/*     */   private static long getBackoffSleepMillis(int attemptsLeft, Instant deadline) {
/* 179 */     if (attemptsLeft <= 0) {
/* 180 */       return 0L;
/*     */     }
/*     */     
/* 183 */     long millisLeft = Duration.between(Instant.now(), deadline).toMillis();
/* 184 */     if (millisLeft < 0L) {
/* 185 */       throw new JedisClusterOperationException("Cluster retry deadline exceeded.");
/*     */     }
/*     */     
/* 188 */     long maxBackOff = millisLeft / (attemptsLeft * attemptsLeft);
/* 189 */     return ThreadLocalRandom.current().nextLong(maxBackOff + 1L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void sleep(long sleepMillis) {
/*     */     try {
/* 198 */       TimeUnit.MILLISECONDS.sleep(sleepMillis);
/* 199 */     } catch (InterruptedException e) {
/* 200 */       throw new JedisClusterOperationException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\executors\ClusterCommandExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */