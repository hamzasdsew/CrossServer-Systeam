package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;

import java.io.File;
import java.io.IOException;

public interface NBTFileHandle extends ReadWriteNBT {
  void save() throws IOException;
  
  File getFile();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\NBTFileHandle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */