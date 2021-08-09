/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.data.smite;

public record SmitePlayerData() {

  /**
   * Empty Json Reponse used for private accounts
   */
  public static final String MOCK_PRIVATE = """
  {
  "ActivePlayerId": 00,
  "Avatar_URL": "",
  "Created_Datetime": "",
  "HoursPlayed": 0,
  "Id": 00,
  "Last_Login_Datetime": "",
  "Leaves": 0,
  "Level": 0,
  "Losses": 0,
  "MasteryLevel": 0,
  "MergedPlayers": null,
  "Name": "Private",
  "Personal_Status_Message": "",
  "Rank_Stat_Conquest": 0,
  "Rank_Stat_Conquest_Controller": 0,
  "Rank_Stat_Duel": 0,
  "Rank_Stat_Duel_Controller": 0,
  "Rank_Stat_Joust": 0,
  "Rank_Stat_Joust_Controller": 0,
  "RankedConquest": {
    "Leaves": 0,
    "Losses": 0,
    "Name": "League",
    "Points": 0,
    "PrevRank": 0,
    "Rank": 0,
    "Rank_Stat": 0,
    "Season": 0,
    "Tier": 0,
    "Trend": 0,
    "Wins": 0,
    "player_id": null,
    "ret_msg": null
  },
  "RankedConquestController": {
    "Leaves": 0,
    "Losses": 0,
    "Name": "League Controller",
    "Points": 0,
    "PrevRank": 0,
    "Rank": 0,
    "Rank_Stat": 0,
    "Season": 0,
    "Tier": 0,
    "Trend": 0,
    "Wins": 0,
    "player_id": null,
    "ret_msg": null
  },
  "RankedDuel": {
    "Leaves": 0,
    "Losses": 0,
    "Name": "Duel",
    "Points": 0,
    "PrevRank": 0,
    "Rank": 0,
    "Rank_Stat": 0,
    "Season": 6,
    "Tier": 0,
    "Trend": 0,
    "Wins": 0,
    "player_id": null,
    "ret_msg": null
  },
  "RankedDuelController": {
    "Leaves": 0,
    "Losses": 0,
    "Name": "Duel Controller",
    "Points": 0,
    "PrevRank": 0,
    "Rank": 0,
    "Rank_Stat": 0,
    "Season": 0,
    "Tier": 0,
    "Trend": 0,
    "Wins": 0,
    "player_id": null,
    "ret_msg": null
  },
  "RankedJoust": {
    "Leaves": 0,
    "Losses": 0,
    "Name": "Joust",
    "Points": 0,
    "PrevRank": 0,
    "Rank": 0,
    "Rank_Stat": 0,
    "Season": 0,
    "Tier": 0,
    "Trend": 0,
    "Wins": 0,
    "player_id": null,
    "ret_msg": null
  },
  "RankedJoustController": {
    "Leaves": 0,
    "Losses": 0,
    "Name": "Joust Controller",
    "Points": 0,
    "PrevRank": 0,
    "Rank": 0,
    "Rank_Stat": 0,
    "Season": 0,
    "Tier": 0,
    "Trend": 0,
    "Wins": 0,
    "player_id": null,
    "ret_msg": null
  },
  "Region": "",
  "TeamId": 0,
  "Team_Name": "",
  "Tier_Conquest": 0,
  "Tier_Duel": 0,
  "Tier_Joust": 0,
  "Total_Achievements": 0,
  "Total_Worshippers": 0,
  "Wins": 0,
  "hz_gamer_tag": null,
  "hz_player_name": "Private",
  "ret_msg": null
}
""";
}
