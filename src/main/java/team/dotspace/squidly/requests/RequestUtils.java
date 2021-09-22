/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.json.JSONObject;
import net.dv8tion.jda.internal.utils.tuple.MutableTriple;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.codes.PlayerStatusCode;
import team.dotspace.squidly.requests.codes.Queue;
import team.dotspace.squidly.requests.command.HirezCommandType;
import team.dotspace.squidly.requests.command.RequestParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static team.dotspace.squidly.requests.codes.ErrorCode.*;

public class RequestUtils {

  public static Optional<APIResponse> handleGeneral(final String playerName, final FormattingFactory formattingFactory) {
    var response = new AtomicReference<APIResponse>();

    getPlayer(playerName)
        .then(playerRes -> {

          var length = playerRes.jsonNode().getArray().length();
          if (length == 0) {
            formattingFactory.withErrorCode(playerName, PLAYER_NOT_FOUND);
            return;
          } else if (length > 1) {
            var players = selectFirstChoices(playerRes, 3);
            formattingFactory.displayPlayerOptions(players);
            return;
          }

          var privacyMode = ((JSONObject) playerRes.jsonNode().getArray().get(0)).getString("privacy_flag");
          var playerId = ((JSONObject) playerRes.jsonNode().getArray().get(0)).getString("player_id");


          if (playerId == null) {
            formattingFactory.withErrorCode(playerName, PLAYER_NOT_FOUND);
            return;
          }

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
                    formattingFactory.withErrorCode(playerName, UNKNOWN);
                    yield UNKNOWN;
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


        }).error(res -> formattingFactory.withErrorCode(playerName, UNKNOWN));


    return Optional.ofNullable(response.get());
  }

  private static APIResponse getPlayer(String PlayerName) {
    var response = new APIRequestBuilder(HirezCommandType.getplayeridbyname)
        .addParameter(RequestParameterType.PLAYER_NAME, PlayerName)
        .build();

    return new APIResponse(response, HirezCommandType.getplayeridbyname);
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

    return new APIResponse(response, HirezCommandType.getplayerbatch);
  }

  private static Set<MutableTriple<Long, String, String>> selectFirstChoices(APIResponse apiResponse, int choices) {
    List<MutableTriple<Long, String, String>> result = new ArrayList<>(choices);
    var dataArray = apiResponse.jsonNode().getArray();

    for (int i = 0; i < choices; i++) {
      if (!(dataArray.length() <= i + 1)) break;
      var object = dataArray.getJSONObject(i);
      result.add(MutableTriple.of(object.getLong("player_id"), object.getString("Name"), object.getString("portal")));
    }

    return Set.copyOf(result);
  }

}
