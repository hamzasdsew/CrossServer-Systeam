/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import java.net.URI;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.RedisProtocol;
/*    */ 
/*    */ 
/*    */ public final class JedisURIHelper
/*    */ {
/*    */   private static final String REDIS = "redis";
/*    */   private static final String REDISS = "rediss";
/*    */   
/*    */   private JedisURIHelper() {
/* 14 */     throw new InstantiationError("Must not instantiate this class");
/*    */   }
/*    */   
/*    */   public static HostAndPort getHostAndPort(URI uri) {
/* 18 */     return new HostAndPort(uri.getHost(), uri.getPort());
/*    */   }
/*    */   
/*    */   public static String getUser(URI uri) {
/* 22 */     String userInfo = uri.getUserInfo();
/* 23 */     if (userInfo != null) {
/* 24 */       String user = userInfo.split(":", 2)[0];
/* 25 */       if (user.isEmpty()) {
/* 26 */         user = null;
/*    */       }
/* 28 */       return user;
/*    */     } 
/* 30 */     return null;
/*    */   }
/*    */   
/*    */   public static String getPassword(URI uri) {
/* 34 */     String userInfo = uri.getUserInfo();
/* 35 */     if (userInfo != null) {
/* 36 */       return userInfo.split(":", 2)[1];
/*    */     }
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public static int getDBIndex(URI uri) {
/* 42 */     String[] pathSplit = uri.getPath().split("/", 2);
/* 43 */     if (pathSplit.length > 1) {
/* 44 */       String dbIndexStr = pathSplit[1];
/* 45 */       if (dbIndexStr.isEmpty()) {
/* 46 */         return 0;
/*    */       }
/* 48 */       return Integer.parseInt(dbIndexStr);
/*    */     } 
/* 50 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public static RedisProtocol getRedisProtocol(URI uri) {
/* 55 */     if (uri.getQuery() == null) return null;
/*    */     
/* 57 */     String[] pairs = uri.getQuery().split("&");
/* 58 */     for (String pair : pairs) {
/* 59 */       int idx = pair.indexOf("=");
/* 60 */       if ("protocol".equals(pair.substring(0, idx))) {
/* 61 */         String ver = pair.substring(idx + 1);
/* 62 */         for (RedisProtocol proto : RedisProtocol.values()) {
/* 63 */           if (proto.version().equals(ver)) {
/* 64 */             return proto;
/*    */           }
/*    */         } 
/* 67 */         throw new IllegalArgumentException("Unknown protocol " + ver);
/*    */       } 
/*    */     } 
/* 70 */     return null;
/*    */   }
/*    */   
/*    */   public static boolean isValid(URI uri) {
/* 74 */     if (isEmpty(uri.getScheme()) || isEmpty(uri.getHost()) || uri.getPort() == -1) {
/* 75 */       return false;
/*    */     }
/*    */     
/* 78 */     return true;
/*    */   }
/*    */   
/*    */   private static boolean isEmpty(String value) {
/* 82 */     return (value == null || value.trim().length() == 0);
/*    */   }
/*    */   
/*    */   public static boolean isRedisScheme(URI uri) {
/* 86 */     return "redis".equals(uri.getScheme());
/*    */   }
/*    */   
/*    */   public static boolean isRedisSSLScheme(URI uri) {
/* 90 */     return "rediss".equals(uri.getScheme());
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\JedisURIHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */