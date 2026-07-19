/*      */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ExpiryOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortedSetOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.bloom.RedisBloomProtocol;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonObjectMapper;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonProtocol;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path2;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoSearchParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.SortingParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZAddParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZRangeParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.GeoRadiusResponse;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.LibraryInfo;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamEntry;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FTSearchParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.Query;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.Schema;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.aggr.AggregationResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.schemafields.SchemaField;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.TSElement;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.TSMRangeElements;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.TimeSeriesBuilderFactory;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.TimeSeriesProtocol;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*      */ 
/*      */ public class CommandObjects {
/*      */   private RedisProtocol protocol;
/*      */   
/*      */   public final void setProtocol(RedisProtocol proto) {
/*   45 */     this.protocol = proto;
/*      */   }
/*      */   private volatile JsonObjectMapper jsonObjectMapper;
/*      */   
/*      */   protected RedisProtocol getProtocol() {
/*   50 */     return this.protocol;
/*      */   }
/*      */ 
/*      */   
/*   54 */   private final AtomicInteger searchDialect = new AtomicInteger(0);
/*      */   
/*   56 */   private JedisBroadcastAndRoundRobinConfig broadcastAndRoundRobinConfig = null;
/*      */   
/*      */   void setBroadcastAndRoundRobinConfig(JedisBroadcastAndRoundRobinConfig config) {
/*   59 */     this.broadcastAndRoundRobinConfig = config;
/*      */   }
/*      */   
/*      */   protected CommandArguments commandArguments(ProtocolCommand command) {
/*   63 */     return new CommandArguments(command);
/*      */   }
/*      */   
/*   66 */   private final CommandObject<String> PING_COMMAND_OBJECT = new CommandObject<>(commandArguments(Protocol.Command.PING), BuilderFactory.STRING);
/*      */   
/*      */   public final CommandObject<String> ping() {
/*   69 */     return this.PING_COMMAND_OBJECT;
/*      */   }
/*      */   
/*   72 */   private final CommandObject<String> FLUSHALL_COMMAND_OBJECT = new CommandObject<>(commandArguments(Protocol.Command.FLUSHALL), BuilderFactory.STRING);
/*      */   
/*      */   public final CommandObject<String> flushAll() {
/*   75 */     return this.FLUSHALL_COMMAND_OBJECT;
/*      */   }
/*      */   
/*   78 */   private final CommandObject<String> FLUSHDB_COMMAND_OBJECT = new CommandObject<>(commandArguments(Protocol.Command.FLUSHDB), BuilderFactory.STRING);
/*      */   
/*      */   public final CommandObject<String> flushDB() {
/*   81 */     return this.FLUSHDB_COMMAND_OBJECT;
/*      */   }
/*      */   
/*      */   public final CommandObject<String> configSet(String parameter, String value) {
/*   85 */     return new CommandObject<>(commandArguments(Protocol.Command.CONFIG).add(Protocol.Keyword.SET).add(parameter).add(value), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Boolean> exists(String key) {
/*   90 */     return new CommandObject<>(commandArguments(Protocol.Command.EXISTS).key(key), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> exists(String... keys) {
/*   94 */     return new CommandObject<>(commandArguments(Protocol.Command.EXISTS).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> exists(byte[] key) {
/*   98 */     return new CommandObject<>(commandArguments(Protocol.Command.EXISTS).key(key), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> exists(byte[]... keys) {
/*  102 */     return new CommandObject<>(commandArguments(Protocol.Command.EXISTS).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> persist(String key) {
/*  106 */     return new CommandObject<>(commandArguments(Protocol.Command.PERSIST).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> persist(byte[] key) {
/*  110 */     return new CommandObject<>(commandArguments(Protocol.Command.PERSIST).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> type(String key) {
/*  114 */     return new CommandObject<>(commandArguments(Protocol.Command.TYPE).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> type(byte[] key) {
/*  118 */     return new CommandObject<>(commandArguments(Protocol.Command.TYPE).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> dump(String key) {
/*  122 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.DUMP).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> dump(byte[] key) {
/*  126 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.DUMP).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> restore(String key, long ttl, byte[] serializedValue) {
/*  130 */     return new CommandObject<>(commandArguments(Protocol.Command.RESTORE).key(key).add(Long.valueOf(ttl))
/*  131 */         .add(serializedValue), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> restore(String key, long ttl, byte[] serializedValue, RestoreParams params) {
/*  135 */     return new CommandObject<>(commandArguments(Protocol.Command.RESTORE).key(key).add(Long.valueOf(ttl))
/*  136 */         .add(serializedValue).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> restore(byte[] key, long ttl, byte[] serializedValue) {
/*  140 */     return new CommandObject<>(commandArguments(Protocol.Command.RESTORE).key(key).add(Long.valueOf(ttl))
/*  141 */         .add(serializedValue), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> restore(byte[] key, long ttl, byte[] serializedValue, RestoreParams params) {
/*  145 */     return new CommandObject<>(commandArguments(Protocol.Command.RESTORE).key(key).add(Long.valueOf(ttl))
/*  146 */         .add(serializedValue).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expire(String key, long seconds) {
/*  150 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIRE).key(key).add(Long.valueOf(seconds)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expire(byte[] key, long seconds) {
/*  154 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIRE).key(key).add(Long.valueOf(seconds)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expire(String key, long seconds, ExpiryOption expiryOption) {
/*  158 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIRE).key(key).add(Long.valueOf(seconds)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> expire(byte[] key, long seconds, ExpiryOption expiryOption) {
/*  163 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIRE).key(key).add(Long.valueOf(seconds)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> pexpire(String key, long milliseconds) {
/*  168 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIRE).key(key).add(Long.valueOf(milliseconds)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pexpire(byte[] key, long milliseconds) {
/*  172 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIRE).key(key).add(Long.valueOf(milliseconds)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pexpire(String key, long milliseconds, ExpiryOption expiryOption) {
/*  176 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIRE).key(key).add(Long.valueOf(milliseconds)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> pexpire(byte[] key, long milliseconds, ExpiryOption expiryOption) {
/*  181 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIRE).key(key).add(Long.valueOf(milliseconds)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> expireTime(String key) {
/*  186 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIRETIME).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expireTime(byte[] key) {
/*  190 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIRETIME).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pexpireTime(String key) {
/*  194 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIRETIME).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pexpireTime(byte[] key) {
/*  198 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIRETIME).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expireAt(String key, long unixTime) {
/*  202 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIREAT).key(key).add(Long.valueOf(unixTime)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expireAt(byte[] key, long unixTime) {
/*  206 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIREAT).key(key).add(Long.valueOf(unixTime)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expireAt(String key, long unixTime, ExpiryOption expiryOption) {
/*  210 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIREAT).key(key).add(Long.valueOf(unixTime)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> expireAt(byte[] key, long unixTime, ExpiryOption expiryOption) {
/*  214 */     return new CommandObject<>(commandArguments(Protocol.Command.EXPIREAT).key(key).add(Long.valueOf(unixTime)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pexpireAt(String key, long millisecondsTimestamp) {
/*  218 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIREAT).key(key).add(Long.valueOf(millisecondsTimestamp)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pexpireAt(byte[] key, long millisecondsTimestamp) {
/*  222 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIREAT).key(key).add(Long.valueOf(millisecondsTimestamp)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pexpireAt(String key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/*  226 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIREAT).key(key).add(Long.valueOf(millisecondsTimestamp)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> pexpireAt(byte[] key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/*  231 */     return new CommandObject<>(commandArguments(Protocol.Command.PEXPIREAT).key(key).add(Long.valueOf(millisecondsTimestamp)).add(expiryOption), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> ttl(String key) {
/*  236 */     return new CommandObject<>(commandArguments(Protocol.Command.TTL).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> ttl(byte[] key) {
/*  240 */     return new CommandObject<>(commandArguments(Protocol.Command.TTL).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pttl(String key) {
/*  244 */     return new CommandObject<>(commandArguments(Protocol.Command.PTTL).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pttl(byte[] key) {
/*  248 */     return new CommandObject<>(commandArguments(Protocol.Command.PTTL).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> touch(String key) {
/*  252 */     return new CommandObject<>(commandArguments(Protocol.Command.TOUCH).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> touch(String... keys) {
/*  256 */     return new CommandObject<>(commandArguments(Protocol.Command.TOUCH).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> touch(byte[] key) {
/*  260 */     return new CommandObject<>(commandArguments(Protocol.Command.TOUCH).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> touch(byte[]... keys) {
/*  264 */     return new CommandObject<>(commandArguments(Protocol.Command.TOUCH).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> sort(String key) {
/*  268 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> sort(String key, SortingParams sortingParams) {
/*  272 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key).addParams((IParams)sortingParams), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> sort(byte[] key) {
/*  276 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> sort(byte[] key, SortingParams sortingParams) {
/*  280 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key).addParams((IParams)sortingParams), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sort(String key, String dstkey) {
/*  284 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key)
/*  285 */         .add(Protocol.Keyword.STORE).key(dstkey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sort(String key, SortingParams sortingParams, String dstkey) {
/*  289 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key).addParams((IParams)sortingParams)
/*  290 */         .add(Protocol.Keyword.STORE).key(dstkey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sort(byte[] key, byte[] dstkey) {
/*  294 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key)
/*  295 */         .add(Protocol.Keyword.STORE).key(dstkey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sort(byte[] key, SortingParams sortingParams, byte[] dstkey) {
/*  299 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT).key(key).addParams((IParams)sortingParams)
/*  300 */         .add(Protocol.Keyword.STORE).key(dstkey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> sortReadonly(byte[] key, SortingParams sortingParams) {
/*  304 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT_RO).key(key).addParams((IParams)sortingParams), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<String>> sortReadonly(String key, SortingParams sortingParams) {
/*  309 */     return new CommandObject<>(commandArguments(Protocol.Command.SORT_RO).key(key).addParams((IParams)sortingParams), BuilderFactory.STRING_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> del(String key) {
/*  314 */     return new CommandObject<>(commandArguments(Protocol.Command.DEL).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> del(String... keys) {
/*  318 */     return new CommandObject<>(commandArguments(Protocol.Command.DEL).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> del(byte[] key) {
/*  322 */     return new CommandObject<>(commandArguments(Protocol.Command.DEL).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> del(byte[]... keys) {
/*  326 */     return new CommandObject<>(commandArguments(Protocol.Command.DEL).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> unlink(String key) {
/*  330 */     return new CommandObject<>(commandArguments(Protocol.Command.UNLINK).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> unlink(String... keys) {
/*  334 */     return new CommandObject<>(commandArguments(Protocol.Command.UNLINK).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> unlink(byte[] key) {
/*  338 */     return new CommandObject<>(commandArguments(Protocol.Command.UNLINK).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> unlink(byte[]... keys) {
/*  342 */     return new CommandObject<>(commandArguments(Protocol.Command.UNLINK).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> copy(String srcKey, String dstKey, boolean replace) {
/*  346 */     CommandArguments args = commandArguments(Protocol.Command.COPY).key(srcKey).key(dstKey);
/*  347 */     if (replace) {
/*  348 */       args.add(Protocol.Keyword.REPLACE);
/*      */     }
/*  350 */     return new CommandObject<>(args, BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> copy(byte[] srcKey, byte[] dstKey, boolean replace) {
/*  354 */     CommandArguments args = commandArguments(Protocol.Command.COPY).key(srcKey).key(dstKey);
/*  355 */     if (replace) {
/*  356 */       args.add(Protocol.Keyword.REPLACE);
/*      */     }
/*  358 */     return new CommandObject<>(args, BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> rename(String oldkey, String newkey) {
/*  362 */     return new CommandObject<>(commandArguments(Protocol.Command.RENAME).key(oldkey).key(newkey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> renamenx(String oldkey, String newkey) {
/*  366 */     return new CommandObject<>(commandArguments(Protocol.Command.RENAMENX).key(oldkey).key(newkey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> rename(byte[] oldkey, byte[] newkey) {
/*  370 */     return new CommandObject<>(commandArguments(Protocol.Command.RENAME).key(oldkey).key(newkey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> renamenx(byte[] oldkey, byte[] newkey) {
/*  374 */     return new CommandObject<>(commandArguments(Protocol.Command.RENAMENX).key(oldkey).key(newkey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public CommandObject<Long> dbSize() {
/*  378 */     return new CommandObject<>(commandArguments(Protocol.Command.DBSIZE), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public CommandObject<Set<String>> keys(String pattern) {
/*  382 */     CommandArguments args = commandArguments(Protocol.Command.KEYS).key(pattern);
/*  383 */     return new CommandObject<>(args, BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public CommandObject<Set<byte[]>> keys(byte[] pattern) {
/*  387 */     CommandArguments args = commandArguments(Protocol.Command.KEYS).key(pattern);
/*  388 */     return new CommandObject<>(args, BuilderFactory.BINARY_SET);
/*      */   }
/*      */   
/*      */   public CommandObject<ScanResult<String>> scan(String cursor) {
/*  392 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor), BuilderFactory.SCAN_RESPONSE);
/*      */   }
/*      */   
/*      */   public CommandObject<ScanResult<String>> scan(String cursor, ScanParams params) {
/*  396 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params), BuilderFactory.SCAN_RESPONSE);
/*      */   }
/*      */   
/*      */   public CommandObject<ScanResult<String>> scan(String cursor, ScanParams params, String type) {
/*  400 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).add(Protocol.Keyword.TYPE).add(type), BuilderFactory.SCAN_RESPONSE);
/*      */   }
/*      */   
/*      */   public CommandObject<ScanResult<byte[]>> scan(byte[] cursor) {
/*  404 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor), BuilderFactory.SCAN_BINARY_RESPONSE);
/*      */   }
/*      */   
/*      */   public CommandObject<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params) {
/*  408 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params), BuilderFactory.SCAN_BINARY_RESPONSE);
/*      */   }
/*      */   
/*      */   public CommandObject<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params, byte[] type) {
/*  412 */     return new CommandObject<>(commandArguments(Protocol.Command.SCAN).add(cursor).addParams((IParams)params).add(Protocol.Keyword.TYPE).add(type), BuilderFactory.SCAN_BINARY_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> randomKey() {
/*  416 */     return new CommandObject<>(commandArguments(Protocol.Command.RANDOMKEY), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> randomBinaryKey() {
/*  420 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.RANDOMKEY), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<String> set(String key, String value) {
/*  426 */     return new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> set(String key, String value, SetParams params) {
/*  430 */     return new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> set(byte[] key, byte[] value) {
/*  434 */     return new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> set(byte[] key, byte[] value, SetParams params) {
/*  438 */     return new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> get(String key) {
/*  442 */     return new CommandObject<>(commandArguments(Protocol.Command.GET).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> setGet(String key, String value) {
/*  446 */     return new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value).add(Protocol.Keyword.GET), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> setGet(String key, String value, SetParams params) {
/*  450 */     return new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value).addParams((IParams)params)
/*  451 */         .add(Protocol.Keyword.GET), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> getDel(String key) {
/*  455 */     return new CommandObject<>(commandArguments(Protocol.Command.GETDEL).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> getEx(String key, GetExParams params) {
/*  459 */     return new CommandObject<>(commandArguments(Protocol.Command.GETEX).key(key).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> get(byte[] key) {
/*  463 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.GET).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> setGet(byte[] key, byte[] value) {
/*  467 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value).add(Protocol.Keyword.GET), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> setGet(byte[] key, byte[] value, SetParams params) {
/*  471 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.SET).key(key).add(value).addParams((IParams)params)
/*  472 */         .add(Protocol.Keyword.GET), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> getDel(byte[] key) {
/*  476 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.GETDEL).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> getEx(byte[] key, GetExParams params) {
/*  480 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.GETEX).key(key).addParams((IParams)params), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> getSet(String key, String value) {
/*  484 */     return new CommandObject<>(commandArguments(Protocol.Command.GETSET).key(key).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> getSet(byte[] key, byte[] value) {
/*  488 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.GETSET).key(key).add(value), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> setnx(String key, String value) {
/*  492 */     return new CommandObject<>(commandArguments(Protocol.Command.SETNX).key(key).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> setex(String key, long seconds, String value) {
/*  496 */     return new CommandObject<>(commandArguments(Protocol.Command.SETEX).key(key).add(Long.valueOf(seconds)).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> psetex(String key, long milliseconds, String value) {
/*  500 */     return new CommandObject<>(commandArguments(Protocol.Command.PSETEX).key(key).add(Long.valueOf(milliseconds)).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> setnx(byte[] key, byte[] value) {
/*  504 */     return new CommandObject<>(commandArguments(Protocol.Command.SETNX).key(key).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> setex(byte[] key, long seconds, byte[] value) {
/*  508 */     return new CommandObject<>(commandArguments(Protocol.Command.SETEX).key(key).add(Long.valueOf(seconds)).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> psetex(byte[] key, long milliseconds, byte[] value) {
/*  512 */     return new CommandObject<>(commandArguments(Protocol.Command.PSETEX).key(key).add(Long.valueOf(milliseconds)).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> setbit(String key, long offset, boolean value) {
/*  516 */     return new CommandObject<>(commandArguments(Protocol.Command.SETBIT).key(key).add(Long.valueOf(offset)).add(Boolean.valueOf(value)), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> setbit(byte[] key, long offset, boolean value) {
/*  520 */     return new CommandObject<>(commandArguments(Protocol.Command.SETBIT).key(key).add(Long.valueOf(offset)).add(Boolean.valueOf(value)), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> getbit(String key, long offset) {
/*  524 */     return new CommandObject<>(commandArguments(Protocol.Command.GETBIT).key(key).add(Long.valueOf(offset)), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> getbit(byte[] key, long offset) {
/*  528 */     return new CommandObject<>(commandArguments(Protocol.Command.GETBIT).key(key).add(Long.valueOf(offset)), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> setrange(String key, long offset, String value) {
/*  532 */     return new CommandObject<>(commandArguments(Protocol.Command.SETRANGE).key(key).add(Long.valueOf(offset)).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> setrange(byte[] key, long offset, byte[] value) {
/*  536 */     return new CommandObject<>(commandArguments(Protocol.Command.SETRANGE).key(key).add(Long.valueOf(offset)).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> getrange(String key, long startOffset, long endOffset) {
/*  540 */     return new CommandObject<>(commandArguments(Protocol.Command.GETRANGE).key(key).add(Long.valueOf(startOffset)).add(Long.valueOf(endOffset)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> getrange(byte[] key, long startOffset, long endOffset) {
/*  544 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.GETRANGE).key(key).add(Long.valueOf(startOffset)).add(Long.valueOf(endOffset)), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> mget(String... keys) {
/*  548 */     return new CommandObject<>(commandArguments(Protocol.Command.MGET).keys((Object[])keys), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> mget(byte[]... keys) {
/*  552 */     return new CommandObject<>(commandArguments(Protocol.Command.MGET).keys((Object[])keys), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> mset(String... keysvalues) {
/*  556 */     return new CommandObject<>(addFlatKeyValueArgs(commandArguments(Protocol.Command.MSET), keysvalues), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> msetnx(String... keysvalues) {
/*  560 */     return new CommandObject<>(addFlatKeyValueArgs(commandArguments(Protocol.Command.MSETNX), keysvalues), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> mset(byte[]... keysvalues) {
/*  564 */     return new CommandObject<>(addFlatKeyValueArgs(commandArguments(Protocol.Command.MSET), keysvalues), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> msetnx(byte[]... keysvalues) {
/*  568 */     return new CommandObject<>(addFlatKeyValueArgs(commandArguments(Protocol.Command.MSETNX), keysvalues), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> incr(String key) {
/*  572 */     return new CommandObject<>(commandArguments(Protocol.Command.INCR).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> incrBy(String key, long increment) {
/*  576 */     return new CommandObject<>(commandArguments(Protocol.Command.INCRBY).key(key).add(Long.valueOf(increment)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> incrByFloat(String key, double increment) {
/*  580 */     return new CommandObject<>(commandArguments(Protocol.Command.INCRBYFLOAT).key(key).add(Double.valueOf(increment)), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> incr(byte[] key) {
/*  584 */     return new CommandObject<>(commandArguments(Protocol.Command.INCR).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> incrBy(byte[] key, long increment) {
/*  588 */     return new CommandObject<>(commandArguments(Protocol.Command.INCRBY).key(key).add(Long.valueOf(increment)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> incrByFloat(byte[] key, double increment) {
/*  592 */     return new CommandObject<>(commandArguments(Protocol.Command.INCRBYFLOAT).key(key).add(Double.valueOf(increment)), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> decr(String key) {
/*  596 */     return new CommandObject<>(commandArguments(Protocol.Command.DECR).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> decrBy(String key, long decrement) {
/*  600 */     return new CommandObject<>(commandArguments(Protocol.Command.DECRBY).key(key).add(Long.valueOf(decrement)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> decr(byte[] key) {
/*  604 */     return new CommandObject<>(commandArguments(Protocol.Command.DECR).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> decrBy(byte[] key, long decrement) {
/*  608 */     return new CommandObject<>(commandArguments(Protocol.Command.DECRBY).key(key).add(Long.valueOf(decrement)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> append(String key, String value) {
/*  612 */     return new CommandObject<>(commandArguments(Protocol.Command.APPEND).key(key).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> append(byte[] key, byte[] value) {
/*  616 */     return new CommandObject<>(commandArguments(Protocol.Command.APPEND).key(key).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> substr(String key, int start, int end) {
/*  620 */     return new CommandObject<>(commandArguments(Protocol.Command.SUBSTR).key(key).add(Integer.valueOf(start)).add(Integer.valueOf(end)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> substr(byte[] key, int start, int end) {
/*  624 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.SUBSTR).key(key).add(Integer.valueOf(start)).add(Integer.valueOf(end)), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> strlen(String key) {
/*  628 */     return new CommandObject<>(commandArguments(Protocol.Command.STRLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> strlen(byte[] key) {
/*  632 */     return new CommandObject<>(commandArguments(Protocol.Command.STRLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitcount(String key) {
/*  636 */     return new CommandObject<>(commandArguments(Protocol.Command.BITCOUNT).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitcount(String key, long start, long end) {
/*  640 */     return new CommandObject<>(commandArguments(Protocol.Command.BITCOUNT).key(key).add(Long.valueOf(start)).add(Long.valueOf(end)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitcount(String key, long start, long end, BitCountOption option) {
/*  644 */     return new CommandObject<>(commandArguments(Protocol.Command.BITCOUNT).key(key).add(Long.valueOf(start)).add(Long.valueOf(end)).add(option), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitcount(byte[] key) {
/*  648 */     return new CommandObject<>(commandArguments(Protocol.Command.BITCOUNT).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitcount(byte[] key, long start, long end) {
/*  652 */     return new CommandObject<>(commandArguments(Protocol.Command.BITCOUNT).key(key).add(Long.valueOf(start)).add(Long.valueOf(end)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitcount(byte[] key, long start, long end, BitCountOption option) {
/*  656 */     return new CommandObject<>(commandArguments(Protocol.Command.BITCOUNT).key(key).add(Long.valueOf(start)).add(Long.valueOf(end)).add(option), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitpos(String key, boolean value) {
/*  660 */     return new CommandObject<>(commandArguments(Protocol.Command.BITPOS).key(key).add(Integer.valueOf(value ? 1 : 0)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitpos(String key, boolean value, BitPosParams params) {
/*  664 */     return new CommandObject<>(commandArguments(Protocol.Command.BITPOS).key(key).add(Integer.valueOf(value ? 1 : 0)).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitpos(byte[] key, boolean value) {
/*  668 */     return new CommandObject<>(commandArguments(Protocol.Command.BITPOS).key(key).add(Integer.valueOf(value ? 1 : 0)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitpos(byte[] key, boolean value, BitPosParams params) {
/*  672 */     return new CommandObject<>(commandArguments(Protocol.Command.BITPOS).key(key).add(Integer.valueOf(value ? 1 : 0)).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> bitfield(String key, String... arguments) {
/*  676 */     return new CommandObject<>(commandArguments(Protocol.Command.BITFIELD).key(key).addObjects((Object[])arguments), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> bitfieldReadonly(String key, String... arguments) {
/*  680 */     return new CommandObject<>(commandArguments(Protocol.Command.BITFIELD_RO).key(key).addObjects((Object[])arguments), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> bitfield(byte[] key, byte[]... arguments) {
/*  684 */     return new CommandObject<>(commandArguments(Protocol.Command.BITFIELD).key(key).addObjects((Object[])arguments), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> bitfieldReadonly(byte[] key, byte[]... arguments) {
/*  688 */     return new CommandObject<>(commandArguments(Protocol.Command.BITFIELD_RO).key(key).addObjects((Object[])arguments), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitop(BitOP op, String destKey, String... srcKeys) {
/*  692 */     return new CommandObject<>(commandArguments(Protocol.Command.BITOP).add(op).key(destKey).keys((Object[])srcKeys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
/*  696 */     return new CommandObject<>(commandArguments(Protocol.Command.BITOP).add(op).key(destKey).keys((Object[])srcKeys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<LCSMatchResult> lcs(String keyA, String keyB, LCSParams params) {
/*  700 */     return new CommandObject<>(commandArguments(Protocol.Command.LCS).key(keyA).key(keyB)
/*  701 */         .addParams((IParams)params), BuilderFactory.STR_ALGO_LCS_RESULT_BUILDER);
/*      */   }
/*      */   
/*      */   public final CommandObject<LCSMatchResult> lcs(byte[] keyA, byte[] keyB, LCSParams params) {
/*  705 */     return new CommandObject<>(commandArguments(Protocol.Command.LCS).key(keyA).key(keyB)
/*  706 */         .addParams((IParams)params), BuilderFactory.STR_ALGO_LCS_RESULT_BUILDER);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> rpush(String key, String... strings) {
/*  712 */     return new CommandObject<>(commandArguments(Protocol.Command.RPUSH).key(key).addObjects((Object[])strings), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> rpush(byte[] key, byte[]... strings) {
/*  716 */     return new CommandObject<>(commandArguments(Protocol.Command.RPUSH).key(key).addObjects((Object[])strings), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpush(String key, String... strings) {
/*  720 */     return new CommandObject<>(commandArguments(Protocol.Command.LPUSH).key(key).addObjects((Object[])strings), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpush(byte[] key, byte[]... strings) {
/*  724 */     return new CommandObject<>(commandArguments(Protocol.Command.LPUSH).key(key).addObjects((Object[])strings), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> llen(String key) {
/*  728 */     return new CommandObject<>(commandArguments(Protocol.Command.LLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> llen(byte[] key) {
/*  732 */     return new CommandObject<>(commandArguments(Protocol.Command.LLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> lrange(String key, long start, long stop) {
/*  736 */     return new CommandObject<>(commandArguments(Protocol.Command.LRANGE).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> lrange(byte[] key, long start, long stop) {
/*  740 */     return new CommandObject<>(commandArguments(Protocol.Command.LRANGE).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ltrim(String key, long start, long stop) {
/*  744 */     return new CommandObject<>(commandArguments(Protocol.Command.LTRIM).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ltrim(byte[] key, long start, long stop) {
/*  748 */     return new CommandObject<>(commandArguments(Protocol.Command.LTRIM).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> lindex(String key, long index) {
/*  752 */     return new CommandObject<>(commandArguments(Protocol.Command.LINDEX).key(key).add(Long.valueOf(index)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> lindex(byte[] key, long index) {
/*  756 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.LINDEX).key(key).add(Long.valueOf(index)), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> lset(String key, long index, String value) {
/*  760 */     return new CommandObject<>(commandArguments(Protocol.Command.LSET).key(key).add(Long.valueOf(index)).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> lset(byte[] key, long index, byte[] value) {
/*  764 */     return new CommandObject<>(commandArguments(Protocol.Command.LSET).key(key).add(Long.valueOf(index)).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lrem(String key, long count, String value) {
/*  768 */     return new CommandObject<>(commandArguments(Protocol.Command.LREM).key(key).add(Long.valueOf(count)).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lrem(byte[] key, long count, byte[] value) {
/*  772 */     return new CommandObject<>(commandArguments(Protocol.Command.LREM).key(key).add(Long.valueOf(count)).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> lpop(String key) {
/*  776 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOP).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> lpop(String key, int count) {
/*  780 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOP).key(key).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> lpop(byte[] key) {
/*  784 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.LPOP).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> lpop(byte[] key, int count) {
/*  788 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOP).key(key).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> rpop(String key) {
/*  792 */     return new CommandObject<>(commandArguments(Protocol.Command.RPOP).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> rpop(String key, int count) {
/*  796 */     return new CommandObject<>(commandArguments(Protocol.Command.RPOP).key(key).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> rpop(byte[] key) {
/*  800 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.RPOP).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> rpop(byte[] key, int count) {
/*  804 */     return new CommandObject<>(commandArguments(Protocol.Command.RPOP).key(key).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpos(String key, String element) {
/*  808 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOS).key(key).add(element), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpos(String key, String element, LPosParams params) {
/*  812 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOS).key(key).add(element).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> lpos(String key, String element, LPosParams params, long count) {
/*  816 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOS).key(key).add(element)
/*  817 */         .addParams((IParams)params).add(Protocol.Keyword.COUNT).add(Long.valueOf(count)), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpos(byte[] key, byte[] element) {
/*  821 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOS).key(key).add(element), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpos(byte[] key, byte[] element, LPosParams params) {
/*  825 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOS).key(key).add(element).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> lpos(byte[] key, byte[] element, LPosParams params, long count) {
/*  829 */     return new CommandObject<>(commandArguments(Protocol.Command.LPOS).key(key).add(element)
/*  830 */         .addParams((IParams)params).add(Protocol.Keyword.COUNT).add(Long.valueOf(count)), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> linsert(String key, ListPosition where, String pivot, String value) {
/*  834 */     return new CommandObject<>(commandArguments(Protocol.Command.LINSERT).key(key).add(where)
/*  835 */         .add(pivot).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value) {
/*  839 */     return new CommandObject<>(commandArguments(Protocol.Command.LINSERT).key(key).add(where)
/*  840 */         .add(pivot).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpushx(String key, String... strings) {
/*  844 */     return new CommandObject<>(commandArguments(Protocol.Command.LPUSHX).key(key).addObjects((Object[])strings), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> rpushx(String key, String... strings) {
/*  848 */     return new CommandObject<>(commandArguments(Protocol.Command.RPUSHX).key(key).addObjects((Object[])strings), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> lpushx(byte[] key, byte[]... args) {
/*  852 */     return new CommandObject<>(commandArguments(Protocol.Command.LPUSHX).key(key).addObjects((Object[])args), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> rpushx(byte[] key, byte[]... args) {
/*  856 */     return new CommandObject<>(commandArguments(Protocol.Command.RPUSHX).key(key).addObjects((Object[])args), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> blpop(int timeout, String key) {
/*  860 */     return new CommandObject<>(commandArguments(Protocol.Command.BLPOP).blocking().key(key).add(Integer.valueOf(timeout)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> blpop(int timeout, String... keys) {
/*  864 */     return new CommandObject<>(commandArguments(Protocol.Command.BLPOP).blocking().keys((Object[])keys).add(Integer.valueOf(timeout)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, String>> blpop(double timeout, String key) {
/*  868 */     return new CommandObject<>(commandArguments(Protocol.Command.BLPOP).blocking().key(key).add(Double.valueOf(timeout)), BuilderFactory.KEYED_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, String>> blpop(double timeout, String... keys) {
/*  872 */     return new CommandObject<>(commandArguments(Protocol.Command.BLPOP).blocking().keys((Object[])keys).add(Double.valueOf(timeout)), BuilderFactory.KEYED_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> blpop(int timeout, byte[]... keys) {
/*  876 */     return new CommandObject<>(commandArguments(Protocol.Command.BLPOP).blocking().keys((Object[])keys).add(Integer.valueOf(timeout)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], byte[]>> blpop(double timeout, byte[]... keys) {
/*  880 */     return new CommandObject<>(commandArguments(Protocol.Command.BLPOP).blocking().keys((Object[])keys).add(Double.valueOf(timeout)), BuilderFactory.BINARY_KEYED_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> brpop(int timeout, String key) {
/*  884 */     return new CommandObject<>(commandArguments(Protocol.Command.BRPOP).blocking().key(key).add(Integer.valueOf(timeout)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> brpop(int timeout, String... keys) {
/*  888 */     return new CommandObject<>(commandArguments(Protocol.Command.BRPOP).blocking().keys((Object[])keys).add(Integer.valueOf(timeout)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, String>> brpop(double timeout, String key) {
/*  892 */     return new CommandObject<>(commandArguments(Protocol.Command.BRPOP).blocking().key(key).add(Double.valueOf(timeout)), BuilderFactory.KEYED_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, String>> brpop(double timeout, String... keys) {
/*  896 */     return new CommandObject<>(commandArguments(Protocol.Command.BRPOP).blocking().keys((Object[])keys).add(Double.valueOf(timeout)), BuilderFactory.KEYED_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> brpop(int timeout, byte[]... keys) {
/*  900 */     return new CommandObject<>(commandArguments(Protocol.Command.BRPOP).blocking().keys((Object[])keys).add(Integer.valueOf(timeout)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], byte[]>> brpop(double timeout, byte[]... keys) {
/*  904 */     return new CommandObject<>(commandArguments(Protocol.Command.BRPOP).blocking().keys((Object[])keys).add(Double.valueOf(timeout)), BuilderFactory.BINARY_KEYED_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> rpoplpush(String srckey, String dstkey) {
/*  908 */     return new CommandObject<>(commandArguments(Protocol.Command.RPOPLPUSH).key(srckey).key(dstkey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> brpoplpush(String source, String destination, int timeout) {
/*  912 */     return new CommandObject<>(commandArguments(Protocol.Command.BRPOPLPUSH).blocking().key(source)
/*  913 */         .key(destination).add(Integer.valueOf(timeout)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
/*  917 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.RPOPLPUSH).key(srckey).key(dstkey), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
/*  921 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.BRPOPLPUSH).blocking().key(source)
/*  922 */         .key(destination).add(Integer.valueOf(timeout)), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> lmove(String srcKey, String dstKey, ListDirection from, ListDirection to) {
/*  926 */     return new CommandObject<>(commandArguments(Protocol.Command.LMOVE).key(srcKey).key(dstKey)
/*  927 */         .add(from).add(to), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> blmove(String srcKey, String dstKey, ListDirection from, ListDirection to, double timeout) {
/*  931 */     return new CommandObject<>(commandArguments(Protocol.Command.BLMOVE).blocking().key(srcKey)
/*  932 */         .key(dstKey).add(from).add(to).add(Double.valueOf(timeout)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> lmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to) {
/*  936 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.LMOVE).key(srcKey).key(dstKey)
/*  937 */         .add(from).add(to), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to, double timeout) {
/*  941 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.BLMOVE).blocking().key(srcKey)
/*  942 */         .key(dstKey).add(from).add(to).add(Double.valueOf(timeout)), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<String>>> lmpop(ListDirection direction, String... keys) {
/*  946 */     return new CommandObject<>(commandArguments(Protocol.Command.LMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/*  947 */         .add(direction), BuilderFactory.KEYED_STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<String>>> lmpop(ListDirection direction, int count, String... keys) {
/*  951 */     return new CommandObject<>(commandArguments(Protocol.Command.LMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/*  952 */         .add(direction).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.KEYED_STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<String>>> blmpop(double timeout, ListDirection direction, String... keys) {
/*  956 */     return new CommandObject<>(commandArguments(Protocol.Command.BLMPOP).blocking().add(Double.valueOf(timeout))
/*  957 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys).add(direction), BuilderFactory.KEYED_STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<String>>> blmpop(double timeout, ListDirection direction, int count, String... keys) {
/*  961 */     return new CommandObject<>(commandArguments(Protocol.Command.BLMPOP).blocking().add(Double.valueOf(timeout))
/*  962 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys).add(direction).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.KEYED_STRING_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<byte[]>>> lmpop(ListDirection direction, byte[]... keys) {
/*  967 */     return new CommandObject<>(commandArguments(Protocol.Command.LMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/*  968 */         .add(direction), BuilderFactory.KEYED_BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<byte[]>>> lmpop(ListDirection direction, int count, byte[]... keys) {
/*  972 */     return new CommandObject<>(commandArguments(Protocol.Command.LMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/*  973 */         .add(direction).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.KEYED_BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<byte[]>>> blmpop(double timeout, ListDirection direction, byte[]... keys) {
/*  977 */     return new CommandObject<>(commandArguments(Protocol.Command.BLMPOP).blocking().add(Double.valueOf(timeout))
/*  978 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys).add(direction), BuilderFactory.KEYED_BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<byte[]>>> blmpop(double timeout, ListDirection direction, int count, byte[]... keys) {
/*  982 */     return new CommandObject<>(commandArguments(Protocol.Command.BLMPOP).blocking().add(Double.valueOf(timeout))
/*  983 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys).add(direction).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.KEYED_BINARY_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> hset(String key, String field, String value) {
/*  990 */     return new CommandObject<>(commandArguments(Protocol.Command.HSET).key(key).add(field).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hset(String key, Map<String, String> hash) {
/*  994 */     return new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.HSET).key(key), hash), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> hget(String key, String field) {
/*  998 */     return new CommandObject<>(commandArguments(Protocol.Command.HGET).key(key).add(field), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hsetnx(String key, String field, String value) {
/* 1002 */     return new CommandObject<>(commandArguments(Protocol.Command.HSETNX).key(key).add(field).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> hmset(String key, Map<String, String> hash) {
/* 1006 */     return new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.HMSET).key(key), hash), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> hmget(String key, String... fields) {
/* 1010 */     return new CommandObject<>(commandArguments(Protocol.Command.HMGET).key(key).addObjects((Object[])fields), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hset(byte[] key, byte[] field, byte[] value) {
/* 1014 */     return new CommandObject<>(commandArguments(Protocol.Command.HSET).key(key).add(field).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hset(byte[] key, Map<byte[], byte[]> hash) {
/* 1018 */     return new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.HSET).key(key), hash), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> hget(byte[] key, byte[] field) {
/* 1022 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.HGET).key(key).add(field), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hsetnx(byte[] key, byte[] field, byte[] value) {
/* 1026 */     return new CommandObject<>(commandArguments(Protocol.Command.HSETNX).key(key).add(field).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> hmset(byte[] key, Map<byte[], byte[]> hash) {
/* 1030 */     return new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.HMSET).key(key), hash), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> hmget(byte[] key, byte[]... fields) {
/* 1034 */     return new CommandObject<>(commandArguments(Protocol.Command.HMGET).key(key).addObjects((Object[])fields), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hincrBy(String key, String field, long value) {
/* 1038 */     return new CommandObject<>(commandArguments(Protocol.Command.HINCRBY).key(key).add(field).add(Long.valueOf(value)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> hincrByFloat(String key, String field, double value) {
/* 1042 */     return new CommandObject<>(commandArguments(Protocol.Command.HINCRBYFLOAT).key(key).add(field).add(Double.valueOf(value)), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> hexists(String key, String field) {
/* 1046 */     return new CommandObject<>(commandArguments(Protocol.Command.HEXISTS).key(key).add(field), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hdel(String key, String... field) {
/* 1050 */     return new CommandObject<>(commandArguments(Protocol.Command.HDEL).key(key).addObjects((Object[])field), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hlen(String key) {
/* 1054 */     return new CommandObject<>(commandArguments(Protocol.Command.HLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hincrBy(byte[] key, byte[] field, long value) {
/* 1058 */     return new CommandObject<>(commandArguments(Protocol.Command.HINCRBY).key(key).add(field).add(Long.valueOf(value)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> hincrByFloat(byte[] key, byte[] field, double value) {
/* 1062 */     return new CommandObject<>(commandArguments(Protocol.Command.HINCRBYFLOAT).key(key).add(field).add(Double.valueOf(value)), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> hexists(byte[] key, byte[] field) {
/* 1066 */     return new CommandObject<>(commandArguments(Protocol.Command.HEXISTS).key(key).add(field), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hdel(byte[] key, byte[]... field) {
/* 1070 */     return new CommandObject<>(commandArguments(Protocol.Command.HDEL).key(key).addObjects((Object[])field), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hlen(byte[] key) {
/* 1074 */     return new CommandObject<>(commandArguments(Protocol.Command.HLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> hkeys(String key) {
/* 1078 */     return new CommandObject<>(commandArguments(Protocol.Command.HKEYS).key(key), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> hvals(String key) {
/* 1082 */     return new CommandObject<>(commandArguments(Protocol.Command.HVALS).key(key), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<byte[]>> hkeys(byte[] key) {
/* 1086 */     return new CommandObject<>(commandArguments(Protocol.Command.HKEYS).key(key), BuilderFactory.BINARY_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> hvals(byte[] key) {
/* 1090 */     return new CommandObject<>(commandArguments(Protocol.Command.HVALS).key(key), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, String>> hgetAll(String key) {
/* 1094 */     return new CommandObject<>(commandArguments(Protocol.Command.HGETALL).key(key), BuilderFactory.STRING_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> hrandfield(String key) {
/* 1098 */     return new CommandObject<>(commandArguments(Protocol.Command.HRANDFIELD).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> hrandfield(String key, long count) {
/* 1102 */     return new CommandObject<>(commandArguments(Protocol.Command.HRANDFIELD).key(key).add(Long.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Map.Entry<String, String>>> hrandfieldWithValues(String key, long count) {
/* 1106 */     return new CommandObject<>(commandArguments(Protocol.Command.HRANDFIELD).key(key).add(Long.valueOf(count)).add(Protocol.Keyword.WITHVALUES), (this.protocol != RedisProtocol.RESP3) ? BuilderFactory.STRING_PAIR_LIST : BuilderFactory.STRING_PAIR_LIST_FROM_PAIRS);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Map<byte[], byte[]>> hgetAll(byte[] key) {
/* 1111 */     return new CommandObject<>(commandArguments(Protocol.Command.HGETALL).key(key), BuilderFactory.BINARY_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> hrandfield(byte[] key) {
/* 1115 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.HRANDFIELD).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> hrandfield(byte[] key, long count) {
/* 1119 */     return new CommandObject<>(commandArguments(Protocol.Command.HRANDFIELD).key(key).add(Long.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Map.Entry<byte[], byte[]>>> hrandfieldWithValues(byte[] key, long count) {
/* 1123 */     return new CommandObject<>(commandArguments(Protocol.Command.HRANDFIELD).key(key).add(Long.valueOf(count)).add(Protocol.Keyword.WITHVALUES), (this.protocol != RedisProtocol.RESP3) ? BuilderFactory.BINARY_PAIR_LIST : BuilderFactory.BINARY_PAIR_LIST_FROM_PAIRS);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<ScanResult<Map.Entry<String, String>>> hscan(String key, String cursor, ScanParams params) {
/* 1128 */     return new CommandObject<>(commandArguments(Protocol.Command.HSCAN).key(key).add(cursor).addParams((IParams)params), BuilderFactory.HSCAN_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hstrlen(String key, String field) {
/* 1132 */     return new CommandObject<>(commandArguments(Protocol.Command.HSTRLEN).key(key).add(field), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<ScanResult<Map.Entry<byte[], byte[]>>> hscan(byte[] key, byte[] cursor, ScanParams params) {
/* 1136 */     return new CommandObject<>(commandArguments(Protocol.Command.HSCAN).key(key).add(cursor).addParams((IParams)params), BuilderFactory.HSCAN_BINARY_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hstrlen(byte[] key, byte[] field) {
/* 1140 */     return new CommandObject<>(commandArguments(Protocol.Command.HSTRLEN).key(key).add(field), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> sadd(String key, String... members) {
/* 1146 */     return new CommandObject<>(commandArguments(Protocol.Command.SADD).key(key).addObjects((Object[])members), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sadd(byte[] key, byte[]... members) {
/* 1150 */     return new CommandObject<>(commandArguments(Protocol.Command.SADD).key(key).addObjects((Object[])members), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> smembers(String key) {
/* 1154 */     return new CommandObject<>(commandArguments(Protocol.Command.SMEMBERS).key(key), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<byte[]>> smembers(byte[] key) {
/* 1158 */     return new CommandObject<>(commandArguments(Protocol.Command.SMEMBERS).key(key), BuilderFactory.BINARY_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> srem(String key, String... members) {
/* 1162 */     return new CommandObject<>(commandArguments(Protocol.Command.SREM).key(key).addObjects((Object[])members), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> srem(byte[] key, byte[]... members) {
/* 1166 */     return new CommandObject<>(commandArguments(Protocol.Command.SREM).key(key).addObjects((Object[])members), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> spop(String key) {
/* 1170 */     return new CommandObject<>(commandArguments(Protocol.Command.SPOP).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> spop(byte[] key) {
/* 1174 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.SPOP).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> spop(String key, long count) {
/* 1178 */     return new CommandObject<>(commandArguments(Protocol.Command.SPOP).key(key).add(Long.valueOf(count)), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<byte[]>> spop(byte[] key, long count) {
/* 1182 */     return new CommandObject<>(commandArguments(Protocol.Command.SPOP).key(key).add(Long.valueOf(count)), BuilderFactory.BINARY_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> scard(String key) {
/* 1186 */     return new CommandObject<>(commandArguments(Protocol.Command.SCARD).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> scard(byte[] key) {
/* 1190 */     return new CommandObject<>(commandArguments(Protocol.Command.SCARD).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> sismember(String key, String member) {
/* 1194 */     return new CommandObject<>(commandArguments(Protocol.Command.SISMEMBER).key(key).add(member), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> sismember(byte[] key, byte[] member) {
/* 1198 */     return new CommandObject<>(commandArguments(Protocol.Command.SISMEMBER).key(key).add(member), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> smismember(String key, String... members) {
/* 1202 */     return new CommandObject<>(commandArguments(Protocol.Command.SMISMEMBER).key(key).addObjects((Object[])members), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> smismember(byte[] key, byte[]... members) {
/* 1206 */     return new CommandObject<>(commandArguments(Protocol.Command.SMISMEMBER).key(key).addObjects((Object[])members), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> srandmember(String key) {
/* 1210 */     return new CommandObject<>(commandArguments(Protocol.Command.SRANDMEMBER).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> srandmember(byte[] key) {
/* 1214 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.SRANDMEMBER).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> srandmember(String key, int count) {
/* 1218 */     return new CommandObject<>(commandArguments(Protocol.Command.SRANDMEMBER).key(key).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> srandmember(byte[] key, int count) {
/* 1222 */     return new CommandObject<>(commandArguments(Protocol.Command.SRANDMEMBER).key(key).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<ScanResult<String>> sscan(String key, String cursor, ScanParams params) {
/* 1226 */     return new CommandObject<>(commandArguments(Protocol.Command.SSCAN).key(key).add(cursor).addParams((IParams)params), BuilderFactory.SSCAN_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<ScanResult<byte[]>> sscan(byte[] key, byte[] cursor, ScanParams params) {
/* 1230 */     return new CommandObject<>(commandArguments(Protocol.Command.SSCAN).key(key).add(cursor).addParams((IParams)params), BuilderFactory.SSCAN_BINARY_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> sdiff(String... keys) {
/* 1234 */     return new CommandObject<>(commandArguments(Protocol.Command.SDIFF).keys((Object[])keys), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sdiffstore(String dstkey, String... keys) {
/* 1238 */     return new CommandObject<>(commandArguments(Protocol.Command.SDIFFSTORE).key(dstkey).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<byte[]>> sdiff(byte[]... keys) {
/* 1242 */     return new CommandObject<>(commandArguments(Protocol.Command.SDIFF).keys((Object[])keys), BuilderFactory.BINARY_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
/* 1246 */     return new CommandObject<>(commandArguments(Protocol.Command.SDIFFSTORE).key(dstkey).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> sinter(String... keys) {
/* 1250 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTER).keys((Object[])keys), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sinterstore(String dstkey, String... keys) {
/* 1254 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTERSTORE).key(dstkey).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sintercard(String... keys) {
/* 1258 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTERCARD).add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sintercard(int limit, String... keys) {
/* 1262 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTERCARD).add(Integer.valueOf(keys.length)).keys((Object[])keys).add(Protocol.Keyword.LIMIT).add(Integer.valueOf(limit)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<byte[]>> sinter(byte[]... keys) {
/* 1266 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTER).keys((Object[])keys), BuilderFactory.BINARY_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sinterstore(byte[] dstkey, byte[]... keys) {
/* 1270 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTERSTORE).key(dstkey).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sintercard(byte[]... keys) {
/* 1274 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTERCARD).add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sintercard(int limit, byte[]... keys) {
/* 1278 */     return new CommandObject<>(commandArguments(Protocol.Command.SINTERCARD).add(Integer.valueOf(keys.length)).keys((Object[])keys).add(Protocol.Keyword.LIMIT).add(Integer.valueOf(limit)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> sunion(String... keys) {
/* 1282 */     return new CommandObject<>(commandArguments(Protocol.Command.SUNION).keys((Object[])keys), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sunionstore(String dstkey, String... keys) {
/* 1286 */     return new CommandObject<>(commandArguments(Protocol.Command.SUNIONSTORE).key(dstkey).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<byte[]>> sunion(byte[]... keys) {
/* 1290 */     return new CommandObject<>(commandArguments(Protocol.Command.SUNION).keys((Object[])keys), BuilderFactory.BINARY_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> sunionstore(byte[] dstkey, byte[]... keys) {
/* 1294 */     return new CommandObject<>(commandArguments(Protocol.Command.SUNIONSTORE).key(dstkey).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> smove(String srckey, String dstkey, String member) {
/* 1298 */     return new CommandObject<>(commandArguments(Protocol.Command.SMOVE).key(srckey).key(dstkey).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
/* 1302 */     return new CommandObject<>(commandArguments(Protocol.Command.SMOVE).key(srckey).key(dstkey).add(member), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> zadd(String key, double score, String member) {
/* 1308 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).add(Double.valueOf(score)).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zadd(String key, double score, String member, ZAddParams params) {
/* 1312 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).addParams((IParams)params)
/* 1313 */         .add(Double.valueOf(score)).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zadd(String key, Map<String, Double> scoreMembers) {
/* 1317 */     return new CommandObject<>(addSortedSetFlatMapArgs(commandArguments(Protocol.Command.ZADD).key(key), scoreMembers), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
/* 1321 */     return new CommandObject<>(addSortedSetFlatMapArgs(commandArguments(Protocol.Command.ZADD).key(key).addParams((IParams)params), scoreMembers), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zaddIncr(String key, double score, String member, ZAddParams params) {
/* 1325 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).add(Protocol.Keyword.INCR)
/* 1326 */         .addParams((IParams)params).add(Double.valueOf(score)).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zadd(byte[] key, double score, byte[] member) {
/* 1330 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).add(Double.valueOf(score)).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zadd(byte[] key, double score, byte[] member, ZAddParams params) {
/* 1334 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).addParams((IParams)params)
/* 1335 */         .add(Double.valueOf(score)).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers) {
/* 1339 */     return new CommandObject<>(addSortedSetFlatMapArgs(commandArguments(Protocol.Command.ZADD).key(key), scoreMembers), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
/* 1343 */     return new CommandObject<>(addSortedSetFlatMapArgs(commandArguments(Protocol.Command.ZADD).key(key).addParams((IParams)params), scoreMembers), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zaddIncr(byte[] key, double score, byte[] member, ZAddParams params) {
/* 1347 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).add(Protocol.Keyword.INCR)
/* 1348 */         .addParams((IParams)params).add(Double.valueOf(score)).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zincrby(String key, double increment, String member) {
/* 1352 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINCRBY).key(key).add(Double.valueOf(increment)).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zincrby(String key, double increment, String member, ZIncrByParams params) {
/* 1356 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).addParams((IParams)params).add(Double.valueOf(increment)).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zincrby(byte[] key, double increment, byte[] member) {
/* 1360 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINCRBY).key(key).add(Double.valueOf(increment)).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
/* 1364 */     return new CommandObject<>(commandArguments(Protocol.Command.ZADD).key(key).addParams((IParams)params).add(Double.valueOf(increment)).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrem(String key, String... members) {
/* 1368 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREM).key(key).addObjects((Object[])members), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrem(byte[] key, byte[]... members) {
/* 1372 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREM).key(key).addObjects((Object[])members), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrank(String key, String member) {
/* 1376 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANK).key(key).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrevrank(String key, String member) {
/* 1380 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANK).key(key).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<Long, Double>> zrankWithScore(String key, String member) {
/* 1384 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANK).key(key).add(member).add(Protocol.Keyword.WITHSCORE), BuilderFactory.ZRANK_WITHSCORE_PAIR);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<Long, Double>> zrevrankWithScore(String key, String member) {
/* 1388 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANK).key(key).add(member).add(Protocol.Keyword.WITHSCORE), BuilderFactory.ZRANK_WITHSCORE_PAIR);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrank(byte[] key, byte[] member) {
/* 1392 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANK).key(key).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrevrank(byte[] key, byte[] member) {
/* 1396 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANK).key(key).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<Long, Double>> zrankWithScore(byte[] key, byte[] member) {
/* 1400 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANK).key(key).add(member).add(Protocol.Keyword.WITHSCORE), BuilderFactory.ZRANK_WITHSCORE_PAIR);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<Long, Double>> zrevrankWithScore(byte[] key, byte[] member) {
/* 1404 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANK).key(key).add(member).add(Protocol.Keyword.WITHSCORE), BuilderFactory.ZRANK_WITHSCORE_PAIR);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> zrandmember(String key) {
/* 1408 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANDMEMBER).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrandmember(String key, long count) {
/* 1412 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANDMEMBER).key(key).add(Long.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrandmemberWithScores(String key, long count) {
/* 1416 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANDMEMBER).key(key).add(Long.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> zrandmember(byte[] key) {
/* 1420 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.ZRANDMEMBER).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrandmember(byte[] key, long count) {
/* 1424 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANDMEMBER).key(key).add(Long.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrandmemberWithScores(byte[] key, long count) {
/* 1428 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANDMEMBER).key(key).add(Long.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zcard(String key) {
/* 1432 */     return new CommandObject<>(commandArguments(Protocol.Command.ZCARD).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zscore(String key, String member) {
/* 1436 */     return new CommandObject<>(commandArguments(Protocol.Command.ZSCORE).key(key).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Double>> zmscore(String key, String... members) {
/* 1440 */     return new CommandObject<>(commandArguments(Protocol.Command.ZMSCORE).key(key).addObjects((Object[])members), BuilderFactory.DOUBLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zcard(byte[] key) {
/* 1444 */     return new CommandObject<>(commandArguments(Protocol.Command.ZCARD).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> zscore(byte[] key, byte[] member) {
/* 1448 */     return new CommandObject<>(commandArguments(Protocol.Command.ZSCORE).key(key).add(member), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Double>> zmscore(byte[] key, byte[]... members) {
/* 1452 */     return new CommandObject<>(commandArguments(Protocol.Command.ZMSCORE).key(key).addObjects((Object[])members), BuilderFactory.DOUBLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Tuple> zpopmax(String key) {
/* 1456 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMAX).key(key), BuilderFactory.TUPLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zpopmax(String key, int count) {
/* 1460 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMAX).key(key).add(Integer.valueOf(count)), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Tuple> zpopmin(String key) {
/* 1464 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMIN).key(key), BuilderFactory.TUPLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zpopmin(String key, int count) {
/* 1468 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMIN).key(key).add(Integer.valueOf(count)), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Tuple> zpopmax(byte[] key) {
/* 1472 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMAX).key(key), BuilderFactory.TUPLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zpopmax(byte[] key, int count) {
/* 1476 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMAX).key(key).add(Integer.valueOf(count)), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Tuple> zpopmin(byte[] key) {
/* 1480 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMIN).key(key), BuilderFactory.TUPLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zpopmin(byte[] key, int count) {
/* 1484 */     return new CommandObject<>(commandArguments(Protocol.Command.ZPOPMIN).key(key).add(Integer.valueOf(count)), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, Tuple>> bzpopmax(double timeout, String... keys) {
/* 1488 */     return new CommandObject<>(commandArguments(Protocol.Command.BZPOPMAX).blocking().keys((Object[])keys).add(Double.valueOf(timeout)), BuilderFactory.KEYED_TUPLE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<KeyValue<String, Tuple>> bzpopmin(double timeout, String... keys) {
/* 1493 */     return new CommandObject<>(commandArguments(Protocol.Command.BZPOPMIN).blocking().keys((Object[])keys).add(Double.valueOf(timeout)), BuilderFactory.KEYED_TUPLE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], Tuple>> bzpopmax(double timeout, byte[]... keys) {
/* 1498 */     return new CommandObject<>(commandArguments(Protocol.Command.BZPOPMAX).blocking().keys((Object[])keys)
/* 1499 */         .add(Double.valueOf(timeout)), BuilderFactory.BINARY_KEYED_TUPLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], Tuple>> bzpopmin(double timeout, byte[]... keys) {
/* 1503 */     return new CommandObject<>(commandArguments(Protocol.Command.BZPOPMIN).blocking().keys((Object[])keys)
/* 1504 */         .add(Double.valueOf(timeout)), BuilderFactory.BINARY_KEYED_TUPLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zcount(String key, double min, double max) {
/* 1508 */     return new CommandObject<>(commandArguments(Protocol.Command.ZCOUNT).key(key).add(Double.valueOf(min)).add(Double.valueOf(max)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zcount(String key, String min, String max) {
/* 1512 */     return new CommandObject<>(commandArguments(Protocol.Command.ZCOUNT).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zcount(byte[] key, double min, double max) {
/* 1516 */     return new CommandObject<>(commandArguments(Protocol.Command.ZCOUNT).key(key).add(Double.valueOf(min)).add(Double.valueOf(max)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zcount(byte[] key, byte[] min, byte[] max) {
/* 1520 */     return new CommandObject<>(commandArguments(Protocol.Command.ZCOUNT).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrange(String key, long start, long stop) {
/* 1524 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrevrange(String key, long start, long stop) {
/* 1528 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGE).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeWithScores(String key, long start, long stop) {
/* 1532 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key)
/* 1533 */         .add(Long.valueOf(start)).add(Long.valueOf(stop)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeWithScores(String key, long start, long stop) {
/* 1537 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGE).key(key)
/* 1538 */         .add(Long.valueOf(start)).add(Long.valueOf(stop)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrange(String key, ZRangeParams zRangeParams) {
/* 1542 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key).addParams((IParams)zRangeParams), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeWithScores(String key, ZRangeParams zRangeParams) {
/* 1546 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key).addParams((IParams)zRangeParams).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrangestore(String dest, String src, ZRangeParams zRangeParams) {
/* 1550 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGESTORE).key(dest).add(src).addParams((IParams)zRangeParams), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrangeByScore(String key, double min, double max) {
/* 1554 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrangeByScore(String key, String min, String max) {
/* 1558 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrevrangeByScore(String key, double max, double min) {
/* 1562 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrevrangeByScore(String key, String max, String min) {
/* 1566 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrangeByScore(String key, double min, double max, int offset, int count) {
/* 1570 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max))
/* 1571 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrangeByScore(String key, String min, String max, int offset, int count) {
/* 1575 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max)
/* 1576 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrevrangeByScore(String key, double max, double min, int offset, int count) {
/* 1580 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min))
/* 1581 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrevrangeByScore(String key, String max, String min, int offset, int count) {
/* 1585 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min)
/* 1586 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
/* 1590 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max))
/* 1591 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
/* 1595 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max)
/* 1596 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
/* 1600 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min))
/* 1601 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
/* 1605 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min)
/* 1606 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
/* 1610 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max))
/* 1611 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
/* 1615 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max)
/* 1616 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
/* 1620 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min))
/* 1621 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
/* 1625 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min)
/* 1626 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrange(byte[] key, long start, long stop) {
/* 1630 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrevrange(byte[] key, long start, long stop) {
/* 1634 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGE).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeWithScores(byte[] key, long start, long stop) {
/* 1638 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key)
/* 1639 */         .add(Long.valueOf(start)).add(Long.valueOf(stop)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeWithScores(byte[] key, long start, long stop) {
/* 1643 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGE).key(key)
/* 1644 */         .add(Long.valueOf(start)).add(Long.valueOf(stop)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrange(byte[] key, ZRangeParams zRangeParams) {
/* 1648 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key).addParams((IParams)zRangeParams), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeWithScores(byte[] key, ZRangeParams zRangeParams) {
/* 1652 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGE).key(key).addParams((IParams)zRangeParams).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zrangestore(byte[] dest, byte[] src, ZRangeParams zRangeParams) {
/* 1656 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGESTORE).key(dest).add(src).addParams((IParams)zRangeParams), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrangeByScore(byte[] key, double min, double max) {
/* 1660 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 1664 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrevrangeByScore(byte[] key, double max, double min) {
/* 1668 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
/* 1672 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
/* 1676 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max))
/* 1677 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 1681 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max)
/* 1682 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
/* 1686 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min))
/* 1687 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 1691 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min)
/* 1692 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max) {
/* 1696 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max))
/* 1697 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
/* 1701 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max)
/* 1702 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
/* 1706 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min))
/* 1707 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
/* 1711 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min)
/* 1712 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
/* 1716 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max))
/* 1717 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 1721 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYSCORE).key(key).add(min).add(max)
/* 1722 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
/* 1726 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(Double.valueOf(max)).add(Double.valueOf(min))
/* 1727 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 1731 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYSCORE).key(key).add(max).add(min)
/* 1732 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByRank(String key, long start, long stop) {
/* 1736 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYRANK).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByScore(String key, double min, double max) {
/* 1740 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByScore(String key, String min, String max) {
/* 1744 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYSCORE).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByRank(byte[] key, long start, long stop) {
/* 1748 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYRANK).key(key).add(Long.valueOf(start)).add(Long.valueOf(stop)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByScore(byte[] key, double min, double max) {
/* 1752 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYSCORE).key(key).add(Double.valueOf(min)).add(Double.valueOf(max)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 1756 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYSCORE).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zlexcount(String key, String min, String max) {
/* 1760 */     return new CommandObject<>(commandArguments(Protocol.Command.ZLEXCOUNT).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrangeByLex(String key, String min, String max) {
/* 1764 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYLEX).key(key).add(min).add(max), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrangeByLex(String key, String min, String max, int offset, int count) {
/* 1768 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYLEX).key(key).add(min).add(max)
/* 1769 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrevrangeByLex(String key, String max, String min) {
/* 1773 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYLEX).key(key).add(max).add(min), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zrevrangeByLex(String key, String max, String min, int offset, int count) {
/* 1777 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYLEX).key(key).add(max).add(min)
/* 1778 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByLex(String key, String min, String max) {
/* 1782 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYLEX).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zlexcount(byte[] key, byte[] min, byte[] max) {
/* 1786 */     return new CommandObject<>(commandArguments(Protocol.Command.ZLEXCOUNT).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 1790 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYLEX).key(key).add(min).add(max), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 1794 */     return new CommandObject<>(commandArguments(Protocol.Command.ZRANGEBYLEX).key(key).add(min).add(max)
/* 1795 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
/* 1799 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYLEX).key(key).add(max).add(min), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 1803 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREVRANGEBYLEX).key(key).add(max).add(min)
/* 1804 */         .add(Protocol.Keyword.LIMIT).add(Integer.valueOf(offset)).add(Integer.valueOf(count)), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zremrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 1808 */     return new CommandObject<>(commandArguments(Protocol.Command.ZREMRANGEBYLEX).key(key).add(min).add(max), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<ScanResult<Tuple>> zscan(String key, String cursor, ScanParams params) {
/* 1812 */     return new CommandObject<>(commandArguments(Protocol.Command.ZSCAN).key(key).add(cursor).addParams((IParams)params), BuilderFactory.ZSCAN_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<ScanResult<Tuple>> zscan(byte[] key, byte[] cursor, ScanParams params) {
/* 1816 */     return new CommandObject<>(commandArguments(Protocol.Command.ZSCAN).key(key).add(cursor).addParams((IParams)params), BuilderFactory.ZSCAN_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zdiff(String... keys) {
/* 1820 */     return new CommandObject<>(commandArguments(Protocol.Command.ZDIFF).add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.STRING_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Tuple>> zdiffWithScores(String... keys) {
/* 1825 */     return new CommandObject<>(commandArguments(Protocol.Command.ZDIFF).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1826 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> zdiffStore(String dstkey, String... keys) {
/* 1834 */     return zdiffstore(dstkey, keys);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zdiffstore(String dstkey, String... keys) {
/* 1838 */     return new CommandObject<>(commandArguments(Protocol.Command.ZDIFFSTORE).key(dstkey)
/* 1839 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zdiff(byte[]... keys) {
/* 1843 */     return new CommandObject<>(commandArguments(Protocol.Command.ZDIFF).add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zdiffWithScores(byte[]... keys) {
/* 1847 */     return new CommandObject<>(commandArguments(Protocol.Command.ZDIFF).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1848 */         .add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> zdiffStore(byte[] dstkey, byte[]... keys) {
/* 1856 */     return zdiffstore(dstkey, keys);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zdiffstore(byte[] dstkey, byte[]... keys) {
/* 1860 */     return new CommandObject<>(commandArguments(Protocol.Command.ZDIFFSTORE).key(dstkey)
/* 1861 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zinter(ZParams params, String... keys) {
/* 1865 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTER).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1866 */         .addParams((IParams)params), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zinterWithScores(ZParams params, String... keys) {
/* 1870 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTER).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1871 */         .addParams((IParams)params).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zinterstore(String dstkey, String... keys) {
/* 1875 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERSTORE).key(dstkey)
/* 1876 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zinterstore(String dstkey, ZParams params, String... keys) {
/* 1880 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERSTORE).key(dstkey)
/* 1881 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zintercard(String... keys) {
/* 1885 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERCARD).add(Integer.valueOf(keys.length))
/* 1886 */         .keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zintercard(long limit, String... keys) {
/* 1890 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERCARD).add(Integer.valueOf(keys.length))
/* 1891 */         .keys((Object[])keys).add(Protocol.Keyword.LIMIT).add(Long.valueOf(limit)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zinterstore(byte[] dstkey, byte[]... sets) {
/* 1895 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERSTORE).key(dstkey)
/* 1896 */         .add(Integer.valueOf(sets.length)).keys((Object[])sets), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 1900 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERSTORE).key(dstkey)
/* 1901 */         .add(Integer.valueOf(sets.length)).keys((Object[])sets).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zintercard(byte[]... keys) {
/* 1905 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERCARD).add(Integer.valueOf(keys.length))
/* 1906 */         .keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zintercard(long limit, byte[]... keys) {
/* 1910 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTERCARD).add(Integer.valueOf(keys.length))
/* 1911 */         .keys((Object[])keys).add(Protocol.Keyword.LIMIT).add(Long.valueOf(limit)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zinter(ZParams params, byte[]... keys) {
/* 1915 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTER).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1916 */         .addParams((IParams)params), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zinterWithScores(ZParams params, byte[]... keys) {
/* 1920 */     return new CommandObject<>(commandArguments(Protocol.Command.ZINTER).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1921 */         .addParams((IParams)params).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zunionstore(String dstkey, String... sets) {
/* 1925 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNIONSTORE).key(dstkey)
/* 1926 */         .add(Integer.valueOf(sets.length)).keys((Object[])sets), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zunionstore(String dstkey, ZParams params, String... sets) {
/* 1930 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNIONSTORE).key(dstkey)
/* 1931 */         .add(Integer.valueOf(sets.length)).keys((Object[])sets).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> zunion(ZParams params, String... keys) {
/* 1935 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNION).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1936 */         .addParams((IParams)params), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zunionWithScores(ZParams params, String... keys) {
/* 1940 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNION).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1941 */         .addParams((IParams)params).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zunionstore(byte[] dstkey, byte[]... sets) {
/* 1945 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNIONSTORE).key(dstkey)
/* 1946 */         .add(Integer.valueOf(sets.length)).keys((Object[])sets), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 1950 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNIONSTORE).key(dstkey)
/* 1951 */         .add(Integer.valueOf(sets.length)).keys((Object[])sets).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> zunion(ZParams params, byte[]... keys) {
/* 1955 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNION).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1956 */         .addParams((IParams)params), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> zunionWithScores(ZParams params, byte[]... keys) {
/* 1960 */     return new CommandObject<>(commandArguments(Protocol.Command.ZUNION).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1961 */         .addParams((IParams)params).add(Protocol.Keyword.WITHSCORES), getTupleListBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<Tuple>>> zmpop(SortedSetOption option, String... keys) {
/* 1965 */     return new CommandObject<>(commandArguments(Protocol.Command.ZMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1966 */         .add(option), BuilderFactory.KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<Tuple>>> zmpop(SortedSetOption option, int count, String... keys) {
/* 1970 */     return new CommandObject<>(commandArguments(Protocol.Command.ZMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1971 */         .add(option).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<Tuple>>> bzmpop(double timeout, SortedSetOption option, String... keys) {
/* 1975 */     return new CommandObject<>(commandArguments(Protocol.Command.BZMPOP).blocking().add(Double.valueOf(timeout)).add(Integer.valueOf(keys.length))
/* 1976 */         .keys((Object[])keys).add(option), BuilderFactory.KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<String, List<Tuple>>> bzmpop(double timeout, SortedSetOption option, int count, String... keys) {
/* 1980 */     return new CommandObject<>(commandArguments(Protocol.Command.BZMPOP).blocking().add(Double.valueOf(timeout)).add(Integer.valueOf(keys.length))
/* 1981 */         .keys((Object[])keys).add(option).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<Tuple>>> zmpop(SortedSetOption option, byte[]... keys) {
/* 1985 */     return new CommandObject<>(commandArguments(Protocol.Command.ZMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1986 */         .add(option), BuilderFactory.BINARY_KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<Tuple>>> zmpop(SortedSetOption option, int count, byte[]... keys) {
/* 1990 */     return new CommandObject<>(commandArguments(Protocol.Command.ZMPOP).add(Integer.valueOf(keys.length)).keys((Object[])keys)
/* 1991 */         .add(option).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.BINARY_KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<Tuple>>> bzmpop(double timeout, SortedSetOption option, byte[]... keys) {
/* 1995 */     return new CommandObject<>(commandArguments(Protocol.Command.BZMPOP).blocking().add(Double.valueOf(timeout)).add(Integer.valueOf(keys.length))
/* 1996 */         .keys((Object[])keys).add(option), BuilderFactory.BINARY_KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<KeyValue<byte[], List<Tuple>>> bzmpop(double timeout, SortedSetOption option, int count, byte[]... keys) {
/* 2000 */     return new CommandObject<>(commandArguments(Protocol.Command.BZMPOP).blocking().add(Double.valueOf(timeout)).add(Integer.valueOf(keys.length))
/* 2001 */         .keys((Object[])keys).add(option).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.BINARY_KEYED_TUPLE_LIST);
/*      */   }
/*      */   
/*      */   private Builder<List<Tuple>> getTupleListBuilder() {
/* 2005 */     return (this.protocol == RedisProtocol.RESP3) ? BuilderFactory.TUPLE_LIST_RESP3 : BuilderFactory.TUPLE_LIST;
/*      */   }
/*      */   
/*      */   private Builder<Set<Tuple>> getTupleSetBuilder() {
/* 2009 */     return (this.protocol == RedisProtocol.RESP3) ? BuilderFactory.TUPLE_ZSET_RESP3 : BuilderFactory.TUPLE_ZSET;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geoadd(String key, double longitude, double latitude, String member) {
/* 2015 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOADD).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude)).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 2019 */     return new CommandObject<>(addGeoCoordinateFlatMapArgs(commandArguments(Protocol.Command.GEOADD).key(key), memberCoordinateMap), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geoadd(String key, GeoAddParams params, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 2023 */     return new CommandObject<>(addGeoCoordinateFlatMapArgs(commandArguments(Protocol.Command.GEOADD).key(key).addParams((IParams)params), memberCoordinateMap), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> geodist(String key, String member1, String member2) {
/* 2027 */     return new CommandObject<>(commandArguments(Protocol.Command.GEODIST).key(key).add(member1).add(member2), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> geodist(String key, String member1, String member2, GeoUnit unit) {
/* 2031 */     return new CommandObject<>(commandArguments(Protocol.Command.GEODIST).key(key).add(member1).add(member2).add(unit), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> geohash(String key, String... members) {
/* 2035 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOHASH).key(key).addObjects((Object[])members), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoCoordinate>> geopos(String key, String... members) {
/* 2039 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOPOS).key(key).addObjects((Object[])members), BuilderFactory.GEO_COORDINATE_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geoadd(byte[] key, double longitude, double latitude, byte[] member) {
/* 2044 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOADD).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude)).add(member), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 2048 */     return new CommandObject<>(addGeoCoordinateFlatMapArgs(commandArguments(Protocol.Command.GEOADD).key(key), memberCoordinateMap), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 2052 */     return new CommandObject<>(addGeoCoordinateFlatMapArgs(commandArguments(Protocol.Command.GEOADD).key(key).addParams((IParams)params), memberCoordinateMap), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> geodist(byte[] key, byte[] member1, byte[] member2) {
/* 2056 */     return new CommandObject<>(commandArguments(Protocol.Command.GEODIST).key(key).add(member1).add(member2), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
/* 2060 */     return new CommandObject<>(commandArguments(Protocol.Command.GEODIST).key(key).add(member1).add(member2).add(unit), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<byte[]>> geohash(byte[] key, byte[]... members) {
/* 2064 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOHASH).key(key).addObjects((Object[])members), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoCoordinate>> geopos(byte[] key, byte[]... members) {
/* 2068 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOPOS).key(key).addObjects((Object[])members), BuilderFactory.GEO_COORDINATE_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2073 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2074 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2079 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2080 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2084 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS_RO).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2085 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2090 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS_RO).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2091 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> georadiusStore(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2096 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2097 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param).addParams((IParams)storeParam), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
/* 2101 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER).key(key).add(member)
/* 2102 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2107 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER).key(key).add(member)
/* 2108 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit) {
/* 2112 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER_RO).key(key).add(member)
/* 2113 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2118 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER_RO).key(key).add(member)
/* 2119 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> georadiusByMemberStore(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2124 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER).key(key).add(member)
/* 2125 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param).addParams((IParams)storeParam), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2129 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2130 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2135 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2136 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 2141 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS_RO).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2142 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2147 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS_RO).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2148 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> georadiusStore(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2153 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUS).key(key).add(Double.valueOf(longitude)).add(Double.valueOf(latitude))
/* 2154 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param).addParams((IParams)storeParam), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 2158 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER).key(key).add(member)
/* 2159 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2163 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER).key(key).add(member)
/* 2164 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 2168 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER_RO).key(key).add(member)
/* 2169 */         .add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 2173 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER_RO).key(key).add(member)
/* 2174 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> georadiusByMemberStore(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 2179 */     return new CommandObject<>(commandArguments(Protocol.Command.GEORADIUSBYMEMBER).key(key).add(member)
/* 2180 */         .add(Double.valueOf(radius)).add(unit).addParams((IParams)param).addParams((IParams)storeParam), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, String member, double radius, GeoUnit unit) {
/* 2185 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2186 */         .add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2191 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key)
/* 2192 */         .add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude())).add(Double.valueOf(coord.getLatitude()))
/* 2193 */         .add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, String member, double width, double height, GeoUnit unit) {
/* 2198 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2199 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2204 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key)
/* 2205 */         .add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude())).add(Double.valueOf(coord.getLatitude()))
/* 2206 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(String key, GeoSearchParam params) {
/* 2210 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key).addParams((IParams)params), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(String dest, String src, String member, double radius, GeoUnit unit) {
/* 2216 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2217 */         .add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(String dest, String src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2222 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude()))
/* 2223 */         .add(Double.valueOf(coord.getLatitude())).add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(String dest, String src, String member, double width, double height, GeoUnit unit) {
/* 2228 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2229 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(String dest, String src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2234 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src)
/* 2235 */         .add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude())).add(Double.valueOf(coord.getLatitude()))
/* 2236 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(String dest, String src, GeoSearchParam params) {
/* 2240 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geosearchStoreStoreDist(String dest, String src, GeoSearchParam params) {
/* 2244 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).addParams((IParams)params).add(Protocol.Keyword.STOREDIST), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 2249 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2250 */         .add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(byte[] key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2255 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key)
/* 2256 */         .add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude())).add(Double.valueOf(coord.getLatitude()))
/* 2257 */         .add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(byte[] key, byte[] member, double width, double height, GeoUnit unit) {
/* 2262 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2263 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(byte[] key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2268 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key)
/* 2269 */         .add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude())).add(Double.valueOf(coord.getLatitude()))
/* 2270 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GeoRadiusResponse>> geosearch(byte[] key, GeoSearchParam params) {
/* 2274 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCH).key(key).addParams((IParams)params), BuilderFactory.GEORADIUS_WITH_PARAMS_RESULT);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, byte[] member, double radius, GeoUnit unit) {
/* 2280 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2281 */         .add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 2286 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src)
/* 2287 */         .add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude())).add(Double.valueOf(coord.getLatitude()))
/* 2288 */         .add(Protocol.Keyword.BYRADIUS).add(Double.valueOf(radius)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, byte[] member, double width, double height, GeoUnit unit) {
/* 2293 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).add(Protocol.Keyword.FROMMEMBER).add(member)
/* 2294 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 2299 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src)
/* 2300 */         .add(Protocol.Keyword.FROMLONLAT).add(Double.valueOf(coord.getLongitude())).add(Double.valueOf(coord.getLatitude()))
/* 2301 */         .add(Protocol.Keyword.BYBOX).add(Double.valueOf(width)).add(Double.valueOf(height)).add(unit), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geosearchStore(byte[] dest, byte[] src, GeoSearchParam params) {
/* 2305 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> geosearchStoreStoreDist(byte[] dest, byte[] src, GeoSearchParam params) {
/* 2309 */     return new CommandObject<>(commandArguments(Protocol.Command.GEOSEARCHSTORE).key(dest).add(src).addParams((IParams)params).add(Protocol.Keyword.STOREDIST), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> pfadd(String key, String... elements) {
/* 2315 */     return new CommandObject<>(commandArguments(Protocol.Command.PFADD).key(key).addObjects((Object[])elements), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> pfmerge(String destkey, String... sourcekeys) {
/* 2319 */     return new CommandObject<>(commandArguments(Protocol.Command.PFMERGE).key(destkey).keys((Object[])sourcekeys), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pfadd(byte[] key, byte[]... elements) {
/* 2323 */     return new CommandObject<>(commandArguments(Protocol.Command.PFADD).key(key).addObjects((Object[])elements), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
/* 2327 */     return new CommandObject<>(commandArguments(Protocol.Command.PFMERGE).key(destkey).keys((Object[])sourcekeys), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pfcount(String key) {
/* 2331 */     return new CommandObject<>(commandArguments(Protocol.Command.PFCOUNT).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pfcount(String... keys) {
/* 2335 */     return new CommandObject<>(commandArguments(Protocol.Command.PFCOUNT).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pfcount(byte[] key) {
/* 2339 */     return new CommandObject<>(commandArguments(Protocol.Command.PFCOUNT).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> pfcount(byte[]... keys) {
/* 2343 */     return new CommandObject<>(commandArguments(Protocol.Command.PFCOUNT).keys((Object[])keys), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<StreamEntryID> xadd(String key, StreamEntryID id, Map<String, String> hash) {
/* 2349 */     return new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.XADD).key(key).add((id == null) ? StreamEntryID.NEW_ENTRY : id), hash), BuilderFactory.STREAM_ENTRY_ID);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<StreamEntryID> xadd(String key, XAddParams params, Map<String, String> hash) {
/* 2354 */     return new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.XADD).key(key).addParams((IParams)params), hash), BuilderFactory.STREAM_ENTRY_ID);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> xlen(String key) {
/* 2359 */     return new CommandObject<>(commandArguments(Protocol.Command.XLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> xadd(byte[] key, XAddParams params, Map<byte[], byte[]> hash) {
/* 2363 */     return (CommandObject)new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.XADD).key(key).addParams((IParams)params), hash), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> xlen(byte[] key) {
/* 2368 */     return new CommandObject<>(commandArguments(Protocol.Command.XLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrange(String key, StreamEntryID start, StreamEntryID end) {
/* 2372 */     return new CommandObject<>(commandArguments(Protocol.Command.XRANGE).key(key).add((start == null) ? "-" : start).add((end == null) ? "+" : end), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrange(String key, StreamEntryID start, StreamEntryID end, int count) {
/* 2377 */     return new CommandObject<>(commandArguments(Protocol.Command.XRANGE).key(key).add((start == null) ? "-" : start).add((end == null) ? "+" : end)
/* 2378 */         .add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrevrange(String key, StreamEntryID end, StreamEntryID start) {
/* 2382 */     return new CommandObject<>(commandArguments(Protocol.Command.XREVRANGE).key(key).add((end == null) ? "+" : end).add((start == null) ? "-" : start), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count) {
/* 2387 */     return new CommandObject<>(commandArguments(Protocol.Command.XREVRANGE).key(key).add((end == null) ? "+" : end).add((start == null) ? "-" : start)
/* 2388 */         .add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrange(String key, String start, String end) {
/* 2392 */     return new CommandObject<>(commandArguments(Protocol.Command.XRANGE).key(key).add(start).add(end), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrange(String key, String start, String end, int count) {
/* 2396 */     return new CommandObject<>(commandArguments(Protocol.Command.XRANGE).key(key).add(start).add(end).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrevrange(String key, String end, String start) {
/* 2400 */     return new CommandObject<>(commandArguments(Protocol.Command.XREVRANGE).key(key).add(end).add(start), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xrevrange(String key, String end, String start, int count) {
/* 2404 */     return new CommandObject<>(commandArguments(Protocol.Command.XREVRANGE).key(key).add(end).add(start).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> xrange(byte[] key, byte[] start, byte[] end) {
/* 2408 */     return new CommandObject<>(commandArguments(Protocol.Command.XRANGE).key(key).add((start == null) ? "-" : start).add((end == null) ? "+" : end), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Object>> xrange(byte[] key, byte[] start, byte[] end, int count) {
/* 2413 */     return new CommandObject<>(commandArguments(Protocol.Command.XRANGE).key(key).add((start == null) ? "-" : start).add((end == null) ? "+" : end)
/* 2414 */         .add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> xrevrange(byte[] key, byte[] end, byte[] start) {
/* 2418 */     return new CommandObject<>(commandArguments(Protocol.Command.XREVRANGE).key(key).add((end == null) ? "+" : end).add((start == null) ? "-" : start), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Object>> xrevrange(byte[] key, byte[] end, byte[] start, int count) {
/* 2423 */     return new CommandObject<>(commandArguments(Protocol.Command.XREVRANGE).key(key).add((end == null) ? "+" : end).add((start == null) ? "-" : start)
/* 2424 */         .add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xack(String key, String group, StreamEntryID... ids) {
/* 2428 */     return new CommandObject<>(commandArguments(Protocol.Command.XACK).key(key).add(group).addObjects((Object[])ids), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xack(byte[] key, byte[] group, byte[]... ids) {
/* 2432 */     return new CommandObject<>(commandArguments(Protocol.Command.XACK).key(key).add(group).addObjects((Object[])ids), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> xgroupCreate(String key, String groupName, StreamEntryID id, boolean makeStream) {
/* 2437 */     CommandArguments args = commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.CREATE).key(key).add(groupName).add((id == null) ? "0-0" : id);
/* 2438 */     if (makeStream) args.add(Protocol.Keyword.MKSTREAM); 
/* 2439 */     return new CommandObject<>(args, BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> xgroupSetID(String key, String groupName, StreamEntryID id) {
/* 2443 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.SETID)
/* 2444 */         .key(key).add(groupName).add(id), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xgroupDestroy(String key, String groupName) {
/* 2448 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.DESTROY)
/* 2449 */         .key(key).add(groupName), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> xgroupCreateConsumer(String key, String groupName, String consumerName) {
/* 2453 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.CREATECONSUMER)
/* 2454 */         .key(key).add(groupName).add(consumerName), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xgroupDelConsumer(String key, String groupName, String consumerName) {
/* 2458 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.DELCONSUMER)
/* 2459 */         .key(key).add(groupName).add(consumerName), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> xgroupCreate(byte[] key, byte[] groupName, byte[] id, boolean makeStream) {
/* 2464 */     CommandArguments args = commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.CREATE).key(key).add(groupName).add(id);
/* 2465 */     if (makeStream) args.add(Protocol.Keyword.MKSTREAM); 
/* 2466 */     return new CommandObject<>(args, BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> xgroupSetID(byte[] key, byte[] groupName, byte[] id) {
/* 2470 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.SETID)
/* 2471 */         .key(key).add(groupName).add(id), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xgroupDestroy(byte[] key, byte[] groupName) {
/* 2475 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.DESTROY)
/* 2476 */         .key(key).add(groupName), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> xgroupCreateConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 2480 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.CREATECONSUMER)
/* 2481 */         .key(key).add(groupName).add(consumerName), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xgroupDelConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 2485 */     return new CommandObject<>(commandArguments(Protocol.Command.XGROUP).add(Protocol.Keyword.DELCONSUMER)
/* 2486 */         .key(key).add(groupName).add(consumerName), BuilderFactory.LONG);
/*      */   }
/*      */   public final CommandObject<Long> xdel(String key, StreamEntryID... ids) {
/* 2489 */     return new CommandObject<>(commandArguments(Protocol.Command.XDEL).key(key).addObjects((Object[])ids), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xtrim(String key, long maxLen, boolean approximate) {
/* 2493 */     CommandArguments args = commandArguments(Protocol.Command.XTRIM).key(key).add(Protocol.Keyword.MAXLEN);
/* 2494 */     if (approximate) args.add(Protocol.BYTES_TILDE); 
/* 2495 */     args.add(Long.valueOf(maxLen));
/* 2496 */     return new CommandObject<>(args, BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xtrim(String key, XTrimParams params) {
/* 2500 */     return new CommandObject<>(commandArguments(Protocol.Command.XTRIM).key(key).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xdel(byte[] key, byte[]... ids) {
/* 2504 */     return new CommandObject<>(commandArguments(Protocol.Command.XDEL).key(key).addObjects((Object[])ids), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xtrim(byte[] key, long maxLen, boolean approximateLength) {
/* 2508 */     CommandArguments args = commandArguments(Protocol.Command.XTRIM).key(key).add(Protocol.Keyword.MAXLEN);
/* 2509 */     if (approximateLength) args.add(Protocol.BYTES_TILDE); 
/* 2510 */     args.add(Long.valueOf(maxLen));
/* 2511 */     return new CommandObject<>(args, BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> xtrim(byte[] key, XTrimParams params) {
/* 2515 */     return new CommandObject<>(commandArguments(Protocol.Command.XTRIM).key(key).addParams((IParams)params), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<StreamPendingSummary> xpending(String key, String groupName) {
/* 2519 */     return new CommandObject<>(commandArguments(Protocol.Command.XPENDING).key(key).add(groupName), BuilderFactory.STREAM_PENDING_SUMMARY);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<StreamPendingEntry>> xpending(String key, String groupName, XPendingParams params) {
/* 2524 */     return new CommandObject<>(commandArguments(Protocol.Command.XPENDING).key(key).add(groupName)
/* 2525 */         .addParams((IParams)params), BuilderFactory.STREAM_PENDING_ENTRY_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> xpending(byte[] key, byte[] groupName) {
/* 2529 */     return new CommandObject(commandArguments(Protocol.Command.XPENDING).key(key).add(groupName), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Object>> xpending(byte[] key, byte[] groupName, XPendingParams params) {
/* 2534 */     return new CommandObject<>(commandArguments(Protocol.Command.XPENDING).key(key).add(groupName)
/* 2535 */         .addParams((IParams)params), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<StreamEntry>> xclaim(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 2540 */     return new CommandObject<>(commandArguments(Protocol.Command.XCLAIM).key(key).add(group)
/* 2541 */         .add(consumerName).add(Long.valueOf(minIdleTime)).addObjects((Object[])ids).addParams((IParams)params), BuilderFactory.STREAM_ENTRY_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<List<StreamEntryID>> xclaimJustId(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 2547 */     return new CommandObject<>(commandArguments(Protocol.Command.XCLAIM).key(key).add(group)
/* 2548 */         .add(consumerName).add(Long.valueOf(minIdleTime)).addObjects((Object[])ids).addParams((IParams)params)
/* 2549 */         .add(Protocol.Keyword.JUSTID), BuilderFactory.STREAM_ENTRY_ID_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Map.Entry<StreamEntryID, List<StreamEntry>>> xautoclaim(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 2555 */     return new CommandObject<>(commandArguments(Protocol.Command.XAUTOCLAIM).key(key).add(group)
/* 2556 */         .add(consumerName).add(Long.valueOf(minIdleTime)).add(start).addParams((IParams)params), BuilderFactory.STREAM_AUTO_CLAIM_RESPONSE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Map.Entry<StreamEntryID, List<StreamEntryID>>> xautoclaimJustId(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 2563 */     return new CommandObject<>(commandArguments(Protocol.Command.XAUTOCLAIM).key(key).add(group)
/* 2564 */         .add(consumerName).add(Long.valueOf(minIdleTime)).add(start).addParams((IParams)params)
/* 2565 */         .add(Protocol.Keyword.JUSTID), BuilderFactory.STREAM_AUTO_CLAIM_JUSTID_RESPONSE);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<byte[]>> xclaim(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 2570 */     return new CommandObject<>(commandArguments(Protocol.Command.XCLAIM).key(key).add(group)
/* 2571 */         .add(consumerName).add(Long.valueOf(minIdleTime)).addObjects((Object[])ids).addParams((IParams)params), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<List<byte[]>> xclaimJustId(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 2577 */     return new CommandObject<>(commandArguments(Protocol.Command.XCLAIM).key(key).add(group)
/* 2578 */         .add(consumerName).add(Long.valueOf(minIdleTime)).addObjects((Object[])ids).addParams((IParams)params)
/* 2579 */         .add(Protocol.Keyword.JUSTID), BuilderFactory.BINARY_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Object>> xautoclaim(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 2584 */     return new CommandObject<>(commandArguments(Protocol.Command.XAUTOCLAIM).key(key).add(groupName)
/* 2585 */         .add(consumerName).add(Long.valueOf(minIdleTime)).add(start).addParams((IParams)params), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Object>> xautoclaimJustId(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 2591 */     return new CommandObject<>(commandArguments(Protocol.Command.XAUTOCLAIM).key(key).add(groupName)
/* 2592 */         .add(consumerName).add(Long.valueOf(minIdleTime)).add(start).addParams((IParams)params)
/* 2593 */         .add(Protocol.Keyword.JUSTID), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<StreamInfo> xinfoStream(String key) {
/* 2597 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.STREAM).key(key), BuilderFactory.STREAM_INFO);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> xinfoStream(byte[] key) {
/* 2601 */     return new CommandObject(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.STREAM).key(key), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<StreamFullInfo> xinfoStreamFull(String key) {
/* 2605 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.STREAM).key(key).add(Protocol.Keyword.FULL), BuilderFactory.STREAM_FULL_INFO);
/*      */   }
/*      */   
/*      */   public final CommandObject<StreamFullInfo> xinfoStreamFull(String key, int count) {
/* 2609 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.STREAM).key(key).add(Protocol.Keyword.FULL).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.STREAM_FULL_INFO);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> xinfoStreamFull(byte[] key, int count) {
/* 2613 */     return new CommandObject(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.STREAM).key(key).add(Protocol.Keyword.FULL).add(Protocol.Keyword.COUNT).add(Integer.valueOf(count)), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> xinfoStreamFull(byte[] key) {
/* 2617 */     return new CommandObject(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.STREAM).key(key).add(Protocol.Keyword.FULL), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamGroupInfo>> xinfoGroups(String key) {
/* 2621 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.GROUPS).key(key), BuilderFactory.STREAM_GROUP_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> xinfoGroups(byte[] key) {
/* 2625 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.GROUPS).key(key), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<List<StreamConsumersInfo>> xinfoConsumers(String key, String group) {
/* 2633 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.CONSUMERS).key(key).add(group), BuilderFactory.STREAM_CONSUMERS_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<StreamConsumerInfo>> xinfoConsumers2(String key, String group) {
/* 2637 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.CONSUMERS).key(key).add(group), BuilderFactory.STREAM_CONSUMER_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> xinfoConsumers(byte[] key, byte[] group) {
/* 2641 */     return new CommandObject<>(commandArguments(Protocol.Command.XINFO).add(Protocol.Keyword.CONSUMERS).key(key).add(group), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Map.Entry<String, List<StreamEntry>>>> xread(XReadParams xReadParams, Map<String, StreamEntryID> streams) {
/* 2646 */     CommandArguments args = commandArguments(Protocol.Command.XREAD).addParams((IParams)xReadParams).add(Protocol.Keyword.STREAMS);
/* 2647 */     Set<Map.Entry<String, StreamEntryID>> entrySet = streams.entrySet();
/* 2648 */     entrySet.forEach(entry -> args.key(entry.getKey()));
/* 2649 */     entrySet.forEach(entry -> args.add(entry.getValue()));
/* 2650 */     return new CommandObject<>(args, BuilderFactory.STREAM_READ_RESPONSE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Map.Entry<String, List<StreamEntry>>>> xreadGroup(String groupName, String consumer, XReadGroupParams xReadGroupParams, Map<String, StreamEntryID> streams) {
/* 2658 */     CommandArguments args = commandArguments(Protocol.Command.XREADGROUP).add(Protocol.Keyword.GROUP).add(groupName).add(consumer).addParams((IParams)xReadGroupParams).add(Protocol.Keyword.STREAMS);
/* 2659 */     Set<Map.Entry<String, StreamEntryID>> entrySet = streams.entrySet();
/* 2660 */     entrySet.forEach(entry -> args.key(entry.getKey()));
/* 2661 */     entrySet.forEach(entry -> args.add(entry.getValue()));
/* 2662 */     return new CommandObject<>(args, BuilderFactory.STREAM_READ_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> xread(XReadParams xReadParams, Map.Entry<byte[], byte[]>... streams) {
/* 2666 */     CommandArguments args = commandArguments(Protocol.Command.XREAD).addParams((IParams)xReadParams).add(Protocol.Keyword.STREAMS);
/* 2667 */     for (Map.Entry<byte[], byte[]> entry : streams) {
/* 2668 */       args.key(entry.getKey());
/*      */     }
/* 2670 */     for (Map.Entry<byte[], byte[]> entry : streams) {
/* 2671 */       args.add(entry.getValue());
/*      */     }
/* 2673 */     return new CommandObject<>(args, BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Object>> xreadGroup(byte[] groupName, byte[] consumer, XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
/* 2680 */     CommandArguments args = commandArguments(Protocol.Command.XREADGROUP).add(Protocol.Keyword.GROUP).add(groupName).add(consumer).addParams((IParams)xReadGroupParams).add(Protocol.Keyword.STREAMS);
/* 2681 */     for (Map.Entry<byte[], byte[]> entry : streams) {
/* 2682 */       args.key(entry.getKey());
/*      */     }
/* 2684 */     for (Map.Entry<byte[], byte[]> entry : streams) {
/* 2685 */       args.add(entry.getValue());
/*      */     }
/* 2687 */     return new CommandObject<>(args, BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Object> eval(String script) {
/* 2693 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(0)), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> eval(String script, String sampleKey) {
/* 2697 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(0)).processKey(sampleKey), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> eval(String script, int keyCount, String... params) {
/* 2701 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(keyCount))
/* 2702 */         .addObjects((Object[])params).processKeys(Arrays.<String>copyOf(params, keyCount)), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Object> eval(String script, List<String> keys, List<String> args) {
/* 2707 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(keys.size()))
/* 2708 */         .keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalReadonly(String script, List<String> keys, List<String> args) {
/* 2712 */     return new CommandObject(commandArguments(Protocol.Command.EVAL_RO).add(script).add(Integer.valueOf(keys.size()))
/* 2713 */         .keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> eval(byte[] script) {
/* 2717 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(0)), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> eval(byte[] script, byte[] sampleKey) {
/* 2721 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(0)).processKey(sampleKey), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> eval(byte[] script, int keyCount, byte[]... params) {
/* 2725 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(keyCount))
/* 2726 */         .addObjects((Object[])params).processKeys(Arrays.<byte[]>copyOf(params, keyCount)), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Object> eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 2731 */     return new CommandObject(commandArguments(Protocol.Command.EVAL).add(script).add(Integer.valueOf(keys.size()))
/* 2732 */         .keys(keys).addObjects(args), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalReadonly(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 2736 */     return new CommandObject(commandArguments(Protocol.Command.EVAL_RO).add(script).add(Integer.valueOf(keys.size()))
/* 2737 */         .keys(keys).addObjects(args), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalsha(String sha1) {
/* 2741 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(0)), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalsha(String sha1, String sampleKey) {
/* 2745 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(0)).processKey(sampleKey), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalsha(String sha1, int keyCount, String... params) {
/* 2749 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(keyCount))
/* 2750 */         .addObjects((Object[])params).processKeys(Arrays.<String>copyOf(params, keyCount)), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Object> evalsha(String sha1, List<String> keys, List<String> args) {
/* 2755 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(keys.size()))
/* 2756 */         .keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalshaReadonly(String sha1, List<String> keys, List<String> args) {
/* 2760 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA_RO).add(sha1).add(Integer.valueOf(keys.size()))
/* 2761 */         .keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalsha(byte[] sha1) {
/* 2765 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(0)), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalsha(byte[] sha1, byte[] sampleKey) {
/* 2769 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(0)).processKey(sampleKey), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
/* 2773 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(keyCount))
/* 2774 */         .addObjects((Object[])params).processKeys(Arrays.<byte[]>copyOf(params, keyCount)), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Object> evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 2779 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA).add(sha1).add(Integer.valueOf(keys.size()))
/* 2780 */         .keys(keys).addObjects(args), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> evalshaReadonly(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 2784 */     return new CommandObject(commandArguments(Protocol.Command.EVALSHA_RO).add(sha1).add(Integer.valueOf(keys.size()))
/* 2785 */         .keys(keys).addObjects(args), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> scriptExists(List<String> sha1s) {
/* 2789 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.EXISTS).addObjects(sha1s), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> scriptExists(String sampleKey, String... sha1s) {
/* 2793 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.EXISTS).addObjects((Object[])sha1s)
/* 2794 */         .processKey(sampleKey), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptLoad(String script) {
/* 2798 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.LOAD).add(script), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptLoad(String script, String sampleKey) {
/* 2802 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.LOAD).add(script).processKey(sampleKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/* 2805 */   private final CommandObject<String> SCRIPT_FLUSH_COMMAND_OBJECT = new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.FLUSH), BuilderFactory.STRING);
/*      */   
/*      */   public final CommandObject<String> scriptFlush() {
/* 2808 */     return this.SCRIPT_FLUSH_COMMAND_OBJECT;
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptFlush(String sampleKey) {
/* 2812 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.FLUSH).processKey(sampleKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptFlush(String sampleKey, FlushMode flushMode) {
/* 2816 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.FLUSH).add(flushMode).processKey(sampleKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/* 2819 */   private final CommandObject<String> SCRIPT_KILL_COMMAND_OBJECT = new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.KILL), BuilderFactory.STRING);
/*      */   
/*      */   public final CommandObject<String> scriptKill() {
/* 2822 */     return this.SCRIPT_KILL_COMMAND_OBJECT;
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptKill(String sampleKey) {
/* 2826 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.KILL).processKey(sampleKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> scriptExists(byte[] sampleKey, byte[]... sha1s) {
/* 2830 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.EXISTS).addObjects((Object[])sha1s)
/* 2831 */         .processKey(sampleKey), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> scriptLoad(byte[] script, byte[] sampleKey) {
/* 2835 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.LOAD).add(script).processKey(sampleKey), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptFlush(byte[] sampleKey) {
/* 2839 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.FLUSH).processKey(sampleKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptFlush(byte[] sampleKey, FlushMode flushMode) {
/* 2843 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.FLUSH).add(flushMode).processKey(sampleKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> scriptKill(byte[] sampleKey) {
/* 2847 */     return new CommandObject<>(commandArguments(Protocol.Command.SCRIPT).add(Protocol.Keyword.KILL).processKey(sampleKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/* 2850 */   private final CommandObject<String> SLOWLOG_RESET_COMMAND_OBJECT = new CommandObject<>(
/* 2851 */       commandArguments(Protocol.Command.SLOWLOG).add(Protocol.Keyword.RESET), BuilderFactory.STRING);
/*      */   
/*      */   public final CommandObject<String> slowlogReset() {
/* 2854 */     return this.SLOWLOG_RESET_COMMAND_OBJECT;
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> fcall(String name, List<String> keys, List<String> args) {
/* 2858 */     return new CommandObject(commandArguments(Protocol.Command.FCALL).add(name).add(Integer.valueOf(keys.size()))
/* 2859 */         .keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> fcallReadonly(String name, List<String> keys, List<String> args) {
/* 2863 */     return new CommandObject(commandArguments(Protocol.Command.FCALL_RO).add(name).add(Integer.valueOf(keys.size()))
/* 2864 */         .keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionDelete(String libraryName) {
/* 2868 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.DELETE).add(libraryName), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<LibraryInfo>> functionList() {
/* 2872 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST), LibraryInfo.LIBRARY_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<LibraryInfo>> functionList(String libraryNamePattern) {
/* 2876 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST).add(Protocol.Keyword.LIBRARYNAME)
/* 2877 */         .add(libraryNamePattern), LibraryInfo.LIBRARY_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<LibraryInfo>> functionListWithCode() {
/* 2881 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST).add(Protocol.Keyword.WITHCODE), LibraryInfo.LIBRARY_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<LibraryInfo>> functionListWithCode(String libraryNamePattern) {
/* 2885 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST).add(Protocol.Keyword.LIBRARYNAME)
/* 2886 */         .add(libraryNamePattern).add(Protocol.Keyword.WITHCODE), LibraryInfo.LIBRARY_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionLoad(String functionCode) {
/* 2890 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LOAD).add(functionCode), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionLoadReplace(String functionCode) {
/* 2894 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LOAD).add(Protocol.Keyword.REPLACE).add(functionCode), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<FunctionStats> functionStats() {
/* 2898 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.STATS), FunctionStats.FUNCTION_STATS_BUILDER);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> functionStatsBinary() {
/* 2902 */     return new CommandObject(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.STATS), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionFlush() {
/* 2906 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.FLUSH), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionFlush(FlushMode mode) {
/* 2910 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.FLUSH).add(mode), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionKill() {
/* 2914 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.KILL), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> fcall(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 2918 */     return new CommandObject(commandArguments(Protocol.Command.FCALL).add(name).add(Integer.valueOf(keys.size()))
/* 2919 */         .keys(keys).addObjects(args), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> fcallReadonly(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 2923 */     return new CommandObject(commandArguments(Protocol.Command.FCALL_RO).add(name).add(Integer.valueOf(keys.size()))
/* 2924 */         .keys(keys).addObjects(args), BuilderFactory.RAW_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionDelete(byte[] libraryName) {
/* 2928 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.DELETE).add(libraryName), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> functionDump() {
/* 2932 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.DUMP), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> functionListBinary() {
/* 2936 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> functionList(byte[] libraryNamePattern) {
/* 2940 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST).add(Protocol.Keyword.LIBRARYNAME)
/* 2941 */         .add(libraryNamePattern), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> functionListWithCodeBinary() {
/* 2945 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST).add(Protocol.Keyword.WITHCODE), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> functionListWithCode(byte[] libraryNamePattern) {
/* 2949 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LIST).add(Protocol.Keyword.LIBRARYNAME)
/* 2950 */         .add(libraryNamePattern).add(Protocol.Keyword.WITHCODE), BuilderFactory.RAW_OBJECT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionLoad(byte[] functionCode) {
/* 2954 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LOAD).add(functionCode), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionLoadReplace(byte[] functionCode) {
/* 2958 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Keyword.LOAD).add(Protocol.Keyword.REPLACE).add(functionCode), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> functionRestore(byte[] serializedValue) {
/* 2962 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Command.RESTORE).add(serializedValue), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> functionRestore(byte[] serializedValue, FunctionRestorePolicy policy) {
/* 2967 */     return new CommandObject<>(commandArguments(Protocol.Command.FUNCTION).add(Protocol.Command.RESTORE).add(serializedValue)
/* 2968 */         .add(policy.getRaw()), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Boolean> copy(String srcKey, String dstKey, int dstDB, boolean replace) {
/* 2974 */     CommandArguments args = commandArguments(Protocol.Command.COPY).key(srcKey).key(dstKey).add(Protocol.Keyword.DB).add(Integer.valueOf(dstDB));
/* 2975 */     if (replace) args.add(Protocol.Keyword.REPLACE); 
/* 2976 */     return new CommandObject<>(args, BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> copy(byte[] srcKey, byte[] dstKey, int dstDB, boolean replace) {
/* 2980 */     CommandArguments args = commandArguments(Protocol.Command.COPY).key(srcKey).key(dstKey).add(Protocol.Keyword.DB).add(Integer.valueOf(dstDB));
/* 2981 */     if (replace) args.add(Protocol.Keyword.REPLACE); 
/* 2982 */     return new CommandObject<>(args, BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, String key, int timeout) {
/* 2986 */     return migrate(host, port, key, 0, timeout);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, String key, int destinationDB, int timeout) {
/* 2990 */     return new CommandObject<>(commandArguments(Protocol.Command.MIGRATE).add(host).add(Integer.valueOf(port)).key(key)
/* 2991 */         .add(Integer.valueOf(destinationDB)).add(Integer.valueOf(timeout)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, int timeout, MigrateParams params, String... keys) {
/* 2995 */     return migrate(host, port, 0, timeout, params, keys);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, int destinationDB, int timeout, MigrateParams params, String... keys) {
/* 3000 */     return new CommandObject<>(commandArguments(Protocol.Command.MIGRATE).add(host).add(Integer.valueOf(port)).add(new byte[0])
/* 3001 */         .add(Integer.valueOf(destinationDB)).add(Integer.valueOf(timeout)).addParams((IParams)params).add(Protocol.Keyword.KEYS).keys((Object[])keys), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, byte[] key, int timeout) {
/* 3006 */     return migrate(host, port, key, 0, timeout);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, byte[] key, int destinationDB, int timeout) {
/* 3010 */     return new CommandObject<>(commandArguments(Protocol.Command.MIGRATE).add(host).add(Integer.valueOf(port)).key(key)
/* 3011 */         .add(Integer.valueOf(destinationDB)).add(Integer.valueOf(timeout)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, int timeout, MigrateParams params, byte[]... keys) {
/* 3015 */     return migrate(host, port, 0, timeout, params, keys);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> migrate(String host, int port, int destinationDB, int timeout, MigrateParams params, byte[]... keys) {
/* 3020 */     return new CommandObject<>(commandArguments(Protocol.Command.MIGRATE).add(host).add(Integer.valueOf(port)).add(new byte[0])
/* 3021 */         .add(Integer.valueOf(destinationDB)).add(Integer.valueOf(timeout)).addParams((IParams)params).add(Protocol.Keyword.KEYS).keys((Object[])keys), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> memoryUsage(String key) {
/* 3026 */     return new CommandObject<>(commandArguments(Protocol.Command.MEMORY).add(Protocol.Keyword.USAGE).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> memoryUsage(String key, int samples) {
/* 3030 */     return new CommandObject<>(commandArguments(Protocol.Command.MEMORY).add(Protocol.Keyword.USAGE).key(key).add(Protocol.Keyword.SAMPLES).add(Integer.valueOf(samples)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> memoryUsage(byte[] key) {
/* 3034 */     return new CommandObject<>(commandArguments(Protocol.Command.MEMORY).add(Protocol.Keyword.USAGE).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> memoryUsage(byte[] key, int samples) {
/* 3038 */     return new CommandObject<>(commandArguments(Protocol.Command.MEMORY).add(Protocol.Keyword.USAGE).key(key).add(Protocol.Keyword.SAMPLES).add(Integer.valueOf(samples)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> objectRefcount(String key) {
/* 3042 */     return new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.REFCOUNT).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> objectEncoding(String key) {
/* 3046 */     return new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.ENCODING).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> objectIdletime(String key) {
/* 3050 */     return new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.IDLETIME).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> objectFreq(String key) {
/* 3054 */     return new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.FREQ).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> objectRefcount(byte[] key) {
/* 3058 */     return new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.REFCOUNT).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<byte[]> objectEncoding(byte[] key) {
/* 3062 */     return (CommandObject)new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.ENCODING).key(key), (Builder)BuilderFactory.BINARY);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> objectIdletime(byte[] key) {
/* 3066 */     return new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.IDLETIME).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> objectFreq(byte[] key) {
/* 3070 */     return new CommandObject<>(commandArguments(Protocol.Command.OBJECT).add(Protocol.Keyword.FREQ).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public CommandObject<Long> waitReplicas(int replicas, long timeout) {
/* 3074 */     return new CommandObject<>(commandArguments(Protocol.Command.WAIT).add(Integer.valueOf(replicas)).add(Long.valueOf(timeout)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> waitReplicas(String sampleKey, int replicas, long timeout) {
/* 3078 */     return new CommandObject<>(commandArguments(Protocol.Command.WAIT).add(Integer.valueOf(replicas)).add(Long.valueOf(timeout)).processKey(sampleKey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> waitReplicas(byte[] sampleKey, int replicas, long timeout) {
/* 3082 */     return new CommandObject<>(commandArguments(Protocol.Command.WAIT).add(Integer.valueOf(replicas)).add(Long.valueOf(timeout)).processKey(sampleKey), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public CommandObject<KeyValue<Long, Long>> waitAOF(long numLocal, long numReplicas, long timeout) {
/* 3086 */     return new CommandObject<>(commandArguments(Protocol.Command.WAITAOF).add(Long.valueOf(numLocal)).add(Long.valueOf(numReplicas)).add(Long.valueOf(timeout)), BuilderFactory.LONG_LONG_PAIR);
/*      */   }
/*      */   
/*      */   public CommandObject<KeyValue<Long, Long>> waitAOF(byte[] sampleKey, long numLocal, long numReplicas, long timeout) {
/* 3090 */     return new CommandObject<>(commandArguments(Protocol.Command.WAITAOF).add(Long.valueOf(numLocal)).add(Long.valueOf(numReplicas)).add(Long.valueOf(timeout)).processKey(sampleKey), BuilderFactory.LONG_LONG_PAIR);
/*      */   }
/*      */   
/*      */   public CommandObject<KeyValue<Long, Long>> waitAOF(String sampleKey, long numLocal, long numReplicas, long timeout) {
/* 3094 */     return new CommandObject<>(commandArguments(Protocol.Command.WAITAOF).add(Long.valueOf(numLocal)).add(Long.valueOf(numReplicas)).add(Long.valueOf(timeout)).processKey(sampleKey), BuilderFactory.LONG_LONG_PAIR);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> publish(String channel, String message) {
/* 3098 */     return new CommandObject<>(commandArguments(Protocol.Command.PUBLISH).add(channel).add(message), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> publish(byte[] channel, byte[] message) {
/* 3102 */     return new CommandObject<>(commandArguments(Protocol.Command.PUBLISH).add(channel).add(message), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> spublish(String channel, String message) {
/* 3106 */     return new CommandObject<>(commandArguments(Protocol.Command.SPUBLISH).key(channel).add(message), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> spublish(byte[] channel, byte[] message) {
/* 3110 */     return new CommandObject<>(commandArguments(Protocol.Command.SPUBLISH).key(channel).add(message), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> hsetObject(String key, String field, Object value) {
/* 3116 */     return new CommandObject<>(commandArguments(Protocol.Command.HSET).key(key).add(field).add(value), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> hsetObject(String key, Map<String, Object> hash) {
/* 3120 */     return new CommandObject<>(addFlatMapArgs(commandArguments(Protocol.Command.HSET).key(key), hash), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   private boolean isRoundRobinSearchCommand() {
/* 3124 */     if (this.broadcastAndRoundRobinConfig == null)
/* 3125 */       return true; 
/* 3126 */     if (this.broadcastAndRoundRobinConfig.getRediSearchModeInCluster() == JedisBroadcastAndRoundRobinConfig.RediSearchMode.LIGHT) {
/* 3127 */       return false;
/*      */     }
/* 3129 */     return true;
/*      */   }
/*      */   
/*      */   private CommandArguments checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand sc, String idx) {
/* 3133 */     CommandArguments ca = commandArguments((ProtocolCommand)sc);
/* 3134 */     if (isRoundRobinSearchCommand()) {
/* 3135 */       ca.add(idx);
/*      */     } else {
/* 3137 */       ca.key(idx);
/*      */     } 
/* 3139 */     return ca;
/*      */   }
/*      */   
/*      */   private CommandArguments checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand sc, String idx1, String idx2) {
/* 3143 */     CommandArguments ca = commandArguments((ProtocolCommand)sc);
/* 3144 */     if (isRoundRobinSearchCommand()) {
/* 3145 */       ca.add(idx1).add(idx2);
/*      */     } else {
/* 3147 */       ca.key(idx1).key(idx2);
/*      */     } 
/* 3149 */     return ca;
/*      */   }
/*      */   
/*      */   private CommandArguments checkAndRoundRobinSearchCommand(CommandArguments commandArguments, byte[] indexName) {
/* 3153 */     return isRoundRobinSearchCommand() ? commandArguments.add(indexName) : commandArguments.key(indexName);
/*      */   }
/*      */   
/*      */   private <T> CommandObject<T> directSearchCommand(CommandObject<T> object, String indexName) {
/* 3157 */     object.getArguments().processKey(indexName);
/* 3158 */     return object;
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> ftCreate(String indexName, IndexOptions indexOptions, Schema schema) {
/* 3163 */     CommandArguments args = checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.CREATE, indexName).addParams((IParams)indexOptions).add(SearchProtocol.SearchKeyword.SCHEMA);
/* 3164 */     schema.fields.forEach(field -> args.addParams((IParams)field));
/* 3165 */     return new CommandObject<>(args, BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<String> ftCreate(String indexName, FTCreateParams createParams, Iterable<SchemaField> schemaFields) {
/* 3171 */     CommandArguments args = checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.CREATE, indexName).addParams((IParams)createParams).add(SearchProtocol.SearchKeyword.SCHEMA);
/* 3172 */     schemaFields.forEach(field -> args.addParams((IParams)field));
/* 3173 */     return new CommandObject<>(args, BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> ftAlter(String indexName, Schema schema) {
/* 3178 */     CommandArguments args = checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.ALTER, indexName).add(SearchProtocol.SearchKeyword.SCHEMA).add(SearchProtocol.SearchKeyword.ADD);
/* 3179 */     schema.fields.forEach(field -> args.addParams((IParams)field));
/* 3180 */     return new CommandObject<>(args, BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> ftAlter(String indexName, Iterable<SchemaField> schemaFields) {
/* 3185 */     CommandArguments args = checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.ALTER, indexName).add(SearchProtocol.SearchKeyword.SCHEMA).add(SearchProtocol.SearchKeyword.ADD);
/* 3186 */     schemaFields.forEach(field -> args.addParams((IParams)field));
/* 3187 */     return new CommandObject<>(args, BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftAliasAdd(String aliasName, String indexName) {
/* 3191 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.ALIASADD, aliasName, indexName), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftAliasUpdate(String aliasName, String indexName) {
/* 3195 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.ALIASUPDATE, aliasName, indexName), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftAliasDel(String aliasName) {
/* 3199 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.ALIASDEL, aliasName), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftDropIndex(String indexName) {
/* 3203 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.DROPINDEX, indexName), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftDropIndexDD(String indexName) {
/* 3207 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.DROPINDEX, indexName).add(SearchProtocol.SearchKeyword.DD), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<SearchResult> ftSearch(String indexName, String query) {
/* 3212 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.SEARCH, indexName).add(query), 
/* 3213 */         getSearchResultBuilder(() -> new SearchResult.SearchResultBuilder(true, false, true)));
/*      */   }
/*      */   
/*      */   public final CommandObject<SearchResult> ftSearch(String indexName, String query, FTSearchParams params) {
/* 3217 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.SEARCH, indexName)
/* 3218 */         .add(query).addParams((IParams)params.dialectOptional(this.searchDialect.get())), 
/* 3219 */         getSearchResultBuilder(() -> new SearchResult.SearchResultBuilder(!params.getNoContent(), params.getWithScores(), true)));
/*      */   }
/*      */   
/*      */   public final CommandObject<SearchResult> ftSearch(String indexName, Query query) {
/* 3223 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.SEARCH, indexName)
/* 3224 */         .addParams((IParams)query.dialectOptional(this.searchDialect.get())), getSearchResultBuilder(() -> new SearchResult.SearchResultBuilder(!query.getNoContent(), query.getWithScores(), true)));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<SearchResult> ftSearch(byte[] indexName, Query query) {
/* 3230 */     if (this.protocol == RedisProtocol.RESP3) {
/* 3231 */       throw new UnsupportedOperationException("binary ft.search is not implemented with resp3.");
/*      */     }
/* 3233 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SEARCH), indexName)
/* 3234 */         .addParams((IParams)query.dialectOptional(this.searchDialect.get())), getSearchResultBuilder(() -> new SearchResult.SearchResultBuilder(!query.getNoContent(), query.getWithScores(), false)));
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> ftExplain(String indexName, Query query) {
/* 3239 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.EXPLAIN, indexName)
/* 3240 */         .addParams((IParams)query.dialectOptional(this.searchDialect.get())), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> ftExplainCLI(String indexName, Query query) {
/* 3244 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.EXPLAINCLI, indexName)
/* 3245 */         .addParams((IParams)query.dialectOptional(this.searchDialect.get())), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<AggregationResult> ftAggregate(String indexName, AggregationBuilder aggr) {
/* 3249 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.AGGREGATE, indexName)
/* 3250 */         .addParams((IParams)aggr.dialectOptional(this.searchDialect.get())), !aggr.isWithCursor() ? AggregationResult.SEARCH_AGGREGATION_RESULT : AggregationResult.SEARCH_AGGREGATION_RESULT_WITH_CURSOR);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<AggregationResult> ftCursorRead(String indexName, long cursorId, int count) {
/* 3255 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.CURSOR).add(SearchProtocol.SearchKeyword.READ)
/* 3256 */         .key(indexName).add(Long.valueOf(cursorId)).add(SearchProtocol.SearchKeyword.COUNT).add(Integer.valueOf(count)), AggregationResult.SEARCH_AGGREGATION_RESULT_WITH_CURSOR);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> ftCursorDel(String indexName, long cursorId) {
/* 3261 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.CURSOR).add(SearchProtocol.SearchKeyword.DEL)
/* 3262 */         .key(indexName).add(Long.valueOf(cursorId)), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Map.Entry<AggregationResult, Map<String, Object>>> ftProfileAggregate(String indexName, FTProfileParams profileParams, AggregationBuilder aggr) {
/* 3267 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.PROFILE, indexName)
/* 3268 */         .add(SearchProtocol.SearchKeyword.AGGREGATE).addParams((IParams)profileParams).add(SearchProtocol.SearchKeyword.QUERY)
/* 3269 */         .addParams((IParams)aggr.dialectOptional(this.searchDialect.get())), new SearchProfileResponseBuilder<>(
/* 3270 */           !aggr.isWithCursor() ? AggregationResult.SEARCH_AGGREGATION_RESULT : AggregationResult.SEARCH_AGGREGATION_RESULT_WITH_CURSOR));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Map.Entry<SearchResult, Map<String, Object>>> ftProfileSearch(String indexName, FTProfileParams profileParams, Query query) {
/* 3276 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.PROFILE, indexName)
/* 3277 */         .add(SearchProtocol.SearchKeyword.SEARCH).addParams((IParams)profileParams).add(SearchProtocol.SearchKeyword.QUERY)
/* 3278 */         .addParams((IParams)query.dialectOptional(this.searchDialect.get())), new SearchProfileResponseBuilder<>(
/* 3279 */           getSearchResultBuilder(() -> new SearchResult.SearchResultBuilder(!query.getNoContent(), query.getWithScores(), true))));
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Map.Entry<SearchResult, Map<String, Object>>> ftProfileSearch(String indexName, FTProfileParams profileParams, String query, FTSearchParams searchParams) {
/* 3284 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.PROFILE, indexName)
/* 3285 */         .add(SearchProtocol.SearchKeyword.SEARCH).addParams((IParams)profileParams).add(SearchProtocol.SearchKeyword.QUERY).add(query)
/* 3286 */         .addParams((IParams)searchParams.dialectOptional(this.searchDialect.get())), new SearchProfileResponseBuilder<>(
/* 3287 */           getSearchResultBuilder(() -> new SearchResult.SearchResultBuilder(!searchParams.getNoContent(), searchParams.getWithScores(), true))));
/*      */   }
/*      */   
/*      */   private Builder<SearchResult> getSearchResultBuilder(Supplier<Builder<SearchResult>> resp2) {
/* 3291 */     if (this.protocol == RedisProtocol.RESP3) return SearchResult.SEARCH_RESULT_BUILDER; 
/* 3292 */     return resp2.get();
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftSynUpdate(String indexName, String synonymGroupId, String... terms) {
/* 3296 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.SYNUPDATE, indexName)
/* 3297 */         .add(synonymGroupId).addObjects((Object[])terms), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, List<String>>> ftSynDump(String indexName) {
/* 3301 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.SYNDUMP, indexName), SearchBuilderFactory.SEARCH_SYNONYM_GROUPS);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> ftDictAdd(String dictionary, String... terms) {
/* 3306 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.DICTADD).add(dictionary).addObjects((Object[])terms), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Long> ftDictDel(String dictionary, String... terms) {
/* 3311 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.DICTDEL).add(dictionary).addObjects((Object[])terms), BuilderFactory.LONG);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Set<String>> ftDictDump(String dictionary) {
/* 3316 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.DICTDUMP).add(dictionary), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> ftDictAddBySampleKey(String indexName, String dictionary, String... terms) {
/* 3320 */     return directSearchCommand(ftDictAdd(dictionary, terms), indexName);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> ftDictDelBySampleKey(String indexName, String dictionary, String... terms) {
/* 3324 */     return directSearchCommand(ftDictDel(dictionary, terms), indexName);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> ftDictDumpBySampleKey(String indexName, String dictionary) {
/* 3328 */     return directSearchCommand(ftDictDump(dictionary), indexName);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Map<String, Double>>> ftSpellCheck(String index, String query) {
/* 3332 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.SPELLCHECK, index).add(query), SearchBuilderFactory.SEARCH_SPELLCHECK_RESPONSE);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<Map<String, Map<String, Double>>> ftSpellCheck(String index, String query, FTSpellCheckParams spellCheckParams) {
/* 3338 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.SPELLCHECK, index).add(query)
/* 3339 */         .addParams((IParams)spellCheckParams.dialectOptional(this.searchDialect.get())), SearchBuilderFactory.SEARCH_SPELLCHECK_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Object>> ftInfo(String indexName) {
/* 3343 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.INFO, indexName), (this.protocol == RedisProtocol.RESP3) ? BuilderFactory.AGGRESSIVE_ENCODED_OBJECT_MAP : BuilderFactory.ENCODED_OBJECT_MAP);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Set<String>> ftTagVals(String indexName, String fieldName) {
/* 3348 */     return new CommandObject<>(checkAndRoundRobinSearchCommand(SearchProtocol.SearchCommand.TAGVALS, indexName)
/* 3349 */         .add(fieldName), BuilderFactory.STRING_SET);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Object>> ftConfigGet(String option) {
/* 3353 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.CONFIG).add(SearchProtocol.SearchKeyword.GET).add(option), (this.protocol == RedisProtocol.RESP3) ? BuilderFactory.AGGRESSIVE_ENCODED_OBJECT_MAP : BuilderFactory.ENCODED_OBJECT_MAP_FROM_PAIRS);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Map<String, Object>> ftConfigGet(String indexName, String option) {
/* 3358 */     return directSearchCommand(ftConfigGet(option), indexName);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftConfigSet(String option, String value) {
/* 3362 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.CONFIG).add(SearchProtocol.SearchKeyword.SET).add(option).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> ftConfigSet(String indexName, String option, String value) {
/* 3366 */     return directSearchCommand(ftConfigSet(option, value), indexName);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> ftSugAdd(String key, String string, double score) {
/* 3370 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGADD).key(key).add(string).add(Double.valueOf(score)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> ftSugAddIncr(String key, String string, double score) {
/* 3374 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGADD).key(key).add(string).add(Double.valueOf(score)).add(SearchProtocol.SearchKeyword.INCR), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> ftSugGet(String key, String prefix) {
/* 3378 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGGET).key(key).add(prefix), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> ftSugGet(String key, String prefix, boolean fuzzy, int max) {
/* 3382 */     CommandArguments args = commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGGET).key(key).add(prefix);
/* 3383 */     if (fuzzy) args.add(SearchProtocol.SearchKeyword.FUZZY); 
/* 3384 */     args.add(SearchProtocol.SearchKeyword.MAX).add(Integer.valueOf(max));
/* 3385 */     return new CommandObject<>(args, BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> ftSugGetWithScores(String key, String prefix) {
/* 3389 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGGET).key(key).add(prefix)
/* 3390 */         .add(SearchProtocol.SearchKeyword.WITHSCORES), BuilderFactory.TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Tuple>> ftSugGetWithScores(String key, String prefix, boolean fuzzy, int max) {
/* 3394 */     CommandArguments args = commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGGET).key(key).add(prefix);
/* 3395 */     if (fuzzy) args.add(SearchProtocol.SearchKeyword.FUZZY); 
/* 3396 */     args.add(SearchProtocol.SearchKeyword.MAX).add(Integer.valueOf(max));
/* 3397 */     args.add(SearchProtocol.SearchKeyword.WITHSCORES);
/* 3398 */     return new CommandObject<>(args, BuilderFactory.TUPLE_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> ftSugDel(String key, String string) {
/* 3402 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGDEL).key(key).add(string), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> ftSugLen(String key) {
/* 3406 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand.SUGLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Set<String>> ftList() {
/* 3410 */     return new CommandObject<>(commandArguments((ProtocolCommand)SearchProtocol.SearchCommand._LIST), BuilderFactory.STRING_SET);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<String> jsonSet(String key, Path2 path, Object object) {
/* 3416 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.SET).key(key).add(path).add(object), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> jsonSetWithEscape(String key, Path2 path, Object object) {
/* 3420 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.SET).key(key).add(path).add(
/* 3421 */           getJsonObjectMapper().toJson(object)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<String> jsonSet(String key, Path path, Object pojo) {
/* 3426 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.SET).key(key).add(path).add(
/* 3427 */           getJsonObjectMapper().toJson(pojo)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<String> jsonSetWithPlainString(String key, Path path, String string) {
/* 3432 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.SET).key(key).add(path).add(string), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> jsonSet(String key, Path2 path, Object object, JsonSetParams params) {
/* 3436 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.SET).key(key).add(path).add(object).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> jsonSetWithEscape(String key, Path2 path, Object object, JsonSetParams params) {
/* 3440 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.SET).key(key).add(path).add(
/* 3441 */           getJsonObjectMapper().toJson(object)).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<String> jsonSet(String key, Path path, Object pojo, JsonSetParams params) {
/* 3446 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.SET).key(key).add(path).add(
/* 3447 */           getJsonObjectMapper().toJson(pojo)).addParams((IParams)params), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> jsonMerge(String key, Path2 path, Object object) {
/* 3451 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.MERGE).key(key).add(path).add(object), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<String> jsonMerge(String key, Path path, Object pojo) {
/* 3456 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.MERGE).key(key).add(path).add(
/* 3457 */           getJsonObjectMapper().toJson(pojo)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> jsonGet(String key) {
/* 3461 */     return new CommandObject(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.GET).key(key), (this.protocol != RedisProtocol.RESP3) ? this.JSON_GENERIC_OBJECT : JsonBuilderFactory.JSON_OBJECT);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final <T> CommandObject<T> jsonGet(String key, Class<T> clazz) {
/* 3467 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.GET).key(key), new JsonObjectBuilder<>(clazz));
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> jsonGet(String key, Path2... paths) {
/* 3471 */     return new CommandObject(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.GET).key(key).addObjects((Object[])paths), JsonBuilderFactory.JSON_OBJECT);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Object> jsonGet(String key, Path... paths) {
/* 3476 */     return new CommandObject(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.GET).key(key).addObjects((Object[])paths), this.JSON_GENERIC_OBJECT);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<String> jsonGetAsPlainString(String key, Path path) {
/* 3481 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.GET).key(key).add(path), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final <T> CommandObject<T> jsonGet(String key, Class<T> clazz, Path... paths) {
/* 3486 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.GET).key(key).addObjects((Object[])paths), new JsonObjectBuilder<>(clazz));
/*      */   }
/*      */   
/*      */   public final CommandObject<List<JSONArray>> jsonMGet(Path2 path, String... keys) {
/* 3490 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.MGET).keys((Object[])keys).add(path), JsonBuilderFactory.JSON_ARRAY_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final <T> CommandObject<List<T>> jsonMGet(Path path, Class<T> clazz, String... keys) {
/* 3495 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.MGET).keys((Object[])keys).add(path), new JsonObjectListBuilder<>(clazz));
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> jsonDel(String key) {
/* 3499 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.DEL).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> jsonDel(String key, Path2 path) {
/* 3503 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.DEL).key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonDel(String key, Path path) {
/* 3508 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.DEL).key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> jsonClear(String key) {
/* 3512 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.CLEAR).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> jsonClear(String key, Path2 path) {
/* 3516 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.CLEAR).key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonClear(String key, Path path) {
/* 3521 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.CLEAR).key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> jsonToggle(String key, Path2 path) {
/* 3525 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.TOGGLE).key(key).add(path), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<String> jsonToggle(String key, Path path) {
/* 3530 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.TOGGLE).key(key).add(path), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Class<?>> jsonType(String key) {
/* 3535 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.TYPE).key(key), JsonBuilderFactory.JSON_TYPE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Class<?>>> jsonType(String key, Path2 path) {
/* 3539 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.TYPE).key(key).add(path), (this.protocol != RedisProtocol.RESP3) ? JsonBuilderFactory.JSON_TYPE_LIST : JsonBuilderFactory.JSON_TYPE_RESPONSE_RESP3_COMPATIBLE);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Class<?>> jsonType(String key, Path path) {
/* 3545 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.TYPE).key(key).add(path), JsonBuilderFactory.JSON_TYPE);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonStrAppend(String key, Object string) {
/* 3550 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.STRAPPEND).key(key).add(
/* 3551 */           getJsonObjectMapper().toJson(string)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonStrAppend(String key, Path2 path, Object string) {
/* 3555 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.STRAPPEND).key(key).add(path).add(
/* 3556 */           getJsonObjectMapper().toJson(string)), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonStrAppend(String key, Path path, Object string) {
/* 3561 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.STRAPPEND).key(key).add(path).add(
/* 3562 */           getJsonObjectMapper().toJson(string)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonStrLen(String key) {
/* 3567 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.STRLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonStrLen(String key, Path2 path) {
/* 3571 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.STRLEN).key(key).add(path), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonStrLen(String key, Path path) {
/* 3576 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.STRLEN).key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> jsonNumIncrBy(String key, Path2 path, double value) {
/* 3580 */     return new CommandObject(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.NUMINCRBY).key(key).add(path).add(Double.valueOf(value)), JsonBuilderFactory.JSON_ARRAY_OR_DOUBLE_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Double> jsonNumIncrBy(String key, Path path, double value) {
/* 3586 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.NUMINCRBY).key(key).add(path).add(Double.valueOf(value)), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> jsonArrAppend(String key, String path, JSONObject... objects) {
/* 3590 */     CommandArguments args = commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRAPPEND).key(key).add(path);
/* 3591 */     for (JSONObject object : objects) {
/* 3592 */       args.add(object);
/*      */     }
/* 3594 */     return new CommandObject<>(args, BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrAppend(String key, Path2 path, Object... objects) {
/* 3598 */     CommandArguments args = commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRAPPEND).key(key).add(path).addObjects(objects);
/* 3599 */     return new CommandObject<>(args, BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrAppendWithEscape(String key, Path2 path, Object... objects) {
/* 3603 */     CommandArguments args = commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRAPPEND).key(key).add(path);
/* 3604 */     for (Object object : objects) {
/* 3605 */       args.add(getJsonObjectMapper().toJson(object));
/*      */     }
/* 3607 */     return new CommandObject<>(args, BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonArrAppend(String key, Path path, Object... pojos) {
/* 3612 */     CommandArguments args = commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRAPPEND).key(key).add(path);
/* 3613 */     for (Object pojo : pojos) {
/* 3614 */       args.add(getJsonObjectMapper().toJson(pojo));
/*      */     }
/* 3616 */     return new CommandObject<>(args, BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrIndex(String key, Path2 path, Object scalar) {
/* 3620 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRINDEX).key(key).add(path).add(scalar), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrIndexWithEscape(String key, Path2 path, Object scalar) {
/* 3624 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRINDEX).key(key).add(path).add(
/* 3625 */           getJsonObjectMapper().toJson(scalar)), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonArrIndex(String key, Path path, Object scalar) {
/* 3630 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRINDEX).key(key).add(path).add(
/* 3631 */           getJsonObjectMapper().toJson(scalar)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrInsert(String key, Path2 path, int index, Object... objects) {
/* 3635 */     CommandArguments args = commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRINSERT).key(key).add(path).add(Integer.valueOf(index)).addObjects(objects);
/* 3636 */     return new CommandObject<>(args, BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrInsertWithEscape(String key, Path2 path, int index, Object... objects) {
/* 3640 */     CommandArguments args = commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRINSERT).key(key).add(path).add(Integer.valueOf(index));
/* 3641 */     for (Object object : objects) {
/* 3642 */       args.add(getJsonObjectMapper().toJson(object));
/*      */     }
/* 3644 */     return new CommandObject<>(args, BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonArrInsert(String key, Path path, int index, Object... pojos) {
/* 3649 */     CommandArguments args = commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRINSERT).key(key).add(path).add(Integer.valueOf(index));
/* 3650 */     for (Object pojo : pojos) {
/* 3651 */       args.add(getJsonObjectMapper().toJson(pojo));
/*      */     }
/* 3653 */     return new CommandObject<>(args, BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Object> jsonArrPop(String key) {
/* 3658 */     return new CommandObject(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key), new JsonObjectBuilder(Object.class));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final <T> CommandObject<T> jsonArrPop(String key, Class<T> clazz) {
/* 3663 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key), new JsonObjectBuilder<>(clazz));
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> jsonArrPop(String key, Path2 path) {
/* 3667 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key).add(path), new JsonObjectListBuilder(Object.class));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Object> jsonArrPop(String key, Path path) {
/* 3672 */     return new CommandObject(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key).add(path), new JsonObjectBuilder(Object.class));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final <T> CommandObject<T> jsonArrPop(String key, Class<T> clazz, Path path) {
/* 3677 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key).add(path), new JsonObjectBuilder<>(clazz));
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Object>> jsonArrPop(String key, Path2 path, int index) {
/* 3681 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key).add(path).add(Integer.valueOf(index)), new JsonObjectListBuilder(Object.class));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Object> jsonArrPop(String key, Path path, int index) {
/* 3686 */     return new CommandObject(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key).add(path).add(Integer.valueOf(index)), new JsonObjectBuilder(Object.class));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final <T> CommandObject<T> jsonArrPop(String key, Class<T> clazz, Path path, int index) {
/* 3691 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRPOP).key(key).add(path).add(Integer.valueOf(index)), new JsonObjectBuilder<>(clazz));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonArrLen(String key) {
/* 3696 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrLen(String key, Path2 path) {
/* 3700 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRLEN).key(key).add(path), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonArrLen(String key, Path path) {
/* 3705 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRLEN).key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonArrTrim(String key, Path2 path, int start, int stop) {
/* 3709 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRTRIM).key(key).add(path).add(Integer.valueOf(start)).add(Integer.valueOf(stop)), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonArrTrim(String key, Path path, int start, int stop) {
/* 3714 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.ARRTRIM).key(key).add(path).add(Integer.valueOf(start)).add(Integer.valueOf(stop)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonObjLen(String key) {
/* 3719 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.OBJLEN).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonObjLen(String key, Path path) {
/* 3724 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.OBJLEN).key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonObjLen(String key, Path2 path) {
/* 3728 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.OBJLEN).key(key).add(path), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<List<String>> jsonObjKeys(String key) {
/* 3733 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.OBJKEYS).key(key), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<List<String>> jsonObjKeys(String key, Path path) {
/* 3738 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.OBJKEYS).key(key).add(path), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<List<String>>> jsonObjKeys(String key, Path2 path) {
/* 3742 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.OBJKEYS).key(key).add(path), BuilderFactory.STRING_LIST_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonDebugMemory(String key) {
/* 3747 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.DEBUG).add("MEMORY").key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Long> jsonDebugMemory(String key, Path path) {
/* 3752 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.DEBUG).add("MEMORY").key(key).add(path), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> jsonDebugMemory(String key, Path2 path) {
/* 3756 */     return new CommandObject<>(commandArguments((ProtocolCommand)JsonProtocol.JsonCommand.DEBUG).add("MEMORY").key(key).add(path), BuilderFactory.LONG_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<String> tsCreate(String key) {
/* 3762 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.CREATE).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tsCreate(String key, TSCreateParams createParams) {
/* 3766 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.CREATE).key(key).addParams((IParams)createParams), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsDel(String key, long fromTimestamp, long toTimestamp) {
/* 3770 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.DEL).key(key)
/* 3771 */         .add(Long.valueOf(fromTimestamp)).add(Long.valueOf(toTimestamp)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tsAlter(String key, TSAlterParams alterParams) {
/* 3775 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.ALTER).key(key).addParams((IParams)alterParams), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsAdd(String key, double value) {
/* 3779 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.ADD).key(key).add(Protocol.BYTES_ASTERISK).add(Double.valueOf(value)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsAdd(String key, long timestamp, double value) {
/* 3783 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.ADD).key(key).add(Long.valueOf(timestamp)).add(Double.valueOf(value)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsAdd(String key, long timestamp, double value, TSCreateParams createParams) {
/* 3787 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.ADD).key(key)
/* 3788 */         .add(Long.valueOf(timestamp)).add(Double.valueOf(value)).addParams((IParams)createParams), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> tsMAdd(Map.Entry<String, TSElement>... entries) {
/* 3792 */     CommandArguments args = commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.MADD);
/* 3793 */     for (Map.Entry<String, TSElement> entry : entries) {
/* 3794 */       args.key(entry.getKey()).add(Long.valueOf(((TSElement)entry.getValue()).getTimestamp())).add(Double.valueOf(((TSElement)entry.getValue()).getValue()));
/*      */     }
/* 3796 */     return new CommandObject<>(args, BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsIncrBy(String key, double value) {
/* 3800 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.INCRBY).key(key).add(Double.valueOf(value)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsIncrBy(String key, double value, long timestamp) {
/* 3804 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.INCRBY).key(key).add(Double.valueOf(value))
/* 3805 */         .add(TimeSeriesProtocol.TimeSeriesKeyword.TIMESTAMP).add(Long.valueOf(timestamp)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsDecrBy(String key, double value) {
/* 3809 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.DECRBY).key(key).add(Double.valueOf(value)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> tsDecrBy(String key, double value, long timestamp) {
/* 3813 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.DECRBY).key(key).add(Double.valueOf(value))
/* 3814 */         .add(TimeSeriesProtocol.TimeSeriesKeyword.TIMESTAMP).add(Long.valueOf(timestamp)), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<TSElement>> tsRange(String key, long fromTimestamp, long toTimestamp) {
/* 3818 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.RANGE).key(key)
/* 3819 */         .add(Long.valueOf(fromTimestamp)).add(Long.valueOf(toTimestamp)), TimeSeriesBuilderFactory.TIMESERIES_ELEMENT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<TSElement>> tsRange(String key, TSRangeParams rangeParams) {
/* 3823 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.RANGE).key(key)
/* 3824 */         .addParams((IParams)rangeParams), TimeSeriesBuilderFactory.TIMESERIES_ELEMENT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<TSElement>> tsRevRange(String key, long fromTimestamp, long toTimestamp) {
/* 3828 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.REVRANGE).key(key)
/* 3829 */         .add(Long.valueOf(fromTimestamp)).add(Long.valueOf(toTimestamp)), TimeSeriesBuilderFactory.TIMESERIES_ELEMENT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<TSElement>> tsRevRange(String key, TSRangeParams rangeParams) {
/* 3833 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.REVRANGE).key(key)
/* 3834 */         .addParams((IParams)rangeParams), TimeSeriesBuilderFactory.TIMESERIES_ELEMENT_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, TSMRangeElements>> tsMRange(long fromTimestamp, long toTimestamp, String... filters) {
/* 3838 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.MRANGE).add(Long.valueOf(fromTimestamp))
/* 3839 */         .add(Long.valueOf(toTimestamp)).add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER).addObjects((Object[])filters), 
/* 3840 */         getTimeseriesMultiRangeResponseBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, TSMRangeElements>> tsMRange(TSMRangeParams multiRangeParams) {
/* 3844 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.MRANGE)
/* 3845 */         .addParams((IParams)multiRangeParams), getTimeseriesMultiRangeResponseBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, TSMRangeElements>> tsMRevRange(long fromTimestamp, long toTimestamp, String... filters) {
/* 3849 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.MREVRANGE).add(Long.valueOf(fromTimestamp))
/* 3850 */         .add(Long.valueOf(toTimestamp)).add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER).addObjects((Object[])filters), 
/* 3851 */         getTimeseriesMultiRangeResponseBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, TSMRangeElements>> tsMRevRange(TSMRangeParams multiRangeParams) {
/* 3855 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.MREVRANGE).addParams((IParams)multiRangeParams), 
/* 3856 */         getTimeseriesMultiRangeResponseBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<TSElement> tsGet(String key) {
/* 3860 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.GET).key(key), TimeSeriesBuilderFactory.TIMESERIES_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<TSElement> tsGet(String key, TSGetParams getParams) {
/* 3864 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.GET).key(key).addParams((IParams)getParams), TimeSeriesBuilderFactory.TIMESERIES_ELEMENT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, TSMGetElement>> tsMGet(TSMGetParams multiGetParams, String... filters) {
/* 3868 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.MGET).addParams((IParams)multiGetParams)
/* 3869 */         .add(TimeSeriesProtocol.TimeSeriesKeyword.FILTER).addObjects((Object[])filters), (this.protocol == RedisProtocol.RESP3) ? TimeSeriesBuilderFactory.TIMESERIES_MGET_RESPONSE_RESP3 : TimeSeriesBuilderFactory.TIMESERIES_MGET_RESPONSE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<String> tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType, long timeBucket) {
/* 3876 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.CREATERULE).key(sourceKey).key(destKey)
/* 3877 */         .add(TimeSeriesProtocol.TimeSeriesKeyword.AGGREGATION).add(aggregationType).add(Long.valueOf(timeBucket)), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType, long bucketDuration, long alignTimestamp) {
/* 3882 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.CREATERULE).key(sourceKey).key(destKey)
/* 3883 */         .add(TimeSeriesProtocol.TimeSeriesKeyword.AGGREGATION).add(aggregationType).add(Long.valueOf(bucketDuration)).add(Long.valueOf(alignTimestamp)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tsDeleteRule(String sourceKey, String destKey) {
/* 3887 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.DELETERULE).key(sourceKey).key(destKey), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> tsQueryIndex(String... filters) {
/* 3891 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.QUERYINDEX)
/* 3892 */         .addObjects((Object[])filters), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<TSInfo> tsInfo(String key) {
/* 3896 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.INFO).key(key), getTimeseriesInfoBuilder());
/*      */   }
/*      */   
/*      */   public final CommandObject<TSInfo> tsInfoDebug(String key) {
/* 3900 */     return new CommandObject<>(commandArguments((ProtocolCommand)TimeSeriesProtocol.TimeSeriesCommand.INFO).key(key).add(TimeSeriesProtocol.TimeSeriesKeyword.DEBUG), 
/* 3901 */         getTimeseriesInfoBuilder());
/*      */   }
/*      */   
/*      */   private Builder<Map<String, TSMRangeElements>> getTimeseriesMultiRangeResponseBuilder() {
/* 3905 */     return (this.protocol == RedisProtocol.RESP3) ? TimeSeriesBuilderFactory.TIMESERIES_MRANGE_RESPONSE_RESP3 : TimeSeriesBuilderFactory.TIMESERIES_MRANGE_RESPONSE;
/*      */   }
/*      */ 
/*      */   
/*      */   private Builder<TSInfo> getTimeseriesInfoBuilder() {
/* 3910 */     return (this.protocol == RedisProtocol.RESP3) ? TSInfo.TIMESERIES_INFO_RESP3 : TSInfo.TIMESERIES_INFO;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<String> bfReserve(String key, double errorRate, long capacity) {
/* 3916 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.RESERVE).key(key)
/* 3917 */         .add(Double.valueOf(errorRate)).add(Long.valueOf(capacity)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> bfReserve(String key, double errorRate, long capacity, BFReserveParams reserveParams) {
/* 3921 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.RESERVE).key(key)
/* 3922 */         .add(Double.valueOf(errorRate)).add(Long.valueOf(capacity)).addParams((IParams)reserveParams), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> bfAdd(String key, String item) {
/* 3926 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.ADD).key(key).add(item), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> bfMAdd(String key, String... items) {
/* 3930 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.MADD).key(key)
/* 3931 */         .addObjects((Object[])items), BuilderFactory.BOOLEAN_WITH_ERROR_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> bfInsert(String key, String... items) {
/* 3935 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.INSERT).key(key)
/* 3936 */         .add(RedisBloomProtocol.RedisBloomKeyword.ITEMS).addObjects((Object[])items), BuilderFactory.BOOLEAN_WITH_ERROR_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> bfInsert(String key, BFInsertParams insertParams, String... items) {
/* 3940 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.INSERT).key(key).addParams((IParams)insertParams)
/* 3941 */         .add(RedisBloomProtocol.RedisBloomKeyword.ITEMS).addObjects((Object[])items), BuilderFactory.BOOLEAN_WITH_ERROR_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> bfExists(String key, String item) {
/* 3945 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.EXISTS).key(key).add(item), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> bfMExists(String key, String... items) {
/* 3949 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.MEXISTS).key(key)
/* 3950 */         .addObjects((Object[])items), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map.Entry<Long, byte[]>> bfScanDump(String key, long iterator) {
/* 3954 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.SCANDUMP).key(key).add(Long.valueOf(iterator)), BLOOM_SCANDUMP_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> bfLoadChunk(String key, long iterator, byte[] data) {
/* 3958 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.LOADCHUNK).key(key).add(Long.valueOf(iterator)).add(data), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> bfCard(String key) {
/* 3962 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.CARD).key(key), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Object>> bfInfo(String key) {
/* 3966 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.BloomFilterCommand.INFO).key(key), BuilderFactory.ENCODED_OBJECT_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> cfReserve(String key, long capacity) {
/* 3970 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.RESERVE).key(key).add(Long.valueOf(capacity)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> cfReserve(String key, long capacity, CFReserveParams reserveParams) {
/* 3974 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.RESERVE).key(key).add(Long.valueOf(capacity)).addParams((IParams)reserveParams), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> cfAdd(String key, String item) {
/* 3978 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.ADD).key(key).add(item), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> cfAddNx(String key, String item) {
/* 3982 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.ADDNX).key(key).add(item), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> cfInsert(String key, String... items) {
/* 3986 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.INSERT).key(key)
/* 3987 */         .add(RedisBloomProtocol.RedisBloomKeyword.ITEMS).addObjects((Object[])items), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> cfInsert(String key, CFInsertParams insertParams, String... items) {
/* 3991 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.INSERT).key(key)
/* 3992 */         .addParams((IParams)insertParams).add(RedisBloomProtocol.RedisBloomKeyword.ITEMS).addObjects((Object[])items), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> cfInsertNx(String key, String... items) {
/* 3996 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.INSERTNX).key(key)
/* 3997 */         .add(RedisBloomProtocol.RedisBloomKeyword.ITEMS).addObjects((Object[])items), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> cfInsertNx(String key, CFInsertParams insertParams, String... items) {
/* 4001 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.INSERTNX).key(key)
/* 4002 */         .addParams((IParams)insertParams).add(RedisBloomProtocol.RedisBloomKeyword.ITEMS).addObjects((Object[])items), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> cfExists(String key, String item) {
/* 4006 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.EXISTS).key(key).add(item), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> cfMExists(String key, String... items) {
/* 4010 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.MEXISTS).key(key)
/* 4011 */         .addObjects((Object[])items), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Boolean> cfDel(String key, String item) {
/* 4015 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.DEL).key(key).add(item), BuilderFactory.BOOLEAN);
/*      */   }
/*      */   
/*      */   public final CommandObject<Long> cfCount(String key, String item) {
/* 4019 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.COUNT).key(key).add(item), BuilderFactory.LONG);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map.Entry<Long, byte[]>> cfScanDump(String key, long iterator) {
/* 4023 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.SCANDUMP).key(key).add(Long.valueOf(iterator)), BLOOM_SCANDUMP_RESPONSE);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> cfLoadChunk(String key, long iterator, byte[] data) {
/* 4027 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.LOADCHUNK).key(key).add(Long.valueOf(iterator)).add(data), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Object>> cfInfo(String key) {
/* 4031 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CuckooFilterCommand.INFO).key(key), BuilderFactory.ENCODED_OBJECT_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> cmsInitByDim(String key, long width, long depth) {
/* 4035 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CountMinSketchCommand.INITBYDIM).key(key).add(Long.valueOf(width))
/* 4036 */         .add(Long.valueOf(depth)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> cmsInitByProb(String key, double error, double probability) {
/* 4040 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CountMinSketchCommand.INITBYPROB).key(key).add(Double.valueOf(error))
/* 4041 */         .add(Double.valueOf(probability)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> cmsIncrBy(String key, Map<String, Long> itemIncrements) {
/* 4045 */     CommandArguments args = commandArguments((ProtocolCommand)RedisBloomProtocol.CountMinSketchCommand.INCRBY).key(key);
/* 4046 */     itemIncrements.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
/* 4047 */     return new CommandObject<>(args, BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> cmsQuery(String key, String... items) {
/* 4051 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CountMinSketchCommand.QUERY).key(key)
/* 4052 */         .addObjects((Object[])items), BuilderFactory.LONG_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> cmsMerge(String destKey, String... keys) {
/* 4056 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CountMinSketchCommand.MERGE).key(destKey)
/* 4057 */         .add(Integer.valueOf(keys.length)).keys((Object[])keys), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> cmsMerge(String destKey, Map<String, Long> keysAndWeights) {
/* 4061 */     CommandArguments args = commandArguments((ProtocolCommand)RedisBloomProtocol.CountMinSketchCommand.MERGE).key(destKey);
/* 4062 */     args.add(Integer.valueOf(keysAndWeights.size()));
/* 4063 */     keysAndWeights.entrySet().forEach(entry -> args.key(entry.getKey()));
/* 4064 */     args.add(RedisBloomProtocol.RedisBloomKeyword.WEIGHTS);
/* 4065 */     keysAndWeights.entrySet().forEach(entry -> args.add(entry.getValue()));
/* 4066 */     return new CommandObject<>(args, BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Object>> cmsInfo(String key) {
/* 4070 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.CountMinSketchCommand.INFO).key(key), BuilderFactory.ENCODED_OBJECT_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> topkReserve(String key, long topk) {
/* 4074 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.RESERVE).key(key).add(Long.valueOf(topk)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> topkReserve(String key, long topk, long width, long depth, double decay) {
/* 4078 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.RESERVE).key(key).add(Long.valueOf(topk))
/* 4079 */         .add(Long.valueOf(width)).add(Long.valueOf(depth)).add(Double.valueOf(decay)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> topkAdd(String key, String... items) {
/* 4083 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.ADD).key(key).addObjects((Object[])items), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> topkIncrBy(String key, Map<String, Long> itemIncrements) {
/* 4087 */     CommandArguments args = commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.INCRBY).key(key);
/* 4088 */     itemIncrements.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
/* 4089 */     return new CommandObject<>(args, BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Boolean>> topkQuery(String key, String... items) {
/* 4093 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.QUERY).key(key).addObjects((Object[])items), BuilderFactory.BOOLEAN_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<String>> topkList(String key) {
/* 4097 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.LIST).key(key), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Long>> topkListWithCount(String key) {
/* 4101 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.LIST).key(key)
/* 4102 */         .add(RedisBloomProtocol.RedisBloomKeyword.WITHCOUNT), BuilderFactory.STRING_LONG_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Object>> topkInfo(String key) {
/* 4106 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TopKCommand.INFO).key(key), BuilderFactory.ENCODED_OBJECT_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tdigestCreate(String key) {
/* 4110 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.CREATE).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tdigestCreate(String key, int compression) {
/* 4114 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.CREATE).key(key).add(RedisBloomProtocol.RedisBloomKeyword.COMPRESSION)
/* 4115 */         .add(Integer.valueOf(compression)), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tdigestReset(String key) {
/* 4119 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.RESET).key(key), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tdigestMerge(String destinationKey, String... sourceKeys) {
/* 4123 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.MERGE).key(destinationKey)
/* 4124 */         .add(Integer.valueOf(sourceKeys.length)).keys((Object[])sourceKeys), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<String> tdigestMerge(TDigestMergeParams mergeParams, String destinationKey, String... sourceKeys) {
/* 4129 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.MERGE).key(destinationKey)
/* 4130 */         .add(Integer.valueOf(sourceKeys.length)).keys((Object[])sourceKeys).addParams((IParams)mergeParams), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<Map<String, Object>> tdigestInfo(String key) {
/* 4134 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.INFO).key(key), BuilderFactory.ENCODED_OBJECT_MAP);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tdigestAdd(String key, double... values) {
/* 4138 */     return new CommandObject<>(addFlatArgs(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.ADD).key(key), values), BuilderFactory.STRING);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Double>> tdigestCDF(String key, double... values) {
/* 4143 */     return new CommandObject<>(addFlatArgs(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.CDF).key(key), values), BuilderFactory.DOUBLE_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Double>> tdigestQuantile(String key, double... quantiles) {
/* 4148 */     return new CommandObject<>(addFlatArgs(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.QUANTILE).key(key), quantiles), BuilderFactory.DOUBLE_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<Double> tdigestMin(String key) {
/* 4153 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.MIN).key(key), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> tdigestMax(String key) {
/* 4157 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.MAX).key(key), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<Double> tdigestTrimmedMean(String key, double lowCutQuantile, double highCutQuantile) {
/* 4161 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.TRIMMED_MEAN).key(key).add(Double.valueOf(lowCutQuantile))
/* 4162 */         .add(Double.valueOf(highCutQuantile)), BuilderFactory.DOUBLE);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<Long>> tdigestRank(String key, double... values) {
/* 4166 */     return new CommandObject<>(addFlatArgs(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.RANK).key(key), values), BuilderFactory.LONG_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Long>> tdigestRevRank(String key, double... values) {
/* 4171 */     return new CommandObject<>(addFlatArgs(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.REVRANK).key(key), values), BuilderFactory.LONG_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Double>> tdigestByRank(String key, long... ranks) {
/* 4176 */     return new CommandObject<>(addFlatArgs(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.BYRANK).key(key), ranks), BuilderFactory.DOUBLE_LIST);
/*      */   }
/*      */ 
/*      */   
/*      */   public final CommandObject<List<Double>> tdigestByRevRank(String key, long... ranks) {
/* 4181 */     return new CommandObject<>(addFlatArgs(commandArguments((ProtocolCommand)RedisBloomProtocol.TDigestCommand.BYREVRANK).key(key), ranks), BuilderFactory.DOUBLE_LIST);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<List<String>> graphList() {
/* 4189 */     return new CommandObject<>(commandArguments((ProtocolCommand)GraphProtocol.GraphCommand.LIST), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<List<String>> graphProfile(String graphName, String query) {
/* 4194 */     return new CommandObject<>(commandArguments((ProtocolCommand)GraphProtocol.GraphCommand.PROFILE).key(graphName).add(query), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<List<String>> graphExplain(String graphName, String query) {
/* 4199 */     return new CommandObject<>(commandArguments((ProtocolCommand)GraphProtocol.GraphCommand.EXPLAIN).key(graphName).add(query), BuilderFactory.STRING_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<List<List<Object>>> graphSlowlog(String graphName) {
/* 4204 */     return new CommandObject<>(commandArguments((ProtocolCommand)GraphProtocol.GraphCommand.SLOWLOG).key(graphName), BuilderFactory.ENCODED_OBJECT_LIST_LIST);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<String> graphConfigSet(String configName, Object value) {
/* 4209 */     return new CommandObject<>(commandArguments((ProtocolCommand)GraphProtocol.GraphCommand.CONFIG).add(GraphProtocol.GraphKeyword.SET).add(configName).add(value), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final CommandObject<Map<String, Object>> graphConfigGet(String configName) {
/* 4214 */     return new CommandObject<>(commandArguments((ProtocolCommand)GraphProtocol.GraphCommand.CONFIG).add(GraphProtocol.GraphKeyword.GET).add(configName), BuilderFactory.ENCODED_OBJECT_MAP);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final CommandObject<String> tFunctionLoad(String libraryCode, TFunctionLoadParams params) {
/* 4220 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisGearsProtocol.GearsCommand.TFUNCTION).add(RedisGearsProtocol.GearsKeyword.LOAD)
/* 4221 */         .addParams((IParams)params).add(libraryCode), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<String> tFunctionDelete(String libraryName) {
/* 4225 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisGearsProtocol.GearsCommand.TFUNCTION).add(RedisGearsProtocol.GearsKeyword.DELETE)
/* 4226 */         .add(libraryName), BuilderFactory.STRING);
/*      */   }
/*      */   
/*      */   public final CommandObject<List<GearsLibraryInfo>> tFunctionList(TFunctionListParams params) {
/* 4230 */     return new CommandObject<>(commandArguments((ProtocolCommand)RedisGearsProtocol.GearsCommand.TFUNCTION).add(RedisGearsProtocol.GearsKeyword.LIST)
/* 4231 */         .addParams((IParams)params), GearsLibraryInfo.GEARS_LIBRARY_INFO_LIST);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> tFunctionCall(String library, String function, List<String> keys, List<String> args) {
/* 4235 */     return new CommandObject(commandArguments((ProtocolCommand)RedisGearsProtocol.GearsCommand.TFCALL).add(library + "." + function)
/* 4236 */         .add(Integer.valueOf(keys.size())).keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */   
/*      */   public final CommandObject<Object> tFunctionCallAsync(String library, String function, List<String> keys, List<String> args) {
/* 4240 */     return new CommandObject(commandArguments((ProtocolCommand)RedisGearsProtocol.GearsCommand.TFCALLASYNC).add(library + "." + function)
/* 4241 */         .add(Integer.valueOf(keys.size())).keys(keys).addObjects(args), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private JsonObjectMapper getJsonObjectMapper() {
/*      */     DefaultGsonObjectMapper defaultGsonObjectMapper;
/* 4257 */     JsonObjectMapper localRef = this.jsonObjectMapper;
/* 4258 */     if (Objects.isNull(localRef)) {
/* 4259 */       synchronized (this) {
/* 4260 */         localRef = this.jsonObjectMapper;
/* 4261 */         if (Objects.isNull(localRef)) {
/* 4262 */           this.jsonObjectMapper = (JsonObjectMapper)(defaultGsonObjectMapper = new DefaultGsonObjectMapper());
/*      */         }
/*      */       } 
/*      */     }
/* 4266 */     return (JsonObjectMapper)defaultGsonObjectMapper;
/*      */   }
/*      */   
/*      */   public void setJsonObjectMapper(JsonObjectMapper jsonObjectMapper) {
/* 4270 */     this.jsonObjectMapper = jsonObjectMapper;
/*      */   }
/*      */   
/*      */   public void setDefaultSearchDialect(int dialect) {
/* 4274 */     if (dialect == 0) throw new IllegalArgumentException("DIALECT=0 cannot be set."); 
/* 4275 */     this.searchDialect.set(dialect);
/*      */   }
/*      */   
/*      */   private class SearchProfileResponseBuilder<T>
/*      */     extends Builder<Map.Entry<T, Map<String, Object>>>
/*      */   {
/*      */     private static final String PROFILE_STR = "profile";
/*      */     private final Builder<T> replyBuilder;
/*      */     
/*      */     public SearchProfileResponseBuilder(Builder<T> replyBuilder) {
/* 4285 */       this.replyBuilder = replyBuilder;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<T, Map<String, Object>> build(Object data) {
/* 4290 */       List list = (List)data;
/* 4291 */       if (list == null || list.isEmpty()) return null;
/*      */       
/* 4293 */       if (list.get(0) instanceof KeyValue) {
/* 4294 */         for (KeyValue keyValue : data) {
/* 4295 */           if ("profile".equals(BuilderFactory.STRING.build(keyValue.getKey()))) {
/* 4296 */             return (Map.Entry<T, Map<String, Object>>)KeyValue.of(this.replyBuilder.build(data), BuilderFactory.AGGRESSIVE_ENCODED_OBJECT_MAP
/* 4297 */                 .build(keyValue.getValue()));
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/* 4302 */       return (Map.Entry<T, Map<String, Object>>)KeyValue.of(this.replyBuilder.build(list.get(0)), SearchBuilderFactory.SEARCH_PROFILE_PROFILE
/* 4303 */           .build(list.get(1)));
/*      */     }
/*      */   }
/*      */   
/*      */   private class JsonObjectBuilder<T>
/*      */     extends Builder<T> {
/*      */     private final Class<T> clazz;
/*      */     
/*      */     public JsonObjectBuilder(Class<T> clazz) {
/* 4312 */       this.clazz = clazz;
/*      */     }
/*      */ 
/*      */     
/*      */     public T build(Object data) {
/* 4317 */       return (T)CommandObjects.this.getJsonObjectMapper().fromJson(BuilderFactory.STRING.build(data), this.clazz);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 4324 */   private final Builder<Object> JSON_GENERIC_OBJECT = new JsonObjectBuilder(Object.class);
/*      */   
/*      */   private class JsonObjectListBuilder<T>
/*      */     extends Builder<List<T>> {
/*      */     private final Class<T> clazz;
/*      */     
/*      */     public JsonObjectListBuilder(Class<T> clazz) {
/* 4331 */       this.clazz = clazz;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<T> build(Object data) {
/* 4336 */       if (data == null) {
/* 4337 */         return null;
/*      */       }
/* 4339 */       List<String> list = BuilderFactory.STRING_LIST.build(data);
/* 4340 */       return (List<T>)list.stream().map(s -> CommandObjects.this.getJsonObjectMapper().fromJson(s, this.clazz)).collect(Collectors.toList());
/*      */     }
/*      */   }
/*      */   
/* 4344 */   private static final Builder<Map.Entry<Long, byte[]>> BLOOM_SCANDUMP_RESPONSE = new Builder<Map.Entry<Long, byte[]>>()
/*      */     {
/*      */       public Map.Entry<Long, byte[]> build(Object data) {
/* 4347 */         List<Object> list = (List<Object>)data;
/* 4348 */         return (Map.Entry<Long, byte[]>)new KeyValue(BuilderFactory.LONG.build(list.get(0)), BuilderFactory.BINARY.build(list.get(1)));
/*      */       }
/*      */     };
/*      */   
/*      */   private CommandArguments addFlatArgs(CommandArguments args, long... values) {
/* 4353 */     for (long value : values) {
/* 4354 */       args.add(Long.valueOf(value));
/*      */     }
/* 4356 */     return args;
/*      */   }
/*      */   
/*      */   private CommandArguments addFlatArgs(CommandArguments args, double... values) {
/* 4360 */     for (double value : values) {
/* 4361 */       args.add(Double.valueOf(value));
/*      */     }
/* 4363 */     return args;
/*      */   }
/*      */   
/*      */   private CommandArguments addFlatKeyValueArgs(CommandArguments args, String... keyvalues) {
/* 4367 */     for (int i = 0; i < keyvalues.length; i += 2) {
/* 4368 */       args.key(keyvalues[i]).add(keyvalues[i + 1]);
/*      */     }
/* 4370 */     return args;
/*      */   }
/*      */   
/*      */   private CommandArguments addFlatKeyValueArgs(CommandArguments args, byte[]... keyvalues) {
/* 4374 */     for (int i = 0; i < keyvalues.length; i += 2) {
/* 4375 */       args.key(keyvalues[i]).add(keyvalues[i + 1]);
/*      */     }
/* 4377 */     return args;
/*      */   }
/*      */   
/*      */   private CommandArguments addFlatMapArgs(CommandArguments args, Map<?, ?> map) {
/* 4381 */     for (Map.Entry<? extends Object, ? extends Object> entry : map.entrySet()) {
/* 4382 */       args.add(entry.getKey());
/* 4383 */       args.add(entry.getValue());
/*      */     } 
/* 4385 */     return args;
/*      */   }
/*      */   
/*      */   private CommandArguments addSortedSetFlatMapArgs(CommandArguments args, Map<?, Double> map) {
/* 4389 */     for (Map.Entry<? extends Object, Double> entry : map.entrySet()) {
/* 4390 */       args.add(entry.getValue());
/* 4391 */       args.add(entry.getKey());
/*      */     } 
/* 4393 */     return args;
/*      */   }
/*      */   
/*      */   private CommandArguments addGeoCoordinateFlatMapArgs(CommandArguments args, Map<?, GeoCoordinate> map) {
/* 4397 */     for (Map.Entry<? extends Object, GeoCoordinate> entry : map.entrySet()) {
/* 4398 */       GeoCoordinate ord = entry.getValue();
/* 4399 */       args.add(Double.valueOf(ord.getLongitude()));
/* 4400 */       args.add(Double.valueOf(ord.getLatitude()));
/* 4401 */       args.add(entry.getKey());
/*      */     } 
/* 4403 */     return args;
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\CommandObjects.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */