package org.cd3daddy.CrossServerTP.libs.gson;

import java.lang.reflect.Type;

public interface JsonSerializationContext {
  JsonElement serialize(Object paramObject);
  
  JsonElement serialize(Object paramObject, Type paramType);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\gson\JsonSerializationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */