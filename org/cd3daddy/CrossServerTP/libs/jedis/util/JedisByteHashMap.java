/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class JedisByteHashMap implements Map<byte[], byte[]>, Cloneable, Serializable {
/*     */   private static final long serialVersionUID = -6971431362627219416L;
/*  14 */   private final Map<ByteArrayWrapper, byte[]> internalMap = (Map)new HashMap<>();
/*     */ 
/*     */   
/*     */   public void clear() {
/*  18 */     this.internalMap.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/*  23 */     if (key instanceof byte[]) return this.internalMap.containsKey(new ByteArrayWrapper((byte[])key)); 
/*  24 */     return this.internalMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/*  29 */     return this.internalMap.containsValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<byte[], byte[]>> entrySet() {
/*  35 */     Iterator<Map.Entry<ByteArrayWrapper, byte[]>> iterator = this.internalMap.entrySet().iterator();
/*  36 */     HashSet<Map.Entry<byte[], byte[]>> hashSet = new HashSet<>();
/*  37 */     while (iterator.hasNext()) {
/*  38 */       Map.Entry<ByteArrayWrapper, byte[]> entry = iterator.next();
/*  39 */       hashSet.add(new JedisByteEntry((entry.getKey()).data, entry.getValue()));
/*     */     } 
/*  41 */     return hashSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] get(Object key) {
/*  46 */     if (key instanceof byte[]) return this.internalMap.get(new ByteArrayWrapper((byte[])key)); 
/*  47 */     return this.internalMap.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  52 */     return this.internalMap.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<byte[]> keySet() {
/*  57 */     Set<byte[]> keySet = (Set)new HashSet<>();
/*  58 */     Iterator<ByteArrayWrapper> iterator = this.internalMap.keySet().iterator();
/*  59 */     while (iterator.hasNext()) {
/*  60 */       keySet.add((iterator.next()).data);
/*     */     }
/*  62 */     return keySet;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] put(byte[] key, byte[] value) {
/*  67 */     return this.internalMap.put(new ByteArrayWrapper(key), value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends byte[], ? extends byte[]> m) {
/*  73 */     Iterator<?> iterator = m.entrySet().iterator();
/*  74 */     while (iterator.hasNext()) {
/*     */       
/*  76 */       Map.Entry<? extends byte[], ? extends byte[]> next = (Map.Entry<? extends byte[], ? extends byte[]>)iterator.next();
/*  77 */       this.internalMap.put(new ByteArrayWrapper(next.getKey()), next.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] remove(Object key) {
/*  83 */     if (key instanceof byte[]) return this.internalMap.remove(new ByteArrayWrapper((byte[])key)); 
/*  84 */     return this.internalMap.remove(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  89 */     return this.internalMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<byte[]> values() {
/*  94 */     return (Collection)this.internalMap.values();
/*     */   }
/*     */   
/*     */   private static final class ByteArrayWrapper implements Serializable {
/*     */     private final byte[] data;
/*     */     
/*     */     public ByteArrayWrapper(byte[] data) {
/* 101 */       if (data == null) {
/* 102 */         throw new NullPointerException();
/*     */       }
/* 104 */       this.data = data;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 109 */       if (other == null) return false; 
/* 110 */       if (other == this) return true; 
/* 111 */       if (!(other instanceof ByteArrayWrapper)) return false;
/*     */       
/* 113 */       return Arrays.equals(this.data, ((ByteArrayWrapper)other).data);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 118 */       return Arrays.hashCode(this.data);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class JedisByteEntry implements Map.Entry<byte[], byte[]> {
/*     */     private byte[] value;
/*     */     private byte[] key;
/*     */     
/*     */     public JedisByteEntry(byte[] key, byte[] value) {
/* 127 */       this.key = key;
/* 128 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getKey() {
/* 133 */       return this.key;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] getValue() {
/* 138 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public byte[] setValue(byte[] value) {
/* 143 */       this.value = value;
/* 144 */       return value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\JedisByteHashMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */