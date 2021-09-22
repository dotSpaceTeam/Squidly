/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.codes;

import java.util.Locale;

public enum Portal {
  UNKNOWN(0),
  HIREZ(1),
  STEAM(5),
  EPIC(28),
  PSN(9),
  XBOX(10),
  NINTENDO(22),
  DISCORD(25),

  MIXER(14),
  GOOGLE(13);

  private final int id;

  Portal(int id) {
    this.id = id;
  }

  public static Portal getById(int id) {
    for (Portal p : Portal.values()) {
      if (p.id == id)
        return p;
    }
    return UNKNOWN;
  }

  public int id() {
    return this.id;
  }

  @Override
  public String toString() {
    return name();
  }
}


