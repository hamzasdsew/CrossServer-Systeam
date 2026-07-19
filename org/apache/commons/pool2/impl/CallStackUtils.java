/*    */ package org.apache.commons.pool2.impl;
/*    */ 
/*    */ import java.security.AccessControlException;
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
/*    */ public final class CallStackUtils
/*    */ {
/*    */   private static boolean canCreateSecurityManager() {
/* 35 */     SecurityManager manager = System.getSecurityManager();
/* 36 */     if (manager == null) {
/* 37 */       return true;
/*    */     }
/*    */     try {
/* 40 */       manager.checkPermission(new RuntimePermission("createSecurityManager"));
/* 41 */       return true;
/* 42 */     } catch (AccessControlException ignored) {
/* 43 */       return false;
/*    */     } 
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
/*    */   @Deprecated
/*    */   public static CallStack newCallStack(String messageFormat, boolean useTimestamp) {
/* 58 */     return newCallStack(messageFormat, useTimestamp, false);
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
/*    */   public static CallStack newCallStack(String messageFormat, boolean useTimestamp, boolean requireFullStackTrace) {
/* 75 */     return (canCreateSecurityManager() && !requireFullStackTrace) ? 
/* 76 */       new SecurityManagerCallStack(messageFormat, useTimestamp) : 
/* 77 */       new ThrowableCallStack(messageFormat, useTimestamp);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\CallStackUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */