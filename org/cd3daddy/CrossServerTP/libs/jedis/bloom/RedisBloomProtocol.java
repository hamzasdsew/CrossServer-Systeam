/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.bloom;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ public class RedisBloomProtocol
/*     */ {
/*     */   public enum BloomFilterCommand
/*     */     implements ProtocolCommand {
/*  11 */     RESERVE("BF.RESERVE"),
/*  12 */     ADD("BF.ADD"),
/*  13 */     MADD("BF.MADD"),
/*  14 */     EXISTS("BF.EXISTS"),
/*  15 */     MEXISTS("BF.MEXISTS"),
/*  16 */     INSERT("BF.INSERT"),
/*  17 */     SCANDUMP("BF.SCANDUMP"),
/*  18 */     LOADCHUNK("BF.LOADCHUNK"),
/*  19 */     CARD("BF.CARD"),
/*  20 */     INFO("BF.INFO");
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     BloomFilterCommand(String alt) {
/*  25 */       this.raw = SafeEncoder.encode(alt);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/*  30 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CuckooFilterCommand
/*     */     implements ProtocolCommand {
/*  36 */     RESERVE("CF.RESERVE"),
/*  37 */     ADD("CF.ADD"),
/*  38 */     ADDNX("CF.ADDNX"),
/*  39 */     INSERT("CF.INSERT"),
/*  40 */     INSERTNX("CF.INSERTNX"),
/*  41 */     EXISTS("CF.EXISTS"),
/*  42 */     MEXISTS("CF.MEXISTS"),
/*  43 */     DEL("CF.DEL"),
/*  44 */     COUNT("CF.COUNT"),
/*  45 */     SCANDUMP("CF.SCANDUMP"),
/*  46 */     LOADCHUNK("CF.LOADCHUNK"),
/*  47 */     INFO("CF.INFO");
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     CuckooFilterCommand(String alt) {
/*  52 */       this.raw = SafeEncoder.encode(alt);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/*  57 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum CountMinSketchCommand
/*     */     implements ProtocolCommand {
/*  63 */     INITBYDIM("CMS.INITBYDIM"),
/*  64 */     INITBYPROB("CMS.INITBYPROB"),
/*  65 */     INCRBY("CMS.INCRBY"),
/*  66 */     QUERY("CMS.QUERY"),
/*  67 */     MERGE("CMS.MERGE"),
/*  68 */     INFO("CMS.INFO");
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     CountMinSketchCommand(String alt) {
/*  73 */       this.raw = SafeEncoder.encode(alt);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/*  78 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum TopKCommand
/*     */     implements ProtocolCommand {
/*  84 */     RESERVE("TOPK.RESERVE"),
/*  85 */     ADD("TOPK.ADD"),
/*  86 */     INCRBY("TOPK.INCRBY"),
/*  87 */     QUERY("TOPK.QUERY"),
/*  88 */     LIST("TOPK.LIST"),
/*  89 */     INFO("TOPK.INFO");
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     TopKCommand(String alt) {
/*  94 */       this.raw = SafeEncoder.encode(alt);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/*  99 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum TDigestCommand
/*     */     implements ProtocolCommand {
/* 105 */     CREATE, INFO, ADD, RESET, MERGE, CDF, QUANTILE, MIN, MAX, TRIMMED_MEAN,
/* 106 */     RANK, REVRANK, BYRANK, BYREVRANK;
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     TDigestCommand() {
/* 111 */       this.raw = SafeEncoder.encode("TDIGEST." + name());
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/* 116 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum RedisBloomKeyword
/*     */     implements Rawable {
/* 122 */     CAPACITY, ERROR, NOCREATE, EXPANSION, NONSCALING, BUCKETSIZE, MAXITERATIONS, ITEMS, WEIGHTS,
/* 123 */     COMPRESSION, OVERRIDE, WITHCOUNT;
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     RedisBloomKeyword() {
/* 128 */       this.raw = SafeEncoder.encode(name());
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/* 133 */       return this.raw;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\bloom\RedisBloomProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */