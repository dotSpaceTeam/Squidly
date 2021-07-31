/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.discord.listener.MatchCommandHandler;

import java.util.ArrayList;
import java.util.Collection;

public class SlashCommandManager extends ListenerAdapter {

  private final Collection<CommandData> commandDataList = new ArrayList<>();

  {
    commandDataList.add(
        new CommandData("match", "Displays important information about a players current match")
            .addOptions(
                new OptionData(OptionType.STRING, "game", "The game which shall be searched for the current game")
                    .addChoice("Paladins", "PALADINS")
                    .addChoice("Smite", "SMITE")
                    .setRequired(true),
                new OptionData(OptionType.STRING, "player", "Displays information about a players current match")
                    .setRequired(true)
            )
    );

    commandDataList.add(
        new CommandData("mymatch", "Displays information about the current match of your saved player")
            .addOptions(
                new OptionData(OptionType.STRING, "game", "The game which shall be searched for the current game")
                    .addChoice("Paladins", "PALADINS")
                    .addChoice("Smite", "SMITE")
                    .setRequired(false)
            )
    );
  }

  public Collection<CommandData> getCommands() {
    return commandDataList;
  }


  @Override
  public void onSlashCommand(@NotNull SlashCommandEvent event) {
    switch (event.getName()) {
      case "match" -> new MatchCommandHandler().handleCommand(event);
      default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
    }
  }
}
