/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisValidationException
/*    */   extends JedisException
/*    */ {
/*    */   private static final long serialVersionUID = 1134169242443303479L;
/*    */   
/*    */   public JedisValidationException(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public JedisValidationException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public JedisValidationException(String message, Throwable cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisValidationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */