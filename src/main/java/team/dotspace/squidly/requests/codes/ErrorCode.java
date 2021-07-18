/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.codes;

public enum ErrorCode {

  SUCCESS(200),
  PLAYER_NOT_FOUND(901),
  PRIVACY(902),
  OFFLINE(910),
  ONLINE(911),
  SELECTIMG(912),
  UNKNOWN(999);

  private final int code;

  ErrorCode(int code) {
    this.code = code;
  }

  public int code() {
    return code;
  }
}
