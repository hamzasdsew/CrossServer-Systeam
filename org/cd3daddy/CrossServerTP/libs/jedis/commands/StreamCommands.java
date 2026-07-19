/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.StreamEntryID;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAddParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XAutoClaimParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XClaimParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XPendingParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XReadGroupParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XReadParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.XTrimParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamConsumerInfo;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamConsumersInfo;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamEntry;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamFullInfo;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamGroupInfo;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamInfo;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamPendingEntry;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.resps.StreamPendingSummary;
/*    */ 
/*    */ public interface StreamCommands {
/*    */   StreamEntryID xadd(String paramString, StreamEntryID paramStreamEntryID, Map<String, String> paramMap);
/*    */   
/*    */   default StreamEntryID xadd(String key, Map<String, String> hash, XAddParams params) {
/* 26 */     return xadd(key, params, hash);
/*    */   }
/*    */   
/*    */   StreamEntryID xadd(String paramString, XAddParams paramXAddParams, Map<String, String> paramMap);
/*    */   
/*    */   long xlen(String paramString);
/*    */   
/*    */   List<StreamEntry> xrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2);
/*    */   
/*    */   List<StreamEntry> xrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2, int paramInt);
/*    */   
/*    */   List<StreamEntry> xrevrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2);
/*    */   
/*    */   List<StreamEntry> xrevrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2, int paramInt);
/*    */   
/*    */   List<StreamEntry> xrange(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   List<StreamEntry> xrange(String paramString1, String paramString2, String paramString3, int paramInt);
/*    */   
/*    */   List<StreamEntry> xrevrange(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   List<StreamEntry> xrevrange(String paramString1, String paramString2, String paramString3, int paramInt);
/*    */   
/*    */   long xack(String paramString1, String paramString2, StreamEntryID... paramVarArgs);
/*    */   
/*    */   String xgroupCreate(String paramString1, String paramString2, StreamEntryID paramStreamEntryID, boolean paramBoolean);
/*    */   
/*    */   String xgroupSetID(String paramString1, String paramString2, StreamEntryID paramStreamEntryID);
/*    */   
/*    */   long xgroupDestroy(String paramString1, String paramString2);
/*    */   
/*    */   boolean xgroupCreateConsumer(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   long xgroupDelConsumer(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   long xdel(String paramString, StreamEntryID... paramVarArgs);
/*    */   
/*    */   long xtrim(String paramString, long paramLong, boolean paramBoolean);
/*    */   
/*    */   long xtrim(String paramString, XTrimParams paramXTrimParams);
/*    */   
/*    */   StreamPendingSummary xpending(String paramString1, String paramString2);
/*    */   
/*    */   List<StreamPendingEntry> xpending(String paramString1, String paramString2, XPendingParams paramXPendingParams);
/*    */   
/*    */   List<StreamEntry> xclaim(String paramString1, String paramString2, String paramString3, long paramLong, XClaimParams paramXClaimParams, StreamEntryID... paramVarArgs);
/*    */   
/*    */   List<StreamEntryID> xclaimJustId(String paramString1, String paramString2, String paramString3, long paramLong, XClaimParams paramXClaimParams, StreamEntryID... paramVarArgs);
/*    */   
/*    */   Map.Entry<StreamEntryID, List<StreamEntry>> xautoclaim(String paramString1, String paramString2, String paramString3, long paramLong, StreamEntryID paramStreamEntryID, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   Map.Entry<StreamEntryID, List<StreamEntryID>> xautoclaimJustId(String paramString1, String paramString2, String paramString3, long paramLong, StreamEntryID paramStreamEntryID, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   StreamInfo xinfoStream(String paramString);
/*    */   
/*    */   StreamFullInfo xinfoStreamFull(String paramString);
/*    */   
/*    */   StreamFullInfo xinfoStreamFull(String paramString, int paramInt);
/*    */   
/*    */   List<StreamGroupInfo> xinfoGroups(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   List<StreamConsumersInfo> xinfoConsumers(String paramString1, String paramString2);
/*    */   
/*    */   List<StreamConsumerInfo> xinfoConsumers2(String paramString1, String paramString2);
/*    */   
/*    */   List<Map.Entry<String, List<StreamEntry>>> xread(XReadParams paramXReadParams, Map<String, StreamEntryID> paramMap);
/*    */   
/*    */   List<Map.Entry<String, List<StreamEntry>>> xreadGroup(String paramString1, String paramString2, XReadGroupParams paramXReadGroupParams, Map<String, StreamEntryID> paramMap);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StreamCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */