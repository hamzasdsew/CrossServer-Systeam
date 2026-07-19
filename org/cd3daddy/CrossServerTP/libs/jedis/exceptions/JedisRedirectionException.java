/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisRedirectionException
/*    */   extends JedisDataException
/*    */ {
/*    */   private static final long serialVersionUID = 3878126572474819403L;
/*    */   private final HostAndPort targetNode;
/*    */   private final int slot;
/*    */   
/*    */   public JedisRedirectionException(String message, HostAndPort targetNode, int slot) {
/* 19 */     super(message);
/* 20 */     this.targetNode = targetNode;
/* 21 */     this.slot = slot;
/*    */   }
/*    */   
/*    */   public JedisRedirectionException(Throwable cause, HostAndPort targetNode, int slot) {
/* 25 */     super(cause);
/* 26 */     this.targetNode = targetNode;
/* 27 */     this.slot = slot;
/*    */   }
/*    */   
/*    */   public JedisRedirectionException(String message, Throwable cause, HostAndPort targetNode, int slot) {
/* 31 */     super(message, cause);
/* 32 */     this.targetNode = targetNode;
/* 33 */     this.slot = slot;
/*    */   }
/*    */   
/*    */   public final HostAndPort getTargetNode() {
/* 37 */     return this.targetNode;
/*    */   }
/*    */   
/*    */   public final int getSlot() {
/* 41 */     return this.slot;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisRedirectionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */