/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.exceptions;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JedisBroadcastException
/*    */   extends JedisDataException
/*    */ {
/*    */   private static final String BROADCAST_ERROR_MESSAGE = "A failure occurred while broadcasting the command.";
/* 17 */   private final Map<HostAndPort, Object> replies = new HashMap<>();
/*    */   
/*    */   public JedisBroadcastException() {
/* 20 */     super("A failure occurred while broadcasting the command.");
/*    */   }
/*    */   
/*    */   public void addReply(HostAndPort node, Object reply) {
/* 24 */     this.replies.put(node, reply);
/*    */   }
/*    */   
/*    */   public Map<HostAndPort, Object> getReplies() {
/* 28 */     return Collections.unmodifiableMap(this.replies);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\exceptions\JedisBroadcastException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */