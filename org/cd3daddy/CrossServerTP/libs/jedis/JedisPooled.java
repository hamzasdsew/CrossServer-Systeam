/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.commons.pool2.PooledObjectFactory;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.PooledConnectionProvider;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisURIHelper;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Pool;
/*     */ 
/*     */ public class JedisPooled
/*     */   extends UnifiedJedis
/*     */ {
/*     */   public JedisPooled() {
/*  18 */     this("127.0.0.1", 6379);
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
/*     */   public JedisPooled(String url) {
/*  30 */     this(URI.create(url));
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
/*     */ 
/*     */   
/*     */   public JedisPooled(String url, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  48 */     this(URI.create(url), sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */   
/*     */   public JedisPooled(String host, int port) {
/*  52 */     this(new HostAndPort(host, port));
/*     */   }
/*     */   
/*     */   public JedisPooled(HostAndPort hostAndPort) {
/*  56 */     this(new PooledConnectionProvider(hostAndPort));
/*     */   }
/*     */   
/*     */   public JedisPooled(String host, int port, boolean ssl) {
/*  60 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.builder().ssl(ssl).build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  66 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.builder().ssl(ssl)
/*  67 */         .sslSocketFactory(sslSocketFactory).sslParameters(sslParameters)
/*  68 */         .hostnameVerifier(hostnameVerifier).build());
/*     */   }
/*     */   
/*     */   public JedisPooled(String host, int port, String user, String password) {
/*  72 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.builder().user(user).password(password).build());
/*     */   }
/*     */   
/*     */   public JedisPooled(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/*  76 */     this(new PooledConnectionProvider(hostAndPort, clientConfig));
/*     */   }
/*     */   
/*     */   public JedisPooled(PooledObjectFactory<Connection> factory) {
/*  80 */     this(new PooledConnectionProvider(factory));
/*     */   }
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig) {
/*  84 */     this(poolConfig, "127.0.0.1", 6379);
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
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String url) {
/*  99 */     this(poolConfig, URI.create(url));
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port) {
/* 104 */     this(poolConfig, host, port, 2000);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, boolean ssl) {
/* 109 */     this(poolConfig, host, port, 2000, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 115 */     this(poolConfig, host, port, 2000, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, String user, String password) {
/* 121 */     this(poolConfig, host, port, 2000, user, password, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout) {
/* 127 */     this(poolConfig, host, port, timeout, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, boolean ssl) {
/* 132 */     this(poolConfig, host, port, timeout, (String)null, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 139 */     this(poolConfig, host, port, timeout, (String)null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password) {
/* 145 */     this(poolConfig, host, port, timeout, password, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, boolean ssl) {
/* 150 */     this(poolConfig, host, port, timeout, password, 0, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 156 */     this(poolConfig, host, port, timeout, password, 0, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String user, String password) {
/* 162 */     this(poolConfig, host, port, timeout, user, password, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String user, String password, boolean ssl) {
/* 167 */     this(poolConfig, host, port, timeout, user, password, 0, ssl);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, int database) {
/* 172 */     this(poolConfig, host, port, timeout, password, database, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, int database, boolean ssl) {
/* 177 */     this(poolConfig, host, port, timeout, password, database, (String)null, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, int database, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 184 */     this(poolConfig, host, port, timeout, password, database, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String user, String password, int database) {
/* 190 */     this(poolConfig, host, port, timeout, user, password, database, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String user, String password, int database, boolean ssl) {
/* 195 */     this(poolConfig, host, port, timeout, user, password, database, (String)null, ssl);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, int database, String clientName) {
/* 200 */     this(poolConfig, host, port, timeout, timeout, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl) {
/* 206 */     this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 213 */     this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String user, String password, int database, String clientName) {
/* 220 */     this(poolConfig, host, port, timeout, timeout, user, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int timeout, String user, String password, int database, String clientName, boolean ssl) {
/* 226 */     this(poolConfig, host, port, timeout, timeout, user, password, database, clientName, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName) {
/* 232 */     this(poolConfig, host, port, connectionTimeout, soTimeout, (String)null, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl) {
/* 238 */     this(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName, ssl, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 246 */     this(poolConfig, host, port, connectionTimeout, soTimeout, (String)null, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName) {
/* 253 */     this(poolConfig, host, port, connectionTimeout, soTimeout, 0, user, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName, boolean ssl) {
/* 259 */     this(poolConfig, host, port, connectionTimeout, soTimeout, user, password, database, clientName, ssl, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 268 */     this(poolConfig, host, port, connectionTimeout, soTimeout, 0, user, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 277 */     this(poolConfig, host, port, connectionTimeout, soTimeout, infiniteSoTimeout, null, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName) {
/* 284 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.create(connectionTimeout, soTimeout, infiniteSoTimeout, user, password, database, clientName, false, null, null, null, null), poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 294 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.create(connectionTimeout, soTimeout, infiniteSoTimeout, user, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier, null), poolConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(URI uri) {
/* 300 */     super(uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 305 */     this(new GenericObjectPoolConfig(), uri, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(URI uri, int timeout) {
/* 310 */     this(new GenericObjectPoolConfig(), uri, timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 315 */     this(new GenericObjectPoolConfig(), uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, URI uri) {
/* 320 */     this(poolConfig, uri, 2000);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 326 */     this(poolConfig, uri, 2000, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, URI uri, int timeout) {
/* 332 */     this(poolConfig, uri, timeout, timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 338 */     this(poolConfig, uri, timeout, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, URI uri, int connectionTimeout, int soTimeout) {
/* 343 */     this(poolConfig, uri, connectionTimeout, soTimeout, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 349 */     this(poolConfig, uri, connectionTimeout, soTimeout, 0, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, URI uri, int connectionTimeout, int soTimeout, int infiniteSoTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 356 */     this(new HostAndPort(uri.getHost(), uri.getPort()), DefaultJedisClientConfig.builder()
/* 357 */         .connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout)
/* 358 */         .blockingSocketTimeoutMillis(infiniteSoTimeout).user(JedisURIHelper.getUser(uri))
/* 359 */         .password(JedisURIHelper.getPassword(uri)).database(JedisURIHelper.getDBIndex(uri))
/* 360 */         .protocol(JedisURIHelper.getRedisProtocol(uri)).ssl(JedisURIHelper.isRedisSSLScheme(uri))
/* 361 */         .sslSocketFactory(sslSocketFactory).sslParameters(sslParameters)
/* 362 */         .hostnameVerifier(hostnameVerifier).build(), poolConfig);
/*     */   }
/*     */   
/*     */   public JedisPooled(HostAndPort hostAndPort, GenericObjectPoolConfig<Connection> poolConfig) {
/* 366 */     this(hostAndPort, DefaultJedisClientConfig.builder().build(), poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/* 371 */     this(hostAndPort, clientConfig, poolConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(HostAndPort hostAndPort, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig) {
/* 376 */     this(new PooledConnectionProvider(hostAndPort, clientConfig, poolConfig));
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, JedisSocketFactory jedisSocketFactory, JedisClientConfig clientConfig) {
/* 381 */     this(new ConnectionFactory(jedisSocketFactory, clientConfig), poolConfig);
/*     */   }
/*     */   
/*     */   public JedisPooled(GenericObjectPoolConfig<Connection> poolConfig, PooledObjectFactory<Connection> factory) {
/* 385 */     this(factory, poolConfig);
/*     */   }
/*     */   
/*     */   public JedisPooled(PooledObjectFactory<Connection> factory, GenericObjectPoolConfig<Connection> poolConfig) {
/* 389 */     this(new PooledConnectionProvider(factory, poolConfig));
/*     */   }
/*     */   
/*     */   public JedisPooled(PooledConnectionProvider provider) {
/* 393 */     super((ConnectionProvider)provider);
/*     */   }
/*     */   
/*     */   public final Pool<Connection> getPool() {
/* 397 */     return ((PooledConnectionProvider)this.provider).getPool();
/*     */   }
/*     */ 
/*     */   
/*     */   public Pipeline pipelined() {
/* 402 */     return (Pipeline)super.pipelined();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisPooled.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */