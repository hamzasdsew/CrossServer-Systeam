/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.function.Function;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisCommandIterationBase;
/*    */ 
/*    */ 
/*    */ public class ScanIteration
/*    */   extends JedisCommandIterationBase<ScanResult<String>, String>
/*    */ {
/*    */   private final int count;
/*    */   private final Function<String, CommandArguments> args;
/*    */   
/*    */   public ScanIteration(ConnectionProvider connectionProvider, int batchCount, String match) {
/* 18 */     super(connectionProvider, BuilderFactory.SCAN_RESPONSE);
/* 19 */     this.count = batchCount;
/* 20 */     this.args = (cursor -> (new CommandArguments(Protocol.Command.SCAN)).add(cursor).add(Protocol.Keyword.MATCH).add(match).add(Protocol.Keyword.COUNT).add(Integer.valueOf(this.count)));
/*    */   }
/*    */ 
/*    */   
/*    */   public ScanIteration(ConnectionProvider connectionProvider, int batchCount, String match, String type) {
/* 25 */     super(connectionProvider, BuilderFactory.SCAN_RESPONSE);
/* 26 */     this.count = batchCount;
/* 27 */     this.args = (cursor -> (new CommandArguments(Protocol.Command.SCAN)).add(cursor).add(Protocol.Keyword.MATCH).add(match).add(Protocol.Keyword.COUNT).add(Integer.valueOf(this.count)).add(Protocol.Keyword.TYPE).add(type));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean isNodeCompleted(ScanResult<String> reply) {
/* 33 */     return reply.isCompleteIteration();
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments initCommandArguments() {
/* 38 */     return this.args.apply(ScanParams.SCAN_POINTER_START);
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments nextCommandArguments(ScanResult<String> lastReply) {
/* 43 */     return this.args.apply(lastReply.getCursor());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Collection<String> convertBatchToData(ScanResult<String> batch) {
/* 48 */     return batch.getResult();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ScanIteration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */