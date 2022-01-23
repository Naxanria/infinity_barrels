package com.naxanria.infinitybarrels;

import com.naxanria.infinitybarrels.client.BarrelRenderOffsetLoader;
import com.naxanria.infinitybarrels.client.BarrelRenderer;
import com.naxanria.infinitybarrels.registry.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/*
  @author: Naxanria
*/
public class ClientSetup
{
  public static void init()
  {
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//    modEventBus.addListener(ClientSetup::setup);
    modEventBus.addListener(ClientSetup::textureStitch);
    modEventBus.addListener(ClientSetup::onBlockEntityRenderRegistration);
    modEventBus.addListener(ClientSetup::onBlockEntityLayerRegistration);
    ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(new BarrelRenderOffsetLoader());
  }
  
  public static void setup(final FMLClientSetupEvent event)
  {

  }
  
  public static void onBlockEntityRenderRegistration(final EntityRenderersEvent.RegisterRenderers event)
  {
    event.registerBlockEntityRenderer(ModBlockEntities.BARREL.get(), BarrelRenderer::new);
  }
  
  public static void onBlockEntityLayerRegistration(final EntityRenderersEvent.RegisterLayerDefinitions event)
  {
    event.registerLayerDefinition(BarrelRenderer.MODEL_RESOURCE_LOCATION, BarrelRenderer.BarrelModel::getLayerDefinition);
  }
  
  public static void textureStitch(final TextureStitchEvent.Pre event)
  {
    if (event.getAtlas().location() == Sheets.CHEST_SHEET)
    {
      event.addSprite(new ResourceLocation(InfinityBarrels.MODID, "entity/barrel"));
    }
  }
}
