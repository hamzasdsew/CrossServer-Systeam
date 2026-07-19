/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ public final class DefaultRedisCredentialsProvider
/*    */   implements RedisCredentialsProvider {
/*    */   private volatile RedisCredentials credentials;
/*    */   
/*    */   public DefaultRedisCredentialsProvider(RedisCredentials credentials) {
/*  8 */     this.credentials = credentials;
/*    */   }
/*    */   
/*    */   public void setCredentials(RedisCredentials credentials) {
/* 12 */     this.credentials = credentials;
/*    */   }
/*    */ 
/*    */   
/*    */   public RedisCredentials get() {
/* 17 */     return this.credentials;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\DefaultRedisCredentialsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */