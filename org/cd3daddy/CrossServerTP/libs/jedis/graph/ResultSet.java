/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.graph;
/*    */ @Deprecated
/*    */ public interface ResultSet extends Iterable<Record> {
/*    */   int size();
/*    */   
/*    */   Header getHeader();
/*    */   
/*    */   Statistics getStatistics();
/*    */   
/*    */   public enum ColumnType {
/* 11 */     UNKNOWN,
/* 12 */     SCALAR,
/* 13 */     NODE,
/* 14 */     RELATION;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\ResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */