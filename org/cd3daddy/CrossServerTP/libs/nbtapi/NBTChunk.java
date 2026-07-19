/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import org.bukkit.Chunk;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.CheckUtil;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*    */ 
/*    */ 
/*    */ public class NBTChunk
/*    */ {
/*    */   private final Chunk chunk;
/*    */   
/*    */   public NBTChunk(Chunk chunk) {
/* 13 */     this.chunk = chunk;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NBTCompound getPersistentDataContainer() {
/* 23 */     CheckUtil.assertAvailable(MinecraftVersion.MC1_16_R3);
/* 24 */     return new NBTPersistentDataContainer(this.chunk.getPersistentDataContainer());
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTChunk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */