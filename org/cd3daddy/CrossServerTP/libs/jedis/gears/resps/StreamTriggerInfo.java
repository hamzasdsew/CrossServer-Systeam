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
/*     */ public class StreamTriggerInfo
/*     */ {
/*     */   private final String name;
/*     */   private final String description;
/*     */   private final String prefix;
/*     */   private final boolean trim;
/*     */   private final long window;
/*     */   private final List<FunctionStreamInfo> streams;
/*     */   
/*     */   public String getName() {
/*  23 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getDescription() {
/*  27 */     return this.description;
/*     */   }
/*     */   
/*     */   public String getPrefix() {
/*  31 */     return this.prefix;
/*     */   }
/*     */   public boolean isTrim() {
/*  34 */     return this.trim;
/*     */   }
/*     */   
/*     */   public long getWindow() {
/*  38 */     return this.window;
/*     */   }
/*     */   
/*     */   public List<FunctionStreamInfo> getStreams() {
/*  42 */     return this.streams;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTriggerInfo(String name, String description, String prefix, long window, boolean trim, List<FunctionStreamInfo> streams) {
/*  47 */     this.name = name;
/*  48 */     this.description = description;
/*  49 */     this.prefix = prefix;
/*  50 */     this.window = window;
/*  51 */     this.trim = trim;
/*  52 */     this.streams = streams;
/*     */   }
/*     */   public StreamTriggerInfo(String name) {
/*  55 */     this(name, null, null, 0L, false, Collections.emptyList());
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTriggerInfo(String name, String description, String prefix, long window, boolean trim) {
/*  60 */     this(name, description, prefix, window, trim, Collections.emptyList());
/*     */   }
/*     */   
/*  63 */   public static final Builder<List<StreamTriggerInfo>> STREAM_TRIGGER_INFO_LIST = new Builder<List<StreamTriggerInfo>>()
/*     */     {
/*     */       public List<StreamTriggerInfo> build(Object data) {
/*  66 */         List<Object> dataAsList = (List<Object>)data;
/*  67 */         if (!dataAsList.isEmpty()) {
/*  68 */           boolean isListOfList = dataAsList.get(0).getClass().isAssignableFrom(ArrayList.class);
/*  69 */           if (isListOfList) {
/*  70 */             if (((List)((List)data).get(0)).get(0) instanceof KeyValue) {
/*  71 */               List<List<KeyValue>> dataAsKeyValues = (List<List<KeyValue>>)data;
/*  72 */               return (List<StreamTriggerInfo>)dataAsKeyValues.stream().map(keyValues -> {
/*     */                     String name = null;
/*     */                     String description = null;
/*     */                     String prefix = null;
/*     */                     long window = 0L;
/*     */                     boolean trim = false;
/*     */                     List<FunctionStreamInfo> streams = null;
/*     */                     for (KeyValue kv : keyValues) {
/*     */                       switch ((String)BuilderFactory.STRING.build(kv.getKey())) {
/*     */                         case "name":
/*     */                           name = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */ 
/*     */                         
/*     */                         case "description":
/*     */                           description = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */                         
/*     */                         case "prefix":
/*     */                           prefix = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */                         
/*     */                         case "window":
/*     */                           window = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */                         
/*     */                         case "trim":
/*     */                           trim = ((Boolean)BuilderFactory.BOOLEAN.build(kv.getValue())).booleanValue();
/*     */                         
/*     */                         case "streams":
/*     */                           streams = (List<FunctionStreamInfo>)FunctionStreamInfo.STREAM_INFO_LIST.build(kv.getValue());
/*     */                       } 
/*     */                     
/*     */                     } 
/*     */                     return new StreamTriggerInfo(name, description, prefix, window, trim, streams);
/* 103 */                   }).collect(Collectors.toList());
/*     */             } 
/* 105 */             return (List<StreamTriggerInfo>)dataAsList.stream().map(pairObject -> (List)pairObject).map(pairList -> {
/*     */                   StreamTriggerInfo result = null;
/*     */                   switch (pairList.size()) {
/*     */                     case 1:
/*     */                       result = new StreamTriggerInfo((String)BuilderFactory.STRING.build(pairList.get(0)));
/*     */                       break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/*     */                     case 10:
/*     */                       result = new StreamTriggerInfo((String)BuilderFactory.STRING.build(pairList.get(3)), (String)BuilderFactory.STRING.build(pairList.get(1)), (String)BuilderFactory.STRING.build(pairList.get(5)), ((Long)BuilderFactory.LONG.build(pairList.get(9))).longValue(), ((Boolean)BuilderFactory.BOOLEAN.build(pairList.get(7))).booleanValue());
/*     */                       break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/*     */                     case 12:
/*     */                       result = new StreamTriggerInfo((String)BuilderFactory.STRING.build(pairList.get(3)), (String)BuilderFactory.STRING.build(pairList.get(1)), (String)BuilderFactory.STRING.build(pairList.get(5)), ((Long)BuilderFactory.LONG.build(pairList.get(11))).longValue(), ((Boolean)BuilderFactory.BOOLEAN.build(pairList.get(9))).booleanValue(), (List<FunctionStreamInfo>)FunctionStreamInfo.STREAM_INFO_LIST.build(pairList.get(7)));
/*     */                       break;
/*     */                   } 
/*     */ 
/*     */ 
/*     */ 
/*     */                   
/*     */                   return result;
/* 133 */                 }).collect(Collectors.toList());
/*     */           } 
/*     */           
/* 136 */           return (List<StreamTriggerInfo>)dataAsList.stream()
/* 137 */             .map(BuilderFactory.STRING::build).map(name -> new StreamTriggerInfo(name, null, null, 0L, false))
/* 138 */             .collect(Collectors.toList());
/*     */         } 
/*     */         
/* 141 */         return Collections.emptyList();
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\resps\StreamTriggerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */