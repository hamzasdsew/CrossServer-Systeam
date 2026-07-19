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
/*    */ public class DrilldownPie
/*    */   extends CustomChart
/*    */ {
/*    */   private final Callable<Map<String, Map<String, Integer>>> callable;
/*    */   
/*    */   public DrilldownPie(String chartId, Callable<Map<String, Map<String, Integer>>> callable) {
/* 19 */     super(chartId);
/* 20 */     this.callable = callable;
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonObjectBuilder.JsonObject getChartData() throws Exception {
/* 25 */     JsonObjectBuilder valuesBuilder = new JsonObjectBuilder();
/*    */     
/* 27 */     Map<String, Map<String, Integer>> map = this.callable.call();
/* 28 */     if (map == null || map.isEmpty())
/*    */     {
/* 30 */       return null;
/*    */     }
/* 32 */     boolean reallyAllSkipped = true;
/* 33 */     for (Map.Entry<String, Map<String, Integer>> entryValues : map.entrySet()) {
/* 34 */       JsonObjectBuilder valueBuilder = new JsonObjectBuilder();
/* 35 */       boolean allSkipped = true;
/* 36 */       for (Map.Entry<String, Integer> valueEntry : (Iterable<Map.Entry<String, Integer>>)((Map)map.get(entryValues.getKey())).entrySet()) {
/* 37 */         valueBuilder.appendField(valueEntry.getKey(), ((Integer)valueEntry.getValue()).intValue());
/* 38 */         allSkipped = false;
/*    */       } 
/* 40 */       if (!allSkipped) {
/* 41 */         reallyAllSkipped = false;
/* 42 */         valuesBuilder.appendField(entryValues.getKey(), valueBuilder.build());
/*    */       } 
/*    */     } 
/* 45 */     if (reallyAllSkipped)
/*    */     {
/* 47 */       return null;
/*    */     }
/*    */     
/* 50 */     return (new JsonObjectBuilder())
/* 51 */       .appendField("values", valuesBuilder.build())
/* 52 */       .build();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\metrics\charts\DrilldownPie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */