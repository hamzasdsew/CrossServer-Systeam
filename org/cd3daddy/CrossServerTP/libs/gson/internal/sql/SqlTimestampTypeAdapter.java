/*    */ package org.cd3daddy.CrossServerTP.libs.gson.internal.sql;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.sql.Timestamp;
/*    */ import java.util.Date;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.Gson;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.TypeAdapter;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.TypeAdapterFactory;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.reflect.TypeToken;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.stream.JsonReader;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.stream.JsonWriter;
/*    */ 
/*    */ class SqlTimestampTypeAdapter
/*    */   extends TypeAdapter<Timestamp> {
/* 15 */   static final TypeAdapterFactory FACTORY = new TypeAdapterFactory()
/*    */     {
/*    */       public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
/* 18 */         if (typeToken.getRawType() == Timestamp.class) {
/* 19 */           TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
/* 20 */           return new SqlTimestampTypeAdapter(dateTypeAdapter);
/*    */         } 
/* 22 */         return null;
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   private final TypeAdapter<Date> dateTypeAdapter;
/*    */   
/*    */   private SqlTimestampTypeAdapter(TypeAdapter<Date> dateTypeAdapter) {
/* 30 */     this.dateTypeAdapter = dateTypeAdapter;
/*    */   }
/*    */ 
/*    */   
/*    */   public Timestamp read(JsonReader in) throws IOException {
/* 35 */     Date date = (Date)this.dateTypeAdapter.read(in);
/* 36 */     return (date != null) ? new Timestamp(date.getTime()) : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(JsonWriter out, Timestamp value) throws IOException {
/* 41 */     this.dateTypeAdapter.write(out, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\gson\internal\sql\SqlTimestampTypeAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */