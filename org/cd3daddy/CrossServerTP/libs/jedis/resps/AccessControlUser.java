/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.StringJoiner;
/*     */ 
/*     */ 
/*     */ public class AccessControlUser
/*     */ {
/*     */   private final Map<String, Object> userInfo;
/*     */   private final List<String> flags;
/*     */   private final List<String> passwords;
/*     */   private final String commands;
/*     */   private final List<String> keysList;
/*     */   private final String keys;
/*     */   private final List<String> channelsList;
/*     */   private final String channels;
/*     */   private final List<String> selectors;
/*     */   
/*     */   public AccessControlUser(Map<String, Object> map) {
/*  22 */     this.userInfo = map;
/*     */     
/*  24 */     this.flags = (List<String>)map.get("flags");
/*     */     
/*  26 */     this.passwords = (List<String>)map.get("passwords");
/*     */     
/*  28 */     this.commands = (String)map.get("commands");
/*     */     
/*  30 */     Object localKeys = map.get("keys");
/*  31 */     if (localKeys == null) {
/*  32 */       this.keys = null;
/*  33 */       this.keysList = null;
/*  34 */     } else if (localKeys instanceof List) {
/*  35 */       this.keysList = (List<String>)localKeys;
/*  36 */       this.keys = joinStrings(this.keysList);
/*     */     } else {
/*  38 */       this.keys = (String)localKeys;
/*  39 */       this.keysList = Arrays.asList(this.keys.split(" "));
/*     */     } 
/*     */     
/*  42 */     Object localChannels = map.get("channels");
/*  43 */     if (localChannels == null) {
/*  44 */       this.channels = null;
/*  45 */       this.channelsList = null;
/*  46 */     } else if (localChannels instanceof List) {
/*  47 */       this.channelsList = (List<String>)localChannels;
/*  48 */       this.channels = joinStrings(this.channelsList);
/*     */     } else {
/*  50 */       this.channels = (String)localChannels;
/*  51 */       this.channelsList = Arrays.asList(this.channels.split(" "));
/*     */     } 
/*     */     
/*  54 */     this.selectors = (List<String>)map.get("selectors");
/*     */   }
/*     */   
/*     */   private static String joinStrings(List<String> list) {
/*  58 */     StringJoiner joiner = new StringJoiner(" ");
/*  59 */     list.forEach(s -> joiner.add(s));
/*  60 */     return joiner.toString();
/*     */   }
/*     */   
/*     */   public List<String> getFlags() {
/*  64 */     return this.flags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public List<String> getPassword() {
/*  72 */     return this.passwords;
/*     */   }
/*     */   
/*     */   public List<String> getPasswords() {
/*  76 */     return this.passwords;
/*     */   }
/*     */   
/*     */   public String getCommands() {
/*  80 */     return this.commands;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Object> getUserInfo() {
/*  87 */     return this.userInfo;
/*     */   }
/*     */   
/*     */   public String getKeys() {
/*  91 */     return this.keys;
/*     */   }
/*     */   
/*     */   public List<String> getKeysList() {
/*  95 */     return this.keysList;
/*     */   }
/*     */   
/*     */   public List<String> getChannelsList() {
/*  99 */     return this.channelsList;
/*     */   }
/*     */   
/*     */   public String getChannels() {
/* 103 */     return this.channels;
/*     */   }
/*     */   
/*     */   public List<String> getSelectors() {
/* 107 */     return this.selectors;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return "AccessControlUser{flags=" + this.flags + ", passwords=" + this.passwords + ", commands='" + this.commands + "', keys='" + this.keys + "', channels='" + this.channels + "', selectors=" + this.selectors + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\AccessControlUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */