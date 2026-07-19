/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.graph;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class RedisGraphQueryUtil
/*    */ {
/* 15 */   public static final List<String> DUMMY_LIST = Collections.emptyList();
/* 16 */   public static final Map<String, List<String>> DUMMY_MAP = Collections.emptyMap();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final String COMPACT_STRING = "--COMPACT";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final String TIMEOUT_STRING = "TIMEOUT";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static String prepareQuery(String query, Map<String, Object> params) {
/* 33 */     StringBuilder sb = new StringBuilder("CYPHER ");
/* 34 */     for (Map.Entry<String, Object> entry : params.entrySet()) {
/* 35 */       sb
/* 36 */         .append(entry.getKey())
/* 37 */         .append('=')
/* 38 */         .append(valueToString(entry.getValue()))
/* 39 */         .append(' ');
/*    */     }
/* 41 */     sb.append(query);
/* 42 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private static String valueToString(Object value) {
/* 46 */     if (value == null) {
/* 47 */       return "null";
/*    */     }
/*    */     
/* 50 */     if (value instanceof String) {
/* 51 */       return quoteString((String)value);
/*    */     }
/* 53 */     if (value instanceof Character) {
/* 54 */       return quoteString(((Character)value).toString());
/*    */     }
/*    */     
/* 57 */     if (value instanceof Object[]) {
/* 58 */       return arrayToString((Object[])value);
/*    */     }
/* 60 */     if (value instanceof List) {
/* 61 */       return arrayToString((List<Object>)value);
/*    */     }
/* 63 */     return value.toString();
/*    */   }
/*    */   
/*    */   private static String quoteString(String str) {
/* 67 */     StringBuilder sb = new StringBuilder(str.length() + 12);
/* 68 */     sb.append('"');
/* 69 */     sb.append(str.replace("\"", "\\\""));
/* 70 */     sb.append('"');
/* 71 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String arrayToString(Object[] arr) {
/* 79 */     return arrayToString(Arrays.asList(arr));
/*    */   }
/*    */   
/*    */   private static String arrayToString(List<Object> arr) {
/* 83 */     StringBuilder sb = (new StringBuilder()).append('[');
/* 84 */     sb.append(String.join(", ", (Iterable<? extends CharSequence>)arr.stream().map(RedisGraphQueryUtil::valueToString).collect(Collectors.toList())));
/* 85 */     sb.append(']');
/* 86 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\RedisGraphQueryUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */