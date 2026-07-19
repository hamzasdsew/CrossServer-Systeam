/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.pool2.BaseObjectPool;
/*     */ import org.apache.commons.pool2.PoolUtils;
/*     */ import org.apache.commons.pool2.PooledObjectFactory;
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
/*     */ public class SoftReferenceObjectPool<T>
/*     */   extends BaseObjectPool<T>
/*     */ {
/*     */   private final PooledObjectFactory<T> factory;
/*  53 */   private final ReferenceQueue<T> refQueue = new ReferenceQueue<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private int numActive;
/*     */ 
/*     */   
/*     */   private long destroyCount;
/*     */ 
/*     */   
/*     */   private long createCount;
/*     */ 
/*     */   
/*  66 */   private final LinkedBlockingDeque<PooledSoftReference<T>> idleReferences = new LinkedBlockingDeque<>();
/*     */ 
/*     */ 
/*     */   
/*  70 */   private final ArrayList<PooledSoftReference<T>> allReferences = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SoftReferenceObjectPool(PooledObjectFactory<T> factory) {
/*  79 */     this.factory = factory;
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
/*     */   public synchronized void addObject() throws Exception {
/* 107 */     assertOpen();
/* 108 */     if (this.factory == null) {
/* 109 */       throw new IllegalStateException("Cannot add objects without a factory.");
/*     */     }
/*     */     
/* 112 */     T obj = (T)this.factory.makeObject().getObject();
/* 113 */     this.createCount++;
/*     */     
/* 115 */     PooledSoftReference<T> ref = new PooledSoftReference<>(new SoftReference<>(obj, this.refQueue));
/*     */     
/* 117 */     this.allReferences.add(ref);
/*     */     
/* 119 */     boolean success = true;
/* 120 */     if (!this.factory.validateObject(ref)) {
/* 121 */       success = false;
/*     */     } else {
/* 123 */       this.factory.passivateObject(ref);
/*     */     } 
/*     */     
/* 126 */     boolean shouldDestroy = !success;
/* 127 */     if (success) {
/* 128 */       this.idleReferences.add(ref);
/* 129 */       notifyAll();
/*     */     } 
/*     */     
/* 132 */     if (shouldDestroy) {
/*     */       try {
/* 134 */         destroy(ref);
/* 135 */       } catch (Exception exception) {}
/*     */     }
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
/*     */   public synchronized T borrowObject() throws Exception {
/* 179 */     assertOpen();
/* 180 */     T obj = null;
/* 181 */     boolean newlyCreated = false;
/* 182 */     PooledSoftReference<T> ref = null;
/* 183 */     while (null == obj) {
/* 184 */       if (this.idleReferences.isEmpty()) {
/* 185 */         if (null == this.factory) {
/* 186 */           throw new NoSuchElementException();
/*     */         }
/* 188 */         newlyCreated = true;
/* 189 */         obj = (T)this.factory.makeObject().getObject();
/* 190 */         this.createCount++;
/*     */         
/* 192 */         ref = new PooledSoftReference<>(new SoftReference<>(obj));
/* 193 */         this.allReferences.add(ref);
/*     */       } else {
/* 195 */         ref = this.idleReferences.pollFirst();
/* 196 */         obj = ref.getObject();
/*     */ 
/*     */ 
/*     */         
/* 200 */         ref.getReference().clear();
/* 201 */         ref.setReference(new SoftReference<>(obj));
/*     */       } 
/* 203 */       if (null != this.factory && null != obj) {
/*     */         try {
/* 205 */           this.factory.activateObject(ref);
/* 206 */           if (!this.factory.validateObject(ref)) {
/* 207 */             throw new Exception("ValidateObject failed");
/*     */           }
/* 209 */         } catch (Throwable t) {
/* 210 */           PoolUtils.checkRethrow(t);
/*     */           try {
/* 212 */             destroy(ref);
/* 213 */           } catch (Throwable t2) {
/* 214 */             PoolUtils.checkRethrow(t2);
/*     */           } finally {
/*     */             
/* 217 */             obj = null;
/*     */           } 
/* 219 */           if (newlyCreated) {
/* 220 */             throw new NoSuchElementException("Could not create a validated object, cause: " + t);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 225 */     this.numActive++;
/* 226 */     ref.allocate();
/* 227 */     return obj;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 235 */     if (null != this.factory) {
/* 236 */       this.idleReferences.forEach(ref -> {
/*     */             try {
/*     */               if (null != ref.getObject()) {
/*     */                 this.factory.destroyObject(ref);
/*     */               }
/* 241 */             } catch (Exception exception) {}
/*     */           });
/*     */     }
/*     */ 
/*     */     
/* 246 */     this.idleReferences.clear();
/* 247 */     pruneClearedReferences();
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
/*     */   public void close() {
/* 261 */     super.close();
/* 262 */     clear();
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
/*     */   private void destroy(PooledSoftReference<T> toDestroy) throws Exception {
/* 274 */     toDestroy.invalidate();
/* 275 */     this.idleReferences.remove(toDestroy);
/* 276 */     this.allReferences.remove(toDestroy);
/*     */     try {
/* 278 */       this.factory.destroyObject(toDestroy);
/*     */     } finally {
/* 280 */       this.destroyCount++;
/* 281 */       toDestroy.getReference().clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PooledSoftReference<T> findReference(T obj) {
/* 293 */     Optional<PooledSoftReference<T>> first = this.allReferences.stream().filter(reference -> (reference.getObject() != null && reference.getObject().equals(obj))).findFirst();
/* 294 */     return first.orElse(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized PooledObjectFactory<T> getFactory() {
/* 304 */     return this.factory;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getNumActive() {
/* 314 */     return this.numActive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized int getNumIdle() {
/* 325 */     pruneClearedReferences();
/* 326 */     return this.idleReferences.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void invalidateObject(T obj) throws Exception {
/* 334 */     PooledSoftReference<T> ref = findReference(obj);
/* 335 */     if (ref == null) {
/* 336 */       throw new IllegalStateException("Object to invalidate is not currently part of this pool");
/*     */     }
/*     */     
/* 339 */     if (this.factory != null) {
/* 340 */       destroy(ref);
/*     */     }
/* 342 */     this.numActive--;
/* 343 */     notifyAll();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void pruneClearedReferences() {
/* 352 */     removeClearedReferences(this.idleReferences.iterator());
/* 353 */     removeClearedReferences(this.allReferences.iterator());
/* 354 */     while (this.refQueue.poll() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeClearedReferences(Iterator<PooledSoftReference<T>> iterator) {
/* 364 */     while (iterator.hasNext()) {
/* 365 */       PooledSoftReference<T> ref = iterator.next();
/* 366 */       if (ref.getReference() == null || ref.getReference().isEnqueued()) {
/* 367 */         iterator.remove();
/*     */       }
/*     */     } 
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
/*     */   public synchronized void returnObject(T obj) throws Exception {
/* 395 */     boolean success = !isClosed();
/* 396 */     PooledSoftReference<T> ref = findReference(obj);
/* 397 */     if (ref == null) {
/* 398 */       throw new IllegalStateException("Returned object not currently part of this pool");
/*     */     }
/*     */     
/* 401 */     if (this.factory != null) {
/* 402 */       if (!this.factory.validateObject(ref)) {
/* 403 */         success = false;
/*     */       } else {
/*     */         try {
/* 406 */           this.factory.passivateObject(ref);
/* 407 */         } catch (Exception e) {
/* 408 */           success = false;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 413 */     boolean shouldDestroy = !success;
/* 414 */     this.numActive--;
/* 415 */     if (success) {
/*     */ 
/*     */       
/* 418 */       ref.deallocate();
/* 419 */       this.idleReferences.add(ref);
/*     */     } 
/* 421 */     notifyAll();
/*     */     
/* 423 */     if (shouldDestroy && this.factory != null) {
/*     */       try {
/* 425 */         destroy(ref);
/* 426 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void toStringAppendFields(StringBuilder builder) {
/* 434 */     super.toStringAppendFields(builder);
/* 435 */     builder.append(", factory=");
/* 436 */     builder.append(this.factory);
/* 437 */     builder.append(", refQueue=");
/* 438 */     builder.append(this.refQueue);
/* 439 */     builder.append(", numActive=");
/* 440 */     builder.append(this.numActive);
/* 441 */     builder.append(", destroyCount=");
/* 442 */     builder.append(this.destroyCount);
/* 443 */     builder.append(", createCount=");
/* 444 */     builder.append(this.createCount);
/* 445 */     builder.append(", idleReferences=");
/* 446 */     builder.append(this.idleReferences);
/* 447 */     builder.append(", allReferences=");
/* 448 */     builder.append(this.allReferences);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\SoftReferenceObjectPool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */