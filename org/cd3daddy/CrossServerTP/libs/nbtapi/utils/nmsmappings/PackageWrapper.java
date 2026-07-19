/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum PackageWrapper
/*    */ {
/* 11 */   NMS(new String(new byte[] { 110, 101, 116, 46, 109, 105, 110, 101, 99, 114, 97, 102, 116, 46, 115, 101, 114, 118, 101, 114
/*    */       })),
/* 13 */   CRAFTBUKKIT(new String(new byte[] { 111, 114, 103, 46, 98, 117, 107, 107, 105, 116, 46, 99, 114, 97, 102, 116, 98, 117, 107, 107, 105, 116
/*    */       })),
/* 15 */   NONE("");
/*    */   
/*    */   private final String uri;
/*    */   
/*    */   PackageWrapper(String uri) {
/* 20 */     this.uri = uri;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUri() {
/* 27 */     return this.uri;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\nmsmappings\PackageWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */