package com.alphastudio.CrossBorderCore.storage;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Map;
import org.bukkit.inventory.ItemStack;

public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
  @Override
  public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
    Map<String, Object> data = src.serialize();
    return context.serialize(data);
  }

  @Override
  public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject obj = json.getAsJsonObject();
    Map<String, Object> data = context.deserialize(obj, Map.class);
    return ItemStack.deserialize(data);
  }
}
