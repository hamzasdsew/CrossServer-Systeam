/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessController;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
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
/*     */ public class SecurityManagerCallStack
/*     */   implements CallStack
/*     */ {
/*     */   private final String messageFormat;
/*     */   private final DateFormat dateFormat;
/*     */   private final PrivateSecurityManager securityManager;
/*     */   private volatile Snapshot snapshot;
/*     */   
/*     */   private static class PrivateSecurityManager
/*     */     extends SecurityManager
/*     */   {
/*     */     private PrivateSecurityManager() {}
/*     */     
/*     */     private List<WeakReference<Class<?>>> getCallStack() {
/*  51 */       Stream<WeakReference<Class<?>>> map = Stream.<Class<?>>of(getClassContext()).map(WeakReference::new);
/*  52 */       return map.collect((Collector)Collectors.toList());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Snapshot
/*     */   {
/*  60 */     private final long timestampMillis = System.currentTimeMillis();
/*     */ 
/*     */ 
/*     */     
/*     */     private final List<WeakReference<Class<?>>> stack;
/*     */ 
/*     */ 
/*     */     
/*     */     private Snapshot(List<WeakReference<Class<?>>> stack) {
/*  69 */       this.stack = stack;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SecurityManagerCallStack(String messageFormat, boolean useTimestamp) {
/*  89 */     this.messageFormat = messageFormat;
/*  90 */     this.dateFormat = useTimestamp ? new SimpleDateFormat(messageFormat) : null;
/*  91 */     this.securityManager = AccessController.<PrivateSecurityManager>doPrivileged(() -> new PrivateSecurityManager());
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  96 */     this.snapshot = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillInStackTrace() {
/* 101 */     this.snapshot = new Snapshot(this.securityManager.getCallStack());
/*     */   }
/*     */   
/*     */   public boolean printStackTrace(PrintWriter writer) {
/*     */     String message;
/* 106 */     Snapshot snapshotRef = this.snapshot;
/* 107 */     if (snapshotRef == null) {
/* 108 */       return false;
/*     */     }
/*     */     
/* 111 */     if (this.dateFormat == null) {
/* 112 */       message = this.messageFormat;
/*     */     } else {
/* 114 */       synchronized (this.dateFormat) {
/* 115 */         message = this.dateFormat.format(Long.valueOf(snapshotRef.timestampMillis));
/*     */       } 
/*     */     } 
/* 118 */     writer.println(message);
/* 119 */     snapshotRef.stack.forEach(reference -> writer.println(reference.get()));
/* 120 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\SecurityManagerCallStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */