/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.stream.Collectors;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Pool;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public class JedisSentinelPool
/*     */   extends Pool<Jedis>
/*     */ {
/*  21 */   private static final Logger LOG = LoggerFactory.getLogger(JedisSentinelPool.class);
/*     */   
/*     */   private final JedisFactory factory;
/*     */   
/*     */   private final JedisClientConfig sentinelClientConfig;
/*     */   
/*  27 */   protected final Collection<MasterListener> masterListeners = new ArrayList<>();
/*     */   
/*     */   private volatile HostAndPort currentHostMaster;
/*     */   
/*  31 */   private final Object initPoolLock = new Object();
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<HostAndPort> sentinels, JedisClientConfig masterClientConfig, JedisClientConfig sentinelClientConfig) {
/*  35 */     this(masterName, sentinels, new JedisFactory(masterClientConfig), sentinelClientConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig) {
/*  40 */     this(masterName, sentinels, poolConfig, 2000, null, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels) {
/*  45 */     this(masterName, sentinels, new GenericObjectPoolConfig(), 2000, null, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, String password) {
/*  50 */     this(masterName, sentinels, new GenericObjectPoolConfig(), 2000, password);
/*     */   }
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, String password, String sentinelPassword) {
/*  54 */     this(masterName, sentinels, new GenericObjectPoolConfig(), 2000, 2000, password, 0, null, 2000, 2000, sentinelPassword, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int timeout, String password) {
/*  60 */     this(masterName, sentinels, poolConfig, timeout, password, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int timeout) {
/*  65 */     this(masterName, sentinels, poolConfig, timeout, null, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, String password) {
/*  70 */     this(masterName, sentinels, poolConfig, 2000, password);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int timeout, String password, int database) {
/*  76 */     this(masterName, sentinels, poolConfig, timeout, timeout, (String)null, password, database);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int timeout, String user, String password, int database) {
/*  82 */     this(masterName, sentinels, poolConfig, timeout, timeout, user, password, database);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int timeout, String password, int database, String clientName) {
/*  88 */     this(masterName, sentinels, poolConfig, timeout, timeout, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int timeout, String user, String password, int database, String clientName) {
/*  94 */     this(masterName, sentinels, poolConfig, timeout, timeout, user, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String password, int database) {
/* 100 */     this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, null, password, database, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String user, String password, int database) {
/* 106 */     this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, user, password, database, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String password, int database, String clientName) {
/* 112 */     this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, null, password, database, clientName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName) {
/* 118 */     this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, user, password, database, clientName, 2000, 2000, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName) {
/* 125 */     this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, infiniteSoTimeout, user, password, database, clientName, 2000, 2000, null, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String password, int database, String clientName, int sentinelConnectionTimeout, int sentinelSoTimeout, String sentinelPassword, String sentinelClientName) {
/* 134 */     this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, null, password, database, clientName, sentinelConnectionTimeout, sentinelSoTimeout, null, sentinelPassword, sentinelClientName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, String user, String password, int database, String clientName, int sentinelConnectionTimeout, int sentinelSoTimeout, String sentinelUser, String sentinelPassword, String sentinelClientName) {
/* 143 */     this(masterName, sentinels, poolConfig, connectionTimeout, soTimeout, 0, user, password, database, clientName, sentinelConnectionTimeout, sentinelSoTimeout, sentinelUser, sentinelPassword, sentinelClientName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, int connectionTimeout, int soTimeout, int infiniteSoTimeout, String user, String password, int database, String clientName, int sentinelConnectionTimeout, int sentinelSoTimeout, String sentinelUser, String sentinelPassword, String sentinelClientName) {
/* 153 */     this(masterName, parseHostAndPorts(sentinels), poolConfig, 
/* 154 */         DefaultJedisClientConfig.builder().connectionTimeoutMillis(connectionTimeout)
/* 155 */         .socketTimeoutMillis(soTimeout).blockingSocketTimeoutMillis(infiniteSoTimeout)
/* 156 */         .user(user).password(password).database(database).clientName(clientName).build(), 
/* 157 */         DefaultJedisClientConfig.builder().connectionTimeoutMillis(sentinelConnectionTimeout)
/* 158 */         .socketTimeoutMillis(sentinelSoTimeout).user(sentinelUser).password(sentinelPassword)
/* 159 */         .clientName(sentinelClientName).build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<String> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, JedisFactory factory) {
/* 165 */     this(masterName, parseHostAndPorts(sentinels), poolConfig, factory, 
/* 166 */         DefaultJedisClientConfig.builder().build());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<HostAndPort> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, JedisClientConfig masterClientConfig, JedisClientConfig sentinelClientConfig) {
/* 172 */     this(masterName, sentinels, poolConfig, new JedisFactory(masterClientConfig), sentinelClientConfig);
/*     */   }
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<HostAndPort> sentinels, JedisFactory factory, JedisClientConfig sentinelClientConfig) {
/* 177 */     super(factory);
/*     */     
/* 179 */     this.factory = factory;
/* 180 */     this.sentinelClientConfig = sentinelClientConfig;
/*     */     
/* 182 */     HostAndPort master = initSentinels(sentinels, masterName);
/* 183 */     initMaster(master);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JedisSentinelPool(String masterName, Set<HostAndPort> sentinels, GenericObjectPoolConfig<Jedis> poolConfig, JedisFactory factory, JedisClientConfig sentinelClientConfig) {
/* 189 */     super(poolConfig, factory);
/*     */     
/* 191 */     this.factory = factory;
/* 192 */     this.sentinelClientConfig = sentinelClientConfig;
/*     */     
/* 194 */     HostAndPort master = initSentinels(sentinels, masterName);
/* 195 */     initMaster(master);
/*     */   }
/*     */   
/*     */   private static Set<HostAndPort> parseHostAndPorts(Set<String> strings) {
/* 199 */     return (Set<HostAndPort>)strings.stream().map(HostAndPort::from).collect(Collectors.toSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public void destroy() {
/* 204 */     for (MasterListener m : this.masterListeners) {
/* 205 */       m.shutdown();
/*     */     }
/*     */     
/* 208 */     super.destroy();
/*     */   }
/*     */   
/*     */   public HostAndPort getCurrentHostMaster() {
/* 212 */     return this.currentHostMaster;
/*     */   }
/*     */   
/*     */   private void initMaster(HostAndPort master) {
/* 216 */     synchronized (this.initPoolLock) {
/* 217 */       if (!master.equals(this.currentHostMaster)) {
/* 218 */         this.currentHostMaster = master;
/* 219 */         this.factory.setHostAndPort(this.currentHostMaster);
/*     */ 
/*     */         
/* 222 */         clear();
/*     */         
/* 224 */         LOG.info("Created JedisSentinelPool to master at {}", master);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private HostAndPort initSentinels(Set<HostAndPort> sentinels, String masterName) {
/* 231 */     HostAndPort master = null;
/* 232 */     boolean sentinelAvailable = false;
/*     */     
/* 234 */     LOG.info("Trying to find master from available Sentinels...");
/*     */     
/* 236 */     for (HostAndPort sentinel : sentinels) {
/*     */       
/* 238 */       LOG.debug("Connecting to Sentinel {}", sentinel);
/*     */       
/* 240 */       try { try (Jedis jedis = new Jedis(sentinel, this.sentinelClientConfig))
/*     */         
/* 242 */         { List<String> masterAddr = jedis.sentinelGetMasterAddrByName(masterName);
/*     */ 
/*     */           
/* 245 */           sentinelAvailable = true;
/*     */           
/* 247 */           if (masterAddr == null || masterAddr.size() != 2)
/* 248 */           { LOG.warn("Can not get master addr, master name: {}. Sentinel: {}", masterName, sentinel);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 255 */             if (jedis != null) { if (null != null) { try { jedis.close(); } catch (Throwable throwable) { null.addSuppressed(throwable); }  continue; }  jedis.close(); }  continue; }  master = toHostAndPort(masterAddr); LOG.debug("Found Redis master at {}", master); }  break; } catch (JedisException e)
/*     */       
/*     */       { 
/* 258 */         LOG.warn("Cannot get master address from sentinel running @ {}. Reason: {}. Trying next one.", sentinel, e); }
/*     */     
/*     */     } 
/*     */ 
/*     */     
/* 263 */     if (master == null) {
/* 264 */       if (sentinelAvailable)
/*     */       {
/* 266 */         throw new JedisException("Can connect to sentinel, but " + masterName + " seems to be not monitored...");
/*     */       }
/*     */       
/* 269 */       throw new JedisConnectionException("All sentinels down, cannot determine where is " + masterName + " master is running...");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 274 */     LOG.info("Redis master running at {}, starting Sentinel listeners...", master);
/*     */     
/* 276 */     for (HostAndPort sentinel : sentinels) {
/*     */       
/* 278 */       MasterListener masterListener = new MasterListener(masterName, sentinel.getHost(), sentinel.getPort());
/*     */       
/* 280 */       masterListener.setDaemon(true);
/* 281 */       this.masterListeners.add(masterListener);
/* 282 */       masterListener.start();
/*     */     } 
/*     */     
/* 285 */     return master;
/*     */   }
/*     */   
/*     */   private HostAndPort toHostAndPort(List<String> getMasterAddrByNameResult) {
/* 289 */     String host = getMasterAddrByNameResult.get(0);
/* 290 */     int port = Integer.parseInt(getMasterAddrByNameResult.get(1));
/*     */     
/* 292 */     return new HostAndPort(host, port);
/*     */   }
/*     */ 
/*     */   
/*     */   public Jedis getResource() {
/*     */     while (true) {
/* 298 */       Jedis jedis = (Jedis)super.getResource();
/* 299 */       jedis.setDataSource(this);
/*     */ 
/*     */       
/* 302 */       HostAndPort master = this.currentHostMaster;
/* 303 */       HostAndPort connection = jedis.getClient().getHostAndPort();
/*     */       
/* 305 */       if (master.equals(connection))
/*     */       {
/* 307 */         return jedis;
/*     */       }
/* 309 */       returnBrokenResource(jedis);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void returnResource(Jedis resource) {
/* 316 */     if (resource != null)
/*     */       try {
/* 318 */         resource.resetState();
/* 319 */         super.returnResource(resource);
/* 320 */       } catch (RuntimeException e) {
/* 321 */         returnBrokenResource(resource);
/* 322 */         LOG.debug("Resource is returned to the pool as broken", e);
/*     */       }  
/*     */   }
/*     */   
/*     */   protected class MasterListener
/*     */     extends Thread
/*     */   {
/*     */     protected String masterName;
/*     */     protected String host;
/*     */     protected int port;
/* 332 */     protected long subscribeRetryWaitTimeMillis = 5000L;
/*     */     protected volatile Jedis j;
/* 334 */     protected AtomicBoolean running = new AtomicBoolean(false);
/*     */ 
/*     */     
/*     */     protected MasterListener() {}
/*     */     
/*     */     public MasterListener(String masterName, String host, int port) {
/* 340 */       super(String.format("MasterListener-%s-[%s:%d]", new Object[] { masterName, host, Integer.valueOf(port) }));
/* 341 */       this.masterName = masterName;
/* 342 */       this.host = host;
/* 343 */       this.port = port;
/*     */     }
/*     */ 
/*     */     
/*     */     public MasterListener(String masterName, String host, int port, long subscribeRetryWaitTimeMillis) {
/* 348 */       this(masterName, host, port);
/* 349 */       this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 355 */       this.running.set(true);
/*     */       
/* 357 */       while (this.running.get()) {
/*     */ 
/*     */ 
/*     */         
/* 361 */         try { if (!this.running.get())
/*     */           
/*     */           { 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 415 */             if (this.j != null)
/* 416 */               this.j.close();  break; }  final HostAndPort hostPort = new HostAndPort(this.host, this.port); this.j = new Jedis(hostPort, JedisSentinelPool.this.sentinelClientConfig); List<String> masterAddr = this.j.sentinelGetMasterAddrByName(this.masterName); if (masterAddr == null || masterAddr.size() != 2) { JedisSentinelPool.LOG.warn("Can not get master addr, master name: {}. Sentinel: {}.", this.masterName, hostPort); } else { JedisSentinelPool.this.initMaster(JedisSentinelPool.this.toHostAndPort(masterAddr)); }  this.j.subscribe(new JedisPubSub() { public void onMessage(String channel, String message) { JedisSentinelPool.LOG.debug("Sentinel {} published: {}.", hostPort, message); String[] switchMasterMsg = message.split(" "); if (switchMasterMsg.length > 3) { if (JedisSentinelPool.MasterListener.this.masterName.equals(switchMasterMsg[0])) { JedisSentinelPool.this.initMaster(JedisSentinelPool.this.toHostAndPort(Arrays.asList(new String[] { switchMasterMsg[3], switchMasterMsg[4] }))); } else { JedisSentinelPool.LOG.debug("Ignoring message on +switch-master for master name {}, our master name is {}", switchMasterMsg[0], JedisSentinelPool.MasterListener.this.masterName); }  } else { JedisSentinelPool.LOG.error("Invalid message received on Sentinel {} on channel +switch-master: {}", hostPort, message); }  } }new String[] { "+switch-master" }); } catch (JedisException e) { if (this.running.get()) { JedisSentinelPool.LOG.error("Lost connection to Sentinel at {}:{}. Sleeping 5000ms and retrying.", new Object[] { this.host, Integer.valueOf(this.port), e }); try { Thread.sleep(this.subscribeRetryWaitTimeMillis); } catch (InterruptedException e1) { JedisSentinelPool.LOG.error("Sleep interrupted: ", e1); }  } else { JedisSentinelPool.LOG.debug("Unsubscribing from Sentinel at {}:{}", this.host, Integer.valueOf(this.port)); }  } finally { if (this.j != null) this.j.close();
/*     */            }
/*     */       
/*     */       } 
/*     */     }
/*     */     
/*     */     public void shutdown() {
/*     */       try {
/* 424 */         JedisSentinelPool.LOG.debug("Shutting down listener on {}:{}", this.host, Integer.valueOf(this.port));
/* 425 */         this.running.set(false);
/*     */         
/* 427 */         if (this.j != null) {
/* 428 */           this.j.close();
/*     */         }
/* 430 */       } catch (RuntimeException e) {
/* 431 */         JedisSentinelPool.LOG.error("Caught exception while shutting down: ", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisSentinelPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */