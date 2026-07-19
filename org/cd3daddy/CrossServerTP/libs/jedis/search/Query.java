/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.params.IParams;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.LazyRawable;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Query
/*     */   implements IParams
/*     */ {
/*     */   public static abstract class Filter
/*     */     implements IParams
/*     */   {
/*     */     public final String property;
/*     */     
/*     */     public Filter(String property) {
/*  28 */       this.property = property;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class NumericFilter
/*     */     extends Filter
/*     */   {
/*     */     private final double min;
/*     */     
/*     */     private final boolean exclusiveMin;
/*     */     private final double max;
/*     */     private final boolean exclusiveMax;
/*     */     
/*     */     public NumericFilter(String property, double min, boolean exclusiveMin, double max, boolean exclusiveMax) {
/*  43 */       super(property);
/*  44 */       this.min = min;
/*  45 */       this.max = max;
/*  46 */       this.exclusiveMax = exclusiveMax;
/*  47 */       this.exclusiveMin = exclusiveMin;
/*     */     }
/*     */     
/*     */     public NumericFilter(String property, double min, double max) {
/*  51 */       this(property, min, false, max, false);
/*     */     }
/*     */     
/*     */     private byte[] formatNum(double num, boolean exclude) {
/*  55 */       return exclude ? SafeEncoder.encode("(" + num) : Protocol.toByteArray(num);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addParams(CommandArguments args) {
/*  60 */       args.add(SearchProtocol.SearchKeyword.FILTER.getRaw());
/*  61 */       args.add(SafeEncoder.encode(this.property));
/*  62 */       args.add(formatNum(this.min, this.exclusiveMin));
/*  63 */       args.add(formatNum(this.max, this.exclusiveMax));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class GeoFilter
/*     */     extends Filter
/*     */   {
/*     */     public static final String KILOMETERS = "km";
/*     */     
/*     */     public static final String METERS = "m";
/*     */     
/*     */     public static final String FEET = "ft";
/*     */     public static final String MILES = "mi";
/*     */     private final double lon;
/*     */     private final double lat;
/*     */     private final double radius;
/*     */     private final String unit;
/*     */     
/*     */     public GeoFilter(String property, double lon, double lat, double radius, String unit) {
/*  83 */       super(property);
/*  84 */       this.lon = lon;
/*  85 */       this.lat = lat;
/*  86 */       this.radius = radius;
/*  87 */       this.unit = unit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void addParams(CommandArguments args) {
/*  92 */       args.add(SearchProtocol.SearchKeyword.GEOFILTER.getRaw());
/*  93 */       args.add(SafeEncoder.encode(this.property));
/*  94 */       args.add(Protocol.toByteArray(this.lon));
/*  95 */       args.add(Protocol.toByteArray(this.lat));
/*  96 */       args.add(Protocol.toByteArray(this.radius));
/*  97 */       args.add(SafeEncoder.encode(this.unit));
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Paging
/*     */   {
/*     */     int offset;
/*     */     int num;
/*     */     
/*     */     public Paging(int offset, int num) {
/* 107 */       this.offset = offset;
/* 108 */       this.num = num;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class HighlightTags
/*     */   {
/*     */     private final String open;
/*     */     private final String close;
/*     */     
/*     */     public HighlightTags(String open, String close) {
/* 118 */       this.open = open;
/* 119 */       this.close = close;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 126 */   private final List<Filter> _filters = new LinkedList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final String _queryString;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 136 */   private final Paging _paging = new Paging(0, 10);
/*     */   
/*     */   private boolean _verbatim = false;
/*     */   private boolean _noContent = false;
/*     */   private boolean _noStopwords = false;
/*     */   private boolean _withScores = false;
/* 142 */   private String _language = null;
/* 143 */   private String[] _fields = null;
/* 144 */   private String[] _keys = null;
/* 145 */   private String[] _returnFields = null;
/* 146 */   private FieldName[] returnFieldNames = null;
/* 147 */   private String[] highlightFields = null;
/* 148 */   private String[] summarizeFields = null;
/* 149 */   private String[] highlightTags = null;
/* 150 */   private String summarizeSeparator = null;
/* 151 */   private int summarizeNumFragments = -1;
/* 152 */   private int summarizeFragmentLen = -1;
/* 153 */   private String _sortBy = null;
/*     */   private boolean _sortAsc = true;
/*     */   private boolean wantsHighlight = false;
/*     */   private boolean wantsSummarize = false;
/* 157 */   private String _scorer = null;
/* 158 */   private Map<String, Object> _params = null;
/*     */   private Integer _dialect;
/* 160 */   private int _slop = -1;
/* 161 */   private long _timeout = -1L;
/*     */   private boolean _inOrder = false;
/* 163 */   private String _expander = null;
/*     */   
/*     */   public Query() {
/* 166 */     this("*");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query(String queryString) {
/* 175 */     this._queryString = queryString;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addParams(CommandArguments args) {
/* 180 */     args.add(SafeEncoder.encode(this._queryString));
/*     */     
/* 182 */     if (this._verbatim) {
/* 183 */       args.add(SearchProtocol.SearchKeyword.VERBATIM.getRaw());
/*     */     }
/* 185 */     if (this._noContent) {
/* 186 */       args.add(SearchProtocol.SearchKeyword.NOCONTENT.getRaw());
/*     */     }
/* 188 */     if (this._noStopwords) {
/* 189 */       args.add(SearchProtocol.SearchKeyword.NOSTOPWORDS.getRaw());
/*     */     }
/* 191 */     if (this._withScores) {
/* 192 */       args.add(SearchProtocol.SearchKeyword.WITHSCORES.getRaw());
/*     */     }
/* 194 */     if (this._language != null) {
/* 195 */       args.add(SearchProtocol.SearchKeyword.LANGUAGE.getRaw());
/* 196 */       args.add(SafeEncoder.encode(this._language));
/*     */     } 
/*     */     
/* 199 */     if (this._scorer != null) {
/* 200 */       args.add(SearchProtocol.SearchKeyword.SCORER.getRaw());
/* 201 */       args.add(SafeEncoder.encode(this._scorer));
/*     */     } 
/*     */     
/* 204 */     if (this._fields != null && this._fields.length > 0) {
/* 205 */       args.add(SearchProtocol.SearchKeyword.INFIELDS.getRaw());
/* 206 */       args.add(Protocol.toByteArray(this._fields.length));
/* 207 */       for (String f : this._fields) {
/* 208 */         args.add(SafeEncoder.encode(f));
/*     */       }
/*     */     } 
/*     */     
/* 212 */     if (this._sortBy != null) {
/* 213 */       args.add(SearchProtocol.SearchKeyword.SORTBY.getRaw());
/* 214 */       args.add(SafeEncoder.encode(this._sortBy));
/* 215 */       args.add((this._sortAsc ? SearchProtocol.SearchKeyword.ASC : SearchProtocol.SearchKeyword.DESC).getRaw());
/*     */     } 
/*     */     
/* 218 */     if (this._paging.offset != 0 || this._paging.num != 10) {
/* 219 */       args.add(SearchProtocol.SearchKeyword.LIMIT.getRaw()).add(Protocol.toByteArray(this._paging.offset)).add(Protocol.toByteArray(this._paging.num));
/*     */     }
/*     */     
/* 222 */     if (!this._filters.isEmpty()) {
/* 223 */       this._filters.forEach(filter -> filter.addParams(args));
/*     */     }
/*     */     
/* 226 */     if (this.wantsHighlight) {
/* 227 */       args.add(SearchProtocol.SearchKeyword.HIGHLIGHT.getRaw());
/* 228 */       if (this.highlightFields != null) {
/* 229 */         args.add(SearchProtocol.SearchKeyword.FIELDS.getRaw());
/* 230 */         args.add(Protocol.toByteArray(this.highlightFields.length));
/* 231 */         for (String s : this.highlightFields) {
/* 232 */           args.add(SafeEncoder.encode(s));
/*     */         }
/*     */       } 
/* 235 */       if (this.highlightTags != null) {
/* 236 */         args.add(SearchProtocol.SearchKeyword.TAGS.getRaw());
/* 237 */         for (String t : this.highlightTags) {
/* 238 */           args.add(SafeEncoder.encode(t));
/*     */         }
/*     */       } 
/*     */     } 
/* 242 */     if (this.wantsSummarize) {
/* 243 */       args.add(SearchProtocol.SearchKeyword.SUMMARIZE.getRaw());
/* 244 */       if (this.summarizeFields != null) {
/* 245 */         args.add(SearchProtocol.SearchKeyword.FIELDS.getRaw());
/* 246 */         args.add(Protocol.toByteArray(this.summarizeFields.length));
/* 247 */         for (String s : this.summarizeFields) {
/* 248 */           args.add(SafeEncoder.encode(s));
/*     */         }
/*     */       } 
/* 251 */       if (this.summarizeNumFragments != -1) {
/* 252 */         args.add(SearchProtocol.SearchKeyword.FRAGS.getRaw());
/* 253 */         args.add(Protocol.toByteArray(this.summarizeNumFragments));
/*     */       } 
/* 255 */       if (this.summarizeFragmentLen != -1) {
/* 256 */         args.add(SearchProtocol.SearchKeyword.LEN.getRaw());
/* 257 */         args.add(Protocol.toByteArray(this.summarizeFragmentLen));
/*     */       } 
/* 259 */       if (this.summarizeSeparator != null) {
/* 260 */         args.add(SearchProtocol.SearchKeyword.SEPARATOR.getRaw());
/* 261 */         args.add(SafeEncoder.encode(this.summarizeSeparator));
/*     */       } 
/*     */     } 
/*     */     
/* 265 */     if (this._keys != null && this._keys.length > 0) {
/* 266 */       args.add(SearchProtocol.SearchKeyword.INKEYS.getRaw());
/* 267 */       args.add(Protocol.toByteArray(this._keys.length));
/* 268 */       for (String f : this._keys) {
/* 269 */         args.add(SafeEncoder.encode(f));
/*     */       }
/*     */     } 
/*     */     
/* 273 */     if (this._returnFields != null && this._returnFields.length > 0) {
/* 274 */       args.add(SearchProtocol.SearchKeyword.RETURN.getRaw());
/* 275 */       args.add(Protocol.toByteArray(this._returnFields.length));
/* 276 */       for (String f : this._returnFields) {
/* 277 */         args.add(SafeEncoder.encode(f));
/*     */       }
/* 279 */     } else if (this.returnFieldNames != null && this.returnFieldNames.length > 0) {
/* 280 */       args.add(SearchProtocol.SearchKeyword.RETURN.getRaw());
/*     */       
/* 282 */       LazyRawable returnCountObject = new LazyRawable();
/*     */       
/* 284 */       args.add(returnCountObject);
/* 285 */       int returnCount = 0;
/* 286 */       for (FieldName fn : this.returnFieldNames) {
/* 287 */         returnCount += fn.addCommandArguments(args);
/*     */       }
/*     */       
/* 290 */       returnCountObject.setRaw(Protocol.toByteArray(returnCount));
/*     */     } 
/*     */     
/* 293 */     if (this._params != null && this._params.size() > 0) {
/* 294 */       args.add(SearchProtocol.SearchKeyword.PARAMS.getRaw());
/* 295 */       args.add(Integer.valueOf(this._params.size() * 2));
/* 296 */       for (Map.Entry<String, Object> entry : this._params.entrySet()) {
/* 297 */         args.add(entry.getKey());
/* 298 */         args.add(entry.getValue());
/*     */       } 
/*     */     } 
/*     */     
/* 302 */     if (this._dialect != null) {
/* 303 */       args.add(SearchProtocol.SearchKeyword.DIALECT.getRaw());
/* 304 */       args.add(this._dialect);
/*     */     } 
/*     */     
/* 307 */     if (this._slop >= 0) {
/* 308 */       args.add(SearchProtocol.SearchKeyword.SLOP.getRaw());
/* 309 */       args.add(Integer.valueOf(this._slop));
/*     */     } 
/*     */     
/* 312 */     if (this._timeout >= 0L) {
/* 313 */       args.add(SearchProtocol.SearchKeyword.TIMEOUT.getRaw());
/* 314 */       args.add(Long.valueOf(this._timeout));
/*     */     } 
/*     */     
/* 317 */     if (this._inOrder) {
/* 318 */       args.add(SearchProtocol.SearchKeyword.INORDER.getRaw());
/*     */     }
/*     */     
/* 321 */     if (this._expander != null) {
/* 322 */       args.add(SearchProtocol.SearchKeyword.EXPANDER.getRaw());
/* 323 */       args.add(SafeEncoder.encode(this._expander));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query limit(Integer offset, Integer limit) {
/* 335 */     this._paging.offset = offset.intValue();
/* 336 */     this._paging.num = limit.intValue();
/* 337 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query addFilter(Filter f) {
/* 347 */     this._filters.add(f);
/* 348 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query setVerbatim() {
/* 357 */     this._verbatim = true;
/* 358 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getNoContent() {
/* 362 */     return this._noContent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query setNoContent() {
/* 371 */     this._noContent = true;
/* 372 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query setNoStopwords() {
/* 381 */     this._noStopwords = true;
/* 382 */     return this;
/*     */   }
/*     */   
/*     */   public boolean getWithScores() {
/* 386 */     return this._withScores;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query setWithScores() {
/* 396 */     this._withScores = true;
/* 397 */     return this;
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
/*     */   public Query setLanguage(String language) {
/* 410 */     this._language = language;
/* 411 */     return this;
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
/*     */   public Query setScorer(String scorer) {
/* 424 */     this._scorer = scorer;
/* 425 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query limitFields(String... fields) {
/* 435 */     this._fields = fields;
/* 436 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query limitKeys(String... keys) {
/* 446 */     this._keys = keys;
/* 447 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query returnFields(String... fields) {
/* 457 */     this._returnFields = fields;
/* 458 */     this.returnFieldNames = null;
/* 459 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query returnFields(FieldName... fields) {
/* 469 */     this.returnFieldNames = fields;
/* 470 */     this._returnFields = null;
/* 471 */     return this;
/*     */   }
/*     */   
/*     */   public Query highlightFields(HighlightTags tags, String... fields) {
/* 475 */     if (fields == null || fields.length > 0) {
/* 476 */       this.highlightFields = fields;
/*     */     }
/* 478 */     if (tags != null) {
/* 479 */       this.highlightTags = new String[] { HighlightTags.access$000(tags), HighlightTags.access$100(tags) };
/*     */     } else {
/* 481 */       this.highlightTags = null;
/*     */     } 
/* 483 */     this.wantsHighlight = true;
/* 484 */     return this;
/*     */   }
/*     */   
/*     */   public Query highlightFields(String... fields) {
/* 488 */     return highlightFields(null, fields);
/*     */   }
/*     */   
/*     */   public Query summarizeFields(int contextLen, int fragmentCount, String separator, String... fields) {
/* 492 */     if (fields == null || fields.length > 0) {
/* 493 */       this.summarizeFields = fields;
/*     */     }
/* 495 */     this.summarizeFragmentLen = contextLen;
/* 496 */     this.summarizeNumFragments = fragmentCount;
/* 497 */     this.summarizeSeparator = separator;
/* 498 */     this.wantsSummarize = true;
/* 499 */     return this;
/*     */   }
/*     */   
/*     */   public Query summarizeFields(String... fields) {
/* 503 */     return summarizeFields(-1, -1, null, fields);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query setSortBy(String field, boolean ascending) {
/* 514 */     this._sortBy = field;
/* 515 */     this._sortAsc = ascending;
/* 516 */     return this;
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
/*     */   public Query addParam(String name, Object value) {
/* 529 */     if (this._params == null) {
/* 530 */       this._params = new HashMap<>();
/*     */     }
/* 532 */     this._params.put(name, value);
/* 533 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query dialect(int dialect) {
/* 543 */     this._dialect = Integer.valueOf(dialect);
/* 544 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query dialectOptional(int dialect) {
/* 553 */     if (dialect != 0 && this._dialect == null) {
/* 554 */       this._dialect = Integer.valueOf(dialect);
/*     */     }
/* 556 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query slop(int slop) {
/* 566 */     this._slop = slop;
/* 567 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query timeout(long timeout) {
/* 577 */     this._timeout = timeout;
/* 578 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query setInOrder() {
/* 587 */     this._inOrder = true;
/* 588 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Query setExpander(String field) {
/* 598 */     this._expander = field;
/* 599 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\Query.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */