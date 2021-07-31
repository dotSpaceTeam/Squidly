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
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayer;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerData;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerMatchData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
                .ifPresent(playerMatchDataList -> {

                  var playerids = playerMatchDataList
                      .stream()
                      .map(PaladinsPlayerMatchData::playerId)
                      .collect(Collectors.joining(","));

                  this.getPlayerData(playerids).ifPresent(playerDataList -> {

                    var players = this.mergeIntoPaladinsPlayer(playerMatchDataList, playerDataList);

                    this.formattingFactory.display(players);

                  });
                });
          });
    });
  }


  private @NotNull Optional<List<PaladinsPlayerMatchData>> getMatchPlayerData(@NotNull JSONObject statusObject) {
    var response = RequestUtils.getMatchPlayerDetails(statusObject.getString("Match"), this.endpoint);

    if (response.isSuccess()) {
      List<PaladinsPlayerMatchData> dataList = new ArrayList<>();
      try {
        var ojectMapper = new ObjectMapper();

        for (Object o : response.jsonNode().getArray())
          dataList.add(ojectMapper.readValue(o.toString(), PaladinsPlayerMatchData.class));

        dataList.sort(Comparator.comparingInt(PaladinsPlayerMatchData::taskForce));

      } catch (JsonProcessingException ignored) {
        this.error(response);
        return Optional.empty();
      }

      return Optional.of(dataList);

    } else this.error(response);


    return Optional.empty();
  }

  private @NotNull Optional<List<PaladinsPlayerData>> getPlayerData(@NotNull String playerIds) {
    var response = RequestUtils.getPlayerDetailsBatch(playerIds, endpoint);

    if (response.isSuccess()) {
      List<PaladinsPlayerData> dataList = new ArrayList<>();
      try {
        var ojectMapper = new ObjectMapper();
        for (Object o : response.jsonNode().getArray()) {
          dataList.add(ojectMapper.readValue(o.toString(), PaladinsPlayerData.class));
        }
      } catch (JsonProcessingException exception) {
        exception.printStackTrace();
        this.error(response);
        return Optional.empty();
      }

      return Optional.of(dataList);
    } else this.error(response);

    return Optional.empty();
  }

  private @NotNull List<PaladinsPlayer> mergeIntoPaladinsPlayer(@NotNull List<PaladinsPlayerMatchData> matchData, @NotNull List<PaladinsPlayerData> playerData) {
    List<PaladinsPlayer> playerList = new ArrayList<>();
    for (int i = 0; i < matchData.size()-1; i++) {
      playerList.add(
          new PaladinsPlayer(playerData.get(i), matchData.get(i))
      );
    }

    return playerList;
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
