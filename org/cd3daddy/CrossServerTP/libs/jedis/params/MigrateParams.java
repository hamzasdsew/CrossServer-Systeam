/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ public class MigrateParams
/*    */   implements IParams {
/*    */   private boolean copy = false;
/*    */   private boolean replace = false;
/* 10 */   private String username = null;
/* 11 */   private String password = null;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static MigrateParams migrateParams() {
/* 17 */     return new MigrateParams();
/*    */   }
/*    */   
/*    */   public MigrateParams copy() {
/* 21 */     this.copy = true;
/* 22 */     return this;
/*    */   }
/*    */   
/*    */   public MigrateParams replace() {
/* 26 */     this.replace = true;
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public MigrateParams auth(String password) {
/* 31 */     this.password = password;
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public MigrateParams auth2(String username, String password) {
/* 36 */     this.username = username;
/* 37 */     this.password = password;
/* 38 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 43 */     if (this.copy) {
/* 44 */       args.add(Protocol.Keyword.COPY);
/*    */     }
/* 46 */     if (this.replace) {
/* 47 */       args.add(Protocol.Keyword.REPLACE);
/*    */     }
/* 49 */     if (this.username != null) {
/* 50 */       args.add(Protocol.Keyword.AUTH2).add(this.username).add(this.password);
/* 51 */     } else if (this.password != null) {
/* 52 */       args.add(Protocol.Keyword.AUTH).add(this.password);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\MigrateParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */