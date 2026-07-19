/*     */ package com.alphastudio.CrossBorderCore.storage;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.block.Block;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BlockManager
/*     */ {
/*     */   public static List<BlockData> collectAirBlocks(Location playerLocation, int radius, String borderAxis, double borderCoordinate, String borderDirection) {
/*  45 */     List<BlockData> airBlocks = new ArrayList<>();
/*  46 */     World world = playerLocation.getWorld();
/*  47 */     if (world == null) return airBlocks;
/*     */     
/*  49 */     int centerX = playerLocation.getBlockX();
/*  50 */     int centerY = playerLocation.getBlockY();
/*  51 */     int centerZ = playerLocation.getBlockZ();
/*     */ 
/*     */     
/*  54 */     for (int xOff = -radius; xOff <= radius; xOff++) {
/*  55 */       for (int yOff = -radius; yOff <= radius; yOff++) {
/*  56 */         for (int zOff = -radius; zOff <= radius; zOff++) {
/*  57 */           int x = centerX + xOff;
/*  58 */           int y = centerY + yOff;
/*  59 */           int z = centerZ + zOff;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  74 */           boolean isOnSourceSide = false;
/*  75 */           if (borderAxis != null) {
/*  76 */             if (borderAxis.equals("X")) {
/*     */               
/*  78 */               isOnSourceSide = borderDirection.equals("POSITIVE") ? ((x <= borderCoordinate)) : ((x >= borderCoordinate));
/*  79 */             } else if (borderAxis.equals("Z")) {
/*     */               
/*  81 */               isOnSourceSide = borderDirection.equals("POSITIVE") ? ((z <= borderCoordinate)) : ((z >= borderCoordinate));
/*  82 */             } else if (borderAxis.equals("Y")) {
/*     */               
/*  84 */               isOnSourceSide = borderDirection.equals("POSITIVE") ? ((y <= borderCoordinate)) : ((y >= borderCoordinate));
/*     */             } 
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*  90 */           if (isOnSourceSide) {
/*  91 */             Block block = world.getBlockAt(x, y, z);
/*  92 */             if (block.getType() == Material.AIR) {
/*  93 */               airBlocks.add(BlockData.fromBlock(block, playerLocation));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 100 */     return airBlocks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearBlocksAtDestination(Location playerLocation, String borderAxis, double borderCoordinate, String borderDirection, boolean isCrawling) {
/* 116 */     World world = playerLocation.getWorld();
/* 117 */     if (world == null)
/*     */       return; 
/* 119 */     int centerX = playerLocation.getBlockX();
/* 120 */     int centerY = playerLocation.getBlockY();
/* 121 */     int centerZ = playerLocation.getBlockZ();
/*     */ 
/*     */     
/* 124 */     for (int xOff = -3; xOff <= 3; xOff++) {
/* 125 */       for (int yOff = -3; yOff <= 3; yOff++) {
/* 126 */         for (int zOff = -3; zOff <= 3; zOff++) {
/* 127 */           int x = centerX + xOff;
/* 128 */           int y = centerY + yOff;
/* 129 */           int z = centerZ + zOff;
/*     */           
/* 131 */           Block block = world.getBlockAt(x, y, z);
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 136 */           boolean isOnThisServerSide = false;
/* 137 */           if (borderAxis != null) {
/* 138 */             if (borderAxis.equals("X")) {
/*     */ 
/*     */ 
/*     */               
/* 142 */               isOnThisServerSide = borderDirection.equals("POSITIVE") ? ((x <= borderCoordinate)) : ((x >= borderCoordinate));
/* 143 */             } else if (borderAxis.equals("Z")) {
/*     */               
/* 145 */               isOnThisServerSide = borderDirection.equals("POSITIVE") ? ((z <= borderCoordinate)) : ((z >= borderCoordinate));
/* 146 */             } else if (borderAxis.equals("Y")) {
/*     */               
/* 148 */               isOnThisServerSide = borderDirection.equals("POSITIVE") ? ((y <= borderCoordinate)) : ((y >= borderCoordinate));
/*     */             } 
/*     */           }
/*     */           
/* 152 */           boolean shouldClear = false;
/*     */           
/* 154 */           if (isOnThisServerSide) {
/*     */ 
/*     */             
/* 157 */             if (block.getType() == Material.AIR) {
/* 158 */               shouldClear = true;
/*     */             }
/*     */           }
/*     */           else {
/*     */             
/* 163 */             int maxY = isCrawling ? 0 : 1;
/* 164 */             boolean isInSpawnArea = (xOff == 0 && zOff == 0 && yOff >= 0 && yOff <= maxY);
/*     */             
/* 166 */             if (isInSpawnArea)
/*     */             {
/* 168 */               shouldClear = block.getType().isSolid();
/*     */             }
/*     */           } 
/*     */           
/* 172 */           if (shouldClear) {
/* 173 */             block.setType(Material.AIR, false);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void restoreBlocks(List<BlockData> blocks, Location playerLocation, String borderAxis, double borderCoordinate, String borderDirection, boolean isCrawling) {
/* 196 */     World world = playerLocation.getWorld();
/* 197 */     if (world == null) {
/*     */       return;
/*     */     }
/* 200 */     if (blocks != null) {
/* 201 */       for (BlockData blockData : blocks) {
/* 202 */         Location blockLoc = playerLocation.clone();
/* 203 */         blockLoc.add(blockData.offsetX, blockData.offsetY, blockData.offsetZ);
/* 204 */         Block block = world.getBlockAt(blockLoc);
/*     */         
/* 206 */         block.setType(Material.AIR, false);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 213 */     int centerX = playerLocation.getBlockX();
/* 214 */     int centerY = playerLocation.getBlockY();
/* 215 */     int centerZ = playerLocation.getBlockZ();
/*     */ 
/*     */ 
/*     */     
/* 219 */     int maxY = isCrawling ? 0 : 1;
/* 220 */     for (int yOff = 0; yOff <= maxY; yOff++) {
/* 221 */       Block block = world.getBlockAt(centerX, centerY + yOff, centerZ);
/*     */ 
/*     */       
/* 224 */       if (block.getType().isSolid())
/* 225 */         block.setType(Material.AIR, false); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\storage\BlockManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */