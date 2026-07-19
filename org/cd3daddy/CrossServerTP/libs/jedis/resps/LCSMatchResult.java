/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LCSMatchResult
/*     */ {
/*     */   private String matchString;
/*     */   private List<MatchedPosition> matches;
/*     */   private long len;
/*     */   
/*     */   public LCSMatchResult(String matchString) {
/*  17 */     this.matchString = matchString;
/*     */   }
/*     */   
/*     */   public LCSMatchResult(long len) {
/*  21 */     this.len = len;
/*     */   }
/*     */   
/*     */   public LCSMatchResult(List<MatchedPosition> matches, long len) {
/*  25 */     this.matches = matches;
/*  26 */     this.len = len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LCSMatchResult(String matchString, List<MatchedPosition> matches, long len) {
/*  37 */     this.matchString = matchString;
/*  38 */     this.matches = Collections.unmodifiableList(matches);
/*  39 */     this.len = len;
/*     */   }
/*     */   
/*     */   public String getMatchString() {
/*  43 */     return this.matchString;
/*     */   }
/*     */   
/*     */   public List<MatchedPosition> getMatches() {
/*  47 */     return this.matches;
/*     */   }
/*     */   
/*     */   public long getLen() {
/*  51 */     return this.len;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class MatchedPosition
/*     */   {
/*     */     private final LCSMatchResult.Position a;
/*     */     
/*     */     private final LCSMatchResult.Position b;
/*     */     
/*     */     private final long matchLen;
/*     */ 
/*     */     
/*     */     public MatchedPosition(LCSMatchResult.Position a, LCSMatchResult.Position b, long matchLen) {
/*  66 */       this.a = a;
/*  67 */       this.b = b;
/*  68 */       this.matchLen = matchLen;
/*     */     }
/*     */     
/*     */     public LCSMatchResult.Position getA() {
/*  72 */       return this.a;
/*     */     }
/*     */     
/*     */     public LCSMatchResult.Position getB() {
/*  76 */       return this.b;
/*     */     }
/*     */     
/*     */     public long getMatchLen() {
/*  80 */       return this.matchLen;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Position
/*     */   {
/*     */     private final long start;
/*     */     
/*     */     private final long end;
/*     */ 
/*     */     
/*     */     public Position(long start, long end) {
/*  94 */       this.start = start;
/*  95 */       this.end = end;
/*     */     }
/*     */     
/*     */     public long getStart() {
/*  99 */       return this.start;
/*     */     }
/*     */     
/*     */     public long getEnd() {
/* 103 */       return this.end;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\LCSMatchResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */