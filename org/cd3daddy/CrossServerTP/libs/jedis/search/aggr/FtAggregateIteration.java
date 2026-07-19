/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisCommandIterationBase;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FtAggregateIteration
/*    */   extends JedisCommandIterationBase<AggregationResult, Row>
/*    */ {
/*    */   private final String indexName;
/*    */   private final CommandArguments args;
/*    */   
/*    */   public FtAggregateIteration(ConnectionProvider connectionProvider, String indexName, AggregationBuilder aggr) {
/* 22 */     super(connectionProvider, AggregationResult.SEARCH_AGGREGATION_RESULT_WITH_CURSOR);
/* 23 */     if (!aggr.isWithCursor()) throw new IllegalArgumentException("cursor must be set"); 
/* 24 */     this.indexName = indexName;
/* 25 */     this.args = (new CommandArguments((ProtocolCommand)SearchProtocol.SearchCommand.AGGREGATE)).add(this.indexName).addParams(aggr);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNodeCompleted(AggregationResult reply) {
/* 30 */     return (reply.getCursorId().longValue() == 0L);
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments initCommandArguments() {
/* 35 */     return this.args;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments nextCommandArguments(AggregationResult lastReply) {
/* 40 */     return (new CommandArguments((ProtocolCommand)SearchProtocol.SearchCommand.CURSOR)).add(SearchProtocol.SearchKeyword.READ)
/* 41 */       .add(this.indexName).add(lastReply.getCursorId());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<Row> convertBatchToData(AggregationResult batch) {
/* 46 */     return batch.getRows();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\FtAggregateIteration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */