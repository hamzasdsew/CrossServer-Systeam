/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ public enum FunctionRestorePolicy
/*    */   implements Rawable
/*    */ {
/*  9 */   FLUSH,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   APPEND,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   REPLACE;
/*    */   
/*    */   private final byte[] raw;
/*    */   
/*    */   FunctionRestorePolicy() {
/* 27 */     this.raw = SafeEncoder.encode(name());
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 32 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\FunctionRestorePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */