/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ClientType;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ public class ClientKillParams
/*    */   implements IParams
/*    */ {
/*    */   public enum SkipMe {
/* 13 */     YES, NO;
/*    */   }
/*    */   
/* 16 */   private final ArrayList<KeyValue<Protocol.Keyword, Object>> params = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ClientKillParams clientKillParams() {
/* 22 */     return new ClientKillParams();
/*    */   }
/*    */   
/*    */   private ClientKillParams addParam(Protocol.Keyword key, Object value) {
/* 26 */     this.params.add(KeyValue.of(key, value));
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public ClientKillParams id(String clientId) {
/* 31 */     return addParam(Protocol.Keyword.ID, clientId);
/*    */   }
/*    */   
/*    */   public ClientKillParams id(byte[] clientId) {
/* 35 */     return addParam(Protocol.Keyword.ID, clientId);
/*    */   }
/*    */   
/*    */   public ClientKillParams type(ClientType type) {
/* 39 */     return addParam(Protocol.Keyword.TYPE, type);
/*    */   }
/*    */   
/*    */   public ClientKillParams addr(String ipPort) {
/* 43 */     return addParam(Protocol.Keyword.ADDR, ipPort);
/*    */   }
/*    */   
/*    */   public ClientKillParams addr(byte[] ipPort) {
/* 47 */     return addParam(Protocol.Keyword.ADDR, ipPort);
/*    */   }
/*    */   
/*    */   public ClientKillParams addr(String ip, int port) {
/* 51 */     return addParam(Protocol.Keyword.ADDR, ip + ':' + port);
/*    */   }
/*    */   
/*    */   public ClientKillParams skipMe(SkipMe skipMe) {
/* 55 */     return addParam(Protocol.Keyword.SKIPME, skipMe);
/*    */   }
/*    */   
/*    */   public ClientKillParams user(String username) {
/* 59 */     return addParam(Protocol.Keyword.USER, username);
/*    */   }
/*    */   
/*    */   public ClientKillParams laddr(String ipPort) {
/* 63 */     return addParam(Protocol.Keyword.LADDR, ipPort);
/*    */   }
/*    */   
/*    */   public ClientKillParams laddr(String ip, int port) {
/* 67 */     return addParam(Protocol.Keyword.LADDR, ip + ':' + port);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 72 */     this.params.forEach(kv -> args.add(kv.getKey()).add(kv.getValue()));
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ClientKillParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */