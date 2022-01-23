package com.naxanria.infinitybarrels.item;

import com.naxanria.infinitybarrels.InfinityBarrels;
import com.naxanria.infinitybarrels.entity.BarrelBlockEntity;
import com.naxanria.infinitybarrels.registry.ModBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;


/*
  @author: Naxanria
*/
public class BarrelWrenchItem extends Item
{
  public BarrelWrenchItem(Properties properties)
  {
    super(properties.stacksTo(1));
  }
  
  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag)
  {
    tooltip.add(new TranslatableComponent(InfinityBarrels.MODID + ".desc.barrel_wrench").withStyle(ChatFormatting.GREEN));
    
    super.appendHoverText(stack, level, tooltip, flag);
  }
  
  @Override
  public InteractionResult useOn(UseOnContext context)
  {
    BlockPos pos = context.getClickedPos();
    Level level = context.getLevel();
    Player player = context.getPlayer();
  
    Block block = level.getBlockState(pos).getBlock();
    if (block == ModBlocks.BARREL.get())
    {
      if (!level.isClientSide())
      {
        // get barrel with same item
        ItemStack barrel = new ItemStack(ModBlocks.BARREL.get());
        BarrelBlockEntity entity = (BarrelBlockEntity) level.getBlockEntity(pos);
        ItemStack item = entity.getItem();
        barrel.getOrCreateTag().put("item", item.serializeNBT());
  
        // attempt to put in inventory
        if (!player.getInventory().add(barrel))
        {
          player.displayClientMessage(new TranslatableComponent(InfinityBarrels.MODID + ".gui.failed_enter_inventory"), true);
        }
        else
        {
          level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
      }
      return InteractionResult.SUCCESS;
    }
  
    return InteractionResult.PASS;
  }
}
