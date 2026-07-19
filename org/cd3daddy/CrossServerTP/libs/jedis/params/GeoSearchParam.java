/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.GeoCoordinate;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortingOrder;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GeoSearchParam
/*     */   implements IParams
/*     */ {
/*     */   private boolean fromMember = false;
/*     */   private boolean fromLonLat = false;
/*     */   private String member;
/*     */   private GeoCoordinate coord;
/*     */   private boolean byRadius = false;
/*     */   private boolean byBox = false;
/*     */   private double radius;
/*     */   private double width;
/*     */   private double height;
/*     */   private GeoUnit unit;
/*     */   private boolean withCoord = false;
/*     */   private boolean withDist = false;
/*     */   private boolean withHash = false;
/*  27 */   private Integer count = null;
/*     */   private boolean any = false;
/*  29 */   private SortingOrder sortingOrder = null;
/*     */ 
/*     */   
/*     */   public static GeoSearchParam geoSearchParam() {
/*  33 */     return new GeoSearchParam();
/*     */   }
/*     */   public GeoSearchParam fromMember(String member) {
/*  36 */     this.fromMember = true;
/*  37 */     this.member = member;
/*  38 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam fromLonLat(double longitude, double latitude) {
/*  42 */     this.fromLonLat = true;
/*  43 */     this.coord = new GeoCoordinate(longitude, latitude);
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam fromLonLat(GeoCoordinate coord) {
/*  48 */     this.fromLonLat = true;
/*  49 */     this.coord = coord;
/*  50 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GeoSearchParam byRadius(double radius, GeoUnit unit) {
/*  55 */     this.byRadius = true;
/*  56 */     this.radius = radius;
/*  57 */     this.unit = unit;
/*  58 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam byBox(double width, double height, GeoUnit unit) {
/*  62 */     this.byBox = true;
/*  63 */     this.width = width;
/*  64 */     this.height = height;
/*  65 */     this.unit = unit;
/*  66 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam withCoord() {
/*  70 */     this.withCoord = true;
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam withDist() {
/*  75 */     this.withDist = true;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam withHash() {
/*  80 */     this.withHash = true;
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam asc() {
/*  85 */     return sortingOrder(SortingOrder.ASC);
/*     */   }
/*     */   
/*     */   public GeoSearchParam desc() {
/*  89 */     return sortingOrder(SortingOrder.DESC);
/*     */   }
/*     */   
/*     */   public GeoSearchParam sortingOrder(SortingOrder order) {
/*  93 */     this.sortingOrder = order;
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam count(int count) {
/*  98 */     this.count = Integer.valueOf(count);
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam count(int count, boolean any) {
/* 103 */     this.count = Integer.valueOf(count);
/* 104 */     this.any = true;
/* 105 */     return this;
/*     */   }
/*     */   
/*     */   public GeoSearchParam any() {
/* 109 */     if (this.count == null) {
/* 110 */       throw new IllegalArgumentException("COUNT must be set before ANY to be set");
/*     */     }
/* 112 */     this.any = true;
/* 113 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 118 */     if (this.fromMember) {
/* 119 */       args.add(Protocol.Keyword.FROMMEMBER).add(this.member);
/* 120 */     } else if (this.fromLonLat) {
/* 121 */       args.add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(this.coord.getLongitude())).add(Double.valueOf(this.coord.getLatitude()));
/*     */     } 
/*     */     
/* 124 */     if (this.byRadius) {
/* 125 */       args.add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(this.radius));
/* 126 */     } else if (this.byBox) {
/* 127 */       args.add(Protocol.Keyword.BYBOX).add(Double.valueOf(this.width)).add(Double.valueOf(this.height));
/*     */     } 
/* 129 */     args.add(this.unit);
/*     */     
/* 131 */     if (this.withCoord) {
/* 132 */       args.add(Protocol.Keyword.WITHCOORD);
/*     */     }
/* 134 */     if (this.withDist) {
/* 135 */       args.add(Protocol.Keyword.WITHDIST);
/*     */     }
/* 137 */     if (this.withHash) {
/* 138 */       args.add(Protocol.Keyword.WITHHASH);
/*     */     }
/*     */     
/* 141 */     if (this.count != null) {
/* 142 */       args.add(Protocol.Keyword.COUNT).add(this.count);
/* 143 */       if (this.any) {
/* 144 */         args.add(Protocol.Keyword.ANY);
/*     */       }
/*     */     } 
/*     */     
/* 148 */     if (this.sortingOrder != null)
/* 149 */       args.add(this.sortingOrder); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\GeoSearchParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */