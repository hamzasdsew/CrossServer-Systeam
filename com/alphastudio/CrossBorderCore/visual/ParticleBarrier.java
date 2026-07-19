/*     */ package com.alphastudio.CrossBorderCore.visual;
/*     */ 
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Particle;
/*     */ import org.bukkit.World;
/*     */ import org.bukkit.entity.Player;
/*     */ import com.alphastudio.CrossBorderCore.config.BorderConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleBarrier
/*     */ {
/*     */   private static final int PARTICLE_HEIGHT = 20;
/*     */   private static final double PARTICLE_SPACING = 2.0D;
/*     */   private static final int MAX_PARTICLES_PER_FRAME = 500;
/*  22 */   private static Particle particleType = Particle.FLAME;
/*     */   
/*     */   public static void setParticleType(Particle particle) {
/*  25 */     particleType = particle;
/*     */   }
/*     */   
/*     */   public static void showBarrier(Player player, BorderConfig border, int viewDistance) {
/*  29 */     World world = player.getWorld();
/*  30 */     if (!world.getName().equals(border.getWorld())) {
/*     */       return;
/*     */     }
/*     */     
/*  34 */     Location playerLoc = player.getLocation();
/*  35 */     double coordinate = border.getCoordinate();
/*     */ 
/*     */     
/*  38 */     switch (border.getSide()) {
/*     */       
/*     */       case EAST:
/*     */       case WEST:
/*  42 */         showVerticalBarrierX(player, world, coordinate, playerLoc, viewDistance);
/*     */         break;
/*     */ 
/*     */       
/*     */       case NORTH:
/*     */       case SOUTH:
/*  48 */         showVerticalBarrierZ(player, world, coordinate, playerLoc, viewDistance);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void showVerticalBarrierX(Player player, World world, double xCoord, Location playerLoc, int viewDistance) {
/*  54 */     double playerZ = playerLoc.getZ();
/*  55 */     double playerY = playerLoc.getY();
/*     */ 
/*     */     
/*  58 */     int extendedDistance = Math.min(viewDistance + 20, 50);
/*     */     
/*  60 */     int particleCount = 0;
/*     */     
/*     */     double offset;
/*     */     
/*  64 */     for (offset = 0.0D; offset <= extendedDistance; offset += 2.0D) {
/*     */       
/*  66 */       for (int direction = -1; direction <= 1; direction += 2) {
/*  67 */         if (offset != 0.0D || direction != -1) {
/*     */           
/*  69 */           double z = playerZ + offset * direction;
/*     */ 
/*     */           
/*  72 */           for (int yOffset = -20; yOffset <= 20; yOffset++) {
/*  73 */             if (particleCount >= 500) {
/*     */               return;
/*     */             }
/*     */             
/*  77 */             double actualY = playerY + yOffset;
/*  78 */             if (actualY >= world.getMinHeight() && actualY <= world.getMaxHeight()) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*  83 */               Location particleLoc = new Location(world, xCoord, actualY, z);
/*     */ 
/*     */               
/*     */               try {
/*  87 */                 player.spawnParticle(particleType, particleLoc, 1, 0.1D, 0.1D, 0.1D, 0.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*  94 */                 particleCount++;
/*  95 */               } catch (Exception exception) {}
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void showVerticalBarrierZ(Player player, World world, double zCoord, Location playerLoc, int viewDistance) {
/* 104 */     double playerX = playerLoc.getX();
/* 105 */     double playerY = playerLoc.getY();
/*     */ 
/*     */     
/* 108 */     int extendedDistance = Math.min(viewDistance + 20, 50);
/*     */     
/* 110 */     int particleCount = 0;
/*     */     
/*     */     double offset;
/*     */     
/* 114 */     for (offset = 0.0D; offset <= extendedDistance; offset += 2.0D) {
/*     */       
/* 116 */       for (int direction = -1; direction <= 1; direction += 2) {
/* 117 */         if (offset != 0.0D || direction != -1) {
/*     */           
/* 119 */           double x = playerX + offset * direction;
/*     */ 
/*     */           
/* 122 */           for (int yOffset = -20; yOffset <= 20; yOffset++) {
/* 123 */             if (particleCount >= 500) {
/*     */               return;
/*     */             }
/*     */             
/* 127 */             double actualY = playerY + yOffset;
/* 128 */             if (actualY >= world.getMinHeight() && actualY <= world.getMaxHeight()) {
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 133 */               Location particleLoc = new Location(world, x, actualY, zCoord);
/*     */ 
/*     */               
/*     */               try {
/* 137 */                 player.spawnParticle(particleType, particleLoc, 1, 0.1D, 0.1D, 0.1D, 0.0D);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/* 144 */                 particleCount++;
/* 145 */               } catch (Exception exception) {}
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\visual\ParticleBarrier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.1.3
 */