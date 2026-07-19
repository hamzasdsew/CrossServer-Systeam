/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
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
/*     */ @Deprecated
/*     */ public class MurmurHash
/*     */   implements Hashing
/*     */ {
/*     */   public static int hash(byte[] data, int seed) {
/*  35 */     return hash(ByteBuffer.wrap(data), seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hash(byte[] data, int offset, int length, int seed) {
/*  47 */     return hash(ByteBuffer.wrap(data, offset, length), seed);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hash(ByteBuffer buf, int seed) {
/*  58 */     ByteOrder byteOrder = buf.order();
/*  59 */     buf.order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/*  61 */     int m = 1540483477;
/*  62 */     int r = 24;
/*     */     
/*  64 */     int h = seed ^ buf.remaining();
/*     */ 
/*     */     
/*  67 */     while (buf.remaining() >= 4) {
/*  68 */       int k = buf.getInt();
/*     */       
/*  70 */       k *= m;
/*  71 */       k ^= k >>> r;
/*  72 */       k *= m;
/*     */       
/*  74 */       h *= m;
/*  75 */       h ^= k;
/*     */     } 
/*     */     
/*  78 */     if (buf.remaining() > 0) {
/*  79 */       ByteBuffer finish = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*     */       
/*  82 */       finish.put(buf).rewind();
/*  83 */       h ^= finish.getInt();
/*  84 */       h *= m;
/*     */     } 
/*     */     
/*  87 */     h ^= h >>> 13;
/*  88 */     h *= m;
/*  89 */     h ^= h >>> 15;
/*     */     
/*  91 */     buf.order(byteOrder);
/*  92 */     return h;
/*     */   }
/*     */   
/*     */   public static long hash64A(byte[] data, int seed) {
/*  96 */     return hash64A(ByteBuffer.wrap(data), seed);
/*     */   }
/*     */   
/*     */   public static long hash64A(byte[] data, int offset, int length, int seed) {
/* 100 */     return hash64A(ByteBuffer.wrap(data, offset, length), seed);
/*     */   }
/*     */   
/*     */   public static long hash64A(ByteBuffer buf, int seed) {
/* 104 */     ByteOrder byteOrder = buf.order();
/* 105 */     buf.order(ByteOrder.LITTLE_ENDIAN);
/*     */     
/* 107 */     long m = -4132994306676758123L;
/* 108 */     int r = 47;
/*     */     
/* 110 */     long h = seed ^ buf.remaining() * m;
/*     */ 
/*     */     
/* 113 */     while (buf.remaining() >= 8) {
/* 114 */       long k = buf.getLong();
/*     */       
/* 116 */       k *= m;
/* 117 */       k ^= k >>> r;
/* 118 */       k *= m;
/*     */       
/* 120 */       h ^= k;
/* 121 */       h *= m;
/*     */     } 
/*     */     
/* 124 */     if (buf.remaining() > 0) {
/* 125 */       ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
/*     */ 
/*     */       
/* 128 */       finish.put(buf).rewind();
/* 129 */       h ^= finish.getLong();
/* 130 */       h *= m;
/*     */     } 
/*     */     
/* 133 */     h ^= h >>> r;
/* 134 */     h *= m;
/* 135 */     h ^= h >>> r;
/*     */     
/* 137 */     buf.order(byteOrder);
/* 138 */     return h;
/*     */   }
/*     */ 
/*     */   
/*     */   public long hash(byte[] key) {
/* 143 */     return hash64A(key, 305441741);
/*     */   }
/*     */ 
/*     */   
/*     */   public long hash(String key) {
/* 148 */     return hash(SafeEncoder.encode(key));
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\MurmurHash.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */