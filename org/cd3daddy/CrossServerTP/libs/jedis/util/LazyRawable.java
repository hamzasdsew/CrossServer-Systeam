/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ 
/*    */ public class LazyRawable
/*    */   implements Rawable {
/*  7 */   private byte[] raw = null;
/*    */   
/*    */   public void setRaw(byte[] raw) {
/* 10 */     this.raw = raw;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getRaw() {
/* 15 */     return this.raw;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\LazyRawable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */