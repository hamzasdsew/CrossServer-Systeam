/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisDataException
/*    */   extends JedisException
/*    */ {
/*    */   private static final long serialVersionUID = 3878126572474819403L;
/*    */   
/*    */   public JedisDataException(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public JedisDataException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public JedisDataException(String message, Throwable cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisDataException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */