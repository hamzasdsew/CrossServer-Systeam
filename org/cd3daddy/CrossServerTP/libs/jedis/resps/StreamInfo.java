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
/*    */ public class StreamInfo
/*    */   implements Serializable
/*    */ {
/*    */   public static final String LENGTH = "length";
/*    */   public static final String RADIX_TREE_KEYS = "radix-tree-keys";
/*    */   public static final String RADIX_TREE_NODES = "radix-tree-nodes";
/*    */   public static final String GROUPS = "groups";
/*    */   public static final String LAST_GENERATED_ID = "last-generated-id";
/*    */   public static final String FIRST_ENTRY = "first-entry";
/*    */   public static final String LAST_ENTRY = "last-entry";
/*    */   private final long length;
/*    */   private final long radixTreeKeys;
/*    */   private final long radixTreeNodes;
/*    */   private final long groups;
/*    */   private final StreamEntryID lastGeneratedId;
/*    */   private final StreamEntry firstEntry;
/*    */   private final StreamEntry lastEntry;
/*    */   private final Map<String, Object> streamInfo;
/*    */   
/*    */   public StreamInfo(Map<String, Object> map) {
/* 36 */     this.streamInfo = map;
/* 37 */     this.length = ((Long)map.get("length")).longValue();
/* 38 */     this.radixTreeKeys = ((Long)map.get("radix-tree-keys")).longValue();
/* 39 */     this.radixTreeNodes = ((Long)map.get("radix-tree-nodes")).longValue();
/* 40 */     this.groups = ((Long)map.get("groups")).longValue();
/* 41 */     this.lastGeneratedId = (StreamEntryID)map.get("last-generated-id");
/* 42 */     this.firstEntry = (StreamEntry)map.get("first-entry");
/* 43 */     this.lastEntry = (StreamEntry)map.get("last-entry");
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLength() {
/* 48 */     return this.length;
/*    */   }
/*    */   
/*    */   public long getRadixTreeKeys() {
/* 52 */     return this.radixTreeKeys;
/*    */   }
/*    */   
/*    */   public long getRadixTreeNodes() {
/* 56 */     return this.radixTreeNodes;
/*    */   }
/*    */   
/*    */   public long getGroups() {
/* 60 */     return this.groups;
/*    */   }
/*    */   
/*    */   public StreamEntryID getLastGeneratedId() {
/* 64 */     return this.lastGeneratedId;
/*    */   }
/*    */   
/*    */   public StreamEntry getFirstEntry() {
/* 68 */     return this.firstEntry;
/*    */   }
/*    */   
/*    */   public StreamEntry getLastEntry() {
/* 72 */     return this.lastEntry;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Object> getStreamInfo() {
/* 79 */     return this.streamInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */