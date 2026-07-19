/*     */ package com.alphastudio.CrossBorderCore.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.bukkit.Particle;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import com.alphastudio.CrossBorderCore.CrossBorderCore;
/*     */ import com.alphastudio.CrossBorderCore.visual.ParticleBarrier;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ConfigManager
/*     */ {
/*     */   private final CrossBorderCore plugin;
/*     */   private final List<BorderConfig> borders;
/*     */   private int checkInterval;
/*     */   private int particleDistance;
/*     */   private String particleTypeName;
/*     */   private boolean entityTeleportEnabled;
/*     */   private boolean teleportVehicles;
/*     */   private boolean teleportLeashedMobs;
/*     */   private boolean teleportNearbyVillagers;
/*     */   private int nearbyEntityDistance;
/*     */   private int maxEntities;
/*     */   private boolean mobsExclusionMode;
/*     */   private List<String> mobsList;
/*     */   private boolean vehiclesExclusionMode;
/*     */   private List<String> vehiclesList;
/*     */   private boolean mobsMustBeLeashed;
/*     */   private String serverName;
/*     */   private String serverOfflineMessage;
/*     */   private String destinationUnavailableMessage;
/*     */   private boolean unsafeDestinationBlockClearingEnabled;
/*     */   private boolean externalTeleportBorderSafetyEnabled;
/*     */   private double externalTeleportBorderSafetyMargin;
/*     */   private int transferTimeoutMs;
/*     */   
/*     */   public ConfigManager(CrossBorderCore plugin) {
/*  37 */     this.plugin = plugin;
/*  38 */     this.borders = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void loadConfig() {
/*  42 */     this.plugin.reloadConfig();
/*  43 */     this.borders.clear();
/*     */ 
/*     */     
/*  46 */     this.checkInterval = this.plugin.getConfig().getInt("check-interval-ticks", 5);
/*  47 */     this.particleDistance = this.plugin.getConfig().getInt("particle-view-distance", 50);
/*  48 */     this.particleTypeName = this.plugin.getConfig().getString("particle-type", "SCRAPE");
/*     */ 
/*     */     
/*  51 */     this.entityTeleportEnabled = this.plugin.getConfig().getBoolean("entity-teleportation.enabled", true);
/*  52 */     this.teleportVehicles = this.plugin.getConfig().getBoolean("entity-teleportation.teleport-vehicles", true);
/*  53 */     this.teleportLeashedMobs = this.plugin.getConfig().getBoolean("entity-teleportation.teleport-leashed-mobs", true);
/*  54 */     this.teleportNearbyVillagers = this.plugin.getConfig().getBoolean("entity-teleportation.teleport-nearby-villagers", true);
/*  55 */     this.nearbyEntityDistance = this.plugin.getConfig().getInt("entity-teleportation.nearby-entity-distance", 5);
/*  56 */     this.maxEntities = this.plugin.getConfig().getInt("entity-teleportation.max-entities", 10);
/*     */ 
/*     */     
/*  59 */     this.mobsExclusionMode = this.plugin.getConfig().getBoolean("entity-teleportation.mobs.exclusion", true);
/*  60 */     this.mobsList = this.plugin.getConfig().getStringList("entity-teleportation.mobs.list");
/*  61 */     this.vehiclesExclusionMode = this.plugin.getConfig().getBoolean("entity-teleportation.vehicles.exclusion", true);
/*  62 */     this.vehiclesList = this.plugin.getConfig().getStringList("entity-teleportation.vehicles.list");
/*  63 */     this.mobsMustBeLeashed = this.plugin.getConfig().getBoolean("entity-teleportation.mobs_must_be_leashed", false);
/*     */ 
/*     */     
/*  66 */     this.serverName = this.plugin.getConfig().getString("server-name", "change-me");
/*  67 */     this.serverOfflineMessage = this.plugin.getConfig().getString("messages.server-offline", "&cThe destination server is currently offline. Please try again later.");
/*     */     this.destinationUnavailableMessage = this.plugin.getConfig().getString("messages.destination-unavailable", this.serverOfflineMessage);
/*     */     this.unsafeDestinationBlockClearingEnabled = this.plugin.getConfig().getBoolean("unsafe-destination-block-clearing.enabled", false);
/*     */     this.externalTeleportBorderSafetyEnabled = this.plugin.getConfig().getBoolean("external-teleport-border-safety.enabled", true);
/*     */     this.externalTeleportBorderSafetyMargin = Math.max(0.0D, this.plugin.getConfig().getDouble("external-teleport-border-safety.margin-blocks", 8.0D));
/*     */     this.transferTimeoutMs = Math.max(500, this.plugin.getConfig().getInt("redis.transfer-timeout-ms", 3000));
/*     */ 
/*     */     
/*  70 */     loadParticleType();
/*     */ 
/*     */     
/*  73 */     ConfigurationSection bordersSection = this.plugin.getConfig().getConfigurationSection("borders");
/*  74 */     if (bordersSection != null && !bordersSection.getKeys(false).isEmpty()) {
/*  75 */       loadBordersFromSection(bordersSection);
/*     */     } else {
/*  77 */       List<?> borderList = this.plugin.getConfig().getList("borders");
/*  78 */       if (borderList != null && !borderList.isEmpty()) {
/*  79 */         loadBordersFromList(borderList);
/*     */       } else {
/*  81 */         this.plugin.getLogger().warning("No borders configured; players will stay on this server.");
/*     */       } 
/*     */     } 
/*     */     
/*  85 */     if (this.borders.isEmpty()) {
/*  86 */       this.plugin.getLogger().warning("No valid borders loaded; teleport will not trigger.");
/*     */     }
/*     */   }
/*     */   
/*     */   public List<BorderConfig> getBorders() {
/* 100 */     return Collections.unmodifiableList(this.borders);
/*     */   }
/*     */   
/*     */   public int getCheckInterval() {
/* 104 */     return this.checkInterval;
/*     */   }
/*     */   
/*     */   public int getParticleDistance() {
/* 108 */     return this.particleDistance;
/*     */   }
/*     */   
/*     */   public String getParticleTypeName() {
/* 112 */     return this.particleTypeName;
/*     */   }
/*     */   
/*     */   public boolean isEntityTeleportEnabled() {
/* 116 */     return this.entityTeleportEnabled;
/*     */   }
/*     */   
/*     */   public boolean isTeleportVehicles() {
/* 120 */     return this.teleportVehicles;
/*     */   }
/*     */   
/*     */   public boolean isTeleportLeashedMobs() {
/* 124 */     return this.teleportLeashedMobs;
/*     */   }
/*     */   
/*     */   public int getMaxEntities() {
/* 128 */     return this.maxEntities;
/*     */   }
/*     */   
/*     */   public boolean isTeleportNearbyVillagers() {
/* 132 */     return this.teleportNearbyVillagers;
/*     */   }
/*     */   
/*     */   public int getNearbyEntityDistance() {
/* 136 */     return this.nearbyEntityDistance;
/*     */   }
/*     */   
/*     */   public boolean isMobsExclusionMode() {
/* 140 */     return this.mobsExclusionMode;
/*     */   }
/*     */   
/*     */   public List<String> getMobsList() {
/* 144 */     return (this.mobsList != null) ? Collections.<String>unmodifiableList(this.mobsList) : Collections.<String>emptyList();
/*     */   }
/*     */   
/*     */   public boolean isVehiclesExclusionMode() {
/* 148 */     return this.vehiclesExclusionMode;
/*     */   }
/*     */   
/*     */   public List<String> getVehiclesList() {
/* 152 */     return (this.vehiclesList != null) ? Collections.<String>unmodifiableList(this.vehiclesList) : Collections.<String>emptyList();
/*     */   }
/*     */   
/*     */   public boolean isMobsMustBeLeashed() {
/* 156 */     return this.mobsMustBeLeashed;
/*     */   }
/*     */   
/*     */   public String getServerName() {
/* 160 */     return this.serverName;
/*     */   }
/*     */   
/*     */   public String getServerOfflineMessage() {
/* 164 */     return this.serverOfflineMessage;
/*     */   }
/*     */
/*     */   public String getDestinationUnavailableMessage() {
/*     */     return this.destinationUnavailableMessage;
/*     */   }
/*     */
/*     */   public boolean isUnsafeDestinationBlockClearingEnabled() {
/*     */     return this.unsafeDestinationBlockClearingEnabled;
/*     */   }
/*     */
/*     */   public boolean isExternalTeleportBorderSafetyEnabled() {
/*     */     return this.externalTeleportBorderSafetyEnabled;
/*     */   }
/*     */
/*     */   public double getExternalTeleportBorderSafetyMargin() {
/*     */     return this.externalTeleportBorderSafetyMargin;
/*     */   }
/*     */
/*     */   public int getTransferTimeoutMs() {
/*     */     return this.transferTimeoutMs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldTeleportEntity(String entityType, boolean isMob) {
/* 174 */     if (isMob) {
/*     */       
/* 176 */       boolean bool = this.mobsList.stream().anyMatch(type -> type.equalsIgnoreCase(entityType));
/*     */ 
/*     */       
/* 179 */       return this.mobsExclusionMode ? (!bool) : bool;
/*     */     } 
/*     */     
/* 182 */     boolean inList = this.vehiclesList.stream().anyMatch(type -> type.equalsIgnoreCase(entityType));
/* 183 */     return this.vehiclesExclusionMode ? (!inList) : inList;
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadBordersFromSection(ConfigurationSection bordersSection) {
/* 189 */     for (String key : bordersSection.getKeys(false)) {
/* 190 */       ConfigurationSection borderSection = bordersSection.getConfigurationSection(key);
/* 191 */       if (borderSection == null) {
/*     */         continue;
/*     */       }
/* 194 */       String world = borderSection.getString("world", "world");
/* 195 */       String sideStr = borderSection.getString("side", "EAST");
/* 196 */       Double coordinate = borderSection.isSet("coordinate") ? Double.valueOf(borderSection.getDouble("coordinate")) : null;
/* 197 */       String targetServer = borderSection.getString("target-server", "");
/* 198 */       String targetWorld = borderSection.getString("target-world", null);
/* 199 */       String targetSideStr = borderSection.getString("target-side", null);
/* 200 */       Double targetCoordinate = borderSection.isSet("target-coordinate") ? Double.valueOf(borderSection.getDouble("target-coordinate")) : null;
/* 201 */       Double targetOffset = Double.valueOf(borderSection.getDouble("target-offset", 5.0D));
/* 202 */       boolean showParticles = borderSection.getBoolean("show-particles", true);
/* 203 */       addBorderIfValid(key, world, sideStr, coordinate, targetServer, targetWorld, targetSideStr, targetCoordinate, targetOffset, showParticles);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadBordersFromList(List<?> borderList) {
/* 206 */     int index = 0;
/* 207 */     for (Object entry : borderList) {
/* 208 */       if (!(entry instanceof Map)) {
/* 209 */         this.plugin.getLogger().warning("Skipping border at index " + index + " (not a map)");
/* 210 */         index++;
/*     */         continue;
/*     */       } 
/* 213 */       Map<?, ?> map = (Map<?, ?>)entry;
/* 214 */       String world = (map.get("world") != null) ? String.valueOf(map.get("world")) : "world";
/* 215 */       String sideStr = (map.get("side") != null) ? String.valueOf(map.get("side")) : "EAST";
/* 216 */       Object coordObj = map.get("coordinate");
/* 217 */       Double coordinate = (coordObj instanceof Number) ? Double.valueOf(((Number)coordObj).doubleValue()) : null;
/* 218 */       String targetServer = (map.get("target-server") != null) ? String.valueOf(map.get("target-server")) : "";
/* 219 */       String targetWorld = (map.get("target-world") != null) ? String.valueOf(map.get("target-world")) : null;
/* 220 */       String targetSideStr = (map.get("target-side") != null) ? String.valueOf(map.get("target-side")) : null;
/* 221 */       Object targetCoordObj = map.get("target-coordinate");
/* 222 */       Double targetCoordinate = (targetCoordObj instanceof Number) ? Double.valueOf(((Number)targetCoordObj).doubleValue()) : null;
/* 223 */       Object targetOffsetObj = map.get("target-offset");
/* 224 */       Double targetOffset = (targetOffsetObj instanceof Number) ? Double.valueOf(((Number)targetOffsetObj).doubleValue()) : Double.valueOf(5.0D);
/* 225 */       Object particlesObj = map.get("show-particles");
/* 226 */       boolean showParticles = (particlesObj == null) ? true : Boolean.parseBoolean(String.valueOf(particlesObj));
/* 227 */       addBorderIfValid("index-" + index, world, sideStr, coordinate, targetServer, targetWorld, targetSideStr, targetCoordinate, targetOffset, showParticles);
/* 228 */       index++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void addBorderIfValid(String key, String world, String sideStr, Double coordinateValue, String targetServer, String targetWorldValue, String targetSideStr, Double targetCoordinateValue, Double targetOffsetValue, boolean showParticles) {
/* 229 */     if (coordinateValue == null) {
/* 230 */       this.plugin.getLogger().warning("Skipping border '" + key + "': missing coordinate");
/*     */       return;
/*     */     } 
/*     */     if ((targetServer == null || targetServer.isBlank()) && (targetWorldValue == null || targetWorldValue.isBlank())) {
/*     */       this.plugin.getLogger().warning("Skipping border '" + key + "': target-world is not set");
/*     */       return;
/*     */     } 
/* 237 */     if (targetSideStr == null || targetSideStr.isBlank()) {
/* 238 */       this.plugin.getLogger().warning("Skipping border '" + key + "': target-side is not set");
/*     */       return;
/*     */     } 
/* 241 */     if (targetCoordinateValue == null) {
/* 242 */       this.plugin.getLogger().warning("Skipping border '" + key + "': target-coordinate is missing");
/*     */       return;
/*     */     } 
/*     */     try {
/* 246 */       BorderConfig.BorderSide side = BorderConfig.BorderSide.valueOf(sideStr.toUpperCase());
/* 247 */       BorderConfig.BorderSide targetSide = BorderConfig.BorderSide.valueOf(targetSideStr.toUpperCase());
/* 248 */       String targetWorld = (targetWorldValue == null || targetWorldValue.isBlank()) ? world : targetWorldValue;
/*     */       String normalizedTargetServer = (targetServer == null || targetServer.isBlank()) ? null : targetServer;
/* 249 */       double coordinate = coordinateValue.doubleValue();
/* 250 */       double targetCoordinate = targetCoordinateValue.doubleValue();
/* 251 */       double targetOffset = (targetOffsetValue == null) ? 5.0D : targetOffsetValue.doubleValue();
/* 252 */       if (targetOffset <= 0.0D) {
/* 253 */         this.plugin.getLogger().warning("Border '" + key + "' has invalid target-offset " + targetOffset + "; defaulting to 5.0");
/* 254 */         targetOffset = 5.0D;
/*     */       } 
/* 256 */       BorderConfig border = new BorderConfig(world, side, coordinate, normalizedTargetServer, targetWorld, targetSide, targetCoordinate, targetOffset, showParticles);
/* 257 */       this.borders.add(border);
/* 258 */       this.plugin.getLogger().info("Loaded border: " + String.valueOf(border));
/* 259 */     } catch (Exception e) {
/* 260 */       this.plugin.getLogger().warning("Failed to load border '" + key + "': " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadParticleType() {
/*     */     try {
/* 251 */       Particle particle = Particle.valueOf(this.particleTypeName.toUpperCase());
/* 252 */       ParticleBarrier.setParticleType(particle);
/* 253 */       this.plugin.getLogger().info("Loaded particle type: " + this.particleTypeName);
/* 254 */     } catch (IllegalArgumentException e) {
/* 255 */       this.plugin.getLogger().warning("Invalid particle type '" + this.particleTypeName + "', using default SCRAPE");
/* 256 */       this.plugin.getLogger().warning("Available particles: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Particle.html");
/* 257 */       ParticleBarrier.setParticleType(Particle.SCRAPE);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\config\ConfigManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
