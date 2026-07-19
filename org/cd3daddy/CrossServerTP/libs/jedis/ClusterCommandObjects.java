/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.util.Set;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.JedisClusterHashTag;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ public class ClusterCommandObjects
/*     */   extends CommandObjects
/*     */ {
/*     */   private static final String CLUSTER_UNSUPPORTED_MESSAGE = "Not supported in cluster mode.";
/*     */   private static final String KEYS_PATTERN_MESSAGE = "Cluster mode only supports KEYS command with pattern containing hash-tag ( curly-brackets enclosed string )";
/*     */   private static final String SCAN_PATTERN_MESSAGE = "Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )";
/*     */   
/*     */   protected ClusterCommandArguments commandArguments(ProtocolCommand command) {
/*  19 */     return new ClusterCommandArguments(command);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandObject<Long> dbSize() {
/*  26 */     throw new UnsupportedOperationException("Not supported in cluster mode.");
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
/*  37 */     if (!JedisClusterHashTag.isClusterCompliantMatchPattern(pattern)) {
/*  38 */       throw new IllegalArgumentException("Cluster mode only supports KEYS command with pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  40 */     return new CommandObject<>(commandArguments(Protocol.Command.KEYS).key(pattern).processKey(pattern), BuilderFactory.STRING_SET);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<Set<byte[]>> keys(byte[] pattern) {
/*  45 */     if (!JedisClusterHashTag.isClusterCompliantMatchPattern(pattern)) {
/*  46 */       throw new IllegalArgumentException("Cluster mode only supports KEYS command with pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  48 */     return new CommandObject<>(commandArguments(Protocol.Command.KEYS).key(pattern).processKey(pattern), BuilderFactory.BINARY_SET);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<String>> scan(String cursor) {
/*  53 */     throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<String>> scan(String cursor, ScanParams params) {
/*  58 */     String match = params.match();
/*  59 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/*  60 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  62 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match), BuilderFactory.SCAN_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<String>> scan(String cursor, ScanParams params, String type) {
/*  67 */     String match = params.match();
/*  68 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/*  69 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  71 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match).add(Protocol.Keyword.TYPE).add(type), BuilderFactory.SCAN_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<byte[]>> scan(byte[] cursor) {
/*  76 */     throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params) {
/*  81 */     byte[] match = params.binaryMatch();
/*  82 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/*  83 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  85 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match), BuilderFactory.SCAN_BINARY_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params, byte[] type) {
/*  90 */     byte[] match = params.binaryMatch();
/*  91 */     if (match == null || !JedisClusterHashTag.isClusterCompliantMatchPattern(match)) {
/*  92 */       throw new IllegalArgumentException("Cluster mode only supports SCAN command with MATCH pattern containing hash-tag ( curly-brackets enclosed string )");
/*     */     }
/*  94 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).processKey(match).add(Protocol.Keyword.TYPE).add(type), BuilderFactory.SCAN_BINARY_RESPONSE);
/*     */   }
/*     */ 
/*     */   
/*     */   public final CommandObject<Long> waitReplicas(int replicas, long timeout) {
/*  99 */     throw new UnsupportedOperationException("Not supported in cluster mode.");
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandObject<KeyValue<Long, Long>> waitAOF(long numLocal, long numReplicas, long timeout) {
/* 104 */     throw new UnsupportedOperationException("Not supported in cluster mode.");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ClusterCommandObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */