/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.Hashing;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisClusterHashTag;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ShardedCommandObjects
/*     */   extends CommandObjects
/*     */ {
/*     */   private final Hashing algo;
/*     */   private final Pattern tagPattern;
/*     */   private static final String KEYS_PATTERN_MESSAGE = "Cluster mode only supports KEYS command with pattern containing hash-tag ( curly-brackets enclosed string )";
/*     */   private static final String SCAN_PATTERN_MESSAGE = "Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )";
/*     */   
/*     */   public ShardedCommandObjects(Hashing algo) {
/*  27 */     this(algo, null);
/*     */   }
/*     */   
/*     */   public ShardedCommandObjects(Hashing algo, Pattern tagPattern) {
/*  31 */     this.algo = algo;
/*  32 */     this.tagPattern = tagPattern;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ShardedCommandArguments commandArguments(ProtocolCommand command) {
/*  37 */     return new ShardedCommandArguments(this.algo, this.tagPattern, command);
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandObject<Long> dbSize() {
/*  42 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final CommandObject<Set<String>> keys(String pattern) {
/*  53 */     if (!JedisClusterHashTag.isClusterCompliantMatchPattern(pattern)) {
/*  54 */       throw new IllegalArgumentException("Cluster mode only supports KEYS command with pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  56 */     return new CommandObject<>(commandArguments(Protocol.Command.KEYS).key(pattern).processKey(pattern), BuilderFactory.STRING_SET);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<Set<byte[]>> keys(byte[] pattern) {
/*  61 */     if (!JedisClusterHashTag.isClusterCompliantMatchPattern(pattern)) {
/*  62 */       throw new IllegalArgumentException("Cluster mode only supports KEYS command with pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  64 */     return new CommandObject<>(commandArguments(Protocol.Command.KEYS).key(pattern).processKey(pattern), BuilderFactory.BINARY_SET);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<String>> scan(String cursor) {
/*  69 */     throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<String>> scan(String cursor, ScanParams params) {
/*  74 */     String match = params.match();
/*  75 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/*  76 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  78 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match), BuilderFactory.SCAN_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<String>> scan(String cursor, ScanParams params, String type) {
/*  83 */     String match = params.match();
/*  84 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/*  85 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  87 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match).add(Protocol.Keyword.TYPE).add(type), BuilderFactory.SCAN_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<byte[]>> scan(byte[] cursor) {
/*  92 */     throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params) {
/*  97 */     byte[] match = params.binaryMatch();
/*  98 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/*  99 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/* 101 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match), BuilderFactory.SCAN_BINARY_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params, byte[] type) {
/* 106 */     byte[] match = params.binaryMatch();
/* 107 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/* 108 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/* 110 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match).add(Protocol.Keyword.TYPE).add(type), BuilderFactory.SCAN_BINARY_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<Long> waitReplicas(int replicas, long timeout) {
/* 115 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandObject<KeyValue<Long, Long>> waitAOF(long numLocal, long numReplicas, long timeout) {
/* 120 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ShardedCommandObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */