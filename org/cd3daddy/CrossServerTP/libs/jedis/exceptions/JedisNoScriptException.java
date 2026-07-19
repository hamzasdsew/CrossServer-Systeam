/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisNoScriptException
/*    */   extends JedisDataException
/*    */ {
/*    */   private static final long serialVersionUID = 4674378093072060731L;
/*    */   
/*    */   public JedisNoScriptException(String message) {
/* 11 */     super(message);
/*    */   }
/*    */   
/*    */   public JedisNoScriptException(Throwable cause) {
/* 15 */     super(cause);
/*    */   }
/*    */   
/*    */   public JedisNoScriptException(String message, Throwable cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisNoScriptException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */