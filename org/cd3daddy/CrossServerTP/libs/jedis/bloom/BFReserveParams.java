/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.bloom;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BFReserveParams
/*    */   implements IParams
/*    */ {
/*    */   private Integer expansion;
/*    */   private boolean nonScaling = false;
/*    */   
/*    */   public static BFReserveParams reserveParams() {
/* 16 */     return new BFReserveParams();
/*    */   }
/*    */   
/*    */   public BFReserveParams expansion(int expansion) {
/* 20 */     this.expansion = Integer.valueOf(expansion);
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public BFReserveParams nonScaling() {
/* 25 */     this.nonScaling = true;
/* 26 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 31 */     if (this.expansion != null) {
/* 32 */       args.add(RedisBloomProtocol.RedisBloomKeyword.EXPANSION).add(Protocol.toByteArray(this.expansion.intValue()));
/*    */     }
/* 34 */     if (this.nonScaling)
/* 35 */       args.add(RedisBloomProtocol.RedisBloomKeyword.NONSCALING); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\BFReserveParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */