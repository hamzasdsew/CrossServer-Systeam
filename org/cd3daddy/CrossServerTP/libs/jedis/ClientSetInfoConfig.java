/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ import org.cd3daddy.CrossServerTP.libs.jedis.exceptions.JedisValidationException;
/*    */ 
/*    */ 
/*    */ public final class ClientSetInfoConfig
/*    */ {
/*    */   private final boolean disabled;
/*    */   private final String libNameSuffix;
/*    */   
/*    */   public ClientSetInfoConfig() {
/* 14 */     this(false, null);
/*    */   }
/*    */   
/*    */   public ClientSetInfoConfig(boolean disabled) {
/* 18 */     this(disabled, null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientSetInfoConfig(String libNameSuffix) {
/* 25 */     this(false, libNameSuffix);
/*    */   }
/*    */   
/*    */   private ClientSetInfoConfig(boolean disabled, String libNameSuffix) {
/* 29 */     this.disabled = disabled;
/* 30 */     this.libNameSuffix = validateLibNameSuffix(libNameSuffix);
/*    */   }
/*    */   
/* 33 */   private static final HashSet<Character> BRACES = new HashSet<>(Arrays.asList(new Character[] { Character.valueOf('('), Character.valueOf(')'), Character.valueOf('['), Character.valueOf(']'), Character.valueOf('{'), Character.valueOf('}') }));
/*    */   
/*    */   private static String validateLibNameSuffix(String suffix) {
/* 36 */     if (suffix == null || suffix.trim().isEmpty()) {
/* 37 */       return null;
/*    */     }
/*    */     
/* 40 */     for (int i = 0; i < suffix.length(); i++) {
/* 41 */       char c = suffix.charAt(i);
/* 42 */       if (c < ' ' || c > '~' || BRACES.contains(Character.valueOf(c))) {
/* 43 */         throw new JedisValidationException("lib-name suffix cannot contain braces, newlines or special characters.");
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 48 */     return suffix.replaceAll("\\s", "-");
/*    */   }
/*    */   
/*    */   public final boolean isDisabled() {
/* 52 */     return this.disabled;
/*    */   }
/*    */   
/*    */   public final String getLibNameSuffix() {
/* 56 */     return this.libNameSuffix;
/*    */   }
/*    */   
/* 59 */   public static final ClientSetInfoConfig DEFAULT = new ClientSetInfoConfig();
/*    */   
/* 61 */   public static final ClientSetInfoConfig DISABLED = new ClientSetInfoConfig(true);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static ClientSetInfoConfig withLibNameSuffix(String suffix) {
/* 68 */     return new ClientSetInfoConfig(suffix);
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\ClientSetInfoConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */