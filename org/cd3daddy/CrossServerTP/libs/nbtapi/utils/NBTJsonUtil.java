/*    */ package org.cd3daddy.CrossServerTP.libs.nbtapi.utils;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import com.mojang.serialization.JsonOps;
/*    */ import java.util.Optional;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.cd3daddy.CrossServerTP.libs.gson.JsonElement;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.NbtApiException;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ClassWrapper;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.MojangToMapping;
/*    */ import org.cd3daddy.CrossServerTP.libs.nbtapi.utils.nmsmappings.ReflectionMethod;
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
/*    */ public class NBTJsonUtil
/*    */ {
/*    */   public static JsonElement itemStackToJson(ItemStack itemStack) {
/*    */     try {
/* 30 */       Codec<Object> itemStackCodec = (Codec<Object>)ClassWrapper.NMS_ITEMSTACK.getClazz().getField((String)MojangToMapping.getMapping().get("net.minecraft.world.item.ItemStack#CODEC")).get(null);
/* 31 */       Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { itemStack });
/* 32 */       DataResult<JsonElement> result = itemStackCodec.encode(stack, (DynamicOps)JsonOps.INSTANCE, JsonOps.INSTANCE
/* 33 */           .emptyMap());
/* 34 */       Optional<JsonElement> opt = (Optional<JsonElement>)result.getClass().getMethod("result", new Class[0]).invoke(result, new Object[0]);
/* 35 */       return opt.orElse(null);
/* 36 */     } catch (Exception ex) {
/* 37 */       throw new NbtApiException("Error trying to get Json of an ItemStack.", ex);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Vortex\Downloads\Temp\CrossServerTP_V2.0.jar!\org\cd3daddy\CrossServerTP\libs\nbtap\\utils\NBTJsonUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */