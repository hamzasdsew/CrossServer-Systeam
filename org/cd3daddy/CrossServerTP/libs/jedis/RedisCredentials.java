/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface RedisCredentials
/*    */ {
/*    */   default String getUser() {
/*  9 */     return null;
/*    */   }
/*    */   
/*    */   default char[] getPassword() {
/* 13 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\RedisCredentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */