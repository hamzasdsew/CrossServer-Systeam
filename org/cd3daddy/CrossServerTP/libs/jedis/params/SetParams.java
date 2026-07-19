/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetParams
/*     */   implements IParams
/*     */ {
/*     */   private Protocol.Keyword existance;
/*     */   private Protocol.Keyword expiration;
/*     */   private Long expirationValue;
/*     */   
/*     */   public static SetParams setParams() {
/*  16 */     return new SetParams();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams nx() {
/*  24 */     this.existance = Protocol.Keyword.NX;
/*  25 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams xx() {
/*  33 */     this.existance = Protocol.Keyword.XX;
/*  34 */     return this;
/*     */   }
/*     */   
/*     */   private SetParams expiration(Protocol.Keyword type, Long value) {
/*  38 */     this.expiration = type;
/*  39 */     this.expirationValue = value;
/*  40 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams ex(long remainingSeconds) {
/*  49 */     return expiration(Protocol.Keyword.EX, Long.valueOf(remainingSeconds));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams px(long remainingMilliseconds) {
/*  58 */     return expiration(Protocol.Keyword.PX, Long.valueOf(remainingMilliseconds));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams exAt(long timestampSeconds) {
/*  67 */     return expiration(Protocol.Keyword.EXAT, Long.valueOf(timestampSeconds));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams pxAt(long timestampMilliseconds) {
/*  76 */     return expiration(Protocol.Keyword.PXAT, Long.valueOf(timestampMilliseconds));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams keepttl() {
/*  85 */     return keepTtl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SetParams keepTtl() {
/*  93 */     return expiration(Protocol.Keyword.KEEPTTL, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/*  98 */     if (this.existance != null) {
/*  99 */       args.add(this.existance);
/*     */     }
/*     */     
/* 102 */     if (this.expiration != null) {
/* 103 */       args.add(this.expiration);
/* 104 */       if (this.expirationValue != null)
/* 105 */         args.add(this.expirationValue); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\SetParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */