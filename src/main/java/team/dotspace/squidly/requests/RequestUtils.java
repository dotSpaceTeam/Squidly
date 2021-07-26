/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.interactions.InteractionHook;
import team.dotspace.squidly.SquidlyBot;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.codes.PlayerStatusCode;
import team.dotspace.squidly.requests.command.HirezCommandType;
import team.dotspace.squidly.requests.command.RequestParameterType;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static team.dotspace.squidly.requests.codes.ErrorCode.*;

public class RequestUtils {

  public static Optional<APIResponse> handleGeneral(final String playerName, final HirezEndpoint endpoint, final InteractionHook interactionHook) {
    var formattingFactory = new FormattingFactory(interactionHook);
    var response = new AtomicReference<APIResponse>();

    getPlayerId(playerName, endpoint)
        .then(playerIdRes -> {

          if (playerIdRes.jsonNode().getArray().length() > 1)
            ; //TODO: Give user the option to select one of the returned players.

          var privacyMode = ((JSONObject) playerIdRes.jsonNode().getArray().get(0)).getString("privacy_flag");
          var playerId = ((JSONObject) playerIdRes.jsonNode().getArray().get(0)).getString("player_id");

          if (privacyMode.equals("y")) {
            formattingFactory.error(PRIVACY);
            return;
          }

          getPlayerStatus(playerId, endpoint)
              .then(playerStatusRes -> {
                var playerStatusObj = ((JSONObject) playerStatusRes.jsonNode().getArray().get(0));

                var et = switch (PlayerStatusCode.getFromCode(playerStatusObj.getInt("status"))) {
                  case LOBBY, ONLINE -> {
                    formattingFactory.error(ONLINE);
                    yield ONLINE;
                  }
                  case SELECTION -> {
                    formattingFactory.error(SELECTING);
                    yield SELECTING;
                  }
                  case OFFLINE -> {
                    formattingFactory.error(OFFLINE);
                    yield OFFLINE;
                  }
                  default -> {
                    formattingFactory.error(PLAYER_NOT_FOUND);
                    yield PLAYER_NOT_FOUND;
                  }
                  case GAME -> SUCCESS;
                };
                if (et != SUCCESS) return;

                var queueId = playerStatusObj.getInt("match_queue_id");
                //Todo: Validate queue to be a non custom game.

                response.set(playerStatusRes);

              }).error(RequestUtils::error);


        }).error(res -> {
      RequestUtils.error(res);
      formattingFactory.error(PLAYER_NOT_FOUND);
    });


    return Optional.ofNullable(response.get());
  }

  private static APIResponse getPlayerId(String PlayerName, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getplayeridbyname)
        .addParameter(RequestParameterType.PLAYER_NAME, PlayerName)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response, HirezCommandType.getplayeridbyname, endpoint);
  }

  private static APIResponse getPlayerStatus(String playerId, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getplayerstatus)
        .addParameter(RequestParameterType.PLAYER_ID, playerId)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response, HirezCommandType.getplayerstatus, endpoint);
  }

  public static APIResponse getMatchPlayerDetails(String matchid, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getmatchplayerdetails)
        .addParameter(RequestParameterType.MATCH_ID, matchid)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response, HirezCommandType.getmatchplayerdetails, endpoint);
  }

  /**
   * Requests Data about one ore multiple players by their playerId.
   * Paladins only!
   *
   * @param playerIds a comma seperated string of up to 20 playerId's
   * @param endpoint  the requested endpoint
   * @return the APIResponse Object.
   */
  public static APIResponse getPlayerDetailsBatch(String playerIds, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getplayerbatch)
        .addParameter(
            RequestParameterType.PLAYER_ID_SET, playerIds)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), HirezCommandType.getplayerbatch, endpoint, response.getBody());
  }

  private static void error(APIResponse apiResponse) {
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
