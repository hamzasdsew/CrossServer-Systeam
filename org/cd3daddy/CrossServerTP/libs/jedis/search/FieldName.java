/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ public class FieldName
/*    */   implements IParams
/*    */ {
/*    */   private final String name;
/*    */   private String attribute;
/*    */   
/*    */   public FieldName(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   public FieldName(String name, String attribute) {
/* 18 */     this.name = name;
/* 19 */     this.attribute = attribute;
/*    */   }
/*    */   
/*    */   public FieldName as(String attribute) {
/* 23 */     if (attribute == null) {
/* 24 */       throw new IllegalArgumentException("Setting null as field attribute is not allowed.");
/*    */     }
/* 26 */     if (this.attribute != null) {
/* 27 */       throw new IllegalStateException("Attribute for this field is already set.");
/*    */     }
/* 29 */     this.attribute = attribute;
/* 30 */     return this;
/*    */   }
/*    */   
/*    */   public final String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */   
/*    */   public final String getAttribute() {
/* 38 */     return this.attribute;
/*    */   }
/*    */   
/*    */   public int addCommandArguments(List<Object> args) {
/* 42 */     args.add(this.name);
/* 43 */     if (this.attribute == null) {
/* 44 */       return 1;
/*    */     }
/*    */     
/* 47 */     args.add(SearchProtocol.SearchKeyword.AS);
/* 48 */     args.add(this.attribute);
/* 49 */     return 3;
/*    */   }
/*    */   
/*    */   public int addCommandArguments(CommandArguments args) {
/* 53 */     args.add(this.name);
/* 54 */     if (this.attribute == null) {
/* 55 */       return 1;
/*    */     }
/*    */     
/* 58 */     args.add(SearchProtocol.SearchKeyword.AS);
/* 59 */     args.add(this.attribute);
/* 60 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 65 */     addCommandArguments(args);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 70 */     return (this.attribute == null) ? this.name : (this.name + " AS " + this.attribute);
/*    */   }
/*    */   
/*    */   public static FieldName of(String name) {
/* 74 */     return new FieldName(name);
/*    */   }
/*    */   
/*    */   public static FieldName[] convert(String... names) {
/* 78 */     if (names == null) {
/* 79 */       return null;
/*    */     }
/* 81 */     FieldName[] fields = new FieldName[names.length];
/* 82 */     for (int i = 0; i < names.length; i++) {
/* 83 */       fields[i] = of(names[i]);
/*    */     }
/* 85 */     return fields;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\FieldName.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */