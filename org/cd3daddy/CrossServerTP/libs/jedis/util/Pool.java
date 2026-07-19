/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import org.apache.commons.pool2.PooledObjectFactory;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPool;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*    */ 
/*    */ public class Pool<T>
/*    */   extends GenericObjectPool<T>
/*    */ {
/*    */   public Pool(GenericObjectPoolConfig<T> poolConfig, PooledObjectFactory<T> factory) {
/* 12 */     this(factory, poolConfig);
/*    */   }
/*    */   
/*    */   public Pool(PooledObjectFactory<T> factory, GenericObjectPoolConfig<T> poolConfig) {
/* 16 */     super(factory, poolConfig);
/*    */   }
/*    */   
/*    */   public Pool(PooledObjectFactory<T> factory) {
/* 20 */     super(factory);
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 25 */     destroy();
/*    */   }
/*    */   
/*    */   public void destroy() {
/*    */     try {
/* 30 */       super.close();
/* 31 */     } catch (RuntimeException e) {
/* 32 */       throw new JedisException("Could not destroy the pool", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public T getResource() {
/*    */     try {
/* 38 */       return (T)borrowObject();
/* 39 */     } catch (JedisException je) {
/* 40 */       throw je;
/* 41 */     } catch (Exception e) {
/* 42 */       throw new JedisException("Could not get a resource from the pool", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void returnResource(T resource) {
/* 47 */     if (resource == null) {
/*    */       return;
/*    */     }
/*    */     try {
/* 51 */       returnObject(resource);
/* 52 */     } catch (RuntimeException e) {
/* 53 */       throw new JedisException("Could not return the resource to the pool", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void returnBrokenResource(T resource) {
/* 58 */     if (resource == null) {
/*    */       return;
/*    */     }
/*    */     try {
/* 62 */       invalidateObject(resource);
/* 63 */     } catch (Exception e) {
/* 64 */       throw new JedisException("Could not return the broken resource to the pool", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void addObjects(int count) {
/*    */     try {
/* 71 */       for (int i = 0; i < count; i++) {
/* 72 */         addObject();
/*    */       }
/* 74 */     } catch (Exception e) {
/* 75 */       throw new JedisException("Error trying to add idle objects", e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\Pool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */