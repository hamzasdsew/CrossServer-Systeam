/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisClusterOperationException;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisClusterCRC16;
/*    */ 
/*    */ public class ClusterCommandArguments
/*    */   extends CommandArguments {
/*  9 */   private int commandHashSlot = -1;
/*    */   
/*    */   public ClusterCommandArguments(ProtocolCommand command) {
/* 12 */     super(command);
/*    */   }
/*    */   
/*    */   public int getCommandHashSlot() {
/* 16 */     return this.commandHashSlot;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments processKey(byte[] key) {
/* 21 */     int hashSlot = JedisClusterCRC16.getSlot(key);
/* 22 */     if (this.commandHashSlot < 0) {
/* 23 */       this.commandHashSlot = hashSlot;
/* 24 */     } else if (this.commandHashSlot != hashSlot) {
/* 25 */       throw new JedisClusterOperationException("Keys must belong to same hashslot.");
/*    */     } 
/* 27 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments processKey(String key) {
/* 32 */     int hashSlot = JedisClusterCRC16.getSlot(key);
/* 33 */     if (this.commandHashSlot < 0) {
/* 34 */       this.commandHashSlot = hashSlot;
/* 35 */     } else if (this.commandHashSlot != hashSlot) {
/* 36 */       throw new JedisClusterOperationException("Keys must belong to same hashslot.");
/*    */     } 
/* 38 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ClusterCommandArguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */