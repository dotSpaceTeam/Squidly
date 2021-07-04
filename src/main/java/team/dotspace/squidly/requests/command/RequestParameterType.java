/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.command;

public enum RequestParameterType {

  DEVELOPER_ID("developerId"),
  SIGNATURE("signature"),
  TIMESTAMP("timestamp"),
  SESSION("session"),
  LANGUAGE_CODE("languageCode"),

  PLAYER("player"), //PlayerName or playerId
  PLAYER_ID("playerId"),
  PLAYER_ID_SET("playerId"),
  PLAYER_NAME("playerName"),
  GAMER_TAG("gamerTag"),
  SEARCH_PLAYER("searchPlayer"), //name or gamer_tag of player for whom to search

  CHAMPION_ID("championId"),
  GOD_ID("godId"),

  QUEUE("queue"),
  SEASON("season"),
  TIER("tier"),
  MATCH_ID("match_id"), //The id of the match. Can be obtained from /getmatchHistory, /gettopmatches & /getmatchidsbyqueue

  PORTAL_ID("portalId"), //1:Hi-Rez, 5:Steam, 9:PS4, 10:Xbox, 22:Switch, 25:Discord, 28:Epic
  PORTAL_USER_ID("portalUserId"), //The (usually) 3rd-Party identifier for a Portal.  Examples:  Steam ID, PS4 GamerTag, Xbox GamerTag, Switch GamerTag.

  CLAN_ID("clanid"), //id of the clan. Can be obtained from searchteams
  SEARCH_TEAM("searchTeam"); //name of clan for whom to search

  String s;
  RequestParameterType(String s) {
    this.s = s;
  }

  public String parameterName() {
    return s;
  }
}
