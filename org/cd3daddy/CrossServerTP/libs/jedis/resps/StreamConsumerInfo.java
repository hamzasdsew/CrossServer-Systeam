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
/*    */ public class StreamConsumerInfo
/*    */ {
/*    */   public static final String NAME = "name";
/*    */   public static final String IDLE = "idle";
/*    */   public static final String PENDING = "pending";
/*    */   public static final String INACTIVE = "inactive";
/*    */   private final String name;
/*    */   private final long idle;
/*    */   private final long pending;
/*    */   private final Long inactive;
/*    */   private final Map<String, Object> consumerInfo;
/*    */   
/*    */   public StreamConsumerInfo(Map<String, Object> map) {
/* 27 */     this.consumerInfo = map;
/* 28 */     this.name = (String)map.get("name");
/* 29 */     this.idle = ((Long)map.get("idle")).longValue();
/* 30 */     this.pending = ((Long)map.get("pending")).longValue();
/* 31 */     this.inactive = (Long)map.get("inactive");
/*    */   }
/*    */   
/*    */   public String getName() {
/* 35 */     return this.name;
/*    */   }
/*    */   
/*    */   public long getIdle() {
/* 39 */     return this.idle;
/*    */   }
/*    */   
/*    */   public long getPending() {
/* 43 */     return this.pending;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getInactive() {
/* 50 */     return this.inactive;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Object> getConsumerInfo() {
/* 58 */     return this.consumerInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamConsumerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */