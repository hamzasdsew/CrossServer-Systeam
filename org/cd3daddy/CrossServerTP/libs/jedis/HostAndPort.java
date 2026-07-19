/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class HostAndPort
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -519876229978427751L;
/*    */   private final String host;
/*    */   private final int port;
/*    */   
/*    */   public HostAndPort(String host, int port) {
/* 13 */     this.host = host;
/* 14 */     this.port = port;
/*    */   }
/*    */   
/*    */   public String getHost() {
/* 18 */     return this.host;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 22 */     return this.port;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 27 */     if (obj == null) return false; 
/* 28 */     if (obj == this) return true; 
/* 29 */     if (!(obj instanceof HostAndPort)) return false; 
/* 30 */     HostAndPort other = (HostAndPort)obj;
/* 31 */     return (this.port == other.port && this.host.equals(other.host));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 36 */     return 31 * this.host.hashCode() + this.port;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 41 */     return this.host + ":" + this.port;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static HostAndPort from(String string) {
/* 50 */     int lastColon = string.lastIndexOf(":");
/* 51 */     String host = string.substring(0, lastColon);
/* 52 */     int port = Integer.parseInt(string.substring(lastColon + 1));
/* 53 */     return new HostAndPort(host, port);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\HostAndPort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */