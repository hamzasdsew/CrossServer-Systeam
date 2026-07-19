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
/*    */ public class SimpleBarChart
/*    */   extends CustomChart
/*    */ {
/*    */   private final Callable<Map<String, Integer>> callable;
/*    */   
/*    */   public SimpleBarChart(String chartId, Callable<Map<String, Integer>> callable) {
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
/* 32 */     for (Map.Entry<String, Integer> entry : map.entrySet()) {
/* 33 */       valuesBuilder.appendField(entry.getKey(), new int[] { ((Integer)entry.getValue()).intValue() });
/*    */     } 
/*    */     
/* 36 */     return (new JsonObjectBuilder())
/* 37 */       .appendField("values", valuesBuilder.build())
/* 38 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\metrics\charts\SimpleBarChart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */