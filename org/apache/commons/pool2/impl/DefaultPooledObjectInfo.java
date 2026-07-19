/*     */ package org.apache.commons.pool2.impl;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Objects;
/*     */ import org.apache.commons.pool2.PooledObject;
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
/*     */ public class DefaultPooledObjectInfo
/*     */   implements DefaultPooledObjectInfoMBean
/*     */ {
/*     */   private static final String PATTERN = "yyyy-MM-dd HH:mm:ss Z";
/*     */   private final PooledObject<?> pooledObject;
/*     */   
/*     */   public DefaultPooledObjectInfo(PooledObject<?> pooledObject) {
/*  45 */     this.pooledObject = Objects.<PooledObject>requireNonNull(pooledObject, "pooledObject");
/*     */   }
/*     */ 
/*     */   
/*     */   public long getBorrowedCount() {
/*  50 */     return this.pooledObject.getBorrowedCount();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getCreateTime() {
/*  55 */     return this.pooledObject.getCreateInstant().toEpochMilli();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateTimeFormatted() {
/*  60 */     return getTimeMillisFormatted(getCreateTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastBorrowTime() {
/*  65 */     return this.pooledObject.getLastBorrowInstant().toEpochMilli();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLastBorrowTimeFormatted() {
/*  71 */     return getTimeMillisFormatted(getLastBorrowTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastBorrowTrace() {
/*  76 */     StringWriter sw = new StringWriter();
/*  77 */     this.pooledObject.printStackTrace(new PrintWriter(sw));
/*  78 */     return sw.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLastReturnTime() {
/*  83 */     return this.pooledObject.getLastReturnInstant().toEpochMilli();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getLastReturnTimeFormatted() {
/*  88 */     return getTimeMillisFormatted(getLastReturnTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPooledObjectToString() {
/*  93 */     return Objects.toString(this.pooledObject.getObject(), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPooledObjectType() {
/*  98 */     Object object = this.pooledObject.getObject();
/*  99 */     return (object != null) ? object.getClass().getName() : null;
/*     */   }
/*     */   
/*     */   private String getTimeMillisFormatted(long millis) {
/* 103 */     return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")).format(Long.valueOf(millis));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 111 */     StringBuilder builder = new StringBuilder();
/* 112 */     builder.append("DefaultPooledObjectInfo [pooledObject=");
/* 113 */     builder.append(this.pooledObject);
/* 114 */     builder.append("]");
/* 115 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\apache\commons\pool2\impl\DefaultPooledObjectInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */