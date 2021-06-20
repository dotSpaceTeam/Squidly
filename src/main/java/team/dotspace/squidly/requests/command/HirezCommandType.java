/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.command;

import team.dotspace.squidly.requests.codes.HirezEndpoint;

import static team.dotspace.squidly.requests.codes.HirezEndpoint.*;
import static team.dotspace.squidly.requests.command.CommandParameterType.*;

public enum HirezCommandType {

  //APIs - Connectivity, Development, & System Status
  ping(ANY),
  createsession(ANY, DEVELOPER_ID, SIGNATURE, TIMESTAMP),
  testsession(ANY, DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  getdataused(ANY, DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  gethirezserverstatus(ANY, DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  getpatchinfo(ANY, DEVELOPER_ID, SIGNATURE, SESSION, TIMESTAMP),
  ;


  HirezEndpoint endpoint;
  CommandParameterType[] requiredTypes;

  HirezCommandType(HirezEndpoint endpoint, CommandParameterType... requiredTypes) {
    this.endpoint = endpoint;
    this.requiredTypes = requiredTypes;
  }

  public HirezEndpoint getEndpoint() {
    return endpoint;
  }

  public CommandParameterType[] getRequiredTypes() {
    return requiredTypes;
  }

}
