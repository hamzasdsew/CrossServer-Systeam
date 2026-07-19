/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.search;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.args.Rawable;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.commands.ProtocolCommand;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ public class SearchProtocol
/*    */ {
/*    */   public enum SearchCommand
/*    */     implements ProtocolCommand {
/* 11 */     CREATE("FT.CREATE"),
/* 12 */     ALTER("FT.ALTER"),
/* 13 */     INFO("FT.INFO"),
/* 14 */     SEARCH("FT.SEARCH"),
/* 15 */     EXPLAIN("FT.EXPLAIN"),
/* 16 */     EXPLAINCLI("FT.EXPLAINCLI"),
/* 17 */     AGGREGATE("FT.AGGREGATE"),
/* 18 */     CURSOR("FT.CURSOR"),
/* 19 */     CONFIG("FT.CONFIG"),
/* 20 */     ALIASADD("FT.ALIASADD"),
/* 21 */     ALIASUPDATE("FT.ALIASUPDATE"),
/* 22 */     ALIASDEL("FT.ALIASDEL"),
/* 23 */     SYNUPDATE("FT.SYNUPDATE"),
/* 24 */     SYNDUMP("FT.SYNDUMP"),
/* 25 */     SUGADD("FT.SUGADD"),
/* 26 */     SUGGET("FT.SUGGET"),
/* 27 */     SUGDEL("FT.SUGDEL"),
/* 28 */     SUGLEN("FT.SUGLEN"),
/* 29 */     DROPINDEX("FT.DROPINDEX"),
/* 30 */     DICTADD("FT.DICTADD"),
/* 31 */     DICTDEL("FT.DICTDEL"),
/* 32 */     DICTDUMP("FT.DICTDUMP"),
/* 33 */     SPELLCHECK("FT.SPELLCHECK"),
/* 34 */     TAGVALS("FT.TAGVALS"),
/* 35 */     PROFILE("FT.PROFILE"),
/* 36 */     _LIST("FT._LIST");
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     SearchCommand(String alt) {
/* 41 */       this.raw = SafeEncoder.encode(alt);
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 46 */       return this.raw;
/*    */     }
/*    */   }
/*    */   
/*    */   public enum SearchKeyword
/*    */     implements Rawable {
/* 52 */     SCHEMA, TEXT, TAG, NUMERIC, GEO, GEOSHAPE, VECTOR, VERBATIM, NOCONTENT, NOSTOPWORDS, WITHSCORES,
/* 53 */     LANGUAGE, INFIELDS, SORTBY, ASC, DESC, LIMIT, HIGHLIGHT, FIELDS, TAGS, SUMMARIZE, FRAGS, LEN,
/* 54 */     SEPARATOR, INKEYS, RETURN, FILTER, GEOFILTER, ADD, INCR, MAX, FUZZY, READ, DEL, DD, TEMPORARY,
/* 55 */     STOPWORDS, NOFREQS, NOFIELDS, NOOFFSETS, NOHL, SET, GET, ON, SORTABLE, UNF, PREFIX,
/* 56 */     LANGUAGE_FIELD, SCORE, SCORE_FIELD, SCORER, PARAMS, AS, DIALECT, SLOP, TIMEOUT, INORDER,
/* 57 */     EXPANDER, MAXTEXTFIELDS, SKIPINITIALSCAN, WITHSUFFIXTRIE, NOSTEM, NOINDEX, PHONETIC, WEIGHT,
/* 58 */     CASESENSITIVE, LOAD, APPLY, GROUPBY, MAXIDLE, WITHCURSOR, DISTANCE, TERMS, INCLUDE, EXCLUDE,
/* 59 */     SEARCH, AGGREGATE, QUERY, LIMITED, COUNT, REDUCE;
/*    */     
/*    */     private final byte[] raw;
/*    */     
/*    */     SearchKeyword() {
/* 64 */       this.raw = SafeEncoder.encode(name());
/*    */     }
/*    */ 
/*    */     
/*    */     public byte[] getRaw() {
/* 69 */       return this.raw;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\search\SearchProtocol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */