/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ 
/*    */ public final class PathUtil
/*    */ {
/* 10 */   private static final Pattern pattern = Pattern.compile("[^\\\\](\\.)");
/* 11 */   private static final Pattern indexPattern = Pattern.compile(".*\\[(-?[0-9]+)\\]");
/*    */   
/*    */   public static List<PathSegment> splitPath(String path) {
/* 14 */     List<PathSegment> list = new ArrayList<>();
/* 15 */     Matcher matcher = pattern.matcher(path);
/* 16 */     int startIndex = 0;
/* 17 */     while (matcher.find(startIndex)) {
/* 18 */       list.add(new PathSegment(path.substring(startIndex, matcher.end() - 1).replace("\\.", ".")));
/* 19 */       startIndex = matcher.end();
/*    */     } 
/* 21 */     list.add(new PathSegment(path.substring(startIndex).replace("\\.", ".")));
/* 22 */     return list;
/*    */   }
/*    */   
/*    */   public static class PathSegment
/*    */   {
/*    */     private final String path;
/*    */     private final Integer index;
/*    */     
/*    */     private PathSegment(String path) {
/* 31 */       Matcher matcher = PathUtil.indexPattern.matcher(path);
/* 32 */       if (matcher.find()) {
/* 33 */         this.path = path.substring(0, path.indexOf("["));
/* 34 */         this.index = Integer.valueOf(Integer.parseInt(matcher.group(1)));
/*    */       } else {
/* 36 */         this.path = path;
/* 37 */         this.index = null;
/*    */       } 
/*    */     }
/*    */     
/*    */     public String getPath() {
/* 42 */       return this.path;
/*    */     }
/*    */     
/*    */     public int getIndex() {
/* 46 */       return this.index.intValue();
/*    */     }
/*    */     
/*    */     public boolean hasIndex() {
/* 50 */       return (this.index != null);
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 55 */       return "PathSegment [path=" + this.path + ", index=" + this.index + "]";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\PathUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */