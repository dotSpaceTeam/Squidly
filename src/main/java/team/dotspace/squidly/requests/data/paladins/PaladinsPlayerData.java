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

  /**
   * Empty Json Reponse used for private accounts
   */
  public static final String MOCK_PRIVATE = """
        {
          "ActivePlayerId": 0,
          "Created_Datetime": "",
          "HoursPlayed": 0,
          "Id": 0,
          "Last_Login_Datetime": "",
          "Leaves": 0,
          "Level": 0,
          "Losses": 0,
          "MasteryLevel": 0,
          "MergedPlayers": null,
          "Name": "Private",
          "Personal_Status_Message": "",
          "Platform": "None",
          "RankedConquest": {
            "Leaves": 0,
            "Losses": 0,
            "Name": "Conquest",
            "Points": 0,
            "PrevRank": 0,
            "Rank": 0,
            "Season": 0,
            "Tier": 0,
            "Trend": 0,
            "Wins": 0,
            "player_id": null,
            "ret_msg": null
          },
          "RankedController": {
            "Leaves": 0,
            "Losses": 0,
            "Name": "Ranked Controller",
            "Points": 0,
            "PrevRank": 0,
            "Rank": 0,
            "Season": 0,
            "Tier": 0,
            "Trend": 0,
            "Wins": 0,
            "player_id": null,
            "ret_msg": null
          },
          "RankedKBM": {
            "Leaves": 0,
            "Losses": 0,
            "Name": "Ranked KBM",
            "Points": 0,
            "PrevRank": 0,
            "Rank": 0,
            "Season": 0,
            "Tier": 0,
            "Trend": 0,
            "Wins": 0,
            "player_id": null,
            "ret_msg": null
          },
          "Region": "None",
          "TeamId": 0,
          "Team_Name": "",
          "Tier_Conquest": 0,
          "Tier_RankedController": 0,
          "Tier_RankedKBM": 0,
          "Total_Achievements": 0,
          "Total_Worshippers": 0,
          "Total_XP": 0,
          "Wins": 0,
          "hz_gamer_tag": null,
          "hz_player_name": "Private",
          "ret_msg": null
        }
        """;


}
