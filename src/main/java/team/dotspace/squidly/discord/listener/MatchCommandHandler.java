/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord.listener;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.discord.FormattingFactory;
import team.dotspace.squidly.requests.RequestFactory;

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
