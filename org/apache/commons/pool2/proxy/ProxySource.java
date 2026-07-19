package org.apache.commons.pool2.proxy;

import org.apache.commons.pool2.UsageTracking;

interface ProxySource<T> {
  T createProxy(T paramT, UsageTracking<T> paramUsageTracking);
  
  T resolveProxy(T paramT);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\proxy\ProxySource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */