/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.charts;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.Callable;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AdvancedPie
/*    */   extends CustomChart
/*    */ {
/*    */   private final Callable<Map<String, Integer>> callable;
/*    */   
/*    */   public AdvancedPie(String chartId, Callable<Map<String, Integer>> callable) {
/* 19 */     super(chartId);
/* 20 */     this.callable = callable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
/* 25 */     JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
/*    */     
/* 27 */     Map<String, Integer> map = this.callable.call();
/* 28 */     if (map == null || map.isEmpty())
/*    */     {
/* 30 */       return null;
/*    */     }
/* 32 */     boolean allSkipped = true;
/* 33 */     for (Map.Entry<String, Integer> entry : map.entrySet()) {
/* 34 */       if (((Integer)entry.getValue()).intValue() == 0) {
/*    */         continue;
/*    */       }
/* 37 */       allSkipped = false;
/* 38 */       valuesBuilder.appendField(entry.getKey(), ((Integer)entry.getValue()).intValue());
/*    */     } 
/* 40 */     if (allSkipped)
/*    */     {
/* 42 */       return null;
/*    */     }
/*    */     
/* 45 */     return (new JsonObjectBuilder())
/* 46 */       .appendField("values", valuesBuilder.build())
/* 47 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\metrics\charts\AdvancedPie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */