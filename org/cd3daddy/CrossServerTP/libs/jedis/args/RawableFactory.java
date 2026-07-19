/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.args;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
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
/*    */ public final class RawableFactory
/*    */ {
/*    */   public static Rawable from(int i) {
/* 19 */     return from(Protocol.toByteArray(i));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Rawable from(double d) {
/* 28 */     return from(Protocol.toByteArray(d));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Rawable from(byte[] binary) {
/* 37 */     return new Raw(binary);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Rawable from(String string) {
/* 46 */     return new RawString(string);
/*    */   }
/*    */ 
/*    */   
/*    */   public static class Raw
/*    */     implements Rawable
/*    */   {
/*    */     private final byte[] raw;
/*    */ 
/*    */     
/*    */     public Raw(byte[] raw) {
/* 57 */       this.raw = Arrays.copyOf(raw, raw.length);
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 62 */       return this.raw;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public static class RawString
/*    */     implements Rawable
/*    */   {
/*    */     private final byte[] raw;
/*    */ 
/*    */     
/*    */     public RawString(String str) {
/* 74 */       this.raw = SafeEncoder.encode(str);
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 79 */       return this.raw;
/*    */     }
/*    */   }
/*    */   
/*    */   private RawableFactory() {
/* 84 */     throw new InstantiationError();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\args\RawableFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */