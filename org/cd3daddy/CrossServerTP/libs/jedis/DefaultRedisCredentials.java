/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ public final class DefaultRedisCredentials
/*    */   implements RedisCredentials {
/*    */   private final String user;
/*    */   private final char[] password;
/*    */   
/*    */   public DefaultRedisCredentials(String user, char[] password) {
/*  9 */     this.user = user;
/* 10 */     this.password = password;
/*    */   }
/*    */   
/*    */   public DefaultRedisCredentials(String user, CharSequence password) {
/* 14 */     this.user = user;
/* 15 */     this
/*    */       
/* 17 */       .password = (password == null) ? null : ((password instanceof String) ? ((String)password).toCharArray() : toCharArray(password));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUser() {
/* 22 */     return this.user;
/*    */   }
/*    */ 
/*    */   
/*    */   public char[] getPassword() {
/* 27 */     return this.password;
/*    */   }
/*    */   
/*    */   private static char[] toCharArray(CharSequence seq) {
/* 31 */     int len = seq.length();
/* 32 */     char[] arr = new char[len];
/* 33 */     for (int i = 0; i < len; i++) {
/* 34 */       arr[i] = seq.charAt(i);
/*    */     }
/* 36 */     return arr;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\DefaultRedisCredentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */