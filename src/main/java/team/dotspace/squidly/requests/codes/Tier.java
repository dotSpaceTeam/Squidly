/*
 * Copyright (c) 2021), dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.codes;

public enum Tier {

  Qualifying(),
  Bronze_V(),
  Bronze_IV(),
  Bronze_III(),
  Bronze_II(),
  Bronze_I(),
  Silver_V(),
  Silver_IV(),
  Silver_III(),
  Silver_II(),
  Silver_I(),
  Gold_V(),
  Gold_IV(),
  Gold_III(),
  Gold_II(),
  Gold_I(),
  Platinum_V(),
  Platinum_IV(),
  Platinum_III(),
  Platinum_II(),
  Platinum_I(),
  Diamond_V(),
  Diamond_IV(),
  Diamond_III(),
  Diamond_II(),
  Diamond_I(),
  Master(),
  Grandmaster();

  private final int id;

  Tier() {
    this.id = ordinal();
  }

  public static Tier getRankFromId(int id) {
    for (Tier value : Tier.values()) {
      if (value.id() == id)
        return value;
    }
    return Qualifying;
  }

  @Override
  public String toString() {
    return this.name().replace("_", " ");
  }

  public int id() {
    return ordinal();
  }
}
