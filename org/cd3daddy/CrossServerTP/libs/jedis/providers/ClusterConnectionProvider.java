/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.providers;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.ClusterCommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.ConnectionPool;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.JedisClientConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.JedisClusterInfoCache;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisClusterOperationException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClusterConnectionProvider
/*     */   implements ConnectionProvider
/*     */ {
/*     */   protected final JedisClusterInfoCache cache;
/*     */   
/*     */   public ClusterConnectionProvider(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig) {
/*  28 */     this.cache = new JedisClusterInfoCache(clientConfig, clusterNodes);
/*  29 */     initializeSlotsCache(clusterNodes, clientConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClusterConnectionProvider(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig) {
/*  34 */     this.cache = new JedisClusterInfoCache(clientConfig, poolConfig, clusterNodes);
/*  35 */     initializeSlotsCache(clusterNodes, clientConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public ClusterConnectionProvider(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Duration topologyRefreshPeriod) {
/*  40 */     this.cache = new JedisClusterInfoCache(clientConfig, poolConfig, clusterNodes, topologyRefreshPeriod);
/*  41 */     initializeSlotsCache(clusterNodes, clientConfig);
/*     */   }
/*     */   
/*     */   private void initializeSlotsCache(Set<HostAndPort> startNodes, JedisClientConfig clientConfig) {
/*  45 */     if (startNodes.isEmpty()) {
/*  46 */       throw new JedisClusterOperationException("No nodes to initialize cluster slots cache.");
/*     */     }
/*     */     
/*  49 */     ArrayList<HostAndPort> startNodeList = new ArrayList<>(startNodes);
/*  50 */     Collections.shuffle(startNodeList);
/*     */     
/*  52 */     JedisException firstException = null;
/*  53 */     for (HostAndPort hostAndPort : startNodeList) {
/*  54 */       try (Connection jedis = new Connection(hostAndPort, clientConfig)) {
/*  55 */         this.cache.discoverClusterNodesAndSlots(jedis);
/*     */         return;
/*  57 */       } catch (JedisException e) {
/*  58 */         if (firstException == null) {
/*  59 */           firstException = e;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  65 */     if (System.getProperty("jedis.cluster.initNoError") != null) {
/*     */       return;
/*     */     }
/*  68 */     JedisClusterOperationException uninitializedException = new JedisClusterOperationException("Could not initialize cluster slots cache.");
/*     */     
/*  70 */     uninitializedException.addSuppressed((Throwable)firstException);
/*  71 */     throw uninitializedException;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  76 */     this.cache.close();
/*     */   }
/*     */   
/*     */   public void renewSlotCache() {
/*  80 */     this.cache.renewClusterSlots(null);
/*     */   }
/*     */   
/*     */   public void renewSlotCache(Connection jedis) {
/*  84 */     this.cache.renewClusterSlots(jedis);
/*     */   }
/*     */   
/*     */   public Map<String, ConnectionPool> getNodes() {
/*  88 */     return this.cache.getNodes();
/*     */   }
/*     */   
/*     */   public HostAndPort getNode(int slot) {
/*  92 */     return (slot >= 0) ? this.cache.getSlotNode(slot) : null;
/*     */   }
/*     */   
/*     */   public Connection getConnection(HostAndPort node) {
/*  96 */     return (node != null) ? this.cache.setupNodeIfNotExist(node).getResource() : getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection(CommandArguments args) {
/* 101 */     int slot = ((ClusterCommandArguments)args).getCommandHashSlot();
/* 102 */     return (slot >= 0) ? getConnectionFromSlot(slot) : getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/* 111 */     List<ConnectionPool> pools = this.cache.getShuffledNodesPool();
/*     */     
/* 113 */     JedisException suppressed = null;
/* 114 */     for (ConnectionPool pool : pools) {
/* 115 */       Connection jedis = null;
/*     */       try {
/* 117 */         jedis = pool.getResource();
/* 118 */         if (jedis == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 122 */         jedis.ping();
/* 123 */         return jedis;
/*     */       }
/* 125 */       catch (JedisException ex) {
/* 126 */         if (suppressed == null) {
/* 127 */           suppressed = ex;
/*     */         }
/* 129 */         if (jedis != null) {
/* 130 */           jedis.close();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     JedisClusterOperationException noReachableNode = new JedisClusterOperationException("No reachable node in cluster.");
/* 136 */     if (suppressed != null) {
/* 137 */       noReachableNode.addSuppressed((Throwable)suppressed);
/*     */     }
/* 139 */     throw noReachableNode;
/*     */   }
/*     */   
/*     */   public Connection getConnectionFromSlot(int slot) {
/* 143 */     ConnectionPool connectionPool = this.cache.getSlotPool(slot);
/* 144 */     if (connectionPool != null)
/*     */     {
/* 146 */       return connectionPool.getResource();
/*     */     }
/*     */ 
/*     */     
/* 150 */     renewSlotCache();
/* 151 */     connectionPool = this.cache.getSlotPool(slot);
/* 152 */     if (connectionPool != null) {
/* 153 */       return connectionPool.getResource();
/*     */     }
/*     */     
/* 156 */     return getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ConnectionPool> getConnectionMap() {
/* 163 */     return Collections.unmodifiableMap(getNodes());
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\providers\ClusterConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */