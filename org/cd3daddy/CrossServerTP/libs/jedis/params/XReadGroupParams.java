/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ public class XReadGroupParams
/*    */   implements IParams {
/*  8 */   private Integer count = null;
/*  9 */   private Integer block = null;
/*    */   private boolean noack = false;
/*    */   
/*    */   public static XReadGroupParams xReadGroupParams() {
/* 13 */     return new XReadGroupParams();
/*    */   }
/*    */   
/*    */   public XReadGroupParams count(int count) {
/* 17 */     this.count = Integer.valueOf(count);
/* 18 */     return this;
/*    */   }
/*    */   
/*    */   public XReadGroupParams block(int block) {
/* 22 */     this.block = Integer.valueOf(block);
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   public XReadGroupParams noAck() {
/* 27 */     this.noack = true;
/* 28 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 33 */     if (this.count != null) {
/* 34 */       args.add(Protocol.Keyword.COUNT).add(this.count);
/*    */     }
/* 36 */     if (this.block != null) {
/* 37 */       args.add(Protocol.Keyword.BLOCK).add(this.block).blocking();
/*    */     }
/* 39 */     if (this.noack)
/* 40 */       args.add(Protocol.Keyword.NOACK); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\XReadGroupParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */