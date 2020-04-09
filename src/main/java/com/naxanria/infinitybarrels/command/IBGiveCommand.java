package com.naxanria.infinitybarrels.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.naxanria.infinitybarrels.init.ModBlocks;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;

/*
  @author: Naxanria
*/
public class IBGiveCommand
{
  public static ArgumentBuilder<CommandSource, ?> register()
  {
    return Commands.literal("give")
      .requires(cs -> cs.hasPermissionLevel(2))
      .then(Commands.argument("item", ItemArgument.item())
        .executes(ctx -> give(ctx.getSource(), ItemArgument.getItem(ctx, "item")))
      );
  }
  
  private static int give(CommandSource source, ItemInput item)
  {
    try
    {
      ServerPlayerEntity player = source.asPlayer();
      if (player == null)
      {
        return 0;
      }
  
      ItemStack stack = new ItemStack(ModBlocks.INFINITY_BARREL);
      stack.getOrCreateChildTag("BlockEntityTag").put("item", item.createStack(1, false).serializeNBT());
      
      if (!player.inventory.addItemStackToInventory(stack))
      {
        InventoryHelper.spawnItemStack(source.getWorld(), player.getPosX(), player.getPosY(), player.getPosZ(), stack);
      }
    }
    catch (CommandSyntaxException e)
    {
      e.printStackTrace();
    }
  
    return 1;
  }
}
