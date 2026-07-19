/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class QueryBuilders
/*     */ {
/*     */   private QueryBuilders() {
/*  15 */     throw new InstantiationError("Must not instantiate this class");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QueryNode intersect(Node... n) {
/*  26 */     return (new IntersectNode()).add(n);
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
/*     */   public static QueryNode intersect(String field, Value... values) {
/*  39 */     return (new IntersectNode()).add(field, values);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QueryNode intersect(String field, String stringValue) {
/*  50 */     return intersect(field, new Value[] { Values.value(stringValue) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QueryNode union(Node... n) {
/*  60 */     return (new UnionNode()).add(n);
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
/*     */   public static QueryNode union(String field, Value... values) {
/*  72 */     return (new UnionNode()).add(field, values);
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
/*     */   public static QueryNode union(String field, String... values) {
/*  84 */     return union(field, (Value[])Arrays.<String>stream(values).map(Values::value).toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QueryNode disjunct(Node... n) {
/*  95 */     return (new DisjunctNode()).add(n);
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
/*     */   public static QueryNode disjunct(String field, Value... values) {
/* 107 */     return (new DisjunctNode()).add(field, values);
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
/*     */   public static QueryNode disjunct(String field, String... values) {
/* 119 */     return disjunct(field, (Value[])Arrays.<String>stream(values).map(Values::value).toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QueryNode disjunctUnion(Node... n) {
/* 130 */     return (new DisjunctUnionNode()).add(n);
/*     */   }
/*     */   
/*     */   public static QueryNode disjunctUnion(String field, Value... values) {
/* 134 */     return (new DisjunctUnionNode()).add(field, values);
/*     */   }
/*     */   
/*     */   public static QueryNode disjunctUnion(String field, String... values) {
/* 138 */     return disjunctUnion(field, (Value[])Arrays.<String>stream(values).map(Values::value).toArray());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static QueryNode optional(Node... n) {
/* 149 */     return (new OptionalNode()).add(n);
/*     */   }
/*     */   
/*     */   public static QueryNode optional(String field, Value... values) {
/* 153 */     return (new OptionalNode()).add(field, values);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\QueryBuilders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */