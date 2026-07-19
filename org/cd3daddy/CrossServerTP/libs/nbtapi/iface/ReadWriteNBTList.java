package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;

import java.util.Collection;
import java.util.ListIterator;
import java.util.function.Predicate;

public interface ReadWriteNBTList<T> extends ReadableNBTList<T> {
  boolean add(T paramT);
  
  void add(int paramInt, T paramT);
  
  T set(int paramInt, T paramT);
  
  T remove(int paramInt);
  
  void clear();
  
  boolean addAll(Collection<? extends T> paramCollection);
  
  boolean addAll(int paramInt, Collection<? extends T> paramCollection);
  
  boolean removeAll(Collection<?> paramCollection);
  
  boolean retainAll(Collection<?> paramCollection);
  
  boolean removeIf(Predicate<? super T> paramPredicate);
  
  boolean remove(Object paramObject);
  
  ListIterator<T> listIterator(int paramInt);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\ReadWriteNBTList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */