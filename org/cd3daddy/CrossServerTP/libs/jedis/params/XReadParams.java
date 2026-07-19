/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ public class XReadParams
/*    */   implements IParams {
/*  8 */   private Integer count = null;
/*  9 */   private Integer block = null;
/*    */   
/*    */   public static XReadParams xReadParams() {
/* 12 */     return new XReadParams();
/*    */   }
/*    */   
/*    */   public XReadParams count(int count) {
/* 16 */     this.count = Integer.valueOf(count);
/* 17 */     return this;
/*    */   }
/*    */   
/*    */   public XReadParams block(int block) {
/* 21 */     this.block = Integer.valueOf(block);
/* 22 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 27 */     if (this.count != null) {
/* 28 */       args.add(Protocol.Keyword.COUNT).add(this.count);
/*    */     }
/* 30 */     if (this.block != null)
/* 31 */       args.add(Protocol.Keyword.BLOCK).add(this.block).blocking(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\XReadParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */