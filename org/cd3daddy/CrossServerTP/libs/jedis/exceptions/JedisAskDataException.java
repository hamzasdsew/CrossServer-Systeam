/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisAskDataException
/*    */   extends JedisRedirectionException
/*    */ {
/*    */   private static final long serialVersionUID = 3878126572474819403L;
/*    */   
/*    */   public JedisAskDataException(Throwable cause, HostAndPort targetHost, int slot) {
/* 13 */     super(cause, targetHost, slot);
/*    */   }
/*    */   
/*    */   public JedisAskDataException(String message, Throwable cause, HostAndPort targetHost, int slot) {
/* 17 */     super(message, cause, targetHost, slot);
/*    */   }
/*    */   
/*    */   public JedisAskDataException(String message, HostAndPort targetHost, int slot) {
/* 21 */     super(message, targetHost, slot);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisAskDataException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */