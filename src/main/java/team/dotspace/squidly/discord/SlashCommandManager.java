/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

public class SlashCommandManager extends ListenerAdapter {

  private final Collection<CommandData> commandDataList = new ArrayList<>();

  {
    commandDataList.add(
        new CommandData("match", "Displays important information about a players current match")
            .addSubcommands(
                new SubcommandData("pala", "Displays information about a players current Paladins match")
                    .addOption(OptionType.STRING, "player", "The name of the player you want the game to be looked up", true),
                new SubcommandData("smite", "Displays important information about a players current Smite match")
                    .addOption(OptionType.STRING, "player", "The name of the player you want the game to be looked up", true)
            )
    );

    commandDataList.add(
        new CommandData("mymatch", "Displays information about the current match of your saved player")
            .addSubcommands(
                new SubcommandData("pala", "Displays information about your current Paladins match"),
                new SubcommandData("smite", "Displays important information about your current Smite match")
            )
    );
  }

  public Collection<CommandData> getCommands() {
    return commandDataList;
  }


  @Override
  public void onSlashCommand(@NotNull SlashCommandEvent event) {
    switch (event.getName()) {
      default ->
          event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
    }
  }
}
