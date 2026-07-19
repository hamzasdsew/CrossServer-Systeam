/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.GraphCommandObjects;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public abstract class MultiNodePipelineBase
/*     */   extends PipelineBase
/*     */ {
/*  23 */   private final Logger log = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  30 */   public static volatile int MULTI_NODE_PIPELINE_SYNC_WORKERS = 3;
/*     */   
/*     */   private final Map<HostAndPort, Queue<Response<?>>> pipelinedResponses;
/*     */   private final Map<HostAndPort, Connection> connections;
/*     */   private volatile boolean syncing = false;
/*     */   
/*     */   public MultiNodePipelineBase(CommandObjects commandObjects) {
/*  37 */     super(commandObjects);
/*  38 */     this.pipelinedResponses = new LinkedHashMap<>();
/*  39 */     this.connections = new LinkedHashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void prepareGraphCommands(ConnectionProvider connectionProvider) {
/*  47 */     GraphCommandObjects graphCommandObjects = new GraphCommandObjects(connectionProvider);
/*  48 */     graphCommandObjects.setBaseCommandArgumentsCreator(comm -> this.commandObjects.commandArguments(comm));
/*  49 */     setGraphCommands(graphCommandObjects);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
/*     */     Queue<Response<?>> queue;
/*     */     Connection connection;
/*  58 */     HostAndPort nodeKey = getNodeKey(commandObject.getArguments());
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (this.pipelinedResponses.containsKey(nodeKey)) {
/*  63 */       queue = this.pipelinedResponses.get(nodeKey);
/*  64 */       connection = this.connections.get(nodeKey);
/*     */     } else {
/*  66 */       this.pipelinedResponses.putIfAbsent(nodeKey, new LinkedList<>());
/*  67 */       queue = this.pipelinedResponses.get(nodeKey);
/*     */       
/*  69 */       Connection newOne = getConnection(nodeKey);
/*  70 */       this.connections.putIfAbsent(nodeKey, newOne);
/*  71 */       connection = this.connections.get(nodeKey);
/*  72 */       if (connection != newOne) {
/*  73 */         this.log.debug("Duplicate connection to {}, closing it.", nodeKey);
/*  74 */         IOUtils.closeQuietly(newOne);
/*     */       } 
/*     */     } 
/*     */     
/*  78 */     connection.sendCommand(commandObject.getArguments());
/*  79 */     Response<T> response = new Response<>(commandObject.getBuilder());
/*  80 */     queue.add(response);
/*  81 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*     */     try {
/*  87 */       sync();
/*     */     } finally {
/*  89 */       this.connections.values().forEach(IOUtils::closeQuietly);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final void sync() {
/*  95 */     if (this.syncing) {
/*     */       return;
/*     */     }
/*  98 */     this.syncing = true;
/*     */     
/* 100 */     ExecutorService executorService = Executors.newFixedThreadPool(MULTI_NODE_PIPELINE_SYNC_WORKERS);
/*     */     
/* 102 */     CountDownLatch countDownLatch = new CountDownLatch(this.pipelinedResponses.size());
/*     */     
/* 104 */     Iterator<Map.Entry<HostAndPort, Queue<Response<?>>>> pipelinedResponsesIterator = this.pipelinedResponses.entrySet().iterator();
/* 105 */     while (pipelinedResponsesIterator.hasNext()) {
/* 106 */       Map.Entry<HostAndPort, Queue<Response<?>>> entry = pipelinedResponsesIterator.next();
/* 107 */       HostAndPort nodeKey = entry.getKey();
/* 108 */       Queue<Response<?>> queue = entry.getValue();
/* 109 */       Connection connection = this.connections.get(nodeKey);
/* 110 */       executorService.submit(() -> {
/*     */             try {
/*     */               List<Object> unformatted = connection.getMany(queue.size());
/*     */               for (Object o : unformatted) {
/*     */                 ((Response)queue.poll()).set(o);
/*     */               }
/* 116 */             } catch (JedisConnectionException jce) {
/*     */               this.log.error("Error with connection to " + nodeKey, (Throwable)jce);
/*     */               
/*     */               pipelinedResponsesIterator.remove();
/*     */               
/*     */               this.connections.remove(nodeKey);
/*     */               IOUtils.closeQuietly(connection);
/*     */             } finally {
/*     */               countDownLatch.countDown();
/*     */             } 
/*     */           });
/*     */     } 
/*     */     try {
/* 129 */       countDownLatch.await();
/* 130 */     } catch (InterruptedException e) {
/* 131 */       this.log.error("Thread is interrupted during sync.", e);
/*     */     } 
/*     */     
/* 134 */     executorService.shutdownNow();
/*     */     
/* 136 */     this.syncing = false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Response<Long> waitReplicas(int replicas, long timeout) {
/* 141 */     return appendCommand(this.commandObjects.waitReplicas(replicas, timeout));
/*     */   }
/*     */   
/*     */   protected abstract HostAndPort getNodeKey(CommandArguments paramCommandArguments);
/*     */   
/*     */   protected abstract Connection getConnection(HostAndPort paramHostAndPort);
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\MultiNodePipelineBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */