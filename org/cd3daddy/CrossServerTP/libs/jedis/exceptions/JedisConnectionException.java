/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisConnectionException
/*    */   extends JedisException
/*    */ {
/*    */   private static final long serialVersionUID = 3878126572474819403L;
/*    */   
/*    */   public JedisConnectionException(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public JedisConnectionException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public JedisConnectionException(String message, Throwable cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisConnectionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */