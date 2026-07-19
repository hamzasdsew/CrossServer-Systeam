/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Node
/*    */ {
/*    */   String toString(Parenthesize paramParenthesize);
/*    */   
/*    */   String toString();
/*    */   
/*    */   public enum Parenthesize
/*    */   {
/* 17 */     ALWAYS,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 25 */     NEVER,
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     DEFAULT;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */