/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.commands;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Response;
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
/*    */ public interface StreamPipelineCommands {
/*    */   Response<StreamEntryID> xadd(String paramString, StreamEntryID paramStreamEntryID, Map<String, String> paramMap);
/*    */   
/*    */   default Response<StreamEntryID> xadd(String key, Map<String, String> hash, XAddParams params) {
/* 27 */     return xadd(key, params, hash);
/*    */   }
/*    */   
/*    */   Response<StreamEntryID> xadd(String paramString, XAddParams paramXAddParams, Map<String, String> paramMap);
/*    */   
/*    */   Response<Long> xlen(String paramString);
/*    */   
/*    */   Response<List<StreamEntry>> xrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2);
/*    */   
/*    */   Response<List<StreamEntry>> xrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2, int paramInt);
/*    */   
/*    */   Response<List<StreamEntry>> xrevrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2);
/*    */   
/*    */   Response<List<StreamEntry>> xrevrange(String paramString, StreamEntryID paramStreamEntryID1, StreamEntryID paramStreamEntryID2, int paramInt);
/*    */   
/*    */   Response<List<StreamEntry>> xrange(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   Response<List<StreamEntry>> xrange(String paramString1, String paramString2, String paramString3, int paramInt);
/*    */   
/*    */   Response<List<StreamEntry>> xrevrange(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   Response<List<StreamEntry>> xrevrange(String paramString1, String paramString2, String paramString3, int paramInt);
/*    */   
/*    */   Response<Long> xack(String paramString1, String paramString2, StreamEntryID... paramVarArgs);
/*    */   
/*    */   Response<String> xgroupCreate(String paramString1, String paramString2, StreamEntryID paramStreamEntryID, boolean paramBoolean);
/*    */   
/*    */   Response<String> xgroupSetID(String paramString1, String paramString2, StreamEntryID paramStreamEntryID);
/*    */   
/*    */   Response<Long> xgroupDestroy(String paramString1, String paramString2);
/*    */   
/*    */   Response<Boolean> xgroupCreateConsumer(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   Response<Long> xgroupDelConsumer(String paramString1, String paramString2, String paramString3);
/*    */   
/*    */   Response<StreamPendingSummary> xpending(String paramString1, String paramString2);
/*    */   
/*    */   Response<List<StreamPendingEntry>> xpending(String paramString1, String paramString2, XPendingParams paramXPendingParams);
/*    */   
/*    */   Response<Long> xdel(String paramString, StreamEntryID... paramVarArgs);
/*    */   
/*    */   Response<Long> xtrim(String paramString, long paramLong, boolean paramBoolean);
/*    */   
/*    */   Response<Long> xtrim(String paramString, XTrimParams paramXTrimParams);
/*    */   
/*    */   Response<List<StreamEntry>> xclaim(String paramString1, String paramString2, String paramString3, long paramLong, XClaimParams paramXClaimParams, StreamEntryID... paramVarArgs);
/*    */   
/*    */   Response<List<StreamEntryID>> xclaimJustId(String paramString1, String paramString2, String paramString3, long paramLong, XClaimParams paramXClaimParams, StreamEntryID... paramVarArgs);
/*    */   
/*    */   Response<Map.Entry<StreamEntryID, List<StreamEntry>>> xautoclaim(String paramString1, String paramString2, String paramString3, long paramLong, StreamEntryID paramStreamEntryID, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   Response<Map.Entry<StreamEntryID, List<StreamEntryID>>> xautoclaimJustId(String paramString1, String paramString2, String paramString3, long paramLong, StreamEntryID paramStreamEntryID, XAutoClaimParams paramXAutoClaimParams);
/*    */   
/*    */   Response<StreamInfo> xinfoStream(String paramString);
/*    */   
/*    */   Response<StreamFullInfo> xinfoStreamFull(String paramString);
/*    */   
/*    */   Response<StreamFullInfo> xinfoStreamFull(String paramString, int paramInt);
/*    */   
/*    */   Response<List<StreamGroupInfo>> xinfoGroups(String paramString);
/*    */   
/*    */   @Deprecated
/*    */   Response<List<StreamConsumersInfo>> xinfoConsumers(String paramString1, String paramString2);
/*    */   
/*    */   Response<List<StreamConsumerInfo>> xinfoConsumers2(String paramString1, String paramString2);
/*    */   
/*    */   Response<List<Map.Entry<String, List<StreamEntry>>>> xread(XReadParams paramXReadParams, Map<String, StreamEntryID> paramMap);
/*    */   
/*    */   Response<List<Map.Entry<String, List<StreamEntry>>>> xreadGroup(String paramString1, String paramString2, XReadGroupParams paramXReadGroupParams, Map<String, StreamEntryID> paramMap);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\StreamPipelineCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */