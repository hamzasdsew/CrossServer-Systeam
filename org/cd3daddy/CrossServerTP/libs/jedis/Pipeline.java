/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.DatabasePipelineCommands;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.GraphCommandObjects;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.MigrateParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ public class Pipeline
/*     */   extends PipelineBase implements DatabasePipelineCommands, Closeable {
/*  17 */   private final Queue<Response<?>> pipelinedResponses = new LinkedList<>();
/*     */   
/*     */   protected final Connection connection;
/*     */   private final boolean closeConnection;
/*     */   
/*     */   public Pipeline(Jedis jedis) {
/*  23 */     this(jedis.getConnection(), false);
/*     */   }
/*     */   
/*     */   public Pipeline(Connection connection) {
/*  27 */     this(connection, false);
/*     */   }
/*     */   
/*     */   public Pipeline(Connection connection, boolean closeConnection) {
/*  31 */     super(new CommandObjects());
/*  32 */     this.connection = connection;
/*  33 */     this.closeConnection = closeConnection;
/*  34 */     RedisProtocol proto = this.connection.getRedisProtocol();
/*  35 */     if (proto != null) this.commandObjects.setProtocol(proto); 
/*  36 */     setGraphCommands(new GraphCommandObjects(this.connection));
/*     */   }
/*     */ 
/*     */   
/*     */   public final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
/*  41 */     this.connection.sendCommand(commandObject.getArguments());
/*  42 */     Response<T> response = new Response<>(commandObject.getBuilder());
/*  43 */     this.pipelinedResponses.add(response);
/*  44 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  49 */     sync();
/*     */     
/*  51 */     if (this.closeConnection) {
/*  52 */       this.connection.close();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sync() {
/*  63 */     if (!hasPipelinedResponse())
/*  64 */       return;  List<Object> unformatted = this.connection.getMany(this.pipelinedResponses.size());
/*  65 */     for (Object rawReply : unformatted) {
/*  66 */       ((Response)this.pipelinedResponses.poll()).set(rawReply);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Object> syncAndReturnAll() {
/*  77 */     if (hasPipelinedResponse()) {
/*  78 */       List<Object> unformatted = this.connection.getMany(this.pipelinedResponses.size());
/*  79 */       List<Object> formatted = new ArrayList();
/*  80 */       for (Object rawReply : unformatted) {
/*     */         try {
/*  82 */           Response<?> response = this.pipelinedResponses.poll();
/*  83 */           response.set(rawReply);
/*  84 */           formatted.add(response.get());
/*  85 */         } catch (JedisDataException e) {
/*  86 */           formatted.add(e);
/*     */         } 
/*     */       } 
/*  89 */       return formatted;
/*     */     } 
/*  91 */     return Collections.emptyList();
/*     */   }
/*     */ 
/*     */   
/*     */   public final boolean hasPipelinedResponse() {
/*  96 */     return (this.pipelinedResponses.size() > 0);
/*     */   }
/*     */   
/*     */   public Response<Long> waitReplicas(int replicas, long timeout) {
/* 100 */     return appendCommand(this.commandObjects.waitReplicas(replicas, timeout));
/*     */   }
/*     */   
/*     */   public Response<KeyValue<Long, Long>> waitAOF(long numLocal, long numReplicas, long timeout) {
/* 104 */     return appendCommand(this.commandObjects.waitAOF(numLocal, numReplicas, timeout));
/*     */   }
/*     */   
/*     */   public Response<List<String>> time() {
/* 108 */     return appendCommand(new CommandObject<>(this.commandObjects.commandArguments(Protocol.Command.TIME), BuilderFactory.STRING_LIST));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> select(int index) {
/* 113 */     return appendCommand(new CommandObject<>(this.commandObjects.commandArguments(Protocol.Command.SELECT).add(Integer.valueOf(index)), BuilderFactory.STRING));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<Long> dbSize() {
/* 118 */     return appendCommand(new CommandObject<>(this.commandObjects.commandArguments(Protocol.Command.DBSIZE), BuilderFactory.LONG));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> swapDB(int index1, int index2) {
/* 123 */     return appendCommand(new CommandObject<>(this.commandObjects.commandArguments(Protocol.Command.SWAPDB)
/* 124 */           .add(Integer.valueOf(index1)).add(Integer.valueOf(index2)), BuilderFactory.STRING));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<Long> move(String key, int dbIndex) {
/* 129 */     return appendCommand(new CommandObject<>(this.commandObjects.commandArguments(Protocol.Command.MOVE)
/* 130 */           .key(key).add(Integer.valueOf(dbIndex)), BuilderFactory.LONG));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<Long> move(byte[] key, int dbIndex) {
/* 135 */     return appendCommand(new CommandObject<>(this.commandObjects.commandArguments(Protocol.Command.MOVE)
/* 136 */           .key(key).add(Integer.valueOf(dbIndex)), BuilderFactory.LONG));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<Boolean> copy(String srcKey, String dstKey, int db, boolean replace) {
/* 141 */     return appendCommand(this.commandObjects.copy(srcKey, dstKey, db, replace));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<Boolean> copy(byte[] srcKey, byte[] dstKey, int db, boolean replace) {
/* 146 */     return appendCommand(this.commandObjects.copy(srcKey, dstKey, db, replace));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> migrate(String host, int port, byte[] key, int destinationDB, int timeout) {
/* 151 */     return appendCommand(this.commandObjects.migrate(host, port, key, destinationDB, timeout));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> migrate(String host, int port, String key, int destinationDB, int timeout) {
/* 156 */     return appendCommand(this.commandObjects.migrate(host, port, key, destinationDB, timeout));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> migrate(String host, int port, int destinationDB, int timeout, MigrateParams params, byte[]... keys) {
/* 161 */     return appendCommand(this.commandObjects.migrate(host, port, destinationDB, timeout, params, keys));
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> migrate(String host, int port, int destinationDB, int timeout, MigrateParams params, String... keys) {
/* 166 */     return appendCommand(this.commandObjects.migrate(host, port, destinationDB, timeout, params, keys));
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\Pipeline.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */