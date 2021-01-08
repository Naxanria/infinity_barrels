package com.naxanria.infinitybarrels.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.naxanria.infinitybarrels.InfinityBarrels;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.Map;

/*
  @author: Naxanria
*/
public class BarrelRenderOffsetLoader extends JsonReloadListener
{
  private static final Gson gson = new GsonBuilder().create();
  private static final String name = "barrel_offsets";
  
  public BarrelRenderOffsetLoader()
  {
    super(gson, name);
  }
  
  @Override
  protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn)
  {
    MutableInt mint = new MutableInt();
    objectIn.forEach((location, element) ->
    {
      if (element.isJsonObject())
      {
        JsonObject object = element.getAsJsonObject();
  
        BarrelRenderer.offsetData.clear();
        
        if (!object.has("id"))
        {
          InfinityBarrels.LOGGER.error("no 'id' specified for offset in {}", location);
        }
        else
        {
          String id = JSONUtils.getString(object, "id", "UNKNOWN");
          float x = JSONUtils.getFloat(object, "x", 0);
          float y = JSONUtils.getFloat(object, "y", 0);
          float z = JSONUtils.getFloat(object, "z", 0);
          float scale = JSONUtils.getFloat(object, "scale", 1);
          BarrelRenderer.addOffset(id, new BarrelRenderer.OffsetData(x, y, z, scale));
          mint.increment();
        }
      }
    });
    
    InfinityBarrels.LOGGER.info("Loaded {} offsets", mint.getValue());
  }
}
