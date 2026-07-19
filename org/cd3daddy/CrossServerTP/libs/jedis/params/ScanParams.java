/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.EnumMap;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScanParams
/*    */   implements IParams
/*    */ {
/* 17 */   private final Map<Protocol.Keyword, ByteBuffer> params = new EnumMap<>(Protocol.Keyword.class);
/*    */   
/* 19 */   public static final String SCAN_POINTER_START = String.valueOf(0);
/* 20 */   public static final byte[] SCAN_POINTER_START_BINARY = SafeEncoder.encode(SCAN_POINTER_START);
/*    */   
/*    */   public ScanParams match(byte[] pattern) {
/* 23 */     this.params.put(Protocol.Keyword.MATCH, ByteBuffer.wrap(pattern));
/* 24 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScanParams match(String pattern) {
/* 31 */     this.params.put(Protocol.Keyword.MATCH, ByteBuffer.wrap(SafeEncoder.encode(pattern)));
/* 32 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ScanParams count(Integer count) {
/* 39 */     this.params.put(Protocol.Keyword.COUNT, ByteBuffer.wrap(Protocol.toByteArray(count.intValue())));
/* 40 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 45 */     for (Map.Entry<Protocol.Keyword, ByteBuffer> param : this.params.entrySet()) {
/* 46 */       args.add(param.getKey());
/* 47 */       args.add(((ByteBuffer)param.getValue()).array());
/*    */     } 
/*    */   }
/*    */   
/*    */   public byte[] binaryMatch() {
/* 52 */     if (this.params.containsKey(Protocol.Keyword.MATCH)) {
/* 53 */       return ((ByteBuffer)this.params.get(Protocol.Keyword.MATCH)).array();
/*    */     }
/* 55 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public String match() {
/* 60 */     if (this.params.containsKey(Protocol.Keyword.MATCH)) {
/* 61 */       return new String(((ByteBuffer)this.params.get(Protocol.Keyword.MATCH)).array());
/*    */     }
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\ScanParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */