/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.bloom;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CFReserveParams
/*    */   implements IParams
/*    */ {
/*    */   private Long bucketSize;
/*    */   private Integer maxIterations;
/*    */   private Integer expansion;
/*    */   
/*    */   public static CFReserveParams reserveParams() {
/* 19 */     return new CFReserveParams();
/*    */   }
/*    */   
/*    */   public CFReserveParams bucketSize(long bucketSize) {
/* 23 */     this.bucketSize = Long.valueOf(bucketSize);
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public CFReserveParams maxIterations(int maxIterations) {
/* 28 */     this.maxIterations = Integer.valueOf(maxIterations);
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public CFReserveParams expansion(int expansion) {
/* 33 */     this.expansion = Integer.valueOf(expansion);
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 39 */     if (this.bucketSize != null) {
/* 40 */       args.add(RedisBloomProtocol.RedisBloomKeyword.BUCKETSIZE).add(Protocol.toByteArray(this.bucketSize.longValue()));
/*    */     }
/* 42 */     if (this.maxIterations != null) {
/* 43 */       args.add(RedisBloomProtocol.RedisBloomKeyword.MAXITERATIONS).add(Protocol.toByteArray(this.maxIterations.intValue()));
/*    */     }
/* 45 */     if (this.expansion != null)
/* 46 */       args.add(RedisBloomProtocol.RedisBloomKeyword.EXPANSION).add(Protocol.toByteArray(this.expansion.intValue())); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\CFReserveParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */