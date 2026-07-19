/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -2946266495682282677L;
/*    */   
/*    */   public JedisException(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public JedisException(Throwable e) {
/* 15 */     super(e);
/*    */   }
/*    */   
/*    */   public JedisException(String message, Throwable cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */