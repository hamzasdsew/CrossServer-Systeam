/*      */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*      */ import java.net.URI;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import javax.net.ssl.HostnameVerifier;
/*      */ import javax.net.ssl.SSLParameters;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ExpiryOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortedSetOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusStoreParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoSearchParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.LPosParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.MigrateParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.SetParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.SortingParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAutoClaimParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XClaimParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZAddParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZRangeParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.GeoRadiusResponse;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.LibraryInfo;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamEntry;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisURIHelper;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*      */ 
/*      */ public class Jedis implements ServerCommands, DatabaseCommands, JedisCommands, JedisBinaryCommands, ControlCommands, ControlBinaryCommands, ClusterCommands, ModuleCommands, GenericControlCommands, SentinelCommands, Closeable {
/*   39 */   private final CommandObjects commandObjects = new CommandObjects(); protected final Connection connection;
/*   40 */   private int db = 0;
/*   41 */   private Transaction transaction = null;
/*      */   private boolean isInMulti = false;
/*      */   private boolean isInWatch = false;
/*   44 */   private Pipeline pipeline = null;
/*   45 */   protected static final byte[][] DUMMY_ARRAY = new byte[0][];
/*      */   
/*   47 */   private Pool<Jedis> dataSource = null;
/*      */   
/*      */   public Jedis() {
/*   50 */     this.connection = new Connection();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Jedis(String url) {
/*   59 */     this(URI.create(url));
/*      */   }
/*      */   
/*      */   public Jedis(HostAndPort hp) {
/*   63 */     this.connection = new Connection(hp);
/*      */   }
/*      */   
/*      */   public Jedis(String host, int port) {
/*   67 */     this.connection = new Connection(host, port);
/*      */   }
/*      */   
/*      */   public Jedis(String host, int port, JedisClientConfig config) {
/*   71 */     this(new HostAndPort(host, port), config);
/*      */   }
/*      */   
/*      */   public Jedis(HostAndPort hostPort, JedisClientConfig config) {
/*   75 */     this.connection = new Connection(hostPort, config);
/*   76 */     RedisProtocol proto = config.getRedisProtocol();
/*   77 */     if (proto != null) this.commandObjects.setProtocol(proto); 
/*      */   }
/*      */   
/*      */   public Jedis(String host, int port, boolean ssl) {
/*   81 */     this(host, port, DefaultJedisClientConfig.builder().ssl(ssl).build());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Jedis(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*   87 */     this(host, port, DefaultJedisClientConfig.builder().ssl(ssl)
/*   88 */         .sslSocketFactory(sslSocketFactory).sslParameters(sslParameters)
/*   89 */         .hostnameVerifier(hostnameVerifier).build());
/*      */   }
/*      */   
/*      */   public Jedis(String host, int port, int timeout) {
/*   93 */     this(host, port, timeout, timeout);
/*      */   }
/*      */   
/*      */   public Jedis(String host, int port, int timeout, boolean ssl) {
/*   97 */     this(host, port, timeout, timeout, ssl);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Jedis(String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  103 */     this(host, port, timeout, timeout, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*      */   }
/*      */ 
/*      */   
/*      */   public Jedis(String host, int port, int connectionTimeout, int soTimeout) {
/*  108 */     this(host, port, DefaultJedisClientConfig.builder()
/*  109 */         .connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout).build());
/*      */   }
/*      */ 
/*      */   
/*      */   public Jedis(String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout) {
/*  114 */     this(host, port, DefaultJedisClientConfig.builder()
/*  115 */         .connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout)
/*  116 */         .blockingSocketTimeoutMillis(infiniteSoTimeout).build());
/*      */   }
/*      */ 
/*      */   
/*      */   public Jedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl) {
/*  121 */     this(host, port, DefaultJedisClientConfig.builder()
/*  122 */         .connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout).ssl(ssl)
/*  123 */         .build());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Jedis(String host, int port, int connectionTimeout, int soTimeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  129 */     this(host, port, DefaultJedisClientConfig.builder()
/*  130 */         .connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout).ssl(ssl)
/*  131 */         .sslSocketFactory(sslSocketFactory).sslParameters(sslParameters)
/*  132 */         .hostnameVerifier(hostnameVerifier).build());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Jedis(String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  139 */     this(host, port, DefaultJedisClientConfig.builder()
/*  140 */         .connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout)
/*  141 */         .blockingSocketTimeoutMillis(infiniteSoTimeout).ssl(ssl)
/*  142 */         .sslSocketFactory(sslSocketFactory).sslParameters(sslParameters)
/*  143 */         .hostnameVerifier(hostnameVerifier).build());
/*      */   }
/*      */   
/*      */   public Jedis(URI uri) {
/*  147 */     if (!JedisURIHelper.isValid(uri)) {
/*  148 */       throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI \"%s\".", new Object[] { uri
/*  149 */               .toString() }));
/*      */     }
/*  151 */     this
/*      */ 
/*      */ 
/*      */       
/*  155 */       .connection = new Connection(new HostAndPort(uri.getHost(), uri.getPort()), DefaultJedisClientConfig.builder().user(JedisURIHelper.getUser(uri)).password(JedisURIHelper.getPassword(uri)).database(JedisURIHelper.getDBIndex(uri)).protocol(JedisURIHelper.getRedisProtocol(uri)).ssl(JedisURIHelper.isRedisSSLScheme(uri)).build());
/*      */   }
/*      */ 
/*      */   
/*      */   public Jedis(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  160 */     this(uri, DefaultJedisClientConfig.builder().sslSocketFactory(sslSocketFactory)
/*  161 */         .sslParameters(sslParameters).hostnameVerifier(hostnameVerifier).build());
/*      */   }
/*      */   
/*      */   public Jedis(URI uri, int timeout) {
/*  165 */     this(uri, timeout, timeout);
/*      */   }
/*      */ 
/*      */   
/*      */   public Jedis(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  170 */     this(uri, timeout, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
/*      */   }
/*      */   
/*      */   public Jedis(URI uri, int connectionTimeout, int soTimeout) {
/*  174 */     this(uri, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/*  175 */         .socketTimeoutMillis(soTimeout).build());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Jedis(URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  181 */     this(uri, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/*  182 */         .socketTimeoutMillis(soTimeout).sslSocketFactory(sslSocketFactory)
/*  183 */         .sslParameters(sslParameters).hostnameVerifier(hostnameVerifier).build());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Jedis(URI uri, int connectionTimeout, int soTimeout, int infiniteSoTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  189 */     this(uri, DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/*  190 */         .socketTimeoutMillis(soTimeout).blockingSocketTimeoutMillis(infiniteSoTimeout)
/*  191 */         .sslSocketFactory(sslSocketFactory).sslParameters(sslParameters)
/*  192 */         .hostnameVerifier(hostnameVerifier).build());
/*      */   }
/*      */   
/*      */   public Jedis(URI uri, JedisClientConfig config) {
/*  196 */     if (!JedisURIHelper.isValid(uri)) {
/*  197 */       throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI \"%s\".", new Object[] { uri
/*  198 */               .toString() }));
/*      */     }
/*  200 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  210 */       .connection = new Connection(new HostAndPort(uri.getHost(), uri.getPort()), DefaultJedisClientConfig.builder().connectionTimeoutMillis(config.getConnectionTimeoutMillis()).socketTimeoutMillis(config.getSocketTimeoutMillis()).blockingSocketTimeoutMillis(config.getBlockingSocketTimeoutMillis()).user(JedisURIHelper.getUser(uri)).password(JedisURIHelper.getPassword(uri)).database(JedisURIHelper.getDBIndex(uri)).clientName(config.getClientName()).protocol(JedisURIHelper.getRedisProtocol(uri)).ssl(JedisURIHelper.isRedisSSLScheme(uri)).sslSocketFactory(config.getSslSocketFactory()).sslParameters(config.getSslParameters()).hostnameVerifier(config.getHostnameVerifier()).build());
/*  211 */     RedisProtocol proto = config.getRedisProtocol();
/*  212 */     if (proto != null) this.commandObjects.setProtocol(proto); 
/*      */   }
/*      */   
/*      */   public Jedis(JedisSocketFactory jedisSocketFactory) {
/*  216 */     this.connection = new Connection(jedisSocketFactory);
/*      */   }
/*      */   
/*      */   public Jedis(JedisSocketFactory jedisSocketFactory, JedisClientConfig clientConfig) {
/*  220 */     this.connection = new Connection(jedisSocketFactory, clientConfig);
/*  221 */     RedisProtocol proto = clientConfig.getRedisProtocol();
/*  222 */     if (proto != null) this.commandObjects.setProtocol(proto); 
/*      */   }
/*      */   
/*      */   public Jedis(Connection connection) {
/*  226 */     this.connection = connection;
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/*  231 */     return "Jedis{" + this.connection + '}';
/*      */   }
/*      */ 
/*      */   
/*      */   public Connection getClient() {
/*  236 */     return getConnection();
/*      */   }
/*      */   
/*      */   public Connection getConnection() {
/*  240 */     return this.connection;
/*      */   }
/*      */ 
/*      */   
/*      */   public void connect() {
/*  245 */     this.connection.connect();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void disconnect() {
/*  252 */     this.connection.disconnect();
/*      */   }
/*      */   
/*      */   public boolean isConnected() {
/*  256 */     return this.connection.isConnected();
/*      */   }
/*      */   
/*      */   public boolean isBroken() {
/*  260 */     return this.connection.isBroken();
/*      */   }
/*      */   
/*      */   public void resetState() {
/*  264 */     if (isConnected()) {
/*  265 */       if (this.transaction != null) {
/*  266 */         this.transaction.close();
/*      */       }
/*      */       
/*  269 */       if (this.pipeline != null) {
/*  270 */         this.pipeline.close();
/*      */       }
/*      */ 
/*      */       
/*  274 */       if (this.isInWatch) {
/*  275 */         this.connection.sendCommand(Protocol.Command.UNWATCH);
/*  276 */         this.connection.getStatusCodeReply();
/*  277 */         this.isInWatch = false;
/*      */       } 
/*      */     } 
/*      */     
/*  281 */     this.transaction = null;
/*  282 */     this.pipeline = null;
/*      */   }
/*      */   
/*      */   protected void setDataSource(Pool<Jedis> jedisPool) {
/*  286 */     this.dataSource = jedisPool;
/*      */   }
/*      */ 
/*      */   
/*      */   public void close() {
/*  291 */     if (this.dataSource != null) {
/*  292 */       Pool<Jedis> pool = this.dataSource;
/*  293 */       this.dataSource = null;
/*  294 */       if (isBroken()) {
/*  295 */         pool.returnBrokenResource(this);
/*      */       } else {
/*  297 */         pool.returnResource(this);
/*      */       } 
/*      */     } else {
/*  300 */       this.connection.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Transaction multi() {
/*  306 */     this.transaction = new Transaction(this);
/*  307 */     return this.transaction;
/*      */   }
/*      */ 
/*      */   
/*      */   public Pipeline pipelined() {
/*  312 */     this.pipeline = new Pipeline(this);
/*  313 */     return this.pipeline;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkIsInMultiOrPipeline() {
/*  319 */     if (this.transaction != null) {
/*  320 */       throw new IllegalStateException("Cannot use Jedis when in Multi. Please use Transaction or reset jedis state.");
/*      */     }
/*  322 */     if (this.pipeline != null && this.pipeline.hasPipelinedResponse()) {
/*  323 */       throw new IllegalStateException("Cannot use Jedis when in Pipeline. Please use Pipeline or reset jedis state.");
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDB() {
/*  329 */     return this.db;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String ping() {
/*  337 */     checkIsInMultiOrPipeline();
/*  338 */     this.connection.sendCommand(Protocol.Command.PING);
/*  339 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] ping(byte[] message) {
/*  348 */     checkIsInMultiOrPipeline();
/*  349 */     this.connection.sendCommand(Protocol.Command.PING, new byte[][] { message });
/*  350 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String select(int index) {
/*  361 */     checkIsInMultiOrPipeline();
/*  362 */     this.connection.sendCommand(Protocol.Command.SELECT, new byte[][] { Protocol.toByteArray(index) });
/*  363 */     String statusCodeReply = this.connection.getStatusCodeReply();
/*  364 */     this.db = index;
/*  365 */     return statusCodeReply;
/*      */   }
/*      */ 
/*      */   
/*      */   public String swapDB(int index1, int index2) {
/*  370 */     checkIsInMultiOrPipeline();
/*  371 */     this.connection.sendCommand(Protocol.Command.SWAPDB, new byte[][] { Protocol.toByteArray(index1), Protocol.toByteArray(index2) });
/*  372 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String flushDB() {
/*  381 */     checkIsInMultiOrPipeline();
/*  382 */     return this.connection.<String>executeCommand(this.commandObjects.flushDB());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String flushDB(FlushMode flushMode) {
/*  392 */     checkIsInMultiOrPipeline();
/*  393 */     this.connection.sendCommand(Protocol.Command.FLUSHDB, new byte[][] { flushMode.getRaw() });
/*  394 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String flushAll() {
/*  404 */     checkIsInMultiOrPipeline();
/*  405 */     return this.connection.<String>executeCommand(this.commandObjects.flushAll());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String flushAll(FlushMode flushMode) {
/*  416 */     checkIsInMultiOrPipeline();
/*  417 */     this.connection.sendCommand(Protocol.Command.FLUSHALL, new byte[][] { flushMode.getRaw() });
/*  418 */     return this.connection.getStatusCodeReply();
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
/*      */   
/*      */   public boolean copy(byte[] srcKey, byte[] dstKey, int db, boolean replace) {
/*  431 */     checkIsInMultiOrPipeline();
/*  432 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.copy(srcKey, dstKey, db, replace))).booleanValue();
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
/*      */   public boolean copy(byte[] srcKey, byte[] dstKey, boolean replace) {
/*  444 */     checkIsInMultiOrPipeline();
/*  445 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.copy(srcKey, dstKey, replace))).booleanValue();
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
/*      */ 
/*      */   
/*      */   public String set(byte[] key, byte[] value) {
/*  459 */     checkIsInMultiOrPipeline();
/*  460 */     return this.connection.<String>executeCommand(this.commandObjects.set(key, value));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String set(byte[] key, byte[] value, SetParams params) {
/*  476 */     checkIsInMultiOrPipeline();
/*  477 */     return this.connection.<String>executeCommand(this.commandObjects.set(key, value, params));
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
/*      */ 
/*      */   
/*      */   public byte[] get(byte[] key) {
/*  491 */     checkIsInMultiOrPipeline();
/*  492 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.get(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] setGet(byte[] key, byte[] value) {
/*  497 */     checkIsInMultiOrPipeline();
/*  498 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.setGet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] setGet(byte[] key, byte[] value, SetParams params) {
/*  503 */     checkIsInMultiOrPipeline();
/*  504 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.setGet(key, value, params));
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
/*      */   
/*      */   public byte[] getDel(byte[] key) {
/*  517 */     checkIsInMultiOrPipeline();
/*  518 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.getDel(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getEx(byte[] key, GetExParams params) {
/*  523 */     checkIsInMultiOrPipeline();
/*  524 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.getEx(key, params));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long exists(byte[]... keys) {
/*  535 */     checkIsInMultiOrPipeline();
/*  536 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.exists(keys))).longValue();
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
/*      */   public boolean exists(byte[] key) {
/*  548 */     checkIsInMultiOrPipeline();
/*  549 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.exists(key))).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long del(byte[]... keys) {
/*  560 */     checkIsInMultiOrPipeline();
/*  561 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.del(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long del(byte[] key) {
/*  566 */     checkIsInMultiOrPipeline();
/*  567 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.del(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long unlink(byte[]... keys) {
/*  585 */     checkIsInMultiOrPipeline();
/*  586 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.unlink(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long unlink(byte[] key) {
/*  591 */     checkIsInMultiOrPipeline();
/*  592 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.unlink(key))).longValue();
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
/*      */   
/*      */   public String type(byte[] key) {
/*  605 */     checkIsInMultiOrPipeline();
/*  606 */     return this.connection.<String>executeCommand(this.commandObjects.type(key));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<byte[]> keys(byte[] pattern) {
/*  639 */     checkIsInMultiOrPipeline();
/*  640 */     return this.connection.<Set<byte[]>>executeCommand(this.commandObjects.keys(pattern));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] randomBinaryKey() {
/*  651 */     checkIsInMultiOrPipeline();
/*  652 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.randomBinaryKey());
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
/*      */ 
/*      */   
/*      */   public String rename(byte[] oldkey, byte[] newkey) {
/*  666 */     checkIsInMultiOrPipeline();
/*  667 */     return this.connection.<String>executeCommand(this.commandObjects.rename(oldkey, newkey));
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
/*      */   
/*      */   public long renamenx(byte[] oldkey, byte[] newkey) {
/*  680 */     checkIsInMultiOrPipeline();
/*  681 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.renamenx(oldkey, newkey))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long dbSize() {
/*  690 */     checkIsInMultiOrPipeline();
/*  691 */     this.connection.sendCommand(Protocol.Command.DBSIZE);
/*  692 */     return this.connection.getIntegerReply().longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long expire(byte[] key, long seconds) {
/*  716 */     checkIsInMultiOrPipeline();
/*  717 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expire(key, seconds))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expire(byte[] key, long seconds, ExpiryOption expiryOption) {
/*  722 */     checkIsInMultiOrPipeline();
/*  723 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expire(key, seconds, expiryOption))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long pexpire(byte[] key, long milliseconds) {
/*  747 */     checkIsInMultiOrPipeline();
/*  748 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpire(key, milliseconds))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpire(byte[] key, long milliseconds, ExpiryOption expiryOption) {
/*  753 */     checkIsInMultiOrPipeline();
/*  754 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpire(key, milliseconds, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireTime(byte[] key) {
/*  759 */     checkIsInMultiOrPipeline();
/*  760 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expireTime(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireTime(byte[] key) {
/*  765 */     checkIsInMultiOrPipeline();
/*  766 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpireTime(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long expireAt(byte[] key, long unixTime) {
/*  794 */     checkIsInMultiOrPipeline();
/*  795 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expireAt(key, unixTime))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long expireAt(byte[] key, long unixTime, ExpiryOption expiryOption) {
/*  800 */     checkIsInMultiOrPipeline();
/*  801 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expireAt(key, unixTime, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireAt(byte[] key, long millisecondsTimestamp) {
/*  806 */     checkIsInMultiOrPipeline();
/*  807 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpireAt(byte[] key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/*  812 */     checkIsInMultiOrPipeline();
/*  813 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp, expiryOption))).longValue();
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
/*      */   public long ttl(byte[] key) {
/*  825 */     checkIsInMultiOrPipeline();
/*  826 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.ttl(key))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long touch(byte[]... keys) {
/*  837 */     checkIsInMultiOrPipeline();
/*  838 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.touch(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long touch(byte[] key) {
/*  843 */     checkIsInMultiOrPipeline();
/*  844 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.touch(key))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long move(byte[] key, int dbIndex) {
/*  859 */     checkIsInMultiOrPipeline();
/*  860 */     this.connection.sendCommand(Protocol.Command.MOVE, new byte[][] { key, Protocol.toByteArray(dbIndex) });
/*  861 */     return this.connection.getIntegerReply().longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getSet(byte[] key, byte[] value) {
/*  876 */     checkIsInMultiOrPipeline();
/*  877 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.getSet(key, value));
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
/*      */ 
/*      */   
/*      */   public List<byte[]> mget(byte[]... keys) {
/*  891 */     checkIsInMultiOrPipeline();
/*  892 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.mget(keys));
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
/*      */ 
/*      */   
/*      */   public long setnx(byte[] key, byte[] value) {
/*  906 */     checkIsInMultiOrPipeline();
/*  907 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.setnx(key, value))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String setex(byte[] key, long seconds, byte[] value) {
/*  923 */     checkIsInMultiOrPipeline();
/*  924 */     return this.connection.<String>executeCommand(this.commandObjects.setex(key, seconds, value));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mset(byte[]... keysvalues) {
/*  945 */     checkIsInMultiOrPipeline();
/*  946 */     return this.connection.<String>executeCommand(this.commandObjects.mset(keysvalues));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long msetnx(byte[]... keysvalues) {
/*  968 */     checkIsInMultiOrPipeline();
/*  969 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.msetnx(keysvalues))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long decrBy(byte[] key, long decrement) {
/*  992 */     checkIsInMultiOrPipeline();
/*  993 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.decrBy(key, decrement))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long decr(byte[] key) {
/* 1015 */     checkIsInMultiOrPipeline();
/* 1016 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.decr(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long incrBy(byte[] key, long increment) {
/* 1039 */     checkIsInMultiOrPipeline();
/* 1040 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.incrBy(key, increment))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double incrByFloat(byte[] key, double increment) {
/* 1064 */     checkIsInMultiOrPipeline();
/* 1065 */     return ((Double)this.connection.<Double>executeCommand(this.commandObjects.incrByFloat(key, increment))).doubleValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long incr(byte[] key) {
/* 1087 */     checkIsInMultiOrPipeline();
/* 1088 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.incr(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long append(byte[] key, byte[] value) {
/* 1105 */     checkIsInMultiOrPipeline();
/* 1106 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.append(key, value))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] substr(byte[] key, int start, int end) {
/* 1127 */     checkIsInMultiOrPipeline();
/* 1128 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.substr(key, start, end));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long hset(byte[] key, byte[] field, byte[] value) {
/* 1145 */     checkIsInMultiOrPipeline();
/* 1146 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hset(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hset(byte[] key, Map<byte[], byte[]> hash) {
/* 1151 */     checkIsInMultiOrPipeline();
/* 1152 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hset(key, hash))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] hget(byte[] key, byte[] field) {
/* 1167 */     checkIsInMultiOrPipeline();
/* 1168 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.hget(key, field));
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
/*      */ 
/*      */   
/*      */   public long hsetnx(byte[] key, byte[] field, byte[] value) {
/* 1182 */     checkIsInMultiOrPipeline();
/* 1183 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hsetnx(key, field, value))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String hmset(byte[] key, Map<byte[], byte[]> hash) {
/* 1198 */     checkIsInMultiOrPipeline();
/* 1199 */     return this.connection.<String>executeCommand(this.commandObjects.hmset(key, hash));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> hmget(byte[] key, byte[]... fields) {
/* 1215 */     checkIsInMultiOrPipeline();
/* 1216 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.hmget(key, fields));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long hincrBy(byte[] key, byte[] field, long value) {
/* 1235 */     checkIsInMultiOrPipeline();
/* 1236 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hincrBy(key, field, value))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double hincrByFloat(byte[] key, byte[] field, double value) {
/* 1256 */     checkIsInMultiOrPipeline();
/* 1257 */     return ((Double)this.connection.<Double>executeCommand(this.commandObjects.hincrByFloat(key, field, value))).doubleValue();
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
/*      */   public boolean hexists(byte[] key, byte[] field) {
/* 1269 */     checkIsInMultiOrPipeline();
/* 1270 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.hexists(key, field))).booleanValue();
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
/*      */ 
/*      */   
/*      */   public long hdel(byte[] key, byte[]... fields) {
/* 1284 */     checkIsInMultiOrPipeline();
/* 1285 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hdel(key, fields))).longValue();
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
/*      */   
/*      */   public long hlen(byte[] key) {
/* 1298 */     checkIsInMultiOrPipeline();
/* 1299 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hlen(key))).longValue();
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
/*      */   public Set<byte[]> hkeys(byte[] key) {
/* 1311 */     checkIsInMultiOrPipeline();
/* 1312 */     return this.connection.<Set<byte[]>>executeCommand(this.commandObjects.hkeys(key));
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
/*      */   public List<byte[]> hvals(byte[] key) {
/* 1324 */     checkIsInMultiOrPipeline();
/* 1325 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.hvals(key));
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
/*      */   public Map<byte[], byte[]> hgetAll(byte[] key) {
/* 1337 */     checkIsInMultiOrPipeline();
/* 1338 */     return this.connection.<Map<byte[], byte[]>>executeCommand(this.commandObjects.hgetAll(key));
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
/*      */   public byte[] hrandfield(byte[] key) {
/* 1350 */     checkIsInMultiOrPipeline();
/* 1351 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.hrandfield(key));
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
/*      */   public List<byte[]> hrandfield(byte[] key, long count) {
/* 1363 */     checkIsInMultiOrPipeline();
/* 1364 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.hrandfield(key, count));
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
/*      */   public List<Map.Entry<byte[], byte[]>> hrandfieldWithValues(byte[] key, long count) {
/* 1376 */     checkIsInMultiOrPipeline();
/* 1377 */     return this.connection.<List<Map.Entry<byte[], byte[]>>>executeCommand(this.commandObjects.hrandfieldWithValues(key, count));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long rpush(byte[] key, byte[]... strings) {
/* 1392 */     checkIsInMultiOrPipeline();
/* 1393 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.rpush(key, strings))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long lpush(byte[] key, byte[]... strings) {
/* 1408 */     checkIsInMultiOrPipeline();
/* 1409 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.lpush(key, strings))).longValue();
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
/*      */ 
/*      */   
/*      */   public long llen(byte[] key) {
/* 1423 */     checkIsInMultiOrPipeline();
/* 1424 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.llen(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> lrange(byte[] key, long start, long stop) {
/* 1461 */     checkIsInMultiOrPipeline();
/* 1462 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.lrange(key, start, stop));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String ltrim(byte[] key, long start, long stop) {
/* 1497 */     checkIsInMultiOrPipeline();
/* 1498 */     return this.connection.<String>executeCommand(this.commandObjects.ltrim(key, start, stop));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] lindex(byte[] key, long index) {
/* 1519 */     checkIsInMultiOrPipeline();
/* 1520 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.lindex(key, index));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String lset(byte[] key, long index, byte[] value) {
/* 1544 */     checkIsInMultiOrPipeline();
/* 1545 */     return this.connection.<String>executeCommand(this.commandObjects.lset(key, index, value));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long lrem(byte[] key, long count, byte[] value) {
/* 1565 */     checkIsInMultiOrPipeline();
/* 1566 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.lrem(key, count, value))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] lpop(byte[] key) {
/* 1581 */     checkIsInMultiOrPipeline();
/* 1582 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.lpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> lpop(byte[] key, int count) {
/* 1587 */     checkIsInMultiOrPipeline();
/* 1588 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.lpop(key, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long lpos(byte[] key, byte[] element) {
/* 1605 */     checkIsInMultiOrPipeline();
/* 1606 */     return this.connection.<Long>executeCommand(this.commandObjects.lpos(key, element));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long lpos(byte[] key, byte[] element, LPosParams params) {
/* 1629 */     checkIsInMultiOrPipeline();
/* 1630 */     return this.connection.<Long>executeCommand(this.commandObjects.lpos(key, element, params));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Long> lpos(byte[] key, byte[] element, LPosParams params, long count) {
/* 1650 */     checkIsInMultiOrPipeline();
/* 1651 */     return this.connection.<List<Long>>executeCommand(this.commandObjects.lpos(key, element, params, count));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] rpop(byte[] key) {
/* 1666 */     checkIsInMultiOrPipeline();
/* 1667 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.rpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> rpop(byte[] key, int count) {
/* 1672 */     checkIsInMultiOrPipeline();
/* 1673 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.rpop(key, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
/* 1693 */     checkIsInMultiOrPipeline();
/* 1694 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.rpoplpush(srckey, dstkey));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long sadd(byte[] key, byte[]... members) {
/* 1710 */     checkIsInMultiOrPipeline();
/* 1711 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sadd(key, members))).longValue();
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
/*      */   
/*      */   public Set<byte[]> smembers(byte[] key) {
/* 1724 */     checkIsInMultiOrPipeline();
/* 1725 */     return this.connection.<Set<byte[]>>executeCommand(this.commandObjects.smembers(key));
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
/*      */ 
/*      */   
/*      */   public long srem(byte[] key, byte[]... members) {
/* 1739 */     checkIsInMultiOrPipeline();
/* 1740 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.srem(key, members))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] spop(byte[] key) {
/* 1756 */     checkIsInMultiOrPipeline();
/* 1757 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.spop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<byte[]> spop(byte[] key, long count) {
/* 1762 */     checkIsInMultiOrPipeline();
/* 1763 */     return this.connection.<Set<byte[]>>executeCommand(this.commandObjects.spop(key, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long smove(byte[] srckey, byte[] dstkey, byte[] member) {
/* 1786 */     checkIsInMultiOrPipeline();
/* 1787 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.smove(srckey, dstkey, member))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long scard(byte[] key) {
/* 1798 */     checkIsInMultiOrPipeline();
/* 1799 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.scard(key))).longValue();
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
/*      */   
/*      */   public boolean sismember(byte[] key, byte[] member) {
/* 1812 */     checkIsInMultiOrPipeline();
/* 1813 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.sismember(key, member))).booleanValue();
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
/*      */   
/*      */   public List<Boolean> smismember(byte[] key, byte[]... members) {
/* 1826 */     checkIsInMultiOrPipeline();
/* 1827 */     return this.connection.<List<Boolean>>executeCommand(this.commandObjects.smismember(key, members));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<byte[]> sinter(byte[]... keys) {
/* 1847 */     checkIsInMultiOrPipeline();
/* 1848 */     return this.connection.<Set<byte[]>>executeCommand(this.commandObjects.sinter(keys));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sinterstore(byte[] dstkey, byte[]... keys) {
/* 1863 */     checkIsInMultiOrPipeline();
/* 1864 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sinterstore(dstkey, keys))).longValue();
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
/*      */   
/*      */   public long sintercard(byte[]... keys) {
/* 1877 */     checkIsInMultiOrPipeline();
/* 1878 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sintercard(keys))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sintercard(int limit, byte[]... keys) {
/* 1893 */     checkIsInMultiOrPipeline();
/* 1894 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sintercard(limit, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<byte[]> sunion(byte[]... keys) {
/* 1912 */     checkIsInMultiOrPipeline();
/* 1913 */     return this.connection.<Set<byte[]>>executeCommand(this.commandObjects.sunion(keys));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sunionstore(byte[] dstkey, byte[]... keys) {
/* 1928 */     checkIsInMultiOrPipeline();
/* 1929 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sunionstore(dstkey, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<byte[]> sdiff(byte[]... keys) {
/* 1954 */     checkIsInMultiOrPipeline();
/* 1955 */     return this.connection.<Set<byte[]>>executeCommand(this.commandObjects.sdiff(keys));
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
/*      */   public long sdiffstore(byte[] dstkey, byte[]... keys) {
/* 1967 */     checkIsInMultiOrPipeline();
/* 1968 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sdiffstore(dstkey, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] srandmember(byte[] key) {
/* 1983 */     checkIsInMultiOrPipeline();
/* 1984 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.srandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> srandmember(byte[] key, int count) {
/* 1989 */     checkIsInMultiOrPipeline();
/* 1990 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.srandmember(key, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, double score, byte[] member) {
/* 2011 */     checkIsInMultiOrPipeline();
/* 2012 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, score, member))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
/* 2018 */     checkIsInMultiOrPipeline();
/* 2019 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, score, member, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
/* 2024 */     checkIsInMultiOrPipeline();
/* 2025 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, scoreMembers))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
/* 2030 */     checkIsInMultiOrPipeline();
/* 2031 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, scoreMembers, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zaddIncr(byte[] key, double score, byte[] member, ZAddParams params) {
/* 2036 */     checkIsInMultiOrPipeline();
/* 2037 */     return this.connection.<Double>executeCommand(this.commandObjects.zaddIncr(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrange(byte[] key, long start, long stop) {
/* 2042 */     checkIsInMultiOrPipeline();
/* 2043 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrange(key, start, stop));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long zrem(byte[] key, byte[]... members) {
/* 2058 */     checkIsInMultiOrPipeline();
/* 2059 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zrem(key, members))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double zincrby(byte[] key, double increment, byte[] member) {
/* 2082 */     checkIsInMultiOrPipeline();
/* 2083 */     return ((Double)this.connection.<Double>executeCommand(this.commandObjects.zincrby(key, increment, member))).doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Double zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
/* 2089 */     checkIsInMultiOrPipeline();
/* 2090 */     return this.connection.<Double>executeCommand(this.commandObjects.zincrby(key, increment, member, params));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long zrank(byte[] key, byte[] member) {
/* 2110 */     checkIsInMultiOrPipeline();
/* 2111 */     return this.connection.<Long>executeCommand(this.commandObjects.zrank(key, member));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long zrevrank(byte[] key, byte[] member) {
/* 2131 */     checkIsInMultiOrPipeline();
/* 2132 */     return this.connection.<Long>executeCommand(this.commandObjects.zrevrank(key, member));
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
/*      */   public KeyValue<Long, Double> zrankWithScore(byte[] key, byte[] member) {
/* 2144 */     checkIsInMultiOrPipeline();
/* 2145 */     return this.connection.<KeyValue<Long, Double>>executeCommand(this.commandObjects.zrankWithScore(key, member));
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
/*      */   public KeyValue<Long, Double> zrevrankWithScore(byte[] key, byte[] member) {
/* 2157 */     checkIsInMultiOrPipeline();
/* 2158 */     return this.connection.<KeyValue<Long, Double>>executeCommand(this.commandObjects.zrevrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrange(byte[] key, long start, long stop) {
/* 2163 */     checkIsInMultiOrPipeline();
/* 2164 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrevrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(byte[] key, long start, long stop) {
/* 2169 */     checkIsInMultiOrPipeline();
/* 2170 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeWithScores(byte[] key, long start, long stop) {
/* 2175 */     checkIsInMultiOrPipeline();
/* 2176 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrange(byte[] key, ZRangeParams zRangeParams) {
/* 2181 */     checkIsInMultiOrPipeline();
/* 2182 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrange(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(byte[] key, ZRangeParams zRangeParams) {
/* 2187 */     checkIsInMultiOrPipeline();
/* 2188 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeWithScores(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zrangestore(byte[] dest, byte[] src, ZRangeParams zRangeParams) {
/* 2193 */     checkIsInMultiOrPipeline();
/* 2194 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zrangestore(dest, src, zRangeParams))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] zrandmember(byte[] key) {
/* 2199 */     checkIsInMultiOrPipeline();
/* 2200 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.zrandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrandmember(byte[] key, long count) {
/* 2205 */     checkIsInMultiOrPipeline();
/* 2206 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrandmemberWithScores(byte[] key, long count) {
/* 2211 */     checkIsInMultiOrPipeline();
/* 2212 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrandmemberWithScores(key, count));
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
/*      */   
/*      */   public long zcard(byte[] key) {
/* 2225 */     checkIsInMultiOrPipeline();
/* 2226 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zcard(key))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public Double zscore(byte[] key, byte[] member) {
/* 2241 */     checkIsInMultiOrPipeline();
/* 2242 */     return this.connection.<Double>executeCommand(this.commandObjects.zscore(key, member));
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
/*      */ 
/*      */   
/*      */   public List<Double> zmscore(byte[] key, byte[]... members) {
/* 2256 */     checkIsInMultiOrPipeline();
/* 2257 */     return this.connection.<List<Double>>executeCommand(this.commandObjects.zmscore(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmax(byte[] key) {
/* 2262 */     checkIsInMultiOrPipeline();
/* 2263 */     return this.connection.<Tuple>executeCommand(this.commandObjects.zpopmax(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmax(byte[] key, int count) {
/* 2268 */     checkIsInMultiOrPipeline();
/* 2269 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zpopmax(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmin(byte[] key) {
/* 2274 */     checkIsInMultiOrPipeline();
/* 2275 */     return this.connection.<Tuple>executeCommand(this.commandObjects.zpopmin(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmin(byte[] key, int count) {
/* 2280 */     checkIsInMultiOrPipeline();
/* 2281 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zpopmin(key, count));
/*      */   }
/*      */   
/*      */   public String watch(byte[]... keys) {
/* 2285 */     checkIsInMultiOrPipeline();
/* 2286 */     this.connection.sendCommand(Protocol.Command.WATCH, keys);
/*      */     
/* 2288 */     String status = this.connection.getStatusCodeReply();
/* 2289 */     this.isInWatch = true;
/* 2290 */     return status;
/*      */   }
/*      */   
/*      */   public String unwatch() {
/* 2294 */     checkIsInMultiOrPipeline();
/* 2295 */     this.connection.sendCommand(Protocol.Command.UNWATCH);
/* 2296 */     return this.connection.getStatusCodeReply();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> sort(byte[] key) {
/* 2314 */     checkIsInMultiOrPipeline();
/* 2315 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.sort(key));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> sort(byte[] key, SortingParams sortingParams) {
/* 2394 */     checkIsInMultiOrPipeline();
/* 2395 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.sort(key, sortingParams));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sort(byte[] key, SortingParams sortingParams, byte[] dstkey) {
/* 2410 */     checkIsInMultiOrPipeline();
/* 2411 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sort(key, sortingParams, dstkey))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long sort(byte[] key, byte[] dstkey) {
/* 2429 */     checkIsInMultiOrPipeline();
/* 2430 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sort(key, dstkey))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> sortReadonly(byte[] key, SortingParams sortingParams) {
/* 2435 */     checkIsInMultiOrPipeline();
/* 2436 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.sortReadonly(key, sortingParams));
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
/*      */   
/*      */   public byte[] lmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to) {
/* 2449 */     checkIsInMultiOrPipeline();
/* 2450 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.lmove(srcKey, dstKey, from, to));
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
/*      */ 
/*      */   
/*      */   public byte[] blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to, double timeout) {
/* 2464 */     checkIsInMultiOrPipeline();
/* 2465 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.blmove(srcKey, dstKey, from, to, timeout));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> blpop(int timeout, byte[]... keys) {
/* 2531 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], byte[]> blpop(double timeout, byte[]... keys) {
/* 2536 */     return this.connection.<KeyValue<byte[], byte[]>>executeCommand(this.commandObjects.blpop(timeout, keys));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> brpop(int timeout, byte[]... keys) {
/* 2602 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], byte[]> brpop(double timeout, byte[]... keys) {
/* 2607 */     return this.connection.<KeyValue<byte[], byte[]>>executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> lmpop(ListDirection direction, byte[]... keys) {
/* 2612 */     checkIsInMultiOrPipeline();
/* 2613 */     return this.connection.<KeyValue<byte[], List<byte[]>>>executeCommand(this.commandObjects.lmpop(direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> lmpop(ListDirection direction, int count, byte[]... keys) {
/* 2618 */     checkIsInMultiOrPipeline();
/* 2619 */     return this.connection.<KeyValue<byte[], List<byte[]>>>executeCommand(this.commandObjects.lmpop(direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> blmpop(double timeout, ListDirection direction, byte[]... keys) {
/* 2624 */     checkIsInMultiOrPipeline();
/* 2625 */     return this.connection.<KeyValue<byte[], List<byte[]>>>executeCommand(this.commandObjects.blmpop(timeout, direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<byte[]>> blmpop(double timeout, ListDirection direction, int count, byte[]... keys) {
/* 2630 */     checkIsInMultiOrPipeline();
/* 2631 */     return this.connection.<KeyValue<byte[], List<byte[]>>>executeCommand(this.commandObjects.blmpop(timeout, direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], Tuple> bzpopmax(double timeout, byte[]... keys) {
/* 2636 */     return this.connection.<KeyValue<byte[], Tuple>>executeCommand(this.commandObjects.bzpopmax(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], Tuple> bzpopmin(double timeout, byte[]... keys) {
/* 2641 */     return this.connection.<KeyValue<byte[], Tuple>>executeCommand(this.commandObjects.bzpopmin(timeout, keys));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String auth(String password) {
/* 2658 */     checkIsInMultiOrPipeline();
/* 2659 */     this.connection.sendCommand(Protocol.Command.AUTH, new String[] { password });
/* 2660 */     return this.connection.getStatusCodeReply();
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
/*      */   
/*      */   public String auth(String user, String password) {
/* 2673 */     checkIsInMultiOrPipeline();
/* 2674 */     this.connection.sendCommand(Protocol.Command.AUTH, new String[] { user, password });
/* 2675 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(byte[] key, double min, double max) {
/* 2680 */     checkIsInMultiOrPipeline();
/* 2681 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(byte[] key, byte[] min, byte[] max) {
/* 2686 */     checkIsInMultiOrPipeline();
/* 2687 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zdiff(byte[]... keys) {
/* 2692 */     checkIsInMultiOrPipeline();
/* 2693 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zdiffWithScores(byte[]... keys) {
/* 2698 */     checkIsInMultiOrPipeline();
/* 2699 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zdiffWithScores(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long zdiffStore(byte[] dstkey, byte[]... keys) {
/* 2705 */     checkIsInMultiOrPipeline();
/* 2706 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zdiffStore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zdiffstore(byte[] dstkey, byte[]... keys) {
/* 2711 */     checkIsInMultiOrPipeline();
/* 2712 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zdiffstore(dstkey, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, double min, double max) {
/* 2764 */     checkIsInMultiOrPipeline();
/* 2765 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 2770 */     checkIsInMultiOrPipeline();
/* 2771 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrangeByScore(key, min, max));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
/* 2826 */     checkIsInMultiOrPipeline();
/* 2827 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2833 */     checkIsInMultiOrPipeline();
/* 2834 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
/* 2886 */     checkIsInMultiOrPipeline();
/* 2887 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
/* 2892 */     checkIsInMultiOrPipeline();
/* 2893 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
/* 2948 */     checkIsInMultiOrPipeline();
/* 2949 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2955 */     checkIsInMultiOrPipeline();
/* 2956 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
/* 2961 */     checkIsInMultiOrPipeline();
/* 2962 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
/* 2967 */     checkIsInMultiOrPipeline();
/* 2968 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
/* 2974 */     checkIsInMultiOrPipeline();
/* 2975 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 2981 */     checkIsInMultiOrPipeline();
/* 2982 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
/* 2987 */     checkIsInMultiOrPipeline();
/* 2988 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
/* 2994 */     checkIsInMultiOrPipeline();
/* 2995 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
/* 3000 */     checkIsInMultiOrPipeline();
/* 3001 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 3007 */     checkIsInMultiOrPipeline();
/* 3008 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zremrangeByRank(byte[] key, long start, long stop) {
/* 3027 */     checkIsInMultiOrPipeline();
/* 3028 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByRank(key, start, stop))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(byte[] key, double min, double max) {
/* 3046 */     checkIsInMultiOrPipeline();
/* 3047 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 3052 */     checkIsInMultiOrPipeline();
/* 3053 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
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
/*      */   public List<byte[]> zunion(ZParams params, byte[]... keys) {
/* 3065 */     checkIsInMultiOrPipeline();
/* 3066 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zunion(params, keys));
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
/*      */   public List<Tuple> zunionWithScores(ZParams params, byte[]... keys) {
/* 3078 */     checkIsInMultiOrPipeline();
/* 3079 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zunionWithScores(params, keys));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zunionstore(byte[] dstkey, byte[]... sets) {
/* 3109 */     checkIsInMultiOrPipeline();
/* 3110 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zunionstore(dstkey, sets))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 3142 */     checkIsInMultiOrPipeline();
/* 3143 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zunionstore(dstkey, params, sets))).longValue();
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
/*      */   public List<byte[]> zinter(ZParams params, byte[]... keys) {
/* 3155 */     checkIsInMultiOrPipeline();
/* 3156 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zinter(params, keys));
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
/*      */   public List<Tuple> zinterWithScores(ZParams params, byte[]... keys) {
/* 3168 */     checkIsInMultiOrPipeline();
/* 3169 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zinterWithScores(params, keys));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zinterstore(byte[] dstkey, byte[]... sets) {
/* 3200 */     checkIsInMultiOrPipeline();
/* 3201 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zinterstore(dstkey, sets))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 3233 */     checkIsInMultiOrPipeline();
/* 3234 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zinterstore(dstkey, params, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(byte[]... keys) {
/* 3239 */     checkIsInMultiOrPipeline();
/* 3240 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zintercard(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(long limit, byte[]... keys) {
/* 3245 */     checkIsInMultiOrPipeline();
/* 3246 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zintercard(limit, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zlexcount(byte[] key, byte[] min, byte[] max) {
/* 3251 */     checkIsInMultiOrPipeline();
/* 3252 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zlexcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 3257 */     checkIsInMultiOrPipeline();
/* 3258 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 3264 */     checkIsInMultiOrPipeline();
/* 3265 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrangeByLex(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
/* 3270 */     checkIsInMultiOrPipeline();
/* 3271 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrevrangeByLex(key, max, min));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 3277 */     checkIsInMultiOrPipeline();
/* 3278 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.zrevrangeByLex(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 3283 */     checkIsInMultiOrPipeline();
/* 3284 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByLex(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption option, byte[]... keys) {
/* 3289 */     checkIsInMultiOrPipeline();
/* 3290 */     return this.connection.<KeyValue<byte[], List<Tuple>>>executeCommand(this.commandObjects.zmpop(option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> zmpop(SortedSetOption option, int count, byte[]... keys) {
/* 3295 */     checkIsInMultiOrPipeline();
/* 3296 */     return this.connection.<KeyValue<byte[], List<Tuple>>>executeCommand(this.commandObjects.zmpop(option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> bzmpop(double timeout, SortedSetOption option, byte[]... keys) {
/* 3301 */     checkIsInMultiOrPipeline();
/* 3302 */     return this.connection.<KeyValue<byte[], List<Tuple>>>executeCommand(this.commandObjects.bzmpop(timeout, option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<byte[], List<Tuple>> bzmpop(double timeout, SortedSetOption option, int count, byte[]... keys) {
/* 3307 */     checkIsInMultiOrPipeline();
/* 3308 */     return this.connection.<KeyValue<byte[], List<Tuple>>>executeCommand(this.commandObjects.bzmpop(timeout, option, count, keys));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String save() {
/* 3326 */     this.connection.sendCommand(Protocol.Command.SAVE);
/* 3327 */     return this.connection.getStatusCodeReply();
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
/*      */   
/*      */   public String bgsave() {
/* 3340 */     this.connection.sendCommand(Protocol.Command.BGSAVE);
/* 3341 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String bgsaveSchedule() {
/* 3346 */     this.connection.sendCommand(Protocol.Command.BGSAVE, Protocol.Keyword.SCHEDULE);
/* 3347 */     return this.connection.getStatusCodeReply();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String bgrewriteaof() {
/* 3366 */     this.connection.sendCommand(Protocol.Command.BGREWRITEAOF);
/* 3367 */     return this.connection.getStatusCodeReply();
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
/*      */   
/*      */   public long lastsave() {
/* 3380 */     this.connection.sendCommand(Protocol.Command.LASTSAVE);
/* 3381 */     return this.connection.getIntegerReply().longValue();
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
/*      */   
/*      */   public void shutdown() throws JedisException {
/* 3394 */     this.connection.sendCommand(Protocol.Command.SHUTDOWN);
/*      */     try {
/* 3396 */       throw new JedisException(this.connection.getStatusCodeReply());
/* 3397 */     } catch (JedisConnectionException jce) {
/*      */       
/* 3399 */       this.connection.setBroken();
/*      */       return;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void shutdown(ShutdownParams shutdownParams) throws JedisException {
/* 3405 */     this.connection.sendCommand((new CommandArguments(Protocol.Command.SHUTDOWN)).addParams((IParams)shutdownParams));
/*      */     try {
/* 3407 */       throw new JedisException(this.connection.getStatusCodeReply());
/* 3408 */     } catch (JedisConnectionException jce) {
/*      */       
/* 3410 */       this.connection.setBroken();
/*      */       return;
/*      */     } 
/*      */   }
/*      */   
/*      */   public String shutdownAbort() {
/* 3416 */     this.connection.sendCommand(Protocol.Command.SHUTDOWN, Protocol.Keyword.ABORT);
/* 3417 */     return this.connection.getStatusCodeReply();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String info() {
/* 3458 */     this.connection.sendCommand(Protocol.Command.INFO);
/* 3459 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String info(String section) {
/* 3464 */     this.connection.sendCommand(Protocol.Command.INFO, new String[] { section });
/* 3465 */     return this.connection.getBulkReply();
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
/*      */   
/*      */   public void monitor(JedisMonitor jedisMonitor) {
/* 3478 */     this.connection.sendCommand(Protocol.Command.MONITOR);
/* 3479 */     this.connection.getStatusCodeReply();
/* 3480 */     jedisMonitor.proceed(this.connection);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String slaveof(String host, int port) {
/* 3507 */     this.connection.sendCommand(Protocol.Command.SLAVEOF, new byte[][] { SafeEncoder.encode(host), Protocol.toByteArray(port) });
/* 3508 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String slaveofNoOne() {
/* 3517 */     this.connection.sendCommand(Protocol.Command.SLAVEOF, new byte[][] { Protocol.Keyword.NO.getRaw(), Protocol.Keyword.ONE.getRaw() });
/* 3518 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String replicaof(String host, int port) {
/* 3523 */     this.connection.sendCommand(Protocol.Command.REPLICAOF, new byte[][] { SafeEncoder.encode(host), Protocol.toByteArray(port) });
/* 3524 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String replicaofNoOne() {
/* 3529 */     this.connection.sendCommand(Protocol.Command.REPLICAOF, new byte[][] { Protocol.Keyword.NO.getRaw(), Protocol.Keyword.ONE.getRaw() });
/* 3530 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> roleBinary() {
/* 3535 */     checkIsInMultiOrPipeline();
/* 3536 */     this.connection.sendCommand(Protocol.Command.ROLE);
/* 3537 */     return BuilderFactory.RAW_OBJECT_LIST.build(this.connection.getOne());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<byte[], byte[]> configGet(byte[] pattern) {
/* 3576 */     checkIsInMultiOrPipeline();
/* 3577 */     this.connection.sendCommand(Protocol.Command.CONFIG, new byte[][] { Protocol.Keyword.GET.getRaw(), pattern });
/* 3578 */     return BuilderFactory.BINARY_MAP.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<byte[], byte[]> configGet(byte[]... patterns) {
/* 3583 */     checkIsInMultiOrPipeline();
/* 3584 */     this.connection.sendCommand(Protocol.Command.CONFIG, joinParameters(Protocol.Keyword.GET.getRaw(), patterns));
/* 3585 */     return BuilderFactory.BINARY_MAP.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String configResetStat() {
/* 3593 */     checkIsInMultiOrPipeline();
/* 3594 */     this.connection.sendCommand(Protocol.Command.CONFIG, Protocol.Keyword.RESETSTAT);
/* 3595 */     return this.connection.getStatusCodeReply();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String configRewrite() {
/* 3626 */     checkIsInMultiOrPipeline();
/* 3627 */     this.connection.sendCommand(Protocol.Command.CONFIG, Protocol.Keyword.REWRITE);
/* 3628 */     return this.connection.getStatusCodeReply();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String configSet(byte[] parameter, byte[] value) {
/* 3662 */     checkIsInMultiOrPipeline();
/* 3663 */     this.connection.sendCommand(Protocol.Command.CONFIG, new byte[][] { Protocol.Keyword.SET.getRaw(), parameter, value });
/* 3664 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String configSet(byte[]... parameterValues) {
/* 3669 */     checkIsInMultiOrPipeline();
/* 3670 */     this.connection.sendCommand(Protocol.Command.CONFIG, joinParameters(Protocol.Keyword.SET.getRaw(), parameterValues));
/* 3671 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String configSetBinary(Map<byte[], byte[]> parameterValues) {
/* 3676 */     checkIsInMultiOrPipeline();
/* 3677 */     CommandArguments args = (new CommandArguments(Protocol.Command.CONFIG)).add(Protocol.Keyword.SET);
/* 3678 */     parameterValues.forEach((k, v) -> args.add(k).add(v));
/* 3679 */     this.connection.sendCommand(args);
/* 3680 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long strlen(byte[] key) {
/* 3685 */     checkIsInMultiOrPipeline();
/* 3686 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.strlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public LCSMatchResult lcs(byte[] keyA, byte[] keyB, LCSParams params) {
/* 3691 */     checkIsInMultiOrPipeline();
/* 3692 */     return this.connection.<LCSMatchResult>executeCommand(this.commandObjects.lcs(keyA, keyB, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long lpushx(byte[] key, byte[]... strings) {
/* 3697 */     checkIsInMultiOrPipeline();
/* 3698 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.lpushx(key, strings))).longValue();
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
/*      */   public long persist(byte[] key) {
/* 3710 */     checkIsInMultiOrPipeline();
/* 3711 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.persist(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long rpushx(byte[] key, byte[]... strings) {
/* 3716 */     checkIsInMultiOrPipeline();
/* 3717 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.rpushx(key, strings))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] echo(byte[] string) {
/* 3722 */     checkIsInMultiOrPipeline();
/* 3723 */     this.connection.sendCommand(Protocol.Command.ECHO, new byte[][] { string });
/* 3724 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value) {
/* 3730 */     checkIsInMultiOrPipeline();
/* 3731 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.linsert(key, where, pivot, value))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
/* 3739 */     checkIsInMultiOrPipeline();
/* 3740 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.brpoplpush(source, destination, timeout));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setbit(byte[] key, long offset, boolean value) {
/* 3748 */     checkIsInMultiOrPipeline();
/* 3749 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.setbit(key, offset, value))).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getbit(byte[] key, long offset) {
/* 3757 */     checkIsInMultiOrPipeline();
/* 3758 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.getbit(key, offset))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(byte[] key, boolean value) {
/* 3763 */     return bitpos(key, value, new BitPosParams());
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(byte[] key, boolean value, BitPosParams params) {
/* 3768 */     checkIsInMultiOrPipeline();
/* 3769 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitpos(key, value, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long setrange(byte[] key, long offset, byte[] value) {
/* 3774 */     checkIsInMultiOrPipeline();
/* 3775 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.setrange(key, offset, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] getrange(byte[] key, long startOffset, long endOffset) {
/* 3780 */     checkIsInMultiOrPipeline();
/* 3781 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.getrange(key, startOffset, endOffset));
/*      */   }
/*      */   
/*      */   public long publish(byte[] channel, byte[] message) {
/* 3785 */     checkIsInMultiOrPipeline();
/* 3786 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.publish(channel, message))).longValue();
/*      */   }
/*      */   
/*      */   public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
/* 3790 */     jedisPubSub.proceed(this.connection, channels);
/*      */   }
/*      */   
/*      */   public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
/* 3794 */     jedisPubSub.proceedWithPatterns(this.connection, patterns);
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
/*      */   public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 3806 */     checkIsInMultiOrPipeline();
/* 3807 */     return this.connection.executeCommand(this.commandObjects.eval(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalReadonly(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 3812 */     checkIsInMultiOrPipeline();
/* 3813 */     return this.connection.executeCommand(this.commandObjects.evalReadonly(script, keys, args));
/*      */   }
/*      */   
/*      */   protected static byte[][] getParamsWithBinary(List<byte[]> keys, List<byte[]> args) {
/* 3817 */     int keyCount = keys.size();
/* 3818 */     int argCount = args.size();
/* 3819 */     byte[][] params = new byte[keyCount + argCount][];
/*      */     int i;
/* 3821 */     for (i = 0; i < keyCount; i++) {
/* 3822 */       params[i] = keys.get(i);
/*      */     }
/* 3824 */     for (i = 0; i < argCount; i++) {
/* 3825 */       params[keyCount + i] = args.get(i);
/*      */     }
/* 3827 */     return params;
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(byte[] script, int keyCount, byte[]... params) {
/* 3832 */     checkIsInMultiOrPipeline();
/* 3833 */     return this.connection.executeCommand(this.commandObjects.eval(script, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(byte[] script) {
/* 3838 */     checkIsInMultiOrPipeline();
/* 3839 */     return this.connection.executeCommand(this.commandObjects.eval(script));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(byte[] sha1) {
/* 3844 */     checkIsInMultiOrPipeline();
/* 3845 */     return this.connection.executeCommand(this.commandObjects.evalsha(sha1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 3850 */     checkIsInMultiOrPipeline();
/* 3851 */     return this.connection.executeCommand(this.commandObjects.evalsha(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalshaReadonly(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 3856 */     checkIsInMultiOrPipeline();
/* 3857 */     return this.connection.executeCommand(this.commandObjects.evalshaReadonly(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
/* 3862 */     checkIsInMultiOrPipeline();
/* 3863 */     return this.connection.executeCommand(this.commandObjects.evalsha(sha1, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptFlush() {
/* 3868 */     this.connection.sendCommand(Protocol.Command.SCRIPT, Protocol.Keyword.FLUSH);
/* 3869 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptFlush(FlushMode flushMode) {
/* 3874 */     this.connection.sendCommand(Protocol.Command.SCRIPT, new byte[][] { Protocol.Keyword.FLUSH.getRaw(), flushMode.getRaw() });
/* 3875 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Boolean scriptExists(byte[] sha1) {
/* 3880 */     byte[][] a = new byte[1][];
/* 3881 */     a[0] = sha1;
/* 3882 */     return scriptExists(a).get(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> scriptExists(byte[]... sha1) {
/* 3887 */     this.connection.sendCommand(Protocol.Command.SCRIPT, joinParameters(Protocol.Keyword.EXISTS.getRaw(), sha1));
/* 3888 */     return BuilderFactory.BOOLEAN_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] scriptLoad(byte[] script) {
/* 3893 */     this.connection.sendCommand(Protocol.Command.SCRIPT, new byte[][] { Protocol.Keyword.LOAD.getRaw(), script });
/* 3894 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptKill() {
/* 3899 */     return this.connection.<String>executeCommand(this.commandObjects.scriptKill());
/*      */   }
/*      */ 
/*      */   
/*      */   public String slowlogReset() {
/* 3904 */     return this.connection.<String>executeCommand(this.commandObjects.slowlogReset());
/*      */   }
/*      */ 
/*      */   
/*      */   public long slowlogLen() {
/* 3909 */     this.connection.sendCommand(Protocol.Command.SLOWLOG, Protocol.Keyword.LEN);
/* 3910 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> slowlogGetBinary() {
/* 3915 */     this.connection.sendCommand(Protocol.Command.SLOWLOG, Protocol.Keyword.GET);
/* 3916 */     return this.connection.getObjectMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> slowlogGetBinary(long entries) {
/* 3921 */     this.connection.sendCommand(Protocol.Command.SLOWLOG, new byte[][] { Protocol.Keyword.GET.getRaw(), Protocol.toByteArray(entries) });
/* 3922 */     return this.connection.getObjectMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectRefcount(byte[] key) {
/* 3927 */     this.connection.sendCommand(Protocol.Command.OBJECT, new byte[][] { Protocol.Keyword.REFCOUNT.getRaw(), key });
/* 3928 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] objectEncoding(byte[] key) {
/* 3933 */     this.connection.sendCommand(Protocol.Command.OBJECT, new byte[][] { Protocol.Keyword.ENCODING.getRaw(), key });
/* 3934 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectIdletime(byte[] key) {
/* 3939 */     this.connection.sendCommand(Protocol.Command.OBJECT, new byte[][] { Protocol.Keyword.IDLETIME.getRaw(), key });
/* 3940 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> objectHelpBinary() {
/* 3945 */     this.connection.sendCommand(Protocol.Command.OBJECT, Protocol.Keyword.HELP);
/* 3946 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectFreq(byte[] key) {
/* 3951 */     this.connection.sendCommand(Protocol.Command.OBJECT, new byte[][] { Protocol.Keyword.FREQ.getRaw(), key });
/* 3952 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(byte[] key) {
/* 3957 */     checkIsInMultiOrPipeline();
/* 3958 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(byte[] key, long start, long end) {
/* 3963 */     checkIsInMultiOrPipeline();
/* 3964 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitcount(key, start, end))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(byte[] key, long start, long end, BitCountOption option) {
/* 3969 */     checkIsInMultiOrPipeline();
/* 3970 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitcount(key, start, end, option))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
/* 3975 */     checkIsInMultiOrPipeline();
/* 3976 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitop(op, destKey, srcKeys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] dump(byte[] key) {
/* 3981 */     checkIsInMultiOrPipeline();
/* 3982 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.dump(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String restore(byte[] key, long ttl, byte[] serializedValue) {
/* 3987 */     checkIsInMultiOrPipeline();
/* 3988 */     return this.connection.<String>executeCommand(this.commandObjects.restore(key, ttl, serializedValue));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String restore(byte[] key, long ttl, byte[] serializedValue, RestoreParams params) {
/* 3994 */     checkIsInMultiOrPipeline();
/* 3995 */     return this.connection.<String>executeCommand(this.commandObjects.restore(key, ttl, serializedValue, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long pttl(byte[] key) {
/* 4000 */     checkIsInMultiOrPipeline();
/* 4001 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pttl(key))).longValue();
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
/*      */   
/*      */   public String psetex(byte[] key, long milliseconds, byte[] value) {
/* 4014 */     checkIsInMultiOrPipeline();
/* 4015 */     return this.connection.<String>executeCommand(this.commandObjects.psetex(key, milliseconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] memoryDoctorBinary() {
/* 4020 */     checkIsInMultiOrPipeline();
/* 4021 */     this.connection.sendCommand(Protocol.Command.MEMORY, Protocol.Keyword.DOCTOR);
/* 4022 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(byte[] key) {
/* 4027 */     checkIsInMultiOrPipeline();
/* 4028 */     this.connection.sendCommand(Protocol.Command.MEMORY, new byte[][] { Protocol.Keyword.USAGE.getRaw(), key });
/* 4029 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(byte[] key, int samples) {
/* 4034 */     checkIsInMultiOrPipeline();
/* 4035 */     this.connection.sendCommand(Protocol.Command.MEMORY, new byte[][] { Protocol.Keyword.USAGE.getRaw(), key, Protocol.Keyword.SAMPLES.getRaw(), Protocol.toByteArray(samples) });
/* 4036 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String failover() {
/* 4041 */     checkIsInMultiOrPipeline();
/* 4042 */     this.connection.sendCommand(Protocol.Command.FAILOVER);
/* 4043 */     this.connection.setTimeoutInfinite();
/*      */     try {
/* 4045 */       return this.connection.getStatusCodeReply();
/*      */     } finally {
/* 4047 */       this.connection.rollbackTimeout();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String failover(FailoverParams failoverParams) {
/* 4053 */     checkIsInMultiOrPipeline();
/* 4054 */     CommandArguments args = (new ClusterCommandArguments(Protocol.Command.FAILOVER)).addParams((IParams)failoverParams);
/* 4055 */     this.connection.sendCommand(args);
/* 4056 */     this.connection.setTimeoutInfinite();
/*      */     try {
/* 4058 */       return this.connection.getStatusCodeReply();
/*      */     } finally {
/* 4060 */       this.connection.rollbackTimeout();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String failoverAbort() {
/* 4066 */     checkIsInMultiOrPipeline();
/* 4067 */     this.connection.sendCommand(Protocol.Command.FAILOVER, Protocol.Keyword.ABORT);
/* 4068 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] aclWhoAmIBinary() {
/* 4073 */     checkIsInMultiOrPipeline();
/* 4074 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.WHOAMI);
/* 4075 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] aclGenPassBinary() {
/* 4080 */     checkIsInMultiOrPipeline();
/* 4081 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.GENPASS);
/* 4082 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] aclGenPassBinary(int bits) {
/* 4087 */     checkIsInMultiOrPipeline();
/* 4088 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.GENPASS.getRaw(), Protocol.toByteArray(bits) });
/* 4089 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> aclListBinary() {
/* 4094 */     checkIsInMultiOrPipeline();
/* 4095 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.LIST);
/* 4096 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> aclUsersBinary() {
/* 4101 */     checkIsInMultiOrPipeline();
/* 4102 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.USERS);
/* 4103 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public AccessControlUser aclGetUser(byte[] name) {
/* 4108 */     checkIsInMultiOrPipeline();
/* 4109 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.GETUSER.getRaw(), name });
/* 4110 */     return BuilderFactory.ACCESS_CONTROL_USER.build(this.connection.getObjectMultiBulkReply());
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclSetUser(byte[] name) {
/* 4115 */     checkIsInMultiOrPipeline();
/* 4116 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.SETUSER.getRaw(), name });
/* 4117 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclSetUser(byte[] name, byte[]... rules) {
/* 4122 */     checkIsInMultiOrPipeline();
/* 4123 */     this.connection.sendCommand(Protocol.Command.ACL, joinParameters(Protocol.Keyword.SETUSER.getRaw(), name, rules));
/* 4124 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long aclDelUser(byte[]... names) {
/* 4129 */     checkIsInMultiOrPipeline();
/* 4130 */     this.connection.sendCommand(Protocol.Command.ACL, joinParameters(Protocol.Keyword.DELUSER.getRaw(), names));
/* 4131 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> aclCatBinary() {
/* 4136 */     checkIsInMultiOrPipeline();
/* 4137 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.CAT);
/* 4138 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> aclCat(byte[] category) {
/* 4143 */     checkIsInMultiOrPipeline();
/* 4144 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.CAT.getRaw(), category });
/* 4145 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> aclLogBinary() {
/* 4150 */     checkIsInMultiOrPipeline();
/* 4151 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.LOG);
/* 4152 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> aclLogBinary(int limit) {
/* 4157 */     checkIsInMultiOrPipeline();
/* 4158 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.LOG.getRaw(), Protocol.toByteArray(limit) });
/* 4159 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclLogReset() {
/* 4164 */     checkIsInMultiOrPipeline();
/* 4165 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.LOG.getRaw(), Protocol.Keyword.RESET.getRaw() });
/* 4166 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientKill(byte[] ipPort) {
/* 4171 */     checkIsInMultiOrPipeline();
/* 4172 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.KILL.getRaw(), ipPort });
/* 4173 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientKill(String ip, int port) {
/* 4178 */     return clientKill(ip + ':' + port);
/*      */   }
/*      */ 
/*      */   
/*      */   public long clientKill(ClientKillParams params) {
/* 4183 */     checkIsInMultiOrPipeline();
/* 4184 */     this.connection.sendCommand((new CommandArguments(Protocol.Command.CLIENT)).add(Protocol.Keyword.KILL).addParams((IParams)params));
/* 4185 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] clientGetnameBinary() {
/* 4190 */     checkIsInMultiOrPipeline();
/* 4191 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.GETNAME);
/* 4192 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] clientListBinary() {
/* 4197 */     checkIsInMultiOrPipeline();
/* 4198 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.LIST);
/* 4199 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] clientListBinary(ClientType type) {
/* 4204 */     checkIsInMultiOrPipeline();
/* 4205 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.LIST.getRaw(), type.getRaw() });
/* 4206 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] clientListBinary(long... clientIds) {
/* 4211 */     checkIsInMultiOrPipeline();
/* 4212 */     this.connection.sendCommand(Protocol.Command.CLIENT, clientListParams(clientIds));
/* 4213 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */   
/*      */   private byte[][] clientListParams(long... clientIds) {
/* 4217 */     byte[][] params = new byte[2 + clientIds.length][];
/* 4218 */     int index = 0;
/* 4219 */     params[index++] = Protocol.Keyword.LIST.getRaw();
/* 4220 */     params[index++] = Protocol.Keyword.ID.getRaw();
/* 4221 */     for (long clientId : clientIds) {
/* 4222 */       params[index++] = Protocol.toByteArray(clientId);
/*      */     }
/* 4224 */     return params;
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] clientInfoBinary() {
/* 4229 */     checkIsInMultiOrPipeline();
/* 4230 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.INFO);
/* 4231 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientSetInfo(ClientAttributeOption attr, byte[] value) {
/* 4236 */     checkIsInMultiOrPipeline();
/* 4237 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.SETINFO.getRaw(), attr.getRaw(), value });
/* 4238 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientSetname(byte[] name) {
/* 4243 */     checkIsInMultiOrPipeline();
/* 4244 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.SETNAME.getRaw(), name });
/* 4245 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long clientId() {
/* 4250 */     checkIsInMultiOrPipeline();
/* 4251 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.ID);
/* 4252 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long clientUnblock(long clientId) {
/* 4261 */     checkIsInMultiOrPipeline();
/* 4262 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.UNBLOCK.getRaw(), Protocol.toByteArray(clientId) });
/* 4263 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long clientUnblock(long clientId, UnblockType unblockType) {
/* 4273 */     checkIsInMultiOrPipeline();
/* 4274 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.UNBLOCK.getRaw(), Protocol.toByteArray(clientId), unblockType.getRaw() });
/* 4275 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientPause(long timeout) {
/* 4280 */     checkIsInMultiOrPipeline();
/* 4281 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.PAUSE.getRaw(), Protocol.toByteArray(timeout) });
/* 4282 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientPause(long timeout, ClientPauseMode mode) {
/* 4287 */     checkIsInMultiOrPipeline();
/* 4288 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.PAUSE.getRaw(), Protocol.toByteArray(timeout), mode.getRaw() });
/* 4289 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientUnpause() {
/* 4294 */     checkIsInMultiOrPipeline();
/* 4295 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.UNPAUSE);
/* 4296 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientNoEvictOn() {
/* 4301 */     checkIsInMultiOrPipeline();
/* 4302 */     this.connection.sendCommand(Protocol.Command.CLIENT, new String[] { "NO-EVICT", "ON" });
/* 4303 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientNoEvictOff() {
/* 4308 */     checkIsInMultiOrPipeline();
/* 4309 */     this.connection.sendCommand(Protocol.Command.CLIENT, new String[] { "NO-EVICT", "OFF" });
/* 4310 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientNoTouchOn() {
/* 4315 */     checkIsInMultiOrPipeline();
/* 4316 */     this.connection.sendCommand(Protocol.Command.CLIENT, new String[] { "NO-TOUCH", "ON" });
/* 4317 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientNoTouchOff() {
/* 4322 */     checkIsInMultiOrPipeline();
/* 4323 */     this.connection.sendCommand(Protocol.Command.CLIENT, new String[] { "NO-TOUCH", "OFF" });
/* 4324 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */   
/*      */   public List<String> time() {
/* 4328 */     checkIsInMultiOrPipeline();
/* 4329 */     this.connection.sendCommand(Protocol.Command.TIME);
/* 4330 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, byte[] key, int destinationDb, int timeout) {
/* 4336 */     checkIsInMultiOrPipeline();
/* 4337 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, key, destinationDb, timeout));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, int destinationDB, int timeout, MigrateParams params, byte[]... keys) {
/* 4343 */     checkIsInMultiOrPipeline();
/* 4344 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, destinationDB, timeout, params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, byte[] key, int timeout) {
/* 4349 */     checkIsInMultiOrPipeline();
/* 4350 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, key, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, int timeout, MigrateParams params, byte[]... keys) {
/* 4355 */     checkIsInMultiOrPipeline();
/* 4356 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, timeout, params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long waitReplicas(int replicas, long timeout) {
/* 4361 */     checkIsInMultiOrPipeline();
/* 4362 */     this.connection.sendCommand(Protocol.Command.WAIT, new byte[][] { Protocol.toByteArray(replicas), Protocol.toByteArray(timeout) });
/* 4363 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<Long, Long> waitAOF(long numLocal, long numReplicas, long timeout) {
/* 4368 */     checkIsInMultiOrPipeline();
/* 4369 */     this.connection.sendCommand(Protocol.Command.WAITAOF, new byte[][] { Protocol.toByteArray(numLocal), Protocol.toByteArray(numReplicas), Protocol.toByteArray(timeout) });
/* 4370 */     return BuilderFactory.LONG_LONG_PAIR.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfadd(byte[] key, byte[]... elements) {
/* 4375 */     checkIsInMultiOrPipeline();
/* 4376 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pfadd(key, elements))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(byte[] key) {
/* 4381 */     checkIsInMultiOrPipeline();
/* 4382 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pfcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
/* 4387 */     checkIsInMultiOrPipeline();
/* 4388 */     return this.connection.<String>executeCommand(this.commandObjects.pfmerge(destkey, sourcekeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(byte[]... keys) {
/* 4393 */     checkIsInMultiOrPipeline();
/* 4394 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pfcount(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> scan(byte[] cursor) {
/* 4399 */     checkIsInMultiOrPipeline();
/* 4400 */     return this.connection.<ScanResult<byte[]>>executeCommand(this.commandObjects.scan(cursor));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
/* 4405 */     checkIsInMultiOrPipeline();
/* 4406 */     return this.connection.<ScanResult<byte[]>>executeCommand(this.commandObjects.scan(cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> scan(byte[] cursor, ScanParams params, byte[] type) {
/* 4411 */     checkIsInMultiOrPipeline();
/* 4412 */     return this.connection.<ScanResult<byte[]>>executeCommand(this.commandObjects.scan(cursor, params, type));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
/* 4417 */     return hscan(key, cursor, new ScanParams());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ScanResult<Map.Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor, ScanParams params) {
/* 4423 */     checkIsInMultiOrPipeline();
/* 4424 */     return this.connection.<ScanResult<Map.Entry<byte[], byte[]>>>executeCommand(this.commandObjects.hscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
/* 4429 */     return sscan(key, cursor, new ScanParams());
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
/* 4434 */     checkIsInMultiOrPipeline();
/* 4435 */     return this.connection.<ScanResult<byte[]>>executeCommand(this.commandObjects.sscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
/* 4440 */     return zscan(key, cursor, new ScanParams());
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
/* 4445 */     checkIsInMultiOrPipeline();
/* 4446 */     return this.connection.<ScanResult<Tuple>>executeCommand(this.commandObjects.zscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long geoadd(byte[] key, double longitude, double latitude, byte[] member) {
/* 4452 */     checkIsInMultiOrPipeline();
/* 4453 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geoadd(key, longitude, latitude, member))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 4458 */     checkIsInMultiOrPipeline();
/* 4459 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geoadd(key, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 4464 */     checkIsInMultiOrPipeline();
/* 4465 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geoadd(key, params, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double geodist(byte[] key, byte[] member1, byte[] member2) {
/* 4470 */     checkIsInMultiOrPipeline();
/* 4471 */     return this.connection.<Double>executeCommand(this.commandObjects.geodist(key, member1, member2));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Double geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
/* 4477 */     checkIsInMultiOrPipeline();
/* 4478 */     return this.connection.<Double>executeCommand(this.commandObjects.geodist(key, member1, member2, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> geohash(byte[] key, byte[]... members) {
/* 4483 */     checkIsInMultiOrPipeline();
/* 4484 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.geohash(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
/* 4489 */     checkIsInMultiOrPipeline();
/* 4490 */     return this.connection.<List<GeoCoordinate>>executeCommand(this.commandObjects.geopos(key, members));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 4496 */     checkIsInMultiOrPipeline();
/* 4497 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 4503 */     checkIsInMultiOrPipeline();
/* 4504 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 4510 */     checkIsInMultiOrPipeline();
/* 4511 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long georadiusStore(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 4518 */     checkIsInMultiOrPipeline();
/* 4519 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 4525 */     checkIsInMultiOrPipeline();
/* 4526 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 4532 */     checkIsInMultiOrPipeline();
/* 4533 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 4539 */     checkIsInMultiOrPipeline();
/* 4540 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 4546 */     checkIsInMultiOrPipeline();
/* 4547 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long georadiusByMemberStore(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 4553 */     checkIsInMultiOrPipeline();
/* 4554 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 4559 */     checkIsInMultiOrPipeline();
/* 4560 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 4565 */     checkIsInMultiOrPipeline();
/* 4566 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, byte[] member, double width, double height, GeoUnit unit) {
/* 4571 */     checkIsInMultiOrPipeline();
/* 4572 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 4577 */     checkIsInMultiOrPipeline();
/* 4578 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(byte[] key, GeoSearchParam params) {
/* 4583 */     checkIsInMultiOrPipeline();
/* 4584 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, byte[] member, double radius, GeoUnit unit) {
/* 4589 */     checkIsInMultiOrPipeline();
/* 4590 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, member, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 4595 */     checkIsInMultiOrPipeline();
/* 4596 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, coord, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, byte[] member, double width, double height, GeoUnit unit) {
/* 4601 */     checkIsInMultiOrPipeline();
/* 4602 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, member, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 4607 */     checkIsInMultiOrPipeline();
/* 4608 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, coord, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(byte[] dest, byte[] src, GeoSearchParam params) {
/* 4613 */     checkIsInMultiOrPipeline();
/* 4614 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStoreStoreDist(byte[] dest, byte[] src, GeoSearchParam params) {
/* 4619 */     checkIsInMultiOrPipeline();
/* 4620 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStoreStoreDist(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 4626 */     checkIsInMultiOrPipeline();
/* 4627 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfield(byte[] key, byte[]... arguments) {
/* 4632 */     checkIsInMultiOrPipeline();
/* 4633 */     return this.connection.<List<Long>>executeCommand(this.commandObjects.bitfield(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfieldReadonly(byte[] key, byte[]... arguments) {
/* 4638 */     checkIsInMultiOrPipeline();
/* 4639 */     return this.connection.<List<Long>>executeCommand(this.commandObjects.bitfieldReadonly(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hstrlen(byte[] key, byte[] field) {
/* 4644 */     checkIsInMultiOrPipeline();
/* 4645 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hstrlen(key, field))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xread(XReadParams xReadParams, Map.Entry<byte[], byte[]>... streams) {
/* 4650 */     checkIsInMultiOrPipeline();
/* 4651 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xread(xReadParams, streams));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> xreadGroup(byte[] groupName, byte[] consumer, XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
/* 4657 */     checkIsInMultiOrPipeline();
/* 4658 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] xadd(byte[] key, XAddParams params, Map<byte[], byte[]> hash) {
/* 4663 */     checkIsInMultiOrPipeline();
/* 4664 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.xadd(key, params, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xlen(byte[] key) {
/* 4669 */     checkIsInMultiOrPipeline();
/* 4670 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrange(byte[] key, byte[] start, byte[] end) {
/* 4675 */     checkIsInMultiOrPipeline();
/* 4676 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrange(byte[] key, byte[] start, byte[] end, int count) {
/* 4681 */     checkIsInMultiOrPipeline();
/* 4682 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrevrange(byte[] key, byte[] end, byte[] start) {
/* 4687 */     checkIsInMultiOrPipeline();
/* 4688 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xrevrange(byte[] key, byte[] end, byte[] start, int count) {
/* 4693 */     checkIsInMultiOrPipeline();
/* 4694 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xack(byte[] key, byte[] group, byte[]... ids) {
/* 4699 */     checkIsInMultiOrPipeline();
/* 4700 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xack(key, group, ids))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String xgroupCreate(byte[] key, byte[] consumer, byte[] id, boolean makeStream) {
/* 4705 */     checkIsInMultiOrPipeline();
/* 4706 */     return this.connection.<String>executeCommand(this.commandObjects.xgroupCreate(key, consumer, id, makeStream));
/*      */   }
/*      */ 
/*      */   
/*      */   public String xgroupSetID(byte[] key, byte[] consumer, byte[] id) {
/* 4711 */     checkIsInMultiOrPipeline();
/* 4712 */     return this.connection.<String>executeCommand(this.commandObjects.xgroupSetID(key, consumer, id));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDestroy(byte[] key, byte[] consumer) {
/* 4717 */     checkIsInMultiOrPipeline();
/* 4718 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xgroupDestroy(key, consumer))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean xgroupCreateConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 4723 */     checkIsInMultiOrPipeline();
/* 4724 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.xgroupCreateConsumer(key, groupName, consumerName))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDelConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 4729 */     checkIsInMultiOrPipeline();
/* 4730 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xgroupDelConsumer(key, groupName, consumerName))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xdel(byte[] key, byte[]... ids) {
/* 4735 */     checkIsInMultiOrPipeline();
/* 4736 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xdel(key, ids))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(byte[] key, long maxLen, boolean approximateLength) {
/* 4741 */     checkIsInMultiOrPipeline();
/* 4742 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xtrim(key, maxLen, approximateLength))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(byte[] key, XTrimParams params) {
/* 4747 */     checkIsInMultiOrPipeline();
/* 4748 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xtrim(key, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xpending(byte[] key, byte[] groupName) {
/* 4753 */     checkIsInMultiOrPipeline();
/* 4754 */     return this.connection.executeCommand(this.commandObjects.xpending(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xpending(byte[] key, byte[] groupName, XPendingParams params) {
/* 4759 */     checkIsInMultiOrPipeline();
/* 4760 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xpending(key, groupName, params));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> xclaim(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 4766 */     checkIsInMultiOrPipeline();
/* 4767 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.xclaim(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<byte[]> xclaimJustId(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 4773 */     checkIsInMultiOrPipeline();
/* 4774 */     return this.connection.<List<byte[]>>executeCommand(this.commandObjects.xclaimJustId(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> xautoclaim(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 4780 */     checkIsInMultiOrPipeline();
/* 4781 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xautoclaim(key, groupName, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> xautoclaimJustId(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 4787 */     checkIsInMultiOrPipeline();
/* 4788 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xautoclaimJustId(key, groupName, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xinfoStream(byte[] key) {
/* 4793 */     checkIsInMultiOrPipeline();
/* 4794 */     return this.connection.executeCommand(this.commandObjects.xinfoStream(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xinfoStreamFull(byte[] key) {
/* 4799 */     checkIsInMultiOrPipeline();
/* 4800 */     return this.connection.executeCommand(this.commandObjects.xinfoStreamFull(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object xinfoStreamFull(byte[] key, int count) {
/* 4805 */     checkIsInMultiOrPipeline();
/* 4806 */     return this.connection.executeCommand(this.commandObjects.xinfoStreamFull(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xinfoGroups(byte[] key) {
/* 4811 */     checkIsInMultiOrPipeline();
/* 4812 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xinfoGroups(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> xinfoConsumers(byte[] key, byte[] group) {
/* 4817 */     checkIsInMultiOrPipeline();
/* 4818 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.xinfoConsumers(key, group));
/*      */   }
/*      */   
/*      */   public Object sendCommand(ProtocolCommand cmd, byte[]... args) {
/* 4822 */     checkIsInMultiOrPipeline();
/* 4823 */     this.connection.sendCommand(cmd, args);
/* 4824 */     return this.connection.getOne();
/*      */   }
/*      */   
/*      */   public Object sendBlockingCommand(ProtocolCommand cmd, byte[]... args) {
/* 4828 */     checkIsInMultiOrPipeline();
/* 4829 */     this.connection.sendCommand(cmd, args);
/* 4830 */     this.connection.setTimeoutInfinite();
/*      */     try {
/* 4832 */       return this.connection.getOne();
/*      */     } finally {
/* 4834 */       this.connection.rollbackTimeout();
/*      */     } 
/*      */   }
/*      */   
/*      */   public Object sendCommand(ProtocolCommand cmd) {
/* 4839 */     return sendCommand(cmd, DUMMY_ARRAY);
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
/*      */   
/*      */   public boolean copy(String srcKey, String dstKey, int db, boolean replace) {
/* 4852 */     checkIsInMultiOrPipeline();
/* 4853 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.copy(srcKey, dstKey, db, replace))).booleanValue();
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
/*      */   public boolean copy(String srcKey, String dstKey, boolean replace) {
/* 4865 */     checkIsInMultiOrPipeline();
/* 4866 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.copy(srcKey, dstKey, replace))).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String ping(String message) {
/* 4876 */     checkIsInMultiOrPipeline();
/* 4877 */     this.connection.sendCommand(Protocol.Command.PING, new String[] { message });
/* 4878 */     return this.connection.getBulkReply();
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
/*      */ 
/*      */   
/*      */   public String set(String key, String value) {
/* 4892 */     checkIsInMultiOrPipeline();
/* 4893 */     return this.connection.<String>executeCommand(this.commandObjects.set(key, value));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String set(String key, String value, SetParams params) {
/* 4909 */     checkIsInMultiOrPipeline();
/* 4910 */     return this.connection.<String>executeCommand(this.commandObjects.set(key, value, params));
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
/*      */ 
/*      */   
/*      */   public String get(String key) {
/* 4924 */     checkIsInMultiOrPipeline();
/* 4925 */     return this.connection.<String>executeCommand(this.commandObjects.get(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String setGet(String key, String value) {
/* 4930 */     checkIsInMultiOrPipeline();
/* 4931 */     return this.connection.<String>executeCommand(this.commandObjects.setGet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String setGet(String key, String value, SetParams params) {
/* 4936 */     checkIsInMultiOrPipeline();
/* 4937 */     return this.connection.<String>executeCommand(this.commandObjects.setGet(key, value, params));
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
/*      */   
/*      */   public String getDel(String key) {
/* 4950 */     checkIsInMultiOrPipeline();
/* 4951 */     return this.connection.<String>executeCommand(this.commandObjects.getDel(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String getEx(String key, GetExParams params) {
/* 4956 */     checkIsInMultiOrPipeline();
/* 4957 */     return this.connection.<String>executeCommand(this.commandObjects.getEx(key, params));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long exists(String... keys) {
/* 4968 */     checkIsInMultiOrPipeline();
/* 4969 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.exists(keys))).longValue();
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
/*      */   public boolean exists(String key) {
/* 4981 */     checkIsInMultiOrPipeline();
/* 4982 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.exists(key))).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long del(String... keys) {
/* 4993 */     checkIsInMultiOrPipeline();
/* 4994 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.del(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long del(String key) {
/* 4999 */     checkIsInMultiOrPipeline();
/* 5000 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.del(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long unlink(String... keys) {
/* 5018 */     checkIsInMultiOrPipeline();
/* 5019 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.unlink(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long unlink(String key) {
/* 5024 */     checkIsInMultiOrPipeline();
/* 5025 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.unlink(key))).longValue();
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
/*      */   
/*      */   public String type(String key) {
/* 5038 */     checkIsInMultiOrPipeline();
/* 5039 */     return this.connection.<String>executeCommand(this.commandObjects.type(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> keys(String pattern) {
/* 5044 */     checkIsInMultiOrPipeline();
/* 5045 */     return this.connection.<Set<String>>executeCommand(this.commandObjects.keys(pattern));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String randomKey() {
/* 5056 */     checkIsInMultiOrPipeline();
/* 5057 */     return this.connection.<String>executeCommand(this.commandObjects.randomKey());
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
/*      */ 
/*      */   
/*      */   public String rename(String oldkey, String newkey) {
/* 5071 */     checkIsInMultiOrPipeline();
/* 5072 */     return this.connection.<String>executeCommand(this.commandObjects.rename(oldkey, newkey));
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
/*      */   
/*      */   public long renamenx(String oldkey, String newkey) {
/* 5085 */     checkIsInMultiOrPipeline();
/* 5086 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.renamenx(oldkey, newkey))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long expire(String key, long seconds) {
/* 5112 */     checkIsInMultiOrPipeline();
/* 5113 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expire(key, seconds))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long expire(String key, long seconds, ExpiryOption expiryOption) {
/* 5128 */     checkIsInMultiOrPipeline();
/* 5129 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expire(key, seconds, expiryOption))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpire(String key, long milliseconds) {
/* 5134 */     checkIsInMultiOrPipeline();
/* 5135 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpire(key, milliseconds))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pexpire(String key, long milliseconds, ExpiryOption expiryOption) {
/* 5140 */     checkIsInMultiOrPipeline();
/* 5141 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpire(key, milliseconds, expiryOption))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long expireTime(String key) {
/* 5156 */     checkIsInMultiOrPipeline();
/* 5157 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expireTime(key))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long pexpireTime(String key) {
/* 5172 */     checkIsInMultiOrPipeline();
/* 5173 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpireTime(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long expireAt(String key, long unixTime) {
/* 5201 */     checkIsInMultiOrPipeline();
/* 5202 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expireAt(key, unixTime))).longValue();
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
/*      */ 
/*      */   
/*      */   public long expireAt(String key, long unixTime, ExpiryOption expiryOption) {
/* 5216 */     checkIsInMultiOrPipeline();
/* 5217 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.expireAt(key, unixTime, expiryOption))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long pexpireAt(String key, long millisecondsTimestamp) {
/* 5232 */     checkIsInMultiOrPipeline();
/* 5233 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long pexpireAt(String key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/* 5248 */     checkIsInMultiOrPipeline();
/* 5249 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp, expiryOption))).longValue();
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
/*      */   
/*      */   public long ttl(String key) {
/* 5262 */     checkIsInMultiOrPipeline();
/* 5263 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.ttl(key))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long touch(String... keys) {
/* 5274 */     checkIsInMultiOrPipeline();
/* 5275 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.touch(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long touch(String key) {
/* 5280 */     checkIsInMultiOrPipeline();
/* 5281 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.touch(key))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long move(String key, int dbIndex) {
/* 5296 */     checkIsInMultiOrPipeline();
/* 5297 */     this.connection.sendCommand(Protocol.Command.MOVE, new byte[][] { SafeEncoder.encode(key), Protocol.toByteArray(dbIndex) });
/* 5298 */     return this.connection.getIntegerReply().longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSet(String key, String value) {
/* 5313 */     checkIsInMultiOrPipeline();
/* 5314 */     return this.connection.<String>executeCommand(this.commandObjects.getSet(key, value));
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
/*      */ 
/*      */   
/*      */   public List<String> mget(String... keys) {
/* 5328 */     checkIsInMultiOrPipeline();
/* 5329 */     return this.connection.<List<String>>executeCommand(this.commandObjects.mget(keys));
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
/*      */ 
/*      */   
/*      */   public long setnx(String key, String value) {
/* 5343 */     checkIsInMultiOrPipeline();
/* 5344 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.setnx(key, value))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String setex(String key, long seconds, String value) {
/* 5360 */     checkIsInMultiOrPipeline();
/* 5361 */     return this.connection.<String>executeCommand(this.commandObjects.setex(key, seconds, value));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String mset(String... keysvalues) {
/* 5382 */     checkIsInMultiOrPipeline();
/* 5383 */     return this.connection.<String>executeCommand(this.commandObjects.mset(keysvalues));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long msetnx(String... keysvalues) {
/* 5404 */     checkIsInMultiOrPipeline();
/* 5405 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.msetnx(keysvalues))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long decrBy(String key, long decrement) {
/* 5428 */     checkIsInMultiOrPipeline();
/* 5429 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.decrBy(key, decrement))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long decr(String key) {
/* 5451 */     checkIsInMultiOrPipeline();
/* 5452 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.decr(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long incrBy(String key, long increment) {
/* 5475 */     checkIsInMultiOrPipeline();
/* 5476 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.incrBy(key, increment))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double incrByFloat(String key, double increment) {
/* 5496 */     checkIsInMultiOrPipeline();
/* 5497 */     return ((Double)this.connection.<Double>executeCommand(this.commandObjects.incrByFloat(key, increment))).doubleValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long incr(String key) {
/* 5519 */     checkIsInMultiOrPipeline();
/* 5520 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.incr(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long append(String key, String value) {
/* 5537 */     checkIsInMultiOrPipeline();
/* 5538 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.append(key, value))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String substr(String key, int start, int end) {
/* 5559 */     checkIsInMultiOrPipeline();
/* 5560 */     return this.connection.<String>executeCommand(this.commandObjects.substr(key, start, end));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long hset(String key, String field, String value) {
/* 5577 */     checkIsInMultiOrPipeline();
/* 5578 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hset(key, field, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long hset(String key, Map<String, String> hash) {
/* 5583 */     checkIsInMultiOrPipeline();
/* 5584 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hset(key, hash))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String hget(String key, String field) {
/* 5599 */     checkIsInMultiOrPipeline();
/* 5600 */     return this.connection.<String>executeCommand(this.commandObjects.hget(key, field));
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
/*      */ 
/*      */   
/*      */   public long hsetnx(String key, String field, String value) {
/* 5614 */     checkIsInMultiOrPipeline();
/* 5615 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hsetnx(key, field, value))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String hmset(String key, Map<String, String> hash) {
/* 5630 */     checkIsInMultiOrPipeline();
/* 5631 */     return this.connection.<String>executeCommand(this.commandObjects.hmset(key, hash));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> hmget(String key, String... fields) {
/* 5647 */     checkIsInMultiOrPipeline();
/* 5648 */     return this.connection.<List<String>>executeCommand(this.commandObjects.hmget(key, fields));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long hincrBy(String key, String field, long value) {
/* 5667 */     checkIsInMultiOrPipeline();
/* 5668 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hincrBy(key, field, value))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double hincrByFloat(String key, String field, double value) {
/* 5688 */     checkIsInMultiOrPipeline();
/* 5689 */     return ((Double)this.connection.<Double>executeCommand(this.commandObjects.hincrByFloat(key, field, value))).doubleValue();
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
/*      */   public boolean hexists(String key, String field) {
/* 5701 */     checkIsInMultiOrPipeline();
/* 5702 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.hexists(key, field))).booleanValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long hdel(String key, String... fields) {
/* 5718 */     checkIsInMultiOrPipeline();
/* 5719 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hdel(key, fields))).longValue();
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
/*      */   
/*      */   public long hlen(String key) {
/* 5732 */     checkIsInMultiOrPipeline();
/* 5733 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hlen(key))).longValue();
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
/*      */   public Set<String> hkeys(String key) {
/* 5745 */     checkIsInMultiOrPipeline();
/* 5746 */     return this.connection.<Set<String>>executeCommand(this.commandObjects.hkeys(key));
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
/*      */   public List<String> hvals(String key) {
/* 5758 */     checkIsInMultiOrPipeline();
/* 5759 */     return this.connection.<List<String>>executeCommand(this.commandObjects.hvals(key));
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
/*      */   public Map<String, String> hgetAll(String key) {
/* 5771 */     checkIsInMultiOrPipeline();
/* 5772 */     return this.connection.<Map<String, String>>executeCommand(this.commandObjects.hgetAll(key));
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
/*      */   public String hrandfield(String key) {
/* 5784 */     checkIsInMultiOrPipeline();
/* 5785 */     return this.connection.<String>executeCommand(this.commandObjects.hrandfield(key));
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
/*      */   
/*      */   public List<String> hrandfield(String key, long count) {
/* 5798 */     checkIsInMultiOrPipeline();
/* 5799 */     return this.connection.<List<String>>executeCommand(this.commandObjects.hrandfield(key, count));
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
/*      */   
/*      */   public List<Map.Entry<String, String>> hrandfieldWithValues(String key, long count) {
/* 5812 */     checkIsInMultiOrPipeline();
/* 5813 */     return this.connection.<List<Map.Entry<String, String>>>executeCommand(this.commandObjects.hrandfieldWithValues(key, count));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long rpush(String key, String... strings) {
/* 5828 */     checkIsInMultiOrPipeline();
/* 5829 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.rpush(key, strings))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long lpush(String key, String... strings) {
/* 5844 */     checkIsInMultiOrPipeline();
/* 5845 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.lpush(key, strings))).longValue();
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
/*      */ 
/*      */   
/*      */   public long llen(String key) {
/* 5859 */     checkIsInMultiOrPipeline();
/* 5860 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.llen(key))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> lrange(String key, long start, long stop) {
/* 5897 */     checkIsInMultiOrPipeline();
/* 5898 */     return this.connection.<List<String>>executeCommand(this.commandObjects.lrange(key, start, stop));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String ltrim(String key, long start, long stop) {
/* 5933 */     checkIsInMultiOrPipeline();
/* 5934 */     return this.connection.<String>executeCommand(this.commandObjects.ltrim(key, start, stop));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String lindex(String key, long index) {
/* 5955 */     checkIsInMultiOrPipeline();
/* 5956 */     return this.connection.<String>executeCommand(this.commandObjects.lindex(key, index));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String lset(String key, long index, String value) {
/* 5980 */     checkIsInMultiOrPipeline();
/* 5981 */     return this.connection.<String>executeCommand(this.commandObjects.lset(key, index, value));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long lrem(String key, long count, String value) {
/* 6001 */     checkIsInMultiOrPipeline();
/* 6002 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.lrem(key, count, value))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String lpop(String key) {
/* 6017 */     checkIsInMultiOrPipeline();
/* 6018 */     return this.connection.<String>executeCommand(this.commandObjects.lpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> lpop(String key, int count) {
/* 6023 */     checkIsInMultiOrPipeline();
/* 6024 */     return this.connection.<List<String>>executeCommand(this.commandObjects.lpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long lpos(String key, String element) {
/* 6029 */     checkIsInMultiOrPipeline();
/* 6030 */     return this.connection.<Long>executeCommand(this.commandObjects.lpos(key, element));
/*      */   }
/*      */ 
/*      */   
/*      */   public Long lpos(String key, String element, LPosParams params) {
/* 6035 */     checkIsInMultiOrPipeline();
/* 6036 */     return this.connection.<Long>executeCommand(this.commandObjects.lpos(key, element, params));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Long> lpos(String key, String element, LPosParams params, long count) {
/* 6042 */     checkIsInMultiOrPipeline();
/* 6043 */     return this.connection.<List<Long>>executeCommand(this.commandObjects.lpos(key, element, params, count));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String rpop(String key) {
/* 6058 */     checkIsInMultiOrPipeline();
/* 6059 */     return this.connection.<String>executeCommand(this.commandObjects.rpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> rpop(String key, int count) {
/* 6064 */     checkIsInMultiOrPipeline();
/* 6065 */     return this.connection.<List<String>>executeCommand(this.commandObjects.rpop(key, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String rpoplpush(String srckey, String dstkey) {
/* 6085 */     checkIsInMultiOrPipeline();
/* 6086 */     return this.connection.<String>executeCommand(this.commandObjects.rpoplpush(srckey, dstkey));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sadd(String key, String... members) {
/* 6101 */     checkIsInMultiOrPipeline();
/* 6102 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sadd(key, members))).longValue();
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
/*      */   
/*      */   public Set<String> smembers(String key) {
/* 6115 */     checkIsInMultiOrPipeline();
/* 6116 */     return this.connection.<Set<String>>executeCommand(this.commandObjects.smembers(key));
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
/*      */ 
/*      */   
/*      */   public long srem(String key, String... members) {
/* 6130 */     checkIsInMultiOrPipeline();
/* 6131 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.srem(key, members))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String spop(String key) {
/* 6147 */     checkIsInMultiOrPipeline();
/* 6148 */     return this.connection.<String>executeCommand(this.commandObjects.spop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<String> spop(String key, long count) {
/* 6153 */     checkIsInMultiOrPipeline();
/* 6154 */     return this.connection.<Set<String>>executeCommand(this.commandObjects.spop(key, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long smove(String srckey, String dstkey, String member) {
/* 6178 */     checkIsInMultiOrPipeline();
/* 6179 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.smove(srckey, dstkey, member))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long scard(String key) {
/* 6190 */     checkIsInMultiOrPipeline();
/* 6191 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.scard(key))).longValue();
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
/*      */   
/*      */   public boolean sismember(String key, String member) {
/* 6204 */     checkIsInMultiOrPipeline();
/* 6205 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.sismember(key, member))).booleanValue();
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
/*      */   
/*      */   public List<Boolean> smismember(String key, String... members) {
/* 6218 */     checkIsInMultiOrPipeline();
/* 6219 */     return this.connection.<List<Boolean>>executeCommand(this.commandObjects.smismember(key, members));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> sinter(String... keys) {
/* 6239 */     checkIsInMultiOrPipeline();
/* 6240 */     return this.connection.<Set<String>>executeCommand(this.commandObjects.sinter(keys));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sinterstore(String dstkey, String... keys) {
/* 6255 */     checkIsInMultiOrPipeline();
/* 6256 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sinterstore(dstkey, keys))).longValue();
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
/*      */   
/*      */   public long sintercard(String... keys) {
/* 6269 */     checkIsInMultiOrPipeline();
/* 6270 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sintercard(keys))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sintercard(int limit, String... keys) {
/* 6285 */     checkIsInMultiOrPipeline();
/* 6286 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sintercard(limit, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> sunion(String... keys) {
/* 6304 */     checkIsInMultiOrPipeline();
/* 6305 */     return this.connection.<Set<String>>executeCommand(this.commandObjects.sunion(keys));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sunionstore(String dstkey, String... keys) {
/* 6320 */     checkIsInMultiOrPipeline();
/* 6321 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sunionstore(dstkey, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> sdiff(String... keys) {
/* 6346 */     checkIsInMultiOrPipeline();
/* 6347 */     return this.connection.<Set<String>>executeCommand(this.commandObjects.sdiff(keys));
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
/*      */   public long sdiffstore(String dstkey, String... keys) {
/* 6359 */     checkIsInMultiOrPipeline();
/* 6360 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sdiffstore(dstkey, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public String srandmember(String key) {
/* 6375 */     checkIsInMultiOrPipeline();
/* 6376 */     return this.connection.<String>executeCommand(this.commandObjects.srandmember(key));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> srandmember(String key, int count) {
/* 6394 */     checkIsInMultiOrPipeline();
/* 6395 */     return this.connection.<List<String>>executeCommand(this.commandObjects.srandmember(key, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zadd(String key, double score, String member) {
/* 6416 */     checkIsInMultiOrPipeline();
/* 6417 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, score, member))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long zadd(String key, double score, String member, ZAddParams params) {
/* 6423 */     checkIsInMultiOrPipeline();
/* 6424 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, score, member, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(String key, Map<String, Double> scoreMembers) {
/* 6429 */     checkIsInMultiOrPipeline();
/* 6430 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, scoreMembers))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
/* 6435 */     checkIsInMultiOrPipeline();
/* 6436 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zadd(key, scoreMembers, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double zaddIncr(String key, double score, String member, ZAddParams params) {
/* 6441 */     checkIsInMultiOrPipeline();
/* 6442 */     return this.connection.<Double>executeCommand(this.commandObjects.zaddIncr(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zdiff(String... keys) {
/* 6447 */     checkIsInMultiOrPipeline();
/* 6448 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zdiffWithScores(String... keys) {
/* 6453 */     checkIsInMultiOrPipeline();
/* 6454 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zdiffWithScores(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public long zdiffStore(String dstkey, String... keys) {
/* 6460 */     checkIsInMultiOrPipeline();
/* 6461 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zdiffStore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zdiffstore(String dstkey, String... keys) {
/* 6466 */     checkIsInMultiOrPipeline();
/* 6467 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zdiffstore(dstkey, keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrange(String key, long start, long stop) {
/* 6472 */     checkIsInMultiOrPipeline();
/* 6473 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrange(key, start, stop));
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
/*      */ 
/*      */   
/*      */   public long zrem(String key, String... members) {
/* 6487 */     checkIsInMultiOrPipeline();
/* 6488 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zrem(key, members))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public double zincrby(String key, double increment, String member) {
/* 6511 */     checkIsInMultiOrPipeline();
/* 6512 */     return ((Double)this.connection.<Double>executeCommand(this.commandObjects.zincrby(key, increment, member))).doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Double zincrby(String key, double increment, String member, ZIncrByParams params) {
/* 6518 */     checkIsInMultiOrPipeline();
/* 6519 */     return this.connection.<Double>executeCommand(this.commandObjects.zincrby(key, increment, member, params));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long zrank(String key, String member) {
/* 6539 */     checkIsInMultiOrPipeline();
/* 6540 */     return this.connection.<Long>executeCommand(this.commandObjects.zrank(key, member));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long zrevrank(String key, String member) {
/* 6560 */     checkIsInMultiOrPipeline();
/* 6561 */     return this.connection.<Long>executeCommand(this.commandObjects.zrevrank(key, member));
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
/*      */   public KeyValue<Long, Double> zrankWithScore(String key, String member) {
/* 6573 */     checkIsInMultiOrPipeline();
/* 6574 */     return this.connection.<KeyValue<Long, Double>>executeCommand(this.commandObjects.zrankWithScore(key, member));
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
/*      */   public KeyValue<Long, Double> zrevrankWithScore(String key, String member) {
/* 6586 */     checkIsInMultiOrPipeline();
/* 6587 */     return this.connection.<KeyValue<Long, Double>>executeCommand(this.commandObjects.zrevrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrange(String key, long start, long stop) {
/* 6592 */     checkIsInMultiOrPipeline();
/* 6593 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrevrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(String key, long start, long stop) {
/* 6598 */     checkIsInMultiOrPipeline();
/* 6599 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeWithScores(String key, long start, long stop) {
/* 6604 */     checkIsInMultiOrPipeline();
/* 6605 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrange(String key, ZRangeParams zRangeParams) {
/* 6610 */     checkIsInMultiOrPipeline();
/* 6611 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrange(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeWithScores(String key, ZRangeParams zRangeParams) {
/* 6616 */     checkIsInMultiOrPipeline();
/* 6617 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeWithScores(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zrangestore(String dest, String src, ZRangeParams zRangeParams) {
/* 6622 */     checkIsInMultiOrPipeline();
/* 6623 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zrangestore(dest, src, zRangeParams))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String zrandmember(String key) {
/* 6628 */     checkIsInMultiOrPipeline();
/* 6629 */     return this.connection.<String>executeCommand(this.commandObjects.zrandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrandmember(String key, long count) {
/* 6634 */     checkIsInMultiOrPipeline();
/* 6635 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrandmemberWithScores(String key, long count) {
/* 6640 */     checkIsInMultiOrPipeline();
/* 6641 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrandmemberWithScores(key, count));
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
/*      */   
/*      */   public long zcard(String key) {
/* 6654 */     checkIsInMultiOrPipeline();
/* 6655 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zcard(key))).longValue();
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
/*      */ 
/*      */ 
/*      */   
/*      */   public Double zscore(String key, String member) {
/* 6670 */     checkIsInMultiOrPipeline();
/* 6671 */     return this.connection.<Double>executeCommand(this.commandObjects.zscore(key, member));
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
/*      */ 
/*      */   
/*      */   public List<Double> zmscore(String key, String... members) {
/* 6685 */     checkIsInMultiOrPipeline();
/* 6686 */     return this.connection.<List<Double>>executeCommand(this.commandObjects.zmscore(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmax(String key) {
/* 6691 */     checkIsInMultiOrPipeline();
/* 6692 */     return this.connection.<Tuple>executeCommand(this.commandObjects.zpopmax(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmax(String key, int count) {
/* 6697 */     checkIsInMultiOrPipeline();
/* 6698 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zpopmax(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Tuple zpopmin(String key) {
/* 6703 */     checkIsInMultiOrPipeline();
/* 6704 */     return this.connection.<Tuple>executeCommand(this.commandObjects.zpopmin(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zpopmin(String key, int count) {
/* 6709 */     checkIsInMultiOrPipeline();
/* 6710 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zpopmin(key, count));
/*      */   }
/*      */   
/*      */   public String watch(String... keys) {
/* 6714 */     checkIsInMultiOrPipeline();
/* 6715 */     this.connection.sendCommand(Protocol.Command.WATCH, keys);
/*      */     
/* 6717 */     String status = this.connection.getStatusCodeReply();
/* 6718 */     this.isInWatch = true;
/* 6719 */     return status;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> sort(String key) {
/* 6737 */     checkIsInMultiOrPipeline();
/* 6738 */     return this.connection.<List<String>>executeCommand(this.commandObjects.sort(key));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> sort(String key, SortingParams sortingParams) {
/* 6817 */     checkIsInMultiOrPipeline();
/* 6818 */     return this.connection.<List<String>>executeCommand(this.commandObjects.sort(key, sortingParams));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public long sort(String key, SortingParams sortingParams, String dstkey) {
/* 6833 */     checkIsInMultiOrPipeline();
/* 6834 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sort(key, sortingParams, dstkey))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> sortReadonly(String key, SortingParams sortingParams) {
/* 6839 */     checkIsInMultiOrPipeline();
/* 6840 */     return this.connection.<List<String>>executeCommand(this.commandObjects.sortReadonly(key, sortingParams));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long sort(String key, String dstkey) {
/* 6858 */     checkIsInMultiOrPipeline();
/* 6859 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.sort(key, dstkey))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String lmove(String srcKey, String dstKey, ListDirection from, ListDirection to) {
/* 6865 */     checkIsInMultiOrPipeline();
/* 6866 */     return this.connection.<String>executeCommand(this.commandObjects.lmove(srcKey, dstKey, from, to));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String blmove(String srcKey, String dstKey, ListDirection from, ListDirection to, double timeout) {
/* 6872 */     checkIsInMultiOrPipeline();
/* 6873 */     return this.connection.<String>executeCommand(this.commandObjects.blmove(srcKey, dstKey, from, to, timeout));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> blpop(int timeout, String... keys) {
/* 6940 */     checkIsInMultiOrPipeline();
/* 6941 */     return this.connection.<List<String>>executeCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> blpop(double timeout, String... keys) {
/* 6946 */     checkIsInMultiOrPipeline();
/* 6947 */     return this.connection.<KeyValue<String, String>>executeCommand(this.commandObjects.blpop(timeout, keys));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> brpop(int timeout, String... keys) {
/* 7014 */     checkIsInMultiOrPipeline();
/* 7015 */     return this.connection.<List<String>>executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> brpop(double timeout, String... keys) {
/* 7020 */     checkIsInMultiOrPipeline();
/* 7021 */     return this.connection.<KeyValue<String, String>>executeCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> lmpop(ListDirection direction, String... keys) {
/* 7026 */     checkIsInMultiOrPipeline();
/* 7027 */     return this.connection.<KeyValue<String, List<String>>>executeCommand(this.commandObjects.lmpop(direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> lmpop(ListDirection direction, int count, String... keys) {
/* 7032 */     checkIsInMultiOrPipeline();
/* 7033 */     return this.connection.<KeyValue<String, List<String>>>executeCommand(this.commandObjects.lmpop(direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> blmpop(double timeout, ListDirection direction, String... keys) {
/* 7038 */     checkIsInMultiOrPipeline();
/* 7039 */     return this.connection.<KeyValue<String, List<String>>>executeCommand(this.commandObjects.blmpop(timeout, direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<String>> blmpop(double timeout, ListDirection direction, int count, String... keys) {
/* 7044 */     checkIsInMultiOrPipeline();
/* 7045 */     return this.connection.<KeyValue<String, List<String>>>executeCommand(this.commandObjects.blmpop(timeout, direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, Tuple> bzpopmax(double timeout, String... keys) {
/* 7050 */     checkIsInMultiOrPipeline();
/* 7051 */     return this.connection.<KeyValue<String, Tuple>>executeCommand(this.commandObjects.bzpopmax(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, Tuple> bzpopmin(double timeout, String... keys) {
/* 7056 */     checkIsInMultiOrPipeline();
/* 7057 */     return this.connection.<KeyValue<String, Tuple>>executeCommand(this.commandObjects.bzpopmin(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> blpop(int timeout, String key) {
/* 7062 */     checkIsInMultiOrPipeline();
/* 7063 */     return this.connection.<List<String>>executeCommand(this.commandObjects.blpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> blpop(double timeout, String key) {
/* 7068 */     checkIsInMultiOrPipeline();
/* 7069 */     return this.connection.<KeyValue<String, String>>executeCommand(this.commandObjects.blpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> brpop(int timeout, String key) {
/* 7074 */     checkIsInMultiOrPipeline();
/* 7075 */     return this.connection.<List<String>>executeCommand(this.commandObjects.brpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, String> brpop(double timeout, String key) {
/* 7080 */     checkIsInMultiOrPipeline();
/* 7081 */     return this.connection.<KeyValue<String, String>>executeCommand(this.commandObjects.brpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(String key, double min, double max) {
/* 7086 */     checkIsInMultiOrPipeline();
/* 7087 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zcount(String key, String min, String max) {
/* 7092 */     checkIsInMultiOrPipeline();
/* 7093 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zcount(key, min, max))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, double min, double max) {
/* 7146 */     checkIsInMultiOrPipeline();
/* 7147 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, String min, String max) {
/* 7152 */     checkIsInMultiOrPipeline();
/* 7153 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrangeByScore(key, min, max));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, double min, double max, int offset, int count) {
/* 7208 */     checkIsInMultiOrPipeline();
/* 7209 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> zrangeByScore(String key, String min, String max, int offset, int count) {
/* 7215 */     checkIsInMultiOrPipeline();
/* 7216 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
/* 7268 */     checkIsInMultiOrPipeline();
/* 7269 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
/* 7274 */     checkIsInMultiOrPipeline();
/* 7275 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
/* 7330 */     checkIsInMultiOrPipeline();
/* 7331 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
/* 7337 */     checkIsInMultiOrPipeline();
/* 7338 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, double max, double min) {
/* 7343 */     checkIsInMultiOrPipeline();
/* 7344 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, String max, String min) {
/* 7349 */     checkIsInMultiOrPipeline();
/* 7350 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
/* 7356 */     checkIsInMultiOrPipeline();
/* 7357 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
/* 7362 */     checkIsInMultiOrPipeline();
/* 7363 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
/* 7369 */     checkIsInMultiOrPipeline();
/* 7370 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
/* 7376 */     checkIsInMultiOrPipeline();
/* 7377 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {
/* 7383 */     checkIsInMultiOrPipeline();
/* 7384 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
/* 7389 */     checkIsInMultiOrPipeline();
/* 7390 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zremrangeByRank(String key, long start, long stop) {
/* 7408 */     checkIsInMultiOrPipeline();
/* 7409 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByRank(key, start, stop))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(String key, double min, double max) {
/* 7427 */     checkIsInMultiOrPipeline();
/* 7428 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByScore(String key, String min, String max) {
/* 7433 */     checkIsInMultiOrPipeline();
/* 7434 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByScore(key, min, max))).longValue();
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
/*      */   public List<String> zunion(ZParams params, String... keys) {
/* 7446 */     checkIsInMultiOrPipeline();
/* 7447 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zunion(params, keys));
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
/*      */   public List<Tuple> zunionWithScores(ZParams params, String... keys) {
/* 7459 */     checkIsInMultiOrPipeline();
/* 7460 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zunionWithScores(params, keys));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zunionstore(String dstkey, String... sets) {
/* 7495 */     checkIsInMultiOrPipeline();
/* 7496 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zunionstore(dstkey, sets))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zunionstore(String dstkey, ZParams params, String... sets) {
/* 7532 */     checkIsInMultiOrPipeline();
/* 7533 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zunionstore(dstkey, params, sets))).longValue();
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
/*      */   public List<String> zinter(ZParams params, String... keys) {
/* 7545 */     checkIsInMultiOrPipeline();
/* 7546 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zinter(params, keys));
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
/*      */   public List<Tuple> zinterWithScores(ZParams params, String... keys) {
/* 7558 */     checkIsInMultiOrPipeline();
/* 7559 */     return this.connection.<List<Tuple>>executeCommand(this.commandObjects.zinterWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(String... keys) {
/* 7564 */     checkIsInMultiOrPipeline();
/* 7565 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zintercard(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zintercard(long limit, String... keys) {
/* 7570 */     checkIsInMultiOrPipeline();
/* 7571 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zintercard(limit, keys))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zinterstore(String dstkey, String... sets) {
/* 7606 */     checkIsInMultiOrPipeline();
/* 7607 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zinterstore(dstkey, sets))).longValue();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long zinterstore(String dstkey, ZParams params, String... sets) {
/* 7643 */     checkIsInMultiOrPipeline();
/* 7644 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zinterstore(dstkey, params, sets))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long zlexcount(String key, String min, String max) {
/* 7649 */     checkIsInMultiOrPipeline();
/* 7650 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zlexcount(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrangeByLex(String key, String min, String max) {
/* 7655 */     checkIsInMultiOrPipeline();
/* 7656 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> zrangeByLex(String key, String min, String max, int offset, int count) {
/* 7662 */     checkIsInMultiOrPipeline();
/* 7663 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrangeByLex(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByLex(String key, String max, String min) {
/* 7668 */     checkIsInMultiOrPipeline();
/* 7669 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrevrangeByLex(key, max, min));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> zrevrangeByLex(String key, String max, String min, int offset, int count) {
/* 7675 */     checkIsInMultiOrPipeline();
/* 7676 */     return this.connection.<List<String>>executeCommand(this.commandObjects.zrevrangeByLex(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public long zremrangeByLex(String key, String min, String max) {
/* 7681 */     checkIsInMultiOrPipeline();
/* 7682 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.zremrangeByLex(key, min, max))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> zmpop(SortedSetOption option, String... keys) {
/* 7687 */     checkIsInMultiOrPipeline();
/* 7688 */     return this.connection.<KeyValue<String, List<Tuple>>>executeCommand(this.commandObjects.zmpop(option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> zmpop(SortedSetOption option, int count, String... keys) {
/* 7693 */     checkIsInMultiOrPipeline();
/* 7694 */     return this.connection.<KeyValue<String, List<Tuple>>>executeCommand(this.commandObjects.zmpop(option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> bzmpop(double timeout, SortedSetOption option, String... keys) {
/* 7699 */     checkIsInMultiOrPipeline();
/* 7700 */     return this.connection.<KeyValue<String, List<Tuple>>>executeCommand(this.commandObjects.bzmpop(timeout, option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public KeyValue<String, List<Tuple>> bzmpop(double timeout, SortedSetOption option, int count, String... keys) {
/* 7705 */     checkIsInMultiOrPipeline();
/* 7706 */     return this.connection.<KeyValue<String, List<Tuple>>>executeCommand(this.commandObjects.bzmpop(timeout, option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public long strlen(String key) {
/* 7711 */     checkIsInMultiOrPipeline();
/* 7712 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.strlen(key))).longValue();
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
/*      */   public LCSMatchResult lcs(String keyA, String keyB, LCSParams params) {
/* 7724 */     checkIsInMultiOrPipeline();
/* 7725 */     return this.connection.<LCSMatchResult>executeCommand(this.commandObjects.lcs(keyA, keyB, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long lpushx(String key, String... strings) {
/* 7730 */     checkIsInMultiOrPipeline();
/* 7731 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.lpushx(key, strings))).longValue();
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
/*      */   public long persist(String key) {
/* 7743 */     checkIsInMultiOrPipeline();
/* 7744 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.persist(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long rpushx(String key, String... strings) {
/* 7749 */     checkIsInMultiOrPipeline();
/* 7750 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.rpushx(key, strings))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String echo(String string) {
/* 7755 */     checkIsInMultiOrPipeline();
/* 7756 */     this.connection.sendCommand(Protocol.Command.ECHO, new String[] { string });
/* 7757 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long linsert(String key, ListPosition where, String pivot, String value) {
/* 7763 */     checkIsInMultiOrPipeline();
/* 7764 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.linsert(key, where, pivot, value))).longValue();
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
/*      */   public String brpoplpush(String source, String destination, int timeout) {
/* 7776 */     checkIsInMultiOrPipeline();
/* 7777 */     return this.connection.<String>executeCommand(this.commandObjects.brpoplpush(source, destination, timeout));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean setbit(String key, long offset, boolean value) {
/* 7788 */     checkIsInMultiOrPipeline();
/* 7789 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.setbit(key, offset, value))).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getbit(String key, long offset) {
/* 7799 */     checkIsInMultiOrPipeline();
/* 7800 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.getbit(key, offset))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long setrange(String key, long offset, String value) {
/* 7805 */     checkIsInMultiOrPipeline();
/* 7806 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.setrange(key, offset, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getrange(String key, long startOffset, long endOffset) {
/* 7811 */     checkIsInMultiOrPipeline();
/* 7812 */     return this.connection.<String>executeCommand(this.commandObjects.getrange(key, startOffset, endOffset));
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(String key, boolean value) {
/* 7817 */     checkIsInMultiOrPipeline();
/* 7818 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitpos(key, value))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitpos(String key, boolean value, BitPosParams params) {
/* 7823 */     checkIsInMultiOrPipeline();
/* 7824 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitpos(key, value, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> role() {
/* 7829 */     checkIsInMultiOrPipeline();
/* 7830 */     this.connection.sendCommand(Protocol.Command.ROLE);
/* 7831 */     return BuilderFactory.ENCODED_OBJECT_LIST.build(this.connection.getOne());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> configGet(String pattern) {
/* 7870 */     checkIsInMultiOrPipeline();
/* 7871 */     this.connection.sendCommand(Protocol.Command.CONFIG, new String[] { Protocol.Keyword.GET.name(), pattern });
/* 7872 */     return BuilderFactory.STRING_MAP.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> configGet(String... patterns) {
/* 7877 */     checkIsInMultiOrPipeline();
/* 7878 */     this.connection.sendCommand(Protocol.Command.CONFIG, joinParameters(Protocol.Keyword.GET.name(), patterns));
/* 7879 */     return BuilderFactory.STRING_MAP.build(this.connection.getOne());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String configSet(String parameter, String value) {
/* 7913 */     checkIsInMultiOrPipeline();
/* 7914 */     this.connection.sendCommand(Protocol.Command.CONFIG, new String[] { Protocol.Keyword.SET.name(), parameter, value });
/* 7915 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String configSet(String... parameterValues) {
/* 7920 */     checkIsInMultiOrPipeline();
/* 7921 */     this.connection.sendCommand(Protocol.Command.CONFIG, joinParameters(Protocol.Keyword.SET.name(), parameterValues));
/* 7922 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String configSet(Map<String, String> parameterValues) {
/* 7927 */     checkIsInMultiOrPipeline();
/* 7928 */     CommandArguments args = (new CommandArguments(Protocol.Command.CONFIG)).add(Protocol.Keyword.SET);
/* 7929 */     parameterValues.forEach((k, v) -> args.add(k).add(v));
/* 7930 */     this.connection.sendCommand(args);
/* 7931 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */   
/*      */   public long publish(String channel, String message) {
/* 7935 */     checkIsInMultiOrPipeline();
/* 7936 */     this.connection.sendCommand(Protocol.Command.PUBLISH, new String[] { channel, message });
/* 7937 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */   
/*      */   public void subscribe(JedisPubSub jedisPubSub, String... channels) {
/* 7941 */     jedisPubSub.proceed(this.connection, channels);
/*      */   }
/*      */   
/*      */   public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
/* 7945 */     jedisPubSub.proceedWithPatterns(this.connection, patterns);
/*      */   }
/*      */   
/*      */   public List<String> pubsubChannels() {
/* 7949 */     checkIsInMultiOrPipeline();
/* 7950 */     this.connection.sendCommand(Protocol.Command.PUBSUB, Protocol.Keyword.CHANNELS);
/* 7951 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */   
/*      */   public List<String> pubsubChannels(String pattern) {
/* 7955 */     checkIsInMultiOrPipeline();
/* 7956 */     this.connection.sendCommand(Protocol.Command.PUBSUB, new String[] { Protocol.Keyword.CHANNELS.name(), pattern });
/* 7957 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */   
/*      */   public Long pubsubNumPat() {
/* 7961 */     checkIsInMultiOrPipeline();
/* 7962 */     this.connection.sendCommand(Protocol.Command.PUBSUB, Protocol.Keyword.NUMPAT);
/* 7963 */     return this.connection.getIntegerReply();
/*      */   }
/*      */   
/*      */   public Map<String, Long> pubsubNumSub(String... channels) {
/* 7967 */     checkIsInMultiOrPipeline();
/* 7968 */     this.connection.sendCommand(Protocol.Command.PUBSUB, joinParameters(Protocol.Keyword.NUMSUB.name(), channels));
/* 7969 */     return BuilderFactory.PUBSUB_NUMSUB_MAP.build(this.connection.getOne());
/*      */   }
/*      */   
/*      */   public List<String> pubsubShardChannels() {
/* 7973 */     checkIsInMultiOrPipeline();
/* 7974 */     this.connection.sendCommand(Protocol.Command.PUBSUB, Protocol.Keyword.SHARDCHANNELS);
/* 7975 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */   
/*      */   public List<String> pubsubShardChannels(String pattern) {
/* 7979 */     checkIsInMultiOrPipeline();
/* 7980 */     this.connection.sendCommand(Protocol.Command.PUBSUB, new String[] { Protocol.Keyword.SHARDCHANNELS.name(), pattern });
/* 7981 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */   
/*      */   public Map<String, Long> pubsubShardNumSub(String... channels) {
/* 7985 */     checkIsInMultiOrPipeline();
/* 7986 */     this.connection.sendCommand(Protocol.Command.PUBSUB, joinParameters(Protocol.Keyword.SHARDNUMSUB.name(), channels));
/* 7987 */     return BuilderFactory.PUBSUB_NUMSUB_MAP.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(String script, int keyCount, String... params) {
/* 7992 */     checkIsInMultiOrPipeline();
/* 7993 */     return this.connection.executeCommand(this.commandObjects.eval(script, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(String script, List<String> keys, List<String> args) {
/* 7998 */     checkIsInMultiOrPipeline();
/* 7999 */     return this.connection.executeCommand(this.commandObjects.eval(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalReadonly(String script, List<String> keys, List<String> args) {
/* 8004 */     checkIsInMultiOrPipeline();
/* 8005 */     return this.connection.executeCommand(this.commandObjects.evalReadonly(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object eval(String script) {
/* 8010 */     checkIsInMultiOrPipeline();
/* 8011 */     return this.connection.executeCommand(this.commandObjects.eval(script));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(String sha1) {
/* 8016 */     checkIsInMultiOrPipeline();
/* 8017 */     return this.connection.executeCommand(this.commandObjects.evalsha(sha1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(String sha1, List<String> keys, List<String> args) {
/* 8022 */     checkIsInMultiOrPipeline();
/* 8023 */     return this.connection.executeCommand(this.commandObjects.evalsha(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalshaReadonly(String sha1, List<String> keys, List<String> args) {
/* 8028 */     checkIsInMultiOrPipeline();
/* 8029 */     return this.connection.executeCommand(this.commandObjects.evalshaReadonly(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object evalsha(String sha1, int keyCount, String... params) {
/* 8034 */     checkIsInMultiOrPipeline();
/* 8035 */     return this.connection.executeCommand(this.commandObjects.evalsha(sha1, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Boolean scriptExists(String sha1) {
/* 8040 */     String[] a = new String[1];
/* 8041 */     a[0] = sha1;
/* 8042 */     return scriptExists(a).get(0);
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Boolean> scriptExists(String... sha1) {
/* 8047 */     this.connection.sendCommand(Protocol.Command.SCRIPT, joinParameters(Protocol.Keyword.EXISTS.name(), sha1));
/* 8048 */     return BuilderFactory.BOOLEAN_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public String scriptLoad(String script) {
/* 8053 */     this.connection.sendCommand(Protocol.Command.SCRIPT, new String[] { Protocol.Keyword.LOAD.name(), script });
/* 8054 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Slowlog> slowlogGet() {
/* 8059 */     this.connection.sendCommand(Protocol.Command.SLOWLOG, Protocol.Keyword.GET);
/* 8060 */     return Slowlog.from(this.connection.getObjectMultiBulkReply());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Slowlog> slowlogGet(long entries) {
/* 8065 */     this.connection.sendCommand(Protocol.Command.SLOWLOG, new byte[][] { Protocol.Keyword.GET.getRaw(), Protocol.toByteArray(entries) });
/* 8066 */     return Slowlog.from(this.connection.getObjectMultiBulkReply());
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectRefcount(String key) {
/* 8071 */     this.connection.sendCommand(Protocol.Command.OBJECT, new String[] { Protocol.Keyword.REFCOUNT.name(), key });
/* 8072 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String objectEncoding(String key) {
/* 8077 */     this.connection.sendCommand(Protocol.Command.OBJECT, new String[] { Protocol.Keyword.ENCODING.name(), key });
/* 8078 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectIdletime(String key) {
/* 8083 */     this.connection.sendCommand(Protocol.Command.OBJECT, new String[] { Protocol.Keyword.IDLETIME.name(), key });
/* 8084 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> objectHelp() {
/* 8089 */     this.connection.sendCommand(Protocol.Command.OBJECT, Protocol.Keyword.HELP);
/* 8090 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long objectFreq(String key) {
/* 8095 */     this.connection.sendCommand(Protocol.Command.OBJECT, new String[] { Protocol.Keyword.FREQ.name(), key });
/* 8096 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(String key) {
/* 8101 */     checkIsInMultiOrPipeline();
/* 8102 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(String key, long start, long end) {
/* 8107 */     checkIsInMultiOrPipeline();
/* 8108 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitcount(key, start, end))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitcount(String key, long start, long end, BitCountOption option) {
/* 8113 */     checkIsInMultiOrPipeline();
/* 8114 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitcount(key, start, end, option))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long bitop(BitOP op, String destKey, String... srcKeys) {
/* 8119 */     checkIsInMultiOrPipeline();
/* 8120 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.bitop(op, destKey, srcKeys))).longValue();
/*      */   }
/*      */   
/*      */   public long commandCount() {
/* 8124 */     checkIsInMultiOrPipeline();
/* 8125 */     this.connection.sendCommand(Protocol.Command.COMMAND, Protocol.Keyword.COUNT);
/* 8126 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */   
/*      */   public Map<String, CommandDocument> commandDocs(String... commands) {
/* 8130 */     checkIsInMultiOrPipeline();
/* 8131 */     this.connection.sendCommand(Protocol.Command.COMMAND, joinParameters(Protocol.Keyword.DOCS.name(), commands));
/* 8132 */     return BuilderFactory.COMMAND_DOCS_RESPONSE.build(this.connection.getOne());
/*      */   }
/*      */   
/*      */   public List<String> commandGetKeys(String... command) {
/* 8136 */     checkIsInMultiOrPipeline();
/* 8137 */     this.connection.sendCommand(Protocol.Command.COMMAND, joinParameters(Protocol.Keyword.GETKEYS.name(), command));
/* 8138 */     return BuilderFactory.STRING_LIST.build(this.connection.getOne());
/*      */   }
/*      */   
/*      */   public List<KeyValue<String, List<String>>> commandGetKeysAndFlags(String... command) {
/* 8142 */     checkIsInMultiOrPipeline();
/* 8143 */     this.connection.sendCommand(Protocol.Command.COMMAND, joinParameters(Protocol.Keyword.GETKEYSANDFLAGS.name(), command));
/* 8144 */     return BuilderFactory.KEYED_STRING_LIST_LIST.build(this.connection.getOne());
/*      */   }
/*      */   
/*      */   public Map<String, CommandInfo> commandInfo(String... commands) {
/* 8148 */     checkIsInMultiOrPipeline();
/* 8149 */     this.connection.sendCommand(Protocol.Command.COMMAND, joinParameters(Protocol.Keyword.INFO.name(), commands));
/* 8150 */     return BuilderFactory.COMMAND_INFO_RESPONSE.build(this.connection.getOne());
/*      */   }
/*      */   
/*      */   public List<String> commandList() {
/* 8154 */     checkIsInMultiOrPipeline();
/* 8155 */     this.connection.sendCommand(Protocol.Command.COMMAND, Protocol.Keyword.LIST);
/* 8156 */     return BuilderFactory.STRING_LIST.build(this.connection.getOne());
/*      */   }
/*      */   
/*      */   public List<String> commandListFilterBy(CommandListFilterByParams filterByParams) {
/* 8160 */     checkIsInMultiOrPipeline();
/* 8161 */     CommandArguments args = (new CommandArguments(Protocol.Command.COMMAND)).add(Protocol.Keyword.LIST).addParams((IParams)filterByParams);
/* 8162 */     this.connection.sendCommand(args);
/* 8163 */     return BuilderFactory.STRING_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public String sentinelMyId() {
/* 8168 */     this.connection.sendCommand(Protocol.Command.SENTINEL, Protocol.SentinelKeyword.MYID);
/* 8169 */     return this.connection.getBulkReply();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Map<String, String>> sentinelMasters() {
/* 8204 */     this.connection.sendCommand(Protocol.Command.SENTINEL, Protocol.SentinelKeyword.MASTERS);
/* 8205 */     return (List<Map<String, String>>)this.connection.getObjectMultiBulkReply().stream()
/* 8206 */       .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, String> sentinelMaster(String masterName) {
/* 8211 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new String[] { Protocol.SentinelKeyword.MASTER.name(), masterName });
/* 8212 */     return BuilderFactory.STRING_MAP.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Map<String, String>> sentinelSentinels(String masterName) {
/* 8217 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new String[] { Protocol.SentinelKeyword.SENTINELS.name(), masterName });
/* 8218 */     return (List<Map<String, String>>)this.connection.getObjectMultiBulkReply().stream()
/* 8219 */       .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
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
/*      */ 
/*      */   
/*      */   public List<String> sentinelGetMasterAddrByName(String masterName) {
/* 8233 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new byte[][] { Protocol.SentinelKeyword.GET_MASTER_ADDR_BY_NAME.getRaw(), SafeEncoder.encode(masterName) });
/* 8234 */     return this.connection.getMultiBulkReply();
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
/*      */   public Long sentinelReset(String pattern) {
/* 8246 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new String[] { Protocol.SentinelKeyword.RESET.name(), pattern });
/* 8247 */     return this.connection.getIntegerReply();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<Map<String, String>> sentinelSlaves(String masterName) {
/* 8287 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new String[] { Protocol.SentinelKeyword.SLAVES.name(), masterName });
/* 8288 */     return (List<Map<String, String>>)this.connection.getObjectMultiBulkReply().stream()
/* 8289 */       .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Map<String, String>> sentinelReplicas(String masterName) {
/* 8294 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new String[] { Protocol.SentinelKeyword.REPLICAS.name(), masterName });
/* 8295 */     return (List<Map<String, String>>)this.connection.getObjectMultiBulkReply().stream()
/* 8296 */       .map(BuilderFactory.STRING_MAP::build).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   public String sentinelFailover(String masterName) {
/* 8301 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new String[] { Protocol.SentinelKeyword.FAILOVER.name(), masterName });
/* 8302 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String sentinelMonitor(String masterName, String ip, int port, int quorum) {
/* 8308 */     CommandArguments args = (new CommandArguments(Protocol.Command.SENTINEL)).add(Protocol.SentinelKeyword.MONITOR).add(masterName).add(ip).add(Integer.valueOf(port)).add(Integer.valueOf(quorum));
/* 8309 */     this.connection.sendCommand(args);
/* 8310 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sentinelRemove(String masterName) {
/* 8315 */     this.connection.sendCommand(Protocol.Command.SENTINEL, new String[] { Protocol.SentinelKeyword.REMOVE.name(), masterName });
/* 8316 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String sentinelSet(String masterName, Map<String, String> parameterMap) {
/* 8321 */     CommandArguments args = (new CommandArguments(Protocol.Command.SENTINEL)).add(Protocol.SentinelKeyword.SET).add(masterName);
/* 8322 */     parameterMap.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
/* 8323 */     this.connection.sendCommand(args);
/* 8324 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] dump(String key) {
/* 8329 */     checkIsInMultiOrPipeline();
/* 8330 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.dump(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public String restore(String key, long ttl, byte[] serializedValue) {
/* 8335 */     checkIsInMultiOrPipeline();
/* 8336 */     return this.connection.<String>executeCommand(this.commandObjects.restore(key, ttl, serializedValue));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String restore(String key, long ttl, byte[] serializedValue, RestoreParams params) {
/* 8342 */     checkIsInMultiOrPipeline();
/* 8343 */     return this.connection.<String>executeCommand(this.commandObjects.restore(key, ttl, serializedValue, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long pttl(String key) {
/* 8348 */     checkIsInMultiOrPipeline();
/* 8349 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pttl(key))).longValue();
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
/*      */   
/*      */   public String psetex(String key, long milliseconds, String value) {
/* 8362 */     checkIsInMultiOrPipeline();
/* 8363 */     return this.connection.<String>executeCommand(this.commandObjects.psetex(key, milliseconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclSetUser(String name) {
/* 8368 */     checkIsInMultiOrPipeline();
/* 8369 */     this.connection.sendCommand(Protocol.Command.ACL, new String[] { Protocol.Keyword.SETUSER.name(), name });
/* 8370 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclSetUser(String name, String... rules) {
/* 8375 */     checkIsInMultiOrPipeline();
/* 8376 */     this.connection.sendCommand(Protocol.Command.ACL, joinParameters(Protocol.Keyword.SETUSER.name(), name, rules));
/* 8377 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long aclDelUser(String... names) {
/* 8382 */     checkIsInMultiOrPipeline();
/* 8383 */     this.connection.sendCommand(Protocol.Command.ACL, joinParameters(Protocol.Keyword.DELUSER.name(), names));
/* 8384 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public AccessControlUser aclGetUser(String name) {
/* 8389 */     checkIsInMultiOrPipeline();
/* 8390 */     this.connection.sendCommand(Protocol.Command.ACL, new String[] { Protocol.Keyword.GETUSER.name(), name });
/* 8391 */     return BuilderFactory.ACCESS_CONTROL_USER.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> aclUsers() {
/* 8396 */     checkIsInMultiOrPipeline();
/* 8397 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.USERS);
/* 8398 */     return BuilderFactory.STRING_LIST.build(this.connection.getObjectMultiBulkReply());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> aclList() {
/* 8403 */     checkIsInMultiOrPipeline();
/* 8404 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.LIST);
/* 8405 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclWhoAmI() {
/* 8410 */     checkIsInMultiOrPipeline();
/* 8411 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.WHOAMI);
/* 8412 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> aclCat() {
/* 8417 */     checkIsInMultiOrPipeline();
/* 8418 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.CAT);
/* 8419 */     return BuilderFactory.STRING_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> aclCat(String category) {
/* 8424 */     checkIsInMultiOrPipeline();
/* 8425 */     this.connection.sendCommand(Protocol.Command.ACL, new String[] { Protocol.Keyword.CAT.name(), category });
/* 8426 */     return BuilderFactory.STRING_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<AccessControlLogEntry> aclLog() {
/* 8431 */     checkIsInMultiOrPipeline();
/* 8432 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.LOG);
/* 8433 */     return BuilderFactory.ACCESS_CONTROL_LOG_ENTRY_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<AccessControlLogEntry> aclLog(int limit) {
/* 8438 */     checkIsInMultiOrPipeline();
/* 8439 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.LOG.getRaw(), Protocol.toByteArray(limit) });
/* 8440 */     return BuilderFactory.ACCESS_CONTROL_LOG_ENTRY_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclLoad() {
/* 8445 */     checkIsInMultiOrPipeline();
/* 8446 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.LOAD);
/* 8447 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclSave() {
/* 8452 */     checkIsInMultiOrPipeline();
/* 8453 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.SAVE);
/* 8454 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclGenPass() {
/* 8459 */     this.connection.sendCommand(Protocol.Command.ACL, Protocol.Keyword.GENPASS);
/* 8460 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclGenPass(int bits) {
/* 8465 */     checkIsInMultiOrPipeline();
/* 8466 */     this.connection.sendCommand(Protocol.Command.ACL, new byte[][] { Protocol.Keyword.GENPASS.getRaw(), Protocol.toByteArray(bits) });
/* 8467 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclDryRun(String username, String command, String... args) {
/* 8472 */     checkIsInMultiOrPipeline();
/* 8473 */     String[] allArgs = new String[3 + args.length];
/* 8474 */     allArgs[0] = Protocol.Keyword.DRYRUN.name();
/* 8475 */     allArgs[1] = username;
/* 8476 */     allArgs[2] = command;
/* 8477 */     System.arraycopy(args, 0, allArgs, 3, args.length);
/* 8478 */     this.connection.sendCommand(Protocol.Command.ACL, allArgs);
/* 8479 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String aclDryRun(String username, CommandArguments commandArgs) {
/* 8484 */     checkIsInMultiOrPipeline();
/* 8485 */     CommandArguments allArgs = (new CommandArguments(Protocol.Command.ACL)).add(Protocol.Keyword.DRYRUN).add(username);
/* 8486 */     Iterator<Rawable> it = commandArgs.iterator();
/* 8487 */     for (; it.hasNext(); allArgs.add(it.next()));
/* 8488 */     this.connection.sendCommand(allArgs);
/* 8489 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] aclDryRunBinary(byte[] username, byte[] command, byte[]... args) {
/* 8494 */     checkIsInMultiOrPipeline();
/* 8495 */     byte[][] allArgs = new byte[3 + args.length][];
/* 8496 */     allArgs[0] = Protocol.Keyword.DRYRUN.getRaw();
/* 8497 */     allArgs[1] = username;
/* 8498 */     allArgs[2] = command;
/* 8499 */     System.arraycopy(args, 0, allArgs, 3, args.length);
/* 8500 */     this.connection.sendCommand(Protocol.Command.ACL, allArgs);
/* 8501 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] aclDryRunBinary(byte[] username, CommandArguments commandArgs) {
/* 8506 */     checkIsInMultiOrPipeline();
/* 8507 */     CommandArguments allArgs = (new CommandArguments(Protocol.Command.ACL)).add(Protocol.Keyword.DRYRUN).add(username);
/* 8508 */     Iterator<Rawable> it = commandArgs.iterator();
/* 8509 */     for (; it.hasNext(); allArgs.add(it.next()));
/* 8510 */     this.connection.sendCommand(allArgs);
/* 8511 */     return this.connection.getBinaryBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientKill(String ipPort) {
/* 8516 */     checkIsInMultiOrPipeline();
/* 8517 */     this.connection.sendCommand(Protocol.Command.CLIENT, new String[] { Protocol.Keyword.KILL.name(), ipPort });
/* 8518 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientGetname() {
/* 8523 */     checkIsInMultiOrPipeline();
/* 8524 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.GETNAME);
/* 8525 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientList() {
/* 8530 */     checkIsInMultiOrPipeline();
/* 8531 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.LIST);
/* 8532 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientList(ClientType type) {
/* 8537 */     checkIsInMultiOrPipeline();
/* 8538 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.LIST.getRaw(), Protocol.Keyword.TYPE.getRaw(), type.getRaw() });
/* 8539 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientList(long... clientIds) {
/* 8544 */     checkIsInMultiOrPipeline();
/* 8545 */     this.connection.sendCommand(Protocol.Command.CLIENT, clientListParams(clientIds));
/* 8546 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientInfo() {
/* 8551 */     checkIsInMultiOrPipeline();
/* 8552 */     this.connection.sendCommand(Protocol.Command.CLIENT, Protocol.Keyword.INFO);
/* 8553 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientSetInfo(ClientAttributeOption attr, String value) {
/* 8558 */     checkIsInMultiOrPipeline();
/* 8559 */     this.connection.sendCommand(Protocol.Command.CLIENT, new byte[][] { Protocol.Keyword.SETINFO.getRaw(), attr.getRaw(), SafeEncoder.encode(value) });
/* 8560 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clientSetname(String name) {
/* 8565 */     checkIsInMultiOrPipeline();
/* 8566 */     this.connection.sendCommand(Protocol.Command.CLIENT, new String[] { Protocol.Keyword.SETNAME.name(), name });
/* 8567 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, String key, int destinationDb, int timeout) {
/* 8573 */     checkIsInMultiOrPipeline();
/* 8574 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, key, destinationDb, timeout));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, int destinationDB, int timeout, MigrateParams params, String... keys) {
/* 8580 */     checkIsInMultiOrPipeline();
/* 8581 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, destinationDB, timeout, params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, String key, int timeout) {
/* 8586 */     checkIsInMultiOrPipeline();
/* 8587 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, key, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public String migrate(String host, int port, int timeout, MigrateParams params, String... keys) {
/* 8592 */     checkIsInMultiOrPipeline();
/* 8593 */     return this.connection.<String>executeCommand(this.commandObjects.migrate(host, port, timeout, params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> scan(String cursor) {
/* 8598 */     return this.connection.<ScanResult<String>>executeCommand(this.commandObjects.scan(cursor));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> scan(String cursor, ScanParams params) {
/* 8603 */     return this.connection.<ScanResult<String>>executeCommand(this.commandObjects.scan(cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> scan(String cursor, ScanParams params, String type) {
/* 8608 */     checkIsInMultiOrPipeline();
/* 8609 */     return this.connection.<ScanResult<String>>executeCommand(this.commandObjects.scan(cursor, params, type));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor, ScanParams params) {
/* 8615 */     checkIsInMultiOrPipeline();
/* 8616 */     return this.connection.<ScanResult<Map.Entry<String, String>>>executeCommand(this.commandObjects.hscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<String> sscan(String key, String cursor, ScanParams params) {
/* 8621 */     checkIsInMultiOrPipeline();
/* 8622 */     return this.connection.<ScanResult<String>>executeCommand(this.commandObjects.sscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
/* 8627 */     checkIsInMultiOrPipeline();
/* 8628 */     return this.connection.<ScanResult<Tuple>>executeCommand(this.commandObjects.zscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public String readonly() {
/* 8633 */     checkIsInMultiOrPipeline();
/* 8634 */     this.connection.sendCommand(Protocol.Command.READONLY);
/* 8635 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String readwrite() {
/* 8640 */     checkIsInMultiOrPipeline();
/* 8641 */     this.connection.sendCommand(Protocol.Command.READWRITE);
/* 8642 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterNodes() {
/* 8647 */     checkIsInMultiOrPipeline();
/* 8648 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.NODES);
/* 8649 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterMeet(String ip, int port) {
/* 8654 */     checkIsInMultiOrPipeline();
/* 8655 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { Protocol.ClusterKeyword.MEET.name(), ip, Integer.toString(port) });
/* 8656 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterReset() {
/* 8661 */     checkIsInMultiOrPipeline();
/* 8662 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.RESET);
/* 8663 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterReset(ClusterResetType resetType) {
/* 8668 */     checkIsInMultiOrPipeline();
/* 8669 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.RESET.getRaw(), resetType.getRaw() });
/* 8670 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterAddSlots(int... slots) {
/* 8675 */     checkIsInMultiOrPipeline();
/* 8676 */     this.connection.sendCommand(Protocol.Command.CLUSTER, joinParameters(Protocol.ClusterKeyword.ADDSLOTS.getRaw(), joinParameters(slots)));
/* 8677 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterDelSlots(int... slots) {
/* 8682 */     checkIsInMultiOrPipeline();
/* 8683 */     this.connection.sendCommand(Protocol.Command.CLUSTER, joinParameters(Protocol.ClusterKeyword.DELSLOTS.getRaw(), joinParameters(slots)));
/* 8684 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterInfo() {
/* 8689 */     checkIsInMultiOrPipeline();
/* 8690 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.INFO);
/* 8691 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> clusterGetKeysInSlot(int slot, int count) {
/* 8696 */     checkIsInMultiOrPipeline();
/* 8697 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.GETKEYSINSLOT.getRaw(), Protocol.toByteArray(slot), Protocol.toByteArray(count) });
/* 8698 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<byte[]> clusterGetKeysInSlotBinary(int slot, int count) {
/* 8703 */     checkIsInMultiOrPipeline();
/* 8704 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.GETKEYSINSLOT.getRaw(), Protocol.toByteArray(slot), Protocol.toByteArray(count) });
/* 8705 */     return this.connection.getBinaryMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterSetSlotNode(int slot, String nodeId) {
/* 8710 */     checkIsInMultiOrPipeline();
/* 8711 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.SETSLOT.getRaw(), Protocol.toByteArray(slot), Protocol.ClusterKeyword.NODE.getRaw(), SafeEncoder.encode(nodeId) });
/* 8712 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterSetSlotMigrating(int slot, String nodeId) {
/* 8717 */     checkIsInMultiOrPipeline();
/* 8718 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.SETSLOT.getRaw(), Protocol.toByteArray(slot), Protocol.ClusterKeyword.MIGRATING.getRaw(), SafeEncoder.encode(nodeId) });
/* 8719 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterSetSlotImporting(int slot, String nodeId) {
/* 8724 */     checkIsInMultiOrPipeline();
/* 8725 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.SETSLOT.getRaw(), Protocol.toByteArray(slot), Protocol.ClusterKeyword.IMPORTING.getRaw(), SafeEncoder.encode(nodeId) });
/* 8726 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterSetSlotStable(int slot) {
/* 8731 */     checkIsInMultiOrPipeline();
/* 8732 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.SETSLOT.getRaw(), Protocol.toByteArray(slot), Protocol.ClusterKeyword.STABLE.getRaw() });
/* 8733 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterForget(String nodeId) {
/* 8738 */     checkIsInMultiOrPipeline();
/* 8739 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { Protocol.ClusterKeyword.FORGET.name(), nodeId });
/* 8740 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterFlushSlots() {
/* 8745 */     checkIsInMultiOrPipeline();
/* 8746 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.FLUSHSLOTS);
/* 8747 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long clusterKeySlot(String key) {
/* 8752 */     checkIsInMultiOrPipeline();
/* 8753 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { Protocol.ClusterKeyword.KEYSLOT.name(), key });
/* 8754 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long clusterCountFailureReports(String nodeId) {
/* 8759 */     checkIsInMultiOrPipeline();
/* 8760 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { "COUNT-FAILURE-REPORTS", nodeId });
/* 8761 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long clusterCountKeysInSlot(int slot) {
/* 8766 */     checkIsInMultiOrPipeline();
/* 8767 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.COUNTKEYSINSLOT.getRaw(), Protocol.toByteArray(slot) });
/* 8768 */     return this.connection.getIntegerReply().longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterSaveConfig() {
/* 8773 */     checkIsInMultiOrPipeline();
/* 8774 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.SAVECONFIG);
/* 8775 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterSetConfigEpoch(long configEpoch) {
/* 8780 */     checkIsInMultiOrPipeline();
/* 8781 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { "SET-CONFIG-EPOCH", Long.toString(configEpoch) });
/* 8782 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterBumpEpoch() {
/* 8787 */     checkIsInMultiOrPipeline();
/* 8788 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.BUMPEPOCH);
/* 8789 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterReplicate(String nodeId) {
/* 8794 */     checkIsInMultiOrPipeline();
/* 8795 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { Protocol.ClusterKeyword.REPLICATE.name(), nodeId });
/* 8796 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> clusterSlaves(String nodeId) {
/* 8802 */     checkIsInMultiOrPipeline();
/* 8803 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { Protocol.ClusterKeyword.SLAVES.name(), nodeId });
/* 8804 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> clusterReplicas(String nodeId) {
/* 8809 */     checkIsInMultiOrPipeline();
/* 8810 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new String[] { Protocol.ClusterKeyword.REPLICAS.name(), nodeId });
/* 8811 */     return this.connection.getMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterFailover() {
/* 8816 */     checkIsInMultiOrPipeline();
/* 8817 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.FAILOVER);
/* 8818 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterFailover(ClusterFailoverOption failoverOption) {
/* 8823 */     checkIsInMultiOrPipeline();
/* 8824 */     this.connection.sendCommand(Protocol.Command.CLUSTER, new byte[][] { Protocol.ClusterKeyword.FAILOVER.getRaw(), failoverOption.getRaw() });
/* 8825 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<Object> clusterSlots() {
/* 8831 */     checkIsInMultiOrPipeline();
/* 8832 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.SLOTS);
/* 8833 */     return this.connection.getObjectMultiBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<ClusterShardInfo> clusterShards() {
/* 8838 */     checkIsInMultiOrPipeline();
/* 8839 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.SHARDS);
/* 8840 */     return BuilderFactory.CLUSTER_SHARD_INFO_LIST.build(this.connection.getObjectMultiBulkReply());
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterMyId() {
/* 8845 */     checkIsInMultiOrPipeline();
/* 8846 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.MYID);
/* 8847 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterMyShardId() {
/* 8852 */     checkIsInMultiOrPipeline();
/* 8853 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.MYSHARDID);
/* 8854 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Map<String, Object>> clusterLinks() {
/* 8859 */     checkIsInMultiOrPipeline();
/* 8860 */     this.connection.sendCommand(Protocol.Command.CLUSTER, Protocol.ClusterKeyword.LINKS);
/* 8861 */     return (List<Map<String, Object>>)this.connection.getObjectMultiBulkReply().stream()
/* 8862 */       .map(BuilderFactory.ENCODED_OBJECT_MAP::build).collect(Collectors.toList());
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterAddSlotsRange(int... ranges) {
/* 8867 */     checkIsInMultiOrPipeline();
/* 8868 */     this.connection.sendCommand(Protocol.Command.CLUSTER, 
/* 8869 */         joinParameters(Protocol.ClusterKeyword.ADDSLOTSRANGE.getRaw(), joinParameters(ranges)));
/* 8870 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String clusterDelSlotsRange(int... ranges) {
/* 8875 */     checkIsInMultiOrPipeline();
/* 8876 */     this.connection.sendCommand(Protocol.Command.CLUSTER, 
/* 8877 */         joinParameters(Protocol.ClusterKeyword.DELSLOTSRANGE.getRaw(), joinParameters(ranges)));
/* 8878 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String asking() {
/* 8883 */     checkIsInMultiOrPipeline();
/* 8884 */     this.connection.sendCommand(Protocol.Command.ASKING);
/* 8885 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfadd(String key, String... elements) {
/* 8890 */     checkIsInMultiOrPipeline();
/* 8891 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pfadd(key, elements))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(String key) {
/* 8896 */     checkIsInMultiOrPipeline();
/* 8897 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pfcount(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long pfcount(String... keys) {
/* 8902 */     checkIsInMultiOrPipeline();
/* 8903 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.pfcount(keys))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String pfmerge(String destkey, String... sourcekeys) {
/* 8908 */     checkIsInMultiOrPipeline();
/* 8909 */     return this.connection.<String>executeCommand(this.commandObjects.pfmerge(destkey, sourcekeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcall(String name, List<String> keys, List<String> args) {
/* 8914 */     return this.connection.executeCommand(this.commandObjects.fcall(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcallReadonly(String name, List<String> keys, List<String> args) {
/* 8919 */     return this.connection.executeCommand(this.commandObjects.fcallReadonly(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionDelete(String libraryName) {
/* 8924 */     checkIsInMultiOrPipeline();
/* 8925 */     return this.connection.<String>executeCommand(this.commandObjects.functionDelete(libraryName));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoad(String functionCode) {
/* 8930 */     checkIsInMultiOrPipeline();
/* 8931 */     return this.connection.<String>executeCommand(this.commandObjects.functionLoad(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoadReplace(String functionCode) {
/* 8936 */     checkIsInMultiOrPipeline();
/* 8937 */     return this.connection.<String>executeCommand(this.commandObjects.functionLoadReplace(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public FunctionStats functionStats() {
/* 8942 */     checkIsInMultiOrPipeline();
/* 8943 */     return this.connection.<FunctionStats>executeCommand(this.commandObjects.functionStats());
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionFlush() {
/* 8948 */     checkIsInMultiOrPipeline();
/* 8949 */     return this.connection.<String>executeCommand(this.commandObjects.functionFlush());
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionFlush(FlushMode mode) {
/* 8954 */     checkIsInMultiOrPipeline();
/* 8955 */     return this.connection.<String>executeCommand(this.commandObjects.functionFlush(mode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionKill() {
/* 8960 */     checkIsInMultiOrPipeline();
/* 8961 */     return this.connection.<String>executeCommand(this.commandObjects.functionKill());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> functionList() {
/* 8966 */     checkIsInMultiOrPipeline();
/* 8967 */     return this.connection.<List<LibraryInfo>>executeCommand(this.commandObjects.functionList());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> functionList(String libraryNamePattern) {
/* 8972 */     checkIsInMultiOrPipeline();
/* 8973 */     return this.connection.<List<LibraryInfo>>executeCommand(this.commandObjects.functionList(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<LibraryInfo> functionListWithCode() {
/* 8978 */     checkIsInMultiOrPipeline();
/* 8979 */     return this.connection.<List<LibraryInfo>>executeCommand(this.commandObjects.functionListWithCode());
/*      */   }
/*      */   
/*      */   public List<LibraryInfo> functionListWithCode(String libraryNamePattern) {
/* 8983 */     checkIsInMultiOrPipeline();
/* 8984 */     return this.connection.<List<LibraryInfo>>executeCommand(this.commandObjects.functionListWithCode(libraryNamePattern));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long geoadd(String key, double longitude, double latitude, String member) {
/* 8990 */     checkIsInMultiOrPipeline();
/* 8991 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geoadd(key, longitude, latitude, member))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 8996 */     checkIsInMultiOrPipeline();
/* 8997 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geoadd(key, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geoadd(String key, GeoAddParams params, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 9002 */     checkIsInMultiOrPipeline();
/* 9003 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geoadd(key, params, memberCoordinateMap))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public Double geodist(String key, String member1, String member2) {
/* 9008 */     checkIsInMultiOrPipeline();
/* 9009 */     return this.connection.<Double>executeCommand(this.commandObjects.geodist(key, member1, member2));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Double geodist(String key, String member1, String member2, GeoUnit unit) {
/* 9015 */     checkIsInMultiOrPipeline();
/* 9016 */     return this.connection.<Double>executeCommand(this.commandObjects.geodist(key, member1, member2, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<String> geohash(String key, String... members) {
/* 9021 */     checkIsInMultiOrPipeline();
/* 9022 */     return this.connection.<List<String>>executeCommand(this.commandObjects.geohash(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoCoordinate> geopos(String key, String... members) {
/* 9027 */     checkIsInMultiOrPipeline();
/* 9028 */     return this.connection.<List<GeoCoordinate>>executeCommand(this.commandObjects.geopos(key, members));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 9034 */     checkIsInMultiOrPipeline();
/* 9035 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 9041 */     checkIsInMultiOrPipeline();
/* 9042 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 9048 */     checkIsInMultiOrPipeline();
/* 9049 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long georadiusStore(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 9055 */     checkIsInMultiOrPipeline();
/* 9056 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 9062 */     checkIsInMultiOrPipeline();
/* 9063 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
/* 9069 */     checkIsInMultiOrPipeline();
/* 9070 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit) {
/* 9076 */     checkIsInMultiOrPipeline();
/* 9077 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 9083 */     checkIsInMultiOrPipeline();
/* 9084 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMember(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public long georadiusByMemberStore(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 9090 */     checkIsInMultiOrPipeline();
/* 9091 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 9097 */     checkIsInMultiOrPipeline();
/* 9098 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, String member, double radius, GeoUnit unit) {
/* 9103 */     checkIsInMultiOrPipeline();
/* 9104 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 9109 */     checkIsInMultiOrPipeline();
/* 9110 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, String member, double width, double height, GeoUnit unit) {
/* 9115 */     checkIsInMultiOrPipeline();
/* 9116 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 9121 */     checkIsInMultiOrPipeline();
/* 9122 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<GeoRadiusResponse> geosearch(String key, GeoSearchParam params) {
/* 9127 */     checkIsInMultiOrPipeline();
/* 9128 */     return this.connection.<List<GeoRadiusResponse>>executeCommand(this.commandObjects.geosearch(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, String member, double radius, GeoUnit unit) {
/* 9133 */     checkIsInMultiOrPipeline();
/* 9134 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, member, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 9139 */     checkIsInMultiOrPipeline();
/* 9140 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, coord, radius, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, String member, double width, double height, GeoUnit unit) {
/* 9145 */     checkIsInMultiOrPipeline();
/* 9146 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, member, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 9151 */     checkIsInMultiOrPipeline();
/* 9152 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, coord, width, height, unit))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStore(String dest, String src, GeoSearchParam params) {
/* 9157 */     checkIsInMultiOrPipeline();
/* 9158 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStore(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long geosearchStoreStoreDist(String dest, String src, GeoSearchParam params) {
/* 9163 */     checkIsInMultiOrPipeline();
/* 9164 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.geosearchStoreStoreDist(dest, src, params))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String moduleLoad(String path) {
/* 9169 */     checkIsInMultiOrPipeline();
/* 9170 */     this.connection.sendCommand(Protocol.Command.MODULE, new String[] { Protocol.Keyword.LOAD.name(), path });
/* 9171 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String moduleLoad(String path, String... args) {
/* 9176 */     checkIsInMultiOrPipeline();
/* 9177 */     this.connection.sendCommand(Protocol.Command.MODULE, joinParameters(Protocol.Keyword.LOAD.name(), path, args));
/* 9178 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String moduleLoadEx(String path, ModuleLoadExParams params) {
/* 9183 */     checkIsInMultiOrPipeline();
/* 9184 */     this.connection.sendCommand((new CommandArguments(Protocol.Command.MODULE)).add(Protocol.Keyword.LOADEX).add(path)
/* 9185 */         .addParams((IParams)params));
/* 9186 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String moduleUnload(String name) {
/* 9191 */     checkIsInMultiOrPipeline();
/* 9192 */     this.connection.sendCommand(Protocol.Command.MODULE, new String[] { Protocol.Keyword.UNLOAD.name(), name });
/* 9193 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Module> moduleList() {
/* 9198 */     checkIsInMultiOrPipeline();
/* 9199 */     this.connection.sendCommand(Protocol.Command.MODULE, Protocol.Keyword.LIST);
/* 9200 */     return BuilderFactory.MODULE_LIST.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfield(String key, String... arguments) {
/* 9205 */     checkIsInMultiOrPipeline();
/* 9206 */     return this.connection.<List<Long>>executeCommand(this.commandObjects.bitfield(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Long> bitfieldReadonly(String key, String... arguments) {
/* 9211 */     checkIsInMultiOrPipeline();
/* 9212 */     return this.connection.<List<Long>>executeCommand(this.commandObjects.bitfieldReadonly(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public long hstrlen(String key, String field) {
/* 9217 */     checkIsInMultiOrPipeline();
/* 9218 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.hstrlen(key, field))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public String memoryDoctor() {
/* 9223 */     checkIsInMultiOrPipeline();
/* 9224 */     this.connection.sendCommand(Protocol.Command.MEMORY, Protocol.Keyword.DOCTOR);
/* 9225 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(String key) {
/* 9230 */     checkIsInMultiOrPipeline();
/* 9231 */     this.connection.sendCommand(Protocol.Command.MEMORY, new String[] { Protocol.Keyword.USAGE.name(), key });
/* 9232 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Long memoryUsage(String key, int samples) {
/* 9237 */     checkIsInMultiOrPipeline();
/* 9238 */     this.connection.sendCommand(Protocol.Command.MEMORY, new byte[][] { Protocol.Keyword.USAGE.getRaw(), SafeEncoder.encode(key), Protocol.Keyword.SAMPLES.getRaw(), Protocol.toByteArray(samples) });
/* 9239 */     return this.connection.getIntegerReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String memoryPurge() {
/* 9244 */     checkIsInMultiOrPipeline();
/* 9245 */     this.connection.sendCommand(Protocol.Command.MEMORY, Protocol.Keyword.PURGE);
/* 9246 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Object> memoryStats() {
/* 9251 */     checkIsInMultiOrPipeline();
/* 9252 */     this.connection.sendCommand(Protocol.Command.MEMORY, Protocol.Keyword.STATS);
/* 9253 */     return BuilderFactory.ENCODED_OBJECT_MAP.build(this.connection.getOne());
/*      */   }
/*      */ 
/*      */   
/*      */   public String lolwut() {
/* 9258 */     checkIsInMultiOrPipeline();
/* 9259 */     this.connection.sendCommand(Protocol.Command.LOLWUT);
/* 9260 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String lolwut(LolwutParams lolwutParams) {
/* 9265 */     checkIsInMultiOrPipeline();
/* 9266 */     this.connection.sendCommand((new CommandArguments(Protocol.Command.LOLWUT)).addParams((IParams)lolwutParams));
/* 9267 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String reset() {
/* 9272 */     this.connection.sendCommand(Protocol.Command.RESET);
/* 9273 */     return this.connection.getStatusCodeReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public String latencyDoctor() {
/* 9278 */     checkIsInMultiOrPipeline();
/* 9279 */     this.connection.sendCommand(Protocol.Command.LATENCY, Protocol.Keyword.DOCTOR);
/* 9280 */     return this.connection.getBulkReply();
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamEntryID xadd(String key, StreamEntryID id, Map<String, String> hash) {
/* 9285 */     checkIsInMultiOrPipeline();
/* 9286 */     return this.connection.<StreamEntryID>executeCommand(this.commandObjects.xadd(key, id, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamEntryID xadd(String key, XAddParams params, Map<String, String> hash) {
/* 9291 */     checkIsInMultiOrPipeline();
/* 9292 */     return this.connection.<StreamEntryID>executeCommand(this.commandObjects.xadd(key, params, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xlen(String key) {
/* 9297 */     checkIsInMultiOrPipeline();
/* 9298 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xlen(key))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end) {
/* 9303 */     checkIsInMultiOrPipeline();
/* 9304 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, StreamEntryID start, StreamEntryID end, int count) {
/* 9310 */     checkIsInMultiOrPipeline();
/* 9311 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start) {
/* 9317 */     checkIsInMultiOrPipeline();
/* 9318 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count) {
/* 9324 */     checkIsInMultiOrPipeline();
/* 9325 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, String start, String end) {
/* 9330 */     checkIsInMultiOrPipeline();
/* 9331 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrange(String key, String start, String end, int count) {
/* 9336 */     checkIsInMultiOrPipeline();
/* 9337 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, String end, String start) {
/* 9342 */     checkIsInMultiOrPipeline();
/* 9343 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xrevrange(String key, String end, String start, int count) {
/* 9348 */     checkIsInMultiOrPipeline();
/* 9349 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Map.Entry<String, List<StreamEntry>>> xread(XReadParams xReadParams, Map<String, StreamEntryID> streams) {
/* 9354 */     checkIsInMultiOrPipeline();
/* 9355 */     return this.connection.<List<Map.Entry<String, List<StreamEntry>>>>executeCommand(this.commandObjects.xread(xReadParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xack(String key, String group, StreamEntryID... ids) {
/* 9360 */     checkIsInMultiOrPipeline();
/* 9361 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xack(key, group, ids))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String xgroupCreate(String key, String groupName, StreamEntryID id, boolean makeStream) {
/* 9367 */     checkIsInMultiOrPipeline();
/* 9368 */     return this.connection.<String>executeCommand(this.commandObjects.xgroupCreate(key, groupName, id, makeStream));
/*      */   }
/*      */ 
/*      */   
/*      */   public String xgroupSetID(String key, String groupName, StreamEntryID id) {
/* 9373 */     checkIsInMultiOrPipeline();
/* 9374 */     return this.connection.<String>executeCommand(this.commandObjects.xgroupSetID(key, groupName, id));
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDestroy(String key, String groupName) {
/* 9379 */     checkIsInMultiOrPipeline();
/* 9380 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xgroupDestroy(key, groupName))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean xgroupCreateConsumer(String key, String groupName, String consumerName) {
/* 9385 */     checkIsInMultiOrPipeline();
/* 9386 */     return ((Boolean)this.connection.<Boolean>executeCommand(this.commandObjects.xgroupCreateConsumer(key, groupName, consumerName))).booleanValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xgroupDelConsumer(String key, String groupName, String consumerName) {
/* 9391 */     checkIsInMultiOrPipeline();
/* 9392 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xgroupDelConsumer(key, groupName, consumerName))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xdel(String key, StreamEntryID... ids) {
/* 9397 */     checkIsInMultiOrPipeline();
/* 9398 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xdel(key, ids))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(String key, long maxLen, boolean approximateLength) {
/* 9403 */     checkIsInMultiOrPipeline();
/* 9404 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xtrim(key, maxLen, approximateLength))).longValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public long xtrim(String key, XTrimParams params) {
/* 9409 */     checkIsInMultiOrPipeline();
/* 9410 */     return ((Long)this.connection.<Long>executeCommand(this.commandObjects.xtrim(key, params))).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Map.Entry<String, List<StreamEntry>>> xreadGroup(String groupName, String consumer, XReadGroupParams xReadGroupParams, Map<String, StreamEntryID> streams) {
/* 9417 */     checkIsInMultiOrPipeline();
/* 9418 */     return this.connection.<List<Map.Entry<String, List<StreamEntry>>>>executeCommand(this.commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamPendingSummary xpending(String key, String groupName) {
/* 9423 */     checkIsInMultiOrPipeline();
/* 9424 */     return this.connection.<StreamPendingSummary>executeCommand(this.commandObjects.xpending(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamPendingEntry> xpending(String key, String groupName, XPendingParams params) {
/* 9429 */     checkIsInMultiOrPipeline();
/* 9430 */     return this.connection.<List<StreamPendingEntry>>executeCommand(this.commandObjects.xpending(key, groupName, params));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<StreamEntry> xclaim(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 9436 */     checkIsInMultiOrPipeline();
/* 9437 */     return this.connection.<List<StreamEntry>>executeCommand(this.commandObjects.xclaim(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public List<StreamEntryID> xclaimJustId(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 9443 */     checkIsInMultiOrPipeline();
/* 9444 */     return this.connection.<List<StreamEntryID>>executeCommand(this.commandObjects.xclaimJustId(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map.Entry<StreamEntryID, List<StreamEntry>> xautoclaim(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 9450 */     checkIsInMultiOrPipeline();
/* 9451 */     return this.connection.<Map.Entry<StreamEntryID, List<StreamEntry>>>executeCommand(this.commandObjects.xautoclaim(key, group, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Map.Entry<StreamEntryID, List<StreamEntryID>> xautoclaimJustId(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 9457 */     checkIsInMultiOrPipeline();
/* 9458 */     return this.connection.<Map.Entry<StreamEntryID, List<StreamEntryID>>>executeCommand(this.commandObjects.xautoclaimJustId(key, group, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamInfo xinfoStream(String key) {
/* 9463 */     return this.connection.<StreamInfo>executeCommand(this.commandObjects.xinfoStream(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamFullInfo xinfoStreamFull(String key) {
/* 9468 */     checkIsInMultiOrPipeline();
/* 9469 */     return this.connection.<StreamFullInfo>executeCommand(this.commandObjects.xinfoStreamFull(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public StreamFullInfo xinfoStreamFull(String key, int count) {
/* 9474 */     checkIsInMultiOrPipeline();
/* 9475 */     return this.connection.<StreamFullInfo>executeCommand(this.commandObjects.xinfoStreamFull(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamGroupInfo> xinfoGroups(String key) {
/* 9480 */     return this.connection.<List<StreamGroupInfo>>executeCommand(this.commandObjects.xinfoGroups(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamConsumersInfo> xinfoConsumers(String key, String group) {
/* 9485 */     return this.connection.<List<StreamConsumersInfo>>executeCommand(this.commandObjects.xinfoConsumers(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<StreamConsumerInfo> xinfoConsumers2(String key, String group) {
/* 9490 */     return this.connection.<List<StreamConsumerInfo>>executeCommand(this.commandObjects.xinfoConsumers2(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcall(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 9495 */     checkIsInMultiOrPipeline();
/* 9496 */     return this.connection.executeCommand(this.commandObjects.fcall(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object fcallReadonly(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 9501 */     checkIsInMultiOrPipeline();
/* 9502 */     return this.connection.executeCommand(this.commandObjects.fcallReadonly(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionDelete(byte[] libraryName) {
/* 9507 */     checkIsInMultiOrPipeline();
/* 9508 */     return this.connection.<String>executeCommand(this.commandObjects.functionDelete(libraryName));
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] functionDump() {
/* 9513 */     checkIsInMultiOrPipeline();
/* 9514 */     return this.connection.<byte[]>executeCommand((CommandObject)this.commandObjects.functionDump());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionListBinary() {
/* 9519 */     checkIsInMultiOrPipeline();
/* 9520 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.functionListBinary());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionList(byte[] libraryNamePattern) {
/* 9525 */     checkIsInMultiOrPipeline();
/* 9526 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.functionList(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionListWithCodeBinary() {
/* 9531 */     checkIsInMultiOrPipeline();
/* 9532 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.functionListWithCodeBinary());
/*      */   }
/*      */ 
/*      */   
/*      */   public List<Object> functionListWithCode(byte[] libraryNamePattern) {
/* 9537 */     checkIsInMultiOrPipeline();
/* 9538 */     return this.connection.<List<Object>>executeCommand(this.commandObjects.functionListWithCode(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoad(byte[] functionCode) {
/* 9543 */     checkIsInMultiOrPipeline();
/* 9544 */     return this.connection.<String>executeCommand(this.commandObjects.functionLoad(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionLoadReplace(byte[] functionCode) {
/* 9549 */     checkIsInMultiOrPipeline();
/* 9550 */     return this.connection.<String>executeCommand(this.commandObjects.functionLoadReplace(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionRestore(byte[] serializedValue) {
/* 9555 */     checkIsInMultiOrPipeline();
/* 9556 */     return this.connection.<String>executeCommand(this.commandObjects.functionRestore(serializedValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public String functionRestore(byte[] serializedValue, FunctionRestorePolicy policy) {
/* 9561 */     checkIsInMultiOrPipeline();
/* 9562 */     return this.connection.<String>executeCommand(this.commandObjects.functionRestore(serializedValue, policy));
/*      */   }
/*      */ 
/*      */   
/*      */   public Object functionStatsBinary() {
/* 9567 */     checkIsInMultiOrPipeline();
/* 9568 */     return this.connection.executeCommand(this.commandObjects.functionStatsBinary());
/*      */   }
/*      */   
/*      */   public Object sendCommand(ProtocolCommand cmd, String... args) {
/* 9572 */     checkIsInMultiOrPipeline();
/* 9573 */     this.connection.sendCommand(cmd, args);
/* 9574 */     return this.connection.getOne();
/*      */   }
/*      */   
/*      */   public Object sendBlockingCommand(ProtocolCommand cmd, String... args) {
/* 9578 */     checkIsInMultiOrPipeline();
/* 9579 */     this.connection.sendCommand(cmd, args);
/* 9580 */     this.connection.setTimeoutInfinite();
/*      */     try {
/* 9582 */       return this.connection.getOne();
/*      */     } finally {
/* 9584 */       this.connection.rollbackTimeout();
/*      */     } 
/*      */   }
/*      */   
/*      */   private static byte[][] joinParameters(int... params) {
/* 9589 */     byte[][] result = new byte[params.length][];
/* 9590 */     for (int i = 0; i < params.length; i++) {
/* 9591 */       result[i] = Protocol.toByteArray(params[i]);
/*      */     }
/* 9593 */     return result;
/*      */   }
/*      */   
/*      */   private static byte[][] joinParameters(byte[] first, byte[][] rest) {
/* 9597 */     byte[][] result = new byte[rest.length + 1][];
/* 9598 */     result[0] = first;
/* 9599 */     System.arraycopy(rest, 0, result, 1, rest.length);
/* 9600 */     return result;
/*      */   }
/*      */   
/*      */   private static byte[][] joinParameters(byte[] first, byte[] second, byte[][] rest) {
/* 9604 */     byte[][] result = new byte[rest.length + 2][];
/* 9605 */     result[0] = first;
/* 9606 */     result[1] = second;
/* 9607 */     System.arraycopy(rest, 0, result, 2, rest.length);
/* 9608 */     return result;
/*      */   }
/*      */   
/*      */   private static String[] joinParameters(String first, String[] rest) {
/* 9612 */     String[] result = new String[rest.length + 1];
/* 9613 */     result[0] = first;
/* 9614 */     System.arraycopy(rest, 0, result, 1, rest.length);
/* 9615 */     return result;
/*      */   }
/*      */   
/*      */   private static String[] joinParameters(String first, String second, String[] rest) {
/* 9619 */     String[] result = new String[rest.length + 2];
/* 9620 */     result[0] = first;
/* 9621 */     result[1] = second;
/* 9622 */     System.arraycopy(rest, 0, result, 2, rest.length);
/* 9623 */     return result;
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\Jedis.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */