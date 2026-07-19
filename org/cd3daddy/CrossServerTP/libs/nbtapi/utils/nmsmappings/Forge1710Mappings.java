/*     */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Forge1710Mappings
/*     */ {
/*  17 */   private static Map<String, String> classMap = new HashMap<>();
/*  18 */   private static Map<String, String> methodMap = new HashMap<>();
/*     */   
/*     */   private static Method crucible_toString;
/*     */   
/*     */   static {
/*  23 */     classMap.put("NMS_NBTBASE", "net.minecraft.nbt.NBTBase");
/*  24 */     classMap.put("NMS_NBTTAGSTRING", "net.minecraft.nbt.NBTTagString");
/*  25 */     classMap.put("NMS_NBTTAGINT", "net.minecraft.nbt.NBTTagInt");
/*  26 */     classMap.put("NMS_NBTTAGINTARRAY", "net.minecraft.nbt.NBTTagIntArray");
/*  27 */     classMap.put("NMS_NBTTAGFLOAT", "net.minecraft.nbt.NBTTagFloat");
/*  28 */     classMap.put("NMS_NBTTAGDOUBLE", "net.minecraft.nbt.NBTTagDouble");
/*  29 */     classMap.put("NMS_NBTTAGLONG", "net.minecraft.nbt.NBTTagLong");
/*  30 */     classMap.put("NMS_ITEMSTACK", "net.minecraft.item.ItemStack");
/*  31 */     classMap.put("NMS_NBTTAGCOMPOUND", "net.minecraft.nbt.NBTTagCompound");
/*  32 */     classMap.put("NMS_NBTTAGLIST", "net.minecraft.nbt.NBTTagList");
/*  33 */     classMap.put("NMS_NBTCOMPRESSEDSTREAMTOOLS", "net.minecraft.nbt.CompressedStreamTools");
/*  34 */     classMap.put("NMS_MOJANGSONPARSER", "io.github.crucible.nbt.Crucible_JsonToNBT");
/*  35 */     classMap.put("NMS_TILEENTITY", "net.minecraft.tileentity.TileEntity");
/*  36 */     classMap.put("NMS_WORLDSERVER", "net.minecraft.world.WorldServer");
/*  37 */     classMap.put("NMS_MINECRAFTSERVER", "net.minecraft.server.MinecraftServer");
/*  38 */     classMap.put("NMS_WORLD", "net.minecraft.world.World");
/*  39 */     classMap.put("NMS_ENTITY", "net.minecraft.entity.Entity");
/*  40 */     classMap.put("NMS_ENTITYTYPES", "net.minecraft.entity.EntityList");
/*  41 */     classMap.put("NMS_REGISTRYMATERIALS", "net.minecraft.util.RegistryNamespaced");
/*  42 */     classMap.put("NMS_GAMEPROFILESERIALIZER", "net.minecraft.nbt.NBTUtil");
/*  43 */     classMap.put("NMS_IREGISTRY", "net.minecraft.util.IRegistry");
/*     */ 
/*     */     
/*  46 */     methodMap.put("COMPOUND_SET_FLOAT", "func_74776_a");
/*  47 */     methodMap.put("COMPOUND_SET_STRING", "func_74778_a");
/*  48 */     methodMap.put("COMPOUND_SET_INT", "func_74768_a");
/*  49 */     methodMap.put("COMPOUND_SET_BYTEARRAY", "func_74773_a");
/*  50 */     methodMap.put("COMPOUND_SET_INTARRAY", "func_74783_a");
/*  51 */     methodMap.put("COMPOUND_SET_LONG", "func_74772_a");
/*  52 */     methodMap.put("COMPOUND_SET_SHORT", "func_74777_a");
/*  53 */     methodMap.put("COMPOUND_SET_BYTE", "func_74774_a");
/*  54 */     methodMap.put("COMPOUND_SET_DOUBLE", "func_74780_a");
/*  55 */     methodMap.put("COMPOUND_SET_BOOLEAN", "func_74757_a");
/*  56 */     methodMap.put("COMPOUND_MERGE", "merge");
/*  57 */     methodMap.put("COMPOUND_SET", "func_74782_a");
/*  58 */     methodMap.put("COMPOUND_GET", "func_74781_a");
/*  59 */     methodMap.put("COMPOUND_GET_LIST", "func_150295_c");
/*  60 */     methodMap.put("COMPOUND_OWN_TYPE", "func_74732_a");
/*  61 */     methodMap.put("COMPOUND_GET_FLOAT", "func_74760_g");
/*  62 */     methodMap.put("COMPOUND_GET_STRING", "func_74779_i");
/*  63 */     methodMap.put("COMPOUND_GET_INT", "func_74762_e");
/*  64 */     methodMap.put("COMPOUND_GET_BYTEARRAY", "func_74770_j");
/*  65 */     methodMap.put("COMPOUND_GET_INTARRAY", "func_74759_k");
/*  66 */     methodMap.put("COMPOUND_GET_LONG", "func_74763_f");
/*  67 */     methodMap.put("COMPOUND_GET_SHORT", "func_74765_d");
/*  68 */     methodMap.put("COMPOUND_GET_BYTE", "func_74771_c");
/*  69 */     methodMap.put("COMPOUND_GET_DOUBLE", "func_74769_h");
/*  70 */     methodMap.put("COMPOUND_GET_BOOLEAN", "func_74767_n");
/*  71 */     methodMap.put("COMPOUND_GET_COMPOUND", "func_74775_l");
/*  72 */     methodMap.put("COMPOUND_REMOVE_KEY", "func_82580_o");
/*  73 */     methodMap.put("COMPOUND_HAS_KEY", "func_74764_b");
/*  74 */     methodMap.put("COMPOUND_GET_KEYS", "func_150296_c");
/*  75 */     methodMap.put("LISTCOMPOUND_GET_KEYS", "func_150296_c");
/*  76 */     methodMap.put("NMSITEM_GETTAG", "func_77978_p");
/*  77 */     methodMap.put("NMSITEM_SAVE", "func_77955_b");
/*  78 */     methodMap.put("NMSITEM_CREATESTACK", "func_77949_a");
/*  79 */     methodMap.put("ITEMSTACK_SET_TAG", "func_77982_d");
/*  80 */     methodMap.put("LIST_SIZE", "func_74745_c");
/*  81 */     methodMap.put("LEGACY_LIST_ADD", "func_74742_a");
/*  82 */     methodMap.put("LIST_GET_STRING", "func_150307_f");
/*  83 */     methodMap.put("LIST_GET_COMPOUND", "func_150305_b");
/*  84 */     methodMap.put("LIST_GET", "func_150305_b");
/*  85 */     methodMap.put("NMS_WORLD_GET_TILEENTITY_1_7_10", "func_147438_o");
/*  86 */     methodMap.put("TILEENTITY_GET_NBT", "func_145841_b");
/*  87 */     methodMap.put("TILEENTITY_SET_NBT", "func_145839_a");
/*  88 */     methodMap.put("TILEENTITY_SET_NBT_LEGACY1151", "func_145839_a");
/*  89 */     methodMap.put("NMS_ENTITY_GET_NBT", "func_70109_d");
/*  90 */     methodMap.put("NMS_ENTITY_SET_NBT", "func_70020_e");
/*  91 */     methodMap.put("NBTFILE_READ", "func_74796_a");
/*  92 */     methodMap.put("NBTFILE_WRITE", "func_74799_a");
/*  93 */     methodMap.put("PARSE_NBT", "getTagFromJson");
/*  94 */     methodMap.put("GAMEPROFILE_DESERIALIZE", "func_152459_a");
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  99 */       crucible_toString = Class.forName("net.minecraft.nbt.NBTTagCompound").getDeclaredMethod("crucible_toString", new Class[0]);
/* 100 */     } catch (Exception e) {
/* 101 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Map<String, String> getClassMappings() {
/* 106 */     return classMap;
/*     */   }
/*     */   
/*     */   public static Map<String, String> getMethodMapping() {
/* 110 */     return methodMap;
/*     */   }
/*     */   
/*     */   public static String toString(Object nbtTagCompound) {
/* 114 */     if (crucible_toString == null)
/* 115 */       throw new NbtApiException("Method not loaded! 'Forge1710Mappings.crucible_toString' "); 
/*     */     try {
/* 117 */       return (String)crucible_toString.invoke(nbtTagCompound, new Object[0]);
/* 118 */     } catch (Exception ex) {
/* 119 */       throw new NbtApiException("Error while calling the method 'crucible_toString', from Forge1710Mappings. Passed Class: " + Forge1710Mappings.class, ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\nmsmappings\Forge1710Mappings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */