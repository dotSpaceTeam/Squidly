/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.command;

import team.dotspace.squidly.requests.codes.HirezEndpoint;

import static team.dotspace.squidly.requests.command.RequestParameterType.*;

public enum HirezCommandType {

  //APIs - Connectivity, Development, & System Status
  ping(),
  createsession(DEVELOPER_ID, SIGNATURE, TIMESTAMP),
  testsession(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  getdataused(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  gethirezserverstatus(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  getpatchinfo(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  //APIs - Players & PlayerIds
  getplayer(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER),
  getplayerbatch(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_ID_SET),
  getplayeridbyname(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_NAME),
  //APIs - PlayerId Info
  getplayerstatus(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_ID),
  getgodranks(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_ID),
  getchampionranks(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_ID),
  getplayerachievements(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_ID),
  getmatchhistory(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_ID),
  getqueuestats(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, PLAYER_ID, QUEUE),
  searchplayers(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, SEARCH_PLAYER),
  //APIs - Match Info
  getdemodetails(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, MATCH_ID),
  getmatchdetails(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, MATCH_ID),
  getmatchplayerdetails(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP, MATCH_ID),
  gettopmatches(DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP);


  RequestParameterType[] requiredTypes;

  HirezCommandType(RequestParameterType... requiredTypes) {
    this.requiredTypes = requiredTypes;
  }

  public HirezEndpoint getEndpoint() {
    return HirezEndpoint.PALADINS;
  }

  public RequestParameterType[] getRequiredTypes() {
    return requiredTypes;
  }

}
