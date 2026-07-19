/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ public class ModuleLoadExParams
/*    */   implements IParams
/*    */ {
/* 12 */   private final List<KeyValue<String, String>> configs = new ArrayList<>();
/* 13 */   private final List<String> args = new ArrayList<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ModuleLoadExParams moduleLoadexParams() {
/* 19 */     return new ModuleLoadExParams();
/*    */   }
/*    */   
/*    */   public ModuleLoadExParams config(String name, String value) {
/* 23 */     this.configs.add(KeyValue.of(name, value));
/* 24 */     return this;
/*    */   }
/*    */   
/*    */   public ModuleLoadExParams arg(String arg) {
/* 28 */     this.args.add(arg);
/* 29 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 35 */     this.configs.forEach(kv -> args.add(Protocol.Keyword.CONFIG).add(kv.getKey()).add(kv.getValue()));
/*    */     
/* 37 */     if (!this.args.isEmpty())
/* 38 */       args.add(Protocol.Keyword.ARGS).addObjects(this.args); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ModuleLoadExParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */