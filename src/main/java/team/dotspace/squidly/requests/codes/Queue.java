/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.requests.codes;

import java.util.List;

public enum Queue {
  UNKOWN(0),


  LIVE_Siege(424),
  LIVE_Team_Deathmatch(469),
  LIVE_Onslaught(452),
  LIVE_Competitive_KBM(486),
  LIVE_Team_Deathmatch_Practice(470),
  LIVE_Competitive_Gamepad(428),
  LIVE_Practice_Siege(425),
  LIVE_Onslaught_Practice(453);

  public static List<Integer> consideredQueues = List.of(
      /* Pala -->  */424, 425, 427, 428, 437, 444, 452, 453, 465, 469, 486
  );

  private final int id;

  Queue(int id) {
    this.id = id;
  }

  public static Queue getFromId(int id) {
    for (Queue value : Queue.values()) {
      if (value.id() == id)
        return value;
    }
    return UNKOWN;
  }

  public boolean isConsidered() {
    return consideredQueues.contains(id);
  }

  public int id() {
    return id;
  }


  @Override
  public String toString() {
    return name().replaceAll("_", " ");
  }
}
