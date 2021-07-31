/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.data.paladins;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.processing.Generated;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("jsonschema2pojo")
public record PaladinsPlayerMatchData(
    @JsonProperty("Account_Champions_Played") int accountChampionsPlayed,
    @JsonProperty("Account_Level") int accountLevel,
    @JsonProperty("ChampionId") int championId,
    @JsonProperty("ChampionLevel") int championLevel,
    @JsonProperty("ChampionName") String championName,
    @JsonProperty("Mastery_Level") int masteryLevel,
    @JsonProperty("Match") int match,
    @JsonProperty("Queue") String queue,
    @JsonProperty("Skin") String skin,
    @JsonProperty("SkinId") int skinId,
    @JsonProperty("Tier") int tier,
    @JsonProperty("mapGame") String mapGame,
    @JsonProperty("playerCreated") String playerCreated,
    @JsonProperty("playerId") String playerId,
    @JsonProperty("playerName") String playerName,
    @JsonProperty("playerPortalId") String playerPortalId,
    @JsonProperty("playerPortalUserId") String playerPortalUserId,
    @JsonProperty("playerRegion") String playerRegion,
    @JsonProperty("ret_msg") String retMsg,
    @JsonProperty("taskForce") int taskForce,
    @JsonProperty("tierLosses") int tierLosses,
    @JsonProperty("tierWins") int tierWins
) {
}
