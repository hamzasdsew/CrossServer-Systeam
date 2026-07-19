/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GeoRadiusStoreParam
/*    */   implements IParams
/*    */ {
/*    */   private boolean store = false;
/*    */   private boolean storeDist = false;
/*    */   private String key;
/*    */   
/*    */   public static GeoRadiusStoreParam geoRadiusStoreParam() {
/* 16 */     return new GeoRadiusStoreParam();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GeoRadiusStoreParam store(String key) {
/* 25 */     if (key != null) {
/* 26 */       this.store = true;
/* 27 */       this.key = key;
/*    */     } 
/* 29 */     return this;
/*    */   }
/*    */   
/*    */   public GeoRadiusStoreParam storeDist(String key) {
/* 33 */     if (key != null) {
/* 34 */       this.storeDist = true;
/* 35 */       this.key = key;
/*    */     } 
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 42 */     if (this.storeDist) {
/* 43 */       args.add(Protocol.Keyword.STOREDIST).key(this.key);
/* 44 */     } else if (this.store) {
/* 45 */       args.add(Protocol.Keyword.STORE).key(this.key);
/*    */     } else {
/* 47 */       throw new IllegalArgumentException(getClass().getSimpleName() + " must has store or storedist option");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\GeoRadiusStoreParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */