/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ClusterConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*    */ 
/*    */ public class ClusterPipeline extends MultiNodePipelineBase {
/*    */   private final ClusterConnectionProvider provider;
/* 12 */   private AutoCloseable closeable = null;
/*    */   
/*    */   public ClusterPipeline(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig) {
/* 15 */     this(new ClusterConnectionProvider(clusterNodes, clientConfig), 
/* 16 */         createClusterCommandObjects(clientConfig.getRedisProtocol()));
/* 17 */     this.closeable = (AutoCloseable)this.provider;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClusterPipeline(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig) {
/* 22 */     this(new ClusterConnectionProvider(clusterNodes, clientConfig, poolConfig), 
/* 23 */         createClusterCommandObjects(clientConfig.getRedisProtocol()));
/* 24 */     this.closeable = (AutoCloseable)this.provider;
/*    */   }
/*    */ 
/*    */   
/*    */   public ClusterPipeline(Set<HostAndPort> clusterNodes, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Duration topologyRefreshPeriod) {
/* 29 */     this(new ClusterConnectionProvider(clusterNodes, clientConfig, poolConfig, topologyRefreshPeriod), 
/* 30 */         createClusterCommandObjects(clientConfig.getRedisProtocol()));
/* 31 */     this.closeable = (AutoCloseable)this.provider;
/*    */   }
/*    */   
/*    */   public ClusterPipeline(ClusterConnectionProvider provider) {
/* 35 */     this(provider, new ClusterCommandObjects());
/*    */   }
/*    */   
/*    */   public ClusterPipeline(ClusterConnectionProvider provider, ClusterCommandObjects commandObjects) {
/* 39 */     super(commandObjects);
/* 40 */     this.provider = provider;
/*    */   }
/*    */   
/*    */   private static ClusterCommandObjects createClusterCommandObjects(RedisProtocol protocol) {
/* 44 */     ClusterCommandObjects cco = new ClusterCommandObjects();
/* 45 */     if (protocol == RedisProtocol.RESP3) cco.setProtocol(protocol); 
/* 46 */     return cco;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 52 */       super.close();
/*    */     } finally {
/* 54 */       IOUtils.closeQuietly(this.closeable);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected HostAndPort getNodeKey(CommandArguments args) {
/* 60 */     return this.provider.getNode(((ClusterCommandArguments)args).getCommandHashSlot());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Connection getConnection(HostAndPort nodeKey) {
/* 65 */     return this.provider.getConnection(nodeKey);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void prepareGraphCommands() {
/* 72 */     prepareGraphCommands((ConnectionProvider)this.provider);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ClusterPipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */