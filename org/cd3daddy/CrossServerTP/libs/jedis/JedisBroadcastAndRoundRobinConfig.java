/*   */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*   */ public interface JedisBroadcastAndRoundRobinConfig {
/*   */   RediSearchMode getRediSearchModeInCluster();
/*   */   
/*   */   public enum RediSearchMode {
/* 6 */     DEFAULT, LIGHT;
/*   */   }
/*   */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\JedisBroadcastAndRoundRobinConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */