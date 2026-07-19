/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class Path
/*    */ {
/* 10 */   public static final Path ROOT_PATH = new Path(".");
/*    */   
/*    */   private final String strPath;
/*    */   
/*    */   public Path(String strPath) {
/* 15 */     this.strPath = strPath;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 20 */     return this.strPath;
/*    */   }
/*    */   
/*    */   public static Path of(String strPath) {
/* 24 */     return new Path(strPath);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 29 */     if (obj == null) {
/* 30 */       return false;
/*    */     }
/* 32 */     if (!(obj instanceof Path)) {
/* 33 */       return false;
/*    */     }
/* 35 */     if (obj == this) {
/* 36 */       return true;
/*    */     }
/* 38 */     return toString().equals(((Path)obj).toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 43 */     return this.strPath.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */