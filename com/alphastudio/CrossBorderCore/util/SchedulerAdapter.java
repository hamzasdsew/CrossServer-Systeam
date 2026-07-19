package com.alphastudio.CrossBorderCore.util;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.EntityScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public final class SchedulerAdapter {
  private static final long MIN_DELAY_TICKS = 1L;
  private static final long TICK_MILLIS = 50L;
  private static final boolean REGION_THREADED = detectRegionThreadedServer();
  private static final TaskHandle EMPTY_HANDLE = new TaskHandle() {
    @Override
    public void cancel() {}

    @Override
    public boolean isPresent() {
      return false;
    }
  };

  private SchedulerAdapter() {}

  public static boolean isFolia() {
    return REGION_THREADED;
  }

  public static TaskHandle runEntity(Plugin plugin, Entity entity, Runnable task) {
    if (entity == null) {
      return EMPTY_HANDLE;
    }
    if (!REGION_THREADED) {
      return wrap(Bukkit.getScheduler().runTask(plugin, runnable(plugin, "Bukkit entity task", task)));
    }
    if (!entity.isValid()) {
      plugin.getLogger().fine("Skipping entity task because the target entity is no longer valid.");
      return EMPTY_HANDLE;
    }

    try {
      EntityScheduler scheduler = entity.getScheduler();
      ScheduledTask scheduledTask =
          scheduler.run(
              plugin,
              ignored -> runnable(plugin, "EntityScheduler task", task).run(),
              () ->
                  plugin
                      .getLogger()
                      .fine(
                          "Skipped entity task because "
                              + entity.getType()
                              + " "
                              + entity.getUniqueId()
                              + " retired before execution."));
      return wrap(scheduledTask);
    } catch (Throwable throwable) {
      logFailure(plugin, "EntityScheduler run failed", throwable);
      return EMPTY_HANDLE;
    }
  }

  public static TaskHandle runEntityLater(Plugin plugin, Entity entity, Runnable task, long delayTicks) {
    if (entity == null) {
      return EMPTY_HANDLE;
    }
    long safeDelay = clampDelay(plugin, "EntityScheduler delay", delayTicks);
    if (!REGION_THREADED) {
      return wrap(
          Bukkit.getScheduler().runTaskLater(plugin, runnable(plugin, "Bukkit delayed entity task", task), safeDelay));
    }
    if (!entity.isValid()) {
      plugin.getLogger().fine("Skipping delayed entity task because the target entity is no longer valid.");
      return EMPTY_HANDLE;
    }

    try {
      ScheduledTask scheduledTask =
          entity
              .getScheduler()
              .runDelayed(
                  plugin,
                  ignored -> runnable(plugin, "EntityScheduler delayed task", task).run(),
                  () ->
                      plugin
                          .getLogger()
                          .fine(
                              "Skipped delayed entity task because "
                                  + entity.getType()
                                  + " "
                                  + entity.getUniqueId()
                                  + " retired before execution."),
                  safeDelay);
      return wrap(scheduledTask);
    } catch (Throwable throwable) {
      logFailure(plugin, "EntityScheduler runDelayed failed", throwable);
      return EMPTY_HANDLE;
    }
  }

  public static TaskHandle runEntityRepeating(
      Plugin plugin, Entity entity, Runnable task, long initialDelayTicks, long periodTicks) {
    if (entity == null) {
      return EMPTY_HANDLE;
    }
    long safeInitialDelay = clampDelay(plugin, "EntityScheduler initial delay", initialDelayTicks);
    long safePeriod = clampPeriod(plugin, "EntityScheduler period", periodTicks);
    if (!REGION_THREADED) {
      return wrap(
          Bukkit.getScheduler()
              .runTaskTimer(
                  plugin, runnable(plugin, "Bukkit repeating entity task", task), safeInitialDelay, safePeriod));
    }
    if (!entity.isValid()) {
      plugin.getLogger().fine("Skipping repeating entity task because the target entity is no longer valid.");
      return EMPTY_HANDLE;
    }

    try {
      ScheduledTask scheduledTask =
          entity
              .getScheduler()
              .runAtFixedRate(
                  plugin,
                  ignored -> runnable(plugin, "EntityScheduler repeating task", task).run(),
                  () ->
                      plugin
                          .getLogger()
                          .fine(
                              "Stopped repeating entity task because "
                                  + entity.getType()
                                  + " "
                                  + entity.getUniqueId()
                                  + " retired."),
                  safeInitialDelay,
                  safePeriod);
      return wrap(scheduledTask);
    } catch (Throwable throwable) {
      logFailure(plugin, "EntityScheduler runAtFixedRate failed", throwable);
      return EMPTY_HANDLE;
    }
  }

  public static TaskHandle runRegion(Plugin plugin, World world, int blockX, int blockZ, Runnable task) {
    if (world == null) {
      return EMPTY_HANDLE;
    }
    if (!REGION_THREADED) {
      return wrap(Bukkit.getScheduler().runTask(plugin, runnable(plugin, "Bukkit region task", task)));
    }

    try {
      RegionScheduler scheduler = Bukkit.getServer().getRegionScheduler();
      ScheduledTask scheduledTask =
          scheduler.run(
              plugin,
              world,
              toChunk(blockX),
              toChunk(blockZ),
              ignored -> runnable(plugin, "RegionScheduler task", task).run());
      return wrap(scheduledTask);
    } catch (Throwable throwable) {
      logFailure(plugin, "RegionScheduler run failed", throwable);
      return EMPTY_HANDLE;
    }
  }

  public static TaskHandle runRegionLater(
      Plugin plugin, World world, int blockX, int blockZ, Runnable task, long delayTicks) {
    if (world == null) {
      return EMPTY_HANDLE;
    }
    long safeDelay = clampDelay(plugin, "RegionScheduler delay", delayTicks);
    if (!REGION_THREADED) {
      return wrap(Bukkit.getScheduler().runTaskLater(plugin, runnable(plugin, "Bukkit delayed region task", task), safeDelay));
    }

    try {
      ScheduledTask scheduledTask =
          Bukkit.getServer()
              .getRegionScheduler()
              .runDelayed(
                  plugin,
                  world,
                  toChunk(blockX),
                  toChunk(blockZ),
                  ignored -> runnable(plugin, "RegionScheduler delayed task", task).run(),
                  safeDelay);
      return wrap(scheduledTask);
    } catch (Throwable throwable) {
      logFailure(plugin, "RegionScheduler runDelayed failed", throwable);
      return EMPTY_HANDLE;
    }
  }

  public static TaskHandle runGlobalRepeating(
      Plugin plugin, Runnable task, long initialDelayTicks, long periodTicks) {
    long safeInitialDelay = clampDelay(plugin, "GlobalRegionScheduler initial delay", initialDelayTicks);
    long safePeriod = clampPeriod(plugin, "GlobalRegionScheduler period", periodTicks);
    if (!REGION_THREADED) {
      return wrap(
          Bukkit.getScheduler()
              .runTaskTimer(plugin, runnable(plugin, "Bukkit repeating global task", task), safeInitialDelay, safePeriod));
    }

    try {
      GlobalRegionScheduler scheduler = Bukkit.getServer().getGlobalRegionScheduler();
      ScheduledTask scheduledTask =
          scheduler.runAtFixedRate(
              plugin,
              ignored -> runnable(plugin, "GlobalRegionScheduler repeating task", task).run(),
              safeInitialDelay,
              safePeriod);
      return wrap(scheduledTask);
    } catch (Throwable throwable) {
      logFailure(plugin, "GlobalRegionScheduler runAtFixedRate failed", throwable);
      return EMPTY_HANDLE;
    }
  }

  public static TaskHandle runAsyncRepeating(
      Plugin plugin, Runnable task, long initialDelayTicks, long periodTicks) {
    long safeInitialDelay = clampDelay(plugin, "AsyncScheduler initial delay", initialDelayTicks);
    long safePeriod = clampPeriod(plugin, "AsyncScheduler period", periodTicks);
    if (!REGION_THREADED) {
      return wrap(
          Bukkit.getScheduler()
              .runTaskTimerAsynchronously(
                  plugin, runnable(plugin, "Bukkit async repeating task", task), safeInitialDelay, safePeriod));
    }

    try {
      AsyncScheduler scheduler = Bukkit.getServer().getAsyncScheduler();
      ScheduledTask scheduledTask =
          scheduler.runAtFixedRate(
              plugin,
              ignored -> runnable(plugin, "AsyncScheduler repeating task", task).run(),
              ticksToMillis(safeInitialDelay),
              ticksToMillis(safePeriod),
              TimeUnit.MILLISECONDS);
      return wrap(scheduledTask);
    } catch (Throwable throwable) {
      logFailure(plugin, "AsyncScheduler runAtFixedRate failed", throwable);
      return EMPTY_HANDLE;
    }
  }

  private static Runnable runnable(Plugin plugin, String context, Runnable task) {
    return () -> {
      try {
        task.run();
      } catch (Throwable throwable) {
        plugin.getLogger().log(Level.SEVERE, context + " threw an exception", unwrap(throwable));
      }
    };
  }

  private static long clampDelay(Plugin plugin, String label, long delayTicks) {
    if (delayTicks >= MIN_DELAY_TICKS) {
      return delayTicks;
    }
    plugin
        .getLogger()
        .warning(label + " was " + delayTicks + " tick(s); clamping to 1 tick for Folia/Canvas safety.");
    return MIN_DELAY_TICKS;
  }

  private static long clampPeriod(Plugin plugin, String label, long periodTicks) {
    if (periodTicks >= MIN_DELAY_TICKS) {
      return periodTicks;
    }
    plugin
        .getLogger()
        .warning(label + " was " + periodTicks + " tick(s); clamping to 1 tick for Folia/Canvas safety.");
    return MIN_DELAY_TICKS;
  }

  private static long ticksToMillis(long ticks) {
    return Math.max(MIN_DELAY_TICKS, ticks) * TICK_MILLIS;
  }

  private static int toChunk(int blockCoordinate) {
    return Math.floorDiv(blockCoordinate, 16);
  }

  private static TaskHandle wrap(ScheduledTask task) {
    if (task == null) {
      return EMPTY_HANDLE;
    }
    return new TaskHandle() {
      @Override
      public void cancel() {
        task.cancel();
      }

      @Override
      public boolean isPresent() {
        return true;
      }
    };
  }

  private static TaskHandle wrap(BukkitTask task) {
    if (task == null) {
      return EMPTY_HANDLE;
    }
    return new TaskHandle() {
      @Override
      public void cancel() {
        task.cancel();
      }

      @Override
      public boolean isPresent() {
        return true;
      }
    };
  }

  private static void logFailure(Plugin plugin, String context, Throwable throwable) {
    Throwable cause = unwrap(throwable);
    plugin.getLogger().warning(context + ": " + cause.getClass().getSimpleName() + ": " + cause.getMessage());
    plugin.getLogger().log(Level.FINE, context + " full stack trace", cause);
  }

  private static Throwable unwrap(Throwable throwable) {
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

  private static boolean detectRegionThreadedServer() {
    try {
      String serverName = Bukkit.getServer().getName().toLowerCase(Locale.ROOT);
      if (serverName.contains("folia") || serverName.contains("canvas")) {
        return true;
      }
      String version = Bukkit.getVersion().toLowerCase(Locale.ROOT);
      if (version.contains("folia") || version.contains("canvas")) {
        return true;
      }
      Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
      return true;
    } catch (Throwable ignored) {
      return false;
    }
  }

  public interface TaskHandle {
    void cancel();

    boolean isPresent();
  }
}
