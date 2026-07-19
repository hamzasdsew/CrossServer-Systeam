/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.GeoUnit;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.args.SortingOrder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.LazyRawable;
/*     */ 
/*     */ public class FTSearchParams
/*     */   implements IParams {
/*     */   private boolean noContent = false;
/*     */   private boolean verbatim = false;
/*     */   private boolean noStopwords = false;
/*     */   private boolean withScores = false;
/*  23 */   private final List<IParams> filters = new LinkedList<>();
/*     */   
/*     */   private Collection<String> inKeys;
/*     */   
/*     */   private Collection<String> inFields;
/*     */   
/*     */   private Collection<String> returnFields;
/*     */   
/*     */   private Collection<FieldName> returnFieldNames;
/*     */   private boolean summarize;
/*     */   private SummarizeParams summarizeParams;
/*     */   private boolean highlight;
/*     */   private HighlightParams highlightParams;
/*     */   private Integer slop;
/*     */   private Long timeout;
/*     */   private boolean inOrder;
/*     */   private String language;
/*     */   private String expander;
/*     */   private String scorer;
/*     */   private String sortBy;
/*     */   private SortingOrder sortOrder;
/*     */   private int[] limit;
/*     */   private Map<String, Object> params;
/*     */   private Integer dialect;
/*     */   
/*     */   public static FTSearchParams searchParams() {
/*  49 */     return new FTSearchParams();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/*  55 */     if (this.noContent) {
/*  56 */       args.add(SearchProtocol.SearchKeyword.NOCONTENT);
/*     */     }
/*  58 */     if (this.verbatim) {
/*  59 */       args.add(SearchProtocol.SearchKeyword.VERBATIM);
/*     */     }
/*  61 */     if (this.noStopwords) {
/*  62 */       args.add(SearchProtocol.SearchKeyword.NOSTOPWORDS);
/*     */     }
/*  64 */     if (this.withScores) {
/*  65 */       args.add(SearchProtocol.SearchKeyword.WITHSCORES);
/*     */     }
/*     */     
/*  68 */     if (!this.filters.isEmpty()) {
/*  69 */       this.filters.forEach(filter -> filter.addParams(args));
/*     */     }
/*     */     
/*  72 */     if (this.inKeys != null && !this.inKeys.isEmpty()) {
/*  73 */       args.add(SearchProtocol.SearchKeyword.INKEYS).add(Integer.valueOf(this.inKeys.size())).addObjects(this.inKeys);
/*     */     }
/*     */     
/*  76 */     if (this.inFields != null && !this.inFields.isEmpty()) {
/*  77 */       args.add(SearchProtocol.SearchKeyword.INFIELDS).add(Integer.valueOf(this.inFields.size())).addObjects(this.inFields);
/*     */     }
/*     */     
/*  80 */     if (this.returnFieldNames != null && !this.returnFieldNames.isEmpty()) {
/*  81 */       args.add(SearchProtocol.SearchKeyword.RETURN);
/*  82 */       LazyRawable returnCountObject = new LazyRawable();
/*  83 */       args.add(returnCountObject);
/*  84 */       int returnCount = 0;
/*  85 */       for (FieldName fn : this.returnFieldNames) {
/*  86 */         returnCount += fn.addCommandArguments(args);
/*     */       }
/*  88 */       returnCountObject.setRaw(Protocol.toByteArray(returnCount));
/*  89 */     } else if (this.returnFields != null && !this.returnFields.isEmpty()) {
/*  90 */       args.add(SearchProtocol.SearchKeyword.RETURN).add(Integer.valueOf(this.returnFields.size())).addObjects(this.returnFields);
/*     */     } 
/*     */     
/*  93 */     if (this.summarizeParams != null) {
/*  94 */       args.addParams(this.summarizeParams);
/*  95 */     } else if (this.summarize) {
/*  96 */       args.add(SearchProtocol.SearchKeyword.SUMMARIZE);
/*     */     } 
/*     */     
/*  99 */     if (this.highlightParams != null) {
/* 100 */       args.addParams(this.highlightParams);
/* 101 */     } else if (this.highlight) {
/* 102 */       args.add(SearchProtocol.SearchKeyword.HIGHLIGHT);
/*     */     } 
/*     */     
/* 105 */     if (this.slop != null) {
/* 106 */       args.add(SearchProtocol.SearchKeyword.SLOP).add(this.slop);
/*     */     }
/*     */     
/* 109 */     if (this.timeout != null) {
/* 110 */       args.add(SearchProtocol.SearchKeyword.TIMEOUT).add(this.timeout);
/*     */     }
/*     */     
/* 113 */     if (this.inOrder) {
/* 114 */       args.add(SearchProtocol.SearchKeyword.INORDER);
/*     */     }
/*     */     
/* 117 */     if (this.language != null) {
/* 118 */       args.add(SearchProtocol.SearchKeyword.LANGUAGE).add(this.language);
/*     */     }
/*     */     
/* 121 */     if (this.expander != null) {
/* 122 */       args.add(SearchProtocol.SearchKeyword.EXPANDER).add(this.expander);
/*     */     }
/*     */     
/* 125 */     if (this.scorer != null) {
/* 126 */       args.add(SearchProtocol.SearchKeyword.SCORER).add(this.scorer);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 133 */     if (this.sortBy != null) {
/* 134 */       args.add(SearchProtocol.SearchKeyword.SORTBY).add(this.sortBy);
/* 135 */       if (this.sortOrder != null) {
/* 136 */         args.add(this.sortOrder);
/*     */       }
/*     */     } 
/*     */     
/* 140 */     if (this.limit != null) {
/* 141 */       args.add(SearchProtocol.SearchKeyword.LIMIT).add(Integer.valueOf(this.limit[0])).add(Integer.valueOf(this.limit[1]));
/*     */     }
/*     */     
/* 144 */     if (this.params != null && !this.params.isEmpty()) {
/* 145 */       args.add(SearchProtocol.SearchKeyword.PARAMS).add(Integer.valueOf(this.params.size() * 2));
/* 146 */       this.params.entrySet().forEach(entry -> args.add(entry.getKey()).add(entry.getValue()));
/*     */     } 
/*     */     
/* 149 */     if (this.dialect != null) {
/* 150 */       args.add(SearchProtocol.SearchKeyword.DIALECT).add(this.dialect);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams noContent() {
/* 160 */     this.noContent = true;
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams verbatim() {
/* 170 */     this.verbatim = true;
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams noStopwords() {
/* 180 */     this.noStopwords = true;
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams withScores() {
/* 191 */     this.withScores = true;
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams filter(String field, double min, double max) {
/* 196 */     return filter(new NumericFilter(field, min, max));
/*     */   }
/*     */   
/*     */   public FTSearchParams filter(String field, double min, boolean exclusiveMin, double max, boolean exclusiveMax) {
/* 200 */     return filter(new NumericFilter(field, min, exclusiveMin, max, exclusiveMax));
/*     */   }
/*     */   
/*     */   public FTSearchParams filter(NumericFilter numericFilter) {
/* 204 */     this.filters.add(numericFilter);
/* 205 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams geoFilter(String field, double lon, double lat, double radius, GeoUnit unit) {
/* 209 */     return geoFilter(new GeoFilter(field, lon, lat, radius, unit));
/*     */   }
/*     */   
/*     */   public FTSearchParams geoFilter(GeoFilter geoFilter) {
/* 213 */     this.filters.add(geoFilter);
/* 214 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams inKeys(String... keys) {
/* 224 */     return inKeys(Arrays.asList(keys));
/*     */   }
/*     */   
/*     */   public FTSearchParams inKeys(Collection<String> keys) {
/* 228 */     this.inKeys = keys;
/* 229 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams inFields(String... fields) {
/* 239 */     return inFields(Arrays.asList(fields));
/*     */   }
/*     */   
/*     */   public FTSearchParams inFields(Collection<String> fields) {
/* 243 */     if (this.inFields == null) {
/* 244 */       this.inFields = new ArrayList<>(fields);
/*     */     } else {
/* 246 */       this.inFields.addAll(fields);
/*     */     } 
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams returnFields(String... fields) {
/* 258 */     if (this.returnFieldNames != null) {
/* 259 */       Arrays.<String>stream(fields).forEach(f -> this.returnFieldNames.add(FieldName.of(f)));
/*     */     } else {
/* 261 */       if (this.returnFields == null) {
/* 262 */         this.returnFields = new ArrayList<>();
/*     */       }
/* 264 */       Arrays.<String>stream(fields).forEach(f -> this.returnFields.add(f));
/*     */     } 
/* 266 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams returnField(FieldName field) {
/* 270 */     initReturnFieldNames();
/* 271 */     this.returnFieldNames.add(field);
/* 272 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams returnFields(FieldName... fields) {
/* 276 */     return returnFields(Arrays.asList(fields));
/*     */   }
/*     */   
/*     */   public FTSearchParams returnFields(Collection<FieldName> fields) {
/* 280 */     initReturnFieldNames();
/* 281 */     this.returnFieldNames.addAll(fields);
/* 282 */     return this;
/*     */   }
/*     */   
/*     */   private void initReturnFieldNames() {
/* 286 */     if (this.returnFieldNames == null) {
/* 287 */       this.returnFieldNames = new ArrayList<>();
/*     */     }
/* 289 */     if (this.returnFields != null) {
/* 290 */       this.returnFields.forEach(f -> this.returnFieldNames.add(FieldName.of(f)));
/* 291 */       this.returnFields = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public FTSearchParams summarize() {
/* 296 */     this.summarize = true;
/* 297 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams summarize(SummarizeParams summarizeParams) {
/* 301 */     this.summarizeParams = summarizeParams;
/* 302 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams highlight() {
/* 306 */     this.highlight = true;
/* 307 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams highlight(HighlightParams highlightParams) {
/* 311 */     this.highlightParams = highlightParams;
/* 312 */     return this;
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
/*     */   public FTSearchParams scorer(String scorer) {
/* 325 */     this.scorer = scorer;
/* 326 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams slop(int slop) {
/* 335 */     this.slop = Integer.valueOf(slop);
/* 336 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams timeout(long timeout) {
/* 340 */     this.timeout = Long.valueOf(timeout);
/* 341 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams inOrder() {
/* 345 */     this.inOrder = true;
/* 346 */     return this;
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
/*     */   public FTSearchParams language(String language) {
/* 359 */     this.language = language;
/* 360 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams sortBy(String sortBy, SortingOrder order) {
/* 371 */     this.sortBy = sortBy;
/* 372 */     this.sortOrder = order;
/* 373 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams limit(int offset, int num) {
/* 384 */     this.limit = new int[] { offset, num };
/* 385 */     return this;
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
/*     */   public FTSearchParams addParam(String name, Object value) {
/* 398 */     if (this.params == null) {
/* 399 */       this.params = new HashMap<>();
/*     */     }
/* 401 */     this.params.put(name, value);
/* 402 */     return this;
/*     */   }
/*     */   
/*     */   public FTSearchParams params(Map<String, Object> paramValues) {
/* 406 */     if (this.params == null) {
/* 407 */       this.params = new HashMap<>(paramValues);
/*     */     } else {
/* 409 */       this.params.putAll(this.params);
/*     */     } 
/* 411 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams dialect(int dialect) {
/* 421 */     this.dialect = Integer.valueOf(dialect);
/* 422 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FTSearchParams dialectOptional(int dialect) {
/* 431 */     if (dialect != 0 && this.dialect == null) {
/* 432 */       this.dialect = Integer.valueOf(dialect);
/*     */     }
/* 434 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getNoContent() {
/* 438 */     return this.noContent;
/*     */   }
/*     */   
/*     */   public boolean getWithScores() {
/* 442 */     return this.withScores;
/*     */   }
/*     */ 
/*     */   
/*     */   public static class NumericFilter
/*     */     implements IParams
/*     */   {
/*     */     private final String field;
/*     */     
/*     */     private final double min;
/*     */     private final boolean exclusiveMin;
/*     */     private final double max;
/*     */     private final boolean exclusiveMax;
/*     */     
/*     */     public NumericFilter(String field, double min, double max) {
/* 457 */       this(field, min, false, max, false);
/*     */     }
/*     */     
/*     */     public NumericFilter(String field, double min, boolean exclusiveMin, double max, boolean exclusiveMax) {
/* 461 */       this.field = field;
/* 462 */       this.min = min;
/* 463 */       this.max = max;
/* 464 */       this.exclusiveMax = exclusiveMax;
/* 465 */       this.exclusiveMin = exclusiveMin;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addParams(CommandArguments args) {
/* 470 */       args.add(SearchProtocol.SearchKeyword.FILTER).add(this.field)
/* 471 */         .add(formatNum(this.min, this.exclusiveMin))
/* 472 */         .add(formatNum(this.max, this.exclusiveMax));
/*     */     }
/*     */     
/*     */     private Object formatNum(double num, boolean exclude) {
/* 476 */       return exclude ? ("(" + num) : Protocol.toByteArray(num);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class GeoFilter
/*     */     implements IParams
/*     */   {
/*     */     private final String field;
/*     */     
/*     */     private final double lon;
/*     */     private final double lat;
/*     */     private final double radius;
/*     */     private final GeoUnit unit;
/*     */     
/*     */     public GeoFilter(String field, double lon, double lat, double radius, GeoUnit unit) {
/* 492 */       this.field = field;
/* 493 */       this.lon = lon;
/* 494 */       this.lat = lat;
/* 495 */       this.radius = radius;
/* 496 */       this.unit = unit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addParams(CommandArguments args) {
/* 501 */       args.add(SearchProtocol.SearchKeyword.GEOFILTER).add(this.field)
/* 502 */         .add(Double.valueOf(this.lon)).add(Double.valueOf(this.lat))
/* 503 */         .add(Double.valueOf(this.radius)).add(this.unit);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SummarizeParams
/*     */     implements IParams
/*     */   {
/*     */     private Collection<String> fields;
/*     */     
/*     */     private Integer fragsNum;
/*     */     private Integer fragSize;
/*     */     private String separator;
/*     */     
/*     */     public SummarizeParams fields(String... fields) {
/* 518 */       return fields(Arrays.asList(fields));
/*     */     }
/*     */     
/*     */     public SummarizeParams fields(Collection<String> fields) {
/* 522 */       this.fields = fields;
/* 523 */       return this;
/*     */     }
/*     */     
/*     */     public SummarizeParams fragsNum(int num) {
/* 527 */       this.fragsNum = Integer.valueOf(num);
/* 528 */       return this;
/*     */     }
/*     */     
/*     */     public SummarizeParams fragSize(int size) {
/* 532 */       this.fragSize = Integer.valueOf(size);
/* 533 */       return this;
/*     */     }
/*     */     
/*     */     public SummarizeParams separator(String separator) {
/* 537 */       this.separator = separator;
/* 538 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addParams(CommandArguments args) {
/* 543 */       args.add(SearchProtocol.SearchKeyword.SUMMARIZE);
/*     */       
/* 545 */       if (this.fields != null) {
/* 546 */         args.add(SearchProtocol.SearchKeyword.FIELDS).add(Integer.valueOf(this.fields.size())).addObjects(this.fields);
/*     */       }
/* 548 */       if (this.fragsNum != null) {
/* 549 */         args.add(SearchProtocol.SearchKeyword.FRAGS).add(this.fragsNum);
/*     */       }
/* 551 */       if (this.fragSize != null) {
/* 552 */         args.add(SearchProtocol.SearchKeyword.LEN).add(this.fragSize);
/*     */       }
/* 554 */       if (this.separator != null) {
/* 555 */         args.add(SearchProtocol.SearchKeyword.SEPARATOR).add(this.separator);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static SummarizeParams summarizeParams() {
/* 561 */     return new SummarizeParams();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class HighlightParams
/*     */     implements IParams
/*     */   {
/*     */     private Collection<String> fields;
/*     */     
/*     */     private String[] tags;
/*     */     
/*     */     public HighlightParams fields(String fields) {
/* 573 */       return fields(Arrays.asList(new String[] { fields }));
/*     */     }
/*     */     
/*     */     public HighlightParams fields(Collection<String> fields) {
/* 577 */       this.fields = fields;
/* 578 */       return this;
/*     */     }
/*     */     
/*     */     public HighlightParams tags(String open, String close) {
/* 582 */       this.tags = new String[] { open, close };
/* 583 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addParams(CommandArguments args) {
/* 588 */       args.add(SearchProtocol.SearchKeyword.HIGHLIGHT);
/*     */       
/* 590 */       if (this.fields != null) {
/* 591 */         args.add(SearchProtocol.SearchKeyword.FIELDS).add(Integer.valueOf(this.fields.size())).addObjects(this.fields);
/*     */       }
/* 593 */       if (this.tags != null) {
/* 594 */         args.add(SearchProtocol.SearchKeyword.TAGS).add(this.tags[0]).add(this.tags[1]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static HighlightParams highlightParams() {
/* 600 */     return new HighlightParams();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\FTSearchParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */