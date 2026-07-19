/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.graph;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.function.Function;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandObject;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.providers.ConnectionProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class GraphCommandObjects
/*     */ {
/*     */   private final RedisGraphCommands graph;
/*     */   private final Connection connection;
/*     */   private final ConnectionProvider provider;
/*     */   private Function<ProtocolCommand, CommandArguments> commArgs;
/*     */   private final ConcurrentHashMap<String, Builder<ResultSet>> builders;
/*     */   
/*     */   public GraphCommandObjects(RedisGraphCommands graphCommands) {
/*  31 */     this.commArgs = (comm -> new CommandArguments(comm));
/*     */     
/*  33 */     this.builders = new ConcurrentHashMap<>();
/*     */ 
/*     */     
/*  36 */     this.graph = graphCommands;
/*  37 */     this.connection = null;
/*  38 */     this.provider = null;
/*     */   } public GraphCommandObjects(Connection connection) {
/*     */     this.commArgs = (comm -> new CommandArguments(comm));
/*     */     this.builders = new ConcurrentHashMap<>();
/*  42 */     this.graph = null;
/*  43 */     this.connection = connection;
/*  44 */     this.provider = null;
/*     */   } public GraphCommandObjects(ConnectionProvider provider) {
/*     */     this.commArgs = (comm -> new CommandArguments(comm));
/*     */     this.builders = new ConcurrentHashMap<>();
/*  48 */     this.graph = null;
/*  49 */     this.connection = null;
/*  50 */     this.provider = provider;
/*     */   }
/*     */   
/*     */   public void setBaseCommandArgumentsCreator(Function<ProtocolCommand, CommandArguments> commArgs) {
/*  54 */     this.commArgs = commArgs;
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ResultSet> graphQuery(String name, String query) {
/*  59 */     return new CommandObject(((CommandArguments)this.commArgs.apply(GraphProtocol.GraphCommand.QUERY)).key(name).add(query).add(GraphProtocol.GraphKeyword.__COMPACT), getBuilder(name));
/*     */   }
/*     */   
/*     */   public final CommandObject<ResultSet> graphReadonlyQuery(String name, String query) {
/*  63 */     return new CommandObject(((CommandArguments)this.commArgs.apply(GraphProtocol.GraphCommand.RO_QUERY)).key(name).add(query).add(GraphProtocol.GraphKeyword.__COMPACT), getBuilder(name));
/*     */   }
/*     */   
/*     */   public final CommandObject<ResultSet> graphQuery(String name, String query, long timeout) {
/*  67 */     return graphQuery(name, GraphQueryParams.queryParams(query).timeout(timeout));
/*     */   }
/*     */   
/*     */   public final CommandObject<ResultSet> graphReadonlyQuery(String name, String query, long timeout) {
/*  71 */     return graphQuery(name, GraphQueryParams.queryParams().readonly().query(query).timeout(timeout));
/*     */   }
/*     */   
/*     */   public final CommandObject<ResultSet> graphQuery(String name, String query, Map<String, Object> params) {
/*  75 */     return graphQuery(name, GraphQueryParams.queryParams(query).params(params));
/*     */   }
/*     */   
/*     */   public final CommandObject<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params) {
/*  79 */     return graphQuery(name, GraphQueryParams.queryParams().readonly().query(query).params(params));
/*     */   }
/*     */   
/*     */   public final CommandObject<ResultSet> graphQuery(String name, String query, Map<String, Object> params, long timeout) {
/*  83 */     return graphQuery(name, GraphQueryParams.queryParams(query).params(params).timeout(timeout));
/*     */   }
/*     */   
/*     */   public final CommandObject<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params, long timeout) {
/*  87 */     return graphQuery(name, GraphQueryParams.queryParams().readonly().query(query).params(params).timeout(timeout));
/*     */   }
/*     */   
/*     */   private CommandObject<ResultSet> graphQuery(String name, GraphQueryParams params) {
/*  91 */     return new CommandObject(((CommandArguments)this.commArgs
/*  92 */         .apply(!params.isReadonly() ? GraphProtocol.GraphCommand.QUERY : GraphProtocol.GraphCommand.RO_QUERY))
/*  93 */         .key(name).addParams(params), getBuilder(name));
/*     */   }
/*     */   
/*     */   public final CommandObject<String> graphDelete(String name) {
/*  97 */     return new CommandObject(((CommandArguments)this.commArgs.apply(GraphProtocol.GraphCommand.DELETE)).key(name), BuilderFactory.STRING);
/*     */   }
/*     */ 
/*     */   
/*     */   private Builder<ResultSet> getBuilder(String graphName) {
/* 102 */     if (!this.builders.containsKey(graphName)) {
/* 103 */       createBuilder(graphName);
/*     */     }
/* 105 */     return this.builders.get(graphName);
/*     */   }
/*     */   
/*     */   private void createBuilder(String graphName) {
/* 109 */     synchronized (this.builders) {
/* 110 */       this.builders.putIfAbsent(graphName, new ResultSetBuilder(new GraphCacheImpl(graphName)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private class GraphCacheImpl
/*     */     implements GraphCache {
/*     */     private final GraphCommandObjects.GraphCacheList labels;
/*     */     private final GraphCommandObjects.GraphCacheList propertyNames;
/*     */     private final GraphCommandObjects.GraphCacheList relationshipTypes;
/*     */     
/*     */     public GraphCacheImpl(String graphName) {
/* 121 */       this.labels = new GraphCommandObjects.GraphCacheList(graphName, "db.labels");
/* 122 */       this.propertyNames = new GraphCommandObjects.GraphCacheList(graphName, "db.propertyKeys");
/* 123 */       this.relationshipTypes = new GraphCommandObjects.GraphCacheList(graphName, "db.relationshipTypes");
/*     */     }
/*     */ 
/*     */     
/*     */     public String getLabel(int index) {
/* 128 */       return this.labels.getCachedData(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getRelationshipType(int index) {
/* 133 */       return this.relationshipTypes.getCachedData(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPropertyName(int index) {
/* 138 */       return this.propertyNames.getCachedData(index);
/*     */     }
/*     */   }
/*     */   
/*     */   private class GraphCacheList
/*     */   {
/*     */     private final String name;
/*     */     private final String query;
/* 146 */     private final List<String> data = new CopyOnWriteArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GraphCacheList(String name, String procedure) {
/* 154 */       this.name = name;
/* 155 */       this.query = "CALL " + procedure + "()";
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getCachedData(int index) {
/* 166 */       if (index >= this.data.size()) {
/* 167 */         synchronized (this.data) {
/* 168 */           if (index >= this.data.size()) {
/* 169 */             getProcedureInfo();
/*     */           }
/*     */         } 
/*     */       }
/* 173 */       return this.data.get(index);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void getProcedureInfo() {
/* 181 */       ResultSet resultSet = callProcedure();
/* 182 */       Iterator<Record> it = resultSet.iterator();
/* 183 */       List<String> newData = new ArrayList<>();
/* 184 */       int i = 0;
/* 185 */       while (it.hasNext()) {
/* 186 */         Record record = it.next();
/* 187 */         if (i >= this.data.size()) {
/* 188 */           newData.add(record.getString(0));
/*     */         }
/* 190 */         i++;
/*     */       } 
/* 192 */       this.data.addAll(newData);
/*     */     }
/*     */ 
/*     */     
/*     */     private ResultSet callProcedure() {
/* 197 */       if (GraphCommandObjects.this.graph != null) {
/* 198 */         return GraphCommandObjects.this.graph.graphQuery(this.name, this.query);
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 204 */       CommandObject<ResultSet> commandObject = new CommandObject((new CommandArguments(GraphProtocol.GraphCommand.QUERY)).key(this.name).add(this.query).add(GraphProtocol.GraphKeyword.__COMPACT), GraphCommandObjects.this.getBuilder(this.name));
/*     */       
/* 206 */       if (GraphCommandObjects.this.connection != null) {
/* 207 */         return (ResultSet)GraphCommandObjects.this.connection.executeCommand(commandObject);
/*     */       }
/* 209 */       try (Connection provided = GraphCommandObjects.this.provider.getConnection(commandObject.getArguments())) {
/* 210 */         return (ResultSet)provided.executeCommand(commandObject);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\GraphCommandObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */