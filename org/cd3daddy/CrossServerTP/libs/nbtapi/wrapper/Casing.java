/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper;
/*    */ 
/*    */ import java.util.function.UnaryOperator;
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
/*    */ 
/*    */ public enum Casing
/*    */ {
/* 31 */   lowercase(String::toLowerCase), PascalCase(String::toLowerCase), snake_case(String::toLowerCase), camelCase(String::toLowerCase), UPPERCASE(String::toUpperCase);
/*    */   
/*    */   private UnaryOperator<String> convert;
/*    */   
/*    */   Casing(UnaryOperator<String> function) {
/* 36 */     this.convert = function; }
/*    */   static { camelCase = new Casing("camelCase", 0, s -> (s.length() < 2) ? s.toLowerCase() : (Character.toLowerCase(s.charAt(0)) + s.substring(1))); snake_case = new Casing("snake_case", 1, s -> { StringBuilder result = new StringBuilder(); result.append(Character.toLowerCase(s.charAt(0))); for (int i = 1; i < s.length(); i++) { char currentChar = s.charAt(i); if (Character.isUpperCase(currentChar)) { result.append('_').append(Character.toLowerCase(currentChar)); } else { result.append(currentChar); }
/*    */              }
/*    */            return result.toString();
/* 40 */         }); PascalCase = new Casing("PascalCase", 2, s -> (s.length() < 2) ? s.toUpperCase() : (Character.toUpperCase(s.charAt(0)) + s.substring(1))); } public String convertString(String str) { return this.convert.apply(str); }
/*    */ 
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\wrapper\Casing.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */