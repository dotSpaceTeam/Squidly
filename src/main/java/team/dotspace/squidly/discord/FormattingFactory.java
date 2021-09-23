/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.internal.utils.tuple.MutableTriple;
import team.dotspace.squidly.requests.codes.ErrorCode;
import team.dotspace.squidly.requests.codes.Queue;
import team.dotspace.squidly.requests.codes.Tier;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayer;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FormattingFactory {

  public static final String SQUIDLY_ICON = "https://raw.githubusercontent.com/luissilva1044894/hirez-api-docs/master/.assets/paladins/avatar/24355.png";
  private static final SimpleDateFormat displayFormat = new SimpleDateFormat("MMM yyyy");
  private static final SimpleDateFormat originalFormat = new SimpleDateFormat("M/d/yyyy h:mm:ss a");
  private final EmbedBuilder embedBuilder;
  private final InteractionHook interactionHook;

  public FormattingFactory(InteractionHook interactionHook) {
    this.interactionHook = interactionHook;
    this.embedBuilder = new EmbedBuilder()
        .setFooter("""
                       Squidly is powered by dotSpace Development.
                       Data provided by Hi-Rez. © 2021 Hi-Rez Studios, Inc. All rights reserved.
                       """, SQUIDLY_ICON);
  }

  public void displayProfile(PaladinsPlayer player) {
    //TODO
  }

  public void displayMatch(List<PaladinsPlayer> playerList) {
    var queue = Queue.getFromId(Integer.parseInt(playerList.get(0).matchData().queue()));

    this.embedBuilder
        .setTitle(queue.toString())
        .addField("Team 1", "", false);

    playerList
        .stream()
        .filter(paladinsPlayer -> paladinsPlayer.matchData().taskForce() == 1)
        .forEachOrdered(this::displayPalaPlayer);

    this.embedBuilder
        .addBlankField(false)
        .addField("Team 2", "", false);

    playerList
        .stream()
        .filter(paladinsPlayer -> paladinsPlayer.matchData().taskForce() == 2)
        .forEachOrdered(this::displayPalaPlayer);

    this.interactionHook.editOriginalEmbeds(embedBuilder.build()).queue();
    this.embedBuilder.clear();
  }

  private void displayPalaPlayer(PaladinsPlayer player) {
    var playerData = player.playerData();
    var matchData = player.matchData();

    var playername = playerData.hzGamerTag() == null ? playerData.hzPlayerName() : playerData.hzGamerTag();
    var gamesPlayed = playerData.wins() + playerData.losses();
    double winRatio = ((double) playerData.wins() / (gamesPlayed != 0 ? gamesPlayed : 1)) * 100;

    try {
      //TODO automate champion and achievement count
      this.embedBuilder
          .addField(
              playername + " (" + matchData.accountLevel() + ")",
              """
                  ```excel
                  %s(%d)
                  %s
                                  
                  %dw/%dl %s%%
                  %d quits
                           
                  %d/51 champions
                  %d/58 achievements
                               
                  %dh played
                  since %s
                  ```
                  """
                  .formatted(
                      matchData.championName(),
                      matchData.championLevel(),
                      Tier.getRankFromId(playerData.tierRankedKBM()).toString(),
                      playerData.wins(),
                      playerData.losses(),
                      (double) Math.round(winRatio * 10) / 10,
                      playerData.leaves(),
                      matchData.accountChampionsPlayed(),
                      playerData.totalAchievements(),
                      playerData.hoursPlayed(),
                      displayFormat.format(originalFormat.parse(playerData.createdDatetime()))
                  ),
              true);
    } catch (ParseException ignored) {
    }
  }

  public void withErrorCode(String playerName, ErrorCode errorCode) {
    this.embedBuilder
        .setColor(Color.RED)
        .setTitle(errorCode.code() + " " + errorCode);

    switch (errorCode) {
      case OFFLINE, ONLINE -> this.embedBuilder
          .setDescription("""
                              ```fix
                              Could not retrieve the requested data :c
                              The player %s is not in a match!
                              ```
                              """.formatted(playerName));
      case PRIVACY -> this.embedBuilder.setDescription("""
                                                           ```fix
                                                           Could not retrieve the requested data :c
                                                           The player %s has a private profile!
                                                           ```
                                                           """.formatted(playerName));
      case SELECTING -> this.embedBuilder
          .setDescription("""
                              ```fix
                              Could not retrieve the requested data :c
                              The player %s is selecting.. Try again in a moment!
                              ```
                              """.formatted(playerName));
      case UNCONSIDERED -> this.embedBuilder
          .setDescription("""
                              ```fix
                              Could not retrieve the requested data :c
                              The player %s is not playing a considered mode!
                              ```
                              """.formatted(playerName));
      case WORK_IN_PROGRESS -> this.embedBuilder
          .setDescription("""
                              ```fix
                              Sorry! This feature is still beeing worked on c:
                              ```
                              """);
      case PLAYER_NOT_FOUND -> this.embedBuilder
          .setDescription("""
                              ```fix
                              Could not retrieve the requested data :c
                              The player %s does not exist. Check for Typos!
                              ```
                              """.formatted(playerName));
      default -> this.embedBuilder
          .setDescription("""
                              Could not retrieve the requested data :c
                              There was an error while handling your request
                              """);
    }


    this.interactionHook.editOriginalEmbeds(this.embedBuilder.build()).queue();
    this.embedBuilder.clear();
  }

  public void displayPlayerOptions(Set<MutableTriple<Long, String, String>> players) {
    List<Button> buttons = new ArrayList<>();
    players.forEach(option -> buttons.add(
        Button.secondary(option.left.toString(), option.middle + " | " + option.right))
    );

    this.embedBuilder
        .setColor(Color.RED)
        .setTitle(ErrorCode.CHOOSE_PLAYER.code() + " " + ErrorCode.CHOOSE_PLAYER)
        .setDescription("""
                            ```fix
                            We found multiple players for your search.
                            Please specify which one to use :)
                            ```
                            """);

    this.interactionHook.editOriginalEmbeds(embedBuilder.build())
        .setActionRow(buttons)
        .queue();

  }
}
