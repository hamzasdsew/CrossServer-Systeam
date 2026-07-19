/*    */ package org.apache.commons.pool2;
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
/*    */ public enum PooledObjectState
/*    */ {
/* 29 */   IDLE,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 34 */   ALLOCATED,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 39 */   EVICTION,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 49 */   EVICTION_RETURN_TO_HEAD,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 54 */   VALIDATION,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 60 */   VALIDATION_PREALLOCATED,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 67 */   VALIDATION_RETURN_TO_HEAD,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 72 */   INVALID,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   ABANDONED,
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 82 */   RETURNING;
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\PooledObjectState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */