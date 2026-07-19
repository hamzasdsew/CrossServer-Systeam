/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.Gson;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultGsonObjectMapper
/*    */   implements JsonObjectMapper
/*    */ {
/* 15 */   private final Gson gson = new Gson();
/*    */ 
/*    */   
/*    */   public <T> T fromJson(String value, Class<T> valueType) {
/* 19 */     return (T)this.gson.fromJson(value, valueType);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toJson(Object value) {
/* 24 */     return this.gson.toJson(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\DefaultGsonObjectMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */