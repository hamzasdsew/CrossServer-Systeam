/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Reducers
/*    */ {
/*    */   public static Reducer count() {
/* 13 */     return new Reducer("COUNT") {
/*    */         protected List<Object> getOwnArgs() {
/* 15 */           return Collections.emptyList();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   private static Reducer singleFieldReducer(String name, String field) {
/* 21 */     return new Reducer(name, field) {
/*    */         protected List<Object> getOwnArgs() {
/* 23 */           return Collections.emptyList();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static Reducer count_distinct(String field) {
/* 29 */     return singleFieldReducer("COUNT_DISTINCT", field);
/*    */   }
/*    */   
/*    */   public static Reducer count_distinctish(String field) {
/* 33 */     return singleFieldReducer("COUNT_DISTINCTISH", field);
/*    */   }
/*    */   
/*    */   public static Reducer sum(String field) {
/* 37 */     return singleFieldReducer("SUM", field);
/*    */   }
/*    */   
/*    */   public static Reducer min(String field) {
/* 41 */     return singleFieldReducer("MIN", field);
/*    */   }
/*    */   
/*    */   public static Reducer max(String field) {
/* 45 */     return singleFieldReducer("MAX", field);
/*    */   }
/*    */   
/*    */   public static Reducer avg(String field) {
/* 49 */     return singleFieldReducer("AVG", field);
/*    */   }
/*    */   
/*    */   public static Reducer stddev(String field) {
/* 53 */     return singleFieldReducer("STDDEV", field);
/*    */   }
/*    */   
/*    */   public static Reducer quantile(String field, final double percentile) {
/* 57 */     return new Reducer("QUANTILE", field) {
/*    */         protected List<Object> getOwnArgs() {
/* 59 */           return Arrays.asList(new Object[] { Double.valueOf(this.val$percentile) });
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static Reducer first_value(String field) {
/* 65 */     return singleFieldReducer("FIRST_VALUE", field);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Reducer first_value(String field, final SortedField sortBy) {
/* 76 */     return new Reducer("FIRST_VALUE", field) {
/*    */         protected List<Object> getOwnArgs() {
/* 78 */           return Arrays.asList(new Object[] { "BY", this.val$sortBy.getField(), this.val$sortBy.getOrder() });
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static Reducer to_list(String field) {
/* 84 */     return singleFieldReducer("TOLIST", field);
/*    */   }
/*    */   
/*    */   public static Reducer random_sample(String field, final int size) {
/* 88 */     return new Reducer("RANDOM_SAMPLE", field) {
/*    */         protected List<Object> getOwnArgs() {
/* 90 */           return Arrays.asList(new Object[] { Integer.valueOf(this.val$size) });
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\Reducers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */