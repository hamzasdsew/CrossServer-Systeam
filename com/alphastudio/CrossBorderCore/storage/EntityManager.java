/*     */ package com.alphastudio.CrossBorderCore.storage;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Ageable;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.EntityType;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.InventoryHolder;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import com.alphastudio.CrossBorderCore.CrossBorderCore;
/*     */ import com.alphastudio.CrossBorderCore.config.ConfigManager;
/*     */ 
/*     */ public class EntityManager {
/*     */   private static final double LEASH_SEARCH_RADIUS = 10.0D;
/*     */   private static final Class<?> PAPER_LEASHABLE_CLASS = resolvePaperLeashableClass();
/*     */   private static final Method PAPER_GET_LEASH_HOLDER_METHOD = resolvePaperLeashableMethod("getLeashHolder");
/*     */   private static final Method PAPER_IS_LEASHED_METHOD = resolvePaperLeashableMethod("isLeashed");
/*     */   private static final Method PAPER_SET_LEASH_HOLDER_METHOD = resolvePaperLeashableMethod("setLeashHolder", Entity.class);
/*     */ 
/*     */   public static class CollectionResult {
/*     */     public final List<EntityData> entityDataList;
/*     */     public final List<Entity> entityReferences;
/*     */     
/*     */     public CollectionResult(List<EntityData> entityDataList, List<Entity> entityReferences) {
/*  30 */       this.entityDataList = entityDataList;
/*  31 */       this.entityReferences = entityReferences;
/*     */     }
/*     */   }
/*     */   
/*     */   public static CollectionResult collectEntitiesWithReferences(Player player, ConfigManager config, boolean includeVehicles, boolean includeLeashedMobs, int nearbyDistance, int maxEntities) {
/*  36 */     CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Starting entity collection for " + player.getName() + "...");
/*     */     
/*  38 */     List<EntityData> finalEntityData = new ArrayList<>();
/*  39 */     List<Entity> entityReferences = new ArrayList<>();
/*  40 */     Set<UUID> processedEntities = new HashSet<>();
/*  41 */     Queue<Entity> toProcess = new ArrayDeque<>();
/*     */ 
/*     */     
/*  44 */     Set<Entity> seedEntities = new HashSet<>();
/*  45 */     seedEntities.add(player);
/*     */     
/*  47 */     if (includeVehicles && player.isInsideVehicle()) {
/*  48 */       Entity vehicle = player.getVehicle();
/*  49 */       if (vehicle != null && isValidVehicle(vehicle)) {
/*  50 */         seedEntities.add(vehicle);
/*     */       }
/*     */     } 
/*     */     
/*  54 */     int maxRadius = Math.max(3, nearbyDistance);
/*  55 */     Collection<Entity> nearbyEntities = player.getWorld().getNearbyEntities(player.getLocation(), maxRadius, maxRadius, maxRadius);
/*     */     Collection<Entity> leashedCandidates = includeLeashedMobs
/*     */         ? player.getWorld().getNearbyEntities(player.getLocation(), LEASH_SEARCH_RADIUS, LEASH_SEARCH_RADIUS, LEASH_SEARCH_RADIUS)
/*     */         : Collections.emptyList();
/*     */ 
/*     */ 
/*     */     
/*  59 */     preMarkNonPlayerPassengers(nearbyEntities, processedEntities);
/*     */     
/*  69 */     for (Entity nearby : nearbyEntities) {
/*  70 */       if (shouldCollectNearbyEntity(nearby, player, config)) {
/*  71 */         seedEntities.add(nearby);
/*     */       }
/*     */     } 
/*     */     
/*  75 */     toProcess.addAll(seedEntities);
/*  76 */     CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Found " + seedEntities.size() + " seed entities. Starting graph traversal.");
/*     */ 
/*     */     
/*  79 */     while (!toProcess.isEmpty() && finalEntityData.size() < maxEntities) {
/*  80 */       Entity currentEntity = toProcess.poll();
/*     */       
/*  82 */       if (currentEntity == null) {
/*     */         continue;
/*     */       }
/*     */       
/*  86 */       if (processedEntities.contains(currentEntity.getUniqueId())) {
/*  87 */         CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] SKIPPED DUPLICATE: " + String.valueOf(currentEntity.getType()) + " (UUID: " + String.valueOf(currentEntity.getUniqueId()) + ") - already processed");
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*  92 */       if (!(currentEntity instanceof Player)) {
/*     */         EntityData.EntityRelation relation = determineRelation(currentEntity, player);
/*  93 */         CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] COLLECTING: " + String.valueOf(currentEntity.getType()) + " (UUID: " + String.valueOf(currentEntity.getUniqueId()) + ", Relation: " + String.valueOf(relation) + ")");
/*  94 */         finalEntityData.add(EntityData.fromEntity(currentEntity, relation, player.getLocation()));
/*  95 */         entityReferences.add(currentEntity);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 100 */         List<Entity> passengers = currentEntity.getPassengers();
/* 101 */         CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Entity " + String.valueOf(currentEntity.getType()) + " has " + passengers.size() + " passengers");
/* 102 */         for (Entity passenger : passengers) {
/* 103 */           if (!(passenger instanceof Player)) {
/*     */             
/* 105 */             if (!entityReferences.contains(passenger)) {
/* 106 */               CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Adding passenger " + String.valueOf(passenger.getType()) + " (UUID: " + 
/* 107 */                   String.valueOf(passenger.getUniqueId()) + ") to entity references for removal");
/* 108 */               entityReferences.add(passenger);
/*     */             } 
/*     */             
/* 111 */             if (!processedEntities.contains(passenger.getUniqueId())) {
/* 112 */               processedEntities.add(passenger.getUniqueId());
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/* 117 */       processedEntities.add(currentEntity.getUniqueId());
/*     */       
/* 119 */       if (!includeLeashedMobs) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 124 */       Entity holder = getLeashHolder(currentEntity);
/* 125 */       if (holder != null && !processedEntities.contains(holder.getUniqueId())) {
/* 126 */         toProcess.add(holder);
/* 127 */         CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Found leash holder (" + String.valueOf(holder.getType()) + "), adding to queue.");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 133 */       for (Entity potentialLeashed : leashedCandidates) {
/* 134 */         if (processedEntities.contains(potentialLeashed.getUniqueId()))
/*     */           continue; 
/* 136 */         Entity potentialHolder = getLeashHolder(potentialLeashed);
/* 137 */         if (potentialHolder != null && potentialHolder.equals(currentEntity)) {
/* 138 */           toProcess.add(potentialLeashed);
/* 139 */           CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Found entity " + String.valueOf(potentialLeashed.getType()) + " leashed to " + String.valueOf(currentEntity.getType()) + ", adding to queue.");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 144 */     CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Finished collection. Found " + finalEntityData.size() + " total entities.");
/* 145 */     return new CollectionResult(finalEntityData, entityReferences);
/*     */   }
/*     */   
/*     */   private static void preMarkNonPlayerPassengers(Collection<Entity> entities, Set<UUID> processedEntities) {
/*     */     for (Entity nearby : entities) {
/*     */       List<Entity> passengers = nearby.getPassengers();
/*     */       for (Entity passenger : passengers) {
/*     */         if (!(passenger instanceof Player)) {
/*     */           processedEntities.add(passenger.getUniqueId());
/*     */           CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Pre-marked passenger " + String.valueOf(passenger.getType()) + " (UUID: " + String.valueOf(passenger.getUniqueId()) + ") of " + String.valueOf(nearby.getType()) + " as processed to prevent duplication");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static EntityData.EntityRelation determineRelation(Entity entity, Player player) {
/* 149 */     if (player.getVehicle() != null && player.getVehicle().equals(entity)) {
/* 150 */       return EntityData.EntityRelation.VEHICLE;
/*     */     }
/* 152 */     if (getLeashHolder(entity) != null) {
/* 153 */       return EntityData.EntityRelation.LEASHED;
/*     */     }
/*     */ 
/*     */     
/* 157 */     return EntityData.EntityRelation.NEARBY;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean shouldCollectNearbyEntity(Entity entity, Player player, ConfigManager config) {
/* 162 */     if (entity.equals(player) || (player.getVehicle() != null && entity.equals(player.getVehicle()))) {
/* 163 */       return false;
/*     */     }
/* 165 */     if (entity instanceof Player) {
/* 166 */       return false;
/*     */     }
/*     */     
/* 169 */     boolean isVehicle = isValidVehicle(entity);
/* 170 */     boolean isMob = entity instanceof LivingEntity;
/*     */     
/* 172 */     if (isVehicle)
/* 173 */       return config.shouldTeleportEntity(entity.getType().name(), false); 
/* 174 */     if (isMob) {
/*     */       boolean isLeashed = getLeashHolder(entity) != null;
/*     */       if (isLeashed && !config.isTeleportLeashedMobs()) {
/*     */         return false;
/*     */       }
/*     */       if (config.isMobsMustBeLeashed() && !isLeashed) {
/*     */         return false;
/*     */       }
/*     */       String entityType = entity.getType().name();
/*     */       if (!isLeashed
/*     */           && !config.isTeleportNearbyVillagers()
/*     */           && (entityType.equals("VILLAGER") || entityType.equals("ALLAY"))) {
/*     */         return false;
/*     */       }
/* 175 */       return config.shouldTeleportEntity(entityType, true);
/*     */     }
/* 177 */     return false;
/*     */   }
/*     */   
/*     */   private static Entity getLeashHolder(Entity entity) {
/*     */     try {
/*     */       if (PAPER_LEASHABLE_CLASS != null && PAPER_GET_LEASH_HOLDER_METHOD != null && PAPER_LEASHABLE_CLASS.isInstance(entity)) {
/*     */         Object leashable = PAPER_LEASHABLE_CLASS.cast(entity);
/*     */         return (Entity)PAPER_GET_LEASH_HOLDER_METHOD.invoke(leashable, new Object[0]);
/*     */       }  if (entity instanceof LivingEntity && ((LivingEntity)entity).isLeashed()) {
/*     */         return ((LivingEntity)entity).getLeashHolder();
/*     */       }
/*     */     } catch (Exception e) {
/*     */       if (entity instanceof LivingEntity && ((LivingEntity)entity).isLeashed()) {
/*     */         return ((LivingEntity)entity).getLeashHolder();
/*     */       }
/*     */     } 
/*     */     return null;
/*     */   }
/*     */   
/*     */   private static Class<?> resolvePaperLeashableClass() {
/*     */     try {
/*     */       return Class.forName("io.papermc.paper.entity.Leashable");
/*     */     } catch (ClassNotFoundException exception) {
/*     */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Method resolvePaperLeashableMethod(String name, Class<?>... parameterTypes) {
/*     */     if (PAPER_LEASHABLE_CLASS == null) {
/*     */       return null;
/*     */     }
/*     */     try {
/*     */       return PAPER_LEASHABLE_CLASS.getMethod(name, parameterTypes);
/*     */     } catch (ReflectiveOperationException exception) {
/*     */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean isValidVehicle(Entity entity) {
/* 199 */     if (entity == null) {
/* 200 */       return false;
/*     */     }
/* 202 */     if (entity instanceof org.bukkit.entity.Vehicle) {
/* 203 */       return true;
/*     */     }
/* 205 */     String typeName = entity.getType().name();
/* 206 */     if (typeName.equals("HORSE") || typeName.equals("DONKEY") || typeName.equals("MULE") || typeName.equals("LLAMA") || typeName.equals("TRADER_LLAMA") || typeName.equals("CAMEL") || typeName.equals("SKELETON_HORSE") || typeName.equals("ZOMBIE_HORSE") || typeName.equals("PIG") || typeName.equals("STRIDER")) {
/* 207 */       return true;
/*     */     }
/* 209 */     for (Entity passenger : entity.getPassengers()) {
/* 210 */       if (passenger instanceof Player) {
/* 211 */         return true;
/*     */       }
/*     */     } 
/* 214 */     return false;
/*     */   }
/*     */   public static Entity spawnEntity(World world, EntityData data, Location playerLocation) {
/*     */     EntityType entityType;
/* 203 */     Location spawnLoc = playerLocation.clone().add(data.offsetX, data.offsetY, data.offsetZ);
/* 204 */     spawnLoc.setYaw(data.yaw);
/* 205 */     spawnLoc.setPitch(data.pitch);
/*     */ 
/*     */     
/*     */     try {
/* 209 */       entityType = EntityType.valueOf(data.entityType);
/* 210 */     } catch (IllegalArgumentException e) {
/* 211 */       return null;
/*     */     } 
/*     */     
/* 214 */     if (entityType == EntityType.PLAYER) {
/* 215 */       return null;
/*     */     }
/*     */     
/* 218 */     Entity entity = world.spawnEntity(spawnLoc, entityType);
/*     */     restorePortableState(entity, data);
/*     */ 
/*     */     
/* 240 */     List<Entity> passengersToMount = new ArrayList<>();
/* 241 */     for (EntityData passengerData : data.passengers) {
/* 242 */       Entity passenger = spawnEntity(world, passengerData, playerLocation);
/* 243 */       if (passenger != null) {
/* 244 */         passengersToMount.add(passenger);
/* 245 */         CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Spawned passenger " + String.valueOf(passenger.getType()) + " for delayed mounting on " + 
/* 246 */             String.valueOf(entity.getType()));
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 252 */     if (!passengersToMount.isEmpty()) {
/* 253 */       Entity finalEntity = entity;
/* 254 */       com.alphastudio.CrossBorderCore.util.SchedulerAdapter.runEntityLater((Plugin)CrossBorderCore.getInstance(), finalEntity, () -> {
/* 255 */             for (Entity passenger : passengersToMount) {
/* 256 */               if (passenger.isValid() && finalEntity.isValid()) {
/* 257 */                 finalEntity.addPassenger(passenger);
/* 258 */                 CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Mounted passenger " + passenger.getType() + " (UUID: " + passenger.getUniqueId() + ") on " + finalEntity.getType() + " (UUID: " + finalEntity.getUniqueId() + ") with delayed mounting");
/*     */                 continue;
/*     */               } 
/* 261 */               CrossBorderCore.getInstance().getLogger().warning("[CrossServerTP] Could not mount passenger " + passenger.getType() + " - entity invalid (passenger valid: " + passenger.isValid() + ", vehicle valid: " + finalEntity.isValid() + ")");
/*     */             } 
/*     */           }, 10L);
/*     */     } 
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
/* 270 */     return entity;
/*     */   }
/*     */
/*     */   private static void restorePortableState(Entity entity, EntityData data) {
/*     */     entity.setCustomName(data.customName);
/*     */     if (data.customNameVisible != null) entity.setCustomNameVisible(data.customNameVisible);
/*     */     if (data.persistent != null) entity.setPersistent(data.persistent);
/*     */     if (data.invulnerable != null) entity.setInvulnerable(data.invulnerable);
/*     */     if (data.silent != null) entity.setSilent(data.silent);
/*     */     if (data.glowing != null) entity.setGlowing(data.glowing);
/*     */     if (data.gravity != null) entity.setGravity(data.gravity);
/*     */     if (data.fireTicks != null) entity.setFireTicks(data.fireTicks);
/*     */
/*     */     if (entity instanceof LivingEntity living && data.health != null) {
/*     */       living.setHealth(Math.max(0.0D, Math.min(data.health, living.getMaxHealth())));
/*     */     }
/*     */
/*     */     if (entity instanceof Ageable ageable) {
/*     */       if (data.age != null) ageable.setAge(data.age);
/*     */       if (data.ageLock != null) ageable.setAgeLock(data.ageLock);
/*     */     }
/*     */
/*     */     if (entity instanceof InventoryHolder holder && data.inventoryContents != null) {
/*     */       holder.getInventory().setContents(data.inventoryContents);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeEntitiesDirectly(List<Entity> entityReferences) {
/* 277 */     int removedCount = 0;
/* 278 */     int skippedCount = 0;
/*     */     
/* 280 */     for (Entity entity : entityReferences) {
/* 281 */       if (entity == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 285 */       if (entity instanceof Player) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 290 */       if (!entity.isValid() || entity.isDead()) {
/* 291 */         skippedCount++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 296 */       if (entity instanceof InventoryHolder) {
/* 297 */         ((InventoryHolder)entity).getInventory().clear();
/*     */       }
/*     */ 
/*     */       
/* 301 */       if (!entity.getPassengers().isEmpty()) {
/* 302 */         List<Entity> passengers = new ArrayList<>(entity.getPassengers());
/* 303 */         for (Entity passenger : passengers) {
/* 304 */           entity.removePassenger(passenger);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       clearLeashHolder(entity);
/*     */ 
/*     */       
/* 329 */       entity.remove();
/* 330 */       removedCount++;
/*     */     } 
/*     */     
/* 333 */     if (removedCount > 0 || skippedCount > 0) {
/* 334 */       CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Removed " + removedCount + " entities from source world" + (
/* 335 */           (skippedCount > 0) ? (" (" + skippedCount + " already removed)") : ""));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void removeEntities(List<EntityData> entityDataList, World world) {
/* 343 */     int removedCount = 0;
/* 344 */     int notFoundCount = 0;
/*     */ 
/*     */     
/* 347 */     for (EntityData data : entityDataList) {
/* 348 */       Entity entity = null;
/*     */       
/* 350 */       for (Entity worldEntity : world.getEntities()) {
/* 351 */         if (worldEntity.getUniqueId().equals(data.entityUUID)) {
/* 352 */           entity = worldEntity;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 357 */       if (entity == null) {
/* 358 */         notFoundCount++;
/*     */         
/*     */         continue;
/*     */       } 
/* 362 */       if (entity instanceof Player) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 367 */       if (!entity.isValid() || entity.isDead()) {
/* 368 */         notFoundCount++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 373 */       if (entity instanceof InventoryHolder) {
/* 374 */         ((InventoryHolder)entity).getInventory().clear();
/*     */       }
/*     */ 
/*     */       
/* 378 */       if (!entity.getPassengers().isEmpty()) {
/* 379 */         List<Entity> passengers = new ArrayList<>(entity.getPassengers());
/* 380 */         for (Entity passenger : passengers) {
/* 381 */           entity.removePassenger(passenger);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/*     */       clearLeashHolder(entity);
/*     */ 
/*     */       
/* 406 */       entity.remove();
/* 407 */       removedCount++;
/*     */     } 
/*     */     
/* 410 */     if (removedCount > 0 || notFoundCount > 0)
/* 411 */       CrossBorderCore.getInstance().getLogger().fine("[CrossServerTP] Removed " + removedCount + " entities" + (
/* 412 */           (notFoundCount > 0) ? (", " + notFoundCount + " not found") : "")); 
/*     */   }
/*     */   
/*     */   private static void clearLeashHolder(Entity entity) {
/*     */     try {
/*     */       if (PAPER_LEASHABLE_CLASS != null
/*     */           && PAPER_IS_LEASHED_METHOD != null
/*     */           && PAPER_SET_LEASH_HOLDER_METHOD != null
/*     */           && PAPER_LEASHABLE_CLASS.isInstance(entity)) {
/*     */         Object leashable = PAPER_LEASHABLE_CLASS.cast(entity);
/*     */         boolean isLeashed = ((Boolean)PAPER_IS_LEASHED_METHOD.invoke(leashable, new Object[0])).booleanValue();
/*     */         if (isLeashed) {
/*     */           PAPER_SET_LEASH_HOLDER_METHOD.invoke(leashable, new Object[] { null });
/*     */         }
/*     */         return;
/*     */       }
/*     */     } catch (Exception ignored) {
/*     */       // Fall back to the Bukkit LivingEntity API below.
/*     */     }
/*     */     if (entity instanceof LivingEntity && ((LivingEntity)entity).isLeashed()) {
/*     */       ((LivingEntity)entity).setLeashHolder(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\storage\EntityManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */
