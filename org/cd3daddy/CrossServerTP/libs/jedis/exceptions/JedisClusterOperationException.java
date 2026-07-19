/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisClusterOperationException
/*    */   extends JedisException
/*    */ {
/*    */   private static final long serialVersionUID = 8124535086306604887L;
/*    */   
/*    */   public JedisClusterOperationException(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public JedisClusterOperationException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public JedisClusterOperationException(String message, Throwable cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisClusterOperationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */