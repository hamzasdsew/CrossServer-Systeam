/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.List;
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
/*    */ 
/*    */ public class ClusterShardInfo
/*    */ {
/*    */   public static final String SLOTS = "slots";
/*    */   public static final String NODES = "nodes";
/*    */   private final List<List<Long>> slots;
/*    */   private final List<ClusterShardNodeInfo> nodes;
/*    */   private final Map<String, Object> clusterShardInfo;
/*    */   
/*    */   public ClusterShardInfo(Map<String, Object> map) {
/* 26 */     this.slots = (List<List<Long>>)map.get("slots");
/* 27 */     this.nodes = (List<ClusterShardNodeInfo>)map.get("nodes");
/*    */     
/* 29 */     this.clusterShardInfo = map;
/*    */   }
/*    */   
/*    */   public List<List<Long>> getSlots() {
/* 33 */     return this.slots;
/*    */   }
/*    */   
/*    */   public List<ClusterShardNodeInfo> getNodes() {
/* 37 */     return this.nodes;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getClusterShardInfo() {
/* 41 */     return this.clusterShardInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\ClusterShardInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */