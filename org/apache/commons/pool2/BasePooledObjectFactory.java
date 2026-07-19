/*    */ package org.apache.commons.pool2;
/*    */ 
/*    */ import java.util.Objects;
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
/*    */ public abstract class BasePooledObjectFactory<T>
/*    */   extends BaseObject
/*    */   implements PooledObjectFactory<T>
/*    */ {
/*    */   public void activateObject(PooledObject<T> p) throws Exception {}
/*    */   
/*    */   public void destroyObject(PooledObject<T> p) throws Exception {}
/*    */   
/*    */   public PooledObject<T> makeObject() throws Exception {
/* 71 */     return wrap(Objects.requireNonNull(create(), () -> String.format("BasePooledObjectFactory(%s).create() = null", new Object[] { getClass().getName() })));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void passivateObject(PooledObject<T> p) throws Exception {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean validateObject(PooledObject<T> p) {
/* 93 */     return true;
/*    */   }
/*    */   
/*    */   public abstract T create() throws Exception;
/*    */   
/*    */   public abstract PooledObject<T> wrap(T paramT);
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\BasePooledObjectFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */