/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XClaimParams
/*    */   implements IParams
/*    */ {
/*    */   private Long idleTime;
/*    */   private Long idleUnixTime;
/*    */   private Integer retryCount;
/*    */   private boolean force;
/*    */   
/*    */   public static XClaimParams xClaimParams() {
/* 17 */     return new XClaimParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XClaimParams idle(long idleTime) {
/* 26 */     this.idleTime = Long.valueOf(idleTime);
/* 27 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XClaimParams time(long idleUnixTime) {
/* 36 */     this.idleUnixTime = Long.valueOf(idleUnixTime);
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XClaimParams retryCount(int count) {
/* 46 */     this.retryCount = Integer.valueOf(count);
/* 47 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public XClaimParams force() {
/* 56 */     this.force = true;
/* 57 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 62 */     if (this.idleTime != null) {
/* 63 */       args.add(Protocol.Keyword.IDLE).add(this.idleTime);
/*    */     }
/* 65 */     if (this.idleUnixTime != null) {
/* 66 */       args.add(Protocol.Keyword.TIME).add(this.idleUnixTime);
/*    */     }
/* 68 */     if (this.retryCount != null) {
/* 69 */       args.add(Protocol.Keyword.RETRYCOUNT).add(this.retryCount);
/*    */     }
/* 71 */     if (this.force)
/* 72 */       args.add(Protocol.Keyword.FORCE); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\XClaimParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */