/*   */ package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;
/*   */ 
/*   */ import javax.annotation.Nonnull;
/*   */ 
/*   */ public interface NBTHandler<T>
/*   */ {
/*   */   default boolean fuzzyMatch(Object obj) {
/* 8 */     return false;
/*   */   }
/*   */   
/*   */   void set(@Nonnull ReadWriteNBT paramReadWriteNBT, @Nonnull String paramString, @Nonnull T paramT);
/*   */   
/*   */   T get(@Nonnull ReadableNBT paramReadableNBT, @Nonnull String paramString);
/*   */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\NBTHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */