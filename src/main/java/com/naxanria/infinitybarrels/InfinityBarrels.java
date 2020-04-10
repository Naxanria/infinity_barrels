package com.naxanria.infinitybarrels;

/*
  @author: Naxanria
*/

import com.naxanria.infinitybarrels.command.IBCommands;
import com.naxanria.infinitybarrels.config.Config;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(InfinityBarrels.MODID)
public class InfinityBarrels
{
  public static final String MODID = "infinity_barrels";
  public static final Logger LOGGER = LogManager.getLogger(MODID);
  
  public static final ItemGroup GROUP = new ItemGroup(MODID)
  {
    @Override
    public ItemStack createIcon()
    {
      return new ItemStack(Blocks.CHEST);
    }
  };
  
  public InfinityBarrels()
  {
    Registrator registrator = new Registrator(GROUP, MODID);
    FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, registrator::registerRecipes);
  
    DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientSetup.init());
    
    MinecraftForge.EVENT_BUS.addListener(this::onServerStarting);
    
    Config.init();
  }
  
  public void onServerStarting(final FMLServerStartingEvent event)
  {
    LOGGER.info("Server starting?");
    new IBCommands(event.getCommandDispatcher());
  }
}
