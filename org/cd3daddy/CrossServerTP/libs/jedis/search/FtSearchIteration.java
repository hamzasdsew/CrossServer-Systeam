/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.function.IntFunction;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.RedisProtocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisCommandIterationBase;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtSearchIteration
/*    */   extends JedisCommandIterationBase<SearchResult, Document>
/*    */ {
/*    */   private int batchStart;
/*    */   private final int batchSize;
/*    */   private final IntFunction<CommandArguments> args;
/*    */   
/*    */   public FtSearchIteration(ConnectionProvider connectionProvider, int batchSize, String indexName, String query, FTSearchParams params) {
/* 22 */     this(connectionProvider, null, batchSize, indexName, query, params);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FtSearchIteration(ConnectionProvider connectionProvider, int batchSize, String indexName, Query query) {
/* 29 */     this(connectionProvider, (RedisProtocol)null, batchSize, indexName, query);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FtSearchIteration(ConnectionProvider connectionProvider, RedisProtocol protocol, int batchSize, String indexName, String query, FTSearchParams params) {
/* 36 */     super(connectionProvider, (protocol == RedisProtocol.RESP3) ? SearchResult.SEARCH_RESULT_BUILDER : new SearchResult.SearchResultBuilder(
/* 37 */           !params.getNoContent(), params.getWithScores(), true));
/* 38 */     this.batchSize = batchSize;
/* 39 */     this.args = (limitFirst -> (new CommandArguments(SearchProtocol.SearchCommand.SEARCH)).add(indexName).add(query).addParams(params.limit(limitFirst, this.batchSize)));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FtSearchIteration(ConnectionProvider connectionProvider, RedisProtocol protocol, int batchSize, String indexName, Query query) {
/* 47 */     super(connectionProvider, (protocol == RedisProtocol.RESP3) ? SearchResult.SEARCH_RESULT_BUILDER : new SearchResult.SearchResultBuilder(
/* 48 */           !query.getNoContent(), query.getWithScores(), true));
/* 49 */     this.batchSize = batchSize;
/* 50 */     this.args = (limitFirst -> (new CommandArguments(SearchProtocol.SearchCommand.SEARCH)).add(indexName).addParams(query.limit(Integer.valueOf(limitFirst), Integer.valueOf(this.batchSize))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isNodeCompleted(SearchResult reply) {
/* 56 */     return (this.batchStart >= reply.getTotalResults() - this.batchSize);
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments initCommandArguments() {
/* 61 */     this.batchStart = 0;
/* 62 */     return this.args.apply(this.batchStart);
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments nextCommandArguments(SearchResult lastReply) {
/* 67 */     this.batchStart += this.batchSize;
/* 68 */     return this.args.apply(this.batchStart);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<Document> convertBatchToData(SearchResult batch) {
/* 73 */     return batch.getDocuments();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\FtSearchIteration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */