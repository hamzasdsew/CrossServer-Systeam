package org.cd3daddy.CrossServerTP.libs.jedis;

import java.net.Socket;
import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisConnectionException;

public interface JedisSocketFactory {
  Socket createSocket() throws JedisConnectionException;
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisSocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */