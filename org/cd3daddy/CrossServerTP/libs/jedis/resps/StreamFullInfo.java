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
/*    */ public class StreamFullInfo
/*    */   implements Serializable
/*    */ {
/*    */   public static final String LENGTH = "length";
/*    */   public static final String RADIX_TREE_KEYS = "radix-tree-keys";
/*    */   public static final String RADIX_TREE_NODES = "radix-tree-nodes";
/*    */   public static final String GROUPS = "groups";
/*    */   public static final String LAST_GENERATED_ID = "last-generated-id";
/*    */   public static final String ENTRIES = "entries";
/*    */   private final long length;
/*    */   private final long radixTreeKeys;
/*    */   private final long radixTreeNodes;
/*    */   private final List<StreamGroupFullInfo> groups;
/*    */   private final StreamEntryID lastGeneratedId;
/*    */   private final List<StreamEntry> entries;
/*    */   private final Map<String, Object> streamFullInfo;
/*    */   
/*    */   public StreamFullInfo(Map<String, Object> map) {
/* 37 */     this.streamFullInfo = map;
/* 38 */     this.length = ((Long)map.get("length")).longValue();
/* 39 */     this.radixTreeKeys = ((Long)map.get("radix-tree-keys")).longValue();
/* 40 */     this.radixTreeNodes = ((Long)map.get("radix-tree-nodes")).longValue();
/* 41 */     this.groups = (List<StreamGroupFullInfo>)map.get("groups");
/* 42 */     this.lastGeneratedId = (StreamEntryID)map.get("last-generated-id");
/* 43 */     this.entries = (List<StreamEntry>)map.get("entries");
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
/*    */   public List<StreamGroupFullInfo> getGroups() {
/* 60 */     return this.groups;
/*    */   }
/*    */   
/*    */   public StreamEntryID getLastGeneratedId() {
/* 64 */     return this.lastGeneratedId;
/*    */   }
/*    */   
/*    */   public List<StreamEntry> getEntries() {
/* 68 */     return this.entries;
/*    */   }
/*    */   
/*    */   public Map<String, Object> getStreamFullInfo() {
/* 72 */     return this.streamFullInfo;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\StreamFullInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */