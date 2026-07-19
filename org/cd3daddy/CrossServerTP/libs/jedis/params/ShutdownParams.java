/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SaveMode;
/*    */ 
/*    */ public class ShutdownParams
/*    */   implements IParams {
/*    */   private SaveMode saveMode;
/*    */   private boolean now;
/*    */   private boolean force;
/*    */   
/*    */   public static ShutdownParams shutdownParams() {
/* 14 */     return new ShutdownParams();
/*    */   }
/*    */   
/*    */   public ShutdownParams saveMode(SaveMode saveMode) {
/* 18 */     this.saveMode = saveMode;
/* 19 */     return this;
/*    */   }
/*    */   
/*    */   public ShutdownParams nosave() {
/* 23 */     return saveMode(SaveMode.NOSAVE);
/*    */   }
/*    */   
/*    */   public ShutdownParams save() {
/* 27 */     return saveMode(SaveMode.SAVE);
/*    */   }
/*    */   
/*    */   public ShutdownParams now() {
/* 31 */     this.now = true;
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public ShutdownParams force() {
/* 36 */     this.force = true;
/* 37 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 42 */     if (this.saveMode != null) {
/* 43 */       args.add(this.saveMode);
/*    */     }
/* 45 */     if (this.now) {
/* 46 */       args.add(Protocol.Keyword.NOW);
/*    */     }
/* 48 */     if (this.force)
/* 49 */       args.add(Protocol.Keyword.FORCE); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ShutdownParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */