/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.graph.entities;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public abstract class GraphEntity
/*     */ {
/*     */   protected long id;
/*     */   protected final Map<String, Property<?>> propertyMap;
/*     */   
/*     */   public GraphEntity() {
/*  17 */     this.propertyMap = new HashMap<>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GraphEntity(int propertiesCapacity) {
/*  26 */     this.propertyMap = new HashMap<>(propertiesCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getId() {
/*  33 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setId(long id) {
/*  40 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(String name, Object value) {
/*  50 */     addProperty(new Property(name, value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getEntityPropertyNames() {
/*  57 */     return this.propertyMap.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProperty(Property<?> property) {
/*  66 */     this.propertyMap.put(property.getName(), property);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNumberOfProperties() {
/*  73 */     return this.propertyMap.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Property getProperty(String propertyName) {
/*  81 */     return this.propertyMap.get(propertyName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeProperty(String name) {
/*  88 */     this.propertyMap.remove(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  93 */     if (this == o) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (!(o instanceof GraphEntity)) {
/*  97 */       return false;
/*     */     }
/*  99 */     GraphEntity that = (GraphEntity)o;
/* 100 */     return (this.id == that.id && 
/* 101 */       Objects.equals(this.propertyMap, that.propertyMap));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 106 */     return Objects.hash(new Object[] { Long.valueOf(this.id), this.propertyMap });
/*     */   }
/*     */   
/*     */   public abstract String toString();
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\entities\GraphEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */