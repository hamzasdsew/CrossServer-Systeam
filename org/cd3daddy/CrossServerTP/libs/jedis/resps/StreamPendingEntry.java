/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.StreamEntryID;
/*    */ 
/*    */ public class StreamPendingEntry
/*    */   implements Serializable {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private StreamEntryID id;
/*    */   private String consumerName;
/*    */   private long idleTime;
/*    */   private long deliveredTimes;
/*    */   
/*    */   public StreamPendingEntry(StreamEntryID id, String consumerName, long idleTime, long deliveredTimes) {
/* 18 */     this.id = id;
/* 19 */     this.consumerName = consumerName;
/* 20 */     this.idleTime = idleTime;
/* 21 */     this.deliveredTimes = deliveredTimes;
/*    */   }
/*    */   
/*    */   public StreamEntryID getID() {
/* 25 */     return this.id;
/*    */   }
/*    */   
/*    */   public long getIdleTime() {
/* 29 */     return this.idleTime;
/*    */   }
/*    */   
/*    */   public long getDeliveredTimes() {
/* 33 */     return this.deliveredTimes;
/*    */   }
/*    */   
/*    */   public String getConsumerName() {
/* 37 */     return this.consumerName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 42 */     return this.id + " " + this.consumerName + " idle:" + this.idleTime + " times:" + this.deliveredTimes;
/*    */   }
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 47 */     out.writeUnshared(this.id);
/* 48 */     out.writeUTF(this.consumerName);
/* 49 */     out.writeLong(this.idleTime);
/* 50 */     out.writeLong(this.deliveredTimes);
/*    */   }
/*    */   
/*    */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 54 */     this.id = (StreamEntryID)in.readUnshared();
/* 55 */     this.consumerName = in.readUTF();
/* 56 */     this.idleTime = in.readLong();
/* 57 */     this.deliveredTimes = in.readLong();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamPendingEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */