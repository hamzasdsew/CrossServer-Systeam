/*    */ package org.cd3daddy.CrossServerTP.libs.jedis;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Module
/*    */ {
/*    */   private final String name;
/*    */   private final int version;
/*    */   
/*    */   public Module(String name, int version) {
/* 11 */     this.name = name;
/* 12 */     this.version = version;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 16 */     return this.name;
/*    */   }
/*    */   
/*    */   public int getVersion() {
/* 20 */     return this.version;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 25 */     if (o == null) return false; 
/* 26 */     if (o == this) return true; 
/* 27 */     if (!(o instanceof Module)) return false;
/*    */     
/* 29 */     Module module = (Module)o;
/*    */     
/* 31 */     if (this.version != module.version) return false; 
/* 32 */     if ((this.name != null) ? !this.name.equals(module.name) : (module.name != null)) return false;
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 38 */     int result = (this.name != null) ? this.name.hashCode() : 0;
/* 39 */     result = 31 * result + this.version;
/* 40 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\jedis\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */