/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ public class LCSParams implements IParams {
/*    */   private boolean len = false;
/*    */   private boolean idx = false;
/*    */   private Long minMatchLen;
/*    */   private boolean withMatchLen = false;
/*    */   
/*    */   public static LCSParams LCSParams() {
/* 13 */     return new LCSParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LCSParams len() {
/* 20 */     this.len = true;
/* 21 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LCSParams idx() {
/* 31 */     this.idx = true;
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LCSParams minMatchLen(long minMatchLen) {
/* 40 */     this.minMatchLen = Long.valueOf(minMatchLen);
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public LCSParams withMatchLen() {
/* 49 */     this.withMatchLen = true;
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 55 */     if (this.len) {
/* 56 */       args.add(Protocol.Keyword.LEN);
/*    */     }
/* 58 */     if (this.idx) {
/* 59 */       args.add(Protocol.Keyword.IDX);
/*    */     }
/* 61 */     if (this.minMatchLen != null) {
/* 62 */       args.add(Protocol.Keyword.MINMATCHLEN).add(this.minMatchLen);
/*    */     }
/* 64 */     if (this.withMatchLen)
/* 65 */       args.add(Protocol.Keyword.WITHMATCHLEN); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\LCSParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */