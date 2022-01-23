package com.naxanria.infinitybarrels.config;

import com.naxanria.infinitybarrels.InfinityBarrels;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

/*
  @author: Naxanria
*/
public class Config
{
  private static Config instance;
  public static Config getInstance()
  {
    return instance;
  }
  
  public static void init()
  {
    final Pair<Config, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Config::new);
    instance = specPair.getLeft();
  
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, specPair.getRight());
  }
  
  public final ForgeConfigSpec.IntValue stackSize;
  
  public Config(ForgeConfigSpec.Builder builder)
  {
    instance = this;
    
    stackSize = builder
      .comment("The amount of stacks that the barrels show it has inside", "default: 1024")
      .defineInRange("stackSize", 1024, 1, Integer.MAX_VALUE);
  }
}
