/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ public final class DoublePrecision
/*    */ {
/*    */   private DoublePrecision() {
/*  6 */     throw new InstantiationError("Must not instantiate this class");
/*    */   }
/*    */ 
/*    */   
/*    */   public static Double parseFloatingPointNumber(String str) throws NumberFormatException {
/* 11 */     if (str == null) return null;
/*    */ 
/*    */     
/*    */     try {
/* 15 */       return Double.valueOf(str);
/*    */     }
/* 17 */     catch (NumberFormatException e) {
/*    */       
/* 19 */       switch (str) {
/*    */         
/*    */         case "inf":
/*    */         case "+inf":
/* 23 */           return Double.valueOf(Double.POSITIVE_INFINITY);
/*    */         
/*    */         case "-inf":
/* 26 */           return Double.valueOf(Double.NEGATIVE_INFINITY);
/*    */         
/*    */         case "nan":
/*    */         case "-nan":
/* 30 */           return Double.valueOf(Double.NaN);
/*    */       } 
/*    */       
/* 33 */       throw e;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static Double parseEncodedFloatingPointNumber(Object val) throws NumberFormatException {
/* 39 */     if (val == null) return null; 
/* 40 */     if (val instanceof Double) return (Double)val; 
/* 41 */     return parseFloatingPointNumber((String)val);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\DoublePrecision.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */