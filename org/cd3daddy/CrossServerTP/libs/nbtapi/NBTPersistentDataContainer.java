/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import java.util.Map;
/*    */ import org.bukkit.persistence.PersistentDataContainer;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*    */ 
/*    */ 
/*    */ public class NBTPersistentDataContainer
/*    */   extends NBTCompound
/*    */ {
/*    */   private final PersistentDataContainer container;
/*    */   
/*    */   public NBTPersistentDataContainer(PersistentDataContainer container) {
/* 14 */     super(null, null);
/* 15 */     this.container = container;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getCompound() {
/* 20 */     return ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_TO_TAG.run(this.container, new Object[0]);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void setCompound(Object compound) {
/* 27 */     Map<Object, Object> map = (Map<Object, Object>)ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_GET_MAP.run(this.container, new Object[0]);
/* 28 */     map.clear();
/* 29 */     ReflectionMethod.CRAFT_PERSISTENT_DATA_CONTAINER_PUT_ALL.run(this.container, new Object[] { compound });
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTPersistentDataContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */