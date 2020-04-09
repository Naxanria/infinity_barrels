package com.naxanria.infinitybarrels.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.naxanria.infinitybarrels.InfinityBarrels;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

/*
  @author: Naxanria
*/
public class IBCommands
{
  public IBCommands(CommandDispatcher<CommandSource> dispatcher)
  {
    InfinityBarrels.LOGGER.info("Commands here....");
    dispatcher.register(commands(Commands.literal("ib")));
    dispatcher.register(commands(Commands.literal("infinity_barrels")));
  }
  
  public static LiteralArgumentBuilder<CommandSource> commands(LiteralArgumentBuilder<CommandSource> literal)
  {
    return literal
      .then(IBGiveCommand.register());
  }
}
