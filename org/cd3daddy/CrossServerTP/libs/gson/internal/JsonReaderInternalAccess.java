package org.cd3daddy.CrossServerTP.libs.gson.internal;

import java.io.IOException;
import org.cd3daddy.CrossServerTP.libs.gson.stream.JsonReader;

public abstract class JsonReaderInternalAccess {
  public static JsonReaderInternalAccess INSTANCE;
  
  public abstract void promoteNameToValue(JsonReader paramJsonReader) throws IOException;
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\gson\internal\JsonReaderInternalAccess.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */