/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TSMGetParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean latest;
/*    */   private boolean withLabels;
/*    */   private String[] selectedLabels;
/*    */   
/*    */   public static TSMGetParams multiGetParams() {
/* 21 */     return new TSMGetParams();
/*    */   }
/*    */   
/*    */   public TSMGetParams latest() {
/* 25 */     this.latest = true;
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public TSMGetParams withLabels(boolean withLabels) {
/* 30 */     this.withLabels = withLabels;
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public TSMGetParams withLabels() {
/* 35 */     return withLabels(true);
/*    */   }
/*    */   
/*    */   public TSMGetParams selectedLabels(String... labels) {
/* 39 */     this.selectedLabels = labels;
/* 40 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 45 */     if (this.latest) {
/* 46 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.LATEST);
/*    */     }
/*    */     
/* 49 */     if (this.withLabels) {
/* 50 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.WITHLABELS);
/* 51 */     } else if (this.selectedLabels != null) {
/* 52 */       args.add(TimeSeriesProtocol.TimeSeriesKeyword.SELECTED_LABELS);
/* 53 */       for (String label : this.selectedLabels)
/* 54 */         args.add(label); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSMGetParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */