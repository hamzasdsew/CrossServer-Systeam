/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.graph;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
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
/*    */ 
/*    */ @Deprecated
/*    */ public class GraphQueryParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean readonly;
/*    */   private String query;
/*    */   private Map<String, Object> params;
/*    */   private Long timeout;
/*    */   
/*    */   public GraphQueryParams() {}
/*    */   
/*    */   public static GraphQueryParams queryParams() {
/* 32 */     return new GraphQueryParams();
/*    */   }
/*    */   
/*    */   public GraphQueryParams(String query) {
/* 36 */     this.query = query;
/*    */   }
/*    */   
/*    */   public static GraphQueryParams queryParams(String query) {
/* 40 */     return new GraphQueryParams(query);
/*    */   }
/*    */   
/*    */   public GraphQueryParams readonly() {
/* 44 */     return readonly(true);
/*    */   }
/*    */   
/*    */   public GraphQueryParams readonly(boolean readonly) {
/* 48 */     this.readonly = readonly;
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public GraphQueryParams query(String queryStr) {
/* 53 */     this.query = queryStr;
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public GraphQueryParams params(Map<String, Object> params) {
/* 58 */     this.params = params;
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public GraphQueryParams addParam(String key, Object value) {
/* 63 */     if (this.params == null) this.params = new HashMap<>(); 
/* 64 */     this.params.put(key, value);
/* 65 */     return this;
/*    */   }
/*    */   
/*    */   public GraphQueryParams timeout(long timeout) {
/* 69 */     this.timeout = Long.valueOf(timeout);
/* 70 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 75 */     if (this.query == null) throw new JedisException("Query string must be set.");
/*    */     
/* 77 */     if (this.params == null) {
/* 78 */       args.add(this.query);
/*    */     } else {
/* 80 */       args.add(RedisGraphQueryUtil.prepareQuery(this.query, this.params));
/*    */     } 
/*    */     
/* 83 */     args.add(GraphProtocol.GraphKeyword.__COMPACT);
/*    */     
/* 85 */     if (this.timeout != null) {
/* 86 */       args.add(GraphProtocol.GraphKeyword.TIMEOUT).add(this.timeout).blocking();
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isReadonly() {
/* 91 */     return this.readonly;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\GraphQueryParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */