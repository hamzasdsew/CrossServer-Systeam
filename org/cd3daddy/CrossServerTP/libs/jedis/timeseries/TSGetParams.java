/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TSGetParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean latest;
/*    */   
/*    */   public static TSGetParams getParams() {
/* 16 */     return new TSGetParams();
/*    */   }
/*    */   
/*    */   public TSGetParams latest() {
/* 20 */     this.latest = true;
/* 21 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 26 */     if (this.latest)
/* 27 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.LATEST); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSGetParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */