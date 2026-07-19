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
/*    */ public class OptionalNode
/*    */   extends IntersectNode
/*    */ {
/*    */   public String toString(Node.Parenthesize mode) {
/* 16 */     String ret = super.toString(Node.Parenthesize.NEVER);
/* 17 */     if (shouldParenthesize(mode)) {
/* 18 */       return "~(" + ret + ")";
/*    */     }
/* 20 */     return "~" + ret;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\querybuilder\OptionalNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */