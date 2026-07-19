/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.RawableFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZRangeParams
/*    */   implements IParams
/*    */ {
/*    */   private final Protocol.Keyword by;
/*    */   private final Rawable min;
/*    */   private final Rawable max;
/*    */   private boolean rev = false;
/*    */   private boolean limit = false;
/*    */   private int offset;
/*    */   private int count;
/*    */   
/*    */   private ZRangeParams() {
/* 23 */     throw new InstantiationError("Empty constructor must not be called.");
/*    */   }
/*    */   
/*    */   public ZRangeParams(int min, int max) {
/* 27 */     this.by = null;
/* 28 */     this.min = RawableFactory.from(min);
/* 29 */     this.max = RawableFactory.from(max);
/*    */   }
/*    */   
/*    */   public static ZRangeParams zrangeParams(int min, int max) {
/* 33 */     return new ZRangeParams(min, max);
/*    */   }
/*    */   
/*    */   public ZRangeParams(double min, double max) {
/* 37 */     this.by = Protocol.Keyword.BYSCORE;
/* 38 */     this.min = RawableFactory.from(min);
/* 39 */     this.max = RawableFactory.from(max);
/*    */   }
/*    */   
/*    */   public static ZRangeParams zrangeByScoreParams(double min, double max) {
/* 43 */     return new ZRangeParams(min, max);
/*    */   }
/*    */   
/*    */   private ZRangeParams(Protocol.Keyword by, Rawable min, Rawable max) {
/* 47 */     if (by == null || by == Protocol.Keyword.BYSCORE || by == Protocol.Keyword.BYLEX) {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 52 */       this.by = by;
/* 53 */       this.min = min;
/* 54 */       this.max = max;
/*    */       return;
/*    */     } 
/*    */     throw new IllegalArgumentException(by.name() + " is not a valid ZRANGE type argument."); } public ZRangeParams(Protocol.Keyword by, String min, String max) {
/* 58 */     this(by, RawableFactory.from(min), RawableFactory.from(max));
/*    */   }
/*    */   
/*    */   public ZRangeParams(Protocol.Keyword by, byte[] min, byte[] max) {
/* 62 */     this(by, RawableFactory.from(min), RawableFactory.from(max));
/*    */   }
/*    */   
/*    */   public static ZRangeParams zrangeByLexParams(String min, String max) {
/* 66 */     return new ZRangeParams(Protocol.Keyword.BYLEX, min, max);
/*    */   }
/*    */   
/*    */   public static ZRangeParams zrangeByLexParams(byte[] min, byte[] max) {
/* 70 */     return new ZRangeParams(Protocol.Keyword.BYLEX, min, max);
/*    */   }
/*    */   
/*    */   public ZRangeParams rev() {
/* 74 */     this.rev = true;
/* 75 */     return this;
/*    */   }
/*    */   
/*    */   public ZRangeParams limit(int offset, int count) {
/* 79 */     this.limit = true;
/* 80 */     this.offset = offset;
/* 81 */     this.count = count;
/* 82 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 88 */     args.add(this.min).add(this.max);
/* 89 */     if (this.by != null) {
/* 90 */       args.add(this.by);
/*    */     }
/*    */     
/* 93 */     if (this.rev) {
/* 94 */       args.add(Protocol.Keyword.REV);
/*    */     }
/*    */     
/* 97 */     if (this.limit)
/* 98 */       args.add(Protocol.Keyword.LIMIT).add(Integer.valueOf(this.offset)).add(Integer.valueOf(this.count)); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ZRangeParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */