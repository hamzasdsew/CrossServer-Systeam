/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ClusterShardNodeInfo
/*    */ {
/*    */   public static final String ID = "id";
/*    */   public static final String ENDPOINT = "endpoint";
/*    */   public static final String IP = "ip";
/*    */   public static final String HOSTNAME = "hostname";
/*    */   public static final String PORT = "port";
/*    */   public static final String TLS_PORT = "tls-port";
/*    */   public static final String ROLE = "role";
/*    */   public static final String REPLICATION_OFFSET = "replication-offset";
/*    */   public static final String HEALTH = "health";
/*    */   private final String id;
/*    */   private final String endpoint;
/*    */   private final String ip;
/*    */   private final String hostname;
/*    */   private final Long port;
/*    */   private final Long tlsPort;
/*    */   private final String role;
/*    */   private final Long replicationOffset;
/*    */   private final String health;
/*    */   private final Map<String, Object> clusterShardNodeInfo;
/*    */   
/*    */   public ClusterShardNodeInfo(Map<String, Object> map) {
/* 38 */     this.id = (String)map.get("id");
/* 39 */     this.endpoint = (String)map.get("endpoint");
/* 40 */     this.ip = (String)map.get("ip");
/* 41 */     this.hostname = (String)map.get("hostname");
/* 42 */     this.port = (Long)map.get("port");
/* 43 */     this.tlsPort = (Long)map.get("tls-port");
/* 44 */     this.role = (String)map.get("role");
/* 45 */     this.replicationOffset = (Long)map.get("replication-offset");
/* 46 */     this.health = (String)map.get("health");
/*    */     
/* 48 */     this.clusterShardNodeInfo = map;
/*    */   }
/*    */   
/*    */   public String getId() {
/* 52 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getEndpoint() {
/* 56 */     return this.endpoint;
/*    */   }
/*    */   
/*    */   public String getIp() {
/* 60 */     return this.ip;
/*    */   }
/*    */   
/*    */   public String getHostname() {
/* 64 */     return this.hostname;
/*    */   }
/*    */   
/*    */   public Long getPort() {
/* 68 */     return this.port;
/*    */   }
/*    */   
/*    */   public Long getTlsPort() {
/* 72 */     return this.tlsPort;
/*    */   }
/*    */   
/*    */   public String getRole() {
/* 76 */     return this.role;
/*    */   }
/*    */   
/*    */   public Long getReplicationOffset() {
/* 80 */     return this.replicationOffset;
/*    */   }
/*    */   
/*    */   public String getHealth() {
/* 84 */     return this.health;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getClusterShardNodeInfo() {
/* 88 */     return this.clusterShardNodeInfo;
/*    */   }
/*    */   
/*    */   public boolean isSsl() {
/* 92 */     return (this.tlsPort != null);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\ClusterShardNodeInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */