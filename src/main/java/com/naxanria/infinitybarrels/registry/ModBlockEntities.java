package com.naxanria.infinitybarrels.registry;

import com.naxanria.infinitybarrels.InfinityBarrels;
import com.naxanria.infinitybarrels.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/*
  @author: Naxanria
*/
public class ModBlockEntities
{
  public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, InfinityBarrels.MODID);
  
  public static final RegistryObject<BlockEntityType<BarrelBlockEntity>> BARREL = ENTITIES.register("infinity_barrel",
    () -> BlockEntityType.Builder.of(BarrelBlockEntity::new, ModBlocks.BARREL.get()).build(null));
}
