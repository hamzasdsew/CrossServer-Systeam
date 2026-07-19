/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.GameprofileUtil;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.MinecraftVersion;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ObjectCreator;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTGameProfile
/*    */ {
/*    */   @Deprecated
/*    */   public static NBTCompound toNBT(GameProfile profile) {
/* 20 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 21 */       return (NBTCompound)GameprofileUtil.writeGameProfile(NBT.createNBTObject(), profile);
/*    */     }
/* 23 */     return new NBTContainer(ReflectionMethod.GAMEPROFILE_SERIALIZE.run(null, new Object[] { ObjectCreator.NMS_NBTTAGCOMPOUND
/* 24 */             .getInstance(new Object[0]), profile }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   public static GameProfile fromNBT(NBTCompound compound) {
/* 35 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 36 */       return GameprofileUtil.readGameProfile((ReadableNBT)compound);
/*    */     }
/* 38 */     return (GameProfile)ReflectionMethod.GAMEPROFILE_DESERIALIZE.run(null, new Object[] {
/* 39 */           NBTReflectionUtil.getToCompount(compound.getCompound(), compound)
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtapi\NBTGameProfile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */