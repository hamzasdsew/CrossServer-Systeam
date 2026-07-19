/*     */ package org.cd3daddy.CrossServerTP.libs.jedis.graph;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisDataException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisException;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.entities.Edge;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.entities.GraphEntity;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.entities.Node;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.entities.Path;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.graph.entities.Point;
/*     */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*     */ 
/*     */ @Deprecated
/*     */ class ResultSetBuilder
/*     */   extends Builder<ResultSet>
/*     */ {
/*     */   private final GraphCache graphCache;
/*     */   
/*     */   ResultSetBuilder(GraphCache cache) {
/*  29 */     this.graphCache = cache;
/*     */   }
/*     */   
/*     */   public ResultSet build(Object data) {
/*     */     Object headerObject, recordsObject, statisticsObject;
/*  34 */     List<Object> rawResponse = (List<Object>)data;
/*     */ 
/*     */     
/*  37 */     if (rawResponse.get(rawResponse.size() - 1) instanceof JedisDataException) {
/*  38 */       throw (JedisDataException)rawResponse.get(rawResponse.size() - 1);
/*     */     }
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
/*  50 */     if (rawResponse.size() == 1) {
/*  51 */       headerObject = Collections.emptyList();
/*  52 */       recordsObject = Collections.emptyList();
/*  53 */       statisticsObject = rawResponse.get(0);
/*  54 */     } else if (rawResponse.size() == 3) {
/*  55 */       headerObject = rawResponse.get(0);
/*  56 */       recordsObject = rawResponse.get(1);
/*  57 */       statisticsObject = rawResponse.get(2);
/*     */     } else {
/*  59 */       throw new JedisException("Unrecognized graph response format.");
/*     */     } 
/*     */     
/*  62 */     HeaderImpl header = parseHeader(headerObject);
/*  63 */     List<Record> records = parseRecords(header, recordsObject);
/*  64 */     StatisticsImpl statistics = parseStatistics(statisticsObject);
/*  65 */     return new ResultSetImpl(header, records, statistics);
/*     */   }
/*     */   
/*     */   private class ResultSetImpl
/*     */     implements ResultSet {
/*     */     private final Header header;
/*     */     private final List<Record> results;
/*     */     private final Statistics statistics;
/*     */     
/*     */     private ResultSetImpl(Header header, List<Record> results, Statistics statistics) {
/*  75 */       this.header = header;
/*  76 */       this.results = results;
/*  77 */       this.statistics = statistics;
/*     */     }
/*     */ 
/*     */     
/*     */     public Header getHeader() {
/*  82 */       return this.header;
/*     */     }
/*     */ 
/*     */     
/*     */     public Statistics getStatistics() {
/*  87 */       return this.statistics;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/*  92 */       return this.results.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  97 */       if (this == o) {
/*  98 */         return true;
/*     */       }
/* 100 */       if (!(o instanceof ResultSetImpl)) {
/* 101 */         return false;
/*     */       }
/* 103 */       ResultSetImpl resultSet = (ResultSetImpl)o;
/* 104 */       return (Objects.equals(getHeader(), resultSet.getHeader()) && 
/* 105 */         Objects.equals(getStatistics(), resultSet.getStatistics()) && 
/* 106 */         Objects.equals(this.results, resultSet.results));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 111 */       return Objects.hash(new Object[] { getHeader(), getStatistics(), this.results });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 116 */       StringBuilder sb = new StringBuilder("ResultSetImpl{");
/* 117 */       sb.append("header=").append(this.header);
/* 118 */       sb.append(", statistics=").append(this.statistics);
/* 119 */       sb.append(", results=").append(this.results);
/* 120 */       sb.append('}');
/* 121 */       return sb.toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Record> iterator() {
/* 126 */       return this.results.iterator();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Record> parseRecords(Header header, Object data) {
/* 132 */     List<List<Object>> rawResultSet = (List<List<Object>>)data;
/*     */     
/* 134 */     if (rawResultSet == null || rawResultSet.isEmpty()) {
/* 135 */       return new ArrayList<>(0);
/*     */     }
/*     */     
/* 138 */     List<Record> results = new ArrayList<>(rawResultSet.size());
/*     */     
/* 140 */     for (List<Object> row : rawResultSet) {
/*     */       
/* 142 */       List<Object> parsedRow = new ArrayList(row.size());
/*     */       
/* 144 */       for (int i = 0; i < row.size(); i++) {
/*     */         
/* 146 */         List<Object> obj = (List<Object>)row.get(i);
/*     */         
/* 148 */         ResultSet.ColumnType objType = header.getSchemaTypes().get(i);
/*     */         
/* 150 */         switch (objType) {
/*     */           case NULL:
/* 152 */             parsedRow.add(deserializeNode(obj));
/*     */             break;
/*     */           case BOOLEAN:
/* 155 */             parsedRow.add(deserializeEdge(obj));
/*     */             break;
/*     */           case DOUBLE:
/* 158 */             parsedRow.add(deserializeScalar(obj));
/*     */             break;
/*     */           default:
/* 161 */             parsedRow.add((Object)null);
/*     */             break;
/*     */         } 
/*     */ 
/*     */       
/*     */       } 
/* 167 */       Record record = new RecordImpl(header.getSchemaNames(), parsedRow);
/* 168 */       results.add(record);
/*     */     } 
/*     */     
/* 171 */     return results;
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
/*     */   private Node deserializeNode(List<Object> rawNodeData) {
/* 184 */     List<Long> labelsIndices = (List<Long>)rawNodeData.get(1);
/* 185 */     List<List<Object>> rawProperties = (List<List<Object>>)rawNodeData.get(2);
/*     */     
/* 187 */     Node node = new Node(labelsIndices.size(), rawProperties.size());
/* 188 */     deserializeGraphEntityId((GraphEntity)node, ((Long)rawNodeData.get(0)).longValue());
/*     */     
/* 190 */     for (Long labelIndex : labelsIndices) {
/* 191 */       String label = this.graphCache.getLabel(labelIndex.intValue());
/* 192 */       node.addLabel(label);
/*     */     } 
/*     */     
/* 195 */     deserializeGraphEntityProperties((GraphEntity)node, rawProperties);
/*     */     
/* 197 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deserializeGraphEntityId(GraphEntity graphEntity, long id) {
/* 205 */     graphEntity.setId(id);
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
/*     */   private Edge deserializeEdge(List<Object> rawEdgeData) {
/* 217 */     List<List<Object>> rawProperties = (List<List<Object>>)rawEdgeData.get(4);
/*     */     
/* 219 */     Edge edge = new Edge(rawProperties.size());
/* 220 */     deserializeGraphEntityId((GraphEntity)edge, ((Long)rawEdgeData.get(0)).longValue());
/*     */     
/* 222 */     String relationshipType = this.graphCache.getRelationshipType(((Long)rawEdgeData.get(1)).intValue());
/* 223 */     edge.setRelationshipType(relationshipType);
/*     */     
/* 225 */     edge.setSource(((Long)rawEdgeData.get(2)).longValue());
/* 226 */     edge.setDestination(((Long)rawEdgeData.get(3)).longValue());
/*     */     
/* 228 */     deserializeGraphEntityProperties((GraphEntity)edge, rawProperties);
/*     */     
/* 230 */     return edge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void deserializeGraphEntityProperties(GraphEntity entity, List<List<Object>> rawProperties) {
/* 241 */     for (List<Object> rawProperty : rawProperties) {
/* 242 */       String name = this.graphCache.getPropertyName(((Long)rawProperty.get(0)).intValue());
/*     */ 
/*     */       
/* 245 */       List<Object> propertyScalar = rawProperty.subList(1, rawProperty.size());
/*     */       
/* 247 */       entity.addProperty(name, deserializeScalar(propertyScalar));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Object deserializeScalar(List<Object> rawScalarData) {
/* 258 */     ScalarType type = getValueTypeFromObject(rawScalarData.get(0));
/*     */     
/* 260 */     Object obj = rawScalarData.get(1);
/* 261 */     switch (type) {
/*     */       case NULL:
/* 263 */         return null;
/*     */       case BOOLEAN:
/* 265 */         return Boolean.valueOf(Boolean.parseBoolean(SafeEncoder.encode((byte[])obj)));
/*     */       case DOUBLE:
/* 267 */         return BuilderFactory.DOUBLE.build(obj);
/*     */       case INTEGER:
/* 269 */         return obj;
/*     */       case STRING:
/* 271 */         return SafeEncoder.encode((byte[])obj);
/*     */       case ARRAY:
/* 273 */         return deserializeArray(obj);
/*     */       case NODE:
/* 275 */         return deserializeNode((List<Object>)obj);
/*     */       case EDGE:
/* 277 */         return deserializeEdge((List<Object>)obj);
/*     */       case PATH:
/* 279 */         return deserializePath(obj);
/*     */       case MAP:
/* 281 */         return deserializeMap(obj);
/*     */       case POINT:
/* 283 */         return deserializePoint(obj);
/*     */     } 
/*     */     
/* 286 */     return obj;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object deserializePoint(Object rawScalarData) {
/* 291 */     return new Point((List)BuilderFactory.DOUBLE_LIST.build(rawScalarData));
/*     */   }
/*     */ 
/*     */   
/*     */   private Map<String, Object> deserializeMap(Object rawScalarData) {
/* 296 */     List<Object> keyTypeValueEntries = (List<Object>)rawScalarData;
/*     */     
/* 298 */     int size = keyTypeValueEntries.size();
/* 299 */     Map<String, Object> map = new HashMap<>(size >> 1);
/*     */     
/* 301 */     for (int i = 0; i < size; i += 2) {
/* 302 */       String key = SafeEncoder.encode((byte[])keyTypeValueEntries.get(i));
/* 303 */       Object value = deserializeScalar((List<Object>)keyTypeValueEntries.get(i + 1));
/* 304 */       map.put(key, value);
/*     */     } 
/* 306 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   private Path deserializePath(Object rawScalarData) {
/* 311 */     List<List<Object>> array = (List<List<Object>>)rawScalarData;
/* 312 */     List<Node> nodes = (List<Node>)deserializeScalar(array.get(0));
/* 313 */     List<Edge> edges = (List<Edge>)deserializeScalar(array.get(1));
/* 314 */     return new Path(nodes, edges);
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Object> deserializeArray(Object rawScalarData) {
/* 319 */     List<List<Object>> array = (List<List<Object>>)rawScalarData;
/* 320 */     List<Object> res = new ArrayList(array.size());
/* 321 */     for (List<Object> arrayValue : array) {
/* 322 */       res.add(deserializeScalar(arrayValue));
/*     */     }
/* 324 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ScalarType getValueTypeFromObject(Object rawScalarType) {
/* 334 */     return getScalarType(((Long)rawScalarType).intValue());
/*     */   }
/*     */   
/*     */   private enum ScalarType {
/* 338 */     UNKNOWN,
/* 339 */     NULL,
/* 340 */     STRING,
/* 341 */     INTEGER,
/* 342 */     BOOLEAN,
/* 343 */     DOUBLE,
/* 344 */     ARRAY,
/* 345 */     EDGE,
/* 346 */     NODE,
/* 347 */     PATH,
/* 348 */     MAP,
/* 349 */     POINT;
/*     */   }
/*     */   
/* 352 */   private static final ScalarType[] SCALAR_TYPES = ScalarType.values();
/*     */   
/*     */   private static ScalarType getScalarType(int index) {
/*     */     try {
/* 356 */       return SCALAR_TYPES[index];
/* 357 */     } catch (IndexOutOfBoundsException e) {
/* 358 */       throw new JedisException("Unrecognized response type");
/*     */     } 
/*     */   }
/*     */   
/*     */   private class RecordImpl
/*     */     implements Record {
/*     */     private final List<String> header;
/*     */     private final List<Object> values;
/*     */     
/*     */     public RecordImpl(List<String> header, List<Object> values) {
/* 368 */       this.header = header;
/* 369 */       this.values = values;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T getValue(int index) {
/* 374 */       return (T)this.values.get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T getValue(String key) {
/* 379 */       return getValue(this.header.indexOf(key));
/*     */     }
/*     */ 
/*     */     
/*     */     public String getString(int index) {
/* 384 */       return this.values.get(index).toString();
/*     */     }
/*     */ 
/*     */     
/*     */     public String getString(String key) {
/* 389 */       return getString(this.header.indexOf(key));
/*     */     }
/*     */ 
/*     */     
/*     */     public List<String> keys() {
/* 394 */       return this.header;
/*     */     }
/*     */ 
/*     */     
/*     */     public List<Object> values() {
/* 399 */       return this.values;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsKey(String key) {
/* 404 */       return this.header.contains(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 409 */       return this.header.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 414 */       if (this == o) {
/* 415 */         return true;
/*     */       }
/* 417 */       if (!(o instanceof RecordImpl)) {
/* 418 */         return false;
/*     */       }
/* 420 */       RecordImpl record = (RecordImpl)o;
/* 421 */       return (Objects.equals(this.header, record.header) && 
/* 422 */         Objects.equals(this.values, record.values));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 427 */       return Objects.hash(new Object[] { this.header, this.values });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 432 */       StringBuilder sb = new StringBuilder("Record{");
/* 433 */       sb.append("values=").append(this.values);
/* 434 */       sb.append('}');
/* 435 */       return sb.toString();
/*     */     }
/*     */   }
/*     */   
/* 439 */   private static final ResultSet.ColumnType[] COLUMN_TYPES = ResultSet.ColumnType.values();
/*     */   
/*     */   private class HeaderImpl
/*     */     implements Header {
/*     */     private final List<ResultSet.ColumnType> schemaTypes;
/*     */     private final List<String> schemaNames;
/*     */     
/*     */     private HeaderImpl() {
/* 447 */       this.schemaTypes = Collections.emptyList();
/* 448 */       this.schemaNames = Collections.emptyList();
/*     */     }
/*     */     
/*     */     private HeaderImpl(List<ResultSet.ColumnType> schemaTypes, List<String> schemaNames) {
/* 452 */       this.schemaTypes = schemaTypes;
/* 453 */       this.schemaNames = schemaNames;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<String> getSchemaNames() {
/* 461 */       return this.schemaNames;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<ResultSet.ColumnType> getSchemaTypes() {
/* 469 */       return this.schemaTypes;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 474 */       if (this == o) {
/* 475 */         return true;
/*     */       }
/* 477 */       if (!(o instanceof HeaderImpl)) {
/* 478 */         return false;
/*     */       }
/* 480 */       HeaderImpl header = (HeaderImpl)o;
/* 481 */       return (Objects.equals(getSchemaTypes(), header.getSchemaTypes()) && 
/* 482 */         Objects.equals(getSchemaNames(), header.getSchemaNames()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 487 */       return Objects.hash(new Object[] { getSchemaTypes(), getSchemaNames() });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 492 */       StringBuilder sb = new StringBuilder("HeaderImpl{");
/* 493 */       sb.append("schemaTypes=").append(this.schemaTypes);
/* 494 */       sb.append(", schemaNames=").append(this.schemaNames);
/* 495 */       sb.append('}');
/* 496 */       return sb.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private HeaderImpl parseHeader(Object data) {
/* 501 */     if (data == null) {
/* 502 */       return new HeaderImpl();
/*     */     }
/*     */     
/* 505 */     List<List<Object>> list = (List<List<Object>>)data;
/* 506 */     List<ResultSet.ColumnType> types = new ArrayList<>(list.size());
/* 507 */     List<String> texts = new ArrayList<>(list.size());
/* 508 */     for (List<Object> tuple : list) {
/* 509 */       types.add(COLUMN_TYPES[((Long)tuple.get(0)).intValue()]);
/* 510 */       texts.add(SafeEncoder.encode((byte[])tuple.get(1)));
/*     */     } 
/* 512 */     return new HeaderImpl(types, texts);
/*     */   }
/*     */   
/*     */   private class StatisticsImpl
/*     */     implements Statistics {
/*     */     private final Map<String, String> statistics;
/*     */     
/*     */     private StatisticsImpl(Map<String, String> statistics) {
/* 520 */       this.statistics = statistics;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getStringValue(String label) {
/* 529 */       return this.statistics.get(label);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int getIntValue(String label) {
/* 538 */       String value = getStringValue(label);
/* 539 */       return (value == null) ? 0 : Integer.parseInt(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int nodesCreated() {
/* 548 */       return getIntValue("Nodes created");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int nodesDeleted() {
/* 557 */       return getIntValue("Nodes deleted");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int indicesCreated() {
/* 566 */       return getIntValue("Indices created");
/*     */     }
/*     */ 
/*     */     
/*     */     public int indicesDeleted() {
/* 571 */       return getIntValue("Indices deleted");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int labelsAdded() {
/* 580 */       return getIntValue("Labels added");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int relationshipsDeleted() {
/* 589 */       return getIntValue("Relationships deleted");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int relationshipsCreated() {
/* 598 */       return getIntValue("Relationships created");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int propertiesSet() {
/* 607 */       return getIntValue("Properties set");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean cachedExecution() {
/* 616 */       return "1".equals(getStringValue("Cached execution"));
/*     */     }
/*     */ 
/*     */     
/*     */     public String queryIntervalExecutionTime() {
/* 621 */       return getStringValue("Query internal execution time");
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 626 */       if (this == o) {
/* 627 */         return true;
/*     */       }
/* 629 */       if (!(o instanceof StatisticsImpl)) {
/* 630 */         return false;
/*     */       }
/* 632 */       StatisticsImpl that = (StatisticsImpl)o;
/* 633 */       return Objects.equals(this.statistics, that.statistics);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 638 */       return Objects.hash(new Object[] { this.statistics });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 643 */       StringBuilder sb = new StringBuilder("Statistics{");
/* 644 */       sb.append(this.statistics);
/* 645 */       sb.append('}');
/* 646 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private StatisticsImpl parseStatistics(Object data) {
/* 653 */     Map<String, String> map = (Map<String, String>)((List)data).stream().map(SafeEncoder::encode).map(s -> s.split(": ")).collect(Collectors.toMap(sa -> sa[0], sa -> sa[1]));
/* 654 */     return new StatisticsImpl(map);
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\graph\ResultSetBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */