/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import javax.net.ssl.HostnameVerifier;
/*    */ import javax.net.ssl.SSLParameters;
/*    */ import javax.net.ssl.SSLSocketFactory;
/*    */ 
/*    */ public interface JedisClientConfig
/*    */ {
/*    */   default RedisProtocol getRedisProtocol() {
/* 11 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int getConnectionTimeoutMillis() {
/* 18 */     return 2000;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int getSocketTimeoutMillis() {
/* 25 */     return 2000;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default int getBlockingSocketTimeoutMillis() {
/* 33 */     return 0;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default String getUser() {
/* 40 */     return null;
/*    */   }
/*    */   
/*    */   default String getPassword() {
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   default Supplier<RedisCredentials> getCredentialsProvider() {
/* 48 */     return new DefaultRedisCredentialsProvider(new DefaultRedisCredentials(
/* 49 */           getUser(), getPassword()));
/*    */   }
/*    */   
/*    */   default int getDatabase() {
/* 53 */     return 0;
/*    */   }
/*    */   
/*    */   default String getClientName() {
/* 57 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean isSsl() {
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   default SSLSocketFactory getSslSocketFactory() {
/* 68 */     return null;
/*    */   }
/*    */   
/*    */   default SSLParameters getSslParameters() {
/* 72 */     return null;
/*    */   }
/*    */   
/*    */   default HostnameVerifier getHostnameVerifier() {
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   default HostAndPortMapper getHostAndPortMapper() {
/* 80 */     return null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default ClientSetInfoConfig getClientSetInfoConfig() {
/* 88 */     return ClientSetInfoConfig.DEFAULT;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisClientConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */