/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ShardedConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Hashing;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class ShardedPipeline
/*    */   extends MultiNodePipelineBase
/*    */ {
/*    */   private final ShardedConnectionProvider provider;
/* 19 */   private AutoCloseable closeable = null;
/*    */   
/*    */   public ShardedPipeline(List<HostAndPort> shards, JedisClientConfig clientConfig) {
/* 22 */     this(new ShardedConnectionProvider(shards, clientConfig));
/* 23 */     this.closeable = (AutoCloseable)this.provider;
/*    */   }
/*    */   
/*    */   public ShardedPipeline(ShardedConnectionProvider provider) {
/* 27 */     super(new ShardedCommandObjects(provider.getHashingAlgo()));
/* 28 */     this.provider = provider;
/*    */   }
/*    */ 
/*    */   
/*    */   public ShardedPipeline(List<HostAndPort> shards, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig, Hashing algo, Pattern tagPattern) {
/* 33 */     this(new ShardedConnectionProvider(shards, clientConfig, poolConfig, algo), tagPattern);
/* 34 */     this.closeable = (AutoCloseable)this.provider;
/*    */   }
/*    */   
/*    */   public ShardedPipeline(ShardedConnectionProvider provider, Pattern tagPattern) {
/* 38 */     super(new ShardedCommandObjects(provider.getHashingAlgo(), tagPattern));
/* 39 */     this.provider = provider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/*    */     try {
/* 45 */       super.close();
/*    */     } finally {
/* 47 */       IOUtils.closeQuietly(this.closeable);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected HostAndPort getNodeKey(CommandArguments args) {
/* 53 */     return this.provider.getNode(((ShardedCommandArguments)args).getKeyHash());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Connection getConnection(HostAndPort nodeKey) {
/* 58 */     return this.provider.getConnection(nodeKey);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void prepareGraphCommands() {
/* 65 */     prepareGraphCommands((ConnectionProvider)this.provider);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ShardedPipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */