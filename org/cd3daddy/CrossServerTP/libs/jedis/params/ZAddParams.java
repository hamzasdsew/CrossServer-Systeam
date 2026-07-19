/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZAddParams
/*    */   implements IParams
/*    */ {
/*    */   private Protocol.Keyword existence;
/*    */   private Protocol.Keyword comparison;
/*    */   private boolean change;
/*    */   
/*    */   public static ZAddParams zAddParams() {
/* 16 */     return new ZAddParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZAddParams nx() {
/* 24 */     this.existence = Protocol.Keyword.NX;
/* 25 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZAddParams xx() {
/* 33 */     this.existence = Protocol.Keyword.XX;
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZAddParams gt() {
/* 42 */     this.comparison = Protocol.Keyword.GT;
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZAddParams lt() {
/* 51 */     this.comparison = Protocol.Keyword.LT;
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZAddParams ch() {
/* 61 */     this.change = true;
/* 62 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 67 */     if (this.existence != null) {
/* 68 */       args.add(this.existence);
/*    */     }
/* 70 */     if (this.comparison != null) {
/* 71 */       args.add(this.comparison);
/*    */     }
/* 73 */     if (this.change)
/* 74 */       args.add(Protocol.Keyword.CH); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ZAddParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */