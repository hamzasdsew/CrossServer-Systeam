/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.StreamEntryID;
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
/*    */ public class StreamGroupInfo
/*    */   implements Serializable
/*    */ {
/*    */   public static final String NAME = "name";
/*    */   public static final String CONSUMERS = "consumers";
/*    */   public static final String PENDING = "pending";
/*    */   public static final String LAST_DELIVERED = "last-delivered-id";
/*    */   private final String name;
/*    */   private final long consumers;
/*    */   private final long pending;
/*    */   private final StreamEntryID lastDeliveredId;
/*    */   private final Map<String, Object> groupInfo;
/*    */   
/*    */   public StreamGroupInfo(Map<String, Object> map) {
/* 30 */     this.groupInfo = map;
/* 31 */     this.name = (String)map.get("name");
/* 32 */     this.consumers = ((Long)map.get("consumers")).longValue();
/* 33 */     this.pending = ((Long)map.get("pending")).longValue();
/* 34 */     this.lastDeliveredId = (StreamEntryID)map.get("last-delivered-id");
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 39 */     return this.name;
/*    */   }
/*    */   
/*    */   public long getConsumers() {
/* 43 */     return this.consumers;
/*    */   }
/*    */   
/*    */   public long getPending() {
/* 47 */     return this.pending;
/*    */   }
/*    */   
/*    */   public StreamEntryID getLastDeliveredId() {
/* 51 */     return this.lastDeliveredId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Object> getGroupInfo() {
/* 58 */     return this.groupInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamGroupInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */