/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
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
/*    */ public class ZIncrByParams
/*    */   implements IParams
/*    */ {
/*    */   private Protocol.Keyword existance;
/*    */   
/*    */   public static ZIncrByParams zIncrByParams() {
/* 24 */     return new ZIncrByParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZIncrByParams nx() {
/* 32 */     this.existance = Protocol.Keyword.NX;
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ZIncrByParams xx() {
/* 41 */     this.existance = Protocol.Keyword.XX;
/* 42 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 47 */     if (this.existance != null) {
/* 48 */       args.add(this.existance);
/*    */     }
/*    */     
/* 51 */     args.add(Protocol.Keyword.INCR);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ZIncrByParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */