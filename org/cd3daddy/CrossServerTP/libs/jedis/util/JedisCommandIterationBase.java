/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Map;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.Queue;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
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
/*    */ public abstract class JedisCommandIterationBase<B, D>
/*    */ {
/*    */   private final Builder<B> builder;
/*    */   private final Queue<Map.Entry> connections;
/*    */   private Map.Entry connection;
/*    */   private B lastReply;
/*    */   private boolean roundRobinCompleted;
/*    */   private boolean iterationCompleted;
/*    */   
/*    */   protected JedisCommandIterationBase(ConnectionProvider connectionProvider, Builder<B> responseBuilder) {
/* 35 */     Map connectionMap = connectionProvider.getConnectionMap();
/* 36 */     ArrayList<Map.Entry> connectionList = new ArrayList<>(connectionMap.entrySet());
/* 37 */     Collections.shuffle(connectionList);
/* 38 */     this.connections = new LinkedList<>(connectionList);
/* 39 */     this.builder = responseBuilder;
/* 40 */     this.iterationCompleted = true;
/* 41 */     this.roundRobinCompleted = this.connections.isEmpty();
/*    */   }
/*    */   
/*    */   public final boolean isIterationCompleted() {
/* 45 */     return this.roundRobinCompleted;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract boolean isNodeCompleted(B paramB);
/*    */ 
/*    */   
/*    */   public final B nextBatch() {
/*    */     CommandArguments args;
/*    */     Object rawReply;
/* 55 */     if (this.roundRobinCompleted) {
/* 56 */       throw new NoSuchElementException();
/*    */     }
/*    */ 
/*    */     
/* 60 */     if (this.iterationCompleted) {
/* 61 */       this.connection = this.connections.poll();
/* 62 */       args = initCommandArguments();
/*    */     } else {
/* 64 */       args = nextCommandArguments(this.lastReply);
/*    */     } 
/*    */ 
/*    */     
/* 68 */     if (this.connection.getValue() instanceof Connection) {
/* 69 */       rawReply = ((Connection)this.connection.getValue()).executeCommand(args);
/* 70 */     } else if (this.connection.getValue() instanceof Pool) {
/* 71 */       try (Connection c = (Connection)((Pool<Connection>)this.connection.getValue()).getResource()) {
/* 72 */         rawReply = c.executeCommand(args);
/*    */       } 
/*    */     } else {
/* 75 */       throw new IllegalArgumentException(this.connection.getValue().getClass() + "is not supported.");
/*    */     } 
/*    */     
/* 78 */     this.lastReply = (B)this.builder.build(rawReply);
/* 79 */     this.iterationCompleted = isNodeCompleted(this.lastReply);
/* 80 */     if (this.iterationCompleted && 
/* 81 */       this.connections.isEmpty()) {
/* 82 */       this.roundRobinCompleted = true;
/*    */     }
/*    */     
/* 85 */     return this.lastReply;
/*    */   }
/*    */   protected abstract CommandArguments initCommandArguments();
/*    */   protected abstract CommandArguments nextCommandArguments(B paramB);
/*    */   protected abstract Collection<D> convertBatchToData(B paramB);
/*    */   public final Collection<D> nextBatchList() {
/* 91 */     return convertBatchToData(nextBatch());
/*    */   }
/*    */   
/*    */   public final Collection<D> collect(Collection<D> c) {
/* 95 */     while (!isIterationCompleted()) {
/* 96 */       c.addAll(nextBatchList());
/*    */     }
/* 98 */     return c;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\JedisCommandIterationBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */