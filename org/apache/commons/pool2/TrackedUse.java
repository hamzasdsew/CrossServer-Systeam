/*    */ package org.apache.commons.pool2;
/*    */ 
/*    */ import java.time.Instant;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TrackedUse
/*    */ {
/*    */   @Deprecated
/*    */   long getLastUsed();
/*    */   
/*    */   default Instant getLastUsedInstant() {
/* 50 */     return Instant.ofEpochMilli(getLastUsed());
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\TrackedUse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */