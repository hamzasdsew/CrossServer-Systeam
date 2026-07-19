/*     */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisAccessControlException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisAskDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisBusyException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisClusterException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisMovedDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisNoScriptException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.RedisInputStream;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.RedisOutputStream;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ public final class Protocol
/*     */ {
/*     */   public static final String DEFAULT_HOST = "127.0.0.1";
/*  27 */   public static final Charset CHARSET = StandardCharsets.UTF_8; public static final int DEFAULT_PORT = 6379; public static final int DEFAULT_SENTINEL_PORT = 26379; public static final int DEFAULT_TIMEOUT = 2000;
/*     */   public static final int DEFAULT_DATABASE = 0;
/*     */   public static final int CLUSTER_HASHSLOTS = 16384;
/*     */   public static final byte ASTERISK_BYTE = 42;
/*     */   public static final byte COLON_BYTE = 58;
/*     */   public static final byte COMMA_BYTE = 44;
/*     */   public static final byte DOLLAR_BYTE = 36;
/*     */   public static final byte EQUAL_BYTE = 61;
/*     */   public static final byte GREATER_THAN_BYTE = 62;
/*     */   public static final byte HASH_BYTE = 35;
/*     */   public static final byte LEFT_BRACE_BYTE = 40;
/*     */   public static final byte MINUS_BYTE = 45;
/*     */   public static final byte PERCENT_BYTE = 37;
/*     */   public static final byte PLUS_BYTE = 43;
/*     */   public static final byte TILDE_BYTE = 126;
/*     */   public static final byte UNDERSCORE_BYTE = 95;
/*  43 */   public static final byte[] BYTES_TRUE = toByteArray(1);
/*  44 */   public static final byte[] BYTES_FALSE = toByteArray(0);
/*  45 */   public static final byte[] BYTES_TILDE = SafeEncoder.encode("~");
/*  46 */   public static final byte[] BYTES_EQUAL = SafeEncoder.encode("=");
/*  47 */   public static final byte[] BYTES_ASTERISK = SafeEncoder.encode("*");
/*     */   
/*  49 */   public static final byte[] POSITIVE_INFINITY_BYTES = "+inf".getBytes();
/*  50 */   public static final byte[] NEGATIVE_INFINITY_BYTES = "-inf".getBytes();
/*     */   
/*     */   private static final String ASK_PREFIX = "ASK ";
/*     */   private static final String MOVED_PREFIX = "MOVED ";
/*     */   private static final String CLUSTERDOWN_PREFIX = "CLUSTERDOWN ";
/*     */   private static final String BUSY_PREFIX = "BUSY ";
/*     */   private static final String NOSCRIPT_PREFIX = "NOSCRIPT ";
/*     */   private static final String WRONGPASS_PREFIX = "WRONGPASS";
/*     */   private static final String NOPERM_PREFIX = "NOPERM";
/*     */   
/*     */   private Protocol() {
/*  61 */     throw new InstantiationError("Must not instantiate this class");
/*     */   }
/*     */   
/*     */   public static void sendCommand(RedisOutputStream os, CommandArguments args) {
/*     */     try {
/*  66 */       os.write((byte)42);
/*  67 */       os.writeIntCrLf(args.size());
/*  68 */       for (Rawable arg : args) {
/*  69 */         os.write((byte)36);
/*  70 */         byte[] bin = arg.getRaw();
/*  71 */         os.writeIntCrLf(bin.length);
/*  72 */         os.write(bin);
/*  73 */         os.writeCrLf();
/*     */       } 
/*  75 */     } catch (IOException e) {
/*  76 */       throw new JedisConnectionException(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void processError(RedisInputStream is) {
/*  81 */     String message = is.readLine();
/*     */ 
/*     */     
/*  84 */     if (message.startsWith("MOVED ")) {
/*  85 */       String[] movedInfo = parseTargetHostAndSlot(message);
/*     */ 
/*     */       
/*  88 */       throw new JedisMovedDataException(message, HostAndPort.from(movedInfo[1]), Integer.parseInt(movedInfo[0]));
/*  89 */     }  if (message.startsWith("ASK ")) {
/*  90 */       String[] askInfo = parseTargetHostAndSlot(message);
/*     */ 
/*     */       
/*  93 */       throw new JedisAskDataException(message, HostAndPort.from(askInfo[1]), Integer.parseInt(askInfo[0]));
/*  94 */     }  if (message.startsWith("CLUSTERDOWN "))
/*  95 */       throw new JedisClusterException(message); 
/*  96 */     if (message.startsWith("BUSY "))
/*  97 */       throw new JedisBusyException(message); 
/*  98 */     if (message.startsWith("NOSCRIPT "))
/*  99 */       throw new JedisNoScriptException(message); 
/* 100 */     if (message.startsWith("WRONGPASS"))
/* 101 */       throw new JedisAccessControlException(message); 
/* 102 */     if (message.startsWith("NOPERM")) {
/* 103 */       throw new JedisAccessControlException(message);
/*     */     }
/* 105 */     throw new JedisDataException(message);
/*     */   }
/*     */   
/*     */   public static String readErrorLineIfPossible(RedisInputStream is) {
/* 109 */     byte b = is.readByte();
/*     */     
/* 111 */     if (b != 45) {
/* 112 */       return null;
/*     */     }
/* 114 */     return is.readLine();
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
/*     */   private static String[] parseTargetHostAndSlot(String clusterRedirectResponse) {
/* 127 */     String[] response = new String[2];
/* 128 */     String[] messageInfo = clusterRedirectResponse.split(" ");
/* 129 */     response[0] = messageInfo[1];
/* 130 */     response[1] = messageInfo[2];
/* 131 */     return response;
/*     */   }
/*     */   
/*     */   private static Object process(RedisInputStream is) {
/* 135 */     byte b = is.readByte();
/*     */     
/* 137 */     switch (b) {
/*     */       case 43:
/* 139 */         return is.readLineBytes();
/*     */       case 36:
/*     */       case 61:
/* 142 */         return processBulkReply(is);
/*     */       case 42:
/* 144 */         return processMultiBulkReply(is);
/*     */       case 95:
/* 146 */         return is.readNullCrLf();
/*     */       case 35:
/* 148 */         return Boolean.valueOf(is.readBooleanCrLf());
/*     */       case 58:
/* 150 */         return Long.valueOf(is.readLongCrLf());
/*     */       case 44:
/* 152 */         return Double.valueOf(is.readDoubleCrLf());
/*     */       case 40:
/* 154 */         return is.readBigIntegerCrLf();
/*     */       case 37:
/* 156 */         return processMapKeyValueReply(is);
/*     */       case 126:
/* 158 */         return processMultiBulkReply(is);
/*     */       case 62:
/* 160 */         return processMultiBulkReply(is);
/*     */       case 45:
/* 162 */         processError(is);
/* 163 */         return null;
/*     */     } 
/*     */     
/* 166 */     throw new JedisConnectionException("Unknown reply: " + (char)b);
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] processBulkReply(RedisInputStream is) {
/* 171 */     int len = is.readIntCrLf();
/* 172 */     if (len == -1) {
/* 173 */       return null;
/*     */     }
/*     */     
/* 176 */     byte[] read = new byte[len];
/* 177 */     int offset = 0;
/* 178 */     while (offset < len) {
/* 179 */       int size = is.read(read, offset, len - offset);
/* 180 */       if (size == -1) {
/* 181 */         throw new JedisConnectionException("It seems like server has closed the connection.");
/*     */       }
/* 183 */       offset += size;
/*     */     } 
/*     */ 
/*     */     
/* 187 */     is.readByte();
/* 188 */     is.readByte();
/*     */     
/* 190 */     return read;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Object> processMultiBulkReply(RedisInputStream is) {
/* 195 */     int num = is.readIntCrLf();
/* 196 */     if (num == -1) return null; 
/* 197 */     List<Object> ret = new ArrayList(num);
/* 198 */     for (int i = 0; i < num; i++) {
/*     */       try {
/* 200 */         ret.add(process(is));
/* 201 */       } catch (JedisDataException e) {
/* 202 */         ret.add(e);
/*     */       } 
/*     */     } 
/* 205 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<KeyValue> processMapKeyValueReply(RedisInputStream is) {
/* 211 */     int num = is.readIntCrLf();
/* 212 */     if (num == -1) return null; 
/* 213 */     List<KeyValue> ret = new ArrayList<>(num);
/* 214 */     for (int i = 0; i < num; i++) {
/* 215 */       ret.add(new KeyValue(process(is), process(is)));
/*     */     }
/* 217 */     return ret;
/*     */   }
/*     */   
/*     */   public static Object read(RedisInputStream is) {
/* 221 */     return process(is);
/*     */   }
/*     */   
/*     */   public static final byte[] toByteArray(boolean value) {
/* 225 */     return value ? BYTES_TRUE : BYTES_FALSE;
/*     */   }
/*     */   
/*     */   public static final byte[] toByteArray(int value) {
/* 229 */     return SafeEncoder.encode(String.valueOf(value));
/*     */   }
/*     */   
/*     */   public static final byte[] toByteArray(long value) {
/* 233 */     return SafeEncoder.encode(String.valueOf(value));
/*     */   }
/*     */   
/*     */   public static final byte[] toByteArray(double value) {
/* 237 */     if (value == Double.POSITIVE_INFINITY)
/* 238 */       return POSITIVE_INFINITY_BYTES; 
/* 239 */     if (value == Double.NEGATIVE_INFINITY) {
/* 240 */       return NEGATIVE_INFINITY_BYTES;
/*     */     }
/* 242 */     return SafeEncoder.encode(String.valueOf(value));
/*     */   }
/*     */   
/*     */   public enum Command
/*     */     implements ProtocolCommand
/*     */   {
/* 248 */     PING, AUTH, HELLO, SET, GET, GETDEL, GETEX, EXISTS, DEL, UNLINK, TYPE, FLUSHDB, FLUSHALL, MOVE,
/* 249 */     KEYS, RANDOMKEY, RENAME, RENAMENX, DUMP, RESTORE, DBSIZE, SELECT, SWAPDB, MIGRATE, ECHO,
/* 250 */     EXPIRE, EXPIREAT, EXPIRETIME, PEXPIRE, PEXPIREAT, PEXPIRETIME, TTL, PTTL,
/* 251 */     MULTI, DISCARD, EXEC, WATCH, UNWATCH, SORT, SORT_RO, INFO, SHUTDOWN, MONITOR, CONFIG, LCS,
/* 252 */     GETSET, MGET, SETNX, SETEX, PSETEX, MSET, MSETNX, DECR, DECRBY, INCR, INCRBY, INCRBYFLOAT,
/* 253 */     STRLEN, APPEND, SUBSTR,
/* 254 */     SETBIT, GETBIT, BITPOS, SETRANGE, GETRANGE, BITCOUNT, BITOP, BITFIELD, BITFIELD_RO,
/* 255 */     HSET, HGET, HSETNX, HMSET, HMGET, HINCRBY, HEXISTS, HDEL, HLEN, HKEYS, HVALS, HGETALL, HSTRLEN,
/* 256 */     HRANDFIELD, HINCRBYFLOAT,
/* 257 */     RPUSH, LPUSH, LLEN, LRANGE, LTRIM, LINDEX, LSET, LREM, LPOP, RPOP, BLPOP, BRPOP, LINSERT, LPOS,
/* 258 */     RPOPLPUSH, BRPOPLPUSH, BLMOVE, LMOVE, LMPOP, BLMPOP, LPUSHX, RPUSHX,
/* 259 */     SADD, SMEMBERS, SREM, SPOP, SMOVE, SCARD, SRANDMEMBER, SINTER, SINTERSTORE, SUNION, SUNIONSTORE,
/* 260 */     SDIFF, SDIFFSTORE, SISMEMBER, SMISMEMBER, SINTERCARD,
/* 261 */     ZADD, ZDIFF, ZDIFFSTORE, ZRANGE, ZREM, ZINCRBY, ZRANK, ZREVRANK, ZREVRANGE, ZRANDMEMBER, ZCARD,
/* 262 */     ZSCORE, ZPOPMAX, ZPOPMIN, ZCOUNT, ZUNION, ZUNIONSTORE, ZINTER, ZINTERSTORE, ZRANGEBYSCORE,
/* 263 */     ZREVRANGEBYSCORE, ZREMRANGEBYRANK, ZREMRANGEBYSCORE, ZLEXCOUNT, ZRANGEBYLEX, ZREVRANGEBYLEX,
/* 264 */     ZREMRANGEBYLEX, ZMSCORE, ZRANGESTORE, ZINTERCARD, ZMPOP, BZMPOP, BZPOPMIN, BZPOPMAX,
/* 265 */     GEOADD, GEODIST, GEOHASH, GEOPOS, GEORADIUS, GEORADIUS_RO, GEOSEARCH, GEOSEARCHSTORE,
/* 266 */     GEORADIUSBYMEMBER, GEORADIUSBYMEMBER_RO,
/* 267 */     PFADD, PFCOUNT, PFMERGE,
/* 268 */     XADD, XLEN, XDEL, XTRIM, XRANGE, XREVRANGE, XREAD, XACK, XGROUP, XREADGROUP, XPENDING, XCLAIM,
/* 269 */     XAUTOCLAIM, XINFO,
/* 270 */     EVAL, EVALSHA, SCRIPT, EVAL_RO, EVALSHA_RO, FUNCTION, FCALL, FCALL_RO,
/* 271 */     SUBSCRIBE, UNSUBSCRIBE, PSUBSCRIBE, PUNSUBSCRIBE, PUBLISH, PUBSUB,
/* 272 */     SSUBSCRIBE, SUNSUBSCRIBE, SPUBLISH,
/* 273 */     SAVE, BGSAVE, BGREWRITEAOF, LASTSAVE, PERSIST, ROLE, FAILOVER, SLOWLOG, OBJECT, CLIENT, TIME,
/* 274 */     SCAN, HSCAN, SSCAN, ZSCAN, WAIT, CLUSTER, ASKING, READONLY, READWRITE, SLAVEOF, REPLICAOF, COPY,
/* 275 */     SENTINEL, MODULE, ACL, TOUCH, MEMORY, LOLWUT, COMMAND, RESET, LATENCY, WAITAOF;
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     Command() {
/* 280 */       this.raw = SafeEncoder.encode(name());
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/* 285 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Keyword
/*     */     implements Rawable {
/* 291 */     AGGREGATE, ALPHA, BY, GET, LIMIT, NO, NOSORT, ONE, SET, STORE, WEIGHTS, WITHSCORE, WITHSCORES,
/* 292 */     RESETSTAT, REWRITE, RESET, FLUSH, EXISTS, LOAD, LEN, HELP, SCHEDULE, MATCH, COUNT, TYPE, KEYS,
/* 293 */     REFCOUNT, ENCODING, IDLETIME, FREQ, REPLACE, GETNAME, SETNAME, SETINFO, LIST, ID, KILL, PERSIST,
/* 294 */     STREAMS, CREATE, MKSTREAM, SETID, DESTROY, DELCONSUMER, MAXLEN, GROUP, IDLE, TIME, BLOCK, NOACK,
/* 295 */     RETRYCOUNT, STREAM, GROUPS, CONSUMERS, JUSTID, WITHVALUES, NOMKSTREAM, MINID, CREATECONSUMER,
/* 296 */     SETUSER, GETUSER, DELUSER, WHOAMI, USERS, CAT, GENPASS, LOG, SAVE, DRYRUN, COPY, AUTH, AUTH2,
/* 297 */     NX, XX, EX, PX, EXAT, PXAT, ABSTTL, KEEPTTL, INCR, LT, GT, CH, INFO, PAUSE, UNPAUSE, UNBLOCK,
/* 298 */     REV, WITHCOORD, WITHDIST, WITHHASH, ANY, FROMMEMBER, FROMLONLAT, BYRADIUS, BYBOX, BYLEX, BYSCORE,
/* 299 */     STOREDIST, TO, FORCE, TIMEOUT, DB, UNLOAD, ABORT, IDX, MINMATCHLEN, WITHMATCHLEN, FULL,
/* 300 */     DELETE, LIBRARYNAME, WITHCODE, DESCRIPTION, GETKEYS, GETKEYSANDFLAGS, DOCS, FILTERBY, DUMP,
/* 301 */     MODULE, ACLCAT, PATTERN, DOCTOR, USAGE, SAMPLES, PURGE, STATS, LOADEX, CONFIG, ARGS, RANK,
/* 302 */     NOW, VERSION, ADDR, SKIPME, USER, LADDR,
/* 303 */     CHANNELS, NUMPAT, NUMSUB, SHARDCHANNELS, SHARDNUMSUB;
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     Keyword() {
/* 308 */       this.raw = SafeEncoder.encode(name());
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/* 313 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum SentinelKeyword
/*     */     implements Rawable {
/* 319 */     MYID, MASTERS, MASTER, SENTINELS, SLAVES, REPLICAS, RESET, FAILOVER, REMOVE, SET, MONITOR,
/* 320 */     GET_MASTER_ADDR_BY_NAME("GET-MASTER-ADDR-BY-NAME");
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     SentinelKeyword() {
/* 325 */       this.raw = SafeEncoder.encode(name());
/*     */     }
/*     */     
/*     */     SentinelKeyword(String str) {
/* 329 */       this.raw = SafeEncoder.encode(str);
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/* 334 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum ResponseKeyword
/*     */     implements Rawable {
/* 340 */     SUBSCRIBE, PSUBSCRIBE, UNSUBSCRIBE, PUNSUBSCRIBE, MESSAGE, PMESSAGE, PONG,
/* 341 */     SSUBSCRIBE, SUNSUBSCRIBE, SMESSAGE;
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     ResponseKeyword() {
/* 346 */       this.raw = SafeEncoder.encode(name().toLowerCase(Locale.ENGLISH));
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/* 351 */       return this.raw;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum ClusterKeyword
/*     */     implements Rawable {
/* 357 */     MEET, RESET, INFO, FAILOVER, SLOTS, NODES, REPLICAS, SLAVES, MYID, ADDSLOTS, DELSLOTS,
/* 358 */     GETKEYSINSLOT, SETSLOT, NODE, MIGRATING, IMPORTING, STABLE, FORGET, FLUSHSLOTS, KEYSLOT,
/* 359 */     COUNTKEYSINSLOT, SAVECONFIG, REPLICATE, LINKS, ADDSLOTSRANGE, DELSLOTSRANGE, BUMPEPOCH,
/* 360 */     MYSHARDID, SHARDS;
/*     */     
/*     */     private final byte[] raw;
/*     */     
/*     */     ClusterKeyword() {
/* 365 */       this.raw = SafeEncoder.encode(name());
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getRaw() {
/* 370 */       return this.raw;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\Protocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */