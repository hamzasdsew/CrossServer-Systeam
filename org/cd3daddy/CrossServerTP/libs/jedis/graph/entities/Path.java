/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.graph.entities;
/*     */ 
/*     */ import java.util.List;
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
/*     */ @Deprecated
/*     */ public final class Path
/*     */ {
/*     */   private final List<Node> nodes;
/*     */   private final List<Edge> edges;
/*     */   
/*     */   public Path(List<Node> nodes, List<Edge> edges) {
/*  23 */     this.nodes = nodes;
/*  24 */     this.edges = edges;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Node> getNodes() {
/*  32 */     return this.nodes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Edge> getEdges() {
/*  40 */     return this.edges;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/*  48 */     return this.edges.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int nodeCount() {
/*  56 */     return this.nodes.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node firstNode() {
/*  65 */     return this.nodes.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node lastNode() {
/*  74 */     return this.nodes.get(this.nodes.size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node getNode(int index) {
/*  84 */     return this.nodes.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Edge getEdge(int index) {
/*  94 */     return this.edges.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  99 */     if (this == o) return true; 
/* 100 */     if (o == null || getClass() != o.getClass()) return false; 
/* 101 */     Path path = (Path)o;
/* 102 */     return (Objects.equals(this.nodes, path.nodes) && 
/* 103 */       Objects.equals(this.edges, path.edges));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     return Objects.hash(new Object[] { this.nodes, this.edges });
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 113 */     StringBuilder sb = new StringBuilder("Path{");
/* 114 */     sb.append("nodes=").append(this.nodes);
/* 115 */     sb.append(", edges=").append(this.edges);
/* 116 */     sb.append('}');
/* 117 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\entities\Path.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */