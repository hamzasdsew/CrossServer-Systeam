/*    */ package org.apache.commons.pool2.impl;
/*    */ 
/*    */ import org.apache.commons.pool2.PooledObject;
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
/*    */ 
/*    */ public class DefaultEvictionPolicy<T>
/*    */   implements EvictionPolicy<T>
/*    */ {
/*    */   public boolean evict(EvictionConfig config, PooledObject<T> underTest, int idleCount) {
/* 49 */     return ((config.getIdleSoftEvictDuration().compareTo(underTest.getIdleDuration()) < 0 && config
/* 50 */       .getMinIdle() < idleCount) || config
/* 51 */       .getIdleEvictDuration().compareTo(underTest.getIdleDuration()) < 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\DefaultEvictionPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */