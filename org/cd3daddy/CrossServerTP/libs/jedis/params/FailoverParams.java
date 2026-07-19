/*    */ package org.cd3daddy.CrossServerTP.libs.jedis.params;
/*    */ 
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.CommandArguments;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.HostAndPort;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.Protocol;
/*    */ 
/*    */ 
/*    */ public class FailoverParams
/*    */   implements IParams
/*    */ {
/*    */   private HostAndPort to;
/*    */   private boolean force;
/*    */   private Long timeout;
/*    */   
/*    */   public static FailoverParams failoverParams() {
/* 16 */     return new FailoverParams();
/*    */   }
/*    */   
/*    */   public FailoverParams to(String host, int port) {
/* 20 */     return to(new HostAndPort(host, port));
/*    */   }
/*    */   
/*    */   public FailoverParams to(HostAndPort to) {
/* 24 */     this.to = to;
/* 25 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FailoverParams force() {
/* 34 */     this.force = true;
/* 35 */     return this;
/*    */   }
/*    */   
/*    */   public FailoverParams timeout(long timeout) {
/* 39 */     this.timeout = Long.valueOf(timeout);
/* 40 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addParams(CommandArguments args) {
/* 46 */     if (this.to != null) {
/* 47 */       args.add(Protocol.Keyword.TO).add(this.to.getHost()).add(Integer.valueOf(this.to.getPort()));
/*    */     }
/*    */     
/* 50 */     if (this.force) {
/* 51 */       if (this.to == null || this.timeout == null) {
/* 52 */         throw new IllegalArgumentException("FAILOVER with force option requires both a timeout and target HOST and IP.");
/*    */       }
/* 54 */       args.add(Protocol.Keyword.FORCE);
/*    */     } 
/*    */     
/* 57 */     if (this.timeout != null)
/* 58 */       args.add(Protocol.Keyword.TIMEOUT).add(this.timeout); 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\params\FailoverParams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */