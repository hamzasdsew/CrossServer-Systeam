/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ import java.util.StringJoiner;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValueNode
/*    */   implements Node
/*    */ {
/*    */   private final Value[] values;
/*    */   private final String field;
/*    */   private final String joinString;
/*    */   
/*    */   public ValueNode(String field, String joinstr, Value... values) {
/* 15 */     this.field = field;
/* 16 */     this.values = values;
/* 17 */     this.joinString = joinstr;
/*    */   }
/*    */   
/*    */   private static Value[] fromStrings(String[] values) {
/* 21 */     Value[] objs = new Value[values.length];
/* 22 */     for (int i = 0; i < values.length; i++) {
/* 23 */       objs[i] = Values.value(values[i]);
/*    */     }
/* 25 */     return objs;
/*    */   }
/*    */   
/*    */   public ValueNode(String field, String joinstr, String... values) {
/* 29 */     this(field, joinstr, fromStrings(values));
/*    */   }
/*    */   
/*    */   private String formatField() {
/* 33 */     if (this.field == null || this.field.isEmpty()) {
/* 34 */       return "";
/*    */     }
/* 36 */     return '@' + this.field + ':';
/*    */   }
/*    */   
/*    */   private String toStringCombinable(Node.Parenthesize mode) {
/* 40 */     StringBuilder sb = new StringBuilder(formatField());
/* 41 */     if (this.values.length > 1 || mode == Node.Parenthesize.ALWAYS) {
/* 42 */       sb.append('(');
/*    */     }
/* 44 */     StringJoiner sj = new StringJoiner(this.joinString);
/* 45 */     for (Value v : this.values) {
/* 46 */       sj.add(v.toString());
/*    */     }
/* 48 */     sb.append(sj.toString());
/* 49 */     if (this.values.length > 1 || mode == Node.Parenthesize.ALWAYS) {
/* 50 */       sb.append(')');
/*    */     }
/* 52 */     return sb.toString();
/*    */   }
/*    */   
/*    */   private String toStringDefault(Node.Parenthesize mode) {
/* 56 */     boolean useParen = (mode == Node.Parenthesize.ALWAYS);
/* 57 */     if (!useParen) {
/* 58 */       useParen = (mode != Node.Parenthesize.NEVER && this.values.length > 1);
/*    */     }
/* 60 */     StringBuilder sb = new StringBuilder();
/* 61 */     if (useParen) {
/* 62 */       sb.append('(');
/*    */     }
/* 64 */     StringJoiner sj = new StringJoiner(this.joinString);
/* 65 */     for (Value v : this.values) {
/* 66 */       sj.add(formatField() + v.toString());
/*    */     }
/* 68 */     sb.append(sj.toString());
/* 69 */     if (useParen) {
/* 70 */       sb.append(')');
/*    */     }
/* 72 */     return sb.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString(Node.Parenthesize mode) {
/* 77 */     if (this.values[0].isCombinable()) {
/* 78 */       return toStringCombinable(mode);
/*    */     }
/* 80 */     return toStringDefault(mode);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\ValueNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */