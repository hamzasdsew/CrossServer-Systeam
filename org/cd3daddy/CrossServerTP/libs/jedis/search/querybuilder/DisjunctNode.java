/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search.querybuilder;
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
/*    */ public class DisjunctNode
/*    */   extends IntersectNode
/*    */ {
/*    */   public String toString(Node.Parenthesize mode) {
/* 17 */     String ret = super.toString(Node.Parenthesize.NEVER);
/* 18 */     if (shouldParenthesize(mode)) {
/* 19 */       return "-(" + ret + ")";
/*    */     }
/* 21 */     return "-" + ret;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\DisjunctNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */