/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.gears.resps;
/*     */ 
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
/*     */ public class GearsLibraryInfo
/*     */ {
/*     */   private final String apiVersion;
/*     */   private final List<String> clusterFunctions;
/*     */   private final String code;
/*     */   private final String configuration;
/*     */   private final String engine;
/*     */   private final List<FunctionInfo> functions;
/*     */   private final List<TriggerInfo> keyspaceTriggers;
/*     */   private final String name;
/*     */   private final List<String> pendingAsyncCalls;
/*     */   private final long pendingJobs;
/*     */   private final List<StreamTriggerInfo> streamTriggers;
/*     */   private final String user;
/*     */   
/*     */   public GearsLibraryInfo(String apiVersion, List<String> clusterFunctions, String code, String configuration, String engine, List<FunctionInfo> functions, List<TriggerInfo> keyspaceTriggers, String name, List<String> pendingAsyncCalls, long pendingJobs, List<StreamTriggerInfo> streamTriggers, String user) {
/*  32 */     this.apiVersion = apiVersion;
/*  33 */     this.clusterFunctions = clusterFunctions;
/*  34 */     this.code = code;
/*  35 */     this.configuration = configuration;
/*  36 */     this.engine = engine;
/*  37 */     this.functions = functions;
/*  38 */     this.keyspaceTriggers = keyspaceTriggers;
/*  39 */     this.name = name;
/*  40 */     this.pendingAsyncCalls = pendingAsyncCalls;
/*  41 */     this.pendingJobs = pendingJobs;
/*  42 */     this.streamTriggers = streamTriggers;
/*  43 */     this.user = user;
/*     */   }
/*     */   public String getApiVersion() {
/*  46 */     return this.apiVersion;
/*     */   }
/*     */   
/*     */   public List<String> getClusterFunctions() {
/*  50 */     return this.clusterFunctions;
/*     */   }
/*     */   
/*     */   public String getCode() {
/*  54 */     return this.code;
/*     */   }
/*     */   
/*     */   public String getConfiguration() {
/*  58 */     return this.configuration;
/*     */   }
/*     */   
/*     */   public String getEngine() {
/*  62 */     return this.engine;
/*     */   }
/*     */   
/*     */   public List<FunctionInfo> getFunctions() {
/*  66 */     return this.functions;
/*     */   }
/*     */   
/*     */   public List<TriggerInfo> getKeyspaceTriggers() {
/*  70 */     return this.keyspaceTriggers;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  74 */     return this.name;
/*     */   }
/*     */   
/*     */   public List<String> getPendingAsyncCalls() {
/*  78 */     return this.pendingAsyncCalls;
/*     */   }
/*     */   
/*     */   public long getPendingJobs() {
/*  82 */     return this.pendingJobs;
/*     */   }
/*     */   
/*     */   public List<StreamTriggerInfo> getStreamTriggers() {
/*  86 */     return this.streamTriggers;
/*     */   }
/*     */   
/*     */   public String getUser() {
/*  90 */     return this.user;
/*     */   }
/*     */   
/*  93 */   public static final Builder<GearsLibraryInfo> GEARS_LIBRARY_INFO = new Builder<GearsLibraryInfo>()
/*     */     {
/*     */       public GearsLibraryInfo build(Object data) {
/*  96 */         if (data == null) return null; 
/*  97 */         List list = (List)data;
/*  98 */         if (list.isEmpty()) return null;
/*     */         
/* 100 */         String apiVersion = null;
/* 101 */         List<String> clusterFunctions = Collections.emptyList();
/* 102 */         String code = null;
/* 103 */         String configuration = null;
/* 104 */         String engine = null;
/* 105 */         List<FunctionInfo> functions = Collections.emptyList();
/* 106 */         List<TriggerInfo> keyspaceTriggers = Collections.emptyList();
/* 107 */         String name = null;
/* 108 */         List<String> pendingAsyncCalls = null;
/* 109 */         long pendingJobs = 0L;
/* 110 */         List<StreamTriggerInfo> streamTriggers = Collections.emptyList();
/* 111 */         String user = null;
/*     */         
/* 113 */         if (list.get(0) instanceof KeyValue) {
/* 114 */           for (KeyValue kv : list) {
/* 115 */             switch ((String)BuilderFactory.STRING.build(kv.getKey())) {
/*     */               case "api_version":
/* 117 */                 apiVersion = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */               
/*     */               case "cluster_functions":
/* 120 */                 clusterFunctions = (List<String>)BuilderFactory.STRING_LIST.build(kv.getValue());
/*     */               
/*     */               case "configuration":
/* 123 */                 configuration = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */               
/*     */               case "engine":
/* 126 */                 engine = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */               
/*     */               case "functions":
/* 129 */                 functions = (List<FunctionInfo>)FunctionInfo.FUNCTION_INFO_LIST.build(kv.getValue());
/*     */               
/*     */               case "keyspace_triggers":
/* 132 */                 keyspaceTriggers = (List<TriggerInfo>)TriggerInfo.KEYSPACE_TRIGGER_INFO_LIST.build(kv.getValue());
/*     */               
/*     */               case "name":
/* 135 */                 name = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */               
/*     */               case "pending_async_calls":
/* 138 */                 pendingAsyncCalls = (List<String>)BuilderFactory.STRING_LIST.build(kv.getValue());
/*     */               
/*     */               case "pending_jobs":
/* 141 */                 pendingJobs = ((Long)BuilderFactory.LONG.build(kv.getValue())).longValue();
/*     */               
/*     */               case "stream_triggers":
/* 144 */                 streamTriggers = (List<StreamTriggerInfo>)StreamTriggerInfo.STREAM_TRIGGER_INFO_LIST.build(kv.getValue());
/*     */               
/*     */               case "user":
/* 147 */                 user = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */               
/*     */               case "code":
/* 150 */                 code = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */             } 
/*     */           
/*     */           } 
/*     */         } else {
/* 155 */           boolean withCode = (list.size() > 23);
/* 156 */           int offset = withCode ? 2 : 0;
/* 157 */           apiVersion = (String)BuilderFactory.STRING.build(list.get(1));
/* 158 */           clusterFunctions = (List<String>)BuilderFactory.STRING_LIST.build(list.get(3));
/* 159 */           code = withCode ? (String)BuilderFactory.STRING.build(list.get(5)) : null;
/* 160 */           configuration = (String)BuilderFactory.STRING.build(list.get(5 + offset));
/* 161 */           engine = (String)BuilderFactory.STRING.build(list.get(7 + offset));
/* 162 */           functions = (List<FunctionInfo>)FunctionInfo.FUNCTION_INFO_LIST.build(list.get(9 + offset));
/* 163 */           keyspaceTriggers = (List<TriggerInfo>)TriggerInfo.KEYSPACE_TRIGGER_INFO_LIST.build(list.get(11 + offset));
/* 164 */           name = (String)BuilderFactory.STRING.build(list.get(13 + offset));
/* 165 */           pendingAsyncCalls = (List<String>)BuilderFactory.STRING_LIST.build(list.get(15 + offset));
/* 166 */           pendingJobs = ((Long)BuilderFactory.LONG.build(list.get(17 + offset))).longValue();
/* 167 */           streamTriggers = (List<StreamTriggerInfo>)StreamTriggerInfo.STREAM_TRIGGER_INFO_LIST.build(list.get(19 + offset));
/* 168 */           user = (String)BuilderFactory.STRING.build(list.get(21 + offset));
/*     */         } 
/* 170 */         return new GearsLibraryInfo(apiVersion, clusterFunctions, code, configuration, engine, functions, keyspaceTriggers, name, pendingAsyncCalls, pendingJobs, streamTriggers, user);
/*     */       }
/*     */     };
/*     */   
/* 174 */   public static final Builder<List<GearsLibraryInfo>> GEARS_LIBRARY_INFO_LIST = new Builder<List<GearsLibraryInfo>>()
/*     */     {
/*     */       public List<GearsLibraryInfo> build(Object data) {
/* 177 */         List<Object> list = (List<Object>)data;
/* 178 */         return (List<GearsLibraryInfo>)list.stream().map(o -> (GearsLibraryInfo)GearsLibraryInfo.GEARS_LIBRARY_INFO.build(o)).collect(Collectors.toList());
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\resps\GearsLibraryInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */