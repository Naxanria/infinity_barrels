package com.naxanria.infinitybarrels;

/*
  @author: Naxanria
*/


import com.naxanria.infinitybarrels.command.IBCommands;
import com.naxanria.infinitybarrels.config.Config;
import com.naxanria.infinitybarrels.registry.ModBlockEntities;
import com.naxanria.infinitybarrels.registry.ModBlocks;
import com.naxanria.infinitybarrels.registry.ModItems;
import com.naxanria.infinitybarrels.registry.ModRecipeSerializers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** @noinspection Convert2MethodRef*/
@Mod(InfinityBarrels.MODID)
public class InfinityBarrels
{
  public static final String MODID = "infinity_barrels";
  public static final Logger LOGGER = LogManager.getLogger(MODID);
  
  public InfinityBarrels()
  {
  
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientSetup.init());
    
    MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    ModBlocks.BLOCKS.register(modEventBus);
    ModItems.ITEMS.register(modEventBus);
    ModBlockEntities.ENTITIES.register(modEventBus);
    ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
    
    Config.init();
  }
  
  public void onServerStarting(final RegisterCommandsEvent event)
  {
    new IBCommands(event.getDispatcher());
  }
}
