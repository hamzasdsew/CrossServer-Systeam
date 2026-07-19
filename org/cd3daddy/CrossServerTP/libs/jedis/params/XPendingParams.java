/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.StreamEntryID;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.RawableFactory;
/*     */ 
/*     */ public class XPendingParams
/*     */   implements IParams
/*     */ {
/*     */   private Long idle;
/*     */   private Rawable start;
/*     */   private Rawable end;
/*     */   private Integer count;
/*     */   private Rawable consumer;
/*     */   
/*     */   public XPendingParams(StreamEntryID start, StreamEntryID end, int count) {
/*  19 */     this(start.toString(), end.toString(), count);
/*     */   }
/*     */   
/*     */   public XPendingParams(String start, String end, int count) {
/*  23 */     this(RawableFactory.from(start), RawableFactory.from(end), Integer.valueOf(count));
/*     */   }
/*     */   
/*     */   public XPendingParams(byte[] start, byte[] end, int count) {
/*  27 */     this(RawableFactory.from(start), RawableFactory.from(end), Integer.valueOf(count));
/*     */   }
/*     */   
/*     */   private XPendingParams(Rawable start, Rawable end, Integer count) {
/*  31 */     this.start = start;
/*  32 */     this.end = end;
/*  33 */     this.count = count;
/*     */   }
/*     */   
/*     */   public XPendingParams() {
/*  37 */     this.start = null;
/*  38 */     this.end = null;
/*  39 */     this.count = null;
/*     */   }
/*     */   
/*     */   public static XPendingParams xPendingParams(StreamEntryID start, StreamEntryID end, int count) {
/*  43 */     return new XPendingParams(start, end, count);
/*     */   }
/*     */   
/*     */   public static XPendingParams xPendingParams(String start, String end, int count) {
/*  47 */     return new XPendingParams(start, end, count);
/*     */   }
/*     */   
/*     */   public static XPendingParams xPendingParams(byte[] start, byte[] end, int count) {
/*  51 */     return new XPendingParams(start, end, count);
/*     */   }
/*     */   
/*     */   public static XPendingParams xPendingParams() {
/*  55 */     return new XPendingParams();
/*     */   }
/*     */   
/*     */   public XPendingParams idle(long idle) {
/*  59 */     this.idle = Long.valueOf(idle);
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public XPendingParams start(StreamEntryID start) {
/*  64 */     this.start = RawableFactory.from(start.toString());
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public XPendingParams end(StreamEntryID end) {
/*  69 */     this.end = RawableFactory.from(end.toString());
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public XPendingParams count(int count) {
/*  74 */     this.count = Integer.valueOf(count);
/*  75 */     return this;
/*     */   }
/*     */   
/*     */   public XPendingParams consumer(String consumer) {
/*  79 */     this.consumer = RawableFactory.from(consumer);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public XPendingParams consumer(byte[] consumer) {
/*  84 */     this.consumer = RawableFactory.from(consumer);
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/*  90 */     if (this.count == null) {
/*  91 */       throw new IllegalArgumentException("start, end and count must be set.");
/*     */     }
/*  93 */     if (this.start == null) this.start = RawableFactory.from("-"); 
/*  94 */     if (this.end == null) this.end = RawableFactory.from("+");
/*     */     
/*  96 */     if (this.idle != null) {
/*  97 */       args.add(Protocol.Keyword.IDLE).add(this.idle);
/*     */     }
/*     */     
/* 100 */     args.add(this.start).add(this.end).add(this.count);
/*     */     
/* 102 */     if (this.consumer != null)
/* 103 */       args.add(this.consumer); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\XPendingParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */