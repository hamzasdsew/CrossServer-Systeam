/*      */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*      */ 
/*      */ import java.io.OutputStream;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import java.util.concurrent.locks.ReadWriteLock;
/*      */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*      */ import org.bukkit.inventory.ItemStack;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.NBTHandler;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBTCompoundList;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBTList;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.CheckUtil;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.PathUtil;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.UUIDUtil;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.Forge1710Mappings;
/*      */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NBTCompound
/*      */   implements ReadWriteNBT
/*      */ {
/*   37 */   private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*   38 */   private final Lock readLock = this.readWriteLock.readLock();
/*   39 */   private final Lock writeLock = this.readWriteLock.writeLock();
/*      */   
/*      */   private String compundName;
/*      */   private NBTCompound parent;
/*      */   private final boolean readOnly;
/*      */   private Object readOnlyCache;
/*      */   
/*      */   protected NBTCompound(NBTCompound owner, String name) {
/*   47 */     this(owner, name, false);
/*      */   }
/*      */   
/*      */   protected NBTCompound(NBTCompound owner, String name, boolean readOnly) {
/*   51 */     this.compundName = name;
/*   52 */     this.parent = owner;
/*   53 */     this.readOnly = readOnly;
/*      */   }
/*      */   
/*      */   protected Lock getReadLock() {
/*   57 */     return this.readLock;
/*      */   }
/*      */   
/*      */   protected Lock getWriteLock() {
/*   61 */     return this.writeLock;
/*      */   }
/*      */   
/*      */   protected void saveCompound() {
/*   65 */     if (this.parent != null)
/*   66 */       this.parent.saveCompound(); 
/*      */   }
/*      */   
/*      */   protected void setResolvedObject(Object object) {
/*   70 */     if (isClosed()) {
/*   71 */       throw new NbtApiException("Tried using closed NBT data!");
/*      */     }
/*   73 */     if (this.readOnly) {
/*   74 */       this.readOnlyCache = object;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setClosed() {
/*   79 */     if (this.parent != null) {
/*   80 */       this.parent.setClosed();
/*      */     }
/*      */   }
/*      */   
/*      */   protected boolean isClosed() {
/*   85 */     if (this.parent != null) {
/*   86 */       return this.parent.isClosed();
/*      */     }
/*   88 */     return false;
/*      */   }
/*      */   
/*      */   protected boolean isReadOnly() {
/*   92 */     return this.readOnly;
/*      */   }
/*      */   
/*      */   protected Object getResolvedObject() {
/*   96 */     if (isClosed()) {
/*   97 */       throw new NbtApiException("Tried using closed NBT data!");
/*      */     }
/*   99 */     if (this.readOnlyCache != null) {
/*  100 */       return this.readOnlyCache;
/*      */     }
/*  102 */     Object rootnbttag = getCompound();
/*  103 */     if (rootnbttag instanceof Optional) {
/*  104 */       rootnbttag = ((Optional)rootnbttag).orElse(null);
/*      */     }
/*  106 */     if (rootnbttag == null) {
/*  107 */       return null;
/*      */     }
/*  109 */     if (!NBTReflectionUtil.validCompound(this))
/*  110 */       throw new NbtApiException("The Compound wasn't able to be linked back to the root!"); 
/*  111 */     Object workingtag = NBTReflectionUtil.getToCompount(rootnbttag, this);
/*  112 */     if (this.readOnly) {
/*  113 */       this.readOnlyCache = workingtag;
/*      */     }
/*  115 */     return workingtag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getName() {
/*  122 */     return this.compundName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getCompound() {
/*  129 */     return this.parent.getCompound();
/*      */   }
/*      */   
/*      */   protected void setCompound(Object compound) {
/*  133 */     this.parent.setCompound(compound);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTCompound getParent() {
/*  140 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void mergeCompound(NBTCompound comp) {
/*  150 */     if (comp == null) {
/*      */       return;
/*      */     }
/*      */     try {
/*  154 */       this.writeLock.lock();
/*  155 */       NBTReflectionUtil.mergeOtherNBTCompound(this, comp);
/*  156 */       saveCompound();
/*      */     } finally {
/*  158 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void mergeCompound(ReadableNBT comp) {
/*  164 */     if (comp instanceof NBTCompound) {
/*  165 */       mergeCompound((NBTCompound)comp);
/*      */     } else {
/*  167 */       throw new NbtApiException("Unknown NBT object: " + comp);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setString(String key, String value) {
/*      */     try {
/*  180 */       this.writeLock.lock();
/*  181 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_STRING, key, value);
/*  182 */       saveCompound();
/*      */     } finally {
/*  184 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getString(String key) {
/*      */     try {
/*  197 */       this.readLock.lock();
/*  198 */       return (String)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_STRING, key);
/*      */     } finally {
/*  200 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setInteger(String key, Integer value) {
/*      */     try {
/*  213 */       this.writeLock.lock();
/*  214 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_INT, key, value);
/*  215 */       saveCompound();
/*      */     } finally {
/*  217 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer getInteger(String key) {
/*      */     try {
/*  230 */       this.readLock.lock();
/*  231 */       return (Integer)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_INT, key);
/*      */     } finally {
/*  233 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setDouble(String key, Double value) {
/*      */     try {
/*  246 */       this.writeLock.lock();
/*  247 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_DOUBLE, key, value);
/*  248 */       saveCompound();
/*      */     } finally {
/*  250 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Double getDouble(String key) {
/*      */     try {
/*  263 */       this.readLock.lock();
/*  264 */       return (Double)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_DOUBLE, key);
/*      */     } finally {
/*  266 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setByte(String key, Byte value) {
/*      */     try {
/*  279 */       this.writeLock.lock();
/*  280 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BYTE, key, value);
/*  281 */       saveCompound();
/*      */     } finally {
/*  283 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Byte getByte(String key) {
/*      */     try {
/*  296 */       this.readLock.lock();
/*  297 */       return (Byte)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BYTE, key);
/*      */     } finally {
/*  299 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShort(String key, Short value) {
/*      */     try {
/*  312 */       this.writeLock.lock();
/*  313 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_SHORT, key, value);
/*  314 */       saveCompound();
/*      */     } finally {
/*  316 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Short getShort(String key) {
/*      */     try {
/*  329 */       this.readLock.lock();
/*  330 */       return (Short)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_SHORT, key);
/*      */     } finally {
/*  332 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLong(String key, Long value) {
/*      */     try {
/*  345 */       this.writeLock.lock();
/*  346 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_LONG, key, value);
/*  347 */       saveCompound();
/*      */     } finally {
/*  349 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Long getLong(String key) {
/*      */     try {
/*  362 */       this.readLock.lock();
/*  363 */       return (Long)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_LONG, key);
/*      */     } finally {
/*  365 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setFloat(String key, Float value) {
/*      */     try {
/*  378 */       this.writeLock.lock();
/*  379 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_FLOAT, key, value);
/*  380 */       saveCompound();
/*      */     } finally {
/*  382 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Float getFloat(String key) {
/*      */     try {
/*  395 */       this.readLock.lock();
/*  396 */       return (Float)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_FLOAT, key);
/*      */     } finally {
/*  398 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setByteArray(String key, byte[] value) {
/*      */     try {
/*  411 */       this.writeLock.lock();
/*  412 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BYTEARRAY, key, value);
/*  413 */       saveCompound();
/*      */     } finally {
/*  415 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getByteArray(String key) {
/*      */     try {
/*  428 */       this.readLock.lock();
/*  429 */       return (byte[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BYTEARRAY, key);
/*      */     } finally {
/*  431 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setIntArray(String key, int[] value) {
/*      */     try {
/*  444 */       this.writeLock.lock();
/*  445 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_INTARRAY, key, value);
/*  446 */       saveCompound();
/*      */     } finally {
/*  448 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int[] getIntArray(String key) {
/*      */     try {
/*  461 */       this.readLock.lock();
/*  462 */       return (int[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_INTARRAY, key);
/*      */     } finally {
/*  464 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setLongArray(String key, long[] value) {
/*  478 */     CheckUtil.assertAvailable(MinecraftVersion.MC1_16_R1);
/*      */     try {
/*  480 */       this.writeLock.lock();
/*  481 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_LONGARRAY, key, value);
/*  482 */       saveCompound();
/*      */     } finally {
/*  484 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public long[] getLongArray(String key) {
/*  498 */     CheckUtil.assertAvailable(MinecraftVersion.MC1_16_R1);
/*      */     try {
/*  500 */       this.readLock.lock();
/*  501 */       return (long[])NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_LONGARRAY, key);
/*      */     } finally {
/*  503 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBoolean(String key, Boolean value) {
/*      */     try {
/*  516 */       this.writeLock.lock();
/*  517 */       NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_BOOLEAN, key, value);
/*  518 */       saveCompound();
/*      */     } finally {
/*  520 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void set(String key, Object val) {
/*  525 */     NBTReflectionUtil.set(this, key, val);
/*  526 */     saveCompound();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Boolean getBoolean(String key) {
/*      */     try {
/*  538 */       this.readLock.lock();
/*  539 */       return (Boolean)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_BOOLEAN, key);
/*      */     } finally {
/*  541 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public void setObject(String key, Object value) {
/*      */     try {
/*  555 */       this.writeLock.lock();
/*  556 */       NBTReflectionUtil.setObject(this, key, value);
/*  557 */       saveCompound();
/*      */     } finally {
/*  559 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T getObject(String key, Class<T> type) {
/*      */     try {
/*  574 */       this.readLock.lock();
/*  575 */       return (T)NBTReflectionUtil.getObject(this, key, (Class)type);
/*      */     } finally {
/*  577 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setItemStack(String key, ItemStack item) {
/*      */     try {
/*  590 */       this.writeLock.lock();
/*  591 */       removeKey(key);
/*  592 */       addCompound(key).mergeCompound(NBTItem.convertItemtoNBT(item));
/*      */     } finally {
/*  594 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack getItemStack(String key) {
/*      */     try {
/*  607 */       this.readLock.lock();
/*  608 */       NBTCompound comp = getCompound(key);
/*  609 */       if (comp == null)
/*  610 */         return null; 
/*  611 */       return NBTItem.convertNBTtoItem(comp);
/*      */     } finally {
/*  613 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setItemStackArray(String key, ItemStack[] items) {
/*      */     try {
/*  626 */       this.writeLock.lock();
/*  627 */       removeKey(key);
/*  628 */       addCompound(key).mergeCompound(NBTItem.convertItemArraytoNBT(items));
/*      */     } finally {
/*  630 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack[] getItemStackArray(String key) {
/*      */     try {
/*  645 */       this.readLock.lock();
/*  646 */       NBTCompound comp = getCompound(key);
/*  647 */       if (comp == null)
/*  648 */         return null; 
/*  649 */       return NBTItem.convertNBTtoItemArray(comp);
/*      */     } finally {
/*  651 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUUID(String key, UUID value) {
/*      */     try {
/*  664 */       this.writeLock.lock();
/*  665 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
/*  666 */         setIntArray(key, UUIDUtil.uuidToIntArray(value));
/*  667 */       } else if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1)) {
/*  668 */         NBTReflectionUtil.setData(this, ReflectionMethod.COMPOUND_SET_UUID, key, value);
/*      */       } else {
/*  670 */         setString(key, value.toString());
/*      */       } 
/*  672 */       saveCompound();
/*      */     } finally {
/*  674 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public UUID getUUID(String key) {
/*      */     try {
/*  687 */       this.readLock.lock();
/*  688 */       NBTType type = getType(key);
/*  689 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4) && type == NBTType.NBTTagIntArray)
/*  690 */         return UUIDUtil.uuidFromIntArray(getIntArray(key)); 
/*  691 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R1) && type == NBTType.NBTTagIntArray)
/*      */       {
/*  693 */         return (UUID)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_UUID, key); } 
/*  694 */       if (type == NBTType.NBTTagString) {
/*      */         try {
/*  696 */           return UUID.fromString(getString(key));
/*  697 */         } catch (IllegalArgumentException ex) {
/*  698 */           return null;
/*      */         } 
/*      */       }
/*  701 */       return null;
/*      */     } finally {
/*      */       
/*  704 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Boolean hasKey(String key) {
/*  717 */     return Boolean.valueOf(hasTag(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasTag(String key) {
/*      */     try {
/*  729 */       this.readLock.lock();
/*  730 */       Boolean b = (Boolean)NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_HAS_KEY, key);
/*  731 */       if (b == null)
/*  732 */         return false; 
/*  733 */       return b.booleanValue();
/*      */     } finally {
/*  735 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void removeKey(String key) {
/*      */     try {
/*  745 */       this.writeLock.lock();
/*  746 */       NBTReflectionUtil.remove(this, key);
/*  747 */       saveCompound();
/*      */     } finally {
/*  749 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> getKeys() {
/*      */     try {
/*  759 */       this.readLock.lock();
/*  760 */       return new HashSet<>(NBTReflectionUtil.getKeys(this));
/*      */     } finally {
/*  762 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTCompound addCompound(String name) {
/*      */     try {
/*  774 */       this.writeLock.lock();
/*  775 */       if (getType(name) == NBTType.NBTTagCompound)
/*  776 */         return getCompound(name); 
/*  777 */       NBTReflectionUtil.addNBTTagCompound(this, name);
/*  778 */       NBTCompound comp = getCompound(name);
/*  779 */       if (comp == null)
/*  780 */         throw new NbtApiException("Error while adding Compound, got null!"); 
/*  781 */       saveCompound();
/*  782 */       return comp;
/*      */     } finally {
/*  784 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTCompound getCompound(String name) {
/*      */     try {
/*  795 */       this.readLock.lock();
/*  796 */       if (getType(name) != NBTType.NBTTagCompound)
/*  797 */         return null; 
/*  798 */       NBTCompound next = new NBTCompound(this, name, this.readOnly);
/*  799 */       if (NBTReflectionUtil.validCompound(next))
/*  800 */         return next; 
/*  801 */       return null;
/*      */     } finally {
/*  803 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTCompound getOrCreateCompound(String name) {
/*  815 */     return addCompound(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTList<String> getStringList(String name) {
/*      */     try {
/*  825 */       this.writeLock.lock();
/*  826 */       NBTList<String> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagString, String.class);
/*  827 */       saveCompound();
/*  828 */       return list;
/*      */     } finally {
/*  830 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTList<Integer> getIntegerList(String name) {
/*      */     try {
/*  841 */       this.writeLock.lock();
/*  842 */       NBTList<Integer> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagInt, Integer.class);
/*  843 */       saveCompound();
/*  844 */       return list;
/*      */     } finally {
/*  846 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTList<int[]> getIntArrayList(String name) {
/*      */     try {
/*  857 */       this.writeLock.lock();
/*  858 */       NBTList<int[]> list = (NBTList)NBTReflectionUtil.getList(this, name, NBTType.NBTTagIntArray, (Class)int[].class);
/*  859 */       saveCompound();
/*  860 */       return list;
/*      */     } finally {
/*  862 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTList<UUID> getUUIDList(String name) {
/*      */     try {
/*  873 */       this.writeLock.lock();
/*  874 */       NBTList<UUID> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagIntArray, UUID.class);
/*  875 */       saveCompound();
/*  876 */       return list;
/*      */     } finally {
/*  878 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTList<Float> getFloatList(String name) {
/*      */     try {
/*  889 */       this.writeLock.lock();
/*  890 */       NBTList<Float> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagFloat, Float.class);
/*  891 */       saveCompound();
/*  892 */       return list;
/*      */     } finally {
/*  894 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTList<Double> getDoubleList(String name) {
/*      */     try {
/*  905 */       this.writeLock.lock();
/*  906 */       NBTList<Double> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagDouble, Double.class);
/*  907 */       saveCompound();
/*  908 */       return list;
/*      */     } finally {
/*  910 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTList<Long> getLongList(String name) {
/*      */     try {
/*  921 */       this.writeLock.lock();
/*  922 */       NBTList<Long> list = NBTReflectionUtil.getList(this, name, NBTType.NBTTagLong, Long.class);
/*  923 */       saveCompound();
/*  924 */       return list;
/*      */     } finally {
/*  926 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTType getListType(String name) {
/*      */     try {
/*  939 */       this.readLock.lock();
/*  940 */       if (getType(name) != NBTType.NBTTagList)
/*  941 */         return null; 
/*  942 */       return NBTReflectionUtil.getListType(this, name);
/*      */     } finally {
/*  944 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTCompoundList getCompoundList(String name) {
/*      */     try {
/*  955 */       this.writeLock.lock();
/*  956 */       NBTCompoundList list = (NBTCompoundList)NBTReflectionUtil.<NBTListCompound>getList(this, name, NBTType.NBTTagCompound, NBTListCompound.class);
/*      */       
/*  958 */       saveCompound();
/*  959 */       return list;
/*      */     } finally {
/*  961 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getOrDefault(String key, T defaultValue) {
/*  980 */     if (defaultValue == null)
/*  981 */       throw new NullPointerException("Default type in getOrDefault can't be null!"); 
/*  982 */     if (!hasTag(key)) {
/*  983 */       return defaultValue;
/*      */     }
/*  985 */     Class<?> clazz = defaultValue.getClass();
/*  986 */     if (clazz == Boolean.class || clazz == boolean.class)
/*  987 */       return (T)getBoolean(key); 
/*  988 */     if (clazz == Byte.class || clazz == byte.class)
/*  989 */       return (T)getByte(key); 
/*  990 */     if (clazz == Short.class || clazz == short.class)
/*  991 */       return (T)getShort(key); 
/*  992 */     if (clazz == Integer.class || clazz == int.class)
/*  993 */       return (T)getInteger(key); 
/*  994 */     if (clazz == Long.class || clazz == long.class)
/*  995 */       return (T)getLong(key); 
/*  996 */     if (clazz == Float.class || clazz == float.class)
/*  997 */       return (T)getFloat(key); 
/*  998 */     if (clazz == Double.class || clazz == double.class)
/*  999 */       return (T)getDouble(key); 
/* 1000 */     if (clazz == byte[].class)
/* 1001 */       return (T)getByteArray(key); 
/* 1002 */     if (clazz == int[].class)
/* 1003 */       return (T)getIntArray(key); 
/* 1004 */     if (clazz == long[].class)
/* 1005 */       return (T)getLongArray(key); 
/* 1006 */     if (clazz == String.class)
/* 1007 */       return (T)getString(key); 
/* 1008 */     if (clazz == UUID.class) {
/* 1009 */       UUID uuid = getUUID(key);
/* 1010 */       return (uuid == null) ? defaultValue : (T)uuid;
/*      */     } 
/* 1012 */     if (clazz.isEnum()) {
/*      */       
/* 1014 */       Object obj = getEnum(key, defaultValue.getClass());
/* 1015 */       return (obj == null) ? defaultValue : (T)obj;
/*      */     } 
/*      */     
/* 1018 */     throw new NbtApiException("Unsupported type for getOrDefault: " + clazz.getName());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getOrNull(String key, Class<?> type) {
/* 1036 */     if (type == null)
/* 1037 */       throw new NullPointerException("Default type in getOrNull can't be null!"); 
/* 1038 */     if (!hasTag(key)) {
/* 1039 */       return null;
/*      */     }
/* 1041 */     if (type == Boolean.class || type == boolean.class)
/* 1042 */       return (T)getBoolean(key); 
/* 1043 */     if (type == Byte.class || type == byte.class)
/* 1044 */       return (T)getByte(key); 
/* 1045 */     if (type == Short.class || type == short.class)
/* 1046 */       return (T)getShort(key); 
/* 1047 */     if (type == Integer.class || type == int.class)
/* 1048 */       return (T)getInteger(key); 
/* 1049 */     if (type == Long.class || type == long.class)
/* 1050 */       return (T)getLong(key); 
/* 1051 */     if (type == Float.class || type == float.class)
/* 1052 */       return (T)getFloat(key); 
/* 1053 */     if (type == Double.class || type == double.class)
/* 1054 */       return (T)getDouble(key); 
/* 1055 */     if (type == byte[].class)
/* 1056 */       return (T)getByteArray(key); 
/* 1057 */     if (type == int[].class)
/* 1058 */       return (T)getIntArray(key); 
/* 1059 */     if (type == long[].class)
/* 1060 */       return (T)getLongArray(key); 
/* 1061 */     if (type == String.class)
/* 1062 */       return (T)getString(key); 
/* 1063 */     if (type == UUID.class)
/* 1064 */       return (T)getUUID(key); 
/* 1065 */     if (type.isEnum()) {
/* 1066 */       return getEnum(key, (Class)type);
/*      */     }
/* 1068 */     throw new NbtApiException("Unsupported type for getOrNull: " + type.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T resolveOrNull(String key, Class<?> type) {
/* 1073 */     List<PathUtil.PathSegment> keys = PathUtil.splitPath(key);
/* 1074 */     NBTCompound tag = this;
/* 1075 */     for (int i = 0; i < keys.size() - 1; i++) {
/* 1076 */       PathUtil.PathSegment pathSegment = keys.get(i);
/* 1077 */       if (!pathSegment.hasIndex()) {
/* 1078 */         tag = tag.getCompound(pathSegment.getPath());
/* 1079 */         if (tag == null) {
/* 1080 */           return null;
/*      */         }
/*      */       }
/* 1083 */       else if (tag.getType(pathSegment.getPath()) == NBTType.NBTTagList && tag
/* 1084 */         .getListType(pathSegment.getPath()) == NBTType.NBTTagCompound) {
/* 1085 */         NBTCompoundList list = tag.getCompoundList(pathSegment.getPath());
/* 1086 */         if (pathSegment.getIndex() >= 0) {
/* 1087 */           tag = list.get(pathSegment.getIndex());
/*      */         } else {
/* 1089 */           tag = list.get(list.size() + pathSegment.getIndex());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1094 */     PathUtil.PathSegment segment = keys.get(keys.size() - 1);
/* 1095 */     if (!segment.hasIndex()) {
/* 1096 */       return tag.getOrNull(segment.getPath(), type);
/*      */     }
/* 1098 */     return getIndexedValue(tag, segment, (Class)type);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T resolveOrDefault(String key, T defaultValue) {
/* 1104 */     List<PathUtil.PathSegment> keys = PathUtil.splitPath(key);
/* 1105 */     NBTCompound tag = this;
/* 1106 */     for (int i = 0; i < keys.size() - 1; i++) {
/* 1107 */       PathUtil.PathSegment pathSegment = keys.get(i);
/* 1108 */       if (!pathSegment.hasIndex()) {
/* 1109 */         tag = tag.getCompound(pathSegment.getPath());
/* 1110 */         if (tag == null) {
/* 1111 */           return defaultValue;
/*      */         }
/*      */       }
/* 1114 */       else if (tag.getType(pathSegment.getPath()) == NBTType.NBTTagList && tag
/* 1115 */         .getListType(pathSegment.getPath()) == NBTType.NBTTagCompound) {
/* 1116 */         NBTCompoundList list = tag.getCompoundList(pathSegment.getPath());
/* 1117 */         if (pathSegment.getIndex() >= 0) {
/* 1118 */           tag = list.get(pathSegment.getIndex());
/*      */         } else {
/* 1120 */           tag = list.get(list.size() + pathSegment.getIndex());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1125 */     PathUtil.PathSegment segment = keys.get(keys.size() - 1);
/* 1126 */     if (!segment.hasIndex()) {
/* 1127 */       return tag.getOrDefault(segment.getPath(), defaultValue);
/*      */     }
/* 1129 */     return getIndexedValue(tag, segment, (Class)defaultValue.getClass());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private <T> T getIndexedValue(NBTCompound comp, PathUtil.PathSegment segment, Class<T> type) {
/* 1136 */     if (type == String.class) {
/* 1137 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagList && comp
/* 1138 */         .getListType(segment.getPath()) == NBTType.NBTTagString) {
/* 1139 */         if (segment.getIndex() >= 0) {
/* 1140 */           return (T)comp.getStringList(segment.getPath()).get(segment.getIndex());
/*      */         }
/* 1142 */         List<String> list = comp.getStringList(segment.getPath());
/* 1143 */         return (T)list.get(list.size() + segment.getIndex());
/*      */       } 
/*      */       
/* 1146 */       throw new NbtApiException("No fitting list/array found for " + segment.getPath() + " of type " + type);
/*      */     } 
/* 1148 */     if (type == int.class || type == Integer.class) {
/* 1149 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagList && comp
/* 1150 */         .getListType(segment.getPath()) == NBTType.NBTTagInt) {
/* 1151 */         if (segment.getIndex() >= 0) {
/* 1152 */           return (T)comp.getIntegerList(segment.getPath()).get(segment.getIndex());
/*      */         }
/* 1154 */         List<Integer> list = comp.getIntegerList(segment.getPath());
/* 1155 */         return (T)list.get(list.size() + segment.getIndex());
/*      */       } 
/* 1157 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagIntArray) {
/* 1158 */         if (segment.getIndex() >= 0) {
/* 1159 */           int[] array = comp.getIntArray(segment.getPath());
/* 1160 */           if (array != null) {
/* 1161 */             return (T)Integer.valueOf(array[segment.getIndex()]);
/*      */           }
/*      */         } else {
/* 1164 */           int[] array = comp.getIntArray(segment.getPath());
/* 1165 */           if (array != null) {
/* 1166 */             return (T)Integer.valueOf(array[array.length + segment.getIndex()]);
/*      */           }
/*      */         } 
/*      */       }
/* 1170 */       throw new NbtApiException("No fitting list/array found for " + segment.getPath() + " of type " + type);
/*      */     } 
/* 1172 */     if (type == long.class || type == Long.class) {
/* 1173 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagList && comp
/* 1174 */         .getListType(segment.getPath()) == NBTType.NBTTagLong) {
/* 1175 */         if (segment.getIndex() >= 0) {
/* 1176 */           return (T)comp.getLongList(segment.getPath()).get(segment.getIndex());
/*      */         }
/* 1178 */         List<Long> list = comp.getLongList(segment.getPath());
/* 1179 */         return (T)list.get(list.size() + segment.getIndex());
/*      */       } 
/* 1181 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagLongArray) {
/* 1182 */         if (segment.getIndex() >= 0) {
/* 1183 */           long[] array = comp.getLongArray(segment.getPath());
/* 1184 */           if (array != null) {
/* 1185 */             return (T)Long.valueOf(array[segment.getIndex()]);
/*      */           }
/*      */         } else {
/* 1188 */           long[] array = comp.getLongArray(segment.getPath());
/* 1189 */           if (array != null) {
/* 1190 */             return (T)Long.valueOf(array[array.length + segment.getIndex()]);
/*      */           }
/*      */         } 
/*      */       }
/* 1194 */       throw new NbtApiException("No fitting list/array found for " + segment.getPath() + " of type " + type);
/*      */     } 
/* 1196 */     if (type == float.class || type == Float.class) {
/* 1197 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagList && comp
/* 1198 */         .getListType(segment.getPath()) == NBTType.NBTTagFloat) {
/* 1199 */         if (segment.getIndex() >= 0) {
/* 1200 */           return (T)comp.getFloatList(segment.getPath()).get(segment.getIndex());
/*      */         }
/* 1202 */         List<Float> list = comp.getFloatList(segment.getPath());
/* 1203 */         return (T)list.get(list.size() + segment.getIndex());
/*      */       } 
/*      */       
/* 1206 */       throw new NbtApiException("No fitting list/array found for " + segment.getPath() + " of type " + type);
/*      */     } 
/* 1208 */     if (type == double.class || type == Double.class) {
/* 1209 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagList && comp
/* 1210 */         .getListType(segment.getPath()) == NBTType.NBTTagDouble) {
/* 1211 */         if (segment.getIndex() >= 0) {
/* 1212 */           return (T)comp.getDoubleList(segment.getPath()).get(segment.getIndex());
/*      */         }
/* 1214 */         List<Double> list = comp.getDoubleList(segment.getPath());
/* 1215 */         return (T)list.get(list.size() + segment.getIndex());
/*      */       } 
/*      */       
/* 1218 */       throw new NbtApiException("No fitting list/array found for " + segment.getPath() + " of type " + type);
/*      */     } 
/* 1220 */     if (type == int[].class) {
/* 1221 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagList && comp
/* 1222 */         .getListType(segment.getPath()) == NBTType.NBTTagIntArray) {
/* 1223 */         if (segment.getIndex() >= 0) {
/* 1224 */           return (T)comp.getIntArrayList(segment.getPath()).get(segment.getIndex());
/*      */         }
/* 1226 */         List<int[]> list = (List<int[]>)comp.getIntArrayList(segment.getPath());
/* 1227 */         return (T)list.get(list.size() + segment.getIndex());
/*      */       } 
/*      */       
/* 1230 */       throw new NbtApiException("No fitting list/array found for " + segment.getPath() + " of type " + type);
/*      */     } 
/* 1232 */     if (type == byte.class || type == Byte.class) {
/* 1233 */       if (comp.getType(segment.getPath()) == NBTType.NBTTagByteArray) {
/* 1234 */         if (segment.getIndex() >= 0) {
/* 1235 */           byte[] array = comp.getByteArray(segment.getPath());
/* 1236 */           if (array != null) {
/* 1237 */             return (T)Byte.valueOf(array[segment.getIndex()]);
/*      */           }
/*      */         } else {
/* 1240 */           byte[] array = comp.getByteArray(segment.getPath());
/* 1241 */           if (array != null) {
/* 1242 */             return (T)Byte.valueOf(array[array.length + segment.getIndex()]);
/*      */           }
/*      */         } 
/*      */       }
/* 1246 */       throw new NbtApiException("No fitting list/array found for " + segment.getPath() + " of type " + type);
/*      */     } 
/* 1248 */     throw new NbtApiException("Unable to get indexed value for type " + type);
/*      */   }
/*      */ 
/*      */   
/*      */   public ReadWriteNBT resolveCompound(String key) {
/* 1253 */     List<PathUtil.PathSegment> keys = PathUtil.splitPath(key);
/* 1254 */     NBTCompound tag = this;
/* 1255 */     for (int i = 0; i < keys.size(); i++) {
/* 1256 */       PathUtil.PathSegment segment = keys.get(i);
/* 1257 */       if (!segment.hasIndex()) {
/* 1258 */         tag = tag.getCompound(segment.getPath());
/* 1259 */         if (tag == null) {
/* 1260 */           return null;
/*      */         }
/*      */       }
/* 1263 */       else if (tag.getType(segment.getPath()) == NBTType.NBTTagList && tag
/* 1264 */         .getListType(segment.getPath()) == NBTType.NBTTagCompound) {
/* 1265 */         NBTCompoundList list = tag.getCompoundList(segment.getPath());
/* 1266 */         if (segment.getIndex() >= 0) {
/* 1267 */           tag = list.get(segment.getIndex());
/*      */         } else {
/* 1269 */           tag = list.get(list.size() + segment.getIndex());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1274 */     return tag;
/*      */   }
/*      */ 
/*      */   
/*      */   public ReadWriteNBT resolveOrCreateCompound(String key) {
/* 1279 */     List<PathUtil.PathSegment> keys = PathUtil.splitPath(key);
/* 1280 */     NBTCompound tag = this;
/* 1281 */     for (int i = 0; i < keys.size(); i++) {
/* 1282 */       PathUtil.PathSegment segment = keys.get(i);
/* 1283 */       if (!segment.hasIndex()) {
/* 1284 */         tag = tag.getOrCreateCompound(segment.getPath());
/* 1285 */         if (tag == null) {
/* 1286 */           return null;
/*      */         }
/*      */       }
/* 1289 */       else if (tag.getType(segment.getPath()) == NBTType.NBTTagList && tag
/* 1290 */         .getListType(segment.getPath()) == NBTType.NBTTagCompound) {
/* 1291 */         NBTCompoundList list = tag.getCompoundList(segment.getPath());
/* 1292 */         if (segment.getIndex() >= 0) {
/* 1293 */           tag = list.get(segment.getIndex());
/*      */         } else {
/* 1295 */           tag = list.get(list.size() + segment.getIndex());
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1300 */     return tag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<?>> void setEnum(String key, E value) {
/* 1313 */     if (value == null) {
/* 1314 */       removeKey(key);
/*      */       return;
/*      */     } 
/* 1317 */     setString(key, value.name());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getEnum(String key, Class<E> type) {
/* 1331 */     if (key == null || type == null) {
/* 1332 */       return null;
/*      */     }
/* 1334 */     String name = getString(key);
/* 1335 */     if (name == null)
/* 1336 */       return null; 
/*      */     try {
/* 1338 */       return Enum.valueOf(type, name);
/* 1339 */     } catch (IllegalArgumentException ex) {
/* 1340 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public NBTType getType(String name) {
/*      */     try {
/* 1351 */       this.readLock.lock();
/* 1352 */       if (MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
/* 1353 */         Object nbtbase = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET, name);
/* 1354 */         if (nbtbase == null)
/* 1355 */           return null; 
/* 1356 */         return NBTType.valueOf(((Byte)ReflectionMethod.COMPOUND_OWN_TYPE_LEGACY.run(nbtbase, new Object[0])).byteValue());
/*      */       } 
/* 1358 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R4)) {
/* 1359 */         Object object = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET, name);
/* 1360 */         if (object == null)
/* 1361 */           return null; 
/* 1362 */         return NBTType.fromName((String)ReflectionMethod.TAGTYPE_GET_NAME.run(ReflectionMethod.TAGTYPE_OWN_TYPE.run(object, new Object[0]), new Object[0]));
/*      */       } 
/* 1364 */       Object o = NBTReflectionUtil.getData(this, ReflectionMethod.COMPOUND_GET_TYPE, name);
/* 1365 */       if (o == null)
/* 1366 */         return null; 
/* 1367 */       return NBTType.valueOf(((Byte)o).byteValue());
/*      */     } finally {
/* 1369 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeCompound(OutputStream stream) {
/*      */     try {
/* 1376 */       this.writeLock.lock();
/* 1377 */       NBTReflectionUtil.writeApiNBT(this, stream);
/*      */     } finally {
/* 1379 */       this.writeLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> T get(String key, NBTHandler<T> handler) {
/* 1385 */     return (T)handler.get((ReadableNBT)this, key);
/*      */   }
/*      */ 
/*      */   
/*      */   public <T> void set(String key, T value, NBTHandler<T> handler) {
/* 1390 */     handler.set(this, key, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1399 */     return asNBTString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String toString(String key) {
/* 1416 */     return asNBTString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearNBT() {
/* 1424 */     for (String key : getKeys()) {
/* 1425 */       removeKey(key);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public String asNBTString() {
/*      */     try {
/* 1437 */       this.readLock.lock();
/* 1438 */       Object comp = getResolvedObject();
/* 1439 */       if (comp == null)
/* 1440 */         return "{}"; 
/* 1441 */       if (MinecraftVersion.isForgePresent() && MinecraftVersion.getVersion() == MinecraftVersion.MC1_7_R4) {
/* 1442 */         return Forge1710Mappings.toString(comp);
/*      */       }
/* 1444 */       return comp.toString();
/*      */     } finally {
/*      */       
/* 1447 */       this.readLock.unlock();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1453 */     return toString().hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1461 */     if (this == obj)
/* 1462 */       return true; 
/* 1463 */     if (obj == null)
/* 1464 */       return false; 
/* 1465 */     if (obj instanceof NBTCompound) {
/* 1466 */       NBTCompound other = (NBTCompound)obj;
/* 1467 */       if (getKeys().equals(other.getKeys())) {
/* 1468 */         for (String key : getKeys()) {
/* 1469 */           if (!isEqual(this, other, key)) {
/* 1470 */             return false;
/*      */           }
/*      */         } 
/* 1473 */         return true;
/*      */       } 
/*      */     } 
/* 1476 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public NBTCompound extractDifference(ReadableNBT other) {
/* 1481 */     if (this == other) {
/* 1482 */       return new NBTContainer();
/*      */     }
/* 1484 */     if (other instanceof NBTCompound) {
/* 1485 */       return saveDiff(new NBTContainer(), this, (NBTCompound)other);
/*      */     }
/* 1487 */     throw new NbtApiException("Unknown NBT object: " + other);
/*      */   }
/*      */ 
/*      */   
/*      */   private static NBTCompound saveDiff(NBTCompound saveTo, NBTCompound compA, NBTCompound compB) {
/* 1492 */     for (String key : compA.getKeys()) {
/* 1493 */       saveDiff(saveTo, compA, compB, key);
/*      */     }
/* 1495 */     return saveTo;
/*      */   }
/*      */   private static void saveDiff(NBTCompound saveTo, NBTCompound compA, NBTCompound compB, String key) {
/*      */     NBTCompound tmp1;
/* 1499 */     boolean typeMismatch = (compA.getType(key) != compB.getType(key));
/* 1500 */     switch (compA.getType(key)) {
/*      */       case NBTTagByte:
/* 1502 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1503 */           saveTo.setByte(key, compA.getByte(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagByteArray:
/* 1507 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1508 */           saveTo.setByteArray(key, compA.getByteArray(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagCompound:
/* 1512 */         tmp1 = compA.getCompound(key);
/* 1513 */         if (tmp1 == null)
/* 1514 */           return;  if (typeMismatch) {
/* 1515 */           saveTo.addCompound(key).mergeCompound(tmp1);
/*      */         } else {
/* 1517 */           NBTCompound tmp2 = compB.getCompound(key);
/* 1518 */           if (tmp2 == null) {
/* 1519 */             saveTo.addCompound(key).mergeCompound(tmp1);
/*      */             return;
/*      */           } 
/* 1522 */           NBTCompound tmpDiff = tmp1.extractDifference((ReadableNBT)tmp2);
/* 1523 */           if (!tmpDiff.getKeys().isEmpty()) {
/* 1524 */             saveTo.addCompound(key).mergeCompound(tmpDiff);
/*      */           }
/*      */         } 
/*      */         return;
/*      */       
/*      */       case NBTTagDouble:
/* 1530 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1531 */           saveTo.setDouble(key, compA.getDouble(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagEnd:
/*      */         return;
/*      */       case NBTTagFloat:
/* 1537 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1538 */           saveTo.setFloat(key, compA.getFloat(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagInt:
/* 1542 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1543 */           saveTo.setInteger(key, compA.getInteger(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagIntArray:
/* 1547 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1548 */           saveTo.setIntArray(key, compA.getIntArray(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagList:
/* 1552 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1553 */           saveTo.set(key, NBTReflectionUtil.getEntry(compA, key));
/*      */         }
/*      */         return;
/*      */       case NBTTagLong:
/* 1557 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1558 */           saveTo.setLong(key, compA.getLong(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagShort:
/* 1562 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1563 */           saveTo.setShort(key, compA.getShort(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagString:
/* 1567 */         if (typeMismatch || !isEqual(compA, compB, key)) {
/* 1568 */           saveTo.setString(key, compA.getString(key));
/*      */         }
/*      */         return;
/*      */       case NBTTagLongArray:
/* 1572 */         if (typeMismatch || !isEqual(compA, compB, key))
/* 1573 */           saveTo.setLongArray(key, compA.getLongArray(key)); 
/*      */         return;
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean isEqual(NBTCompound compA, NBTCompound compB, String key) {
/*      */     NBTCompound tmp;
/* 1580 */     if (compA.getType(key) != compB.getType(key))
/* 1581 */       return false; 
/* 1582 */     switch (compA.getType(key)) {
/*      */       case NBTTagByte:
/* 1584 */         return compA.getByte(key).equals(compB.getByte(key));
/*      */       case NBTTagByteArray:
/* 1586 */         return Arrays.equals(compA.getByteArray(key), compB.getByteArray(key));
/*      */       case NBTTagCompound:
/* 1588 */         tmp = compA.getCompound(key);
/* 1589 */         return (tmp != null && tmp.equals(compB.getCompound(key)));
/*      */       
/*      */       case NBTTagDouble:
/* 1592 */         return compA.getDouble(key).equals(compB.getDouble(key));
/*      */       case NBTTagEnd:
/* 1594 */         return true;
/*      */       case NBTTagFloat:
/* 1596 */         return compA.getFloat(key).equals(compB.getFloat(key));
/*      */       case NBTTagInt:
/* 1598 */         return compA.getInteger(key).equals(compB.getInteger(key));
/*      */       case NBTTagIntArray:
/* 1600 */         return Arrays.equals(compA.getIntArray(key), compB.getIntArray(key));
/*      */       case NBTTagList:
/* 1602 */         return NBTReflectionUtil.getEntry(compA, key).toString()
/* 1603 */           .equals(NBTReflectionUtil.getEntry(compB, key).toString());
/*      */       case NBTTagLong:
/* 1605 */         return compA.getLong(key).equals(compB.getLong(key));
/*      */       case NBTTagShort:
/* 1607 */         return compA.getShort(key).equals(compB.getShort(key));
/*      */       case NBTTagString:
/* 1609 */         return compA.getString(key).equals(compB.getString(key));
/*      */       case NBTTagLongArray:
/* 1611 */         return Arrays.equals(compA.getLongArray(key), compB.getLongArray(key));
/*      */     } 
/* 1613 */     return false;
/*      */   }
/*      */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTCompound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */