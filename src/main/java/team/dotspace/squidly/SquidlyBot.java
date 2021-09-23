/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.dotspace.squidly.discord.CommandManager;
import team.dotspace.squidly.discord.listener.PrivateChatListener;
import team.dotspace.squidly.requests.session.HirezSessionHandler;

public class SquidlyBot {

  private static SquidlyBot instance;
  final Logger logger = LoggerFactory.getLogger(SquidlyBot.class);
  private final JDA jda;
  private final HirezCredentialPair hirezCredentialPair;
  private HirezSessionHandler sessionHandler;
  private CommandManager commandManager;

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
    this.commandManager = new CommandManager();
    this.jda.getPresence().setPresence(Activity.of(Activity.ActivityType.DEFAULT,"in plat forever!"), true);
    this.jda.addEventListener(commandManager, new PrivateChatListener());
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

  public CommandManager getCommandManager() {
    return commandManager;
  }
}
