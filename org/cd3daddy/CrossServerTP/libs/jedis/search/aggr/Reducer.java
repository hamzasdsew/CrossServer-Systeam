/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Reducer
/*    */ {
/*    */   private final String name;
/*    */   private final String field;
/*    */   private String alias;
/*    */   
/*    */   protected Reducer(String name) {
/* 18 */     this.name = name;
/* 19 */     this.field = null;
/*    */   }
/*    */   
/*    */   protected Reducer(String name, String field) {
/* 23 */     this.name = name;
/* 24 */     this.field = field;
/*    */   }
/*    */   
/*    */   public final Reducer as(String alias) {
/* 28 */     this.alias = alias;
/* 29 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract List<Object> getOwnArgs();
/*    */   
/*    */   public final void addArgs(List<Object> args) {
/* 36 */     args.add(SearchProtocol.SearchKeyword.REDUCE);
/* 37 */     args.add(this.name);
/*    */     
/* 39 */     List<Object> ownArgs = getOwnArgs();
/* 40 */     if (this.field != null) {
/* 41 */       args.add(Integer.valueOf(1 + ownArgs.size()));
/* 42 */       args.add(this.field);
/*    */     } else {
/* 44 */       args.add(Integer.valueOf(ownArgs.size()));
/*    */     } 
/* 46 */     args.addAll(ownArgs);
/*    */     
/* 48 */     if (this.alias != null) {
/* 49 */       args.add(SearchProtocol.SearchKeyword.AS);
/* 50 */       args.add(this.alias);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\Reducer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */