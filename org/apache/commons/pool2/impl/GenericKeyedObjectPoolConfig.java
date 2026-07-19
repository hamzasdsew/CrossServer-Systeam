/*     */ package org.apache.commons.pool2.impl;
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
/*     */ public class GenericKeyedObjectPoolConfig<T>
/*     */   extends BaseObjectPoolConfig<T>
/*     */ {
/*     */   public static final int DEFAULT_MAX_TOTAL_PER_KEY = 8;
/*     */   public static final int DEFAULT_MAX_TOTAL = -1;
/*     */   public static final int DEFAULT_MIN_IDLE_PER_KEY = 0;
/*     */   public static final int DEFAULT_MAX_IDLE_PER_KEY = 8;
/*  58 */   private int minIdlePerKey = 0;
/*     */   
/*  60 */   private int maxIdlePerKey = 8;
/*     */   
/*  62 */   private int maxTotalPerKey = 8;
/*     */   
/*  64 */   private int maxTotal = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericKeyedObjectPoolConfig<T> clone() {
/*     */     try {
/*  76 */       return (GenericKeyedObjectPoolConfig<T>)super.clone();
/*  77 */     } catch (CloneNotSupportedException e) {
/*  78 */       throw new AssertionError();
/*     */     } 
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
/*     */   
/*     */   public int getMaxIdlePerKey() {
/*  92 */     return this.maxIdlePerKey;
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
/*     */   
/*     */   public int getMaxTotal() {
/* 105 */     return this.maxTotal;
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
/*     */   
/*     */   public int getMaxTotalPerKey() {
/* 118 */     return this.maxTotalPerKey;
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
/*     */   
/*     */   public int getMinIdlePerKey() {
/* 131 */     return this.minIdlePerKey;
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
/*     */   
/*     */   public void setMaxIdlePerKey(int maxIdlePerKey) {
/* 144 */     this.maxIdlePerKey = maxIdlePerKey;
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
/*     */   
/*     */   public void setMaxTotal(int maxTotal) {
/* 157 */     this.maxTotal = maxTotal;
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
/*     */   
/*     */   public void setMaxTotalPerKey(int maxTotalPerKey) {
/* 170 */     this.maxTotalPerKey = maxTotalPerKey;
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
/*     */   
/*     */   public void setMinIdlePerKey(int minIdlePerKey) {
/* 183 */     this.minIdlePerKey = minIdlePerKey;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void toStringAppendFields(StringBuilder builder) {
/* 188 */     super.toStringAppendFields(builder);
/* 189 */     builder.append(", minIdlePerKey=");
/* 190 */     builder.append(this.minIdlePerKey);
/* 191 */     builder.append(", maxIdlePerKey=");
/* 192 */     builder.append(this.maxIdlePerKey);
/* 193 */     builder.append(", maxTotalPerKey=");
/* 194 */     builder.append(this.maxTotalPerKey);
/* 195 */     builder.append(", maxTotal=");
/* 196 */     builder.append(this.maxTotal);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\GenericKeyedObjectPoolConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */