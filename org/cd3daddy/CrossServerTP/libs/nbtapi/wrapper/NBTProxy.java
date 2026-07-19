/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.NBTHandler;
/*    */ 
/*    */ 
/*    */ public interface NBTProxy
/*    */ {
/* 11 */   public static final Map<Class<?>, NBTHandler<Object>> handlers = new HashMap<>();
/*    */ 
/*    */   
/*    */   default void init() {}
/*    */ 
/*    */   
/*    */   default Casing getCasing() {
/* 18 */     return Casing.PascalCase;
/*    */   }
/*    */ 
/*    */   
/*    */   default <T> NBTHandler<T> getHandler(Class<T> clazz) {
/* 23 */     return (NBTHandler<T>)handlers.get(clazz);
/*    */   }
/*    */   
/*    */   default Collection<NBTHandler<Object>> getHandlers() {
/* 27 */     return handlers.values();
/*    */   }
/*    */ 
/*    */   
/*    */   default <T> void registerHandler(Class<T> clazz, NBTHandler<T> handler) {
/* 32 */     handlers.put(clazz, handler);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\wrapper\NBTProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */