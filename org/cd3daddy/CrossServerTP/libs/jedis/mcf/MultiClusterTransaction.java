/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.mcf;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.RedisProtocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.TransactionBase;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.ResultSet;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.MultiClusterPooledConnectionProvider;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ public class MultiClusterTransaction
/*     */   extends TransactionBase
/*     */ {
/*  27 */   private static final Builder<?> NO_OP_BUILDER = BuilderFactory.RAW_OBJECT;
/*     */   
/*     */   private final CircuitBreakerFailoverConnectionProvider provider;
/*  30 */   private final AtomicInteger extraCommandCount = new AtomicInteger();
/*  31 */   private final Queue<KeyValue<CommandArguments, Response<?>>> commands = new LinkedList<>();
/*     */ 
/*     */   
/*     */   private boolean inWatch = false;
/*     */ 
/*     */   
/*     */   private boolean inMulti = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiClusterTransaction(MultiClusterPooledConnectionProvider provider) {
/*  42 */     this(provider, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiClusterTransaction(MultiClusterPooledConnectionProvider provider, boolean doMulti) {
/*  53 */     try (Connection connection = provider.getConnection()) {
/*  54 */       RedisProtocol proto = connection.getRedisProtocol();
/*  55 */       if (proto != null) this.commandObjects.setProtocol(proto);
/*     */     
/*     */     } 
/*  58 */     this.provider = new CircuitBreakerFailoverConnectionProvider(provider);
/*     */     
/*  60 */     if (doMulti) multi();
/*     */   
/*     */   }
/*     */   
/*     */   public final void multi() {
/*  65 */     appendCommand(new CommandObject(new CommandArguments((ProtocolCommand)Protocol.Command.MULTI), NO_OP_BUILDER));
/*  66 */     this.extraCommandCount.incrementAndGet();
/*  67 */     this.inMulti = true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String watch(String... keys) {
/*  76 */     appendCommand(new CommandObject((new CommandArguments((ProtocolCommand)Protocol.Command.WATCH)).addObjects((Object[])keys), NO_OP_BUILDER));
/*  77 */     this.extraCommandCount.incrementAndGet();
/*  78 */     this.inWatch = true;
/*  79 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String watch(byte[]... keys) {
/*  88 */     appendCommand(new CommandObject((new CommandArguments((ProtocolCommand)Protocol.Command.WATCH)).addObjects((Object[])keys), NO_OP_BUILDER));
/*  89 */     this.extraCommandCount.incrementAndGet();
/*  90 */     this.inWatch = true;
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final String unwatch() {
/*  99 */     appendCommand(new CommandObject(new CommandArguments((ProtocolCommand)Protocol.Command.UNWATCH), NO_OP_BUILDER));
/* 100 */     this.extraCommandCount.incrementAndGet();
/* 101 */     this.inWatch = false;
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected final <T> Response<T> appendCommand(CommandObject<T> commandObject) {
/* 107 */     CommandArguments args = commandObject.getArguments();
/* 108 */     Response<T> response = new Response(commandObject.getBuilder());
/* 109 */     this.commands.add(KeyValue.of(args, response));
/* 110 */     return response;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 115 */     clear();
/*     */   }
/*     */   
/*     */   private void clear() {
/* 119 */     if (this.inMulti) {
/* 120 */       discard();
/* 121 */     } else if (this.inWatch) {
/* 122 */       unwatch();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final List<Object> exec() {
/* 128 */     if (!this.inMulti) {
/* 129 */       throw new IllegalStateException("EXEC without MULTI");
/*     */     }
/*     */     
/* 132 */     try (Connection connection = this.provider.getConnection()) {
/*     */       
/* 134 */       this.commands.forEach(command -> connection.sendCommand((CommandArguments)command.getKey()));
/*     */ 
/*     */ 
/*     */       
/* 138 */       connection.getMany(this.commands.size());
/*     */ 
/*     */       
/* 141 */       for (int idx = 0; idx < this.extraCommandCount.get(); idx++) {
/* 142 */         this.commands.poll();
/*     */       }
/*     */       
/* 145 */       connection.sendCommand((ProtocolCommand)Protocol.Command.EXEC);
/*     */       
/* 147 */       List<Object> unformatted = connection.getObjectMultiBulkReply();
/* 148 */       if (unformatted == null) {
/* 149 */         this.commands.clear();
/* 150 */         return null;
/*     */       } 
/*     */       
/* 153 */       List<Object> formatted = new ArrayList(unformatted.size() - this.extraCommandCount.get());
/* 154 */       for (Object rawReply : unformatted) {
/*     */         try {
/* 156 */           Response<?> response = (Response)((KeyValue)this.commands.poll()).getValue();
/* 157 */           response.set(rawReply);
/* 158 */           formatted.add(response.get());
/* 159 */         } catch (JedisDataException e) {
/* 160 */           formatted.add(e);
/*     */         } 
/*     */       } 
/* 163 */       return formatted;
/*     */     } finally {
/*     */       
/* 166 */       this.inMulti = false;
/* 167 */       this.inWatch = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public final String discard() {
/* 173 */     if (!this.inMulti) {
/* 174 */       throw new IllegalStateException("DISCARD without MULTI");
/*     */     }
/*     */     
/* 177 */     try (Connection connection = this.provider.getConnection()) {
/*     */       
/* 179 */       this.commands.forEach(command -> connection.sendCommand((CommandArguments)command.getKey()));
/*     */ 
/*     */ 
/*     */       
/* 183 */       connection.getMany(this.commands.size());
/*     */       
/* 185 */       connection.sendCommand((ProtocolCommand)Protocol.Command.DISCARD);
/*     */       
/* 187 */       return connection.getStatusCodeReply();
/*     */     } finally {
/* 189 */       this.inMulti = false;
/* 190 */       this.inWatch = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query) {
/* 197 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query) {
/* 202 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query, long timeout) {
/* 207 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query, long timeout) {
/* 212 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query, Map<String, Object> params) {
/* 217 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params) {
/* 222 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 227 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 232 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<String> graphDelete(String name) {
/* 237 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Response<List<String>> graphProfile(String graphName, String query) {
/* 242 */     throw new UnsupportedOperationException("Graph commands are not supported.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\mcf\MultiClusterTransaction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */