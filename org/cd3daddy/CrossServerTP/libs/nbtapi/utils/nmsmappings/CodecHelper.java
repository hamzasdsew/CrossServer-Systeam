/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NBTReflectionUtil;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*    */ 
/*    */ 
/*    */ public class CodecHelper
/*    */ {
/*    */   public static Object convertItemStackToNbt(Object itemStack) {
/* 12 */     Object result = null;
/*    */     
/*    */     try {
/* 15 */       result = NBTReflectionUtil.itemstack_codec.encodeStart(NBTReflectionUtil.nbtRegistryOps, itemStack);
/* 16 */       Objects.requireNonNull(result);
/* 17 */       return ((Optional)result.getClass().getMethod("result", new Class[0]).invoke(result, new Object[0])).get();
/* 18 */     } catch (Exception e) {
/* 19 */       throw new NbtApiException("Failed to convert ItemStack to NBT. " + result + " " + itemStack, e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static Object convertNbtToItemStack(Object nbt) {
/* 24 */     Object result = null;
/*    */     
/*    */     try {
/* 27 */       result = NBTReflectionUtil.itemstack_codec.parse(NBTReflectionUtil.nbtRegistryOps, nbt);
/* 28 */       Objects.requireNonNull(result);
/* 29 */       return ((Optional)result.getClass().getMethod("result", new Class[0]).invoke(result, new Object[0])).get();
/* 30 */     } catch (Exception e) {
/* 31 */       throw new NbtApiException("Failed to convert NBT to ItemStack. " + result + " " + nbt, e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\nmsmappings\CodecHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */