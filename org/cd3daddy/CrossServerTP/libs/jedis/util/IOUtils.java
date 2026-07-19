/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Socket;
/*    */ 
/*    */ 
/*    */ public class IOUtils
/*    */ {
/*    */   public static void closeQuietly(Socket sock) {
/* 10 */     if (sock != null) {
/*    */       try {
/* 12 */         sock.close();
/* 13 */       } catch (IOException iOException) {}
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void closeQuietly(AutoCloseable resource) {
/* 21 */     if (resource != null) {
/*    */       try {
/* 23 */         resource.close();
/* 24 */       } catch (Exception exception) {}
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private IOUtils() {
/* 31 */     throw new InstantiationError("Must not instantiate this class");
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\IOUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */