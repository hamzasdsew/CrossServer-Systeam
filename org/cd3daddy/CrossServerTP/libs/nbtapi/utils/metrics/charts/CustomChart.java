/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.charts;
/*    */ 
/*    */ import java.util.function.BiConsumer;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.metrics.json.JsonObjectBuilder;
/*    */ 
/*    */ 
/*    */ public abstract class CustomChart
/*    */ {
/*    */   private final String chartId;
/*    */   
/*    */   protected CustomChart(String chartId) {
/* 12 */     if (chartId == null) {
/* 13 */       throw new IllegalArgumentException("chartId must not be null");
/*    */     }
/* 15 */     this.chartId = chartId;
/*    */   }
/*    */   
/*    */   public JsonObjectBuilder.JsonObject getRequestJsonObject(BiConsumer<String, Throwable> errorLogger, boolean logErrors) {
/* 19 */     JsonObjectBuilder builder = new JsonObjectBuilder();
/* 20 */     builder.appendField("chartId", this.chartId);
/*    */     try {
/* 22 */       JsonObjectBuilder.JsonObject data = getChartData();
/* 23 */       if (data == null)
/*    */       {
/* 25 */         return null;
/*    */       }
/* 27 */       builder.appendField("data", data);
/* 28 */     } catch (Throwable t) {
/* 29 */       if (logErrors)
/*    */       {
/* 31 */         errorLogger.accept("Failed to get data for custom chart with id " + this.chartId, t);
/*    */       }
/* 33 */       return null;
/*    */     } 
/* 35 */     return builder.build();
/*    */   }
/*    */   
/*    */   protected abstract JsonObjectBuilder.JsonObject getChartData() throws Exception;
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\metrics\charts\CustomChart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */