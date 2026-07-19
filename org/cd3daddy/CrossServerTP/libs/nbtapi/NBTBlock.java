/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import org.bukkit.block.Block;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTBlock
/*    */ {
/*    */   private final Block block;
/*    */   private final NBTChunk nbtChunk;
/*    */   
/*    */   public NBTBlock(Block block) {
/* 26 */     this.block = block;
/* 27 */     if (!MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_16_R3)) {
/* 28 */       throw new NbtApiException("NBTBlock is only working for 1.16.4+!");
/*    */     }
/* 30 */     this.nbtChunk = new NBTChunk(block.getChunk());
/*    */   }
/*    */   
/*    */   public NBTCompound getData() {
/* 34 */     return this.nbtChunk.getPersistentDataContainer().getOrCreateCompound("blocks")
/* 35 */       .getOrCreateCompound(this.block.getX() + "_" + this.block.getY() + "_" + this.block.getZ());
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */