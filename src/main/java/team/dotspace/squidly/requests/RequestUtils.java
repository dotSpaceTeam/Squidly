/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.command.HirezCommandType;
import team.dotspace.squidly.requests.command.RequestParameterType;

public class RequestUtils {

  public static APIResponse getPlayerId(String PlayerName, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getplayeridbyname)
        .addParameter(RequestParameterType.PLAYER_NAME, PlayerName)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), response.getBody());
  }

  public static APIResponse getPlayerStatus(String playerId, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getplayerstatus)
        .addParameter(RequestParameterType.PLAYER_ID, playerId)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), response.getBody());
  }

  public static APIResponse getMatchPlayerDetails(String matchid, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getmatchplayerdetails)
        .addParameter(RequestParameterType.MATCH_ID, matchid)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), response.getBody());
  }
}
