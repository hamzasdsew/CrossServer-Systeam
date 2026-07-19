/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ public final class ByteArrayComparator {
/*    */   private ByteArrayComparator() {
/*  5 */     throw new InstantiationError("Must not instantiate this class");
/*    */   }
/*    */   
/*    */   public static int compare(byte[] val1, byte[] val2) {
/*  9 */     int len1 = val1.length;
/* 10 */     int len2 = val2.length;
/* 11 */     int lmin = Math.min(len1, len2);
/*    */     
/* 13 */     for (int i = 0; i < lmin; i++) {
/* 14 */       byte b1 = val1[i];
/* 15 */       byte b2 = val2[i];
/* 16 */       if (b1 < b2) return -1; 
/* 17 */       if (b1 > b2) return 1;
/*    */     
/*    */     } 
/* 20 */     if (len1 < len2) return -1; 
/* 21 */     if (len1 > len2) return 1; 
/* 22 */     return 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\ByteArrayComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */