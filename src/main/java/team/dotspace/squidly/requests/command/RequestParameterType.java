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

  PLAYER("player"),
  PLAYER_ID("playerId"),
  PLAYER_NAME("playerName"),
  GAMER_TAG("gamerTag"),
  SEARCH_PLAYER("searchPlayer"),

  CHAMPION_ID("championId"),
  GOD_ID("godId"),

  QUEUE("queue"),
  SEASON("season"),
  TIER("tier"),
  MATCH_ID("match_id"),

  PORTAL_ID("portalId"),
  PORTAL_USER_ID("portalUserId"),

  CLAN_ID("clanid"),
  SEARCH_TEAM("searchTeam");

  String s;
  RequestParameterType(String s) {
    this.s = s;
  }

  public String parameterName() {
    return s;
  }
}
