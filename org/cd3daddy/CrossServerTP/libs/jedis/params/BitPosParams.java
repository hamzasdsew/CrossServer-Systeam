/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.BitCountOption;
/*    */ 
/*    */ 
/*    */ public class BitPosParams
/*    */   implements IParams
/*    */ {
/*    */   private Long start;
/*    */   private Long end;
/*    */   private BitCountOption modifier;
/*    */   
/*    */   public BitPosParams() {}
/*    */   
/*    */   public BitPosParams(long start) {
/* 17 */     this.start = Long.valueOf(start);
/*    */   }
/*    */ 
/*    */   
/*    */   public BitPosParams(long start, long end) {
/* 22 */     this(start);
/*    */     
/* 24 */     this.end = Long.valueOf(end);
/*    */   }
/*    */   
/*    */   public static BitPosParams bitPosParams() {
/* 28 */     return new BitPosParams();
/*    */   }
/*    */   
/*    */   public BitPosParams start(long start) {
/* 32 */     this.start = Long.valueOf(start);
/* 33 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BitPosParams end(long end) {
/* 40 */     this.end = Long.valueOf(end);
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BitPosParams modifier(BitCountOption modifier) {
/* 49 */     this.modifier = modifier;
/* 50 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 55 */     if (this.start != null) {
/* 56 */       args.add(this.start);
/* 57 */       if (this.end != null) {
/* 58 */         args.add(this.end);
/* 59 */         if (this.modifier != null)
/* 60 */           args.add(this.modifier); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\BitPosParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */