/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.InteractionHook;
import team.dotspace.squidly.requests.codes.ErrorCode;

import java.awt.*;

public class FormattingFactory {

  private final EmbedBuilder embedBuilder;
  private final InteractionHook interactionHook;

  public FormattingFactory(InteractionHook interactionHook) {
    this.embedBuilder = new EmbedBuilder();
    this.interactionHook = interactionHook;
  }

  public void error(ErrorCode errorCode) {
    this.embedBuilder
        .setColor(Color.RED)
        .setTitle("Failure!")
        .getDescriptionBuilder()
        .append("An issue occured fulfilling your command")
        .append(" ")
        .append(errorCode.name());

    this.interactionHook.editOriginalEmbeds(this.embedBuilder.build()).queue();
  }
}
