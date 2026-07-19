/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.bukkit.Metrics;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.charts.CustomChart;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.charts.DrilldownPie;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.charts.SimplePie;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
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
/*     */ public enum MinecraftVersion
/*     */ {
/*  28 */   UNKNOWN(2147483647),
/*  29 */   MC1_7_R4(174), MC1_8_R3(183), MC1_9_R1(191), MC1_9_R2(192), MC1_10_R1(1101), MC1_11_R1(1111), MC1_12_R1(1121),
/*  30 */   MC1_13_R1(1131), MC1_13_R2(1132), MC1_14_R1(1141), MC1_15_R1(1151), MC1_16_R1(1161), MC1_16_R2(1162),
/*  31 */   MC1_16_R3(1163), MC1_17_R1(1171), MC1_18_R1(1181, true), MC1_18_R2(1182, true), MC1_19_R1(1191, true),
/*  32 */   MC1_19_R2(1192, true), MC1_19_R3(1193, true), MC1_20_R1(1201, true), MC1_20_R2(1202, true), MC1_20_R3(1203, true),
/*  33 */   MC1_20_R4(1204, true), MC1_21_R1(1211, true), MC1_21_R2(1212, true), MC1_21_R3(1213, true), MC1_21_R4(1214, true),
/*  34 */   MC1_21_R5(1215, true), MC1_21_R6(1216, true);
/*     */   
/*     */   private static MinecraftVersion version;
/*     */   
/*     */   private static Boolean hasGsonSupport;
/*     */   private static Boolean isForgePresent;
/*     */   
/*     */   static {
/*  42 */     bStatsDisabled = false;
/*  43 */     disablePackageWarning = false;
/*  44 */     updateCheckDisabled = true;
/*     */ 
/*     */ 
/*     */     
/*  48 */     logger = Logger.getLogger("NBTAPI");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  58 */     VERSION_TO_REVISION = new HashMap<String, MinecraftVersion>()
/*     */       {
/*     */       
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   private static Boolean isNeoForgePresent;
/*     */   
/*     */   private static Boolean isFabricPresent;
/*     */   
/*     */   private static Boolean isFoliaPresent;
/*     */   
/*     */   private static boolean bStatsDisabled;
/*     */   
/*     */   private static boolean disablePackageWarning;
/*     */   
/*     */   private static boolean updateCheckDisabled;
/*     */   
/*     */   private static Logger logger;
/*     */   
/*     */   protected static final String VERSION = "2.15.3";
/*     */   
/*     */   private final int versionId;
/*     */   private final boolean mojangMapping;
/*     */   private static final Map<String, MinecraftVersion> VERSION_TO_REVISION;
/*     */   
/*     */   MinecraftVersion(int versionId, boolean mojangMapping) {
/*  86 */     this.versionId = versionId;
/*  87 */     this.mojangMapping = mojangMapping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getVersionId() {
/*  94 */     return this.versionId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMojangMapping() {
/* 102 */     return this.mojangMapping;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPackageName() {
/* 112 */     if (this == UNKNOWN) {
/*     */       try {
/* 114 */         return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
/* 115 */       } catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */     
/* 119 */     return name().replace("MC", "v");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAtLeastVersion(MinecraftVersion version) {
/* 129 */     return (getVersion().getVersionId() >= version.getVersionId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNewerThan(MinecraftVersion version) {
/* 139 */     return (getVersion().getVersionId() > version.getVersionId());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static MinecraftVersion getVersion() {
/* 149 */     if (version != null) {
/* 150 */       return version;
/*     */     }
/*     */     try {
/* 153 */       String ver = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
/* 154 */       logger.info("[NBTAPI] Found Minecraft: " + ver + "! Trying to find NMS support");
/* 155 */       version = valueOf(ver.replace("v", "MC"));
/* 156 */     } catch (Exception ex) {
/* 157 */       logger.info("[NBTAPI] Found Minecraft: " + Bukkit.getServer().getBukkitVersion().split("-")[0] + "! Trying to find NMS support");
/*     */       
/* 159 */       version = VERSION_TO_REVISION.getOrDefault(Bukkit.getServer().getBukkitVersion().split("-")[0], UNKNOWN);
/*     */     } 
/*     */     
/* 162 */     if (version != UNKNOWN) {
/* 163 */       logger.info("[NBTAPI] NMS support '" + version.name() + "' loaded!");
/*     */     } else {
/* 165 */       logger.warning("[NBTAPI] This Server-Version(" + Bukkit.getServer().getBukkitVersion() + ") is not supported by this NBT-API Version(" + "2.15.3" + ") located in " + 
/*     */           
/* 167 */           VersionChecker.getPlugin() + ". The NBT-API will try to work as good as it can! Some functions may not work!");
/*     */     } 
/*     */     
/* 170 */     init();
/* 171 */     return version;
/*     */   }
/*     */   
/*     */   public static String getNBTAPIVersion() {
/* 175 */     return "2.15.3";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void init() {
/* 181 */     String defaultPackage = new String(new byte[] { 100, 101, 46, 116, 114, 55, 122, 119, 46, 99, 104, 97, 110, 103, 101, 109, 101, 46, 110, 98, 116, 97, 112, 105, 46, 117, 116, 105, 108, 115 });
/*     */     
/* 183 */     String reservedPackage = new String(new byte[] { 100, 101, 46, 116, 114, 55, 122, 119, 46, 110, 98, 116, 97, 112, 105, 46, 117, 116, 105, 108, 115 });
/*     */     
/*     */     try {
/* 186 */       if (hasGsonSupport() && !bStatsDisabled) {
/* 187 */         Plugin plugin = Bukkit.getPluginManager().getPlugin(VersionChecker.getPlugin());
/* 188 */         if (plugin != null && plugin instanceof org.bukkit.plugin.java.JavaPlugin) {
/* 189 */           getLogger()
/* 190 */             .info("[NBTAPI] Using the plugin '" + plugin.getName() + "' to create a bStats instance!");
/* 191 */           Metrics metrics = new Metrics(plugin, 1058);
/* 192 */           metrics.addCustomChart((CustomChart)new SimplePie("nbtapi_version", () -> "2.15.3"));
/*     */ 
/*     */           
/* 195 */           metrics.addCustomChart((CustomChart)new DrilldownPie("nms_version", () -> {
/*     */                   Map<String, Map<String, Integer>> map = new HashMap<>();
/*     */                   Map<String, Integer> entry = new HashMap<>();
/*     */                   entry.put(Bukkit.getName(), Integer.valueOf(1));
/*     */                   map.put(getVersion().name(), entry);
/*     */                   return map;
/*     */                 }));
/* 202 */           metrics.addCustomChart((CustomChart)new SimplePie("shaded", () -> Boolean.toString(!"NBTAPI".equals(VersionChecker.getPlugin()))));
/*     */ 
/*     */           
/* 205 */           metrics.addCustomChart((CustomChart)new SimplePie("server_software", () -> Bukkit.getName()));
/*     */ 
/*     */           
/* 208 */           metrics.addCustomChart((CustomChart)new SimplePie("parent_plugin", () -> VersionChecker.getPluginforBStats()));
/*     */ 
/*     */           
/* 211 */           metrics.addCustomChart((CustomChart)new SimplePie("parent_plugin_type", () -> VersionChecker.getPluginType()));
/*     */ 
/*     */           
/* 214 */           metrics.addCustomChart((CustomChart)new SimplePie("special_environment", () -> isFoliaPresent() ? "Folia" : (isForgePresent() ? "Forge" : (isFabricPresent() ? "Fabric" : (isNeoForgePresent() ? "NeoForge" : "None")))));
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
/* 227 */           metrics.addCustomChart((CustomChart)new SimplePie("bindings_check", () -> {
/*     */                   boolean failedBinding = false;
/*     */                   
/*     */                   for (ClassWrapper c : ClassWrapper.values()) {
/*     */                     if (c.isEnabled() && c.getClazz() == null) {
/*     */                       failedBinding = true;
/*     */                     }
/*     */                   } 
/*     */                   
/*     */                   for (ReflectionMethod method : ReflectionMethod.values()) {
/*     */                     if (method.isCompatible() && !method.isLoaded()) {
/*     */                       failedBinding = true;
/*     */                     }
/*     */                   } 
/*     */                   return failedBinding ? "Failed" : "Pass";
/*     */                 }));
/* 243 */         } else if (plugin == null) {
/* 244 */           getLogger().info("[NBTAPI] Unable to create a bStats instance!!");
/*     */         } 
/*     */       } 
/* 247 */     } catch (Exception ex) {
/* 248 */       logger.log(Level.WARNING, "[NBTAPI] Error enabling Metrics!", ex);
/*     */     } 
/*     */     
/* 251 */     if (hasGsonSupport() && !updateCheckDisabled)
/* 252 */       (new Thread(() -> {
/*     */             try {
/*     */               VersionChecker.checkForUpdates();
/* 255 */             } catch (Exception ex) {
/*     */               logger.log(Level.WARNING, "[NBTAPI] Error while checking for updates! Error: " + ex.getMessage());
/*     */             } 
/* 258 */           })).start(); 
/* 259 */     if (!disablePackageWarning && MinecraftVersion.class.getPackage().getName().equals(defaultPackage)) {
/* 260 */       logger.warning("#########################################- NBTAPI -#########################################");
/*     */       
/* 262 */       logger.warning("The NBT-API package has not been moved! This *will* cause problems with other plugins containing");
/*     */       
/* 264 */       logger.warning("a different version of the api! Please read the guide on the plugin page on how to get the");
/*     */       
/* 266 */       logger.warning("Maven Shade plugin to relocate the api to your personal location! If you are not the developer,");
/*     */       
/* 268 */       logger.warning("please check your plugins and contact their developer, so they can fix this issue.");
/* 269 */       logger.warning("#########################################- NBTAPI -#########################################");
/*     */     } 
/*     */     
/* 272 */     if (!disablePackageWarning && !"NBTAPI".equals(VersionChecker.getPlugin())) {
/*     */       
/* 274 */       if (!"de.tr7zw.nbtapi.utils".equals(reservedPackage)) {
/* 275 */         logger.warning("#########################################- NBTAPI -#########################################");
/*     */         
/* 277 */         logger.warning("The NBT-API inside " + 
/* 278 */             VersionChecker.getPlugin() + " is the plugin version, not the API!");
/* 279 */         logger.warning("The plugin itself should never be shaded! Remove the `-plugin` from the dependency and fix your shading setup.");
/*     */         
/* 281 */         logger.warning("For more info check: https://github.com/tr7zw/Item-NBT-API/wiki/Using-Maven#option-2-shading-the-nbt-api-into-your-plugin");
/*     */         
/* 283 */         logger.warning("#########################################- NBTAPI -#########################################");
/*     */         
/*     */         return;
/*     */       } 
/* 287 */       if (MinecraftVersion.class.getPackage().getName().equals("de.tr7zw.nbtapi.utils")) {
/* 288 */         logger.warning("#########################################- NBTAPI -#########################################");
/*     */         
/* 290 */         logger.warning("The NBT-API inside " + 
/* 291 */             VersionChecker.getPlugin() + " is located at 'de.tr7zw.nbtapi.utils'!");
/* 292 */         logger.warning("This package name is reserved for the official NBTAPI plugin, and not intended to be used for shading!");
/*     */         
/* 294 */         logger.warning("Please change the relocate to something else. For example: com.example.util.nbtapi");
/* 295 */         logger.warning("#########################################- NBTAPI -#########################################");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean hasGsonSupport() {
/* 305 */     if (hasGsonSupport != null) {
/* 306 */       return hasGsonSupport.booleanValue();
/*     */     }
/*     */     try {
/* 309 */       Class.forName("org.cd3daddy.CrossServerTP.libs.gson.Gson");
/* 310 */       hasGsonSupport = Boolean.valueOf(true);
/* 311 */     } catch (Exception ex) {
/* 312 */       logger.info("[NBTAPI] Gson not found! This will not allow the usage of some methods!");
/* 313 */       hasGsonSupport = Boolean.valueOf(false);
/*     */     } 
/* 315 */     return hasGsonSupport.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFabricPresent() {
/* 322 */     if (isFabricPresent != null) {
/* 323 */       return isFabricPresent.booleanValue();
/*     */     }
/*     */     try {
/* 326 */       logger.info("[NBTAPI] Found Fabric: " + Class.forName("net.fabricmc.api.ModInitializer"));
/* 327 */       isFabricPresent = Boolean.valueOf(true);
/* 328 */     } catch (Exception ex) {
/* 329 */       isFabricPresent = Boolean.valueOf(false);
/*     */     } 
/* 331 */     return isFabricPresent.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isForgePresent() {
/* 338 */     if (isForgePresent != null) {
/* 339 */       return isForgePresent.booleanValue();
/*     */     }
/*     */     try {
/* 342 */       logger.info("[NBTAPI] Found Forge: " + (
/* 343 */           (getVersion() == MC1_7_R4) ? (String)Class.forName("cpw.mods.fml.common.Loader") : 
/* 344 */           (String)Class.forName("net.minecraftforge.fml.common.Loader")));
/* 345 */       isForgePresent = Boolean.valueOf(true);
/* 346 */     } catch (Exception ex) {
/* 347 */       isForgePresent = Boolean.valueOf(false);
/*     */     } 
/* 349 */     return isForgePresent.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNeoForgePresent() {
/* 356 */     if (isNeoForgePresent != null) {
/* 357 */       return isNeoForgePresent.booleanValue();
/*     */     }
/*     */     try {
/* 360 */       logger.info("[NBTAPI] Found NeoForge: " + Class.forName("net.neoforged.neoforge.common.NeoForge"));
/* 361 */       isNeoForgePresent = Boolean.valueOf(true);
/* 362 */     } catch (Exception ex) {
/* 363 */       isNeoForgePresent = Boolean.valueOf(false);
/*     */     } 
/* 365 */     return isNeoForgePresent.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isFoliaPresent() {
/* 372 */     if (isFoliaPresent != null) {
/* 373 */       return isFoliaPresent.booleanValue();
/*     */     }
/*     */     try {
/* 376 */       logger.info("[NBTAPI] Found Folia: " + Class.forName("io.papermc.paper.threadedregions.RegionizedServer"));
/* 377 */       isFoliaPresent = Boolean.valueOf(true);
/* 378 */     } catch (Exception ex) {
/* 379 */       isFoliaPresent = Boolean.valueOf(false);
/*     */     } 
/* 381 */     return isFoliaPresent.booleanValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void disableBStats() {
/* 390 */     bStatsDisabled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void disableUpdateCheck() {
/* 398 */     updateCheckDisabled = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void enableUpdateCheck() {
/* 406 */     updateCheckDisabled = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void disablePackageWarning() {
/* 416 */     disablePackageWarning = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger() {
/* 423 */     return logger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void replaceLogger(Logger logger) {
/* 432 */     if (logger == null)
/* 433 */       throw new NullPointerException("Logger can not be null!"); 
/* 434 */     MinecraftVersion.logger = logger;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\MinecraftVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */