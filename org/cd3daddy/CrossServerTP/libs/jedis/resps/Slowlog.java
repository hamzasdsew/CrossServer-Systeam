/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.resps;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.BuilderFactory;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.util.SafeEncoder;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Slowlog
/*    */ {
/*    */   private final long id;
/*    */   private final long timeStamp;
/*    */   private final long executionTime;
/*    */   private final List<String> args;
/*    */   private HostAndPort clientIpPort;
/*    */   private String clientName;
/*    */   private static final String COMMA = ",";
/*    */   
/*    */   private Slowlog(List<Object> properties) {
/* 22 */     this.id = ((Long)properties.get(0)).longValue();
/* 23 */     this.timeStamp = ((Long)properties.get(1)).longValue();
/* 24 */     this.executionTime = ((Long)properties.get(2)).longValue();
/*    */     
/* 26 */     this.args = (List<String>)BuilderFactory.STRING_LIST.build(properties.get(3));
/* 27 */     if (properties.size() == 4)
/*    */       return; 
/* 29 */     this.clientIpPort = HostAndPort.from(SafeEncoder.encode((byte[])properties.get(4)));
/* 30 */     this.clientName = SafeEncoder.encode((byte[])properties.get(5));
/*    */   }
/*    */ 
/*    */   
/*    */   public static List<Slowlog> from(List<Object> nestedMultiBulkReply) {
/* 35 */     List<Slowlog> logs = new ArrayList<>(nestedMultiBulkReply.size());
/* 36 */     for (Object obj : nestedMultiBulkReply) {
/* 37 */       List<Object> properties = (List<Object>)obj;
/* 38 */       logs.add(new Slowlog(properties));
/*    */     } 
/* 40 */     return logs;
/*    */   }
/*    */   
/*    */   public long getId() {
/* 44 */     return this.id;
/*    */   }
/*    */   
/*    */   public long getTimeStamp() {
/* 48 */     return this.timeStamp;
/*    */   }
/*    */   
/*    */   public long getExecutionTime() {
/* 52 */     return this.executionTime;
/*    */   }
/*    */   
/*    */   public List<String> getArgs() {
/* 56 */     return this.args;
/*    */   }
/*    */   
/*    */   public HostAndPort getClientIpPort() {
/* 60 */     return this.clientIpPort;
/*    */   }
/*    */   
/*    */   public String getClientName() {
/* 64 */     return this.clientName;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 69 */     return this.id + "," + this.timeStamp + "," + this.executionTime + 
/* 70 */       "," + this.args;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\resps\Slowlog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */