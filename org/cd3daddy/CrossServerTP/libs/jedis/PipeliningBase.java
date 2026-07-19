/*      */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ExpiryOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.ListDirection;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortedSetOption;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.ResultSet;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.JsonSetParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.json.Path2;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoRadiusStoreParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.GeoSearchParam;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.LPosParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.SetParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.SortingParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAutoClaimParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XClaimParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZAddParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ZRangeParams;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.GeoRadiusResponse;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.ScanResult;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamEntry;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.Tuple;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.search.Query;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.timeseries.TSElement;
/*      */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*      */ 
/*      */ public abstract class PipeliningBase implements PipelineCommands, PipelineBinaryCommands, RedisModulePipelineCommands {
/*      */   protected PipeliningBase(CommandObjects commandObjects) {
/*   36 */     this.commandObjects = commandObjects;
/*      */   }
/*      */   
/*      */   protected final CommandObjects commandObjects;
/*      */   private GraphCommandObjects graphCommandObjects;
/*      */   
/*      */   protected final void setGraphCommands(GraphCommandObjects graphCommandObjects) {
/*   43 */     this.graphCommandObjects = graphCommandObjects;
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract <T> Response<T> appendCommand(CommandObject<T> paramCommandObject);
/*      */   
/*      */   public Response<Boolean> exists(String key) {
/*   50 */     return appendCommand(this.commandObjects.exists(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> exists(String... keys) {
/*   55 */     return appendCommand(this.commandObjects.exists(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> persist(String key) {
/*   60 */     return appendCommand(this.commandObjects.persist(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> type(String key) {
/*   65 */     return appendCommand(this.commandObjects.type(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> dump(String key) {
/*   70 */     return (Response)appendCommand((CommandObject)this.commandObjects.dump(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> restore(String key, long ttl, byte[] serializedValue) {
/*   75 */     return appendCommand(this.commandObjects.restore(key, ttl, serializedValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> restore(String key, long ttl, byte[] serializedValue, RestoreParams params) {
/*   80 */     return appendCommand(this.commandObjects.restore(key, ttl, serializedValue, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expire(String key, long seconds) {
/*   85 */     return appendCommand(this.commandObjects.expire(key, seconds));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expire(String key, long seconds, ExpiryOption expiryOption) {
/*   90 */     return appendCommand(this.commandObjects.expire(key, seconds, expiryOption));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpire(String key, long milliseconds) {
/*   95 */     return appendCommand(this.commandObjects.pexpire(key, milliseconds));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpire(String key, long milliseconds, ExpiryOption expiryOption) {
/*  100 */     return appendCommand(this.commandObjects.pexpire(key, milliseconds, expiryOption));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expireTime(String key) {
/*  105 */     return appendCommand(this.commandObjects.expireTime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpireTime(String key) {
/*  110 */     return appendCommand(this.commandObjects.pexpireTime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expireAt(String key, long unixTime) {
/*  115 */     return appendCommand(this.commandObjects.expireAt(key, unixTime));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expireAt(String key, long unixTime, ExpiryOption expiryOption) {
/*  120 */     return appendCommand(this.commandObjects.expireAt(key, unixTime, expiryOption));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpireAt(String key, long millisecondsTimestamp) {
/*  125 */     return appendCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpireAt(String key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/*  130 */     return appendCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp, expiryOption));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ttl(String key) {
/*  135 */     return appendCommand(this.commandObjects.ttl(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pttl(String key) {
/*  140 */     return appendCommand(this.commandObjects.pttl(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> touch(String key) {
/*  145 */     return appendCommand(this.commandObjects.touch(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> touch(String... keys) {
/*  150 */     return appendCommand(this.commandObjects.touch(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> sort(String key) {
/*  155 */     return appendCommand(this.commandObjects.sort(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sort(String key, String dstKey) {
/*  160 */     return appendCommand(this.commandObjects.sort(key, dstKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> sort(String key, SortingParams sortingParams) {
/*  165 */     return appendCommand(this.commandObjects.sort(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sort(String key, SortingParams sortingParams, String dstKey) {
/*  170 */     return appendCommand(this.commandObjects.sort(key, sortingParams, dstKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> sortReadonly(String key, SortingParams sortingParams) {
/*  175 */     return appendCommand(this.commandObjects.sortReadonly(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> del(String key) {
/*  180 */     return appendCommand(this.commandObjects.del(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> del(String... keys) {
/*  185 */     return appendCommand(this.commandObjects.del(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> unlink(String key) {
/*  190 */     return appendCommand(this.commandObjects.unlink(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> unlink(String... keys) {
/*  195 */     return appendCommand(this.commandObjects.unlink(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> copy(String srcKey, String dstKey, boolean replace) {
/*  200 */     return appendCommand(this.commandObjects.copy(srcKey, dstKey, replace));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> rename(String oldkey, String newkey) {
/*  205 */     return appendCommand(this.commandObjects.rename(oldkey, newkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> renamenx(String oldkey, String newkey) {
/*  210 */     return appendCommand(this.commandObjects.renamenx(oldkey, newkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> memoryUsage(String key) {
/*  215 */     return appendCommand(this.commandObjects.memoryUsage(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> memoryUsage(String key, int samples) {
/*  220 */     return appendCommand(this.commandObjects.memoryUsage(key, samples));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> objectRefcount(String key) {
/*  225 */     return appendCommand(this.commandObjects.objectRefcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> objectEncoding(String key) {
/*  230 */     return appendCommand(this.commandObjects.objectEncoding(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> objectIdletime(String key) {
/*  235 */     return appendCommand(this.commandObjects.objectIdletime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> objectFreq(String key) {
/*  240 */     return appendCommand(this.commandObjects.objectFreq(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> migrate(String host, int port, String key, int timeout) {
/*  245 */     return appendCommand(this.commandObjects.migrate(host, port, key, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> migrate(String host, int port, int timeout, MigrateParams params, String... keys) {
/*  250 */     return appendCommand(this.commandObjects.migrate(host, port, timeout, params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> keys(String pattern) {
/*  255 */     return appendCommand(this.commandObjects.keys(pattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<String>> scan(String cursor) {
/*  260 */     return appendCommand(this.commandObjects.scan(cursor));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<String>> scan(String cursor, ScanParams params) {
/*  265 */     return appendCommand(this.commandObjects.scan(cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<String>> scan(String cursor, ScanParams params, String type) {
/*  270 */     return appendCommand(this.commandObjects.scan(cursor, params, type));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> randomKey() {
/*  275 */     return appendCommand(this.commandObjects.randomKey());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> get(String key) {
/*  280 */     return appendCommand(this.commandObjects.get(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> setGet(String key, String value, SetParams params) {
/*  285 */     return appendCommand(this.commandObjects.setGet(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> getDel(String key) {
/*  290 */     return appendCommand(this.commandObjects.getDel(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> getEx(String key, GetExParams params) {
/*  295 */     return appendCommand(this.commandObjects.getEx(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> setbit(String key, long offset, boolean value) {
/*  300 */     return appendCommand(this.commandObjects.setbit(key, offset, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> getbit(String key, long offset) {
/*  305 */     return appendCommand(this.commandObjects.getbit(key, offset));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> setrange(String key, long offset, String value) {
/*  310 */     return appendCommand(this.commandObjects.setrange(key, offset, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> getrange(String key, long startOffset, long endOffset) {
/*  315 */     return appendCommand(this.commandObjects.getrange(key, startOffset, endOffset));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> getSet(String key, String value) {
/*  320 */     return appendCommand(this.commandObjects.getSet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> setnx(String key, String value) {
/*  325 */     return appendCommand(this.commandObjects.setnx(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> setex(String key, long seconds, String value) {
/*  330 */     return appendCommand(this.commandObjects.setex(key, seconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> psetex(String key, long milliseconds, String value) {
/*  335 */     return appendCommand(this.commandObjects.psetex(key, milliseconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> mget(String... keys) {
/*  340 */     return appendCommand(this.commandObjects.mget(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> mset(String... keysvalues) {
/*  345 */     return appendCommand(this.commandObjects.mset(keysvalues));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> msetnx(String... keysvalues) {
/*  350 */     return appendCommand(this.commandObjects.msetnx(keysvalues));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> incr(String key) {
/*  355 */     return appendCommand(this.commandObjects.incr(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> incrBy(String key, long increment) {
/*  360 */     return appendCommand(this.commandObjects.incrBy(key, increment));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> incrByFloat(String key, double increment) {
/*  365 */     return appendCommand(this.commandObjects.incrByFloat(key, increment));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> decr(String key) {
/*  370 */     return appendCommand(this.commandObjects.decr(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> decrBy(String key, long decrement) {
/*  375 */     return appendCommand(this.commandObjects.decrBy(key, decrement));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> append(String key, String value) {
/*  380 */     return appendCommand(this.commandObjects.append(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> substr(String key, int start, int end) {
/*  385 */     return appendCommand(this.commandObjects.substr(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> strlen(String key) {
/*  390 */     return appendCommand(this.commandObjects.strlen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitcount(String key) {
/*  395 */     return appendCommand(this.commandObjects.bitcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitcount(String key, long start, long end) {
/*  400 */     return appendCommand(this.commandObjects.bitcount(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitcount(String key, long start, long end, BitCountOption option) {
/*  405 */     return appendCommand(this.commandObjects.bitcount(key, start, end, option));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitpos(String key, boolean value) {
/*  410 */     return appendCommand(this.commandObjects.bitpos(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitpos(String key, boolean value, BitPosParams params) {
/*  415 */     return appendCommand(this.commandObjects.bitpos(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> bitfield(String key, String... arguments) {
/*  420 */     return appendCommand(this.commandObjects.bitfield(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> bitfieldReadonly(String key, String... arguments) {
/*  425 */     return appendCommand(this.commandObjects.bitfieldReadonly(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitop(BitOP op, String destKey, String... srcKeys) {
/*  430 */     return appendCommand(this.commandObjects.bitop(op, destKey, srcKeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<LCSMatchResult> lcs(String keyA, String keyB, LCSParams params) {
/*  435 */     return appendCommand(this.commandObjects.lcs(keyA, keyB, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> set(String key, String value) {
/*  440 */     return appendCommand(this.commandObjects.set(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> set(String key, String value, SetParams params) {
/*  445 */     return appendCommand(this.commandObjects.set(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> rpush(String key, String... string) {
/*  450 */     return appendCommand(this.commandObjects.rpush(key, string));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Response<Long> lpush(String key, String... string) {
/*  456 */     return appendCommand(this.commandObjects.lpush(key, string));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> llen(String key) {
/*  461 */     return appendCommand(this.commandObjects.llen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> lrange(String key, long start, long stop) {
/*  466 */     return appendCommand(this.commandObjects.lrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ltrim(String key, long start, long stop) {
/*  471 */     return appendCommand(this.commandObjects.ltrim(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> lindex(String key, long index) {
/*  476 */     return appendCommand(this.commandObjects.lindex(key, index));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> lset(String key, long index, String value) {
/*  481 */     return appendCommand(this.commandObjects.lset(key, index, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lrem(String key, long count, String value) {
/*  486 */     return appendCommand(this.commandObjects.lrem(key, count, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> lpop(String key) {
/*  491 */     return appendCommand(this.commandObjects.lpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> lpop(String key, int count) {
/*  496 */     return appendCommand(this.commandObjects.lpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lpos(String key, String element) {
/*  501 */     return appendCommand(this.commandObjects.lpos(key, element));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lpos(String key, String element, LPosParams params) {
/*  506 */     return appendCommand(this.commandObjects.lpos(key, element, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> lpos(String key, String element, LPosParams params, long count) {
/*  511 */     return appendCommand(this.commandObjects.lpos(key, element, params, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> rpop(String key) {
/*  516 */     return appendCommand(this.commandObjects.rpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> rpop(String key, int count) {
/*  521 */     return appendCommand(this.commandObjects.rpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> linsert(String key, ListPosition where, String pivot, String value) {
/*  526 */     return appendCommand(this.commandObjects.linsert(key, where, pivot, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lpushx(String key, String... strings) {
/*  531 */     return appendCommand(this.commandObjects.lpushx(key, strings));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> rpushx(String key, String... strings) {
/*  536 */     return appendCommand(this.commandObjects.rpushx(key, strings));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> blpop(int timeout, String key) {
/*  541 */     return appendCommand(this.commandObjects.blpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, String>> blpop(double timeout, String key) {
/*  546 */     return appendCommand(this.commandObjects.blpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> brpop(int timeout, String key) {
/*  551 */     return appendCommand(this.commandObjects.brpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, String>> brpop(double timeout, String key) {
/*  556 */     return appendCommand(this.commandObjects.brpop(timeout, key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> blpop(int timeout, String... keys) {
/*  561 */     return appendCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, String>> blpop(double timeout, String... keys) {
/*  566 */     return appendCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> brpop(int timeout, String... keys) {
/*  571 */     return appendCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, String>> brpop(double timeout, String... keys) {
/*  576 */     return appendCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> rpoplpush(String srcKey, String dstKey) {
/*  581 */     return appendCommand(this.commandObjects.rpoplpush(srcKey, dstKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> brpoplpush(String source, String destination, int timeout) {
/*  586 */     return appendCommand(this.commandObjects.brpoplpush(source, destination, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> lmove(String srcKey, String dstKey, ListDirection from, ListDirection to) {
/*  591 */     return appendCommand(this.commandObjects.lmove(srcKey, dstKey, from, to));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> blmove(String srcKey, String dstKey, ListDirection from, ListDirection to, double timeout) {
/*  596 */     return appendCommand(this.commandObjects.blmove(srcKey, dstKey, from, to, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<String>>> lmpop(ListDirection direction, String... keys) {
/*  601 */     return appendCommand(this.commandObjects.lmpop(direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<String>>> lmpop(ListDirection direction, int count, String... keys) {
/*  606 */     return appendCommand(this.commandObjects.lmpop(direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<String>>> blmpop(double timeout, ListDirection direction, String... keys) {
/*  611 */     return appendCommand(this.commandObjects.blmpop(timeout, direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<String>>> blmpop(double timeout, ListDirection direction, int count, String... keys) {
/*  616 */     return appendCommand(this.commandObjects.blmpop(timeout, direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hset(String key, String field, String value) {
/*  621 */     return appendCommand(this.commandObjects.hset(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hset(String key, Map<String, String> hash) {
/*  626 */     return appendCommand(this.commandObjects.hset(key, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> hget(String key, String field) {
/*  631 */     return appendCommand(this.commandObjects.hget(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hsetnx(String key, String field, String value) {
/*  636 */     return appendCommand(this.commandObjects.hsetnx(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> hmset(String key, Map<String, String> hash) {
/*  641 */     return appendCommand(this.commandObjects.hmset(key, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> hmget(String key, String... fields) {
/*  646 */     return appendCommand(this.commandObjects.hmget(key, fields));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hincrBy(String key, String field, long value) {
/*  651 */     return appendCommand(this.commandObjects.hincrBy(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> hincrByFloat(String key, String field, double value) {
/*  656 */     return appendCommand(this.commandObjects.hincrByFloat(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> hexists(String key, String field) {
/*  661 */     return appendCommand(this.commandObjects.hexists(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hdel(String key, String... field) {
/*  666 */     return appendCommand(this.commandObjects.hdel(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hlen(String key) {
/*  671 */     return appendCommand(this.commandObjects.hlen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> hkeys(String key) {
/*  676 */     return appendCommand(this.commandObjects.hkeys(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> hvals(String key) {
/*  681 */     return appendCommand(this.commandObjects.hvals(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, String>> hgetAll(String key) {
/*  686 */     return appendCommand(this.commandObjects.hgetAll(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> hrandfield(String key) {
/*  691 */     return appendCommand(this.commandObjects.hrandfield(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> hrandfield(String key, long count) {
/*  696 */     return appendCommand(this.commandObjects.hrandfield(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Map.Entry<String, String>>> hrandfieldWithValues(String key, long count) {
/*  701 */     return appendCommand(this.commandObjects.hrandfieldWithValues(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<Map.Entry<String, String>>> hscan(String key, String cursor, ScanParams params) {
/*  706 */     return appendCommand(this.commandObjects.hscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hstrlen(String key, String field) {
/*  711 */     return appendCommand(this.commandObjects.hstrlen(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sadd(String key, String... members) {
/*  716 */     return appendCommand(this.commandObjects.sadd(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> smembers(String key) {
/*  721 */     return appendCommand(this.commandObjects.smembers(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> srem(String key, String... members) {
/*  726 */     return appendCommand(this.commandObjects.srem(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> spop(String key) {
/*  731 */     return appendCommand(this.commandObjects.spop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> spop(String key, long count) {
/*  736 */     return appendCommand(this.commandObjects.spop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> scard(String key) {
/*  741 */     return appendCommand(this.commandObjects.scard(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> sismember(String key, String member) {
/*  746 */     return appendCommand(this.commandObjects.sismember(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> smismember(String key, String... members) {
/*  751 */     return appendCommand(this.commandObjects.smismember(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> srandmember(String key) {
/*  756 */     return appendCommand(this.commandObjects.srandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> srandmember(String key, int count) {
/*  761 */     return appendCommand(this.commandObjects.srandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<String>> sscan(String key, String cursor, ScanParams params) {
/*  766 */     return appendCommand(this.commandObjects.sscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> sdiff(String... keys) {
/*  771 */     return appendCommand(this.commandObjects.sdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sdiffStore(String dstKey, String... keys) {
/*  776 */     return appendCommand(this.commandObjects.sdiffstore(dstKey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> sinter(String... keys) {
/*  781 */     return appendCommand(this.commandObjects.sinter(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sinterstore(String dstKey, String... keys) {
/*  786 */     return appendCommand(this.commandObjects.sinterstore(dstKey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sintercard(String... keys) {
/*  791 */     return appendCommand(this.commandObjects.sintercard(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sintercard(int limit, String... keys) {
/*  796 */     return appendCommand(this.commandObjects.sintercard(limit, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> sunion(String... keys) {
/*  801 */     return appendCommand(this.commandObjects.sunion(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sunionstore(String dstKey, String... keys) {
/*  806 */     return appendCommand(this.commandObjects.sunionstore(dstKey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> smove(String srcKey, String dstKey, String member) {
/*  811 */     return appendCommand(this.commandObjects.smove(srcKey, dstKey, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(String key, double score, String member) {
/*  816 */     return appendCommand(this.commandObjects.zadd(key, score, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(String key, double score, String member, ZAddParams params) {
/*  821 */     return appendCommand(this.commandObjects.zadd(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(String key, Map<String, Double> scoreMembers) {
/*  826 */     return appendCommand(this.commandObjects.zadd(key, scoreMembers));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(String key, Map<String, Double> scoreMembers, ZAddParams params) {
/*  831 */     return appendCommand(this.commandObjects.zadd(key, scoreMembers, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zaddIncr(String key, double score, String member, ZAddParams params) {
/*  836 */     return appendCommand(this.commandObjects.zaddIncr(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrem(String key, String... members) {
/*  841 */     return appendCommand(this.commandObjects.zrem(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zincrby(String key, double increment, String member) {
/*  846 */     return appendCommand(this.commandObjects.zincrby(key, increment, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zincrby(String key, double increment, String member, ZIncrByParams params) {
/*  851 */     return appendCommand(this.commandObjects.zincrby(key, increment, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrank(String key, String member) {
/*  856 */     return appendCommand(this.commandObjects.zrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrevrank(String key, String member) {
/*  861 */     return appendCommand(this.commandObjects.zrevrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<Long, Double>> zrankWithScore(String key, String member) {
/*  866 */     return appendCommand(this.commandObjects.zrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<Long, Double>> zrevrankWithScore(String key, String member) {
/*  871 */     return appendCommand(this.commandObjects.zrevrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrange(String key, long start, long stop) {
/*  876 */     return appendCommand(this.commandObjects.zrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrevrange(String key, long start, long stop) {
/*  881 */     return appendCommand(this.commandObjects.zrevrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeWithScores(String key, long start, long stop) {
/*  886 */     return appendCommand(this.commandObjects.zrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeWithScores(String key, long start, long stop) {
/*  891 */     return appendCommand(this.commandObjects.zrevrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> zrandmember(String key) {
/*  896 */     return appendCommand(this.commandObjects.zrandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrandmember(String key, long count) {
/*  901 */     return appendCommand(this.commandObjects.zrandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrandmemberWithScores(String key, long count) {
/*  906 */     return appendCommand(this.commandObjects.zrandmemberWithScores(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zcard(String key) {
/*  911 */     return appendCommand(this.commandObjects.zcard(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zscore(String key, String member) {
/*  916 */     return appendCommand(this.commandObjects.zscore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Double>> zmscore(String key, String... members) {
/*  921 */     return appendCommand(this.commandObjects.zmscore(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Tuple> zpopmax(String key) {
/*  926 */     return appendCommand(this.commandObjects.zpopmax(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zpopmax(String key, int count) {
/*  931 */     return appendCommand(this.commandObjects.zpopmax(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Tuple> zpopmin(String key) {
/*  936 */     return appendCommand(this.commandObjects.zpopmin(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zpopmin(String key, int count) {
/*  941 */     return appendCommand(this.commandObjects.zpopmin(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zcount(String key, double min, double max) {
/*  946 */     return appendCommand(this.commandObjects.zcount(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zcount(String key, String min, String max) {
/*  951 */     return appendCommand(this.commandObjects.zcount(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrangeByScore(String key, double min, double max) {
/*  956 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrangeByScore(String key, String min, String max) {
/*  961 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrevrangeByScore(String key, double max, double min) {
/*  966 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrangeByScore(String key, double min, double max, int offset, int count) {
/*  971 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrevrangeByScore(String key, String max, String min) {
/*  976 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrangeByScore(String key, String min, String max, int offset, int count) {
/*  981 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrevrangeByScore(String key, double max, double min, int offset, int count) {
/*  986 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(String key, double min, double max) {
/*  991 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min) {
/*  996 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
/* 1001 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrevrangeByScore(String key, String max, String min, int offset, int count) {
/* 1006 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(String key, String min, String max) {
/* 1011 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min) {
/* 1016 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
/* 1021 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
/* 1026 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
/* 1031 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrange(String key, ZRangeParams zRangeParams) {
/* 1036 */     return appendCommand(this.commandObjects.zrange(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeWithScores(String key, ZRangeParams zRangeParams) {
/* 1041 */     return appendCommand(this.commandObjects.zrangeWithScores(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrangestore(String dest, String src, ZRangeParams zRangeParams) {
/* 1046 */     return appendCommand(this.commandObjects.zrangestore(dest, src, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByRank(String key, long start, long stop) {
/* 1051 */     return appendCommand(this.commandObjects.zremrangeByRank(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByScore(String key, double min, double max) {
/* 1056 */     return appendCommand(this.commandObjects.zremrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByScore(String key, String min, String max) {
/* 1061 */     return appendCommand(this.commandObjects.zremrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zlexcount(String key, String min, String max) {
/* 1066 */     return appendCommand(this.commandObjects.zlexcount(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrangeByLex(String key, String min, String max) {
/* 1071 */     return appendCommand(this.commandObjects.zrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrangeByLex(String key, String min, String max, int offset, int count) {
/* 1076 */     return appendCommand(this.commandObjects.zrangeByLex(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrevrangeByLex(String key, String max, String min) {
/* 1081 */     return appendCommand(this.commandObjects.zrevrangeByLex(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zrevrangeByLex(String key, String max, String min, int offset, int count) {
/* 1086 */     return appendCommand(this.commandObjects.zrevrangeByLex(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByLex(String key, String min, String max) {
/* 1091 */     return appendCommand(this.commandObjects.zremrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<Tuple>> zscan(String key, String cursor, ScanParams params) {
/* 1096 */     return appendCommand(this.commandObjects.zscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, Tuple>> bzpopmax(double timeout, String... keys) {
/* 1101 */     return appendCommand(this.commandObjects.bzpopmax(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, Tuple>> bzpopmin(double timeout, String... keys) {
/* 1106 */     return appendCommand(this.commandObjects.bzpopmin(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<Tuple>>> zmpop(SortedSetOption option, String... keys) {
/* 1111 */     return appendCommand(this.commandObjects.zmpop(option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<Tuple>>> zmpop(SortedSetOption option, int count, String... keys) {
/* 1116 */     return appendCommand(this.commandObjects.zmpop(option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<Tuple>>> bzmpop(double timeout, SortedSetOption option, String... keys) {
/* 1121 */     return appendCommand(this.commandObjects.bzmpop(timeout, option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<String, List<Tuple>>> bzmpop(double timeout, SortedSetOption option, int count, String... keys) {
/* 1126 */     return appendCommand(this.commandObjects.bzmpop(timeout, option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zdiff(String... keys) {
/* 1131 */     return appendCommand(this.commandObjects.zdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zdiffWithScores(String... keys) {
/* 1136 */     return appendCommand(this.commandObjects.zdiffWithScores(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Response<Long> zdiffStore(String dstKey, String... keys) {
/* 1142 */     return appendCommand(this.commandObjects.zdiffStore(dstKey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zdiffstore(String dstKey, String... keys) {
/* 1147 */     return appendCommand(this.commandObjects.zdiffstore(dstKey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zinterstore(String dstKey, String... sets) {
/* 1152 */     return appendCommand(this.commandObjects.zinterstore(dstKey, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zinterstore(String dstKey, ZParams params, String... sets) {
/* 1157 */     return appendCommand(this.commandObjects.zinterstore(dstKey, params, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zinter(ZParams params, String... keys) {
/* 1162 */     return appendCommand(this.commandObjects.zinter(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zinterWithScores(ZParams params, String... keys) {
/* 1167 */     return appendCommand(this.commandObjects.zinterWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zintercard(String... keys) {
/* 1172 */     return appendCommand(this.commandObjects.zintercard(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zintercard(long limit, String... keys) {
/* 1177 */     return appendCommand(this.commandObjects.zintercard(limit, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> zunion(ZParams params, String... keys) {
/* 1182 */     return appendCommand(this.commandObjects.zunion(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zunionWithScores(ZParams params, String... keys) {
/* 1187 */     return appendCommand(this.commandObjects.zunionWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zunionstore(String dstKey, String... sets) {
/* 1192 */     return appendCommand(this.commandObjects.zunionstore(dstKey, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zunionstore(String dstKey, ZParams params, String... sets) {
/* 1197 */     return appendCommand(this.commandObjects.zunionstore(dstKey, params, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geoadd(String key, double longitude, double latitude, String member) {
/* 1202 */     return appendCommand(this.commandObjects.geoadd(key, longitude, latitude, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geoadd(String key, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 1207 */     return appendCommand(this.commandObjects.geoadd(key, memberCoordinateMap));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geoadd(String key, GeoAddParams params, Map<String, GeoCoordinate> memberCoordinateMap) {
/* 1212 */     return appendCommand(this.commandObjects.geoadd(key, params, memberCoordinateMap));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> geodist(String key, String member1, String member2) {
/* 1217 */     return appendCommand(this.commandObjects.geodist(key, member1, member2));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> geodist(String key, String member1, String member2, GeoUnit unit) {
/* 1222 */     return appendCommand(this.commandObjects.geodist(key, member1, member2, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> geohash(String key, String... members) {
/* 1227 */     return appendCommand(this.commandObjects.geohash(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoCoordinate>> geopos(String key, String... members) {
/* 1232 */     return appendCommand(this.commandObjects.geopos(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 1237 */     return appendCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 1242 */     return appendCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadius(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1247 */     return appendCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusReadonly(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1252 */     return appendCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit) {
/* 1257 */     return appendCommand(this.commandObjects.georadiusByMember(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit) {
/* 1262 */     return appendCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMember(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1267 */     return appendCommand(this.commandObjects.georadiusByMember(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1272 */     return appendCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> georadiusStore(String key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 1277 */     return appendCommand(this.commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> georadiusByMemberStore(String key, String member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 1282 */     return appendCommand(this.commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(String key, String member, double radius, GeoUnit unit) {
/* 1287 */     return appendCommand(this.commandObjects.geosearch(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(String key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 1292 */     return appendCommand(this.commandObjects.geosearch(key, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(String key, String member, double width, double height, GeoUnit unit) {
/* 1297 */     return appendCommand(this.commandObjects.geosearch(key, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(String key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 1302 */     return appendCommand(this.commandObjects.geosearch(key, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(String key, GeoSearchParam params) {
/* 1307 */     return appendCommand(this.commandObjects.geosearch(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(String dest, String src, String member, double radius, GeoUnit unit) {
/* 1312 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(String dest, String src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 1317 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(String dest, String src, String member, double width, double height, GeoUnit unit) {
/* 1322 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(String dest, String src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 1327 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(String dest, String src, GeoSearchParam params) {
/* 1332 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStoreStoreDist(String dest, String src, GeoSearchParam params) {
/* 1337 */     return appendCommand(this.commandObjects.geosearchStoreStoreDist(dest, src, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pfadd(String key, String... elements) {
/* 1342 */     return appendCommand(this.commandObjects.pfadd(key, elements));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> pfmerge(String destkey, String... sourcekeys) {
/* 1347 */     return appendCommand(this.commandObjects.pfmerge(destkey, sourcekeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pfcount(String key) {
/* 1352 */     return appendCommand(this.commandObjects.pfcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pfcount(String... keys) {
/* 1357 */     return appendCommand(this.commandObjects.pfcount(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<StreamEntryID> xadd(String key, StreamEntryID id, Map<String, String> hash) {
/* 1362 */     return appendCommand(this.commandObjects.xadd(key, id, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<StreamEntryID> xadd(String key, XAddParams params, Map<String, String> hash) {
/* 1367 */     return appendCommand(this.commandObjects.xadd(key, params, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xlen(String key) {
/* 1372 */     return appendCommand(this.commandObjects.xlen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrange(String key, StreamEntryID start, StreamEntryID end) {
/* 1377 */     return appendCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrange(String key, StreamEntryID start, StreamEntryID end, int count) {
/* 1382 */     return appendCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrevrange(String key, StreamEntryID end, StreamEntryID start) {
/* 1387 */     return appendCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrevrange(String key, StreamEntryID end, StreamEntryID start, int count) {
/* 1392 */     return appendCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrange(String key, String start, String end) {
/* 1397 */     return appendCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrange(String key, String start, String end, int count) {
/* 1402 */     return appendCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrevrange(String key, String end, String start) {
/* 1407 */     return appendCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xrevrange(String key, String end, String start, int count) {
/* 1412 */     return appendCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xack(String key, String group, StreamEntryID... ids) {
/* 1417 */     return appendCommand(this.commandObjects.xack(key, group, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> xgroupCreate(String key, String groupName, StreamEntryID id, boolean makeStream) {
/* 1422 */     return appendCommand(this.commandObjects.xgroupCreate(key, groupName, id, makeStream));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> xgroupSetID(String key, String groupName, StreamEntryID id) {
/* 1427 */     return appendCommand(this.commandObjects.xgroupSetID(key, groupName, id));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xgroupDestroy(String key, String groupName) {
/* 1432 */     return appendCommand(this.commandObjects.xgroupDestroy(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> xgroupCreateConsumer(String key, String groupName, String consumerName) {
/* 1437 */     return appendCommand(this.commandObjects.xgroupCreateConsumer(key, groupName, consumerName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xgroupDelConsumer(String key, String groupName, String consumerName) {
/* 1442 */     return appendCommand(this.commandObjects.xgroupDelConsumer(key, groupName, consumerName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<StreamPendingSummary> xpending(String key, String groupName) {
/* 1447 */     return appendCommand(this.commandObjects.xpending(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamPendingEntry>> xpending(String key, String groupName, XPendingParams params) {
/* 1452 */     return appendCommand(this.commandObjects.xpending(key, groupName, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xdel(String key, StreamEntryID... ids) {
/* 1457 */     return appendCommand(this.commandObjects.xdel(key, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xtrim(String key, long maxLen, boolean approximate) {
/* 1462 */     return appendCommand(this.commandObjects.xtrim(key, maxLen, approximate));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xtrim(String key, XTrimParams params) {
/* 1467 */     return appendCommand(this.commandObjects.xtrim(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntry>> xclaim(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 1472 */     return appendCommand(this.commandObjects.xclaim(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamEntryID>> xclaimJustId(String key, String group, String consumerName, long minIdleTime, XClaimParams params, StreamEntryID... ids) {
/* 1477 */     return appendCommand(this.commandObjects.xclaimJustId(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map.Entry<StreamEntryID, List<StreamEntry>>> xautoclaim(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 1482 */     return appendCommand(this.commandObjects.xautoclaim(key, group, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map.Entry<StreamEntryID, List<StreamEntryID>>> xautoclaimJustId(String key, String group, String consumerName, long minIdleTime, StreamEntryID start, XAutoClaimParams params) {
/* 1487 */     return appendCommand(this.commandObjects.xautoclaimJustId(key, group, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<StreamInfo> xinfoStream(String key) {
/* 1492 */     return appendCommand(this.commandObjects.xinfoStream(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<StreamFullInfo> xinfoStreamFull(String key) {
/* 1497 */     return appendCommand(this.commandObjects.xinfoStreamFull(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<StreamFullInfo> xinfoStreamFull(String key, int count) {
/* 1502 */     return appendCommand(this.commandObjects.xinfoStreamFull(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamGroupInfo>> xinfoGroups(String key) {
/* 1507 */     return appendCommand(this.commandObjects.xinfoGroups(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamConsumersInfo>> xinfoConsumers(String key, String group) {
/* 1512 */     return appendCommand(this.commandObjects.xinfoConsumers(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<StreamConsumerInfo>> xinfoConsumers2(String key, String group) {
/* 1517 */     return appendCommand(this.commandObjects.xinfoConsumers2(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Map.Entry<String, List<StreamEntry>>>> xread(XReadParams xReadParams, Map<String, StreamEntryID> streams) {
/* 1522 */     return appendCommand(this.commandObjects.xread(xReadParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Map.Entry<String, List<StreamEntry>>>> xreadGroup(String groupName, String consumer, XReadGroupParams xReadGroupParams, Map<String, StreamEntryID> streams) {
/* 1527 */     return appendCommand(this.commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(String script) {
/* 1532 */     return appendCommand(this.commandObjects.eval(script));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(String script, int keyCount, String... params) {
/* 1537 */     return appendCommand(this.commandObjects.eval(script, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(String script, List<String> keys, List<String> args) {
/* 1542 */     return appendCommand(this.commandObjects.eval(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalReadonly(String script, List<String> keys, List<String> args) {
/* 1547 */     return appendCommand(this.commandObjects.evalReadonly(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(String sha1) {
/* 1552 */     return appendCommand(this.commandObjects.evalsha(sha1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(String sha1, int keyCount, String... params) {
/* 1557 */     return appendCommand(this.commandObjects.evalsha(sha1, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(String sha1, List<String> keys, List<String> args) {
/* 1562 */     return appendCommand(this.commandObjects.evalsha(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalshaReadonly(String sha1, List<String> keys, List<String> args) {
/* 1567 */     return appendCommand(this.commandObjects.evalshaReadonly(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> waitReplicas(String sampleKey, int replicas, long timeout) {
/* 1572 */     return appendCommand(this.commandObjects.waitReplicas(sampleKey, replicas, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<Long, Long>> waitAOF(String sampleKey, long numLocal, long numReplicas, long timeout) {
/* 1577 */     return appendCommand(this.commandObjects.waitAOF(sampleKey, numLocal, numReplicas, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(String script, String sampleKey) {
/* 1582 */     return appendCommand(this.commandObjects.eval(script, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(String sha1, String sampleKey) {
/* 1587 */     return appendCommand(this.commandObjects.evalsha(sha1, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> scriptExists(String sampleKey, String... sha1) {
/* 1592 */     return appendCommand(this.commandObjects.scriptExists(sampleKey, sha1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> scriptLoad(String script, String sampleKey) {
/* 1597 */     return appendCommand(this.commandObjects.scriptLoad(script, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> scriptFlush(String sampleKey) {
/* 1602 */     return appendCommand(this.commandObjects.scriptFlush(sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> scriptFlush(String sampleKey, FlushMode flushMode) {
/* 1607 */     return appendCommand(this.commandObjects.scriptFlush(sampleKey, flushMode));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> scriptKill(String sampleKey) {
/* 1612 */     return appendCommand(this.commandObjects.scriptKill(sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> fcall(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 1617 */     return appendCommand(this.commandObjects.fcall(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> fcall(String name, List<String> keys, List<String> args) {
/* 1622 */     return appendCommand(this.commandObjects.fcall(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> fcallReadonly(byte[] name, List<byte[]> keys, List<byte[]> args) {
/* 1627 */     return appendCommand(this.commandObjects.fcallReadonly(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> fcallReadonly(String name, List<String> keys, List<String> args) {
/* 1632 */     return appendCommand(this.commandObjects.fcallReadonly(name, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionDelete(byte[] libraryName) {
/* 1637 */     return appendCommand(this.commandObjects.functionDelete(libraryName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionDelete(String libraryName) {
/* 1642 */     return appendCommand(this.commandObjects.functionDelete(libraryName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> functionDump() {
/* 1647 */     return (Response)appendCommand((CommandObject)this.commandObjects.functionDump());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<LibraryInfo>> functionList(String libraryNamePattern) {
/* 1652 */     return appendCommand(this.commandObjects.functionList(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<LibraryInfo>> functionList() {
/* 1657 */     return appendCommand(this.commandObjects.functionList());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<LibraryInfo>> functionListWithCode(String libraryNamePattern) {
/* 1662 */     return appendCommand(this.commandObjects.functionListWithCode(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<LibraryInfo>> functionListWithCode() {
/* 1667 */     return appendCommand(this.commandObjects.functionListWithCode());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> functionListBinary() {
/* 1672 */     return appendCommand(this.commandObjects.functionListBinary());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> functionList(byte[] libraryNamePattern) {
/* 1677 */     return appendCommand(this.commandObjects.functionList(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> functionListWithCodeBinary() {
/* 1682 */     return appendCommand(this.commandObjects.functionListWithCodeBinary());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> functionListWithCode(byte[] libraryNamePattern) {
/* 1687 */     return appendCommand(this.commandObjects.functionListWithCode(libraryNamePattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionLoad(byte[] functionCode) {
/* 1692 */     return appendCommand(this.commandObjects.functionLoad(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionLoad(String functionCode) {
/* 1697 */     return appendCommand(this.commandObjects.functionLoad(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionLoadReplace(byte[] functionCode) {
/* 1702 */     return appendCommand(this.commandObjects.functionLoadReplace(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionLoadReplace(String functionCode) {
/* 1707 */     return appendCommand(this.commandObjects.functionLoadReplace(functionCode));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionRestore(byte[] serializedValue) {
/* 1712 */     return appendCommand(this.commandObjects.functionRestore(serializedValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionRestore(byte[] serializedValue, FunctionRestorePolicy policy) {
/* 1717 */     return appendCommand(this.commandObjects.functionRestore(serializedValue, policy));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionFlush() {
/* 1722 */     return appendCommand(this.commandObjects.functionFlush());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionFlush(FlushMode mode) {
/* 1727 */     return appendCommand(this.commandObjects.functionFlush(mode));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> functionKill() {
/* 1732 */     return appendCommand(this.commandObjects.functionKill());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<FunctionStats> functionStats() {
/* 1737 */     return appendCommand(this.commandObjects.functionStats());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> functionStatsBinary() {
/* 1742 */     return appendCommand(this.commandObjects.functionStatsBinary());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geoadd(byte[] key, double longitude, double latitude, byte[] member) {
/* 1747 */     return appendCommand(this.commandObjects.geoadd(key, longitude, latitude, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geoadd(byte[] key, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 1752 */     return appendCommand(this.commandObjects.geoadd(key, memberCoordinateMap));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geoadd(byte[] key, GeoAddParams params, Map<byte[], GeoCoordinate> memberCoordinateMap) {
/* 1757 */     return appendCommand(this.commandObjects.geoadd(key, params, memberCoordinateMap));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> geodist(byte[] key, byte[] member1, byte[] member2) {
/* 1762 */     return appendCommand(this.commandObjects.geodist(key, member1, member2));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> geodist(byte[] key, byte[] member1, byte[] member2, GeoUnit unit) {
/* 1767 */     return appendCommand(this.commandObjects.geodist(key, member1, member2, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> geohash(byte[] key, byte[]... members) {
/* 1772 */     return appendCommand(this.commandObjects.geohash(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoCoordinate>> geopos(byte[] key, byte[]... members) {
/* 1777 */     return appendCommand(this.commandObjects.geopos(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 1782 */     return appendCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit) {
/* 1787 */     return appendCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadius(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1792 */     return appendCommand(this.commandObjects.georadius(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusReadonly(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1797 */     return appendCommand(this.commandObjects.georadiusReadonly(key, longitude, latitude, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 1802 */     return appendCommand(this.commandObjects.georadiusByMember(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 1807 */     return appendCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMember(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1812 */     return appendCommand(this.commandObjects.georadiusByMember(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> georadiusByMemberReadonly(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
/* 1817 */     return appendCommand(this.commandObjects.georadiusByMemberReadonly(key, member, radius, unit, param));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> georadiusStore(byte[] key, double longitude, double latitude, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 1822 */     return appendCommand(this.commandObjects.georadiusStore(key, longitude, latitude, radius, unit, param, storeParam));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> georadiusByMemberStore(byte[] key, byte[] member, double radius, GeoUnit unit, GeoRadiusParam param, GeoRadiusStoreParam storeParam) {
/* 1827 */     return appendCommand(this.commandObjects.georadiusByMemberStore(key, member, radius, unit, param, storeParam));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(byte[] key, byte[] member, double radius, GeoUnit unit) {
/* 1832 */     return appendCommand(this.commandObjects.geosearch(key, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(byte[] key, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 1837 */     return appendCommand(this.commandObjects.geosearch(key, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(byte[] key, byte[] member, double width, double height, GeoUnit unit) {
/* 1842 */     return appendCommand(this.commandObjects.geosearch(key, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(byte[] key, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 1847 */     return appendCommand(this.commandObjects.geosearch(key, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<GeoRadiusResponse>> geosearch(byte[] key, GeoSearchParam params) {
/* 1852 */     return appendCommand(this.commandObjects.geosearch(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(byte[] dest, byte[] src, byte[] member, double radius, GeoUnit unit) {
/* 1857 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, member, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double radius, GeoUnit unit) {
/* 1862 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, coord, radius, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(byte[] dest, byte[] src, byte[] member, double width, double height, GeoUnit unit) {
/* 1867 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, member, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(byte[] dest, byte[] src, GeoCoordinate coord, double width, double height, GeoUnit unit) {
/* 1872 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, coord, width, height, unit));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStore(byte[] dest, byte[] src, GeoSearchParam params) {
/* 1877 */     return appendCommand(this.commandObjects.geosearchStore(dest, src, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> geosearchStoreStoreDist(byte[] dest, byte[] src, GeoSearchParam params) {
/* 1882 */     return appendCommand(this.commandObjects.geosearchStoreStoreDist(dest, src, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hset(byte[] key, byte[] field, byte[] value) {
/* 1887 */     return appendCommand(this.commandObjects.hset(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hset(byte[] key, Map<byte[], byte[]> hash) {
/* 1892 */     return appendCommand(this.commandObjects.hset(key, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> hget(byte[] key, byte[] field) {
/* 1897 */     return (Response)appendCommand((CommandObject)this.commandObjects.hget(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hsetnx(byte[] key, byte[] field, byte[] value) {
/* 1902 */     return appendCommand(this.commandObjects.hsetnx(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> hmset(byte[] key, Map<byte[], byte[]> hash) {
/* 1907 */     return appendCommand(this.commandObjects.hmset(key, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> hmget(byte[] key, byte[]... fields) {
/* 1912 */     return appendCommand(this.commandObjects.hmget(key, fields));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hincrBy(byte[] key, byte[] field, long value) {
/* 1917 */     return appendCommand(this.commandObjects.hincrBy(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> hincrByFloat(byte[] key, byte[] field, double value) {
/* 1922 */     return appendCommand(this.commandObjects.hincrByFloat(key, field, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> hexists(byte[] key, byte[] field) {
/* 1927 */     return appendCommand(this.commandObjects.hexists(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hdel(byte[] key, byte[]... field) {
/* 1932 */     return appendCommand(this.commandObjects.hdel(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hlen(byte[] key) {
/* 1937 */     return appendCommand(this.commandObjects.hlen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<byte[]>> hkeys(byte[] key) {
/* 1942 */     return appendCommand(this.commandObjects.hkeys(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> hvals(byte[] key) {
/* 1947 */     return appendCommand(this.commandObjects.hvals(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<byte[], byte[]>> hgetAll(byte[] key) {
/* 1952 */     return appendCommand(this.commandObjects.hgetAll(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> hrandfield(byte[] key) {
/* 1957 */     return (Response)appendCommand((CommandObject)this.commandObjects.hrandfield(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> hrandfield(byte[] key, long count) {
/* 1962 */     return appendCommand(this.commandObjects.hrandfield(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Map.Entry<byte[], byte[]>>> hrandfieldWithValues(byte[] key, long count) {
/* 1967 */     return appendCommand(this.commandObjects.hrandfieldWithValues(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<Map.Entry<byte[], byte[]>>> hscan(byte[] key, byte[] cursor, ScanParams params) {
/* 1972 */     return appendCommand(this.commandObjects.hscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> hstrlen(byte[] key, byte[] field) {
/* 1977 */     return appendCommand(this.commandObjects.hstrlen(key, field));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pfadd(byte[] key, byte[]... elements) {
/* 1982 */     return appendCommand(this.commandObjects.pfadd(key, elements));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> pfmerge(byte[] destkey, byte[]... sourcekeys) {
/* 1987 */     return appendCommand(this.commandObjects.pfmerge(destkey, sourcekeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pfcount(byte[] key) {
/* 1992 */     return appendCommand(this.commandObjects.pfcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pfcount(byte[]... keys) {
/* 1997 */     return appendCommand(this.commandObjects.pfcount(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> exists(byte[] key) {
/* 2002 */     return appendCommand(this.commandObjects.exists(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> exists(byte[]... keys) {
/* 2007 */     return appendCommand(this.commandObjects.exists(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> persist(byte[] key) {
/* 2012 */     return appendCommand(this.commandObjects.persist(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> type(byte[] key) {
/* 2017 */     return appendCommand(this.commandObjects.type(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> dump(byte[] key) {
/* 2022 */     return (Response)appendCommand((CommandObject)this.commandObjects.dump(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> restore(byte[] key, long ttl, byte[] serializedValue) {
/* 2027 */     return appendCommand(this.commandObjects.restore(key, ttl, serializedValue));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> restore(byte[] key, long ttl, byte[] serializedValue, RestoreParams params) {
/* 2032 */     return appendCommand(this.commandObjects.restore(key, ttl, serializedValue, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expire(byte[] key, long seconds) {
/* 2037 */     return appendCommand(this.commandObjects.expire(key, seconds));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expire(byte[] key, long seconds, ExpiryOption expiryOption) {
/* 2042 */     return appendCommand(this.commandObjects.expire(key, seconds, expiryOption));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpire(byte[] key, long milliseconds) {
/* 2047 */     return appendCommand(this.commandObjects.pexpire(key, milliseconds));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpire(byte[] key, long milliseconds, ExpiryOption expiryOption) {
/* 2052 */     return appendCommand(this.commandObjects.pexpire(key, milliseconds, expiryOption));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expireTime(byte[] key) {
/* 2057 */     return appendCommand(this.commandObjects.expireTime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpireTime(byte[] key) {
/* 2062 */     return appendCommand(this.commandObjects.pexpireTime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expireAt(byte[] key, long unixTime) {
/* 2067 */     return appendCommand(this.commandObjects.expireAt(key, unixTime));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> expireAt(byte[] key, long unixTime, ExpiryOption expiryOption) {
/* 2072 */     return appendCommand(this.commandObjects.expireAt(key, unixTime));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpireAt(byte[] key, long millisecondsTimestamp) {
/* 2077 */     return appendCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pexpireAt(byte[] key, long millisecondsTimestamp, ExpiryOption expiryOption) {
/* 2082 */     return appendCommand(this.commandObjects.pexpireAt(key, millisecondsTimestamp, expiryOption));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ttl(byte[] key) {
/* 2087 */     return appendCommand(this.commandObjects.ttl(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> pttl(byte[] key) {
/* 2092 */     return appendCommand(this.commandObjects.pttl(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> touch(byte[] key) {
/* 2097 */     return appendCommand(this.commandObjects.touch(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> touch(byte[]... keys) {
/* 2102 */     return appendCommand(this.commandObjects.touch(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> sort(byte[] key) {
/* 2107 */     return appendCommand(this.commandObjects.sort(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> sort(byte[] key, SortingParams sortingParams) {
/* 2112 */     return appendCommand(this.commandObjects.sort(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> sortReadonly(byte[] key, SortingParams sortingParams) {
/* 2117 */     return appendCommand(this.commandObjects.sortReadonly(key, sortingParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> del(byte[] key) {
/* 2122 */     return appendCommand(this.commandObjects.del(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> del(byte[]... keys) {
/* 2127 */     return appendCommand(this.commandObjects.del(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> unlink(byte[] key) {
/* 2132 */     return appendCommand(this.commandObjects.unlink(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> unlink(byte[]... keys) {
/* 2137 */     return appendCommand(this.commandObjects.unlink(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> copy(byte[] srcKey, byte[] dstKey, boolean replace) {
/* 2142 */     return appendCommand(this.commandObjects.copy(srcKey, dstKey, replace));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> rename(byte[] oldkey, byte[] newkey) {
/* 2147 */     return appendCommand(this.commandObjects.rename(oldkey, newkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> renamenx(byte[] oldkey, byte[] newkey) {
/* 2152 */     return appendCommand(this.commandObjects.renamenx(oldkey, newkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sort(byte[] key, SortingParams sortingParams, byte[] dstkey) {
/* 2157 */     return appendCommand(this.commandObjects.sort(key, sortingParams, dstkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sort(byte[] key, byte[] dstkey) {
/* 2162 */     return appendCommand(this.commandObjects.sort(key, dstkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> memoryUsage(byte[] key) {
/* 2167 */     return appendCommand(this.commandObjects.memoryUsage(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> memoryUsage(byte[] key, int samples) {
/* 2172 */     return appendCommand(this.commandObjects.memoryUsage(key, samples));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> objectRefcount(byte[] key) {
/* 2177 */     return appendCommand(this.commandObjects.objectRefcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> objectEncoding(byte[] key) {
/* 2182 */     return (Response)appendCommand((CommandObject)this.commandObjects.objectEncoding(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> objectIdletime(byte[] key) {
/* 2187 */     return appendCommand(this.commandObjects.objectIdletime(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> objectFreq(byte[] key) {
/* 2192 */     return appendCommand(this.commandObjects.objectFreq(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> migrate(String host, int port, byte[] key, int timeout) {
/* 2197 */     return appendCommand(this.commandObjects.migrate(host, port, key, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> migrate(String host, int port, int timeout, MigrateParams params, byte[]... keys) {
/* 2202 */     return appendCommand(this.commandObjects.migrate(host, port, timeout, params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<byte[]>> keys(byte[] pattern) {
/* 2207 */     return appendCommand(this.commandObjects.keys(pattern));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<byte[]>> scan(byte[] cursor) {
/* 2212 */     return appendCommand(this.commandObjects.scan(cursor));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params) {
/* 2217 */     return appendCommand(this.commandObjects.scan(cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<byte[]>> scan(byte[] cursor, ScanParams params, byte[] type) {
/* 2222 */     return appendCommand(this.commandObjects.scan(cursor, params, type));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> randomBinaryKey() {
/* 2227 */     return (Response)appendCommand((CommandObject)this.commandObjects.randomBinaryKey());
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> rpush(byte[] key, byte[]... args) {
/* 2232 */     return appendCommand(this.commandObjects.rpush(key, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lpush(byte[] key, byte[]... args) {
/* 2237 */     return appendCommand(this.commandObjects.lpush(key, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> llen(byte[] key) {
/* 2242 */     return appendCommand(this.commandObjects.llen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> lrange(byte[] key, long start, long stop) {
/* 2247 */     return appendCommand(this.commandObjects.lrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ltrim(byte[] key, long start, long stop) {
/* 2252 */     return appendCommand(this.commandObjects.ltrim(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> lindex(byte[] key, long index) {
/* 2257 */     return (Response)appendCommand((CommandObject)this.commandObjects.lindex(key, index));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> lset(byte[] key, long index, byte[] value) {
/* 2262 */     return appendCommand(this.commandObjects.lset(key, index, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lrem(byte[] key, long count, byte[] value) {
/* 2267 */     return appendCommand(this.commandObjects.lrem(key, count, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> lpop(byte[] key) {
/* 2272 */     return (Response)appendCommand((CommandObject)this.commandObjects.lpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> lpop(byte[] key, int count) {
/* 2277 */     return appendCommand(this.commandObjects.lpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lpos(byte[] key, byte[] element) {
/* 2282 */     return appendCommand(this.commandObjects.lpos(key, element));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lpos(byte[] key, byte[] element, LPosParams params) {
/* 2287 */     return appendCommand(this.commandObjects.lpos(key, element, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> lpos(byte[] key, byte[] element, LPosParams params, long count) {
/* 2292 */     return appendCommand(this.commandObjects.lpos(key, element, params, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> rpop(byte[] key) {
/* 2297 */     return (Response)appendCommand((CommandObject)this.commandObjects.rpop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> rpop(byte[] key, int count) {
/* 2302 */     return appendCommand(this.commandObjects.rpop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> linsert(byte[] key, ListPosition where, byte[] pivot, byte[] value) {
/* 2307 */     return appendCommand(this.commandObjects.linsert(key, where, pivot, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> lpushx(byte[] key, byte[]... args) {
/* 2312 */     return appendCommand(this.commandObjects.lpushx(key, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> rpushx(byte[] key, byte[]... args) {
/* 2317 */     return appendCommand(this.commandObjects.rpushx(key, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> blpop(int timeout, byte[]... keys) {
/* 2322 */     return appendCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], byte[]>> blpop(double timeout, byte[]... keys) {
/* 2327 */     return appendCommand(this.commandObjects.blpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> brpop(int timeout, byte[]... keys) {
/* 2332 */     return appendCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], byte[]>> brpop(double timeout, byte[]... keys) {
/* 2337 */     return appendCommand(this.commandObjects.brpop(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> rpoplpush(byte[] srckey, byte[] dstkey) {
/* 2342 */     return (Response)appendCommand((CommandObject)this.commandObjects.rpoplpush(srckey, dstkey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> brpoplpush(byte[] source, byte[] destination, int timeout) {
/* 2347 */     return (Response)appendCommand((CommandObject)this.commandObjects.brpoplpush(source, destination, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> lmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to) {
/* 2352 */     return (Response)appendCommand((CommandObject)this.commandObjects.lmove(srcKey, dstKey, from, to));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> blmove(byte[] srcKey, byte[] dstKey, ListDirection from, ListDirection to, double timeout) {
/* 2357 */     return (Response)appendCommand((CommandObject)this.commandObjects.blmove(srcKey, dstKey, from, to, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<byte[]>>> lmpop(ListDirection direction, byte[]... keys) {
/* 2362 */     return appendCommand(this.commandObjects.lmpop(direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<byte[]>>> lmpop(ListDirection direction, int count, byte[]... keys) {
/* 2367 */     return appendCommand(this.commandObjects.lmpop(direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<byte[]>>> blmpop(double timeout, ListDirection direction, byte[]... keys) {
/* 2372 */     return appendCommand(this.commandObjects.blmpop(timeout, direction, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<byte[]>>> blmpop(double timeout, ListDirection direction, int count, byte[]... keys) {
/* 2377 */     return appendCommand(this.commandObjects.blmpop(timeout, direction, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> waitReplicas(byte[] sampleKey, int replicas, long timeout) {
/* 2382 */     return appendCommand(this.commandObjects.waitReplicas(sampleKey, replicas, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<Long, Long>> waitAOF(byte[] sampleKey, long numLocal, long numReplicas, long timeout) {
/* 2387 */     return appendCommand(this.commandObjects.waitAOF(sampleKey, numLocal, numReplicas, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(byte[] script, byte[] sampleKey) {
/* 2392 */     return appendCommand(this.commandObjects.eval(script, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(byte[] sha1, byte[] sampleKey) {
/* 2397 */     return appendCommand(this.commandObjects.evalsha(sha1, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> scriptExists(byte[] sampleKey, byte[]... sha1s) {
/* 2402 */     return appendCommand(this.commandObjects.scriptExists(sampleKey, sha1s));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> scriptLoad(byte[] script, byte[] sampleKey) {
/* 2407 */     return (Response)appendCommand((CommandObject)this.commandObjects.scriptLoad(script, sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> scriptFlush(byte[] sampleKey) {
/* 2412 */     return appendCommand(this.commandObjects.scriptFlush(sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> scriptFlush(byte[] sampleKey, FlushMode flushMode) {
/* 2417 */     return appendCommand(this.commandObjects.scriptFlush(sampleKey, flushMode));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> scriptKill(byte[] sampleKey) {
/* 2422 */     return appendCommand(this.commandObjects.scriptKill(sampleKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(byte[] script) {
/* 2427 */     return appendCommand(this.commandObjects.eval(script));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(byte[] script, int keyCount, byte[]... params) {
/* 2432 */     return appendCommand(this.commandObjects.eval(script, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 2437 */     return appendCommand(this.commandObjects.eval(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalReadonly(byte[] script, List<byte[]> keys, List<byte[]> args) {
/* 2442 */     return appendCommand(this.commandObjects.evalReadonly(script, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(byte[] sha1) {
/* 2447 */     return appendCommand(this.commandObjects.evalsha(sha1));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(byte[] sha1, int keyCount, byte[]... params) {
/* 2452 */     return appendCommand(this.commandObjects.evalsha(sha1, keyCount, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 2457 */     return appendCommand(this.commandObjects.evalsha(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> evalshaReadonly(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
/* 2462 */     return appendCommand(this.commandObjects.evalshaReadonly(sha1, keys, args));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sadd(byte[] key, byte[]... members) {
/* 2467 */     return appendCommand(this.commandObjects.sadd(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<byte[]>> smembers(byte[] key) {
/* 2472 */     return appendCommand(this.commandObjects.smembers(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> srem(byte[] key, byte[]... members) {
/* 2477 */     return appendCommand(this.commandObjects.srem(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> spop(byte[] key) {
/* 2482 */     return (Response)appendCommand((CommandObject)this.commandObjects.spop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<byte[]>> spop(byte[] key, long count) {
/* 2487 */     return appendCommand(this.commandObjects.spop(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> scard(byte[] key) {
/* 2492 */     return appendCommand(this.commandObjects.scard(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> sismember(byte[] key, byte[] member) {
/* 2497 */     return appendCommand(this.commandObjects.sismember(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> smismember(byte[] key, byte[]... members) {
/* 2502 */     return appendCommand(this.commandObjects.smismember(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> srandmember(byte[] key) {
/* 2507 */     return (Response)appendCommand((CommandObject)this.commandObjects.srandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> srandmember(byte[] key, int count) {
/* 2512 */     return appendCommand(this.commandObjects.srandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<byte[]>> sscan(byte[] key, byte[] cursor, ScanParams params) {
/* 2517 */     return appendCommand(this.commandObjects.sscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<byte[]>> sdiff(byte[]... keys) {
/* 2522 */     return appendCommand(this.commandObjects.sdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sdiffstore(byte[] dstkey, byte[]... keys) {
/* 2527 */     return appendCommand(this.commandObjects.sdiffstore(dstkey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<byte[]>> sinter(byte[]... keys) {
/* 2532 */     return appendCommand(this.commandObjects.sinter(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sinterstore(byte[] dstkey, byte[]... keys) {
/* 2537 */     return appendCommand(this.commandObjects.sinterstore(dstkey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sintercard(byte[]... keys) {
/* 2542 */     return appendCommand(this.commandObjects.sintercard(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sintercard(int limit, byte[]... keys) {
/* 2547 */     return appendCommand(this.commandObjects.sintercard(limit, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<byte[]>> sunion(byte[]... keys) {
/* 2552 */     return appendCommand(this.commandObjects.sunion(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> sunionstore(byte[] dstkey, byte[]... keys) {
/* 2557 */     return appendCommand(this.commandObjects.sunionstore(dstkey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> smove(byte[] srckey, byte[] dstkey, byte[] member) {
/* 2562 */     return appendCommand(this.commandObjects.smove(srckey, dstkey, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(byte[] key, double score, byte[] member) {
/* 2567 */     return appendCommand(this.commandObjects.zadd(key, score, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(byte[] key, double score, byte[] member, ZAddParams params) {
/* 2572 */     return appendCommand(this.commandObjects.zadd(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers) {
/* 2577 */     return appendCommand(this.commandObjects.zadd(key, scoreMembers));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zadd(byte[] key, Map<byte[], Double> scoreMembers, ZAddParams params) {
/* 2582 */     return appendCommand(this.commandObjects.zadd(key, scoreMembers, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zaddIncr(byte[] key, double score, byte[] member, ZAddParams params) {
/* 2587 */     return appendCommand(this.commandObjects.zaddIncr(key, score, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrem(byte[] key, byte[]... members) {
/* 2592 */     return appendCommand(this.commandObjects.zrem(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zincrby(byte[] key, double increment, byte[] member) {
/* 2597 */     return appendCommand(this.commandObjects.zincrby(key, increment, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zincrby(byte[] key, double increment, byte[] member, ZIncrByParams params) {
/* 2602 */     return appendCommand(this.commandObjects.zincrby(key, increment, member, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrank(byte[] key, byte[] member) {
/* 2607 */     return appendCommand(this.commandObjects.zrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrevrank(byte[] key, byte[] member) {
/* 2612 */     return appendCommand(this.commandObjects.zrevrank(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<Long, Double>> zrankWithScore(byte[] key, byte[] member) {
/* 2617 */     return appendCommand(this.commandObjects.zrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<Long, Double>> zrevrankWithScore(byte[] key, byte[] member) {
/* 2622 */     return appendCommand(this.commandObjects.zrevrankWithScore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrange(byte[] key, long start, long stop) {
/* 2627 */     return appendCommand(this.commandObjects.zrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrevrange(byte[] key, long start, long stop) {
/* 2632 */     return appendCommand(this.commandObjects.zrevrange(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeWithScores(byte[] key, long start, long stop) {
/* 2637 */     return appendCommand(this.commandObjects.zrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeWithScores(byte[] key, long start, long stop) {
/* 2642 */     return appendCommand(this.commandObjects.zrevrangeWithScores(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> zrandmember(byte[] key) {
/* 2647 */     return (Response)appendCommand((CommandObject)this.commandObjects.zrandmember(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrandmember(byte[] key, long count) {
/* 2652 */     return appendCommand(this.commandObjects.zrandmember(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrandmemberWithScores(byte[] key, long count) {
/* 2657 */     return appendCommand(this.commandObjects.zrandmemberWithScores(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zcard(byte[] key) {
/* 2662 */     return appendCommand(this.commandObjects.zcard(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> zscore(byte[] key, byte[] member) {
/* 2667 */     return appendCommand(this.commandObjects.zscore(key, member));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Double>> zmscore(byte[] key, byte[]... members) {
/* 2672 */     return appendCommand(this.commandObjects.zmscore(key, members));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Tuple> zpopmax(byte[] key) {
/* 2677 */     return appendCommand(this.commandObjects.zpopmax(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zpopmax(byte[] key, int count) {
/* 2682 */     return appendCommand(this.commandObjects.zpopmax(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Tuple> zpopmin(byte[] key) {
/* 2687 */     return appendCommand(this.commandObjects.zpopmin(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zpopmin(byte[] key, int count) {
/* 2692 */     return appendCommand(this.commandObjects.zpopmin(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zcount(byte[] key, double min, double max) {
/* 2697 */     return appendCommand(this.commandObjects.zcount(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zcount(byte[] key, byte[] min, byte[] max) {
/* 2702 */     return appendCommand(this.commandObjects.zcount(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrangeByScore(byte[] key, double min, double max) {
/* 2707 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 2712 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrevrangeByScore(byte[] key, double max, double min) {
/* 2717 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrangeByScore(byte[] key, double min, double max, int offset, int count) {
/* 2722 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
/* 2727 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrangeByScore(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2732 */     return appendCommand(this.commandObjects.zrangeByScore(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrevrangeByScore(byte[] key, double max, double min, int offset, int count) {
/* 2737 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max) {
/* 2742 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min) {
/* 2747 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(byte[] key, double min, double max, int offset, int count) {
/* 2752 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrevrangeByScore(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 2757 */     return appendCommand(this.commandObjects.zrevrangeByScore(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
/* 2762 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min) {
/* 2767 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2772 */     return appendCommand(this.commandObjects.zrangeByScoreWithScores(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, double max, double min, int offset, int count) {
/* 2777 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrevrangeByScoreWithScores(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 2782 */     return appendCommand(this.commandObjects.zrevrangeByScoreWithScores(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByRank(byte[] key, long start, long stop) {
/* 2787 */     return appendCommand(this.commandObjects.zremrangeByRank(key, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByScore(byte[] key, double min, double max) {
/* 2792 */     return appendCommand(this.commandObjects.zremrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByScore(byte[] key, byte[] min, byte[] max) {
/* 2797 */     return appendCommand(this.commandObjects.zremrangeByScore(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zlexcount(byte[] key, byte[] min, byte[] max) {
/* 2802 */     return appendCommand(this.commandObjects.zlexcount(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 2807 */     return appendCommand(this.commandObjects.zrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrangeByLex(byte[] key, byte[] min, byte[] max, int offset, int count) {
/* 2812 */     return appendCommand(this.commandObjects.zrangeByLex(key, min, max, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
/* 2817 */     return appendCommand(this.commandObjects.zrevrangeByLex(key, max, min));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrevrangeByLex(byte[] key, byte[] max, byte[] min, int offset, int count) {
/* 2822 */     return appendCommand(this.commandObjects.zrevrangeByLex(key, max, min, offset, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zrange(byte[] key, ZRangeParams zRangeParams) {
/* 2827 */     return appendCommand(this.commandObjects.zrange(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zrangeWithScores(byte[] key, ZRangeParams zRangeParams) {
/* 2832 */     return appendCommand(this.commandObjects.zrangeWithScores(key, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zrangestore(byte[] dest, byte[] src, ZRangeParams zRangeParams) {
/* 2837 */     return appendCommand(this.commandObjects.zrangestore(dest, src, zRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zremrangeByLex(byte[] key, byte[] min, byte[] max) {
/* 2842 */     return appendCommand(this.commandObjects.zremrangeByLex(key, min, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ScanResult<Tuple>> zscan(byte[] key, byte[] cursor, ScanParams params) {
/* 2847 */     return appendCommand(this.commandObjects.zscan(key, cursor, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], Tuple>> bzpopmax(double timeout, byte[]... keys) {
/* 2852 */     return appendCommand(this.commandObjects.bzpopmax(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], Tuple>> bzpopmin(double timeout, byte[]... keys) {
/* 2857 */     return appendCommand(this.commandObjects.bzpopmin(timeout, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<Tuple>>> zmpop(SortedSetOption option, byte[]... keys) {
/* 2862 */     return appendCommand(this.commandObjects.zmpop(option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<Tuple>>> zmpop(SortedSetOption option, int count, byte[]... keys) {
/* 2867 */     return appendCommand(this.commandObjects.zmpop(option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<Tuple>>> bzmpop(double timeout, SortedSetOption option, byte[]... keys) {
/* 2872 */     return appendCommand(this.commandObjects.bzmpop(timeout, option, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<KeyValue<byte[], List<Tuple>>> bzmpop(double timeout, SortedSetOption option, int count, byte[]... keys) {
/* 2877 */     return appendCommand(this.commandObjects.bzmpop(timeout, option, count, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zdiff(byte[]... keys) {
/* 2882 */     return appendCommand(this.commandObjects.zdiff(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zdiffWithScores(byte[]... keys) {
/* 2887 */     return appendCommand(this.commandObjects.zdiffWithScores(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Response<Long> zdiffStore(byte[] dstkey, byte[]... keys) {
/* 2893 */     return appendCommand(this.commandObjects.zdiffStore(dstkey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zdiffstore(byte[] dstkey, byte[]... keys) {
/* 2898 */     return appendCommand(this.commandObjects.zdiffstore(dstkey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zinter(ZParams params, byte[]... keys) {
/* 2903 */     return appendCommand(this.commandObjects.zinter(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zinterWithScores(ZParams params, byte[]... keys) {
/* 2908 */     return appendCommand(this.commandObjects.zinterWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zinterstore(byte[] dstkey, byte[]... sets) {
/* 2913 */     return appendCommand(this.commandObjects.zinterstore(dstkey, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 2918 */     return appendCommand(this.commandObjects.zinterstore(dstkey, params, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zintercard(byte[]... keys) {
/* 2923 */     return appendCommand(this.commandObjects.zintercard(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zintercard(long limit, byte[]... keys) {
/* 2928 */     return appendCommand(this.commandObjects.zintercard(limit, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> zunion(ZParams params, byte[]... keys) {
/* 2933 */     return appendCommand(this.commandObjects.zunion(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> zunionWithScores(ZParams params, byte[]... keys) {
/* 2938 */     return appendCommand(this.commandObjects.zunionWithScores(params, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zunionstore(byte[] dstkey, byte[]... sets) {
/* 2943 */     return appendCommand(this.commandObjects.zunionstore(dstkey, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
/* 2948 */     return appendCommand(this.commandObjects.zunionstore(dstkey, params, sets));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> xadd(byte[] key, XAddParams params, Map<byte[], byte[]> hash) {
/* 2953 */     return (Response)appendCommand((CommandObject)this.commandObjects.xadd(key, params, hash));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xlen(byte[] key) {
/* 2958 */     return appendCommand(this.commandObjects.xlen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xrange(byte[] key, byte[] start, byte[] end) {
/* 2963 */     return appendCommand(this.commandObjects.xrange(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xrange(byte[] key, byte[] start, byte[] end, int count) {
/* 2968 */     return appendCommand(this.commandObjects.xrange(key, start, end, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xrevrange(byte[] key, byte[] end, byte[] start) {
/* 2973 */     return appendCommand(this.commandObjects.xrevrange(key, end, start));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xrevrange(byte[] key, byte[] end, byte[] start, int count) {
/* 2978 */     return appendCommand(this.commandObjects.xrevrange(key, end, start, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xack(byte[] key, byte[] group, byte[]... ids) {
/* 2983 */     return appendCommand(this.commandObjects.xack(key, group, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> xgroupCreate(byte[] key, byte[] groupName, byte[] id, boolean makeStream) {
/* 2988 */     return appendCommand(this.commandObjects.xgroupCreate(key, groupName, id, makeStream));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> xgroupSetID(byte[] key, byte[] groupName, byte[] id) {
/* 2993 */     return appendCommand(this.commandObjects.xgroupSetID(key, groupName, id));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xgroupDestroy(byte[] key, byte[] groupName) {
/* 2998 */     return appendCommand(this.commandObjects.xgroupDestroy(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> xgroupCreateConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 3003 */     return appendCommand(this.commandObjects.xgroupCreateConsumer(key, groupName, consumerName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xgroupDelConsumer(byte[] key, byte[] groupName, byte[] consumerName) {
/* 3008 */     return appendCommand(this.commandObjects.xgroupDelConsumer(key, groupName, consumerName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xdel(byte[] key, byte[]... ids) {
/* 3013 */     return appendCommand(this.commandObjects.xdel(key, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xtrim(byte[] key, long maxLen, boolean approximateLength) {
/* 3018 */     return appendCommand(this.commandObjects.xtrim(key, maxLen, approximateLength));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> xtrim(byte[] key, XTrimParams params) {
/* 3023 */     return appendCommand(this.commandObjects.xtrim(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> xpending(byte[] key, byte[] groupName) {
/* 3028 */     return appendCommand(this.commandObjects.xpending(key, groupName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xpending(byte[] key, byte[] groupName, XPendingParams params) {
/* 3033 */     return appendCommand(this.commandObjects.xpending(key, groupName, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> xclaim(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 3038 */     return appendCommand(this.commandObjects.xclaim(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> xclaimJustId(byte[] key, byte[] group, byte[] consumerName, long minIdleTime, XClaimParams params, byte[]... ids) {
/* 3043 */     return appendCommand(this.commandObjects.xclaimJustId(key, group, consumerName, minIdleTime, params, ids));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xautoclaim(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 3048 */     return appendCommand(this.commandObjects.xautoclaim(key, groupName, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xautoclaimJustId(byte[] key, byte[] groupName, byte[] consumerName, long minIdleTime, byte[] start, XAutoClaimParams params) {
/* 3053 */     return appendCommand(this.commandObjects.xautoclaimJustId(key, groupName, consumerName, minIdleTime, start, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> xinfoStream(byte[] key) {
/* 3058 */     return appendCommand(this.commandObjects.xinfoStream(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> xinfoStreamFull(byte[] key) {
/* 3063 */     return appendCommand(this.commandObjects.xinfoStreamFull(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> xinfoStreamFull(byte[] key, int count) {
/* 3068 */     return appendCommand(this.commandObjects.xinfoStreamFull(key, count));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xinfoGroups(byte[] key) {
/* 3073 */     return appendCommand(this.commandObjects.xinfoGroups(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xinfoConsumers(byte[] key, byte[] group) {
/* 3078 */     return appendCommand(this.commandObjects.xinfoConsumers(key, group));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xread(XReadParams xReadParams, Map.Entry<byte[], byte[]>... streams) {
/* 3083 */     return appendCommand(this.commandObjects.xread(xReadParams, streams));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Response<List<Object>> xreadGroup(byte[] groupName, byte[] consumer, XReadGroupParams xReadGroupParams, Map.Entry<byte[], byte[]>... streams) {
/* 3089 */     return appendCommand(this.commandObjects.xreadGroup(groupName, consumer, xReadGroupParams, streams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> set(byte[] key, byte[] value) {
/* 3094 */     return appendCommand(this.commandObjects.set(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> set(byte[] key, byte[] value, SetParams params) {
/* 3099 */     return appendCommand(this.commandObjects.set(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> get(byte[] key) {
/* 3104 */     return (Response)appendCommand((CommandObject)this.commandObjects.get(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> setGet(byte[] key, byte[] value, SetParams params) {
/* 3109 */     return (Response)appendCommand((CommandObject)this.commandObjects.setGet(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> getDel(byte[] key) {
/* 3114 */     return (Response)appendCommand((CommandObject)this.commandObjects.getDel(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> getEx(byte[] key, GetExParams params) {
/* 3119 */     return (Response)appendCommand((CommandObject)this.commandObjects.getEx(key, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> setbit(byte[] key, long offset, boolean value) {
/* 3124 */     return appendCommand(this.commandObjects.setbit(key, offset, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> getbit(byte[] key, long offset) {
/* 3129 */     return appendCommand(this.commandObjects.getbit(key, offset));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> setrange(byte[] key, long offset, byte[] value) {
/* 3134 */     return appendCommand(this.commandObjects.setrange(key, offset, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> getrange(byte[] key, long startOffset, long endOffset) {
/* 3139 */     return (Response)appendCommand((CommandObject)this.commandObjects.getrange(key, startOffset, endOffset));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> getSet(byte[] key, byte[] value) {
/* 3144 */     return (Response)appendCommand((CommandObject)this.commandObjects.getSet(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> setnx(byte[] key, byte[] value) {
/* 3149 */     return appendCommand(this.commandObjects.setnx(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> setex(byte[] key, long seconds, byte[] value) {
/* 3154 */     return appendCommand(this.commandObjects.setex(key, seconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> psetex(byte[] key, long milliseconds, byte[] value) {
/* 3159 */     return appendCommand(this.commandObjects.psetex(key, milliseconds, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<byte[]>> mget(byte[]... keys) {
/* 3164 */     return appendCommand(this.commandObjects.mget(keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> mset(byte[]... keysvalues) {
/* 3169 */     return appendCommand(this.commandObjects.mset(keysvalues));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> msetnx(byte[]... keysvalues) {
/* 3174 */     return appendCommand(this.commandObjects.msetnx(keysvalues));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> incr(byte[] key) {
/* 3179 */     return appendCommand(this.commandObjects.incr(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> incrBy(byte[] key, long increment) {
/* 3184 */     return appendCommand(this.commandObjects.incrBy(key, increment));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> incrByFloat(byte[] key, double increment) {
/* 3189 */     return appendCommand(this.commandObjects.incrByFloat(key, increment));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> decr(byte[] key) {
/* 3194 */     return appendCommand(this.commandObjects.decr(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> decrBy(byte[] key, long decrement) {
/* 3199 */     return appendCommand(this.commandObjects.decrBy(key, decrement));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> append(byte[] key, byte[] value) {
/* 3204 */     return appendCommand(this.commandObjects.append(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<byte[]> substr(byte[] key, int start, int end) {
/* 3209 */     return (Response)appendCommand((CommandObject)this.commandObjects.substr(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> strlen(byte[] key) {
/* 3214 */     return appendCommand(this.commandObjects.strlen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitcount(byte[] key) {
/* 3219 */     return appendCommand(this.commandObjects.bitcount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitcount(byte[] key, long start, long end) {
/* 3224 */     return appendCommand(this.commandObjects.bitcount(key, start, end));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitcount(byte[] key, long start, long end, BitCountOption option) {
/* 3229 */     return appendCommand(this.commandObjects.bitcount(key, start, end, option));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitpos(byte[] key, boolean value) {
/* 3234 */     return appendCommand(this.commandObjects.bitpos(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitpos(byte[] key, boolean value, BitPosParams params) {
/* 3239 */     return appendCommand(this.commandObjects.bitpos(key, value, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> bitfield(byte[] key, byte[]... arguments) {
/* 3244 */     return appendCommand(this.commandObjects.bitfield(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> bitfieldReadonly(byte[] key, byte[]... arguments) {
/* 3249 */     return appendCommand(this.commandObjects.bitfieldReadonly(key, arguments));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
/* 3254 */     return appendCommand(this.commandObjects.bitop(op, destKey, srcKeys));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Response<String> ftCreate(String indexName, IndexOptions indexOptions, Schema schema) {
/* 3260 */     return appendCommand(this.commandObjects.ftCreate(indexName, indexOptions, schema));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftCreate(String indexName, FTCreateParams createParams, Iterable<SchemaField> schemaFields) {
/* 3265 */     return appendCommand(this.commandObjects.ftCreate(indexName, createParams, schemaFields));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftAlter(String indexName, Schema schema) {
/* 3270 */     return appendCommand(this.commandObjects.ftAlter(indexName, schema));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftAlter(String indexName, Iterable<SchemaField> schemaFields) {
/* 3275 */     return appendCommand(this.commandObjects.ftAlter(indexName, schemaFields));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftAliasAdd(String aliasName, String indexName) {
/* 3280 */     return appendCommand(this.commandObjects.ftAliasAdd(aliasName, indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftAliasUpdate(String aliasName, String indexName) {
/* 3285 */     return appendCommand(this.commandObjects.ftAliasUpdate(aliasName, indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftAliasDel(String aliasName) {
/* 3290 */     return appendCommand(this.commandObjects.ftAliasDel(aliasName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftDropIndex(String indexName) {
/* 3295 */     return appendCommand(this.commandObjects.ftDropIndex(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftDropIndexDD(String indexName) {
/* 3300 */     return appendCommand(this.commandObjects.ftDropIndexDD(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<SearchResult> ftSearch(String indexName, String query) {
/* 3305 */     return appendCommand(this.commandObjects.ftSearch(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<SearchResult> ftSearch(String indexName, String query, FTSearchParams searchParams) {
/* 3310 */     return appendCommand(this.commandObjects.ftSearch(indexName, query, searchParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<SearchResult> ftSearch(String indexName, Query query) {
/* 3315 */     return appendCommand(this.commandObjects.ftSearch(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Response<SearchResult> ftSearch(byte[] indexName, Query query) {
/* 3321 */     return appendCommand(this.commandObjects.ftSearch(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftExplain(String indexName, Query query) {
/* 3326 */     return appendCommand(this.commandObjects.ftExplain(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> ftExplainCLI(String indexName, Query query) {
/* 3331 */     return appendCommand(this.commandObjects.ftExplainCLI(indexName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<AggregationResult> ftAggregate(String indexName, AggregationBuilder aggr) {
/* 3336 */     return appendCommand(this.commandObjects.ftAggregate(indexName, aggr));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftSynUpdate(String indexName, String synonymGroupId, String... terms) {
/* 3341 */     return appendCommand(this.commandObjects.ftSynUpdate(indexName, synonymGroupId, terms));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, List<String>>> ftSynDump(String indexName) {
/* 3346 */     return appendCommand(this.commandObjects.ftSynDump(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ftDictAdd(String dictionary, String... terms) {
/* 3351 */     return appendCommand(this.commandObjects.ftDictAdd(dictionary, terms));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ftDictDel(String dictionary, String... terms) {
/* 3356 */     return appendCommand(this.commandObjects.ftDictDel(dictionary, terms));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> ftDictDump(String dictionary) {
/* 3361 */     return appendCommand(this.commandObjects.ftDictDump(dictionary));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ftDictAddBySampleKey(String indexName, String dictionary, String... terms) {
/* 3366 */     return appendCommand(this.commandObjects.ftDictAddBySampleKey(indexName, dictionary, terms));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ftDictDelBySampleKey(String indexName, String dictionary, String... terms) {
/* 3371 */     return appendCommand(this.commandObjects.ftDictDelBySampleKey(indexName, dictionary, terms));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> ftDictDumpBySampleKey(String indexName, String dictionary) {
/* 3376 */     return appendCommand(this.commandObjects.ftDictDumpBySampleKey(indexName, dictionary));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Map<String, Double>>> ftSpellCheck(String index, String query) {
/* 3381 */     return appendCommand(this.commandObjects.ftSpellCheck(index, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Map<String, Double>>> ftSpellCheck(String index, String query, FTSpellCheckParams spellCheckParams) {
/* 3386 */     return appendCommand(this.commandObjects.ftSpellCheck(index, query, spellCheckParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> ftInfo(String indexName) {
/* 3391 */     return appendCommand(this.commandObjects.ftInfo(indexName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Set<String>> ftTagVals(String indexName, String fieldName) {
/* 3396 */     return appendCommand(this.commandObjects.ftTagVals(indexName, fieldName));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> ftConfigGet(String option) {
/* 3401 */     return appendCommand(this.commandObjects.ftConfigGet(option));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> ftConfigGet(String indexName, String option) {
/* 3406 */     return appendCommand(this.commandObjects.ftConfigGet(indexName, option));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftConfigSet(String option, String value) {
/* 3411 */     return appendCommand(this.commandObjects.ftConfigSet(option, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> ftConfigSet(String indexName, String option, String value) {
/* 3416 */     return appendCommand(this.commandObjects.ftConfigSet(indexName, option, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ftSugAdd(String key, String string, double score) {
/* 3421 */     return appendCommand(this.commandObjects.ftSugAdd(key, string, score));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ftSugAddIncr(String key, String string, double score) {
/* 3426 */     return appendCommand(this.commandObjects.ftSugAddIncr(key, string, score));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> ftSugGet(String key, String prefix) {
/* 3431 */     return appendCommand(this.commandObjects.ftSugGet(key, prefix));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> ftSugGet(String key, String prefix, boolean fuzzy, int max) {
/* 3436 */     return appendCommand(this.commandObjects.ftSugGet(key, prefix, fuzzy, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> ftSugGetWithScores(String key, String prefix) {
/* 3441 */     return appendCommand(this.commandObjects.ftSugGetWithScores(key, prefix));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Tuple>> ftSugGetWithScores(String key, String prefix, boolean fuzzy, int max) {
/* 3446 */     return appendCommand(this.commandObjects.ftSugGetWithScores(key, prefix, fuzzy, max));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> ftSugDel(String key, String string) {
/* 3451 */     return appendCommand(this.commandObjects.ftSugDel(key, string));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> ftSugLen(String key) {
/* 3456 */     return appendCommand(this.commandObjects.ftSugLen(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Response<LCSMatchResult> lcs(byte[] keyA, byte[] keyB, LCSParams params) {
/* 3463 */     return appendCommand(this.commandObjects.lcs(keyA, keyB, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonSet(String key, Path2 path, Object object) {
/* 3468 */     return appendCommand(this.commandObjects.jsonSet(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonSetWithEscape(String key, Path2 path, Object object) {
/* 3473 */     return appendCommand(this.commandObjects.jsonSetWithEscape(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonSet(String key, Path path, Object object) {
/* 3478 */     return appendCommand(this.commandObjects.jsonSet(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonSet(String key, Path2 path, Object object, JsonSetParams params) {
/* 3483 */     return appendCommand(this.commandObjects.jsonSet(key, path, object, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonSetWithEscape(String key, Path2 path, Object object, JsonSetParams params) {
/* 3488 */     return appendCommand(this.commandObjects.jsonSetWithEscape(key, path, object, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonSet(String key, Path path, Object object, JsonSetParams params) {
/* 3493 */     return appendCommand(this.commandObjects.jsonSet(key, path, object, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonMerge(String key, Path2 path, Object object) {
/* 3498 */     return appendCommand(this.commandObjects.jsonMerge(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonMerge(String key, Path path, Object object) {
/* 3503 */     return appendCommand(this.commandObjects.jsonMerge(key, path, object));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> jsonGet(String key) {
/* 3508 */     return appendCommand(this.commandObjects.jsonGet(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Response<T> jsonGet(String key, Class<T> clazz) {
/* 3513 */     return appendCommand(this.commandObjects.jsonGet(key, clazz));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> jsonGet(String key, Path2... paths) {
/* 3518 */     return appendCommand(this.commandObjects.jsonGet(key, paths));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> jsonGet(String key, Path... paths) {
/* 3523 */     return appendCommand(this.commandObjects.jsonGet(key, paths));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Response<T> jsonGet(String key, Class<T> clazz, Path... paths) {
/* 3528 */     return appendCommand(this.commandObjects.jsonGet(key, clazz, paths));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<JSONArray>> jsonMGet(Path2 path, String... keys) {
/* 3533 */     return appendCommand(this.commandObjects.jsonMGet(path, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Response<List<T>> jsonMGet(Path path, Class<T> clazz, String... keys) {
/* 3538 */     return appendCommand(this.commandObjects.jsonMGet(path, clazz, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonDel(String key) {
/* 3543 */     return appendCommand(this.commandObjects.jsonDel(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonDel(String key, Path2 path) {
/* 3548 */     return appendCommand(this.commandObjects.jsonDel(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonDel(String key, Path path) {
/* 3553 */     return appendCommand(this.commandObjects.jsonDel(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonClear(String key) {
/* 3558 */     return appendCommand(this.commandObjects.jsonClear(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonClear(String key, Path2 path) {
/* 3563 */     return appendCommand(this.commandObjects.jsonClear(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonClear(String key, Path path) {
/* 3568 */     return appendCommand(this.commandObjects.jsonClear(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> jsonToggle(String key, Path2 path) {
/* 3573 */     return appendCommand(this.commandObjects.jsonToggle(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> jsonToggle(String key, Path path) {
/* 3578 */     return appendCommand(this.commandObjects.jsonToggle(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Class<?>> jsonType(String key) {
/* 3583 */     return appendCommand(this.commandObjects.jsonType(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Class<?>>> jsonType(String key, Path2 path) {
/* 3588 */     return appendCommand(this.commandObjects.jsonType(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Class<?>> jsonType(String key, Path path) {
/* 3593 */     return appendCommand(this.commandObjects.jsonType(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonStrAppend(String key, Object string) {
/* 3598 */     return appendCommand(this.commandObjects.jsonStrAppend(key, string));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonStrAppend(String key, Path2 path, Object string) {
/* 3603 */     return appendCommand(this.commandObjects.jsonStrAppend(key, path, string));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonStrAppend(String key, Path path, Object string) {
/* 3608 */     return appendCommand(this.commandObjects.jsonStrAppend(key, path, string));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonStrLen(String key) {
/* 3613 */     return appendCommand(this.commandObjects.jsonStrLen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonStrLen(String key, Path2 path) {
/* 3618 */     return appendCommand(this.commandObjects.jsonStrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonStrLen(String key, Path path) {
/* 3623 */     return appendCommand(this.commandObjects.jsonStrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> jsonNumIncrBy(String key, Path2 path, double value) {
/* 3628 */     return appendCommand(this.commandObjects.jsonNumIncrBy(key, path, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> jsonNumIncrBy(String key, Path path, double value) {
/* 3633 */     return appendCommand(this.commandObjects.jsonNumIncrBy(key, path, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrAppend(String key, Path2 path, Object... objects) {
/* 3638 */     return appendCommand(this.commandObjects.jsonArrAppend(key, path, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrAppendWithEscape(String key, Path2 path, Object... objects) {
/* 3643 */     return appendCommand(this.commandObjects.jsonArrAppendWithEscape(key, path, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonArrAppend(String key, Path path, Object... objects) {
/* 3648 */     return appendCommand(this.commandObjects.jsonArrAppend(key, path, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrIndex(String key, Path2 path, Object scalar) {
/* 3653 */     return appendCommand(this.commandObjects.jsonArrIndex(key, path, scalar));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrIndexWithEscape(String key, Path2 path, Object scalar) {
/* 3658 */     return appendCommand(this.commandObjects.jsonArrIndexWithEscape(key, path, scalar));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonArrIndex(String key, Path path, Object scalar) {
/* 3663 */     return appendCommand(this.commandObjects.jsonArrIndex(key, path, scalar));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrInsert(String key, Path2 path, int index, Object... objects) {
/* 3668 */     return appendCommand(this.commandObjects.jsonArrInsert(key, path, index, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrInsertWithEscape(String key, Path2 path, int index, Object... objects) {
/* 3673 */     return appendCommand(this.commandObjects.jsonArrInsertWithEscape(key, path, index, objects));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonArrInsert(String key, Path path, int index, Object... pojos) {
/* 3678 */     return appendCommand(this.commandObjects.jsonArrInsert(key, path, index, pojos));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> jsonArrPop(String key) {
/* 3683 */     return appendCommand(this.commandObjects.jsonArrPop(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonArrLen(String key, Path path) {
/* 3688 */     return appendCommand(this.commandObjects.jsonArrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrTrim(String key, Path2 path, int start, int stop) {
/* 3693 */     return appendCommand(this.commandObjects.jsonArrTrim(key, path, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonArrTrim(String key, Path path, int start, int stop) {
/* 3698 */     return appendCommand(this.commandObjects.jsonArrTrim(key, path, start, stop));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Response<T> jsonArrPop(String key, Class<T> clazz, Path path) {
/* 3703 */     return appendCommand(this.commandObjects.jsonArrPop(key, clazz, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> jsonArrPop(String key, Path2 path, int index) {
/* 3708 */     return appendCommand(this.commandObjects.jsonArrPop(key, path, index));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> jsonArrPop(String key, Path path, int index) {
/* 3713 */     return appendCommand(this.commandObjects.jsonArrPop(key, path, index));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Response<T> jsonArrPop(String key, Class<T> clazz, Path path, int index) {
/* 3718 */     return appendCommand(this.commandObjects.jsonArrPop(key, clazz, path, index));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> jsonArrLen(String key) {
/* 3723 */     return appendCommand(this.commandObjects.jsonArrLen(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> jsonArrLen(String key, Path2 path) {
/* 3728 */     return appendCommand(this.commandObjects.jsonArrLen(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> Response<T> jsonArrPop(String key, Class<T> clazz) {
/* 3733 */     return appendCommand(this.commandObjects.jsonArrPop(key, clazz));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Object>> jsonArrPop(String key, Path2 path) {
/* 3738 */     return appendCommand(this.commandObjects.jsonArrPop(key, path));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> jsonArrPop(String key, Path path) {
/* 3743 */     return appendCommand(this.commandObjects.jsonArrPop(key, path));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Response<String> tsCreate(String key) {
/* 3750 */     return appendCommand(this.commandObjects.tsCreate(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tsCreate(String key, TSCreateParams createParams) {
/* 3755 */     return appendCommand(this.commandObjects.tsCreate(key, createParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsDel(String key, long fromTimestamp, long toTimestamp) {
/* 3760 */     return appendCommand(this.commandObjects.tsDel(key, fromTimestamp, toTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tsAlter(String key, TSAlterParams alterParams) {
/* 3765 */     return appendCommand(this.commandObjects.tsAlter(key, alterParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsAdd(String key, double value) {
/* 3770 */     return appendCommand(this.commandObjects.tsAdd(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsAdd(String key, long timestamp, double value) {
/* 3775 */     return appendCommand(this.commandObjects.tsAdd(key, timestamp, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsAdd(String key, long timestamp, double value, TSCreateParams createParams) {
/* 3780 */     return appendCommand(this.commandObjects.tsAdd(key, timestamp, value, createParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> tsMAdd(Map.Entry<String, TSElement>... entries) {
/* 3785 */     return appendCommand(this.commandObjects.tsMAdd(entries));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsIncrBy(String key, double value) {
/* 3790 */     return appendCommand(this.commandObjects.tsIncrBy(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsIncrBy(String key, double value, long timestamp) {
/* 3795 */     return appendCommand(this.commandObjects.tsIncrBy(key, value, timestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsDecrBy(String key, double value) {
/* 3800 */     return appendCommand(this.commandObjects.tsDecrBy(key, value));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> tsDecrBy(String key, double value, long timestamp) {
/* 3805 */     return appendCommand(this.commandObjects.tsDecrBy(key, value, timestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<TSElement>> tsRange(String key, long fromTimestamp, long toTimestamp) {
/* 3810 */     return appendCommand(this.commandObjects.tsRange(key, fromTimestamp, toTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<TSElement>> tsRange(String key, TSRangeParams rangeParams) {
/* 3815 */     return appendCommand(this.commandObjects.tsRange(key, rangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<TSElement>> tsRevRange(String key, long fromTimestamp, long toTimestamp) {
/* 3820 */     return appendCommand(this.commandObjects.tsRevRange(key, fromTimestamp, toTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<TSElement>> tsRevRange(String key, TSRangeParams rangeParams) {
/* 3825 */     return appendCommand(this.commandObjects.tsRevRange(key, rangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, TSMRangeElements>> tsMRange(long fromTimestamp, long toTimestamp, String... filters) {
/* 3830 */     return appendCommand(this.commandObjects.tsMRange(fromTimestamp, toTimestamp, filters));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, TSMRangeElements>> tsMRange(TSMRangeParams multiRangeParams) {
/* 3835 */     return appendCommand(this.commandObjects.tsMRange(multiRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, TSMRangeElements>> tsMRevRange(long fromTimestamp, long toTimestamp, String... filters) {
/* 3840 */     return appendCommand(this.commandObjects.tsMRevRange(fromTimestamp, toTimestamp, filters));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, TSMRangeElements>> tsMRevRange(TSMRangeParams multiRangeParams) {
/* 3845 */     return appendCommand(this.commandObjects.tsMRevRange(multiRangeParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<TSElement> tsGet(String key) {
/* 3850 */     return appendCommand(this.commandObjects.tsGet(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<TSElement> tsGet(String key, TSGetParams getParams) {
/* 3855 */     return appendCommand(this.commandObjects.tsGet(key, getParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, TSMGetElement>> tsMGet(TSMGetParams multiGetParams, String... filters) {
/* 3860 */     return appendCommand(this.commandObjects.tsMGet(multiGetParams, filters));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType, long timeBucket) {
/* 3865 */     return appendCommand(this.commandObjects.tsCreateRule(sourceKey, destKey, aggregationType, timeBucket));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tsCreateRule(String sourceKey, String destKey, AggregationType aggregationType, long bucketDuration, long alignTimestamp) {
/* 3870 */     return appendCommand(this.commandObjects.tsCreateRule(sourceKey, destKey, aggregationType, bucketDuration, alignTimestamp));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tsDeleteRule(String sourceKey, String destKey) {
/* 3875 */     return appendCommand(this.commandObjects.tsDeleteRule(sourceKey, destKey));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> tsQueryIndex(String... filters) {
/* 3880 */     return appendCommand(this.commandObjects.tsQueryIndex(filters));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Response<String> bfReserve(String key, double errorRate, long capacity) {
/* 3887 */     return appendCommand(this.commandObjects.bfReserve(key, errorRate, capacity));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> bfReserve(String key, double errorRate, long capacity, BFReserveParams reserveParams) {
/* 3892 */     return appendCommand(this.commandObjects.bfReserve(key, errorRate, capacity, reserveParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> bfAdd(String key, String item) {
/* 3897 */     return appendCommand(this.commandObjects.bfAdd(key, item));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> bfMAdd(String key, String... items) {
/* 3902 */     return appendCommand(this.commandObjects.bfMAdd(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> bfInsert(String key, String... items) {
/* 3907 */     return appendCommand(this.commandObjects.bfInsert(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> bfInsert(String key, BFInsertParams insertParams, String... items) {
/* 3912 */     return appendCommand(this.commandObjects.bfInsert(key, insertParams, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> bfExists(String key, String item) {
/* 3917 */     return appendCommand(this.commandObjects.bfExists(key, item));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> bfMExists(String key, String... items) {
/* 3922 */     return appendCommand(this.commandObjects.bfMExists(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map.Entry<Long, byte[]>> bfScanDump(String key, long iterator) {
/* 3927 */     return appendCommand(this.commandObjects.bfScanDump(key, iterator));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> bfLoadChunk(String key, long iterator, byte[] data) {
/* 3932 */     return appendCommand(this.commandObjects.bfLoadChunk(key, iterator, data));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> bfCard(String key) {
/* 3937 */     return appendCommand(this.commandObjects.bfCard(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> bfInfo(String key) {
/* 3942 */     return appendCommand(this.commandObjects.bfInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> cfReserve(String key, long capacity) {
/* 3947 */     return appendCommand(this.commandObjects.cfReserve(key, capacity));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> cfReserve(String key, long capacity, CFReserveParams reserveParams) {
/* 3952 */     return appendCommand(this.commandObjects.cfReserve(key, capacity, reserveParams));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> cfAdd(String key, String item) {
/* 3957 */     return appendCommand(this.commandObjects.cfAdd(key, item));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> cfAddNx(String key, String item) {
/* 3962 */     return appendCommand(this.commandObjects.cfAddNx(key, item));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> cfInsert(String key, String... items) {
/* 3967 */     return appendCommand(this.commandObjects.cfInsert(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> cfInsert(String key, CFInsertParams insertParams, String... items) {
/* 3972 */     return appendCommand(this.commandObjects.cfInsert(key, insertParams, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> cfInsertNx(String key, String... items) {
/* 3977 */     return appendCommand(this.commandObjects.cfInsertNx(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> cfInsertNx(String key, CFInsertParams insertParams, String... items) {
/* 3982 */     return appendCommand(this.commandObjects.cfInsertNx(key, insertParams, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> cfExists(String key, String item) {
/* 3987 */     return appendCommand(this.commandObjects.cfExists(key, item));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Boolean> cfDel(String key, String item) {
/* 3992 */     return appendCommand(this.commandObjects.cfDel(key, item));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Long> cfCount(String key, String item) {
/* 3997 */     return appendCommand(this.commandObjects.cfCount(key, item));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map.Entry<Long, byte[]>> cfScanDump(String key, long iterator) {
/* 4002 */     return appendCommand(this.commandObjects.cfScanDump(key, iterator));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> cfLoadChunk(String key, long iterator, byte[] data) {
/* 4007 */     return appendCommand(this.commandObjects.cfLoadChunk(key, iterator, data));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> cfInfo(String key) {
/* 4012 */     return appendCommand(this.commandObjects.cfInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> cmsInitByDim(String key, long width, long depth) {
/* 4017 */     return appendCommand(this.commandObjects.cmsInitByDim(key, width, depth));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> cmsInitByProb(String key, double error, double probability) {
/* 4022 */     return appendCommand(this.commandObjects.cmsInitByProb(key, error, probability));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> cmsIncrBy(String key, Map<String, Long> itemIncrements) {
/* 4027 */     return appendCommand(this.commandObjects.cmsIncrBy(key, itemIncrements));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> cmsQuery(String key, String... items) {
/* 4032 */     return appendCommand(this.commandObjects.cmsQuery(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> cmsMerge(String destKey, String... keys) {
/* 4037 */     return appendCommand(this.commandObjects.cmsMerge(destKey, keys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> cmsMerge(String destKey, Map<String, Long> keysAndWeights) {
/* 4042 */     return appendCommand(this.commandObjects.cmsMerge(destKey, keysAndWeights));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> cmsInfo(String key) {
/* 4047 */     return appendCommand(this.commandObjects.cmsInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> topkReserve(String key, long topk) {
/* 4052 */     return appendCommand(this.commandObjects.topkReserve(key, topk));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> topkReserve(String key, long topk, long width, long depth, double decay) {
/* 4057 */     return appendCommand(this.commandObjects.topkReserve(key, topk, width, depth, decay));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> topkAdd(String key, String... items) {
/* 4062 */     return appendCommand(this.commandObjects.topkAdd(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> topkIncrBy(String key, Map<String, Long> itemIncrements) {
/* 4067 */     return appendCommand(this.commandObjects.topkIncrBy(key, itemIncrements));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Boolean>> topkQuery(String key, String... items) {
/* 4072 */     return appendCommand(this.commandObjects.topkQuery(key, items));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> topkList(String key) {
/* 4077 */     return appendCommand(this.commandObjects.topkList(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Long>> topkListWithCount(String key) {
/* 4082 */     return appendCommand(this.commandObjects.topkListWithCount(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> topkInfo(String key) {
/* 4087 */     return appendCommand(this.commandObjects.topkInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tdigestCreate(String key) {
/* 4092 */     return appendCommand(this.commandObjects.tdigestCreate(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tdigestCreate(String key, int compression) {
/* 4097 */     return appendCommand(this.commandObjects.tdigestCreate(key, compression));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tdigestReset(String key) {
/* 4102 */     return appendCommand(this.commandObjects.tdigestReset(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tdigestMerge(String destinationKey, String... sourceKeys) {
/* 4107 */     return appendCommand(this.commandObjects.tdigestMerge(destinationKey, sourceKeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tdigestMerge(TDigestMergeParams mergeParams, String destinationKey, String... sourceKeys) {
/* 4112 */     return appendCommand(this.commandObjects.tdigestMerge(mergeParams, destinationKey, sourceKeys));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Map<String, Object>> tdigestInfo(String key) {
/* 4117 */     return appendCommand(this.commandObjects.tdigestInfo(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> tdigestAdd(String key, double... values) {
/* 4122 */     return appendCommand(this.commandObjects.tdigestAdd(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Double>> tdigestCDF(String key, double... values) {
/* 4127 */     return appendCommand(this.commandObjects.tdigestCDF(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Double>> tdigestQuantile(String key, double... quantiles) {
/* 4132 */     return appendCommand(this.commandObjects.tdigestQuantile(key, quantiles));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> tdigestMin(String key) {
/* 4137 */     return appendCommand(this.commandObjects.tdigestMin(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> tdigestMax(String key) {
/* 4142 */     return appendCommand(this.commandObjects.tdigestMax(key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Double> tdigestTrimmedMean(String key, double lowCutQuantile, double highCutQuantile) {
/* 4147 */     return appendCommand(this.commandObjects.tdigestTrimmedMean(key, lowCutQuantile, highCutQuantile));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> tdigestRank(String key, double... values) {
/* 4152 */     return appendCommand(this.commandObjects.tdigestRank(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Long>> tdigestRevRank(String key, double... values) {
/* 4157 */     return appendCommand(this.commandObjects.tdigestRevRank(key, values));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Double>> tdigestByRank(String key, long... ranks) {
/* 4162 */     return appendCommand(this.commandObjects.tdigestByRank(key, ranks));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<Double>> tdigestByRevRank(String key, long... ranks) {
/* 4167 */     return appendCommand(this.commandObjects.tdigestByRevRank(key, ranks));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphQuery(String name, String query) {
/* 4174 */     return appendCommand(this.graphCommandObjects.graphQuery(name, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphReadonlyQuery(String name, String query) {
/* 4179 */     return appendCommand(this.graphCommandObjects.graphReadonlyQuery(name, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphQuery(String name, String query, long timeout) {
/* 4184 */     return appendCommand(this.graphCommandObjects.graphQuery(name, query, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphReadonlyQuery(String name, String query, long timeout) {
/* 4189 */     return appendCommand(this.graphCommandObjects.graphReadonlyQuery(name, query, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphQuery(String name, String query, Map<String, Object> params) {
/* 4194 */     return appendCommand(this.graphCommandObjects.graphQuery(name, query, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params) {
/* 4199 */     return appendCommand(this.graphCommandObjects.graphReadonlyQuery(name, query, params));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 4204 */     return appendCommand(this.graphCommandObjects.graphQuery(name, query, params, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<ResultSet> graphReadonlyQuery(String name, String query, Map<String, Object> params, long timeout) {
/* 4209 */     return appendCommand(this.graphCommandObjects.graphReadonlyQuery(name, query, params, timeout));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<String> graphDelete(String name) {
/* 4214 */     return appendCommand(this.graphCommandObjects.graphDelete(name));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<List<String>> graphProfile(String graphName, String query) {
/* 4219 */     return appendCommand(this.commandObjects.graphProfile(graphName, query));
/*      */   }
/*      */ 
/*      */   
/*      */   public Response<Object> sendCommand(ProtocolCommand cmd, String... args) {
/* 4224 */     return sendCommand((new CommandArguments(cmd)).addObjects((Object[])args));
/*      */   }
/*      */   
/*      */   public Response<Object> sendCommand(ProtocolCommand cmd, byte[]... args) {
/* 4228 */     return sendCommand((new CommandArguments(cmd)).addObjects((Object[])args));
/*      */   }
/*      */   
/*      */   public Response<Object> sendCommand(CommandArguments args) {
/* 4232 */     return executeCommand(new CommandObject(args, BuilderFactory.RAW_OBJECT));
/*      */   }
/*      */   
/*      */   public <T> Response<T> executeCommand(CommandObject<T> command) {
/* 4236 */     return appendCommand(command);
/*      */   }
/*      */   
/*      */   public void setJsonObjectMapper(JsonObjectMapper jsonObjectMapper) {
/* 4240 */     this.commandObjects.setJsonObjectMapper(jsonObjectMapper);
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\PipeliningBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */