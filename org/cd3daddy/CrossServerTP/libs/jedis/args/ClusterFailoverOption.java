/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ClusterFailoverOption
/*    */   implements Rawable
/*    */ {
/* 12 */   FORCE, TAKEOVER;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   ClusterFailoverOption() {
/* 17 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 22 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\ClusterFailoverOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */