/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.RequestFactory;
import team.dotspace.squidly.requests.util.RequestUtils;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayer;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerData;
import team.dotspace.squidly.requests.data.paladins.PaladinsPlayerMatchData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MatchCommandHandler {

  public static void handleCommand(@NotNull SlashCommandEvent event) {
    // Guaranteed to not be Null because option 'player' is required!
    final var playerName = event.getOption("player").getAsString();

    event.deferReply().queue(
        interactionHook ->
            new RequestFactory().requestMatch(
                playerName,
                new FormattingFactory(interactionHook)
            ));
  }

}
