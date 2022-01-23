package com.naxanria.infinitybarrels.command;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;


/*
  @author: Naxanria
*/
public class IBCommands
{
  public IBCommands(CommandDispatcher<CommandSourceStack> dispatcher)
  {
    dispatcher.register(commands(Commands.literal( "ib")));
    dispatcher.register(commands(Commands.literal("infinity_barrels")));
  }
  
  public static LiteralArgumentBuilder<CommandSourceStack> commands(LiteralArgumentBuilder<CommandSourceStack> literal)
  {
    return literal
      .then(IBGiveCommand.register());
  }
}
