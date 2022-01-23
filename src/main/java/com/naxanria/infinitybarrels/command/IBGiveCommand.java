package com.naxanria.infinitybarrels.command;


import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.naxanria.infinitybarrels.entity.BarrelBlockEntity;
import com.naxanria.infinitybarrels.registry.ModBlocks;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;


/*
  @author: Naxanria
*/
public class IBGiveCommand
{
  public static ArgumentBuilder<CommandSourceStack, ?> register()
  {
    return Commands.literal("give")
      .requires(cs -> cs.hasPermission(2))
      .then(Commands.argument("item", ItemArgument.item())
        .executes(ctx -> give(ctx.getSource(), ItemArgument.getItem(ctx, "item")))
      );
  }
  
  private static int give(CommandSourceStack sourceStack, ItemInput item)
  {
    try
    {
      ServerPlayer player = sourceStack.getPlayerOrException();
      
      ItemStack stack = new ItemStack(ModBlocks.BARREL.get());
      stack.getOrCreateTag().put("item", item.createItemStack(1, false).serializeNBT());
      
      if (!player.getInventory().add(stack))
      {
        BarrelBlockEntity.spawnItemStack(sourceStack.getLevel(), player.getOnPos().above(), stack);
      }
    }
    catch (CommandSyntaxException e)
    {
      e.printStackTrace();
    }
  
    return 1;
  }
}
