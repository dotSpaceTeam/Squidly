/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord;

import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.discord.listener.MatchCommandHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class CommandManager extends ListenerAdapter {

  private final Collection<CommandData> commandDataList = new ArrayList<>();
  private final Cache<String, String> activeRequestCache;

  {
    final String PLAYER_DESC = "The player to retrieve the information from";
    commandDataList.add(
        new CommandData("match", "Displays important information about a players live match")
            .addOptions(
                new OptionData(OptionType.STRING, "player", PLAYER_DESC)
                    .setRequired(true)
            )
    );

    commandDataList.add(
        new CommandData("profile", "Displays generic information about a player")
            .addOptions(
                new OptionData(OptionType.STRING, "player", PLAYER_DESC)
                    .setRequired(true)
            )
    );
  }

  public CommandManager() {
    this.activeRequestCache = Cache2kBuilder.of(String.class, String.class)
        .expireAfterWrite(1, TimeUnit.MINUTES)
        .build();
  }

  public Collection<CommandData> getCommands() {
    return commandDataList;
  }

  public Cache<String, String> getActiveRequestCache() {
    return activeRequestCache;
  }

  @Override
  public void onButtonClick(@NotNull ButtonClickEvent event) {
    event.reply("U clicked on playedid: %s".formatted(event.getButton().getId())).queue();
    //TODO Handle Button Click
  }

  @Override
  public void onSlashCommand(@NotNull SlashCommandEvent event) {
    switch (event.getName()) {
      case "match" -> new MatchCommandHandler().handleCommand(event);
      default -> event.reply("I can't handle that command right now :(").setEphemeral(true).queue();
    }
  }
}
