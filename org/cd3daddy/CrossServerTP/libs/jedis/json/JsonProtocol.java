/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public class JsonProtocol {
/*    */   public enum JsonCommand
/*    */     implements ProtocolCommand {
/*  9 */     DEL("JSON.DEL"),
/* 10 */     GET("JSON.GET"),
/* 11 */     MGET("JSON.MGET"),
/* 12 */     MERGE("JSON.MERGE"),
/* 13 */     SET("JSON.SET"),
/* 14 */     TYPE("JSON.TYPE"),
/* 15 */     STRAPPEND("JSON.STRAPPEND"),
/* 16 */     STRLEN("JSON.STRLEN"),
/* 17 */     NUMINCRBY("JSON.NUMINCRBY"),
/* 18 */     ARRAPPEND("JSON.ARRAPPEND"),
/* 19 */     ARRINDEX("JSON.ARRINDEX"),
/* 20 */     ARRINSERT("JSON.ARRINSERT"),
/* 21 */     ARRLEN("JSON.ARRLEN"),
/* 22 */     ARRPOP("JSON.ARRPOP"),
/* 23 */     ARRTRIM("JSON.ARRTRIM"),
/* 24 */     CLEAR("JSON.CLEAR"),
/* 25 */     TOGGLE("JSON.TOGGLE"),
/* 26 */     OBJKEYS("JSON.OBJKEYS"),
/* 27 */     OBJLEN("JSON.OBJLEN"),
/* 28 */     DEBUG("JSON.DEBUG"),
/* 29 */     RESP("JSON.RESP");
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     JsonCommand(String alt) {
/* 34 */       this.raw = SafeEncoder.encode(alt);
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 39 */       return this.raw;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\JsonProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */