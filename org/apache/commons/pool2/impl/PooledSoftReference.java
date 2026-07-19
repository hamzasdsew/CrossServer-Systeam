/*    */ package org.apache.commons.pool2.impl;
/*    */ 
/*    */ import java.lang.ref.SoftReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PooledSoftReference<T>
/*    */   extends DefaultPooledObject<T>
/*    */ {
/*    */   private volatile SoftReference<T> reference;
/*    */   
/*    */   public PooledSoftReference(SoftReference<T> reference) {
/* 42 */     super(null);
/* 43 */     this.reference = reference;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T getObject() {
/* 57 */     return this.reference.get();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized SoftReference<T> getReference() {
/* 66 */     return this.reference;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized void setReference(SoftReference<T> reference) {
/* 80 */     this.reference = reference;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     StringBuilder result = new StringBuilder();
/* 89 */     result.append("Referenced Object: ");
/* 90 */     result.append(getObject().toString());
/* 91 */     result.append(", State: ");
/* 92 */     synchronized (this) {
/* 93 */       result.append(getState().toString());
/*    */     } 
/* 95 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\PooledSoftReference.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */