package org.cd3daddy.CrossServerTP.libs.gson;

import org.cd3daddy.CrossServerTP.libs.gson.reflect.TypeToken;

public interface TypeAdapterFactory {
  <T> TypeAdapter<T> create(Gson paramGson, TypeToken<T> paramTypeToken);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\gson\TypeAdapterFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */