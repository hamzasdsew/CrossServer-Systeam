/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RestoreParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean replace;
/*    */   private boolean absTtl;
/*    */   private Long idleTime;
/*    */   private Long frequency;
/*    */   
/*    */   public static RestoreParams restoreParams() {
/* 17 */     return new RestoreParams();
/*    */   }
/*    */   
/*    */   public RestoreParams replace() {
/* 21 */     this.replace = true;
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public RestoreParams absTtl() {
/* 26 */     this.absTtl = true;
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public RestoreParams idleTime(long idleTime) {
/* 31 */     this.idleTime = Long.valueOf(idleTime);
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public RestoreParams frequency(long frequency) {
/* 36 */     this.frequency = Long.valueOf(frequency);
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 42 */     if (this.replace) {
/* 43 */       args.add(Protocol.Keyword.REPLACE);
/*    */     }
/*    */     
/* 46 */     if (this.absTtl) {
/* 47 */       args.add(Protocol.Keyword.ABSTTL);
/*    */     }
/*    */     
/* 50 */     if (this.idleTime != null) {
/* 51 */       args.add(Protocol.Keyword.IDLETIME).add(this.idleTime);
/*    */     }
/*    */     
/* 54 */     if (this.frequency != null)
/* 55 */       args.add(Protocol.Keyword.FREQ).add(this.frequency); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\RestoreParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */