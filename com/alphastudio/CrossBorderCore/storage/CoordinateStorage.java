/*     */ package com.alphastudio.CrossBorderCore.storage;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.ThreadPoolExecutor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.function.Supplier;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import com.alphastudio.CrossBorderCore.CrossBorderCore;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import redis.clients.jedis.Jedis;
/*     */ import redis.clients.jedis.Transaction;
/*     */ import com.alphastudio.CrossBorderCore.redis.RedisManager;
/*     */ 
/*     */ public class CoordinateStorage
/*     */ {
/*     */   private final CrossBorderCore plugin;
/*     */   private final Gson gson;
/*     */   private RedisManager redisManager;
/*     */   private final boolean redisEnabled;
/*     */   private final String keyPrefix;
/*     */   private final ThreadPoolExecutor storageExecutor;
/*     */   private final Map<String, CachedServerStatus> serverStatusCache = new java.util.concurrent.ConcurrentHashMap<>();
/*     */   private final AtomicBoolean statusRefreshInProgress = new AtomicBoolean();
/*     */   private final long serverStatusCacheTtlMs;
/*     */   private static final AtomicInteger STORAGE_THREAD_SEQUENCE = new AtomicInteger();
/*     */   private static final int REDIS_EXPIRE_SECONDS = 30;
/*     */   private static final int VEHICLE_REDIS_EXPIRE_SECONDS = 60;
/*     */
/*     */   private static class CachedServerStatus {
/*     */     private final boolean online;
/*     */     private final long checkedAt;
/*     */
/*     */     private CachedServerStatus(boolean online, long checkedAt) {
/*     */       this.online = online;
/*     */       this.checkedAt = checkedAt;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PendingTeleport
/*     */   {
/*     */     public double x;
/*     */     public double y;
/*     */     public double z;
/*     */     public float yaw;
/*     */     public float pitch;
/*     */     public String world;
/*     */     public long timestamp;
/*     */     public boolean isFlying;
/*     */     public boolean allowFlight;
/*     */     public boolean isGliding;
/*     */     public boolean isCrawling;
/*     */     public float flySpeed;
/*     */     public float walkSpeed;
/*     */     public List<EntityData> entities;
/*     */     public List<BlockData> blocks;
/*     */     public String borderAxis;
/*     */     public int borderCoordinate;
/*     */     public String borderDirection;
/*     */     public UUID vehicleUUID;
/*     */     public double velocityX;
/*     */     public double velocityY;
/*     */     public double velocityZ;
/*     */     public boolean sprinting;
/*     */     public long arrivalProtectedUntil;
/*     */     public UUID transferId;
/*     */     public String sourceServer;
/*     */     public String targetServer;
/*     */     
/*     */     public PendingTeleport(double x, double y, double z, float yaw, float pitch, String world) {
/*  45 */       this.x = x;
/*  46 */       this.y = y;
/*  47 */       this.z = z;
/*  48 */       this.yaw = yaw;
/*  49 */       this.pitch = pitch;
/*  50 */       this.world = world;
/*  51 */       this.timestamp = System.currentTimeMillis();
/*     */       
/*  53 */       this.isFlying = false;
/*  54 */       this.allowFlight = false;
/*  55 */       this.isGliding = false;
/*  56 */       this.isCrawling = false;
/*  57 */       this.flySpeed = 0.1F;
/*  58 */       this.walkSpeed = 0.2F;
/*  59 */       this.velocityX = 0.0D;
/*  60 */       this.velocityY = 0.0D;
/*  61 */       this.velocityZ = 0.0D;
/*  62 */       this.sprinting = false;
/*  63 */       this.arrivalProtectedUntil = 0L;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PendingTeleport(double x, double y, double z, float yaw, float pitch, String world, boolean isFlying, boolean allowFlight, boolean isGliding, boolean isCrawling, float flySpeed, float walkSpeed) {
/*  64 */       this(x, y, z, yaw, pitch, world, isFlying, allowFlight, isGliding, isCrawling, flySpeed, walkSpeed, 0.0D, 0.0D, 0.0D, false, 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PendingTeleport(double x, double y, double z, float yaw, float pitch, String world, boolean isFlying, boolean allowFlight, boolean isGliding, boolean isCrawling, float flySpeed, float walkSpeed, double velocityX, double velocityY, double velocityZ, boolean sprinting, long arrivalProtectedUntil) {
/*  70 */       this.x = x;
/*  71 */       this.y = y;
/*  72 */       this.z = z;
/*  73 */       this.yaw = yaw;
/*  74 */       this.pitch = pitch;
/*  75 */       this.world = world;
/*  76 */       this.timestamp = System.currentTimeMillis();
/*  77 */       this.isFlying = isFlying;
/*  78 */       this.allowFlight = allowFlight;
/*  79 */       this.isGliding = isGliding;
/*  80 */       this.isCrawling = isCrawling;
/*  81 */       this.flySpeed = flySpeed;
/*  82 */       this.walkSpeed = walkSpeed;
/*  83 */       this.velocityX = velocityX;
/*  84 */       this.velocityY = velocityY;
/*  85 */       this.velocityZ = velocityZ;
/*  86 */       this.sprinting = sprinting;
/*  87 */       this.arrivalProtectedUntil = arrivalProtectedUntil;
/*  88 */       this.entities = new ArrayList<>();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PendingTeleport(double x, double y, double z, float yaw, float pitch, String world, boolean isFlying, boolean allowFlight, boolean isGliding, boolean isCrawling, float flySpeed, float walkSpeed, List<EntityData> entities) {
/*  93 */       this(x, y, z, yaw, pitch, world, isFlying, allowFlight, isGliding, isCrawling, flySpeed, walkSpeed);
/*  94 */       this.entities = (entities != null) ? entities : new ArrayList<>();
/*  95 */       this.blocks = new ArrayList<>();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public PendingTeleport(double x, double y, double z, float yaw, float pitch, String world, boolean isFlying, boolean allowFlight, boolean isGliding, boolean isCrawling, float flySpeed, float walkSpeed, List<EntityData> entities, List<BlockData> blocks, String borderAxis, int borderCoordinate, String borderDirection) {
/* 102 */       this(x, y, z, yaw, pitch, world, isFlying, allowFlight, isGliding, isCrawling, flySpeed, walkSpeed, entities, blocks, borderAxis, borderCoordinate, borderDirection, 0.0D, 0.0D, 0.0D, false, 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public PendingTeleport(double x, double y, double z, float yaw, float pitch, String world, boolean isFlying, boolean allowFlight, boolean isGliding, boolean isCrawling, float flySpeed, float walkSpeed, List<EntityData> entities, List<BlockData> blocks, String borderAxis, int borderCoordinate, String borderDirection, double velocityX, double velocityY, double velocityZ, boolean sprinting, long arrivalProtectedUntil) {
/* 108 */       this(x, y, z, yaw, pitch, world, isFlying, allowFlight, isGliding, isCrawling, flySpeed, walkSpeed, velocityX, velocityY, velocityZ, sprinting, arrivalProtectedUntil);
/* 109 */       this.entities = (entities != null) ? entities : new ArrayList<>();
/* 110 */       this.blocks = (blocks != null) ? blocks : new ArrayList<>();
/* 111 */       this.borderAxis = borderAxis;
/* 112 */       this.borderCoordinate = borderCoordinate;
/* 113 */       this.borderDirection = borderDirection;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CoordinateStorage(CrossBorderCore plugin) {
/* 105 */     this.plugin = plugin;
/*     */     int asyncThreads = Math.max(1, plugin.getConfig().getInt("redis.async-threads", 2));
/*     */     int asyncQueueCapacity = Math.max(16, plugin.getConfig().getInt("redis.async-queue-capacity", 256));
/*     */     this.serverStatusCacheTtlMs = Math.max(500L, plugin.getConfig().getLong("redis.status-cache-ttl-ms", 3000L));
/*     */     this.storageExecutor = new ThreadPoolExecutor(
/*     */         asyncThreads,
/*     */         asyncThreads,
/*     */         0L,
/*     */         TimeUnit.MILLISECONDS,
/*     */         new ArrayBlockingQueue<>(asyncQueueCapacity),
/*     */         runnable -> {
/*     */           Thread thread = new Thread(runnable, "CrossBorderCore-Redis-" + STORAGE_THREAD_SEQUENCE.incrementAndGet());
/*     */           thread.setDaemon(true);
/*     */           return thread;
/*     */         },
/*     */         new ThreadPoolExecutor.AbortPolicy());
/* 106 */     this
/*     */ 
/*     */ 
/*     */       
/* 110 */       .gson = (new GsonBuilder()).disableHtmlEscaping().registerTypeAdapter(ItemStack.class, new ItemStackAdapter()).create();
/*     */ 
/*     */     
/* 113 */     String redisHost = plugin.getConfig().getString("redis.host", "localhost");
/* 114 */     int redisPort = plugin.getConfig().getInt("redis.port", 6379);
/* 115 */     String redisPassword = plugin.getConfig().getString("redis.password", "");
/* 116 */     int redisDatabase = plugin.getConfig().getInt("redis.database", 0);
/* 117 */     this.keyPrefix = plugin.getConfig().getString("redis.key-prefix", "crossservertp:");
/*     */     boolean redisConfigured = plugin.getConfig().getBoolean("redis.enabled", true);
/*     */     if (!redisConfigured) {
/*     */       this.redisEnabled = false;
/*     */       plugin.getLogger().info("Redis is disabled. Local-world transfers remain available; remote-server transfers are unavailable.");
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 120 */     int maxTotal = plugin.getConfig().getInt("redis.pool.max-total", 8);
/* 121 */     int maxIdle = plugin.getConfig().getInt("redis.pool.max-idle", 8);
/* 122 */     int minIdle = plugin.getConfig().getInt("redis.pool.min-idle", 1);
/*     */ 
/*     */     
/* 125 */     int connectionTimeout = plugin.getConfig().getInt("redis.timeout.connection", 2000);
/*     */ 
/*     */     
/* 128 */     int maxRetries = plugin.getConfig().getInt("redis.reconnect.max-retries", 2);
/* 129 */     int retryDelay = plugin.getConfig().getInt("redis.reconnect.initial-delay-ms", 250);
/*     */ 
/*     */     
/* 132 */     boolean connected = false;
/* 133 */     RedisManager tempRedisManager = null;
/*     */     
/*     */     try {
/* 136 */       tempRedisManager = new RedisManager(redisHost, redisPort, redisPassword, redisDatabase, maxTotal, maxIdle, minIdle, connectionTimeout, maxRetries, retryDelay, plugin.getLogger());
/*     */ 
/*     */       
/* 139 */       int maxStartupRetries = 2;
/* 140 */       int startupRetryDelay = 500;
/* 141 */       Exception lastException = null;
/*     */       
/* 143 */       for (int attempt = 0; attempt <= maxStartupRetries; attempt++) { 
/* 144 */         try { Jedis jedis = tempRedisManager.getJedisPool().getResource(); 
/* 145 */           try { jedis.ping();
/* 146 */             verifyWritableRedis(jedis);
/* 147 */             connected = true;
/*     */             
/* 149 */             if (jedis != null) jedis.close();  } catch (Throwable throwable) { if (jedis != null) try { jedis.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  break; } catch (Exception e)
/* 150 */         { lastException = e;
/* 151 */           if (attempt < maxStartupRetries) {
/* 152 */             plugin.getLogger().warning("Redis connection attempt " + (attempt + 1) + "/" + (maxStartupRetries + 1) + " failed: " + e
/* 153 */                 .getMessage() + ". Retrying in " + startupRetryDelay + "ms...");
/*     */             try {
/* 155 */               Thread.sleep(startupRetryDelay);
/* 156 */             } catch (InterruptedException ie) {
/* 157 */               Thread.currentThread().interrupt();
/*     */               
/*     */               break;
/*     */             } 
/*     */           }  }
/*     */          }
/*     */       
/* 163 */       if (connected) {
/* 164 */         plugin.getLogger().info("Successfully connected to Redis at " + redisHost + ":" + redisPort + " (database: " + redisDatabase + ", prefix: " + this.keyPrefix + ")");
/*     */         
/* 166 */         plugin.getLogger().info("Pool settings - Max: " + maxTotal + ", Idle: " + maxIdle + ", Timeout: " + connectionTimeout + "ms");
/*     */ 
/*     */         
/* 169 */         this.redisManager = tempRedisManager;
/* 170 */         tempRedisManager = null;
/*     */       } else {
/* 172 */         throw lastException;
/*     */       } 
/* 174 */     } catch (Exception e) {
/* 175 */       plugin.getLogger().warning("Failed to connect to Redis after 3 attempts: " + e.getMessage());
/* 176 */       plugin.getLogger().warning("Remote-server transfers will NOT work without Redis.");
/* 177 */       plugin.getLogger().warning("Local-world transfers remain available.");
/* 178 */       plugin.getLogger().warning("Please ensure Redis is running and the configuration is correct.");
/*     */     } finally {
/*     */       
/* 181 */       if (tempRedisManager != null) {
/* 182 */         tempRedisManager.close();
/*     */       }
/*     */     } 
/*     */     
/* 186 */     this.redisEnabled = connected;
/*     */   }
/*     */ 
/*     */   private void verifyWritableRedis(Jedis jedis) {
/* 190 */     String probeKey = this.keyPrefix + "write-probe:" + UUID.randomUUID();
/*     */     try {
/* 192 */       jedis.setex(probeKey, 5L, "ok");
/* 193 */       jedis.del(probeKey);
/*     */     } catch (Exception e) {
/* 195 */       throw new IllegalStateException("Redis is reachable but not writable. CrossServerTP must use the writable primary/master endpoint, not a read-only replica.", e);
/*     */     } 
/*     */   }
/*     */
/*     */   public CompletableFuture<Boolean> updateServerStatusAsync(String serverName) {
/*     */     if (!this.redisEnabled || serverName == null || serverName.equals("change-me")) {
/*     */       return CompletableFuture.completedFuture(false);
/*     */     }
/*     */     return submitAsync(() -> {
/*     */       Boolean updated = this.redisManager.executeWithRetry(jedis -> {
/*     */         jedis.setex(this.keyPrefix + "status:" + serverName, 10L, "online");
/*     */         return Boolean.TRUE;
/*     */       });
/*     */       return Boolean.TRUE.equals(updated);
/*     */     });
/*     */   }
/*     */
/*     */   public boolean isServerOnlineCached(String serverName) {
/*     */     if (!this.redisEnabled || serverName == null || serverName.isBlank()) {
/*     */       return false;
/*     */     }
/*     */     CachedServerStatus status = this.serverStatusCache.get(serverName);
/*     */     return status != null
/*     */         && status.online
/*     */         && System.currentTimeMillis() - status.checkedAt <= this.serverStatusCacheTtlMs;
/*     */   }
/*     */
/*     */   public CompletableFuture<Boolean> refreshServerStatusesAsync(Collection<String> serverNames) {
/*     */     if (!this.redisEnabled || serverNames == null || serverNames.isEmpty()) {
/*     */       return CompletableFuture.completedFuture(false);
/*     */     }
/*     */     if (!this.statusRefreshInProgress.compareAndSet(false, true)) {
/*     */       return CompletableFuture.completedFuture(false);
/*     */     }
/*     */
/*     */     LinkedHashSet<String> distinctNames = new LinkedHashSet<>();
/*     */     for (String serverName : serverNames) {
/*     */       if (serverName != null && !serverName.isBlank()) {
/*     */         distinctNames.add(serverName);
/*     */       }
/*     */     }
/*     */     if (distinctNames.isEmpty()) {
/*     */       this.statusRefreshInProgress.set(false);
/*     */       return CompletableFuture.completedFuture(false);
/*     */     }
/*     */
/*     */     return submitAsync(() -> {
/*     */       List<String> names = new ArrayList<>(distinctNames);
/*     */       String[] keys = names.stream().map(name -> this.keyPrefix + "status:" + name).toArray(String[]::new);
/*     */       List<String> statuses = this.redisManager.executeWithRetry(jedis -> jedis.mget(keys));
/*     */       long checkedAt = System.currentTimeMillis();
/*     */       for (int index = 0; index < names.size(); index++) {
/*     */         boolean online = statuses != null && index < statuses.size() && statuses.get(index) != null;
/*     */         this.serverStatusCache.put(names.get(index), new CachedServerStatus(online, checkedAt));
/*     */       }
/*     */       return statuses != null;
/*     */     }).whenComplete((ignored, throwable) -> this.statusRefreshInProgress.set(false));
/*     */   }
/*     */
/*     */   public CompletableFuture<Boolean> storeTeleportsAsync(Map<UUID, PendingTeleport> teleports) {
/*     */     if (!this.redisEnabled || teleports == null || teleports.isEmpty()) {
/*     */       return CompletableFuture.completedFuture(false);
/*     */     }
/*     */     return submitAsync(() -> {
/*     */       Boolean stored = this.redisManager.executeWithRetry(jedis -> {
/*     */         Transaction transaction = jedis.multi();
/*     */         for (Map.Entry<UUID, PendingTeleport> entry : teleports.entrySet()) {
/*     */           PendingTeleport teleport = entry.getValue();
/*     */           int ttlSeconds = teleport.vehicleUUID == null ? REDIS_EXPIRE_SECONDS : VEHICLE_REDIS_EXPIRE_SECONDS;
/*     */           transaction.setex(this.keyPrefix + "teleport:" + entry.getKey(), ttlSeconds, this.gson.toJson(teleport));
/*     */         }
/*     */         transaction.exec();
/*     */         return Boolean.TRUE;
/*     */       });
/*     */       return Boolean.TRUE.equals(stored);
/*     */     });
/*     */   }
/*     */
/*     */   public CompletableFuture<PendingTeleport> getTeleportAsync(UUID playerId) {
/*     */     if (!this.redisEnabled) {
/*     */       return CompletableFuture.completedFuture(null);
/*     */     }
/*     */     return submitAsync(() -> this.redisManager.executeWithRetry(jedis -> {
/*     */       String json = jedis.get(this.keyPrefix + "teleport:" + playerId);
/*     */       return json == null ? null : this.gson.fromJson(json, PendingTeleport.class);
/*     */     }));
/*     */   }
/*     */
/*     */   public CompletableFuture<Boolean> removeTeleportAsync(UUID playerId) {
/*     */     if (!this.redisEnabled) {
/*     */       return CompletableFuture.completedFuture(false);
/*     */     }
/*     */     return submitAsync(() -> {
/*     */       Boolean removed = this.redisManager.executeWithRetry(jedis -> Boolean.valueOf(jedis.del(this.keyPrefix + "teleport:" + playerId) > 0L));
/*     */       return Boolean.TRUE.equals(removed);
/*     */     });
/*     */   }
/*     */
/*     */   private <T> CompletableFuture<T> submitAsync(Supplier<T> operation) {
/*     */     CompletableFuture<T> future = new CompletableFuture<>();
/*     */     try {
/*     */       this.storageExecutor.execute(() -> {
/*     */         try {
/*     */           future.complete(operation.get());
/*     */         } catch (Throwable throwable) {
/*     */           future.completeExceptionally(throwable);
/*     */         }
/*     */       });
/*     */     } catch (RejectedExecutionException exception) {
/*     */       future.completeExceptionally(exception);
/*     */     }
/*     */     return future;
/*     */   }
/*     */   
/*     */   public boolean isRedisEnabled() {
/* 332 */     return this.redisEnabled;
/*     */   }
/*     */   
/*     */   public void close() {
/*     */     this.storageExecutor.shutdownNow();
/* 336 */     if (this.redisManager != null) {
/* 337 */       this.redisManager.close();
/* 338 */       this.plugin.getLogger().info("Closed Redis connection pool");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\storage\CoordinateStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
