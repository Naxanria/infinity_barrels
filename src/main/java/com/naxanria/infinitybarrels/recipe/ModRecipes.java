package com.naxanria.infinitybarrels.recipe;

import com.naxanria.infinitybarrels.InfinityBarrels;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/*
  @author: Naxanria
*/
public class ModRecipes
{
  public static final IRecipeType<BarrelRecipe> BARREL = register("barrel");
  
  private static <T extends IRecipe<?>> IRecipeType<T> register(final String key)
  {
    return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(InfinityBarrels.MODID, key), new IRecipeType<T>()
    {
      @Override
      public String toString()
      {
        return key;
      }
    });
  }
  
  public static void init()
  { }
}
