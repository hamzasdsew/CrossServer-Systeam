/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortingOrder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SortingParams
/*     */   implements IParams
/*     */ {
/*  20 */   private final List<Object> params = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams by(String pattern) {
/*  35 */     return by(SafeEncoder.encode(pattern));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams by(byte[] pattern) {
/*  51 */     this.params.add(Protocol.Keyword.BY);
/*  52 */     this.params.add(pattern);
/*  53 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams nosort() {
/*  64 */     this.params.add(Protocol.Keyword.BY);
/*  65 */     this.params.add(Protocol.Keyword.NOSORT);
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams desc() {
/*  74 */     return sortingOrder(SortingOrder.DESC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams asc() {
/*  82 */     return sortingOrder(SortingOrder.ASC);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams sortingOrder(SortingOrder order) {
/*  91 */     this.params.add(order.getRaw());
/*  92 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams limit(int start, int count) {
/* 102 */     this.params.add(Protocol.Keyword.LIMIT);
/* 103 */     this.params.add(Integer.valueOf(start));
/* 104 */     this.params.add(Integer.valueOf(count));
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams alpha() {
/* 114 */     this.params.add(Protocol.Keyword.ALPHA);
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams get(String... patterns) {
/* 133 */     for (String pattern : patterns) {
/* 134 */       this.params.add(Protocol.Keyword.GET);
/* 135 */       this.params.add(pattern);
/*     */     } 
/* 137 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortingParams get(byte[]... patterns) {
/* 155 */     for (byte[] pattern : patterns) {
/* 156 */       this.params.add(Protocol.Keyword.GET);
/* 157 */       this.params.add(pattern);
/*     */     } 
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 164 */     args.addObjects(this.params);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\SortingParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */