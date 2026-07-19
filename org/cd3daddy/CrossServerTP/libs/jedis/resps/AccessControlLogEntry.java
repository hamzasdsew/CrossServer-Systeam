/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AccessControlLogEntry
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final String COUNT = "count";
/*     */   public static final String REASON = "reason";
/*     */   public static final String CONTEXT = "context";
/*     */   public static final String OBJECT = "object";
/*     */   public static final String USERNAME = "username";
/*     */   public static final String AGE_SECONDS = "age-seconds";
/*     */   public static final String CLIENT_INFO = "client-info";
/*     */   public static final String ENTRY_ID = "entry-id";
/*     */   public static final String TIMESTAMP_CREATED = "timestamp-created";
/*     */   public static final String TIMESTAMP_LAST_UPDATED = "timestamp-last-updated";
/*     */   private final long count;
/*     */   private final String reason;
/*     */   private final String context;
/*     */   private final String object;
/*     */   private final String username;
/*     */   private final Double ageSeconds;
/*     */   private final Map<String, String> clientInfo;
/*     */   private final Map<String, Object> logEntry;
/*     */   private final long entryId;
/*     */   private final long timestampCreated;
/*     */   private final long timestampLastUpdated;
/*     */   
/*     */   public AccessControlLogEntry(Map<String, Object> map) {
/*  42 */     this.count = ((Long)map.get("count")).longValue();
/*  43 */     this.reason = (String)map.get("reason");
/*  44 */     this.context = (String)map.get("context");
/*  45 */     this.object = (String)map.get("object");
/*  46 */     this.username = (String)map.get("username");
/*  47 */     this.ageSeconds = (Double)map.get("age-seconds");
/*  48 */     this.clientInfo = getMapFromRawClientInfo((String)map.get("client-info"));
/*  49 */     this.logEntry = map;
/*  50 */     this.entryId = ((Long)map.get("entry-id")).longValue();
/*  51 */     this.timestampCreated = ((Long)map.get("timestamp-created")).longValue();
/*  52 */     this.timestampLastUpdated = ((Long)map.get("timestamp-last-updated")).longValue();
/*     */   }
/*     */   
/*     */   public long getCount() {
/*  56 */     return this.count;
/*     */   }
/*     */   
/*     */   public String getReason() {
/*  60 */     return this.reason;
/*     */   }
/*     */   
/*     */   public String getContext() {
/*  64 */     return this.context;
/*     */   }
/*     */   
/*     */   public String getObject() {
/*  68 */     return this.object;
/*     */   }
/*     */   
/*     */   public String getUsername() {
/*  72 */     return this.username;
/*     */   }
/*     */   
/*     */   public Double getAgeSeconds() {
/*  76 */     return this.ageSeconds;
/*     */   }
/*     */   
/*     */   public Map<String, String> getClientInfo() {
/*  80 */     return this.clientInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getlogEntry() {
/*  87 */     return this.logEntry;
/*     */   }
/*     */   
/*     */   public long getEntryId() {
/*  91 */     return this.entryId;
/*     */   }
/*     */   
/*     */   public long getTimestampCreated() {
/*  95 */     return this.timestampCreated;
/*     */   }
/*     */   
/*     */   public long getTimestampLastUpdated() {
/*  99 */     return this.timestampLastUpdated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, String> getMapFromRawClientInfo(String clientInfo) {
/* 109 */     String[] entries = clientInfo.split(" ");
/* 110 */     Map<String, String> clientInfoMap = new LinkedHashMap<>(entries.length);
/* 111 */     for (String entry : entries) {
/* 112 */       String[] kvArray = entry.split("=");
/* 113 */       clientInfoMap.put(kvArray[0], (kvArray.length == 2) ? kvArray[1] : "");
/*     */     } 
/* 115 */     return clientInfoMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 120 */     return "AccessControlLogEntry{count=" + this.count + ", reason='" + this.reason + '\'' + ", context='" + this.context + '\'' + ", object='" + this.object + '\'' + ", username='" + this.username + '\'' + ", ageSeconds='" + this.ageSeconds + '\'' + ", clientInfo=" + this.clientInfo + ", entryId=" + this.entryId + ", timestampCreated=" + this.timestampCreated + ", timestampLastUpdated=" + this.timestampLastUpdated + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\AccessControlLogEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */