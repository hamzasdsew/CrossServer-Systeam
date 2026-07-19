/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisMovedDataException
/*    */   extends JedisRedirectionException
/*    */ {
/*    */   private static final long serialVersionUID = 3878126572474819403L;
/*    */   
/*    */   public JedisMovedDataException(String message, HostAndPort targetNode, int slot) {
/* 13 */     super(message, targetNode, slot);
/*    */   }
/*    */   
/*    */   public JedisMovedDataException(Throwable cause, HostAndPort targetNode, int slot) {
/* 17 */     super(cause, targetNode, slot);
/*    */   }
/*    */   
/*    */   public JedisMovedDataException(String message, Throwable cause, HostAndPort targetNode, int slot) {
/* 21 */     super(message, cause, targetNode, slot);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisMovedDataException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */