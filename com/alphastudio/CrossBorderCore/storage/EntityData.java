package com.alphastudio.CrossBorderCore.storage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class EntityData {
  public enum EntityRelation { VEHICLE, LEASHED, NEARBY }

  public UUID entityUUID;
  public String entityType;
  public double offsetX;
  public double offsetY;
  public double offsetZ;
  public float yaw;
  public float pitch;
  public EntityRelation relation;
  public UUID leashHolderUUID;
  public String customName;
  public Boolean customNameVisible;
  public Boolean persistent;
  public Boolean invulnerable;
  public Boolean silent;
  public Boolean glowing;
  public Boolean gravity;
  public Integer fireTicks;
  public Double health;
  public Integer age;
  public Boolean ageLock;
  public ItemStack[] inventoryContents;
  public List<EntityData> passengers = new ArrayList<>();

  public static EntityData fromEntity(Entity entity, EntityRelation relation, Location origin) {
    EntityData data = new EntityData();
    data.entityUUID = entity.getUniqueId();
    data.entityType = entity.getType().name();
    data.offsetX = entity.getLocation().getX() - origin.getX();
    data.offsetY = entity.getLocation().getY() - origin.getY();
    data.offsetZ = entity.getLocation().getZ() - origin.getZ();
    data.yaw = entity.getLocation().getYaw();
    data.pitch = entity.getLocation().getPitch();
    data.relation = relation;
    data.customName = entity.getCustomName();
    data.customNameVisible = entity.isCustomNameVisible();
    data.persistent = entity.isPersistent();
    data.invulnerable = entity.isInvulnerable();
    data.silent = entity.isSilent();
    data.glowing = entity.isGlowing();
    data.gravity = entity.hasGravity();
    data.fireTicks = entity.getFireTicks();

    if (entity instanceof LivingEntity living && living.isLeashed() && living.getLeashHolder() != null) {
      data.leashHolderUUID = living.getLeashHolder().getUniqueId();
    }

    if (entity instanceof LivingEntity living) {
      data.health = living.getHealth();
    }

    if (entity instanceof Ageable ageable) {
      data.age = ageable.getAge();
      data.ageLock = ageable.getAgeLock();
    }

    if (entity instanceof InventoryHolder holder) {
      data.inventoryContents = Arrays.stream(holder.getInventory().getContents())
          .map(item -> item == null ? null : item.clone())
          .toArray(ItemStack[]::new);
    }

    for (Entity passenger : entity.getPassengers()) {
      if (passenger.getType() == EntityType.PLAYER) continue;
      data.passengers.add(fromEntity(passenger, EntityRelation.NEARBY, origin));
    }

    return data;
  }
}
