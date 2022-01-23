package com.naxanria.infinitybarrels.registry;

import com.naxanria.infinitybarrels.InfinityBarrels;
import com.naxanria.infinitybarrels.recipe.BarrelRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/*
  @author: Naxanria
*/
public class ModRecipeSerializers
{
  public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, InfinityBarrels.MODID);
  
  public static final RegistryObject<RecipeSerializer<?>> BARREL = RECIPE_SERIALIZERS.register(BarrelRecipe.NAME, () -> BarrelRecipe.SERIALIZER);
}
