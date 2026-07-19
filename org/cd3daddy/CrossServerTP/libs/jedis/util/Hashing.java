/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public interface Hashing
/*    */ {
/* 11 */   public static final Hashing MURMUR_HASH = new MurmurHash();
/* 12 */   public static final ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<>();
/*    */   
/* 14 */   public static final Hashing MD5 = new Hashing()
/*    */     {
/*    */       public long hash(String key) {
/* 17 */         return hash(SafeEncoder.encode(key));
/*    */       }
/*    */ 
/*    */       
/*    */       public long hash(byte[] key) {
/*    */         try {
/* 23 */           if (md5Holder.get() == null) {
/* 24 */             md5Holder.set(MessageDigest.getInstance("MD5"));
/*    */           }
/* 26 */         } catch (NoSuchAlgorithmException e) {
/* 27 */           throw new IllegalStateException("++++ no md5 algorithm found");
/*    */         } 
/* 29 */         MessageDigest md5 = md5Holder.get();
/*    */         
/* 31 */         md5.reset();
/* 32 */         md5.update(key);
/* 33 */         byte[] bKey = md5.digest();
/* 34 */         return (bKey[3] & 0xFF) << 24L | (bKey[2] & 0xFF) << 16L | (bKey[1] & 0xFF) << 8L | (bKey[0] & 0xFF);
/*    */       }
/*    */     };
/*    */   
/*    */   long hash(String paramString);
/*    */   
/*    */   long hash(byte[] paramArrayOfbyte);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\Hashing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */