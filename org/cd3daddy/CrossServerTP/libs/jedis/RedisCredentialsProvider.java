package org.cd3daddy.CrossServerTP.libs.jedis;

import java.util.function.Supplier;

public interface RedisCredentialsProvider extends Supplier<RedisCredentials> {
  default void prepare() {}
  
  default void cleanUp() {}
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\RedisCredentialsProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */