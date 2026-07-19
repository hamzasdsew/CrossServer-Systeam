/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.gears;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ public class TFunctionLoadParams
/*    */   implements IParams {
/*    */   private boolean replace = false;
/*    */   private String config;
/*    */   
/*    */   public static TFunctionLoadParams loadParams() {
/* 12 */     return new TFunctionLoadParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 17 */     if (this.replace) {
/* 18 */       args.add(RedisGearsProtocol.GearsKeyword.REPLACE);
/*    */     }
/*    */     
/* 21 */     if (this.config != null && !this.config.isEmpty()) {
/* 22 */       args.add(RedisGearsProtocol.GearsKeyword.CONFIG).add(this.config);
/*    */     }
/*    */   }
/*    */   
/*    */   public TFunctionLoadParams replace() {
/* 27 */     this.replace = true;
/* 28 */     return this;
/*    */   }
/*    */   
/*    */   public TFunctionLoadParams config(String config) {
/* 32 */     this.config = config;
/* 33 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\TFunctionLoadParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */