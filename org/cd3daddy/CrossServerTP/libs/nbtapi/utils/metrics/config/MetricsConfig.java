/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.config;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MetricsConfig
/*     */ {
/*     */   private final File file;
/*     */   private final boolean defaultEnabled;
/*     */   private String serverUUID;
/*     */   private boolean enabled;
/*     */   private boolean logErrors;
/*     */   private boolean logSentData;
/*     */   private boolean logResponseStatusText;
/*     */   private boolean didExistBefore = true;
/*     */   
/*     */   public MetricsConfig(File file, boolean defaultEnabled) throws IOException {
/*  35 */     this.file = file;
/*  36 */     this.defaultEnabled = defaultEnabled;
/*     */     
/*  38 */     setupConfig();
/*     */   }
/*     */   
/*     */   public String getServerUUID() {
/*  42 */     return this.serverUUID;
/*     */   }
/*     */   
/*     */   public boolean isEnabled() {
/*  46 */     return this.enabled;
/*     */   }
/*     */   
/*     */   public boolean isLogErrorsEnabled() {
/*  50 */     return this.logErrors;
/*     */   }
/*     */   
/*     */   public boolean isLogSentDataEnabled() {
/*  54 */     return this.logSentData;
/*     */   }
/*     */   
/*     */   public boolean isLogResponseStatusTextEnabled() {
/*  58 */     return this.logResponseStatusText;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean didExistBefore() {
/*  67 */     return this.didExistBefore;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setupConfig() throws IOException {
/*  74 */     if (!this.file.exists()) {
/*  75 */       this.didExistBefore = false;
/*  76 */       writeConfig();
/*     */     } 
/*  78 */     readConfig();
/*  79 */     if (this.serverUUID == null) {
/*     */       
/*  81 */       writeConfig();
/*  82 */       readConfig();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeConfig() throws IOException {
/*  90 */     List<String> configContent = new ArrayList<>();
/*  91 */     configContent.add("# bStats (https://bStats.org) collects some basic information for plugin authors, like");
/*  92 */     configContent.add("# how many people use their plugin and their total player count. It's recommended to keep");
/*  93 */     configContent.add("# bStats enabled, but if you're not comfortable with this, you can turn this setting off.");
/*  94 */     configContent.add("# There is no performance penalty associated with having metrics enabled, and data sent to");
/*  95 */     configContent.add("# bStats is fully anonymous.");
/*  96 */     configContent.add("enabled=" + this.defaultEnabled);
/*  97 */     configContent.add("server-uuid=" + UUID.randomUUID().toString());
/*  98 */     configContent.add("log-errors=false");
/*  99 */     configContent.add("log-sent-data=false");
/* 100 */     configContent.add("log-response-status-text=false");
/* 101 */     writeFile(this.file, configContent);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readConfig() throws IOException {
/* 108 */     List<String> lines = readFile(this.file);
/* 109 */     if (lines == null) {
/* 110 */       throw new AssertionError("Content of newly created file is null");
/*     */     }
/*     */     
/* 113 */     this.enabled = ((Boolean)getConfigValue("enabled", lines).<Boolean>map("true"::equals).orElse(Boolean.valueOf(true))).booleanValue();
/* 114 */     this.serverUUID = getConfigValue("server-uuid", lines).orElse(null);
/* 115 */     this.logErrors = ((Boolean)getConfigValue("log-errors", lines).<Boolean>map("true"::equals).orElse(Boolean.valueOf(false))).booleanValue();
/* 116 */     this.logSentData = ((Boolean)getConfigValue("log-sent-data", lines).<Boolean>map("true"::equals).orElse(Boolean.valueOf(false))).booleanValue();
/* 117 */     this.logResponseStatusText = ((Boolean)getConfigValue("log-response-status-text", lines).<Boolean>map("true"::equals).orElse(Boolean.valueOf(false))).booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Optional<String> getConfigValue(String key, List<String> lines) {
/* 128 */     return lines.stream()
/* 129 */       .filter(line -> line.startsWith(key + "="))
/* 130 */       .map(line -> line.replaceFirst(Pattern.quote(key + "="), ""))
/* 131 */       .findFirst();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<String> readFile(File file) throws IOException {
/* 141 */     if (!file.exists()) {
/* 142 */       return null;
/*     */     }
/*     */     
/* 145 */     FileReader fileReader = new FileReader(file); 
/* 146 */     try { BufferedReader bufferedReader = new BufferedReader(fileReader);
/*     */       
/* 148 */       try { List<String> list = bufferedReader.lines().collect((Collector)Collectors.toList());
/* 149 */         bufferedReader.close(); fileReader.close(); return list; }
/*     */       catch (Throwable throwable) { try { bufferedReader.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */          throw throwable; }
/*     */        }
/*     */     catch (Throwable throwable) { try {
/*     */         fileReader.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       }  throw throwable; }
/* 159 */      } private void writeFile(File file, List<String> lines) throws IOException { if (!file.exists()) {
/* 160 */       file.getParentFile().mkdirs();
/* 161 */       file.createNewFile();
/*     */     } 
/*     */     
/* 164 */     FileWriter fileWriter = new FileWriter(file); try {
/* 165 */       BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
/*     */       
/* 167 */       try { for (String line : lines) {
/* 168 */           bufferedWriter.write(line);
/* 169 */           bufferedWriter.newLine();
/*     */         } 
/* 171 */         bufferedWriter.close(); } catch (Throwable throwable) { try { bufferedWriter.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  fileWriter.close();
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         fileWriter.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     }  }
/*     */ 
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\metrics\config\MetricsConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */