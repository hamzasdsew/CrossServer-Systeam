/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.function.Supplier;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*    */ 
/*    */ public class Response<T> implements Supplier<T> {
/*  7 */   protected T response = null;
/*  8 */   protected JedisDataException exception = null;
/*    */   
/*    */   private boolean building = false;
/*    */   
/*    */   private boolean built = false;
/*    */   private boolean set = false;
/*    */   private Builder<T> builder;
/*    */   private Object data;
/* 16 */   private Response<?> dependency = null;
/*    */   
/*    */   public Response(Builder<T> b) {
/* 19 */     this.builder = b;
/*    */   }
/*    */   
/*    */   public void set(Object data) {
/* 23 */     this.data = data;
/* 24 */     this.set = true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public T get() {
/* 30 */     if (this.dependency != null && this.dependency.set && !this.dependency.built) {
/* 31 */       this.dependency.build();
/*    */     }
/* 33 */     if (!this.set) {
/* 34 */       throw new IllegalStateException("Please close pipeline or multi block before calling this method.");
/*    */     }
/*    */     
/* 37 */     if (!this.built) {
/* 38 */       build();
/*    */     }
/* 40 */     if (this.exception != null) {
/* 41 */       throw this.exception;
/*    */     }
/* 43 */     return this.response;
/*    */   }
/*    */   
/*    */   public void setDependency(Response<?> dependency) {
/* 47 */     this.dependency = dependency;
/*    */   }
/*    */ 
/*    */   
/*    */   private void build() {
/* 52 */     if (this.building) {
/*    */       return;
/*    */     }
/*    */     
/* 56 */     this.building = true;
/*    */     try {
/* 58 */       if (this.data != null) {
/* 59 */         if (this.data instanceof JedisDataException) {
/* 60 */           this.exception = (JedisDataException)this.data;
/*    */         } else {
/* 62 */           this.response = this.builder.build(this.data);
/*    */         } 
/*    */       }
/*    */       
/* 66 */       this.data = null;
/*    */     } finally {
/* 68 */       this.building = false;
/* 69 */       this.built = true;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     return "Response " + this.builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */