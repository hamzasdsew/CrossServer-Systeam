package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;

import java.util.function.BiConsumer;
import org.bukkit.inventory.meta.ItemMeta;

public interface ReadWriteItemNBT extends ReadWriteNBT, ReadableItemNBT {
  boolean hasCustomNbtData();
  
  void clearCustomNBT();
  
  void modifyMeta(BiConsumer<ReadableNBT, ItemMeta> paramBiConsumer);
  
  <T extends ItemMeta> void modifyMeta(Class<T> paramClass, BiConsumer<ReadableNBT, T> paramBiConsumer);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\ReadWriteItemNBT.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */