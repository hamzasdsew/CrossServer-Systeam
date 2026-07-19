/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisBusyException
/*    */   extends JedisDataException
/*    */ {
/*    */   private static final long serialVersionUID = 3992655220229243478L;
/*    */   
/*    */   public JedisBusyException(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public JedisBusyException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public JedisBusyException(String message, Throwable cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisBusyException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */