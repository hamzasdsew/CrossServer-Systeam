/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.providers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.ConnectionPool;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Jedis;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.JedisClientConfig;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.JedisPubSub;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ public class SentineledConnectionProvider
/*     */   implements ConnectionProvider
/*     */ {
/*  26 */   private static final Logger LOG = LoggerFactory.getLogger(SentineledConnectionProvider.class);
/*     */   
/*     */   protected static final long DEFAULT_SUBSCRIBE_RETRY_WAIT_TIME_MILLIS = 5000L;
/*     */   
/*     */   private volatile HostAndPort currentMaster;
/*     */   
/*     */   private volatile ConnectionPool pool;
/*     */   
/*     */   private final String masterName;
/*     */   
/*     */   private final JedisClientConfig masterClientConfig;
/*     */   
/*     */   private final GenericObjectPoolConfig<Connection> masterPoolConfig;
/*     */   
/*  40 */   protected final Collection<SentinelListener> sentinelListeners = new ArrayList<>();
/*     */   
/*     */   private final JedisClientConfig sentinelClientConfig;
/*     */   
/*     */   private final long subscribeRetryWaitTimeMillis;
/*     */   
/*  46 */   private final Object initPoolLock = new Object();
/*     */ 
/*     */   
/*     */   public SentineledConnectionProvider(String masterName, JedisClientConfig masterClientConfig, Set<HostAndPort> sentinels, JedisClientConfig sentinelClientConfig) {
/*  50 */     this(masterName, masterClientConfig, null, sentinels, sentinelClientConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SentineledConnectionProvider(String masterName, JedisClientConfig masterClientConfig, GenericObjectPoolConfig<Connection> poolConfig, Set<HostAndPort> sentinels, JedisClientConfig sentinelClientConfig) {
/*  56 */     this(masterName, masterClientConfig, poolConfig, sentinels, sentinelClientConfig, 5000L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SentineledConnectionProvider(String masterName, JedisClientConfig masterClientConfig, GenericObjectPoolConfig<Connection> poolConfig, Set<HostAndPort> sentinels, JedisClientConfig sentinelClientConfig, long subscribeRetryWaitTimeMillis) {
/*  65 */     this.masterName = masterName;
/*  66 */     this.masterClientConfig = masterClientConfig;
/*  67 */     this.masterPoolConfig = poolConfig;
/*     */     
/*  69 */     this.sentinelClientConfig = sentinelClientConfig;
/*  70 */     this.subscribeRetryWaitTimeMillis = subscribeRetryWaitTimeMillis;
/*     */     
/*  72 */     HostAndPort master = initSentinels(sentinels);
/*  73 */     initMaster(master);
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() {
/*  78 */     return this.pool.getResource();
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection(CommandArguments args) {
/*  83 */     return this.pool.getResource();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  88 */     this.sentinelListeners.forEach(SentinelListener::shutdown);
/*     */     
/*  90 */     this.pool.close();
/*     */   }
/*     */   
/*     */   public HostAndPort getCurrentMaster() {
/*  94 */     return this.currentMaster;
/*     */   }
/*     */   
/*     */   private void initMaster(HostAndPort master) {
/*  98 */     synchronized (this.initPoolLock) {
/*  99 */       if (!master.equals(this.currentMaster)) {
/* 100 */         this.currentMaster = master;
/*     */         
/* 102 */         ConnectionPool newPool = (this.masterPoolConfig != null) ? new ConnectionPool(this.currentMaster, this.masterClientConfig, this.masterPoolConfig) : new ConnectionPool(this.currentMaster, this.masterClientConfig);
/*     */ 
/*     */ 
/*     */         
/* 106 */         ConnectionPool existingPool = this.pool;
/* 107 */         this.pool = newPool;
/* 108 */         LOG.info("Created connection pool to master at {}.", master);
/*     */         
/* 110 */         if (existingPool != null)
/*     */         {
/*     */ 
/*     */           
/* 114 */           existingPool.close();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private HostAndPort initSentinels(Set<HostAndPort> sentinels) {
/* 122 */     HostAndPort master = null;
/* 123 */     boolean sentinelAvailable = false;
/*     */     
/* 125 */     LOG.debug("Trying to find master from available sentinels...");
/*     */     
/* 127 */     for (HostAndPort sentinel : sentinels) {
/*     */       
/* 129 */       LOG.debug("Connecting to Sentinel {}...", sentinel);
/*     */       
/* 131 */       try { try (Jedis jedis = new Jedis(sentinel, this.sentinelClientConfig))
/*     */         
/* 133 */         { List<String> masterAddr = jedis.sentinelGetMasterAddrByName(this.masterName);
/*     */ 
/*     */           
/* 136 */           sentinelAvailable = true;
/*     */           
/* 138 */           if (masterAddr == null || masterAddr.size() != 2)
/* 139 */           { LOG.warn("Sentinel {} is not monitoring master {}.", sentinel, this.masterName);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 146 */             if (jedis != null) { if (null != null) { try { jedis.close(); } catch (Throwable throwable) { null.addSuppressed(throwable); }  continue; }  jedis.close(); }  continue; }  master = toHostAndPort(masterAddr); LOG.debug("Redis master reported at {}.", master); }  break; } catch (JedisException e)
/*     */       
/*     */       { 
/* 149 */         LOG.warn("Could not get master address from {}.", sentinel, e); }
/*     */     
/*     */     } 
/*     */     
/* 153 */     if (master == null) {
/* 154 */       if (sentinelAvailable)
/*     */       {
/* 156 */         throw new JedisException("Can connect to sentinel, but " + this.masterName + " seems to be not monitored.");
/*     */       }
/*     */       
/* 159 */       throw new JedisConnectionException("All sentinels down, cannot determine where " + this.masterName + " is running.");
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 164 */     LOG.info("Redis master running at {}. Starting sentinel listeners...", master);
/*     */     
/* 166 */     for (HostAndPort sentinel : sentinels) {
/*     */       
/* 168 */       SentinelListener listener = new SentinelListener(sentinel);
/*     */       
/* 170 */       listener.setDaemon(true);
/* 171 */       this.sentinelListeners.add(listener);
/* 172 */       listener.start();
/*     */     } 
/*     */     
/* 175 */     return master;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HostAndPort toHostAndPort(List<String> masterAddr) {
/* 182 */     return toHostAndPort(masterAddr.get(0), masterAddr.get(1));
/*     */   }
/*     */   
/*     */   private static HostAndPort toHostAndPort(String hostStr, String portStr) {
/* 186 */     return new HostAndPort(hostStr, Integer.parseInt(portStr));
/*     */   }
/*     */   
/*     */   protected class SentinelListener
/*     */     extends Thread {
/*     */     protected final HostAndPort node;
/*     */     protected volatile Jedis sentinelJedis;
/* 193 */     protected AtomicBoolean running = new AtomicBoolean(false);
/*     */     
/*     */     public SentinelListener(HostAndPort node) {
/* 196 */       super(String.format("%s-SentinelListener-[%s]", new Object[] { SentineledConnectionProvider.access$000(this$0), node.toString() }));
/* 197 */       this.node = node;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/* 203 */       this.running.set(true);
/*     */       
/* 205 */       while (this.running.get()) {
/*     */ 
/*     */ 
/*     */         
/* 209 */         try { if (!this.running.get())
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
/* 261 */             IOUtils.closeQuietly((AutoCloseable)this.sentinelJedis); break; }  this.sentinelJedis = new Jedis(this.node, SentineledConnectionProvider.this.sentinelClientConfig); List<String> masterAddr = this.sentinelJedis.sentinelGetMasterAddrByName(SentineledConnectionProvider.this.masterName); if (masterAddr == null || masterAddr.size() != 2) { SentineledConnectionProvider.LOG.warn("Can not get master {} address. Sentinel: {}.", SentineledConnectionProvider.this.masterName, this.node); } else { SentineledConnectionProvider.this.initMaster(SentineledConnectionProvider.toHostAndPort(masterAddr)); }  this.sentinelJedis.subscribe(new JedisPubSub() { public void onMessage(String channel, String message) { SentineledConnectionProvider.LOG.debug("Sentinel {} published: {}.", SentineledConnectionProvider.SentinelListener.this.node, message); String[] switchMasterMsg = message.split(" "); if (switchMasterMsg.length > 3) { if (SentineledConnectionProvider.this.masterName.equals(switchMasterMsg[0])) { SentineledConnectionProvider.this.initMaster(SentineledConnectionProvider.toHostAndPort(switchMasterMsg[3], switchMasterMsg[4])); } else { SentineledConnectionProvider.LOG.debug("Ignoring message on +switch-master for master {}. Our master is {}.", switchMasterMsg[0], SentineledConnectionProvider.this.masterName); }  } else { SentineledConnectionProvider.LOG.error("Invalid message received on sentinel {} on channel +switch-master: {}.", SentineledConnectionProvider.SentinelListener.this.node, message); }  } }new String[] { "+switch-master" }); } catch (JedisException e) { if (this.running.get()) { SentineledConnectionProvider.LOG.error("Lost connection to sentinel {}. Sleeping {}ms and retrying.", new Object[] { this.node, Long.valueOf(SentineledConnectionProvider.access$600(this.this$0)), e }); try { Thread.sleep(SentineledConnectionProvider.this.subscribeRetryWaitTimeMillis); } catch (InterruptedException se) { SentineledConnectionProvider.LOG.error("Sleep interrupted.", se); }  } else { SentineledConnectionProvider.LOG.debug("Unsubscribing from sentinel {}.", this.node); }  } finally { IOUtils.closeQuietly((AutoCloseable)this.sentinelJedis); }
/*     */       
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void shutdown() {
/*     */       try {
/* 269 */         SentineledConnectionProvider.LOG.debug("Shutting down listener on {}.", this.node);
/* 270 */         this.running.set(false);
/*     */         
/* 272 */         if (this.sentinelJedis != null) {
/* 273 */           this.sentinelJedis.close();
/*     */         }
/* 275 */       } catch (RuntimeException e) {
/* 276 */         SentineledConnectionProvider.LOG.error("Error while shutting down.", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\providers\SentineledConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */