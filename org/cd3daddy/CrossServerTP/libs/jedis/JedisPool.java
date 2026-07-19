/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.commons.pool2.PooledObjectFactory;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Pool;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JedisPool
/*     */   extends Pool<Jedis>
/*     */ {
/*  19 */   private static final Logger log = LoggerFactory.getLogger(JedisPool.class);
/*     */   
/*     */   public JedisPool() {
/*  22 */     this("127.0.0.1", 6379);
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
/*     */   public JedisPool(String url) {
/*  34 */     this(URI.create(url));
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
/*     */   public JedisPool(String url, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  52 */     this(new GenericObjectPoolConfig(), new JedisFactory(URI.create(url), 2000, 2000, null, sslSocketFactory, sslParameters, hostnameVerifier));
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(String host, int port) {
/*  57 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.builder().build());
/*     */   }
/*     */   
/*     */   public JedisPool(String host, int port, boolean ssl) {
/*  61 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.builder().ssl(ssl).build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  67 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.builder().ssl(ssl)
/*  68 */         .sslSocketFactory(sslSocketFactory).sslParameters(sslParameters)
/*  69 */         .hostnameVerifier(hostnameVerifier).build());
/*     */   }
/*     */   
/*     */   public JedisPool(String host, int port, String user, String password) {
/*  73 */     this(new HostAndPort(host, port), DefaultJedisClientConfig.builder().user(user).password(password).build());
/*     */   }
/*     */   
/*     */   public JedisPool(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/*  77 */     this(new JedisFactory(hostAndPort, clientConfig));
/*     */   }
/*     */   
/*     */   public JedisPool(PooledObjectFactory<Jedis> factory) {
/*  81 */     super(factory);
/*     */   }
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig) {
/*  85 */     this(poolConfig, "127.0.0.1", 6379);
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
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String url) {
/* 100 */     this(poolConfig, URI.create(url));
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port) {
/* 105 */     this(poolConfig, host, port, 2000);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, boolean ssl) {
/* 110 */     this(poolConfig, host, port, 2000, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 116 */     this(poolConfig, host, port, 2000, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, String user, String password) {
/* 122 */     this(poolConfig, host, port, 2000, user, password, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout) {
/* 127 */     this(poolConfig, host, port, timeout, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, boolean ssl) {
/* 132 */     this(poolConfig, host, port, timeout, (String)null, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 139 */     this(poolConfig, host, port, timeout, (String)null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password) {
/* 145 */     this(poolConfig, host, port, timeout, password, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, boolean ssl) {
/* 150 */     this(poolConfig, host, port, timeout, password, 0, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 157 */     this(poolConfig, host, port, timeout, password, 0, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String user, String password) {
/* 163 */     this(poolConfig, host, port, timeout, user, password, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String user, String password, boolean ssl) {
/* 168 */     this(poolConfig, host, port, timeout, user, password, 0, ssl);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, int database) {
/* 173 */     this(poolConfig, host, port, timeout, password, database, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, int database, boolean ssl) {
/* 178 */     this(poolConfig, host, port, timeout, password, database, (String)null, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, int database, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 185 */     this(poolConfig, host, port, timeout, password, database, null, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String user, String password, int database) {
/* 191 */     this(poolConfig, host, port, timeout, user, password, database, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String user, String password, int database, boolean ssl) {
/* 196 */     this(poolConfig, host, port, timeout, user, password, database, (String)null, ssl);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, int database, String clientName) {
/* 201 */     this(poolConfig, host, port, timeout, timeout, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl) {
/* 207 */     this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 214 */     this(poolConfig, host, port, timeout, timeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String user, String password, int database, String clientName) {
/* 221 */     this(poolConfig, host, port, timeout, timeout, user, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int timeout, String user, String password, int database, String clientName, boolean ssl) {
/* 227 */     this(poolConfig, host, port, timeout, timeout, user, password, database, clientName, ssl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName) {
/* 233 */     this(poolConfig, new JedisFactory(host, port, connectionTimeout, soTimeout, password, database, clientName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl) {
/* 240 */     this(poolConfig, host, port, connectionTimeout, soTimeout, password, database, clientName, ssl, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 248 */     this(poolConfig, new JedisFactory(host, port, connectionTimeout, soTimeout, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName) {
/* 255 */     this(poolConfig, new JedisFactory(host, port, connectionTimeout, soTimeout, user, password, database, clientName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName, boolean ssl) {
/* 262 */     this(poolConfig, host, port, connectionTimeout, soTimeout, user, password, database, clientName, ssl, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 271 */     this(poolConfig, host, port, connectionTimeout, soTimeout, 0, user, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 280 */     this(poolConfig, host, port, connectionTimeout, soTimeout, infiniteSoTimeout, null, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName) {
/* 287 */     this(poolConfig, new JedisFactory(host, port, connectionTimeout, soTimeout, infiniteSoTimeout, user, password, database, clientName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 296 */     this(poolConfig, new JedisFactory(host, port, connectionTimeout, soTimeout, infiniteSoTimeout, user, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(URI uri) {
/* 302 */     this(new GenericObjectPoolConfig(), uri);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 307 */     this(new GenericObjectPoolConfig(), uri, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(URI uri, int timeout) {
/* 312 */     this(new GenericObjectPoolConfig(), uri, timeout);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 317 */     this(new GenericObjectPoolConfig(), uri, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, URI uri) {
/* 322 */     this(poolConfig, uri, 2000);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, URI uri, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 328 */     this(poolConfig, uri, 2000, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, URI uri, int timeout) {
/* 333 */     this(poolConfig, uri, timeout, timeout);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, URI uri, int timeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 339 */     this(poolConfig, uri, timeout, timeout, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, URI uri, int connectionTimeout, int soTimeout) {
/* 344 */     this(poolConfig, uri, connectionTimeout, soTimeout, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, URI uri, int connectionTimeout, int soTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 350 */     this(poolConfig, new JedisFactory(uri, connectionTimeout, soTimeout, null, sslSocketFactory, sslParameters, hostnameVerifier));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, URI uri, int connectionTimeout, int soTimeout, int infiniteSoTimeout, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 358 */     this(poolConfig, new JedisFactory(uri, connectionTimeout, soTimeout, infiniteSoTimeout, null, sslSocketFactory, sslParameters, hostnameVerifier));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/* 364 */     this(poolConfig, new JedisFactory(hostAndPort, clientConfig));
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, JedisSocketFactory jedisSocketFactory, JedisClientConfig clientConfig) {
/* 369 */     this(poolConfig, new JedisFactory(jedisSocketFactory, clientConfig));
/*     */   }
/*     */   
/*     */   public JedisPool(GenericObjectPoolConfig<Jedis> poolConfig, PooledObjectFactory<Jedis> factory) {
/* 373 */     super(poolConfig, factory);
/*     */   }
/*     */ 
/*     */   
/*     */   public Jedis getResource() {
/* 378 */     Jedis jedis = (Jedis)super.getResource();
/* 379 */     jedis.setDataSource(this);
/* 380 */     return jedis;
/*     */   }
/*     */ 
/*     */   
/*     */   public void returnResource(Jedis resource) {
/* 385 */     if (resource != null)
/*     */       try {
/* 387 */         resource.resetState();
/* 388 */         super.returnResource(resource);
/* 389 */       } catch (RuntimeException e) {
/* 390 */         returnBrokenResource(resource);
/* 391 */         log.warn("Resource is returned to the pool as broken", e);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */