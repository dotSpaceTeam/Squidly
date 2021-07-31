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
public record PaladinsPlayerData(

    @JsonProperty("ActivePlayerId") long activePlayerId,
    @JsonProperty("AvatarId") long avatarId,
    @JsonProperty("AvatarURL") String avatarURL,
    @JsonProperty("Created_Datetime") String createdDatetime,
    @JsonProperty("HoursPlayed") long hoursPlayed,
    @JsonProperty("Id") long id,
    @JsonProperty("Last_Login_Datetime") String lastLoginDatetime,
    @JsonProperty("Leaves") long leaves,
    @JsonProperty("Level") long level,
    @JsonProperty("LoadingFrame") String loadingFrame,
    @JsonProperty("Losses") long losses,
    @JsonProperty("MasteryLevel") long masteryLevel,
    @JsonProperty("MergedPlayers") Object mergedPlayers,
    @JsonProperty("MinutesPlayed") long minutesPlayed,
    @JsonProperty("Name") String name,
    @JsonProperty("Personal_Status_Message") String personalStatusMessage,
    @JsonProperty("Platform") String platform,
    @JsonProperty("RankedConquest") RankedContainer rankedConquest,
    @JsonProperty("RankedController") RankedContainer rankedController,
    @JsonProperty("RankedKBM") RankedContainer rankedKBM,
    @JsonProperty("Region") String region,
    @JsonProperty("TeamId") long teamId,
    @JsonProperty("Team_Name") String teamName,
    @JsonProperty("Tier_Conquest") long tierConquest,
    @JsonProperty("Tier_RankedController") long tierRankedController,
    @JsonProperty("Tier_RankedKBM") long tierRankedKBM,
    @JsonProperty("Title") String title,
    @JsonProperty("Total_Achievements") long totalAchievements,
    @JsonProperty("Total_Worshippers") long totalWorshippers,
    @JsonProperty("Total_XP") long totalXP,
    @JsonProperty("Wins") long wins,
    @JsonProperty("hz_gamer_tag") Object hzGamerTag,
    @JsonProperty("hz_player_name") String hzPlayerName,
    @JsonProperty("ret_msg") String retMsg
) {

  @JsonInclude(JsonInclude.Include.NON_NULL)
  @Generated("jsonschema2pojo")
  private record RankedContainer(
      @JsonProperty("Leaves") long leaves,
      @JsonProperty("Losses") long losses,
      @JsonProperty("Name") String name,
      @JsonProperty("Points") long points,
      @JsonProperty("PrevRank") long prevRank,
      @JsonProperty("Rank") long rank,
      @JsonProperty("Season") long season,
      @JsonProperty("Tier") long tier,
      @JsonProperty("Trend") long trend,
      @JsonProperty("Wins") long wins,
      @JsonProperty("player_id") Object playerId,
      @JsonProperty("ret_msg") Object retMsg
  ) {
  }
}
