/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.providers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.ConnectionPool;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.DefaultJedisClientConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.JedisClientConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.ShardedCommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Hashing;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ShardedConnectionProvider
/*     */   implements ConnectionProvider
/*     */ {
/*  28 */   private final TreeMap<Long, HostAndPort> nodes = new TreeMap<>();
/*  29 */   private final Map<String, ConnectionPool> resources = new HashMap<>();
/*     */   private final JedisClientConfig clientConfig;
/*     */   private final GenericObjectPoolConfig<Connection> poolConfig;
/*     */   private final Hashing algo;
/*     */   
/*     */   public ShardedConnectionProvider(List<HostAndPort> shards) {
/*  35 */     this(shards, (JedisClientConfig)DefaultJedisClientConfig.builder().build());
/*     */   }
/*     */   
/*     */   public ShardedConnectionProvider(List<HostAndPort> shards, JedisClientConfig clientConfig) {
/*  39 */     this(shards, clientConfig, new GenericObjectPoolConfig());
/*     */   }
/*     */ 
/*     */   
/*     */   public ShardedConnectionProvider(List<HostAndPort> shards, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig) {
/*  44 */     this(shards, clientConfig, poolConfig, Hashing.MURMUR_HASH);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShardedConnectionProvider(List<HostAndPort> shards, JedisClientConfig clientConfig, Hashing algo) {
/*  49 */     this(shards, clientConfig, null, algo);
/*     */   }
/*     */ 
/*     */   
/*     */   public ShardedConnectionProvider(List<HostAndPort> shards, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Hashing algo) {
/*  54 */     this.clientConfig = clientConfig;
/*  55 */     this.poolConfig = poolConfig;
/*  56 */     this.algo = algo;
/*  57 */     initialize(shards);
/*     */   }
/*     */   
/*     */   private void initialize(List<HostAndPort> shards) {
/*  61 */     for (int i = 0; i < shards.size(); i++) {
/*  62 */       HostAndPort shard = shards.get(i);
/*  63 */       for (int n = 0; n < 160; n++) {
/*  64 */         Long hash = Long.valueOf(this.algo.hash("SHARD-" + i + "-NODE-" + n));
/*  65 */         this.nodes.put(hash, shard);
/*  66 */         setupNodeIfNotExist(shard);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private ConnectionPool setupNodeIfNotExist(HostAndPort node) {
/*  72 */     String nodeKey = node.toString();
/*  73 */     ConnectionPool existingPool = this.resources.get(nodeKey);
/*  74 */     if (existingPool != null) return existingPool;
/*     */     
/*  76 */     ConnectionPool nodePool = (this.poolConfig == null) ? new ConnectionPool(node, this.clientConfig) : new ConnectionPool(node, this.clientConfig, this.poolConfig);
/*     */     
/*  78 */     this.resources.put(nodeKey, nodePool);
/*  79 */     return nodePool;
/*     */   }
/*     */   
/*     */   public Hashing getHashingAlgo() {
/*  83 */     return this.algo;
/*     */   }
/*     */   
/*     */   private void reset() {
/*  87 */     for (ConnectionPool pool : this.resources.values()) {
/*     */       try {
/*  89 */         if (pool != null) {
/*  90 */           pool.destroy();
/*     */         }
/*  92 */       } catch (RuntimeException runtimeException) {}
/*     */     } 
/*     */ 
/*     */     
/*  96 */     this.resources.clear();
/*  97 */     this.nodes.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 102 */     reset();
/*     */   }
/*     */   
/*     */   public HostAndPort getNode(Long hash) {
/* 106 */     return (hash != null) ? getNodeFromHash(hash) : null;
/*     */   }
/*     */   
/*     */   public Connection getConnection(HostAndPort node) {
/* 110 */     return (node != null) ? setupNodeIfNotExist(node).getResource() : getConnection();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection(CommandArguments args) {
/* 115 */     Long hash = ((ShardedCommandArguments)args).getKeyHash();
/* 116 */     return (hash != null) ? getConnection(getNodeFromHash(hash)) : getConnection();
/*     */   }
/*     */   
/*     */   private List<ConnectionPool> getShuffledNodesPool() {
/* 120 */     List<ConnectionPool> pools = new ArrayList<>(this.resources.values());
/* 121 */     Collections.shuffle(pools);
/* 122 */     return pools;
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/* 127 */     List<ConnectionPool> pools = getShuffledNodesPool();
/*     */     
/* 129 */     JedisException suppressed = null;
/* 130 */     for (ConnectionPool pool : pools) {
/* 131 */       Connection jedis = null;
/*     */       try {
/* 133 */         jedis = pool.getResource();
/* 134 */         if (jedis == null) {
/*     */           continue;
/*     */         }
/*     */         
/* 138 */         jedis.ping();
/* 139 */         return jedis;
/*     */       }
/* 141 */       catch (JedisException ex) {
/* 142 */         if (suppressed == null) {
/* 143 */           suppressed = ex;
/*     */         }
/* 145 */         if (jedis != null) {
/* 146 */           jedis.close();
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     JedisException noReachableNode = new JedisException("No reachable shard.");
/* 152 */     if (suppressed != null) {
/* 153 */       noReachableNode.addSuppressed((Throwable)suppressed);
/*     */     }
/* 155 */     throw noReachableNode;
/*     */   }
/*     */   
/*     */   private HostAndPort getNodeFromHash(Long hash) {
/* 159 */     SortedMap<Long, HostAndPort> tail = this.nodes.tailMap(hash);
/* 160 */     if (tail.isEmpty()) {
/* 161 */       return this.nodes.get(this.nodes.firstKey());
/*     */     }
/* 163 */     return tail.get(tail.firstKey());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, ConnectionPool> getConnectionMap() {
/* 168 */     return Collections.unmodifiableMap(this.resources);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\providers\ShardedConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */