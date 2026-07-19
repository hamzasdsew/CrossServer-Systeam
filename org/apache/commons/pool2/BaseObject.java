/*    */ package org.apache.commons.pool2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseObject
/*    */ {
/*    */   public String toString() {
/* 28 */     StringBuilder builder = new StringBuilder();
/* 29 */     builder.append(getClass().getSimpleName());
/* 30 */     builder.append(" [");
/* 31 */     toStringAppendFields(builder);
/* 32 */     builder.append("]");
/* 33 */     return builder.toString();
/*    */   }
/*    */   
/*    */   protected void toStringAppendFields(StringBuilder builder) {}
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\BaseObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */