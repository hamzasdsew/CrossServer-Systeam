/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandInfo
/*     */ {
/*     */   private final long arity;
/*     */   private final List<String> flags;
/*     */   private final long firstKey;
/*     */   private final long lastKey;
/*     */   private final long step;
/*     */   private final List<String> aclCategories;
/*     */   private final List<String> tips;
/*     */   private final List<String> subcommands;
/*     */   
/*     */   public CommandInfo(long arity, List<String> flags, long firstKey, long lastKey, long step, List<String> aclCategories, List<String> tips, List<String> subcommands) {
/*  22 */     this.arity = arity;
/*  23 */     this.flags = flags;
/*  24 */     this.firstKey = firstKey;
/*  25 */     this.lastKey = lastKey;
/*  26 */     this.step = step;
/*  27 */     this.aclCategories = aclCategories;
/*  28 */     this.tips = tips;
/*  29 */     this.subcommands = subcommands;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getArity() {
/*  43 */     return this.arity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getFlags() {
/*  50 */     return this.flags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getFirstKey() {
/*  57 */     return this.firstKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLastKey() {
/*  65 */     return this.lastKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getStep() {
/*  72 */     return this.step;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getAclCategories() {
/*  79 */     return this.aclCategories;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getTips() {
/*  86 */     return this.tips;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> getSubcommands() {
/*  93 */     return this.subcommands;
/*     */   }
/*     */   
/*  96 */   public static final Builder<CommandInfo> COMMAND_INFO_BUILDER = new Builder<CommandInfo>()
/*     */     {
/*     */       public CommandInfo build(Object data) {
/*  99 */         List<Object> commandData = (List<Object>)data;
/*     */         
/* 101 */         long arity = ((Long)BuilderFactory.LONG.build(commandData.get(1))).longValue();
/* 102 */         List<String> flags = (List<String>)BuilderFactory.STRING_LIST.build(commandData.get(2));
/* 103 */         long firstKey = ((Long)BuilderFactory.LONG.build(commandData.get(3))).longValue();
/* 104 */         long lastKey = ((Long)BuilderFactory.LONG.build(commandData.get(4))).longValue();
/* 105 */         long step = ((Long)BuilderFactory.LONG.build(commandData.get(5))).longValue();
/* 106 */         List<String> aclCategories = (List<String>)BuilderFactory.STRING_LIST.build(commandData.get(6));
/* 107 */         List<String> tips = (List<String>)BuilderFactory.STRING_LIST.build(commandData.get(7));
/* 108 */         List<String> subcommands = (List<String>)BuilderFactory.STRING_LIST.build(commandData.get(9));
/*     */         
/* 110 */         return new CommandInfo(arity, flags, firstKey, lastKey, step, aclCategories, tips, subcommands);
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\CommandInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */