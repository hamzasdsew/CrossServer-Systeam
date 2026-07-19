package org.cd3daddy.CrossServerTP.libs.jedis.graph;

import java.util.List;

@Deprecated
public interface Record {
  <T> T getValue(int paramInt);
  
  <T> T getValue(String paramString);
  
  String getString(int paramInt);
  
  String getString(String paramString);
  
  List<String> keys();
  
  List<Object> values();
  
  boolean containsKey(String paramString);
  
  int size();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\Record.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */