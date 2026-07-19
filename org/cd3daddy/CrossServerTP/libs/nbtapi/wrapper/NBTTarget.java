/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ @Retention(RetentionPolicy.RUNTIME)
/*    */ @Target({ElementType.METHOD})
/*    */ public @interface NBTTarget
/*    */ {
/*    */   String value();
/*    */   
/*    */   Type type() default Type.AUTOMATIC;
/*    */   
/*    */   public enum Type {
/* 17 */     AUTOMATIC, GET, SET, HAS;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\wrapper\NBTTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */