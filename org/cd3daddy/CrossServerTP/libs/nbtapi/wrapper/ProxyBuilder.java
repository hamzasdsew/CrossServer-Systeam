/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.wrapper;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.function.Function;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTType;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.NBTHandler;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*     */ 
/*     */ public class ProxyBuilder<T extends NBTProxy>
/*     */   implements InvocationHandler
/*     */ {
/*  20 */   private static final Map<Method, Function<Arguments, Object>> METHOD_CACHE = new ConcurrentHashMap<>();
/*     */   
/*     */   private final Class<T> target;
/*     */   private final ReadWriteNBT nbt;
/*     */   private boolean readOnly;
/*     */   
/*     */   public ProxyBuilder(ReadWriteNBT nbt, Class<T> target) {
/*  27 */     if (!target.isInterface()) {
/*  28 */       throw new NbtApiException("A proxy can only be built from an interface! Check the wiki for examples.");
/*     */     }
/*  30 */     this.target = target;
/*  31 */     this.nbt = nbt;
/*     */   }
/*     */   
/*     */   public ProxyBuilder<T> readOnly() {
/*  35 */     this.readOnly = true;
/*  36 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public T build() {
/*  41 */     NBTProxy nBTProxy = (NBTProxy)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { this.target }, this);
/*  42 */     nBTProxy.init();
/*  43 */     return (T)nBTProxy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*  48 */     METHOD_CACHE.computeIfAbsent(method, m -> createFunction((NBTProxy)proxy, m));
/*  49 */     return ((Function)METHOD_CACHE.get(method)).apply(new Arguments(this.target, (NBTProxy)proxy, this.readOnly, this.nbt, args));
/*     */   }
/*     */   
/*     */   private static class Arguments {
/*     */     Class<?> target;
/*     */     NBTProxy proxy;
/*     */     ReadWriteNBT nbt;
/*     */     Object[] args;
/*     */     boolean readOnly;
/*     */     
/*     */     public Arguments(Class<?> target, NBTProxy proxy, boolean readOnly, ReadWriteNBT nbt, Object[] args) {
/*  60 */       this.target = target;
/*  61 */       this.proxy = proxy;
/*  62 */       this.nbt = nbt;
/*  63 */       this.args = args;
/*  64 */       this.readOnly = readOnly;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static Function<Arguments, Object> createFunction(NBTProxy proxy, Method method) {
/*  70 */     if ("toString".equals(method.getName()) && method.getParameterCount() == 0 && method
/*  71 */       .getReturnType() == String.class) {
/*  72 */       return arguments -> arguments.nbt.toString();
/*     */     }
/*  74 */     if (method.isDefault()) {
/*  75 */       return arguments -> DefaultMethodInvoker.invokeDefault(arguments.target, arguments.proxy, method, arguments.args);
/*     */     }
/*     */     
/*  78 */     NBTTarget.Type action = getAction(method);
/*  79 */     if (action == NBTTarget.Type.SET) {
/*  80 */       String fieldName = getNBTName(proxy.getCasing(), method);
/*  81 */       return arguments -> {
/*     */           if (arguments.readOnly) {
/*     */             throw new NbtApiException("Tried calling a set method on a read only object.");
/*     */           }
/*     */           return setNBT(arguments.nbt, arguments.proxy, fieldName, arguments.args[0]);
/*     */         };
/*     */     } 
/*  88 */     if (action == NBTTarget.Type.GET) {
/*  89 */       Class<?> retType = method.getReturnType();
/*  90 */       String fieldName = getNBTName(proxy.getCasing(), method);
/*     */       
/*  92 */       if (retType.isInterface() && NBTProxy.class.isAssignableFrom(retType)) {
/*  93 */         return arguments -> {
/*     */             if (arguments.nbt.hasTag(fieldName) && arguments.nbt.getType(fieldName) != NBTType.NBTTagCompound) {
/*     */               throw new NbtApiException("Tried getting a '" + retType + "' proxy from the field '" + fieldName + "', but it's not a TagCompound!");
/*     */             }
/*     */             
/*     */             return (new ProxyBuilder<>(arguments.nbt.getOrCreateCompound(fieldName), retType)).build();
/*     */           };
/*     */       }
/*     */       
/* 102 */       if (retType == ProxyList.class) {
/*     */ 
/*     */         
/* 105 */         Class<?> parameterType = (Class)((ParameterizedType)method.getGenericReturnType()).getActualTypeArguments()[0];
/* 106 */         if (parameterType != null && parameterType.isInterface() && NBTProxy.class
/* 107 */           .isAssignableFrom(parameterType)) {
/* 108 */           return arguments -> new ProxiedList<>(arguments.nbt.getCompoundList(fieldName), parameterType);
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 113 */       NBTHandler<Object> handler = proxy.getHandler(retType);
/* 114 */       if (handler != null) {
/* 115 */         return arguments -> handler.get((ReadableNBT)arguments.nbt, fieldName);
/*     */       }
/* 117 */       return arguments -> arguments.nbt.getOrNull(fieldName, retType);
/*     */     } 
/* 119 */     if (action == NBTTarget.Type.HAS) {
/* 120 */       String fieldName = getNBTName(proxy.getCasing(), method);
/* 121 */       return arguments -> Boolean.valueOf(arguments.nbt.hasTag(fieldName));
/*     */     } 
/* 123 */     throw new IllegalArgumentException("The method '" + method
/* 124 */         .getName() + "' in '" + method.getDeclaringClass().getName() + "' can not be handled by the NBT-API. Please check the Wiki for examples!");
/*     */   }
/*     */ 
/*     */   
/*     */   private static NBTTarget.Type getAction(Method method) {
/* 129 */     NBTTarget target = method.<NBTTarget>getAnnotation(NBTTarget.class);
/* 130 */     if (target != null) {
/* 131 */       if (target.type() == NBTTarget.Type.HAS && method.getParameterCount() == 0 && method
/* 132 */         .getReturnType() == boolean.class) {
/* 133 */         return NBTTarget.Type.HAS;
/*     */       }
/* 135 */       if (target.type() == NBTTarget.Type.GET && method.getParameterCount() == 0) {
/* 136 */         return NBTTarget.Type.GET;
/*     */       }
/* 138 */       if (target.type() == NBTTarget.Type.SET && method.getParameterCount() == 1) {
/* 139 */         return NBTTarget.Type.SET;
/*     */       }
/*     */     } 
/* 142 */     if (method.getName().startsWith("set") && method.getParameterCount() == 1) {
/* 143 */       return NBTTarget.Type.SET;
/*     */     }
/* 145 */     if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
/* 146 */       return NBTTarget.Type.GET;
/*     */     }
/* 148 */     if (method.getName().startsWith("has") && method.getParameterCount() == 0 && method
/* 149 */       .getReturnType() == boolean.class) {
/* 150 */       return NBTTarget.Type.HAS;
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */   
/*     */   private static String getNBTName(Casing casing, Method method) {
/* 156 */     NBTTarget target = method.<NBTTarget>getAnnotation(NBTTarget.class);
/* 157 */     if (target != null) {
/* 158 */       return target.value();
/*     */     }
/* 160 */     return casing.convertString(method.getName().substring(3));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object setNBT(ReadWriteNBT nbt, NBTProxy proxy, String key, Object value) {
/* 166 */     if (value == null) {
/* 167 */       nbt.removeKey(key);
/* 168 */     } else if (value instanceof Boolean) {
/* 169 */       nbt.setBoolean(key, (Boolean)value);
/* 170 */     } else if (value instanceof Byte) {
/* 171 */       nbt.setByte(key, (Byte)value);
/* 172 */     } else if (value instanceof Short) {
/* 173 */       nbt.setShort(key, (Short)value);
/* 174 */     } else if (value instanceof Integer) {
/* 175 */       nbt.setInteger(key, (Integer)value);
/* 176 */     } else if (value instanceof Long) {
/* 177 */       nbt.setLong(key, (Long)value);
/* 178 */     } else if (value instanceof Float) {
/* 179 */       nbt.setFloat(key, (Float)value);
/* 180 */     } else if (value instanceof Double) {
/* 181 */       nbt.setDouble(key, (Double)value);
/* 182 */     } else if (value instanceof byte[]) {
/* 183 */       nbt.setByteArray(key, (byte[])value);
/* 184 */     } else if (value instanceof int[]) {
/* 185 */       nbt.setIntArray(key, (int[])value);
/* 186 */     } else if (value instanceof long[]) {
/* 187 */       nbt.setLongArray(key, (long[])value);
/* 188 */     } else if (value instanceof String) {
/* 189 */       nbt.setString(key, (String)value);
/* 190 */     } else if (value instanceof UUID) {
/* 191 */       nbt.setUUID(key, (UUID)value);
/* 192 */     } else if (value.getClass().isEnum()) {
/* 193 */       nbt.setEnum(key, (Enum)value);
/*     */     } else {
/*     */       
/* 196 */       NBTHandler<Object> handler = proxy.getHandler(value.getClass());
/* 197 */       if (handler != null) {
/* 198 */         handler.set(nbt, key, value);
/*     */       } else {
/* 200 */         for (NBTHandler<Object> nbth : proxy.getHandlers()) {
/* 201 */           if (nbth.fuzzyMatch(value)) {
/* 202 */             nbth.set(nbt, key, value);
/* 203 */             return null;
/*     */           } 
/*     */         } 
/* 206 */         throw new IllegalArgumentException("Tried setting an object of type '" + value.getClass().getName() + "'. This is not a supported NBT value. Please check the Wiki for examples!");
/*     */       } 
/*     */     } 
/*     */     
/* 210 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\wrapper\ProxyBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */