/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.json;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Path2
/*    */ {
/*  8 */   public static final Path2 ROOT_PATH = new Path2("$");
/*    */   
/*    */   private final String str;
/*    */   
/*    */   public Path2(String str) {
/* 13 */     if (str == null) {
/* 14 */       throw new NullPointerException("Path cannot be null.");
/*    */     }
/* 16 */     if (str.isEmpty()) {
/* 17 */       throw new IllegalArgumentException("Path cannot be empty.");
/*    */     }
/* 19 */     if (str.charAt(0) == '$') {
/* 20 */       this.str = str;
/* 21 */     } else if (str.charAt(0) == '.') {
/* 22 */       this.str = '$' + str;
/*    */     } else {
/* 24 */       this.str = "$." + str;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 30 */     return this.str;
/*    */   }
/*    */   
/*    */   public static Path2 of(String path) {
/* 34 */     return new Path2(path);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 39 */     if (obj == null) {
/* 40 */       return false;
/*    */     }
/* 42 */     if (!(obj instanceof Path2)) {
/* 43 */       return false;
/*    */     }
/* 45 */     if (obj == this) {
/* 46 */       return true;
/*    */     }
/* 48 */     return toString().equals(((Path2)obj).toString());
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 53 */     return this.str.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\Path2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */