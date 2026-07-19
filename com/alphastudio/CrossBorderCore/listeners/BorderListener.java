/*     */ package com.alphastudio.CrossBorderCore.listeners;
/*     */ import com.google.common.io.ByteArrayDataOutput;
/*     */ import com.google.common.io.ByteStreams;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.ExecutionException;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.logging.Level;
/*     */ import io.papermc.paper.entity.TeleportFlag;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.ChatColor;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.WorldBorder;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.entity.Pose;
/*     */ import org.bukkit.entity.Vehicle;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.BlockBreakEvent;
/*     */ import org.bukkit.event.block.BlockPlaceEvent;
/*     */ import org.bukkit.event.player.PlayerJoinEvent;
/*     */ import org.bukkit.event.player.PlayerMoveEvent;
/*     */ import org.bukkit.event.player.PlayerTeleportEvent;
/*     */ import org.bukkit.event.player.PlayerQuitEvent;
/*     */ import org.bukkit.event.vehicle.VehicleMoveEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.util.Vector;
/*     */ import com.alphastudio.CrossBorderCore.CrossBorderCore;
/*     */ import com.alphastudio.CrossBorderCore.config.BorderConfig;
/*     */ import com.alphastudio.CrossBorderCore.config.ConfigManager;
/*     */ import com.alphastudio.CrossBorderCore.storage.BlockData;
/*     */ import com.alphastudio.CrossBorderCore.storage.BlockManager;
/*     */ import com.alphastudio.CrossBorderCore.storage.CoordinateStorage;
/*     */ import com.alphastudio.CrossBorderCore.storage.EntityData;
/*     */ import com.alphastudio.CrossBorderCore.storage.EntityManager;
/*     */ import com.alphastudio.CrossBorderCore.visual.ParticleBarrier;
/*     */ import com.alphastudio.CrossBorderCore.util.SchedulerAdapter;
/*     */ 
/*     */ public class BorderListener implements Listener {
/*     */   private final CrossBorderCore plugin;
/*     */   private final ConfigManager configManager;
/*     */   private final CoordinateStorage coordinateStorage;
/*     */   private final Map<UUID, Long> recentTeleports;
/*     */   private final Map<UUID, Long> blockedPassageMessages;
/*     */   private final Map<UUID, Long> blockedRepositions;
/*     */   private final Set<UUID> playersNearBorder;
/*     */   private final Map<UUID, SchedulerAdapter.TaskHandle> foliaParticleTasks;
/*     */   private final Set<UUID> transfersInProgress;
/*     */   
/*     */   public BorderListener(CrossBorderCore plugin, ConfigManager configManager, CoordinateStorage coordinateStorage) {
/*  42 */     this.plugin = plugin;
/*  43 */     this.configManager = configManager;
/*  44 */     this.coordinateStorage = coordinateStorage;
/*  45 */     this.recentTeleports = new ConcurrentHashMap<>();
/*  46 */     this.blockedPassageMessages = new ConcurrentHashMap<>();
/*  47 */     this.blockedRepositions = new ConcurrentHashMap<>();
/*  48 */     this.playersNearBorder = ConcurrentHashMap.newKeySet();
/*  49 */     this.foliaParticleTasks = new ConcurrentHashMap<>();
/*     */     this.transfersInProgress = ConcurrentHashMap.newKeySet();
/*     */ 
/*     */     
/*  50 */     startParticleTask();
/*     */ 
/*     */     
/*  53 */     startCleanupTask();
/*     */   }
/*     */   private SchedulerAdapter.TaskHandle particleTask; private SchedulerAdapter.TaskHandle cleanupTask; private static final long TELEPORT_COOLDOWN_MS = 2000L; private static final long BLOCKED_MESSAGE_COOLDOWN_MS = 1500L; private static final long BLOCKED_REPOSITION_COOLDOWN_MS = 250L; private static final int TELEPORT_TRIGGER_DISTANCE = 1; private static final double MIN_SOURCE_TRIGGER_MARGIN = 1.0D; private static final double DESTINATION_OFFSET = 5.0D; private static final double SOURCE_BLOCK_OFFSET = 1.5D;
/*     */
/*     */   private static class PreparedTransfer {
/*     */     private final UUID transferId;
/*     */     private final Player player;
/*     */     private final BorderConfig border;
/*     */     private final Location sourceLocation;
/*     */     private final Location destination;
/*     */     private final Entity vehicle;
/*     */     private final List<Player> additionalPlayers;
/*     */     private final List<Entity> entityReferences;
/*     */     private final Map<UUID, CoordinateStorage.PendingTeleport> pendingTeleports;
/*     */     private final long protectionDeadline;
/*     */     private final Vector velocity;
/*     */
/*     */     private PreparedTransfer(
/*     */         UUID transferId,
/*     */         Player player,
/*     */         BorderConfig border,
/*     */         Location sourceLocation,
/*     */         Location destination,
/*     */         Entity vehicle,
/*     */         List<Player> additionalPlayers,
/*     */         List<Entity> entityReferences,
/*     */         Map<UUID, CoordinateStorage.PendingTeleport> pendingTeleports,
/*     */         long protectionDeadline,
/*     */         Vector velocity) {
/*     */       this.transferId = transferId;
/*     */       this.player = player;
/*     */       this.border = border;
/*     */       this.sourceLocation = sourceLocation;
/*     */       this.destination = destination;
/*     */       this.vehicle = vehicle;
/*     */       this.additionalPlayers = additionalPlayers;
/*     */       this.entityReferences = entityReferences;
/*     */       this.pendingTeleports = pendingTeleports;
/*     */       this.protectionDeadline = protectionDeadline;
/*     */       this.velocity = velocity;
/*     */     }
/*     */   }
/*     */   
/*     */   private void startCleanupTask() {
/*  58 */     this.cleanupTask = SchedulerAdapter.runAsyncRepeating((Plugin)this.plugin, () -> {
/*  59 */           long now = System.currentTimeMillis();
/*  60 */           this.recentTeleports.entrySet().removeIf(entry -> entry.getValue().longValue() <= now);
/*     */         }, 600L, 600L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void startParticleTask() {
/*  68 */     long period = Math.max(1L, this.configManager.getCheckInterval());
/*  69 */     if (SchedulerAdapter.isFolia()) {
/*  70 */       Bukkit.getOnlinePlayers().forEach(this::scheduleParticleTaskForPlayer);
/*     */     } else {
/*  72 */       this.particleTask = SchedulerAdapter.runGlobalRepeating((Plugin)this.plugin, () -> {
/*  73 */             for (Player player : Bukkit.getOnlinePlayers()) {
/*  74 */               checkPlayerNearBorder(player);
/*     */             }
/*     */           }, 0L, period);
/*     */     } 
/*     */   }

/*     */   private void scheduleParticleTaskForPlayer(Player player) {
/*  81 */     SchedulerAdapter.TaskHandle handle = SchedulerAdapter.runEntityRepeating((Plugin)this.plugin, (Entity)player, () -> checkPlayerNearBorder(player), 1L, Math.max(1L, this.configManager.getCheckInterval()));
/*  82 */     this.foliaParticleTasks.put(player.getUniqueId(), handle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
/*     */   public void onPlayerTeleport(PlayerTeleportEvent event) {
/*     */     if (!this.configManager.isExternalTeleportBorderSafetyEnabled()) {
/*     */       return;
/*     */     }
/*     */     PlayerTeleportEvent.TeleportCause cause = event.getCause();
/*     */     if (cause != PlayerTeleportEvent.TeleportCause.PLUGIN && cause != PlayerTeleportEvent.TeleportCause.COMMAND) {
/*     */       return;
/*     */     }
/*     */     Player player = event.getPlayer();
/*     */     UUID playerId = player.getUniqueId();
/*     */     if (this.transfersInProgress.contains(playerId) || isOnCooldown(playerId)) {
/*     */       return;
/*     */     }
/*     */     Location to = event.getTo();
/*     */     if (to == null || to.getWorld() == null) {
/*     */       return;
/*     */     }
/*     */     Location adjusted = adjustExternalTeleportDestination(to);
/*     */     if (adjusted != null) {
/*     */       event.setTo(adjusted);
/*     */     }
/*     */   }
/*     */
/*     */   @EventHandler
/*     */   public void onPlayerJoin(PlayerJoinEvent event) {
/*  75 */     if (SchedulerAdapter.isFolia()) {
/*  76 */       scheduleParticleTaskForPlayer(event.getPlayer());
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onPlayerMove(PlayerMoveEvent event) {
/*  82 */     Location from = event.getFrom();
/*  83 */     Location to = event.getTo();
/*     */     
/*  85 */     if (to == null) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     Player player = event.getPlayer();
/*  90 */     UUID playerId = player.getUniqueId();
/*     */ 
/*     */ 
/*     */     
/*     */     if (this.transfersInProgress.contains(playerId)) {
/*     */       event.setCancelled(true);
/*     */       return;
/*     */     }
/*     */
/*  88 */     if (isOnCooldown(playerId)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  93 */     for (BorderConfig border : this.configManager.getBorders()) {
/*  94 */       if (!player.getWorld().getName().equals(border.getWorld())) {
/*     */         continue;
/*     */       }
/*  97 */       if (!isRouteConfigured(border)) {
/*     */         continue;
/*     */       }
/*     */ 
/*  97 */       int distanceToBorder = getDistanceToBorder(to, border);
/*  98 */       if (distanceToBorder > TELEPORT_TRIGGER_DISTANCE) {
/*     */         continue;
/*     */       }
/*     */       
/* 101 */       if (hasCrossedBorder(from, to, border) || isPastBorder(to, border)) {
/* 102 */         if (!isTargetAvailable(border)) {
/* 103 */           blockPlayerCrossing(event, player, to, border);
/*     */           return;
/*     */         } 
/* 106 */         event.setCancelled(true);
/* 107 */         handleBorderCrossing(player, from, to, border);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleBorderCrossing(Player player, Location from, Location to, BorderConfig border) {
/*     */     UUID playerId = player.getUniqueId();
/*     */     if (!this.transfersInProgress.add(playerId)) {
/*     */       return;
/*     */     }
/*     */     if (!isTargetAvailable(border)) {
/*     */       this.transfersInProgress.remove(playerId);
/*     */       keepInsideSource(player, to, border);
/*     */       return;
/*     */     }
/*     */
/*     */     PreparedTransfer transfer;
/*     */     try {
/*     */       transfer = prepareTransfer(player, to, border);
/*     */     } catch (Throwable throwable) {
/*     */       this.transfersInProgress.remove(playerId);
/*     */       this.plugin.getLogger().log(Level.WARNING, "Failed to snapshot transfer for " + player.getName(), unwrap(throwable));
/*     */       keepInsideSource(player, to, border);
/*     */       return;
/*     */     }
/*     */
/*     */     if (transfer.vehicle != null && transfer.vehicle.isValid()) {
/*     */       transfer.vehicle.setVelocity(new Vector(0.0D, 0.0D, 0.0D));
/*     */     }
/*     */
/*     */     if (border.isLocalWorldTransfer()) {
/*     */       completePreparedTransfer(transfer, true, null);
/*     */       return;
/*     */     }
/*     */
/*     */     this.coordinateStorage.storeTeleportsAsync(transfer.pendingTeleports)
/*     */         .orTimeout(this.configManager.getTransferTimeoutMs(), TimeUnit.MILLISECONDS)
/*     */         .whenComplete((stored, throwable) -> {
/*     */           SchedulerAdapter.TaskHandle handle = SchedulerAdapter.runEntity(
/*     */               (Plugin)this.plugin,
/*     */               (Entity)player,
/*     */               () -> completePreparedTransfer(transfer, Boolean.TRUE.equals(stored), throwable));
/*     */           if (!handle.isPresent()) {
/*     */             releaseTransferReservations(transfer);
/*     */           }
/*     */         });
/*     */   }
/*     */
/*     */   private PreparedTransfer prepareTransfer(Player player, Location to, BorderConfig border) {
/*     */     World inspectionWorld = resolveTargetInspectionWorld(player.getWorld(), border);
/*     */     Location destination = new Location(inspectionWorld, to.getX(), to.getY(), to.getZ(), to.getYaw(), to.getPitch());
/*     */     adjustDestinationInsideTarget(destination, border);
/*     */
/*     */     boolean isCrawling = player.getPose() == Pose.SWIMMING;
/*     */     String targetBorderAxis = getBorderAxis(border.getTargetSide());
/*     */     String targetBorderDirection = getBorderDirection(border.getTargetSide());
/*     */     Entity vehicle = player.isInsideVehicle() ? player.getVehicle() : null;
/*     */     Vector transferVelocity = getTransferVelocity(player, vehicle);
/*     */     long protectionDeadline = System.currentTimeMillis() + TELEPORT_COOLDOWN_MS;
/*     */     UUID transferId = UUID.randomUUID();
/*     */
/*     */     List<Player> additionalPlayers = new ArrayList<>();
/*     */     if (vehicle != null) {
/*     */       for (Entity passenger : vehicle.getPassengers()) {
/*     */         if (passenger instanceof Player && !passenger.equals(player)) {
/*     */           Player otherPlayer = (Player)passenger;
/*     */           if (!isOnCooldown(otherPlayer.getUniqueId()) && !this.transfersInProgress.contains(otherPlayer.getUniqueId())) {
/*     */             additionalPlayers.add(otherPlayer);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */
/*     */     List<BlockData> blocks = new ArrayList<>();
/*     */     if (this.configManager.isUnsafeDestinationBlockClearingEnabled()) {
/*     */       blocks = BlockManager.collectAirBlocks(destination, 3, targetBorderAxis, (int)border.getTargetCoordinate(), targetBorderDirection);
/*     */     }
/*     */
/*     */     List<EntityData> entities = null;
/*     */     List<Entity> entityReferences = new ArrayList<>();
/*     */     if (this.configManager.isEntityTeleportEnabled()) {
/*     */       EntityManager.CollectionResult result = EntityManager.collectEntitiesWithReferences(
/*     */           player,
/*     */           this.configManager,
/*     */           this.configManager.isTeleportVehicles(),
/*     */           this.configManager.isTeleportLeashedMobs(),
/*     */           this.configManager.getNearbyEntityDistance(),
/*     */           this.configManager.getMaxEntities());
/*     */       entities = result.entityDataList;
/*     */       entityReferences = result.entityReferences;
/*     */     }
/*     */
/*     */     UUID vehicleUUID = findVehicleUuid(entities);
/*     */     List<Player> reservedAdditionalPlayers = new ArrayList<>();
/*     */     Map<UUID, CoordinateStorage.PendingTeleport> pendingTeleports = new LinkedHashMap<>();
/*     */     CoordinateStorage.PendingTeleport primaryTeleport = new CoordinateStorage.PendingTeleport(
/*     */         destination.getX(), destination.getY(), destination.getZ(), destination.getYaw(), destination.getPitch(),
/*     */         border.getTargetWorld(), player.isFlying(), player.getAllowFlight(), player.isGliding(), isCrawling,
/*     */         player.getFlySpeed(), player.getWalkSpeed(), entities, blocks, targetBorderAxis,
/*     */         (int)border.getTargetCoordinate(), targetBorderDirection, transferVelocity.getX(), transferVelocity.getY(),
/*     */         transferVelocity.getZ(), player.isSprinting(), protectionDeadline);
/*     */     setTransferMetadata(primaryTeleport, transferId, border);
/*     */     pendingTeleports.put(player.getUniqueId(), primaryTeleport);
/*     */
/*     */     for (Player otherPlayer : additionalPlayers) {
/*     */       CoordinateStorage.PendingTeleport passengerTeleport = new CoordinateStorage.PendingTeleport(
/*     */           destination.getX(), destination.getY() + 0.5D, destination.getZ(), otherPlayer.getLocation().getYaw(),
/*     */           otherPlayer.getLocation().getPitch(), border.getTargetWorld(), otherPlayer.isFlying(),
/*     */           otherPlayer.getAllowFlight(), otherPlayer.isGliding(), otherPlayer.getPose() == Pose.SWIMMING,
/*     */           otherPlayer.getFlySpeed(), otherPlayer.getWalkSpeed(), transferVelocity.getX(), transferVelocity.getY(),
/*     */           transferVelocity.getZ(), otherPlayer.isSprinting(), protectionDeadline);
/*     */       if (!this.transfersInProgress.add(otherPlayer.getUniqueId())) {
/*     */         continue;
/*     */       }
/*     */       reservedAdditionalPlayers.add(otherPlayer);
/*     */       passengerTeleport.vehicleUUID = vehicleUUID;
/*     */       setTransferMetadata(passengerTeleport, transferId, border);
/*     */       pendingTeleports.put(otherPlayer.getUniqueId(), passengerTeleport);
/*     */     }
/*     */
/*     */     return new PreparedTransfer(
/*     */         transferId, player, border, to.clone(), destination, vehicle, reservedAdditionalPlayers, entityReferences,
/*     */         pendingTeleports, protectionDeadline, transferVelocity);
/*     */   }
/*     */
/*     */   private void completePreparedTransfer(PreparedTransfer transfer, boolean stored, Throwable throwable) {
/*     */     if (!stored || throwable != null) {
/*     */       if (throwable != null) {
/*     */         this.plugin.getLogger().log(Level.WARNING, "Failed to persist transfer " + transfer.transferId, unwrap(throwable));
/*     */       }
/*     */       keepInsideSource(transfer.player, transfer.sourceLocation, transfer.border);
/*     */       releaseTransferReservations(transfer);
/*     */       return;
/*     */     }
/*     */     if (!transfer.player.isOnline() || !transfer.player.isValid()) {
/*     */       releaseTransferReservations(transfer);
/*     */       return;
/*     */     }
/*     */     if (!isTargetAvailable(transfer.border)) {
/*     */       removeStoredTeleports(transfer);
/*     */       keepInsideSource(transfer.player, transfer.sourceLocation, transfer.border);
/*     */       releaseTransferReservations(transfer);
/*     */       return;
/*     */     }
/*     */
/*     */     EntityManager.removeEntitiesDirectly(transfer.entityReferences);
/*     */
/*     */     for (UUID playerId : transfer.pendingTeleports.keySet()) {
/*     */       markRecentTeleport(playerId, transfer.protectionDeadline);
/*     */     }
/*     */     if (transfer.border.isLocalWorldTransfer()) {
/*     */       restoreLocalWorldTransfer(transfer);
/*     */       this.plugin.getLogger().fine("Committed local-world transfer " + transfer.transferId + " for "
/*     */           + transfer.player.getName() + " to " + transfer.border.getTargetWorld() + " at "
/*     */           + String.format("%.2f, %.2f, %.2f", transfer.destination.getX(), transfer.destination.getY(),
/*     */               transfer.destination.getZ()));
/*     */       releaseTransferReservations(transfer);
/*     */       return;
/*     */     }
/*     */     sendPlayerToServer(transfer.player, transfer.border.getTargetServer());
/*     */     for (Player otherPlayer : transfer.additionalPlayers) {
/*     */       SchedulerAdapter.runEntity((Plugin)this.plugin, (Entity)otherPlayer, () -> {
/*     */             if (otherPlayer.isOnline() && otherPlayer.isValid()) {
/*     */               sendPlayerToServer(otherPlayer, transfer.border.getTargetServer());
/*     */             }
/*     */           });
/*     */     }
/*     */
/*     */     this.plugin.getLogger().fine("Committed transfer " + transfer.transferId + " for " + transfer.player.getName()
/*     */         + " to " + transfer.border.getTargetServer() + " at "
/*     */         + String.format("%.2f, %.2f, %.2f (velocity: %.3f, %.3f, %.3f)", transfer.destination.getX(),
/*     */             transfer.destination.getY(), transfer.destination.getZ(), transfer.velocity.getX(),
/*     */             transfer.velocity.getY(), transfer.velocity.getZ()));
/*     */     releaseTransferReservations(transfer);
/*     */   }
/*     */
/*     */   private void restoreLocalWorldTransfer(PreparedTransfer transfer) {
/*     */     CoordinateStorage.PendingTeleport primary = transfer.pendingTeleports.get(transfer.player.getUniqueId());
/*     */     this.plugin.getPlayerJoinListener().restoreLocalTransfer(transfer.player, primary);
/*     */     for (Player otherPlayer : transfer.additionalPlayers) {
/*     */       CoordinateStorage.PendingTeleport passenger = transfer.pendingTeleports.get(otherPlayer.getUniqueId());
/*     */       if (passenger != null) {
/*     */         this.plugin.getPlayerJoinListener().restoreLocalTransfer(otherPlayer, passenger);
/*     */       }
/*     */     }
/*     */   }
/*     */
/*     */   private void setTransferMetadata(CoordinateStorage.PendingTeleport teleport, UUID transferId, BorderConfig border) {
/*     */     teleport.transferId = transferId;
/*     */     teleport.sourceServer = this.configManager.getServerName();
/*     */     teleport.targetServer = border.getTargetServer();
/*     */   }
/*     */
/*     */   private UUID findVehicleUuid(List<EntityData> entities) {
/*     */     if (entities == null) {
/*     */       return null;
/*     */     }
/*     */     for (EntityData entity : entities) {
/*     */       if (entity.relation == EntityData.EntityRelation.VEHICLE) {
/*     */         return entity.entityUUID;
/*     */       }
/*     */     }
/*     */     return null;
/*     */   }
/*     */
/*     */   private void keepInsideSource(Player player, Location location, BorderConfig border) {
/*     */     if (player == null || !player.isOnline() || !player.isValid()) {
/*     */       return;
/*     */     }
/*     */     Entity vehicle = player.isInsideVehicle() ? player.getVehicle() : null;
/*     */     if (vehicle != null && vehicle.isValid()) {
/*     */       blockVehicleCrossing(player, vehicle, location, border);
/*     */       return;
/*     */     }
/*     */     player.teleportAsync(adjustLocationInsideSource(location.clone(), border), PlayerTeleportEvent.TeleportCause.PLUGIN)
/*     */         .whenComplete((success, throwable) -> {
/*     */           if (throwable != null) {
/*     */             logBlockedTeleportFailure((Entity)player, border, throwable);
/*     */           }
/*     */         });
/*     */     player.setVelocity(getBlockedVelocity(border));
/*     */     player.setFallDistance(0.0F);
/*     */     sendBlockedPassageMessage(player);
/*     */   }
/*     */
/*     */   private void removeStoredTeleports(PreparedTransfer transfer) {
/*     */     for (UUID playerId : transfer.pendingTeleports.keySet()) {
/*     */       this.coordinateStorage.removeTeleportAsync(playerId);
/*     */     }
/*     */   }
/*     */
/*     */   private void releaseTransferReservations(PreparedTransfer transfer) {
/*     */     for (UUID playerId : transfer.pendingTeleports.keySet()) {
/*     */       this.transfersInProgress.remove(playerId);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onVehicleMove(VehicleMoveEvent event) {
/* 353 */     Location from = event.getFrom();
/* 354 */     Location to = event.getTo();
/* 355 */     Vehicle vehicle = event.getVehicle();
/*     */ 
/*     */     
/* 358 */     boolean hasPlayerPassenger = false;
/* 359 */     Player controllingPlayer = null;
/*     */     
/* 361 */     for (Entity passenger : vehicle.getPassengers()) {
/* 362 */       if (passenger instanceof Player) {
/* 363 */         hasPlayerPassenger = true;
/* 364 */         if (controllingPlayer == null) {
/* 365 */           controllingPlayer = (Player)passenger;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     if (controllingPlayer != null && this.transfersInProgress.contains(controllingPlayer.getUniqueId())) {
/*     */       vehicle.setVelocity(new Vector(0.0D, 0.0D, 0.0D));
/*     */       return;
/*     */     }
/*     */
/*     */     
/* 372 */     if (vehicle.getType().name().equals("HAPPY_GHAST")) {
/*     */ 
/*     */       
/* 375 */       if (hasPlayerPassenger && controllingPlayer != null) {
/*     */         
/* 377 */         if (isOnCooldown(controllingPlayer.getUniqueId())) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 382 */         for (BorderConfig border : this.configManager.getBorders()) {
/* 383 */           if (!vehicle.getWorld().getName().equals(border.getWorld())) {
/*     */             continue;
/*     */           }
/* 386 */           if (!isRouteConfigured(border)) {
/*     */             continue;
/*     */           }
/* 390 */           if (hasCrossedBorder((Entity)vehicle, from, to, border) || isPastBorder(to, border)) {
/* 391 */             if (!isTargetAvailable(border)) {
/* 392 */               blockVehicleCrossing(controllingPlayer, (Entity)vehicle, to, border);
/*     */               return;
/*     */             } 
/* 395 */             handleBorderCrossing(controllingPlayer, from, to, border);
/*     */             return;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 397 */         Player nearbyPlayer = findPlayerNearHappyGhast((Entity)vehicle);
/* 398 */         if (nearbyPlayer != null) {
/*     */           
/* 400 */           if (isOnCooldown(nearbyPlayer.getUniqueId())) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/* 405 */           for (BorderConfig border : this.configManager.getBorders()) {
/* 406 */             if (!vehicle.getWorld().getName().equals(border.getWorld())) {
/*     */               continue;
/*     */             }
/* 409 */             if (!isRouteConfigured(border)) {
/*     */               continue;
/*     */             }
/* 414 */             if (hasCrossedBorder((Entity)vehicle, from, to, border) || isPastBorder(to, border)) {
/* 415 */               if (!isTargetAvailable(border)) {
/* 416 */                 blockVehicleCrossing(nearbyPlayer, (Entity)vehicle, to, border);
/*     */                 return;
/*     */               } 
/* 419 */               handleBorderCrossing(nearbyPlayer, nearbyPlayer.getLocation(), to, border);
/*     */               return;
/*     */             } 
/*     */           } 
/*     */         } else {
/*     */           return;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 429 */     if (!hasPlayerPassenger || controllingPlayer == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 434 */     if (isOnCooldown(controllingPlayer.getUniqueId())) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 439 */     for (BorderConfig border : this.configManager.getBorders()) {
/* 440 */       if (!vehicle.getWorld().getName().equals(border.getWorld())) {
/*     */         continue;
/*     */       }
/* 443 */       if (!isRouteConfigured(border)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 445 */       if (hasCrossedBorder((Entity)vehicle, from, to, border) || isPastBorder(to, border)) {
/* 446 */         if (!isTargetAvailable(border)) {
/* 447 */           blockVehicleCrossing(controllingPlayer, (Entity)vehicle, to, border);
/*     */           return;
/*     */         } 
/* 450 */         handleBorderCrossing(controllingPlayer, from, to, border);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Player findPlayerNearHappyGhast(Entity happyGhast) {
/* 458 */     Location ghastLoc = happyGhast.getLocation();
/*     */ 
/*     */     
/* 461 */     for (Entity entity : happyGhast.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
/* 462 */       if (entity instanceof Player) {
/* 463 */         Player player = (Player)entity;
/*     */ 
/*     */         
/* 466 */         double verticalDistance = Math.abs(player.getLocation().getY() - ghastLoc.getY());
/* 467 */         if (verticalDistance <= 3.0D) {
/* 468 */           return player;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 473 */     return null;
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onPlayerQuit(PlayerQuitEvent event) {
/* 488 */     UUID playerId = event.getPlayer().getUniqueId();
/* 489 */     this.recentTeleports.remove(playerId);
/* 490 */     this.blockedPassageMessages.remove(playerId);
/* 491 */     this.playersNearBorder.remove(playerId);
/*     */     this.transfersInProgress.remove(playerId);
/* 492 */     SchedulerAdapter.TaskHandle handle = this.foliaParticleTasks.remove(playerId);
/* 493 */     if (handle != null) {
/* 494 */       handle.cancel();
/*     */     }
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onBlockBreak(BlockBreakEvent event) {
/* 485 */     Player player = event.getPlayer();
/* 486 */     Location blockLoc = event.getBlock().getLocation();
/*     */ 
/*     */     
/* 489 */     for (BorderConfig border : this.configManager.getBorders()) {
/* 490 */       if (!player.getWorld().getName().equals(border.getWorld())) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 495 */       if (isBlockPastBorder(blockLoc, border)) {
/* 496 */         int distancePastBorder = getDistancePastBorder(blockLoc, border);
/*     */ 
/*     */ 
/*     */         
/* 500 */         if (distancePastBorder > 2) {
/* 501 */           event.setCancelled(true);
/* 502 */           player.sendMessage("§cYou cannot break blocks that far past the server border!");
/*     */           return;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @EventHandler
/*     */   public void onBlockPlace(BlockPlaceEvent event) {
/* 511 */     Player player = event.getPlayer();
/* 512 */     Location blockLoc = event.getBlock().getLocation();
/*     */ 
/*     */     
/* 515 */     for (BorderConfig border : this.configManager.getBorders()) {
/* 516 */       if (!player.getWorld().getName().equals(border.getWorld())) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 521 */       if (isBlockPastBorder(blockLoc, border)) {
/* 522 */         event.setCancelled(true);
/* 523 */         player.sendMessage("§cYou cannot place blocks past the server border!");
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isRouteConfigured(BorderConfig border) {
/*     */     return border.getTargetWorld() != null && !border.getTargetWorld().isBlank();
/*     */   }
/*     */
/*     */   private boolean isTargetAvailable(BorderConfig border) {
/*     */     if (border.isLocalWorldTransfer()) {
/*     */       return Bukkit.getWorld(border.getTargetWorld()) != null;
/*     */     }
/* 531 */     String targetServer = border.getTargetServer();
/* 532 */     return this.coordinateStorage.isServerOnlineCached(targetServer);
/*     */   }
/*     */ 
/*     */   private void blockPlayerCrossing(PlayerMoveEvent event, Player player, Location to, BorderConfig border) {
/* 536 */     Location safeLocation = adjustLocationInsideSource(to.clone(), border);
/* 537 */     event.setTo(safeLocation);
/* 538 */     player.setVelocity(getBlockedVelocity(border));
/* 539 */     player.setFallDistance(0.0F);
/* 540 */     sendBlockedPassageMessage(player);
/*     */   }
/*     */ 
/*     */   private void blockVehicleCrossing(Player player, Entity vehicle, Location to, BorderConfig border) {
/* 544 */     if (vehicle == null || !vehicle.isValid()) {
/* 545 */       return;
/*     */     }
/* 547 */     Location safeLocation = adjustLocationInsideSource(to.clone(), border);
/* 548 */     Vector blockedVelocity = getBlockedVelocity(border);
/* 549 */     vehicle.setVelocity(blockedVelocity);
/* 550 */     vehicle.setFallDistance(0.0F);
/* 551 */     sendBlockedPassageMessage(player);
/* 552 */     if (!shouldReposition(vehicle.getUniqueId(), BLOCKED_REPOSITION_COOLDOWN_MS)) {
/* 553 */       return;
/*     */     }
/* 555 */     vehicle.teleportAsync(safeLocation, PlayerTeleportEvent.TeleportCause.PLUGIN, new TeleportFlag[] { TeleportFlag.EntityState.RETAIN_PASSENGERS }).whenComplete((success, throwable) -> {
/* 556 */           if (throwable != null) {
/* 557 */             logBlockedTeleportFailure(vehicle, border, throwable);
/* 558 */           } else if (!Boolean.TRUE.equals(success)) {
/* 559 */             this.plugin.getLogger().warning("Failed to keep vehicle " + vehicle.getType() + " inside border " + border.getSide() + " because the async correction returned false.");
/*     */           } else {
/* 561 */             SchedulerAdapter.runEntity((Plugin)this.plugin, vehicle, () -> {
/* 562 */                   vehicle.setVelocity(getBlockedVelocity(border));
/* 563 */                   vehicle.setFallDistance(0.0F);
/*     */                 });
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   private void sendBlockedPassageMessage(Player player) {
/* 570 */     if (player == null) {
/* 571 */       return;
/*     */     }
/* 573 */     if (!shouldReposition(player.getUniqueId(), BLOCKED_MESSAGE_COOLDOWN_MS, this.blockedPassageMessages)) {
/* 574 */       return;
/*     */     }
/* 576 */     player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.configManager.getDestinationUnavailableMessage()));
/*     */   }
/*     */ 
/*     */   private boolean shouldReposition(UUID uniqueId, long cooldownMs) {
/* 580 */     return shouldReposition(uniqueId, cooldownMs, this.blockedRepositions);
/*     */   }
/*     */ 
/*     */   private boolean shouldReposition(UUID uniqueId, long cooldownMs, Map<UUID, Long> cooldowns) {
/* 584 */     long now = System.currentTimeMillis();
/* 585 */     Long blockedUntil = cooldowns.get(uniqueId);
/* 586 */     if (blockedUntil != null && blockedUntil.longValue() > now) {
/* 587 */       return false;
/*     */     }
/* 589 */     cooldowns.put(uniqueId, Long.valueOf(now + cooldownMs));
/* 590 */     return true;
/*     */   }
/*     */ 
/*     */   private Vector getBlockedVelocity(BorderConfig border) {
/* 594 */     switch (border.getSide()) {
/*     */       case EAST:
/* 596 */         return new Vector(-0.45D, 0.0D, 0.0D);
/*     */       case WEST:
/* 598 */         return new Vector(0.45D, 0.0D, 0.0D);
/*     */       case SOUTH:
/* 600 */         return new Vector(0.0D, 0.0D, -0.45D);
/*     */       case NORTH:
/* 602 */         return new Vector(0.0D, 0.0D, 0.45D);
/*     */     } 
/* 604 */     return new Vector(0.0D, 0.0D, 0.0D);
/*     */   }
/*     */
/*     */   private Location adjustExternalTeleportDestination(Location original) {
/*     */     double margin = this.configManager.getExternalTeleportBorderSafetyMargin();
/*     */     if (margin <= 0.0D || original.getWorld() == null) {
/*     */       return null;
/*     */     }
/*     */     Location adjusted = original.clone();
/*     */     boolean changed = false;
/*     */     String worldName = adjusted.getWorld().getName();
/*     */
/*     */     for (BorderConfig border : this.configManager.getBorders()) {
/*     */       if (!worldName.equals(border.getWorld())) {
/*     */         continue;
/*     */       }
/*     */       changed |= pushInsideConfiguredBorder(adjusted, border, margin);
/*     */     }
/*     */
/*     */     changed |= pushInsideVanillaWorldBorder(adjusted, margin);
/*     */     return changed ? adjusted : null;
/*     */   }
/*     */
/*     */   private boolean pushInsideConfiguredBorder(Location loc, BorderConfig border, double margin) {
/*     */     double coordinate = border.getCoordinate();
/*     */     switch (border.getSide()) {
/*     */       case EAST:
/*     */         return setXIfGreater(loc, coordinate - margin);
/*     */       case WEST:
/*     */         return setXIfLess(loc, coordinate + margin);
/*     */       case SOUTH:
/*     */         return setZIfGreater(loc, coordinate - margin);
/*     */       case NORTH:
/*     */         return setZIfLess(loc, coordinate + margin);
/*     */     }
/*     */     return false;
/*     */   }
/*     */
/*     */   private boolean pushInsideVanillaWorldBorder(Location loc, double margin) {
/*     */     World world = loc.getWorld();
/*     */     if (world == null) {
/*     */       return false;
/*     */     }
/*     */     WorldBorder worldBorder = world.getWorldBorder();
/*     */     Location center = worldBorder.getCenter();
/*     */     double halfSize = worldBorder.getSize() / 2.0D;
/*     */     double minX = center.getX() - halfSize + margin;
/*     */     double maxX = center.getX() + halfSize - margin;
/*     */     double minZ = center.getZ() - halfSize + margin;
/*     */     double maxZ = center.getZ() + halfSize - margin;
/*     */     boolean changed = false;
/*     */
/*     */     if (minX > maxX) {
/*     */       double centerX = center.getX();
/*     */       changed |= setXIfLess(loc, centerX);
/*     */       changed |= setXIfGreater(loc, centerX);
/*     */     } else {
/*     */       changed |= setXIfLess(loc, minX);
/*     */       changed |= setXIfGreater(loc, maxX);
/*     */     }
/*     */
/*     */     if (minZ > maxZ) {
/*     */       double centerZ = center.getZ();
/*     */       changed |= setZIfLess(loc, centerZ);
/*     */       changed |= setZIfGreater(loc, centerZ);
/*     */     } else {
/*     */       changed |= setZIfLess(loc, minZ);
/*     */       changed |= setZIfGreater(loc, maxZ);
/*     */     }
/*     */     return changed;
/*     */   }
/*     */
/*     */   private boolean setXIfLess(Location loc, double value) {
/*     */     if (loc.getX() >= value) {
/*     */       return false;
/*     */     }
/*     */     loc.setX(value);
/*     */     return true;
/*     */   }
/*     */
/*     */   private boolean setXIfGreater(Location loc, double value) {
/*     */     if (loc.getX() <= value) {
/*     */       return false;
/*     */     }
/*     */     loc.setX(value);
/*     */     return true;
/*     */   }
/*     */
/*     */   private boolean setZIfLess(Location loc, double value) {
/*     */     if (loc.getZ() >= value) {
/*     */       return false;
/*     */     }
/*     */     loc.setZ(value);
/*     */     return true;
/*     */   }
/*     */
/*     */   private boolean setZIfGreater(Location loc, double value) {
/*     */     if (loc.getZ() <= value) {
/*     */       return false;
/*     */     }
/*     */     loc.setZ(value);
/*     */     return true;
/*     */   }
/*     */ 
/*     */   private void logBlockedTeleportFailure(Entity entity, BorderConfig border, Throwable throwable) {
/* 608 */     Throwable actual = unwrap(throwable);
/* 609 */     this.plugin.getLogger().log(Level.WARNING, "Failed to snap " + entity.getType() + " back inside border " + border.getSide() + ": " + actual.getMessage(), actual);
/*     */   }
/*     */ 
/*     */   private Throwable unwrap(Throwable throwable) {
/* 613 */     Throwable current = throwable;
/* 614 */     while (current instanceof java.lang.reflect.InvocationTargetException || current instanceof CompletionException || current instanceof ExecutionException) {
/* 615 */       Throwable cause = current.getCause();
/* 616 */       if (cause == null) {
/* 617 */         break;
/*     */       }
/* 619 */       current = cause;
/*     */     } 
/* 621 */     return current;
/*     */   }
/*     */ 
/*     */   private boolean isBlockPastBorder(Location loc, BorderConfig border) {
/* 533 */     double coordinate = border.getCoordinate();
/*     */     
/* 535 */     switch (border.getSide()) {
/*     */       case EAST:
/* 537 */         return (loc.getBlockX() > (int)coordinate);
/*     */       case WEST:
/* 539 */         return (loc.getBlockX() < (int)coordinate);
/*     */       case SOUTH:
/* 541 */         return (loc.getBlockZ() > (int)coordinate);
/*     */       case NORTH:
/* 543 */         return (loc.getBlockZ() < (int)coordinate);
/*     */     } 
/* 545 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getDistancePastBorder(Location loc, BorderConfig border) {
/* 553 */     double coordinate = border.getCoordinate();
/*     */     
/* 555 */     switch (border.getSide()) {
/*     */       case EAST:
/* 557 */         return (int)Math.max(0.0D, loc.getBlockX() - coordinate);
/*     */       case WEST:
/* 559 */         return (int)Math.max(0.0D, coordinate - loc.getBlockX());
/*     */       case SOUTH:
/* 561 */         return (int)Math.max(0.0D, loc.getBlockZ() - coordinate);
/*     */       case NORTH:
/* 563 */         return (int)Math.max(0.0D, coordinate - loc.getBlockZ());
/*     */     } 
/* 565 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasCrossedBorder(Location from, Location to, BorderConfig border) {
/* 570 */     return hasCrossedBorder(from, to, border, MIN_SOURCE_TRIGGER_MARGIN);
/*     */   }
/*     */
/*     */   private boolean hasCrossedBorder(Entity movingEntity, Location from, Location to, BorderConfig border) {
/*     */     double margin = MIN_SOURCE_TRIGGER_MARGIN;
/*     */     if (movingEntity != null) {
/*     */       double halfSize;
/*     */       switch (border.getSide()) {
/*     */         case EAST:
/*     */         case WEST:
/*     */           halfSize = movingEntity.getBoundingBox().getWidthX() / 2.0D;
/*     */           break;
/*     */         case NORTH:
/*     */         case SOUTH:
/*     */           halfSize = movingEntity.getBoundingBox().getWidthZ() / 2.0D;
/*     */           break;
/*     */         default:
/*     */           halfSize = 0.0D;
/*     */       }
/*     */       margin = Math.max(margin, halfSize + 0.25D);
/*     */     }
/*     */     return hasCrossedBorder(from, to, border, margin);
/*     */   }
/*     */
/*     */   private boolean hasCrossedBorder(Location from, Location to, BorderConfig border, double margin) {
/*     */     double coordinate = border.getCoordinate();
/*     */     
/* 572 */     switch (border.getSide()) {
/*     */       case EAST:
/* 574 */         return (to.getX() >= coordinate - margin && to.getX() > from.getX());
/*     */       case WEST:
/* 576 */         return (to.getX() <= coordinate + margin && to.getX() < from.getX());
/*     */       case SOUTH:
/* 578 */         return (to.getZ() >= coordinate - margin && to.getZ() > from.getZ());
/*     */       case NORTH:
/* 580 */         return (to.getZ() <= coordinate + margin && to.getZ() < from.getZ());
/*     */     } 
/* 582 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isPastBorder(Location loc, BorderConfig border) {
/* 587 */     double coordinate = border.getCoordinate();
/*     */     
/* 589 */     switch (border.getSide()) {
/*     */       case EAST:
/* 591 */         return (loc.getX() > coordinate);
/*     */       case WEST:
/* 593 */         return (loc.getX() < coordinate);
/*     */       case SOUTH:
/* 595 */         return (loc.getZ() > coordinate);
/*     */       case NORTH:
/* 597 */         return (loc.getZ() < coordinate);
/*     */     } 
/* 599 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkPlayerNearBorder(Player player) {
/* 604 */     boolean isNear = false;
/*     */     
/* 606 */     for (BorderConfig border : this.configManager.getBorders()) {
/* 607 */       if (!player.getWorld().getName().equals(border.getWorld())) {
/*     */         continue;
/*     */       }
/*     */       
/* 611 */       if (!border.isShowParticles()) {
/*     */         continue;
/*     */       }
/*     */       
/* 615 */       int distance = getDistanceToBorder(player.getLocation(), border);
/*     */       
/* 617 */       if (distance <= this.configManager.getParticleDistance()) {
/* 618 */         isNear = true;
/*     */         
/* 620 */         UUID uUID = player.getUniqueId();
/* 621 */         if (!this.playersNearBorder.contains(uUID) || distance <= 10) {
/* 622 */           ParticleBarrier.showBarrier(player, border, this.configManager.getParticleDistance());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 627 */     UUID playerId = player.getUniqueId();
/* 628 */     if (isNear) {
/* 629 */       this.playersNearBorder.add(playerId);
/*     */     } else {
/* 631 */       this.playersNearBorder.remove(playerId);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getDistanceToBorder(Location location, BorderConfig border) {
/* 636 */     double coordinate = border.getCoordinate();
/*     */     
/* 638 */     switch (border.getSide()) {
/*     */       case EAST:
/*     */       case WEST:
/* 641 */         return (int)Math.abs(location.getX() - coordinate);
/*     */       
/*     */       case SOUTH:
/*     */       case NORTH:
/* 645 */         return (int)Math.abs(location.getZ() - coordinate);
/*     */     } 
/*     */     
/* 648 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   private World resolveTargetInspectionWorld(World fallbackWorld, BorderConfig border) {
/* 652 */     String targetWorldName = border.getTargetWorld();
/* 653 */     if (targetWorldName == null || targetWorldName.isBlank()) {
/* 654 */       return fallbackWorld;
/*     */     }
/* 656 */     World targetWorld = Bukkit.getWorld(targetWorldName);
/* 657 */     return (targetWorld != null) ? targetWorld : fallbackWorld;
/*     */   }
/*     */ 
/*     */   private String getBorderAxis(BorderConfig.BorderSide side) {
/* 661 */     switch (side) {
/*     */       case EAST:
/*     */       case WEST:
/* 664 */         return "X";
/*     */       case SOUTH:
/*     */       case NORTH:
/* 667 */         return "Z";
/*     */     } 
/* 669 */     return "X";
/*     */   }
/*     */ 
/*     */   private String getBorderDirection(BorderConfig.BorderSide side) {
/* 673 */     switch (side) {
/*     */       case EAST:
/*     */       case SOUTH:
/* 676 */         return "POSITIVE";
/*     */       case WEST:
/*     */       case NORTH:
/* 679 */         return "NEGATIVE";
/*     */     } 
/* 681 */     return "POSITIVE";
/*     */   }
/*     */ 
/*     */   
/*     */   private Location adjustDestinationInsideTarget(Location loc, BorderConfig border) {
/* 685 */     double coordinate = border.getTargetCoordinate();
/* 686 */     double offset = border.getTargetOffset();
/* 687 */     switch (border.getTargetSide()) {
/*     */       case WEST:
/* 689 */         loc.setX(coordinate + offset);
/*     */         break;
/*     */       case EAST:
/* 692 */         loc.setX(coordinate - offset);
/*     */         break;
/*     */       case NORTH:
/* 695 */         loc.setZ(coordinate + offset);
/*     */         break;
/*     */       case SOUTH:
/* 698 */         loc.setZ(coordinate - offset);
/*     */         break;
/*     */     } 
/* 700 */     return loc;
/*     */   }
/*     */ 
/*     */   private Location adjustLocationInsideSource(Location loc, BorderConfig border) {
/* 672 */     double coordinate = border.getCoordinate();
/* 673 */     switch (border.getSide()) {
/*     */       case EAST:
/* 675 */         loc.setX(Math.min(loc.getX(), coordinate - SOURCE_BLOCK_OFFSET));
/*     */         break;
/*     */       case WEST:
/* 678 */         loc.setX(Math.max(loc.getX(), coordinate + SOURCE_BLOCK_OFFSET));
/*     */         break;
/*     */       case SOUTH:
/* 681 */         loc.setZ(Math.min(loc.getZ(), coordinate - SOURCE_BLOCK_OFFSET));
/*     */         break;
/*     */       case NORTH:
/* 684 */         loc.setZ(Math.max(loc.getZ(), coordinate + SOURCE_BLOCK_OFFSET));
/*     */         break;
/*     */     } 
/* 687 */     return loc;
/*     */   }
/*     */ 
/*     */   
/*     */   private Vector getTransferVelocity(Player player, Entity vehicle) {
/* 691 */     Vector sourceVelocity = (vehicle != null && vehicle.isValid()) ? vehicle.getVelocity() : player.getVelocity();
/* 692 */     if (sourceVelocity == null) {
/* 693 */       return new Vector(0.0D, 0.0D, 0.0D);
/*     */     }
/* 695 */     Vector velocity = sourceVelocity.clone();
/* 696 */     if (!Double.isFinite(velocity.getX()) || !Double.isFinite(velocity.getY()) || !Double.isFinite(velocity.getZ())) {
/* 697 */       return new Vector(0.0D, 0.0D, 0.0D);
/*     */     }
/* 699 */     return velocity;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isOnCooldown(UUID playerId) {
/* 684 */     Long protectedUntil = this.recentTeleports.get(playerId);
/* 685 */     if (protectedUntil == null) {
/* 686 */       return false;
/*     */     }
/*     */     
/* 689 */     long now = System.currentTimeMillis();
/* 690 */     if (protectedUntil.longValue() <= now) {
/* 691 */       this.recentTeleports.remove(playerId);
/* 692 */       return false;
/*     */     } 
/* 694 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void markRecentTeleport(UUID playerId, long protectedUntil) {
/* 699 */     this.recentTeleports.merge(playerId, Long.valueOf(protectedUntil), (current, incoming) -> Long.valueOf(Math.max(current.longValue(), incoming.longValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   private void sendPlayerToServer(Player player, String serverName) {
/* 704 */     ByteArrayDataOutput out = ByteStreams.newDataOutput();
/* 705 */     out.writeUTF("Connect");
/* 706 */     out.writeUTF(serverName);
/*     */ 
/*     */     
/* 709 */     player.sendPluginMessage((Plugin)this.plugin, "BungeeCord", out.toByteArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearPlayerNearBorder(UUID playerId) {
/* 680 */     this.playersNearBorder.remove(playerId);
/*     */   }
/*     */   
/*     */   public void cleanup() {
/* 684 */     if (this.particleTask != null) {
/* 685 */       this.particleTask.cancel();
/* 686 */       this.particleTask = null;
/*     */     } 
/* 688 */     if (!this.foliaParticleTasks.isEmpty()) {
/* 689 */       this.foliaParticleTasks.values().forEach(SchedulerAdapter.TaskHandle::cancel);
/* 690 */       this.foliaParticleTasks.clear();
/*     */     }
/* 688 */     if (this.cleanupTask != null) {
/* 689 */       this.cleanupTask.cancel();
/* 690 */       this.cleanupTask = null;
/*     */     } 
/* 692 */     this.recentTeleports.clear();
/* 693 */     this.blockedPassageMessages.clear();
/* 694 */     this.blockedRepositions.clear();
/* 695 */     this.playersNearBorder.clear();
/*     */     this.transfersInProgress.clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\listeners\BorderListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
