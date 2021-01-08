package com.naxanria.infinitybarrels;

import com.naxanria.infinitybarrels.client.BarrelRenderOffsetLoader;
import com.naxanria.infinitybarrels.client.BarrelRenderer;
import com.naxanria.infinitybarrels.init.ModTiles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/*
  @author: Naxanria
*/
public class ClientSetup
{
  public static void init()
  {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::setup);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(ClientSetup::textureStitch);
    ((IReloadableResourceManager) Minecraft.getInstance().getResourceManager()).addReloadListener(new BarrelRenderOffsetLoader());
  }
  
  public static void setup(final FMLClientSetupEvent event)
  {
    ClientRegistry.bindTileEntityRenderer(ModTiles.INFINITY_BARREL, BarrelRenderer::new);
  }
  
  private static void textureStitch(final TextureStitchEvent.Pre event)
  {
    
    if (event.getMap().getTextureLocation().equals(Atlases.CHEST_ATLAS))
    {
      event.addSprite(new ResourceLocation(InfinityBarrels.MODID, "entity/barrel"));
    }
  }
}
