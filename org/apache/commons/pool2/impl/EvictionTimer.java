/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.time.Duration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ScheduledFuture;
/*     */ import java.util.concurrent.ScheduledThreadPoolExecutor;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EvictionTimer
/*     */ {
/*     */   private static ScheduledThreadPoolExecutor executor;
/*     */   
/*     */   private static class EvictorThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/*     */     private EvictorThreadFactory() {}
/*     */     
/*     */     public Thread newThread(Runnable runnable) {
/*  58 */       Thread thread = new Thread(null, runnable, "commons-pool-evictor");
/*  59 */       thread.setDaemon(true);
/*  60 */       AccessController.doPrivileged(() -> {
/*     */             thread.setContextClassLoader(EvictorThreadFactory.class.getClassLoader());
/*     */             
/*     */             return null;
/*     */           });
/*  65 */       return thread;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static class Reaper
/*     */     implements Runnable
/*     */   {
/*     */     private Reaper() {}
/*     */     
/*     */     public void run() {
/*  76 */       synchronized (EvictionTimer.class) {
/*  77 */         for (Map.Entry<WeakReference<BaseGenericObjectPool<?>.Evictor>, EvictionTimer.WeakRunner<BaseGenericObjectPool<?>.Evictor>> entry : (Iterable<Map.Entry<WeakReference<BaseGenericObjectPool<?>.Evictor>, EvictionTimer.WeakRunner<BaseGenericObjectPool<?>.Evictor>>>)EvictionTimer.TASK_MAP
/*  78 */           .entrySet()) {
/*  79 */           if (((WeakReference)entry.getKey()).get() == null) {
/*  80 */             EvictionTimer.executor.remove(entry.getValue());
/*  81 */             EvictionTimer.TASK_MAP.remove(entry.getKey());
/*     */           } 
/*     */         } 
/*  84 */         if (EvictionTimer.TASK_MAP.isEmpty() && EvictionTimer.executor != null) {
/*  85 */           EvictionTimer.executor.shutdown();
/*  86 */           EvictionTimer.executor.setCorePoolSize(0);
/*  87 */           EvictionTimer.executor = null;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WeakRunner<R extends Runnable>
/*     */     implements Runnable
/*     */   {
/*     */     private final WeakReference<R> ref;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private WeakRunner(WeakReference<R> ref) {
/* 108 */       this.ref = ref;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 113 */       Runnable task = (Runnable)this.ref.get();
/* 114 */       if (task != null) {
/* 115 */         task.run();
/*     */       } else {
/* 117 */         EvictionTimer.executor.remove(this);
/* 118 */         EvictionTimer.TASK_MAP.remove(this.ref);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   private static final HashMap<WeakReference<BaseGenericObjectPool<?>.Evictor>, WeakRunner<BaseGenericObjectPool<?>.Evictor>> TASK_MAP = new HashMap<>();
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
/*     */   static synchronized void cancel(BaseGenericObjectPool<?>.Evictor evictor, Duration timeout, boolean restarting) {
/* 143 */     if (evictor != null) {
/* 144 */       evictor.cancel();
/* 145 */       remove(evictor);
/*     */     } 
/* 147 */     if (!restarting && executor != null && TASK_MAP.isEmpty()) {
/* 148 */       executor.shutdown();
/*     */       try {
/* 150 */         executor.awaitTermination(timeout.toMillis(), TimeUnit.MILLISECONDS);
/* 151 */       } catch (InterruptedException interruptedException) {}
/*     */ 
/*     */ 
/*     */       
/* 155 */       executor.setCorePoolSize(0);
/* 156 */       executor = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ScheduledThreadPoolExecutor getExecutor() {
/* 166 */     return executor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static synchronized int getNumTasks() {
/* 173 */     return TASK_MAP.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static HashMap<WeakReference<BaseGenericObjectPool<?>.Evictor>, WeakRunner<BaseGenericObjectPool<?>.Evictor>> getTaskMap() {
/* 182 */     return TASK_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void remove(BaseGenericObjectPool<?>.Evictor evictor) {
/* 192 */     for (Map.Entry<WeakReference<BaseGenericObjectPool<?>.Evictor>, WeakRunner<BaseGenericObjectPool<?>.Evictor>> entry : TASK_MAP.entrySet()) {
/* 193 */       if (((WeakReference<BaseGenericObjectPool<?>.Evictor>)entry.getKey()).get() == evictor) {
/* 194 */         executor.remove(entry.getValue());
/* 195 */         TASK_MAP.remove(entry.getKey());
/*     */         break;
/*     */       } 
/*     */     } 
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
/*     */ 
/*     */   
/*     */   static synchronized void schedule(BaseGenericObjectPool<?>.Evictor task, Duration delay, Duration period) {
/* 214 */     if (null == executor) {
/* 215 */       executor = new ScheduledThreadPoolExecutor(1, new EvictorThreadFactory());
/* 216 */       executor.setRemoveOnCancelPolicy(true);
/* 217 */       executor.scheduleAtFixedRate(new Reaper(), delay.toMillis(), period.toMillis(), TimeUnit.MILLISECONDS);
/*     */     } 
/* 219 */     WeakReference<BaseGenericObjectPool<?>.Evictor> ref = new WeakReference<>(task);
/* 220 */     WeakRunner<BaseGenericObjectPool<?>.Evictor> runner = new WeakRunner<>(ref);
/* 221 */     ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(runner, delay.toMillis(), period
/* 222 */         .toMillis(), TimeUnit.MILLISECONDS);
/* 223 */     task.setScheduledFuture(scheduledFuture);
/* 224 */     TASK_MAP.put(ref, runner);
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
/*     */   public String toString() {
/* 237 */     StringBuilder builder = new StringBuilder();
/* 238 */     builder.append("EvictionTimer []");
/* 239 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\EvictionTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */