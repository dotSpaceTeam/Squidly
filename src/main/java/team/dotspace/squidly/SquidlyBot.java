/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.dotspace.squidly.discord.SlashCommandManager;
import team.dotspace.squidly.requests.session.HirezSessionHandler;

import java.util.Arrays;

public class SquidlyBot {

  private static SquidlyBot instance;
  final Logger logger = LoggerFactory.getLogger(SquidlyBot.class);
  private final JDA jda;
  private final HirezCredentialPair hirezCredentialPair;
  private HirezSessionHandler sessionHandler;
  private SlashCommandManager slashCommandManager;

  public SquidlyBot(JDA jda, HirezCredentialPair credentialPair) {
    this.jda = jda;
    this.hirezCredentialPair = credentialPair;
    instance = this;
    this.initialize();
  }

  public static SquidlyBot getInstance() {
    return instance;
  }

  private void initialize() {
    this.sessionHandler = new HirezSessionHandler();
    this.slashCommandManager = new SlashCommandManager();
    this.jda.getPresence().setPresence(Activity.competing("Silver League"), true);

    var action = jda.updateCommands().addCommands(slashCommandManager.getCommands());
    action.queue(
        commands -> this.logger.info("Sucessfully updated the following commands: {}", Arrays.toString(commands.toArray())),
        throwable -> this.logger.error("Failure while updating the commands: {}", Arrays.toString(throwable.getSuppressed())));

    this.jda.addEventListener(slashCommandManager);
  }

  public Logger getLogger() {
    return logger;
  }

  public JDA getJda() {
    return jda;
  }

  public HirezCredentialPair getHirezCredentialPair() {
    return hirezCredentialPair;
  }

  public HirezSessionHandler getSessionHandler() {
    return sessionHandler;
  }
}
