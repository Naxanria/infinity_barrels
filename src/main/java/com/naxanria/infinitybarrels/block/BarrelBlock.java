package com.naxanria.infinitybarrels.block;

import com.naxanria.infinitybarrels.entity.BarrelBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;


/*
  @author: Naxanria
*/
public class BarrelBlock extends Block implements EntityBlock
{
  public static final DirectionProperty FACING = BlockStateProperties.FACING;
  
  public BarrelBlock(Properties properties)
  {
    super(properties);
    registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.SOUTH));
  }
  
  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
  {
    builder.add(FACING);
  }
  
  @Override
  public InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_)
  {
    return InteractionResult.PASS;
  }
  
  @Override
  public void attack(BlockState state, Level level, BlockPos pos, Player player)
  {
    if (!level.isClientSide())
    {
      getBlockEntity(level, pos).ifPresent(b -> b.onClick(level, player));
    }
  }
  
  @Override
  public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag)
  {
    if (stack.hasTag())
    {
      CompoundTag tag = stack.getTag();
      
      if (tag.contains("BlockEntityTag"))
      {
        tag = tag.getCompound("BlockEntityTag");
      }
    
      if (tag.contains("item"))
      {
        ItemStack item = ItemStack.of(tag.getCompound("item"));
        Component displayName = item.getDisplayName();
        displayName.getStyle().applyFormat(ChatFormatting.AQUA);
        tooltip.add(displayName);
      }
    }
    
    super.appendHoverText(stack, level, tooltip, flag);
  }
  
//  @Override
//  @OnlyIn(Dist.CLIENT)
//  public float getAmbientOcclusionLightValue(BlockState state, IBlockReader worldIn, BlockPos pos)
//  {
//    return 1.0F;
//  }
  
  @Override
  public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
  {
    if (stack.hasTag())
    {
      CompoundTag tag = stack.getTag();
      if (tag.contains("BlockEntityTag"))
      {
        tag = tag.getCompound("BlockEntityTag");
      }
      
      if (tag.contains("item"))
      {
        ItemStack item = ItemStack.of(tag.getCompound("item"));
        getBlockEntity(level, pos).ifPresent(b -> b.setItem(item));
      }
    }
  }
  
  @Nullable
  @Override
  public BlockState getStateForPlacement(BlockPlaceContext context)
  {
    return defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
  }
  
  public Optional<BarrelBlockEntity> getBlockEntity(Level level, BlockPos pos)
  {
    BlockEntity blockEntity = level.getBlockEntity(pos);
    return Optional.ofNullable(blockEntity instanceof BarrelBlockEntity ? (BarrelBlockEntity) blockEntity : null);
  }
  
  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
  {
    return new BarrelBlockEntity(pos, state);
  }
}
