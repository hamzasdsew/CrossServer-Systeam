/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.GeoCoordinate;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RediSearchUtil
/*     */ {
/*     */   public static Map<String, String> toStringMap(Map<String, Object> input) {
/*  24 */     return toStringMap(input, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> toStringMap(Map<String, Object> input, boolean stringEscape) {
/*  36 */     Map<String, String> output = new HashMap<>(input.size());
/*  37 */     for (Map.Entry<String, Object> entry : input.entrySet()) {
/*  38 */       String str, key = entry.getKey();
/*  39 */       Object obj = entry.getValue();
/*  40 */       if (key == null || obj == null) {
/*  41 */         throw new NullPointerException("A null argument cannot be sent to Redis.");
/*     */       }
/*     */       
/*  44 */       if (obj instanceof byte[]) {
/*  45 */         str = SafeEncoder.encode((byte[])obj);
/*  46 */       } else if (obj instanceof GeoCoordinate) {
/*  47 */         GeoCoordinate geo = (GeoCoordinate)obj;
/*  48 */         str = geo.getLongitude() + "," + geo.getLatitude();
/*  49 */       } else if (obj instanceof String) {
/*  50 */         str = stringEscape ? escape((String)obj) : (String)obj;
/*     */       } else {
/*  52 */         str = String.valueOf(obj);
/*     */       } 
/*  54 */       output.put(key, str);
/*     */     } 
/*  56 */     return output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] toByteArray(float[] input) {
/*  67 */     byte[] bytes = new byte[4 * input.length];
/*  68 */     ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer().put(input);
/*  69 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static byte[] ToByteArray(float[] input) {
/*  77 */     return toByteArray(input);
/*     */   }
/*     */   
/*  80 */   private static final Set<Character> ESCAPE_CHARS = new HashSet<>(Arrays.asList(new Character[] { 
/*  81 */           Character.valueOf(','), Character.valueOf('.'), Character.valueOf('<'), Character.valueOf('>'), Character.valueOf('{'), Character.valueOf('}'), Character.valueOf('['), 
/*  82 */           Character.valueOf(']'), Character.valueOf('"'), Character.valueOf('\''), Character.valueOf(':'), Character.valueOf(';'), Character.valueOf('!'), Character.valueOf('@'), 
/*  83 */           Character.valueOf('#'), Character.valueOf('$'), Character.valueOf('%'), Character.valueOf('^'), Character.valueOf('&'), Character.valueOf('*'), Character.valueOf('('), 
/*  84 */           Character.valueOf(')'), Character.valueOf('-'), Character.valueOf('+'), Character.valueOf('='), Character.valueOf('~'), Character.valueOf('|') }));
/*     */ 
/*     */   
/*     */   public static String escape(String text) {
/*  88 */     return escape(text, false);
/*     */   }
/*     */   
/*     */   public static String escapeQuery(String query) {
/*  92 */     return escape(query, true);
/*     */   }
/*     */   
/*     */   public static String escape(String text, boolean querying) {
/*  96 */     char[] chars = text.toCharArray();
/*     */     
/*  98 */     StringBuilder sb = new StringBuilder();
/*  99 */     for (char ch : chars) {
/* 100 */       if (ESCAPE_CHARS.contains(Character.valueOf(ch)) || (querying && ch == ' '))
/*     */       {
/* 102 */         sb.append("\\");
/*     */       }
/* 104 */       sb.append(ch);
/*     */     } 
/* 106 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String unescape(String text) {
/* 110 */     return text.replace("\\", "");
/*     */   }
/*     */   
/*     */   private RediSearchUtil() {
/* 114 */     throw new InstantiationError("Must not instantiate this class");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\RediSearchUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */