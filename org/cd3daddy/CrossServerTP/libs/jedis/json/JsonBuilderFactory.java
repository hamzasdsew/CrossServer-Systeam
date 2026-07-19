/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.json;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.json.JSONArray;
/*     */ import org.json.JSONException;
/*     */ import org.json.JSONObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class JsonBuilderFactory
/*     */ {
/*  17 */   public static final Builder<Class<?>> JSON_TYPE = new Builder<Class<?>>()
/*     */     {
/*     */       public Class<?> build(Object data) {
/*  20 */         if (data == null) return null; 
/*  21 */         String str = (String)BuilderFactory.STRING.build(data);
/*  22 */         switch (str) {
/*     */           case "null":
/*  24 */             return null;
/*     */           case "boolean":
/*  26 */             return boolean.class;
/*     */           case "integer":
/*  28 */             return int.class;
/*     */           case "number":
/*  30 */             return float.class;
/*     */           case "string":
/*  32 */             return String.class;
/*     */           case "object":
/*  34 */             return Object.class;
/*     */           case "array":
/*  36 */             return List.class;
/*     */         } 
/*  38 */         throw new JedisException("Unknown type: " + str);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*     */       public String toString() {
/*  44 */         return "Class<?>";
/*     */       }
/*     */     };
/*     */   
/*  48 */   public static final Builder<List<Class<?>>> JSON_TYPE_LIST = new Builder<List<Class<?>>>()
/*     */     {
/*     */       public List<Class<?>> build(Object data) {
/*  51 */         List<Object> list = (List<Object>)data;
/*  52 */         List<Class<?>> classes = new ArrayList<>(list.size());
/*  53 */         for (Object elem : list) {
/*     */           try {
/*  55 */             classes.add(JsonBuilderFactory.JSON_TYPE.build(elem));
/*  56 */           } catch (JedisException je) {
/*  57 */             classes.add(null);
/*     */           } 
/*     */         } 
/*  60 */         return classes;
/*     */       }
/*     */     };
/*     */   
/*  64 */   public static final Builder<List<List<Class<?>>>> JSON_TYPE_RESPONSE_RESP3 = new Builder<List<List<Class<?>>>>()
/*     */     {
/*     */       public List<List<Class<?>>> build(Object data) {
/*  67 */         return (List<List<Class<?>>>)((List)data).stream().map(JsonBuilderFactory.JSON_TYPE_LIST::build).collect(Collectors.toList());
/*     */       }
/*     */     };
/*     */   
/*  71 */   public static final Builder<List<Class<?>>> JSON_TYPE_RESPONSE_RESP3_COMPATIBLE = new Builder<List<Class<?>>>()
/*     */     {
/*     */       public List<Class<?>> build(Object data) {
/*  74 */         List<List<Class<?>>> fullReply = (List<List<Class<?>>>)JsonBuilderFactory.JSON_TYPE_RESPONSE_RESP3.build(data);
/*  75 */         return (fullReply == null) ? null : fullReply.get(0);
/*     */       }
/*     */     };
/*     */   
/*  79 */   public static final Builder<Object> JSON_OBJECT = new Builder<Object>()
/*     */     {
/*     */       public Object build(Object data) {
/*  82 */         if (data == null) {
/*  83 */           return null;
/*     */         }
/*     */         
/*  86 */         if (!(data instanceof byte[])) {
/*  87 */           return data;
/*     */         }
/*  89 */         String str = (String)BuilderFactory.STRING.build(data);
/*  90 */         if (str.charAt(0) == '{') {
/*     */           try {
/*  92 */             return new JSONObject(str);
/*  93 */           } catch (Exception exception) {}
/*     */         }
/*  95 */         else if (str.charAt(0) == '[') {
/*     */           try {
/*  97 */             return new JSONArray(str);
/*  98 */           } catch (Exception exception) {}
/*     */         } 
/*     */         
/* 101 */         return str;
/*     */       }
/*     */     };
/*     */   
/* 105 */   public static final Builder<JSONArray> JSON_ARRAY = new Builder<JSONArray>()
/*     */     {
/*     */       public JSONArray build(Object data) {
/* 108 */         if (data == null) {
/* 109 */           return null;
/*     */         }
/* 111 */         String str = (String)BuilderFactory.STRING.build(data);
/*     */         try {
/* 113 */           return new JSONArray(str);
/* 114 */         } catch (JSONException ex) {
/*     */           
/* 116 */           throw new JedisException(ex);
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/* 121 */   public static final Builder<Object> JSON_ARRAY_OR_DOUBLE_LIST = new Builder<Object>()
/*     */     {
/*     */       public Object build(Object data) {
/* 124 */         if (data == null) return null; 
/* 125 */         if (data instanceof List) return BuilderFactory.DOUBLE_LIST.build(data); 
/* 126 */         return JsonBuilderFactory.JSON_ARRAY.build(data);
/*     */       }
/*     */     };
/*     */   
/* 130 */   public static final Builder<List<JSONArray>> JSON_ARRAY_LIST = new Builder<List<JSONArray>>()
/*     */     {
/*     */       public List<JSONArray> build(Object data) {
/* 133 */         if (data == null) {
/* 134 */           return null;
/*     */         }
/* 136 */         List<Object> list = (List<Object>)data;
/* 137 */         return (List<JSONArray>)list.stream().map(o -> (JSONArray)JsonBuilderFactory.JSON_ARRAY.build(o)).collect(Collectors.toList());
/*     */       }
/*     */     };
/*     */   
/*     */   private JsonBuilderFactory() {
/* 142 */     throw new InstantiationError("Must not instantiate this class");
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\json\JsonBuilderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */