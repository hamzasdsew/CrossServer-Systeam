/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*     */ 
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.FlushMode;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SaveMode;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.LolwutParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ShutdownParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
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
/*     */ public interface ServerCommands
/*     */ {
/*     */   String ping();
/*     */   
/*     */   String ping(String paramString);
/*     */   
/*     */   String echo(String paramString);
/*     */   
/*     */   byte[] echo(byte[] paramArrayOfbyte);
/*     */   
/*     */   String flushDB();
/*     */   
/*     */   String flushDB(FlushMode paramFlushMode);
/*     */   
/*     */   String flushAll();
/*     */   
/*     */   String flushAll(FlushMode paramFlushMode);
/*     */   
/*     */   String auth(String paramString);
/*     */   
/*     */   String auth(String paramString1, String paramString2);
/*     */   
/*     */   String save();
/*     */   
/*     */   String bgsave();
/*     */   
/*     */   String bgsaveSchedule();
/*     */   
/*     */   String bgrewriteaof();
/*     */   
/*     */   long lastsave();
/*     */   
/*     */   void shutdown() throws JedisException;
/*     */   
/*     */   default void shutdown(SaveMode saveMode) throws JedisException {
/* 120 */     shutdown(ShutdownParams.shutdownParams().saveMode(saveMode));
/*     */   }
/*     */   
/*     */   void shutdown(ShutdownParams paramShutdownParams) throws JedisException;
/*     */   
/*     */   String shutdownAbort();
/*     */   
/*     */   String info();
/*     */   
/*     */   String info(String paramString);
/*     */   
/*     */   @Deprecated
/*     */   String slaveof(String paramString, int paramInt);
/*     */   
/*     */   @Deprecated
/*     */   String slaveofNoOne();
/*     */   
/*     */   String replicaof(String paramString, int paramInt);
/*     */   
/*     */   String replicaofNoOne();
/*     */   
/*     */   long waitReplicas(int paramInt, long paramLong);
/*     */   
/*     */   KeyValue<Long, Long> waitAOF(long paramLong1, long paramLong2, long paramLong3);
/*     */   
/*     */   String lolwut();
/*     */   
/*     */   String lolwut(LolwutParams paramLolwutParams);
/*     */   
/*     */   String reset();
/*     */   
/*     */   String latencyDoctor();
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ServerCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */