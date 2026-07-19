/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JsonSetParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean nx = false;
/*    */   private boolean xx = false;
/*    */   
/*    */   public static JsonSetParams jsonSetParams() {
/* 15 */     return new JsonSetParams();
/*    */   }
/*    */   
/*    */   public JsonSetParams nx() {
/* 19 */     this.nx = true;
/* 20 */     this.xx = false;
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public JsonSetParams xx() {
/* 25 */     this.nx = false;
/* 26 */     this.xx = true;
/* 27 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 32 */     if (this.nx) {
/* 33 */       args.add("NX");
/*    */     }
/* 35 */     if (this.xx)
/* 36 */       args.add("XX"); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\JsonSetParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */