/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GeoAddParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean nx = false;
/*    */   private boolean xx = false;
/*    */   private boolean ch = false;
/*    */   
/*    */   public static GeoAddParams geoAddParams() {
/* 16 */     return new GeoAddParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GeoAddParams nx() {
/* 24 */     this.nx = true;
/* 25 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GeoAddParams xx() {
/* 33 */     this.xx = true;
/* 34 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GeoAddParams ch() {
/* 43 */     this.ch = true;
/* 44 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 49 */     if (this.nx) {
/* 50 */       args.add(Protocol.Keyword.NX);
/* 51 */     } else if (this.xx) {
/* 52 */       args.add(Protocol.Keyword.XX);
/*    */     } 
/*    */     
/* 55 */     if (this.ch)
/* 56 */       args.add(Protocol.Keyword.CH); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\GeoAddParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */