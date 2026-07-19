/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.time.Duration;
/*    */ import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
/*    */ 
/*    */ public class ConnectionPoolConfig
/*    */   extends GenericObjectPoolConfig<Connection>
/*    */ {
/*    */   public ConnectionPoolConfig() {
/* 10 */     setTestWhileIdle(true);
/* 11 */     setMinEvictableIdleTime(Duration.ofMillis(60000L));
/* 12 */     setTimeBetweenEvictionRuns(Duration.ofMillis(30000L));
/* 13 */     setNumTestsPerEvictionRun(-1);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ConnectionPoolConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */