/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.codes;

public enum PlayerStatusCode {

  OFFLINE(0),
  LOBBY(1),
  SELECTION(2),
  GAME(3),
  ONLINE(4),
  UNKNOWN(5);

  PlayerStatusCode(int code) {
  }
}
