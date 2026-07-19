/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.bloom;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TDigestMergeParams
/*    */   implements IParams
/*    */ {
/*    */   private Integer compression;
/*    */   private boolean override = false;
/*    */   
/*    */   public static TDigestMergeParams mergeParams() {
/* 16 */     return new TDigestMergeParams();
/*    */   }
/*    */   
/*    */   public TDigestMergeParams compression(int compression) {
/* 20 */     this.compression = Integer.valueOf(compression);
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public TDigestMergeParams override() {
/* 25 */     this.override = true;
/* 26 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 31 */     if (this.compression != null) {
/* 32 */       args.add(RedisBloomProtocol.RedisBloomKeyword.COMPRESSION).add(Protocol.toByteArray(this.compression.intValue()));
/*    */     }
/* 34 */     if (this.override)
/* 35 */       args.add(RedisBloomProtocol.RedisBloomKeyword.OVERRIDE); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\TDigestMergeParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */