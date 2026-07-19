/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.timeseries;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public class TimeSeriesProtocol
/*    */ {
/*  9 */   public static final byte[] PLUS = SafeEncoder.encode("+");
/* 10 */   public static final byte[] MINUS = SafeEncoder.encode("-");
/*    */   
/*    */   public enum TimeSeriesCommand
/*    */     implements ProtocolCommand {
/* 14 */     CREATE("TS.CREATE"),
/* 15 */     RANGE("TS.RANGE"),
/* 16 */     REVRANGE("TS.REVRANGE"),
/* 17 */     MRANGE("TS.MRANGE"),
/* 18 */     MREVRANGE("TS.MREVRANGE"),
/* 19 */     CREATERULE("TS.CREATERULE"),
/* 20 */     DELETERULE("TS.DELETERULE"),
/* 21 */     ADD("TS.ADD"),
/* 22 */     MADD("TS.MADD"),
/* 23 */     DEL("TS.DEL"),
/* 24 */     INCRBY("TS.INCRBY"),
/* 25 */     DECRBY("TS.DECRBY"),
/* 26 */     INFO("TS.INFO"),
/* 27 */     GET("TS.GET"),
/* 28 */     MGET("TS.MGET"),
/* 29 */     ALTER("TS.ALTER"),
/* 30 */     QUERYINDEX("TS.QUERYINDEX");
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     TimeSeriesCommand(String alt) {
/* 35 */       this.raw = SafeEncoder.encode(alt);
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 40 */       return this.raw;
/*    */     }
/*    */   }
/*    */   
/*    */   public enum TimeSeriesKeyword
/*    */     implements Rawable {
/* 46 */     RESET,
/* 47 */     FILTER,
/* 48 */     AGGREGATION,
/* 49 */     LABELS,
/* 50 */     RETENTION,
/* 51 */     TIMESTAMP,
/* 52 */     WITHLABELS,
/* 53 */     SELECTED_LABELS,
/* 54 */     COUNT,
/* 55 */     ENCODING,
/* 56 */     COMPRESSED,
/* 57 */     UNCOMPRESSED,
/* 58 */     CHUNK_SIZE,
/* 59 */     DUPLICATE_POLICY,
/* 60 */     ON_DUPLICATE,
/* 61 */     ALIGN,
/* 62 */     FILTER_BY_TS,
/* 63 */     FILTER_BY_VALUE,
/* 64 */     GROUPBY,
/* 65 */     REDUCE,
/* 66 */     DEBUG,
/* 67 */     LATEST,
/* 68 */     EMPTY,
/* 69 */     BUCKETTIMESTAMP;
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     TimeSeriesKeyword() {
/* 74 */       this.raw = SafeEncoder.encode(name());
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 79 */       return this.raw;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\timeseries\TimeSeriesProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */