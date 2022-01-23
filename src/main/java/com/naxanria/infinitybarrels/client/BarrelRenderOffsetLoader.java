package com.naxanria.infinitybarrels.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.naxanria.infinitybarrels.InfinityBarrels;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.event.AddReloadListenerEvent;

import java.util.Map;

/*
  @author: Naxanria
*/
public class BarrelRenderOffsetLoader extends SimpleJsonResourceReloadListener
{
  public static void onAddReloadListener(final AddReloadListenerEvent event)
  {
    event.addListener(new BarrelRenderOffsetLoader());
  }
  
  private static final Gson GSON = new GsonBuilder().create();
  private static final String NAME = "barrel_offsets";
  
  public BarrelRenderOffsetLoader()
  {
    super(GSON, NAME);
  }
  
  @Override
  protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler)
  {
    map.forEach((location, element) ->
    {
      if (element.isJsonObject())
      {
        JsonObject json = element.getAsJsonObject();
  
        BarrelRenderer.offsetData.clear();
  
        if (!json.has("id"))
        {
          InfinityBarrels.LOGGER.error("no 'id' specified for offset in {}", location);
        }
        else
        {
          String id = GsonHelper.getAsString(json, "id", "UNKNOWN");
          float x = GsonHelper.getAsFloat(json, "x", 0);
          float y = GsonHelper.getAsFloat(json, "y", 0);
          float z = GsonHelper.getAsFloat(json, "z", 0);
          float scale = GsonHelper.getAsFloat(json, "scale", 1);
          BarrelRenderer.addOffset(id, new BarrelRenderer.OffsetData(x, y, z, scale));
  
        }
      }
    });
  }
}
