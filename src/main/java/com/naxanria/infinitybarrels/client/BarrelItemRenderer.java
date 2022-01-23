package com.naxanria.infinitybarrels.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.naxanria.infinitybarrels.entity.BarrelBlockEntity;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Supplier;


/*
  @author: Naxanria
*/
public class BarrelItemRenderer extends BlockEntityWithoutLevelRenderer
{
  private final BarrelBlockEntity dummy = new BarrelBlockEntity(new BlockPos(0, 0, 0), null);
  private final Supplier<ItemStack> dummyItem = () -> new ItemStack(Blocks.STONE);
  
  public final BlockEntityRenderDispatcher dispatcher;
  
  public BarrelItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet entityModelSet)
  {
    super(dispatcher, entityModelSet);
    this.dispatcher = dispatcher;
  }
  
  @Override
  public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource bufferSource, int combinedLight, int combinedOverlay)
  {
    if (stack.hasTag())
    {
      CompoundTag stackTag = stack.getTag();
      CompoundTag itemTag = stackTag.contains("BlockEntityTag") ? stackTag.getCompound("BlockEntityTag").getCompound("item") : stackTag.getCompound("item");
      ItemStack item = ItemStack.of(itemTag);
      dummy.setItem(item);
    }
    else
    {
      dummy.setItem(dummyItem.get());
    }
    
    dispatcher.renderItem(dummy, poseStack, bufferSource, combinedLight, combinedOverlay);
  }
  
}
