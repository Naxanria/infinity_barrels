package com.naxanria.infinitybarrels.block;

import com.naxanria.infinitybarrels.tile.BarrelTile;
import com.wtbw.mods.lib.block.BaseTileBlock;
import com.wtbw.mods.lib.block.SixWayTileBlock;
import com.wtbw.mods.lib.util.TextComponentBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

/*
  @author: Naxanria
*/
public class BarrelBlock extends SixWayTileBlock<BarrelTile>
{
  public BarrelBlock(Properties properties)
  {
    super(properties, (world, state) -> new BarrelTile());
  }
  
  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult hit)
  {
    return ActionResultType.PASS;
  }
  
  @Override
  public void onBlockClicked(BlockState state, World world, BlockPos pos, PlayerEntity player)
  {
    if (!world.isRemote)
    {
      BarrelTile barrel = getTileEntity(world, pos);
      barrel.onClick(world, player);
    }
  }
  
  @Override
  public void addInformation(ItemStack stack, @Nullable IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn)
  {
    if (stack.hasTag())
    {
      CompoundNBT tag = stack.getTag();
      
      if (tag.contains("BlockEntityTag"))
      {
        tag = tag.getCompound("BlockEntityTag");
      }
    
      if (tag.contains("item"))
      {
        ItemStack item = ItemStack.read(tag.getCompound("item"));
        ITextComponent displayName = item.getDisplayName();
        Style style = displayName.getStyle();
        style.setColor(Color.func_240744_a_(TextFormatting.AQUA));
        tooltip.add(displayName);
      }
    }
    
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }
  
  @Override
  @OnlyIn(Dist.CLIENT)
  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
  {
    return 1.0F;
  }
  
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
  {
    if (stack.hasTag())
    {
      CompoundNBT tag = stack.getTag();
      if (tag.contains("BlockEntityTag"))
      {
        tag = tag.getCompound("BlockEntityTag");
      }
      
      if (tag.contains("item"))
      {
        ItemStack item = ItemStack.read(tag.getCompound("item"));
        BarrelTile tileEntity = getTileEntity(worldIn, pos);
        if (tileEntity != null)
        {
          tileEntity.setItem(item);
        }
      }
    }
    
  }
}
