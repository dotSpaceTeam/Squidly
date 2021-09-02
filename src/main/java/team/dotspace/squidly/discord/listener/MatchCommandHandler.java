/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.RequestUtils;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayer;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerData;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerMatchData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MatchCommandHandler {

  private FormattingFactory formattingFactory;

  public void handleCommand(@NotNull SlashCommandEvent event) {
    // Guaranteed to not be Null because option 'player' is required!
    final var playerName = event.getOption("player").getAsString();

    event.deferReply().queue(interactionHook -> {

      this.formattingFactory = new FormattingFactory(interactionHook);

      RequestUtils.handleGeneral(playerName, this.formattingFactory)
          .ifPresent(responseData -> {
            var statusObject = (JSONObject) responseData.jsonNode().getArray().get(0);

            var dataList = this.getMatchPlayerData(statusObject);

            var playerids = dataList.stream()
                .map(PaladinsPlayerMatchData::playerId)
                .collect(Collectors.joining(","));


            var playerDataList = this.getPlayerData(playerids);
            var players = this.mergeIntoPaladinsPlayer(dataList, playerDataList);
            this.formattingFactory.displayMatch(players);


          });
    });
  }


  private @NotNull List<PaladinsPlayerMatchData> getMatchPlayerData(@NotNull JSONObject statusObject) {
    var response = RequestUtils.getMatchPlayerDetails(statusObject.getString("Match"));
    List<PaladinsPlayerMatchData> dataList = new ArrayList<>();

    if (response.isSuccess()) {
      try {
        var ojectMapper = new ObjectMapper();

        for (Object o : response.jsonNode().getArray())
          dataList.add(ojectMapper.readValue(o.toString(), PaladinsPlayerMatchData.class));

      } catch (JsonProcessingException exception) {
        exception.printStackTrace();
      }
    }

    return dataList;
  }


  private @NotNull List<PaladinsPlayerData> getPlayerData(@NotNull String playerIds) {
    var response = RequestUtils.getPlayerDetailsBatch(playerIds);
    List<PaladinsPlayerData> dataList = new ArrayList<>();

    if (response.isSuccess()) {
      try {
        var ojectMapper = new ObjectMapper();

        for (Object o : response.jsonNode().getArray())
          dataList.add(ojectMapper.readValue(o.toString(), PaladinsPlayerData.class));

      } catch (JsonProcessingException exception) {
        exception.printStackTrace();
      }
    }

    return dataList;
  }

  private @NotNull List<PaladinsPlayer> mergeIntoPaladinsPlayer(@NotNull List<PaladinsPlayerMatchData> matchDataList, @NotNull List<PaladinsPlayerData> playerDataList) {
    var map = new HashMap<String, PaladinsPlayerData>();
    var result = new ArrayList<PaladinsPlayer>();

    //Fill map with ids and corresponding playerData
    matchDataList
        .forEach(matchData ->
                     playerDataList.stream()
                         .filter(playerData -> String.valueOf(playerData.activePlayerId()).equals(matchData.playerId()))
                         .findFirst().ifPresent(playerData -> map.put(matchData.playerId(), playerData)));

    //Lookup playerData from map and merge into paladinsPlayer object
    matchDataList.forEach(matchData -> result.add(new PaladinsPlayer(
        map.getOrDefault(matchData.playerId(), PaladinsPlayerData.MOCK_PRIVATE),
        matchData)));

    return result;
  }

}
