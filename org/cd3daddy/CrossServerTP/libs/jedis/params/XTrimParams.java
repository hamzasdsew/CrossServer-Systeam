/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XTrimParams
/*    */   implements IParams
/*    */ {
/*    */   private Long maxLen;
/*    */   private boolean approximateTrimming;
/*    */   private boolean exactTrimming;
/*    */   private String minId;
/*    */   private Long limit;
/*    */   
/*    */   public static XTrimParams xTrimParams() {
/* 20 */     return new XTrimParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public XTrimParams maxLen(long maxLen) {
/* 25 */     this.maxLen = Long.valueOf(maxLen);
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public XTrimParams minId(String minId) {
/* 30 */     this.minId = minId;
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public XTrimParams approximateTrimming() {
/* 35 */     this.approximateTrimming = true;
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public XTrimParams exactTrimming() {
/* 40 */     this.exactTrimming = true;
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public XTrimParams limit(long limit) {
/* 45 */     this.limit = Long.valueOf(limit);
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 51 */     if (this.maxLen != null) {
/* 52 */       args.add(Protocol.Keyword.MAXLEN);
/*    */       
/* 54 */       if (this.approximateTrimming) {
/* 55 */         args.add(Protocol.BYTES_TILDE);
/* 56 */       } else if (this.exactTrimming) {
/* 57 */         args.add(Protocol.BYTES_EQUAL);
/*    */       } 
/*    */       
/* 60 */       args.add(Protocol.toByteArray(this.maxLen.longValue()));
/* 61 */     } else if (this.minId != null) {
/* 62 */       args.add(Protocol.Keyword.MINID);
/*    */       
/* 64 */       if (this.approximateTrimming) {
/* 65 */         args.add(Protocol.BYTES_TILDE);
/* 66 */       } else if (this.exactTrimming) {
/* 67 */         args.add(Protocol.BYTES_EQUAL);
/*    */       } 
/*    */       
/* 70 */       args.add(this.minId);
/*    */     } 
/*    */     
/* 73 */     if (this.limit != null)
/* 74 */       args.add(Protocol.Keyword.LIMIT).add(this.limit); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\XTrimParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */