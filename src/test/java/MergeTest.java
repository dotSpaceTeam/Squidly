/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MergeTest {


  public static void main(String[] args) {

    final var matchDataList = List.of(
        new matchData("111", 1),
        new matchData("333", 1),
        new matchData("222", 1),
        new matchData("444", 1),
        new matchData("666", 2),
        new matchData("777", 2),
        new matchData("555", 2),
        new matchData("888", 2));

    final var playerList = List.of(
        new playerData("111"),
        new playerData("222"),
        new playerData("333"),
        new playerData("444"),
        new playerData("666"),
        new playerData("777"));


    var map = new HashMap<String, playerData>();
    var result = new ArrayList<>();

    matchDataList
        .forEach(matchData ->
                            playerList
                                .stream()
                                .filter(playerData -> playerData.Id() == matchData.activeId())
                                .forEach(playerData -> map.put(matchData.activeId(), playerData)));

    matchDataList.forEach(matchData -> result.add(new data(matchData, map.getOrDefault(matchData.activeId(),new playerData("0")))));


    System.out.println(result.toString());


  }

}

record matchData(String activeId, int taskForce) {
}

record playerData(String Id) {
}

record data(matchData matchData, playerData playerData) {
}
