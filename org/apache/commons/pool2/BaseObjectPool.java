/*     */ package org.apache.commons.pool2;
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
/*     */ public abstract class BaseObjectPool<T>
/*     */   extends BaseObject
/*     */   implements ObjectPool<T>
/*     */ {
/*     */   private volatile boolean closed;
/*     */   
/*     */   public void addObject() throws Exception {
/*  45 */     throw new UnsupportedOperationException();
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
/*     */   protected final void assertOpen() throws IllegalStateException {
/*  57 */     if (isClosed()) {
/*  58 */       throw new IllegalStateException("Pool not open");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract T borrowObject() throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() throws Exception {
/*  73 */     throw new UnsupportedOperationException();
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
/*     */   public void close() {
/*  85 */     this.closed = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumActive() {
/*  95 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumIdle() {
/* 105 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void invalidateObject(T paramT) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isClosed() {
/* 117 */     return this.closed;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void returnObject(T paramT) throws Exception;
/*     */ 
/*     */   
/*     */   protected void toStringAppendFields(StringBuilder builder) {
/* 125 */     builder.append("closed=");
/* 126 */     builder.append(this.closed);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\BaseObjectPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */