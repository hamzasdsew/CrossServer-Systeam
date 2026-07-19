/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisClusterException
/*    */   extends JedisDataException
/*    */ {
/*    */   private static final long serialVersionUID = 3878126572474819403L;
/*    */   
/*    */   public JedisClusterException(Throwable cause) {
/* 11 */     super(cause);
/*    */   }
/*    */   
/*    */   public JedisClusterException(String message, Throwable cause) {
/* 15 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public JedisClusterException(String message) {
/* 19 */     super(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisClusterException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */