/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Group
/*    */ {
/* 12 */   private final List<String> fields = new ArrayList<>();
/* 13 */   private final List<Reducer> reducers = new ArrayList<>();
/*    */   
/*    */   public Group(String... fields) {
/* 16 */     this.fields.addAll(Arrays.asList(fields));
/*    */   }
/*    */   
/*    */   public Group reduce(Reducer r) {
/* 20 */     this.reducers.add(r);
/* 21 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addArgs(List<Object> args) {
/* 26 */     args.add(Integer.valueOf(this.fields.size()));
/* 27 */     args.addAll(this.fields);
/*    */     
/* 29 */     this.reducers.forEach(r -> r.addArgs(args));
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\Group.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */