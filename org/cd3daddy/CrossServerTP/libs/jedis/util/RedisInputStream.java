/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigInteger;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
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
/*     */ public class RedisInputStream
/*     */   extends FilterInputStream
/*     */ {
/*  26 */   private static final int INPUT_BUFFER_SIZE = Integer.parseInt(
/*  27 */       System.getProperty("jedis.bufferSize.input", 
/*  28 */         System.getProperty("jedis.bufferSize", "8192")));
/*     */   
/*     */   protected final byte[] buf;
/*     */   protected int count;
/*     */   protected int limit;
/*     */   
/*     */   public RedisInputStream(InputStream in, int size) {
/*  35 */     super(in);
/*  36 */     if (size <= 0) {
/*  37 */       throw new IllegalArgumentException("Buffer size <= 0");
/*     */     }
/*  39 */     this.buf = new byte[size];
/*     */   }
/*     */   
/*     */   public RedisInputStream(InputStream in) {
/*  43 */     this(in, INPUT_BUFFER_SIZE);
/*     */   }
/*     */   
/*     */   public byte readByte() throws JedisConnectionException {
/*  47 */     ensureFill();
/*  48 */     return this.buf[this.count++];
/*     */   }
/*     */   
/*     */   private void ensureCrLf() {
/*  52 */     byte[] buf = this.buf;
/*     */     
/*  54 */     ensureFill();
/*  55 */     if (buf[this.count++] == 13) {
/*     */       
/*  57 */       ensureFill();
/*  58 */       if (buf[this.count++] == 10) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/*  63 */     throw new JedisConnectionException("Unexpected character!");
/*     */   }
/*     */   
/*     */   public String readLine() {
/*  67 */     StringBuilder sb = new StringBuilder();
/*     */     while (true) {
/*  69 */       ensureFill();
/*     */       
/*  71 */       byte b = this.buf[this.count++];
/*  72 */       if (b == 13) {
/*  73 */         ensureFill();
/*     */         
/*  75 */         byte c = this.buf[this.count++];
/*  76 */         if (c == 10) {
/*     */           break;
/*     */         }
/*  79 */         sb.append((char)b);
/*  80 */         sb.append((char)c); continue;
/*     */       } 
/*  82 */       sb.append((char)b);
/*     */     } 
/*     */ 
/*     */     
/*  86 */     String reply = sb.toString();
/*  87 */     if (reply.length() == 0) {
/*  88 */       throw new JedisConnectionException("It seems like server has closed the connection.");
/*     */     }
/*     */     
/*  91 */     return reply;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] readLineBytes() {
/* 102 */     ensureFill();
/*     */     
/* 104 */     int pos = this.count;
/* 105 */     byte[] buf = this.buf;
/*     */     while (true) {
/* 107 */       if (pos == this.limit) {
/* 108 */         return readLineBytesSlowly();
/*     */       }
/*     */       
/* 111 */       if (buf[pos++] == 13) {
/* 112 */         if (pos == this.limit) {
/* 113 */           return readLineBytesSlowly();
/*     */         }
/*     */         
/* 116 */         if (buf[pos++] == 10) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     int N = pos - this.count - 2;
/* 123 */     byte[] line = new byte[N];
/* 124 */     System.arraycopy(buf, this.count, line, 0, N);
/* 125 */     this.count = pos;
/* 126 */     return line;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] readLineBytesSlowly() {
/* 135 */     ByteArrayOutputStream bout = null;
/*     */     while (true) {
/* 137 */       ensureFill();
/*     */       
/* 139 */       byte b = this.buf[this.count++];
/* 140 */       if (b == 13) {
/* 141 */         ensureFill();
/*     */         
/* 143 */         byte c = this.buf[this.count++];
/* 144 */         if (c == 10) {
/*     */           break;
/*     */         }
/*     */         
/* 148 */         if (bout == null) {
/* 149 */           bout = new ByteArrayOutputStream(16);
/*     */         }
/*     */         
/* 152 */         bout.write(b);
/* 153 */         bout.write(c); continue;
/*     */       } 
/* 155 */       if (bout == null) {
/* 156 */         bout = new ByteArrayOutputStream(16);
/*     */       }
/*     */       
/* 159 */       bout.write(b);
/*     */     } 
/*     */ 
/*     */     
/* 163 */     return (bout == null) ? new byte[0] : bout.toByteArray();
/*     */   }
/*     */   
/*     */   public Object readNullCrLf() {
/* 167 */     ensureCrLf();
/* 168 */     return null;
/*     */   }
/*     */   
/*     */   public boolean readBooleanCrLf() {
/* 172 */     byte[] buf = this.buf;
/*     */     
/* 174 */     ensureFill();
/* 175 */     byte b = buf[this.count++];
/*     */     
/* 177 */     ensureCrLf();
/* 178 */     switch (b) { case 116:
/* 179 */         return true;
/* 180 */       case 102: return false; }
/* 181 */      throw new JedisConnectionException("Unexpected character!");
/*     */   }
/*     */ 
/*     */   
/*     */   public int readIntCrLf() {
/* 186 */     return (int)readLongCrLf();
/*     */   }
/*     */   
/*     */   public long readLongCrLf() {
/* 190 */     byte[] buf = this.buf;
/*     */     
/* 192 */     ensureFill();
/*     */     
/* 194 */     boolean isNeg = (buf[this.count] == 45);
/* 195 */     if (isNeg) {
/* 196 */       this.count++;
/*     */     }
/*     */     
/* 199 */     long value = 0L;
/*     */     while (true) {
/* 201 */       ensureFill();
/*     */       
/* 203 */       int b = buf[this.count++];
/* 204 */       if (b == 13) {
/* 205 */         ensureFill();
/*     */         
/* 207 */         if (buf[this.count++] != 10) {
/* 208 */           throw new JedisConnectionException("Unexpected character!");
/*     */         }
/*     */         
/*     */         break;
/*     */       } 
/* 213 */       value = value * 10L + b - 48L;
/*     */     } 
/*     */ 
/*     */     
/* 217 */     return isNeg ? -value : value;
/*     */   }
/*     */   
/*     */   public double readDoubleCrLf() {
/* 221 */     return DoublePrecision.parseFloatingPointNumber(readLine()).doubleValue();
/*     */   }
/*     */   
/*     */   public BigInteger readBigIntegerCrLf() {
/* 225 */     return new BigInteger(readLine());
/*     */   }
/*     */ 
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws JedisConnectionException {
/* 230 */     ensureFill();
/*     */     
/* 232 */     int length = Math.min(this.limit - this.count, len);
/* 233 */     System.arraycopy(this.buf, this.count, b, off, length);
/* 234 */     this.count += length;
/* 235 */     return length;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureFill() throws JedisConnectionException {
/* 243 */     if (this.count >= this.limit)
/*     */       try {
/* 245 */         this.limit = this.in.read(this.buf);
/* 246 */         this.count = 0;
/* 247 */         if (this.limit == -1) {
/* 248 */           throw new JedisConnectionException("Unexpected end of stream.");
/*     */         }
/* 250 */       } catch (IOException e) {
/* 251 */         throw new JedisConnectionException(e);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\RedisInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */