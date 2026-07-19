/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LibraryInfo
/*     */ {
/*     */   private final String libraryName;
/*     */   private final String engine;
/*     */   private final List<Map<String, Object>> functions;
/*     */   private final String libraryCode;
/*     */   
/*     */   public LibraryInfo(String libraryName, String engineName, List<Map<String, Object>> functions) {
/*  24 */     this(libraryName, engineName, functions, null);
/*     */   }
/*     */   
/*     */   public LibraryInfo(String libraryName, String engineName, List<Map<String, Object>> functions, String code) {
/*  28 */     this.libraryName = libraryName;
/*  29 */     this.engine = engineName;
/*  30 */     this.functions = functions;
/*  31 */     this.libraryCode = code;
/*     */   }
/*     */   
/*     */   public String getLibraryName() {
/*  35 */     return this.libraryName;
/*     */   }
/*     */   
/*     */   public String getEngine() {
/*  39 */     return this.engine;
/*     */   }
/*     */   
/*     */   public List<Map<String, Object>> getFunctions() {
/*  43 */     return this.functions;
/*     */   }
/*     */   
/*     */   public String getLibraryCode() {
/*  47 */     return this.libraryCode;
/*     */   }
/*     */   
/*  50 */   public static final Builder<LibraryInfo> LIBRARY_INFO = new Builder<LibraryInfo>()
/*     */     {
/*     */       public LibraryInfo build(Object data) {
/*  53 */         if (data == null) return null; 
/*  54 */         List<List<Object>> list = (List)data;
/*  55 */         if (list.isEmpty()) return null;
/*     */         
/*  57 */         if (list.get(0) instanceof KeyValue) {
/*  58 */           String str1 = null, enginename = null, librarycode = null;
/*  59 */           List<Map<String, Object>> list1 = null;
/*  60 */           for (KeyValue kv : list) {
/*  61 */             switch ((String)BuilderFactory.STRING.build(kv.getKey())) {
/*     */               case "library_name":
/*  63 */                 str1 = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */               
/*     */               case "engine":
/*  66 */                 enginename = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */               
/*     */               case "functions":
/*  69 */                 list1 = (List<Map<String, Object>>)((List)kv.getValue()).stream().map(o -> (Map)BuilderFactory.ENCODED_OBJECT_MAP.build(o)).collect(Collectors.toList());
/*     */               
/*     */               case "library_code":
/*  72 */                 librarycode = (String)BuilderFactory.STRING.build(kv.getValue());
/*     */             } 
/*     */           
/*     */           } 
/*  76 */           return new LibraryInfo(str1, enginename, list1, librarycode);
/*     */         } 
/*     */         
/*  79 */         String libname = (String)BuilderFactory.STRING.build(list.get(1));
/*  80 */         String engine = (String)BuilderFactory.STRING.build(list.get(3));
/*  81 */         List<Object> rawFunctions = list.get(5);
/*  82 */         List<Map<String, Object>> functions = (List<Map<String, Object>>)rawFunctions.stream().map(o -> (Map)BuilderFactory.ENCODED_OBJECT_MAP.build(o)).collect(Collectors.toList());
/*  83 */         if (list.size() <= 6) {
/*  84 */           return new LibraryInfo(libname, engine, functions);
/*     */         }
/*  86 */         String code = (String)BuilderFactory.STRING.build(list.get(7));
/*  87 */         return new LibraryInfo(libname, engine, functions, code);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*  95 */   public static final Builder<LibraryInfo> LIBRARY_BUILDER = LIBRARY_INFO;
/*     */   
/*  97 */   public static final Builder<List<LibraryInfo>> LIBRARY_INFO_LIST = new Builder<List<LibraryInfo>>()
/*     */     {
/*     */       public List<LibraryInfo> build(Object data) {
/* 100 */         List<Object> list = (List<Object>)data;
/* 101 */         return (List<LibraryInfo>)list.stream().map(o -> (LibraryInfo)LibraryInfo.LIBRARY_INFO.build(o)).collect(Collectors.toList());
/*     */       }
/*     */     };
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\LibraryInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */