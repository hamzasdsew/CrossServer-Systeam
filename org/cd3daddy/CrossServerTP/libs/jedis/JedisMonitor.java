/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ public abstract class JedisMonitor
/*    */ {
/*    */   protected Connection client;
/*    */   
/*    */   public void proceed(Connection client) {
/*  8 */     this.client = client;
/*  9 */     this.client.setTimeoutInfinite();
/*    */     do {
/* 11 */       String command = client.getBulkReply();
/* 12 */       onCommand(command);
/* 13 */     } while (client.isConnected());
/*    */   }
/*    */   
/*    */   public abstract void onCommand(String paramString);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisMonitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */