/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.graph.entities;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class Node
/*    */   extends GraphEntity
/*    */ {
/*    */   private final List<String> labels;
/*    */   
/*    */   public Node() {
/* 19 */     this.labels = new ArrayList<>();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Node(int labelsCapacity, int propertiesCapacity) {
/* 29 */     super(propertiesCapacity);
/* 30 */     this.labels = new ArrayList<>(labelsCapacity);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void addLabel(String label) {
/* 38 */     this.labels.add(label);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeLabel(String label) {
/* 45 */     this.labels.remove(label);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getLabel(int index) {
/* 55 */     return this.labels.get(index);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNumberOfLabels() {
/* 63 */     return this.labels.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 68 */     if (this == o) return true; 
/* 69 */     if (!(o instanceof Node)) return false; 
/* 70 */     if (!super.equals(o)) return false; 
/* 71 */     Node node = (Node)o;
/* 72 */     return Objects.equals(this.labels, node.labels);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 77 */     return Objects.hash(new Object[] { Integer.valueOf(super.hashCode()), this.labels });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 82 */     StringBuilder sb = new StringBuilder("Node{");
/* 83 */     sb.append("labels=").append(this.labels);
/* 84 */     sb.append(", id=").append(this.id);
/* 85 */     sb.append(", propertyMap=").append(this.propertyMap);
/* 86 */     sb.append('}');
/* 87 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\entities\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */