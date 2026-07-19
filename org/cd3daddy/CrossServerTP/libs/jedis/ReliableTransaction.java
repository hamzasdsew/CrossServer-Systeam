/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.GraphCommandObjects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReliableTransaction
/*     */   extends TransactionBase
/*     */ {
/*     */   private static final String QUEUED_STR = "QUEUED";
/*  27 */   private final Queue<Response<?>> pipelinedResponses = new LinkedList<>();
/*     */ 
/*     */   
/*     */   protected final Connection connection;
/*     */   
/*     */   private final boolean closeConnection;
/*     */   
/*     */   private boolean broken = false;
/*     */   
/*     */   private boolean inWatch = false;
/*     */   
/*     */   private boolean inMulti = false;
/*     */ 
/*     */   
/*     */   public ReliableTransaction(Connection connection) {
/*  42 */     this(connection, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReliableTransaction(Connection connection, boolean doMulti) {
/*  55 */     this(connection, doMulti, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ReliableTransaction(Connection connection, boolean doMulti, boolean closeConnection) {
/*  69 */     this.connection = connection;
/*  70 */     this.closeConnection = closeConnection;
/*  71 */     setGraphCommands(new GraphCommandObjects(this.connection));
/*  72 */     if (doMulti) multi();
/*     */   
/*     */   }
/*     */   
/*     */   public final void multi() {
/*  77 */     this.connection.sendCommand(Protocol.Command.MULTI);
/*  78 */     String status = this.connection.getStatusCodeReply();
/*  79 */     if (!"OK".equals(status)) {
/*  80 */       throw new JedisException("MULTI command failed. Received response: " + status);
/*     */     }
/*  82 */     this.inMulti = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String watch(String... keys) {
/*  87 */     this.connection.sendCommand(Protocol.Command.WATCH, keys);
/*  88 */     String status = this.connection.getStatusCodeReply();
/*  89 */     this.inWatch = true;
/*  90 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String watch(byte[]... keys) {
/*  95 */     this.connection.sendCommand(Protocol.Command.WATCH, keys);
/*  96 */     String status = this.connection.getStatusCodeReply();
/*  97 */     this.inWatch = true;
/*  98 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String unwatch() {
/* 103 */     this.connection.sendCommand(Protocol.Command.UNWATCH);
/* 104 */     String status = this.connection.getStatusCodeReply();
/* 105 */     this.inWatch = false;
/* 106 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
/* 111 */     this.connection.sendCommand(commandObject.getArguments());
/* 112 */     String status = this.connection.getStatusCodeReply();
/* 113 */     if (!"QUEUED".equals(status)) {
/* 114 */       throw new JedisException(status);
/*     */     }
/* 116 */     Response<T> response = new Response<>(commandObject.getBuilder());
/* 117 */     this.pipelinedResponses.add(response);
/* 118 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/*     */     try {
/* 124 */       clear();
/*     */     } finally {
/* 126 */       if (this.closeConnection) {
/* 127 */         this.connection.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public final void clear() {
/* 134 */     if (this.broken) {
/*     */       return;
/*     */     }
/* 137 */     if (this.inMulti) {
/* 138 */       discard();
/* 139 */     } else if (this.inWatch) {
/* 140 */       unwatch();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Object> exec() {
/* 146 */     if (!this.inMulti) {
/* 147 */       throw new IllegalStateException("EXEC without MULTI");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 153 */       this.connection.sendCommand(Protocol.Command.EXEC);
/*     */       
/* 155 */       List<Object> unformatted = this.connection.getObjectMultiBulkReply();
/* 156 */       if (unformatted == null) {
/* 157 */         this.pipelinedResponses.clear();
/* 158 */         return null;
/*     */       } 
/*     */       
/* 161 */       List<Object> formatted = new ArrayList(unformatted.size());
/* 162 */       for (Object o : unformatted) {
/*     */         try {
/* 164 */           Response<?> response = this.pipelinedResponses.poll();
/* 165 */           response.set(o);
/* 166 */           formatted.add(response.get());
/* 167 */         } catch (JedisDataException e) {
/* 168 */           formatted.add(e);
/*     */         } 
/*     */       } 
/* 171 */       return formatted;
/* 172 */     } catch (JedisConnectionException jce) {
/* 173 */       this.broken = true;
/* 174 */       throw jce;
/*     */     } finally {
/* 176 */       this.inMulti = false;
/* 177 */       this.inWatch = false;
/* 178 */       this.pipelinedResponses.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String discard() {
/* 184 */     if (!this.inMulti) {
/* 185 */       throw new IllegalStateException("DISCARD without MULTI");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 191 */       this.connection.sendCommand(Protocol.Command.DISCARD);
/* 192 */       String status = this.connection.getStatusCodeReply();
/* 193 */       if (!"OK".equals(status)) {
/* 194 */         throw new JedisException("DISCARD command failed. Received response: " + status);
/*     */       }
/* 196 */       return status;
/* 197 */     } catch (JedisConnectionException jce) {
/* 198 */       this.broken = true;
/* 199 */       throw jce;
/*     */     } finally {
/* 201 */       this.inMulti = false;
/* 202 */       this.inWatch = false;
/* 203 */       this.pipelinedResponses.clear();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ReliableTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */