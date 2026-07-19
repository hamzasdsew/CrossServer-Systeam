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
/*    */ 
/*    */ 
/*    */ public class BFInsertParams
/*    */   implements IParams
/*    */ {
/*    */   private Long capacity;
/*    */   private Double errorRate;
/*    */   private Integer expansion;
/*    */   private boolean noCreate = false;
/*    */   private boolean nonScaling = false;
/*    */   
/*    */   public static BFInsertParams insertParams() {
/* 23 */     return new BFInsertParams();
/*    */   }
/*    */   
/*    */   public BFInsertParams capacity(long capacity) {
/* 27 */     this.capacity = Long.valueOf(capacity);
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   public BFInsertParams error(double errorRate) {
/* 32 */     this.errorRate = Double.valueOf(errorRate);
/* 33 */     return this;
/*    */   }
/*    */   
/*    */   public BFInsertParams expansion(int expansion) {
/* 37 */     this.expansion = Integer.valueOf(expansion);
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public BFInsertParams noCreate() {
/* 42 */     this.noCreate = true;
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public BFInsertParams nonScaling() {
/* 47 */     this.nonScaling = true;
/* 48 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 53 */     if (this.capacity != null) {
/* 54 */       args.add(RedisBloomProtocol.RedisBloomKeyword.CAPACITY).add(Protocol.toByteArray(this.capacity.longValue()));
/*    */     }
/* 56 */     if (this.errorRate != null) {
/* 57 */       args.add(RedisBloomProtocol.RedisBloomKeyword.ERROR).add(Protocol.toByteArray(this.errorRate.doubleValue()));
/*    */     }
/* 59 */     if (this.expansion != null) {
/* 60 */       args.add(RedisBloomProtocol.RedisBloomKeyword.EXPANSION).add(Protocol.toByteArray(this.expansion.intValue()));
/*    */     }
/* 62 */     if (this.noCreate) {
/* 63 */       args.add(RedisBloomProtocol.RedisBloomKeyword.NOCREATE);
/*    */     }
/* 65 */     if (this.nonScaling)
/* 66 */       args.add(RedisBloomProtocol.RedisBloomKeyword.NONSCALING); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\BFInsertParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */