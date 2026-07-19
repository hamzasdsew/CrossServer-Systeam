/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper;
/*    */ 
/*    */ import java.util.ConcurrentModificationException;
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBTCompoundList;
/*    */ 
/*    */ class ProxiedList<E extends NBTProxy>
/*    */   implements ProxyList<E>
/*    */ {
/*    */   private final ReadWriteNBTCompoundList nbt;
/*    */   private final Class<E> proxy;
/*    */   
/*    */   public ProxiedList(ReadWriteNBTCompoundList nbt, Class<E> proxyClass) {
/* 16 */     this.nbt = nbt;
/* 17 */     this.proxy = proxyClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 22 */     ReadWriteNBT tag = (ReadWriteNBT)this.nbt.get(index);
/* 23 */     return (new ProxyBuilder<>(tag, this.proxy)).build();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 28 */     return this.nbt.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void remove(int index) {
/* 33 */     this.nbt.remove(index);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Iterator<E> iterator() {
/* 39 */     return new Itr();
/*    */   }
/*    */ 
/*    */   
/*    */   public E addCompound() {
/* 44 */     ReadWriteNBT tag = this.nbt.addCompound();
/* 45 */     return (new ProxyBuilder<>(tag, this.proxy)).build();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 50 */     return this.nbt.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   private class Itr
/*    */     implements Iterator<E>
/*    */   {
/* 57 */     int cursor = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 64 */     int lastRet = -1;
/*    */     
/*    */     public boolean hasNext() {
/* 67 */       return (this.cursor != ProxiedList.this.size());
/*    */     }
/*    */     
/*    */     public E next() {
/*    */       try {
/* 72 */         int i = this.cursor;
/* 73 */         E next = ProxiedList.this.get(i);
/* 74 */         this.lastRet = i;
/* 75 */         this.cursor = i + 1;
/* 76 */         return next;
/* 77 */       } catch (IndexOutOfBoundsException e) {
/* 78 */         throw new NoSuchElementException();
/*    */       } 
/*    */     }
/*    */     
/*    */     public void remove() {
/* 83 */       if (this.lastRet < 0) {
/* 84 */         throw new IllegalStateException();
/*    */       }
/*    */       try {
/* 87 */         ProxiedList.this.remove(this.lastRet);
/* 88 */         if (this.lastRet < this.cursor)
/* 89 */           this.cursor--; 
/* 90 */         this.lastRet = -1;
/* 91 */       } catch (IndexOutOfBoundsException e) {
/* 92 */         throw new ConcurrentModificationException();
/*    */       } 
/*    */     }
/*    */     
/*    */     private Itr() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\wrapper\ProxiedList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */