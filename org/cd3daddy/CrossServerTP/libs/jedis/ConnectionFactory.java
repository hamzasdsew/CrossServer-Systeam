/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import org.apache.commons.pool2.PooledObject;
/*    */ import org.apache.commons.pool2.PooledObjectFactory;
/*    */ import org.apache.commons.pool2.impl.DefaultPooledObject;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectionFactory
/*    */   implements PooledObjectFactory<Connection>
/*    */ {
/* 17 */   private static final Logger logger = LoggerFactory.getLogger(ConnectionFactory.class);
/*    */   
/*    */   private final JedisSocketFactory jedisSocketFactory;
/*    */   
/*    */   private final JedisClientConfig clientConfig;
/*    */   
/*    */   public ConnectionFactory(HostAndPort hostAndPort) {
/* 24 */     this.clientConfig = DefaultJedisClientConfig.builder().build();
/* 25 */     this.jedisSocketFactory = new DefaultJedisSocketFactory(hostAndPort);
/*    */   }
/*    */   
/*    */   public ConnectionFactory(HostAndPort hostAndPort, JedisClientConfig clientConfig) {
/* 29 */     this.clientConfig = DefaultJedisClientConfig.copyConfig(clientConfig);
/* 30 */     this.jedisSocketFactory = new DefaultJedisSocketFactory(hostAndPort, this.clientConfig);
/*    */   }
/*    */   
/*    */   public ConnectionFactory(JedisSocketFactory jedisSocketFactory, JedisClientConfig clientConfig) {
/* 34 */     this.clientConfig = DefaultJedisClientConfig.copyConfig(clientConfig);
/* 35 */     this.jedisSocketFactory = jedisSocketFactory;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void activateObject(PooledObject<Connection> pooledConnection) throws Exception {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void destroyObject(PooledObject<Connection> pooledConnection) throws Exception {
/* 45 */     Connection jedis = (Connection)pooledConnection.getObject();
/* 46 */     if (jedis.isConnected()) {
/*    */       try {
/* 48 */         jedis.close();
/* 49 */       } catch (RuntimeException e) {
/* 50 */         logger.debug("Error while close", e);
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public PooledObject<Connection> makeObject() throws Exception {
/* 57 */     Connection jedis = null;
/*    */     try {
/* 59 */       jedis = new Connection(this.jedisSocketFactory, this.clientConfig);
/* 60 */       return (PooledObject<Connection>)new DefaultPooledObject(jedis);
/* 61 */     } catch (JedisException je) {
/* 62 */       logger.debug("Error while makeObject", (Throwable)je);
/* 63 */       throw je;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void passivateObject(PooledObject<Connection> pooledConnection) throws Exception {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean validateObject(PooledObject<Connection> pooledConnection) {
/* 74 */     Connection jedis = (Connection)pooledConnection.getObject();
/*    */     
/*    */     try {
/* 77 */       return (jedis.isConnected() && jedis.ping());
/* 78 */     } catch (Exception e) {
/* 79 */       logger.error("Error while validating pooled Connection object.", e);
/* 80 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */