/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.aggr.AggregationBuilder;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.aggr.AggregationResult;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields.SchemaField;
/*    */ 
/*    */ 
/*    */ public interface RediSearchPipelineCommands
/*    */ {
/*    */   Response<String> ftCreate(String paramString, IndexOptions paramIndexOptions, Schema paramSchema);
/*    */   
/*    */   Response<String> ftCreate(String indexName, SchemaField... schemaFields) {
/* 19 */     return ftCreate(indexName, Arrays.asList(schemaFields));
/*    */   }
/*    */   
/*    */   Response<String> ftCreate(String indexName, FTCreateParams createParams, SchemaField... schemaFields) {
/* 23 */     return ftCreate(indexName, createParams, Arrays.asList(schemaFields));
/*    */   }
/*    */   
/*    */   default Response<String> ftCreate(String indexName, Iterable<SchemaField> schemaFields) {
/* 27 */     return ftCreate(indexName, FTCreateParams.createParams(), schemaFields);
/*    */   }
/*    */   
/*    */   Response<String> ftCreate(String paramString, FTCreateParams paramFTCreateParams, Iterable<SchemaField> paramIterable);
/*    */   
/*    */   Response<String> ftAlter(String indexName, Schema.Field... fields) {
/* 33 */     return ftAlter(indexName, Schema.from(fields));
/*    */   }
/*    */   
/*    */   Response<String> ftAlter(String paramString, Schema paramSchema);
/*    */   
/*    */   Response<String> ftAlter(String indexName, SchemaField... schemaFields) {
/* 39 */     return ftAlter(indexName, Arrays.asList(schemaFields));
/*    */   }
/*    */   
/*    */   Response<String> ftAlter(String paramString, Iterable<SchemaField> paramIterable);
/*    */   
/*    */   Response<String> ftAliasAdd(String paramString1, String paramString2);
/*    */   
/*    */   Response<String> ftAliasUpdate(String paramString1, String paramString2);
/*    */   
/*    */   Response<String> ftAliasDel(String paramString);
/*    */   
/*    */   Response<String> ftDropIndex(String paramString);
/*    */   
/*    */   Response<String> ftDropIndexDD(String paramString);
/*    */   
/*    */   default Response<SearchResult> ftSearch(String indexName) {
/* 55 */     return ftSearch(indexName, "*");
/*    */   }
/*    */   
/*    */   Response<SearchResult> ftSearch(String paramString1, String paramString2);
/*    */   
/*    */   Response<SearchResult> ftSearch(String paramString1, String paramString2, FTSearchParams paramFTSearchParams);
/*    */   
/*    */   Response<SearchResult> ftSearch(String paramString, Query paramQuery);
/*    */   
/*    */   @Deprecated
/*    */   Response<SearchResult> ftSearch(byte[] paramArrayOfbyte, Query paramQuery);
/*    */   
/*    */   Response<String> ftExplain(String paramString, Query paramQuery);
/*    */   
/*    */   Response<List<String>> ftExplainCLI(String paramString, Query paramQuery);
/*    */   
/*    */   Response<AggregationResult> ftAggregate(String paramString, AggregationBuilder paramAggregationBuilder);
/*    */   
/*    */   Response<String> ftSynUpdate(String paramString1, String paramString2, String... paramVarArgs);
/*    */   
/*    */   Response<Map<String, List<String>>> ftSynDump(String paramString);
/*    */   
/*    */   Response<Long> ftDictAdd(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Long> ftDictDel(String paramString, String... paramVarArgs);
/*    */   
/*    */   Response<Set<String>> ftDictDump(String paramString);
/*    */   
/*    */   Response<Long> ftDictAddBySampleKey(String paramString1, String paramString2, String... paramVarArgs);
/*    */   
/*    */   Response<Long> ftDictDelBySampleKey(String paramString1, String paramString2, String... paramVarArgs);
/*    */   
/*    */   Response<Set<String>> ftDictDumpBySampleKey(String paramString1, String paramString2);
/*    */   
/*    */   Response<Map<String, Map<String, Double>>> ftSpellCheck(String paramString1, String paramString2);
/*    */   
/*    */   Response<Map<String, Map<String, Double>>> ftSpellCheck(String paramString1, String paramString2, FTSpellCheckParams paramFTSpellCheckParams);
/*    */   
/*    */   Response<Map<String, Object>> ftInfo(String paramString);
/*    */   
/*    */   Response<Set<String>> ftTagVals(String paramString1, String paramString2);
/*    */   
/*    */   Response<Map<String, Object>> ftConfigGet(String paramString);
/*    */   
/*    */   Response<Map<String, Object>> ftConfigGet(String paramString1, String paramString2);
/*    */   
/*    */   Response<String> ftConfigSet(String paramString1, String paramString2);
/*    */   
/*    */   Response<String> ftConfigSet(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   Response<Long> ftSugAdd(String paramString1, String paramString2, double paramDouble);
/*    */   
/*    */   Response<Long> ftSugAddIncr(String paramString1, String paramString2, double paramDouble);
/*    */   
/*    */   Response<List<String>> ftSugGet(String paramString1, String paramString2);
/*    */   
/*    */   Response<List<String>> ftSugGet(String paramString1, String paramString2, boolean paramBoolean, int paramInt);
/*    */   
/*    */   Response<List<Tuple>> ftSugGetWithScores(String paramString1, String paramString2);
/*    */   
/*    */   Response<List<Tuple>> ftSugGetWithScores(String paramString1, String paramString2, boolean paramBoolean, int paramInt);
/*    */   
/*    */   Response<Boolean> ftSugDel(String paramString1, String paramString2);
/*    */   
/*    */   Response<Long> ftSugLen(String paramString);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\RediSearchPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */