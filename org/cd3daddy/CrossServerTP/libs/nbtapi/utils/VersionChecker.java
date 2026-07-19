/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.logging.Level;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.cd3daddy.CrossServerTP.libs.gson.JsonArray;
/*     */ import org.cd3daddy.CrossServerTP.libs.gson.JsonElement;
/*     */ import org.cd3daddy.CrossServerTP.libs.gson.JsonObject;
/*     */ import org.cd3daddy.CrossServerTP.libs.gson.JsonParser;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VersionChecker
/*     */ {
/*     */   private static final String USER_AGENT = "nbt-api Version check";
/*     */   private static final String REQUEST_URL = "https://api.spiget.org/v2/resources/7939/versions?size=100";
/*     */   public static boolean hideOk = false;
/*     */   
/*     */   protected static void checkForUpdates() throws Exception {
/*  29 */     URL url = new URL("https://api.spiget.org/v2/resources/7939/versions?size=100");
/*  30 */     HttpURLConnection connection = (HttpURLConnection)url.openConnection();
/*  31 */     connection.addRequestProperty("User-Agent", "nbt-api Version check");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  37 */     InputStream inputStream = connection.getInputStream();
/*  38 */     InputStreamReader reader = new InputStreamReader(inputStream);
/*     */ 
/*     */     
/*  41 */     JsonElement element = (new JsonParser()).parse(reader);
/*  42 */     if (element.isJsonArray()) {
/*     */       
/*  44 */       JsonArray updates = (JsonArray)element;
/*  45 */       JsonObject latest = (JsonObject)updates.get(updates.size() - 1);
/*  46 */       int versionDifference = getVersionDifference(latest.get("name").getAsString());
/*  47 */       if (versionDifference == -1) {
/*  48 */         MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] The NBT-API in '" + 
/*  49 */             getPlugin() + "' seems to be outdated!");
/*  50 */         MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Current Version: '2.15.3' Newest Version: " + latest
/*  51 */             .get("name").getAsString() + "'");
/*  52 */         MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Please update the NBTAPI or the plugin that contains the api(nag the mod author when the newest release has an old version, not the NBTAPI dev)!");
/*     */       
/*     */       }
/*  55 */       else if (versionDifference == 0) {
/*  56 */         if (!hideOk)
/*  57 */           MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] The NBT-API seems to be up-to-date!"); 
/*  58 */       } else if (versionDifference == 1) {
/*  59 */         MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] The NBT-API in '" + getPlugin() + "' seems to be a future Version, not yet released on Spigot/CurseForge! This is not an error!");
/*     */         
/*  61 */         MinecraftVersion.getLogger().log(Level.INFO, "[NBTAPI] Current Version: '2.15.3' Newest Version: " + latest
/*  62 */             .get("name").getAsString() + "'");
/*     */       } 
/*     */     } else {
/*     */       
/*  66 */       MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error when looking for Updates! Got non Json Array: '" + element
/*  67 */           .toString() + "'");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getVersionDifference(String version) {
/*  76 */     String current = "2.15.3";
/*  77 */     if (current.equals(version))
/*  78 */       return 0; 
/*  79 */     String pattern = "\\.";
/*  80 */     if ((current.split(pattern)).length != 3 || (version.split(pattern)).length != 3)
/*  81 */       return -1; 
/*  82 */     int curMaj = Integer.parseInt(current.split(pattern)[0]);
/*  83 */     int curMin = Integer.parseInt(current.split(pattern)[1]);
/*  84 */     String curPatch = current.split(pattern)[2];
/*  85 */     int relMaj = Integer.parseInt(version.split(pattern)[0]);
/*  86 */     int relMin = Integer.parseInt(version.split(pattern)[1]);
/*  87 */     String relPatch = version.split(pattern)[2];
/*  88 */     if (curMaj < relMaj)
/*  89 */       return -1; 
/*  90 */     if (curMaj > relMaj)
/*  91 */       return 1; 
/*  92 */     if (curMin < relMin)
/*  93 */       return -1; 
/*  94 */     if (curMin > relMin)
/*  95 */       return 1; 
/*  96 */     int curPatchN = Integer.parseInt(curPatch.split("-")[0]);
/*  97 */     int relPatchN = Integer.parseInt(relPatch.split("-")[0]);
/*  98 */     if (curPatchN < relPatchN)
/*  99 */       return -1; 
/* 100 */     if (curPatchN > relPatchN)
/* 101 */       return 1; 
/* 102 */     if (!relPatch.contains("-") && curPatch.contains("-")) {
/* 103 */       return -1;
/*     */     }
/* 105 */     if (relPatch.contains("-") && curPatch.contains("-"))
/* 106 */       return 0; 
/* 107 */     return 1;
/*     */   }
/*     */   
/*     */   protected static String getPlugin() {
/* 111 */     ClassLoader classLoader = VersionChecker.class.getClassLoader();
/* 112 */     InputStream inputStream = classLoader.getResourceAsStream("paper-plugin.yml");
/* 113 */     if (inputStream != null) {
/* 114 */       try { InputStreamReader reader = new InputStreamReader(inputStream); 
/* 115 */         try { YamlConfiguration pluginYml = YamlConfiguration.loadConfiguration(reader);
/* 116 */           String str = pluginYml.getString("name");
/* 117 */           reader.close(); return str; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/*     */       {  }
/* 119 */       catch (IllegalArgumentException e)
/*     */       
/* 121 */       { MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading paper-plugin.yml: " + e.getMessage()); }
/*     */     
/*     */     }
/*     */     
/* 125 */     inputStream = classLoader.getResourceAsStream("plugin.yml");
/*     */     
/* 127 */     if (inputStream != null) {
/* 128 */       try { InputStreamReader reader = new InputStreamReader(inputStream); 
/* 129 */         try { YamlConfiguration pluginYml = YamlConfiguration.loadConfiguration(reader);
/* 130 */           String str = pluginYml.getString("name");
/* 131 */           reader.close(); return str; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/*     */       {  }
/* 133 */       catch (IllegalArgumentException e)
/*     */       
/* 135 */       { MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading plugin.yml: " + e.getMessage()); }
/*     */     
/*     */     }
/* 138 */     return NBTItem.class.getPackage().getName();
/*     */   }
/*     */   
/*     */   protected static String getPluginforBStats() {
/* 142 */     ClassLoader classLoader = VersionChecker.class.getClassLoader();
/* 143 */     InputStream inputStream = classLoader.getResourceAsStream("paper-plugin.yml");
/* 144 */     if (inputStream != null) {
/* 145 */       try { InputStreamReader reader = new InputStreamReader(inputStream); 
/* 146 */         try { YamlConfiguration pluginYml = YamlConfiguration.loadConfiguration(reader);
/* 147 */           String str = pluginYml.getString("name");
/* 148 */           reader.close(); return str; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/*     */       {  }
/* 150 */       catch (IllegalArgumentException e)
/*     */       
/* 152 */       { MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading paper-plugin.yml: " + e.getMessage()); }
/*     */     
/*     */     }
/*     */     
/* 156 */     inputStream = classLoader.getResourceAsStream("plugin.yml");
/*     */     
/* 158 */     if (inputStream != null) {
/* 159 */       try { InputStreamReader reader = new InputStreamReader(inputStream); 
/* 160 */         try { YamlConfiguration pluginYml = YamlConfiguration.loadConfiguration(reader);
/* 161 */           String str = pluginYml.getString("name");
/* 162 */           reader.close(); return str; } catch (Throwable throwable) { try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException iOException)
/*     */       {  }
/* 164 */       catch (IllegalArgumentException e)
/*     */       
/* 166 */       { MinecraftVersion.getLogger().log(Level.WARNING, "[NBTAPI] Error reading plugin.yml: " + e.getMessage()); }
/*     */     
/*     */     }
/* 169 */     return "UnknownPlugin";
/*     */   }
/*     */   
/*     */   protected static String getPluginType() {
/* 173 */     ClassLoader classLoader = VersionChecker.class.getClassLoader();
/* 174 */     InputStream inputStream = classLoader.getResourceAsStream("paper-plugin.yml");
/* 175 */     if (inputStream != null) {
/*     */       try {
/* 177 */         inputStream.close();
/* 178 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 181 */       return "PaperPlugin";
/*     */     } 
/*     */     
/* 184 */     inputStream = classLoader.getResourceAsStream("plugin.yml");
/* 185 */     if (inputStream != null) {
/*     */       try {
/* 187 */         inputStream.close();
/* 188 */       } catch (IOException iOException) {}
/*     */ 
/*     */       
/* 191 */       return "SpigotPlugin";
/*     */     } 
/* 193 */     return "UnknownPlugin";
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\VersionChecker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */