package com.naxanria.infinitybarrels.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.naxanria.infinitybarrels.InfinityBarrels;
import com.naxanria.infinitybarrels.block.BarrelBlock;
import com.naxanria.infinitybarrels.entity.BarrelBlockEntity;
import com.naxanria.infinitybarrels.registry.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

/*
  @author: Naxanria
*/
public class BarrelRenderer implements BlockEntityRenderer<BarrelBlockEntity>
{
  public static final ModelLayerLocation MODEL_RESOURCE_LOCATION = new ModelLayerLocation(new ResourceLocation(InfinityBarrels.MODID, "barrel"), "main");
  public static final Material TEXTURE = new Material(Sheets.CHEST_SHEET, new ResourceLocation(InfinityBarrels.MODID, "entity/barrel"));
  public static final Map<Integer, OffsetData> offsetData = new HashMap<>();
  
  public static final OffsetData IDENTITY = new OffsetData(1);
  
  public static class OffsetData
  {
    public final float x;
    public final float y;
    public final float z;
    public final float scale;
  
    public OffsetData(float scale)
    {
      x = 0;
      y = 0;
      z = 0;
      this.scale = scale;
    }
  
    public OffsetData(float x, float y, float z)
    {
      this.x = x;
      this.y = y;
      this.z = z;
      scale = 1;
    }
  
    public OffsetData(float x, float y, float z, float scale)
    {
      this.x = x;
      this.y = y;
      this.z = z;
      this.scale = scale;
    }
  }
  
  public static void addOffset(String location, OffsetData data)
  {
    int hash = location.hashCode();
    offsetData.put(hash, data);
  }
  
  public static OffsetData getOffset(ResourceLocation location)
  {
    int hash = location.toString().hashCode();
    return offsetData.getOrDefault(hash, IDENTITY);
  }
  
  public static class BarrelModel extends Model
  {
    private final ModelPart model;
    
    public BarrelModel(ModelPart model)
    {
      super(RenderType::entitySolid);
      this.model = model;
    }
    
    public static LayerDefinition getLayerDefinition()
    {
      MeshDefinition meshDefinition = new MeshDefinition();
      PartDefinition root = meshDefinition.getRoot();
      root.addOrReplaceChild("main", CubeListBuilder.create().addBox(0, 0, 0, 16, 16, 16), PartPose.ZERO);
      return LayerDefinition.create(meshDefinition, 64, 64);
    }
  
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha)
    {
      model.render(poseStack, buffer, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
  }
  
  private final BarrelModel model;
  
  public BarrelRenderer(BlockEntityRendererProvider.Context context)
  {
    model = new BarrelModel(context.bakeLayer(MODEL_RESOURCE_LOCATION));
  }
  
  @Override
  public void render(BarrelBlockEntity barrel, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay)
  {
    Level level = barrel.getLevel();
    BlockState state = level != null ? barrel.getBlockState() : ModBlocks.BARREL.get().defaultBlockState().setValue(BarrelBlock.FACING, Direction.SOUTH);
    
    poseStack.pushPose();
    
    // set up matrix
    poseStack.translate(.5, .5, .5);
    float angle = 0;
    Direction facing = state.getValue(BarrelBlock.FACING);
    if (facing.getNormal().getY() == 0)
    {
      angle = facing.toYRot();
      poseStack.mulPose(Vector3f.YP.rotationDegrees(-angle));
    }
    else
    {
      angle = 90 * facing.getNormal().getY();
      poseStack.mulPose(Vector3f.XP.rotationDegrees(-angle));
    }
    poseStack.translate(-.5, -.5, -.5);
    
    // render model
  
    VertexConsumer builder = TEXTURE.buffer(buffer, RenderType::entityCutout);
//    IVertexBuilder builder = TEXTURE.getBuffer(buffer, RenderType::getEntityCutout);
    
    model.renderToBuffer(poseStack, builder, combinedLight, combinedOverlay, 1f, 1f, 1f, 1f);
    
    // render item in front
    ItemStack item = barrel.getItem();
    if (!item.isEmpty())
    {
      poseStack.pushPose();
      
      Block block = Block.byItem(item.getItem());
  
      OffsetData offset = getOffset(block.getRegistryName());
      poseStack.translate(.5 + offset.x, .5 + offset.y, 1.025 + offset.z);
      poseStack.mulPose(Vector3f.YP.rotationDegrees(180));
      float scale = (block != Blocks.AIR) ? .7f : .8f;
      poseStack.scale(scale * offset.scale, scale * offset.scale, scale * offset.scale);
      ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
      itemRenderer.render(item, ItemTransforms.TransformType.FIXED, true,
        poseStack, buffer, combinedLight, combinedOverlay, itemRenderer.getModel(item, null, null, 1));
        
      poseStack.popPose();
    }
    
    poseStack.popPose();
  }
}
