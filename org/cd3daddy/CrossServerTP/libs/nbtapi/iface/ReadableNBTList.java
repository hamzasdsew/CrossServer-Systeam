/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.iface;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTType;
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
/*     */ public interface ReadableNBTList<T>
/*     */   extends Iterable<T>
/*     */ {
/*     */   T get(int paramInt);
/*     */   
/*     */   int size();
/*     */   
/*     */   NBTType getType();
/*     */   
/*     */   boolean isEmpty();
/*     */   
/*     */   boolean contains(Object paramObject);
/*     */   
/*     */   int indexOf(Object paramObject);
/*     */   
/*     */   boolean containsAll(Collection<?> paramCollection);
/*     */   
/*     */   int lastIndexOf(Object paramObject);
/*     */   
/*     */   Object[] toArray();
/*     */   
/*     */   <E> E[] toArray(E[] paramArrayOfE);
/*     */   
/*     */   List<T> subList(int paramInt1, int paramInt2);
/*     */   
/*     */   default List<T> toListCopy() {
/* 112 */     List<T> list = new ArrayList<>();
/* 113 */     Objects.requireNonNull(list); iterator().forEachRemaining(list::add);
/* 114 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\iface\ReadableNBTList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */