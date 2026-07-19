/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import org.apache.commons.pool2.PooledObjectFactory;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Pool;
/*    */ 
/*    */ public class ConnectionPool
/*    */   extends Pool<Connection> {
/*    */   public ConnectionPool(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/* 10 */     this(new ConnectionFactory(hostAndPort, clientConfig));
/*    */   }
/*    */   
/*    */   public ConnectionPool(PooledObjectFactory<Connection> factory) {
/* 14 */     super(factory);
/*    */   }
/*    */ 
/*    */   
/*    */   public ConnectionPool(HostAndPort hostAndPort, JedisClientConfig clientConfig, GenericObjectPoolConfig<Connection> poolConfig) {
/* 19 */     this(new ConnectionFactory(hostAndPort, clientConfig), poolConfig);
/*    */   }
/*    */ 
/*    */   
/*    */   public ConnectionPool(PooledObjectFactory<Connection> factory, GenericObjectPoolConfig<Connection> poolConfig) {
/* 24 */     super(factory, poolConfig);
/*    */   }
/*    */ 
/*    */   
/*    */   public Connection getResource() {
/* 29 */     Connection conn = (Connection)super.getResource();
/* 30 */     conn.setHandlingPool(this);
/* 31 */     return conn;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ConnectionPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */