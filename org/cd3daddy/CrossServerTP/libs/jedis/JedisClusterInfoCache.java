/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisClusterOperationException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JedisClusterInfoCache
/*     */ {
/*  32 */   private static final Logger logger = LoggerFactory.getLogger(JedisClusterInfoCache.class);
/*     */   
/*  34 */   private final Map<String, ConnectionPool> nodes = new HashMap<>();
/*  35 */   private final ConnectionPool[] slots = new ConnectionPool[16384];
/*  36 */   private final HostAndPort[] slotNodes = new HostAndPort[16384];
/*     */   
/*  38 */   private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
/*  39 */   private final Lock r = this.rwl.readLock();
/*  40 */   private final Lock w = this.rwl.writeLock();
/*  41 */   private final Lock rediscoverLock = new ReentrantLock();
/*     */ 
/*     */   
/*     */   private final GenericObjectPoolConfig<Connection> poolConfig;
/*     */   
/*     */   private final JedisClientConfig clientConfig;
/*     */   
/*     */   private final Set<HostAndPort> startNodes;
/*     */   
/*     */   private static final int MASTER_NODE_INDEX = 2;
/*     */   
/*  52 */   private ScheduledExecutorService topologyRefreshExecutor = null;
/*     */   
/*     */   class TopologyRefreshTask
/*     */     implements Runnable {
/*     */     public void run() {
/*  57 */       JedisClusterInfoCache.logger.debug("Cluster topology refresh run, old nodes: {}", JedisClusterInfoCache.this.nodes.keySet());
/*  58 */       JedisClusterInfoCache.this.renewClusterSlots(null);
/*  59 */       JedisClusterInfoCache.logger.debug("Cluster topology refresh run, new nodes: {}", JedisClusterInfoCache.this.nodes.keySet());
/*     */     }
/*     */   }
/*     */   
/*     */   public JedisClusterInfoCache(JedisClientConfig clientConfig, Set<HostAndPort> startNodes) {
/*  64 */     this(clientConfig, null, startNodes);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisClusterInfoCache(JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Set<HostAndPort> startNodes) {
/*  69 */     this(clientConfig, poolConfig, startNodes, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisClusterInfoCache(JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Set<HostAndPort> startNodes, Duration topologyRefreshPeriod) {
/*  75 */     this.poolConfig = poolConfig;
/*  76 */     this.clientConfig = clientConfig;
/*  77 */     this.startNodes = startNodes;
/*  78 */     if (topologyRefreshPeriod != null) {
/*  79 */       logger.info("Cluster topology refresh start, period: {}, startNodes: {}", topologyRefreshPeriod, startNodes);
/*  80 */       this.topologyRefreshExecutor = Executors.newSingleThreadScheduledExecutor();
/*  81 */       this.topologyRefreshExecutor.scheduleWithFixedDelay(new TopologyRefreshTask(), topologyRefreshPeriod.toMillis(), topologyRefreshPeriod
/*  82 */           .toMillis(), TimeUnit.MILLISECONDS);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean checkClusterSlotSequence(List<Object> slotsInfo) {
/*  92 */     List<Integer> slots = new ArrayList<>();
/*  93 */     for (Object slotInfoObj : slotsInfo) {
/*  94 */       List<Object> slotInfo = (List<Object>)slotInfoObj;
/*  95 */       slots.addAll(getAssignedSlotArray(slotInfo));
/*     */     } 
/*  97 */     Collections.sort(slots);
/*  98 */     if (slots.size() != 16384) {
/*  99 */       return false;
/*     */     }
/* 101 */     for (int i = 0; i < 16384; i++) {
/* 102 */       if (i != ((Integer)slots.get(i)).intValue()) {
/* 103 */         return false;
/*     */       }
/*     */     } 
/* 106 */     return true;
/*     */   }
/*     */   
/*     */   public void discoverClusterNodesAndSlots(Connection jedis) {
/* 110 */     List<Object> slotsInfo = executeClusterSlots(jedis);
/* 111 */     if (System.getProperty("jedis.cluster.initNoError") == null) {
/* 112 */       if (slotsInfo.isEmpty()) {
/* 113 */         throw new JedisClusterOperationException("Cluster slots list is empty.");
/*     */       }
/* 115 */       if (!checkClusterSlotSequence(slotsInfo)) {
/* 116 */         throw new JedisClusterOperationException("Cluster slots have holes.");
/*     */       }
/*     */     } 
/* 119 */     this.w.lock();
/*     */     try {
/* 121 */       reset();
/* 122 */       for (Object slotInfoObj : slotsInfo) {
/* 123 */         List<Object> slotInfo = (List<Object>)slotInfoObj;
/*     */         
/* 125 */         if (slotInfo.size() <= 2) {
/*     */           continue;
/*     */         }
/*     */         
/* 129 */         List<Integer> slotNums = getAssignedSlotArray(slotInfo);
/*     */ 
/*     */         
/* 132 */         int size = slotInfo.size();
/* 133 */         for (int i = 2; i < size; i++) {
/* 134 */           List<Object> hostInfos = (List<Object>)slotInfo.get(i);
/* 135 */           if (!hostInfos.isEmpty()) {
/*     */ 
/*     */ 
/*     */             
/* 139 */             HostAndPort targetNode = generateHostAndPort(hostInfos);
/* 140 */             setupNodeIfNotExist(targetNode);
/* 141 */             if (i == 2)
/* 142 */               assignSlotsToNode(slotNums, targetNode); 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 147 */       this.w.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renewClusterSlots(Connection jedis) {
/* 153 */     if (this.rediscoverLock.tryLock()) {
/*     */       
/*     */       try {
/* 156 */         if (jedis != null) {
/*     */           try {
/* 158 */             discoverClusterSlots(jedis);
/*     */             return;
/* 160 */           } catch (JedisException jedisException) {}
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 167 */         if (this.startNodes != null) {
/* 168 */           for (HostAndPort hostAndPort : this.startNodes) {
/* 169 */             try (Connection j = new Connection(hostAndPort, this.clientConfig)) {
/* 170 */               discoverClusterSlots(j);
/*     */               return;
/* 172 */             } catch (JedisException jedisException) {}
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 179 */         for (ConnectionPool jp : getShuffledNodesPool()) {
/* 180 */           try (Connection j = jp.getResource())
/*     */           
/* 182 */           { if (this.startNodes != null && this.startNodes.contains(j.getHostAndPort()))
/*     */             
/*     */             { 
/*     */ 
/*     */               
/* 187 */               if (j != null) { if (null != null) { try { j.close(); } catch (Throwable throwable) { null.addSuppressed(throwable); }  continue; }  j.close(); }  continue; }  discoverClusterSlots(j); return; } catch (JedisException jedisException) {}
/*     */         }
/*     */       
/*     */       }
/*     */       finally {
/*     */         
/* 193 */         this.rediscoverLock.unlock();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void discoverClusterSlots(Connection jedis) {
/* 199 */     List<Object> slotsInfo = executeClusterSlots(jedis);
/* 200 */     if (System.getProperty("jedis.cluster.initNoError") == null) {
/* 201 */       if (slotsInfo.isEmpty()) {
/* 202 */         throw new JedisClusterOperationException("Cluster slots list is empty.");
/*     */       }
/* 204 */       if (!checkClusterSlotSequence(slotsInfo)) {
/* 205 */         throw new JedisClusterOperationException("Cluster slots have holes.");
/*     */       }
/*     */     } 
/* 208 */     this.w.lock();
/*     */     try {
/* 210 */       Arrays.fill((Object[])this.slots, (Object)null);
/* 211 */       Arrays.fill((Object[])this.slotNodes, (Object)null);
/* 212 */       Set<String> hostAndPortKeys = new HashSet<>();
/*     */       
/* 214 */       for (Object slotInfoObj : slotsInfo) {
/* 215 */         List<Object> slotInfo = (List<Object>)slotInfoObj;
/*     */         
/* 217 */         if (slotInfo.size() <= 2) {
/*     */           continue;
/*     */         }
/*     */         
/* 221 */         List<Integer> slotNums = getAssignedSlotArray(slotInfo);
/*     */         
/* 223 */         int size = slotInfo.size();
/* 224 */         for (int i = 2; i < size; i++) {
/* 225 */           List<Object> hostInfos = (List<Object>)slotInfo.get(i);
/* 226 */           if (!hostInfos.isEmpty()) {
/*     */ 
/*     */ 
/*     */             
/* 230 */             HostAndPort targetNode = generateHostAndPort(hostInfos);
/* 231 */             hostAndPortKeys.add(getNodeKey(targetNode));
/* 232 */             setupNodeIfNotExist(targetNode);
/* 233 */             if (i == 2) {
/* 234 */               assignSlotsToNode(slotNums, targetNode);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 240 */       Iterator<Map.Entry<String, ConnectionPool>> entryIt = this.nodes.entrySet().iterator();
/* 241 */       while (entryIt.hasNext()) {
/* 242 */         Map.Entry<String, ConnectionPool> entry = entryIt.next();
/* 243 */         if (!hostAndPortKeys.contains(entry.getKey())) {
/* 244 */           ConnectionPool pool = entry.getValue();
/*     */           try {
/* 246 */             if (pool != null) {
/* 247 */               pool.destroy();
/*     */             }
/* 249 */           } catch (Exception exception) {}
/*     */ 
/*     */           
/* 252 */           entryIt.remove();
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 256 */       this.w.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private HostAndPort generateHostAndPort(List<Object> hostInfos) {
/* 261 */     String host = SafeEncoder.encode((byte[])hostInfos.get(0));
/* 262 */     int port = ((Long)hostInfos.get(1)).intValue();
/* 263 */     return new HostAndPort(host, port);
/*     */   }
/*     */   
/*     */   public ConnectionPool setupNodeIfNotExist(HostAndPort node) {
/* 267 */     this.w.lock();
/*     */     try {
/* 269 */       String nodeKey = getNodeKey(node);
/* 270 */       ConnectionPool existingPool = this.nodes.get(nodeKey);
/* 271 */       if (existingPool != null) return existingPool;
/*     */       
/* 273 */       ConnectionPool nodePool = (this.poolConfig == null) ? new ConnectionPool(node, this.clientConfig) : new ConnectionPool(node, this.clientConfig, this.poolConfig);
/*     */       
/* 275 */       this.nodes.put(nodeKey, nodePool);
/* 276 */       return nodePool;
/*     */     } finally {
/* 278 */       this.w.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void assignSlotToNode(int slot, HostAndPort targetNode) {
/* 283 */     this.w.lock();
/*     */     try {
/* 285 */       ConnectionPool targetPool = setupNodeIfNotExist(targetNode);
/* 286 */       this.slots[slot] = targetPool;
/* 287 */       this.slotNodes[slot] = targetNode;
/*     */     } finally {
/* 289 */       this.w.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void assignSlotsToNode(List<Integer> targetSlots, HostAndPort targetNode) {
/* 294 */     this.w.lock();
/*     */     try {
/* 296 */       ConnectionPool targetPool = setupNodeIfNotExist(targetNode);
/* 297 */       for (Integer slot : targetSlots) {
/* 298 */         this.slots[slot.intValue()] = targetPool;
/* 299 */         this.slotNodes[slot.intValue()] = targetNode;
/*     */       } 
/*     */     } finally {
/* 302 */       this.w.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public ConnectionPool getNode(String nodeKey) {
/* 307 */     this.r.lock();
/*     */     try {
/* 309 */       return this.nodes.get(nodeKey);
/*     */     } finally {
/* 311 */       this.r.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public ConnectionPool getNode(HostAndPort node) {
/* 316 */     return getNode(getNodeKey(node));
/*     */   }
/*     */   
/*     */   public ConnectionPool getSlotPool(int slot) {
/* 320 */     this.r.lock();
/*     */     try {
/* 322 */       return this.slots[slot];
/*     */     } finally {
/* 324 */       this.r.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public HostAndPort getSlotNode(int slot) {
/* 329 */     this.r.lock();
/*     */     try {
/* 331 */       return this.slotNodes[slot];
/*     */     } finally {
/* 333 */       this.r.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<String, ConnectionPool> getNodes() {
/* 338 */     this.r.lock();
/*     */     try {
/* 340 */       return new HashMap<>(this.nodes);
/*     */     } finally {
/* 342 */       this.r.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<ConnectionPool> getShuffledNodesPool() {
/* 347 */     this.r.lock();
/*     */     try {
/* 349 */       List<ConnectionPool> pools = new ArrayList<>(this.nodes.values());
/* 350 */       Collections.shuffle(pools);
/* 351 */       return pools;
/*     */     } finally {
/* 353 */       this.r.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reset() {
/* 361 */     this.w.lock();
/*     */     try {
/* 363 */       for (ConnectionPool pool : this.nodes.values()) {
/*     */         try {
/* 365 */           if (pool != null) {
/* 366 */             pool.destroy();
/*     */           }
/* 368 */         } catch (RuntimeException runtimeException) {}
/*     */       } 
/*     */ 
/*     */       
/* 372 */       this.nodes.clear();
/* 373 */       Arrays.fill((Object[])this.slots, (Object)null);
/* 374 */       Arrays.fill((Object[])this.slotNodes, (Object)null);
/*     */     } finally {
/* 376 */       this.w.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 381 */     reset();
/* 382 */     if (this.topologyRefreshExecutor != null) {
/* 383 */       logger.info("Cluster topology refresh shutdown, startNodes: {}", this.startNodes);
/* 384 */       this.topologyRefreshExecutor.shutdownNow();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getNodeKey(HostAndPort hnp) {
/* 390 */     return hnp.toString();
/*     */   }
/*     */   
/*     */   private List<Object> executeClusterSlots(Connection jedis) {
/* 394 */     jedis.sendCommand(Protocol.Command.CLUSTER, new String[] { "SLOTS" });
/* 395 */     return jedis.getObjectMultiBulkReply();
/*     */   }
/*     */   
/*     */   private List<Integer> getAssignedSlotArray(List<Object> slotInfo) {
/* 399 */     List<Integer> slotNums = new ArrayList<>();
/* 400 */     int slot = ((Long)slotInfo.get(0)).intValue(); for (; slot <= ((Long)slotInfo.get(1))
/* 401 */       .intValue(); slot++) {
/* 402 */       slotNums.add(Integer.valueOf(slot));
/*     */     }
/* 404 */     return slotNums;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisClusterInfoCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */