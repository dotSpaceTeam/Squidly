/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests;

import kong.unirest.json.JSONObject;
import team.dotspace.squidly.requests.codes.HirezEndpoint;
import team.dotspace.squidly.requests.codes.PlayerStatusCode;
import team.dotspace.squidly.requests.command.HirezCommandType;
import team.dotspace.squidly.requests.command.RequestParameterType;
import team.dotspace.squidly.requests.data.ResponseData;

import static team.dotspace.squidly.requests.codes.ErrorCode.*;

public class RequestUtils {

  /**
   * Chains multiple API Calls togehther to gather most necessary information about a player.
   * There will still be an response if one ore more request fail or when the operations
   * could not gather the infomation needed. If no request is failing the last response is returned.
   * Otherwise the first failing one is returned.
   *
   * @param playerName the name of the player which should be searched for
   * @param endpoint   the hirez endpoint; in this case <code>SMITE</code> or <code>PALADINS</code>
   * @return an ResponseData Object
   */
  public static ResponseData handleDefaultInfo(String playerName, HirezEndpoint endpoint) {
    APIResponse lastApiResponse;
    /*
    Retrieves the player id for the specified playerName.
    If the request fails the response will be forwarded.
    If it's successful it will continue processing.
    */
    lastApiResponse = RequestUtils.getPlayerId(playerName, endpoint);
    if (lastApiResponse.isFailure())
      return new ResponseData(PLAYER_NOT_FOUND, HirezCommandType.getplayeridbyname, endpoint, lastApiResponse);


    /*
    Checks the JSONObject for the "privacy_flag" attribute.
    If the flag is 'y' we'll stop processing because any other information
    about this player can not be fetched. Otherwise continute processing.
    */
    var playerObject = (JSONObject) lastApiResponse.jsonNode().getArray().get(0);
    if (playerObject.getString("privacy_flag").equals("y"))
      return new ResponseData(PRIVACY, HirezCommandType.getplayeridbyname, endpoint, lastApiResponse);

    /*
    Uses the player_id gathered from the last request to request information about
    the players current status. If the API Call fails stop processing.
     */
    lastApiResponse = RequestUtils.getPlayerStatus(playerObject.getString("player_id"), endpoint);
    if (lastApiResponse.isFailure())
      return new ResponseData(UNKNOWN, HirezCommandType.getplayerstatus, endpoint, lastApiResponse);

    /*
    Checks wether the player is currenlty in status GAME. If not, stop processing
    because there will be no data about his current game. Otherwise continue processing.
     */
    var statusObject = (JSONObject) lastApiResponse.jsonNode().getArray().get(0);
    return switch (PlayerStatusCode.values()[statusObject.getInt("status")]) {
      case OFFLINE -> new ResponseData(OFFLINE, HirezCommandType.getplayerstatus, endpoint, lastApiResponse);
      case LOBBY, ONLINE -> new ResponseData(ONLINE, HirezCommandType.getplayerstatus, endpoint, lastApiResponse);
      case SELECTION -> new ResponseData(SELECTING, HirezCommandType.getplayerstatus, endpoint, lastApiResponse);
      default -> new ResponseData(UNKNOWN, HirezCommandType.getplayerstatus, endpoint, lastApiResponse);
      case GAME -> new ResponseData(SUCCESS, HirezCommandType.getplayerstatus, endpoint, lastApiResponse);
    };
  }

  private static APIResponse getPlayerId(String PlayerName, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getplayeridbyname)
        .addParameter(RequestParameterType.PLAYER_NAME, PlayerName)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), HirezCommandType.getplayeridbyname, endpoint, response.getBody());
  }

  private static APIResponse getPlayerStatus(String playerId, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getplayerstatus)
        .addParameter(RequestParameterType.PLAYER_ID, playerId)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), HirezCommandType.getplayerstatus, endpoint, response.getBody());
  }

  public static APIResponse getMatchPlayerDetails(String matchid, HirezEndpoint endpoint) {
    var response = new APIRequestBuilder(HirezCommandType.getmatchplayerdetails)
        .addParameter(RequestParameterType.MATCH_ID, matchid)
        .changeEndpoint(endpoint)
        .build();

    return new APIResponse(response.getStatus(), response.getStatusText(), HirezCommandType.getmatchplayerdetails, endpoint, response.getBody());
  }
}
