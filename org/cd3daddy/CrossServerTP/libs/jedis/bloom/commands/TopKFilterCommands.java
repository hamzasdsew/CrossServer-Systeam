/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.bloom.commands;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface TopKFilterCommands
/*    */ {
/*    */   String topkReserve(String paramString, long paramLong);
/*    */   
/*    */   String topkReserve(String paramString, long paramLong1, long paramLong2, long paramLong3, double paramDouble);
/*    */   
/*    */   List<String> topkAdd(String paramString, String... paramVarArgs);
/*    */   
/*    */   default String topkIncrBy(String key, String item, long increment) {
/* 48 */     return topkIncrBy(key, Collections.singletonMap(item, Long.valueOf(increment))).get(0);
/*    */   }
/*    */   
/*    */   List<String> topkIncrBy(String paramString, Map<String, Long> paramMap);
/*    */   
/*    */   List<Boolean> topkQuery(String paramString, String... paramVarArgs);
/*    */   
/*    */   List<String> topkList(String paramString);
/*    */   
/*    */   Map<String, Long> topkListWithCount(String paramString);
/*    */   
/*    */   Map<String, Object> topkInfo(String paramString);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\TopKFilterCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */