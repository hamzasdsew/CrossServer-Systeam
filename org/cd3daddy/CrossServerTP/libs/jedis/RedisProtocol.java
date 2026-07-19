/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ public enum RedisProtocol
/*    */ {
/*  5 */   RESP2("2"),
/*  6 */   RESP3("3");
/*    */   
/*    */   private final String version;
/*    */   
/*    */   RedisProtocol(String ver) {
/* 11 */     this.version = ver;
/*    */   }
/*    */   
/*    */   public String version() {
/* 15 */     return this.version;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\RedisProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */