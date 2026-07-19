/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.gears.resps;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Builder;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.KeyValue;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FunctionInfo
/*    */ {
/*    */   private final String name;
/*    */   private final String description;
/*    */   private final boolean isAsync;
/*    */   private final List<String> flags;
/*    */   
/*    */   public String getName() {
/* 21 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 25 */     return this.description;
/*    */   }
/*    */   
/*    */   public boolean isAsync() {
/* 29 */     return this.isAsync;
/*    */   }
/*    */   
/*    */   public List<String> getFlags() {
/* 33 */     return this.flags;
/*    */   }
/*    */   
/*    */   public FunctionInfo(String name, String description, boolean isAsync, List<String> flags) {
/* 37 */     this.name = name;
/* 38 */     this.description = description;
/* 39 */     this.isAsync = isAsync;
/* 40 */     this.flags = flags;
/*    */   }
/*    */   
/* 43 */   public static final Builder<List<FunctionInfo>> FUNCTION_INFO_LIST = new Builder<List<FunctionInfo>>()
/*    */     {
/*    */       public List<FunctionInfo> build(Object data) {
/* 46 */         List<Object> dataAsList = (List<Object>)data;
/* 47 */         if (!dataAsList.isEmpty()) {
/* 48 */           boolean isListOfList = dataAsList.get(0).getClass().isAssignableFrom(ArrayList.class);
/*    */           
/* 50 */           if (isListOfList) {
/* 51 */             if (((List)((List)data).get(0)).get(0) instanceof KeyValue) {
/* 52 */               List<List<KeyValue>> dataAsKeyValues = (List<List<KeyValue>>)data;
/* 53 */               return (List<FunctionInfo>)dataAsKeyValues.stream().map(keyValues -> {
/*    */                     String name = null;
/*    */                     String description = null;
/*    */                     List<String> flags = Collections.emptyList();
/*    */                     boolean isAsync = false;
/*    */                     for (KeyValue kv : keyValues) {
/*    */                       switch ((String)BuilderFactory.STRING.build(kv.getKey())) {
/*    */                         case "name":
/*    */                           name = (String)BuilderFactory.STRING.build(kv.getValue());
/*    */                         
/*    */                         case "description":
/*    */                           description = (String)BuilderFactory.STRING.build(kv.getValue());
/*    */                         
/*    */                         case "raw-arguments":
/*    */                           flags = (List<String>)BuilderFactory.STRING_LIST.build(kv.getValue());
/*    */                         
/*    */                         case "is_async":
/*    */                           isAsync = ((Boolean)BuilderFactory.BOOLEAN.build(kv.getValue())).booleanValue();
/*    */                       } 
/*    */                     
/*    */                     } 
/*    */                     return new FunctionInfo(name, description, isAsync, flags);
/* 75 */                   }).collect(Collectors.toList());
/*    */             } 
/* 77 */             return (List<FunctionInfo>)dataAsList.stream().map(pairObject -> (List)pairObject)
/* 78 */               .map(pairList -> new FunctionInfo((String)BuilderFactory.STRING.build(pairList.get(7)), (String)BuilderFactory.STRING.build(pairList.get(1)), ((Boolean)BuilderFactory.BOOLEAN.build(pairList.get(5))).booleanValue(), (List<String>)BuilderFactory.STRING_LIST.build(pairList.get(3))))
/*    */ 
/*    */ 
/*    */ 
/*    */               
/* 83 */               .collect(Collectors.toList());
/*    */           } 
/*    */           
/* 86 */           return (List<FunctionInfo>)dataAsList.stream()
/* 87 */             .map(BuilderFactory.STRING::build)
/* 88 */             .map(name -> new FunctionInfo(name, null, false, null))
/* 89 */             .collect(Collectors.toList());
/*    */         } 
/*    */         
/* 92 */         return Collections.emptyList();
/*    */       }
/*    */     };
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\gears\resps\FunctionInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */