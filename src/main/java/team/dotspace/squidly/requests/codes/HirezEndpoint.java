/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.codes;

public enum HirezEndpoint {

  PALADINS("https://api.paladins.com/paladinsapi.svc/");

  String url;

  HirezEndpoint(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }
}
