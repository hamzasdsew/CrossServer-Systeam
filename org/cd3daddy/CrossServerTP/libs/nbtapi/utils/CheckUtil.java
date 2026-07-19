/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CheckUtil
/*    */ {
/*    */   public static void assertAvailable(MinecraftVersion version) {
/* 12 */     if (!MinecraftVersion.isAtLeastVersion(version))
/* 13 */       throw new NbtApiException("This Method is only avaliable for the version " + version
/* 14 */           .name() + " and above!"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\CheckUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */