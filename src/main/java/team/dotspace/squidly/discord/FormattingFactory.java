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

  private final EmbedBuilder embedBuilder;
  private final InteractionHook interactionHook;

  public FormattingFactory(InteractionHook interactionHook) {
    this.embedBuilder = new EmbedBuilder();
    this.interactionHook = interactionHook;
  }

  public void display(List<PaladinsPlayer> playerList) {
    var queue = Queue.getFromId(Integer.parseInt(playerList.get(0).matchData().queue()));
    this.embedBuilder
        .setTitle(queue.toString())
        .addField("Team 1", "", false);

    int lastTeam = playerList.get(0).matchData().taskForce();
    for (PaladinsPlayer player : playerList) {
      var playerData = player.playerData();
      var matchData = player.matchData();

      if (lastTeam != matchData.taskForce()) {
        this.embedBuilder
            .addBlankField(false)
            .addField("Team 2", "", false);
      }
      lastTeam = matchData.taskForce();

      this.embedBuilder
          .addField(
              matchData.playerName() + " (Lvl. " + matchData.accountLevel() + ")",
              """
                  ```excel
                  %s(%s)
                  ||%s||
                                    
                  %s
                  %sh played
                  %s/%s Champions played
                  ```
                  """
                  .formatted(
                      matchData.championName(),
                      matchData.championLevel(),
                      playerData.title(),
                      Tier.getRankFromId(playerData.tierRankedKBM()).toString(),
                      playerData.hoursPlayed(),
                      matchData.accountChampionsPlayed(),
                      50 //TODO get all champion count
                  ),
              true);
    }

    this.interactionHook.editOriginalEmbeds(embedBuilder.build()).queue();
  }

  public void error(ErrorCode errorCode) {
    this.embedBuilder
        .setColor(Color.RED)
        .setTitle("Failure!")
        .getDescriptionBuilder()
        .append("An issue occured fulfilling your command")
        .append(" ")
        .append(errorCode.name());

    this.interactionHook.editOriginalEmbeds(this.embedBuilder.build()).queue();
  }
}
