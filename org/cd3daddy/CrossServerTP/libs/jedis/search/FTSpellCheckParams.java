/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FTSpellCheckParams
/*    */   implements IParams
/*    */ {
/*    */   private Collection<Map.Entry<String, Rawable>> terms;
/*    */   private Integer distance;
/*    */   private Integer dialect;
/*    */   
/*    */   public static FTSpellCheckParams spellCheckParams() {
/* 28 */     return new FTSpellCheckParams();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FTSpellCheckParams includeTerm(String dictionary) {
/* 35 */     return addTerm(dictionary, SearchProtocol.SearchKeyword.INCLUDE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FTSpellCheckParams excludeTerm(String dictionary) {
/* 42 */     return addTerm(dictionary, SearchProtocol.SearchKeyword.EXCLUDE);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private FTSpellCheckParams addTerm(String dictionary, Rawable type) {
/* 49 */     if (this.terms == null) {
/* 50 */       this.terms = new ArrayList<>();
/*    */     }
/* 52 */     this.terms.add(KeyValue.of(dictionary, type));
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FTSpellCheckParams distance(int distance) {
/* 60 */     this.distance = Integer.valueOf(distance);
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FTSpellCheckParams dialect(int dialect) {
/* 68 */     this.dialect = Integer.valueOf(dialect);
/* 69 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FTSpellCheckParams dialectOptional(int dialect) {
/* 78 */     if (dialect != 0 && this.dialect == null) {
/* 79 */       this.dialect = Integer.valueOf(dialect);
/*    */     }
/* 81 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 87 */     if (this.terms != null) {
/* 88 */       this.terms.forEach(kv -> args.add(SearchProtocol.SearchKeyword.TERMS).add(kv.getValue()).add(kv.getKey()));
/*    */     }
/*    */     
/* 91 */     if (this.distance != null) {
/* 92 */       args.add(SearchProtocol.SearchKeyword.DISTANCE).add(this.distance);
/*    */     }
/*    */     
/* 95 */     if (this.dialect != null)
/* 96 */       args.add(SearchProtocol.SearchKeyword.DIALECT).add(this.dialect); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\FTSpellCheckParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */