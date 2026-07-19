/*   */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*   */ 
/*   */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*   */ 
/*   */ public abstract class JedisPubSub
/*   */   extends JedisPubSubBase<String>
/*   */ {
/*   */   protected final String encode(byte[] raw) {
/* 9 */     return SafeEncoder.encode(raw);
/*   */   }
/*   */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisPubSub.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */