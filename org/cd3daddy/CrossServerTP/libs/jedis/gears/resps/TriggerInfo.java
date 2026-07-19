/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.gears.resps;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TriggerInfo
/*     */ {
/*     */   private final String name;
/*     */   private final String description;
/*     */   private final String lastError;
/*     */   private final long lastExecutionTime;
/*     */   private final long numFailed;
/*     */   private final long numFinished;
/*     */   private final long numSuccess;
/*     */   private final long numTrigger;
/*     */   private final long totalExecutionTime;
/*     */   
/*     */   public String getName() {
/*  35 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  39 */     return this.description;
/*     */   }
/*     */   
/*     */   public String getLastError() {
/*  43 */     return this.lastError;
/*     */   }
/*     */   
/*     */   public long getLastExecutionTime() {
/*  47 */     return this.lastExecutionTime;
/*     */   }
/*     */   
/*     */   public long getNumFailed() {
/*  51 */     return this.numFailed;
/*     */   }
/*     */   
/*     */   public long getNumFinished() {
/*  55 */     return this.numFinished;
/*     */   }
/*     */   
/*     */   public long getNumSuccess() {
/*  59 */     return this.numSuccess;
/*     */   }
/*     */   
/*     */   public long getNumTrigger() {
/*  63 */     return this.numTrigger;
/*     */   }
/*     */   
/*     */   public long getTotalExecutionTime() {
/*  67 */     return this.totalExecutionTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public TriggerInfo(String name, String description, String lastError, long numFinished, long numSuccess, long numFailed, long numTrigger, long lastExecutionTime, long totalExecutionTime) {
/*  72 */     this.name = name;
/*  73 */     this.description = description;
/*  74 */     this.lastError = lastError;
/*  75 */     this.numFinished = numFinished;
/*  76 */     this.numSuccess = numSuccess;
/*  77 */     this.numFailed = numFailed;
/*  78 */     this.numTrigger = numTrigger;
/*  79 */     this.lastExecutionTime = lastExecutionTime;
/*  80 */     this.totalExecutionTime = totalExecutionTime;
/*     */   }
/*     */   
/*  83 */   public static final Builder<List<TriggerInfo>> KEYSPACE_TRIGGER_INFO_LIST = new Builder<List<TriggerInfo>>()
/*     */     {
/*     */       public List<TriggerInfo> build(Object data) {
/*  86 */         List<Object> dataAsList = (List<Object>)data;
/*  87 */         if (!dataAsList.isEmpty()) {
/*  88 */           boolean isListOfList = dataAsList.get(0).getClass().isAssignableFrom(ArrayList.class);
/*  89 */           if (isListOfList) {
/*  90 */             if (((List)((List)data).get(0)).get(0) instanceof KeyValue) {
/*  91 */               List<List<KeyValue>> dataAsKeyValues = (List<List<KeyValue>>)data;
/*  92 */               return (List<TriggerInfo>)dataAsKeyValues.stream().map(keyValues -> {
/*     */                     String name = null;
/*     */                     String description = null;
/*     */                     String lastError = null;
/*     */                     long lastExecutionTime = 0L;
/*     */                     long numFailed = 0L;
/*     */                     long numFinished = 0L;
/*     */                     long numSuccess = 0L;
/*     */                     long numTrigger = 0L;
/*     */                     long totalExecutionTime = 0L;
/*     */                     for (KeyValue kv : keyValues) {
/*     */                       switch ((String)BuilderFactory.STRING.build(kv.getKey())) {
/*     */                         case "name":
/*     */                           name = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */ 
/*     */                         
/*     */                         case "description":
/*     */                           description = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */ 
/*     */                         
/*     */                         case "last_error":
/*     */                           lastError = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */                         
/*     */                         case "last_execution_time":
/*     */                           lastExecutionTime = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */                         
/*     */                         case "num_failed":
/*     */                           numFailed = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */                         
/*     */                         case "num_finished":
/*     */                           numFinished = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */                         
/*     */                         case "num_success":
/*     */                           numSuccess = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */                         
/*     */                         case "num_trigger":
/*     */                           numTrigger = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */                         
/*     */                         case "total_execution_time":
/*     */                           totalExecutionTime = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */                       } 
/*     */                     
/*     */                     } 
/*     */                     return new TriggerInfo(name, description, lastError, numFinished, numSuccess, numFailed, numTrigger, lastExecutionTime, totalExecutionTime);
/* 136 */                   }).collect(Collectors.toList());
/*     */             } 
/* 138 */             return (List<TriggerInfo>)dataAsList.stream().map(pairObject -> (List)pairObject)
/* 139 */               .map(pairList -> new TriggerInfo((String)BuilderFactory.STRING.build(pairList.get(7)), (String)BuilderFactory.STRING.build(pairList.get(1)), (String)BuilderFactory.STRING.build(pairList.get(3)), ((Long)BuilderFactory.LONG.build(pairList.get(11))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(13))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(9))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(15))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(5))).longValue(), ((Long)BuilderFactory.LONG.build(pairList.get(17))).longValue()))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 149 */               .collect(Collectors.toList());
/*     */           } 
/*     */           
/* 152 */           return (List<TriggerInfo>)dataAsList.stream()
/* 153 */             .map(BuilderFactory.STRING::build)
/* 154 */             .map(name -> new TriggerInfo(name, null, null, 0L, 0L, 0L, 0L, 0L, 0L))
/* 155 */             .collect(Collectors.toList());
/*     */         } 
/*     */         
/* 158 */         return Collections.emptyList();
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\resps\TriggerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */