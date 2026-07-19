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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CountMinSketchCommands
/*    */ {
/*    */   String cmsInitByDim(String paramString, long paramLong1, long paramLong2);
/*    */   
/*    */   String cmsInitByProb(String paramString, double paramDouble1, double paramDouble2);
/*    */   
/*    */   default long cmsIncrBy(String key, String item, long increment) {
/* 52 */     return ((Long)cmsIncrBy(key, Collections.singletonMap(item, Long.valueOf(increment))).get(0)).longValue();
/*    */   }
/*    */   
/*    */   List<Long> cmsIncrBy(String paramString, Map<String, Long> paramMap);
/*    */   
/*    */   List<Long> cmsQuery(String paramString, String... paramVarArgs);
/*    */   
/*    */   String cmsMerge(String paramString, String... paramVarArgs);
/*    */   
/*    */   String cmsMerge(String paramString, Map<String, Long> paramMap);
/*    */   
/*    */   Map<String, Object> cmsInfo(String paramString);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\commands\CountMinSketchCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */