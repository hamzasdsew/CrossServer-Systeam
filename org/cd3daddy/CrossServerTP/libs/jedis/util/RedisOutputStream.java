/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*     */ 
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RedisOutputStream
/*     */   extends FilterOutputStream
/*     */ {
/*  14 */   private static final int OUTPUT_BUFFER_SIZE = Integer.parseInt(
/*  15 */       System.getProperty("jedis.bufferSize.output", 
/*  16 */         System.getProperty("jedis.bufferSize", "8192")));
/*     */   
/*     */   protected final byte[] buf;
/*     */   
/*     */   protected int count;
/*     */   
/*  22 */   private static final int[] sizeTable = new int[] { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };
/*     */ 
/*     */   
/*  25 */   private static final byte[] DigitTens = new byte[] { 48, 48, 48, 48, 48, 48, 48, 48, 48, 48, 49, 49, 49, 49, 49, 49, 49, 49, 49, 49, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 51, 51, 51, 51, 51, 51, 51, 51, 51, 51, 52, 52, 52, 52, 52, 52, 52, 52, 52, 52, 53, 53, 53, 53, 53, 53, 53, 53, 53, 53, 54, 54, 54, 54, 54, 54, 54, 54, 54, 54, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 56, 56, 56, 56, 56, 56, 56, 56, 56, 56, 57, 57, 57, 57, 57, 57, 57, 57, 57, 57 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  32 */   private static final byte[] DigitOnes = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static final byte[] digits = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };
/*     */ 
/*     */ 
/*     */   
/*     */   public RedisOutputStream(OutputStream out) {
/*  44 */     this(out, OUTPUT_BUFFER_SIZE);
/*     */   }
/*     */   
/*     */   public RedisOutputStream(OutputStream out, int size) {
/*  48 */     super(out);
/*  49 */     if (size <= 0) {
/*  50 */       throw new IllegalArgumentException("Buffer size <= 0");
/*     */     }
/*  52 */     this.buf = new byte[size];
/*     */   }
/*     */   
/*     */   private void flushBuffer() throws IOException {
/*  56 */     if (this.count > 0) {
/*  57 */       this.out.write(this.buf, 0, this.count);
/*  58 */       this.count = 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void write(byte b) throws IOException {
/*  63 */     if (this.count == this.buf.length) {
/*  64 */       flushBuffer();
/*     */     }
/*  66 */     this.buf[this.count++] = b;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  71 */     write(b, 0, b.length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  76 */     if (len >= this.buf.length) {
/*  77 */       flushBuffer();
/*  78 */       this.out.write(b, off, len);
/*     */     } else {
/*  80 */       if (len >= this.buf.length - this.count) {
/*  81 */         flushBuffer();
/*     */       }
/*     */       
/*  84 */       System.arraycopy(b, off, this.buf, this.count, len);
/*  85 */       this.count += len;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void writeCrLf() throws IOException {
/*  90 */     if (2 >= this.buf.length - this.count) {
/*  91 */       flushBuffer();
/*     */     }
/*     */     
/*  94 */     this.buf[this.count++] = 13;
/*  95 */     this.buf[this.count++] = 10;
/*     */   }
/*     */   
/*     */   public void writeIntCrLf(int value) throws IOException {
/*  99 */     if (value < 0) {
/* 100 */       write((byte)45);
/* 101 */       value = -value;
/*     */     } 
/*     */     
/* 104 */     int size = 0;
/* 105 */     while (value > sizeTable[size]) {
/* 106 */       size++;
/*     */     }
/* 108 */     size++;
/* 109 */     if (size >= this.buf.length - this.count) {
/* 110 */       flushBuffer();
/*     */     }
/*     */ 
/*     */     
/* 114 */     int charPos = this.count + size;
/*     */     
/* 116 */     while (value >= 65536) {
/* 117 */       int q = value / 100;
/* 118 */       int r = value - (q << 6) + (q << 5) + (q << 2);
/* 119 */       value = q;
/* 120 */       this.buf[--charPos] = DigitOnes[r];
/* 121 */       this.buf[--charPos] = DigitTens[r];
/*     */     } 
/*     */     
/*     */     do {
/* 125 */       int q = value * 52429 >>> 19;
/* 126 */       int r = value - (q << 3) + (q << 1);
/* 127 */       this.buf[--charPos] = digits[r];
/* 128 */       value = q;
/* 129 */     } while (value != 0);
/*     */     
/* 131 */     this.count += size;
/*     */     
/* 133 */     writeCrLf();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/* 138 */     flushBuffer();
/* 139 */     this.out.flush();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\RedisOutputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */