/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import team.dotspace.squidly.requests.codes.ErrorCode;
import team.dotspace.squidly.requests.codes.Queue;
import team.dotspace.squidly.requests.codes.Tier;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayer;

import java.awt.*;
import java.util.List;

public class FormattingFactory {

  public static final String SQUIDLY_ICON = "https://raw.githubusercontent.com/luissilva1044894/hirez-api-docs/master/.assets/paladins/avatar/24355.png";
  private final EmbedBuilder embedBuilder;
  private final InteractionHook interactionHook;

  public FormattingFactory(InteractionHook interactionHook) {
    this.interactionHook = interactionHook;
    this.embedBuilder = new EmbedBuilder()
        .setFooter("""
                       Squidly is powered by dotSpace Development.
                       Data provided by Hi-Rez. © 2021 Hi-Rez Studios, Inc. All rights reserved.
                       """, this.interactionHook.getInteraction().getUser().getAvatarUrl());
  }

  public void display(List<PaladinsPlayer> playerList) {
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

    this.embedBuilder
        .addField(
            matchData.playerName() + " (Lvl. " + matchData.accountLevel() + ")",
            """
                ```excel
                %s(%s)
                "%s"
                                  
                %s
                                
                %sw/%sl
                %s quits
                                
                %sh played
                %s/%s champions
                ```
                """
                .formatted(
                    matchData.championName(),
                    matchData.championLevel(),
                    playerData.title() == null ? "" : playerData.title(),
                    Tier.getRankFromId(playerData.tierRankedKBM()).toString(),
                    matchData.tierWins(),
                    matchData.tierLosses(),
                    playerData.leaves(),
                    playerData.hoursPlayed(),
                    matchData.accountChampionsPlayed(),
                    50 //TODO get all champion count
                ),
            true);
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
      default -> this.embedBuilder
          .setDescription("""
                              ```fix
                              Could not retrieve the requested data :c
                              Either %s does not exist or there was a internal problem
                              ```
                              """.formatted(playerName));
    }

    this.interactionHook.editOriginalEmbeds(this.embedBuilder.build()).queue();
    this.embedBuilder.clear();
  }
}
