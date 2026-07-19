/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.util;
/*    */ 
/*    */ import java.util.AbstractMap;
/*    */ 
/*    */ public class KeyValue<K, V>
/*    */   extends AbstractMap.SimpleImmutableEntry<K, V> {
/*    */   public KeyValue(K key, V value) {
/*  8 */     super(key, value);
/*    */   }
/*    */   
/*    */   public static <K, V> KeyValue<K, V> of(K key, V value) {
/* 12 */     return new KeyValue<>(key, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedi\\util\KeyValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */