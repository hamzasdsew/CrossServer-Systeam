/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.aggr.AggregationBuilder;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.aggr.AggregationResult;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields.SchemaField;
/*    */ 
/*    */ 
/*    */ public interface RediSearchCommands
/*    */ {
/*    */   String ftCreate(String paramString, IndexOptions paramIndexOptions, Schema paramSchema);
/*    */   
/*    */   String ftCreate(String indexName, SchemaField... schemaFields) {
/* 18 */     return ftCreate(indexName, Arrays.asList(schemaFields));
/*    */   }
/*    */   
/*    */   String ftCreate(String indexName, FTCreateParams createParams, SchemaField... schemaFields) {
/* 22 */     return ftCreate(indexName, createParams, Arrays.asList(schemaFields));
/*    */   }
/*    */   
/*    */   default String ftCreate(String indexName, Iterable<SchemaField> schemaFields) {
/* 26 */     return ftCreate(indexName, FTCreateParams.createParams(), schemaFields);
/*    */   }
/*    */   
/*    */   String ftCreate(String paramString, FTCreateParams paramFTCreateParams, Iterable<SchemaField> paramIterable);
/*    */   
/*    */   String ftAlter(String indexName, Schema.Field... fields) {
/* 32 */     return ftAlter(indexName, Schema.from(fields));
/*    */   }
/*    */   
/*    */   String ftAlter(String paramString, Schema paramSchema);
/*    */   
/*    */   String ftAlter(String indexName, SchemaField... schemaFields) {
/* 38 */     return ftAlter(indexName, Arrays.asList(schemaFields));
/*    */   }
/*    */   
/*    */   String ftAlter(String paramString, Iterable<SchemaField> paramIterable);
/*    */   
/*    */   String ftAliasAdd(String paramString1, String paramString2);
/*    */   
/*    */   String ftAliasUpdate(String paramString1, String paramString2);
/*    */   
/*    */   String ftAliasDel(String paramString);
/*    */   
/*    */   String ftDropIndex(String paramString);
/*    */   
/*    */   String ftDropIndexDD(String paramString);
/*    */   
/*    */   default SearchResult ftSearch(String indexName) {
/* 54 */     return ftSearch(indexName, "*");
/*    */   }
/*    */   
/*    */   SearchResult ftSearch(String paramString1, String paramString2);
/*    */   
/*    */   SearchResult ftSearch(String paramString1, String paramString2, FTSearchParams paramFTSearchParams);
/*    */   
/*    */   SearchResult ftSearch(String paramString, Query paramQuery);
/*    */   
/*    */   @Deprecated
/*    */   SearchResult ftSearch(byte[] paramArrayOfbyte, Query paramQuery);
/*    */   
/*    */   String ftExplain(String paramString, Query paramQuery);
/*    */   
/*    */   List<String> ftExplainCLI(String paramString, Query paramQuery);
/*    */   
/*    */   AggregationResult ftAggregate(String paramString, AggregationBuilder paramAggregationBuilder);
/*    */   
/*    */   AggregationResult ftCursorRead(String paramString, long paramLong, int paramInt);
/*    */   
/*    */   String ftCursorDel(String paramString, long paramLong);
/*    */   
/*    */   Map.Entry<AggregationResult, Map<String, Object>> ftProfileAggregate(String paramString, FTProfileParams paramFTProfileParams, AggregationBuilder paramAggregationBuilder);
/*    */   
/*    */   Map.Entry<SearchResult, Map<String, Object>> ftProfileSearch(String paramString, FTProfileParams paramFTProfileParams, Query paramQuery);
/*    */   
/*    */   Map.Entry<SearchResult, Map<String, Object>> ftProfileSearch(String paramString1, FTProfileParams paramFTProfileParams, String paramString2, FTSearchParams paramFTSearchParams);
/*    */   
/*    */   String ftSynUpdate(String paramString1, String paramString2, String... paramVarArgs);
/*    */   
/*    */   Map<String, List<String>> ftSynDump(String paramString);
/*    */   
/*    */   long ftDictAdd(String paramString, String... paramVarArgs);
/*    */   
/*    */   long ftDictDel(String paramString, String... paramVarArgs);
/*    */   
/*    */   Set<String> ftDictDump(String paramString);
/*    */   
/*    */   long ftDictAddBySampleKey(String paramString1, String paramString2, String... paramVarArgs);
/*    */   
/*    */   long ftDictDelBySampleKey(String paramString1, String paramString2, String... paramVarArgs);
/*    */   
/*    */   Set<String> ftDictDumpBySampleKey(String paramString1, String paramString2);
/*    */   
/*    */   Map<String, Map<String, Double>> ftSpellCheck(String paramString1, String paramString2);
/*    */   
/*    */   Map<String, Map<String, Double>> ftSpellCheck(String paramString1, String paramString2, FTSpellCheckParams paramFTSpellCheckParams);
/*    */   
/*    */   Map<String, Object> ftInfo(String paramString);
/*    */   
/*    */   Set<String> ftTagVals(String paramString1, String paramString2);
/*    */   
/*    */   Map<String, Object> ftConfigGet(String paramString);
/*    */   
/*    */   Map<String, Object> ftConfigGet(String paramString1, String paramString2);
/*    */   
/*    */   String ftConfigSet(String paramString1, String paramString2);
/*    */   
/*    */   String ftConfigSet(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   long ftSugAdd(String paramString1, String paramString2, double paramDouble);
/*    */   
/*    */   long ftSugAddIncr(String paramString1, String paramString2, double paramDouble);
/*    */   
/*    */   List<String> ftSugGet(String paramString1, String paramString2);
/*    */   
/*    */   List<String> ftSugGet(String paramString1, String paramString2, boolean paramBoolean, int paramInt);
/*    */   
/*    */   List<Tuple> ftSugGetWithScores(String paramString1, String paramString2);
/*    */   
/*    */   List<Tuple> ftSugGetWithScores(String paramString1, String paramString2, boolean paramBoolean, int paramInt);
/*    */   
/*    */   boolean ftSugDel(String paramString1, String paramString2);
/*    */   
/*    */   long ftSugLen(String paramString);
/*    */   
/*    */   Set<String> ftList();
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\RediSearchCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */