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
/*    */ public class StreamConsumerFullInfo
/*    */   implements Serializable
/*    */ {
/*    */   public static final String NAME = "name";
/*    */   public static final String SEEN_TIME = "seen-time";
/*    */   public static final String ACTIVE_TIME = "active-time";
/*    */   public static final String PEL_COUNT = "pel-count";
/*    */   public static final String PENDING = "pending";
/*    */   private final String name;
/*    */   private final Long seenTime;
/*    */   private final Long activeTime;
/*    */   private final Long pelCount;
/*    */   private final List<List<Object>> pending;
/*    */   private final Map<String, Object> consumerInfo;
/*    */   
/*    */   public StreamConsumerFullInfo(Map<String, Object> map) {
/* 31 */     this.consumerInfo = map;
/* 32 */     this.name = (String)map.get("name");
/* 33 */     this.seenTime = (Long)map.get("seen-time");
/* 34 */     this.activeTime = (Long)map.get("active-time");
/* 35 */     this.pending = (List<List<Object>>)map.get("pending");
/* 36 */     this.pelCount = (Long)map.get("pel-count");
/*    */     
/* 38 */     this.pending.forEach(entry -> entry.set(0, new StreamEntryID((String)entry.get(0))));
/*    */   }
/*    */   
/*    */   public String getName() {
/* 42 */     return this.name;
/*    */   }
/*    */   
/*    */   public Long getSeenTime() {
/* 46 */     return this.seenTime;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getActiveTime() {
/* 53 */     return this.activeTime;
/*    */   }
/*    */   
/*    */   public Long getPelCount() {
/* 57 */     return this.pelCount;
/*    */   }
/*    */   
/*    */   public List<List<Object>> getPending() {
/* 61 */     return this.pending;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Object> getConsumerInfo() {
/* 68 */     return this.consumerInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamConsumerFullInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */