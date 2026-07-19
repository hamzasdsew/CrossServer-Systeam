/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search.aggr;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.search.FieldName;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.search.SearchProtocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.LazyRawable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AggregationBuilder
/*     */   implements IParams
/*     */ {
/*  22 */   private final List<Object> aggrArgs = new ArrayList();
/*     */   private Integer dialect;
/*     */   private boolean isWithCursor = false;
/*     */   
/*     */   public AggregationBuilder(String query) {
/*  27 */     this.aggrArgs.add(query);
/*     */   }
/*     */   
/*     */   public AggregationBuilder() {
/*  31 */     this("*");
/*     */   }
/*     */   
/*     */   public AggregationBuilder load(String... fields) {
/*  35 */     return load(FieldName.convert(fields));
/*     */   }
/*     */   
/*     */   public AggregationBuilder load(FieldName... fields) {
/*  39 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.LOAD);
/*  40 */     LazyRawable rawLoadCount = new LazyRawable();
/*  41 */     this.aggrArgs.add(rawLoadCount);
/*  42 */     int loadCount = 0;
/*  43 */     for (FieldName fn : fields) {
/*  44 */       loadCount += fn.addCommandArguments(this.aggrArgs);
/*     */     }
/*  46 */     rawLoadCount.setRaw(Protocol.toByteArray(loadCount));
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder loadAll() {
/*  51 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.LOAD);
/*  52 */     this.aggrArgs.add(Protocol.BYTES_ASTERISK);
/*  53 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder limit(int offset, int count) {
/*  57 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.LIMIT);
/*  58 */     this.aggrArgs.add(Integer.valueOf(offset));
/*  59 */     this.aggrArgs.add(Integer.valueOf(count));
/*  60 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder limit(int count) {
/*  64 */     return limit(0, count);
/*     */   }
/*     */   
/*     */   public AggregationBuilder sortBy(SortedField... fields) {
/*  68 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.SORTBY);
/*  69 */     this.aggrArgs.add(Integer.toString(fields.length * 2));
/*  70 */     for (SortedField field : fields) {
/*  71 */       this.aggrArgs.add(field.getField());
/*  72 */       this.aggrArgs.add(field.getOrder());
/*     */     } 
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder sortByAsc(String field) {
/*  78 */     return sortBy(new SortedField[] { SortedField.asc(field) });
/*     */   }
/*     */   
/*     */   public AggregationBuilder sortByDesc(String field) {
/*  82 */     return sortBy(new SortedField[] { SortedField.desc(field) });
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
/*     */   public AggregationBuilder sortByMax(int max) {
/*  94 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.MAX);
/*  95 */     this.aggrArgs.add(Integer.valueOf(max));
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AggregationBuilder sortBy(int max, SortedField... fields) {
/* 107 */     sortBy(fields);
/* 108 */     sortByMax(max);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder apply(String projection, String alias) {
/* 113 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.APPLY);
/* 114 */     this.aggrArgs.add(projection);
/* 115 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.AS);
/* 116 */     this.aggrArgs.add(alias);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder groupBy(Group group) {
/* 121 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.GROUPBY);
/* 122 */     group.addArgs(this.aggrArgs);
/* 123 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder groupBy(Collection<String> fields, Collection<Reducer> reducers) {
/* 127 */     String[] fieldsArr = new String[fields.size()];
/* 128 */     Group g = new Group(fields.<String>toArray(fieldsArr));
/* 129 */     reducers.forEach(r -> g.reduce(r));
/* 130 */     groupBy(g);
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder groupBy(String field, Reducer... reducers) {
/* 135 */     return groupBy(Collections.singletonList(field), Arrays.asList(reducers));
/*     */   }
/*     */   
/*     */   public AggregationBuilder filter(String expression) {
/* 139 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.FILTER);
/* 140 */     this.aggrArgs.add(expression);
/* 141 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder cursor(int count) {
/* 145 */     this.isWithCursor = true;
/* 146 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.WITHCURSOR);
/* 147 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.COUNT);
/* 148 */     this.aggrArgs.add(Integer.valueOf(count));
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder cursor(int count, long maxIdle) {
/* 153 */     this.isWithCursor = true;
/* 154 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.WITHCURSOR);
/* 155 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.COUNT);
/* 156 */     this.aggrArgs.add(Integer.valueOf(count));
/* 157 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.MAXIDLE);
/* 158 */     this.aggrArgs.add(Long.valueOf(maxIdle));
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder verbatim() {
/* 163 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.VERBATIM);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder timeout(long timeout) {
/* 168 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.TIMEOUT);
/* 169 */     this.aggrArgs.add(Long.valueOf(timeout));
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder params(Map<String, Object> params) {
/* 174 */     this.aggrArgs.add(SearchProtocol.SearchKeyword.PARAMS);
/* 175 */     this.aggrArgs.add(Integer.valueOf(params.size() * 2));
/* 176 */     params.forEach((k, v) -> {
/*     */           this.aggrArgs.add(k);
/*     */           this.aggrArgs.add(v);
/*     */         });
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public AggregationBuilder dialect(int dialect) {
/* 184 */     this.dialect = Integer.valueOf(dialect);
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AggregationBuilder dialectOptional(int dialect) {
/* 194 */     if (dialect != 0 && this.dialect == null) {
/* 195 */       this.dialect = Integer.valueOf(dialect);
/*     */     }
/* 197 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isWithCursor() {
/* 201 */     return this.isWithCursor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments commArgs) {
/* 206 */     commArgs.addObjects(this.aggrArgs);
/* 207 */     if (this.dialect != null)
/* 208 */       commArgs.add(SearchProtocol.SearchKeyword.DIALECT).add(this.dialect); 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\aggr\AggregationBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */