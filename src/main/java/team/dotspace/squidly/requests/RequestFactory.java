/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.json.JSONObject;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayer;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerData;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerMatchData;
import team.dotspace.squidly.requests.util.RequestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class RequestFactory {

  public void requestMatch(String playerName, FormattingFactory formattingFactory) {

    RequestUtils.handleGeneral(playerName, formattingFactory)
        .ifPresent(responseData -> {
          var statusObject = (JSONObject) responseData.jsonNode().getArray().get(0);

          var dataList = this.getMatchPlayerData(statusObject);

          var playerids = dataList.stream()
              .map(PaladinsPlayerMatchData::playerId)
              .collect(Collectors.joining(","));


          var playerDataList = this.getPlayerData(playerids);
          var players = this.mergeMatchData(dataList, playerDataList);
          formattingFactory.displayMatch(players);


        });


  }

  public void requestProfile() {

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

  private @NotNull List<PaladinsPlayer> mergeMatchData(@NotNull List<PaladinsPlayerMatchData> matchDataList, @NotNull List<PaladinsPlayerData> playerDataList) {
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
