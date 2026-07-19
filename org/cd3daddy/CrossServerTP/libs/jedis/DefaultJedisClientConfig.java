/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.function.Supplier;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DefaultJedisClientConfig
/*     */   implements JedisClientConfig
/*     */ {
/*     */   private final RedisProtocol redisProtocol;
/*     */   private final int connectionTimeoutMillis;
/*     */   private final int socketTimeoutMillis;
/*     */   private final int blockingSocketTimeoutMillis;
/*     */   private volatile Supplier<RedisCredentials> credentialsProvider;
/*     */   private final int database;
/*     */   private final String clientName;
/*     */   private final boolean ssl;
/*     */   private final SSLSocketFactory sslSocketFactory;
/*     */   private final SSLParameters sslParameters;
/*     */   private final HostnameVerifier hostnameVerifier;
/*     */   private final HostAndPortMapper hostAndPortMapper;
/*     */   private final ClientSetInfoConfig clientSetInfoConfig;
/*     */   
/*     */   private DefaultJedisClientConfig(RedisProtocol protocol, int connectionTimeoutMillis, int soTimeoutMillis, int blockingSocketTimeoutMillis, Supplier<RedisCredentials> credentialsProvider, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier, HostAndPortMapper hostAndPortMapper, ClientSetInfoConfig clientSetInfoConfig) {
/*  34 */     this.redisProtocol = protocol;
/*  35 */     this.connectionTimeoutMillis = connectionTimeoutMillis;
/*  36 */     this.socketTimeoutMillis = soTimeoutMillis;
/*  37 */     this.blockingSocketTimeoutMillis = blockingSocketTimeoutMillis;
/*  38 */     this.credentialsProvider = credentialsProvider;
/*  39 */     this.database = database;
/*  40 */     this.clientName = clientName;
/*  41 */     this.ssl = ssl;
/*  42 */     this.sslSocketFactory = sslSocketFactory;
/*  43 */     this.sslParameters = sslParameters;
/*  44 */     this.hostnameVerifier = hostnameVerifier;
/*  45 */     this.hostAndPortMapper = hostAndPortMapper;
/*  46 */     this.clientSetInfoConfig = clientSetInfoConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public RedisProtocol getRedisProtocol() {
/*  51 */     return this.redisProtocol;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConnectionTimeoutMillis() {
/*  56 */     return this.connectionTimeoutMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSocketTimeoutMillis() {
/*  61 */     return this.socketTimeoutMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBlockingSocketTimeoutMillis() {
/*  66 */     return this.blockingSocketTimeoutMillis;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUser() {
/*  71 */     return ((RedisCredentials)this.credentialsProvider.get()).getUser();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/*  76 */     char[] password = ((RedisCredentials)this.credentialsProvider.get()).getPassword();
/*  77 */     return (password == null) ? null : new String(password);
/*     */   }
/*     */ 
/*     */   
/*     */   public Supplier<RedisCredentials> getCredentialsProvider() {
/*  82 */     return this.credentialsProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDatabase() {
/*  87 */     return this.database;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getClientName() {
/*  92 */     return this.clientName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSsl() {
/*  97 */     return this.ssl;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLSocketFactory getSslSocketFactory() {
/* 102 */     return this.sslSocketFactory;
/*     */   }
/*     */ 
/*     */   
/*     */   public SSLParameters getSslParameters() {
/* 107 */     return this.sslParameters;
/*     */   }
/*     */ 
/*     */   
/*     */   public HostnameVerifier getHostnameVerifier() {
/* 112 */     return this.hostnameVerifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public HostAndPortMapper getHostAndPortMapper() {
/* 117 */     return this.hostAndPortMapper;
/*     */   }
/*     */ 
/*     */   
/*     */   public ClientSetInfoConfig getClientSetInfoConfig() {
/* 122 */     return this.clientSetInfoConfig;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 126 */     return new Builder();
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/* 131 */     private RedisProtocol redisProtocol = null;
/*     */     
/* 133 */     private int connectionTimeoutMillis = 2000;
/* 134 */     private int socketTimeoutMillis = 2000;
/* 135 */     private int blockingSocketTimeoutMillis = 0;
/*     */     
/* 137 */     private String user = null;
/* 138 */     private String password = null;
/*     */     private Supplier<RedisCredentials> credentialsProvider;
/* 140 */     private int database = 0;
/* 141 */     private String clientName = null;
/*     */     
/*     */     private boolean ssl = false;
/* 144 */     private SSLSocketFactory sslSocketFactory = null;
/* 145 */     private SSLParameters sslParameters = null;
/* 146 */     private HostnameVerifier hostnameVerifier = null;
/*     */     
/* 148 */     private HostAndPortMapper hostAndPortMapper = null;
/*     */     
/* 150 */     private ClientSetInfoConfig clientSetInfoConfig = ClientSetInfoConfig.DEFAULT;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public DefaultJedisClientConfig build() {
/* 156 */       if (this.credentialsProvider == null) {
/* 157 */         this.credentialsProvider = new DefaultRedisCredentialsProvider(new DefaultRedisCredentials(this.user, this.password));
/*     */       }
/*     */ 
/*     */       
/* 161 */       return new DefaultJedisClientConfig(this.redisProtocol, this.connectionTimeoutMillis, this.socketTimeoutMillis, this.blockingSocketTimeoutMillis, this.credentialsProvider, this.database, this.clientName, this.ssl, this.sslSocketFactory, this.sslParameters, this.hostnameVerifier, this.hostAndPortMapper, this.clientSetInfoConfig);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder resp3() {
/* 170 */       return protocol(RedisProtocol.RESP3);
/*     */     }
/*     */     
/*     */     public Builder protocol(RedisProtocol protocol) {
/* 174 */       this.redisProtocol = protocol;
/* 175 */       return this;
/*     */     }
/*     */     
/*     */     public Builder timeoutMillis(int timeoutMillis) {
/* 179 */       this.connectionTimeoutMillis = timeoutMillis;
/* 180 */       this.socketTimeoutMillis = timeoutMillis;
/* 181 */       return this;
/*     */     }
/*     */     
/*     */     public Builder connectionTimeoutMillis(int connectionTimeoutMillis) {
/* 185 */       this.connectionTimeoutMillis = connectionTimeoutMillis;
/* 186 */       return this;
/*     */     }
/*     */     
/*     */     public Builder socketTimeoutMillis(int socketTimeoutMillis) {
/* 190 */       this.socketTimeoutMillis = socketTimeoutMillis;
/* 191 */       return this;
/*     */     }
/*     */     
/*     */     public Builder blockingSocketTimeoutMillis(int blockingSocketTimeoutMillis) {
/* 195 */       this.blockingSocketTimeoutMillis = blockingSocketTimeoutMillis;
/* 196 */       return this;
/*     */     }
/*     */     
/*     */     public Builder user(String user) {
/* 200 */       this.user = user;
/* 201 */       return this;
/*     */     }
/*     */     
/*     */     public Builder password(String password) {
/* 205 */       this.password = password;
/* 206 */       return this;
/*     */     }
/*     */     
/*     */     public Builder credentials(RedisCredentials credentials) {
/* 210 */       this.credentialsProvider = new DefaultRedisCredentialsProvider(credentials);
/* 211 */       return this;
/*     */     }
/*     */     
/*     */     public Builder credentialsProvider(Supplier<RedisCredentials> credentials) {
/* 215 */       this.credentialsProvider = credentials;
/* 216 */       return this;
/*     */     }
/*     */     
/*     */     public Builder database(int database) {
/* 220 */       this.database = database;
/* 221 */       return this;
/*     */     }
/*     */     
/*     */     public Builder clientName(String clientName) {
/* 225 */       this.clientName = clientName;
/* 226 */       return this;
/*     */     }
/*     */     
/*     */     public Builder ssl(boolean ssl) {
/* 230 */       this.ssl = ssl;
/* 231 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
/* 235 */       this.sslSocketFactory = sslSocketFactory;
/* 236 */       return this;
/*     */     }
/*     */     
/*     */     public Builder sslParameters(SSLParameters sslParameters) {
/* 240 */       this.sslParameters = sslParameters;
/* 241 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
/* 245 */       this.hostnameVerifier = hostnameVerifier;
/* 246 */       return this;
/*     */     }
/*     */     
/*     */     public Builder hostAndPortMapper(HostAndPortMapper hostAndPortMapper) {
/* 250 */       this.hostAndPortMapper = hostAndPortMapper;
/* 251 */       return this;
/*     */     }
/*     */     
/*     */     public Builder clientSetInfoConfig(ClientSetInfoConfig setInfoConfig) {
/* 255 */       this.clientSetInfoConfig = setInfoConfig;
/* 256 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private Builder() {}
/*     */   }
/*     */   
/*     */   public static DefaultJedisClientConfig create(int connectionTimeoutMillis, int soTimeoutMillis, int blockingSocketTimeoutMillis, String user, String password, int database, String clientName, boolean ssl, SSLSocketFactory sslSocketFactory, SSLParameters sslParameters, HostnameVerifier hostnameVerifier, HostAndPortMapper hostAndPortMapper) {
/* 264 */     return new DefaultJedisClientConfig(null, connectionTimeoutMillis, soTimeoutMillis, blockingSocketTimeoutMillis, new DefaultRedisCredentialsProvider(new DefaultRedisCredentials(user, password)), database, clientName, ssl, sslSocketFactory, sslParameters, hostnameVerifier, hostAndPortMapper, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DefaultJedisClientConfig copyConfig(JedisClientConfig copy) {
/* 271 */     return new DefaultJedisClientConfig(copy.getRedisProtocol(), copy
/* 272 */         .getConnectionTimeoutMillis(), copy.getSocketTimeoutMillis(), copy
/* 273 */         .getBlockingSocketTimeoutMillis(), copy.getCredentialsProvider(), copy
/* 274 */         .getDatabase(), copy.getClientName(), copy.isSsl(), copy.getSslSocketFactory(), copy
/* 275 */         .getSslParameters(), copy.getHostnameVerifier(), copy.getHostAndPortMapper(), copy
/* 276 */         .getClientSetInfoConfig());
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\DefaultJedisClientConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */