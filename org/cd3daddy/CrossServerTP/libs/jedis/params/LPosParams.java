/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ public class LPosParams
/*    */   implements IParams {
/*    */   private Integer rank;
/*    */   private Integer maxlen;
/*    */   
/*    */   public static LPosParams lPosParams() {
/* 12 */     return new LPosParams();
/*    */   }
/*    */   
/*    */   public LPosParams rank(int rank) {
/* 16 */     this.rank = Integer.valueOf(rank);
/* 17 */     return this;
/*    */   }
/*    */   
/*    */   public LPosParams maxlen(int maxLen) {
/* 21 */     this.maxlen = Integer.valueOf(maxLen);
/* 22 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 27 */     if (this.rank != null) {
/* 28 */       args.add(Protocol.Keyword.RANK).add(this.rank);
/*    */     }
/*    */     
/* 31 */     if (this.maxlen != null)
/* 32 */       args.add(Protocol.Keyword.MAXLEN).add(this.maxlen); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\LPosParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */