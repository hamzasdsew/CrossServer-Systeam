/*    */ package org.apache.commons.pool2.impl;
/*    */ 
/*    */ import java.util.concurrent.locks.Condition;
/*    */ import java.util.concurrent.locks.ReentrantLock;
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
/*    */ class InterruptibleReentrantLock
/*    */   extends ReentrantLock
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public InterruptibleReentrantLock(boolean fairness) {
/* 43 */     super(fairness);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void interruptWaiters(Condition condition) {
/* 52 */     getWaitingThreads(condition).forEach(Thread::interrupt);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\InterruptibleReentrantLock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */