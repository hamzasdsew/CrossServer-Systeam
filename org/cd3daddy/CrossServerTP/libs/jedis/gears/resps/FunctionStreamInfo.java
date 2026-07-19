/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.gears.resps;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*    */ 
/*    */ public class FunctionStreamInfo
/*    */ {
/*    */   private final String name;
/*    */   private final String idToReadFrom;
/*    */   private final String lastError;
/*    */   private final long lastLag;
/*    */   private final long lastProcessedTime;
/*    */   private final long totalLag;
/*    */   private final long totalProcessedTime;
/*    */   private final long totalRecordProcessed;
/*    */   private final List<String> pendingIds;
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getIdToReadFrom() {
/* 25 */     return this.idToReadFrom;
/*    */   }
/*    */   
/*    */   public String getLastError() {
/* 29 */     return this.lastError;
/*    */   }
/*    */   
/*    */   public long getLastLag() {
/* 33 */     return this.lastLag;
/*    */   }
/*    */   
/*    */   public long getLastProcessedTime() {
/* 37 */     return this.lastProcessedTime;
/*    */   }
/*    */   
/*    */   public long getTotalLag() {
/* 41 */     return this.totalLag;
/*    */   }
/*    */   
/*    */   public long getTotalProcessedTime() {
/* 45 */     return this.totalProcessedTime;
/*    */   }
/*    */   
/*    */   public long getTotalRecordProcessed() {
/* 49 */     return this.totalRecordProcessed;
/*    */   }
/*    */   
/*    */   public List<String> getPendingIds() {
/* 53 */     return this.pendingIds;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FunctionStreamInfo(String name, String idToReadFrom, String lastError, long lastProcessedTime, long lastLag, long totalLag, long totalProcessedTime, long totalRecordProcessed, List<String> pendingIds) {
/* 59 */     this.name = name;
/* 60 */     this.idToReadFrom = idToReadFrom;
/* 61 */     this.lastError = lastError;
/* 62 */     this.lastProcessedTime = lastProcessedTime;
/* 63 */     this.lastLag = lastLag;
/* 64 */     this.totalLag = totalLag;
/* 65 */     this.totalProcessedTime = totalProcessedTime;
/* 66 */     this.totalRecordProcessed = totalRecordProcessed;
/* 67 */     this.pendingIds = pendingIds;
/*    */   }
/*    */   
/* 70 */   public static final Builder<List<FunctionStreamInfo>> STREAM_INFO_LIST = new Builder<List<FunctionStreamInfo>>()
/*    */     {
/*    */       public List<FunctionStreamInfo> build(Object data) {
/* 73 */         return (List<FunctionStreamInfo>)((List)data).stream().map(pairObject -> (List)pairObject)
/* 74 */           .map(pairList -> new FunctionStreamInfo((String)BuilderFactory.STRING.build(pairList.get(9)), (String)BuilderFactory.STRING.build(pairList.get(1)), (String)BuilderFactory.STRING.build(pairList.get(3)), ((Long)BuilderFactory.LONG.build(pairList.get(7))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(5))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(13))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(15))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(17))).longValue(), (List<String>)BuilderFactory.STRING_LIST.build(pairList.get(11))))
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
/* 85 */           .collect(Collectors.toList());
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\resps\FunctionStreamInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */