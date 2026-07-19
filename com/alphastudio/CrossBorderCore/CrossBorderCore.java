/*     */ package com.alphastudio.CrossBorderCore;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.bukkit.command.CommandExecutor;
/*     */ import org.bukkit.command.TabCompleter;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import com.alphastudio.CrossBorderCore.commands.CrossBorderCoreCommand;
/*     */ import com.alphastudio.CrossBorderCore.config.ConfigManager;
/*     */ import com.alphastudio.CrossBorderCore.listeners.BorderListener;
/*     */ import com.alphastudio.CrossBorderCore.listeners.PlayerJoinListener;
/*     */ import com.alphastudio.CrossBorderCore.storage.CoordinateStorage;
/*     */ import com.alphastudio.CrossBorderCore.util.SchedulerAdapter;
/*     */ 
/*     */ public class CrossBorderCore extends JavaPlugin {
/*     */   private static CrossBorderCore instance;
/*     */   private ConfigManager configManager;
/*     */   private BorderListener borderListener;
/*     */   private PlayerJoinListener joinListener;
/*     */   private CoordinateStorage coordinateStorage;
/*     */   private SchedulerAdapter.TaskHandle heartbeatTask;
/*     */   private SchedulerAdapter.TaskHandle serverStatusRefreshTask;
/*     */   private Map<UUID, Entity> vehicleRespawnMap;
/*     */   private Map<UUID, Long> vehicleRespawnTimestamps;
/*     */   private SchedulerAdapter.TaskHandle vehicleCleanupTask;
/*     */   private static final long VEHICLE_RESPAWN_TTL_MS = 15000L;
/*     */   
/*     */   public void onEnable() {
/*  31 */     instance = this;
/*     */ 
/*     */     
/*  34 */     getServer().getMessenger().registerOutgoingPluginChannel((Plugin)this, "BungeeCord");
/*     */ 
/*     */     
/*  37 */     saveDefaultConfig();
/*     */ 
/*     */     
/*  40 */     this.coordinateStorage = new CoordinateStorage(this);
/*     */ 
/*     */     
/*  43 */     this.configManager = new ConfigManager(this);
/*  44 */     this.configManager.loadConfig();
/*     */ 
/*     */     
/*  47 */     this.vehicleRespawnMap = new ConcurrentHashMap<>();
/*  48 */     this.vehicleRespawnTimestamps = new ConcurrentHashMap<>();
/*     */ 
/*     */     
/*  51 */     this.vehicleCleanupTask = SchedulerAdapter.runAsyncRepeating((Plugin)this, () -> {
/*  52 */           long now = System.currentTimeMillis();
/*  53 */           this.vehicleRespawnTimestamps.entrySet().removeIf(entry -> {
/*  54 */                 boolean expired = (now - entry.getValue().longValue() > VEHICLE_RESPAWN_TTL_MS);
/*  55 */                 if (expired) {
/*  56 */                   this.vehicleRespawnMap.remove(entry.getKey());
/*     */                 }
/*  58 */                 return expired;
/*     */               });
/*     */         }, 100L, 100L);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     if (this.coordinateStorage.isRedisEnabled()) {
/*  65 */       String serverName = this.configManager.getServerName();
/*  66 */       if (serverName == null || serverName.equals("change-me")) {
/*  67 */         getLogger().warning("--------------------------------------------------");
/*  68 */         getLogger().warning("Server name is not set in config.yml!");
/*  69 */         getLogger().warning("Redis heartbeat is disabled.");
/*  70 */         getLogger().warning("Other servers will not know if this server is online.");
/*  71 */         getLogger().warning("Please set a unique 'server-name' in your config.yml.");
/*  72 */         getLogger().warning("--------------------------------------------------");
/*     */       } else {
/*  74 */         this.heartbeatTask = SchedulerAdapter.runAsyncRepeating((Plugin)this, () -> this.coordinateStorage.updateServerStatusAsync(serverName), 1L, 100L);
/*     */ 
/*     */         
/*  77 */         getLogger().info("Started Redis heartbeat for server: " + serverName);
/*     */       } 
/*     */       this.serverStatusRefreshTask = SchedulerAdapter.runAsyncRepeating((Plugin)this, () -> {
/*     */             java.util.List<String> targetServers = this.configManager.getBorders().stream()
/*     */                 .map(border -> border.getTargetServer())
/*     */                 .filter(targetServer -> targetServer != null && !targetServer.isBlank())
/*     */                 .distinct()
/*     */                 .toList();
/*     */             this.coordinateStorage.refreshServerStatusesAsync(targetServers);
/*     */           }, 1L, 20L);
/*     */       getLogger().info("Started asynchronous Redis server-status cache refresh.");
/*     */     } 
/*     */ 
/*     */     
/*  82 */     this.borderListener = new BorderListener(this, this.configManager, this.coordinateStorage);
/*  83 */     getServer().getPluginManager().registerEvents((Listener)this.borderListener, (Plugin)this);
/*     */ 
/*     */     
/*  86 */     this.joinListener = new PlayerJoinListener(this, this.coordinateStorage, this.borderListener);
/*  87 */     getServer().getPluginManager().registerEvents((Listener)this.joinListener, (Plugin)this);
/*     */ 
/*     */     
/*  90 */     CrossBorderCoreCommand commandHandler = new CrossBorderCoreCommand(this, this.configManager);
/*  91 */     getCommand("crossbordercore").setExecutor((CommandExecutor)commandHandler);
/*  92 */     getCommand("crossbordercore").setTabCompleter((TabCompleter)commandHandler);
/*     */     
/*  94 */     getLogger().info("==================================================");
/*  95 */     getLogger().info("CrossBorder Core " + getDescription().getVersion() + " enabled");
/*  96 */     getLogger().info("Scheduler mode: " + (SchedulerAdapter.isFolia() ? "Canvas/Folia region-threaded" : "Bukkit-compatible"));
/*  97 */     getLogger().info("Loaded " + this.configManager.getBorders().size() + " border(s)");
/*  98 */     getLogger().info("==================================================");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 100 */     if (this.heartbeatTask != null) {
/* 101 */       this.heartbeatTask.cancel();
/* 102 */       this.heartbeatTask = null;
/*     */     } 
/*     */     if (this.serverStatusRefreshTask != null) {
/*     */       this.serverStatusRefreshTask.cancel();
/*     */       this.serverStatusRefreshTask = null;
/*     */     }
/* 104 */     if (this.vehicleCleanupTask != null) {
/* 105 */       this.vehicleCleanupTask.cancel();
/* 106 */       this.vehicleCleanupTask = null;
/*     */     } 
/* 108 */     if (this.borderListener != null) {
/* 109 */       this.borderListener.cleanup();
/* 110 */       this.borderListener = null;
/*     */     } 
/* 112 */     if (this.coordinateStorage != null) {
/* 113 */       this.coordinateStorage.close();
/* 114 */       this.coordinateStorage = null;
/*     */     } 
/*     */ 
/*     */     
/* 118 */     if (this.vehicleRespawnMap != null) {
/* 119 */       this.vehicleRespawnMap.clear();
/* 120 */       this.vehicleRespawnMap = null;
/*     */     } 
/* 122 */     if (this.vehicleRespawnTimestamps != null) {
/* 123 */       this.vehicleRespawnTimestamps.clear();
/* 124 */       this.vehicleRespawnTimestamps = null;
/*     */     } 
/*     */     
/* 127 */     getServer().getMessenger().unregisterOutgoingPluginChannel((Plugin)this);
/*     */ 
/*     */     
/* 130 */     instance = null;
/* 131 */     this.configManager = null;
/*     */     
/* 133 */     getLogger().info("CrossBorder Core has been disabled!");
/*     */   }
/*     */   
/*     */   public static CrossBorderCore getInstance() {
/* 137 */     return instance;
/*     */   }
/*     */   
/*     */   public ConfigManager getConfigManager() {
/* 141 */     return this.configManager;
/*     */   }
/*     */   
/*     */   public CoordinateStorage getCoordinateStorage() {
/* 145 */     return this.coordinateStorage;
/*     */   }
/*     */
/*     */   public PlayerJoinListener getPlayerJoinListener() {
/*     */     return this.joinListener;
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
/*     */   public void registerVehicleRespawn(UUID oldUUID, Entity newEntity) {
/* 157 */     this.vehicleRespawnMap.put(oldUUID, newEntity);
/* 158 */     this.vehicleRespawnTimestamps.put(oldUUID, Long.valueOf(System.currentTimeMillis()));
/* 159 */     getLogger().fine("Registered vehicle respawn mapping: " + String.valueOf(oldUUID) + " -> " + String.valueOf(newEntity.getUniqueId()) + " (" + String.valueOf(newEntity.getType()) + ")");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Entity getVehicleByOldUUID(UUID oldUUID) {
/* 170 */     Entity entity = this.vehicleRespawnMap.get(oldUUID);
/* 171 */     if (entity != null && !entity.isValid()) {
/*     */       
/* 173 */       this.vehicleRespawnMap.remove(oldUUID);
/* 174 */       this.vehicleRespawnTimestamps.remove(oldUUID);
/* 175 */       return null;
/*     */     } 
/* 177 */     return entity;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\CrossServerTP.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
