/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.Gson;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class GsonWrapper
/*    */ {
/* 26 */   private static Gson gson = new Gson();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getString(Object obj) {
/* 35 */     return gson.toJson(obj);
/*    */   }
/*    */   
/*    */   public static void overwriteGsonInstance(Gson replacement) {
/* 39 */     gson = replacement;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> T deserializeJson(String json, Class<T> type) {
/*    */     try {
/* 51 */       if (json == null) {
/* 52 */         return null;
/*    */       }
/*    */       
/* 55 */       T obj = (T)gson.fromJson(json, type);
/* 56 */       return type.cast(obj);
/* 57 */     } catch (Exception ex) {
/* 58 */       throw new NbtApiException("Error while converting json to " + type.getName(), ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\GsonWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */