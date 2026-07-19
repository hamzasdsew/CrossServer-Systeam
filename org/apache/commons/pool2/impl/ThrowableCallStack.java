/*    */ package org.apache.commons.pool2.impl;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.text.DateFormat;
/*    */ import java.text.SimpleDateFormat;
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
/*    */ public class ThrowableCallStack
/*    */   implements CallStack
/*    */ {
/*    */   private final String messageFormat;
/*    */   private final DateFormat dateFormat;
/*    */   private volatile Snapshot snapshot;
/*    */   
/*    */   private static class Snapshot
/*    */     extends Throwable
/*    */   {
/*    */     private static final long serialVersionUID = 1L;
/*    */     
/*    */     private Snapshot() {}
/*    */     
/* 38 */     private final long timestampMillis = System.currentTimeMillis();
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
/*    */ 
/*    */ 
/*    */   
/*    */   public ThrowableCallStack(String messageFormat, boolean useTimestamp) {
/* 55 */     this.messageFormat = messageFormat;
/* 56 */     this.dateFormat = useTimestamp ? new SimpleDateFormat(messageFormat) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 61 */     this.snapshot = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fillInStackTrace() {
/* 66 */     this.snapshot = new Snapshot();
/*    */   }
/*    */   
/*    */   public synchronized boolean printStackTrace(PrintWriter writer) {
/*    */     String message;
/* 71 */     Snapshot snapshotRef = this.snapshot;
/* 72 */     if (snapshotRef == null) {
/* 73 */       return false;
/*    */     }
/*    */     
/* 76 */     if (this.dateFormat == null) {
/* 77 */       message = this.messageFormat;
/*    */     } else {
/* 79 */       synchronized (this.dateFormat) {
/* 80 */         message = this.dateFormat.format(Long.valueOf(snapshotRef.timestampMillis));
/*    */       } 
/*    */     } 
/* 83 */     writer.println(message);
/* 84 */     snapshotRef.printStackTrace(writer);
/* 85 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\ThrowableCallStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */