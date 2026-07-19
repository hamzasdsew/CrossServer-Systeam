/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XAutoClaimParams
/*    */   implements IParams
/*    */ {
/*    */   private Integer count;
/*    */   
/*    */   public static XAutoClaimParams xAutoClaimParams() {
/* 14 */     return new XAutoClaimParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XAutoClaimParams count(int count) {
/* 23 */     this.count = Integer.valueOf(count);
/* 24 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 29 */     if (this.count != null)
/* 30 */       args.add(Protocol.Keyword.COUNT.getRaw()).add(this.count); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\XAutoClaimParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */