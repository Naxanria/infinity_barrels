package com.naxanria.infinitybarrels.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.naxanria.infinitybarrels.tile.BarrelTile;
import com.wtbw.mods.lib.util.Cache;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Supplier;

/*
  @author: Naxanria
*/
public class BarrelItemRenderer extends ItemStackTileEntityRenderer
{
  private final Cache<BarrelTile> dummy = Cache.create(BarrelTile::new);
  private final Supplier<ItemStack> dummyItem = () -> new ItemStack(Blocks.STONE);
  
  @Override
  public void func_239207_a_(ItemStack itemStackIn, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
  {
    BarrelTile barrel = dummy.get();
    if (itemStackIn.hasTag())
    {
      CompoundNBT tag = itemStackIn.getChildTag("BlockEntityTag");
      ItemStack stack = ItemStack.read(tag.getCompound("item"));
      barrel.setItem(stack);
    }
    else
    {
      barrel.setItem(dummyItem.get());
    }
  
    TileEntityRendererDispatcher.instance.renderItem(barrel, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
  }
}
