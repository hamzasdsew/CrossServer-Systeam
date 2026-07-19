/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
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
/*     */ public class MojangToMapping
/*     */ {
/*  19 */   private static Map<String, String> MC1_18R1 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
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
/*  83 */   private static Map<String, String> MC1_18R2 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  93 */   private static Map<String, String> MC1_19R1 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 104 */   private static Map<String, String> MC1_19R2 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 115 */   private static Map<String, String> MC1_20R1 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 127 */   private static Map<String, String> MC1_20R2 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   private static Map<String, String> MC1_20R3 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
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
/* 154 */   private static Map<String, String> MC1_20R4 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 179 */   private static Map<String, String> MC1_21R1 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 190 */   private static Map<String, String> MC1_21R2 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
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
/* 204 */   private static Map<String, String> MC1_21R3 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 215 */   private static Map<String, String> MC1_21R4 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 247 */   private static Map<String, String> MC1_21R5 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
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
/*     */   
/* 288 */   private static Map<String, String> MC1_21R6 = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
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
/*     */   public static Map<String, String> getMapping() {
/* 305 */     switch (MinecraftVersion.getVersion()) {
/*     */       case MC1_21_R6:
/* 307 */         return MC1_21R6;
/*     */       case MC1_21_R5:
/* 309 */         return MC1_21R5;
/*     */       case MC1_21_R4:
/* 311 */         return MC1_21R4;
/*     */       case MC1_21_R3:
/* 313 */         return MC1_21R3;
/*     */       case MC1_21_R2:
/* 315 */         return MC1_21R2;
/*     */       case MC1_21_R1:
/* 317 */         return MC1_21R1;
/*     */       case MC1_20_R4:
/* 319 */         return MC1_20R4;
/*     */       case MC1_20_R3:
/* 321 */         return MC1_20R3;
/*     */       case MC1_20_R2:
/* 323 */         return MC1_20R2;
/*     */       case MC1_20_R1:
/* 325 */         return MC1_20R1;
/*     */       case MC1_19_R3:
/* 327 */         return MC1_19R2;
/*     */       case MC1_19_R2:
/* 329 */         return MC1_19R2;
/*     */       case MC1_19_R1:
/* 331 */         return MC1_19R1;
/*     */       case MC1_18_R2:
/* 333 */         return MC1_18R2;
/*     */       case MC1_18_R1:
/* 335 */         return MC1_18R1;
/*     */       case UNKNOWN:
/* 337 */         return MC1_20R2;
/*     */     } 
/*     */     
/* 340 */     throw new NbtApiException("No fitting mapping found for version " + MinecraftVersion.getVersion() + ". This is a bug!");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\nmsmappings\MojangToMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */