/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.io.InputStream;
/*    */ import java.util.Properties;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JedisMetaInfo
/*    */ {
/*    */   private static final String groupId;
/*    */   private static final String artifactId;
/*    */   private static final String version;
/*    */   
/*    */   static {
/* 17 */     Properties p = new Properties();
/*    */     
/* 19 */     try (InputStream in = JedisMetaInfo.class.getClassLoader().getResourceAsStream("org/cd3daddy/CrossServerTP/libs/jedis/pom.properties")) {
/* 20 */       p.load(in);
/* 21 */     } catch (Exception e) {
/* 22 */       LoggerFactory.getLogger(JedisMetaInfo.class)
/* 23 */         .error("Load Jedis meta info from pom.properties failed", e);
/*    */     } 
/*    */     
/* 26 */     groupId = p.getProperty("groupId", null);
/* 27 */     artifactId = p.getProperty("artifactId", null);
/* 28 */     version = p.getProperty("version", null);
/*    */   }
/*    */   
/*    */   public static String getGroupId() {
/* 32 */     return groupId;
/*    */   }
/*    */   
/*    */   public static String getArtifactId() {
/* 36 */     return artifactId;
/*    */   }
/*    */   
/*    */   public static String getVersion() {
/* 40 */     return version;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisMetaInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */