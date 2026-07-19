/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ public class CommandListFilterByParams
/*    */   implements IParams {
/*    */   private String moduleName;
/*    */   private String category;
/*    */   private String pattern;
/*    */   
/*    */   public static CommandListFilterByParams commandListFilterByParams() {
/* 13 */     return new CommandListFilterByParams();
/*    */   }
/*    */   
/*    */   public CommandListFilterByParams filterByModule(String moduleName) {
/* 17 */     this.moduleName = moduleName;
/* 18 */     return this;
/*    */   }
/*    */   
/*    */   public CommandListFilterByParams filterByAclCat(String category) {
/* 22 */     this.category = category;
/* 23 */     return this;
/*    */   }
/*    */   
/*    */   public CommandListFilterByParams filterByPattern(String pattern) {
/* 27 */     this.pattern = pattern;
/* 28 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 33 */     args.add(Protocol.Keyword.FILTERBY);
/*    */     
/* 35 */     if (this.moduleName != null && this.category == null && this.pattern == null) {
/* 36 */       args.add(Protocol.Keyword.MODULE);
/* 37 */       args.add(this.moduleName);
/* 38 */     } else if (this.moduleName == null && this.category != null && this.pattern == null) {
/* 39 */       args.add(Protocol.Keyword.ACLCAT);
/* 40 */       args.add(this.category);
/* 41 */     } else if (this.moduleName == null && this.category == null && this.pattern != null) {
/* 42 */       args.add(Protocol.Keyword.PATTERN);
/* 43 */       args.add(this.pattern);
/*    */     } else {
/* 45 */       throw new IllegalArgumentException("Must choose exactly one filter in " + 
/* 46 */           getClass().getSimpleName());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\CommandListFilterByParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */