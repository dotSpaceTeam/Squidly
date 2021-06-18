/*
 * Copyright (c) 2021, dotSpace Development All rights reserved.
 * Read LICENSE.md for full license agreement.
 */

package team.dotspace.squidly;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import team.dotspace.squidly.validation.StartUpValidation;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.EnumSet;

public class Main {

  public static void main(String[] args) throws LoginException, IOException {

    if (StartUpValidation.validate(args)) {
      JDA jda = JDABuilder
          .createLight(StartUpValidation.getTOKEN(), EnumSet.noneOf(GatewayIntent.class))
          .build();

      new SquidlyBot(jda, StartUpValidation.getCREDENTIALS());
    }

  }

}
