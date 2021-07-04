/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly.discord.listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import team.dotspace.squidly.SquidlyBot;

import java.util.Arrays;

public class PrivateChatListener extends ListenerAdapter {

  final SquidlyBot instance = SquidlyBot.getInstance();
  final JDA jda = instance.getJda();

  @Override
  public void onPrivateMessageReceived(@NotNull PrivateMessageReceivedEvent event) {
    if (event.getAuthor().isBot()) return;
    if (!event.getAuthor().getId().equals("")) return;
    if (!event.getMessage().getContentRaw().equals("update!")) return;

    var commandList = instance.getSlashCommandManager().getCommands();
    var action = jda.updateCommands().addCommands(commandList);
    action.queue(
        commands -> event.getChannel().sendMessage("Sucessfully updated!").queue(),
        throwable -> {
          event.getChannel().sendMessage("Failure while updating the commands! Check console logs!").queue();
          instance.getLogger().error("Failure while updating the commands: {}", Arrays.toString(throwable.getSuppressed()));
        });
  }
}
