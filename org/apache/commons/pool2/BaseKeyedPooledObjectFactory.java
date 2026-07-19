/*     */ package org.apache.commons.pool2;
/*     */ 
/*     */ import java.util.Objects;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class BaseKeyedPooledObjectFactory<K, V>
/*     */   extends BaseObject
/*     */   implements KeyedPooledObjectFactory<K, V>
/*     */ {
/*     */   public void activateObject(K key, PooledObject<V> p) throws Exception {}
/*     */   
/*     */   public void destroyObject(K key, PooledObject<V> p) throws Exception {}
/*     */   
/*     */   public PooledObject<V> makeObject(K key) throws Exception {
/*  80 */     return wrap(
/*  81 */         Objects.requireNonNull(create(key), () -> String.format("BaseKeyedPooledObjectFactory(%s).create(key=%s) = null", new Object[] { getClass().getName(), key })));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void passivateObject(K key, PooledObject<V> p) throws Exception {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean validateObject(K key, PooledObject<V> p) {
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public abstract V create(K paramK) throws Exception;
/*     */   
/*     */   public abstract PooledObject<V> wrap(V paramV);
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\BaseKeyedPooledObjectFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */