package org.apache.commons.pool2.impl;

public interface DefaultPooledObjectInfoMBean {
  long getBorrowedCount();
  
  long getCreateTime();
  
  String getCreateTimeFormatted();
  
  long getLastBorrowTime();
  
  String getLastBorrowTimeFormatted();
  
  String getLastBorrowTrace();
  
  long getLastReturnTime();
  
  String getLastReturnTimeFormatted();
  
  String getPooledObjectToString();
  
  String getPooledObjectType();
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\DefaultPooledObjectInfoMBean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */