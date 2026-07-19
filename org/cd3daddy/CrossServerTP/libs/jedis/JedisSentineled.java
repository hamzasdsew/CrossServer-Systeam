/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.SentineledConnectionProvider;
/*    */ 
/*    */ public class JedisSentineled
/*    */   extends UnifiedJedis {
/*    */   public JedisSentineled(String masterName, JedisClientConfig masterClientConfig, Set<HostAndPort> sentinels, JedisClientConfig sentinelClientConfig) {
/* 11 */     this(new SentineledConnectionProvider(masterName, masterClientConfig, sentinels, sentinelClientConfig));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public JedisSentineled(String masterName, JedisClientConfig masterClientConfig, GenericObjectPoolConfig<Connection> poolConfig, Set<HostAndPort> sentinels, JedisClientConfig sentinelClientConfig) {
/* 17 */     this(new SentineledConnectionProvider(masterName, masterClientConfig, poolConfig, sentinels, sentinelClientConfig));
/*    */   }
/*    */   
/*    */   public JedisSentineled(SentineledConnectionProvider sentineledConnectionProvider) {
/* 21 */     super((ConnectionProvider)sentineledConnectionProvider);
/*    */   }
/*    */   
/*    */   public HostAndPort getCurrentMaster() {
/* 25 */     return ((SentineledConnectionProvider)this.provider).getCurrentMaster();
/*    */   }
/*    */ 
/*    */   
/*    */   public Pipeline pipelined() {
/* 30 */     return (Pipeline)super.pipelined();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisSentineled.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */