/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
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
/*     */ public class Transaction
/*     */   extends TransactionBase
/*     */ {
/*  23 */   private final Queue<Response<?>> pipelinedResponses = new LinkedList<>();
/*     */   
/*  25 */   private Jedis jedis = null;
/*     */   
/*     */   protected final Connection connection;
/*     */   
/*     */   private final boolean closeConnection;
/*     */   
/*     */   private boolean broken = false;
/*     */   
/*     */   private boolean inWatch = false;
/*     */   private boolean inMulti = false;
/*     */   
/*     */   public Transaction(Jedis jedis) {
/*  37 */     this(jedis.getConnection());
/*  38 */     this.jedis = jedis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transaction(Connection connection) {
/*  49 */     this(connection, true);
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
/*     */   public Transaction(Connection connection, boolean doMulti) {
/*  62 */     this(connection, doMulti, false);
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
/*     */   public Transaction(Connection connection, boolean doMulti, boolean closeConnection) {
/*  76 */     this.connection = connection;
/*  77 */     this.closeConnection = closeConnection;
/*  78 */     setGraphCommands(new GraphCommandObjects(this.connection));
/*  79 */     if (doMulti) multi();
/*     */   
/*     */   }
/*     */   
/*     */   public final void multi() {
/*  84 */     this.connection.sendCommand(Protocol.Command.MULTI);
/*     */     
/*  86 */     this.inMulti = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public String watch(String... keys) {
/*  91 */     this.connection.sendCommand(Protocol.Command.WATCH, keys);
/*  92 */     String status = this.connection.getStatusCodeReply();
/*  93 */     this.inWatch = true;
/*  94 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String watch(byte[]... keys) {
/*  99 */     this.connection.sendCommand(Protocol.Command.WATCH, keys);
/* 100 */     String status = this.connection.getStatusCodeReply();
/* 101 */     this.inWatch = true;
/* 102 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   public String unwatch() {
/* 107 */     this.connection.sendCommand(Protocol.Command.UNWATCH);
/* 108 */     String status = this.connection.getStatusCodeReply();
/* 109 */     this.inWatch = false;
/* 110 */     return status;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
/* 115 */     this.connection.sendCommand(commandObject.getArguments());
/*     */     
/* 117 */     Response<T> response = new Response<>(commandObject.getBuilder());
/* 118 */     this.pipelinedResponses.add(response);
/* 119 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public final void close() {
/*     */     try {
/* 125 */       clear();
/*     */     } finally {
/* 127 */       if (this.closeConnection) {
/* 128 */         this.connection.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public final void clear() {
/* 135 */     if (this.broken) {
/*     */       return;
/*     */     }
/* 138 */     if (this.inMulti) {
/* 139 */       discard();
/* 140 */     } else if (this.inWatch) {
/* 141 */       unwatch();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Object> exec() {
/* 147 */     if (!this.inMulti) {
/* 148 */       throw new IllegalStateException("EXEC without MULTI");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 154 */       this.connection.getMany(1 + this.pipelinedResponses.size());
/*     */       
/* 156 */       this.connection.sendCommand(Protocol.Command.EXEC);
/*     */       
/* 158 */       List<Object> unformatted = this.connection.getObjectMultiBulkReply();
/* 159 */       if (unformatted == null) {
/* 160 */         this.pipelinedResponses.clear();
/* 161 */         return null;
/*     */       } 
/*     */       
/* 164 */       List<Object> formatted = new ArrayList(unformatted.size());
/* 165 */       for (Object o : unformatted) {
/*     */         try {
/* 167 */           Response<?> response = this.pipelinedResponses.poll();
/* 168 */           response.set(o);
/* 169 */           formatted.add(response.get());
/* 170 */         } catch (JedisDataException e) {
/* 171 */           formatted.add(e);
/*     */         } 
/*     */       } 
/* 174 */       return formatted;
/* 175 */     } catch (JedisConnectionException jce) {
/* 176 */       this.broken = true;
/* 177 */       throw jce;
/*     */     } finally {
/* 179 */       this.inMulti = false;
/* 180 */       this.inWatch = false;
/* 181 */       this.pipelinedResponses.clear();
/* 182 */       if (this.jedis != null) {
/* 183 */         this.jedis.resetState();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String discard() {
/* 190 */     if (!this.inMulti) {
/* 191 */       throw new IllegalStateException("DISCARD without MULTI");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 197 */       this.connection.getMany(1 + this.pipelinedResponses.size());
/*     */       
/* 199 */       this.connection.sendCommand(Protocol.Command.DISCARD);
/*     */       
/* 201 */       return this.connection.getStatusCodeReply();
/* 202 */     } catch (JedisConnectionException jce) {
/* 203 */       this.broken = true;
/* 204 */       throw jce;
/*     */     } finally {
/* 206 */       this.inMulti = false;
/* 207 */       this.inWatch = false;
/* 208 */       this.pipelinedResponses.clear();
/* 209 */       if (this.jedis != null)
/* 210 */         this.jedis.resetState(); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\Transaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */