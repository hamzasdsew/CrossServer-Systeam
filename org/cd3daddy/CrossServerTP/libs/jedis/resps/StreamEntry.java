/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.StreamEntryID;
/*    */ 
/*    */ public class StreamEntry implements Serializable {
/*    */   private static final long serialVersionUID = 1L;
/*    */   private StreamEntryID id;
/*    */   private Map<String, String> fields;
/*    */   
/*    */   public StreamEntry(StreamEntryID id, Map<String, String> fields) {
/* 16 */     this.id = id;
/* 17 */     this.fields = fields;
/*    */   }
/*    */   
/*    */   public StreamEntryID getID() {
/* 21 */     return this.id;
/*    */   }
/*    */   
/*    */   public Map<String, String> getFields() {
/* 25 */     return this.fields;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     return this.id + " " + this.fields;
/*    */   }
/*    */   
/*    */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 34 */     out.writeUnshared(this.id);
/* 35 */     out.writeUnshared(this.fields);
/*    */   }
/*    */   
/*    */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 39 */     this.id = (StreamEntryID)in.readUnshared();
/* 40 */     this.fields = (Map<String, String>)in.readUnshared();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */