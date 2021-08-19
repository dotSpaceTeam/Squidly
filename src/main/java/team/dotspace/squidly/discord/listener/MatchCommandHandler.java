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
import team.dotspace.squidly.SquidlyBot;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.APIResponse;
import team.dotspace.squidly.requests.RequestUtils;
import team.dotspace.squidly.requests.codes.ErrorCode;
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayer;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerData;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerMatchData;
import team.dotspace.squidly.requests.data.smite.SmitePlayerData;
import team.dotspace.squidly.requests.data.smite.SmitePlayerMatchData;

import java.util.*;
import java.util.stream.Collectors;

public class MatchCommandHandler {

  private HirezEndpoint endpoint;
  private FormattingFactory formattingFactory;

  public void handleCommand(@NotNull SlashCommandEvent event) {
    // Guaranteed to not be Null because option 'player' and 'game' are required!
    final var game = event.getOption("game").getAsString();
    final var playerName = event.getOption("player").getAsString();
    this.endpoint = HirezEndpoint.valueOf(game);

    event.deferReply().queue(interactionHook -> {

      this.formattingFactory = new FormattingFactory(interactionHook);

      RequestUtils.handleGeneral(playerName, this.endpoint, this.formattingFactory)
          .ifPresent(responseData -> {
            var statusObject = (JSONObject) responseData.jsonNode().getArray().get(0);

            this.getMatchPlayerData(statusObject)
                .ifPresent(matchDataArray -> {

                  if (matchDataArray instanceof PaladinsPlayerMatchData[] dataArray) {

                    var playerids = Arrays
                        .stream(dataArray)
                        .map(PaladinsPlayerMatchData::playerId)
                        .collect(Collectors.joining(","));

                    this.getPlayerData(playerids).ifPresent(playerDataList -> {
                      var players = this.mergeIntoPaladinsPlayer(dataArray, (PaladinsPlayerData[]) playerDataList);
                      this.formattingFactory.display(players);
                    });
                  }

                  if (matchDataArray instanceof SmitePlayerMatchData[] dataArray) {

                  }
                });
          });
    });
  }


  private @NotNull Optional<Object[]> getMatchPlayerData(@NotNull JSONObject statusObject) {
    var response = RequestUtils.getMatchPlayerDetails(statusObject.getString("Match"), this.endpoint);

    if (response.isSuccess()) {
      List<Object> dataList = new ArrayList<>();
      var ojectMapper = new ObjectMapper();

      for (Object o : response.jsonNode().getArray())
        try {
          switch (this.endpoint) {
            case PALADINS -> dataList.add(ojectMapper.readValue(o.toString(), PaladinsPlayerMatchData.class));
            case SMITE -> dataList.add(ojectMapper.readValue(o.toString(), SmitePlayerMatchData.class));
          }
        } catch (JsonProcessingException exception) {
          exception.printStackTrace();
          this.error(response);
          return Optional.empty();
        }

      return switch (this.endpoint) {
        default -> Optional.of(dataList.toArray(new PaladinsPlayerMatchData[1]));
        case SMITE -> Optional.of(dataList.toArray(new SmitePlayerMatchData[1]));
      };

    } else this.error(response);


    return Optional.empty();
  }

  //TODO Rework for smite
  private @NotNull Optional<Object[]> getPlayerData(@NotNull String playerIds) {
    var response = RequestUtils.getPlayerDetailsBatch(playerIds, endpoint);

    if (response.isSuccess()) {

      List<Object> dataList = new ArrayList<>();

      try {
        var ojectMapper = new ObjectMapper();

        for (Object o : response.jsonNode().getArray())
          switch (this.endpoint) {
            case PALADINS -> dataList.add(ojectMapper.readValue(o.toString(), PaladinsPlayerData.class));
            case SMITE -> this.formattingFactory.withErrorCode("", ErrorCode.WORK_IN_PROGRESS);
          }

      } catch (JsonProcessingException exception) {
        exception.printStackTrace();
        this.error(response);
        return Optional.empty();
      }

      return switch (this.endpoint) {
        default -> Optional.of(dataList.toArray(new PaladinsPlayerData[0]));
        case SMITE -> Optional.of(dataList.toArray(new SmitePlayerData[0]));
      };
    } else this.error(response);

    return Optional.empty();
  }

  private @NotNull List<PaladinsPlayer> mergeIntoPaladinsPlayer(@NotNull PaladinsPlayerMatchData[] matchDataArray, @NotNull PaladinsPlayerData[] playerDataArray) {
    var map = new HashMap<String, PaladinsPlayerData>();
    var result = new ArrayList<PaladinsPlayer>();

    //Fill map with ids and corresponding playerData
    Arrays.stream(matchDataArray)
        .forEachOrdered(matchData ->
                     Arrays.stream(playerDataArray)
                         .filter(playerData -> String.valueOf(playerData.activePlayerId()).equals(matchData.playerId()))
                         .findFirst().ifPresent(playerData -> map.put(matchData.playerId(), playerData)));

    //Lookup playerData from map and merge into paladinsPlayer object
    Arrays.stream(matchDataArray).forEachOrdered(matchData -> result.add(new PaladinsPlayer(
        map.getOrDefault(matchData.playerId(), PaladinsPlayerData.MOCK_PRIVATE),
        matchData)));

    return result;
  }

  private void error(@NotNull APIResponse apiResponse) {
    SquidlyBot.getInstance().getLogger().error(
        "Error {}: {} after executing {} on endpoint {}. json={}",
        apiResponse.statusCode(),
        apiResponse.statusText(),
        apiResponse.commandType().name(),
        apiResponse.endpoint().name(),
        apiResponse.jsonNode().toPrettyString()
    );
  }

}
