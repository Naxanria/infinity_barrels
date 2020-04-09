package com.naxanria.infinitybarrels;

import com.naxanria.infinitybarrels.client.BarrelRenderer;
import com.naxanria.infinitybarrels.init.ModTiles;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/*
  @author: Naxanria
*/
public class ClientSetup
{
  public static void setup(final FMLClientSetupEvent event)
  {
    ClientRegistry.bindTileEntityRenderer(ModTiles.INFINITY_BARREL, BarrelRenderer::new);
  }
}
