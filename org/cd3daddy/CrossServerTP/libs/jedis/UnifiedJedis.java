/*      */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*      */ import java.net.URI;
/*      */ import java.time.Duration;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ExpiryOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortedSetOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.executors.CommandExecutor;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.GraphCommandObjects;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.RedisGraphCommands;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.ResultSet;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonSetParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path2;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusStoreParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoSearchParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.LPosParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.SetParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.SortingParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAutoClaimParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XClaimParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZAddParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZRangeParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ClusterConnectionProvider;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ShardedConnectionProvider;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.GeoRadiusResponse;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.LibraryInfo;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamEntry;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FTProfileParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FTSearchParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.Query;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.aggr.AggregationBuilder;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.TSElement;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.TSMRangeElements;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisURIHelper;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*      */ 
/*      */ public class UnifiedJedis implements JedisCommands, JedisBinaryCommands, SampleKeyedCommands, SampleBinaryKeyedCommands, RedisModuleCommands, AutoCloseable {
/*   51 */   protected RedisProtocol protocol = null;
/*      */   protected final ConnectionProvider provider;
/*      */   protected final CommandExecutor executor;
/*      */   protected final CommandObjects commandObjects;
/*      */   private final GraphCommandObjects graphCommandObjects;
/*   56 */   private JedisBroadcastAndRoundRobinConfig broadcastAndRoundRobinConfig = null;
/*      */   
/*      */   public UnifiedJedis() {
/*   59 */     this(new HostAndPort("127.0.0.1", 6379));
/*      */   }
/*      */ 
/*      */   
/*      */   public UnifiedJedis(HostAndPort hostAndPort) {
/*   64 */     this((ConnectionProvider)new PooledConnectionProvider(hostAndPort));
/*      */   }
/*      */   
/*      */   public UnifiedJedis(String url) {
/*   68 */     this(URI.create(url));
/*      */   }
/*      */   
/*      */   public UnifiedJedis(URI uri) {
/*   72 */     this(JedisURIHelper.getHostAndPort(uri), DefaultJedisClientConfig.builder()
/*   73 */         .user(JedisURIHelper.getUser(uri)).password(JedisURIHelper.getPassword(uri))
/*   74 */         .database(JedisURIHelper.getDBIndex(uri)).protocol(JedisURIHelper.getRedisProtocol(uri))
/*   75 */         .ssl(JedisURIHelper.isRedisSSLScheme(uri)).build());
/*      */   }
/*      */   
/*      */   public UnifiedJedis(URI uri, JedisClientConfig config) {
/*   79 */     this(JedisURIHelper.getHostAndPort(uri), DefaultJedisClientConfig.builder()
/*   80 */         .connectionTimeoutMillis(config.getConnectionTimeoutMillis())
/*   81 */         .socketTimeoutMillis(config.getSocketTimeoutMillis())
/*   82 */         .blockingSocketTimeoutMillis(config.getBlockingSocketTimeoutMillis())
/*   83 */         .user(JedisURIHelper.getUser(uri)).password(JedisURIHelper.getPassword(uri))
/*   84 */         .database(JedisURIHelper.getDBIndex(uri)).clientName(config.getClientName())
/*   85 */         .protocol(JedisURIHelper.getRedisProtocol(uri))
/*   86 */         .ssl(JedisURIHelper.isRedisSSLScheme(uri)).sslSocketFactory(config.getSslSocketFactory())
/*   87 */         .sslParameters(config.getSslParameters()).hostnameVerifier(config.getHostnameVerifier())
/*   88 */         .build());
/*      */   }
/*      */ 
/*      */   
/*      */   public UnifiedJedis(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/*   93 */     this((ConnectionProvider)new PooledConnectionProvider(hostAndPort, clientConfig));
/*   94 */     RedisProtocol proto = clientConfig.getRedisProtocol();
/*   95 */     if (proto != null) this.commandObjects.setProtocol(proto); 
/*      */   }
/*      */   
/*      */   public UnifiedJedis(ConnectionProvider provider) {
/*   99 */     this.provider = provider;
/*  100 */     this.executor = (CommandExecutor)new DefaultCommandExecutor(provider);
/*  101 */     this.commandObjects = new CommandObjects();
/*  102 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  103 */     this.graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*  104 */     try (Connection conn = this.provider.getConnection()) {
/*  105 */       if (conn != null) {
/*  106 */         RedisProtocol proto = conn.getRedisProtocol();
/*  107 */         if (proto != null) this.commandObjects.setProtocol(proto);
/*      */       
/*      */       } 
/*  110 */     } catch (JedisException jedisException) {}
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnifiedJedis(JedisSocketFactory socketFactory) {
/*  122 */     this(new Connection(socketFactory));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnifiedJedis(JedisSocketFactory socketFactory, JedisClientConfig clientConfig) {
/*  132 */     this(new Connection(socketFactory, clientConfig));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnifiedJedis(Connection connection) {
/*  142 */     this.provider = null;
/*  143 */     this.executor = (CommandExecutor)new SimpleCommandExecutor(connection);
/*  144 */     this.commandObjects = new CommandObjects();
/*  145 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  146 */     RedisProtocol proto = connection.getRedisProtocol();
/*  147 */     if (proto == RedisProtocol.RESP3) this.commandObjects.setProtocol(proto); 
/*      */   }
/*      */   
/*      */   public UnifiedJedis(Set<HostAndPort> jedisClusterNodes, JedisClientConfig clientConfig, int maxAttempts) {
/*  151 */     this(new ClusterConnectionProvider(jedisClusterNodes, clientConfig), maxAttempts, 
/*  152 */         Duration.ofMillis((maxAttempts * clientConfig.getSocketTimeoutMillis())));
/*  153 */     RedisProtocol proto = clientConfig.getRedisProtocol();
/*  154 */     if (proto != null) this.commandObjects.setProtocol(proto); 
/*      */   }
/*      */   
/*      */   public UnifiedJedis(Set<HostAndPort> jedisClusterNodes, JedisClientConfig clientConfig, int maxAttempts, Duration maxTotalRetriesDuration) {
/*  158 */     this(new ClusterConnectionProvider(jedisClusterNodes, clientConfig), maxAttempts, maxTotalRetriesDuration);
/*  159 */     RedisProtocol proto = clientConfig.getRedisProtocol();
/*  160 */     if (proto != null) this.commandObjects.setProtocol(proto);
/*      */   
/*      */   }
/*      */   
/*      */   public UnifiedJedis(Set<HostAndPort> jedisClusterNodes, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, int maxAttempts, Duration maxTotalRetriesDuration) {
/*  165 */     this(new ClusterConnectionProvider(jedisClusterNodes, clientConfig, poolConfig), maxAttempts, maxTotalRetriesDuration);
/*  166 */     RedisProtocol proto = clientConfig.getRedisProtocol();
/*  167 */     if (proto != null) this.commandObjects.setProtocol(proto); 
/*      */   }
/*      */   
/*      */   public UnifiedJedis(ClusterConnectionProvider provider, int maxAttempts, Duration maxTotalRetriesDuration) {
/*  171 */     this.provider = (ConnectionProvider)provider;
/*  172 */     this.executor = (CommandExecutor)new ClusterCommandExecutor(provider, maxAttempts, maxTotalRetriesDuration);
/*  173 */     this.commandObjects = new ClusterCommandObjects();
/*  174 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  175 */     this.graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public UnifiedJedis(ShardedConnectionProvider provider) {
/*  183 */     this.provider = (ConnectionProvider)provider;
/*  184 */     this.executor = (CommandExecutor)new DefaultCommandExecutor((ConnectionProvider)provider);
/*  185 */     this.commandObjects = new ShardedCommandObjects(provider.getHashingAlgo());
/*  186 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  187 */     this.graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public UnifiedJedis(ShardedConnectionProvider provider, Pattern tagPattern) {
/*  195 */     this.provider = (ConnectionProvider)provider;
/*  196 */     this.executor = (CommandExecutor)new DefaultCommandExecutor((ConnectionProvider)provider);
/*  197 */     this.commandObjects = new ShardedCommandObjects(provider.getHashingAlgo(), tagPattern);
/*  198 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  199 */     this.graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*      */   }
/*      */   
/*      */   public UnifiedJedis(ConnectionProvider provider, int maxAttempts, Duration maxTotalRetriesDuration) {
/*  203 */     this.provider = provider;
/*  204 */     this.executor = (CommandExecutor)new RetryableCommandExecutor(provider, maxAttempts, maxTotalRetriesDuration);
/*  205 */     this.commandObjects = new CommandObjects();
/*  206 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  207 */     this.graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnifiedJedis(MultiClusterPooledConnectionProvider provider) {
/*  218 */     this.provider = (ConnectionProvider)provider;
/*  219 */     this.executor = (CommandExecutor)new CircuitBreakerCommandExecutor(provider);
/*  220 */     this.commandObjects = new CommandObjects();
/*  221 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  222 */     this.graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UnifiedJedis(CommandExecutor executor) {
/*  232 */     this.provider = null;
/*  233 */     this.executor = executor;
/*  234 */     this.commandObjects = new CommandObjects();
/*  235 */     this.graphCommandObjects = new GraphCommandObjects((RedisGraphCommands)this);
/*  236 */     this.graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {
/*  241 */     IOUtils.closeQuietly((AutoCloseable)this.executor);
/*      */   }
/*      */   
/*      */   protected final void setProtocol(RedisProtocol protocol) {
/*  245 */     this.protocol = protocol;
/*  246 */     this.commandObjects.setProtocol(this.protocol);
/*      */   }
/*      */   
/*      */   public final <T> T executeCommand(CommandObject<T> commandObject) {
/*  250 */     return (T)this.executor.executeCommand(commandObject);
/*      */   }
/*      */   
/*      */   public final <T> T broadcastCommand(CommandObject<T> commandObject) {
/*  254 */     return (T)this.executor.broadcastCommand(commandObject);
/*      */   }
/*      */   
/*      */   private <T> T checkAndBroadcastCommand(CommandObject<T> commandObject) {
/*  258 */     boolean broadcast = true;
/*      */     
/*  260 */     if (this.broadcastAndRoundRobinConfig != null && 
/*  261 */       commandObject.getArguments().getCommand() instanceof org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol.SearchCommand && this.broadcastAndRoundRobinConfig
/*  262 */       .getRediSearchModeInCluster() == JedisBroadcastAndRoundRobinConfig.RediSearchMode.LIGHT) {
/*  263 */       broadcast = false;
/*      */     }
/*      */     
/*  266 */     return broadcast ? broadcastCommand(commandObject) : executeCommand(commandObject);
/*      */   }
/*      */   
/*      */   public void setBroadcastAndRoundRobinConfig(JedisBroadcastAndRoundRobinConfig config) {
/*  270 */     this.broadcastAndRoundRobinConfig = config;
/*  271 */     this.commandObjects.setBroadcastAndRoundRobinConfig(this.broadcastAndRoundRobinConfig);
/*      */   }
/*      */   
/*      */   public String ping() {
/*  275 */     return checkAndBroadcastCommand(this.commandObjects.ping());
/*      */   }
/*      */   
/*      */   public String flushDB() {
/*  279 */     return checkAndBroadcastCommand(this.commandObjects.flushDB());
/*      */   }
/*      */   
/*      */   public String flushAll() {
/*  283 */     return checkAndBroadcastCommand(this.commandObjects.flushAll());
/*      */   }
/*      */   
/*      */   public String configSet(String parameter, String value) {
/*  287 */     return checkAndBroadcastCommand(this.commandObjects.configSet(parameter, value));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean exists(String key) {
/*  293 */     return ((Boolean)executeCommand(this.commandObjects.exists(key))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long exists(String... keys) {
/*  298 */     return ((Long)executeCommand(this.commandObjects.exists(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long persist(String key) {
/*  303 */     return ((Long)executeCommand(this.commandObjects.persist(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String type(String key) {
/*  308 */     return executeCommand(this.commandObjects.type(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean exists(byte[] key) {
/*  313 */     return ((Boolean)executeCommand(this.commandObjects.exists(key))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long exists(byte[]... keys) {
/*  318 */     return ((Long)executeCommand(this.commandObjects.exists(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long persist(byte[] key) {
/*  323 */     return ((Long)executeCommand(this.commandObjects.persist(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String type(byte[] key) {
/*  328 */     return executeCommand(this.commandObjects.type(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] dump(String key) {
/*  333 */     return executeCommand((CommandObject)this.commandObjects.dump(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String restore(String key, long ttl, byte[] serializedValue) {
/*  338 */     return executeCommand(this.commandObjects.restore(key, ttl, serializedValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public String restore(String key, long ttl, byte[] serializedValue, RestoreParams params) {
/*  343 */     return executeCommand(this.commandObjects.restore(key, ttl, serializedValue, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] dump(byte[] key) {
/*  348 */     return executeCommand((CommandObject)this.commandObjects.dump(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String restore(byte[] key, long ttl, byte[] serializedValue) {
/*  353 */     return executeCommand(this.commandObjects.restore(key, ttl, serializedValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public String restore(byte[] key, long ttl, byte[] serializedValue, RestoreParams params) {
/*  358 */     return executeCommand(this.commandObjects.restore(key, ttl, serializedValue, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long expire(String key, long seconds) {
/*  363 */     return ((Long)executeCommand(this.commandObjects.expire(key, seconds))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expire(String key, long seconds, ExpiryOption expiryOption) {
/*  368 */     return ((Long)executeCommand(this.commandObjects.expire(key, seconds, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpire(String key, long milliseconds) {
/*  373 */     return ((Long)executeCommand(this.commandObjects.pexpire(key, milliseconds))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpire(String key, long milliseconds, ExpiryOption expiryOption) {
/*  378 */     return ((Long)executeCommand(this.commandObjects.pexpire(key, milliseconds, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireTime(String key) {
/*  383 */     return ((Long)executeCommand(this.commandObjects.expireTime(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireTime(String key) {
/*  388 */     return ((Long)executeCommand(this.commandObjects.pexpireTime(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireAt(String key, long unixTime) {
/*  393 */     return ((Long)executeCommand(this.commandObjects.expireAt(key, unixTime))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireAt(String key, long unixTime, ExpiryOption expiryOption) {
/*  398 */     return ((Long)executeCommand(this.commandObjects.expireAt(key, unixTime, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireAt(String key, long millisecondsTimestamp) {
/*  403 */     return ((Long)executeCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireAt(String key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/*  408 */     return ((Long)executeCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expire(byte[] key, long seconds) {
/*  413 */     return ((Long)executeCommand(this.commandObjects.expire(key, seconds))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expire(byte[] key, long seconds, ExpiryOption expiryOption) {
/*  418 */     return ((Long)executeCommand(this.commandObjects.expire(key, seconds, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpire(byte[] key, long milliseconds) {
/*  423 */     return ((Long)executeCommand(this.commandObjects.pexpire(key, milliseconds))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpire(byte[] key, long milliseconds, ExpiryOption expiryOption) {
/*  428 */     return ((Long)executeCommand(this.commandObjects.pexpire(key, milliseconds, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireTime(byte[] key) {
/*  433 */     return ((Long)executeCommand(this.commandObjects.expireTime(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireTime(byte[] key) {
/*  438 */     return ((Long)executeCommand(this.commandObjects.pexpireTime(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireAt(byte[] key, long unixTime) {
/*  443 */     return ((Long)executeCommand(this.commandObjects.expireAt(key, unixTime))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireAt(byte[] key, long unixTime, ExpiryOption expiryOption) {
/*  448 */     return ((Long)executeCommand(this.commandObjects.expireAt(key, unixTime, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireAt(byte[] key, long millisecondsTimestamp) {
/*  453 */     return ((Long)executeCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireAt(byte[] key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/*  458 */     return ((Long)executeCommand(this.commandObjects.expireAt(key, millisecondsTimestamp, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long ttl(String key) {
/*  463 */     return ((Long)executeCommand(this.commandObjects.ttl(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pttl(String key) {
/*  468 */     return ((Long)executeCommand(this.commandObjects.pttl(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long touch(String key) {
/*  473 */     return ((Long)executeCommand(this.commandObjects.touch(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long touch(String... keys) {
/*  478 */     return ((Long)executeCommand(this.commandObjects.touch(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long ttl(byte[] key) {
/*  483 */     return ((Long)executeCommand(this.commandObjects.ttl(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pttl(byte[] key) {
/*  488 */     return ((Long)executeCommand(this.commandObjects.pttl(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long touch(byte[] key) {
/*  493 */     return ((Long)executeCommand(this.commandObjects.touch(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long touch(byte[]... keys) {
/*  498 */     return ((Long)executeCommand(this.commandObjects.touch(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> sort(String key) {
/*  503 */     return executeCommand(this.commandObjects.sort(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> sort(String key, SortingParams sortingParams) {
/*  508 */     return executeCommand(this.commandObjects.sort(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sort(String key, String dstkey) {
/*  513 */     return ((Long)executeCommand(this.commandObjects.sort(key, dstkey))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long sort(String key, SortingParams sortingParams, String dstkey) {
/*  518 */     return ((Long)executeCommand(this.commandObjects.sort(key, sortingParams, dstkey))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> sortReadonly(String key, SortingParams sortingParams) {
/*  523 */     return executeCommand(this.commandObjects.sortReadonly(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> sort(byte[] key) {
/*  528 */     return executeCommand(this.commandObjects.sort(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> sort(byte[] key, SortingParams sortingParams) {
/*  533 */     return executeCommand(this.commandObjects.sort(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sort(byte[] key, byte[] dstkey) {
/*  538 */     return ((Long)executeCommand(this.commandObjects.sort(key, dstkey))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> sortReadonly(byte[] key, SortingParams sortingParams) {
/*  543 */     return executeCommand(this.commandObjects.sortReadonly(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sort(byte[] key, SortingParams sortingParams, byte[] dstkey) {
/*  548 */     return ((Long)executeCommand(this.commandObjects.sort(key, sortingParams, dstkey))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long del(String key) {
/*  553 */     return ((Long)executeCommand(this.commandObjects.del(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long del(String... keys) {
/*  558 */     return ((Long)executeCommand(this.commandObjects.del(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long unlink(String key) {
/*  563 */     return ((Long)executeCommand(this.commandObjects.unlink(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long unlink(String... keys) {
/*  568 */     return ((Long)executeCommand(this.commandObjects.unlink(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long del(byte[] key) {
/*  573 */     return ((Long)executeCommand(this.commandObjects.del(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long del(byte[]... keys) {
/*  578 */     return ((Long)executeCommand(this.commandObjects.del(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long unlink(byte[] key) {
/*  583 */     return ((Long)executeCommand(this.commandObjects.unlink(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long unlink(byte[]... keys) {
/*  588 */     return ((Long)executeCommand(this.commandObjects.unlink(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(String key) {
/*  593 */     return executeCommand(this.commandObjects.memoryUsage(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(String key, int samples) {
/*  598 */     return executeCommand(this.commandObjects.memoryUsage(key, samples));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(byte[] key) {
/*  603 */     return executeCommand(this.commandObjects.memoryUsage(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(byte[] key, int samples) {
/*  608 */     return executeCommand(this.commandObjects.memoryUsage(key, samples));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean copy(String srcKey, String dstKey, boolean replace) {
/*  613 */     return ((Boolean)executeCommand(this.commandObjects.copy(srcKey, dstKey, replace))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String rename(String oldkey, String newkey) {
/*  618 */     return executeCommand(this.commandObjects.rename(oldkey, newkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public long renamenx(String oldkey, String newkey) {
/*  623 */     return ((Long)executeCommand(this.commandObjects.renamenx(oldkey, newkey))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean copy(byte[] srcKey, byte[] dstKey, boolean replace) {
/*  628 */     return ((Boolean)executeCommand(this.commandObjects.copy(srcKey, dstKey, replace))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String rename(byte[] oldkey, byte[] newkey) {
/*  633 */     return executeCommand(this.commandObjects.rename(oldkey, newkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public long renamenx(byte[] oldkey, byte[] newkey) {
/*  638 */     return ((Long)executeCommand(this.commandObjects.renamenx(oldkey, newkey))).longValue();
/*      */   }
/*      */   
/*      */   public long dbSize() {
/*  642 */     return ((Long)executeCommand(this.commandObjects.dbSize())).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> keys(String pattern) {
/*  647 */     return executeCommand(this.commandObjects.keys(pattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> scan(String cursor) {
/*  652 */     return executeCommand(this.commandObjects.scan(cursor));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> scan(String cursor, ScanParams params) {
/*  657 */     return executeCommand(this.commandObjects.scan(cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> scan(String cursor, ScanParams params, String type) {
/*  662 */     return executeCommand(this.commandObjects.scan(cursor, params, type));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScanIteration scanIteration(int batchCount, String match) {
/*  671 */     return new ScanIteration(this.provider, batchCount, match);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScanIteration scanIteration(int batchCount, String match, String type) {
/*  681 */     return new ScanIteration(this.provider, batchCount, match, type);
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> keys(byte[] pattern) {
/*  686 */     return executeCommand(this.commandObjects.keys(pattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> scan(byte[] cursor) {
/*  691 */     return executeCommand(this.commandObjects.scan(cursor));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
/*  696 */     return executeCommand(this.commandObjects.scan(cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> scan(byte[] cursor, ScanParams params, byte[] type) {
/*  701 */     return executeCommand(this.commandObjects.scan(cursor, params, type));
/*      */   }
/*      */ 
/*      */   
/*      */   public String randomKey() {
/*  706 */     return executeCommand(this.commandObjects.randomKey());
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] randomBinaryKey() {
/*  711 */     return executeCommand((CommandObject)this.commandObjects.randomBinaryKey());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String set(String key, String value) {
/*  718 */     return executeCommand(this.commandObjects.set(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String set(String key, String value, SetParams params) {
/*  723 */     return executeCommand(this.commandObjects.set(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String get(String key) {
/*  728 */     return executeCommand(this.commandObjects.get(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String setGet(String key, String value) {
/*  733 */     return executeCommand(this.commandObjects.setGet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String setGet(String key, String value, SetParams params) {
/*  738 */     return executeCommand(this.commandObjects.setGet(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDel(String key) {
/*  743 */     return executeCommand(this.commandObjects.getDel(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String getEx(String key, GetExParams params) {
/*  748 */     return executeCommand(this.commandObjects.getEx(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String set(byte[] key, byte[] value) {
/*  753 */     return executeCommand(this.commandObjects.set(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String set(byte[] key, byte[] value, SetParams params) {
/*  758 */     return executeCommand(this.commandObjects.set(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] get(byte[] key) {
/*  763 */     return executeCommand((CommandObject)this.commandObjects.get(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] setGet(byte[] key, byte[] value) {
/*  768 */     return executeCommand((CommandObject)this.commandObjects.setGet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] setGet(byte[] key, byte[] value, SetParams params) {
/*  773 */     return executeCommand((CommandObject)this.commandObjects.setGet(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getDel(byte[] key) {
/*  778 */     return executeCommand((CommandObject)this.commandObjects.getDel(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getEx(byte[] key, GetExParams params) {
/*  783 */     return executeCommand((CommandObject)this.commandObjects.getEx(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setbit(String key, long offset, boolean value) {
/*  788 */     return ((Boolean)executeCommand(this.commandObjects.setbit(key, offset, value))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getbit(String key, long offset) {
/*  793 */     return ((Boolean)executeCommand(this.commandObjects.getbit(key, offset))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long setrange(String key, long offset, String value) {
/*  798 */     return ((Long)executeCommand(this.commandObjects.setrange(key, offset, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getrange(String key, long startOffset, long endOffset) {
/*  803 */     return executeCommand(this.commandObjects.getrange(key, startOffset, endOffset));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setbit(byte[] key, long offset, boolean value) {
/*  808 */     return ((Boolean)executeCommand(this.commandObjects.setbit(key, offset, value))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getbit(byte[] key, long offset) {
/*  813 */     return ((Boolean)executeCommand(this.commandObjects.getbit(key, offset))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long setrange(byte[] key, long offset, byte[] value) {
/*  818 */     return ((Long)executeCommand(this.commandObjects.setrange(key, offset, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getrange(byte[] key, long startOffset, long endOffset) {
/*  823 */     return executeCommand((CommandObject)this.commandObjects.getrange(key, startOffset, endOffset));
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSet(String key, String value) {
/*  828 */     return executeCommand(this.commandObjects.getSet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public long setnx(String key, String value) {
/*  833 */     return ((Long)executeCommand(this.commandObjects.setnx(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String setex(String key, long seconds, String value) {
/*  838 */     return executeCommand(this.commandObjects.setex(key, seconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String psetex(String key, long milliseconds, String value) {
/*  843 */     return executeCommand(this.commandObjects.psetex(key, milliseconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getSet(byte[] key, byte[] value) {
/*  848 */     return executeCommand((CommandObject)this.commandObjects.getSet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public long setnx(byte[] key, byte[] value) {
/*  853 */     return ((Long)executeCommand(this.commandObjects.setnx(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String setex(byte[] key, long seconds, byte[] value) {
/*  858 */     return executeCommand(this.commandObjects.setex(key, seconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String psetex(byte[] key, long milliseconds, byte[] value) {
/*  863 */     return executeCommand(this.commandObjects.psetex(key, milliseconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public long incr(String key) {
/*  868 */     return ((Long)executeCommand(this.commandObjects.incr(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long incrBy(String key, long increment) {
/*  873 */     return ((Long)executeCommand(this.commandObjects.incrBy(key, increment))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double incrByFloat(String key, double increment) {
/*  878 */     return ((Double)executeCommand(this.commandObjects.incrByFloat(key, increment))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long decr(String key) {
/*  883 */     return ((Long)executeCommand(this.commandObjects.decr(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long decrBy(String key, long decrement) {
/*  888 */     return ((Long)executeCommand(this.commandObjects.decrBy(key, decrement))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long incr(byte[] key) {
/*  893 */     return ((Long)executeCommand(this.commandObjects.incr(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long incrBy(byte[] key, long increment) {
/*  898 */     return ((Long)executeCommand(this.commandObjects.incrBy(key, increment))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double incrByFloat(byte[] key, double increment) {
/*  903 */     return ((Double)executeCommand(this.commandObjects.incrByFloat(key, increment))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long decr(byte[] key) {
/*  908 */     return ((Long)executeCommand(this.commandObjects.decr(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long decrBy(byte[] key, long decrement) {
/*  913 */     return ((Long)executeCommand(this.commandObjects.decrBy(key, decrement))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> mget(String... keys) {
/*  918 */     return executeCommand(this.commandObjects.mget(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String mset(String... keysvalues) {
/*  923 */     return executeCommand(this.commandObjects.mset(keysvalues));
/*      */   }
/*      */ 
/*      */   
/*      */   public long msetnx(String... keysvalues) {
/*  928 */     return ((Long)executeCommand(this.commandObjects.msetnx(keysvalues))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> mget(byte[]... keys) {
/*  933 */     return executeCommand(this.commandObjects.mget(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String mset(byte[]... keysvalues) {
/*  938 */     return executeCommand(this.commandObjects.mset(keysvalues));
/*      */   }
/*      */ 
/*      */   
/*      */   public long msetnx(byte[]... keysvalues) {
/*  943 */     return ((Long)executeCommand(this.commandObjects.msetnx(keysvalues))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long append(String key, String value) {
/*  948 */     return ((Long)executeCommand(this.commandObjects.append(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String substr(String key, int start, int end) {
/*  953 */     return executeCommand(this.commandObjects.substr(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public long strlen(String key) {
/*  958 */     return ((Long)executeCommand(this.commandObjects.strlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long append(byte[] key, byte[] value) {
/*  963 */     return ((Long)executeCommand(this.commandObjects.append(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] substr(byte[] key, int start, int end) {
/*  968 */     return executeCommand((CommandObject)this.commandObjects.substr(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public long strlen(byte[] key) {
/*  973 */     return ((Long)executeCommand(this.commandObjects.strlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(String key) {
/*  978 */     return ((Long)executeCommand(this.commandObjects.bitcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(String key, long start, long end) {
/*  983 */     return ((Long)executeCommand(this.commandObjects.bitcount(key, start, end))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(String key, long start, long end, BitCountOption option) {
/*  988 */     return ((Long)executeCommand(this.commandObjects.bitcount(key, start, end, option))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(String key, boolean value) {
/*  993 */     return ((Long)executeCommand(this.commandObjects.bitpos(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(String key, boolean value, BitPosParams params) {
/*  998 */     return ((Long)executeCommand(this.commandObjects.bitpos(key, value, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(byte[] key) {
/* 1003 */     return ((Long)executeCommand(this.commandObjects.bitcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(byte[] key, long start, long end) {
/* 1008 */     return ((Long)executeCommand(this.commandObjects.bitcount(key, start, end))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(byte[] key, long start, long end, BitCountOption option) {
/* 1013 */     return ((Long)executeCommand(this.commandObjects.bitcount(key, start, end, option))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(byte[] key, boolean value) {
/* 1018 */     return ((Long)executeCommand(this.commandObjects.bitpos(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(byte[] key, boolean value, BitPosParams params) {
/* 1023 */     return ((Long)executeCommand(this.commandObjects.bitpos(key, value, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfield(String key, String... arguments) {
/* 1028 */     return executeCommand(this.commandObjects.bitfield(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfieldReadonly(String key, String... arguments) {
/* 1033 */     return executeCommand(this.commandObjects.bitfieldReadonly(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfield(byte[] key, byte[]... arguments) {
/* 1038 */     return executeCommand(this.commandObjects.bitfield(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfieldReadonly(byte[] key, byte[]... arguments) {
/* 1043 */     return executeCommand(this.commandObjects.bitfieldReadonly(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitop(BitOP op, String destKey, String... srcKeys) {
/* 1048 */     return ((Long)executeCommand(this.commandObjects.bitop(op, destKey, srcKeys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
/* 1053 */     return ((Long)executeCommand(this.commandObjects.bitop(op, destKey, srcKeys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public LCSMatchResult lcs(String keyA, String keyB, LCSParams params) {
/* 1058 */     return executeCommand(this.commandObjects.lcs(keyA, keyB, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public LCSMatchResult lcs(byte[] keyA, byte[] keyB, LCSParams params) {
/* 1063 */     return executeCommand(this.commandObjects.lcs(keyA, keyB, params));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long rpush(String key, String... string) {
/* 1070 */     return ((Long)executeCommand(this.commandObjects.rpush(key, string))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long lpush(String key, String... string) {
/* 1075 */     return ((Long)executeCommand(this.commandObjects.lpush(key, string))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long llen(String key) {
/* 1080 */     return ((Long)executeCommand(this.commandObjects.llen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> lrange(String key, long start, long stop) {
/* 1085 */     return executeCommand(this.commandObjects.lrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ltrim(String key, long start, long stop) {
/* 1090 */     return executeCommand(this.commandObjects.ltrim(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public String lindex(String key, long index) {
/* 1095 */     return executeCommand(this.commandObjects.lindex(key, index));
/*      */   }
/*      */ 
/*      */   
/*      */   public long rpush(byte[] key, byte[]... args) {
/* 1100 */     return ((Long)executeCommand(this.commandObjects.rpush(key, args))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long lpush(byte[] key, byte[]... args) {
/* 1105 */     return ((Long)executeCommand(this.commandObjects.lpush(key, args))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long llen(byte[] key) {
/* 1110 */     return ((Long)executeCommand(this.commandObjects.llen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> lrange(byte[] key, long start, long stop) {
/* 1115 */     return executeCommand(this.commandObjects.lrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ltrim(byte[] key, long start, long stop) {
/* 1120 */     return executeCommand(this.commandObjects.ltrim(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] lindex(byte[] key, long index) {
/* 1125 */     return executeCommand((CommandObject)this.commandObjects.lindex(key, index));
/*      */   }
/*      */ 
/*      */   
/*      */   public String lset(String key, long index, String value) {
/* 1130 */     return executeCommand(this.commandObjects.lset(key, index, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public long lrem(String key, long count, String value) {
/* 1135 */     return ((Long)executeCommand(this.commandObjects.lrem(key, count, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String lpop(String key) {
/* 1140 */     return executeCommand(this.commandObjects.lpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> lpop(String key, int count) {
/* 1145 */     return executeCommand(this.commandObjects.lpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public String lset(byte[] key, long index, byte[] value) {
/* 1150 */     return executeCommand(this.commandObjects.lset(key, index, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public long lrem(byte[] key, long count, byte[] value) {
/* 1155 */     return ((Long)executeCommand(this.commandObjects.lrem(key, count, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] lpop(byte[] key) {
/* 1160 */     return executeCommand((CommandObject)this.commandObjects.lpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> lpop(byte[] key, int count) {
/* 1165 */     return executeCommand(this.commandObjects.lpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long lpos(String key, String element) {
/* 1170 */     return executeCommand(this.commandObjects.lpos(key, element));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long lpos(String key, String element, LPosParams params) {
/* 1175 */     return executeCommand(this.commandObjects.lpos(key, element, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> lpos(String key, String element, LPosParams params, long count) {
/* 1180 */     return executeCommand(this.commandObjects.lpos(key, element, params, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long lpos(byte[] key, byte[] element) {
/* 1185 */     return executeCommand(this.commandObjects.lpos(key, element));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long lpos(byte[] key, byte[] element, LPosParams params) {
/* 1190 */     return executeCommand(this.commandObjects.lpos(key, element, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> lpos(byte[] key, byte[] element, LPosParams params, long count) {
/* 1195 */     return executeCommand(this.commandObjects.lpos(key, element, params, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public String rpop(String key) {
/* 1200 */     return executeCommand(this.commandObjects.rpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> rpop(String key, int count) {
/* 1205 */     return executeCommand(this.commandObjects.rpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] rpop(byte[] key) {
/* 1210 */     return executeCommand((CommandObject)this.commandObjects.rpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> rpop(byte[] key, int count) {
/* 1215 */     return executeCommand(this.commandObjects.rpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long linsert(String key, ListPosition where, String pivot, String value) {
/* 1220 */     return ((Long)executeCommand(this.commandObjects.linsert(key, where, pivot, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long lpushx(String key, String... strings) {
/* 1225 */     return ((Long)executeCommand(this.commandObjects.lpushx(key, strings))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long rpushx(String key, String... strings) {
/* 1230 */     return ((Long)executeCommand(this.commandObjects.rpushx(key, strings))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value) {
/* 1235 */     return ((Long)executeCommand(this.commandObjects.linsert(key, where, pivot, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long lpushx(byte[] key, byte[]... args) {
/* 1240 */     return ((Long)executeCommand(this.commandObjects.lpushx(key, args))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long rpushx(byte[] key, byte[]... args) {
/* 1245 */     return ((Long)executeCommand(this.commandObjects.rpushx(key, args))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> blpop(int timeout, String key) {
/* 1250 */     return executeCommand(this.commandObjects.blpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> blpop(double timeout, String key) {
/* 1255 */     return executeCommand(this.commandObjects.blpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> brpop(int timeout, String key) {
/* 1260 */     return executeCommand(this.commandObjects.brpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> brpop(double timeout, String key) {
/* 1265 */     return executeCommand(this.commandObjects.brpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> blpop(int timeout, String... keys) {
/* 1270 */     return executeCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> blpop(double timeout, String... keys) {
/* 1275 */     return executeCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> brpop(int timeout, String... keys) {
/* 1280 */     return executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> brpop(double timeout, String... keys) {
/* 1285 */     return executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> blpop(int timeout, byte[]... keys) {
/* 1290 */     return executeCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], byte[]> blpop(double timeout, byte[]... keys) {
/* 1295 */     return executeCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> brpop(int timeout, byte[]... keys) {
/* 1300 */     return executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], byte[]> brpop(double timeout, byte[]... keys) {
/* 1305 */     return executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String rpoplpush(String srckey, String dstkey) {
/* 1310 */     return executeCommand(this.commandObjects.rpoplpush(srckey, dstkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public String brpoplpush(String source, String destination, int timeout) {
/* 1315 */     return executeCommand(this.commandObjects.brpoplpush(source, destination, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
/* 1320 */     return executeCommand((CommandObject)this.commandObjects.rpoplpush(srckey, dstkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
/* 1325 */     return executeCommand((CommandObject)this.commandObjects.brpoplpush(source, destination, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public String lmove(String srcKey, String dstKey, ListDirection from, ListDirection to) {
/* 1330 */     return executeCommand(this.commandObjects.lmove(srcKey, dstKey, from, to));
/*      */   }
/*      */ 
/*      */   
/*      */   public String blmove(String srcKey, String dstKey, ListDirection from, ListDirection to, double timeout) {
/* 1335 */     return executeCommand(this.commandObjects.blmove(srcKey, dstKey, from, to, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] lmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to) {
/* 1340 */     return executeCommand((CommandObject)this.commandObjects.lmove(srcKey, dstKey, from, to));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to, double timeout) {
/* 1345 */     return executeCommand((CommandObject)this.commandObjects.blmove(srcKey, dstKey, from, to, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> lmpop(ListDirection direction, String... keys) {
/* 1350 */     return executeCommand(this.commandObjects.lmpop(direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> lmpop(ListDirection direction, int count, String... keys) {
/* 1355 */     return executeCommand(this.commandObjects.lmpop(direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> blmpop(double timeout, ListDirection direction, String... keys) {
/* 1360 */     return executeCommand(this.commandObjects.blmpop(timeout, direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> blmpop(double timeout, ListDirection direction, int count, String... keys) {
/* 1365 */     return executeCommand(this.commandObjects.blmpop(timeout, direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> lmpop(ListDirection direction, byte[]... keys) {
/* 1370 */     return executeCommand(this.commandObjects.lmpop(direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> lmpop(ListDirection direction, int count, byte[]... keys) {
/* 1375 */     return executeCommand(this.commandObjects.lmpop(direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> blmpop(double timeout, ListDirection direction, byte[]... keys) {
/* 1380 */     return executeCommand(this.commandObjects.blmpop(timeout, direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> blmpop(double timeout, ListDirection direction, int count, byte[]... keys) {
/* 1385 */     return executeCommand(this.commandObjects.blmpop(timeout, direction, count, keys));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long hset(String key, String field, String value) {
/* 1392 */     return ((Long)executeCommand(this.commandObjects.hset(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hset(String key, Map<String, String> hash) {
/* 1397 */     return ((Long)executeCommand(this.commandObjects.hset(key, hash))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String hget(String key, String field) {
/* 1402 */     return executeCommand(this.commandObjects.hget(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hsetnx(String key, String field, String value) {
/* 1407 */     return ((Long)executeCommand(this.commandObjects.hsetnx(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String hmset(String key, Map<String, String> hash) {
/* 1412 */     return executeCommand(this.commandObjects.hmset(key, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> hmget(String key, String... fields) {
/* 1417 */     return executeCommand(this.commandObjects.hmget(key, fields));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hset(byte[] key, byte[] field, byte[] value) {
/* 1422 */     return ((Long)executeCommand(this.commandObjects.hset(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hset(byte[] key, Map<byte[], byte[]> hash) {
/* 1427 */     return ((Long)executeCommand(this.commandObjects.hset(key, hash))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] hget(byte[] key, byte[] field) {
/* 1432 */     return executeCommand((CommandObject)this.commandObjects.hget(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hsetnx(byte[] key, byte[] field, byte[] value) {
/* 1437 */     return ((Long)executeCommand(this.commandObjects.hsetnx(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String hmset(byte[] key, Map<byte[], byte[]> hash) {
/* 1442 */     return executeCommand(this.commandObjects.hmset(key, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> hmget(byte[] key, byte[]... fields) {
/* 1447 */     return executeCommand(this.commandObjects.hmget(key, fields));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hincrBy(String key, String field, long value) {
/* 1452 */     return ((Long)executeCommand(this.commandObjects.hincrBy(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double hincrByFloat(String key, String field, double value) {
/* 1457 */     return ((Double)executeCommand(this.commandObjects.hincrByFloat(key, field, value))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hexists(String key, String field) {
/* 1462 */     return ((Boolean)executeCommand(this.commandObjects.hexists(key, field))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hdel(String key, String... field) {
/* 1467 */     return ((Long)executeCommand(this.commandObjects.hdel(key, field))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hlen(String key) {
/* 1472 */     return ((Long)executeCommand(this.commandObjects.hlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hincrBy(byte[] key, byte[] field, long value) {
/* 1477 */     return ((Long)executeCommand(this.commandObjects.hincrBy(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double hincrByFloat(byte[] key, byte[] field, double value) {
/* 1482 */     return ((Double)executeCommand(this.commandObjects.hincrByFloat(key, field, value))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hexists(byte[] key, byte[] field) {
/* 1487 */     return ((Boolean)executeCommand(this.commandObjects.hexists(key, field))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hdel(byte[] key, byte[]... field) {
/* 1492 */     return ((Long)executeCommand(this.commandObjects.hdel(key, field))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hlen(byte[] key) {
/* 1497 */     return ((Long)executeCommand(this.commandObjects.hlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> hkeys(String key) {
/* 1502 */     return executeCommand(this.commandObjects.hkeys(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> hvals(String key) {
/* 1507 */     return executeCommand(this.commandObjects.hvals(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> hgetAll(String key) {
/* 1512 */     return executeCommand(this.commandObjects.hgetAll(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> hkeys(byte[] key) {
/* 1517 */     return executeCommand(this.commandObjects.hkeys(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> hvals(byte[] key) {
/* 1522 */     return executeCommand(this.commandObjects.hvals(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<byte[], byte[]> hgetAll(byte[] key) {
/* 1527 */     return executeCommand(this.commandObjects.hgetAll(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String hrandfield(String key) {
/* 1532 */     return executeCommand(this.commandObjects.hrandfield(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> hrandfield(String key, long count) {
/* 1537 */     return executeCommand(this.commandObjects.hrandfield(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Map.Entry<String, String>> hrandfieldWithValues(String key, long count) {
/* 1542 */     return executeCommand(this.commandObjects.hrandfieldWithValues(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
/* 1547 */     return executeCommand(this.commandObjects.hscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hstrlen(String key, String field) {
/* 1552 */     return ((Long)executeCommand(this.commandObjects.hstrlen(key, field))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] hrandfield(byte[] key) {
/* 1557 */     return executeCommand((CommandObject)this.commandObjects.hrandfield(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> hrandfield(byte[] key, long count) {
/* 1562 */     return executeCommand(this.commandObjects.hrandfield(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Map.Entry<byte[], byte[]>> hrandfieldWithValues(byte[] key, long count) {
/* 1567 */     return executeCommand(this.commandObjects.hrandfieldWithValues(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
/* 1572 */     return executeCommand(this.commandObjects.hscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hstrlen(byte[] key, byte[] field) {
/* 1577 */     return ((Long)executeCommand(this.commandObjects.hstrlen(key, field))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long sadd(String key, String... members) {
/* 1584 */     return ((Long)executeCommand(this.commandObjects.sadd(key, members))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> smembers(String key) {
/* 1589 */     return executeCommand(this.commandObjects.smembers(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public long srem(String key, String... members) {
/* 1594 */     return ((Long)executeCommand(this.commandObjects.srem(key, members))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String spop(String key) {
/* 1599 */     return executeCommand(this.commandObjects.spop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> spop(String key, long count) {
/* 1604 */     return executeCommand(this.commandObjects.spop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long scard(String key) {
/* 1609 */     return ((Long)executeCommand(this.commandObjects.scard(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean sismember(String key, String member) {
/* 1614 */     return ((Boolean)executeCommand(this.commandObjects.sismember(key, member))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> smismember(String key, String... members) {
/* 1619 */     return executeCommand(this.commandObjects.smismember(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sadd(byte[] key, byte[]... members) {
/* 1624 */     return ((Long)executeCommand(this.commandObjects.sadd(key, members))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> smembers(byte[] key) {
/* 1629 */     return executeCommand(this.commandObjects.smembers(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public long srem(byte[] key, byte[]... members) {
/* 1634 */     return ((Long)executeCommand(this.commandObjects.srem(key, members))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] spop(byte[] key) {
/* 1639 */     return executeCommand((CommandObject)this.commandObjects.spop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> spop(byte[] key, long count) {
/* 1644 */     return executeCommand(this.commandObjects.spop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long scard(byte[] key) {
/* 1649 */     return ((Long)executeCommand(this.commandObjects.scard(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean sismember(byte[] key, byte[] member) {
/* 1654 */     return ((Boolean)executeCommand(this.commandObjects.sismember(key, member))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> smismember(byte[] key, byte[]... members) {
/* 1659 */     return executeCommand(this.commandObjects.smismember(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public String srandmember(String key) {
/* 1664 */     return executeCommand(this.commandObjects.srandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> srandmember(String key, int count) {
/* 1669 */     return executeCommand(this.commandObjects.srandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
/* 1674 */     return executeCommand(this.commandObjects.sscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] srandmember(byte[] key) {
/* 1679 */     return executeCommand((CommandObject)this.commandObjects.srandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> srandmember(byte[] key, int count) {
/* 1684 */     return executeCommand(this.commandObjects.srandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
/* 1689 */     return executeCommand(this.commandObjects.sscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> sdiff(String... keys) {
/* 1694 */     return executeCommand(this.commandObjects.sdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sdiffstore(String dstkey, String... keys) {
/* 1699 */     return ((Long)executeCommand(this.commandObjects.sdiffstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> sinter(String... keys) {
/* 1704 */     return executeCommand(this.commandObjects.sinter(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sinterstore(String dstkey, String... keys) {
/* 1709 */     return ((Long)executeCommand(this.commandObjects.sinterstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long sintercard(String... keys) {
/* 1714 */     return ((Long)executeCommand(this.commandObjects.sintercard(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long sintercard(int limit, String... keys) {
/* 1719 */     return ((Long)executeCommand(this.commandObjects.sintercard(limit, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> sunion(String... keys) {
/* 1724 */     return executeCommand(this.commandObjects.sunion(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sunionstore(String dstkey, String... keys) {
/* 1729 */     return ((Long)executeCommand(this.commandObjects.sunionstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long smove(String srckey, String dstkey, String member) {
/* 1734 */     return ((Long)executeCommand(this.commandObjects.smove(srckey, dstkey, member))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> sdiff(byte[]... keys) {
/* 1739 */     return executeCommand(this.commandObjects.sdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sdiffstore(byte[] dstkey, byte[]... keys) {
/* 1744 */     return ((Long)executeCommand(this.commandObjects.sdiffstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> sinter(byte[]... keys) {
/* 1749 */     return executeCommand(this.commandObjects.sinter(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sinterstore(byte[] dstkey, byte[]... keys) {
/* 1754 */     return ((Long)executeCommand(this.commandObjects.sinterstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long sintercard(byte[]... keys) {
/* 1759 */     return ((Long)executeCommand(this.commandObjects.sintercard(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long sintercard(int limit, byte[]... keys) {
/* 1764 */     return ((Long)executeCommand(this.commandObjects.sintercard(limit, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> sunion(byte[]... keys) {
/* 1769 */     return executeCommand(this.commandObjects.sunion(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long sunionstore(byte[] dstkey, byte[]... keys) {
/* 1774 */     return ((Long)executeCommand(this.commandObjects.sunionstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long smove(byte[] srckey, byte[] dstkey, byte[] member) {
/* 1779 */     return ((Long)executeCommand(this.commandObjects.smove(srckey, dstkey, member))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zadd(String key, double score, String member) {
/* 1786 */     return ((Long)executeCommand(this.commandObjects.zadd(key, score, member))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(String key, double score, String member, ZAddParams params) {
/* 1791 */     return ((Long)executeCommand(this.commandObjects.zadd(key, score, member, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(String key, Map<String, Double> scoreMembers) {
/* 1796 */     return ((Long)executeCommand(this.commandObjects.zadd(key, scoreMembers))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
/* 1801 */     return ((Long)executeCommand(this.commandObjects.zadd(key, scoreMembers, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zaddIncr(String key, double score, String member, ZAddParams params) {
/* 1806 */     return executeCommand(this.commandObjects.zaddIncr(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, double score, byte[] member) {
/* 1811 */     return ((Long)executeCommand(this.commandObjects.zadd(key, score, member))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
/* 1816 */     return ((Long)executeCommand(this.commandObjects.zadd(key, score, member, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
/* 1821 */     return ((Long)executeCommand(this.commandObjects.zadd(key, scoreMembers))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
/* 1826 */     return ((Long)executeCommand(this.commandObjects.zadd(key, scoreMembers, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zaddIncr(byte[] key, double score, byte[] member, ZAddParams params) {
/* 1831 */     return executeCommand(this.commandObjects.zaddIncr(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zrem(String key, String... members) {
/* 1836 */     return ((Long)executeCommand(this.commandObjects.zrem(key, members))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double zincrby(String key, double increment, String member) {
/* 1841 */     return ((Double)executeCommand(this.commandObjects.zincrby(key, increment, member))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zincrby(String key, double increment, String member, ZIncrByParams params) {
/* 1846 */     return executeCommand(this.commandObjects.zincrby(key, increment, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long zrank(String key, String member) {
/* 1851 */     return executeCommand(this.commandObjects.zrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long zrevrank(String key, String member) {
/* 1856 */     return executeCommand(this.commandObjects.zrevrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<Long, Double> zrankWithScore(String key, String member) {
/* 1861 */     return executeCommand(this.commandObjects.zrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<Long, Double> zrevrankWithScore(String key, String member) {
/* 1866 */     return executeCommand(this.commandObjects.zrevrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zrem(byte[] key, byte[]... members) {
/* 1871 */     return ((Long)executeCommand(this.commandObjects.zrem(key, members))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double zincrby(byte[] key, double increment, byte[] member) {
/* 1876 */     return ((Double)executeCommand(this.commandObjects.zincrby(key, increment, member))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
/* 1881 */     return executeCommand(this.commandObjects.zincrby(key, increment, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long zrank(byte[] key, byte[] member) {
/* 1886 */     return executeCommand(this.commandObjects.zrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long zrevrank(byte[] key, byte[] member) {
/* 1891 */     return executeCommand(this.commandObjects.zrevrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<Long, Double> zrankWithScore(byte[] key, byte[] member) {
/* 1896 */     return executeCommand(this.commandObjects.zrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<Long, Double> zrevrankWithScore(byte[] key, byte[] member) {
/* 1901 */     return executeCommand(this.commandObjects.zrevrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public String zrandmember(String key) {
/* 1906 */     return executeCommand(this.commandObjects.zrandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrandmember(String key, long count) {
/* 1911 */     return executeCommand(this.commandObjects.zrandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrandmemberWithScores(String key, long count) {
/* 1916 */     return executeCommand(this.commandObjects.zrandmemberWithScores(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcard(String key) {
/* 1921 */     return ((Long)executeCommand(this.commandObjects.zcard(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zscore(String key, String member) {
/* 1926 */     return executeCommand(this.commandObjects.zscore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Double> zmscore(String key, String... members) {
/* 1931 */     return executeCommand(this.commandObjects.zmscore(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] zrandmember(byte[] key) {
/* 1936 */     return executeCommand((CommandObject)this.commandObjects.zrandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrandmember(byte[] key, long count) {
/* 1941 */     return executeCommand(this.commandObjects.zrandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrandmemberWithScores(byte[] key, long count) {
/* 1946 */     return executeCommand(this.commandObjects.zrandmemberWithScores(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcard(byte[] key) {
/* 1951 */     return ((Long)executeCommand(this.commandObjects.zcard(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zscore(byte[] key, byte[] member) {
/* 1956 */     return executeCommand(this.commandObjects.zscore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Double> zmscore(byte[] key, byte[]... members) {
/* 1961 */     return executeCommand(this.commandObjects.zmscore(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmax(String key) {
/* 1966 */     return executeCommand(this.commandObjects.zpopmax(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmax(String key, int count) {
/* 1971 */     return executeCommand(this.commandObjects.zpopmax(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmin(String key) {
/* 1976 */     return executeCommand(this.commandObjects.zpopmin(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmin(String key, int count) {
/* 1981 */     return executeCommand(this.commandObjects.zpopmin(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(String key, double min, double max) {
/* 1986 */     return ((Long)executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(String key, String min, String max) {
/* 1991 */     return ((Long)executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmax(byte[] key) {
/* 1996 */     return executeCommand(this.commandObjects.zpopmax(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmax(byte[] key, int count) {
/* 2001 */     return executeCommand(this.commandObjects.zpopmax(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmin(byte[] key) {
/* 2006 */     return executeCommand(this.commandObjects.zpopmin(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmin(byte[] key, int count) {
/* 2011 */     return executeCommand(this.commandObjects.zpopmin(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(byte[] key, double min, double max) {
/* 2016 */     return ((Long)executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(byte[] key, byte[] min, byte[] max) {
/* 2021 */     return ((Long)executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrange(String key, long start, long stop) {
/* 2026 */     return executeCommand(this.commandObjects.zrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrange(String key, long start, long stop) {
/* 2031 */     return executeCommand(this.commandObjects.zrevrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(String key, long start, long stop) {
/* 2036 */     return executeCommand(this.commandObjects.zrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeWithScores(String key, long start, long stop) {
/* 2041 */     return executeCommand(this.commandObjects.zrevrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrange(String key, ZRangeParams zRangeParams) {
/* 2046 */     return executeCommand(this.commandObjects.zrange(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(String key, ZRangeParams zRangeParams) {
/* 2051 */     return executeCommand(this.commandObjects.zrangeWithScores(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zrangestore(String dest, String src, ZRangeParams zRangeParams) {
/* 2056 */     return ((Long)executeCommand(this.commandObjects.zrangestore(dest, src, zRangeParams))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, double min, double max) {
/* 2061 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, String min, String max) {
/* 2066 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, double max, double min) {
/* 2071 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, double min, double max, int offset, int count) {
/* 2076 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, String max, String min) {
/* 2081 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, String min, String max, int offset, int count) {
/* 2086 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
/* 2091 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
/* 2096 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
/* 2101 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
/* 2106 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
/* 2111 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
/* 2116 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
/* 2121 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
/* 2126 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
/* 2131 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
/* 2136 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrange(byte[] key, long start, long stop) {
/* 2141 */     return executeCommand(this.commandObjects.zrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrange(byte[] key, long start, long stop) {
/* 2146 */     return executeCommand(this.commandObjects.zrevrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(byte[] key, long start, long stop) {
/* 2151 */     return executeCommand(this.commandObjects.zrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeWithScores(byte[] key, long start, long stop) {
/* 2156 */     return executeCommand(this.commandObjects.zrevrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrange(byte[] key, ZRangeParams zRangeParams) {
/* 2161 */     return executeCommand(this.commandObjects.zrange(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(byte[] key, ZRangeParams zRangeParams) {
/* 2166 */     return executeCommand(this.commandObjects.zrangeWithScores(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zrangestore(byte[] dest, byte[] src, ZRangeParams zRangeParams) {
/* 2171 */     return ((Long)executeCommand(this.commandObjects.zrangestore(dest, src, zRangeParams))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, double min, double max) {
/* 2176 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 2181 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
/* 2186 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
/* 2191 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
/* 2196 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2201 */     return executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
/* 2206 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
/* 2211 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
/* 2216 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
/* 2221 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 2226 */     return executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
/* 2231 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
/* 2236 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2241 */     return executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
/* 2246 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 2251 */     return executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByRank(String key, long start, long stop) {
/* 2256 */     return ((Long)executeCommand(this.commandObjects.zremrangeByRank(key, start, stop))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(String key, double min, double max) {
/* 2261 */     return ((Long)executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(String key, String min, String max) {
/* 2266 */     return ((Long)executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByRank(byte[] key, long start, long stop) {
/* 2271 */     return ((Long)executeCommand(this.commandObjects.zremrangeByRank(key, start, stop))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(byte[] key, double min, double max) {
/* 2276 */     return ((Long)executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 2281 */     return ((Long)executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zlexcount(String key, String min, String max) {
/* 2286 */     return ((Long)executeCommand(this.commandObjects.zlexcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByLex(String key, String min, String max) {
/* 2291 */     return executeCommand(this.commandObjects.zrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByLex(String key, String min, String max, int offset, int count) {
/* 2296 */     return executeCommand(this.commandObjects.zrangeByLex(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByLex(String key, String max, String min) {
/* 2301 */     return executeCommand(this.commandObjects.zrevrangeByLex(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
/* 2306 */     return executeCommand(this.commandObjects.zrevrangeByLex(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByLex(String key, String min, String max) {
/* 2311 */     return ((Long)executeCommand(this.commandObjects.zremrangeByLex(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zlexcount(byte[] key, byte[] min, byte[] max) {
/* 2316 */     return ((Long)executeCommand(this.commandObjects.zlexcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 2321 */     return executeCommand(this.commandObjects.zrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2326 */     return executeCommand(this.commandObjects.zrangeByLex(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
/* 2331 */     return executeCommand(this.commandObjects.zrevrangeByLex(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 2336 */     return executeCommand(this.commandObjects.zrevrangeByLex(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 2341 */     return ((Long)executeCommand(this.commandObjects.zremrangeByLex(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
/* 2346 */     return executeCommand(this.commandObjects.zscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
/* 2351 */     return executeCommand(this.commandObjects.zscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, Tuple> bzpopmax(double timeout, String... keys) {
/* 2356 */     return executeCommand(this.commandObjects.bzpopmax(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, Tuple> bzpopmin(double timeout, String... keys) {
/* 2361 */     return executeCommand(this.commandObjects.bzpopmin(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], Tuple> bzpopmax(double timeout, byte[]... keys) {
/* 2366 */     return executeCommand(this.commandObjects.bzpopmax(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], Tuple> bzpopmin(double timeout, byte[]... keys) {
/* 2371 */     return executeCommand(this.commandObjects.bzpopmin(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zdiff(String... keys) {
/* 2376 */     return executeCommand(this.commandObjects.zdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zdiffWithScores(String... keys) {
/* 2381 */     return executeCommand(this.commandObjects.zdiffWithScores(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long zdiffStore(String dstkey, String... keys) {
/* 2387 */     return ((Long)executeCommand(this.commandObjects.zdiffStore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zdiffstore(String dstkey, String... keys) {
/* 2392 */     return ((Long)executeCommand(this.commandObjects.zdiffstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zdiff(byte[]... keys) {
/* 2397 */     return executeCommand(this.commandObjects.zdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zdiffWithScores(byte[]... keys) {
/* 2402 */     return executeCommand(this.commandObjects.zdiffWithScores(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long zdiffStore(byte[] dstkey, byte[]... keys) {
/* 2408 */     return ((Long)executeCommand(this.commandObjects.zdiffStore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zdiffstore(byte[] dstkey, byte[]... keys) {
/* 2413 */     return ((Long)executeCommand(this.commandObjects.zdiffstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zinterstore(String dstkey, String... sets) {
/* 2418 */     return ((Long)executeCommand(this.commandObjects.zinterstore(dstkey, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zinterstore(String dstkey, ZParams params, String... sets) {
/* 2423 */     return ((Long)executeCommand(this.commandObjects.zinterstore(dstkey, params, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zinter(ZParams params, String... keys) {
/* 2428 */     return executeCommand(this.commandObjects.zinter(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zinterWithScores(ZParams params, String... keys) {
/* 2433 */     return executeCommand(this.commandObjects.zinterWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zinterstore(byte[] dstkey, byte[]... sets) {
/* 2438 */     return ((Long)executeCommand(this.commandObjects.zinterstore(dstkey, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 2443 */     return ((Long)executeCommand(this.commandObjects.zinterstore(dstkey, params, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(byte[]... keys) {
/* 2448 */     return ((Long)executeCommand(this.commandObjects.zintercard(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(long limit, byte[]... keys) {
/* 2453 */     return ((Long)executeCommand(this.commandObjects.zintercard(limit, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(String... keys) {
/* 2458 */     return ((Long)executeCommand(this.commandObjects.zintercard(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(long limit, String... keys) {
/* 2463 */     return ((Long)executeCommand(this.commandObjects.zintercard(limit, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zinter(ZParams params, byte[]... keys) {
/* 2468 */     return executeCommand(this.commandObjects.zinter(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zinterWithScores(ZParams params, byte[]... keys) {
/* 2473 */     return executeCommand(this.commandObjects.zinterWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zunion(ZParams params, String... keys) {
/* 2478 */     return executeCommand(this.commandObjects.zunion(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zunionWithScores(ZParams params, String... keys) {
/* 2483 */     return executeCommand(this.commandObjects.zunionWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zunionstore(String dstkey, String... sets) {
/* 2488 */     return ((Long)executeCommand(this.commandObjects.zunionstore(dstkey, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zunionstore(String dstkey, ZParams params, String... sets) {
/* 2493 */     return ((Long)executeCommand(this.commandObjects.zunionstore(dstkey, params, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zunion(ZParams params, byte[]... keys) {
/* 2498 */     return executeCommand(this.commandObjects.zunion(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zunionWithScores(ZParams params, byte[]... keys) {
/* 2503 */     return executeCommand(this.commandObjects.zunionWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zunionstore(byte[] dstkey, byte[]... sets) {
/* 2508 */     return ((Long)executeCommand(this.commandObjects.zunionstore(dstkey, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 2513 */     return ((Long)executeCommand(this.commandObjects.zunionstore(dstkey, params, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> zmpop(SortedSetOption option, String... keys) {
/* 2518 */     return executeCommand(this.commandObjects.zmpop(option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> zmpop(SortedSetOption option, int count, String... keys) {
/* 2523 */     return executeCommand(this.commandObjects.zmpop(option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> bzmpop(double timeout, SortedSetOption option, String... keys) {
/* 2528 */     return executeCommand(this.commandObjects.bzmpop(timeout, option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> bzmpop(double timeout, SortedSetOption option, int count, String... keys) {
/* 2533 */     return executeCommand(this.commandObjects.bzmpop(timeout, option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption option, byte[]... keys) {
/* 2538 */     return executeCommand(this.commandObjects.zmpop(option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption option, int count, byte[]... keys) {
/* 2543 */     return executeCommand(this.commandObjects.zmpop(option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> bzmpop(double timeout, SortedSetOption option, byte[]... keys) {
/* 2548 */     return executeCommand(this.commandObjects.bzmpop(timeout, option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> bzmpop(double timeout, SortedSetOption option, int count, byte[]... keys) {
/* 2553 */     return executeCommand(this.commandObjects.bzmpop(timeout, option, count, keys));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long geoadd(String key, double longitude, double latitude, String member) {
/* 2560 */     return ((Long)executeCommand(this.commandObjects.geoadd(key, longitude, latitude, member))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 2565 */     return ((Long)executeCommand(this.commandObjects.geoadd(key, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(String key, GeoAddParams params, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 2570 */     return ((Long)executeCommand(this.commandObjects.geoadd(key, params, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double geodist(String key, String member1, String member2) {
/* 2575 */     return executeCommand(this.commandObjects.geodist(key, member1, member2));
/*      */   }
/*      */ 
/*      */   
/*      */   public Double geodist(String key, String member1, String member2, GeoUnit unit) {
/* 2580 */     return executeCommand(this.commandObjects.geodist(key, member1, member2, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> geohash(String key, String... members) {
/* 2585 */     return executeCommand(this.commandObjects.geohash(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoCoordinate> geopos(String key, String... members) {
/* 2590 */     return executeCommand(this.commandObjects.geopos(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
/* 2595 */     return ((Long)executeCommand(this.commandObjects.geoadd(key, longitude, latitude, member))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 2600 */     return ((Long)executeCommand(this.commandObjects.geoadd(key, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 2605 */     return ((Long)executeCommand(this.commandObjects.geoadd(key, params, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double geodist(byte[] key, byte[] member1, byte[] member2) {
/* 2610 */     return executeCommand(this.commandObjects.geodist(key, member1, member2));
/*      */   }
/*      */ 
/*      */   
/*      */   public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
/* 2615 */     return executeCommand(this.commandObjects.geodist(key, member1, member2, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> geohash(byte[] key, byte[]... members) {
/* 2620 */     return executeCommand(this.commandObjects.geohash(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
/* 2625 */     return executeCommand(this.commandObjects.geopos(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2630 */     return executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2635 */     return executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2640 */     return executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2645 */     return executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
/* 2650 */     return executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit) {
/* 2655 */     return executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2660 */     return executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2665 */     return executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public long georadiusStore(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2670 */     return ((Long)executeCommand(this.commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long georadiusByMemberStore(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2675 */     return ((Long)executeCommand(this.commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, String member, double radius, GeoUnit unit) {
/* 2680 */     return executeCommand(this.commandObjects.geosearch(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2685 */     return executeCommand(this.commandObjects.geosearch(key, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, String member, double width, double height, GeoUnit unit) {
/* 2690 */     return executeCommand(this.commandObjects.geosearch(key, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2695 */     return executeCommand(this.commandObjects.geosearch(key, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, GeoSearchParam params) {
/* 2700 */     return executeCommand(this.commandObjects.geosearch(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, String member, double radius, GeoUnit unit) {
/* 2705 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, member, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2710 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, coord, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, String member, double width, double height, GeoUnit unit) {
/* 2715 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, member, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2720 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, coord, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, GeoSearchParam params) {
/* 2725 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStoreStoreDist(String dest, String src, GeoSearchParam params) {
/* 2730 */     return ((Long)executeCommand(this.commandObjects.geosearchStoreStoreDist(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2735 */     return executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2740 */     return executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2745 */     return executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2750 */     return executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 2755 */     return executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 2760 */     return executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2765 */     return executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2770 */     return executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public long georadiusStore(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2775 */     return ((Long)executeCommand(this.commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long georadiusByMemberStore(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2780 */     return ((Long)executeCommand(this.commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 2785 */     return executeCommand(this.commandObjects.geosearch(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2790 */     return executeCommand(this.commandObjects.geosearch(key, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double width, double height, GeoUnit unit) {
/* 2795 */     return executeCommand(this.commandObjects.geosearch(key, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2800 */     return executeCommand(this.commandObjects.geosearch(key, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, GeoSearchParam params) {
/* 2805 */     return executeCommand(this.commandObjects.geosearch(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, byte[] member, double radius, GeoUnit unit) {
/* 2810 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, member, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2815 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, coord, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, byte[] member, double width, double height, GeoUnit unit) {
/* 2820 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, member, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2825 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, coord, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, GeoSearchParam params) {
/* 2830 */     return ((Long)executeCommand(this.commandObjects.geosearchStore(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStoreStoreDist(byte[] dest, byte[] src, GeoSearchParam params) {
/* 2835 */     return ((Long)executeCommand(this.commandObjects.geosearchStoreStoreDist(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long pfadd(String key, String... elements) {
/* 2842 */     return ((Long)executeCommand(this.commandObjects.pfadd(key, elements))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String pfmerge(String destkey, String... sourcekeys) {
/* 2847 */     return executeCommand(this.commandObjects.pfmerge(destkey, sourcekeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(String key) {
/* 2852 */     return ((Long)executeCommand(this.commandObjects.pfcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(String... keys) {
/* 2857 */     return ((Long)executeCommand(this.commandObjects.pfcount(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfadd(byte[] key, byte[]... elements) {
/* 2862 */     return ((Long)executeCommand(this.commandObjects.pfadd(key, elements))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
/* 2867 */     return executeCommand(this.commandObjects.pfmerge(destkey, sourcekeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(byte[] key) {
/* 2872 */     return ((Long)executeCommand(this.commandObjects.pfcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(byte[]... keys) {
/* 2877 */     return ((Long)executeCommand(this.commandObjects.pfcount(keys))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public StreamEntryID xadd(String key, StreamEntryID id, Map<String, String> hash) {
/* 2884 */     return executeCommand(this.commandObjects.xadd(key, id, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamEntryID xadd(String key, XAddParams params, Map<String, String> hash) {
/* 2889 */     return executeCommand(this.commandObjects.xadd(key, params, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xlen(String key) {
/* 2894 */     return ((Long)executeCommand(this.commandObjects.xlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end) {
/* 2899 */     return executeCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end, int count) {
/* 2904 */     return executeCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start) {
/* 2909 */     return executeCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count) {
/* 2914 */     return executeCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, String start, String end) {
/* 2919 */     return executeCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, String start, String end, int count) {
/* 2924 */     return executeCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, String end, String start) {
/* 2929 */     return executeCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, String end, String start, int count) {
/* 2934 */     return executeCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xack(String key, String group, StreamEntryID... ids) {
/* 2939 */     return ((Long)executeCommand(this.commandObjects.xack(key, group, ids))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String xgroupCreate(String key, String groupName, StreamEntryID id, boolean makeStream) {
/* 2944 */     return executeCommand(this.commandObjects.xgroupCreate(key, groupName, id, makeStream));
/*      */   }
/*      */ 
/*      */   
/*      */   public String xgroupSetID(String key, String groupName, StreamEntryID id) {
/* 2949 */     return executeCommand(this.commandObjects.xgroupSetID(key, groupName, id));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDestroy(String key, String groupName) {
/* 2954 */     return ((Long)executeCommand(this.commandObjects.xgroupDestroy(key, groupName))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean xgroupCreateConsumer(String key, String groupName, String consumerName) {
/* 2959 */     return ((Boolean)executeCommand(this.commandObjects.xgroupCreateConsumer(key, groupName, consumerName))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDelConsumer(String key, String groupName, String consumerName) {
/* 2964 */     return ((Long)executeCommand(this.commandObjects.xgroupDelConsumer(key, groupName, consumerName))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamPendingSummary xpending(String key, String groupName) {
/* 2969 */     return executeCommand(this.commandObjects.xpending(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamPendingEntry> xpending(String key, String groupName, XPendingParams params) {
/* 2974 */     return executeCommand(this.commandObjects.xpending(key, groupName, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xdel(String key, StreamEntryID... ids) {
/* 2979 */     return ((Long)executeCommand(this.commandObjects.xdel(key, ids))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(String key, long maxLen, boolean approximate) {
/* 2984 */     return ((Long)executeCommand(this.commandObjects.xtrim(key, maxLen, approximate))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(String key, XTrimParams params) {
/* 2989 */     return ((Long)executeCommand(this.commandObjects.xtrim(key, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xclaim(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 2994 */     return executeCommand(this.commandObjects.xclaim(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntryID> xclaimJustId(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 2999 */     return executeCommand(this.commandObjects.xclaimJustId(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map.Entry<StreamEntryID, List<StreamEntry>> xautoclaim(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 3004 */     return executeCommand(this.commandObjects.xautoclaim(key, group, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map.Entry<StreamEntryID, List<StreamEntryID>> xautoclaimJustId(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 3009 */     return executeCommand(this.commandObjects.xautoclaimJustId(key, group, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamInfo xinfoStream(String key) {
/* 3014 */     return executeCommand(this.commandObjects.xinfoStream(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamFullInfo xinfoStreamFull(String key) {
/* 3019 */     return executeCommand(this.commandObjects.xinfoStreamFull(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamFullInfo xinfoStreamFull(String key, int count) {
/* 3024 */     return executeCommand(this.commandObjects.xinfoStreamFull(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamGroupInfo> xinfoGroups(String key) {
/* 3029 */     return executeCommand(this.commandObjects.xinfoGroups(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamConsumersInfo> xinfoConsumers(String key, String group) {
/* 3034 */     return executeCommand(this.commandObjects.xinfoConsumers(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamConsumerInfo> xinfoConsumers2(String key, String group) {
/* 3039 */     return executeCommand(this.commandObjects.xinfoConsumers2(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Map.Entry<String, List<StreamEntry>>> xread(XReadParams xReadParams, Map<String, StreamEntryID> streams) {
/* 3044 */     return executeCommand(this.commandObjects.xread(xReadParams, streams));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Map.Entry<String, List<StreamEntry>>> xreadGroup(String groupName, String consumer, XReadGroupParams xReadGroupParams, Map<String, StreamEntryID> streams) {
/* 3050 */     return executeCommand(this.commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] xadd(byte[] key, XAddParams params, Map<byte[], byte[]> hash) {
/* 3055 */     return executeCommand((CommandObject)this.commandObjects.xadd(key, params, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xlen(byte[] key) {
/* 3060 */     return ((Long)executeCommand(this.commandObjects.xlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrange(byte[] key, byte[] start, byte[] end) {
/* 3065 */     return executeCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrange(byte[] key, byte[] start, byte[] end, int count) {
/* 3070 */     return executeCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrevrange(byte[] key, byte[] end, byte[] start) {
/* 3075 */     return executeCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrevrange(byte[] key, byte[] end, byte[] start, int count) {
/* 3080 */     return executeCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xack(byte[] key, byte[] group, byte[]... ids) {
/* 3085 */     return ((Long)executeCommand(this.commandObjects.xack(key, group, ids))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String xgroupCreate(byte[] key, byte[] groupName, byte[] id, boolean makeStream) {
/* 3090 */     return executeCommand(this.commandObjects.xgroupCreate(key, groupName, id, makeStream));
/*      */   }
/*      */ 
/*      */   
/*      */   public String xgroupSetID(byte[] key, byte[] groupName, byte[] id) {
/* 3095 */     return executeCommand(this.commandObjects.xgroupSetID(key, groupName, id));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDestroy(byte[] key, byte[] groupName) {
/* 3100 */     return ((Long)executeCommand(this.commandObjects.xgroupDestroy(key, groupName))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean xgroupCreateConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 3105 */     return ((Boolean)executeCommand(this.commandObjects.xgroupCreateConsumer(key, groupName, consumerName))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDelConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 3110 */     return ((Long)executeCommand(this.commandObjects.xgroupDelConsumer(key, groupName, consumerName))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xdel(byte[] key, byte[]... ids) {
/* 3115 */     return ((Long)executeCommand(this.commandObjects.xdel(key, ids))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(byte[] key, long maxLen, boolean approximateLength) {
/* 3120 */     return ((Long)executeCommand(this.commandObjects.xtrim(key, maxLen, approximateLength))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(byte[] key, XTrimParams params) {
/* 3125 */     return ((Long)executeCommand(this.commandObjects.xtrim(key, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xpending(byte[] key, byte[] groupName) {
/* 3130 */     return executeCommand(this.commandObjects.xpending(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xpending(byte[] key, byte[] groupName, XPendingParams params) {
/* 3135 */     return executeCommand(this.commandObjects.xpending(key, groupName, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> xclaim(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 3140 */     return executeCommand(this.commandObjects.xclaim(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> xclaimJustId(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 3145 */     return executeCommand(this.commandObjects.xclaimJustId(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xautoclaim(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 3150 */     return executeCommand(this.commandObjects.xautoclaim(key, groupName, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xautoclaimJustId(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 3155 */     return executeCommand(this.commandObjects.xautoclaimJustId(key, groupName, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xinfoStream(byte[] key) {
/* 3160 */     return executeCommand(this.commandObjects.xinfoStream(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xinfoStreamFull(byte[] key) {
/* 3165 */     return executeCommand(this.commandObjects.xinfoStreamFull(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xinfoStreamFull(byte[] key, int count) {
/* 3170 */     return executeCommand(this.commandObjects.xinfoStreamFull(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xinfoGroups(byte[] key) {
/* 3175 */     return executeCommand(this.commandObjects.xinfoGroups(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xinfoConsumers(byte[] key, byte[] group) {
/* 3180 */     return executeCommand(this.commandObjects.xinfoConsumers(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xread(XReadParams xReadParams, Map.Entry<byte[], byte[]>... streams) {
/* 3185 */     return executeCommand(this.commandObjects.xread(xReadParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xreadGroup(byte[] groupName, byte[] consumer, XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
/* 3190 */     return executeCommand(this.commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object eval(String script) {
/* 3197 */     return executeCommand(this.commandObjects.eval(script));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(String script, int keyCount, String... params) {
/* 3202 */     return executeCommand(this.commandObjects.eval(script, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(String script, List<String> keys, List<String> args) {
/* 3207 */     return executeCommand(this.commandObjects.eval(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalReadonly(String script, List<String> keys, List<String> args) {
/* 3212 */     return executeCommand(this.commandObjects.evalReadonly(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(String sha1) {
/* 3217 */     return executeCommand(this.commandObjects.evalsha(sha1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(String sha1, int keyCount, String... params) {
/* 3222 */     return executeCommand(this.commandObjects.evalsha(sha1, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(String sha1, List<String> keys, List<String> args) {
/* 3227 */     return executeCommand(this.commandObjects.evalsha(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalshaReadonly(String sha1, List<String> keys, List<String> args) {
/* 3232 */     return executeCommand(this.commandObjects.evalshaReadonly(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(byte[] script) {
/* 3237 */     return executeCommand(this.commandObjects.eval(script));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(byte[] script, int keyCount, byte[]... params) {
/* 3242 */     return executeCommand(this.commandObjects.eval(script, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 3247 */     return executeCommand(this.commandObjects.eval(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalReadonly(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 3252 */     return executeCommand(this.commandObjects.evalReadonly(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(byte[] sha1) {
/* 3257 */     return executeCommand(this.commandObjects.evalsha(sha1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
/* 3262 */     return executeCommand(this.commandObjects.evalsha(sha1, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 3267 */     return executeCommand(this.commandObjects.evalsha(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalshaReadonly(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 3272 */     return executeCommand(this.commandObjects.evalshaReadonly(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcall(String name, List<String> keys, List<String> args) {
/* 3277 */     return executeCommand(this.commandObjects.fcall(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcallReadonly(String name, List<String> keys, List<String> args) {
/* 3282 */     return executeCommand(this.commandObjects.fcallReadonly(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionDelete(String libraryName) {
/* 3287 */     return checkAndBroadcastCommand(this.commandObjects.functionDelete(libraryName));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionFlush() {
/* 3292 */     return checkAndBroadcastCommand(this.commandObjects.functionFlush());
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionFlush(FlushMode mode) {
/* 3297 */     return checkAndBroadcastCommand(this.commandObjects.functionFlush(mode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionKill() {
/* 3302 */     return checkAndBroadcastCommand(this.commandObjects.functionKill());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> functionList() {
/* 3307 */     return executeCommand(this.commandObjects.functionList());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> functionList(String libraryNamePattern) {
/* 3312 */     return executeCommand(this.commandObjects.functionList(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> functionListWithCode() {
/* 3317 */     return executeCommand(this.commandObjects.functionListWithCode());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> functionListWithCode(String libraryNamePattern) {
/* 3322 */     return executeCommand(this.commandObjects.functionListWithCode(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoad(String functionCode) {
/* 3327 */     return checkAndBroadcastCommand(this.commandObjects.functionLoad(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoadReplace(String functionCode) {
/* 3332 */     return checkAndBroadcastCommand(this.commandObjects.functionLoadReplace(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public FunctionStats functionStats() {
/* 3337 */     return executeCommand(this.commandObjects.functionStats());
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcall(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 3342 */     return executeCommand(this.commandObjects.fcall(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcallReadonly(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 3347 */     return executeCommand(this.commandObjects.fcallReadonly(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionDelete(byte[] libraryName) {
/* 3352 */     return checkAndBroadcastCommand(this.commandObjects.functionDelete(libraryName));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] functionDump() {
/* 3357 */     return executeCommand((CommandObject)this.commandObjects.functionDump());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionListBinary() {
/* 3362 */     return executeCommand(this.commandObjects.functionListBinary());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionList(byte[] libraryNamePattern) {
/* 3367 */     return executeCommand(this.commandObjects.functionList(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionListWithCodeBinary() {
/* 3372 */     return executeCommand(this.commandObjects.functionListWithCodeBinary());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionListWithCode(byte[] libraryNamePattern) {
/* 3377 */     return executeCommand(this.commandObjects.functionListWithCode(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoad(byte[] functionCode) {
/* 3382 */     return checkAndBroadcastCommand(this.commandObjects.functionLoad(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoadReplace(byte[] functionCode) {
/* 3387 */     return checkAndBroadcastCommand(this.commandObjects.functionLoadReplace(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionRestore(byte[] serializedValue) {
/* 3392 */     return checkAndBroadcastCommand(this.commandObjects.functionRestore(serializedValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionRestore(byte[] serializedValue, FunctionRestorePolicy policy) {
/* 3397 */     return checkAndBroadcastCommand(this.commandObjects.functionRestore(serializedValue, policy));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object functionStatsBinary() {
/* 3402 */     return executeCommand(this.commandObjects.functionStatsBinary());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long objectRefcount(String key) {
/* 3409 */     return executeCommand(this.commandObjects.objectRefcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String objectEncoding(String key) {
/* 3414 */     return executeCommand(this.commandObjects.objectEncoding(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectIdletime(String key) {
/* 3419 */     return executeCommand(this.commandObjects.objectIdletime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectFreq(String key) {
/* 3424 */     return executeCommand(this.commandObjects.objectFreq(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectRefcount(byte[] key) {
/* 3429 */     return executeCommand(this.commandObjects.objectRefcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] objectEncoding(byte[] key) {
/* 3434 */     return executeCommand((CommandObject)this.commandObjects.objectEncoding(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectIdletime(byte[] key) {
/* 3439 */     return executeCommand(this.commandObjects.objectIdletime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectFreq(byte[] key) {
/* 3444 */     return executeCommand(this.commandObjects.objectFreq(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, String key, int timeout) {
/* 3449 */     return executeCommand(this.commandObjects.migrate(host, port, key, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, int timeout, MigrateParams params, String... keys) {
/* 3454 */     return executeCommand(this.commandObjects.migrate(host, port, timeout, params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, byte[] key, int timeout) {
/* 3459 */     return executeCommand(this.commandObjects.migrate(host, port, key, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, int timeout, MigrateParams params, byte[]... keys) {
/* 3464 */     return executeCommand(this.commandObjects.migrate(host, port, timeout, params, keys));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long waitReplicas(String sampleKey, int replicas, long timeout) {
/* 3471 */     return ((Long)executeCommand(this.commandObjects.waitReplicas(sampleKey, replicas, timeout))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long waitReplicas(byte[] sampleKey, int replicas, long timeout) {
/* 3476 */     return ((Long)executeCommand(this.commandObjects.waitReplicas(sampleKey, replicas, timeout))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<Long, Long> waitAOF(String sampleKey, long numLocal, long numReplicas, long timeout) {
/* 3481 */     return executeCommand(this.commandObjects.waitAOF(sampleKey, numLocal, numReplicas, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<Long, Long> waitAOF(byte[] sampleKey, long numLocal, long numReplicas, long timeout) {
/* 3486 */     return executeCommand(this.commandObjects.waitAOF(sampleKey, numLocal, numReplicas, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(String script, String sampleKey) {
/* 3491 */     return executeCommand(this.commandObjects.eval(script, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(String sha1, String sampleKey) {
/* 3496 */     return executeCommand(this.commandObjects.evalsha(sha1, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(byte[] script, byte[] sampleKey) {
/* 3501 */     return executeCommand(this.commandObjects.eval(script, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(byte[] sha1, byte[] sampleKey) {
/* 3506 */     return executeCommand(this.commandObjects.evalsha(sha1, sampleKey));
/*      */   }
/*      */   
/*      */   public List<Boolean> scriptExists(List<String> sha1s) {
/* 3510 */     return checkAndBroadcastCommand(this.commandObjects.scriptExists(sha1s));
/*      */   }
/*      */ 
/*      */   
/*      */   public Boolean scriptExists(String sha1, String sampleKey) {
/* 3515 */     return scriptExists(sampleKey, new String[] { sha1 }).get(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> scriptExists(String sampleKey, String... sha1s) {
/* 3520 */     return executeCommand(this.commandObjects.scriptExists(sampleKey, sha1s));
/*      */   }
/*      */ 
/*      */   
/*      */   public Boolean scriptExists(byte[] sha1, byte[] sampleKey) {
/* 3525 */     return scriptExists(sampleKey, new byte[][] { sha1 }).get(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> scriptExists(byte[] sampleKey, byte[]... sha1s) {
/* 3530 */     return executeCommand(this.commandObjects.scriptExists(sampleKey, sha1s));
/*      */   }
/*      */   
/*      */   public String scriptLoad(String script) {
/* 3534 */     return checkAndBroadcastCommand(this.commandObjects.scriptLoad(script));
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptLoad(String script, String sampleKey) {
/* 3539 */     return executeCommand(this.commandObjects.scriptLoad(script, sampleKey));
/*      */   }
/*      */   
/*      */   public String scriptFlush() {
/* 3543 */     return checkAndBroadcastCommand(this.commandObjects.scriptFlush());
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptFlush(String sampleKey) {
/* 3548 */     return executeCommand(this.commandObjects.scriptFlush(sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptFlush(String sampleKey, FlushMode flushMode) {
/* 3553 */     return executeCommand(this.commandObjects.scriptFlush(sampleKey, flushMode));
/*      */   }
/*      */   
/*      */   public String scriptKill() {
/* 3557 */     return checkAndBroadcastCommand(this.commandObjects.scriptKill());
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptKill(String sampleKey) {
/* 3562 */     return executeCommand(this.commandObjects.scriptKill(sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] scriptLoad(byte[] script, byte[] sampleKey) {
/* 3567 */     return executeCommand((CommandObject)this.commandObjects.scriptLoad(script, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptFlush(byte[] sampleKey) {
/* 3572 */     return executeCommand(this.commandObjects.scriptFlush(sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptFlush(byte[] sampleKey, FlushMode flushMode) {
/* 3577 */     return executeCommand(this.commandObjects.scriptFlush(sampleKey, flushMode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptKill(byte[] sampleKey) {
/* 3582 */     return executeCommand(this.commandObjects.scriptKill(sampleKey));
/*      */   }
/*      */   
/*      */   public String slowlogReset() {
/* 3586 */     return checkAndBroadcastCommand(this.commandObjects.slowlogReset());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long publish(String channel, String message) {
/* 3592 */     return ((Long)executeCommand(this.commandObjects.publish(channel, message))).longValue();
/*      */   }
/*      */   
/*      */   public long publish(byte[] channel, byte[] message) {
/* 3596 */     return ((Long)executeCommand(this.commandObjects.publish(channel, message))).longValue();
/*      */   }
/*      */   
/*      */   public void subscribe(JedisPubSub jedisPubSub, String... channels) {
/* 3600 */     try (Connection connection = this.provider.getConnection()) {
/* 3601 */       jedisPubSub.proceed(connection, channels);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
/* 3606 */     try (Connection connection = this.provider.getConnection()) {
/* 3607 */       jedisPubSub.proceedWithPatterns(connection, patterns);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
/* 3612 */     try (Connection connection = this.provider.getConnection()) {
/* 3613 */       jedisPubSub.proceed(connection, channels);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
/* 3618 */     try (Connection connection = this.provider.getConnection()) {
/* 3619 */       jedisPubSub.proceedWithPatterns(connection, patterns);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long hsetObject(String key, String field, Object value) {
/* 3626 */     return ((Long)executeCommand(this.commandObjects.hsetObject(key, field, value))).longValue();
/*      */   }
/*      */   
/*      */   public long hsetObject(String key, Map<String, Object> hash) {
/* 3630 */     return ((Long)executeCommand(this.commandObjects.hsetObject(key, hash))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftCreate(String indexName, IndexOptions indexOptions, Schema schema) {
/* 3635 */     return checkAndBroadcastCommand(this.commandObjects.ftCreate(indexName, indexOptions, schema));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftCreate(String indexName, FTCreateParams createParams, Iterable<SchemaField> schemaFields) {
/* 3640 */     return checkAndBroadcastCommand(this.commandObjects.ftCreate(indexName, createParams, schemaFields));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftAlter(String indexName, Schema schema) {
/* 3645 */     return checkAndBroadcastCommand(this.commandObjects.ftAlter(indexName, schema));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftAlter(String indexName, Iterable<SchemaField> schemaFields) {
/* 3650 */     return checkAndBroadcastCommand(this.commandObjects.ftAlter(indexName, schemaFields));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftAliasAdd(String aliasName, String indexName) {
/* 3655 */     return checkAndBroadcastCommand(this.commandObjects.ftAliasAdd(aliasName, indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftAliasUpdate(String aliasName, String indexName) {
/* 3660 */     return checkAndBroadcastCommand(this.commandObjects.ftAliasUpdate(aliasName, indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftAliasDel(String aliasName) {
/* 3665 */     return checkAndBroadcastCommand(this.commandObjects.ftAliasDel(aliasName));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftDropIndex(String indexName) {
/* 3670 */     return checkAndBroadcastCommand(this.commandObjects.ftDropIndex(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftDropIndexDD(String indexName) {
/* 3675 */     return checkAndBroadcastCommand(this.commandObjects.ftDropIndexDD(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public SearchResult ftSearch(String indexName, String query) {
/* 3680 */     return executeCommand(this.commandObjects.ftSearch(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public SearchResult ftSearch(String indexName, String query, FTSearchParams params) {
/* 3685 */     return executeCommand(this.commandObjects.ftSearch(indexName, query, params));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FtSearchIteration ftSearchIteration(int batchSize, String indexName, String query, FTSearchParams params) {
/* 3697 */     return new FtSearchIteration(this.provider, this.commandObjects.getProtocol(), batchSize, indexName, query, params);
/*      */   }
/*      */ 
/*      */   
/*      */   public SearchResult ftSearch(String indexName, Query query) {
/* 3702 */     return executeCommand(this.commandObjects.ftSearch(indexName, query));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FtSearchIteration ftSearchIteration(int batchSize, String indexName, Query query) {
/* 3713 */     return new FtSearchIteration(this.provider, this.commandObjects.getProtocol(), batchSize, indexName, query);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public SearchResult ftSearch(byte[] indexName, Query query) {
/* 3719 */     return executeCommand(this.commandObjects.ftSearch(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftExplain(String indexName, Query query) {
/* 3724 */     return executeCommand(this.commandObjects.ftExplain(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> ftExplainCLI(String indexName, Query query) {
/* 3729 */     return executeCommand(this.commandObjects.ftExplainCLI(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public AggregationResult ftAggregate(String indexName, AggregationBuilder aggr) {
/* 3734 */     return executeCommand(this.commandObjects.ftAggregate(indexName, aggr));
/*      */   }
/*      */ 
/*      */   
/*      */   public AggregationResult ftCursorRead(String indexName, long cursorId, int count) {
/* 3739 */     return executeCommand(this.commandObjects.ftCursorRead(indexName, cursorId, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftCursorDel(String indexName, long cursorId) {
/* 3744 */     return executeCommand(this.commandObjects.ftCursorDel(indexName, cursorId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FtAggregateIteration ftAggregateIteration(String indexName, AggregationBuilder aggr) {
/* 3754 */     return new FtAggregateIteration(this.provider, indexName, aggr);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map.Entry<AggregationResult, Map<String, Object>> ftProfileAggregate(String indexName, FTProfileParams profileParams, AggregationBuilder aggr) {
/* 3760 */     return executeCommand(this.commandObjects.ftProfileAggregate(indexName, profileParams, aggr));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map.Entry<SearchResult, Map<String, Object>> ftProfileSearch(String indexName, FTProfileParams profileParams, Query query) {
/* 3766 */     return executeCommand(this.commandObjects.ftProfileSearch(indexName, profileParams, query));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map.Entry<SearchResult, Map<String, Object>> ftProfileSearch(String indexName, FTProfileParams profileParams, String query, FTSearchParams searchParams) {
/* 3772 */     return executeCommand(this.commandObjects.ftProfileSearch(indexName, profileParams, query, searchParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftSynUpdate(String indexName, String synonymGroupId, String... terms) {
/* 3777 */     return executeCommand(this.commandObjects.ftSynUpdate(indexName, synonymGroupId, terms));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, List<String>> ftSynDump(String indexName) {
/* 3782 */     return executeCommand(this.commandObjects.ftSynDump(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public long ftDictAdd(String dictionary, String... terms) {
/* 3787 */     return ((Long)executeCommand(this.commandObjects.ftDictAdd(dictionary, terms))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long ftDictDel(String dictionary, String... terms) {
/* 3792 */     return ((Long)executeCommand(this.commandObjects.ftDictDel(dictionary, terms))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> ftDictDump(String dictionary) {
/* 3797 */     return executeCommand(this.commandObjects.ftDictDump(dictionary));
/*      */   }
/*      */ 
/*      */   
/*      */   public long ftDictAddBySampleKey(String indexName, String dictionary, String... terms) {
/* 3802 */     return ((Long)executeCommand(this.commandObjects.ftDictAddBySampleKey(indexName, dictionary, terms))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long ftDictDelBySampleKey(String indexName, String dictionary, String... terms) {
/* 3807 */     return ((Long)executeCommand(this.commandObjects.ftDictDelBySampleKey(indexName, dictionary, terms))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> ftDictDumpBySampleKey(String indexName, String dictionary) {
/* 3812 */     return executeCommand(this.commandObjects.ftDictDumpBySampleKey(indexName, dictionary));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Map<String, Double>> ftSpellCheck(String index, String query) {
/* 3817 */     return executeCommand(this.commandObjects.ftSpellCheck(index, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Map<String, Double>> ftSpellCheck(String index, String query, FTSpellCheckParams spellCheckParams) {
/* 3822 */     return executeCommand(this.commandObjects.ftSpellCheck(index, query, spellCheckParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> ftInfo(String indexName) {
/* 3827 */     return executeCommand(this.commandObjects.ftInfo(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> ftTagVals(String indexName, String fieldName) {
/* 3832 */     return executeCommand(this.commandObjects.ftTagVals(indexName, fieldName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> ftConfigGet(String option) {
/* 3837 */     return executeCommand(this.commandObjects.ftConfigGet(option));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> ftConfigGet(String indexName, String option) {
/* 3842 */     return executeCommand(this.commandObjects.ftConfigGet(indexName, option));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftConfigSet(String option, String value) {
/* 3847 */     return executeCommand(this.commandObjects.ftConfigSet(option, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String ftConfigSet(String indexName, String option, String value) {
/* 3852 */     return executeCommand(this.commandObjects.ftConfigSet(indexName, option, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public long ftSugAdd(String key, String string, double score) {
/* 3857 */     return ((Long)executeCommand(this.commandObjects.ftSugAdd(key, string, score))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long ftSugAddIncr(String key, String string, double score) {
/* 3862 */     return ((Long)executeCommand(this.commandObjects.ftSugAddIncr(key, string, score))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> ftSugGet(String key, String prefix) {
/* 3867 */     return executeCommand(this.commandObjects.ftSugGet(key, prefix));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> ftSugGet(String key, String prefix, boolean fuzzy, int max) {
/* 3872 */     return executeCommand(this.commandObjects.ftSugGet(key, prefix, fuzzy, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> ftSugGetWithScores(String key, String prefix) {
/* 3877 */     return executeCommand(this.commandObjects.ftSugGetWithScores(key, prefix));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> ftSugGetWithScores(String key, String prefix, boolean fuzzy, int max) {
/* 3882 */     return executeCommand(this.commandObjects.ftSugGetWithScores(key, prefix, fuzzy, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean ftSugDel(String key, String string) {
/* 3887 */     return ((Boolean)executeCommand(this.commandObjects.ftSugDel(key, string))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long ftSugLen(String key) {
/* 3892 */     return ((Long)executeCommand(this.commandObjects.ftSugLen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> ftList() {
/* 3897 */     return executeCommand(this.commandObjects.ftList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String jsonSet(String key, Path2 path, Object object) {
/* 3904 */     return executeCommand(this.commandObjects.jsonSet(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   public String jsonSetWithEscape(String key, Path2 path, Object object) {
/* 3909 */     return executeCommand(this.commandObjects.jsonSetWithEscape(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String jsonSet(String key, Path path, Object pojo) {
/* 3915 */     return executeCommand(this.commandObjects.jsonSet(key, path, pojo));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String jsonSetWithPlainString(String key, Path path, String string) {
/* 3921 */     return executeCommand(this.commandObjects.jsonSetWithPlainString(key, path, string));
/*      */   }
/*      */ 
/*      */   
/*      */   public String jsonSet(String key, Path2 path, Object pojo, JsonSetParams params) {
/* 3926 */     return executeCommand(this.commandObjects.jsonSet(key, path, pojo, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String jsonSetWithEscape(String key, Path2 path, Object pojo, JsonSetParams params) {
/* 3931 */     return executeCommand(this.commandObjects.jsonSetWithEscape(key, path, pojo, params));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String jsonSet(String key, Path path, Object pojo, JsonSetParams params) {
/* 3937 */     return executeCommand(this.commandObjects.jsonSet(key, path, pojo, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String jsonMerge(String key, Path2 path, Object object) {
/* 3942 */     return executeCommand(this.commandObjects.jsonMerge(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String jsonMerge(String key, Path path, Object pojo) {
/* 3948 */     return executeCommand(this.commandObjects.jsonMerge(key, path, pojo));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object jsonGet(String key) {
/* 3953 */     return executeCommand(this.commandObjects.jsonGet(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T jsonGet(String key, Class<T> clazz) {
/* 3959 */     return executeCommand(this.commandObjects.jsonGet(key, clazz));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object jsonGet(String key, Path2... paths) {
/* 3964 */     return executeCommand(this.commandObjects.jsonGet(key, paths));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Object jsonGet(String key, Path... paths) {
/* 3970 */     return executeCommand(this.commandObjects.jsonGet(key, paths));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String jsonGetAsPlainString(String key, Path path) {
/* 3976 */     return executeCommand(this.commandObjects.jsonGetAsPlainString(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T jsonGet(String key, Class<T> clazz, Path... paths) {
/* 3982 */     return executeCommand(this.commandObjects.jsonGet(key, clazz, paths));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<JSONArray> jsonMGet(Path2 path, String... keys) {
/* 3987 */     return executeCommand(this.commandObjects.jsonMGet(path, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> List<T> jsonMGet(Path path, Class<T> clazz, String... keys) {
/* 3993 */     return executeCommand(this.commandObjects.jsonMGet(path, clazz, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long jsonDel(String key) {
/* 3998 */     return ((Long)executeCommand(this.commandObjects.jsonDel(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long jsonDel(String key, Path2 path) {
/* 4003 */     return ((Long)executeCommand(this.commandObjects.jsonDel(key, path))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonDel(String key, Path path) {
/* 4009 */     return ((Long)executeCommand(this.commandObjects.jsonDel(key, path))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long jsonClear(String key) {
/* 4014 */     return ((Long)executeCommand(this.commandObjects.jsonClear(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long jsonClear(String key, Path2 path) {
/* 4019 */     return ((Long)executeCommand(this.commandObjects.jsonClear(key, path))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonClear(String key, Path path) {
/* 4025 */     return ((Long)executeCommand(this.commandObjects.jsonClear(key, path))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> jsonToggle(String key, Path2 path) {
/* 4030 */     return executeCommand(this.commandObjects.jsonToggle(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String jsonToggle(String key, Path path) {
/* 4036 */     return executeCommand(this.commandObjects.jsonToggle(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Class<?> jsonType(String key) {
/* 4042 */     return executeCommand(this.commandObjects.jsonType(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Class<?>> jsonType(String key, Path2 path) {
/* 4047 */     return executeCommand(this.commandObjects.jsonType(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Class<?> jsonType(String key, Path path) {
/* 4053 */     return executeCommand(this.commandObjects.jsonType(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonStrAppend(String key, Object string) {
/* 4059 */     return ((Long)executeCommand(this.commandObjects.jsonStrAppend(key, string))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonStrAppend(String key, Path2 path, Object string) {
/* 4064 */     return executeCommand(this.commandObjects.jsonStrAppend(key, path, string));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonStrAppend(String key, Path path, Object string) {
/* 4070 */     return ((Long)executeCommand(this.commandObjects.jsonStrAppend(key, path, string))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonStrLen(String key) {
/* 4076 */     return executeCommand(this.commandObjects.jsonStrLen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonStrLen(String key, Path2 path) {
/* 4081 */     return executeCommand(this.commandObjects.jsonStrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonStrLen(String key, Path path) {
/* 4087 */     return executeCommand(this.commandObjects.jsonStrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object jsonNumIncrBy(String key, Path2 path, double value) {
/* 4092 */     return executeCommand(this.commandObjects.jsonNumIncrBy(key, path, value));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public double jsonNumIncrBy(String key, Path path, double value) {
/* 4098 */     return ((Double)executeCommand(this.commandObjects.jsonNumIncrBy(key, path, value))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrAppend(String key, Path2 path, Object... objects) {
/* 4103 */     return executeCommand(this.commandObjects.jsonArrAppend(key, path, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrAppendWithEscape(String key, Path2 path, Object... objects) {
/* 4108 */     return executeCommand(this.commandObjects.jsonArrAppendWithEscape(key, path, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonArrAppend(String key, Path path, Object... pojos) {
/* 4114 */     return executeCommand(this.commandObjects.jsonArrAppend(key, path, pojos));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrIndex(String key, Path2 path, Object scalar) {
/* 4119 */     return executeCommand(this.commandObjects.jsonArrIndex(key, path, scalar));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrIndexWithEscape(String key, Path2 path, Object scalar) {
/* 4124 */     return executeCommand(this.commandObjects.jsonArrIndexWithEscape(key, path, scalar));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonArrIndex(String key, Path path, Object scalar) {
/* 4130 */     return ((Long)executeCommand(this.commandObjects.jsonArrIndex(key, path, scalar))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrInsert(String key, Path2 path, int index, Object... objects) {
/* 4135 */     return executeCommand(this.commandObjects.jsonArrInsert(key, path, index, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrInsertWithEscape(String key, Path2 path, int index, Object... objects) {
/* 4140 */     return executeCommand(this.commandObjects.jsonArrInsertWithEscape(key, path, index, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonArrInsert(String key, Path path, int index, Object... pojos) {
/* 4146 */     return ((Long)executeCommand(this.commandObjects.jsonArrInsert(key, path, index, pojos))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Object jsonArrPop(String key) {
/* 4152 */     return executeCommand(this.commandObjects.jsonArrPop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T jsonArrPop(String key, Class<T> clazz) {
/* 4158 */     return executeCommand(this.commandObjects.jsonArrPop(key, clazz));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> jsonArrPop(String key, Path2 path) {
/* 4163 */     return executeCommand(this.commandObjects.jsonArrPop(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Object jsonArrPop(String key, Path path) {
/* 4169 */     return executeCommand(this.commandObjects.jsonArrPop(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T jsonArrPop(String key, Class<T> clazz, Path path) {
/* 4175 */     return executeCommand(this.commandObjects.jsonArrPop(key, clazz, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> jsonArrPop(String key, Path2 path, int index) {
/* 4180 */     return executeCommand(this.commandObjects.jsonArrPop(key, path, index));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Object jsonArrPop(String key, Path path, int index) {
/* 4186 */     return executeCommand(this.commandObjects.jsonArrPop(key, path, index));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T jsonArrPop(String key, Class<T> clazz, Path path, int index) {
/* 4192 */     return executeCommand(this.commandObjects.jsonArrPop(key, clazz, path, index));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonArrLen(String key) {
/* 4198 */     return executeCommand(this.commandObjects.jsonArrLen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrLen(String key, Path2 path) {
/* 4203 */     return executeCommand(this.commandObjects.jsonArrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonArrLen(String key, Path path) {
/* 4209 */     return executeCommand(this.commandObjects.jsonArrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonArrTrim(String key, Path2 path, int start, int stop) {
/* 4214 */     return executeCommand(this.commandObjects.jsonArrTrim(key, path, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonArrTrim(String key, Path path, int start, int stop) {
/* 4220 */     return executeCommand(this.commandObjects.jsonArrTrim(key, path, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonObjLen(String key) {
/* 4226 */     return executeCommand(this.commandObjects.jsonObjLen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Long jsonObjLen(String key, Path path) {
/* 4232 */     return executeCommand(this.commandObjects.jsonObjLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonObjLen(String key, Path2 path) {
/* 4237 */     return executeCommand(this.commandObjects.jsonObjLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> jsonObjKeys(String key) {
/* 4243 */     return executeCommand(this.commandObjects.jsonObjKeys(key));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> jsonObjKeys(String key, Path path) {
/* 4249 */     return executeCommand(this.commandObjects.jsonObjKeys(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<List<String>> jsonObjKeys(String key, Path2 path) {
/* 4254 */     return executeCommand(this.commandObjects.jsonObjKeys(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonDebugMemory(String key) {
/* 4260 */     return ((Long)executeCommand(this.commandObjects.jsonDebugMemory(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long jsonDebugMemory(String key, Path path) {
/* 4266 */     return ((Long)executeCommand(this.commandObjects.jsonDebugMemory(key, path))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> jsonDebugMemory(String key, Path2 path) {
/* 4271 */     return executeCommand(this.commandObjects.jsonDebugMemory(key, path));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String tsCreate(String key) {
/* 4278 */     return executeCommand(this.commandObjects.tsCreate(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tsCreate(String key, TSCreateParams createParams) {
/* 4283 */     return executeCommand(this.commandObjects.tsCreate(key, createParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsDel(String key, long fromTimestamp, long toTimestamp) {
/* 4288 */     return ((Long)executeCommand(this.commandObjects.tsDel(key, fromTimestamp, toTimestamp))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String tsAlter(String key, TSAlterParams alterParams) {
/* 4293 */     return executeCommand(this.commandObjects.tsAlter(key, alterParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsAdd(String key, double value) {
/* 4298 */     return ((Long)executeCommand(this.commandObjects.tsAdd(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsAdd(String key, long timestamp, double value) {
/* 4303 */     return ((Long)executeCommand(this.commandObjects.tsAdd(key, timestamp, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsAdd(String key, long timestamp, double value, TSCreateParams createParams) {
/* 4308 */     return ((Long)executeCommand(this.commandObjects.tsAdd(key, timestamp, value, createParams))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> tsMAdd(Map.Entry<String, TSElement>... entries) {
/* 4313 */     return executeCommand(this.commandObjects.tsMAdd(entries));
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsIncrBy(String key, double value) {
/* 4318 */     return ((Long)executeCommand(this.commandObjects.tsIncrBy(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsIncrBy(String key, double value, long timestamp) {
/* 4323 */     return ((Long)executeCommand(this.commandObjects.tsIncrBy(key, value, timestamp))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsDecrBy(String key, double value) {
/* 4328 */     return ((Long)executeCommand(this.commandObjects.tsDecrBy(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long tsDecrBy(String key, double value, long timestamp) {
/* 4333 */     return ((Long)executeCommand(this.commandObjects.tsDecrBy(key, value, timestamp))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<TSElement> tsRange(String key, long fromTimestamp, long toTimestamp) {
/* 4338 */     return executeCommand(this.commandObjects.tsRange(key, fromTimestamp, toTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<TSElement> tsRange(String key, TSRangeParams rangeParams) {
/* 4343 */     return executeCommand(this.commandObjects.tsRange(key, rangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<TSElement> tsRevRange(String key, long fromTimestamp, long toTimestamp) {
/* 4348 */     return executeCommand(this.commandObjects.tsRevRange(key, fromTimestamp, toTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<TSElement> tsRevRange(String key, TSRangeParams rangeParams) {
/* 4353 */     return executeCommand(this.commandObjects.tsRevRange(key, rangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, TSMRangeElements> tsMRange(long fromTimestamp, long toTimestamp, String... filters) {
/* 4358 */     return executeCommand(this.commandObjects.tsMRange(fromTimestamp, toTimestamp, filters));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, TSMRangeElements> tsMRange(TSMRangeParams multiRangeParams) {
/* 4363 */     return executeCommand(this.commandObjects.tsMRange(multiRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, TSMRangeElements> tsMRevRange(long fromTimestamp, long toTimestamp, String... filters) {
/* 4368 */     return executeCommand(this.commandObjects.tsMRevRange(fromTimestamp, toTimestamp, filters));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, TSMRangeElements> tsMRevRange(TSMRangeParams multiRangeParams) {
/* 4373 */     return executeCommand(this.commandObjects.tsMRevRange(multiRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public TSElement tsGet(String key) {
/* 4378 */     return executeCommand(this.commandObjects.tsGet(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public TSElement tsGet(String key, TSGetParams getParams) {
/* 4383 */     return executeCommand(this.commandObjects.tsGet(key, getParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, TSMGetElement> tsMGet(TSMGetParams multiGetParams, String... filters) {
/* 4388 */     return executeCommand(this.commandObjects.tsMGet(multiGetParams, filters));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType, long timeBucket) {
/* 4393 */     return executeCommand(this.commandObjects.tsCreateRule(sourceKey, destKey, aggregationType, timeBucket));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType, long bucketDuration, long alignTimestamp) {
/* 4398 */     return executeCommand(this.commandObjects.tsCreateRule(sourceKey, destKey, aggregationType, bucketDuration, alignTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tsDeleteRule(String sourceKey, String destKey) {
/* 4403 */     return executeCommand(this.commandObjects.tsDeleteRule(sourceKey, destKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> tsQueryIndex(String... filters) {
/* 4408 */     return executeCommand(this.commandObjects.tsQueryIndex(filters));
/*      */   }
/*      */ 
/*      */   
/*      */   public TSInfo tsInfo(String key) {
/* 4413 */     return (TSInfo)this.executor.executeCommand(this.commandObjects.tsInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public TSInfo tsInfoDebug(String key) {
/* 4418 */     return executeCommand(this.commandObjects.tsInfoDebug(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String bfReserve(String key, double errorRate, long capacity) {
/* 4425 */     return executeCommand(this.commandObjects.bfReserve(key, errorRate, capacity));
/*      */   }
/*      */ 
/*      */   
/*      */   public String bfReserve(String key, double errorRate, long capacity, BFReserveParams reserveParams) {
/* 4430 */     return executeCommand(this.commandObjects.bfReserve(key, errorRate, capacity, reserveParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean bfAdd(String key, String item) {
/* 4435 */     return ((Boolean)executeCommand(this.commandObjects.bfAdd(key, item))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> bfMAdd(String key, String... items) {
/* 4440 */     return executeCommand(this.commandObjects.bfMAdd(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> bfInsert(String key, String... items) {
/* 4445 */     return executeCommand(this.commandObjects.bfInsert(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> bfInsert(String key, BFInsertParams insertParams, String... items) {
/* 4450 */     return executeCommand(this.commandObjects.bfInsert(key, insertParams, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean bfExists(String key, String item) {
/* 4455 */     return ((Boolean)executeCommand(this.commandObjects.bfExists(key, item))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> bfMExists(String key, String... items) {
/* 4460 */     return executeCommand(this.commandObjects.bfMExists(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map.Entry<Long, byte[]> bfScanDump(String key, long iterator) {
/* 4465 */     return executeCommand(this.commandObjects.bfScanDump(key, iterator));
/*      */   }
/*      */ 
/*      */   
/*      */   public String bfLoadChunk(String key, long iterator, byte[] data) {
/* 4470 */     return executeCommand(this.commandObjects.bfLoadChunk(key, iterator, data));
/*      */   }
/*      */ 
/*      */   
/*      */   public long bfCard(String key) {
/* 4475 */     return ((Long)executeCommand(this.commandObjects.bfCard(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> bfInfo(String key) {
/* 4480 */     return executeCommand(this.commandObjects.bfInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String cfReserve(String key, long capacity) {
/* 4485 */     return executeCommand(this.commandObjects.cfReserve(key, capacity));
/*      */   }
/*      */ 
/*      */   
/*      */   public String cfReserve(String key, long capacity, CFReserveParams reserveParams) {
/* 4490 */     return executeCommand(this.commandObjects.cfReserve(key, capacity, reserveParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean cfAdd(String key, String item) {
/* 4495 */     return ((Boolean)executeCommand(this.commandObjects.cfAdd(key, item))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean cfAddNx(String key, String item) {
/* 4500 */     return ((Boolean)executeCommand(this.commandObjects.cfAddNx(key, item))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> cfInsert(String key, String... items) {
/* 4505 */     return executeCommand(this.commandObjects.cfInsert(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> cfInsert(String key, CFInsertParams insertParams, String... items) {
/* 4510 */     return executeCommand(this.commandObjects.cfInsert(key, insertParams, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> cfInsertNx(String key, String... items) {
/* 4515 */     return executeCommand(this.commandObjects.cfInsertNx(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> cfInsertNx(String key, CFInsertParams insertParams, String... items) {
/* 4520 */     return executeCommand(this.commandObjects.cfInsertNx(key, insertParams, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean cfExists(String key, String item) {
/* 4525 */     return ((Boolean)executeCommand(this.commandObjects.cfExists(key, item))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> cfMExists(String key, String... items) {
/* 4530 */     return executeCommand(this.commandObjects.cfMExists(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean cfDel(String key, String item) {
/* 4535 */     return ((Boolean)executeCommand(this.commandObjects.cfDel(key, item))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long cfCount(String key, String item) {
/* 4540 */     return ((Long)executeCommand(this.commandObjects.cfCount(key, item))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Map.Entry<Long, byte[]> cfScanDump(String key, long iterator) {
/* 4545 */     return executeCommand(this.commandObjects.cfScanDump(key, iterator));
/*      */   }
/*      */ 
/*      */   
/*      */   public String cfLoadChunk(String key, long iterator, byte[] data) {
/* 4550 */     return executeCommand(this.commandObjects.cfLoadChunk(key, iterator, data));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> cfInfo(String key) {
/* 4555 */     return executeCommand(this.commandObjects.cfInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String cmsInitByDim(String key, long width, long depth) {
/* 4560 */     return executeCommand(this.commandObjects.cmsInitByDim(key, width, depth));
/*      */   }
/*      */ 
/*      */   
/*      */   public String cmsInitByProb(String key, double error, double probability) {
/* 4565 */     return executeCommand(this.commandObjects.cmsInitByProb(key, error, probability));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> cmsIncrBy(String key, Map<String, Long> itemIncrements) {
/* 4570 */     return executeCommand(this.commandObjects.cmsIncrBy(key, itemIncrements));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> cmsQuery(String key, String... items) {
/* 4575 */     return executeCommand(this.commandObjects.cmsQuery(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public String cmsMerge(String destKey, String... keys) {
/* 4580 */     return executeCommand(this.commandObjects.cmsMerge(destKey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String cmsMerge(String destKey, Map<String, Long> keysAndWeights) {
/* 4585 */     return executeCommand(this.commandObjects.cmsMerge(destKey, keysAndWeights));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> cmsInfo(String key) {
/* 4590 */     return executeCommand(this.commandObjects.cmsInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String topkReserve(String key, long topk) {
/* 4595 */     return executeCommand(this.commandObjects.topkReserve(key, topk));
/*      */   }
/*      */ 
/*      */   
/*      */   public String topkReserve(String key, long topk, long width, long depth, double decay) {
/* 4600 */     return executeCommand(this.commandObjects.topkReserve(key, topk, width, depth, decay));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> topkAdd(String key, String... items) {
/* 4605 */     return executeCommand(this.commandObjects.topkAdd(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> topkIncrBy(String key, Map<String, Long> itemIncrements) {
/* 4610 */     return executeCommand(this.commandObjects.topkIncrBy(key, itemIncrements));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> topkQuery(String key, String... items) {
/* 4615 */     return executeCommand(this.commandObjects.topkQuery(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> topkList(String key) {
/* 4620 */     return executeCommand(this.commandObjects.topkList(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Long> topkListWithCount(String key) {
/* 4625 */     return executeCommand(this.commandObjects.topkListWithCount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> topkInfo(String key) {
/* 4630 */     return executeCommand(this.commandObjects.topkInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tdigestCreate(String key) {
/* 4635 */     return executeCommand(this.commandObjects.tdigestCreate(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tdigestCreate(String key, int compression) {
/* 4640 */     return executeCommand(this.commandObjects.tdigestCreate(key, compression));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tdigestReset(String key) {
/* 4645 */     return executeCommand(this.commandObjects.tdigestReset(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tdigestMerge(String destinationKey, String... sourceKeys) {
/* 4650 */     return executeCommand(this.commandObjects.tdigestMerge(destinationKey, sourceKeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tdigestMerge(TDigestMergeParams mergeParams, String destinationKey, String... sourceKeys) {
/* 4655 */     return executeCommand(this.commandObjects.tdigestMerge(mergeParams, destinationKey, sourceKeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> tdigestInfo(String key) {
/* 4660 */     return executeCommand(this.commandObjects.tdigestInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tdigestAdd(String key, double... values) {
/* 4665 */     return executeCommand(this.commandObjects.tdigestAdd(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Double> tdigestCDF(String key, double... values) {
/* 4670 */     return executeCommand(this.commandObjects.tdigestCDF(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Double> tdigestQuantile(String key, double... quantiles) {
/* 4675 */     return executeCommand(this.commandObjects.tdigestQuantile(key, quantiles));
/*      */   }
/*      */ 
/*      */   
/*      */   public double tdigestMin(String key) {
/* 4680 */     return ((Double)executeCommand(this.commandObjects.tdigestMin(key))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double tdigestMax(String key) {
/* 4685 */     return ((Double)executeCommand(this.commandObjects.tdigestMax(key))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public double tdigestTrimmedMean(String key, double lowCutQuantile, double highCutQuantile) {
/* 4690 */     return ((Double)executeCommand(this.commandObjects.tdigestTrimmedMean(key, lowCutQuantile, highCutQuantile))).doubleValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> tdigestRank(String key, double... values) {
/* 4695 */     return executeCommand(this.commandObjects.tdigestRank(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> tdigestRevRank(String key, double... values) {
/* 4700 */     return executeCommand(this.commandObjects.tdigestRevRank(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Double> tdigestByRank(String key, long... ranks) {
/* 4705 */     return executeCommand(this.commandObjects.tdigestByRank(key, ranks));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Double> tdigestByRevRank(String key, long... ranks) {
/* 4710 */     return executeCommand(this.commandObjects.tdigestByRevRank(key, ranks));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphQuery(String name, String query) {
/* 4718 */     return executeCommand(this.graphCommandObjects.graphQuery(name, query));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphReadonlyQuery(String name, String query) {
/* 4724 */     return executeCommand(this.graphCommandObjects.graphReadonlyQuery(name, query));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphQuery(String name, String query, long timeout) {
/* 4730 */     return executeCommand(this.graphCommandObjects.graphQuery(name, query, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphReadonlyQuery(String name, String query, long timeout) {
/* 4736 */     return executeCommand(this.graphCommandObjects.graphReadonlyQuery(name, query, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphQuery(String name, String query, Map<String, Object> params) {
/* 4742 */     return executeCommand(this.graphCommandObjects.graphQuery(name, query, params));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphReadonlyQuery(String name, String query, Map<String, Object> params) {
/* 4748 */     return executeCommand(this.graphCommandObjects.graphReadonlyQuery(name, query, params));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 4754 */     return executeCommand(this.graphCommandObjects.graphQuery(name, query, params, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public ResultSet graphReadonlyQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 4760 */     return executeCommand(this.graphCommandObjects.graphReadonlyQuery(name, query, params, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String graphDelete(String name) {
/* 4766 */     return executeCommand(this.graphCommandObjects.graphDelete(name));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> graphList() {
/* 4772 */     return executeCommand(this.commandObjects.graphList());
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> graphProfile(String graphName, String query) {
/* 4778 */     return executeCommand(this.commandObjects.graphProfile(graphName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> graphExplain(String graphName, String query) {
/* 4784 */     return executeCommand(this.commandObjects.graphExplain(graphName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<List<Object>> graphSlowlog(String graphName) {
/* 4790 */     return executeCommand(this.commandObjects.graphSlowlog(graphName));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String graphConfigSet(String configName, Object value) {
/* 4796 */     return executeCommand(this.commandObjects.graphConfigSet(configName, value));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Map<String, Object> graphConfigGet(String configName) {
/* 4802 */     return executeCommand(this.commandObjects.graphConfigGet(configName));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String tFunctionLoad(String libraryCode, TFunctionLoadParams params) {
/* 4809 */     return executeCommand(this.commandObjects.tFunctionLoad(libraryCode, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String tFunctionDelete(String libraryName) {
/* 4814 */     return executeCommand(this.commandObjects.tFunctionDelete(libraryName));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GearsLibraryInfo> tFunctionList(TFunctionListParams params) {
/* 4819 */     return executeCommand(this.commandObjects.tFunctionList(params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object tFunctionCall(String library, String function, List<String> keys, List<String> args) {
/* 4824 */     return executeCommand(this.commandObjects.tFunctionCall(library, function, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object tFunctionCallAsync(String library, String function, List<String> keys, List<String> args) {
/* 4829 */     return executeCommand(this.commandObjects.tFunctionCallAsync(library, function, keys, args));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public PipelineBase pipelined() {
/* 4837 */     if (this.provider == null)
/* 4838 */       throw new IllegalStateException("It is not allowed to create Pipeline from this " + getClass()); 
/* 4839 */     if (this.provider instanceof MultiClusterPooledConnectionProvider) {
/* 4840 */       return (PipelineBase)new MultiClusterPipeline((MultiClusterPooledConnectionProvider)this.provider);
/*      */     }
/* 4842 */     return new Pipeline(this.provider.getConnection(), true);
/*      */   }
/*      */ 
/*      */   
/*      */   public AbstractTransaction multi() {
/* 4847 */     if (this.provider == null)
/* 4848 */       throw new IllegalStateException("It is not allowed to create Pipeline from this " + getClass()); 
/* 4849 */     if (this.provider instanceof MultiClusterPooledConnectionProvider) {
/* 4850 */       return (AbstractTransaction)new MultiClusterTransaction((MultiClusterPooledConnectionProvider)this.provider);
/*      */     }
/* 4852 */     return new Transaction(this.provider.getConnection(), true, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object sendCommand(ProtocolCommand cmd) {
/* 4857 */     return executeCommand(this.commandObjects.commandArguments(cmd));
/*      */   }
/*      */   
/*      */   public Object sendCommand(ProtocolCommand cmd, byte[]... args) {
/* 4861 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args));
/*      */   }
/*      */   
/*      */   public Object sendBlockingCommand(ProtocolCommand cmd, byte[]... args) {
/* 4865 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args).blocking());
/*      */   }
/*      */   
/*      */   public Object sendCommand(ProtocolCommand cmd, String... args) {
/* 4869 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args));
/*      */   }
/*      */   
/*      */   public Object sendBlockingCommand(ProtocolCommand cmd, String... args) {
/* 4873 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args).blocking());
/*      */   }
/*      */   
/*      */   public Object sendCommand(byte[] sampleKey, ProtocolCommand cmd, byte[]... args) {
/* 4877 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args).processKey(sampleKey));
/*      */   }
/*      */   
/*      */   public Object sendBlockingCommand(byte[] sampleKey, ProtocolCommand cmd, byte[]... args) {
/* 4881 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args).blocking().processKey(sampleKey));
/*      */   }
/*      */   
/*      */   public Object sendCommand(String sampleKey, ProtocolCommand cmd, String... args) {
/* 4885 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args).processKey(sampleKey));
/*      */   }
/*      */   
/*      */   public Object sendBlockingCommand(String sampleKey, ProtocolCommand cmd, String... args) {
/* 4889 */     return executeCommand(this.commandObjects.commandArguments(cmd).addObjects((Object[])args).blocking().processKey(sampleKey));
/*      */   }
/*      */   
/*      */   public Object executeCommand(CommandArguments args) {
/* 4893 */     return executeCommand(new CommandObject(args, BuilderFactory.RAW_OBJECT));
/*      */   }
/*      */   
/*      */   public void setJsonObjectMapper(JsonObjectMapper jsonObjectMapper) {
/* 4897 */     this.commandObjects.setJsonObjectMapper(jsonObjectMapper);
/*      */   }
/*      */   
/*      */   public void setDefaultSearchDialect(int dialect) {
/* 4901 */     this.commandObjects.setDefaultSearchDialect(dialect);
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\UnifiedJedis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */