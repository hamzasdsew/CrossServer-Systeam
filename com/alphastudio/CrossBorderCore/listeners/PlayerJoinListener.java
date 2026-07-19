package com.alphastudio.CrossBorderCore.listeners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import com.alphastudio.CrossBorderCore.CrossBorderCore;
import com.alphastudio.CrossBorderCore.storage.BlockManager;
import com.alphastudio.CrossBorderCore.storage.CoordinateStorage;
import com.alphastudio.CrossBorderCore.storage.EntityData;
import com.alphastudio.CrossBorderCore.storage.EntityManager;
import com.alphastudio.CrossBorderCore.util.SchedulerAdapter;

public class PlayerJoinListener implements Listener {
  private static final Class<?> PAPER_LEASHABLE_CLASS = resolvePaperLeashableClass();
  private static final Method PAPER_SET_LEASH_HOLDER_METHOD =
      resolvePaperLeashableMethod("setLeashHolder", Entity.class);

  private final CrossBorderCore plugin;
  private final CoordinateStorage coordinateStorage;
  private final BorderListener borderListener;

  public PlayerJoinListener(CrossBorderCore plugin, CoordinateStorage coordinateStorage, BorderListener borderListener) {
    this.plugin = plugin;
    this.coordinateStorage = coordinateStorage;
    this.borderListener = borderListener;
  }

  public void restoreLocalTransfer(Player player, CoordinateStorage.PendingTeleport pending) {
    if (player == null || pending == null) {
      return;
    }
    UUID playerId = player.getUniqueId();
    SchedulerAdapter.TaskHandle handle =
        SchedulerAdapter.runEntity(
            (Plugin) this.plugin,
            (Entity) player,
            () -> handlePendingTeleport(player, playerId, player.getName(), pending));
    if (!handle.isPresent()) {
      this.plugin.getLogger().warning("Could not schedule local-world transfer for " + player.getName() + ".");
    }
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    UUID playerId = player.getUniqueId();
    String playerName = player.getName();

    this.borderListener.clearPlayerNearBorder(playerId);

    this.coordinateStorage
        .getTeleportAsync(playerId)
        .whenComplete(
            (pending, throwable) -> {
              if (throwable != null) {
                this.plugin
                    .getLogger()
                    .log(Level.WARNING, "Failed to read pending teleport for " + playerName, unwrap(throwable));
                return;
              }
              if (pending == null) {
                return;
              }
              SchedulerAdapter.TaskHandle handle =
                  SchedulerAdapter.runEntity(
                      (Plugin) this.plugin,
                      (Entity) player,
                      () -> handlePendingTeleport(player, playerId, playerName, pending));
              if (!handle.isPresent()) {
                this.plugin
                    .getLogger()
                    .warning("Could not schedule pending teleport processing for " + playerName + ".");
              }
            });
  }

  private void handlePendingTeleport(
      Player player, UUID playerId, String playerName, CoordinateStorage.PendingTeleport pending) {
    String localServer = this.plugin.getConfigManager().getServerName();
    if (pending.targetServer != null
        && !pending.targetServer.isBlank()
        && !pending.targetServer.equals(localServer)) {
      this.plugin
          .getLogger()
          .fine(
              "Ignoring transfer "
                  + pending.transferId
                  + " for "
                  + playerName
                  + " because it targets "
                  + pending.targetServer
                  + ", not "
                  + localServer
                  + ".");
      return;
    }
    World world = Bukkit.getWorld(pending.world);
    if (world == null) {
      this.plugin
          .getLogger()
          .severe(
              "Cannot restore pending teleport for "
                  + player.getName()
                  + " because world '"
                  + pending.world
                  + "' is not loaded.");
      player.sendMessage("Teleport failed: target world is not loaded. Please contact an admin.");
      return;
    }

    if (pending.arrivalProtectedUntil > System.currentTimeMillis()) {
      this.borderListener.markRecentTeleport(playerId, pending.arrivalProtectedUntil);
    }

    Location exactLoc = new Location(world, pending.x, pending.y, pending.z, pending.yaw, pending.pitch);
    SchedulerAdapter.TaskHandle handle =
        SchedulerAdapter.runEntityLater(
            (Plugin) this.plugin,
            (Entity) player,
            () -> prepareDestinationAndTeleport(player, playerName, pending, exactLoc),
            1L);

    if (!handle.isPresent()) {
      this.plugin
          .getLogger()
          .severe(
              "Failed to schedule pending teleport restore for "
                  + player.getName()
                  + " on Canvas/Folia.");
    }
  }

  private void prepareDestinationAndTeleport(
      Player player, String playerName, CoordinateStorage.PendingTeleport pending, Location exactLoc) {
    if (!player.isOnline() || !player.isValid()) {
      return;
    }

    World world = exactLoc.getWorld();
    if (world == null) {
      this.plugin
          .getLogger()
          .severe("Cannot prepare destination for " + playerName + " because the world is missing.");
      return;
    }

    SchedulerAdapter.TaskHandle regionHandle =
        SchedulerAdapter.runRegion(
            (Plugin) this.plugin,
            world,
            exactLoc.getBlockX(),
            exactLoc.getBlockZ(),
            () -> {
              if (this.plugin.getConfigManager().isUnsafeDestinationBlockClearingEnabled()) {
                if (pending.blocks != null && !pending.blocks.isEmpty()) {
                  BlockManager.restoreBlocks(
                      pending.blocks,
                      exactLoc,
                      pending.borderAxis,
                      pending.borderCoordinate,
                      pending.borderDirection,
                      pending.isCrawling);
                } else {
                  BlockManager.clearBlocksAtDestination(
                      exactLoc,
                      pending.borderAxis,
                      pending.borderCoordinate,
                      pending.borderDirection,
                      pending.isCrawling);
                }
              }

              player
                  .teleportAsync(exactLoc, TeleportCause.PLUGIN)
                  .whenComplete(
                      (success, throwable) ->
                          handleTeleportCompletion(
                              player, playerName, pending, exactLoc, success, throwable));
            });

    if (!regionHandle.isPresent()) {
      this.plugin
          .getLogger()
          .severe(
              "Failed to schedule destination region preparation for "
                  + player.getName()
                  + " at "
                  + formatLocation(exactLoc)
                  + ".");
    }
  }

  private void handleTeleportCompletion(
      Player player,
      String playerName,
      CoordinateStorage.PendingTeleport pending,
      Location exactLoc,
      Boolean success,
      Throwable throwable) {
    if (throwable != null) {
      this.plugin
          .getLogger()
          .log(
              Level.SEVERE,
              "Async teleport failed for " + playerName + " to " + formatLocation(exactLoc),
              unwrap(throwable));
      return;
    }
    if (!Boolean.TRUE.equals(success)) {
      this.plugin
          .getLogger()
          .warning(
              "Async teleport returned false for "
                  + playerName
                  + " to "
                  + formatLocation(exactLoc)
                  + ".");
      return;
    }

    SchedulerAdapter.TaskHandle postHandle =
        SchedulerAdapter.runEntity(
            (Plugin) this.plugin,
            (Entity) player,
            () -> completeTeleportRestore(player, pending, exactLoc));

    if (!postHandle.isPresent()) {
      this.plugin
          .getLogger()
          .severe(
              "Teleport completed for "
                  + playerName
                  + " but post-teleport restore could not be scheduled.");
    }
  }

  private void completeTeleportRestore(
      Player player, CoordinateStorage.PendingTeleport pending, Location exactLoc) {
    if (!player.isOnline() || !player.isValid()) {
      return;
    }

    this.coordinateStorage.removeTeleportAsync(player.getUniqueId());
    restorePlayerAttributes(player, pending);

    this.plugin
        .getLogger()
        .fine(
            "Teleported "
                + player.getName()
                + " to stored position: "
                + String.format("%.2f, %.2f, %.2f", pending.x, pending.y, pending.z));

    if (pending.entities != null && !pending.entities.isEmpty()) {
      SchedulerAdapter.TaskHandle spawnHandle =
          SchedulerAdapter.runEntityLater(
              (Plugin) this.plugin,
              (Entity) player,
              () -> spawnEntities(player, pending.entities, exactLoc, pending),
              3L);
      if (!spawnHandle.isPresent()) {
        this.plugin
            .getLogger()
            .warning("Could not schedule transported entity restore for " + player.getName() + ".");
      }
    } else if (pending.vehicleUUID != null) {
      attemptVehicleMount(player, pending, 0);
    } else {
      schedulePlayerMomentumRestore(player, pending, 1L);
    }
  }

  private void attemptVehicleMount(Player player, CoordinateStorage.PendingTeleport pending, int attemptNumber) {
    int maxAttempts = 10;
    long delayTicks = 5L + attemptNumber * 2L;

    SchedulerAdapter.runEntityLater(
        (Plugin) this.plugin,
        (Entity) player,
        () -> {
          Entity vehicleEntity = this.plugin.getVehicleByOldUUID(pending.vehicleUUID);
          if (vehicleEntity != null && vehicleEntity.isValid()) {
            vehicleEntity.setFallDistance(0.0F);
            player.setFallDistance(0.0F);
            if (!vehicleEntity.getPassengers().contains(player)) {
              vehicleEntity.addPassenger((Entity) player);
            }
            continueMountedMovement(player, pending, vehicleEntity, true);
            this.plugin
                .getLogger()
                .fine(
                    "Mounted passenger "
                        + player.getName()
                        + " on vehicle "
                        + vehicleEntity.getType()
                        + " (UUID: "
                        + vehicleEntity.getUniqueId()
                        + ") after "
                        + (attemptNumber + 1)
                        + " attempt(s)");
          } else if (attemptNumber < maxAttempts - 1) {
            this.plugin
                .getLogger()
                .fine(
                    "Vehicle "
                        + pending.vehicleUUID
                        + " not found for "
                        + player.getName()
                        + " - retrying (attempt "
                        + (attemptNumber + 1)
                        + "/"
                        + maxAttempts
                        + ")");
            attemptVehicleMount(player, pending, attemptNumber + 1);
          } else {
            this.plugin
                .getLogger()
                .warning(
                    "Could not find vehicle "
                        + pending.vehicleUUID
                        + " to mount "
                        + player.getName()
                        + " after "
                        + maxAttempts
                        + " attempts - vehicle may not have spawned or mapping expired");
            schedulePlayerMomentumRestore(player, pending, 1L);
          }
        },
        delayTicks);
  }

  private void restorePlayerAttributes(Player player, CoordinateStorage.PendingTeleport pending) {
    player.setAllowFlight(pending.allowFlight);
    player.setFlying(pending.allowFlight && pending.isFlying);
    player.setGliding(pending.isGliding);
    player.setSwimming(pending.isCrawling);
    player.setSprinting(pending.sprinting);

    float flySpeed = (pending.flySpeed > 0.0F && pending.flySpeed <= 1.0F) ? pending.flySpeed : 0.1F;
    float walkSpeed = (pending.walkSpeed > 0.0F && pending.walkSpeed <= 1.0F) ? pending.walkSpeed : 0.2F;
    player.setFlySpeed(flySpeed);
    player.setWalkSpeed(walkSpeed);
    player.setFallDistance(0.0F);

    this.plugin
        .getLogger()
        .fine(
            "Restored attributes for "
                + player.getName()
                + " - Flying: "
                + pending.isFlying
                + ", Gliding: "
                + pending.isGliding
                + ", Crawling: "
                + pending.isCrawling
                + ", Sprinting: "
                + pending.sprinting);
  }

  private void schedulePlayerMomentumRestore(
      Player player, CoordinateStorage.PendingTeleport pending, long delayTicks) {
    SchedulerAdapter.TaskHandle momentumHandle =
        SchedulerAdapter.runEntityLater(
            (Plugin) this.plugin,
            (Entity) player,
            () -> continuePlayerMovement(player, pending),
            delayTicks);
    if (!momentumHandle.isPresent()) {
      this.plugin
          .getLogger()
          .warning("Could not schedule momentum restore for " + player.getName() + ".");
      return;
    }

    if (pending.isGliding || pending.isFlying) {
      SchedulerAdapter.runEntityLater(
          (Plugin) this.plugin,
          (Entity) player,
          () -> continuePlayerMovement(player, pending),
          delayTicks + 2L);
    }
  }

  private void continuePlayerMovement(Player player, CoordinateStorage.PendingTeleport pending) {
    if (!player.isOnline() || !player.isValid()) {
      return;
    }
    player.setFallDistance(0.0F);
    player.setSprinting(pending.sprinting);
    if (pending.allowFlight && pending.isFlying) {
      player.setFlying(true);
    }
    if (pending.isGliding) {
      player.setGliding(true);
    }
    applyVelocity(player, getStoredVelocity(pending));
  }

  private void continueMountedMovement(
      Player player,
      CoordinateStorage.PendingTeleport pending,
      Entity mount,
      boolean reapplyPlayerState) {
    if (mount == null || !mount.isValid()) {
      schedulePlayerMomentumRestore(player, pending, 1L);
      return;
    }
    mount.setFallDistance(0.0F);
    player.setFallDistance(0.0F);
    if (reapplyPlayerState && player.isOnline() && player.isValid()) {
      player.setSprinting(false);
      if (pending.isGliding) {
        player.setGliding(false);
      }
    }
    applyVelocity(mount, getStoredVelocity(pending));
  }

  private void restoreMomentum(Player player, CoordinateStorage.PendingTeleport pending) {
    continuePlayerMovement(player, pending);
  }

  private void spawnEntities(
      Player player,
      List<EntityData> entities,
      Location playerLocation,
      CoordinateStorage.PendingTeleport pending) {
    List<Entity> spawnedEntities = new ArrayList<>();
    Entity vehicleEntity = null;
    int failedSpawns = 0;

    for (EntityData entityData : entities) {
      Entity spawnedEntity = EntityManager.spawnEntity(playerLocation.getWorld(), entityData, playerLocation);
      if (spawnedEntity != null) {
        spawnedEntities.add(spawnedEntity);
        this.plugin
            .getLogger()
            .fine(
                "Spawned entity: "
                    + spawnedEntity.getType()
                    + " (relation: "
                    + entityData.relation
                    + ", passengers: "
                    + entityData.passengers.size()
                    + ")");

        if (entityData.relation == EntityData.EntityRelation.VEHICLE) {
          vehicleEntity = spawnedEntity;
          this.plugin.registerVehicleRespawn(entityData.entityUUID, spawnedEntity);
        }
      } else {
        failedSpawns++;
        this.plugin
            .getLogger()
            .severe(
                "FAILED to spawn entity for "
                    + player.getName()
                    + " - Type: "
                    + entityData.entityType
                    + ", Relation: "
                    + entityData.relation
                    + ", UUID: "
                    + entityData.entityUUID
                    + ", Passengers: "
                    + entityData.passengers.size());
      }
    }

    if (failedSpawns > 0) {
      this.plugin
          .getLogger()
          .warning(
              "Failed to spawn "
                  + failedSpawns
                  + "/"
                  + entities.size()
                  + " entities for "
                  + player.getName()
                  + ". Check logs for details.");
    }

    if (spawnedEntities.isEmpty()) {
      return;
    }

    this.plugin.getLogger().fine("Spawned " + spawnedEntities.size() + " entities for " + player.getName());

    Entity finalVehicleEntity = vehicleEntity;
    SchedulerAdapter.runEntityLater(
        (Plugin) this.plugin,
        (Entity) player,
        () -> {
          if (finalVehicleEntity != null && finalVehicleEntity.isValid()) {
            if (!finalVehicleEntity.getPassengers().contains(player)) {
              finalVehicleEntity.addPassenger((Entity) player);
            }
            continueMountedMovement(player, pending, finalVehicleEntity, true);
            this.plugin.getLogger().fine("Mounted " + player.getName() + " on " + finalVehicleEntity.getType());
          }

          Map<UUID, Entity> oldUuidToEntityMap = new HashMap<>();
          oldUuidToEntityMap.put(player.getUniqueId(), player);

          int i = 0;
          while (i < entities.size() && i < spawnedEntities.size()) {
            EntityData entityData = entities.get(i);
            Entity spawnedEntity = spawnedEntities.get(i);
            oldUuidToEntityMap.put(entityData.entityUUID, spawnedEntity);
            i++;
          }

          i = 0;
          while (i < entities.size() && i < spawnedEntities.size()) {
            EntityData entityData = entities.get(i);
            Entity spawnedEntity = spawnedEntities.get(i);

            if (entityData.relation == EntityData.EntityRelation.LEASHED
                && entityData.leashHolderUUID != null) {
              Entity holder = oldUuidToEntityMap.get(entityData.leashHolderUUID);
              if (holder == null) {
                this.plugin
                    .getLogger()
                    .warning(
                        "Could not find leash holder for "
                            + entityData.entityType
                            + " (holder UUID: "
                            + entityData.leashHolderUUID
                            + ")");
              } else {
                boolean leashed = false;
                try {
                  if (PAPER_LEASHABLE_CLASS != null
                      && PAPER_SET_LEASH_HOLDER_METHOD != null
                      && PAPER_LEASHABLE_CLASS.isInstance(spawnedEntity)) {
                    Object leashable = PAPER_LEASHABLE_CLASS.cast(spawnedEntity);
                    PAPER_SET_LEASH_HOLDER_METHOD.invoke(leashable, holder);
                    leashed = true;
                  } else if (spawnedEntity instanceof LivingEntity livingEntity) {
                    livingEntity.setLeashHolder(holder);
                    leashed = true;
                  }
                } catch (Exception exception) {
                  if (spawnedEntity instanceof LivingEntity livingEntity) {
                    livingEntity.setLeashHolder(holder);
                    leashed = true;
                  }
                }
                if (leashed) {
                  String holderName =
                      (holder instanceof Player) ? ((Player) holder).getName() : holder.getType().name();
                  this.plugin.getLogger().fine("Leashed " + spawnedEntity.getType() + " to " + holderName);
                }
              }
            }
            i++;
          }

          Vector storedVelocity = getStoredVelocity(pending);
          if (storedVelocity.lengthSquared() > 0.0D) {
            for (Entity spawnedEntity : spawnedEntities) {
              applyVelocity(spawnedEntity, storedVelocity.clone());
            }
          }

          if (finalVehicleEntity == null) {
            schedulePlayerMomentumRestore(player, pending, 1L);
          }
        },
        2L);
  }

  private Vector getStoredVelocity(CoordinateStorage.PendingTeleport pending) {
    return new Vector(pending.velocityX, pending.velocityY, pending.velocityZ);
  }

  private void applyVelocity(Entity entity, Vector velocity) {
    if (entity == null || !entity.isValid() || velocity.lengthSquared() <= 0.0D) {
      return;
    }
    entity.setFallDistance(0.0F);
    entity.setVelocity(velocity);
  }

  private String formatLocation(Location location) {
    return location.getWorld().getName()
        + " "
        + String.format("%.2f, %.2f, %.2f", location.getX(), location.getY(), location.getZ());
  }

  private Throwable unwrap(Throwable throwable) {
    Throwable current = throwable;
    while (current instanceof InvocationTargetException
        || current instanceof CompletionException
        || current instanceof ExecutionException) {
      Throwable cause = current.getCause();
      if (cause == null) {
        break;
      }
      current = cause;
    }
    return current;
  }

  private static Class<?> resolvePaperLeashableClass() {
    try {
      return Class.forName("io.papermc.paper.entity.Leashable");
    } catch (ClassNotFoundException exception) {
      return null;
    }
  }

  private static Method resolvePaperLeashableMethod(String name, Class<?>... parameterTypes) {
    if (PAPER_LEASHABLE_CLASS == null) {
      return null;
    }
    try {
      return PAPER_LEASHABLE_CLASS.getMethod(name, parameterTypes);
    } catch (ReflectiveOperationException exception) {
      return null;
    }
  }
}
