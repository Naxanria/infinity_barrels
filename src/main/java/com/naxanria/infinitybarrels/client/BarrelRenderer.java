package com.naxanria.infinitybarrels.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.naxanria.infinitybarrels.InfinityBarrels;
import com.naxanria.infinitybarrels.block.BarrelBlock;
import com.naxanria.infinitybarrels.init.ModBlocks;
import com.naxanria.infinitybarrels.tile.BarrelTile;
import com.wtbw.mods.lib.util.Cache;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/*
  @author: Naxanria
*/
public class BarrelRenderer extends TileEntityRenderer<BarrelTile>
{
  public static final Material TEXTURE = new Material(Atlases.CHEST_ATLAS, new ResourceLocation(InfinityBarrels.MODID, "entity/barrel"));
  
  private static class BarrelModel extends Model
  {
    private final ModelRenderer model;
    
    public BarrelModel()
    {
      super(RenderType::getEntitySolid);
      model = createModel();
    }
    
    private ModelRenderer createModel()
    {
      ModelRenderer modelRenderer = new ModelRenderer(64, 64, 0, 0);
      modelRenderer.addBox(0, 0, 0, 16, 16, 16);
      return modelRenderer;
    }
  
    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
      model.render(matrixStack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
  }
  
  private final BarrelModel model;
  
  public BarrelRenderer(TileEntityRendererDispatcher rendererDispatcherIn)
  {
    super(rendererDispatcherIn);
    model = new BarrelModel();
  }

  @Override
  public void render(BarrelTile barrel, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay)
  {
    World world = barrel.getWorld();
    BlockState state = world != null ? barrel.getBlockState() : ModBlocks.INFINITY_BARREL.getDefaultState().with(BarrelBlock.FACING, Direction.SOUTH);
    
    matrixStack.push();
    
    // set up matrix
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
    
    // render model
    IVertexBuilder builder = TEXTURE.getBuffer(buffer, RenderType::getEntityCutout);
    
    model.render(matrixStack, builder, combinedLight, combinedOverlay, 1f, 1f, 1f, 1f);
    
    // render item in front
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
