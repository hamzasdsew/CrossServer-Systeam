/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.StreamEntryID;
/*    */ 
/*    */ 
/*    */ public class StreamPendingSummary
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private final long total;
/*    */   private final StreamEntryID minId;
/*    */   private final StreamEntryID maxId;
/*    */   private final Map<String, Long> consumerMessageCount;
/*    */   
/*    */   public StreamPendingSummary(long total, StreamEntryID minId, StreamEntryID maxId, Map<String, Long> consumerMessageCount) {
/* 18 */     this.total = total;
/* 19 */     this.minId = minId;
/* 20 */     this.maxId = maxId;
/* 21 */     this.consumerMessageCount = consumerMessageCount;
/*    */   }
/*    */   
/*    */   public long getTotal() {
/* 25 */     return this.total;
/*    */   }
/*    */   
/*    */   public StreamEntryID getMinId() {
/* 29 */     return this.minId;
/*    */   }
/*    */   
/*    */   public StreamEntryID getMaxId() {
/* 33 */     return this.maxId;
/*    */   }
/*    */   
/*    */   public Map<String, Long> getConsumerMessageCount() {
/* 37 */     return this.consumerMessageCount;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamPendingSummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */