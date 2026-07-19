/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.StringJoiner;
/*    */ 
/*    */ public abstract class QueryNode
/*    */   implements Node {
/* 11 */   private final List<Node> children = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract String getJoinString();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueryNode add(String field, Value... values) {
/* 23 */     this.children.add(new ValueNode(field, getJoinString(), values));
/* 24 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueryNode add(String field, String... values) {
/* 35 */     this.children.add(new ValueNode(field, getJoinString(), values));
/* 36 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueryNode add(String field, Collection<Value> values) {
/* 47 */     return add(field, values.<Value>toArray(new Value[0]));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public QueryNode add(Node... nodes) {
/* 57 */     this.children.addAll(Arrays.asList(nodes));
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   protected boolean shouldParenthesize(Node.Parenthesize mode) {
/* 62 */     if (mode == Node.Parenthesize.ALWAYS) {
/* 63 */       return true;
/*    */     }
/* 65 */     if (mode == Node.Parenthesize.NEVER) {
/* 66 */       return false;
/*    */     }
/* 68 */     return (this.children.size() > 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(Node.Parenthesize parenMode) {
/* 73 */     StringBuilder sb = new StringBuilder();
/* 74 */     StringJoiner sj = new StringJoiner(getJoinString());
/* 75 */     if (shouldParenthesize(parenMode)) {
/* 76 */       sb.append('(');
/*    */     }
/* 78 */     for (Node n : this.children) {
/* 79 */       sj.add(n.toString(parenMode));
/*    */     }
/* 81 */     sb.append(sj.toString());
/* 82 */     if (shouldParenthesize(parenMode)) {
/* 83 */       sb.append(')');
/*    */     }
/* 85 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return toString(Node.Parenthesize.DEFAULT);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\QueryNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */