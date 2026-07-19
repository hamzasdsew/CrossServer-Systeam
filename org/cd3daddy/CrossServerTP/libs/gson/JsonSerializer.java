package org.cd3daddy.CrossServerTP.libs.gson;

import java.lang.reflect.Type;

public interface JsonSerializer<T> {
  JsonElement serialize(T paramT, Type paramType, JsonSerializationContext paramJsonSerializationContext);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\gson\JsonSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */