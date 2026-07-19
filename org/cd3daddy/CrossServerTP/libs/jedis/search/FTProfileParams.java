/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FTProfileParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean limited;
/*    */   
/*    */   public static FTProfileParams profileParams() {
/* 16 */     return new FTProfileParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FTProfileParams limited() {
/* 23 */     this.limited = true;
/* 24 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 30 */     if (this.limited)
/* 31 */       args.add(SearchProtocol.SearchKeyword.LIMITED); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\FTProfileParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */