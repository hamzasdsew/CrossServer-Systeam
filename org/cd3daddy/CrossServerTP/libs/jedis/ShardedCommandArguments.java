/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Hashing;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class ShardedCommandArguments
/*    */   extends CommandArguments
/*    */ {
/*    */   private final Hashing algo;
/*    */   private final Pattern tagPattern;
/* 17 */   private Long keyHash = null;
/*    */   
/*    */   public ShardedCommandArguments(Hashing algo, ProtocolCommand command) {
/* 20 */     this(algo, null, command);
/*    */   }
/*    */   
/*    */   public ShardedCommandArguments(Hashing algo, Pattern tagPattern, ProtocolCommand command) {
/* 24 */     super(command);
/* 25 */     this.algo = algo;
/* 26 */     this.tagPattern = tagPattern;
/*    */   }
/*    */   
/*    */   public Long getKeyHash() {
/* 30 */     return this.keyHash;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments processKey(byte[] key) {
/* 35 */     long hash = this.algo.hash(key);
/* 36 */     if (this.keyHash == null) {
/* 37 */       this.keyHash = Long.valueOf(hash);
/* 38 */     } else if (this.keyHash.longValue() != hash) {
/* 39 */       throw new JedisException("Keys must generate same hash.");
/*    */     } 
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CommandArguments processKey(String key) {
/* 46 */     key = getKeyTag(key);
/* 47 */     long hash = this.algo.hash(key);
/* 48 */     if (this.keyHash == null) {
/* 49 */       this.keyHash = Long.valueOf(hash);
/* 50 */     } else if (this.keyHash.longValue() != hash) {
/* 51 */       throw new JedisException("Keys must generate same hash.");
/*    */     } 
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   private String getKeyTag(String key) {
/* 57 */     if (this.tagPattern != null) {
/* 58 */       Matcher m = this.tagPattern.matcher(key);
/* 59 */       if (m.find()) return m.group(1); 
/*    */     } 
/* 61 */     return key;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ShardedCommandArguments.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */