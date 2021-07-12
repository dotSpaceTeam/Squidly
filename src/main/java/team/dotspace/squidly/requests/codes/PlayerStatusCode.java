/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.codes;

public enum PlayerStatusCode {

  OFFLINE(),
  LOBBY(),
  SELECTION(),
  GAME(),
  ONLINE(),
  UNKNOWN();

  private final int code;
  PlayerStatusCode() {
    this.code = ordinal();
  }
}
