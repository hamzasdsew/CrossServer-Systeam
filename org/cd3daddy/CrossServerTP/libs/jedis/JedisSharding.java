/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ShardedConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Hashing;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class JedisSharding
/*    */   extends UnifiedJedis
/*    */ {
/* 15 */   public static final Pattern DEFAULT_KEY_TAG_PATTERN = Pattern.compile("\\{(.+?)\\}");
/*    */   
/*    */   public JedisSharding(List<HostAndPort> shards) {
/* 18 */     this(new ShardedConnectionProvider(shards));
/*    */   }
/*    */   
/*    */   public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig) {
/* 22 */     this(new ShardedConnectionProvider(shards, clientConfig));
/* 23 */     setProtocol(clientConfig);
/*    */   }
/*    */ 
/*    */   
/*    */   public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig) {
/* 28 */     this(new ShardedConnectionProvider(shards, clientConfig, poolConfig));
/* 29 */     setProtocol(clientConfig);
/*    */   }
/*    */   
/*    */   public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig, Hashing algo) {
/* 33 */     this(new ShardedConnectionProvider(shards, clientConfig, algo));
/* 34 */     setProtocol(clientConfig);
/*    */   }
/*    */ 
/*    */   
/*    */   public JedisSharding(List<HostAndPort> shards, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Hashing algo) {
/* 39 */     this(new ShardedConnectionProvider(shards, clientConfig, poolConfig, algo));
/* 40 */     setProtocol(clientConfig);
/*    */   }
/*    */   
/*    */   public JedisSharding(ShardedConnectionProvider provider) {
/* 44 */     super(provider);
/*    */   }
/*    */   
/*    */   public JedisSharding(ShardedConnectionProvider provider, Pattern tagPattern) {
/* 48 */     super(provider, tagPattern);
/*    */   }
/*    */   
/*    */   private void setProtocol(JedisClientConfig clientConfig) {
/* 52 */     RedisProtocol proto = clientConfig.getRedisProtocol();
/* 53 */     if (proto == RedisProtocol.RESP3) this.commandObjects.setProtocol(proto);
/*    */   
/*    */   }
/*    */   
/*    */   public ShardedPipeline pipelined() {
/* 58 */     return new ShardedPipeline((ShardedConnectionProvider)this.provider);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Transaction multi() {
/* 67 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisSharding.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */