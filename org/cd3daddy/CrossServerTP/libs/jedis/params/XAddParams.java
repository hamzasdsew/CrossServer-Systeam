/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.StreamEntryID;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.RawableFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XAddParams
/*     */   implements IParams
/*     */ {
/*     */   private Rawable id;
/*     */   private Long maxLen;
/*     */   private boolean approximateTrimming;
/*     */   private boolean exactTrimming;
/*     */   private boolean nomkstream;
/*     */   private String minId;
/*     */   private Long limit;
/*     */   
/*     */   public static XAddParams xAddParams() {
/*  27 */     return new XAddParams();
/*     */   }
/*     */   
/*     */   public XAddParams noMkStream() {
/*  31 */     this.nomkstream = true;
/*  32 */     return this;
/*     */   }
/*     */   
/*     */   public XAddParams id(byte[] id) {
/*  36 */     this.id = RawableFactory.from(id);
/*  37 */     return this;
/*     */   }
/*     */   
/*     */   public XAddParams id(String id) {
/*  41 */     this.id = RawableFactory.from(id);
/*  42 */     return this;
/*     */   }
/*     */   
/*     */   public XAddParams id(StreamEntryID id) {
/*  46 */     return id(id.toString());
/*     */   }
/*     */   
/*     */   public XAddParams id(long time, long sequence) {
/*  50 */     return id(time + "-" + sequence);
/*     */   }
/*     */   
/*     */   public XAddParams id(long time) {
/*  54 */     return id(time + "-*");
/*     */   }
/*     */   
/*     */   public XAddParams maxLen(long maxLen) {
/*  58 */     this.maxLen = Long.valueOf(maxLen);
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public XAddParams minId(String minId) {
/*  63 */     this.minId = minId;
/*  64 */     return this;
/*     */   }
/*     */   
/*     */   public XAddParams approximateTrimming() {
/*  68 */     this.approximateTrimming = true;
/*  69 */     return this;
/*     */   }
/*     */   
/*     */   public XAddParams exactTrimming() {
/*  73 */     this.exactTrimming = true;
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public XAddParams limit(long limit) {
/*  78 */     this.limit = Long.valueOf(limit);
/*  79 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/*  85 */     if (this.nomkstream) {
/*  86 */       args.add(Protocol.Keyword.NOMKSTREAM);
/*     */     }
/*     */     
/*  89 */     if (this.maxLen != null) {
/*  90 */       args.add(Protocol.Keyword.MAXLEN);
/*     */       
/*  92 */       if (this.approximateTrimming) {
/*  93 */         args.add(Protocol.BYTES_TILDE);
/*  94 */       } else if (this.exactTrimming) {
/*  95 */         args.add(Protocol.BYTES_EQUAL);
/*     */       } 
/*     */       
/*  98 */       args.add(this.maxLen);
/*  99 */     } else if (this.minId != null) {
/* 100 */       args.add(Protocol.Keyword.MINID);
/*     */       
/* 102 */       if (this.approximateTrimming) {
/* 103 */         args.add(Protocol.BYTES_TILDE);
/* 104 */       } else if (this.exactTrimming) {
/* 105 */         args.add(Protocol.BYTES_EQUAL);
/*     */       } 
/*     */       
/* 108 */       args.add(this.minId);
/*     */     } 
/*     */     
/* 111 */     if (this.limit != null) {
/* 112 */       args.add(Protocol.Keyword.LIMIT).add(this.limit);
/*     */     }
/*     */     
/* 115 */     args.add((this.id != null) ? this.id : StreamEntryID.NEW_ENTRY);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\XAddParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */