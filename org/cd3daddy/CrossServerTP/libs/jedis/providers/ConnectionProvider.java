/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.providers;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Connection;
/*    */ 
/*    */ public interface ConnectionProvider
/*    */   extends AutoCloseable {
/*    */   Connection getConnection();
/*    */   
/*    */   Connection getConnection(CommandArguments paramCommandArguments);
/*    */   
/*    */   default Map<?, ?> getConnectionMap() {
/* 15 */     Connection c = getConnection();
/* 16 */     return Collections.singletonMap(c.toString(), c);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\providers\ConnectionProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */