package org.cd3daddy.CrossServerTP.libs.jedis.json;

public interface JsonObjectMapper {
  <T> T fromJson(String paramString, Class<T> paramClass);
  
  String toJson(Object paramObject);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\JsonObjectMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */