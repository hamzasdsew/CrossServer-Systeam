/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.ScanParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public class ScanResult<T>
/*    */ {
/*    */   private byte[] cursor;
/*    */   private List<T> results;
/*    */   
/*    */   public ScanResult(String cursor, List<T> results) {
/* 13 */     this(SafeEncoder.encode(cursor), results);
/*    */   }
/*    */   
/*    */   public ScanResult(byte[] cursor, List<T> results) {
/* 17 */     this.cursor = cursor;
/* 18 */     this.results = results;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCursor() {
/* 26 */     return SafeEncoder.encode(this.cursor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isCompleteIteration() {
/* 34 */     return ScanParams.SCAN_POINTER_START.equals(getCursor());
/*    */   }
/*    */   
/*    */   public byte[] getCursorAsBytes() {
/* 38 */     return this.cursor;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<T> getResult() {
/* 46 */     return this.results;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\ScanResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */