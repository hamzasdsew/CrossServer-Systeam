/*   */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*   */ 
/*   */ public abstract class BinaryJedisPubSub
/*   */   extends JedisPubSubBase<byte[]>
/*   */ {
/*   */   protected final byte[] encode(byte[] raw) {
/* 7 */     return raw;
/*   */   }
/*   */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\BinaryJedisPubSub.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */