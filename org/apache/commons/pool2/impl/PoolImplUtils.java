/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.time.temporal.ChronoUnit;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.TimeUnit;
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
/*     */ class PoolImplUtils
/*     */ {
/*     */   static Class<?> getFactoryType(Class<? extends PooledObjectFactory> factoryClass) {
/*  46 */     Class<PooledObjectFactory> type = PooledObjectFactory.class;
/*  47 */     Object genericType = getGenericType(type, factoryClass);
/*  48 */     if (genericType instanceof Integer) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  53 */       ParameterizedType pi = getParameterizedType(type, factoryClass);
/*  54 */       if (pi != null) {
/*     */         
/*  56 */         Type[] bounds = ((TypeVariable<GenericDeclaration>)pi.getActualTypeArguments()[((Integer)genericType).intValue()]).getBounds();
/*  57 */         if (bounds != null && bounds.length > 0) {
/*  58 */           Type bound0 = bounds[0];
/*  59 */           if (bound0 instanceof Class) {
/*  60 */             return (Class)bound0;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/*  65 */       return Object.class;
/*     */     } 
/*  67 */     return (Class)genericType;
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
/*     */   private static <T> Object getGenericType(Class<T> type, Class<? extends T> clazz) {
/*  80 */     if (type == null || clazz == null)
/*     */     {
/*  82 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  86 */     ParameterizedType pi = getParameterizedType(type, clazz);
/*  87 */     if (pi != null) {
/*  88 */       return getTypeParameter(clazz, pi.getActualTypeArguments()[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  93 */     Class<? extends T> superClass = (Class)clazz.getSuperclass();
/*     */     
/*  95 */     Object result = getGenericType(type, superClass);
/*  96 */     if (result instanceof Class)
/*     */     {
/*  98 */       return result;
/*     */     }
/* 100 */     if (result instanceof Integer) {
/*     */ 
/*     */       
/* 103 */       ParameterizedType superClassType = (ParameterizedType)clazz.getGenericSuperclass();
/* 104 */       return getTypeParameter(clazz, superClassType.getActualTypeArguments()[((Integer)result).intValue()]);
/*     */     } 
/*     */     
/* 107 */     return null;
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
/*     */   private static <T> ParameterizedType getParameterizedType(Class<T> type, Class<? extends T> clazz) {
/* 119 */     for (Type iface : clazz.getGenericInterfaces()) {
/*     */       
/* 121 */       if (iface instanceof ParameterizedType) {
/* 122 */         ParameterizedType pi = (ParameterizedType)iface;
/*     */         
/* 124 */         if (pi.getRawType() instanceof Class && type.isAssignableFrom((Class)pi.getRawType())) {
/* 125 */           return pi;
/*     */         }
/*     */       } 
/*     */     } 
/* 129 */     return null;
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
/*     */   private static Object getTypeParameter(Class<?> clazz, Type argType) {
/* 143 */     if (argType instanceof Class) {
/* 144 */       return argType;
/*     */     }
/* 146 */     TypeVariable[] arrayOfTypeVariable = (TypeVariable[])clazz.getTypeParameters();
/* 147 */     for (int i = 0; i < arrayOfTypeVariable.length; i++) {
/* 148 */       if (arrayOfTypeVariable[i].equals(argType)) {
/* 149 */         return Integer.valueOf(i);
/*     */       }
/*     */     } 
/* 152 */     return null;
/*     */   }
/*     */   
/*     */   static boolean isPositive(Duration delay) {
/* 156 */     return (delay != null && !delay.isNegative() && !delay.isZero());
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
/*     */   static Instant max(Instant a, Instant b) {
/* 168 */     return (a.compareTo(b) > 0) ? a : b;
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
/*     */   static Instant min(Instant a, Instant b) {
/* 180 */     return (a.compareTo(b) < 0) ? a : b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Duration nonNull(Duration value, Duration defaultValue) {
/* 191 */     return (value != null) ? value : Objects.<Duration>requireNonNull(defaultValue, "defaultValue");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static ChronoUnit toChronoUnit(TimeUnit timeUnit) {
/* 202 */     switch ((TimeUnit)Objects.requireNonNull((T)timeUnit)) {
/*     */       case NANOSECONDS:
/* 204 */         return ChronoUnit.NANOS;
/*     */       case MICROSECONDS:
/* 206 */         return ChronoUnit.MICROS;
/*     */       case MILLISECONDS:
/* 208 */         return ChronoUnit.MILLIS;
/*     */       case SECONDS:
/* 210 */         return ChronoUnit.SECONDS;
/*     */       case MINUTES:
/* 212 */         return ChronoUnit.MINUTES;
/*     */       case HOURS:
/* 214 */         return ChronoUnit.HOURS;
/*     */       case DAYS:
/* 216 */         return ChronoUnit.DAYS;
/*     */     } 
/* 218 */     throw new IllegalArgumentException(timeUnit.toString());
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
/*     */   static Duration toDuration(long amount, TimeUnit timeUnit) {
/* 230 */     return Duration.of(amount, toChronoUnit(timeUnit));
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\PoolImplUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */