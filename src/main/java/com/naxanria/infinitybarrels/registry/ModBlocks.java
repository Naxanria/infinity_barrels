package com.naxanria.infinitybarrels.registry;

import com.naxanria.infinitybarrels.InfinityBarrels;

import com.naxanria.infinitybarrels.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/*
  @author: Naxanria
*/
public class ModBlocks
{
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, InfinityBarrels.MODID);
  
  public static final RegistryObject<Block> BARREL = BLOCKS.register("infinity_barrel",
    () -> new BarrelBlock(BlockBehaviour.Properties.of(Material.WOOD).strength(-1, 36000000).noOcclusion()));
}
