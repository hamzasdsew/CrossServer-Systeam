/*    */ package org.apache.commons.pool2.impl;
/*    */ 
/*    */ import java.io.PrintWriter;
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
/*    */ public class NoOpCallStack
/*    */   implements CallStack
/*    */ {
/* 32 */   public static final CallStack INSTANCE = new NoOpCallStack();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void fillInStackTrace() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean printStackTrace(PrintWriter writer) {
/* 52 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\NoOpCallStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */