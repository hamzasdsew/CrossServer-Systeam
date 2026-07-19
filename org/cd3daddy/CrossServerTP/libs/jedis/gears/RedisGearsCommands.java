/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.gears;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.gears.resps.GearsLibraryInfo;
/*    */ 
/*    */ 
/*    */ public interface RedisGearsCommands
/*    */ {
/*    */   default String tFunctionLoad(String libraryCode) {
/* 10 */     return tFunctionLoad(libraryCode, TFunctionLoadParams.loadParams());
/*    */   }
/*    */   
/*    */   String tFunctionLoad(String paramString, TFunctionLoadParams paramTFunctionLoadParams);
/*    */   
/*    */   default List<GearsLibraryInfo> tFunctionList() {
/* 16 */     return tFunctionList(TFunctionListParams.listParams());
/*    */   }
/*    */   
/*    */   List<GearsLibraryInfo> tFunctionList(TFunctionListParams paramTFunctionListParams);
/*    */   
/*    */   String tFunctionDelete(String paramString);
/*    */   
/*    */   Object tFunctionCall(String paramString1, String paramString2, List<String> paramList1, List<String> paramList2);
/*    */   
/*    */   Object tFunctionCallAsync(String paramString1, String paramString2, List<String> paramList1, List<String> paramList2);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\RedisGearsCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */