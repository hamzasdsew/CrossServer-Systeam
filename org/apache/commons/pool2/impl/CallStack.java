package org.apache.commons.pool2.impl;

import java.io.PrintWriter;

public interface CallStack {
  void clear();
  
  void fillInStackTrace();
  
  boolean printStackTrace(PrintWriter paramPrintWriter);
}


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\CallStack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */