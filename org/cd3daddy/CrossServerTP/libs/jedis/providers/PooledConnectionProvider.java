/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.providers;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.pool2.PooledObjectFactory;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.ConnectionFactory;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.ConnectionPool;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.JedisClientConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Pool;
/*    */ 
/*    */ public class PooledConnectionProvider
/*    */   implements ConnectionProvider
/*    */ {
/*    */   private final Pool<Connection> pool;
/* 19 */   private Object connectionMapKey = "";
/*    */   
/*    */   public PooledConnectionProvider(HostAndPort hostAndPort) {
/* 22 */     this((PooledObjectFactory<Connection>)new ConnectionFactory(hostAndPort));
/* 23 */     this.connectionMapKey = hostAndPort;
/*    */   }
/*    */   
/*    */   public PooledConnectionProvider(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/* 27 */     this((Pool<Connection>)new ConnectionPool(hostAndPort, clientConfig));
/* 28 */     this.connectionMapKey = hostAndPort;
/*    */   }
/*    */ 
/*    */   
/*    */   public PooledConnectionProvider(HostAndPort hostAndPort, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig) {
/* 33 */     this((PooledObjectFactory<Connection>)new ConnectionFactory(hostAndPort, clientConfig), poolConfig);
/* 34 */     this.connectionMapKey = hostAndPort;
/*    */   }
/*    */   
/*    */   public PooledConnectionProvider(PooledObjectFactory<Connection> factory) {
/* 38 */     this((Pool<Connection>)new ConnectionPool(factory));
/* 39 */     this.connectionMapKey = factory;
/*    */   }
/*    */ 
/*    */   
/*    */   public PooledConnectionProvider(PooledObjectFactory<Connection> factory, GenericObjectPoolConfig<Connection> poolConfig) {
/* 44 */     this((Pool<Connection>)new ConnectionPool(factory, poolConfig));
/* 45 */     this.connectionMapKey = factory;
/*    */   }
/*    */   
/*    */   private PooledConnectionProvider(Pool<Connection> pool) {
/* 49 */     this.pool = pool;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 54 */     this.pool.close();
/*    */   }
/*    */   
/*    */   public final Pool<Connection> getPool() {
/* 58 */     return this.pool;
/*    */   }
/*    */ 
/*    */   
/*    */   public Connection getConnection() {
/* 63 */     return (Connection)this.pool.getResource();
/*    */   }
/*    */ 
/*    */   
/*    */   public Connection getConnection(CommandArguments args) {
/* 68 */     return (Connection)this.pool.getResource();
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<?, Pool<Connection>> getConnectionMap() {
/* 73 */     return Collections.singletonMap(this.connectionMapKey, this.pool);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\providers\PooledConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */