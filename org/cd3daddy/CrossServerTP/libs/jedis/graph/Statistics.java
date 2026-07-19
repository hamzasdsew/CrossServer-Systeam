package org.cd3daddy.CrossServerTP.libs.jedis.graph;

@Deprecated
public interface Statistics {
  int nodesCreated();
  
  int nodesDeleted();
  
  int indicesCreated();
  
  int indicesDeleted();
  
  int labelsAdded();
  
  int relationshipsDeleted();
  
  int relationshipsCreated();
  
  int propertiesSet();
  
  boolean cachedExecution();
  
  String queryIntervalExecutionTime();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\Statistics.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */