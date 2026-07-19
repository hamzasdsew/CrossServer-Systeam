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
/*    */ public class AdvancedBarChart
/*    */   extends CustomChart
/*    */ {
/*    */   private final Callable<Map<String, int[]>> callable;
/*    */   
/*    */   public AdvancedBarChart(String chartId, Callable<Map<String, int[]>> callable) {
/* 19 */     super(chartId);
/* 20 */     this.callable = callable;
/*    */   }
/*    */ 
/*    */   
/*    */   protected JsonObjectBuilder.JsonObject getChartData() throws Exception {
/* 25 */     JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
/* 26 */     Map<String, int[]> map = this.callable.call();
/* 27 */     if (map == null || map.isEmpty())
/*    */     {
/* 29 */       return null;
/*    */     }
/* 31 */     boolean allSkipped = true;
/* 32 */     for (Map.Entry<String, int[]> entry : map.entrySet()) {
/* 33 */       if (((int[])entry.getValue()).length == 0) {
/*    */         continue;
/*    */       }
/* 36 */       allSkipped = false;
/* 37 */       valuesBuilder.appendField(entry.getKey(), entry.getValue());
/*    */     } 
/* 39 */     if (allSkipped)
/*    */     {
/* 41 */       return null;
/*    */     }
/*    */     
/* 44 */     return (new JsonObjectBuilder())
/* 45 */       .appendField("values", valuesBuilder.build())
/* 46 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\metrics\charts\AdvancedBarChart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */