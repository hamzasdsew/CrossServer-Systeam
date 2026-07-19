/*    */ package com.alphastudio.CrossBorderCore.storage;
/*    */ 
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.block.Block;
/*    */ import org.bukkit.block.BlockState;
/*    */ import org.bukkit.block.Container;
/*    */ import org.bukkit.block.Sign;
/*    */ import org.bukkit.inventory.ItemStack;
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
/*    */ 
/*    */ 
/*    */ public class BlockData
/*    */ {
/*    */   public int offsetX;
/*    */   public int offsetY;
/*    */   public int offsetZ;
/*    */   public String material;
/*    */   public String blockDataString;
/*    */   public ItemStack[] containerContents;
/*    */   public String[] signLines;
/*    */   public String tileEntityType;
/*    */   
/*    */   public static BlockData fromBlock(Block block, Location playerLocation) {
/* 38 */     BlockData data = new BlockData();
/*    */ 
/*    */     
/* 41 */     Location blockLoc = block.getLocation();
/* 42 */     data.offsetX = blockLoc.getBlockX() - playerLocation.getBlockX();
/* 43 */     data.offsetY = blockLoc.getBlockY() - playerLocation.getBlockY();
/* 44 */     data.offsetZ = blockLoc.getBlockZ() - playerLocation.getBlockZ();
/*    */ 
/*    */     
/* 47 */     data.material = block.getType().name();
/*    */ 
/*    */     
/* 50 */     data.blockDataString = block.getBlockData().getAsString();
/*    */ 
/*    */     
/* 53 */     BlockState state = block.getState();
/*    */ 
/*    */     
/* 56 */     if (state instanceof Container) {
/* 57 */       Container container = (Container)state;
/* 58 */       data.tileEntityType = "CONTAINER";
/* 59 */       data.containerContents = container.getInventory().getContents();
/*    */     } 
/*    */ 
/*    */     
/* 63 */     if (state instanceof Sign) {
/* 64 */       Sign sign = (Sign)state;
/* 65 */       data.tileEntityType = "SIGN";
/* 66 */       data.signLines = sign.getLines();
/*    */     } 
/*    */     
/* 69 */     return data;
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\storage\BlockData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */