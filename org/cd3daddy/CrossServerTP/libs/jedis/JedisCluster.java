/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ClusterConnectionProvider;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisClusterCRC16;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JedisCluster
/*     */   extends UnifiedJedis
/*     */ {
/*     */   public static final String INIT_NO_ERROR_PROPERTY = "jedis.cluster.initNoError";
/*     */   public static final int DEFAULT_TIMEOUT = 2000;
/*     */   public static final int DEFAULT_MAX_ATTEMPTS = 5;
/*     */   
/*     */   public JedisCluster(HostAndPort node) {
/*  24 */     this(Collections.singleton(node));
/*     */   }
/*     */   
/*     */   public JedisCluster(HostAndPort node, int timeout) {
/*  28 */     this(Collections.singleton(node), timeout);
/*     */   }
/*     */   
/*     */   public JedisCluster(HostAndPort node, int timeout, int maxAttempts) {
/*  32 */     this(Collections.singleton(node), timeout, maxAttempts);
/*     */   }
/*     */   
/*     */   public JedisCluster(HostAndPort node, GenericObjectPoolConfig<Connection> poolConfig) {
/*  36 */     this(Collections.singleton(node), poolConfig);
/*     */   }
/*     */   
/*     */   public JedisCluster(HostAndPort node, int timeout, GenericObjectPoolConfig<Connection> poolConfig) {
/*  40 */     this(Collections.singleton(node), timeout, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, int timeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
/*  45 */     this(Collections.singleton(node), timeout, maxAttempts, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
/*  50 */     this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig<Connection> poolConfig) {
/*  55 */     this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
/*  61 */     this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
/*  68 */     this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, user, password, clientName, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
/*  75 */     this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, password, clientName, poolConfig, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
/*  82 */     this(Collections.singleton(node), connectionTimeout, soTimeout, maxAttempts, user, password, clientName, poolConfig, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(HostAndPort node, JedisClientConfig clientConfig, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
/*  88 */     this(Collections.singleton(node), clientConfig, maxAttempts, poolConfig);
/*     */   }
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> nodes) {
/*  92 */     this(nodes, 2000);
/*     */   }
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> nodes, int timeout) {
/*  96 */     this(nodes, DefaultJedisClientConfig.builder().timeoutMillis(timeout).build());
/*     */   }
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> nodes, int timeout, int maxAttempts) {
/* 100 */     this(nodes, DefaultJedisClientConfig.builder().timeoutMillis(timeout).build(), maxAttempts);
/*     */   }
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> nodes, String user, String password) {
/* 104 */     this(nodes, DefaultJedisClientConfig.builder().user(user).password(password).build());
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> nodes, String user, String password, HostAndPortMapper hostAndPortMap) {
/* 109 */     this(nodes, DefaultJedisClientConfig.builder().user(user).password(password)
/* 110 */         .hostAndPortMapper(hostAndPortMap).build());
/*     */   }
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> nodes, GenericObjectPoolConfig<Connection> poolConfig) {
/* 114 */     this(nodes, 2000, 5, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> nodes, int timeout, GenericObjectPoolConfig<Connection> poolConfig) {
/* 119 */     this(nodes, timeout, 5, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int timeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
/* 124 */     this(clusterNodes, timeout, timeout, maxAttempts, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
/* 129 */     this(clusterNodes, connectionTimeout, soTimeout, maxAttempts, (String)null, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String password, GenericObjectPoolConfig<Connection> poolConfig) {
/* 134 */     this(clusterNodes, connectionTimeout, soTimeout, maxAttempts, password, (String)null, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
/* 140 */     this(clusterNodes, connectionTimeout, soTimeout, maxAttempts, (String)null, password, clientName, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
/* 147 */     this(clusterNodes, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/* 148 */         .socketTimeoutMillis(soTimeout).user(user).password(password).clientName(clientName).build(), maxAttempts, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int infiniteSoTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig) {
/* 155 */     this(clusterNodes, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/* 156 */         .socketTimeoutMillis(soTimeout).blockingSocketTimeoutMillis(infiniteSoTimeout)
/* 157 */         .user(user).password(password).clientName(clientName).build(), maxAttempts, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
/* 163 */     this(clusterNodes, connectionTimeout, soTimeout, maxAttempts, (String)null, password, clientName, poolConfig, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, int connectionTimeout, int soTimeout, int maxAttempts, String user, String password, String clientName, GenericObjectPoolConfig<Connection> poolConfig, boolean ssl) {
/* 170 */     this(clusterNodes, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/* 171 */         .socketTimeoutMillis(soTimeout).user(user).password(password).clientName(clientName).ssl(ssl).build(), maxAttempts, poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts, GenericObjectPoolConfig<Connection> poolConfig) {
/* 177 */     this(clusterNodes, clientConfig, maxAttempts, 
/* 178 */         Duration.ofMillis(clientConfig.getSocketTimeoutMillis() * maxAttempts), poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts, Duration maxTotalRetriesDuration, GenericObjectPoolConfig<Connection> poolConfig) {
/* 184 */     super(clusterNodes, clientConfig, poolConfig, maxAttempts, maxTotalRetriesDuration);
/*     */   }
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig) {
/* 188 */     this(clusterNodes, clientConfig, 5);
/*     */   }
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts) {
/* 192 */     super(clusterNodes, clientConfig, maxAttempts);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, int maxAttempts, Duration maxTotalRetriesDuration) {
/* 197 */     super(clusterNodes, clientConfig, maxAttempts, maxTotalRetriesDuration);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Duration topologyRefreshPeriod, int maxAttempts, Duration maxTotalRetriesDuration) {
/* 203 */     this(new ClusterConnectionProvider(clusterNodes, clientConfig, poolConfig, topologyRefreshPeriod), maxAttempts, maxTotalRetriesDuration);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisCluster(ClusterConnectionProvider provider, int maxAttempts, Duration maxTotalRetriesDuration) {
/* 209 */     super(provider, maxAttempts, maxTotalRetriesDuration);
/*     */   }
/*     */   
/*     */   public Map<String, ConnectionPool> getClusterNodes() {
/* 213 */     return ((ClusterConnectionProvider)this.provider).getNodes();
/*     */   }
/*     */   
/*     */   public Connection getConnectionFromSlot(int slot) {
/* 217 */     return ((ClusterConnectionProvider)this.provider).getConnectionFromSlot(slot);
/*     */   }
/*     */ 
/*     */   
/*     */   public long spublish(String channel, String message) {
/* 222 */     return ((Long)executeCommand(this.commandObjects.spublish(channel, message))).longValue();
/*     */   }
/*     */   
/*     */   public long spublish(byte[] channel, byte[] message) {
/* 226 */     return ((Long)executeCommand(this.commandObjects.spublish(channel, message))).longValue();
/*     */   }
/*     */   
/*     */   public void ssubscribe(JedisShardedPubSub jedisPubSub, String... channels) {
/* 230 */     try (Connection connection = getConnectionFromSlot(JedisClusterCRC16.getSlot(channels[0]))) {
/* 231 */       jedisPubSub.proceed(connection, channels);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void ssubscribe(BinaryJedisShardedPubSub jedisPubSub, byte[]... channels) {
/* 236 */     try (Connection connection = getConnectionFromSlot(JedisClusterCRC16.getSlot(channels[0]))) {
/* 237 */       jedisPubSub.proceed(connection, channels);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClusterPipeline pipelined() {
/* 244 */     return new ClusterPipeline((ClusterConnectionProvider)this.provider, (ClusterCommandObjects)this.commandObjects);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transaction multi() {
/* 253 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisCluster.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */