/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.graph.entities;
/*     */ 
/*     */ import java.util.Objects;
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
/*     */ @Deprecated
/*     */ public class Edge
/*     */   extends GraphEntity
/*     */ {
/*     */   private String relationshipType;
/*     */   private long source;
/*     */   private long destination;
/*     */   
/*     */   public Edge() {}
/*     */   
/*     */   public Edge(int propertiesCapacity) {
/*  28 */     super(propertiesCapacity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRelationshipType() {
/*  36 */     return this.relationshipType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRelationshipType(String relationshipType) {
/*  43 */     this.relationshipType = relationshipType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getSource() {
/*  51 */     return this.source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSource(long source) {
/*  58 */     this.source = source;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDestination() {
/*  66 */     return this.destination;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDestination(long destination) {
/*  74 */     this.destination = destination;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  80 */     if (this == o) return true; 
/*  81 */     if (!(o instanceof Edge)) return false; 
/*  82 */     if (!super.equals(o)) return false; 
/*  83 */     Edge edge = (Edge)o;
/*  84 */     return (this.source == edge.source && this.destination == edge.destination && 
/*     */       
/*  86 */       Objects.equals(this.relationshipType, edge.relationshipType));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  91 */     return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), this.relationshipType, Long.valueOf(this.source), Long.valueOf(this.destination) });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     StringBuilder sb = new StringBuilder("Edge{");
/*  97 */     sb.append("relationshipType='").append(this.relationshipType).append('\'');
/*  98 */     sb.append(", source=").append(this.source);
/*  99 */     sb.append(", destination=").append(this.destination);
/* 100 */     sb.append(", id=").append(this.id);
/* 101 */     sb.append(", propertyMap=").append(this.propertyMap);
/* 102 */     sb.append('}');
/* 103 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\entities\Edge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */