/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GetExParams
/*    */   implements IParams
/*    */ {
/*    */   private Protocol.Keyword expiration;
/*    */   private Long expirationValue;
/*    */   private boolean persist;
/*    */   
/*    */   public static GetExParams getExParams() {
/* 17 */     return new GetExParams();
/*    */   }
/*    */   
/*    */   private GetExParams expiration(Protocol.Keyword type, Long value) {
/* 21 */     this.expiration = type;
/* 22 */     this.expirationValue = value;
/* 23 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GetExParams ex(long secondsToExpire) {
/* 31 */     return expiration(Protocol.Keyword.EX, Long.valueOf(secondsToExpire));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GetExParams px(long millisecondsToExpire) {
/* 39 */     return expiration(Protocol.Keyword.PX, Long.valueOf(millisecondsToExpire));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GetExParams exAt(long seconds) {
/* 48 */     return expiration(Protocol.Keyword.EXAT, Long.valueOf(seconds));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GetExParams pxAt(long milliseconds) {
/* 57 */     return expiration(Protocol.Keyword.PXAT, Long.valueOf(milliseconds));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GetExParams persist() {
/* 65 */     return expiration(Protocol.Keyword.PERSIST, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 70 */     if (this.expiration != null) {
/* 71 */       args.add(this.expiration);
/* 72 */       if (this.expirationValue != null)
/* 73 */         args.add(this.expirationValue); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\GetExParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */