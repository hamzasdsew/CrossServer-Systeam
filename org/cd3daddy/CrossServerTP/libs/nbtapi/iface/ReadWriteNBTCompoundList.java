package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;

import java.util.function.Predicate;

public interface ReadWriteNBTCompoundList extends ReadableNBTList<ReadWriteNBT> {
  ReadWriteNBT addCompound();
  
  ReadWriteNBT addCompound(ReadableNBT paramReadableNBT);
  
  ReadWriteNBT remove(int paramInt);
  
  void clear();
  
  boolean removeIf(Predicate<? super ReadWriteNBT> paramPredicate);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\ReadWriteNBTCompoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */