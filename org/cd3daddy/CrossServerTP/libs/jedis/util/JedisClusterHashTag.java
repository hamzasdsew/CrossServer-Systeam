/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JedisClusterHashTag
/*    */ {
/*    */   private JedisClusterHashTag() {
/* 10 */     throw new InstantiationError("Must not instantiate this class");
/*    */   }
/*    */   
/*    */   public static String getHashTag(String key) {
/* 14 */     return extractHashTag(key, true);
/*    */   }
/*    */   
/*    */   public static boolean isClusterCompliantMatchPattern(byte[] matchPattern) {
/* 18 */     return isClusterCompliantMatchPattern(SafeEncoder.encode(matchPattern));
/*    */   }
/*    */   
/*    */   public static boolean isClusterCompliantMatchPattern(String matchPattern) {
/* 22 */     String tag = extractHashTag(matchPattern, false);
/* 23 */     return (tag != null && !tag.isEmpty());
/*    */   }
/*    */   
/*    */   private static String extractHashTag(String key, boolean returnKeyOnAbsence) {
/* 27 */     int s = key.indexOf("{");
/* 28 */     if (s > -1) {
/* 29 */       int e = key.indexOf("}", s + 1);
/* 30 */       if (e > -1 && e != s + 1) {
/* 31 */         return key.substring(s + 1, e);
/*    */       }
/*    */     } 
/* 34 */     return returnKeyOnAbsence ? key : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\JedisClusterHashTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */