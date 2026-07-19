/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommandDocument
/*    */ {
/*    */   private static final String SUMMARY_STR = "summary";
/*    */   private static final String SINCE_STR = "since";
/*    */   private static final String GROUP_STR = "group";
/*    */   private static final String COMPLEXITY_STR = "complexity";
/*    */   private static final String HISTORY_STR = "history";
/*    */   private final String summary;
/*    */   private final String since;
/*    */   private final String group;
/*    */   private final String complexity;
/*    */   private final List<String> history;
/*    */   
/*    */   @Deprecated
/*    */   public CommandDocument(String summary, String since, String group, String complexity, List<String> history) {
/* 30 */     this.summary = summary;
/* 31 */     this.since = since;
/* 32 */     this.group = group;
/* 33 */     this.complexity = complexity;
/* 34 */     this.history = history;
/*    */   }
/*    */   
/*    */   public CommandDocument(Map<String, Object> map) {
/* 38 */     this.summary = (String)map.get("summary");
/* 39 */     this.since = (String)map.get("since");
/* 40 */     this.group = (String)map.get("group");
/* 41 */     this.complexity = (String)map.get("complexity");
/*    */     
/* 43 */     List<Object> historyObject = (List<Object>)map.get("history");
/* 44 */     if (historyObject == null) {
/* 45 */       this.history = null;
/* 46 */     } else if (historyObject.isEmpty()) {
/* 47 */       this.history = Collections.emptyList();
/* 48 */     } else if (historyObject.get(0) instanceof KeyValue) {
/* 49 */       this
/*    */         
/* 51 */         .history = (List<String>)historyObject.stream().map(o -> (KeyValue)o).map(kv -> (String)kv.getKey() + ": " + (String)kv.getValue()).collect(Collectors.toList());
/*    */     } else {
/* 53 */       this
/*    */         
/* 55 */         .history = (List<String>)historyObject.stream().map(o -> (List)o).map(l -> (String)l.get(0) + ": " + (String)l.get(1)).collect(Collectors.toList());
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getSummary() {
/* 60 */     return this.summary;
/*    */   }
/*    */   
/*    */   public String getSince() {
/* 64 */     return this.since;
/*    */   }
/*    */   
/*    */   public String getGroup() {
/* 68 */     return this.group;
/*    */   }
/*    */   
/*    */   public String getComplexity() {
/* 72 */     return this.complexity;
/*    */   }
/*    */   
/*    */   public List<String> getHistory() {
/* 76 */     return this.history;
/*    */   }
/*    */   
/*    */   @Deprecated
/* 80 */   public static final Builder<CommandDocument> COMMAND_DOCUMENT_BUILDER = new Builder<CommandDocument>()
/*    */     {
/*    */       public CommandDocument build(Object data) {
/* 83 */         List<Object> commandData = (List<Object>)data;
/* 84 */         String summary = (String)BuilderFactory.STRING.build(commandData.get(1));
/* 85 */         String since = (String)BuilderFactory.STRING.build(commandData.get(3));
/* 86 */         String group = (String)BuilderFactory.STRING.build(commandData.get(5));
/* 87 */         String complexity = (String)BuilderFactory.STRING.build(commandData.get(7));
/* 88 */         List<String> history = null;
/* 89 */         if (((String)BuilderFactory.STRING.build(commandData.get(8))).equals("history")) {
/* 90 */           List<List<Object>> rawHistory = (List<List<Object>>)commandData.get(9);
/* 91 */           history = new ArrayList<>(rawHistory.size());
/* 92 */           for (List<Object> timePoint : rawHistory) {
/* 93 */             history.add((String)BuilderFactory.STRING.build(timePoint.get(0)) + ": " + (String)BuilderFactory.STRING.build(timePoint.get(1)));
/*    */           }
/*    */         } 
/* 96 */         return new CommandDocument(summary, since, group, complexity, history);
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\CommandDocument.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */