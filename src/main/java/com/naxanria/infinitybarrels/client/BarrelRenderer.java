package com.naxanria.infinitybarrels.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.naxanria.infinitybarrels.block.BarrelBlock;
import com.naxanria.infinitybarrels.init.ModBlocks;
import com.naxanria.infinitybarrels.tile.BarrelTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class BarrelRenderer extends TileEntityRenderer<BarrelTile>
{
  public BarrelRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
  {
    super(rendererDispatcherIn);
  }
  
  @Override
  public void render(BarrelTile barrel, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
  {
    World world = barrel.getWorld();
    BlockState state = world != null ? barrel.getBlockState() : ModBlocks.INFINITY_BARREL.getDefaultState().with(BarrelBlock.FACING, Direction.SOUTH);
    
    matrixStack.push();
    
    matrixStack.translate(.5, .5, .5);
    float angle = 0;
    Direction facing = state.get(BarrelBlock.FACING);
    if (facing.getDirectionVec().getY() == 0)
    {
      angle = facing.getHorizontalAngle();
      matrixStack.rotate(Vector3f.YP.rotationDegrees(-angle));
    }
    else
    {
      angle = 90 * facing.getDirectionVec().getY();
      matrixStack.rotate(Vector3f.XP.rotationDegrees(-angle));
    }
    matrixStack.translate(-.5, -.5, -.5);
    
    if (!barrel.getItem().isEmpty())
    {
      matrixStack.push();
  
      Block block = Block.getBlockFromItem(barrel.getItem().getItem());
      if (block == Blocks.AIR)
      {
        matrixStack.translate(.5, .5, 1.025);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
        float scale = .7f;
        matrixStack.scale(scale, scale, scale);
      }
      else
      {
        matrixStack.translate(.5, .5, .9);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
        float scale = .8f;
        matrixStack.scale(scale, scale, scale);
      }
  
      Minecraft.getInstance().getItemRenderer().renderItem(barrel.getItem(), ItemCameraTransforms.TransformType.FIXED, combinedLight,
        OverlayTexture.NO_OVERLAY, matrixStack, buffer);
      
      matrixStack.pop();
    }
    
    matrixStack.pop();
  }
}
