/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.function.Predicate;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBTList;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class NBTList<T>
/*     */   implements List<T>, ReadWriteNBTList<T>
/*     */ {
/*     */   private String listName;
/*     */   private NBTCompound parent;
/*     */   private NBTType type;
/*     */   protected Object listObject;
/*     */   
/*     */   protected NBTList(NBTCompound owner, String name, NBTType type, Object list) {
/*  31 */     this.parent = owner;
/*  32 */     this.listName = name;
/*  33 */     this.type = type;
/*  34 */     this.listObject = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  41 */     return this.listName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTCompound getParent() {
/*  48 */     return this.parent;
/*     */   }
/*     */   
/*     */   private void validateClosed() {
/*  52 */     if (this.parent.isClosed()) {
/*  53 */       throw new NbtApiException("Tried using closed NBT data!");
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateWritable() {
/*  58 */     if (getParent().isReadOnly()) {
/*  59 */       throw new NbtApiException("Tried setting data in read only mode!");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void save() {
/*  64 */     validateClosed();
/*  65 */     this.parent.set(this.listName, this.listObject);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract Object asTag(T paramT);
/*     */   
/*     */   public boolean add(T element) {
/*  72 */     validateClosed();
/*  73 */     validateWritable();
/*     */     try {
/*  75 */       this.parent.getWriteLock().lock();
/*  76 */       if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
/*  77 */         ReflectionMethod.LIST_ADD.run(this.listObject, new Object[] { Integer.valueOf(size()), asTag(element) });
/*     */       } else {
/*  79 */         ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, new Object[] { asTag(element) });
/*     */       } 
/*  81 */       save();
/*  82 */       return true;
/*  83 */     } catch (Exception ex) {
/*  84 */       throw new NbtApiException(ex);
/*     */     } finally {
/*  86 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, T element) {
/*  92 */     validateClosed();
/*  93 */     validateWritable();
/*     */     try {
/*  95 */       this.parent.getWriteLock().lock();
/*  96 */       if (MinecraftVersion.getVersion().getVersionId() >= MinecraftVersion.MC1_14_R1.getVersionId()) {
/*  97 */         ReflectionMethod.LIST_ADD.run(this.listObject, new Object[] { Integer.valueOf(index), asTag(element) });
/*     */       } else {
/*  99 */         ReflectionMethod.LEGACY_LIST_ADD.run(this.listObject, new Object[] { asTag(element) });
/*     */       } 
/* 101 */       save();
/* 102 */     } catch (Exception ex) {
/* 103 */       throw new NbtApiException(ex);
/*     */     } finally {
/* 105 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public T set(int index, T element) {
/* 111 */     validateClosed();
/* 112 */     validateWritable();
/*     */     try {
/* 114 */       this.parent.getWriteLock().lock();
/* 115 */       T prev = get(index);
/* 116 */       ReflectionMethod.LIST_SET.run(this.listObject, new Object[] { Integer.valueOf(index), asTag(element) });
/* 117 */       save();
/* 118 */       return prev;
/* 119 */     } catch (Exception ex) {
/* 120 */       throw new NbtApiException(ex);
/*     */     } finally {
/* 122 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public T remove(int i) {
/* 128 */     validateClosed();
/* 129 */     validateWritable();
/*     */     try {
/* 131 */       this.parent.getWriteLock().lock();
/* 132 */       T old = get(i);
/* 133 */       ReflectionMethod.LIST_REMOVE_KEY.run(this.listObject, new Object[] { Integer.valueOf(i) });
/* 134 */       save();
/* 135 */       return old;
/* 136 */     } catch (Exception ex) {
/* 137 */       throw new NbtApiException(ex);
/*     */     } finally {
/* 139 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 145 */     validateClosed();
/*     */     try {
/* 147 */       this.parent.getReadLock().lock();
/* 148 */       return ((Integer)ReflectionMethod.LIST_SIZE.run(this.listObject, new Object[0])).intValue();
/* 149 */     } catch (Exception ex) {
/* 150 */       throw new NbtApiException(ex);
/*     */     } finally {
/* 152 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NBTType getType() {
/* 161 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 166 */     return (size() == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 171 */     while (!isEmpty()) {
/* 172 */       remove(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 178 */     validateClosed();
/*     */     try {
/* 180 */       this.parent.getReadLock().lock(); int i;
/* 181 */       for (i = 0; i < size(); i++) {
/* 182 */         if (o.equals(get(i)))
/* 183 */           return true; 
/*     */       } 
/* 185 */       i = 0; return i;
/*     */     } finally {
/* 187 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int indexOf(Object o) {
/* 193 */     validateClosed();
/*     */     try {
/* 195 */       this.parent.getReadLock().lock(); int i;
/* 196 */       for (i = 0; i < size(); i++) {
/* 197 */         if (o.equals(get(i)))
/* 198 */           return i; 
/*     */       } 
/* 200 */       i = -1; return i;
/*     */     } finally {
/* 202 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends T> c) {
/* 208 */     validateClosed();
/*     */     try {
/* 210 */       this.parent.getWriteLock().lock();
/* 211 */       int size = size();
/* 212 */       for (T ele : c) {
/* 213 */         add(ele);
/*     */       }
/* 215 */       return (size != size());
/*     */     } finally {
/* 217 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int index, Collection<? extends T> c) {
/* 223 */     validateClosed();
/*     */     try {
/* 225 */       this.parent.getWriteLock().lock();
/* 226 */       int size = size();
/* 227 */       for (T ele : c) {
/* 228 */         add(index++, ele);
/*     */       }
/* 230 */       return (size != size());
/*     */     } finally {
/* 232 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 238 */     validateClosed();
/*     */     try {
/* 240 */       this.parent.getReadLock().lock();
/* 241 */       for (Object ele : c) {
/* 242 */         if (!contains(ele))
/* 243 */           return false; 
/*     */       } 
/* 245 */       return true;
/*     */     } finally {
/* 247 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int lastIndexOf(Object o) {
/* 253 */     validateClosed();
/*     */     try {
/* 255 */       this.parent.getReadLock().lock();
/* 256 */       int index = -1; int i;
/* 257 */       for (i = 0; i < size(); i++) {
/* 258 */         if (o.equals(get(i)))
/* 259 */           index = i; 
/*     */       } 
/* 261 */       i = index; return i;
/*     */     } finally {
/* 263 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 269 */     validateClosed();
/*     */     try {
/* 271 */       this.parent.getWriteLock().lock();
/* 272 */       int size = size();
/* 273 */       for (Object obj : c) {
/* 274 */         remove(obj);
/*     */       }
/* 276 */       return (size != size());
/*     */     } finally {
/* 278 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 284 */     validateClosed();
/*     */     try {
/* 286 */       this.parent.getWriteLock().lock();
/* 287 */       int size = size();
/* 288 */       for (Object obj : c) {
/* 289 */         for (int i = 0; i < size(); i++) {
/* 290 */           if (!obj.equals(get(i))) {
/* 291 */             remove(i--);
/*     */           }
/*     */         } 
/*     */       } 
/* 295 */       return (size != size());
/*     */     } finally {
/* 297 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 303 */     validateClosed();
/*     */     try {
/* 305 */       this.parent.getWriteLock().lock();
/* 306 */       int size = size();
/* 307 */       int id = indexOf(o);
/* 308 */       if (id != -1) {
/* 309 */         remove(id);
/*     */       }
/* 311 */       return (size != size());
/*     */     } finally {
/* 313 */       this.parent.getWriteLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 319 */     return new Iterator<T>()
/*     */       {
/* 321 */         private int index = -1;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 325 */           return (NBTList.this.size() > this.index + 1);
/*     */         }
/*     */ 
/*     */         
/*     */         public T next() {
/* 330 */           if (!hasNext())
/* 331 */             throw new NoSuchElementException(); 
/* 332 */           return NBTList.this.get(++this.index);
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 337 */           NBTList.this.remove(this.index);
/* 338 */           this.index--;
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator() {
/* 345 */     return listIterator(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<T> listIterator(final int startIndex) {
/* 350 */     final NBTList<T> list = this;
/* 351 */     return new ListIterator<T>()
/*     */       {
/* 353 */         int index = startIndex - 1;
/*     */ 
/*     */         
/*     */         public void add(T e) {
/* 357 */           list.add(this.index, e);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 362 */           return (NBTList.this.size() > this.index + 1);
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean hasPrevious() {
/* 367 */           return (this.index >= 0 && this.index <= NBTList.this.size());
/*     */         }
/*     */ 
/*     */         
/*     */         public T next() {
/* 372 */           if (!hasNext())
/* 373 */             throw new NoSuchElementException(); 
/* 374 */           return NBTList.this.get(++this.index);
/*     */         }
/*     */ 
/*     */         
/*     */         public int nextIndex() {
/* 379 */           return this.index + 1;
/*     */         }
/*     */ 
/*     */         
/*     */         public T previous() {
/* 384 */           if (!hasPrevious())
/* 385 */             throw new NoSuchElementException("Id: " + (this.index - 1)); 
/* 386 */           return NBTList.this.get(this.index--);
/*     */         }
/*     */ 
/*     */         
/*     */         public int previousIndex() {
/* 391 */           return this.index - 1;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 396 */           list.remove(this.index);
/* 397 */           this.index--;
/*     */         }
/*     */ 
/*     */         
/*     */         public void set(T e) {
/* 402 */           list.set(this.index, e);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 409 */     validateClosed();
/*     */     try {
/* 411 */       this.parent.getReadLock().lock();
/* 412 */       Object[] ar = new Object[size()];
/* 413 */       for (int i = 0; i < size(); i++)
/* 414 */         ar[i] = get(i); 
/* 415 */       return ar;
/*     */     } finally {
/* 417 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <E> E[] toArray(E[] a) {
/* 424 */     validateClosed();
/*     */     try {
/* 426 */       this.parent.getReadLock().lock();
/* 427 */       E[] ar = Arrays.copyOf(a, size());
/* 428 */       Arrays.fill((Object[])ar, (Object)null);
/* 429 */       Class<?> arrayclass = a.getClass().getComponentType();
/* 430 */       for (int i = 0; i < size(); i++) {
/* 431 */         T obj = get(i);
/* 432 */         if (arrayclass.isInstance(obj)) {
/* 433 */           ar[i] = (E)get(i);
/*     */         } else {
/* 435 */           throw new ArrayStoreException("The array does not match the objects stored in the List.");
/*     */         } 
/*     */       } 
/* 438 */       return ar;
/*     */     } finally {
/* 440 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<T> subList(int fromIndex, int toIndex) {
/* 446 */     validateClosed();
/*     */     try {
/* 448 */       this.parent.getReadLock().lock();
/* 449 */       ArrayList<T> list = new ArrayList<>();
/* 450 */       for (int i = fromIndex; i < toIndex; i++)
/* 451 */         list.add(get(i)); 
/* 452 */       return list;
/*     */     } finally {
/* 454 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeIf(Predicate<? super T> filter) {
/* 460 */     return super.removeIf(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 465 */     validateClosed();
/*     */     try {
/* 467 */       this.parent.getReadLock().lock();
/* 468 */       return this.listObject.toString();
/*     */     } finally {
/* 470 */       this.parent.getReadLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */