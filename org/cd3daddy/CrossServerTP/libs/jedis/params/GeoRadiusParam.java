/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortingOrder;
/*    */ 
/*    */ public class GeoRadiusParam
/*    */   implements IParams
/*    */ {
/*    */   private boolean withCoord = false;
/*    */   private boolean withDist = false;
/*    */   private boolean withHash = false;
/* 13 */   private Integer count = null;
/*    */   private boolean any = false;
/* 15 */   private SortingOrder sortingOrder = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static GeoRadiusParam geoRadiusParam() {
/* 21 */     return new GeoRadiusParam();
/*    */   }
/*    */   
/*    */   public GeoRadiusParam withCoord() {
/* 25 */     this.withCoord = true;
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public GeoRadiusParam withDist() {
/* 30 */     this.withDist = true;
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public GeoRadiusParam withHash() {
/* 35 */     this.withHash = true;
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public GeoRadiusParam sortAscending() {
/* 40 */     return sortingOrder(SortingOrder.ASC);
/*    */   }
/*    */   
/*    */   public GeoRadiusParam sortDescending() {
/* 44 */     return sortingOrder(SortingOrder.DESC);
/*    */   }
/*    */   
/*    */   public GeoRadiusParam sortingOrder(SortingOrder order) {
/* 48 */     this.sortingOrder = order;
/* 49 */     return this;
/*    */   }
/*    */   
/*    */   public GeoRadiusParam count(int count) {
/* 53 */     this.count = Integer.valueOf(count);
/* 54 */     return this;
/*    */   }
/*    */   
/*    */   public GeoRadiusParam count(int count, boolean any) {
/* 58 */     this.count = Integer.valueOf(count);
/* 59 */     this.any = any;
/* 60 */     return this;
/*    */   }
/*    */   
/*    */   public GeoRadiusParam any() {
/* 64 */     if (this.count == null) {
/* 65 */       throw new IllegalArgumentException("COUNT must be set before ANY to be set");
/*    */     }
/* 67 */     this.any = true;
/* 68 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 74 */     if (this.withCoord) {
/* 75 */       args.add(Protocol.Keyword.WITHCOORD);
/*    */     }
/* 77 */     if (this.withDist) {
/* 78 */       args.add(Protocol.Keyword.WITHDIST);
/*    */     }
/* 80 */     if (this.withHash) {
/* 81 */       args.add(Protocol.Keyword.WITHHASH);
/*    */     }
/*    */     
/* 84 */     if (this.count != null) {
/* 85 */       args.add(Protocol.Keyword.COUNT).add(this.count);
/* 86 */       if (this.any) {
/* 87 */         args.add(Protocol.Keyword.ANY);
/*    */       }
/*    */     } 
/*    */     
/* 91 */     if (this.sortingOrder != null)
/* 92 */       args.add(this.sortingOrder); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\GeoRadiusParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */