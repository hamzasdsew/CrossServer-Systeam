/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTType;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadWriteNBTCompoundList;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBT;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.iface.ReadableNBTList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GameprofileUtil
/*     */ {
/*  21 */   private static Method GET_NAME = null;
/*  22 */   private static Method GET_ID = null;
/*  23 */   private static Method GET_VALUE = null;
/*  24 */   private static Method GET_SIGNATURE = null;
/*  25 */   private static Method GET_PROPERTY_NAME = null;
/*  26 */   private static Method GET_PROPERTIES = null;
/*     */   
/*     */   static {
/*  29 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
/*     */       try {
/*  31 */         GET_NAME = GameProfile.class.getDeclaredMethod("name", new Class[0]);
/*  32 */         GET_ID = GameProfile.class.getDeclaredMethod("id", new Class[0]);
/*  33 */         GET_VALUE = Property.class.getDeclaredMethod("value", new Class[0]);
/*  34 */         GET_SIGNATURE = Property.class.getDeclaredMethod("signature", new Class[0]);
/*  35 */         GET_PROPERTY_NAME = Property.class.getDeclaredMethod("name", new Class[0]);
/*  36 */         GET_PROPERTIES = GameProfile.class.getDeclaredMethod("properties", new Class[0]);
/*  37 */       } catch (NoSuchMethodException e) {
/*  38 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static GameProfile readGameProfile(ReadableNBT arg) {
/*  45 */     String string = null;
/*  46 */     UUID uUID = null;
/*  47 */     if (arg.hasTag("Name") && arg.getType("Name") == NBTType.NBTTagString) {
/*  48 */       string = arg.getString("Name");
/*  49 */     } else if (arg.hasTag("name") && arg.getType("name") == NBTType.NBTTagString) {
/*  50 */       string = arg.getString("name");
/*     */     } 
/*  52 */     if (arg.hasTag("Id") && arg.getType("Id") == NBTType.NBTTagIntArray && (arg.getIntArray("Id")).length == 4) {
/*  53 */       uUID = arg.getUUID("Id");
/*  54 */     } else if (arg.hasTag("id") && arg.getType("id") == NBTType.NBTTagIntArray && (arg
/*  55 */       .getIntArray("id")).length == 4) {
/*  56 */       uUID = arg.getUUID("id");
/*     */     } 
/*     */     try {
/*  59 */       GameProfile gameProfile = new GameProfile(uUID, string);
/*  60 */       if (arg.hasTag("Properties") && arg.getType("Properties") == NBTType.NBTTagCompound) {
/*  61 */         ReadableNBT compoundTag = arg.getCompound("Properties");
/*  62 */         for (String string2 : compoundTag.getKeys()) {
/*  63 */           ReadableNBTList<ReadWriteNBT> listTag = compoundTag.getCompoundList(string);
/*  64 */           for (int i = 0; i < listTag.size(); i++) {
/*  65 */             ReadableNBT compoundTag2 = (ReadableNBT)listTag.get(i);
/*  66 */             String string3 = compoundTag2.getString("Value");
/*  67 */             if (compoundTag2.hasTag("Signature") && compoundTag2
/*  68 */               .getType("Signature") == NBTType.NBTTagString) {
/*  69 */               gameProfile.getProperties().put(string2, new Property(string2, string3, compoundTag2
/*  70 */                     .getString("Signature")));
/*     */             } else {
/*  72 */               gameProfile.getProperties().put(string2, new Property(string2, string3));
/*     */             }
/*     */           
/*     */           } 
/*     */         } 
/*  77 */       } else if (arg.getType("properties") == NBTType.NBTTagList) {
/*  78 */         ReadableNBTList<ReadWriteNBT> listTag = arg.getCompoundList("properties");
/*  79 */         for (int i = 0; i < listTag.size(); i++) {
/*  80 */           ReadableNBT compoundTag2 = (ReadableNBT)listTag.get(i);
/*  81 */           String string2 = compoundTag2.getString("name");
/*  82 */           String string3 = compoundTag2.getString("value");
/*  83 */           if (compoundTag2.hasTag("signature") && compoundTag2
/*  84 */             .getType("signature") == NBTType.NBTTagString) {
/*  85 */             gameProfile.getProperties().put(string2, new Property(string2, string3, compoundTag2
/*  86 */                   .getString("signature")));
/*     */           } else {
/*  88 */             gameProfile.getProperties().put(string2, new Property(string2, string3));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  93 */       return gameProfile;
/*  94 */     } catch (Throwable var11) {
/*  95 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static ReadWriteNBT writeGameProfile(ReadWriteNBT arg, GameProfile gameProfile) {
/* 100 */     String name = getName(gameProfile);
/* 101 */     if (name != null && !name.isEmpty()) {
/* 102 */       String nameKey = MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? "name" : "Name";
/* 103 */       arg.setString(nameKey, getName(gameProfile));
/*     */     } 
/* 105 */     UUID id = getId(gameProfile);
/* 106 */     if (id != null) {
/* 107 */       String idKey = MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4) ? "id" : "Id";
/* 108 */       arg.setUUID(idKey, id);
/*     */     } 
/* 110 */     PropertyMap properties = getProperties(gameProfile);
/* 111 */     if (!properties.isEmpty()) {
/* 112 */       if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_20_R4)) {
/* 113 */         ReadWriteNBTCompoundList list = arg.getCompoundList("properties");
/* 114 */         for (Property property : properties.values()) {
/* 115 */           ReadWriteNBT tag = list.addCompound();
/* 116 */           tag.setString("name", getPropertyName(property));
/* 117 */           tag.setString("value", getValue(property));
/* 118 */           if (property.hasSignature()) {
/* 119 */             tag.setString("signature", getSignature(property));
/*     */           }
/*     */         } 
/*     */       } else {
/* 123 */         ReadWriteNBT compoundTag = arg.getOrCreateCompound("Properties");
/* 124 */         for (String string : gameProfile.getProperties().keySet()) {
/* 125 */           ReadWriteNBTCompoundList list = compoundTag.getCompoundList(string);
/* 126 */           for (Property property : properties.get(string)) {
/* 127 */             ReadWriteNBT tag = list.addCompound();
/* 128 */             tag.setString("Value", getValue(property));
/* 129 */             if (property.hasSignature()) {
/* 130 */               tag.setString("Signature", getSignature(property));
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 136 */     return arg;
/*     */   }
/*     */   
/*     */   private static String getName(GameProfile profile) {
/* 140 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
/*     */       try {
/* 142 */         return GET_NAME.invoke(profile, new Object[0]).toString();
/* 143 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 144 */         throw new NbtApiException("Failed to get GameProfile name via reflection", e);
/*     */       } 
/*     */     }
/* 147 */     return profile.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   private static UUID getId(GameProfile profile) {
/* 152 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
/*     */       try {
/* 154 */         return (UUID)GET_ID.invoke(profile, new Object[0]);
/* 155 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 156 */         throw new NbtApiException("Failed to get GameProfile id via reflection", e);
/*     */       } 
/*     */     }
/* 159 */     return profile.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getValue(Property property) {
/* 164 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
/*     */       try {
/* 166 */         return GET_VALUE.invoke(property, new Object[0]).toString();
/* 167 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 168 */         throw new NbtApiException("Failed to get Property value via reflection", e);
/*     */       } 
/*     */     }
/* 171 */     return property.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getSignature(Property property) {
/* 176 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
/*     */       try {
/* 178 */         return GET_SIGNATURE.invoke(property, new Object[0]).toString();
/* 179 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 180 */         throw new NbtApiException("Failed to get Property signature via reflection", e);
/*     */       } 
/*     */     }
/* 183 */     return property.getSignature();
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getPropertyName(Property property) {
/* 188 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
/*     */       try {
/* 190 */         return GET_PROPERTY_NAME.invoke(property, new Object[0]).toString();
/* 191 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 192 */         throw new NbtApiException("Failed to get Property name via reflection", e);
/*     */       } 
/*     */     }
/* 195 */     return property.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   private static PropertyMap getProperties(GameProfile profile) {
/* 200 */     if (MinecraftVersion.isAtLeastVersion(MinecraftVersion.MC1_21_R6)) {
/*     */       try {
/* 202 */         return (PropertyMap)GET_PROPERTIES.invoke(profile, new Object[0]);
/* 203 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 204 */         throw new NbtApiException("Failed to get GameProfile properties via reflection", e);
/*     */       } 
/*     */     }
/* 207 */     return profile.getProperties();
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\GameprofileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */