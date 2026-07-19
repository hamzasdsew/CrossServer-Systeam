/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.mcf;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObjects;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.PipelineBase;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.RedisProtocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.ResultSet;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.MultiClusterPooledConnectionProvider;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ public class MultiClusterPipeline extends PipelineBase implements Closeable {
/*     */   private final CircuitBreakerFailoverConnectionProvider failoverProvider;
/*  21 */   private final Queue<KeyValue<CommandArguments, Response<?>>> commands = new LinkedList<>();
/*     */   
/*     */   public MultiClusterPipeline(MultiClusterPooledConnectionProvider pooledProvider) {
/*  24 */     super(new CommandObjects());
/*  25 */     try (Connection connection = pooledProvider.getConnection()) {
/*  26 */       RedisProtocol proto = connection.getRedisProtocol();
/*  27 */       if (proto != null) this.commandObjects.setProtocol(proto);
/*     */     
/*     */     } 
/*  30 */     this.failoverProvider = new CircuitBreakerFailoverConnectionProvider(pooledProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
/*  35 */     CommandArguments args = commandObject.getArguments();
/*  36 */     Response<T> response = new Response(commandObject.getBuilder());
/*  37 */     this.commands.add(KeyValue.of(args, response));
/*  38 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  43 */     sync();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sync() {
/*  53 */     if (this.commands.isEmpty())
/*     */       return; 
/*  55 */     try (Connection connection = this.failoverProvider.getConnection()) {
/*     */       
/*  57 */       this.commands.forEach(command -> connection.sendCommand((CommandArguments)command.getKey()));
/*     */ 
/*     */       
/*  60 */       List<Object> unformatted = connection.getMany(this.commands.size());
/*  61 */       unformatted.forEach(rawReply -> ((Response)((KeyValue)this.commands.poll()).getValue()).set(rawReply));
/*     */     } 
/*     */   }
/*     */   
/*     */   public Response<Long> waitReplicas(int replicas, long timeout) {
/*  66 */     return appendCommand(this.commandObjects.waitReplicas(replicas, timeout));
/*     */   }
/*     */   
/*     */   public Response<KeyValue<Long, Long>> waitAOF(long numLocal, long numReplicas, long timeout) {
/*  70 */     return appendCommand(this.commandObjects.waitAOF(numLocal, numReplicas, timeout));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query) {
/*  76 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query) {
/*  81 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query, long timeout) {
/*  86 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query, long timeout) {
/*  91 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query, Map<String, Object> params) {
/*  96 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params) {
/* 101 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 106 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 111 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> graphDelete(String name) {
/* 116 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<List<String>> graphProfile(String graphName, String query) {
/* 121 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\mcf\MultiClusterPipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */