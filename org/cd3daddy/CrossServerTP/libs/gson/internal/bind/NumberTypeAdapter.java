/*    */ package org.cd3daddy.CrossServerTP.libs.gson.internal.bind;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.Gson;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.JsonSyntaxException;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.ToNumberPolicy;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.ToNumberStrategy;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.TypeAdapter;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.TypeAdapterFactory;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.reflect.TypeToken;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.stream.JsonReader;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.stream.JsonToken;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.stream.JsonWriter;
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
/*    */ 
/*    */ 
/*    */ public final class NumberTypeAdapter
/*    */   extends TypeAdapter<Number>
/*    */ {
/* 39 */   private static final TypeAdapterFactory LAZILY_PARSED_NUMBER_FACTORY = newFactory((ToNumberStrategy)ToNumberPolicy.LAZILY_PARSED_NUMBER);
/*    */   
/*    */   private final ToNumberStrategy toNumberStrategy;
/*    */   
/*    */   private NumberTypeAdapter(ToNumberStrategy toNumberStrategy) {
/* 44 */     this.toNumberStrategy = toNumberStrategy;
/*    */   }
/*    */   
/*    */   private static TypeAdapterFactory newFactory(ToNumberStrategy toNumberStrategy) {
/* 48 */     final NumberTypeAdapter adapter = new NumberTypeAdapter(toNumberStrategy);
/* 49 */     return new TypeAdapterFactory()
/*    */       {
/*    */         public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
/* 52 */           return (type.getRawType() == Number.class) ? adapter : null;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static TypeAdapterFactory getFactory(ToNumberStrategy toNumberStrategy) {
/* 58 */     if (toNumberStrategy == ToNumberPolicy.LAZILY_PARSED_NUMBER) {
/* 59 */       return LAZILY_PARSED_NUMBER_FACTORY;
/*    */     }
/* 61 */     return newFactory(toNumberStrategy);
/*    */   }
/*    */ 
/*    */   
/*    */   public Number read(JsonReader in) throws IOException {
/* 66 */     JsonToken jsonToken = in.peek();
/* 67 */     switch (jsonToken) {
/*    */       case NULL:
/* 69 */         in.nextNull();
/* 70 */         return null;
/*    */       case NUMBER:
/*    */       case STRING:
/* 73 */         return this.toNumberStrategy.readNumber(in);
/*    */     } 
/* 75 */     throw new JsonSyntaxException("Expecting number, got: " + jsonToken + "; at path " + in.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(JsonWriter out, Number value) throws IOException {
/* 80 */     out.value(value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\gson\internal\bind\NumberTypeAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */