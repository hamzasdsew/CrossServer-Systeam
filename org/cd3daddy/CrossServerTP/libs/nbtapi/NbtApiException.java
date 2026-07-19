/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
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
/*    */ public class NbtApiException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -993309714559452334L;
/* 23 */   public static Boolean confirmedBroken = null;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NbtApiException() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NbtApiException(String message, Throwable cause) {
/* 37 */     super(generateMessage(message), cause);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NbtApiException(String message) {
/* 44 */     super(generateMessage(message));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NbtApiException(Throwable cause) {
/* 51 */     super(generateMessage((cause == null) ? null : cause.toString()), cause);
/*    */   }
/*    */   
/*    */   private static String generateMessage(String message) {
/* 55 */     if (message == null)
/* 56 */       return null; 
/* 57 */     if (confirmedBroken == null)
/* 58 */       return "[?][" + MinecraftVersion.getNBTAPIVersion() + "]" + message; 
/* 59 */     if (!confirmedBroken.booleanValue()) {
/* 60 */       return "[Selfchecked][" + MinecraftVersion.getNBTAPIVersion() + "]" + message;
/*    */     }
/*    */     
/* 63 */     return "[" + MinecraftVersion.getVersion() + "][" + MinecraftVersion.getNBTAPIVersion() + "]There were errors detected during the server self-check! Please, make sure that NBT-API is up to date. Error message: " + message;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NbtApiException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */