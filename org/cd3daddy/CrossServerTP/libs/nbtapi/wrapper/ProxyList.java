package org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper;

public interface ProxyList<T extends NBTProxy> extends Iterable<T> {
  T addCompound();
  
  int size();
  
  boolean isEmpty();
  
  T get(int paramInt);
  
  void remove(int paramInt);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\wrapper\ProxyList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */