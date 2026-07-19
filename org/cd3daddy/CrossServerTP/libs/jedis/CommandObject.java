/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ public class CommandObject<T>
/*    */ {
/*    */   private final CommandArguments arguments;
/*    */   private final Builder<T> builder;
/*    */   
/*    */   public CommandObject(CommandArguments args, Builder<T> builder) {
/*  9 */     this.arguments = args;
/* 10 */     this.builder = builder;
/*    */   }
/*    */   
/*    */   public CommandArguments getArguments() {
/* 14 */     return this.arguments;
/*    */   }
/*    */   
/*    */   public Builder<T> getBuilder() {
/* 18 */     return this.builder;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\CommandObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */