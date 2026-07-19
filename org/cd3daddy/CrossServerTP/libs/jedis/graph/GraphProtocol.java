/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.graph;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ @Deprecated
/*    */ public class GraphProtocol
/*    */ {
/*    */   @Deprecated
/*    */   public enum GraphCommand
/*    */     implements ProtocolCommand {
/* 13 */     QUERY,
/* 14 */     RO_QUERY,
/* 15 */     DELETE,
/* 16 */     LIST,
/* 17 */     PROFILE,
/* 18 */     EXPLAIN,
/* 19 */     SLOWLOG,
/* 20 */     CONFIG;
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     GraphCommand() {
/* 25 */       this.raw = SafeEncoder.encode("GRAPH." + name());
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 30 */       return this.raw;
/*    */     }
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public enum GraphKeyword
/*    */     implements Rawable {
/* 37 */     CYPHER,
/* 38 */     TIMEOUT,
/* 39 */     SET,
/* 40 */     GET,
/* 41 */     __COMPACT("--COMPACT");
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     GraphKeyword() {
/* 46 */       this.raw = SafeEncoder.encode(name());
/*    */     }
/*    */     
/*    */     GraphKeyword(String alt) {
/* 50 */       this.raw = SafeEncoder.encode(alt);
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 55 */       return this.raw;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\GraphProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */