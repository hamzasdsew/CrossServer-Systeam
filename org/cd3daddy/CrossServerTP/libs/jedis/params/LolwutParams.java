/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ public class LolwutParams
/*    */   implements IParams {
/*    */   private Integer version;
/*    */   private String[] opargs;
/*    */   
/*    */   public LolwutParams version(int version) {
/* 12 */     this.version = Integer.valueOf(version);
/* 13 */     return this;
/*    */   }
/*    */   
/*    */   @Deprecated
/*    */   public LolwutParams args(String... args) {
/* 18 */     return optionalArguments(args);
/*    */   }
/*    */   
/*    */   public LolwutParams optionalArguments(String... args) {
/* 22 */     this.opargs = args;
/* 23 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 28 */     if (this.version != null) {
/* 29 */       args.add(Protocol.Keyword.VERSION).add(this.version);
/*    */       
/* 31 */       if (this.opargs != null && this.opargs.length > 0)
/* 32 */         args.addObjects((Object[])this.opargs); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\LolwutParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */