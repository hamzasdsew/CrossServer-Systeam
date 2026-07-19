/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class SafeEncoder
/*    */ {
/* 13 */   public static volatile Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
/*    */   
/*    */   private SafeEncoder() {
/* 16 */     throw new InstantiationError("Must not instantiate this class");
/*    */   }
/*    */   
/*    */   public static byte[][] encodeMany(String... strs) {
/* 20 */     byte[][] many = new byte[strs.length][];
/* 21 */     for (int i = 0; i < strs.length; i++) {
/* 22 */       many[i] = encode(strs[i]);
/*    */     }
/* 24 */     return many;
/*    */   }
/*    */   
/*    */   public static byte[] encode(String str) {
/* 28 */     if (str == null) {
/* 29 */       throw new IllegalArgumentException("null value cannot be sent to redis");
/*    */     }
/* 31 */     return str.getBytes(DEFAULT_CHARSET);
/*    */   }
/*    */   
/*    */   public static String encode(byte[] data) {
/* 35 */     return new String(data, DEFAULT_CHARSET);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object encodeObject(Object dataToEncode) {
/* 45 */     if (dataToEncode instanceof byte[]) {
/* 46 */       return encode((byte[])dataToEncode);
/*    */     }
/*    */     
/* 49 */     if (dataToEncode instanceof KeyValue) {
/* 50 */       KeyValue keyValue = (KeyValue)dataToEncode;
/* 51 */       return new KeyValue<>(encodeObject(keyValue.getKey()), encodeObject(keyValue.getValue()));
/*    */     } 
/*    */     
/* 54 */     if (dataToEncode instanceof List) {
/* 55 */       List arrayToDecode = (List)dataToEncode;
/* 56 */       List<Object> returnValueArray = new ArrayList(arrayToDecode.size());
/* 57 */       for (Object arrayEntry : arrayToDecode)
/*    */       {
/* 59 */         returnValueArray.add(encodeObject(arrayEntry));
/*    */       }
/* 61 */       return returnValueArray;
/*    */     } 
/*    */     
/* 64 */     return dataToEncode;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\SafeEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */