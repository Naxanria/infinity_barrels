package com.naxanria.infinitybarrels;

import com.naxanria.infinitybarrels.block.BarrelBlock;
import com.naxanria.infinitybarrels.client.BarrelItemRenderer;
import com.naxanria.infinitybarrels.init.ModBlocks;
import com.naxanria.infinitybarrels.recipe.BarrelRecipe;
import com.naxanria.infinitybarrels.recipe.ModRecipes;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.event.RegistryEvent;

/*
  @author: Naxanria
*/
public class Registrator extends com.wtbw.mods.lib.Registrator
{
  public Registrator(ItemGroup group, String modid)
  {
    super(group, modid);
  }
  
  @Override
  protected void registerAllBlocks()
  {
    register(new BarrelBlock(getBlockProperties(Material.WOOD, 2, 2).notSolid()), "infinity_barrel", false);
  }
  
  @Override
  protected void registerAllItems()
  {
    register(new BlockItem(ModBlocks.INFINITY_BARREL, getItemProperties().setISTER(() -> () -> new BarrelItemRenderer())), "infinity_barrel");
  }
  
  @Override
  protected void registerAllContainers()
  { }
  
  public void registerRecipes(final RegistryEvent.Register<IRecipeSerializer<?>> event)
  {
    ModRecipes.init();
    
    event.getRegistry().register(BarrelRecipe.SERIALIZER);
  }
}
