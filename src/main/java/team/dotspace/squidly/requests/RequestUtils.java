/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.json.JSONObject;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.codes.PlayerStatusCode;
import team.dotspace.squidly.requests.codes.Queue;
import team.dotspace.squidly.requests.command.HirezCommandType;
import team.dotspace.squidly.requests.command.RequestParameterType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static team.dotspace.squidly.requests.codes.ErrorCode.*;

public class RequestUtils {

  public static Optional<APIResponse> handleGeneral(final String playerName, final FormattingFactory formattingFactory) {
    var response = new AtomicReference<APIResponse>();

    searchPlayer(playerName)
        .then(playerIdRes -> {

          if (playerIdRes.jsonNode().getArray().length() > 1)
            ; //TODO: Give user the option to select one of the returned players.

          var privacyMode = ((JSONObject) playerIdRes.jsonNode().getArray().get(0)).getString("privacy_flag");
          var playerId = ((JSONObject) playerIdRes.jsonNode().getArray().get(0)).getString("player_id");

          if (privacyMode.equals("y")) {
            formattingFactory.withErrorCode(playerName, PRIVACY);
            return;
          }

          getPlayerStatus(playerId)
              .then(playerStatusRes -> {
                var playerStatusObj = ((JSONObject) playerStatusRes.jsonNode().getArray().get(0));

                var et = switch (PlayerStatusCode.getFromCode(playerStatusObj.getInt("status"))) {
                  case LOBBY, ONLINE -> {
                    formattingFactory.withErrorCode(playerName, ONLINE);
                    yield ONLINE;
                  }
                  case SELECTION -> {
                    formattingFactory.withErrorCode(playerName, SELECTING);
                    yield SELECTING;
                  }
                  case OFFLINE -> {
                    formattingFactory.withErrorCode(playerName, OFFLINE);
                    yield OFFLINE;
                  }
                  default -> {
                    formattingFactory.withErrorCode(playerName, PLAYER_NOT_FOUND);
                    yield PLAYER_NOT_FOUND;
                  }
                  case GAME -> SUCCESS;
                };
                if (et != SUCCESS) return;

                var queueId = playerStatusObj.getInt("match_queue_id");
                if (!Queue.consideredQueues.contains(queueId)) {
                  formattingFactory.withErrorCode(playerName, UNCONSIDERED);
                  return;
                }

                response.set(playerStatusRes);

              });


        }).error(res -> formattingFactory.withErrorCode(playerName, PLAYER_NOT_FOUND));


    return Optional.ofNullable(response.get());
  }

  private static APIResponse searchPlayer(String PlayerName) {
    var response = new APIRequestBuilder(HirezCommandType.searchplayers)
        .addParameter(RequestParameterType.SEARCH_PLAYER, PlayerName)
        .build();

    return new APIResponse(response, HirezCommandType.getplayeridbyname);
  }

  private static APIResponse getPlayerStatus(String playerId) {
    var response = new APIRequestBuilder(HirezCommandType.getplayerstatus)
        .addParameter(RequestParameterType.PLAYER_ID, playerId)
        .build();

    return new APIResponse(response, HirezCommandType.getplayerstatus);
  }

  public static APIResponse getMatchPlayerDetails(String matchid) {
    var response = new APIRequestBuilder(HirezCommandType.getmatchplayerdetails)
        .addParameter(RequestParameterType.MATCH_ID, matchid)
        .build();

    return new APIResponse(response, HirezCommandType.getmatchplayerdetails);
  }

  /**
   * Requests Data about one ore multiple players by their playerId.
   * Paladins only!
   *
   * @param playerIds a comma seperated string of up to 20 playerId's
   * @return the APIResponse Object.
   */
  public static APIResponse getPlayerDetailsBatch(String playerIds) {
    var response = new APIRequestBuilder(HirezCommandType.getplayerbatch)
        .addParameter(
            RequestParameterType.PLAYER_ID_SET, playerIds)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), HirezCommandType.getplayerbatch, response.getBody());
  }

}
