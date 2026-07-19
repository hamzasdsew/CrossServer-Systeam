/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.gears;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public class RedisGearsProtocol
/*    */ {
/*    */   public enum GearsCommand
/*    */     implements ProtocolCommand {
/* 11 */     TFUNCTION,
/* 12 */     TFCALL,
/* 13 */     TFCALLASYNC;
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     GearsCommand() {
/* 18 */       this.raw = SafeEncoder.encode(name());
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 23 */       return this.raw;
/*    */     }
/*    */   }
/*    */   
/*    */   public enum GearsKeyword
/*    */     implements Rawable {
/* 29 */     CONFIG,
/* 30 */     REPLACE,
/* 31 */     LOAD,
/* 32 */     DELETE,
/* 33 */     LIST,
/* 34 */     WITHCODE,
/* 35 */     LIBRARY,
/* 36 */     VERBOSE;
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     GearsKeyword() {
/* 41 */       this.raw = SafeEncoder.encode(name());
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 46 */       return this.raw;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\RedisGearsProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */