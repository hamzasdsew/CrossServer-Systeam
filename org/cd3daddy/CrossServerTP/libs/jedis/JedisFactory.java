/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.net.URI;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.apache.commons.pool2.PooledObject;
/*     */ import org.apache.commons.pool2.PooledObjectFactory;
/*     */ import org.apache.commons.pool2.impl.DefaultPooledObject;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.InvalidURIException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisURIHelper;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JedisFactory
/*     */   implements PooledObjectFactory<Jedis>
/*     */ {
/*  25 */   private static final Logger logger = LoggerFactory.getLogger(JedisFactory.class);
/*     */   
/*     */   private final JedisSocketFactory jedisSocketFactory;
/*     */   
/*     */   private final JedisClientConfig clientConfig;
/*     */ 
/*     */   
/*     */   protected JedisFactory(String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName) {
/*  33 */     this(host, port, connectionTimeout, soTimeout, password, database, clientName, false, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JedisFactory(String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName) {
/*  38 */     this(host, port, connectionTimeout, soTimeout, 0, user, password, database, clientName);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JedisFactory(String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName) {
/*  43 */     this(host, port, connectionTimeout, soTimeout, infiniteSoTimeout, user, password, database, clientName, false, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JedisFactory(int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName) {
/*  51 */     this(connectionTimeout, soTimeout, infiniteSoTimeout, user, password, database, clientName, false, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JedisFactory(String host, int port, int connectionTimeout, int soTimeout, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  58 */     this(host, port, connectionTimeout, soTimeout, null, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JedisFactory(String host, int port, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  65 */     this(host, port, connectionTimeout, soTimeout, 0, user, password, database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */   
/*     */   protected JedisFactory(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/*  69 */     this.clientConfig = DefaultJedisClientConfig.copyConfig(clientConfig);
/*  70 */     this.jedisSocketFactory = new DefaultJedisSocketFactory(hostAndPort, this.clientConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JedisFactory(String host, int port, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  77 */     this
/*     */ 
/*     */ 
/*     */       
/*  81 */       .clientConfig = DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout).blockingSocketTimeoutMillis(infiniteSoTimeout).user(user).password(password).database(database).clientName(clientName).ssl(ssl).sslSocketFactory(sslSocketFactory).sslParameters(sslParameters).hostnameVerifier(hostnameVerifier).build();
/*  82 */     this.jedisSocketFactory = new DefaultJedisSocketFactory(new HostAndPort(host, port), this.clientConfig);
/*     */   }
/*     */   
/*     */   protected JedisFactory(JedisSocketFactory jedisSocketFactory, JedisClientConfig clientConfig) {
/*  86 */     this.clientConfig = DefaultJedisClientConfig.copyConfig(clientConfig);
/*  87 */     this.jedisSocketFactory = jedisSocketFactory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JedisFactory(int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/*  96 */     this(DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/*  97 */         .socketTimeoutMillis(soTimeout).blockingSocketTimeoutMillis(infiniteSoTimeout).user(user)
/*  98 */         .password(password).database(database).clientName(clientName)
/*  99 */         .ssl(ssl).sslSocketFactory(sslSocketFactory)
/* 100 */         .sslParameters(sslParameters).hostnameVerifier(hostnameVerifier).build());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   JedisFactory(JedisClientConfig clientConfig) {
/* 107 */     this.clientConfig = clientConfig;
/* 108 */     this.jedisSocketFactory = new DefaultJedisSocketFactory(clientConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   protected JedisFactory(URI uri, int connectionTimeout, int soTimeout, String clientName) {
/* 113 */     this(uri, connectionTimeout, soTimeout, clientName, (SSLSocketFactory)null, (SSLParameters)null, (HostnameVerifier)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JedisFactory(URI uri, int connectionTimeout, int soTimeout, String clientName, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 119 */     this(uri, connectionTimeout, soTimeout, 0, clientName, sslSocketFactory, sslParameters, hostnameVerifier);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected JedisFactory(URI uri, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String clientName, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier) {
/* 125 */     if (!JedisURIHelper.isValid(uri)) {
/* 126 */       throw new InvalidURIException(String.format("Cannot open Redis connection due invalid URI. %s", new Object[] { uri
/* 127 */               .toString() }));
/*     */     }
/* 129 */     this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 135 */       .clientConfig = DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout).socketTimeoutMillis(soTimeout).blockingSocketTimeoutMillis(infiniteSoTimeout).user(JedisURIHelper.getUser(uri)).password(JedisURIHelper.getPassword(uri)).database(JedisURIHelper.getDBIndex(uri)).clientName(clientName).protocol(JedisURIHelper.getRedisProtocol(uri)).ssl(JedisURIHelper.isRedisSSLScheme(uri)).sslSocketFactory(sslSocketFactory).sslParameters(sslParameters).hostnameVerifier(hostnameVerifier).build();
/* 136 */     this.jedisSocketFactory = new DefaultJedisSocketFactory(new HostAndPort(uri.getHost(), uri.getPort()), this.clientConfig);
/*     */   }
/*     */   
/*     */   void setHostAndPort(HostAndPort hostAndPort) {
/* 140 */     if (!(this.jedisSocketFactory instanceof DefaultJedisSocketFactory)) {
/* 141 */       throw new IllegalStateException("setHostAndPort method has limited capability.");
/*     */     }
/* 143 */     ((DefaultJedisSocketFactory)this.jedisSocketFactory).updateHostAndPort(hostAndPort);
/*     */   }
/*     */ 
/*     */   
/*     */   public void activateObject(PooledObject<Jedis> pooledJedis) throws Exception {
/* 148 */     Jedis jedis = (Jedis)pooledJedis.getObject();
/* 149 */     if (jedis.getDB() != this.clientConfig.getDatabase()) {
/* 150 */       jedis.select(this.clientConfig.getDatabase());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroyObject(PooledObject<Jedis> pooledJedis) throws Exception {
/* 156 */     Jedis jedis = (Jedis)pooledJedis.getObject();
/* 157 */     if (jedis.isConnected()) {
/*     */       try {
/* 159 */         jedis.close();
/* 160 */       } catch (RuntimeException e) {
/* 161 */         logger.debug("Error while close", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public PooledObject<Jedis> makeObject() throws Exception {
/* 168 */     Jedis jedis = null;
/*     */     try {
/* 170 */       jedis = new Jedis(this.jedisSocketFactory, this.clientConfig);
/* 171 */       return (PooledObject<Jedis>)new DefaultPooledObject(jedis);
/* 172 */     } catch (JedisException je) {
/* 173 */       logger.debug("Error while makeObject", (Throwable)je);
/* 174 */       throw je;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void passivateObject(PooledObject<Jedis> pooledJedis) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validateObject(PooledObject<Jedis> pooledJedis) {
/* 185 */     Jedis jedis = (Jedis)pooledJedis.getObject();
/*     */     try {
/* 187 */       boolean targetHasNotChanged = true;
/* 188 */       if (this.jedisSocketFactory instanceof DefaultJedisSocketFactory) {
/* 189 */         HostAndPort targetAddress = ((DefaultJedisSocketFactory)this.jedisSocketFactory).getHostAndPort();
/* 190 */         HostAndPort objectAddress = jedis.getConnection().getHostAndPort();
/*     */ 
/*     */         
/* 193 */         targetHasNotChanged = (targetAddress.getHost().equals(objectAddress.getHost()) && targetAddress.getPort() == objectAddress.getPort());
/*     */       } 
/*     */       
/* 196 */       return (targetHasNotChanged && jedis
/* 197 */         .getConnection().isConnected() && jedis
/* 198 */         .ping().equals("PONG"));
/* 199 */     } catch (Exception e) {
/* 200 */       logger.error("Error while validating pooled Jedis object.", e);
/* 201 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */