/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.DoublePrecision;
/*    */ 
/*    */ public class Row
/*    */ {
/*    */   private final Map<String, Object> fields;
/*    */   
/*    */   public Row(Map<String, Object> fields) {
/* 11 */     this.fields = fields;
/*    */   }
/*    */   
/*    */   public boolean containsKey(String key) {
/* 15 */     return this.fields.containsKey(key);
/*    */   }
/*    */   
/*    */   public Object get(String key) {
/* 19 */     return this.fields.get(key);
/*    */   }
/*    */   
/*    */   public String getString(String key) {
/* 23 */     if (!containsKey(key)) {
/* 24 */       return "";
/*    */     }
/* 26 */     return (String)this.fields.get(key);
/*    */   }
/*    */   
/*    */   public long getLong(String key) {
/* 30 */     if (!containsKey(key)) {
/* 31 */       return 0L;
/*    */     }
/* 33 */     return Long.parseLong((String)this.fields.get(key));
/*    */   }
/*    */   
/*    */   public double getDouble(String key) {
/* 37 */     if (!containsKey(key)) {
/* 38 */       return 0.0D;
/*    */     }
/* 40 */     return DoublePrecision.parseFloatingPointNumber((String)this.fields.get(key)).doubleValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return String.valueOf(this.fields);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\Row.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */