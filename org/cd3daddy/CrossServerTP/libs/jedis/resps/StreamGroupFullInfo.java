/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.List;
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
/*    */ 
/*    */ 
/*    */ public class StreamGroupFullInfo
/*    */   implements Serializable
/*    */ {
/*    */   public static final String NAME = "name";
/*    */   public static final String CONSUMERS = "consumers";
/*    */   public static final String PENDING = "pending";
/*    */   public static final String LAST_DELIVERED = "last-delivered-id";
/*    */   public static final String PEL_COUNT = "pel-count";
/*    */   private final String name;
/*    */   private final List<StreamConsumerFullInfo> consumers;
/*    */   private final List<List<Object>> pending;
/*    */   private final Long pelCount;
/*    */   private final StreamEntryID lastDeliveredId;
/*    */   private final Map<String, Object> groupFullInfo;
/*    */   
/*    */   public StreamGroupFullInfo(Map<String, Object> map) {
/* 35 */     this.groupFullInfo = map;
/* 36 */     this.name = (String)map.get("name");
/* 37 */     this.consumers = (List<StreamConsumerFullInfo>)map.get("consumers");
/* 38 */     this.pending = (List<List<Object>>)map.get("pending");
/* 39 */     this.lastDeliveredId = (StreamEntryID)map.get("last-delivered-id");
/* 40 */     this.pelCount = (Long)map.get("pel-count");
/*    */     
/* 42 */     this.pending.stream().forEach(entry -> entry.set(0, new StreamEntryID((String)entry.get(0))));
/*    */   }
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */   
/*    */   public List<StreamConsumerFullInfo> getConsumers() {
/* 50 */     return this.consumers;
/*    */   }
/*    */   
/*    */   public List<List<Object>> getPending() {
/* 54 */     return this.pending;
/*    */   }
/*    */   
/*    */   public StreamEntryID getLastDeliveredId() {
/* 58 */     return this.lastDeliveredId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Object> getGroupFullInfo() {
/* 65 */     return this.groupFullInfo;
/*    */   }
/*    */   
/*    */   public Long getPelCount() {
/* 69 */     return this.pelCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamGroupFullInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */