/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*    */ 
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UUIDUtil
/*    */ {
/*    */   public static UUID uuidFromIntArray(int[] is) {
/*  8 */     return new UUID(is[0] << 32L | is[1] & 0xFFFFFFFFL, is[2] << 32L | is[3] & 0xFFFFFFFFL);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int[] uuidToIntArray(UUID uUID) {
/* 13 */     long l = uUID.getMostSignificantBits();
/* 14 */     long m = uUID.getLeastSignificantBits();
/* 15 */     return leastMostToIntArray(l, m);
/*    */   }
/*    */   
/*    */   private static int[] leastMostToIntArray(long l, long m) {
/* 19 */     return new int[] { (int)(l >> 32L), (int)l, (int)(m >> 32L), (int)m };
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\UUIDUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */