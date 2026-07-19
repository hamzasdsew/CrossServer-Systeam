package org.cd3daddy.CrossServerTP.libs.jedis.commands;

import java.util.List;
import java.util.Map;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ClusterFailoverOption;
import org.cd3daddy.CrossServerTP.libs.jedis.args.ClusterResetType;
import org.cd3daddy.CrossServerTP.libs.jedis.resps.ClusterShardInfo;

public interface ClusterCommands {
  String asking();
  
  String readonly();
  
  String readwrite();
  
  String clusterNodes();
  
  String clusterMeet(String paramString, int paramInt);
  
  String clusterAddSlots(int... paramVarArgs);
  
  String clusterDelSlots(int... paramVarArgs);
  
  String clusterInfo();
  
  List<String> clusterGetKeysInSlot(int paramInt1, int paramInt2);
  
  List<byte[]> clusterGetKeysInSlotBinary(int paramInt1, int paramInt2);
  
  String clusterSetSlotNode(int paramInt, String paramString);
  
  String clusterSetSlotMigrating(int paramInt, String paramString);
  
  String clusterSetSlotImporting(int paramInt, String paramString);
  
  String clusterSetSlotStable(int paramInt);
  
  String clusterForget(String paramString);
  
  String clusterFlushSlots();
  
  long clusterKeySlot(String paramString);
  
  long clusterCountFailureReports(String paramString);
  
  long clusterCountKeysInSlot(int paramInt);
  
  String clusterSaveConfig();
  
  String clusterSetConfigEpoch(long paramLong);
  
  String clusterBumpEpoch();
  
  String clusterReplicate(String paramString);
  
  @Deprecated
  List<String> clusterSlaves(String paramString);
  
  List<String> clusterReplicas(String paramString);
  
  String clusterFailover();
  
  String clusterFailover(ClusterFailoverOption paramClusterFailoverOption);
  
  @Deprecated
  List<Object> clusterSlots();
  
  List<ClusterShardInfo> clusterShards();
  
  String clusterReset();
  
  String clusterReset(ClusterResetType paramClusterResetType);
  
  String clusterMyId();
  
  String clusterMyShardId();
  
  List<Map<String, Object>> clusterLinks();
  
  String clusterAddSlotsRange(int... paramVarArgs);
  
  String clusterDelSlotsRange(int... paramVarArgs);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\commands\ClusterCommands.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */