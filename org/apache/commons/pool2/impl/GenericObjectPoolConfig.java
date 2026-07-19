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
/*     */ public class GenericObjectPoolConfig<T>
/*     */   extends BaseObjectPoolConfig<T>
/*     */ {
/*     */   public static final int DEFAULT_MAX_TOTAL = 8;
/*     */   public static final int DEFAULT_MAX_IDLE = 8;
/*     */   public static final int DEFAULT_MIN_IDLE = 0;
/*  52 */   private int maxTotal = 8;
/*     */   
/*  54 */   private int maxIdle = 8;
/*     */   
/*  56 */   private int minIdle = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   public GenericObjectPoolConfig<T> clone() {
/*     */     try {
/*  62 */       return (GenericObjectPoolConfig<T>)super.clone();
/*  63 */     } catch (CloneNotSupportedException e) {
/*  64 */       throw new AssertionError();
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
/*     */   public int getMaxIdle() {
/*  78 */     return this.maxIdle;
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
/*     */   
/*     */   public int getMaxTotal() {
/*  92 */     return this.maxTotal;
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
/*     */   public int getMinIdle() {
/* 105 */     return this.minIdle;
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
/*     */   
/*     */   public void setMaxIdle(int maxIdle) {
/* 119 */     this.maxIdle = maxIdle;
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
/* 132 */     this.maxTotal = maxTotal;
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
/*     */   public void setMinIdle(int minIdle) {
/* 145 */     this.minIdle = minIdle;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void toStringAppendFields(StringBuilder builder) {
/* 150 */     super.toStringAppendFields(builder);
/* 151 */     builder.append(", maxTotal=");
/* 152 */     builder.append(this.maxTotal);
/* 153 */     builder.append(", maxIdle=");
/* 154 */     builder.append(this.maxIdle);
/* 155 */     builder.append(", minIdle=");
/* 156 */     builder.append(this.minIdle);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\GenericObjectPoolConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */