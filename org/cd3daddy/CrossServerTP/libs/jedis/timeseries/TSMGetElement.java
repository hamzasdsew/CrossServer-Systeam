/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ public class TSMGetElement
/*    */   extends KeyValue<String, TSElement> {
/*    */   private final Map<String, String> labels;
/*    */   
/*    */   public TSMGetElement(String key, Map<String, String> labels, TSElement value) {
/* 11 */     super(key, value);
/* 12 */     this.labels = labels;
/*    */   }
/*    */   
/*    */   public Map<String, String> getLabels() {
/* 16 */     return this.labels;
/*    */   }
/*    */   
/*    */   public TSElement getElement() {
/* 20 */     return (TSElement)getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return getClass().getSimpleName() + "{key=" + 
/* 26 */       (String)getKey() + ", labels=" + 
/* 27 */       this.labels + ", element=" + 
/* 28 */       getElement() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TSMGetElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */