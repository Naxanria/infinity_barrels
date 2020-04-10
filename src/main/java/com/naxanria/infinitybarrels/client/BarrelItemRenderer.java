package com.naxanria.infinitybarrels.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.naxanria.infinitybarrels.tile.BarrelTile;
import com.wtbw.mods.lib.util.Cache;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

/*
  @author: Naxanria
*/
public class BarrelItemRenderer extends ItemStackTileEntityRenderer
{
  private Cache<BarrelTile> dummy = Cache.create(BarrelTile::new);
  
  @Override
  public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn)
  {
    BarrelTile barrel = dummy.get();
    if (itemStackIn.hasTag())
    {
      CompoundNBT tag = itemStackIn.getChildTag("BlockEntityTag");
      ItemStack stack = ItemStack.read(tag.getCompound("item"));
      barrel.setItem(stack);
  
      TileEntityRendererDispatcher.instance.renderItem(barrel, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
  }
}
