package com.naxanria.infinitybarrels.registry;

import com.naxanria.infinitybarrels.InfinityBarrels;
import com.naxanria.infinitybarrels.client.BarrelItemRenderer;
import com.naxanria.infinitybarrels.item.BarrelWrenchItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

/*
  @author: Naxanria
*/
public class ModItems
{
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, InfinityBarrels.MODID);
  
  public static final RegistryObject<Item> BARREL = ITEMS.register("infinity_barrel",
    () -> new BlockItem(ModBlocks.BARREL.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC).fireResistant())
    {
      @Override
      public void initializeClient(Consumer<IItemRenderProperties> consumer)
      {
        consumer.accept(new IItemRenderProperties()
        {
          @Override
          public BlockEntityWithoutLevelRenderer getItemStackRenderer()
          {
            return new BarrelItemRenderer(Minecraft.getInstance().getBlockEntityRenderDispatcher(), new EntityModelSet());
          }
        });
      }
    });
  
  public static final RegistryObject<Item> BARREL_WRENCH = ITEMS.register("barrel_wrench", () -> new BarrelWrenchItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
  
}
