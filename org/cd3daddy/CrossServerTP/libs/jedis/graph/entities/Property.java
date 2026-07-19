/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.graph.entities;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class Property<T>
/*    */ {
/*    */   private final String name;
/*    */   private final T value;
/*    */   
/*    */   public Property(String name, T value) {
/* 16 */     this.name = name;
/* 17 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public T getValue() {
/* 25 */     return this.value;
/*    */   }
/*    */   
/*    */   private boolean valueEquals(Object value1, Object value2) {
/* 29 */     if (value1 instanceof Integer) value1 = Long.valueOf(((Integer)value1).longValue()); 
/* 30 */     if (value2 instanceof Integer) value2 = Long.valueOf(((Integer)value2).longValue()); 
/* 31 */     return Objects.equals(value1, value2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 36 */     if (this == o) return true; 
/* 37 */     if (!(o instanceof Property)) return false; 
/* 38 */     Property<?> property = (Property)o;
/* 39 */     return (Objects.equals(this.name, property.name) && 
/* 40 */       valueEquals(this.value, property.value));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 45 */     return Objects.hash(new Object[] { this.name, this.value });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     StringBuilder sb = new StringBuilder("Property{");
/* 56 */     sb.append("name='").append(this.name).append('\'');
/* 57 */     sb.append(", value=").append(this.value);
/* 58 */     sb.append('}');
/* 59 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\entities\Property.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */