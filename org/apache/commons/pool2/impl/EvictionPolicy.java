package org.apache.commons.pool2.impl;

import org.apache.commons.pool2.PooledObject;

public interface EvictionPolicy<T> {
  boolean evict(EvictionConfig paramEvictionConfig, PooledObject<T> paramPooledObject, int paramInt);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\EvictionPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */