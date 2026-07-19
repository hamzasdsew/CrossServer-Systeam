/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.gears;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ 
/*    */ public class TFunctionListParams
/*    */   implements IParams
/*    */ {
/*    */   private boolean withCode = false;
/*    */   private int verbose;
/*    */   private String libraryName;
/*    */   
/*    */   public static TFunctionListParams listParams() {
/* 15 */     return new TFunctionListParams();
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 20 */     if (this.withCode) {
/* 21 */       args.add(RedisGearsProtocol.GearsKeyword.WITHCODE);
/*    */     }
/*    */     
/* 24 */     if (this.verbose > 0 && this.verbose < 4) {
/* 25 */       args.add(String.join("", Collections.nCopies(this.verbose, "v")));
/* 26 */     } else if (this.verbose != 0) {
/* 27 */       throw new IllegalArgumentException("verbose must be between 1 and 3");
/*    */     } 
/*    */     
/* 30 */     if (this.libraryName != null) {
/* 31 */       args.add(RedisGearsProtocol.GearsKeyword.LIBRARY).add(this.libraryName);
/*    */     }
/*    */   }
/*    */   
/*    */   public TFunctionListParams withCode() {
/* 36 */     this.withCode = true;
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public TFunctionListParams verbose(int verbose) {
/* 41 */     this.verbose = verbose;
/* 42 */     return this;
/*    */   }
/*    */   
/*    */   public TFunctionListParams library(String libraryName) {
/* 46 */     this.libraryName = libraryName;
/* 47 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\TFunctionListParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */